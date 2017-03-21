package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StorageIn;
import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.ProjectSite;
import org.fwoxford.repository.StorageInRepository;
import org.fwoxford.service.StorageInService;
import org.fwoxford.service.dto.StorageInDTO;
import org.fwoxford.service.mapper.StorageInMapper;
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
 * Test class for the StorageInResource REST controller.
 *
 * @see StorageInResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StorageInResourceIntTest {

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

    private static final String DEFAULT_STORAGE_IN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_IN_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_STORAGE_IN_PERSON_ID_1 = 100L;
    private static final Long UPDATED_STORAGE_IN_PERSON_ID_1 = 99L;

    private static final String DEFAULT_STORAGE_IN_PERSON_NAME_1 = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_IN_PERSON_NAME_1 = "BBBBBBBBBB";

    private static final Long DEFAULT_STORAGE_IN_PERSON_ID_2 = 100L;
    private static final Long UPDATED_STORAGE_IN_PERSON_ID_2 = 99L;

    private static final String DEFAULT_STORANGE_IN_PERSON_NAME_2 = "AAAAAAAAAA";
    private static final String UPDATED_STORANGE_IN_PERSON_NAME_2 = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STORAGE_IN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STORAGE_IN_DATE = LocalDate.now(ZoneId.systemDefault());

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
    private StorageInRepository storageInRepository;

    @Autowired
    private StorageInMapper storageInMapper;

    @Autowired
    private StorageInService storageInService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStorageInMockMvc;

    private StorageIn storageIn;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StorageInResource storageInResource = new StorageInResource(storageInService);
        this.restStorageInMockMvc = MockMvcBuilders.standaloneSetup(storageInResource)
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
    public static StorageIn createEntity(EntityManager em) {
        StorageIn storageIn = new StorageIn()
                .projectCode(DEFAULT_PROJECT_CODE)
                .project_site_code(DEFAULT_PROJECT_SITE_CODE)
                .receiveDate(DEFAULT_RECEIVE_DATE)
                .receiveId(DEFAULT_RECEIVE_ID)
                .receiveName(DEFAULT_RECEIVE_NAME)
                .storageInType(DEFAULT_STORAGE_IN_TYPE)
                .storageInPersonId1(DEFAULT_STORAGE_IN_PERSON_ID_1)
                .storageInPersonName1(DEFAULT_STORAGE_IN_PERSON_NAME_1)
                .storageInPersonId2(DEFAULT_STORAGE_IN_PERSON_ID_2)
                .storangeInPersonName2(DEFAULT_STORANGE_IN_PERSON_NAME_2)
                .storageInDate(DEFAULT_STORAGE_IN_DATE)
                .sampleNumber(DEFAULT_SAMPLE_NUMBER)
                .signId(DEFAULT_SIGN_ID)
                .signName(DEFAULT_SIGN_NAME)
                .signDate(DEFAULT_SIGN_DATE)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        Tranship tranship = TranshipResourceIntTest.createEntity(em);
        em.persist(tranship);
        em.flush();
        storageIn.setTranship(tranship);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        storageIn.setProject(project);
        // Add required entity
        ProjectSite projectSite = ProjectSiteResourceIntTest.createEntity(em);
        em.persist(projectSite);
        em.flush();
        storageIn.setProjectSite(projectSite);
        return storageIn;
    }

    @Before
    public void initTest() {
        storageIn = createEntity(em);
    }

    @Test
    @Transactional
    public void createStorageIn() throws Exception {
        int databaseSizeBeforeCreate = storageInRepository.findAll().size();

        // Create the StorageIn
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isCreated());

        // Validate the StorageIn in the database
        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeCreate + 1);
        StorageIn testStorageIn = storageInList.get(storageInList.size() - 1);
        assertThat(testStorageIn.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testStorageIn.getProject_site_code()).isEqualTo(DEFAULT_PROJECT_SITE_CODE);
        assertThat(testStorageIn.getReceiveDate()).isEqualTo(DEFAULT_RECEIVE_DATE);
        assertThat(testStorageIn.getReceiveId()).isEqualTo(DEFAULT_RECEIVE_ID);
        assertThat(testStorageIn.getReceiveName()).isEqualTo(DEFAULT_RECEIVE_NAME);
        assertThat(testStorageIn.getStorageInType()).isEqualTo(DEFAULT_STORAGE_IN_TYPE);
        assertThat(testStorageIn.getStorageInPersonId1()).isEqualTo(DEFAULT_STORAGE_IN_PERSON_ID_1);
        assertThat(testStorageIn.getStorageInPersonName1()).isEqualTo(DEFAULT_STORAGE_IN_PERSON_NAME_1);
        assertThat(testStorageIn.getStorageInPersonId2()).isEqualTo(DEFAULT_STORAGE_IN_PERSON_ID_2);
        assertThat(testStorageIn.getStorangeInPersonName2()).isEqualTo(DEFAULT_STORANGE_IN_PERSON_NAME_2);
        assertThat(testStorageIn.getStorageInDate()).isEqualTo(DEFAULT_STORAGE_IN_DATE);
        assertThat(testStorageIn.getSampleNumber()).isEqualTo(DEFAULT_SAMPLE_NUMBER);
        assertThat(testStorageIn.getSignId()).isEqualTo(DEFAULT_SIGN_ID);
        assertThat(testStorageIn.getSignName()).isEqualTo(DEFAULT_SIGN_NAME);
        assertThat(testStorageIn.getSignDate()).isEqualTo(DEFAULT_SIGN_DATE);
        assertThat(testStorageIn.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStorageIn.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createStorageInWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storageInRepository.findAll().size();

        // Create the StorageIn with an existing ID
        StorageIn existingStorageIn = new StorageIn();
        existingStorageIn.setId(1L);
        StorageInDTO existingStorageInDTO = storageInMapper.storageInToStorageInDTO(existingStorageIn);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStorageInDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setProjectCode(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProject_site_codeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setProject_site_code(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setReceiveDate(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiveIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setReceiveId(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiveNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setReceiveName(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorageInTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setStorageInType(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorageInPersonId1IsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setStorageInPersonId1(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorageInPersonName1IsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setStorageInPersonName1(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorageInPersonId2IsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setStorageInPersonId2(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorangeInPersonName2IsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setStorangeInPersonName2(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorageInDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setStorageInDate(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setSampleNumber(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setSignId(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setSignName(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setSignDate(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInRepository.findAll().size();
        // set the field null
        storageIn.setStatus(null);

        // Create the StorageIn, which fails.
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        restStorageInMockMvc.perform(post("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isBadRequest());

        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStorageIns() throws Exception {
        // Initialize the database
        storageInRepository.saveAndFlush(storageIn);

        // Get all the storageInList
        restStorageInMockMvc.perform(get("/api/storage-ins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageIn.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].project_site_code").value(hasItem(DEFAULT_PROJECT_SITE_CODE.toString())))
            .andExpect(jsonPath("$.[*].receiveDate").value(hasItem(DEFAULT_RECEIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiveId").value(hasItem(DEFAULT_RECEIVE_ID.intValue())))
            .andExpect(jsonPath("$.[*].receiveName").value(hasItem(DEFAULT_RECEIVE_NAME.toString())))
            .andExpect(jsonPath("$.[*].storageInType").value(hasItem(DEFAULT_STORAGE_IN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].storageInPersonId1").value(hasItem(DEFAULT_STORAGE_IN_PERSON_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].storageInPersonName1").value(hasItem(DEFAULT_STORAGE_IN_PERSON_NAME_1.toString())))
            .andExpect(jsonPath("$.[*].storageInPersonId2").value(hasItem(DEFAULT_STORAGE_IN_PERSON_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].storangeInPersonName2").value(hasItem(DEFAULT_STORANGE_IN_PERSON_NAME_2.toString())))
            .andExpect(jsonPath("$.[*].storageInDate").value(hasItem(DEFAULT_STORAGE_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].sampleNumber").value(hasItem(DEFAULT_SAMPLE_NUMBER)))
            .andExpect(jsonPath("$.[*].signId").value(hasItem(DEFAULT_SIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME.toString())))
            .andExpect(jsonPath("$.[*].signDate").value(hasItem(DEFAULT_SIGN_DATE.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getStorageIn() throws Exception {
        // Initialize the database
        storageInRepository.saveAndFlush(storageIn);

        // Get the storageIn
        restStorageInMockMvc.perform(get("/api/storage-ins/{id}", storageIn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storageIn.getId().intValue()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.project_site_code").value(DEFAULT_PROJECT_SITE_CODE.toString()))
            .andExpect(jsonPath("$.receiveDate").value(DEFAULT_RECEIVE_DATE.toString()))
            .andExpect(jsonPath("$.receiveId").value(DEFAULT_RECEIVE_ID.intValue()))
            .andExpect(jsonPath("$.receiveName").value(DEFAULT_RECEIVE_NAME.toString()))
            .andExpect(jsonPath("$.storageInType").value(DEFAULT_STORAGE_IN_TYPE.toString()))
            .andExpect(jsonPath("$.storageInPersonId1").value(DEFAULT_STORAGE_IN_PERSON_ID_1.intValue()))
            .andExpect(jsonPath("$.storageInPersonName1").value(DEFAULT_STORAGE_IN_PERSON_NAME_1.toString()))
            .andExpect(jsonPath("$.storageInPersonId2").value(DEFAULT_STORAGE_IN_PERSON_ID_2.intValue()))
            .andExpect(jsonPath("$.storangeInPersonName2").value(DEFAULT_STORANGE_IN_PERSON_NAME_2.toString()))
            .andExpect(jsonPath("$.storageInDate").value(DEFAULT_STORAGE_IN_DATE.toString()))
            .andExpect(jsonPath("$.sampleNumber").value(DEFAULT_SAMPLE_NUMBER))
            .andExpect(jsonPath("$.signId").value(DEFAULT_SIGN_ID.intValue()))
            .andExpect(jsonPath("$.signName").value(DEFAULT_SIGN_NAME.toString()))
            .andExpect(jsonPath("$.signDate").value(DEFAULT_SIGN_DATE.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStorageIn() throws Exception {
        // Get the storageIn
        restStorageInMockMvc.perform(get("/api/storage-ins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStorageIn() throws Exception {
        // Initialize the database
        storageInRepository.saveAndFlush(storageIn);
        int databaseSizeBeforeUpdate = storageInRepository.findAll().size();

        // Update the storageIn
        StorageIn updatedStorageIn = storageInRepository.findOne(storageIn.getId());
        updatedStorageIn
                .projectCode(UPDATED_PROJECT_CODE)
                .project_site_code(UPDATED_PROJECT_SITE_CODE)
                .receiveDate(UPDATED_RECEIVE_DATE)
                .receiveId(UPDATED_RECEIVE_ID)
                .receiveName(UPDATED_RECEIVE_NAME)
                .storageInType(UPDATED_STORAGE_IN_TYPE)
                .storageInPersonId1(UPDATED_STORAGE_IN_PERSON_ID_1)
                .storageInPersonName1(UPDATED_STORAGE_IN_PERSON_NAME_1)
                .storageInPersonId2(UPDATED_STORAGE_IN_PERSON_ID_2)
                .storangeInPersonName2(UPDATED_STORANGE_IN_PERSON_NAME_2)
                .storageInDate(UPDATED_STORAGE_IN_DATE)
                .sampleNumber(UPDATED_SAMPLE_NUMBER)
                .signId(UPDATED_SIGN_ID)
                .signName(UPDATED_SIGN_NAME)
                .signDate(UPDATED_SIGN_DATE)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(updatedStorageIn);

        restStorageInMockMvc.perform(put("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isOk());

        // Validate the StorageIn in the database
        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeUpdate);
        StorageIn testStorageIn = storageInList.get(storageInList.size() - 1);
        assertThat(testStorageIn.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testStorageIn.getProject_site_code()).isEqualTo(UPDATED_PROJECT_SITE_CODE);
        assertThat(testStorageIn.getReceiveDate()).isEqualTo(UPDATED_RECEIVE_DATE);
        assertThat(testStorageIn.getReceiveId()).isEqualTo(UPDATED_RECEIVE_ID);
        assertThat(testStorageIn.getReceiveName()).isEqualTo(UPDATED_RECEIVE_NAME);
        assertThat(testStorageIn.getStorageInType()).isEqualTo(UPDATED_STORAGE_IN_TYPE);
        assertThat(testStorageIn.getStorageInPersonId1()).isEqualTo(UPDATED_STORAGE_IN_PERSON_ID_1);
        assertThat(testStorageIn.getStorageInPersonName1()).isEqualTo(UPDATED_STORAGE_IN_PERSON_NAME_1);
        assertThat(testStorageIn.getStorageInPersonId2()).isEqualTo(UPDATED_STORAGE_IN_PERSON_ID_2);
        assertThat(testStorageIn.getStorangeInPersonName2()).isEqualTo(UPDATED_STORANGE_IN_PERSON_NAME_2);
        assertThat(testStorageIn.getStorageInDate()).isEqualTo(UPDATED_STORAGE_IN_DATE);
        assertThat(testStorageIn.getSampleNumber()).isEqualTo(UPDATED_SAMPLE_NUMBER);
        assertThat(testStorageIn.getSignId()).isEqualTo(UPDATED_SIGN_ID);
        assertThat(testStorageIn.getSignName()).isEqualTo(UPDATED_SIGN_NAME);
        assertThat(testStorageIn.getSignDate()).isEqualTo(UPDATED_SIGN_DATE);
        assertThat(testStorageIn.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStorageIn.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingStorageIn() throws Exception {
        int databaseSizeBeforeUpdate = storageInRepository.findAll().size();

        // Create the StorageIn
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStorageInMockMvc.perform(put("/api/storage-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInDTO)))
            .andExpect(status().isCreated());

        // Validate the StorageIn in the database
        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStorageIn() throws Exception {
        // Initialize the database
        storageInRepository.saveAndFlush(storageIn);
        int databaseSizeBeforeDelete = storageInRepository.findAll().size();

        // Get the storageIn
        restStorageInMockMvc.perform(delete("/api/storage-ins/{id}", storageIn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StorageIn> storageInList = storageInRepository.findAll();
        assertThat(storageInList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageIn.class);
    }
}
