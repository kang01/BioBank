package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.repository.FrozenBoxTypeRepository;
import org.fwoxford.service.FrozenBoxTypeService;
import org.fwoxford.service.dto.FrozenBoxTypeDTO;
import org.fwoxford.service.mapper.FrozenBoxTypeMapper;
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
 * Test class for the FrozenBoxTypeResource REST controller.
 *
 * @see FrozenBoxTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class FrozenBoxTypeResourceIntTest {

    private static final String DEFAULT_FROZEN_BOX_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_TYPE_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_TYPE_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_TYPE_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_TYPE_COLUMNS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private FrozenBoxTypeRepository frozenBoxTypeRepository;

    @Autowired
    private FrozenBoxTypeMapper frozenBoxTypeMapper;

    @Autowired
    private FrozenBoxTypeService frozenBoxTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFrozenBoxTypeMockMvc;

    private FrozenBoxType frozenBoxType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrozenBoxTypeResource frozenBoxTypeResource = new FrozenBoxTypeResource(frozenBoxTypeService);
        this.restFrozenBoxTypeMockMvc = MockMvcBuilders.standaloneSetup(frozenBoxTypeResource)
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
    public static FrozenBoxType createEntity(EntityManager em) {
        FrozenBoxType frozenBoxType = new FrozenBoxType()
                .frozenBoxTypeCode(DEFAULT_FROZEN_BOX_TYPE_CODE)
                .frozenBoxTypeName(DEFAULT_FROZEN_BOX_TYPE_NAME)
                .frozenBoxTypeRows(DEFAULT_FROZEN_BOX_TYPE_ROWS)
                .frozenBoxTypeColumns(DEFAULT_FROZEN_BOX_TYPE_COLUMNS)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        return frozenBoxType;
    }

    @Before
    public void initTest() {
        frozenBoxType = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrozenBoxType() throws Exception {
        int databaseSizeBeforeCreate = frozenBoxTypeRepository.findAll().size();

        // Create the FrozenBoxType
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);

        restFrozenBoxTypeMockMvc.perform(post("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenBoxType in the database
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FrozenBoxType testFrozenBoxType = frozenBoxTypeList.get(frozenBoxTypeList.size() - 1);
        assertThat(testFrozenBoxType.getFrozenBoxTypeCode()).isEqualTo(DEFAULT_FROZEN_BOX_TYPE_CODE);
        assertThat(testFrozenBoxType.getFrozenBoxTypeName()).isEqualTo(DEFAULT_FROZEN_BOX_TYPE_NAME);
        assertThat(testFrozenBoxType.getFrozenBoxTypeRows()).isEqualTo(DEFAULT_FROZEN_BOX_TYPE_ROWS);
        assertThat(testFrozenBoxType.getFrozenBoxTypeColumns()).isEqualTo(DEFAULT_FROZEN_BOX_TYPE_COLUMNS);
        assertThat(testFrozenBoxType.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testFrozenBoxType.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createFrozenBoxTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frozenBoxTypeRepository.findAll().size();

        // Create the FrozenBoxType with an existing ID
        FrozenBoxType existingFrozenBoxType = new FrozenBoxType();
        existingFrozenBoxType.setId(1L);
        FrozenBoxTypeDTO existingFrozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(existingFrozenBoxType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrozenBoxTypeMockMvc.perform(post("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFrozenBoxTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFrozenBoxTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxTypeRepository.findAll().size();
        // set the field null
        frozenBoxType.setFrozenBoxTypeCode(null);

        // Create the FrozenBoxType, which fails.
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);

        restFrozenBoxTypeMockMvc.perform(post("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxTypeRepository.findAll().size();
        // set the field null
        frozenBoxType.setFrozenBoxTypeName(null);

        // Create the FrozenBoxType, which fails.
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);

        restFrozenBoxTypeMockMvc.perform(post("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxTypeRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxTypeRepository.findAll().size();
        // set the field null
        frozenBoxType.setFrozenBoxTypeRows(null);

        // Create the FrozenBoxType, which fails.
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);

        restFrozenBoxTypeMockMvc.perform(post("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxTypeColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxTypeRepository.findAll().size();
        // set the field null
        frozenBoxType.setFrozenBoxTypeColumns(null);

        // Create the FrozenBoxType, which fails.
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);

        restFrozenBoxTypeMockMvc.perform(post("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFrozenBoxTypes() throws Exception {
        // Initialize the database
        frozenBoxTypeRepository.saveAndFlush(frozenBoxType);

        // Get all the frozenBoxTypeList
        restFrozenBoxTypeMockMvc.perform(get("/api/frozen-box-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frozenBoxType.getId().intValue())))
            .andExpect(jsonPath("$.[*].frozenBoxTypeCode").value(hasItem(DEFAULT_FROZEN_BOX_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxTypeName").value(hasItem(DEFAULT_FROZEN_BOX_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxTypeRows").value(hasItem(DEFAULT_FROZEN_BOX_TYPE_ROWS.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxTypeColumns").value(hasItem(DEFAULT_FROZEN_BOX_TYPE_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getFrozenBoxType() throws Exception {
        // Initialize the database
        frozenBoxTypeRepository.saveAndFlush(frozenBoxType);

        // Get the frozenBoxType
        restFrozenBoxTypeMockMvc.perform(get("/api/frozen-box-types/{id}", frozenBoxType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frozenBoxType.getId().intValue()))
            .andExpect(jsonPath("$.frozenBoxTypeCode").value(DEFAULT_FROZEN_BOX_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.frozenBoxTypeName").value(DEFAULT_FROZEN_BOX_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.frozenBoxTypeRows").value(DEFAULT_FROZEN_BOX_TYPE_ROWS.toString()))
            .andExpect(jsonPath("$.frozenBoxTypeColumns").value(DEFAULT_FROZEN_BOX_TYPE_COLUMNS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFrozenBoxType() throws Exception {
        // Get the frozenBoxType
        restFrozenBoxTypeMockMvc.perform(get("/api/frozen-box-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrozenBoxType() throws Exception {
        // Initialize the database
        frozenBoxTypeRepository.saveAndFlush(frozenBoxType);
        int databaseSizeBeforeUpdate = frozenBoxTypeRepository.findAll().size();

        // Update the frozenBoxType
        FrozenBoxType updatedFrozenBoxType = frozenBoxTypeRepository.findOne(frozenBoxType.getId());
        updatedFrozenBoxType
                .frozenBoxTypeCode(UPDATED_FROZEN_BOX_TYPE_CODE)
                .frozenBoxTypeName(UPDATED_FROZEN_BOX_TYPE_NAME)
                .frozenBoxTypeRows(UPDATED_FROZEN_BOX_TYPE_ROWS)
                .frozenBoxTypeColumns(UPDATED_FROZEN_BOX_TYPE_COLUMNS)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(updatedFrozenBoxType);

        restFrozenBoxTypeMockMvc.perform(put("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxTypeDTO)))
            .andExpect(status().isOk());

        // Validate the FrozenBoxType in the database
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeUpdate);
        FrozenBoxType testFrozenBoxType = frozenBoxTypeList.get(frozenBoxTypeList.size() - 1);
        assertThat(testFrozenBoxType.getFrozenBoxTypeCode()).isEqualTo(UPDATED_FROZEN_BOX_TYPE_CODE);
        assertThat(testFrozenBoxType.getFrozenBoxTypeName()).isEqualTo(UPDATED_FROZEN_BOX_TYPE_NAME);
        assertThat(testFrozenBoxType.getFrozenBoxTypeRows()).isEqualTo(UPDATED_FROZEN_BOX_TYPE_ROWS);
        assertThat(testFrozenBoxType.getFrozenBoxTypeColumns()).isEqualTo(UPDATED_FROZEN_BOX_TYPE_COLUMNS);
        assertThat(testFrozenBoxType.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testFrozenBoxType.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingFrozenBoxType() throws Exception {
        int databaseSizeBeforeUpdate = frozenBoxTypeRepository.findAll().size();

        // Create the FrozenBoxType
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFrozenBoxTypeMockMvc.perform(put("/api/frozen-box-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenBoxType in the database
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFrozenBoxType() throws Exception {
        // Initialize the database
        frozenBoxTypeRepository.saveAndFlush(frozenBoxType);
        int databaseSizeBeforeDelete = frozenBoxTypeRepository.findAll().size();

        // Get the frozenBoxType
        restFrozenBoxTypeMockMvc.perform(delete("/api/frozen-box-types/{id}", frozenBoxType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();
        assertThat(frozenBoxTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrozenBoxType.class);
    }
}
