package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.EquipmentGroup;
import org.fwoxford.repository.EquipmentGroupRepository;
import org.fwoxford.service.EquipmentGroupService;
import org.fwoxford.service.dto.EquipmentGroupDTO;
import org.fwoxford.service.mapper.EquipmentGroupMapper;
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
 * Test class for the EquipmentGroupResource REST controller.
 *
 * @see EquipmentGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class EquipmentGroupResourceIntTest {

    private static final String DEFAULT_EQUIPMENT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_GROUP_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_EQUIPMENT_GROUP_MANAGER_ID = 100L;
    private static final Long UPDATED_EQUIPMENT_GROUP_MANAGER_ID = 99L;

    private static final String DEFAULT_EQUIPMENT_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_MANAGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_GROUP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_GROUP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private EquipmentGroupRepository equipmentGroupRepository;

    @Autowired
    private EquipmentGroupMapper equipmentGroupMapper;

    @Autowired
    private EquipmentGroupService equipmentGroupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEquipmentGroupMockMvc;

    private EquipmentGroup equipmentGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EquipmentGroupResource equipmentGroupResource = new EquipmentGroupResource(equipmentGroupService);
        this.restEquipmentGroupMockMvc = MockMvcBuilders.standaloneSetup(equipmentGroupResource)
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
    public static EquipmentGroup createEntity(EntityManager em) {
        EquipmentGroup equipmentGroup = new EquipmentGroup()
                .equipmentGroupName(DEFAULT_EQUIPMENT_GROUP_NAME)
                .equipmentGroupManagerId(DEFAULT_EQUIPMENT_GROUP_MANAGER_ID)
                .equipmentManagerName(DEFAULT_EQUIPMENT_MANAGER_NAME)
                .equipmentGroupAddress(DEFAULT_EQUIPMENT_GROUP_ADDRESS)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return equipmentGroup;
    }

    @Before
    public void initTest() {
        equipmentGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentGroup() throws Exception {
        int databaseSizeBeforeCreate = equipmentGroupRepository.findAll().size();

        // Create the EquipmentGroup
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);

        restEquipmentGroupMockMvc.perform(post("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentGroup in the database
        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentGroup testEquipmentGroup = equipmentGroupList.get(equipmentGroupList.size() - 1);
        assertThat(testEquipmentGroup.getEquipmentGroupName()).isEqualTo(DEFAULT_EQUIPMENT_GROUP_NAME);
        assertThat(testEquipmentGroup.getEquipmentGroupManagerId()).isEqualTo(DEFAULT_EQUIPMENT_GROUP_MANAGER_ID);
        assertThat(testEquipmentGroup.getEquipmentManagerName()).isEqualTo(DEFAULT_EQUIPMENT_MANAGER_NAME);
        assertThat(testEquipmentGroup.getEquipmentGroupAddress()).isEqualTo(DEFAULT_EQUIPMENT_GROUP_ADDRESS);
        assertThat(testEquipmentGroup.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEquipmentGroup.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createEquipmentGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentGroupRepository.findAll().size();

        // Create the EquipmentGroup with an existing ID
        EquipmentGroup existingEquipmentGroup = new EquipmentGroup();
        existingEquipmentGroup.setId(1L);
        EquipmentGroupDTO existingEquipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(existingEquipmentGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentGroupMockMvc.perform(post("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEquipmentGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEquipmentGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentGroupRepository.findAll().size();
        // set the field null
        equipmentGroup.setEquipmentGroupName(null);

        // Create the EquipmentGroup, which fails.
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);

        restEquipmentGroupMockMvc.perform(post("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentGroupManagerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentGroupRepository.findAll().size();
        // set the field null
        equipmentGroup.setEquipmentGroupManagerId(null);

        // Create the EquipmentGroup, which fails.
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);

        restEquipmentGroupMockMvc.perform(post("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentManagerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentGroupRepository.findAll().size();
        // set the field null
        equipmentGroup.setEquipmentManagerName(null);

        // Create the EquipmentGroup, which fails.
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);

        restEquipmentGroupMockMvc.perform(post("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentGroupAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentGroupRepository.findAll().size();
        // set the field null
        equipmentGroup.setEquipmentGroupAddress(null);

        // Create the EquipmentGroup, which fails.
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);

        restEquipmentGroupMockMvc.perform(post("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentGroupRepository.findAll().size();
        // set the field null
        equipmentGroup.setStatus(null);

        // Create the EquipmentGroup, which fails.
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);

        restEquipmentGroupMockMvc.perform(post("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentGroups() throws Exception {
        // Initialize the database
        equipmentGroupRepository.saveAndFlush(equipmentGroup);

        // Get all the equipmentGroupList
        restEquipmentGroupMockMvc.perform(get("/api/equipment-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentGroupName").value(hasItem(DEFAULT_EQUIPMENT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentGroupManagerId").value(hasItem(DEFAULT_EQUIPMENT_GROUP_MANAGER_ID.intValue())))
            .andExpect(jsonPath("$.[*].equipmentManagerName").value(hasItem(DEFAULT_EQUIPMENT_MANAGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentGroupAddress").value(hasItem(DEFAULT_EQUIPMENT_GROUP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getEquipmentGroup() throws Exception {
        // Initialize the database
        equipmentGroupRepository.saveAndFlush(equipmentGroup);

        // Get the equipmentGroup
        restEquipmentGroupMockMvc.perform(get("/api/equipment-groups/{id}", equipmentGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentGroup.getId().intValue()))
            .andExpect(jsonPath("$.equipmentGroupName").value(DEFAULT_EQUIPMENT_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.equipmentGroupManagerId").value(DEFAULT_EQUIPMENT_GROUP_MANAGER_ID.intValue()))
            .andExpect(jsonPath("$.equipmentManagerName").value(DEFAULT_EQUIPMENT_MANAGER_NAME.toString()))
            .andExpect(jsonPath("$.equipmentGroupAddress").value(DEFAULT_EQUIPMENT_GROUP_ADDRESS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentGroup() throws Exception {
        // Get the equipmentGroup
        restEquipmentGroupMockMvc.perform(get("/api/equipment-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentGroup() throws Exception {
        // Initialize the database
        equipmentGroupRepository.saveAndFlush(equipmentGroup);
        int databaseSizeBeforeUpdate = equipmentGroupRepository.findAll().size();

        // Update the equipmentGroup
        EquipmentGroup updatedEquipmentGroup = equipmentGroupRepository.findOne(equipmentGroup.getId());
        updatedEquipmentGroup
                .equipmentGroupName(UPDATED_EQUIPMENT_GROUP_NAME)
                .equipmentGroupManagerId(UPDATED_EQUIPMENT_GROUP_MANAGER_ID)
                .equipmentManagerName(UPDATED_EQUIPMENT_MANAGER_NAME)
                .equipmentGroupAddress(UPDATED_EQUIPMENT_GROUP_ADDRESS)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(updatedEquipmentGroup);

        restEquipmentGroupMockMvc.perform(put("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentGroup in the database
        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeUpdate);
        EquipmentGroup testEquipmentGroup = equipmentGroupList.get(equipmentGroupList.size() - 1);
        assertThat(testEquipmentGroup.getEquipmentGroupName()).isEqualTo(UPDATED_EQUIPMENT_GROUP_NAME);
        assertThat(testEquipmentGroup.getEquipmentGroupManagerId()).isEqualTo(UPDATED_EQUIPMENT_GROUP_MANAGER_ID);
        assertThat(testEquipmentGroup.getEquipmentManagerName()).isEqualTo(UPDATED_EQUIPMENT_MANAGER_NAME);
        assertThat(testEquipmentGroup.getEquipmentGroupAddress()).isEqualTo(UPDATED_EQUIPMENT_GROUP_ADDRESS);
        assertThat(testEquipmentGroup.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEquipmentGroup.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentGroup() throws Exception {
        int databaseSizeBeforeUpdate = equipmentGroupRepository.findAll().size();

        // Create the EquipmentGroup
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEquipmentGroupMockMvc.perform(put("/api/equipment-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentGroup in the database
        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEquipmentGroup() throws Exception {
        // Initialize the database
        equipmentGroupRepository.saveAndFlush(equipmentGroup);
        int databaseSizeBeforeDelete = equipmentGroupRepository.findAll().size();

        // Get the equipmentGroup
        restEquipmentGroupMockMvc.perform(delete("/api/equipment-groups/{id}", equipmentGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EquipmentGroup> equipmentGroupList = equipmentGroupRepository.findAll();
        assertThat(equipmentGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentGroup.class);
    }
}
