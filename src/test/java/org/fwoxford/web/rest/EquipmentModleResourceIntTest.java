package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.EquipmentModle;
import org.fwoxford.repository.EquipmentModleRepository;
import org.fwoxford.service.EquipmentModleService;
import org.fwoxford.service.dto.EquipmentModleDTO;
import org.fwoxford.service.mapper.EquipmentModleMapper;
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
 * Test class for the EquipmentModleResource REST controller.
 *
 * @see EquipmentModleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class EquipmentModleResourceIntTest {

    private static final String DEFAULT_EQUIPMENT_MODEL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_MODEL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_MODEL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AREA_NUMBER = 100;
    private static final Integer UPDATED_AREA_NUMBER = 99;

    private static final Integer DEFAULT_SHELVE_NUMBER_IN_AREA = 100;
    private static final Integer UPDATED_SHELVE_NUMBER_IN_AREA = 99;

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TEMPERATURE_MAX = 20;
    private static final Integer UPDATED_TEMPERATURE_MAX = 19;

    private static final Integer DEFAULT_TEMPERATURE_MIN = 20;
    private static final Integer UPDATED_TEMPERATURE_MIN = 19;

    @Autowired
    private EquipmentModleRepository equipmentModleRepository;

    @Autowired
    private EquipmentModleMapper equipmentModleMapper;

    @Autowired
    private EquipmentModleService equipmentModleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEquipmentModleMockMvc;

    private EquipmentModle equipmentModle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EquipmentModleResource equipmentModleResource = new EquipmentModleResource(equipmentModleService);
        this.restEquipmentModleMockMvc = MockMvcBuilders.standaloneSetup(equipmentModleResource)
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
    public static EquipmentModle createEntity(EntityManager em) {
        EquipmentModle equipmentModle = new EquipmentModle()
                .equipmentModelCode(DEFAULT_EQUIPMENT_MODEL_CODE)
                .equipmentModelName(DEFAULT_EQUIPMENT_MODEL_NAME)
                .equipmentType(DEFAULT_EQUIPMENT_TYPE)
                .areaNumber(DEFAULT_AREA_NUMBER)
                .shelveNumberInArea(DEFAULT_SHELVE_NUMBER_IN_AREA)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .temperatureMax(DEFAULT_TEMPERATURE_MAX)
                .temperatureMin(DEFAULT_TEMPERATURE_MIN);
        return equipmentModle;
    }

    @Before
    public void initTest() {
        equipmentModle = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentModle() throws Exception {
        int databaseSizeBeforeCreate = equipmentModleRepository.findAll().size();

        // Create the EquipmentModle
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentModle in the database
        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentModle testEquipmentModle = equipmentModleList.get(equipmentModleList.size() - 1);
        assertThat(testEquipmentModle.getEquipmentModelCode()).isEqualTo(DEFAULT_EQUIPMENT_MODEL_CODE);
        assertThat(testEquipmentModle.getEquipmentModelName()).isEqualTo(DEFAULT_EQUIPMENT_MODEL_NAME);
        assertThat(testEquipmentModle.getEquipmentType()).isEqualTo(DEFAULT_EQUIPMENT_TYPE);
        assertThat(testEquipmentModle.getAreaNumber()).isEqualTo(DEFAULT_AREA_NUMBER);
        assertThat(testEquipmentModle.getShelveNumberInArea()).isEqualTo(DEFAULT_SHELVE_NUMBER_IN_AREA);
        assertThat(testEquipmentModle.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testEquipmentModle.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEquipmentModle.getTemperatureMax()).isEqualTo(DEFAULT_TEMPERATURE_MAX);
        assertThat(testEquipmentModle.getTemperatureMin()).isEqualTo(DEFAULT_TEMPERATURE_MIN);
    }

    @Test
    @Transactional
    public void createEquipmentModleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentModleRepository.findAll().size();

        // Create the EquipmentModle with an existing ID
        EquipmentModle existingEquipmentModle = new EquipmentModle();
        existingEquipmentModle.setId(1L);
        EquipmentModleDTO existingEquipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(existingEquipmentModle);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEquipmentModleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEquipmentModelCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentModleRepository.findAll().size();
        // set the field null
        equipmentModle.setEquipmentModelCode(null);

        // Create the EquipmentModle, which fails.
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentModelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentModleRepository.findAll().size();
        // set the field null
        equipmentModle.setEquipmentModelName(null);

        // Create the EquipmentModle, which fails.
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentModleRepository.findAll().size();
        // set the field null
        equipmentModle.setEquipmentType(null);

        // Create the EquipmentModle, which fails.
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentModleRepository.findAll().size();
        // set the field null
        equipmentModle.setAreaNumber(null);

        // Create the EquipmentModle, which fails.
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShelveNumberInAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentModleRepository.findAll().size();
        // set the field null
        equipmentModle.setShelveNumberInArea(null);

        // Create the EquipmentModle, which fails.
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentModleRepository.findAll().size();
        // set the field null
        equipmentModle.setStatus(null);

        // Create the EquipmentModle, which fails.
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        restEquipmentModleMockMvc.perform(post("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentModles() throws Exception {
        // Initialize the database
        equipmentModleRepository.saveAndFlush(equipmentModle);

        // Get all the equipmentModleList
        restEquipmentModleMockMvc.perform(get("/api/equipment-modles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentModle.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentModelCode").value(hasItem(DEFAULT_EQUIPMENT_MODEL_CODE.toString())))
            .andExpect(jsonPath("$.[*].equipmentModelName").value(hasItem(DEFAULT_EQUIPMENT_MODEL_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].areaNumber").value(hasItem(DEFAULT_AREA_NUMBER)))
            .andExpect(jsonPath("$.[*].shelveNumberInArea").value(hasItem(DEFAULT_SHELVE_NUMBER_IN_AREA)))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].temperatureMax").value(hasItem(DEFAULT_TEMPERATURE_MAX)))
            .andExpect(jsonPath("$.[*].temperatureMin").value(hasItem(DEFAULT_TEMPERATURE_MIN)));
    }

    @Test
    @Transactional
    public void getEquipmentModle() throws Exception {
        // Initialize the database
        equipmentModleRepository.saveAndFlush(equipmentModle);

        // Get the equipmentModle
        restEquipmentModleMockMvc.perform(get("/api/equipment-modles/{id}", equipmentModle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentModle.getId().intValue()))
            .andExpect(jsonPath("$.equipmentModelCode").value(DEFAULT_EQUIPMENT_MODEL_CODE.toString()))
            .andExpect(jsonPath("$.equipmentModelName").value(DEFAULT_EQUIPMENT_MODEL_NAME.toString()))
            .andExpect(jsonPath("$.equipmentType").value(DEFAULT_EQUIPMENT_TYPE.toString()))
            .andExpect(jsonPath("$.areaNumber").value(DEFAULT_AREA_NUMBER))
            .andExpect(jsonPath("$.shelveNumberInArea").value(DEFAULT_SHELVE_NUMBER_IN_AREA))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.temperatureMax").value(DEFAULT_TEMPERATURE_MAX))
            .andExpect(jsonPath("$.temperatureMin").value(DEFAULT_TEMPERATURE_MIN));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentModle() throws Exception {
        // Get the equipmentModle
        restEquipmentModleMockMvc.perform(get("/api/equipment-modles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentModle() throws Exception {
        // Initialize the database
        equipmentModleRepository.saveAndFlush(equipmentModle);
        int databaseSizeBeforeUpdate = equipmentModleRepository.findAll().size();

        // Update the equipmentModle
        EquipmentModle updatedEquipmentModle = equipmentModleRepository.findOne(equipmentModle.getId());
        updatedEquipmentModle
                .equipmentModelCode(UPDATED_EQUIPMENT_MODEL_CODE)
                .equipmentModelName(UPDATED_EQUIPMENT_MODEL_NAME)
                .equipmentType(UPDATED_EQUIPMENT_TYPE)
                .areaNumber(UPDATED_AREA_NUMBER)
                .shelveNumberInArea(UPDATED_SHELVE_NUMBER_IN_AREA)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .temperatureMax(UPDATED_TEMPERATURE_MAX)
                .temperatureMin(UPDATED_TEMPERATURE_MIN);
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(updatedEquipmentModle);

        restEquipmentModleMockMvc.perform(put("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentModle in the database
        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeUpdate);
        EquipmentModle testEquipmentModle = equipmentModleList.get(equipmentModleList.size() - 1);
        assertThat(testEquipmentModle.getEquipmentModelCode()).isEqualTo(UPDATED_EQUIPMENT_MODEL_CODE);
        assertThat(testEquipmentModle.getEquipmentModelName()).isEqualTo(UPDATED_EQUIPMENT_MODEL_NAME);
        assertThat(testEquipmentModle.getEquipmentType()).isEqualTo(UPDATED_EQUIPMENT_TYPE);
        assertThat(testEquipmentModle.getAreaNumber()).isEqualTo(UPDATED_AREA_NUMBER);
        assertThat(testEquipmentModle.getShelveNumberInArea()).isEqualTo(UPDATED_SHELVE_NUMBER_IN_AREA);
        assertThat(testEquipmentModle.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testEquipmentModle.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEquipmentModle.getTemperatureMax()).isEqualTo(UPDATED_TEMPERATURE_MAX);
        assertThat(testEquipmentModle.getTemperatureMin()).isEqualTo(UPDATED_TEMPERATURE_MIN);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentModle() throws Exception {
        int databaseSizeBeforeUpdate = equipmentModleRepository.findAll().size();

        // Create the EquipmentModle
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEquipmentModleMockMvc.perform(put("/api/equipment-modles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentModleDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentModle in the database
        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEquipmentModle() throws Exception {
        // Initialize the database
        equipmentModleRepository.saveAndFlush(equipmentModle);
        int databaseSizeBeforeDelete = equipmentModleRepository.findAll().size();

        // Get the equipmentModle
        restEquipmentModleMockMvc.perform(delete("/api/equipment-modles/{id}", equipmentModle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EquipmentModle> equipmentModleList = equipmentModleRepository.findAll();
        assertThat(equipmentModleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentModle.class);
    }
}
