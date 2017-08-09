package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Attachment;
import org.fwoxford.repository.AttachmentRepository;
import org.fwoxford.service.AttachmentService;
import org.fwoxford.service.dto.AttachmentDTO;
import org.fwoxford.service.mapper.AttachmentMapper;
import org.fwoxford.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AttachmentResource REST controller.
 *
 * @see AttachmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class AttachmentResourceIntTest {

    private static final Long DEFAULT_BUSINESS_ID = 1L;
    private static final Long UPDATED_BUSINESS_ID = 2L;

    private static final String DEFAULT_BUSINESS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_ID_1 = 1L;
    private static final Long UPDATED_FILE_ID_1 = 2L;

    private static final Long DEFAULT_FILE_ID_2 = 1L;
    private static final Long UPDATED_FILE_ID_2 = 2L;

    private static final String DEFAULT_FILE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttachmentMockMvc;

    private Attachment attachment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttachmentResource attachmentResource = new AttachmentResource(attachmentService);
        this.restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createEntity(EntityManager em) {
        Attachment attachment = new Attachment()
                .businessId(DEFAULT_BUSINESS_ID)
                .businessType(DEFAULT_BUSINESS_TYPE)
                .fileId1(DEFAULT_FILE_ID_1)
                .fileId2(DEFAULT_FILE_ID_2)
                .fileTitle(DEFAULT_FILE_TITLE)
                .fileName(DEFAULT_FILE_NAME)
                .description(DEFAULT_DESCRIPTION)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return attachment;
    }

    @Before
    public void initTest() {
        attachment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttachment() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getBusinessId()).isEqualTo(DEFAULT_BUSINESS_ID);
        assertThat(testAttachment.getBusinessType()).isEqualTo(DEFAULT_BUSINESS_TYPE);
        assertThat(testAttachment.getFileId1()).isEqualTo(DEFAULT_FILE_ID_1);
        assertThat(testAttachment.getFileId2()).isEqualTo(DEFAULT_FILE_ID_2);
        assertThat(testAttachment.getFileTitle()).isEqualTo(DEFAULT_FILE_TITLE);
        assertThat(testAttachment.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAttachment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAttachment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAttachment.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createAttachmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // Create the Attachment with an existing ID
        Attachment existingAttachment = new Attachment();
        existingAttachment.setId(1L);
        AttachmentDTO existingAttachmentDTO = attachmentMapper.attachmentToAttachmentDTO(existingAttachment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAttachmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBusinessIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setBusinessId(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBusinessTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setBusinessType(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileId1IsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setFileId1(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setStatus(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessId").value(hasItem(DEFAULT_BUSINESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].businessType").value(hasItem(DEFAULT_BUSINESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileId1").value(hasItem(DEFAULT_FILE_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].fileId2").value(hasItem(DEFAULT_FILE_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].fileTitle").value(hasItem(DEFAULT_FILE_TITLE.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", attachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
            .andExpect(jsonPath("$.businessId").value(DEFAULT_BUSINESS_ID.intValue()))
            .andExpect(jsonPath("$.businessType").value(DEFAULT_BUSINESS_TYPE.toString()))
            .andExpect(jsonPath("$.fileId1").value(DEFAULT_FILE_ID_1.intValue()))
            .andExpect(jsonPath("$.fileId2").value(DEFAULT_FILE_ID_2.intValue()))
            .andExpect(jsonPath("$.fileTitle").value(DEFAULT_FILE_TITLE.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAttachment() throws Exception {
        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment
        Attachment updatedAttachment = attachmentRepository.findOne(attachment.getId());
        updatedAttachment
                .businessId(UPDATED_BUSINESS_ID)
                .businessType(UPDATED_BUSINESS_TYPE)
                .fileId1(UPDATED_FILE_ID_1)
                .fileId2(UPDATED_FILE_ID_2)
                .fileTitle(UPDATED_FILE_TITLE)
                .fileName(UPDATED_FILE_NAME)
                .description(UPDATED_DESCRIPTION)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(updatedAttachment);

        restAttachmentMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getBusinessId()).isEqualTo(UPDATED_BUSINESS_ID);
        assertThat(testAttachment.getBusinessType()).isEqualTo(UPDATED_BUSINESS_TYPE);
        assertThat(testAttachment.getFileId1()).isEqualTo(UPDATED_FILE_ID_1);
        assertThat(testAttachment.getFileId2()).isEqualTo(UPDATED_FILE_ID_2);
        assertThat(testAttachment.getFileTitle()).isEqualTo(UPDATED_FILE_TITLE);
        assertThat(testAttachment.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAttachment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAttachment.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAttachmentMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);
        int databaseSizeBeforeDelete = attachmentRepository.findAll().size();

        // Get the attachment
        restAttachmentMockMvc.perform(delete("/api/attachments/{id}", attachment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachment.class);
    }
}
