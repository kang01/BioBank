package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.CheckType;
import org.fwoxford.repository.CheckTypeRepository;
import org.fwoxford.service.CheckTypeService;
import org.fwoxford.service.dto.CheckTypeDTO;
import org.fwoxford.service.mapper.CheckTypeMapper;
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
 * Test class for the CheckTypeResource REST controller.
 *
 * @see CheckTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class CheckTypeResourceIntTest {

    private static final String DEFAULT_CHECK_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CHECK_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private CheckTypeRepository checkTypeRepository;

    @Autowired
    private CheckTypeMapper checkTypeMapper;

    @Autowired
    private CheckTypeService checkTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCheckTypeMockMvc;

    private CheckType checkType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CheckTypeResource checkTypeResource = new CheckTypeResource(checkTypeService);
        this.restCheckTypeMockMvc = MockMvcBuilders.standaloneSetup(checkTypeResource)
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
    public static CheckType createEntity(EntityManager em) {
        CheckType checkType = new CheckType()
                .checkTypeCode(DEFAULT_CHECK_TYPE_CODE)
                .checkTypeName(DEFAULT_CHECK_TYPE_NAME)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return checkType;
    }

    @Before
    public void initTest() {
        checkType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckType() throws Exception {
        int databaseSizeBeforeCreate = checkTypeRepository.findAll().size();

        // Create the CheckType
        CheckTypeDTO checkTypeDTO = checkTypeMapper.checkTypeToCheckTypeDTO(checkType);

        restCheckTypeMockMvc.perform(post("/api/check-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CheckType in the database
        List<CheckType> checkTypeList = checkTypeRepository.findAll();
        assertThat(checkTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CheckType testCheckType = checkTypeList.get(checkTypeList.size() - 1);
        assertThat(testCheckType.getCheckTypeCode()).isEqualTo(DEFAULT_CHECK_TYPE_CODE);
        assertThat(testCheckType.getCheckTypeName()).isEqualTo(DEFAULT_CHECK_TYPE_NAME);
        assertThat(testCheckType.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCheckType.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createCheckTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkTypeRepository.findAll().size();

        // Create the CheckType with an existing ID
        CheckType existingCheckType = new CheckType();
        existingCheckType.setId(1L);
        CheckTypeDTO existingCheckTypeDTO = checkTypeMapper.checkTypeToCheckTypeDTO(existingCheckType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckTypeMockMvc.perform(post("/api/check-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCheckTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CheckType> checkTypeList = checkTypeRepository.findAll();
        assertThat(checkTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCheckTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkTypeRepository.findAll().size();
        // set the field null
        checkType.setCheckTypeCode(null);

        // Create the CheckType, which fails.
        CheckTypeDTO checkTypeDTO = checkTypeMapper.checkTypeToCheckTypeDTO(checkType);

        restCheckTypeMockMvc.perform(post("/api/check-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CheckType> checkTypeList = checkTypeRepository.findAll();
        assertThat(checkTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = checkTypeRepository.findAll().size();
        // set the field null
        checkType.setStatus(null);

        // Create the CheckType, which fails.
        CheckTypeDTO checkTypeDTO = checkTypeMapper.checkTypeToCheckTypeDTO(checkType);

        restCheckTypeMockMvc.perform(post("/api/check-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CheckType> checkTypeList = checkTypeRepository.findAll();
        assertThat(checkTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCheckTypes() throws Exception {
        // Initialize the database
        checkTypeRepository.saveAndFlush(checkType);

        // Get all the checkTypeList
        restCheckTypeMockMvc.perform(get("/api/check-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkType.getId().intValue())))
            .andExpect(jsonPath("$.[*].checkTypeCode").value(hasItem(DEFAULT_CHECK_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].checkTypeName").value(hasItem(DEFAULT_CHECK_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getCheckType() throws Exception {
        // Initialize the database
        checkTypeRepository.saveAndFlush(checkType);

        // Get the checkType
        restCheckTypeMockMvc.perform(get("/api/check-types/{id}", checkType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checkType.getId().intValue()))
            .andExpect(jsonPath("$.checkTypeCode").value(DEFAULT_CHECK_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.checkTypeName").value(DEFAULT_CHECK_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCheckType() throws Exception {
        // Get the checkType
        restCheckTypeMockMvc.perform(get("/api/check-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckType() throws Exception {
        // Initialize the database
        checkTypeRepository.saveAndFlush(checkType);
        int databaseSizeBeforeUpdate = checkTypeRepository.findAll().size();

        // Update the checkType
        CheckType updatedCheckType = checkTypeRepository.findOne(checkType.getId());
        updatedCheckType
                .checkTypeCode(UPDATED_CHECK_TYPE_CODE)
                .checkTypeName(UPDATED_CHECK_TYPE_NAME)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        CheckTypeDTO checkTypeDTO = checkTypeMapper.checkTypeToCheckTypeDTO(updatedCheckType);

        restCheckTypeMockMvc.perform(put("/api/check-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkTypeDTO)))
            .andExpect(status().isOk());

        // Validate the CheckType in the database
        List<CheckType> checkTypeList = checkTypeRepository.findAll();
        assertThat(checkTypeList).hasSize(databaseSizeBeforeUpdate);
        CheckType testCheckType = checkTypeList.get(checkTypeList.size() - 1);
        assertThat(testCheckType.getCheckTypeCode()).isEqualTo(UPDATED_CHECK_TYPE_CODE);
        assertThat(testCheckType.getCheckTypeName()).isEqualTo(UPDATED_CHECK_TYPE_NAME);
        assertThat(testCheckType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCheckType.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingCheckType() throws Exception {
        int databaseSizeBeforeUpdate = checkTypeRepository.findAll().size();

        // Create the CheckType
        CheckTypeDTO checkTypeDTO = checkTypeMapper.checkTypeToCheckTypeDTO(checkType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCheckTypeMockMvc.perform(put("/api/check-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CheckType in the database
        List<CheckType> checkTypeList = checkTypeRepository.findAll();
        assertThat(checkTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCheckType() throws Exception {
        // Initialize the database
        checkTypeRepository.saveAndFlush(checkType);
        int databaseSizeBeforeDelete = checkTypeRepository.findAll().size();

        // Get the checkType
        restCheckTypeMockMvc.perform(delete("/api/check-types/{id}", checkType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CheckType> checkTypeList = checkTypeRepository.findAll();
        assertThat(checkTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CheckType.class);
    }
}
