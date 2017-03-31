package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.ProjectSite;
import org.fwoxford.repository.TranshipRepository;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.mapper.TranshipMapper;
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
 * Test class for the TranshipResource REST controller.
 *
 * @see TranshipResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class TranshipResourceIntTest {

    private static final LocalDate DEFAULT_TRANSHIP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSHIP_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_SITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_SITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TRACK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSHIP_BATCH = "AAAAAAAAAA";
    private static final String UPDATED_TRANSHIP_BATCH = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSHIP_STATE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSHIP_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RECEIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SAMPLE_NUMBER = 100;
    private static final Integer UPDATED_SAMPLE_NUMBER = 99;

    private static final Integer DEFAULT_FROZEN_BOX_NUMBER = 20;
    private static final Integer UPDATED_FROZEN_BOX_NUMBER = 19;

    private static final Integer DEFAULT_EMPTY_TUBE_NUMBER = 20;
    private static final Integer UPDATED_EMPTY_TUBE_NUMBER = 19;

    private static final Integer DEFAULT_EMPTY_HOLE_NUMBER = 20;
    private static final Integer UPDATED_EMPTY_HOLE_NUMBER = 19;

    private static final Integer DEFAULT_SAMPLE_SATISFACTION = 20;
    private static final Integer UPDATED_SAMPLE_SATISFACTION = 19;

    private static final Integer DEFAULT_EFFECTIVE_SAMPLE_NUMBER = 20;
    private static final Integer UPDATED_EFFECTIVE_SAMPLE_NUMBER = 19;

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSHIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSHIP_CODE = "BBBBBBBBBB";

    @Autowired
    private TranshipRepository transhipRepository;

    @Autowired
    private TranshipMapper transhipMapper;

    @Autowired
    private TranshipService transhipService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTranshipMockMvc;

    private Tranship tranship;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TranshipResource transhipResource = new TranshipResource(transhipService);
        this.restTranshipMockMvc = MockMvcBuilders.standaloneSetup(transhipResource)
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
    public static Tranship createEntity(EntityManager em) {
        Tranship tranship = new Tranship()
                .transhipDate(DEFAULT_TRANSHIP_DATE)
                .projectCode(DEFAULT_PROJECT_CODE)
                .projectName(DEFAULT_PROJECT_NAME)
                .projectSiteCode(DEFAULT_PROJECT_SITE_CODE)
                .projectSiteName(DEFAULT_PROJECT_SITE_NAME)
                .trackNumber(DEFAULT_TRACK_NUMBER)
                .transhipBatch(DEFAULT_TRANSHIP_BATCH)
                .transhipState(DEFAULT_TRANSHIP_STATE)
                .receiver(DEFAULT_RECEIVER)
                .receiveDate(DEFAULT_RECEIVE_DATE)
                .sampleNumber(DEFAULT_SAMPLE_NUMBER)
                .frozenBoxNumber(DEFAULT_FROZEN_BOX_NUMBER)
                .emptyTubeNumber(DEFAULT_EMPTY_TUBE_NUMBER)
                .emptyHoleNumber(DEFAULT_EMPTY_HOLE_NUMBER)
                .sampleSatisfaction(DEFAULT_SAMPLE_SATISFACTION)
                .effectiveSampleNumber(DEFAULT_EFFECTIVE_SAMPLE_NUMBER)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .transhipCode(DEFAULT_TRANSHIP_CODE);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        tranship.setProject(project);
        // Add required entity
        ProjectSite projectSite = ProjectSiteResourceIntTest.createEntity(em);
        em.persist(projectSite);
        em.flush();
        tranship.setProjectSite(projectSite);
        return tranship;
    }

    @Before
    public void initTest() {
        tranship = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranship() throws Exception {
        int databaseSizeBeforeCreate = transhipRepository.findAll().size();

        // Create the Tranship
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isCreated());

        // Validate the Tranship in the database
        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeCreate + 1);
        Tranship testTranship = transhipList.get(transhipList.size() - 1);
        assertThat(testTranship.getTranshipDate()).isEqualTo(DEFAULT_TRANSHIP_DATE);
        assertThat(testTranship.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testTranship.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testTranship.getProjectSiteCode()).isEqualTo(DEFAULT_PROJECT_SITE_CODE);
        assertThat(testTranship.getProjectSiteName()).isEqualTo(DEFAULT_PROJECT_SITE_NAME);
        assertThat(testTranship.getTrackNumber()).isEqualTo(DEFAULT_TRACK_NUMBER);
        assertThat(testTranship.getTranshipBatch()).isEqualTo(DEFAULT_TRANSHIP_BATCH);
        assertThat(testTranship.getTranshipState()).isEqualTo(DEFAULT_TRANSHIP_STATE);
        assertThat(testTranship.getReceiver()).isEqualTo(DEFAULT_RECEIVER);
        assertThat(testTranship.getReceiveDate()).isEqualTo(DEFAULT_RECEIVE_DATE);
        assertThat(testTranship.getSampleNumber()).isEqualTo(DEFAULT_SAMPLE_NUMBER);
        assertThat(testTranship.getFrozenBoxNumber()).isEqualTo(DEFAULT_FROZEN_BOX_NUMBER);
        assertThat(testTranship.getEmptyTubeNumber()).isEqualTo(DEFAULT_EMPTY_TUBE_NUMBER);
        assertThat(testTranship.getEmptyHoleNumber()).isEqualTo(DEFAULT_EMPTY_HOLE_NUMBER);
        assertThat(testTranship.getSampleSatisfaction()).isEqualTo(DEFAULT_SAMPLE_SATISFACTION);
        assertThat(testTranship.getEffectiveSampleNumber()).isEqualTo(DEFAULT_EFFECTIVE_SAMPLE_NUMBER);
        assertThat(testTranship.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testTranship.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTranship.getTranshipCode()).isEqualTo(DEFAULT_TRANSHIP_CODE);
    }

    @Test
    @Transactional
    public void createTranshipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transhipRepository.findAll().size();

        // Create the Tranship with an existing ID
        Tranship existingTranship = new Tranship();
        existingTranship.setId(1L);
        TranshipDTO existingTranshipDTO = transhipMapper.transhipToTranshipDTO(existingTranship);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTranshipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTranshipDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setTranshipDate(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setProjectCode(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setProjectName(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectSiteCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setProjectSiteCode(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectSiteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setProjectSiteName(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTrackNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setTrackNumber(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTranshipBatchIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setTranshipBatch(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTranshipStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setTranshipState(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiverIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setReceiver(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setReceiveDate(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setSampleNumber(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setFrozenBoxNumber(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmptyTubeNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setEmptyTubeNumber(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmptyHoleNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setEmptyHoleNumber(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEffectiveSampleNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setEffectiveSampleNumber(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setStatus(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTranshipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipRepository.findAll().size();
        // set the field null
        tranship.setTranshipCode(null);

        // Create the Tranship, which fails.
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        restTranshipMockMvc.perform(post("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isBadRequest());

        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTranships() throws Exception {
        // Initialize the database
        transhipRepository.saveAndFlush(tranship);

        // Get all the transhipList
        restTranshipMockMvc.perform(get("/api/tranships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tranship.getId().intValue())))
            .andExpect(jsonPath("$.[*].transhipDate").value(hasItem(DEFAULT_TRANSHIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].projectSiteCode").value(hasItem(DEFAULT_PROJECT_SITE_CODE.toString())))
            .andExpect(jsonPath("$.[*].projectSiteName").value(hasItem(DEFAULT_PROJECT_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].trackNumber").value(hasItem(DEFAULT_TRACK_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].transhipBatch").value(hasItem(DEFAULT_TRANSHIP_BATCH.toString())))
            .andExpect(jsonPath("$.[*].transhipState").value(hasItem(DEFAULT_TRANSHIP_STATE.toString())))
            .andExpect(jsonPath("$.[*].receiver").value(hasItem(DEFAULT_RECEIVER.toString())))
            .andExpect(jsonPath("$.[*].receiveDate").value(hasItem(DEFAULT_RECEIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].sampleNumber").value(hasItem(DEFAULT_SAMPLE_NUMBER)))
            .andExpect(jsonPath("$.[*].frozenBoxNumber").value(hasItem(DEFAULT_FROZEN_BOX_NUMBER)))
            .andExpect(jsonPath("$.[*].emptyTubeNumber").value(hasItem(DEFAULT_EMPTY_TUBE_NUMBER)))
            .andExpect(jsonPath("$.[*].emptyHoleNumber").value(hasItem(DEFAULT_EMPTY_HOLE_NUMBER)))
            .andExpect(jsonPath("$.[*].sampleSatisfaction").value(hasItem(DEFAULT_SAMPLE_SATISFACTION)))
            .andExpect(jsonPath("$.[*].effectiveSampleNumber").value(hasItem(DEFAULT_EFFECTIVE_SAMPLE_NUMBER)))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].transhipCode").value(hasItem(DEFAULT_TRANSHIP_CODE.toString())));
    }

    @Test
    @Transactional
    public void getTranship() throws Exception {
        // Initialize the database
        transhipRepository.saveAndFlush(tranship);

        // Get the tranship
        restTranshipMockMvc.perform(get("/api/tranships/{id}", tranship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tranship.getId().intValue()))
            .andExpect(jsonPath("$.transhipDate").value(DEFAULT_TRANSHIP_DATE.toString()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME.toString()))
            .andExpect(jsonPath("$.projectSiteCode").value(DEFAULT_PROJECT_SITE_CODE.toString()))
            .andExpect(jsonPath("$.projectSiteName").value(DEFAULT_PROJECT_SITE_NAME.toString()))
            .andExpect(jsonPath("$.trackNumber").value(DEFAULT_TRACK_NUMBER.toString()))
            .andExpect(jsonPath("$.transhipBatch").value(DEFAULT_TRANSHIP_BATCH.toString()))
            .andExpect(jsonPath("$.transhipState").value(DEFAULT_TRANSHIP_STATE.toString()))
            .andExpect(jsonPath("$.receiver").value(DEFAULT_RECEIVER.toString()))
            .andExpect(jsonPath("$.receiveDate").value(DEFAULT_RECEIVE_DATE.toString()))
            .andExpect(jsonPath("$.sampleNumber").value(DEFAULT_SAMPLE_NUMBER))
            .andExpect(jsonPath("$.frozenBoxNumber").value(DEFAULT_FROZEN_BOX_NUMBER))
            .andExpect(jsonPath("$.emptyTubeNumber").value(DEFAULT_EMPTY_TUBE_NUMBER))
            .andExpect(jsonPath("$.emptyHoleNumber").value(DEFAULT_EMPTY_HOLE_NUMBER))
            .andExpect(jsonPath("$.sampleSatisfaction").value(DEFAULT_SAMPLE_SATISFACTION))
            .andExpect(jsonPath("$.effectiveSampleNumber").value(DEFAULT_EFFECTIVE_SAMPLE_NUMBER))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.transhipCode").value(DEFAULT_TRANSHIP_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranship() throws Exception {
        // Get the tranship
        restTranshipMockMvc.perform(get("/api/tranships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranship() throws Exception {
        // Initialize the database
        transhipRepository.saveAndFlush(tranship);
        int databaseSizeBeforeUpdate = transhipRepository.findAll().size();

        // Update the tranship
        Tranship updatedTranship = transhipRepository.findOne(tranship.getId());
        updatedTranship
                .transhipDate(UPDATED_TRANSHIP_DATE)
                .projectCode(UPDATED_PROJECT_CODE)
                .projectName(UPDATED_PROJECT_NAME)
                .projectSiteCode(UPDATED_PROJECT_SITE_CODE)
                .projectSiteName(UPDATED_PROJECT_SITE_NAME)
                .trackNumber(UPDATED_TRACK_NUMBER)
                .transhipBatch(UPDATED_TRANSHIP_BATCH)
                .transhipState(UPDATED_TRANSHIP_STATE)
                .receiver(UPDATED_RECEIVER)
                .receiveDate(UPDATED_RECEIVE_DATE)
                .sampleNumber(UPDATED_SAMPLE_NUMBER)
                .frozenBoxNumber(UPDATED_FROZEN_BOX_NUMBER)
                .emptyTubeNumber(UPDATED_EMPTY_TUBE_NUMBER)
                .emptyHoleNumber(UPDATED_EMPTY_HOLE_NUMBER)
                .sampleSatisfaction(UPDATED_SAMPLE_SATISFACTION)
                .effectiveSampleNumber(UPDATED_EFFECTIVE_SAMPLE_NUMBER)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .transhipCode(UPDATED_TRANSHIP_CODE);
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(updatedTranship);

        restTranshipMockMvc.perform(put("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isOk());

        // Validate the Tranship in the database
        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeUpdate);
        Tranship testTranship = transhipList.get(transhipList.size() - 1);
        assertThat(testTranship.getTranshipDate()).isEqualTo(UPDATED_TRANSHIP_DATE);
        assertThat(testTranship.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testTranship.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testTranship.getProjectSiteCode()).isEqualTo(UPDATED_PROJECT_SITE_CODE);
        assertThat(testTranship.getProjectSiteName()).isEqualTo(UPDATED_PROJECT_SITE_NAME);
        assertThat(testTranship.getTrackNumber()).isEqualTo(UPDATED_TRACK_NUMBER);
        assertThat(testTranship.getTranshipBatch()).isEqualTo(UPDATED_TRANSHIP_BATCH);
        assertThat(testTranship.getTranshipState()).isEqualTo(UPDATED_TRANSHIP_STATE);
        assertThat(testTranship.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testTranship.getReceiveDate()).isEqualTo(UPDATED_RECEIVE_DATE);
        assertThat(testTranship.getSampleNumber()).isEqualTo(UPDATED_SAMPLE_NUMBER);
        assertThat(testTranship.getFrozenBoxNumber()).isEqualTo(UPDATED_FROZEN_BOX_NUMBER);
        assertThat(testTranship.getEmptyTubeNumber()).isEqualTo(UPDATED_EMPTY_TUBE_NUMBER);
        assertThat(testTranship.getEmptyHoleNumber()).isEqualTo(UPDATED_EMPTY_HOLE_NUMBER);
        assertThat(testTranship.getSampleSatisfaction()).isEqualTo(UPDATED_SAMPLE_SATISFACTION);
        assertThat(testTranship.getEffectiveSampleNumber()).isEqualTo(UPDATED_EFFECTIVE_SAMPLE_NUMBER);
        assertThat(testTranship.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testTranship.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTranship.getTranshipCode()).isEqualTo(UPDATED_TRANSHIP_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingTranship() throws Exception {
        int databaseSizeBeforeUpdate = transhipRepository.findAll().size();

        // Create the Tranship
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTranshipMockMvc.perform(put("/api/tranships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipDTO)))
            .andExpect(status().isCreated());

        // Validate the Tranship in the database
        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTranship() throws Exception {
        // Initialize the database
        transhipRepository.saveAndFlush(tranship);
        int databaseSizeBeforeDelete = transhipRepository.findAll().size();

        // Get the tranship
        restTranshipMockMvc.perform(delete("/api/tranships/{id}", tranship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tranship> transhipList = transhipRepository.findAll();
        assertThat(transhipList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tranship.class);
    }
}
