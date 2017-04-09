package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.*;
import org.fwoxford.domain.StockIn;
import org.fwoxford.repository.StockInBoxRepository;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.mapper.StockInBoxMapper;
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
 * Test class for the StockInBoxResource REST controller.
 *
 * @see StockInBoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockInBoxResourceIntTest {

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
    private StockInBoxRepository stockInBoxRepository;

    @Autowired
    private StockInBoxMapper stockInBoxMapper;

    @Autowired
    private StockInBoxService stockInBoxService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockInBoxMockMvc;

    private StockInBox stockInBox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockInBoxResource stockInBoxResource = new StockInBoxResource(stockInBoxService);
        this.restStockInBoxMockMvc = MockMvcBuilders.standaloneSetup(stockInBoxResource)
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
    public static StockInBox createEntity(EntityManager em) {
        StockInBox stockInBox = new StockInBox()
                .equipmentCode(DEFAULT_EQUIPMENT_CODE)
                .areaCode(DEFAULT_AREA_CODE)
                .supportRackCode(DEFAULT_SUPPORT_RACK_CODE)
                .rowsInShelf(DEFAULT_ROWS_IN_SHELF)
                .columnsInShelf(DEFAULT_COLUMNS_IN_SHELF)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE);
        // Add required entity
        StockIn stockIn = StockInResourceIntTest.createEntity(em);
        em.persist(stockIn);
        em.flush();
        stockInBox.setStockIn(stockIn);
        // Add required entity
        Equipment equipment = EquipmentResourceIntTest.createEntity(em);
        em.persist(equipment);
        em.flush();
        stockInBox.setEquipment(equipment);
        // Add required entity
        SupportRack supportRack = SupportRackResourceIntTest.createEntity(em);
        em.persist(supportRack);
        em.flush();
        stockInBox.setSupportRack(supportRack);
        // Add required entity
        Area area = AreaResourceIntTest.createEntity(em);
        em.persist(area);
        em.flush();
        stockInBox.setArea(area);
        return stockInBox;
    }

    @Before
    public void initTest() {
        stockInBox = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockInBox() throws Exception {
        int databaseSizeBeforeCreate = stockInBoxRepository.findAll().size();

        // Create the StockInBox
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInBox in the database
        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeCreate + 1);
        StockInBox testStockInBox = stockInBoxList.get(stockInBoxList.size() - 1);
        assertThat(testStockInBox.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
        assertThat(testStockInBox.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testStockInBox.getSupportRackCode()).isEqualTo(DEFAULT_SUPPORT_RACK_CODE);
        assertThat(testStockInBox.getRowsInShelf()).isEqualTo(DEFAULT_ROWS_IN_SHELF);
        assertThat(testStockInBox.getColumnsInShelf()).isEqualTo(DEFAULT_COLUMNS_IN_SHELF);
        assertThat(testStockInBox.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockInBox.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockInBox.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
    }

    @Test
    @Transactional
    public void createStockInBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockInBoxRepository.findAll().size();

        // Create the StockInBox with an existing ID
        StockInBox existingStockInBox = new StockInBox();
        existingStockInBox.setId(1L);
        StockInBoxDTO existingStockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(existingStockInBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockInBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEquipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxRepository.findAll().size();
        // set the field null
        stockInBox.setEquipmentCode(null);

        // Create the StockInBox, which fails.
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxRepository.findAll().size();
        // set the field null
        stockInBox.setAreaCode(null);

        // Create the StockInBox, which fails.
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupportRackCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxRepository.findAll().size();
        // set the field null
        stockInBox.setSupportRackCode(null);

        // Create the StockInBox, which fails.
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRowsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxRepository.findAll().size();
        // set the field null
        stockInBox.setRowsInShelf(null);

        // Create the StockInBox, which fails.
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColumnsInShelfIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxRepository.findAll().size();
        // set the field null
        stockInBox.setColumnsInShelf(null);

        // Create the StockInBox, which fails.
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxRepository.findAll().size();
        // set the field null
        stockInBox.setStatus(null);

        // Create the StockInBox, which fails.
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInBoxRepository.findAll().size();
        // set the field null
        stockInBox.setFrozenBoxCode(null);

        // Create the StockInBox, which fails.
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        restStockInBoxMockMvc.perform(post("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockInBoxes() throws Exception {
        // Initialize the database
        stockInBoxRepository.saveAndFlush(stockInBox);

        // Get all the stockInBoxList
        restStockInBoxMockMvc.perform(get("/api/stock-in-boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockInBox.getId().intValue())))
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
    public void getStockInBox() throws Exception {
        // Initialize the database
        stockInBoxRepository.saveAndFlush(stockInBox);

        // Get the stockInBox
        restStockInBoxMockMvc.perform(get("/api/stock-in-boxes/{id}", stockInBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockInBox.getId().intValue()))
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
    public void getNonExistingStockInBox() throws Exception {
        // Get the stockInBox
        restStockInBoxMockMvc.perform(get("/api/stock-in-boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockInBox() throws Exception {
        // Initialize the database
        stockInBoxRepository.saveAndFlush(stockInBox);
        int databaseSizeBeforeUpdate = stockInBoxRepository.findAll().size();

        // Update the stockInBox
        StockInBox updatedStockInBox = stockInBoxRepository.findOne(stockInBox.getId());
        updatedStockInBox
                .equipmentCode(UPDATED_EQUIPMENT_CODE)
                .areaCode(UPDATED_AREA_CODE)
                .supportRackCode(UPDATED_SUPPORT_RACK_CODE)
                .rowsInShelf(UPDATED_ROWS_IN_SHELF)
                .columnsInShelf(UPDATED_COLUMNS_IN_SHELF)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE);
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(updatedStockInBox);

        restStockInBoxMockMvc.perform(put("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isOk());

        // Validate the StockInBox in the database
        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeUpdate);
        StockInBox testStockInBox = stockInBoxList.get(stockInBoxList.size() - 1);
        assertThat(testStockInBox.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
        assertThat(testStockInBox.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testStockInBox.getSupportRackCode()).isEqualTo(UPDATED_SUPPORT_RACK_CODE);
        assertThat(testStockInBox.getRowsInShelf()).isEqualTo(UPDATED_ROWS_IN_SHELF);
        assertThat(testStockInBox.getColumnsInShelf()).isEqualTo(UPDATED_COLUMNS_IN_SHELF);
        assertThat(testStockInBox.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockInBox.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockInBox.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingStockInBox() throws Exception {
        int databaseSizeBeforeUpdate = stockInBoxRepository.findAll().size();

        // Create the StockInBox
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockInBoxMockMvc.perform(put("/api/stock-in-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInBox in the database
        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockInBox() throws Exception {
        // Initialize the database
        stockInBoxRepository.saveAndFlush(stockInBox);
        int databaseSizeBeforeDelete = stockInBoxRepository.findAll().size();

        // Get the stockInBox
        restStockInBoxMockMvc.perform(delete("/api/stock-in-boxes/{id}", stockInBox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockInBox> stockInBoxList = stockInBoxRepository.findAll();
        assertThat(stockInBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockInBox.class);
    }
}
