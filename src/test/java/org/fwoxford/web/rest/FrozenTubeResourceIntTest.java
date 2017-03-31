package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.FrozenTubeType;
import org.fwoxford.domain.SampleType;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.mapper.FrozenTubeMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.fwoxford.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FrozenTubeResource REST controller.
 *
 * @see FrozenTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class FrozenTubeResourceIntTest {

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_TUBE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TEMP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TEMP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_TUBE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_TUBE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SAMPLE_USED_TIMES_MOST = 20;
    private static final Integer UPDATED_SAMPLE_USED_TIMES_MOST = 19;

    private static final Integer DEFAULT_SAMPLE_USED_TIMES = 20;
    private static final Integer UPDATED_SAMPLE_USED_TIMES = 19;

    private static final Integer DEFAULT_FROZEN_TUBE_VOLUMNS = 20;
    private static final Integer UPDATED_FROZEN_TUBE_VOLUMNS = 19;

    private static final String DEFAULT_FROZEN_TUBE_VOLUMNS_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_VOLUMNS_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_COLUMNS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_PATIENT_ID = 1L;
    private static final Long UPDATED_PATIENT_ID = 2L;

    private static final ZonedDateTime DEFAULT_DOB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DOB = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_DISEASE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DISEASE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VISIT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VISIT_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_VISIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VISIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private FrozenTubeService frozenTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFrozenTubeMockMvc;

    private FrozenTube frozenTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrozenTubeResource frozenTubeResource = new FrozenTubeResource(frozenTubeService);
        this.restFrozenTubeMockMvc = MockMvcBuilders.standaloneSetup(frozenTubeResource)
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
    public static FrozenTube createEntity(EntityManager em) {
        FrozenTube frozenTube = new FrozenTube()
                .projectCode(DEFAULT_PROJECT_CODE)
                .frozenTubeCode(DEFAULT_FROZEN_TUBE_CODE)
                .sampleTempCode(DEFAULT_SAMPLE_TEMP_CODE)
                .sampleCode(DEFAULT_SAMPLE_CODE)
                .frozenTubeTypeCode(DEFAULT_FROZEN_TUBE_TYPE_CODE)
                .frozenTubeTypeName(DEFAULT_FROZEN_TUBE_TYPE_NAME)
                .sampleTypeCode(DEFAULT_SAMPLE_TYPE_CODE)
                .sampleTypeName(DEFAULT_SAMPLE_TYPE_NAME)
                .sampleUsedTimesMost(DEFAULT_SAMPLE_USED_TIMES_MOST)
                .sampleUsedTimes(DEFAULT_SAMPLE_USED_TIMES)
                .frozenTubeVolumns(DEFAULT_FROZEN_TUBE_VOLUMNS)
                .frozenTubeVolumnsUnit(DEFAULT_FROZEN_TUBE_VOLUMNS_UNIT)
                .tubeRows(DEFAULT_TUBE_ROWS)
                .tubeColumns(DEFAULT_TUBE_COLUMNS)
                .memo(DEFAULT_MEMO)
                .errorType(DEFAULT_ERROR_TYPE)
                .status(DEFAULT_STATUS)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .patientId(DEFAULT_PATIENT_ID)
                .dob(DEFAULT_DOB)
                .gender(DEFAULT_GENDER)
                .diseaseType(DEFAULT_DISEASE_TYPE)
                .visitType(DEFAULT_VISIT_TYPE)
                .visitDate(DEFAULT_VISIT_DATE);
        // Add required entity
        FrozenTubeType frozenTubeType = FrozenTubeTypeResourceIntTest.createEntity(em);
        em.persist(frozenTubeType);
        em.flush();
        frozenTube.setFrozenTubeType(frozenTubeType);
        // Add required entity
        SampleType sampleType = SampleTypeResourceIntTest.createEntity(em);
        em.persist(sampleType);
        em.flush();
        frozenTube.setSampleType(sampleType);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        frozenTube.setProject(project);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        frozenTube.setFrozenBox(frozenBox);
        return frozenTube;
    }

    @Before
    public void initTest() {
        frozenTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrozenTube() throws Exception {
        int databaseSizeBeforeCreate = frozenTubeRepository.findAll().size();

        // Create the FrozenTube
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenTube in the database
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeCreate + 1);
        FrozenTube testFrozenTube = frozenTubeList.get(frozenTubeList.size() - 1);
        assertThat(testFrozenTube.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testFrozenTube.getFrozenTubeCode()).isEqualTo(DEFAULT_FROZEN_TUBE_CODE);
        assertThat(testFrozenTube.getSampleTempCode()).isEqualTo(DEFAULT_SAMPLE_TEMP_CODE);
        assertThat(testFrozenTube.getSampleCode()).isEqualTo(DEFAULT_SAMPLE_CODE);
        assertThat(testFrozenTube.getFrozenTubeTypeCode()).isEqualTo(DEFAULT_FROZEN_TUBE_TYPE_CODE);
        assertThat(testFrozenTube.getFrozenTubeTypeName()).isEqualTo(DEFAULT_FROZEN_TUBE_TYPE_NAME);
        assertThat(testFrozenTube.getSampleTypeCode()).isEqualTo(DEFAULT_SAMPLE_TYPE_CODE);
        assertThat(testFrozenTube.getSampleTypeName()).isEqualTo(DEFAULT_SAMPLE_TYPE_NAME);
        assertThat(testFrozenTube.getSampleUsedTimesMost()).isEqualTo(DEFAULT_SAMPLE_USED_TIMES_MOST);
        assertThat(testFrozenTube.getSampleUsedTimes()).isEqualTo(DEFAULT_SAMPLE_USED_TIMES);
        assertThat(testFrozenTube.getFrozenTubeVolumns()).isEqualTo(DEFAULT_FROZEN_TUBE_VOLUMNS);
        assertThat(testFrozenTube.getFrozenTubeVolumnsUnit()).isEqualTo(DEFAULT_FROZEN_TUBE_VOLUMNS_UNIT);
        assertThat(testFrozenTube.getTubeRows()).isEqualTo(DEFAULT_TUBE_ROWS);
        assertThat(testFrozenTube.getTubeColumns()).isEqualTo(DEFAULT_TUBE_COLUMNS);
        assertThat(testFrozenTube.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testFrozenTube.getErrorType()).isEqualTo(DEFAULT_ERROR_TYPE);
        assertThat(testFrozenTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFrozenTube.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testFrozenTube.getPatientId()).isEqualTo(DEFAULT_PATIENT_ID);
        assertThat(testFrozenTube.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testFrozenTube.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testFrozenTube.getDiseaseType()).isEqualTo(DEFAULT_DISEASE_TYPE);
        assertThat(testFrozenTube.getVisitType()).isEqualTo(DEFAULT_VISIT_TYPE);
        assertThat(testFrozenTube.getVisitDate()).isEqualTo(DEFAULT_VISIT_DATE);
    }

    @Test
    @Transactional
    public void createFrozenTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frozenTubeRepository.findAll().size();

        // Create the FrozenTube with an existing ID
        FrozenTube existingFrozenTube = new FrozenTube();
        existingFrozenTube.setId(1L);
        FrozenTubeDTO existingFrozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(existingFrozenTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setProjectCode(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setFrozenTubeCode(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTempCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setSampleTempCode(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setSampleCode(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setFrozenTubeTypeCode(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setFrozenTubeTypeName(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setSampleTypeCode(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setSampleTypeName(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleUsedTimesMostIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setSampleUsedTimesMost(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleUsedTimesIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setSampleUsedTimes(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeVolumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setFrozenTubeVolumns(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeVolumnsUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setFrozenTubeVolumnsUnit(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setTubeRows(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setTubeColumns(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setStatus(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRepository.findAll().size();
        // set the field null
        frozenTube.setFrozenBoxCode(null);

        // Create the FrozenTube, which fails.
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        restFrozenTubeMockMvc.perform(post("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFrozenTubes() throws Exception {
        // Initialize the database
        frozenTubeRepository.saveAndFlush(frozenTube);

        // Get all the frozenTubeList
        restFrozenTubeMockMvc.perform(get("/api/frozen-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frozenTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenTubeCode").value(hasItem(DEFAULT_FROZEN_TUBE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTempCode").value(hasItem(DEFAULT_SAMPLE_TEMP_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleCode").value(hasItem(DEFAULT_SAMPLE_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenTubeTypeCode").value(hasItem(DEFAULT_FROZEN_TUBE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenTubeTypeName").value(hasItem(DEFAULT_FROZEN_TUBE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].sampleTypeCode").value(hasItem(DEFAULT_SAMPLE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTypeName").value(hasItem(DEFAULT_SAMPLE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].sampleUsedTimesMost").value(hasItem(DEFAULT_SAMPLE_USED_TIMES_MOST)))
            .andExpect(jsonPath("$.[*].sampleUsedTimes").value(hasItem(DEFAULT_SAMPLE_USED_TIMES)))
            .andExpect(jsonPath("$.[*].frozenTubeVolumns").value(hasItem(DEFAULT_FROZEN_TUBE_VOLUMNS)))
            .andExpect(jsonPath("$.[*].frozenTubeVolumnsUnit").value(hasItem(DEFAULT_FROZEN_TUBE_VOLUMNS_UNIT.toString())))
            .andExpect(jsonPath("$.[*].tubeRows").value(hasItem(DEFAULT_TUBE_ROWS.toString())))
            .andExpect(jsonPath("$.[*].tubeColumns").value(hasItem(DEFAULT_TUBE_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].errorType").value(hasItem(DEFAULT_ERROR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].patientId").value(hasItem(DEFAULT_PATIENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(sameInstant(DEFAULT_DOB))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].diseaseType").value(hasItem(DEFAULT_DISEASE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].visitDate").value(hasItem(sameInstant(DEFAULT_VISIT_DATE))));
    }

    @Test
    @Transactional
    public void getFrozenTube() throws Exception {
        // Initialize the database
        frozenTubeRepository.saveAndFlush(frozenTube);

        // Get the frozenTube
        restFrozenTubeMockMvc.perform(get("/api/frozen-tubes/{id}", frozenTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frozenTube.getId().intValue()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.frozenTubeCode").value(DEFAULT_FROZEN_TUBE_CODE.toString()))
            .andExpect(jsonPath("$.sampleTempCode").value(DEFAULT_SAMPLE_TEMP_CODE.toString()))
            .andExpect(jsonPath("$.sampleCode").value(DEFAULT_SAMPLE_CODE.toString()))
            .andExpect(jsonPath("$.frozenTubeTypeCode").value(DEFAULT_FROZEN_TUBE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.frozenTubeTypeName").value(DEFAULT_FROZEN_TUBE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.sampleTypeCode").value(DEFAULT_SAMPLE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.sampleTypeName").value(DEFAULT_SAMPLE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.sampleUsedTimesMost").value(DEFAULT_SAMPLE_USED_TIMES_MOST))
            .andExpect(jsonPath("$.sampleUsedTimes").value(DEFAULT_SAMPLE_USED_TIMES))
            .andExpect(jsonPath("$.frozenTubeVolumns").value(DEFAULT_FROZEN_TUBE_VOLUMNS))
            .andExpect(jsonPath("$.frozenTubeVolumnsUnit").value(DEFAULT_FROZEN_TUBE_VOLUMNS_UNIT.toString()))
            .andExpect(jsonPath("$.tubeRows").value(DEFAULT_TUBE_ROWS.toString()))
            .andExpect(jsonPath("$.tubeColumns").value(DEFAULT_TUBE_COLUMNS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.errorType").value(DEFAULT_ERROR_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.patientId").value(DEFAULT_PATIENT_ID.intValue()))
            .andExpect(jsonPath("$.dob").value(sameInstant(DEFAULT_DOB)))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.diseaseType").value(DEFAULT_DISEASE_TYPE.toString()))
            .andExpect(jsonPath("$.visitType").value(DEFAULT_VISIT_TYPE.toString()))
            .andExpect(jsonPath("$.visitDate").value(sameInstant(DEFAULT_VISIT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingFrozenTube() throws Exception {
        // Get the frozenTube
        restFrozenTubeMockMvc.perform(get("/api/frozen-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrozenTube() throws Exception {
        // Initialize the database
        frozenTubeRepository.saveAndFlush(frozenTube);
        int databaseSizeBeforeUpdate = frozenTubeRepository.findAll().size();

        // Update the frozenTube
        FrozenTube updatedFrozenTube = frozenTubeRepository.findOne(frozenTube.getId());
        updatedFrozenTube
                .projectCode(UPDATED_PROJECT_CODE)
                .frozenTubeCode(UPDATED_FROZEN_TUBE_CODE)
                .sampleTempCode(UPDATED_SAMPLE_TEMP_CODE)
                .sampleCode(UPDATED_SAMPLE_CODE)
                .frozenTubeTypeCode(UPDATED_FROZEN_TUBE_TYPE_CODE)
                .frozenTubeTypeName(UPDATED_FROZEN_TUBE_TYPE_NAME)
                .sampleTypeCode(UPDATED_SAMPLE_TYPE_CODE)
                .sampleTypeName(UPDATED_SAMPLE_TYPE_NAME)
                .sampleUsedTimesMost(UPDATED_SAMPLE_USED_TIMES_MOST)
                .sampleUsedTimes(UPDATED_SAMPLE_USED_TIMES)
                .frozenTubeVolumns(UPDATED_FROZEN_TUBE_VOLUMNS)
                .frozenTubeVolumnsUnit(UPDATED_FROZEN_TUBE_VOLUMNS_UNIT)
                .tubeRows(UPDATED_TUBE_ROWS)
                .tubeColumns(UPDATED_TUBE_COLUMNS)
                .memo(UPDATED_MEMO)
                .errorType(UPDATED_ERROR_TYPE)
                .status(UPDATED_STATUS)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .patientId(UPDATED_PATIENT_ID)
                .dob(UPDATED_DOB)
                .gender(UPDATED_GENDER)
                .diseaseType(UPDATED_DISEASE_TYPE)
                .visitType(UPDATED_VISIT_TYPE)
                .visitDate(UPDATED_VISIT_DATE);
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(updatedFrozenTube);

        restFrozenTubeMockMvc.perform(put("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isOk());

        // Validate the FrozenTube in the database
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeUpdate);
        FrozenTube testFrozenTube = frozenTubeList.get(frozenTubeList.size() - 1);
        assertThat(testFrozenTube.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testFrozenTube.getFrozenTubeCode()).isEqualTo(UPDATED_FROZEN_TUBE_CODE);
        assertThat(testFrozenTube.getSampleTempCode()).isEqualTo(UPDATED_SAMPLE_TEMP_CODE);
        assertThat(testFrozenTube.getSampleCode()).isEqualTo(UPDATED_SAMPLE_CODE);
        assertThat(testFrozenTube.getFrozenTubeTypeCode()).isEqualTo(UPDATED_FROZEN_TUBE_TYPE_CODE);
        assertThat(testFrozenTube.getFrozenTubeTypeName()).isEqualTo(UPDATED_FROZEN_TUBE_TYPE_NAME);
        assertThat(testFrozenTube.getSampleTypeCode()).isEqualTo(UPDATED_SAMPLE_TYPE_CODE);
        assertThat(testFrozenTube.getSampleTypeName()).isEqualTo(UPDATED_SAMPLE_TYPE_NAME);
        assertThat(testFrozenTube.getSampleUsedTimesMost()).isEqualTo(UPDATED_SAMPLE_USED_TIMES_MOST);
        assertThat(testFrozenTube.getSampleUsedTimes()).isEqualTo(UPDATED_SAMPLE_USED_TIMES);
        assertThat(testFrozenTube.getFrozenTubeVolumns()).isEqualTo(UPDATED_FROZEN_TUBE_VOLUMNS);
        assertThat(testFrozenTube.getFrozenTubeVolumnsUnit()).isEqualTo(UPDATED_FROZEN_TUBE_VOLUMNS_UNIT);
        assertThat(testFrozenTube.getTubeRows()).isEqualTo(UPDATED_TUBE_ROWS);
        assertThat(testFrozenTube.getTubeColumns()).isEqualTo(UPDATED_TUBE_COLUMNS);
        assertThat(testFrozenTube.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testFrozenTube.getErrorType()).isEqualTo(UPDATED_ERROR_TYPE);
        assertThat(testFrozenTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFrozenTube.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testFrozenTube.getPatientId()).isEqualTo(UPDATED_PATIENT_ID);
        assertThat(testFrozenTube.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testFrozenTube.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testFrozenTube.getDiseaseType()).isEqualTo(UPDATED_DISEASE_TYPE);
        assertThat(testFrozenTube.getVisitType()).isEqualTo(UPDATED_VISIT_TYPE);
        assertThat(testFrozenTube.getVisitDate()).isEqualTo(UPDATED_VISIT_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFrozenTube() throws Exception {
        int databaseSizeBeforeUpdate = frozenTubeRepository.findAll().size();

        // Create the FrozenTube
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFrozenTubeMockMvc.perform(put("/api/frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenTube in the database
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFrozenTube() throws Exception {
        // Initialize the database
        frozenTubeRepository.saveAndFlush(frozenTube);
        int databaseSizeBeforeDelete = frozenTubeRepository.findAll().size();

        // Get the frozenTube
        restFrozenTubeMockMvc.perform(delete("/api/frozen-tubes/{id}", frozenTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll();
        assertThat(frozenTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrozenTube.class);
    }
}
