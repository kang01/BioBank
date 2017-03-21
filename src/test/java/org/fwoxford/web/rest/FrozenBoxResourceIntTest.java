package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.domain.SampleType;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.ProjectSite;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.mapper.FrozenBoxMapper;
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
 * Test class for the FrozenBoxResource REST controller.
 *
 * @see FrozenBoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class FrozenBoxResourceIntTest {

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_COLUMNS = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_SITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_SITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_RACK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_RACK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SAMPLE_NUMBER = 20;
    private static final Integer UPDATED_SAMPLE_NUMBER = 19;

    private static final String DEFAULT_IS_SPLIT = "AAAAAAAAAA";
    private static final String UPDATED_IS_SPLIT = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private FrozenBoxService frozenBoxService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFrozenBoxMockMvc;

    private FrozenBox frozenBox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrozenBoxResource frozenBoxResource = new FrozenBoxResource(frozenBoxService);
        this.restFrozenBoxMockMvc = MockMvcBuilders.standaloneSetup(frozenBoxResource)
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
    public static FrozenBox createEntity(EntityManager em) {
        FrozenBox frozenBox = new FrozenBox()
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .frozenBoxTypeCode(DEFAULT_FROZEN_BOX_TYPE_CODE)
                .frozenBoxRows(DEFAULT_FROZEN_BOX_ROWS)
                .frozenBoxColumns(DEFAULT_FROZEN_BOX_COLUMNS)
                .projectCode(DEFAULT_PROJECT_CODE)
                .projectName(DEFAULT_PROJECT_NAME)
                .projectSiteCode(DEFAULT_PROJECT_SITE_CODE)
                .projectSiteName(DEFAULT_PROJECT_SITE_NAME)
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .sampleTypeCode(DEFAULT_SAMPLE_TYPE_CODE)
                .sampleTypeName(DEFAULT_SAMPLE_TYPE_NAME)
                .sampleNumber(DEFAULT_SAMPLE_NUMBER)
                .isSplit(DEFAULT_IS_SPLIT)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        FrozenBoxType frozenBoxType = FrozenBoxTypeResourceIntTest.createEntity(em);
        em.persist(frozenBoxType);
        em.flush();
        frozenBox.setFrozenBoxType(frozenBoxType);
        // Add required entity
        SampleType sampleType = SampleTypeResourceIntTest.createEntity(em);
        em.persist(sampleType);
        em.flush();
        frozenBox.setSampleType(sampleType);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        frozenBox.setProject(project);
        // Add required entity
        ProjectSite projectSite = ProjectSiteResourceIntTest.createEntity(em);
        em.persist(projectSite);
        em.flush();
        frozenBox.setProjectSite(projectSite);
        return frozenBox;
    }

    @Before
    public void initTest() {
        frozenBox = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrozenBox() throws Exception {
        int databaseSizeBeforeCreate = frozenBoxRepository.findAll().size();

        // Create the FrozenBox
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenBox in the database
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeCreate + 1);
        FrozenBox testFrozenBox = frozenBoxList.get(frozenBoxList.size() - 1);
        assertThat(testFrozenBox.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testFrozenBox.getFrozenBoxTypeCode()).isEqualTo(DEFAULT_FROZEN_BOX_TYPE_CODE);
        assertThat(testFrozenBox.getFrozenBoxRows()).isEqualTo(DEFAULT_FROZEN_BOX_ROWS);
        assertThat(testFrozenBox.getFrozenBoxColumns()).isEqualTo(DEFAULT_FROZEN_BOX_COLUMNS);
        assertThat(testFrozenBox.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testFrozenBox.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testFrozenBox.getProjectSiteCode()).isEqualTo(DEFAULT_PROJECT_SITE_CODE);
        assertThat(testFrozenBox.getProjectSiteName()).isEqualTo(DEFAULT_PROJECT_SITE_NAME);
        assertThat(testFrozenBox.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testFrozenBox.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testFrozenBox.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testFrozenBox.getSampleTypeCode()).isEqualTo(DEFAULT_SAMPLE_TYPE_CODE);
        assertThat(testFrozenBox.getSampleTypeName()).isEqualTo(DEFAULT_SAMPLE_TYPE_NAME);
        assertThat(testFrozenBox.getSampleNumber()).isEqualTo(DEFAULT_SAMPLE_NUMBER);
        assertThat(testFrozenBox.getIsSplit()).isEqualTo(DEFAULT_IS_SPLIT);
        assertThat(testFrozenBox.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testFrozenBox.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createFrozenBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frozenBoxRepository.findAll().size();

        // Create the FrozenBox with an existing ID
        FrozenBox existingFrozenBox = new FrozenBox();
        existingFrozenBox.setId(1L);
        FrozenBoxDTO existingFrozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(existingFrozenBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFrozenBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setFrozenBoxCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setFrozenBoxTypeCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setFrozenBoxRows(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setFrozenBoxColumns(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setProjectCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setProjectName(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectSiteCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setProjectSiteCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectSiteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setProjectSiteName(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setEquipmentCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setAreaCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setSupportRackCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setSampleTypeCode(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setSampleTypeName(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setSampleNumber(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsSplitIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setIsSplit(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxRepository.findAll().size();
        // set the field null
        frozenBox.setStatus(null);

        // Create the FrozenBox, which fails.
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        restFrozenBoxMockMvc.perform(post("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFrozenBoxes() throws Exception {
        // Initialize the database
        frozenBoxRepository.saveAndFlush(frozenBox);

        // Get all the frozenBoxList
        restFrozenBoxMockMvc.perform(get("/api/frozen-boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frozenBox.getId().intValue())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxTypeCode").value(hasItem(DEFAULT_FROZEN_BOX_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxRows").value(hasItem(DEFAULT_FROZEN_BOX_ROWS.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxColumns").value(hasItem(DEFAULT_FROZEN_BOX_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME.toString())))
            .andExpect(jsonPath("$.[*].projectSiteCode").value(hasItem(DEFAULT_PROJECT_SITE_CODE.toString())))
            .andExpect(jsonPath("$.[*].projectSiteName").value(hasItem(DEFAULT_PROJECT_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTypeCode").value(hasItem(DEFAULT_SAMPLE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTypeName").value(hasItem(DEFAULT_SAMPLE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].sampleNumber").value(hasItem(DEFAULT_SAMPLE_NUMBER)))
            .andExpect(jsonPath("$.[*].isSplit").value(hasItem(DEFAULT_IS_SPLIT.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getFrozenBox() throws Exception {
        // Initialize the database
        frozenBoxRepository.saveAndFlush(frozenBox);

        // Get the frozenBox
        restFrozenBoxMockMvc.perform(get("/api/frozen-boxes/{id}", frozenBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frozenBox.getId().intValue()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.frozenBoxTypeCode").value(DEFAULT_FROZEN_BOX_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.frozenBoxRows").value(DEFAULT_FROZEN_BOX_ROWS.toString()))
            .andExpect(jsonPath("$.frozenBoxColumns").value(DEFAULT_FROZEN_BOX_COLUMNS.toString()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME.toString()))
            .andExpect(jsonPath("$.projectSiteCode").value(DEFAULT_PROJECT_SITE_CODE.toString()))
            .andExpect(jsonPath("$.projectSiteName").value(DEFAULT_PROJECT_SITE_NAME.toString()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()))
            .andExpect(jsonPath("$.sampleTypeCode").value(DEFAULT_SAMPLE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.sampleTypeName").value(DEFAULT_SAMPLE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.sampleNumber").value(DEFAULT_SAMPLE_NUMBER))
            .andExpect(jsonPath("$.isSplit").value(DEFAULT_IS_SPLIT.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFrozenBox() throws Exception {
        // Get the frozenBox
        restFrozenBoxMockMvc.perform(get("/api/frozen-boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrozenBox() throws Exception {
        // Initialize the database
        frozenBoxRepository.saveAndFlush(frozenBox);
        int databaseSizeBeforeUpdate = frozenBoxRepository.findAll().size();

        // Update the frozenBox
        FrozenBox updatedFrozenBox = frozenBoxRepository.findOne(frozenBox.getId());
        updatedFrozenBox
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .frozenBoxTypeCode(UPDATED_FROZEN_BOX_TYPE_CODE)
                .frozenBoxRows(UPDATED_FROZEN_BOX_ROWS)
                .frozenBoxColumns(UPDATED_FROZEN_BOX_COLUMNS)
                .projectCode(UPDATED_PROJECT_CODE)
                .projectName(UPDATED_PROJECT_NAME)
                .projectSiteCode(UPDATED_PROJECT_SITE_CODE)
                .projectSiteName(UPDATED_PROJECT_SITE_NAME)
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .sampleTypeCode(UPDATED_SAMPLE_TYPE_CODE)
                .sampleTypeName(UPDATED_SAMPLE_TYPE_NAME)
                .sampleNumber(UPDATED_SAMPLE_NUMBER)
                .isSplit(UPDATED_IS_SPLIT)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(updatedFrozenBox);

        restFrozenBoxMockMvc.perform(put("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isOk());

        // Validate the FrozenBox in the database
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeUpdate);
        FrozenBox testFrozenBox = frozenBoxList.get(frozenBoxList.size() - 1);
        assertThat(testFrozenBox.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testFrozenBox.getFrozenBoxTypeCode()).isEqualTo(UPDATED_FROZEN_BOX_TYPE_CODE);
        assertThat(testFrozenBox.getFrozenBoxRows()).isEqualTo(UPDATED_FROZEN_BOX_ROWS);
        assertThat(testFrozenBox.getFrozenBoxColumns()).isEqualTo(UPDATED_FROZEN_BOX_COLUMNS);
        assertThat(testFrozenBox.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testFrozenBox.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testFrozenBox.getProjectSiteCode()).isEqualTo(UPDATED_PROJECT_SITE_CODE);
        assertThat(testFrozenBox.getProjectSiteName()).isEqualTo(UPDATED_PROJECT_SITE_NAME);
        assertThat(testFrozenBox.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testFrozenBox.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testFrozenBox.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testFrozenBox.getSampleTypeCode()).isEqualTo(UPDATED_SAMPLE_TYPE_CODE);
        assertThat(testFrozenBox.getSampleTypeName()).isEqualTo(UPDATED_SAMPLE_TYPE_NAME);
        assertThat(testFrozenBox.getSampleNumber()).isEqualTo(UPDATED_SAMPLE_NUMBER);
        assertThat(testFrozenBox.getIsSplit()).isEqualTo(UPDATED_IS_SPLIT);
        assertThat(testFrozenBox.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testFrozenBox.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingFrozenBox() throws Exception {
        int databaseSizeBeforeUpdate = frozenBoxRepository.findAll().size();

        // Create the FrozenBox
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFrozenBoxMockMvc.perform(put("/api/frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenBox in the database
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFrozenBox() throws Exception {
        // Initialize the database
        frozenBoxRepository.saveAndFlush(frozenBox);
        int databaseSizeBeforeDelete = frozenBoxRepository.findAll().size();

        // Get the frozenBox
        restFrozenBoxMockMvc.perform(delete("/api/frozen-boxes/{id}", frozenBox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        assertThat(frozenBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrozenBox.class);
    }
}
