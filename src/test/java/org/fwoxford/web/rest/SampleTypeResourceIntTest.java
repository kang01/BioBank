package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.SampleType;
import org.fwoxford.repository.SampleTypeRepository;
import org.fwoxford.service.SampleTypeService;
import org.fwoxford.service.dto.SampleTypeDTO;
import org.fwoxford.service.mapper.SampleTypeMapper;
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
 * Test class for the SampleTypeResource REST controller.
 *
 * @see SampleTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class SampleTypeResourceIntTest {

    private static final String DEFAULT_SAMPLE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_FRONT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FRONT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BACK_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACK_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_MIXED = 20;
    private static final Integer UPDATED_IS_MIXED = 19;

    @Autowired
    private SampleTypeRepository sampleTypeRepository;

    @Autowired
    private SampleTypeMapper sampleTypeMapper;

    @Autowired
    private SampleTypeService sampleTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSampleTypeMockMvc;

    private SampleType sampleType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SampleTypeResource sampleTypeResource = new SampleTypeResource(sampleTypeService);
        this.restSampleTypeMockMvc = MockMvcBuilders.standaloneSetup(sampleTypeResource)
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
    public static SampleType createEntity(EntityManager em) {
        SampleType sampleType = new SampleType()
                .sampleTypeCode(DEFAULT_SAMPLE_TYPE_CODE)
                .sampleTypeName(DEFAULT_SAMPLE_TYPE_NAME)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .frontColor(DEFAULT_FRONT_COLOR)
                .backColor(DEFAULT_BACK_COLOR)
                .isMixed(DEFAULT_IS_MIXED);
        return sampleType;
    }

    @Before
    public void initTest() {
        sampleType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSampleType() throws Exception {
        int databaseSizeBeforeCreate = sampleTypeRepository.findAll().size();

        // Create the SampleType
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SampleType in the database
        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SampleType testSampleType = sampleTypeList.get(sampleTypeList.size() - 1);
        assertThat(testSampleType.getSampleTypeCode()).isEqualTo(DEFAULT_SAMPLE_TYPE_CODE);
        assertThat(testSampleType.getSampleTypeName()).isEqualTo(DEFAULT_SAMPLE_TYPE_NAME);
        assertThat(testSampleType.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testSampleType.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSampleType.getFrontColor()).isEqualTo(DEFAULT_FRONT_COLOR);
        assertThat(testSampleType.getBackColor()).isEqualTo(DEFAULT_BACK_COLOR);
        assertThat(testSampleType.getIsMixed()).isEqualTo(DEFAULT_IS_MIXED);
    }

    @Test
    @Transactional
    public void createSampleTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sampleTypeRepository.findAll().size();

        // Create the SampleType with an existing ID
        SampleType existingSampleType = new SampleType();
        existingSampleType.setId(1L);
        SampleTypeDTO existingSampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(existingSampleType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSampleTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSampleTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleTypeRepository.findAll().size();
        // set the field null
        sampleType.setSampleTypeCode(null);

        // Create the SampleType, which fails.
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleTypeRepository.findAll().size();
        // set the field null
        sampleType.setSampleTypeName(null);

        // Create the SampleType, which fails.
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleTypeRepository.findAll().size();
        // set the field null
        sampleType.setStatus(null);

        // Create the SampleType, which fails.
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrontColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleTypeRepository.findAll().size();
        // set the field null
        sampleType.setFrontColor(null);

        // Create the SampleType, which fails.
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBackColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleTypeRepository.findAll().size();
        // set the field null
        sampleType.setBackColor(null);

        // Create the SampleType, which fails.
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsMixedIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleTypeRepository.findAll().size();
        // set the field null
        sampleType.setIsMixed(null);

        // Create the SampleType, which fails.
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        restSampleTypeMockMvc.perform(post("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSampleTypes() throws Exception {
        // Initialize the database
        sampleTypeRepository.saveAndFlush(sampleType);

        // Get all the sampleTypeList
        restSampleTypeMockMvc.perform(get("/api/sample-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sampleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleTypeCode").value(hasItem(DEFAULT_SAMPLE_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleTypeName").value(hasItem(DEFAULT_SAMPLE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].frontColor").value(hasItem(DEFAULT_FRONT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].backColor").value(hasItem(DEFAULT_BACK_COLOR.toString())))
            .andExpect(jsonPath("$.[*].isMixed").value(hasItem(DEFAULT_IS_MIXED)));
    }

    @Test
    @Transactional
    public void getSampleType() throws Exception {
        // Initialize the database
        sampleTypeRepository.saveAndFlush(sampleType);

        // Get the sampleType
        restSampleTypeMockMvc.perform(get("/api/sample-types/{id}", sampleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sampleType.getId().intValue()))
            .andExpect(jsonPath("$.sampleTypeCode").value(DEFAULT_SAMPLE_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.sampleTypeName").value(DEFAULT_SAMPLE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.frontColor").value(DEFAULT_FRONT_COLOR.toString()))
            .andExpect(jsonPath("$.backColor").value(DEFAULT_BACK_COLOR.toString()))
            .andExpect(jsonPath("$.isMixed").value(DEFAULT_IS_MIXED));
    }

    @Test
    @Transactional
    public void getNonExistingSampleType() throws Exception {
        // Get the sampleType
        restSampleTypeMockMvc.perform(get("/api/sample-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSampleType() throws Exception {
        // Initialize the database
        sampleTypeRepository.saveAndFlush(sampleType);
        int databaseSizeBeforeUpdate = sampleTypeRepository.findAll().size();

        // Update the sampleType
        SampleType updatedSampleType = sampleTypeRepository.findOne(sampleType.getId());
        updatedSampleType
                .sampleTypeCode(UPDATED_SAMPLE_TYPE_CODE)
                .sampleTypeName(UPDATED_SAMPLE_TYPE_NAME)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .frontColor(UPDATED_FRONT_COLOR)
                .backColor(UPDATED_BACK_COLOR)
                .isMixed(UPDATED_IS_MIXED);
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(updatedSampleType);

        restSampleTypeMockMvc.perform(put("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isOk());

        // Validate the SampleType in the database
        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeUpdate);
        SampleType testSampleType = sampleTypeList.get(sampleTypeList.size() - 1);
        assertThat(testSampleType.getSampleTypeCode()).isEqualTo(UPDATED_SAMPLE_TYPE_CODE);
        assertThat(testSampleType.getSampleTypeName()).isEqualTo(UPDATED_SAMPLE_TYPE_NAME);
        assertThat(testSampleType.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testSampleType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSampleType.getFrontColor()).isEqualTo(UPDATED_FRONT_COLOR);
        assertThat(testSampleType.getBackColor()).isEqualTo(UPDATED_BACK_COLOR);
        assertThat(testSampleType.getIsMixed()).isEqualTo(UPDATED_IS_MIXED);
    }

    @Test
    @Transactional
    public void updateNonExistingSampleType() throws Exception {
        int databaseSizeBeforeUpdate = sampleTypeRepository.findAll().size();

        // Create the SampleType
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSampleTypeMockMvc.perform(put("/api/sample-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SampleType in the database
        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSampleType() throws Exception {
        // Initialize the database
        sampleTypeRepository.saveAndFlush(sampleType);
        int databaseSizeBeforeDelete = sampleTypeRepository.findAll().size();

        // Get the sampleType
        restSampleTypeMockMvc.perform(delete("/api/sample-types/{id}", sampleType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        assertThat(sampleTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleType.class);
    }
}
