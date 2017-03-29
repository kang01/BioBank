package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StorageInBox;
import org.fwoxford.domain.StorageIn;
import org.fwoxford.domain.Equipment;
import org.fwoxford.domain.SupportRack;
import org.fwoxford.domain.Area;
import org.fwoxford.repository.StorageInBoxRepository;
import org.fwoxford.service.StorageInBoxService;
import org.fwoxford.service.dto.StorageInBoxDTO;
import org.fwoxford.service.mapper.StorageInBoxMapper;
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
 * Test class for the StorageInBoxResource REST controller.
 *
 * @see StorageInBoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StorageInBoxResourceIntTest {

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

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    @Autowired
    private StorageInBoxRepository storageInBoxRepository;

    @Autowired
    private StorageInBoxMapper storageInBoxMapper;

    @Autowired
    private StorageInBoxService storageInBoxService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStorageInBoxMockMvc;

    private StorageInBox storageInBox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StorageInBoxResource storageInBoxResource = new StorageInBoxResource(storageInBoxService);
        this.restStorageInBoxMockMvc = MockMvcBuilders.standaloneSetup(storageInBoxResource)
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
    public static StorageInBox createEntity(EntityManager em) {
        StorageInBox storageInBox = new StorageInBox()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE);
        // Add required entity
        StorageIn storageIn = StorageInResourceIntTest.createEntity(em);
        em.persist(storageIn);
        em.flush();
        storageInBox.setStorageIn(storageIn);
        // Add required entity
        Equipment equipment = EquipmentResourceIntTest.createEntity(em);
        em.persist(equipment);
        em.flush();
        storageInBox.setEquipment(equipment);
        // Add required entity
        SupportRack supportRack = SupportRackResourceIntTest.createEntity(em);
        em.persist(supportRack);
        em.flush();
        storageInBox.setSupportRack(supportRack);
        // Add required entity
        Area area = AreaResourceIntTest.createEntity(em);
        em.persist(area);
        em.flush();
        storageInBox.setArea(area);
        return storageInBox;
    }

    @Before
    public void initTest() {
        storageInBox = createEntity(em);
    }

    @Test
    @Transactional
    public void createStorageInBox() throws Exception {
        int databaseSizeBeforeCreate = storageInBoxRepository.findAll().size();

        // Create the StorageInBox
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StorageInBox in the database
        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeCreate + 1);
        StorageInBox testStorageInBox = storageInBoxList.get(storageInBoxList.size() - 1);
        assertThat(testStorageInBox.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testStorageInBox.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testStorageInBox.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testStorageInBox.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testStorageInBox.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testStorageInBox.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStorageInBox.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStorageInBox.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
    }

    @Test
    @Transactional
    public void createStorageInBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storageInBoxRepository.findAll().size();

        // Create the StorageInBox with an existing ID
        StorageInBox existingStorageInBox = new StorageInBox();
        existingStorageInBox.setId(1L);
        StorageInBoxDTO existingStorageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(existingStorageInBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStorageInBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEquipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInBoxRepository.findAll().size();
        // set the field null
        storageInBox.setEquipmentCode(null);

        // Create the StorageInBox, which fails.
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInBoxRepository.findAll().size();
        // set the field null
        storageInBox.setAreaCode(null);

        // Create the StorageInBox, which fails.
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInBoxRepository.findAll().size();
        // set the field null
        storageInBox.setSupportRackCode(null);

        // Create the StorageInBox, which fails.
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRowsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInBoxRepository.findAll().size();
        // set the field null
        storageInBox.setRowsInShelf(null);

        // Create the StorageInBox, which fails.
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColumnsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInBoxRepository.findAll().size();
        // set the field null
        storageInBox.setColumnsInShelf(null);

        // Create the StorageInBox, which fails.
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInBoxRepository.findAll().size();
        // set the field null
        storageInBox.setStatus(null);

        // Create the StorageInBox, which fails.
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = storageInBoxRepository.findAll().size();
        // set the field null
        storageInBox.setFrozenBoxCode(null);

        // Create the StorageInBox, which fails.
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        restStorageInBoxMockMvc.perform(post("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStorageInBoxes() throws Exception {
        // Initialize the database
        storageInBoxRepository.saveAndFlush(storageInBox);

        // Get all the storageInBoxList
        restStorageInBoxMockMvc.perform(get("/api/storage-in-boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageInBox.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())))
            .andExpect(jsonPath("$.[*].rowsInShelf").value(hasItem(DEFAULT_ROWS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].columnsInShelf").value(hasItem(DEFAULT_COLUMNS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())));
    }

    @Test
    @Transactional
    public void getStorageInBox() throws Exception {
        // Initialize the database
        storageInBoxRepository.saveAndFlush(storageInBox);

        // Get the storageInBox
        restStorageInBoxMockMvc.perform(get("/api/storage-in-boxes/{id}", storageInBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storageInBox.getId().intValue()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()))
            .andExpect(jsonPath("$.rowsInShelf").value(DEFAULT_ROWS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.columnsInShelf").value(DEFAULT_COLUMNS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStorageInBox() throws Exception {
        // Get the storageInBox
        restStorageInBoxMockMvc.perform(get("/api/storage-in-boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStorageInBox() throws Exception {
        // Initialize the database
        storageInBoxRepository.saveAndFlush(storageInBox);
        int databaseSizeBeforeUpdate = storageInBoxRepository.findAll().size();

        // Update the storageInBox
        StorageInBox updatedStorageInBox = storageInBoxRepository.findOne(storageInBox.getId());
        updatedStorageInBox
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE);
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(updatedStorageInBox);

        restStorageInBoxMockMvc.perform(put("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isOk());

        // Validate the StorageInBox in the database
        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeUpdate);
        StorageInBox testStorageInBox = storageInBoxList.get(storageInBoxList.size() - 1);
        assertThat(testStorageInBox.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testStorageInBox.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testStorageInBox.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testStorageInBox.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testStorageInBox.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testStorageInBox.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStorageInBox.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStorageInBox.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingStorageInBox() throws Exception {
        int databaseSizeBeforeUpdate = storageInBoxRepository.findAll().size();

        // Create the StorageInBox
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStorageInBoxMockMvc.perform(put("/api/storage-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storageInBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StorageInBox in the database
        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStorageInBox() throws Exception {
        // Initialize the database
        storageInBoxRepository.saveAndFlush(storageInBox);
        int databaseSizeBeforeDelete = storageInBoxRepository.findAll().size();

        // Get the storageInBox
        restStorageInBoxMockMvc.perform(delete("/api/storage-in-boxes/{id}", storageInBox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StorageInBox> storageInBoxList = storageInBoxRepository.findAll();
        assertThat(storageInBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageInBox.class);
    }
}
