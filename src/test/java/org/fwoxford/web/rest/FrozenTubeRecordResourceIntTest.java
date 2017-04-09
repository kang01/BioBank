package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.FrozenTubeRecord;
import org.fwoxford.domain.FrozenTubeType;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.FrozenTubeRecordRepository;
import org.fwoxford.service.FrozenTubeRecordService;
import org.fwoxford.service.dto.FrozenTubeRecordDTO;
import org.fwoxford.service.mapper.FrozenTubeRecordMapper;
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
 * Test class for the FrozenTubeRecordResource REST controller.
 *
 * @see FrozenTubeRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class FrozenTubeRecordResourceIntTest {

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TEMP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TEMP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_COLUMNS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_MODIFY_STATE = 20;
    private static final Integer UPDATED_IS_MODIFY_STATE = 19;

    private static final Integer DEFAULT_IS_MODIFY_POSITION = 20;
    private static final Integer UPDATED_IS_MODIFY_POSITION = 19;

    @Autowired
    private FrozenTubeRecordRepository frozenTubeRecordRepository;

    @Autowired
    private FrozenTubeRecordMapper frozenTubeRecordMapper;

    @Autowired
    private FrozenTubeRecordService frozenTubeRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFrozenTubeRecordMockMvc;

    private FrozenTubeRecord frozenTubeRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrozenTubeRecordResource frozenTubeRecordResource = new FrozenTubeRecordResource(frozenTubeRecordService);
        this.restFrozenTubeRecordMockMvc = MockMvcBuilders.standaloneSetup(frozenTubeRecordResource)
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
    public static FrozenTubeRecord createEntity(EntityManager em) {
        FrozenTubeRecord frozenTubeRecord = new FrozenTubeRecord()
                .projectCode(DEFAULT_PROJECT_CODE)
                .sampleTempCode(DEFAULT_SAMPLE_TEMP_CODE)
                .sampleCode(DEFAULT_SAMPLE_CODE)
                .sampleTypeCode(DEFAULT_SAMPLE_TYPE_CODE)
                .sampleTypeName(DEFAULT_SAMPLE_TYPE_NAME)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .tubeRows(DEFAULT_TUBE_ROWS)
                .tubeColumns(DEFAULT_TUBE_COLUMNS)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .isModifyState(DEFAULT_IS_MODIFY_STATE)
                .isModifyPosition(DEFAULT_IS_MODIFY_POSITION);
        // Add required entity
        FrozenTubeType tubeType = FrozenTubeTypeResourceIntTest.createEntity(em);
        em.persist(tubeType);
        em.flush();
        frozenTubeRecord.setTubeType(tubeType);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        frozenTubeRecord.setFrozenBox(frozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        frozenTubeRecord.setFrozenTube(frozenTube);
        return frozenTubeRecord;
    }

    @Before
    public void initTest() {
        frozenTubeRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrozenTubeRecord() throws Exception {
        int databaseSizeBeforeCreate = frozenTubeRecordRepository.findAll().size();

        // Create the FrozenTubeRecord
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenTubeRecord in the database
        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeCreate + 1);
        FrozenTubeRecord testFrozenTubeRecord = frozenTubeRecordList.get(frozenTubeRecordList.size() - 1);
        assertThat(testFrozenTubeRecord.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testFrozenTubeRecord.getSampleTempCode()).isEqualTo(DEFAULT_SAMPLE_TEMP_CODE);
        assertThat(testFrozenTubeRecord.getSampleCode()).isEqualTo(DEFAULT_SAMPLE_CODE);
        assertThat(testFrozenTubeRecord.getSampleTypeCode()).isEqualTo(DEFAULT_SAMPLE_TYPE_CODE);
        assertThat(testFrozenTubeRecord.getSampleTypeName()).isEqualTo(DEFAULT_SAMPLE_TYPE_NAME);
        assertThat(testFrozenTubeRecord.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testFrozenTubeRecord.getTubeRows()).isEqualTo(DEFAULT_TUBE_ROWS);
        assertThat(testFrozenTubeRecord.getTubeColumns()).isEqualTo(DEFAULT_TUBE_COLUMNS);
        assertThat(testFrozenTubeRecord.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testFrozenTubeRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFrozenTubeRecord.getIsModifyState()).isEqualTo(DEFAULT_IS_MODIFY_STATE);
        assertThat(testFrozenTubeRecord.getIsModifyPosition()).isEqualTo(DEFAULT_IS_MODIFY_POSITION);
    }

    @Test
    @Transactional
    public void createFrozenTubeRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frozenTubeRecordRepository.findAll().size();

        // Create the FrozenTubeRecord with an existing ID
        FrozenTubeRecord existingFrozenTubeRecord = new FrozenTubeRecord();
        existingFrozenTubeRecord.setId(1L);
        FrozenTubeRecordDTO existingFrozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(existingFrozenTubeRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFrozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setProjectCode(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTempCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setSampleTempCode(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setSampleCode(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setSampleTypeCode(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setSampleTypeName(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setFrozenBoxCode(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setTubeRows(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setTubeColumns(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setStatus(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsModifyStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setIsModifyState(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsModifyPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeRecordRepository.findAll().size();
        // set the field null
        frozenTubeRecord.setIsModifyPosition(null);

        // Create the FrozenTubeRecord, which fails.
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(post("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFrozenTubeRecords() throws Exception {
        // Initialize the database
        frozenTubeRecordRepository.saveAndFlush(frozenTubeRecord);

        // Get all the frozenTubeRecordList
        restFrozenTubeRecordMockMvc.perform(get("/api/frozen-tube-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frozenTubeRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTempCode").value(hasItem(DEFAULT_SAMPLE_TEMP_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleCode").value(hasItem(DEFAULT_SAMPLE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTypeCode").value(hasItem(DEFAULT_SAMPLE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTypeName").value(hasItem(DEFAULT_SAMPLE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].tubeRows").value(hasItem(DEFAULT_TUBE_ROWS.toString())))
            .andExpect(jsonPath("$.[*].tubeColumns").value(hasItem(DEFAULT_TUBE_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isModifyState").value(hasItem(DEFAULT_IS_MODIFY_STATE)))
            .andExpect(jsonPath("$.[*].isModifyPosition").value(hasItem(DEFAULT_IS_MODIFY_POSITION)));
    }

    @Test
    @Transactional
    public void getFrozenTubeRecord() throws Exception {
        // Initialize the database
        frozenTubeRecordRepository.saveAndFlush(frozenTubeRecord);

        // Get the frozenTubeRecord
        restFrozenTubeRecordMockMvc.perform(get("/api/frozen-tube-records/{id}", frozenTubeRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frozenTubeRecord.getId().intValue()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.sampleTempCode").value(DEFAULT_SAMPLE_TEMP_CODE.toString()))
            .andExpect(jsonPath("$.sampleCode").value(DEFAULT_SAMPLE_CODE.toString()))
            .andExpect(jsonPath("$.sampleTypeCode").value(DEFAULT_SAMPLE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.sampleTypeName").value(DEFAULT_SAMPLE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.tubeRows").value(DEFAULT_TUBE_ROWS.toString()))
            .andExpect(jsonPath("$.tubeColumns").value(DEFAULT_TUBE_COLUMNS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isModifyState").value(DEFAULT_IS_MODIFY_STATE))
            .andExpect(jsonPath("$.isModifyPosition").value(DEFAULT_IS_MODIFY_POSITION));
    }

    @Test
    @Transactional
    public void getNonExistingFrozenTubeRecord() throws Exception {
        // Get the frozenTubeRecord
        restFrozenTubeRecordMockMvc.perform(get("/api/frozen-tube-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrozenTubeRecord() throws Exception {
        // Initialize the database
        frozenTubeRecordRepository.saveAndFlush(frozenTubeRecord);
        int databaseSizeBeforeUpdate = frozenTubeRecordRepository.findAll().size();

        // Update the frozenTubeRecord
        FrozenTubeRecord updatedFrozenTubeRecord = frozenTubeRecordRepository.findOne(frozenTubeRecord.getId());
        updatedFrozenTubeRecord
                .projectCode(UPDATED_PROJECT_CODE)
                .sampleTempCode(UPDATED_SAMPLE_TEMP_CODE)
                .sampleCode(UPDATED_SAMPLE_CODE)
                .sampleTypeCode(UPDATED_SAMPLE_TYPE_CODE)
                .sampleTypeName(UPDATED_SAMPLE_TYPE_NAME)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .tubeRows(UPDATED_TUBE_ROWS)
                .tubeColumns(UPDATED_TUBE_COLUMNS)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .isModifyState(UPDATED_IS_MODIFY_STATE)
                .isModifyPosition(UPDATED_IS_MODIFY_POSITION);
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(updatedFrozenTubeRecord);

        restFrozenTubeRecordMockMvc.perform(put("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isOk());

        // Validate the FrozenTubeRecord in the database
        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeUpdate);
        FrozenTubeRecord testFrozenTubeRecord = frozenTubeRecordList.get(frozenTubeRecordList.size() - 1);
        assertThat(testFrozenTubeRecord.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testFrozenTubeRecord.getSampleTempCode()).isEqualTo(UPDATED_SAMPLE_TEMP_CODE);
        assertThat(testFrozenTubeRecord.getSampleCode()).isEqualTo(UPDATED_SAMPLE_CODE);
        assertThat(testFrozenTubeRecord.getSampleTypeCode()).isEqualTo(UPDATED_SAMPLE_TYPE_CODE);
        assertThat(testFrozenTubeRecord.getSampleTypeName()).isEqualTo(UPDATED_SAMPLE_TYPE_NAME);
        assertThat(testFrozenTubeRecord.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testFrozenTubeRecord.getTubeRows()).isEqualTo(UPDATED_TUBE_ROWS);
        assertThat(testFrozenTubeRecord.getTubeColumns()).isEqualTo(UPDATED_TUBE_COLUMNS);
        assertThat(testFrozenTubeRecord.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testFrozenTubeRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFrozenTubeRecord.getIsModifyState()).isEqualTo(UPDATED_IS_MODIFY_STATE);
        assertThat(testFrozenTubeRecord.getIsModifyPosition()).isEqualTo(UPDATED_IS_MODIFY_POSITION);
    }

    @Test
    @Transactional
    public void updateNonExistingFrozenTubeRecord() throws Exception {
        int databaseSizeBeforeUpdate = frozenTubeRecordRepository.findAll().size();

        // Create the FrozenTubeRecord
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFrozenTubeRecordMockMvc.perform(put("/api/frozen-tube-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenTubeRecord in the database
        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFrozenTubeRecord() throws Exception {
        // Initialize the database
        frozenTubeRecordRepository.saveAndFlush(frozenTubeRecord);
        int databaseSizeBeforeDelete = frozenTubeRecordRepository.findAll().size();

        // Get the frozenTubeRecord
        restFrozenTubeRecordMockMvc.perform(delete("/api/frozen-tube-records/{id}", frozenTubeRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FrozenTubeRecord> frozenTubeRecordList = frozenTubeRecordRepository.findAll();
        assertThat(frozenTubeRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrozenTubeRecord.class);
    }
}
