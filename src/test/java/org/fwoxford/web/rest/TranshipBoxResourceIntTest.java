package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.TranshipBox;
import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.TranshipBoxPosition;
import org.fwoxford.repository.TranshipBoxRepository;
import org.fwoxford.service.TranshipBoxService;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.mapper.TranshipBoxMapper;
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
 * Test class for the TranshipBoxResource REST controller.
 *
 * @see TranshipBoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class TranshipBoxResourceIntTest {

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

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

    @Autowired
    private TranshipBoxRepository transhipBoxRepository;

    @Autowired
    private TranshipBoxMapper transhipBoxMapper;

    @Autowired
    private TranshipBoxService transhipBoxService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTranshipBoxMockMvc;

    private TranshipBox transhipBox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TranshipBoxResource transhipBoxResource = new TranshipBoxResource(transhipBoxService);
        this.restTranshipBoxMockMvc = MockMvcBuilders.standaloneSetup(transhipBoxResource)
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
    public static TranshipBox createEntity(EntityManager em) {
        TranshipBox transhipBox = new TranshipBox()
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        Tranship tranship = TranshipResourceIntTest.createEntity(em);
        em.persist(tranship);
        em.flush();
        transhipBox.setTranship(tranship);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        transhipBox.setFrozenBox(frozenBox);
        // Add required entity
        TranshipBoxPosition transhipBoxPosition = TranshipBoxPositionResourceIntTest.createEntity(em);
        em.persist(transhipBoxPosition);
        em.flush();
        return transhipBox;
    }

    @Before
    public void initTest() {
        transhipBox = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranshipBox() throws Exception {
        int databaseSizeBeforeCreate = transhipBoxRepository.findAll().size();

        // Create the TranshipBox
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipBox in the database
        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeCreate + 1);
        TranshipBox testTranshipBox = transhipBoxList.get(transhipBoxList.size() - 1);
        assertThat(testTranshipBox.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testTranshipBox.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testTranshipBox.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testTranshipBox.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testTranshipBox.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testTranshipBox.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testTranshipBox.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testTranshipBox.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createTranshipBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transhipBoxRepository.findAll().size();

        // Create the TranshipBox with an existing ID
        TranshipBox existingTranshipBox = new TranshipBox();
        existingTranshipBox.setId(1L);
        TranshipBoxDTO existingTranshipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(existingTranshipBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTranshipBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxRepository.findAll().size();
        // set the field null
        transhipBox.setFrozenBoxCode(null);

        // Create the TranshipBox, which fails.
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxRepository.findAll().size();
        // set the field null
        transhipBox.setEquipmentCode(null);

        // Create the TranshipBox, which fails.
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxRepository.findAll().size();
        // set the field null
        transhipBox.setAreaCode(null);

        // Create the TranshipBox, which fails.
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxRepository.findAll().size();
        // set the field null
        transhipBox.setSupportRackCode(null);

        // Create the TranshipBox, which fails.
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRowsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxRepository.findAll().size();
        // set the field null
        transhipBox.setRowsInShelf(null);

        // Create the TranshipBox, which fails.
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColumnsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxRepository.findAll().size();
        // set the field null
        transhipBox.setColumnsInShelf(null);

        // Create the TranshipBox, which fails.
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxRepository.findAll().size();
        // set the field null
        transhipBox.setStatus(null);

        // Create the TranshipBox, which fails.
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        restTranshipBoxMockMvc.perform(post("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTranshipBoxes() throws Exception {
        // Initialize the database
        transhipBoxRepository.saveAndFlush(transhipBox);

        // Get all the transhipBoxList
        restTranshipBoxMockMvc.perform(get("/api/tranship-boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transhipBox.getId().intValue())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())))
            .andExpect(jsonPath("$.[*].rowsInShelf").value(hasItem(DEFAULT_ROWS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].columnsInShelf").value(hasItem(DEFAULT_COLUMNS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTranshipBox() throws Exception {
        // Initialize the database
        transhipBoxRepository.saveAndFlush(transhipBox);

        // Get the transhipBox
        restTranshipBoxMockMvc.perform(get("/api/tranship-boxes/{id}", transhipBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transhipBox.getId().intValue()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()))
            .andExpect(jsonPath("$.rowsInShelf").value(DEFAULT_ROWS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.columnsInShelf").value(DEFAULT_COLUMNS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranshipBox() throws Exception {
        // Get the transhipBox
        restTranshipBoxMockMvc.perform(get("/api/tranship-boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranshipBox() throws Exception {
        // Initialize the database
        transhipBoxRepository.saveAndFlush(transhipBox);
        int databaseSizeBeforeUpdate = transhipBoxRepository.findAll().size();

        // Update the transhipBox
        TranshipBox updatedTranshipBox = transhipBoxRepository.findOne(transhipBox.getId());
        updatedTranshipBox
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(updatedTranshipBox);

        restTranshipBoxMockMvc.perform(put("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isOk());

        // Validate the TranshipBox in the database
        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeUpdate);
        TranshipBox testTranshipBox = transhipBoxList.get(transhipBoxList.size() - 1);
        assertThat(testTranshipBox.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testTranshipBox.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testTranshipBox.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testTranshipBox.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testTranshipBox.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testTranshipBox.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testTranshipBox.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testTranshipBox.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingTranshipBox() throws Exception {
        int databaseSizeBeforeUpdate = transhipBoxRepository.findAll().size();

        // Create the TranshipBox
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTranshipBoxMockMvc.perform(put("/api/tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipBox in the database
        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTranshipBox() throws Exception {
        // Initialize the database
        transhipBoxRepository.saveAndFlush(transhipBox);
        int databaseSizeBeforeDelete = transhipBoxRepository.findAll().size();

        // Get the transhipBox
        restTranshipBoxMockMvc.perform(delete("/api/tranship-boxes/{id}", transhipBox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TranshipBox> transhipBoxList = transhipBoxRepository.findAll();
        assertThat(transhipBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TranshipBox.class);
    }
}
