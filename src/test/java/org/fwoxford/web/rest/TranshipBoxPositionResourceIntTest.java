package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.TranshipBoxPosition;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.repository.TranshipBoxPositionRepository;
import org.fwoxford.service.TranshipBoxPositionService;
import org.fwoxford.service.dto.TranshipBoxPositionDTO;
import org.fwoxford.service.mapper.TranshipBoxPositionMapper;
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
 * Test class for the TranshipBoxPositionResource REST controller.
 *
 * @see TranshipBoxPositionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class TranshipBoxPositionResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

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

    @Autowired
    private TranshipBoxPositionRepository transhipBoxPositionRepository;

    @Autowired
    private TranshipBoxPositionMapper transhipBoxPositionMapper;

    @Autowired
    private TranshipBoxPositionService transhipBoxPositionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTranshipBoxPositionMockMvc;

    private TranshipBoxPosition transhipBoxPosition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TranshipBoxPositionResource transhipBoxPositionResource = new TranshipBoxPositionResource(transhipBoxPositionService);
        this.restTranshipBoxPositionMockMvc = MockMvcBuilders.standaloneSetup(transhipBoxPositionResource)
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
    public static TranshipBoxPosition createEntity(EntityManager em) {
        TranshipBoxPosition transhipBoxPosition = new TranshipBoxPosition()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF);
        // Add required entity
        TranshipBox transhipBox = TranshipBoxResourceIntTest.createEntity(em);
        em.persist(transhipBox);
        em.flush();
        transhipBoxPosition.setTranshipBox(transhipBox);
        return transhipBoxPosition;
    }

    @Before
    public void initTest() {
        transhipBoxPosition = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranshipBoxPosition() throws Exception {
        int databaseSizeBeforeCreate = transhipBoxPositionRepository.findAll().size();

        // Create the TranshipBoxPosition
        TranshipBoxPositionDTO transhipBoxPositionDTO = transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(transhipBoxPosition);

        restTranshipBoxPositionMockMvc.perform(post("/api/tranship-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipBoxPosition in the database
        List<TranshipBoxPosition> transhipBoxPositionList = transhipBoxPositionRepository.findAll();
        assertThat(transhipBoxPositionList).hasSize(databaseSizeBeforeCreate + 1);
        TranshipBoxPosition testTranshipBoxPosition = transhipBoxPositionList.get(transhipBoxPositionList.size() - 1);
        assertThat(testTranshipBoxPosition.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTranshipBoxPosition.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testTranshipBoxPosition.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testTranshipBoxPosition.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testTranshipBoxPosition.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testTranshipBoxPosition.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testTranshipBoxPosition.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
    }

    @Test
    @Transactional
    public void createTranshipBoxPositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transhipBoxPositionRepository.findAll().size();

        // Create the TranshipBoxPosition with an existing ID
        TranshipBoxPosition existingTranshipBoxPosition = new TranshipBoxPosition();
        existingTranshipBoxPosition.setId(1L);
        TranshipBoxPositionDTO existingTranshipBoxPositionDTO = transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(existingTranshipBoxPosition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranshipBoxPositionMockMvc.perform(post("/api/tranship-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTranshipBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TranshipBoxPosition> transhipBoxPositionList = transhipBoxPositionRepository.findAll();
        assertThat(transhipBoxPositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipBoxPositionRepository.findAll().size();
        // set the field null
        transhipBoxPosition.setStatus(null);

        // Create the TranshipBoxPosition, which fails.
        TranshipBoxPositionDTO transhipBoxPositionDTO = transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(transhipBoxPosition);

        restTranshipBoxPositionMockMvc.perform(post("/api/tranship-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipBoxPosition> transhipBoxPositionList = transhipBoxPositionRepository.findAll();
        assertThat(transhipBoxPositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTranshipBoxPositions() throws Exception {
        // Initialize the database
        transhipBoxPositionRepository.saveAndFlush(transhipBoxPosition);

        // Get all the transhipBoxPositionList
        restTranshipBoxPositionMockMvc.perform(get("/api/tranship-box-positions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transhipBoxPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())))
            .andExpect(jsonPath("$.[*].rowsInShelf").value(hasItem(DEFAULT_ROWS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].columnsInShelf").value(hasItem(DEFAULT_COLUMNS_IN_SHELF.toString())));
    }

    @Test
    @Transactional
    public void getTranshipBoxPosition() throws Exception {
        // Initialize the database
        transhipBoxPositionRepository.saveAndFlush(transhipBoxPosition);

        // Get the transhipBoxPosition
        restTranshipBoxPositionMockMvc.perform(get("/api/tranship-box-positions/{id}", transhipBoxPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transhipBoxPosition.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()))
            .andExpect(jsonPath("$.rowsInShelf").value(DEFAULT_ROWS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.columnsInShelf").value(DEFAULT_COLUMNS_IN_SHELF.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranshipBoxPosition() throws Exception {
        // Get the transhipBoxPosition
        restTranshipBoxPositionMockMvc.perform(get("/api/tranship-box-positions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranshipBoxPosition() throws Exception {
        // Initialize the database
        transhipBoxPositionRepository.saveAndFlush(transhipBoxPosition);
        int databaseSizeBeforeUpdate = transhipBoxPositionRepository.findAll().size();

        // Update the transhipBoxPosition
        TranshipBoxPosition updatedTranshipBoxPosition = transhipBoxPositionRepository.findOne(transhipBoxPosition.getId());
        updatedTranshipBoxPosition
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF);
        TranshipBoxPositionDTO transhipBoxPositionDTO = transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(updatedTranshipBoxPosition);

        restTranshipBoxPositionMockMvc.perform(put("/api/tranship-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxPositionDTO)))
            .andExpect(status().isOk());

        // Validate the TranshipBoxPosition in the database
        List<TranshipBoxPosition> transhipBoxPositionList = transhipBoxPositionRepository.findAll();
        assertThat(transhipBoxPositionList).hasSize(databaseSizeBeforeUpdate);
        TranshipBoxPosition testTranshipBoxPosition = transhipBoxPositionList.get(transhipBoxPositionList.size() - 1);
        assertThat(testTranshipBoxPosition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTranshipBoxPosition.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testTranshipBoxPosition.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testTranshipBoxPosition.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testTranshipBoxPosition.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testTranshipBoxPosition.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testTranshipBoxPosition.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
    }

    @Test
    @Transactional
    public void updateNonExistingTranshipBoxPosition() throws Exception {
        int databaseSizeBeforeUpdate = transhipBoxPositionRepository.findAll().size();

        // Create the TranshipBoxPosition
        TranshipBoxPositionDTO transhipBoxPositionDTO = transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(transhipBoxPosition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTranshipBoxPositionMockMvc.perform(put("/api/tranship-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipBoxPosition in the database
        List<TranshipBoxPosition> transhipBoxPositionList = transhipBoxPositionRepository.findAll();
        assertThat(transhipBoxPositionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTranshipBoxPosition() throws Exception {
        // Initialize the database
        transhipBoxPositionRepository.saveAndFlush(transhipBoxPosition);
        int databaseSizeBeforeDelete = transhipBoxPositionRepository.findAll().size();

        // Get the transhipBoxPosition
        restTranshipBoxPositionMockMvc.perform(delete("/api/tranship-box-positions/{id}", transhipBoxPosition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TranshipBoxPosition> transhipBoxPositionList = transhipBoxPositionRepository.findAll();
        assertThat(transhipBoxPositionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TranshipBoxPosition.class);
    }
}
