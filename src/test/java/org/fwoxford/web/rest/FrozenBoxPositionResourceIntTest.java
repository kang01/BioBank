package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.FrozenBoxPosition;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.repository.FrozenBoxPositionRepository;
import org.fwoxford.service.FrozenBoxPositionService;
import org.fwoxford.service.dto.FrozenBoxPositionDTO;
import org.fwoxford.service.mapper.FrozenBoxPositionMapper;
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
 * Test class for the FrozenBoxPositionResource REST controller.
 *
 * @see FrozenBoxPositionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class FrozenBoxPositionResourceIntTest {

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

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private FrozenBoxPositionRepository frozenBoxPositionRepository;

    @Autowired
    private FrozenBoxPositionMapper frozenBoxPositionMapper;

    @Autowired
    private FrozenBoxPositionService frozenBoxPositionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFrozenBoxPositionMockMvc;

    private FrozenBoxPosition frozenBoxPosition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FrozenBoxPositionResource frozenBoxPositionResource = new FrozenBoxPositionResource(frozenBoxPositionService);
        this.restFrozenBoxPositionMockMvc = MockMvcBuilders.standaloneSetup(frozenBoxPositionResource)
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
    public static FrozenBoxPosition createEntity(EntityManager em) {
        FrozenBoxPosition frozenBoxPosition = new FrozenBoxPosition()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        frozenBoxPosition.setFrozenBox(frozenBox);
        return frozenBoxPosition;
    }

    @Before
    public void initTest() {
        frozenBoxPosition = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrozenBoxPosition() throws Exception {
        int databaseSizeBeforeCreate = frozenBoxPositionRepository.findAll().size();

        // Create the FrozenBoxPosition
        FrozenBoxPositionDTO frozenBoxPositionDTO = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(frozenBoxPosition);

        restFrozenBoxPositionMockMvc.perform(post("/api/frozen-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenBoxPosition in the database
        List<FrozenBoxPosition> frozenBoxPositionList = frozenBoxPositionRepository.findAll();
        assertThat(frozenBoxPositionList).hasSize(databaseSizeBeforeCreate + 1);
        FrozenBoxPosition testFrozenBoxPosition = frozenBoxPositionList.get(frozenBoxPositionList.size() - 1);
        assertThat(testFrozenBoxPosition.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testFrozenBoxPosition.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testFrozenBoxPosition.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testFrozenBoxPosition.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testFrozenBoxPosition.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testFrozenBoxPosition.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testFrozenBoxPosition.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testFrozenBoxPosition.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createFrozenBoxPositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frozenBoxPositionRepository.findAll().size();

        // Create the FrozenBoxPosition with an existing ID
        FrozenBoxPosition existingFrozenBoxPosition = new FrozenBoxPosition();
        existingFrozenBoxPosition.setId(1L);
        FrozenBoxPositionDTO existingFrozenBoxPositionDTO = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(existingFrozenBoxPosition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrozenBoxPositionMockMvc.perform(post("/api/frozen-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFrozenBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FrozenBoxPosition> frozenBoxPositionList = frozenBoxPositionRepository.findAll();
        assertThat(frozenBoxPositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxPositionRepository.findAll().size();
        // set the field null
        frozenBoxPosition.setFrozenBoxCode(null);

        // Create the FrozenBoxPosition, which fails.
        FrozenBoxPositionDTO frozenBoxPositionDTO = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(frozenBoxPosition);

        restFrozenBoxPositionMockMvc.perform(post("/api/frozen-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBoxPosition> frozenBoxPositionList = frozenBoxPositionRepository.findAll();
        assertThat(frozenBoxPositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = frozenBoxPositionRepository.findAll().size();
        // set the field null
        frozenBoxPosition.setStatus(null);

        // Create the FrozenBoxPosition, which fails.
        FrozenBoxPositionDTO frozenBoxPositionDTO = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(frozenBoxPosition);

        restFrozenBoxPositionMockMvc.perform(post("/api/frozen-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        List<FrozenBoxPosition> frozenBoxPositionList = frozenBoxPositionRepository.findAll();
        assertThat(frozenBoxPositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFrozenBoxPositions() throws Exception {
        // Initialize the database
        frozenBoxPositionRepository.saveAndFlush(frozenBoxPosition);

        // Get all the frozenBoxPositionList
        restFrozenBoxPositionMockMvc.perform(get("/api/frozen-box-positions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frozenBoxPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())))
            .andExpect(jsonPath("$.[*].rowsInShelf").value(hasItem(DEFAULT_ROWS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].columnsInShelf").value(hasItem(DEFAULT_COLUMNS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getFrozenBoxPosition() throws Exception {
        // Initialize the database
        frozenBoxPositionRepository.saveAndFlush(frozenBoxPosition);

        // Get the frozenBoxPosition
        restFrozenBoxPositionMockMvc.perform(get("/api/frozen-box-positions/{id}", frozenBoxPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frozenBoxPosition.getId().intValue()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()))
            .andExpect(jsonPath("$.rowsInShelf").value(DEFAULT_ROWS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.columnsInShelf").value(DEFAULT_COLUMNS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFrozenBoxPosition() throws Exception {
        // Get the frozenBoxPosition
        restFrozenBoxPositionMockMvc.perform(get("/api/frozen-box-positions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrozenBoxPosition() throws Exception {
        // Initialize the database
        frozenBoxPositionRepository.saveAndFlush(frozenBoxPosition);
        int databaseSizeBeforeUpdate = frozenBoxPositionRepository.findAll().size();

        // Update the frozenBoxPosition
        FrozenBoxPosition updatedFrozenBoxPosition = frozenBoxPositionRepository.findOne(frozenBoxPosition.getId());
        updatedFrozenBoxPosition
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        FrozenBoxPositionDTO frozenBoxPositionDTO = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(updatedFrozenBoxPosition);

        restFrozenBoxPositionMockMvc.perform(put("/api/frozen-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxPositionDTO)))
            .andExpect(status().isOk());

        // Validate the FrozenBoxPosition in the database
        List<FrozenBoxPosition> frozenBoxPositionList = frozenBoxPositionRepository.findAll();
        assertThat(frozenBoxPositionList).hasSize(databaseSizeBeforeUpdate);
        FrozenBoxPosition testFrozenBoxPosition = frozenBoxPositionList.get(frozenBoxPositionList.size() - 1);
        assertThat(testFrozenBoxPosition.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testFrozenBoxPosition.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testFrozenBoxPosition.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testFrozenBoxPosition.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testFrozenBoxPosition.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testFrozenBoxPosition.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testFrozenBoxPosition.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testFrozenBoxPosition.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingFrozenBoxPosition() throws Exception {
        int databaseSizeBeforeUpdate = frozenBoxPositionRepository.findAll().size();

        // Create the FrozenBoxPosition
        FrozenBoxPositionDTO frozenBoxPositionDTO = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(frozenBoxPosition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFrozenBoxPositionMockMvc.perform(put("/api/frozen-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frozenBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the FrozenBoxPosition in the database
        List<FrozenBoxPosition> frozenBoxPositionList = frozenBoxPositionRepository.findAll();
        assertThat(frozenBoxPositionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFrozenBoxPosition() throws Exception {
        // Initialize the database
        frozenBoxPositionRepository.saveAndFlush(frozenBoxPosition);
        int databaseSizeBeforeDelete = frozenBoxPositionRepository.findAll().size();

        // Get the frozenBoxPosition
        restFrozenBoxPositionMockMvc.perform(delete("/api/frozen-box-positions/{id}", frozenBoxPosition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FrozenBoxPosition> frozenBoxPositionList = frozenBoxPositionRepository.findAll();
        assertThat(frozenBoxPositionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrozenBoxPosition.class);
    }
}
