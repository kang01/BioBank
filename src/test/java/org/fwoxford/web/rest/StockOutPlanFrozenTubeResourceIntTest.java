package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutPlanFrozenTube;
import org.fwoxford.domain.StockOutPlan;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.StockOutPlanFrozenTubeRepository;
import org.fwoxford.service.StockOutPlanFrozenTubeService;
import org.fwoxford.service.dto.StockOutPlanFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutPlanFrozenTubeMapper;
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
 * Test class for the StockOutPlanFrozenTubeResource REST controller.
 *
 * @see StockOutPlanFrozenTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutPlanFrozenTubeResourceIntTest {

    private static final String DEFAULT_TUBE_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_COLUMNS = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    @Autowired
    private StockOutPlanFrozenTubeMapper stockOutPlanFrozenTubeMapper;

    @Autowired
    private StockOutPlanFrozenTubeService stockOutPlanFrozenTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutPlanFrozenTubeMockMvc;

    private StockOutPlanFrozenTube stockOutPlanFrozenTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutPlanFrozenTubeResource stockOutPlanFrozenTubeResource = new StockOutPlanFrozenTubeResource(stockOutPlanFrozenTubeService);
        this.restStockOutPlanFrozenTubeMockMvc = MockMvcBuilders.standaloneSetup(stockOutPlanFrozenTubeResource)
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
    public static StockOutPlanFrozenTube createEntity(EntityManager em) {
        StockOutPlanFrozenTube stockOutPlanFrozenTube = new StockOutPlanFrozenTube()
                .tubeRows(DEFAULT_TUBE_ROWS)
                .tubeColumns(DEFAULT_TUBE_COLUMNS)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutPlan stockOutPlan = StockOutPlanResourceIntTest.createEntity(em);
        em.persist(stockOutPlan);
        em.flush();
        stockOutPlanFrozenTube.setStockOutPlan(stockOutPlan);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        stockOutPlanFrozenTube.setFrozenBox(frozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        stockOutPlanFrozenTube.setFrozenTube(frozenTube);
        return stockOutPlanFrozenTube;
    }

    @Before
    public void initTest() {
        stockOutPlanFrozenTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutPlanFrozenTube() throws Exception {
        int databaseSizeBeforeCreate = stockOutPlanFrozenTubeRepository.findAll().size();

        // Create the StockOutPlanFrozenTube
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube);

        restStockOutPlanFrozenTubeMockMvc.perform(post("/api/stock-out-plan-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutPlanFrozenTube in the database
        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutPlanFrozenTube testStockOutPlanFrozenTube = stockOutPlanFrozenTubeList.get(stockOutPlanFrozenTubeList.size() - 1);
        assertThat(testStockOutPlanFrozenTube.getTubeRows()).isEqualTo(DEFAULT_TUBE_ROWS);
        assertThat(testStockOutPlanFrozenTube.getTubeColumns()).isEqualTo(DEFAULT_TUBE_COLUMNS);
        assertThat(testStockOutPlanFrozenTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutPlanFrozenTube.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutPlanFrozenTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutPlanFrozenTubeRepository.findAll().size();

        // Create the StockOutPlanFrozenTube with an existing ID
        StockOutPlanFrozenTube existingStockOutPlanFrozenTube = new StockOutPlanFrozenTube();
        existingStockOutPlanFrozenTube.setId(1L);
        StockOutPlanFrozenTubeDTO existingStockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(existingStockOutPlanFrozenTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutPlanFrozenTubeMockMvc.perform(post("/api/stock-out-plan-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutPlanFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTubeRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutPlanFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutPlanFrozenTube.setTubeRows(null);

        // Create the StockOutPlanFrozenTube, which fails.
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube);

        restStockOutPlanFrozenTubeMockMvc.perform(post("/api/stock-out-plan-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutPlanFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutPlanFrozenTube.setTubeColumns(null);

        // Create the StockOutPlanFrozenTube, which fails.
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube);

        restStockOutPlanFrozenTubeMockMvc.perform(post("/api/stock-out-plan-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutPlanFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutPlanFrozenTube.setStatus(null);

        // Create the StockOutPlanFrozenTube, which fails.
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube);

        restStockOutPlanFrozenTubeMockMvc.perform(post("/api/stock-out-plan-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutPlanFrozenTubes() throws Exception {
        // Initialize the database
        stockOutPlanFrozenTubeRepository.saveAndFlush(stockOutPlanFrozenTube);

        // Get all the stockOutPlanFrozenTubeList
        restStockOutPlanFrozenTubeMockMvc.perform(get("/api/stock-out-plan-frozen-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutPlanFrozenTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].tubeRows").value(hasItem(DEFAULT_TUBE_ROWS.toString())))
            .andExpect(jsonPath("$.[*].tubeColumns").value(hasItem(DEFAULT_TUBE_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutPlanFrozenTube() throws Exception {
        // Initialize the database
        stockOutPlanFrozenTubeRepository.saveAndFlush(stockOutPlanFrozenTube);

        // Get the stockOutPlanFrozenTube
        restStockOutPlanFrozenTubeMockMvc.perform(get("/api/stock-out-plan-frozen-tubes/{id}", stockOutPlanFrozenTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutPlanFrozenTube.getId().intValue()))
            .andExpect(jsonPath("$.tubeRows").value(DEFAULT_TUBE_ROWS.toString()))
            .andExpect(jsonPath("$.tubeColumns").value(DEFAULT_TUBE_COLUMNS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutPlanFrozenTube() throws Exception {
        // Get the stockOutPlanFrozenTube
        restStockOutPlanFrozenTubeMockMvc.perform(get("/api/stock-out-plan-frozen-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutPlanFrozenTube() throws Exception {
        // Initialize the database
        stockOutPlanFrozenTubeRepository.saveAndFlush(stockOutPlanFrozenTube);
        int databaseSizeBeforeUpdate = stockOutPlanFrozenTubeRepository.findAll().size();

        // Update the stockOutPlanFrozenTube
        StockOutPlanFrozenTube updatedStockOutPlanFrozenTube = stockOutPlanFrozenTubeRepository.findOne(stockOutPlanFrozenTube.getId());
        updatedStockOutPlanFrozenTube
                .tubeRows(UPDATED_TUBE_ROWS)
                .tubeColumns(UPDATED_TUBE_COLUMNS)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(updatedStockOutPlanFrozenTube);

        restStockOutPlanFrozenTubeMockMvc.perform(put("/api/stock-out-plan-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanFrozenTubeDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutPlanFrozenTube in the database
        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeUpdate);
        StockOutPlanFrozenTube testStockOutPlanFrozenTube = stockOutPlanFrozenTubeList.get(stockOutPlanFrozenTubeList.size() - 1);
        assertThat(testStockOutPlanFrozenTube.getTubeRows()).isEqualTo(UPDATED_TUBE_ROWS);
        assertThat(testStockOutPlanFrozenTube.getTubeColumns()).isEqualTo(UPDATED_TUBE_COLUMNS);
        assertThat(testStockOutPlanFrozenTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutPlanFrozenTube.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutPlanFrozenTube() throws Exception {
        int databaseSizeBeforeUpdate = stockOutPlanFrozenTubeRepository.findAll().size();

        // Create the StockOutPlanFrozenTube
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutPlanFrozenTubeMockMvc.perform(put("/api/stock-out-plan-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutPlanFrozenTube in the database
        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutPlanFrozenTube() throws Exception {
        // Initialize the database
        stockOutPlanFrozenTubeRepository.saveAndFlush(stockOutPlanFrozenTube);
        int databaseSizeBeforeDelete = stockOutPlanFrozenTubeRepository.findAll().size();

        // Get the stockOutPlanFrozenTube
        restStockOutPlanFrozenTubeMockMvc.perform(delete("/api/stock-out-plan-frozen-tubes/{id}", stockOutPlanFrozenTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeList = stockOutPlanFrozenTubeRepository.findAll();
        assertThat(stockOutPlanFrozenTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutPlanFrozenTube.class);
    }
}
