package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.SupportRack;
import org.fwoxford.domain.SupportRackType;
import org.fwoxford.domain.Area;
import org.fwoxford.repository.SupportRackRepository;
import org.fwoxford.service.SupportRackService;
import org.fwoxford.service.dto.SupportRackDTO;
import org.fwoxford.service.mapper.SupportRackMapper;
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
 * Test class for the SupportRackResource REST controller.
 *
 * @see SupportRackResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class SupportRackResourceIntTest {

    private static final String DEFAULT_SUPPORT_RACK_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_RACK_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_RACK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_RACK_CODE = "BBBBBBBBBB";

    @Autowired
    private SupportRackRepository supportRackRepository;

    @Autowired
    private SupportRackMapper supportRackMapper;

    @Autowired
    private SupportRackService supportRackService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSupportRackMockMvc;

    private SupportRack supportRack;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupportRackResource supportRackResource = new SupportRackResource(supportRackService);
        this.restSupportRackMockMvc = MockMvcBuilders.standaloneSetup(supportRackResource)
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
    public static SupportRack createEntity(EntityManager em) {
        SupportRack supportRack = new SupportRack()
                .supportRackTypeCode(DEFAULT_SUPPORT_RACK_TYPE_CODE)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE);
        // Add required entity
        SupportRackType supportRackType = SupportRackTypeResourceIntTest.createEntity(em);
        em.persist(supportRackType);
        em.flush();
        supportRack.setSupportRackType(supportRackType);
        // Add required entity
        Area area = AreaResourceIntTest.createEntity(em);
        em.persist(area);
        em.flush();
        supportRack.setArea(area);
        return supportRack;
    }

    @Before
    public void initTest() {
        supportRack = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupportRack() throws Exception {
        int databaseSizeBeforeCreate = supportRackRepository.findAll().size();

        // Create the SupportRack
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);

        restSupportRackMockMvc.perform(post("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackDTO)))
            .andExpect(status().isCreated());

        // Validate the SupportRack in the database
        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeCreate + 1);
        SupportRack testSupportRack = supportRackList.get(supportRackList.size() - 1);
        assertThat(testSupportRack.getSupportRackTypeCode()).isEqualTo(DEFAULT_SUPPORT_RACK_TYPE_CODE);
        assertThat(testSupportRack.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testSupportRack.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSupportRack.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
    }

    @Test
    @Transactional
    public void createSupportRackWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supportRackRepository.findAll().size();

        // Create the SupportRack with an existing ID
        SupportRack existingSupportRack = new SupportRack();
        existingSupportRack.setId(1L);
        SupportRackDTO existingSupportRackDTO = supportRackMapper.supportRackToSupportRackDTO(existingSupportRack);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupportRackMockMvc.perform(post("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSupportRackDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSupportRackTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackRepository.findAll().size();
        // set the field null
        supportRack.setSupportRackTypeCode(null);

        // Create the SupportRack, which fails.
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);

        restSupportRackMockMvc.perform(post("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackRepository.findAll().size();
        // Create the SupportRack, which fails.
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);

        restSupportRackMockMvc.perform(post("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackRepository.findAll().size();
        // set the field null
        supportRack.setStatus(null);

        // Create the SupportRack, which fails.
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);

        restSupportRackMockMvc.perform(post("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackRepository.findAll().size();
        // set the field null
        supportRack.setSupportRackCode(null);

        // Create the SupportRack, which fails.
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);

        restSupportRackMockMvc.perform(post("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupportRacks() throws Exception {
        // Initialize the database
        supportRackRepository.saveAndFlush(supportRack);

        // Get all the supportRackList
        restSupportRackMockMvc.perform(get("/api/support-racks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportRack.getId().intValue())))
            .andExpect(jsonPath("$.[*].supportRackTypeCode").value(hasItem(DEFAULT_SUPPORT_RACK_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())));
    }

    @Test
    @Transactional
    public void getSupportRack() throws Exception {
        // Initialize the database
        supportRackRepository.saveAndFlush(supportRack);

        // Get the supportRack
        restSupportRackMockMvc.perform(get("/api/support-racks/{id}", supportRack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supportRack.getId().intValue()))
            .andExpect(jsonPath("$.supportRackTypeCode").value(DEFAULT_SUPPORT_RACK_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSupportRack() throws Exception {
        // Get the supportRack
        restSupportRackMockMvc.perform(get("/api/support-racks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupportRack() throws Exception {
        // Initialize the database
        supportRackRepository.saveAndFlush(supportRack);
        int databaseSizeBeforeUpdate = supportRackRepository.findAll().size();

        // Update the supportRack
        SupportRack updatedSupportRack = supportRackRepository.findOne(supportRack.getId());
        updatedSupportRack
                .supportRackTypeCode(UPDATED_SUPPORT_RACK_TYPE_CODE)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE);
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(updatedSupportRack);

        restSupportRackMockMvc.perform(put("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackDTO)))
            .andExpect(status().isOk());

        // Validate the SupportRack in the database
        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeUpdate);
        SupportRack testSupportRack = supportRackList.get(supportRackList.size() - 1);
        assertThat(testSupportRack.getSupportRackTypeCode()).isEqualTo(UPDATED_SUPPORT_RACK_TYPE_CODE);
        assertThat(testSupportRack.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testSupportRack.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSupportRack.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingSupportRack() throws Exception {
        int databaseSizeBeforeUpdate = supportRackRepository.findAll().size();

        // Create the SupportRack
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSupportRackMockMvc.perform(put("/api/support-racks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackDTO)))
            .andExpect(status().isCreated());

        // Validate the SupportRack in the database
        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSupportRack() throws Exception {
        // Initialize the database
        supportRackRepository.saveAndFlush(supportRack);
        int databaseSizeBeforeDelete = supportRackRepository.findAll().size();

        // Get the supportRack
        restSupportRackMockMvc.perform(delete("/api/support-racks/{id}", supportRack.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SupportRack> supportRackList = supportRackRepository.findAll();
        assertThat(supportRackList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportRack.class);
    }
}
