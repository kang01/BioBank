package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.SupportRackType;
import org.fwoxford.repository.SupportRackTypeRepository;
import org.fwoxford.service.SupportRackTypeService;
import org.fwoxford.service.dto.SupportRackTypeDTO;
import org.fwoxford.service.mapper.SupportRackTypeMapper;
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
 * Test class for the SupportRackTypeResource REST controller.
 *
 * @see SupportRackTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class SupportRackTypeResourceIntTest {

    private static final String DEFAULT_SUPPORT_RACK_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_RACK_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_RACK_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_RACK_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_RACK_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_RACK_COLUMNS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private SupportRackTypeRepository supportRackTypeRepository;

    @Autowired
    private SupportRackTypeMapper supportRackTypeMapper;

    @Autowired
    private SupportRackTypeService supportRackTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSupportRackTypeMockMvc;

    private SupportRackType supportRackType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupportRackTypeResource supportRackTypeResource = new SupportRackTypeResource(supportRackTypeService);
        this.restSupportRackTypeMockMvc = MockMvcBuilders.standaloneSetup(supportRackTypeResource)
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
    public static SupportRackType createEntity(EntityManager em) {
        SupportRackType supportRackType = new SupportRackType()
                .supportRackTypeCode(DEFAULT_SUPPORT_RACK_TYPE_CODE)
                .supportRackRows(DEFAULT_SUPPORT_RACK_ROWS)
                .supportRackColumns(DEFAULT_SUPPORT_RACK_COLUMNS)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        return supportRackType;
    }

    @Before
    public void initTest() {
        supportRackType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupportRackType() throws Exception {
        int databaseSizeBeforeCreate = supportRackTypeRepository.findAll().size();

        // Create the SupportRackType
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);

        restSupportRackTypeMockMvc.perform(post("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SupportRackType in the database
        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SupportRackType testSupportRackType = supportRackTypeList.get(supportRackTypeList.size() - 1);
        assertThat(testSupportRackType.getSupportRackTypeCode()).isEqualTo(DEFAULT_SUPPORT_RACK_TYPE_CODE);
        assertThat(testSupportRackType.getSupportRackRows()).isEqualTo(DEFAULT_SUPPORT_RACK_ROWS);
        assertThat(testSupportRackType.getSupportRackColumns()).isEqualTo(DEFAULT_SUPPORT_RACK_COLUMNS);
        assertThat(testSupportRackType.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testSupportRackType.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createSupportRackTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supportRackTypeRepository.findAll().size();

        // Create the SupportRackType with an existing ID
        SupportRackType existingSupportRackType = new SupportRackType();
        existingSupportRackType.setId(1L);
        SupportRackTypeDTO existingSupportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(existingSupportRackType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupportRackTypeMockMvc.perform(post("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSupportRackTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSupportRackTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackTypeRepository.findAll().size();
        // set the field null
        supportRackType.setSupportRackTypeCode(null);

        // Create the SupportRackType, which fails.
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);

        restSupportRackTypeMockMvc.perform(post("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackTypeRepository.findAll().size();
        // set the field null
        supportRackType.setSupportRackRows(null);

        // Create the SupportRackType, which fails.
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);

        restSupportRackTypeMockMvc.perform(post("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackTypeRepository.findAll().size();
        // set the field null
        supportRackType.setSupportRackColumns(null);

        // Create the SupportRackType, which fails.
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);

        restSupportRackTypeMockMvc.perform(post("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = supportRackTypeRepository.findAll().size();
        // set the field null
        supportRackType.setStatus(null);

        // Create the SupportRackType, which fails.
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);

        restSupportRackTypeMockMvc.perform(post("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupportRackTypes() throws Exception {
        // Initialize the database
        supportRackTypeRepository.saveAndFlush(supportRackType);

        // Get all the supportRackTypeList
        restSupportRackTypeMockMvc.perform(get("/api/support-rack-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportRackType.getId().intValue())))
            .andExpect(jsonPath("$.[*].supportRackTypeCode").value(hasItem(DEFAULT_SUPPORT_RACK_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackRows").value(hasItem(DEFAULT_SUPPORT_RACK_ROWS.toString())))
            .andExpect(jsonPath("$.[*].supportRackColumns").value(hasItem(DEFAULT_SUPPORT_RACK_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSupportRackType() throws Exception {
        // Initialize the database
        supportRackTypeRepository.saveAndFlush(supportRackType);

        // Get the supportRackType
        restSupportRackTypeMockMvc.perform(get("/api/support-rack-types/{id}", supportRackType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supportRackType.getId().intValue()))
            .andExpect(jsonPath("$.supportRackTypeCode").value(DEFAULT_SUPPORT_RACK_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.supportRackRows").value(DEFAULT_SUPPORT_RACK_ROWS.toString()))
            .andExpect(jsonPath("$.supportRackColumns").value(DEFAULT_SUPPORT_RACK_COLUMNS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSupportRackType() throws Exception {
        // Get the supportRackType
        restSupportRackTypeMockMvc.perform(get("/api/support-rack-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupportRackType() throws Exception {
        // Initialize the database
        supportRackTypeRepository.saveAndFlush(supportRackType);
        int databaseSizeBeforeUpdate = supportRackTypeRepository.findAll().size();

        // Update the supportRackType
        SupportRackType updatedSupportRackType = supportRackTypeRepository.findOne(supportRackType.getId());
        updatedSupportRackType
                .supportRackTypeCode(UPDATED_SUPPORT_RACK_TYPE_CODE)
                .supportRackRows(UPDATED_SUPPORT_RACK_ROWS)
                .supportRackColumns(UPDATED_SUPPORT_RACK_COLUMNS)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(updatedSupportRackType);

        restSupportRackTypeMockMvc.perform(put("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackTypeDTO)))
            .andExpect(status().isOk());

        // Validate the SupportRackType in the database
        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeUpdate);
        SupportRackType testSupportRackType = supportRackTypeList.get(supportRackTypeList.size() - 1);
        assertThat(testSupportRackType.getSupportRackTypeCode()).isEqualTo(UPDATED_SUPPORT_RACK_TYPE_CODE);
        assertThat(testSupportRackType.getSupportRackRows()).isEqualTo(UPDATED_SUPPORT_RACK_ROWS);
        assertThat(testSupportRackType.getSupportRackColumns()).isEqualTo(UPDATED_SUPPORT_RACK_COLUMNS);
        assertThat(testSupportRackType.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testSupportRackType.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSupportRackType() throws Exception {
        int databaseSizeBeforeUpdate = supportRackTypeRepository.findAll().size();

        // Create the SupportRackType
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSupportRackTypeMockMvc.perform(put("/api/support-rack-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportRackTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SupportRackType in the database
        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSupportRackType() throws Exception {
        // Initialize the database
        supportRackTypeRepository.saveAndFlush(supportRackType);
        int databaseSizeBeforeDelete = supportRackTypeRepository.findAll().size();

        // Get the supportRackType
        restSupportRackTypeMockMvc.perform(delete("/api/support-rack-types/{id}", supportRackType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SupportRackType> supportRackTypeList = supportRackTypeRepository.findAll();
        assertThat(supportRackTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportRackType.class);
    }
}
