package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.SampleClassification;
import org.fwoxford.repository.SampleClassificationRepository;
import org.fwoxford.service.SampleClassificationService;
import org.fwoxford.service.dto.SampleClassificationDTO;
import org.fwoxford.service.mapper.SampleClassificationMapper;
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
 * Test class for the SampleClassificationResource REST controller.
 *
 * @see SampleClassificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class SampleClassificationResourceIntTest {

    private static final String DEFAULT_SAMPLE_CLASSIFICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_CLASSIFICATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_CLASSIFICATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_CLASSIFICATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_FRONT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FRONT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACK_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACK_COLOR = "BBBBBBBBBB";

    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;

    @Autowired
    private SampleClassificationMapper sampleClassificationMapper;

    @Autowired
    private SampleClassificationService sampleClassificationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSampleClassificationMockMvc;

    private SampleClassification sampleClassification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SampleClassificationResource sampleClassificationResource = new SampleClassificationResource(sampleClassificationService);
        this.restSampleClassificationMockMvc = MockMvcBuilders.standaloneSetup(sampleClassificationResource)
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
    public static SampleClassification createEntity(EntityManager em) {
        SampleClassification sampleClassification = new SampleClassification()
                .sampleClassificationName(DEFAULT_SAMPLE_CLASSIFICATION_NAME)
                .sampleClassificationCode(DEFAULT_SAMPLE_CLASSIFICATION_CODE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .frontColor(DEFAULT_FRONT_COLOR)
                .backColor(DEFAULT_BACK_COLOR);
        return sampleClassification;
    }

    @Before
    public void initTest() {
        sampleClassification = createEntity(em);
    }

    @Test
    @Transactional
    public void createSampleClassification() throws Exception {
        int databaseSizeBeforeCreate = sampleClassificationRepository.findAll().size();

        // Create the SampleClassification
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);

        restSampleClassificationMockMvc.perform(post("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isCreated());

        // Validate the SampleClassification in the database
        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeCreate + 1);
        SampleClassification testSampleClassification = sampleClassificationList.get(sampleClassificationList.size() - 1);
        assertThat(testSampleClassification.getSampleClassificationName()).isEqualTo(DEFAULT_SAMPLE_CLASSIFICATION_NAME);
        assertThat(testSampleClassification.getSampleClassificationCode()).isEqualTo(DEFAULT_SAMPLE_CLASSIFICATION_CODE);
        assertThat(testSampleClassification.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSampleClassification.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testSampleClassification.getFrontColor()).isEqualTo(DEFAULT_FRONT_COLOR);
        assertThat(testSampleClassification.getBackColor()).isEqualTo(DEFAULT_BACK_COLOR);
    }

    @Test
    @Transactional
    public void createSampleClassificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sampleClassificationRepository.findAll().size();

        // Create the SampleClassification with an existing ID
        SampleClassification existingSampleClassification = new SampleClassification();
        existingSampleClassification.setId(1L);
        SampleClassificationDTO existingSampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(existingSampleClassification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSampleClassificationMockMvc.perform(post("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSampleClassificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSampleClassificationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleClassificationRepository.findAll().size();
        // set the field null
        sampleClassification.setSampleClassificationName(null);

        // Create the SampleClassification, which fails.
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);

        restSampleClassificationMockMvc.perform(post("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isBadRequest());

        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleClassificationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleClassificationRepository.findAll().size();
        // set the field null
        sampleClassification.setSampleClassificationCode(null);

        // Create the SampleClassification, which fails.
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);

        restSampleClassificationMockMvc.perform(post("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isBadRequest());

        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleClassificationRepository.findAll().size();
        // set the field null
        sampleClassification.setStatus(null);

        // Create the SampleClassification, which fails.
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);

        restSampleClassificationMockMvc.perform(post("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isBadRequest());

        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrontColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleClassificationRepository.findAll().size();
        // set the field null
        sampleClassification.setFrontColor(null);

        // Create the SampleClassification, which fails.
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);

        restSampleClassificationMockMvc.perform(post("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isBadRequest());

        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBackColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleClassificationRepository.findAll().size();
        // set the field null
        sampleClassification.setBackColor(null);

        // Create the SampleClassification, which fails.
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);

        restSampleClassificationMockMvc.perform(post("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isBadRequest());

        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSampleClassifications() throws Exception {
        // Initialize the database
        sampleClassificationRepository.saveAndFlush(sampleClassification);

        // Get all the sampleClassificationList
        restSampleClassificationMockMvc.perform(get("/api/sample-classifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sampleClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleClassificationName").value(hasItem(DEFAULT_SAMPLE_CLASSIFICATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].sampleClassificationCode").value(hasItem(DEFAULT_SAMPLE_CLASSIFICATION_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].frontColor").value(hasItem(DEFAULT_FRONT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].backColor").value(hasItem(DEFAULT_BACK_COLOR.toString())));
    }

    @Test
    @Transactional
    public void getSampleClassification() throws Exception {
        // Initialize the database
        sampleClassificationRepository.saveAndFlush(sampleClassification);

        // Get the sampleClassification
        restSampleClassificationMockMvc.perform(get("/api/sample-classifications/{id}", sampleClassification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sampleClassification.getId().intValue()))
            .andExpect(jsonPath("$.sampleClassificationName").value(DEFAULT_SAMPLE_CLASSIFICATION_NAME.toString()))
            .andExpect(jsonPath("$.sampleClassificationCode").value(DEFAULT_SAMPLE_CLASSIFICATION_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.frontColor").value(DEFAULT_FRONT_COLOR.toString()))
            .andExpect(jsonPath("$.backColor").value(DEFAULT_BACK_COLOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSampleClassification() throws Exception {
        // Get the sampleClassification
        restSampleClassificationMockMvc.perform(get("/api/sample-classifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSampleClassification() throws Exception {
        // Initialize the database
        sampleClassificationRepository.saveAndFlush(sampleClassification);
        int databaseSizeBeforeUpdate = sampleClassificationRepository.findAll().size();

        // Update the sampleClassification
        SampleClassification updatedSampleClassification = sampleClassificationRepository.findOne(sampleClassification.getId());
        updatedSampleClassification
                .sampleClassificationName(UPDATED_SAMPLE_CLASSIFICATION_NAME)
                .sampleClassificationCode(UPDATED_SAMPLE_CLASSIFICATION_CODE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .frontColor(UPDATED_FRONT_COLOR)
                .backColor(UPDATED_BACK_COLOR);
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(updatedSampleClassification);

        restSampleClassificationMockMvc.perform(put("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isOk());

        // Validate the SampleClassification in the database
        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeUpdate);
        SampleClassification testSampleClassification = sampleClassificationList.get(sampleClassificationList.size() - 1);
        assertThat(testSampleClassification.getSampleClassificationName()).isEqualTo(UPDATED_SAMPLE_CLASSIFICATION_NAME);
        assertThat(testSampleClassification.getSampleClassificationCode()).isEqualTo(UPDATED_SAMPLE_CLASSIFICATION_CODE);
        assertThat(testSampleClassification.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSampleClassification.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testSampleClassification.getFrontColor()).isEqualTo(UPDATED_FRONT_COLOR);
        assertThat(testSampleClassification.getBackColor()).isEqualTo(UPDATED_BACK_COLOR);
    }

    @Test
    @Transactional
    public void updateNonExistingSampleClassification() throws Exception {
        int databaseSizeBeforeUpdate = sampleClassificationRepository.findAll().size();

        // Create the SampleClassification
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSampleClassificationMockMvc.perform(put("/api/sample-classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleClassificationDTO)))
            .andExpect(status().isCreated());

        // Validate the SampleClassification in the database
        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSampleClassification() throws Exception {
        // Initialize the database
        sampleClassificationRepository.saveAndFlush(sampleClassification);
        int databaseSizeBeforeDelete = sampleClassificationRepository.findAll().size();

        // Get the sampleClassification
        restSampleClassificationMockMvc.perform(delete("/api/sample-classifications/{id}", sampleClassification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SampleClassification> sampleClassificationList = sampleClassificationRepository.findAll();
        assertThat(sampleClassificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleClassification.class);
    }
}
