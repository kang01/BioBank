package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutHandoverBox;
import org.fwoxford.repository.StockOutHandoverBoxRepository;
import org.fwoxford.service.StockOutHandoverBoxService;
import org.fwoxford.service.dto.StockOutHandoverBoxDTO;
import org.fwoxford.service.mapper.StockOutHandoverBoxMapper;
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
 * Test class for the StockOutHandoverBoxResource REST controller.
 *
 * @see StockOutHandoverBoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutHandoverBoxResourceIntTest {

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
    private StockOutHandoverBoxRepository stockOutHandoverBoxRepository;

    @Autowired
    private StockOutHandoverBoxMapper stockOutHandoverBoxMapper;

    @Autowired
    private StockOutHandoverBoxService stockOutHandoverBoxService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutHandoverBoxMockMvc;

    private StockOutHandoverBox stockOutHandoverBox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutHandoverBoxResource stockOutHandoverBoxResource = new StockOutHandoverBoxResource(stockOutHandoverBoxService);
        this.restStockOutHandoverBoxMockMvc = MockMvcBuilders.standaloneSetup(stockOutHandoverBoxResource)
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
    public static StockOutHandoverBox createEntity(EntityManager em) {
        StockOutHandoverBox stockOutHandoverBox = new StockOutHandoverBox()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return stockOutHandoverBox;
    }

    @Before
    public void initTest() {
        stockOutHandoverBox = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutHandoverBox() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandoverBoxRepository.findAll().size();

        // Create the StockOutHandoverBox
        StockOutHandoverBoxDTO stockOutHandoverBoxDTO = stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(stockOutHandoverBox);

        restStockOutHandoverBoxMockMvc.perform(post("/api/stock-out-handover-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandoverBox in the database
        List<StockOutHandoverBox> stockOutHandoverBoxList = stockOutHandoverBoxRepository.findAll();
        assertThat(stockOutHandoverBoxList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutHandoverBox testStockOutHandoverBox = stockOutHandoverBoxList.get(stockOutHandoverBoxList.size() - 1);
        assertThat(testStockOutHandoverBox.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testStockOutHandoverBox.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testStockOutHandoverBox.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testStockOutHandoverBox.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testStockOutHandoverBox.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testStockOutHandoverBox.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutHandoverBox.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutHandoverBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandoverBoxRepository.findAll().size();

        // Create the StockOutHandoverBox with an existing ID
        StockOutHandoverBox existingStockOutHandoverBox = new StockOutHandoverBox();
        existingStockOutHandoverBox.setId(1L);
        StockOutHandoverBoxDTO existingStockOutHandoverBoxDTO = stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(existingStockOutHandoverBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutHandoverBoxMockMvc.perform(post("/api/stock-out-handover-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutHandoverBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutHandoverBox> stockOutHandoverBoxList = stockOutHandoverBoxRepository.findAll();
        assertThat(stockOutHandoverBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutHandoverBoxRepository.findAll().size();
        // set the field null
        stockOutHandoverBox.setStatus(null);

        // Create the StockOutHandoverBox, which fails.
        StockOutHandoverBoxDTO stockOutHandoverBoxDTO = stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(stockOutHandoverBox);

        restStockOutHandoverBoxMockMvc.perform(post("/api/stock-out-handover-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutHandoverBox> stockOutHandoverBoxList = stockOutHandoverBoxRepository.findAll();
        assertThat(stockOutHandoverBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutHandoverBoxes() throws Exception {
        // Initialize the database
        stockOutHandoverBoxRepository.saveAndFlush(stockOutHandoverBox);

        // Get all the stockOutHandoverBoxList
        restStockOutHandoverBoxMockMvc.perform(get("/api/stock-out-handover-boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutHandoverBox.getId().intValue())))
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
    public void getStockOutHandoverBox() throws Exception {
        // Initialize the database
        stockOutHandoverBoxRepository.saveAndFlush(stockOutHandoverBox);

        // Get the stockOutHandoverBox
        restStockOutHandoverBoxMockMvc.perform(get("/api/stock-out-handover-boxes/{id}", stockOutHandoverBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutHandoverBox.getId().intValue()))
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
    public void getNonExistingStockOutHandoverBox() throws Exception {
        // Get the stockOutHandoverBox
        restStockOutHandoverBoxMockMvc.perform(get("/api/stock-out-handover-boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutHandoverBox() throws Exception {
        // Initialize the database
        stockOutHandoverBoxRepository.saveAndFlush(stockOutHandoverBox);
        int databaseSizeBeforeUpdate = stockOutHandoverBoxRepository.findAll().size();

        // Update the stockOutHandoverBox
        StockOutHandoverBox updatedStockOutHandoverBox = stockOutHandoverBoxRepository.findOne(stockOutHandoverBox.getId());
        updatedStockOutHandoverBox
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutHandoverBoxDTO stockOutHandoverBoxDTO = stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(updatedStockOutHandoverBox);

        restStockOutHandoverBoxMockMvc.perform(put("/api/stock-out-handover-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverBoxDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutHandoverBox in the database
        List<StockOutHandoverBox> stockOutHandoverBoxList = stockOutHandoverBoxRepository.findAll();
        assertThat(stockOutHandoverBoxList).hasSize(databaseSizeBeforeUpdate);
        StockOutHandoverBox testStockOutHandoverBox = stockOutHandoverBoxList.get(stockOutHandoverBoxList.size() - 1);
        assertThat(testStockOutHandoverBox.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testStockOutHandoverBox.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testStockOutHandoverBox.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testStockOutHandoverBox.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testStockOutHandoverBox.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testStockOutHandoverBox.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutHandoverBox.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutHandoverBox() throws Exception {
        int databaseSizeBeforeUpdate = stockOutHandoverBoxRepository.findAll().size();

        // Create the StockOutHandoverBox
        StockOutHandoverBoxDTO stockOutHandoverBoxDTO = stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(stockOutHandoverBox);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutHandoverBoxMockMvc.perform(put("/api/stock-out-handover-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandoverBox in the database
        List<StockOutHandoverBox> stockOutHandoverBoxList = stockOutHandoverBoxRepository.findAll();
        assertThat(stockOutHandoverBoxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutHandoverBox() throws Exception {
        // Initialize the database
        stockOutHandoverBoxRepository.saveAndFlush(stockOutHandoverBox);
        int databaseSizeBeforeDelete = stockOutHandoverBoxRepository.findAll().size();

        // Get the stockOutHandoverBox
        restStockOutHandoverBoxMockMvc.perform(delete("/api/stock-out-handover-boxes/{id}", stockOutHandoverBox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutHandoverBox> stockOutHandoverBoxList = stockOutHandoverBoxRepository.findAll();
        assertThat(stockOutHandoverBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutHandoverBox.class);
    }
}
