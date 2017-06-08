package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockInBoxPosition;
import org.fwoxford.domain.StockInBox;
import org.fwoxford.repository.StockInBoxPositionRepository;
import org.fwoxford.service.StockInBoxPositionService;
import org.fwoxford.service.dto.StockInBoxPositionDTO;
import org.fwoxford.service.mapper.StockInBoxPositionMapper;
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
 * Test class for the StockInBoxPositionResource REST controller.
 *
 * @see StockInBoxPositionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockInBoxPositionResourceIntTest {

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
    private StockInBoxPositionRepository stockInBoxPositionRepository;

    @Autowired
    private StockInBoxPositionMapper stockInBoxPositionMapper;

    @Autowired
    private StockInBoxPositionService stockInBoxPositionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockInBoxPositionMockMvc;

    private StockInBoxPosition stockInBoxPosition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockInBoxPositionResource stockInBoxPositionResource = new StockInBoxPositionResource(stockInBoxPositionService);
        this.restStockInBoxPositionMockMvc = MockMvcBuilders.standaloneSetup(stockInBoxPositionResource)
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
    public static StockInBoxPosition createEntity(EntityManager em) {
        StockInBoxPosition stockInBoxPosition = new StockInBoxPosition()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockInBox stockInBox = StockInBoxResourceIntTest.createEntity(em);
        em.persist(stockInBox);
        em.flush();
        stockInBoxPosition.setStockInBox(stockInBox);
        return stockInBoxPosition;
    }

    @Before
    public void initTest() {
        stockInBoxPosition = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockInBoxPosition() throws Exception {
        int databaseSizeBeforeCreate = stockInBoxPositionRepository.findAll().size();

        // Create the StockInBoxPosition
        StockInBoxPositionDTO stockInBoxPositionDTO = stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(stockInBoxPosition);

        restStockInBoxPositionMockMvc.perform(post("/api/stock-in-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInBoxPosition in the database
        List<StockInBoxPosition> stockInBoxPositionList = stockInBoxPositionRepository.findAll();
        assertThat(stockInBoxPositionList).hasSize(databaseSizeBeforeCreate + 1);
        StockInBoxPosition testStockInBoxPosition = stockInBoxPositionList.get(stockInBoxPositionList.size() - 1);
        assertThat(testStockInBoxPosition.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testStockInBoxPosition.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testStockInBoxPosition.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testStockInBoxPosition.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testStockInBoxPosition.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testStockInBoxPosition.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockInBoxPosition.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockInBoxPositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockInBoxPositionRepository.findAll().size();

        // Create the StockInBoxPosition with an existing ID
        StockInBoxPosition existingStockInBoxPosition = new StockInBoxPosition();
        existingStockInBoxPosition.setId(1L);
        StockInBoxPositionDTO existingStockInBoxPositionDTO = stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(existingStockInBoxPosition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockInBoxPositionMockMvc.perform(post("/api/stock-in-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockInBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockInBoxPosition> stockInBoxPositionList = stockInBoxPositionRepository.findAll();
        assertThat(stockInBoxPositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxPositionRepository.findAll().size();
        // set the field null
        stockInBoxPosition.setStatus(null);

        // Create the StockInBoxPosition, which fails.
        StockInBoxPositionDTO stockInBoxPositionDTO = stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(stockInBoxPosition);

        restStockInBoxPositionMockMvc.perform(post("/api/stock-in-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxPositionDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBoxPosition> stockInBoxPositionList = stockInBoxPositionRepository.findAll();
        assertThat(stockInBoxPositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockInBoxPositions() throws Exception {
        // Initialize the database
        stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);

        // Get all the stockInBoxPositionList
        restStockInBoxPositionMockMvc.perform(get("/api/stock-in-box-positions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockInBoxPosition.getId().intValue())))
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
    public void getStockInBoxPosition() throws Exception {
        // Initialize the database
        stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);

        // Get the stockInBoxPosition
        restStockInBoxPositionMockMvc.perform(get("/api/stock-in-box-positions/{id}", stockInBoxPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockInBoxPosition.getId().intValue()))
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
    public void getNonExistingStockInBoxPosition() throws Exception {
        // Get the stockInBoxPosition
        restStockInBoxPositionMockMvc.perform(get("/api/stock-in-box-positions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockInBoxPosition() throws Exception {
        // Initialize the database
        stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);
        int databaseSizeBeforeUpdate = stockInBoxPositionRepository.findAll().size();

        // Update the stockInBoxPosition
        StockInBoxPosition updatedStockInBoxPosition = stockInBoxPositionRepository.findOne(stockInBoxPosition.getId());
        updatedStockInBoxPosition
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockInBoxPositionDTO stockInBoxPositionDTO = stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(updatedStockInBoxPosition);

        restStockInBoxPositionMockMvc.perform(put("/api/stock-in-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxPositionDTO)))
            .andExpect(status().isOk());

        // Validate the StockInBoxPosition in the database
        List<StockInBoxPosition> stockInBoxPositionList = stockInBoxPositionRepository.findAll();
        assertThat(stockInBoxPositionList).hasSize(databaseSizeBeforeUpdate);
        StockInBoxPosition testStockInBoxPosition = stockInBoxPositionList.get(stockInBoxPositionList.size() - 1);
        assertThat(testStockInBoxPosition.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testStockInBoxPosition.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testStockInBoxPosition.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testStockInBoxPosition.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testStockInBoxPosition.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testStockInBoxPosition.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockInBoxPosition.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockInBoxPosition() throws Exception {
        int databaseSizeBeforeUpdate = stockInBoxPositionRepository.findAll().size();

        // Create the StockInBoxPosition
        StockInBoxPositionDTO stockInBoxPositionDTO = stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(stockInBoxPosition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockInBoxPositionMockMvc.perform(put("/api/stock-in-box-positions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxPositionDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInBoxPosition in the database
        List<StockInBoxPosition> stockInBoxPositionList = stockInBoxPositionRepository.findAll();
        assertThat(stockInBoxPositionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockInBoxPosition() throws Exception {
        // Initialize the database
        stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);
        int databaseSizeBeforeDelete = stockInBoxPositionRepository.findAll().size();

        // Get the stockInBoxPosition
        restStockInBoxPositionMockMvc.perform(delete("/api/stock-in-box-positions/{id}", stockInBoxPosition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockInBoxPosition> stockInBoxPositionList = stockInBoxPositionRepository.findAll();
        assertThat(stockInBoxPositionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockInBoxPosition.class);
    }
}
