package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutRequirement;
import org.fwoxford.domain.StockOutApply;
import org.fwoxford.repository.StockOutRequirementRepository;
import org.fwoxford.service.StockOutRequirementService;
import org.fwoxford.service.dto.StockOutRequirementDTO;
import org.fwoxford.service.mapper.StockOutRequirementMapper;
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
 * Test class for the StockOutRequirementResource REST controller.
 *
 * @see StockOutRequirementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutRequirementResourceIntTest {

    private static final String DEFAULT_REQUIREMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT_OF_SAMPLE = 1;
    private static final Integer UPDATED_COUNT_OF_SAMPLE = 2;

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE_MIN = 1;
    private static final Integer UPDATED_AGE_MIN = 2;

    private static final Integer DEFAULT_AGE_MAX = 1;
    private static final Integer UPDATED_AGE_MAX = 2;

    private static final String DEFAULT_DISEASE_TYPE = "1";
    private static final String UPDATED_DISEASE_TYPE = "2";

    private static final Boolean DEFAULT_IS_HEMOLYSIS = false;
    private static final Boolean UPDATED_IS_HEMOLYSIS = true;

    private static final Boolean DEFAULT_IS_BLOOD_LIPID = false;
    private static final Boolean UPDATED_IS_BLOOD_LIPID = true;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_APPLY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_APPLY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIREMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENT_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_IMPORTING_FILE_ID = 1L;
    private static final Long UPDATED_IMPORTING_FILE_ID = 2L;

    @Autowired
    private StockOutRequirementRepository stockOutRequirementRepository;

    @Autowired
    private StockOutRequirementMapper stockOutRequirementMapper;

    @Autowired
    private StockOutRequirementService stockOutRequirementService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutRequirementMockMvc;

    private StockOutRequirement stockOutRequirement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutRequirementResource stockOutRequirementResource = new StockOutRequirementResource(stockOutRequirementService);
        this.restStockOutRequirementMockMvc = MockMvcBuilders.standaloneSetup(stockOutRequirementResource)
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
    public static StockOutRequirement createEntity(EntityManager em) {
        StockOutRequirement stockOutRequirement = new StockOutRequirement()
                .requirementName(DEFAULT_REQUIREMENT_NAME)
                .countOfSample(DEFAULT_COUNT_OF_SAMPLE)
                .sex(DEFAULT_SEX)
                .ageMin(DEFAULT_AGE_MIN)
                .ageMax(DEFAULT_AGE_MAX)
                .diseaseType(DEFAULT_DISEASE_TYPE)
                .isHemolysis(DEFAULT_IS_HEMOLYSIS)
                .isBloodLipid(DEFAULT_IS_BLOOD_LIPID)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .applyCode(DEFAULT_APPLY_CODE)
                .requirementCode(DEFAULT_REQUIREMENT_CODE)
                .importingFileId(DEFAULT_IMPORTING_FILE_ID);
        // Add required entity
        StockOutApply stockOutApply = StockOutApplyResourceIntTest.createEntity(em);
        em.persist(stockOutApply);
        em.flush();
        stockOutRequirement.setStockOutApply(stockOutApply);
        return stockOutRequirement;
    }

    @Before
    public void initTest() {
        stockOutRequirement = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutRequirement() throws Exception {
        int databaseSizeBeforeCreate = stockOutRequirementRepository.findAll().size();

        // Create the StockOutRequirement
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);

        restStockOutRequirementMockMvc.perform(post("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequirementDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutRequirement in the database
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutRequirement testStockOutRequirement = stockOutRequirementList.get(stockOutRequirementList.size() - 1);
        assertThat(testStockOutRequirement.getRequirementName()).isEqualTo(DEFAULT_REQUIREMENT_NAME);
        assertThat(testStockOutRequirement.getCountOfSample()).isEqualTo(DEFAULT_COUNT_OF_SAMPLE);
        assertThat(testStockOutRequirement.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testStockOutRequirement.getAgeMin()).isEqualTo(DEFAULT_AGE_MIN);
        assertThat(testStockOutRequirement.getAgeMax()).isEqualTo(DEFAULT_AGE_MAX);
        assertThat(testStockOutRequirement.getDiseaseType()).isEqualTo(DEFAULT_DISEASE_TYPE);
        assertThat(testStockOutRequirement.isIsHemolysis()).isEqualTo(DEFAULT_IS_HEMOLYSIS);
        assertThat(testStockOutRequirement.isIsBloodLipid()).isEqualTo(DEFAULT_IS_BLOOD_LIPID);
        assertThat(testStockOutRequirement.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutRequirement.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockOutRequirement.getApplyCode()).isEqualTo(DEFAULT_APPLY_CODE);
        assertThat(testStockOutRequirement.getRequirementCode()).isEqualTo(DEFAULT_REQUIREMENT_CODE);
        assertThat(testStockOutRequirement.getImportingFileId()).isEqualTo(DEFAULT_IMPORTING_FILE_ID);
    }

    @Test
    @Transactional
    public void createStockOutRequirementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutRequirementRepository.findAll().size();

        // Create the StockOutRequirement with an existing ID
        StockOutRequirement existingStockOutRequirement = new StockOutRequirement();
        existingStockOutRequirement.setId(1L);
        StockOutRequirementDTO existingStockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(existingStockOutRequirement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutRequirementMockMvc.perform(post("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutRequirementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRequirementNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutRequirementRepository.findAll().size();
        // set the field null
        stockOutRequirement.setRequirementName(null);

        // Create the StockOutRequirement, which fails.
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);

        restStockOutRequirementMockMvc.perform(post("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequirementDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutRequirementRepository.findAll().size();
        // set the field null
        stockOutRequirement.setStatus(null);

        // Create the StockOutRequirement, which fails.
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);

        restStockOutRequirementMockMvc.perform(post("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequirementDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutRequirementRepository.findAll().size();
        // set the field null
        stockOutRequirement.setApplyCode(null);

        // Create the StockOutRequirement, which fails.
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);

        restStockOutRequirementMockMvc.perform(post("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequirementDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequirementCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutRequirementRepository.findAll().size();
        // set the field null
        stockOutRequirement.setRequirementCode(null);

        // Create the StockOutRequirement, which fails.
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);

        restStockOutRequirementMockMvc.perform(post("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequirementDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutRequirements() throws Exception {
        // Initialize the database
        stockOutRequirementRepository.saveAndFlush(stockOutRequirement);

        // Get all the stockOutRequirementList
        restStockOutRequirementMockMvc.perform(get("/api/stock-out-requirements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].requirementName").value(hasItem(DEFAULT_REQUIREMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].countOfSample").value(hasItem(DEFAULT_COUNT_OF_SAMPLE)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].ageMin").value(hasItem(DEFAULT_AGE_MIN)))
            .andExpect(jsonPath("$.[*].ageMax").value(hasItem(DEFAULT_AGE_MAX)))
            .andExpect(jsonPath("$.[*].diseaseType").value(hasItem(DEFAULT_DISEASE_TYPE)))
            .andExpect(jsonPath("$.[*].isHemolysis").value(hasItem(DEFAULT_IS_HEMOLYSIS.booleanValue())))
            .andExpect(jsonPath("$.[*].isBloodLipid").value(hasItem(DEFAULT_IS_BLOOD_LIPID.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].applyCode").value(hasItem(DEFAULT_APPLY_CODE.toString())))
            .andExpect(jsonPath("$.[*].requirementCode").value(hasItem(DEFAULT_REQUIREMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].importingFileId").value(hasItem(DEFAULT_IMPORTING_FILE_ID.intValue())));
    }

    @Test
    @Transactional
    public void getStockOutRequirement() throws Exception {
        // Initialize the database
        stockOutRequirementRepository.saveAndFlush(stockOutRequirement);

        // Get the stockOutRequirement
        restStockOutRequirementMockMvc.perform(get("/api/stock-out-requirements/{id}", stockOutRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutRequirement.getId().intValue()))
            .andExpect(jsonPath("$.requirementName").value(DEFAULT_REQUIREMENT_NAME.toString()))
            .andExpect(jsonPath("$.countOfSample").value(DEFAULT_COUNT_OF_SAMPLE))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.ageMin").value(DEFAULT_AGE_MIN))
            .andExpect(jsonPath("$.ageMax").value(DEFAULT_AGE_MAX))
            .andExpect(jsonPath("$.diseaseType").value(DEFAULT_DISEASE_TYPE))
            .andExpect(jsonPath("$.isHemolysis").value(DEFAULT_IS_HEMOLYSIS.booleanValue()))
            .andExpect(jsonPath("$.isBloodLipid").value(DEFAULT_IS_BLOOD_LIPID.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.applyCode").value(DEFAULT_APPLY_CODE.toString()))
            .andExpect(jsonPath("$.requirementCode").value(DEFAULT_REQUIREMENT_CODE.toString()))
            .andExpect(jsonPath("$.importingFileId").value(DEFAULT_IMPORTING_FILE_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutRequirement() throws Exception {
        // Get the stockOutRequirement
        restStockOutRequirementMockMvc.perform(get("/api/stock-out-requirements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutRequirement() throws Exception {
        // Initialize the database
        stockOutRequirementRepository.saveAndFlush(stockOutRequirement);
        int databaseSizeBeforeUpdate = stockOutRequirementRepository.findAll().size();

        // Update the stockOutRequirement
        StockOutRequirement updatedStockOutRequirement = stockOutRequirementRepository.findOne(stockOutRequirement.getId());
        updatedStockOutRequirement
                .requirementName(UPDATED_REQUIREMENT_NAME)
                .countOfSample(UPDATED_COUNT_OF_SAMPLE)
                .sex(UPDATED_SEX)
                .ageMin(UPDATED_AGE_MIN)
                .ageMax(UPDATED_AGE_MAX)
                .diseaseType(UPDATED_DISEASE_TYPE)
                .isHemolysis(UPDATED_IS_HEMOLYSIS)
                .isBloodLipid(UPDATED_IS_BLOOD_LIPID)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .applyCode(UPDATED_APPLY_CODE)
                .requirementCode(UPDATED_REQUIREMENT_CODE)
                .importingFileId(UPDATED_IMPORTING_FILE_ID);
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(updatedStockOutRequirement);

        restStockOutRequirementMockMvc.perform(put("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequirementDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutRequirement in the database
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeUpdate);
        StockOutRequirement testStockOutRequirement = stockOutRequirementList.get(stockOutRequirementList.size() - 1);
        assertThat(testStockOutRequirement.getRequirementName()).isEqualTo(UPDATED_REQUIREMENT_NAME);
        assertThat(testStockOutRequirement.getCountOfSample()).isEqualTo(UPDATED_COUNT_OF_SAMPLE);
        assertThat(testStockOutRequirement.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testStockOutRequirement.getAgeMin()).isEqualTo(UPDATED_AGE_MIN);
        assertThat(testStockOutRequirement.getAgeMax()).isEqualTo(UPDATED_AGE_MAX);
        assertThat(testStockOutRequirement.getDiseaseType()).isEqualTo(UPDATED_DISEASE_TYPE);
        assertThat(testStockOutRequirement.isIsHemolysis()).isEqualTo(UPDATED_IS_HEMOLYSIS);
        assertThat(testStockOutRequirement.isIsBloodLipid()).isEqualTo(UPDATED_IS_BLOOD_LIPID);
        assertThat(testStockOutRequirement.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutRequirement.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockOutRequirement.getApplyCode()).isEqualTo(UPDATED_APPLY_CODE);
        assertThat(testStockOutRequirement.getRequirementCode()).isEqualTo(UPDATED_REQUIREMENT_CODE);
        assertThat(testStockOutRequirement.getImportingFileId()).isEqualTo(UPDATED_IMPORTING_FILE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutRequirement() throws Exception {
        int databaseSizeBeforeUpdate = stockOutRequirementRepository.findAll().size();

        // Create the StockOutRequirement
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutRequirementMockMvc.perform(put("/api/stock-out-requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequirementDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutRequirement in the database
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutRequirement() throws Exception {
        // Initialize the database
        stockOutRequirementRepository.saveAndFlush(stockOutRequirement);
        int databaseSizeBeforeDelete = stockOutRequirementRepository.findAll().size();

        // Get the stockOutRequirement
        restStockOutRequirementMockMvc.perform(delete("/api/stock-out-requirements/{id}", stockOutRequirement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findAll();
        assertThat(stockOutRequirementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutRequirement.class);
    }
}
