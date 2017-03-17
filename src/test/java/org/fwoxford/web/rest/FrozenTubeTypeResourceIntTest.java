package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.FrozenTubeType;
import org.fwoxford.repository.FrozenTubeTypeRepository;
import org.fwoxford.service.FrozenTubeTypeService;
import org.fwoxford.service.dto.FrozenTubeTypeDTO;
import org.fwoxford.service.mapper.FrozenTubeTypeMapper;
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
 * Test class for the FrozenTubeTypeResource REST controller.
 *
 * @see FrozenTubeTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class FrozenTubeTypeResourceIntTest {

    private static final String DEFAULT_FROZEN_TUBE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_TUBE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_TYPE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SAMPLE_USED_TIMES_MOST = 20;
    private static final Integer UPDATED_SAMPLE_USED_TIMES_MOST = 19;

    private static final Integer DEFAULT_FROZEN_TUBE_VOLUMN = 20;
    private static final Integer UPDATED_FROZEN_TUBE_VOLUMN = 19;

    private static final String DEFAULT_FROZEN_TUBE_VOLUMN_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_VOLUMN_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_FRONT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FRONT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACK_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACK_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private FrozenTubeTypeRepository frozenTubeTypeRepository;

    @Autowired
    private FrozenTubeTypeMapper frozenTubeTypeMapper;

    @Autowired
    private FrozenTubeTypeService frozenTubeTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFrozenTubeTypeMockMvc;

    private FrozenTubeType frozenTubeType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrozenTubeTypeResource frozenTubeTypeResource = new FrozenTubeTypeResource(frozenTubeTypeService);
        this.restFrozenTubeTypeMockMvc = MockMvcBuilders.standaloneSetup(frozenTubeTypeResource)
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
    public static FrozenTubeType createEntity(EntityManager em) {
        FrozenTubeType frozenTubeType = new FrozenTubeType()
                .frozenTubeTypeCode(DEFAULT_FROZEN_TUBE_TYPE_CODE)
                .frozenTubeTypeName(DEFAULT_FROZEN_TUBE_TYPE_NAME)
                .sampleUsedTimesMost(DEFAULT_SAMPLE_USED_TIMES_MOST)
                .frozenTubeVolumn(DEFAULT_FROZEN_TUBE_VOLUMN)
                .frozenTubeVolumnUnit(DEFAULT_FROZEN_TUBE_VOLUMN_UNIT)
                .frontColor(DEFAULT_FRONT_COLOR)
                .backColor(DEFAULT_BACK_COLOR)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        return frozenTubeType;
    }

    @Before
    public void initTest() {
        frozenTubeType = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrozenTubeType() throws Exception {
        int databaseSizeBeforeCreate = frozenTubeTypeRepository.findAll().size();

        // Create the FrozenTubeType
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenTubeType in the database
        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FrozenTubeType testFrozenTubeType = frozenTubeTypeList.get(frozenTubeTypeList.size() - 1);
        assertThat(testFrozenTubeType.getFrozenTubeTypeCode()).isEqualTo(DEFAULT_FROZEN_TUBE_TYPE_CODE);
        assertThat(testFrozenTubeType.getFrozenTubeTypeName()).isEqualTo(DEFAULT_FROZEN_TUBE_TYPE_NAME);
        assertThat(testFrozenTubeType.getSampleUsedTimesMost()).isEqualTo(DEFAULT_SAMPLE_USED_TIMES_MOST);
        assertThat(testFrozenTubeType.getFrozenTubeVolumn()).isEqualTo(DEFAULT_FROZEN_TUBE_VOLUMN);
        assertThat(testFrozenTubeType.getFrozenTubeVolumnUnit()).isEqualTo(DEFAULT_FROZEN_TUBE_VOLUMN_UNIT);
        assertThat(testFrozenTubeType.getFrontColor()).isEqualTo(DEFAULT_FRONT_COLOR);
        assertThat(testFrozenTubeType.getBackColor()).isEqualTo(DEFAULT_BACK_COLOR);
        assertThat(testFrozenTubeType.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testFrozenTubeType.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createFrozenTubeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frozenTubeTypeRepository.findAll().size();

        // Create the FrozenTubeType with an existing ID
        FrozenTubeType existingFrozenTubeType = new FrozenTubeType();
        existingFrozenTubeType.setId(1L);
        FrozenTubeTypeDTO existingFrozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(existingFrozenTubeType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFrozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFrozenTubeTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setFrozenTubeTypeCode(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setFrozenTubeTypeName(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleUsedTimesMostIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setSampleUsedTimesMost(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeVolumnIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setFrozenTubeVolumn(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenTubeVolumnUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setFrozenTubeVolumnUnit(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrontColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setFrontColor(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBackColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setBackColor(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenTubeTypeRepository.findAll().size();
        // set the field null
        frozenTubeType.setStatus(null);

        // Create the FrozenTubeType, which fails.
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        restFrozenTubeTypeMockMvc.perform(post("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFrozenTubeTypes() throws Exception {
        // Initialize the database
        frozenTubeTypeRepository.saveAndFlush(frozenTubeType);

        // Get all the frozenTubeTypeList
        restFrozenTubeTypeMockMvc.perform(get("/api/frozen-tube-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frozenTubeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].frozenTubeTypeCode").value(hasItem(DEFAULT_FROZEN_TUBE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenTubeTypeName").value(hasItem(DEFAULT_FROZEN_TUBE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].sampleUsedTimesMost").value(hasItem(DEFAULT_SAMPLE_USED_TIMES_MOST)))
            .andExpect(jsonPath("$.[*].frozenTubeVolumn").value(hasItem(DEFAULT_FROZEN_TUBE_VOLUMN)))
            .andExpect(jsonPath("$.[*].frozenTubeVolumnUnit").value(hasItem(DEFAULT_FROZEN_TUBE_VOLUMN_UNIT.toString())))
            .andExpect(jsonPath("$.[*].frontColor").value(hasItem(DEFAULT_FRONT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].backColor").value(hasItem(DEFAULT_BACK_COLOR.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getFrozenTubeType() throws Exception {
        // Initialize the database
        frozenTubeTypeRepository.saveAndFlush(frozenTubeType);

        // Get the frozenTubeType
        restFrozenTubeTypeMockMvc.perform(get("/api/frozen-tube-types/{id}", frozenTubeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frozenTubeType.getId().intValue()))
            .andExpect(jsonPath("$.frozenTubeTypeCode").value(DEFAULT_FROZEN_TUBE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.frozenTubeTypeName").value(DEFAULT_FROZEN_TUBE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.sampleUsedTimesMost").value(DEFAULT_SAMPLE_USED_TIMES_MOST))
            .andExpect(jsonPath("$.frozenTubeVolumn").value(DEFAULT_FROZEN_TUBE_VOLUMN))
            .andExpect(jsonPath("$.frozenTubeVolumnUnit").value(DEFAULT_FROZEN_TUBE_VOLUMN_UNIT.toString()))
            .andExpect(jsonPath("$.frontColor").value(DEFAULT_FRONT_COLOR.toString()))
            .andExpect(jsonPath("$.backColor").value(DEFAULT_BACK_COLOR.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFrozenTubeType() throws Exception {
        // Get the frozenTubeType
        restFrozenTubeTypeMockMvc.perform(get("/api/frozen-tube-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrozenTubeType() throws Exception {
        // Initialize the database
        frozenTubeTypeRepository.saveAndFlush(frozenTubeType);
        int databaseSizeBeforeUpdate = frozenTubeTypeRepository.findAll().size();

        // Update the frozenTubeType
        FrozenTubeType updatedFrozenTubeType = frozenTubeTypeRepository.findOne(frozenTubeType.getId());
        updatedFrozenTubeType
                .frozenTubeTypeCode(UPDATED_FROZEN_TUBE_TYPE_CODE)
                .frozenTubeTypeName(UPDATED_FROZEN_TUBE_TYPE_NAME)
                .sampleUsedTimesMost(UPDATED_SAMPLE_USED_TIMES_MOST)
                .frozenTubeVolumn(UPDATED_FROZEN_TUBE_VOLUMN)
                .frozenTubeVolumnUnit(UPDATED_FROZEN_TUBE_VOLUMN_UNIT)
                .frontColor(UPDATED_FRONT_COLOR)
                .backColor(UPDATED_BACK_COLOR)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(updatedFrozenTubeType);

        restFrozenTubeTypeMockMvc.perform(put("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isOk());

        // Validate the FrozenTubeType in the database
        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeUpdate);
        FrozenTubeType testFrozenTubeType = frozenTubeTypeList.get(frozenTubeTypeList.size() - 1);
        assertThat(testFrozenTubeType.getFrozenTubeTypeCode()).isEqualTo(UPDATED_FROZEN_TUBE_TYPE_CODE);
        assertThat(testFrozenTubeType.getFrozenTubeTypeName()).isEqualTo(UPDATED_FROZEN_TUBE_TYPE_NAME);
        assertThat(testFrozenTubeType.getSampleUsedTimesMost()).isEqualTo(UPDATED_SAMPLE_USED_TIMES_MOST);
        assertThat(testFrozenTubeType.getFrozenTubeVolumn()).isEqualTo(UPDATED_FROZEN_TUBE_VOLUMN);
        assertThat(testFrozenTubeType.getFrozenTubeVolumnUnit()).isEqualTo(UPDATED_FROZEN_TUBE_VOLUMN_UNIT);
        assertThat(testFrozenTubeType.getFrontColor()).isEqualTo(UPDATED_FRONT_COLOR);
        assertThat(testFrozenTubeType.getBackColor()).isEqualTo(UPDATED_BACK_COLOR);
        assertThat(testFrozenTubeType.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testFrozenTubeType.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingFrozenTubeType() throws Exception {
        int databaseSizeBeforeUpdate = frozenTubeTypeRepository.findAll().size();

        // Create the FrozenTubeType
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFrozenTubeTypeMockMvc.perform(put("/api/frozen-tube-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenTubeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenTubeType in the database
        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFrozenTubeType() throws Exception {
        // Initialize the database
        frozenTubeTypeRepository.saveAndFlush(frozenTubeType);
        int databaseSizeBeforeDelete = frozenTubeTypeRepository.findAll().size();

        // Get the frozenTubeType
        restFrozenTubeTypeMockMvc.perform(delete("/api/frozen-tube-types/{id}", frozenTubeType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FrozenTubeType> frozenTubeTypeList = frozenTubeTypeRepository.findAll();
        assertThat(frozenTubeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrozenTubeType.class);
    }
}
