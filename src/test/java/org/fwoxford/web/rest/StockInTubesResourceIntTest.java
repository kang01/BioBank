package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockInTubes;
import org.fwoxford.domain.StockIn;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.FrozenBoxPosition;
import org.fwoxford.repository.StockInTubesRepository;
import org.fwoxford.service.StockInTubesService;
import org.fwoxford.service.dto.StockInTubesDTO;
import org.fwoxford.service.mapper.StockInTubesMapper;
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
 * Test class for the StockInTubesResource REST controller.
 *
 * @see StockInTubesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockInTubesResourceIntTest {

    private static final String DEFAULT_STOCK_IN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_IN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSHIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSHIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSHIP_BATCH = "AAAAAAAAAA";
    private static final String UPDATED_TRANSHIP_BATCH = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_TUBE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_TUBE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ROWS_IN_TUBE = "AAAAAAAAAA";
    private static final String UPDATED_ROWS_IN_TUBE = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMNS_IN_TUBE = "AAAAAAAAAA";
    private static final String UPDATED_COLUMNS_IN_TUBE = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private StockInTubesRepository stockInTubesRepository;

    @Autowired
    private StockInTubesMapper stockInTubesMapper;

    @Autowired
    private StockInTubesService stockInTubesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockInTubesMockMvc;

    private StockInTubes stockInTubes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockInTubesResource stockInTubesResource = new StockInTubesResource(stockInTubesService);
        this.restStockInTubesMockMvc = MockMvcBuilders.standaloneSetup(stockInTubesResource)
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
    public static StockInTubes createEntity(EntityManager em) {
        StockInTubes stockInTubes = new StockInTubes()
                .stockInCode(DEFAULT_STOCK_IN_CODE)
                .transhipCode(DEFAULT_TRANSHIP_CODE)
                .transhipBatch(DEFAULT_TRANSHIP_BATCH)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .sampleCode(DEFAULT_SAMPLE_CODE)
                .frozenTubeCode(DEFAULT_FROZEN_TUBE_CODE)
                .rowsInTube(DEFAULT_ROWS_IN_TUBE)
                .columnsInTube(DEFAULT_COLUMNS_IN_TUBE)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        StockIn stockIn = StockInResourceIntTest.createEntity(em);
        em.persist(stockIn);
        em.flush();
        stockInTubes.setStockIn(stockIn);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        stockInTubes.setFrozenBox(frozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        stockInTubes.setFrozenTube(frozenTube);
        // Add required entity
        FrozenBoxPosition frozenBoxPosition = FrozenBoxPositionResourceIntTest.createEntity(em);
        em.persist(frozenBoxPosition);
        em.flush();
        stockInTubes.setFrozenBoxPosition(frozenBoxPosition);
        return stockInTubes;
    }

    @Before
    public void initTest() {
        stockInTubes = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockInTubes() throws Exception {
        int databaseSizeBeforeCreate = stockInTubesRepository.findAll().size();

        // Create the StockInTubes
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);

        restStockInTubesMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInTubes in the database
        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeCreate + 1);
        StockInTubes testStockInTubes = stockInTubesList.get(stockInTubesList.size() - 1);
        assertThat(testStockInTubes.getStockInCode()).isEqualTo(DEFAULT_STOCK_IN_CODE);
        assertThat(testStockInTubes.getTranshipCode()).isEqualTo(DEFAULT_TRANSHIP_CODE);
        assertThat(testStockInTubes.getTranshipBatch()).isEqualTo(DEFAULT_TRANSHIP_BATCH);
        assertThat(testStockInTubes.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testStockInTubes.getSampleCode()).isEqualTo(DEFAULT_SAMPLE_CODE);
        assertThat(testStockInTubes.getFrozenTubeCode()).isEqualTo(DEFAULT_FROZEN_TUBE_CODE);
        assertThat(testStockInTubes.getRowsInTube()).isEqualTo(DEFAULT_ROWS_IN_TUBE);
        assertThat(testStockInTubes.getColumnsInTube()).isEqualTo(DEFAULT_COLUMNS_IN_TUBE);
        assertThat(testStockInTubes.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockInTubes.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createStockInTubesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockInTubesRepository.findAll().size();

        // Create the StockInTubes with an existing ID
        StockInTubes existingStockInTubes = new StockInTubes();
        existingStockInTubes.setId(1L);
        StockInTubesDTO existingStockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(existingStockInTubes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockInTubesMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockInTubesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStockInCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubesRepository.findAll().size();
        // set the field null
        stockInTubes.setStockInCode(null);

        // Create the StockInTubes, which fails.
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);

        restStockInTubesMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubesRepository.findAll().size();
        // set the field null
        stockInTubes.setFrozenBoxCode(null);

        // Create the StockInTubes, which fails.
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);

        restStockInTubesMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRowsInTubeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubesRepository.findAll().size();
        // set the field null
        stockInTubes.setRowsInTube(null);

        // Create the StockInTubes, which fails.
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);

        restStockInTubesMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColumnsInTubeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubesRepository.findAll().size();
        // set the field null
        stockInTubes.setColumnsInTube(null);

        // Create the StockInTubes, which fails.
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);

        restStockInTubesMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubesRepository.findAll().size();
        // set the field null
        stockInTubes.setStatus(null);

        // Create the StockInTubes, which fails.
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);

        restStockInTubesMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockInTubes() throws Exception {
        // Initialize the database
        stockInTubesRepository.saveAndFlush(stockInTubes);

        // Get all the stockInTubesList
        restStockInTubesMockMvc.perform(get("/api/stock-in-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockInTubes.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockInCode").value(hasItem(DEFAULT_STOCK_IN_CODE.toString())))
            .andExpect(jsonPath("$.[*].transhipCode").value(hasItem(DEFAULT_TRANSHIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].transhipBatch").value(hasItem(DEFAULT_TRANSHIP_BATCH.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleCode").value(hasItem(DEFAULT_SAMPLE_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenTubeCode").value(hasItem(DEFAULT_FROZEN_TUBE_CODE.toString())))
            .andExpect(jsonPath("$.[*].rowsInTube").value(hasItem(DEFAULT_ROWS_IN_TUBE.toString())))
            .andExpect(jsonPath("$.[*].columnsInTube").value(hasItem(DEFAULT_COLUMNS_IN_TUBE.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getStockInTubes() throws Exception {
        // Initialize the database
        stockInTubesRepository.saveAndFlush(stockInTubes);

        // Get the stockInTubes
        restStockInTubesMockMvc.perform(get("/api/stock-in-tubes/{id}", stockInTubes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockInTubes.getId().intValue()))
            .andExpect(jsonPath("$.stockInCode").value(DEFAULT_STOCK_IN_CODE.toString()))
            .andExpect(jsonPath("$.transhipCode").value(DEFAULT_TRANSHIP_CODE.toString()))
            .andExpect(jsonPath("$.transhipBatch").value(DEFAULT_TRANSHIP_BATCH.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.sampleCode").value(DEFAULT_SAMPLE_CODE.toString()))
            .andExpect(jsonPath("$.frozenTubeCode").value(DEFAULT_FROZEN_TUBE_CODE.toString()))
            .andExpect(jsonPath("$.rowsInTube").value(DEFAULT_ROWS_IN_TUBE.toString()))
            .andExpect(jsonPath("$.columnsInTube").value(DEFAULT_COLUMNS_IN_TUBE.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockInTubes() throws Exception {
        // Get the stockInTubes
        restStockInTubesMockMvc.perform(get("/api/stock-in-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockInTubes() throws Exception {
        // Initialize the database
        stockInTubesRepository.saveAndFlush(stockInTubes);
        int databaseSizeBeforeUpdate = stockInTubesRepository.findAll().size();

        // Update the stockInTubes
        StockInTubes updatedStockInTubes = stockInTubesRepository.findOne(stockInTubes.getId());
        updatedStockInTubes
                .stockInCode(UPDATED_STOCK_IN_CODE)
                .transhipCode(UPDATED_TRANSHIP_CODE)
                .transhipBatch(UPDATED_TRANSHIP_BATCH)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .sampleCode(UPDATED_SAMPLE_CODE)
                .frozenTubeCode(UPDATED_FROZEN_TUBE_CODE)
                .rowsInTube(UPDATED_ROWS_IN_TUBE)
                .columnsInTube(UPDATED_COLUMNS_IN_TUBE)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(updatedStockInTubes);

        restStockInTubesMockMvc.perform(put("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isOk());

        // Validate the StockInTubes in the database
        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeUpdate);
        StockInTubes testStockInTubes = stockInTubesList.get(stockInTubesList.size() - 1);
        assertThat(testStockInTubes.getStockInCode()).isEqualTo(UPDATED_STOCK_IN_CODE);
        assertThat(testStockInTubes.getTranshipCode()).isEqualTo(UPDATED_TRANSHIP_CODE);
        assertThat(testStockInTubes.getTranshipBatch()).isEqualTo(UPDATED_TRANSHIP_BATCH);
        assertThat(testStockInTubes.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testStockInTubes.getSampleCode()).isEqualTo(UPDATED_SAMPLE_CODE);
        assertThat(testStockInTubes.getFrozenTubeCode()).isEqualTo(UPDATED_FROZEN_TUBE_CODE);
        assertThat(testStockInTubes.getRowsInTube()).isEqualTo(UPDATED_ROWS_IN_TUBE);
        assertThat(testStockInTubes.getColumnsInTube()).isEqualTo(UPDATED_COLUMNS_IN_TUBE);
        assertThat(testStockInTubes.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockInTubes.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingStockInTubes() throws Exception {
        int databaseSizeBeforeUpdate = stockInTubesRepository.findAll().size();

        // Create the StockInTubes
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockInTubesMockMvc.perform(put("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubesDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInTubes in the database
        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockInTubes() throws Exception {
        // Initialize the database
        stockInTubesRepository.saveAndFlush(stockInTubes);
        int databaseSizeBeforeDelete = stockInTubesRepository.findAll().size();

        // Get the stockInTubes
        restStockInTubesMockMvc.perform(delete("/api/stock-in-tubes/{id}", stockInTubes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockInTubes> stockInTubesList = stockInTubesRepository.findAll();
        assertThat(stockInTubesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockInTubes.class);
    }
}
