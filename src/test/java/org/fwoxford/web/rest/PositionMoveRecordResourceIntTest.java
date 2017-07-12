package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.PositionMoveRecord;
import org.fwoxford.domain.Equipment;
import org.fwoxford.domain.Area;
import org.fwoxford.domain.SupportRack;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.PositionMove;
import org.fwoxford.repository.PositionMoveRecordRepository;
import org.fwoxford.service.PositionMoveRecordService;
import org.fwoxford.service.dto.PositionMoveRecordDTO;
import org.fwoxford.service.mapper.PositionMoveRecordMapper;
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
 * Test class for the PositionMoveRecordResource REST controller.
 *
 * @see PositionMoveRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class PositionMoveRecordResourceIntTest {

    private static final String DEFAULT_EQUIPMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_RACK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_RACK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ROWS_IN_SHELF = "AAAAAAAAAA";
    private static final String UPDATED_ROWS_IN_SHELF = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMNS_IN_SHELF = "AAAAAAAAAA";
    private static final String UPDATED_COLUMNS_IN_SHELF = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_COLUMNS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WHETHER_FREEZING_AND_THAWING = false;
    private static final Boolean UPDATED_WHETHER_FREEZING_AND_THAWING = true;

    private static final String DEFAULT_MOVE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MOVE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_SITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private PositionMoveRecordRepository positionMoveRecordRepository;

    @Autowired
    private PositionMoveRecordMapper positionMoveRecordMapper;

    @Autowired
    private PositionMoveRecordService positionMoveRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPositionMoveRecordMockMvc;

    private PositionMoveRecord positionMoveRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionMoveRecordResource positionMoveRecordResource = new PositionMoveRecordResource(positionMoveRecordService);
        this.restPositionMoveRecordMockMvc = MockMvcBuilders.standaloneSetup(positionMoveRecordResource)
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
    public static PositionMoveRecord createEntity(EntityManager em) {
        PositionMoveRecord positionMoveRecord = new PositionMoveRecord()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .tubeRows(DEFAULT_TUBE_ROWS)
                .tubeColumns(DEFAULT_TUBE_COLUMNS)
                .whetherFreezingAndThawing(DEFAULT_WHETHER_FREEZING_AND_THAWING)
                .moveType(DEFAULT_MOVE_TYPE)
                .projectCode(DEFAULT_PROJECT_CODE)
                .projectSiteCode(DEFAULT_PROJECT_SITE_CODE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        Equipment equipment = EquipmentResourceIntTest.createEntity(em);
        em.persist(equipment);
        em.flush();
        positionMoveRecord.setEquipment(equipment);
        // Add required entity
        Area area = AreaResourceIntTest.createEntity(em);
        em.persist(area);
        em.flush();
        positionMoveRecord.setArea(area);
        // Add required entity
        SupportRack supportRack = SupportRackResourceIntTest.createEntity(em);
        em.persist(supportRack);
        em.flush();
        positionMoveRecord.setSupportRack(supportRack);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        positionMoveRecord.setFrozenBox(frozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        positionMoveRecord.setFrozenTube(frozenTube);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        positionMoveRecord.setProject(project);
        // Add required entity
        PositionMove positionMove = PositionMoveResourceIntTest.createEntity(em);
        em.persist(positionMove);
        em.flush();
        positionMoveRecord.setPositionMove(positionMove);
        return positionMoveRecord;
    }

    @Before
    public void initTest() {
        positionMoveRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createPositionMoveRecord() throws Exception {
        int databaseSizeBeforeCreate = positionMoveRecordRepository.findAll().size();

        // Create the PositionMoveRecord
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionMoveRecord in the database
        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeCreate + 1);
        PositionMoveRecord testPositionMoveRecord = positionMoveRecordList.get(positionMoveRecordList.size() - 1);
        assertThat(testPositionMoveRecord.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testPositionMoveRecord.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testPositionMoveRecord.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testPositionMoveRecord.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testPositionMoveRecord.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testPositionMoveRecord.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testPositionMoveRecord.getTubeRows()).isEqualTo(DEFAULT_TUBE_ROWS);
        assertThat(testPositionMoveRecord.getTubeColumns()).isEqualTo(DEFAULT_TUBE_COLUMNS);
        assertThat(testPositionMoveRecord.isWhetherFreezingAndThawing()).isEqualTo(DEFAULT_WHETHER_FREEZING_AND_THAWING);
        assertThat(testPositionMoveRecord.getMoveType()).isEqualTo(DEFAULT_MOVE_TYPE);
        assertThat(testPositionMoveRecord.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testPositionMoveRecord.getProjectSiteCode()).isEqualTo(DEFAULT_PROJECT_SITE_CODE);
        assertThat(testPositionMoveRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPositionMoveRecord.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createPositionMoveRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionMoveRecordRepository.findAll().size();

        // Create the PositionMoveRecord with an existing ID
        PositionMoveRecord existingPositionMoveRecord = new PositionMoveRecord();
        existingPositionMoveRecord.setId(1L);
        PositionMoveRecordDTO existingPositionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(existingPositionMoveRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPositionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEquipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setEquipmentCode(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setAreaCode(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setSupportRackCode(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRowsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setRowsInShelf(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColumnsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setColumnsInShelf(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setFrozenBoxCode(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setTubeRows(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setTubeColumns(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMoveTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setMoveType(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setProjectCode(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRecordRepository.findAll().size();
        // set the field null
        positionMoveRecord.setStatus(null);

        // Create the PositionMoveRecord, which fails.
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        restPositionMoveRecordMockMvc.perform(post("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPositionMoveRecords() throws Exception {
        // Initialize the database
        positionMoveRecordRepository.saveAndFlush(positionMoveRecord);

        // Get all the positionMoveRecordList
        restPositionMoveRecordMockMvc.perform(get("/api/position-move-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionMoveRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())))
            .andExpect(jsonPath("$.[*].rowsInShelf").value(hasItem(DEFAULT_ROWS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].columnsInShelf").value(hasItem(DEFAULT_COLUMNS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].tubeRows").value(hasItem(DEFAULT_TUBE_ROWS.toString())))
            .andExpect(jsonPath("$.[*].tubeColumns").value(hasItem(DEFAULT_TUBE_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].whetherFreezingAndThawing").value(hasItem(DEFAULT_WHETHER_FREEZING_AND_THAWING.booleanValue())))
            .andExpect(jsonPath("$.[*].moveType").value(hasItem(DEFAULT_MOVE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].projectSiteCode").value(hasItem(DEFAULT_PROJECT_SITE_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getPositionMoveRecord() throws Exception {
        // Initialize the database
        positionMoveRecordRepository.saveAndFlush(positionMoveRecord);

        // Get the positionMoveRecord
        restPositionMoveRecordMockMvc.perform(get("/api/position-move-records/{id}", positionMoveRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(positionMoveRecord.getId().intValue()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()))
            .andExpect(jsonPath("$.rowsInShelf").value(DEFAULT_ROWS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.columnsInShelf").value(DEFAULT_COLUMNS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.tubeRows").value(DEFAULT_TUBE_ROWS.toString()))
            .andExpect(jsonPath("$.tubeColumns").value(DEFAULT_TUBE_COLUMNS.toString()))
            .andExpect(jsonPath("$.whetherFreezingAndThawing").value(DEFAULT_WHETHER_FREEZING_AND_THAWING.booleanValue()))
            .andExpect(jsonPath("$.moveType").value(DEFAULT_MOVE_TYPE.toString()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.projectSiteCode").value(DEFAULT_PROJECT_SITE_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPositionMoveRecord() throws Exception {
        // Get the positionMoveRecord
        restPositionMoveRecordMockMvc.perform(get("/api/position-move-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePositionMoveRecord() throws Exception {
        // Initialize the database
        positionMoveRecordRepository.saveAndFlush(positionMoveRecord);
        int databaseSizeBeforeUpdate = positionMoveRecordRepository.findAll().size();

        // Update the positionMoveRecord
        PositionMoveRecord updatedPositionMoveRecord = positionMoveRecordRepository.findOne(positionMoveRecord.getId());
        updatedPositionMoveRecord
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .tubeRows(UPDATED_TUBE_ROWS)
                .tubeColumns(UPDATED_TUBE_COLUMNS)
                .whetherFreezingAndThawing(UPDATED_WHETHER_FREEZING_AND_THAWING)
                .moveType(UPDATED_MOVE_TYPE)
                .projectCode(UPDATED_PROJECT_CODE)
                .projectSiteCode(UPDATED_PROJECT_SITE_CODE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(updatedPositionMoveRecord);

        restPositionMoveRecordMockMvc.perform(put("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isOk());

        // Validate the PositionMoveRecord in the database
        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeUpdate);
        PositionMoveRecord testPositionMoveRecord = positionMoveRecordList.get(positionMoveRecordList.size() - 1);
        assertThat(testPositionMoveRecord.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testPositionMoveRecord.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testPositionMoveRecord.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testPositionMoveRecord.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testPositionMoveRecord.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testPositionMoveRecord.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testPositionMoveRecord.getTubeRows()).isEqualTo(UPDATED_TUBE_ROWS);
        assertThat(testPositionMoveRecord.getTubeColumns()).isEqualTo(UPDATED_TUBE_COLUMNS);
        assertThat(testPositionMoveRecord.isWhetherFreezingAndThawing()).isEqualTo(UPDATED_WHETHER_FREEZING_AND_THAWING);
        assertThat(testPositionMoveRecord.getMoveType()).isEqualTo(UPDATED_MOVE_TYPE);
        assertThat(testPositionMoveRecord.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testPositionMoveRecord.getProjectSiteCode()).isEqualTo(UPDATED_PROJECT_SITE_CODE);
        assertThat(testPositionMoveRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPositionMoveRecord.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingPositionMoveRecord() throws Exception {
        int databaseSizeBeforeUpdate = positionMoveRecordRepository.findAll().size();

        // Create the PositionMoveRecord
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPositionMoveRecordMockMvc.perform(put("/api/position-move-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionMoveRecord in the database
        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePositionMoveRecord() throws Exception {
        // Initialize the database
        positionMoveRecordRepository.saveAndFlush(positionMoveRecord);
        int databaseSizeBeforeDelete = positionMoveRecordRepository.findAll().size();

        // Get the positionMoveRecord
        restPositionMoveRecordMockMvc.perform(delete("/api/position-move-records/{id}", positionMoveRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PositionMoveRecord> positionMoveRecordList = positionMoveRecordRepository.findAll();
        assertThat(positionMoveRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionMoveRecord.class);
    }
}
