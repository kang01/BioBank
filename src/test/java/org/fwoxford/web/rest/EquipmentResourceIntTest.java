package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Equipment;
import org.fwoxford.domain.EquipmentGroup;
import org.fwoxford.domain.EquipmentModle;
import org.fwoxford.repository.EquipmentRepository;
import org.fwoxford.service.EquipmentService;
import org.fwoxford.service.dto.EquipmentDTO;
import org.fwoxford.service.mapper.EquipmentMapper;
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
 * Test class for the EquipmentResource REST controller.
 *
 * @see EquipmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class EquipmentResourceIntTest {

    private static final String DEFAULT_EQUIPMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TEMPERATURE = 20;
    private static final Integer UPDATED_TEMPERATURE = 19;

    private static final String DEFAULT_EQUIPMENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMPOULES_MAX = 20;
    private static final Integer UPDATED_AMPOULES_MAX = 19;

    private static final Integer DEFAULT_AMPOULES_MIN = 20;
    private static final Integer UPDATED_AMPOULES_MIN = 19;

    private static final String DEFAULT_LABEL_1 = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL_2 = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL_4 = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_4 = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL_3 = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_3 = "BBBBBBBBBB";

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EquipmentResource equipmentResource = new EquipmentResource(equipmentService);
        this.restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
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
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .temperature(DEFAULT_TEMPERATURE)
                .equipmentAddress(DEFAULT_EQUIPMENT_ADDRESS)
                .ampoulesMax(DEFAULT_AMPOULES_MAX)
                .ampoulesMin(DEFAULT_AMPOULES_MIN)
                .label1(DEFAULT_LABEL_1)
                .label2(DEFAULT_LABEL_2)
                .label4(DEFAULT_LABEL_4)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .label3(DEFAULT_LABEL_3);
        // Add required entity
        EquipmentGroup equipmentGroup = EquipmentGroupResourceIntTest.createEntity(em);
        em.persist(equipmentGroup);
        em.flush();
        equipment.setEquipmentGroup(equipmentGroup);
        // Add required entity
        EquipmentModle equipmentModle = EquipmentModleResourceIntTest.createEntity(em);
        em.persist(equipmentModle);
        em.flush();
        equipment.setEquipmentModle(equipmentModle);
        return equipment;
    }

    @Before
    public void initTest() {
        equipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testEquipment.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testEquipment.getEquipmentAddress()).isEqualTo(DEFAULT_EQUIPMENT_ADDRESS);
        assertThat(testEquipment.getAmpoulesMax()).isEqualTo(DEFAULT_AMPOULES_MAX);
        assertThat(testEquipment.getAmpoulesMin()).isEqualTo(DEFAULT_AMPOULES_MIN);
        assertThat(testEquipment.getLabel1()).isEqualTo(DEFAULT_LABEL_1);
        assertThat(testEquipment.getLabel2()).isEqualTo(DEFAULT_LABEL_2);
        assertThat(testEquipment.getLabel4()).isEqualTo(DEFAULT_LABEL_4);
        assertThat(testEquipment.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testEquipment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEquipment.getLabel3()).isEqualTo(DEFAULT_LABEL_3);
    }

    @Test
    @Transactional
    public void createEquipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment with an existing ID
        Equipment existingEquipment = new Equipment();
        existingEquipment.setId(1L);
        EquipmentDTO existingEquipmentDTO = equipmentMapper.equipmentToEquipmentDTO(existingEquipment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEquipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEquipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setEquipmentCode(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTemperatureIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setTemperature(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setEquipmentAddress(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmpoulesMaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setAmpoulesMax(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmpoulesMinIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setAmpoulesMin(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setStatus(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE)))
            .andExpect(jsonPath("$.[*].equipmentAddress").value(hasItem(DEFAULT_EQUIPMENT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].ampoulesMax").value(hasItem(DEFAULT_AMPOULES_MAX)))
            .andExpect(jsonPath("$.[*].ampoulesMin").value(hasItem(DEFAULT_AMPOULES_MIN)))
            .andExpect(jsonPath("$.[*].label1").value(hasItem(DEFAULT_LABEL_1.toString())))
            .andExpect(jsonPath("$.[*].label2").value(hasItem(DEFAULT_LABEL_2.toString())))
            .andExpect(jsonPath("$.[*].label4").value(hasItem(DEFAULT_LABEL_4.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].label3").value(hasItem(DEFAULT_LABEL_3.toString())));
    }

    @Test
    @Transactional
    public void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE))
            .andExpect(jsonPath("$.equipmentAddress").value(DEFAULT_EQUIPMENT_ADDRESS.toString()))
            .andExpect(jsonPath("$.ampoulesMax").value(DEFAULT_AMPOULES_MAX))
            .andExpect(jsonPath("$.ampoulesMin").value(DEFAULT_AMPOULES_MIN))
            .andExpect(jsonPath("$.label1").value(DEFAULT_LABEL_1.toString()))
            .andExpect(jsonPath("$.label2").value(DEFAULT_LABEL_2.toString()))
            .andExpect(jsonPath("$.label4").value(DEFAULT_LABEL_4.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.label3").value(DEFAULT_LABEL_3.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findOne(equipment.getId());
        updatedEquipment
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .temperature(UPDATED_TEMPERATURE)
                .equipmentAddress(UPDATED_EQUIPMENT_ADDRESS)
                .ampoulesMax(UPDATED_AMPOULES_MAX)
                .ampoulesMin(UPDATED_AMPOULES_MIN)
                .label1(UPDATED_LABEL_1)
                .label2(UPDATED_LABEL_2)
                .label4(UPDATED_LABEL_4)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .label3(UPDATED_LABEL_3);
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(updatedEquipment);

        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testEquipment.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testEquipment.getEquipmentAddress()).isEqualTo(UPDATED_EQUIPMENT_ADDRESS);
        assertThat(testEquipment.getAmpoulesMax()).isEqualTo(UPDATED_AMPOULES_MAX);
        assertThat(testEquipment.getAmpoulesMin()).isEqualTo(UPDATED_AMPOULES_MIN);
        assertThat(testEquipment.getLabel1()).isEqualTo(UPDATED_LABEL_1);
        assertThat(testEquipment.getLabel2()).isEqualTo(UPDATED_LABEL_2);
        assertThat(testEquipment.getLabel4()).isEqualTo(UPDATED_LABEL_4);
        assertThat(testEquipment.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testEquipment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEquipment.getLabel3()).isEqualTo(UPDATED_LABEL_3);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Get the equipment
        restEquipmentMockMvc.perform(delete("/api/equipment/{id}", equipment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipment.class);
    }
}
