package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockIn;
import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.ProjectSite;
import org.fwoxford.repository.StockInRepository;
import org.fwoxford.service.StockInService;
import org.fwoxford.service.dto.StockInDTO;
import org.fwoxford.service.mapper.StockInMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockInResource REST controller.
 *
 * @see StockInResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockInResourceIntTest {

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_SITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RECEIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_RECEIVE_ID = 100L;
    private static final Long UPDATED_RECEIVE_ID = 99L;

    private static final String DEFAULT_RECEIVE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STOck_IN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STOck_IN_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_STOck_IN_PERSON_ID_1 = 100L;
    private static final Long UPDATED_STOck_IN_PERSON_ID_1 = 99L;

    private static final String DEFAULT_STOck_IN_PERSON_NAME_1 = "AAAAAAAAAA";
    private static final String UPDATED_STOck_IN_PERSON_NAME_1 = "BBBBBBBBBB";

    private static final Long DEFAULT_STOck_IN_PERSON_ID_2 = 100L;
    private static final Long UPDATED_STOck_IN_PERSON_ID_2 = 99L;

    private static final String DEFAULT_STORANGE_IN_PERSON_NAME_2 = "AAAAAAAAAA";
    private static final String UPDATED_STORANGE_IN_PERSON_NAME_2 = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STOck_IN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STOck_IN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SAMPLE_NUMBER = 100;
    private static final Integer UPDATED_SAMPLE_NUMBER = 99;

    private static final Long DEFAULT_SIGN_ID = 100L;
    private static final Long UPDATED_SIGN_ID = 99L;

    private static final String DEFAULT_SIGN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SIGN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SIGN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private StockInMapper stockInMapper;

    @Autowired
    private StockInService stockInService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockInMockMvc;

    private StockIn stockIn;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockInResource stockInResource = new StockInResource(stockInService);
        this.restStockInMockMvc = MockMvcBuilders.standaloneSetup(stockInResource)
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
    public static StockIn createEntity(EntityManager em) {
        StockIn stockIn = new StockIn()
                .projectCode(DEFAULT_PROJECT_CODE)
                .projectSiteCode(DEFAULT_PROJECT_SITE_CODE)
                .receiveDate(DEFAULT_RECEIVE_DATE)
                .receiveId(DEFAULT_RECEIVE_ID)
                .receiveName(DEFAULT_RECEIVE_NAME)
                .stockInType(DEFAULT_STOck_IN_TYPE)
                .storeKeeperId1(DEFAULT_STOck_IN_PERSON_ID_1)
                .storeKeeper1(DEFAULT_STOck_IN_PERSON_NAME_1)
                .storeKeeperId2(DEFAULT_STOck_IN_PERSON_ID_2)
                .storeKeeper2(DEFAULT_STORANGE_IN_PERSON_NAME_2)
                .stockInDate(DEFAULT_STOck_IN_DATE)
                .countOfSample(DEFAULT_SAMPLE_NUMBER)
                .signId(DEFAULT_SIGN_ID)
                .signName(DEFAULT_SIGN_NAME)
                .signDate(DEFAULT_SIGN_DATE)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        Tranship tranship = TranshipResourceIntTest.createEntity(em);
        em.persist(tranship);
        em.flush();
        stockIn.setTranship(tranship);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        stockIn.setProject(project);
        // Add required entity
        ProjectSite projectSite = ProjectSiteResourceIntTest.createEntity(em);
        em.persist(projectSite);
        em.flush();
        stockIn.setProjectSite(projectSite);
        return stockIn;
    }

    @Before
    public void initTest() {
        stockIn = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockIn() throws Exception {
        int databaseSizeBeforeCreate = stockInRepository.findAll().size();

        // Create the StockIn
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isCreated());

        // Validate the StockIn in the database
        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeCreate + 1);
        StockIn testStockIn = stockInList.get(stockInList.size() - 1);
        assertThat(testStockIn.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testStockIn.getProjectSiteCode()).isEqualTo(DEFAULT_PROJECT_SITE_CODE);
        assertThat(testStockIn.getReceiveDate()).isEqualTo(DEFAULT_RECEIVE_DATE);
        assertThat(testStockIn.getReceiveId()).isEqualTo(DEFAULT_RECEIVE_ID);
        assertThat(testStockIn.getReceiveName()).isEqualTo(DEFAULT_RECEIVE_NAME);
        assertThat(testStockIn.getStockInType()).isEqualTo(DEFAULT_STOck_IN_TYPE);
        assertThat(testStockIn.getStoreKeeperId1()).isEqualTo(DEFAULT_STOck_IN_PERSON_ID_1);
        assertThat(testStockIn.getStoreKeeper1()).isEqualTo(DEFAULT_STOck_IN_PERSON_NAME_1);
        assertThat(testStockIn.getStoreKeeperId2()).isEqualTo(DEFAULT_STOck_IN_PERSON_ID_2);
        assertThat(testStockIn.getStoreKeeper2()).isEqualTo(DEFAULT_STORANGE_IN_PERSON_NAME_2);
        assertThat(testStockIn.getStockInDate()).isEqualTo(DEFAULT_STOck_IN_DATE);
        assertThat(testStockIn.getCountOfSample()).isEqualTo(DEFAULT_SAMPLE_NUMBER);
        assertThat(testStockIn.getSignId()).isEqualTo(DEFAULT_SIGN_ID);
        assertThat(testStockIn.getSignName()).isEqualTo(DEFAULT_SIGN_NAME);
        assertThat(testStockIn.getSignDate()).isEqualTo(DEFAULT_SIGN_DATE);
        assertThat(testStockIn.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockIn.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createStockInWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockInRepository.findAll().size();

        // Create the StockIn with an existing ID
        StockIn existingStockIn = new StockIn();
        existingStockIn.setId(1L);
        StockInDTO existingStockInDTO = stockInMapper.stockInToStockInDTO(existingStockIn);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockInDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setProjectCode(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProject_site_codeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setProjectSiteCode(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setReceiveDate(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiveIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setReceiveId(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiveNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setReceiveName(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockInTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setStockInType(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockInPersonId1IsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setStoreKeeperId1(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockInPersonName1IsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setStoreKeeper1(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockInPersonId2IsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setStoreKeeperId2(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorangeInPersonName2IsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setStoreKeeper2(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockInDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setStockInDate(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setCountOfSample(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setSignId(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setSignName(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setSignDate(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInRepository.findAll().size();
        // set the field null
        stockIn.setStatus(null);

        // Create the StockIn, which fails.
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        restStockInMockMvc.perform(post("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isBadRequest());

        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockIns() throws Exception {
        // Initialize the database
        stockInRepository.saveAndFlush(stockIn);

        // Get all the stockInList
        restStockInMockMvc.perform(get("/api/stock-ins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockIn.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].project_site_code").value(hasItem(DEFAULT_PROJECT_SITE_CODE.toString())))
            .andExpect(jsonPath("$.[*].receiveDate").value(hasItem(DEFAULT_RECEIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiveId").value(hasItem(DEFAULT_RECEIVE_ID.intValue())))
            .andExpect(jsonPath("$.[*].receiveName").value(hasItem(DEFAULT_RECEIVE_NAME.toString())))
            .andExpect(jsonPath("$.[*].stockInType").value(hasItem(DEFAULT_STOck_IN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].stockInPersonId1").value(hasItem(DEFAULT_STOck_IN_PERSON_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].stockInPersonName1").value(hasItem(DEFAULT_STOck_IN_PERSON_NAME_1.toString())))
            .andExpect(jsonPath("$.[*].stockInPersonId2").value(hasItem(DEFAULT_STOck_IN_PERSON_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].storangeInPersonName2").value(hasItem(DEFAULT_STORANGE_IN_PERSON_NAME_2.toString())))
            .andExpect(jsonPath("$.[*].stockInDate").value(hasItem(DEFAULT_STOck_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].sampleNumber").value(hasItem(DEFAULT_SAMPLE_NUMBER)))
            .andExpect(jsonPath("$.[*].signId").value(hasItem(DEFAULT_SIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME.toString())))
            .andExpect(jsonPath("$.[*].signDate").value(hasItem(DEFAULT_SIGN_DATE.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getStockIn() throws Exception {
        // Initialize the database
        stockInRepository.saveAndFlush(stockIn);

        // Get the stockIn
        restStockInMockMvc.perform(get("/api/stock-ins/{id}", stockIn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockIn.getId().intValue()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.project_site_code").value(DEFAULT_PROJECT_SITE_CODE.toString()))
            .andExpect(jsonPath("$.receiveDate").value(DEFAULT_RECEIVE_DATE.toString()))
            .andExpect(jsonPath("$.receiveId").value(DEFAULT_RECEIVE_ID.intValue()))
            .andExpect(jsonPath("$.receiveName").value(DEFAULT_RECEIVE_NAME.toString()))
            .andExpect(jsonPath("$.stockInType").value(DEFAULT_STOck_IN_TYPE.toString()))
            .andExpect(jsonPath("$.stockInPersonId1").value(DEFAULT_STOck_IN_PERSON_ID_1.intValue()))
            .andExpect(jsonPath("$.stockInPersonName1").value(DEFAULT_STOck_IN_PERSON_NAME_1.toString()))
            .andExpect(jsonPath("$.stockInPersonId2").value(DEFAULT_STOck_IN_PERSON_ID_2.intValue()))
            .andExpect(jsonPath("$.storangeInPersonName2").value(DEFAULT_STORANGE_IN_PERSON_NAME_2.toString()))
            .andExpect(jsonPath("$.stockInDate").value(DEFAULT_STOck_IN_DATE.toString()))
            .andExpect(jsonPath("$.sampleNumber").value(DEFAULT_SAMPLE_NUMBER))
            .andExpect(jsonPath("$.signId").value(DEFAULT_SIGN_ID.intValue()))
            .andExpect(jsonPath("$.signName").value(DEFAULT_SIGN_NAME.toString()))
            .andExpect(jsonPath("$.signDate").value(DEFAULT_SIGN_DATE.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockIn() throws Exception {
        // Get the stockIn
        restStockInMockMvc.perform(get("/api/stock-ins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockIn() throws Exception {
        // Initialize the database
        stockInRepository.saveAndFlush(stockIn);
        int databaseSizeBeforeUpdate = stockInRepository.findAll().size();

        // Update the stockIn
        StockIn updatedStockIn = stockInRepository.findOne(stockIn.getId());
        updatedStockIn
                .projectCode(UPDATED_PROJECT_CODE)
                .projectSiteCode(UPDATED_PROJECT_SITE_CODE)
                .receiveDate(UPDATED_RECEIVE_DATE)
                .receiveId(UPDATED_RECEIVE_ID)
                .receiveName(UPDATED_RECEIVE_NAME)
                .stockInType(UPDATED_STOck_IN_TYPE)
                .storeKeeperId1(UPDATED_STOck_IN_PERSON_ID_1)
                .storeKeeper1(UPDATED_STOck_IN_PERSON_NAME_1)
                .storeKeeperId2(UPDATED_STOck_IN_PERSON_ID_2)
                .storeKeeper2(UPDATED_STORANGE_IN_PERSON_NAME_2)
                .stockInDate(UPDATED_STOck_IN_DATE)
                .countOfSample(UPDATED_SAMPLE_NUMBER)
                .signId(UPDATED_SIGN_ID)
                .signName(UPDATED_SIGN_NAME)
                .signDate(UPDATED_SIGN_DATE)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(updatedStockIn);

        restStockInMockMvc.perform(put("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isOk());

        // Validate the StockIn in the database
        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeUpdate);
        StockIn testStockIn = stockInList.get(stockInList.size() - 1);
        assertThat(testStockIn.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testStockIn.getProjectSiteCode()).isEqualTo(UPDATED_PROJECT_SITE_CODE);
        assertThat(testStockIn.getReceiveDate()).isEqualTo(UPDATED_RECEIVE_DATE);
        assertThat(testStockIn.getReceiveId()).isEqualTo(UPDATED_RECEIVE_ID);
        assertThat(testStockIn.getReceiveName()).isEqualTo(UPDATED_RECEIVE_NAME);
        assertThat(testStockIn.getStockInType()).isEqualTo(UPDATED_STOck_IN_TYPE);
        assertThat(testStockIn.getStoreKeeperId1()).isEqualTo(UPDATED_STOck_IN_PERSON_ID_1);
        assertThat(testStockIn.getStoreKeeper1()).isEqualTo(UPDATED_STOck_IN_PERSON_NAME_1);
        assertThat(testStockIn.getStoreKeeperId2()).isEqualTo(UPDATED_STOck_IN_PERSON_ID_2);
        assertThat(testStockIn.getStoreKeeper2()).isEqualTo(UPDATED_STORANGE_IN_PERSON_NAME_2);
        assertThat(testStockIn.getStockInDate()).isEqualTo(UPDATED_STOck_IN_DATE);
        assertThat(testStockIn.getCountOfSample()).isEqualTo(UPDATED_SAMPLE_NUMBER);
        assertThat(testStockIn.getSignId()).isEqualTo(UPDATED_SIGN_ID);
        assertThat(testStockIn.getSignName()).isEqualTo(UPDATED_SIGN_NAME);
        assertThat(testStockIn.getSignDate()).isEqualTo(UPDATED_SIGN_DATE);
        assertThat(testStockIn.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockIn.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingStockIn() throws Exception {
        int databaseSizeBeforeUpdate = stockInRepository.findAll().size();

        // Create the StockIn
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockInMockMvc.perform(put("/api/stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInDTO)))
            .andExpect(status().isCreated());

        // Validate the StockIn in the database
        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockIn() throws Exception {
        // Initialize the database
        stockInRepository.saveAndFlush(stockIn);
        int databaseSizeBeforeDelete = stockInRepository.findAll().size();

        // Get the stockIn
        restStockInMockMvc.perform(delete("/api/stock-ins/{id}", stockIn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockIn> stockInList = stockInRepository.findAll();
        assertThat(stockInList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockIn.class);
    }
}
