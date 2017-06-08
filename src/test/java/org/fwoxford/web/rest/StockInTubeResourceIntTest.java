package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockInTube;
import org.fwoxford.domain.StockInBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.StockInTubeRepository;
import org.fwoxford.service.StockInTubeService;
import org.fwoxford.service.dto.StockInTubeDTO;
import org.fwoxford.service.mapper.StockInTubeMapper;
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
 * Test class for the StockInTubeResource REST controller.
 *
 * @see StockInTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockInTubeResourceIntTest {

    private static final String DEFAULT_ROWS_IN_TUBE = "AAAAAAAAAA";
    private static final String UPDATED_ROWS_IN_TUBE = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMNS_IN_TUBE = "AAAAAAAAAA";
    private static final String UPDATED_COLUMNS_IN_TUBE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    @Autowired
    private StockInTubeRepository stockInTubeRepository;

    @Autowired
    private StockInTubeMapper stockInTubeMapper;

    @Autowired
    private StockInTubeService stockInTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockInTubeMockMvc;

    private StockInTube stockInTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockInTubeResource stockInTubeResource = new StockInTubeResource(stockInTubeService);
        this.restStockInTubeMockMvc = MockMvcBuilders.standaloneSetup(stockInTubeResource)
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
    public static StockInTube createEntity(EntityManager em) {
        StockInTube stockInTube = new StockInTube()
                .rowsInTube(DEFAULT_ROWS_IN_TUBE)
                .columnsInTube(DEFAULT_COLUMNS_IN_TUBE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE);
        // Add required entity
        StockInBox stockInBox = StockInBoxResourceIntTest.createEntity(em);
        em.persist(stockInBox);
        em.flush();
        stockInTube.setStockInBox(stockInBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        stockInTube.setFrozenTube(frozenTube);
        return stockInTube;
    }

    @Before
    public void initTest() {
        stockInTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockInTube() throws Exception {
        int databaseSizeBeforeCreate = stockInTubeRepository.findAll().size();

        // Create the StockInTube
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);

        restStockInTubeMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInTube in the database
        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeCreate + 1);
        StockInTube testStockInTube = stockInTubeList.get(stockInTubeList.size() - 1);
        assertThat(testStockInTube.getRowsInTube()).isEqualTo(DEFAULT_ROWS_IN_TUBE);
        assertThat(testStockInTube.getColumnsInTube()).isEqualTo(DEFAULT_COLUMNS_IN_TUBE);
        assertThat(testStockInTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockInTube.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockInTube.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
    }

    @Test
    @Transactional
    public void createStockInTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockInTubeRepository.findAll().size();

        // Create the StockInTube with an existing ID
        StockInTube existingStockInTube = new StockInTube();
        existingStockInTube.setId(1L);
        StockInTubeDTO existingStockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(existingStockInTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockInTubeMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockInTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRowsInTubeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubeRepository.findAll().size();
        // set the field null
        stockInTube.setRowsInTube(null);

        // Create the StockInTube, which fails.
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);

        restStockInTubeMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColumnsInTubeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubeRepository.findAll().size();
        // set the field null
        stockInTube.setColumnsInTube(null);

        // Create the StockInTube, which fails.
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);

        restStockInTubeMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubeRepository.findAll().size();
        // set the field null
        stockInTube.setStatus(null);

        // Create the StockInTube, which fails.
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);

        restStockInTubeMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTubeRepository.findAll().size();
        // set the field null
        stockInTube.setFrozenBoxCode(null);

        // Create the StockInTube, which fails.
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);

        restStockInTubeMockMvc.perform(post("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockInTubes() throws Exception {
        // Initialize the database
        stockInTubeRepository.saveAndFlush(stockInTube);

        // Get all the stockInTubeList
        restStockInTubeMockMvc.perform(get("/api/stock-in-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockInTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].rowsInTube").value(hasItem(DEFAULT_ROWS_IN_TUBE.toString())))
            .andExpect(jsonPath("$.[*].columnsInTube").value(hasItem(DEFAULT_COLUMNS_IN_TUBE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())));
    }

    @Test
    @Transactional
    public void getStockInTube() throws Exception {
        // Initialize the database
        stockInTubeRepository.saveAndFlush(stockInTube);

        // Get the stockInTube
        restStockInTubeMockMvc.perform(get("/api/stock-in-tubes/{id}", stockInTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockInTube.getId().intValue()))
            .andExpect(jsonPath("$.rowsInTube").value(DEFAULT_ROWS_IN_TUBE.toString()))
            .andExpect(jsonPath("$.columnsInTube").value(DEFAULT_COLUMNS_IN_TUBE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockInTube() throws Exception {
        // Get the stockInTube
        restStockInTubeMockMvc.perform(get("/api/stock-in-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockInTube() throws Exception {
        // Initialize the database
        stockInTubeRepository.saveAndFlush(stockInTube);
        int databaseSizeBeforeUpdate = stockInTubeRepository.findAll().size();

        // Update the stockInTube
        StockInTube updatedStockInTube = stockInTubeRepository.findOne(stockInTube.getId());
        updatedStockInTube
                .rowsInTube(UPDATED_ROWS_IN_TUBE)
                .columnsInTube(UPDATED_COLUMNS_IN_TUBE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE);
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(updatedStockInTube);

        restStockInTubeMockMvc.perform(put("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubeDTO)))
            .andExpect(status().isOk());

        // Validate the StockInTube in the database
        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeUpdate);
        StockInTube testStockInTube = stockInTubeList.get(stockInTubeList.size() - 1);
        assertThat(testStockInTube.getRowsInTube()).isEqualTo(UPDATED_ROWS_IN_TUBE);
        assertThat(testStockInTube.getColumnsInTube()).isEqualTo(UPDATED_COLUMNS_IN_TUBE);
        assertThat(testStockInTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockInTube.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockInTube.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingStockInTube() throws Exception {
        int databaseSizeBeforeUpdate = stockInTubeRepository.findAll().size();

        // Create the StockInTube
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockInTubeMockMvc.perform(put("/api/stock-in-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInTube in the database
        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockInTube() throws Exception {
        // Initialize the database
        stockInTubeRepository.saveAndFlush(stockInTube);
        int databaseSizeBeforeDelete = stockInTubeRepository.findAll().size();

        // Get the stockInTube
        restStockInTubeMockMvc.perform(delete("/api/stock-in-tubes/{id}", stockInTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockInTube> stockInTubeList = stockInTubeRepository.findAll();
        assertThat(stockInTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockInTube.class);
    }
}
