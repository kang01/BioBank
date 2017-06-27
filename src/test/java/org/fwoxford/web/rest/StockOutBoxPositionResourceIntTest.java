package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutBoxPosition;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.repository.StockOutBoxPositionRepository;
import org.fwoxford.service.StockOutBoxPositionService;
import org.fwoxford.service.dto.StockOutBoxPositionDTO;
import org.fwoxford.service.mapper.StockOutBoxPositionMapper;
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
 * Test class for the StockOutBoxPositionResource REST controller.
 *
 * @see StockOutBoxPositionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutBoxPositionResourceIntTest {

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

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutBoxPositionRepository stockOutBoxPositionRepository;

    @Autowired
    private StockOutBoxPositionMapper stockOutBoxPositionMapper;

    @Autowired
    private StockOutBoxPositionService stockOutBoxPositionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutBoxPositionMockMvc;

    private StockOutBoxPosition stockOutBoxPosition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutBoxPositionResource stockOutBoxPositionResource = new StockOutBoxPositionResource(stockOutBoxPositionService);
        this.restStockOutBoxPositionMockMvc = MockMvcBuilders.standaloneSetup(stockOutBoxPositionResource)
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
    public static StockOutBoxPosition createEntity(EntityManager em) {
        StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutFrozenBox stockOutFrozenBox = StockOutFrozenBoxResourceIntTest.createEntity(em);
        em.persist(stockOutFrozenBox);
        em.flush();
        stockOutBoxPosition.stockOutFrozenBox(stockOutFrozenBox);
        return stockOutBoxPosition;
    }

    @Before
    public void initTest() {
        stockOutBoxPosition = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutBoxPosition() throws Exception {
        int databaseSizeBeforeCreate = stockOutBoxPositionRepository.findAll().size();

        // Create the StockOutBoxPosition
        StockOutBoxPositionDTO stockOutBoxPositionDTO = stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(stockOutBoxPosition);

        restStockOutBoxPositionMockMvc.perform(post("/api/stock-out-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutBoxPosition in the database
        List<StockOutBoxPosition> stockOutBoxPositionList = stockOutBoxPositionRepository.findAll();
        assertThat(stockOutBoxPositionList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutBoxPosition testStockOutBoxPosition = stockOutBoxPositionList.get(stockOutBoxPositionList.size() - 1);
        assertThat(testStockOutBoxPosition.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testStockOutBoxPosition.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testStockOutBoxPosition.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testStockOutBoxPosition.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testStockOutBoxPosition.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testStockOutBoxPosition.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutBoxPosition.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutBoxPositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutBoxPositionRepository.findAll().size();

        // Create the StockOutBoxPosition with an existing ID
        StockOutBoxPosition existingStockOutBoxPosition = new StockOutBoxPosition();
        existingStockOutBoxPosition.setId(1L);
        StockOutBoxPositionDTO existingStockOutBoxPositionDTO = stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(existingStockOutBoxPosition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutBoxPositionMockMvc.perform(post("/api/stock-out-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutBoxPosition> stockOutBoxPositionList = stockOutBoxPositionRepository.findAll();
        assertThat(stockOutBoxPositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutBoxPositionRepository.findAll().size();
        // set the field null
        stockOutBoxPosition.setStatus(null);

        // Create the StockOutBoxPosition, which fails.
        StockOutBoxPositionDTO stockOutBoxPositionDTO = stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(stockOutBoxPosition);

        restStockOutBoxPositionMockMvc.perform(post("/api/stock-out-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutBoxPosition> stockOutBoxPositionList = stockOutBoxPositionRepository.findAll();
        assertThat(stockOutBoxPositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutBoxPositions() throws Exception {
        // Initialize the database
        stockOutBoxPositionRepository.saveAndFlush(stockOutBoxPosition);

        // Get all the stockOutBoxPositionList
        restStockOutBoxPositionMockMvc.perform(get("/api/stock-out-box-positions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutBoxPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].supportRackCode").value(hasItem(DEFAULT_SUPPORT_RACK_CODE.toString())))
            .andExpect(jsonPath("$.[*].rowsInShelf").value(hasItem(DEFAULT_ROWS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].columnsInShelf").value(hasItem(DEFAULT_COLUMNS_IN_SHELF.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutBoxPosition() throws Exception {
        // Initialize the database
        stockOutBoxPositionRepository.saveAndFlush(stockOutBoxPosition);

        // Get the stockOutBoxPosition
        restStockOutBoxPositionMockMvc.perform(get("/api/stock-out-box-positions/{id}", stockOutBoxPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutBoxPosition.getId().intValue()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.supportRackCode").value(DEFAULT_SUPPORT_RACK_CODE.toString()))
            .andExpect(jsonPath("$.rowsInShelf").value(DEFAULT_ROWS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.columnsInShelf").value(DEFAULT_COLUMNS_IN_SHELF.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutBoxPosition() throws Exception {
        // Get the stockOutBoxPosition
        restStockOutBoxPositionMockMvc.perform(get("/api/stock-out-box-positions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutBoxPosition() throws Exception {
        // Initialize the database
        stockOutBoxPositionRepository.saveAndFlush(stockOutBoxPosition);
        int databaseSizeBeforeUpdate = stockOutBoxPositionRepository.findAll().size();

        // Update the stockOutBoxPosition
        StockOutBoxPosition updatedStockOutBoxPosition = stockOutBoxPositionRepository.findOne(stockOutBoxPosition.getId());
        updatedStockOutBoxPosition
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutBoxPositionDTO stockOutBoxPositionDTO = stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(updatedStockOutBoxPosition);

        restStockOutBoxPositionMockMvc.perform(put("/api/stock-out-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxPositionDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutBoxPosition in the database
        List<StockOutBoxPosition> stockOutBoxPositionList = stockOutBoxPositionRepository.findAll();
        assertThat(stockOutBoxPositionList).hasSize(databaseSizeBeforeUpdate);
        StockOutBoxPosition testStockOutBoxPosition = stockOutBoxPositionList.get(stockOutBoxPositionList.size() - 1);
        assertThat(testStockOutBoxPosition.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testStockOutBoxPosition.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testStockOutBoxPosition.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testStockOutBoxPosition.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testStockOutBoxPosition.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testStockOutBoxPosition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutBoxPosition.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutBoxPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockOutBoxPositionRepository.findAll().size();

        // Create the StockOutBoxPosition
        StockOutBoxPositionDTO stockOutBoxPositionDTO = stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(stockOutBoxPosition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutBoxPositionMockMvc.perform(put("/api/stock-out-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutBoxPosition in the database
        List<StockOutBoxPosition> stockOutBoxPositionList = stockOutBoxPositionRepository.findAll();
        assertThat(stockOutBoxPositionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutBoxPosition() throws Exception {
        // Initialize the database
        stockOutBoxPositionRepository.saveAndFlush(stockOutBoxPosition);
        int databaseSizeBeforeDelete = stockOutBoxPositionRepository.findAll().size();

        // Get the stockOutBoxPosition
        restStockOutBoxPositionMockMvc.perform(delete("/api/stock-out-box-positions/{id}", stockOutBoxPosition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutBoxPosition> stockOutBoxPositionList = stockOutBoxPositionRepository.findAll();
        assertThat(stockOutBoxPositionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutBoxPosition.class);
    }
}
