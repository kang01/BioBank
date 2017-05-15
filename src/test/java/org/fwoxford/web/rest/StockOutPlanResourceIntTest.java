package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutPlan;
import org.fwoxford.domain.StockOutApply;
import org.fwoxford.repository.StockOutPlanRepository;
import org.fwoxford.service.StockOutPlanService;
import org.fwoxford.service.dto.StockOutPlanDTO;
import org.fwoxford.service.mapper.StockOutPlanMapper;
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
 * Test class for the StockOutPlanResource REST controller.
 *
 * @see StockOutPlanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutPlanResourceIntTest {

    private static final String DEFAULT_STOCK_OUT_PLAN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_OUT_PLAN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_APPLY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_APPLY_NUMBER = "BBBBBBBBBB";

    @Autowired
    private StockOutPlanRepository stockOutPlanRepository;

    @Autowired
    private StockOutPlanMapper stockOutPlanMapper;

    @Autowired
    private StockOutPlanService stockOutPlanService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutPlanMockMvc;

    private StockOutPlan stockOutPlan;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutPlanResource stockOutPlanResource = new StockOutPlanResource(stockOutPlanService);
        this.restStockOutPlanMockMvc = MockMvcBuilders.standaloneSetup(stockOutPlanResource)
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
    public static StockOutPlan createEntity(EntityManager em) {
        StockOutPlan stockOutPlan = new StockOutPlan()
                .stockOutPlanCode(DEFAULT_STOCK_OUT_PLAN_CODE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .applyNumber(DEFAULT_APPLY_NUMBER);
        // Add required entity
        StockOutApply stockOutApply = StockOutApplyResourceIntTest.createEntity(em);
        em.persist(stockOutApply);
        em.flush();
        stockOutPlan.setStockOutApply(stockOutApply);
        return stockOutPlan;
    }

    @Before
    public void initTest() {
        stockOutPlan = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutPlan() throws Exception {
        int databaseSizeBeforeCreate = stockOutPlanRepository.findAll().size();

        // Create the StockOutPlan
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);

        restStockOutPlanMockMvc.perform(post("/api/stock-out-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutPlan in the database
        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutPlan testStockOutPlan = stockOutPlanList.get(stockOutPlanList.size() - 1);
        assertThat(testStockOutPlan.getStockOutPlanCode()).isEqualTo(DEFAULT_STOCK_OUT_PLAN_CODE);
        assertThat(testStockOutPlan.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutPlan.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockOutPlan.getApplyNumber()).isEqualTo(DEFAULT_APPLY_NUMBER);
    }

    @Test
    @Transactional
    public void createStockOutPlanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutPlanRepository.findAll().size();

        // Create the StockOutPlan with an existing ID
        StockOutPlan existingStockOutPlan = new StockOutPlan();
        existingStockOutPlan.setId(1L);
        StockOutPlanDTO existingStockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(existingStockOutPlan);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutPlanMockMvc.perform(post("/api/stock-out-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStockOutPlanCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutPlanRepository.findAll().size();
        // set the field null
        stockOutPlan.setStockOutPlanCode(null);

        // Create the StockOutPlan, which fails.
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);

        restStockOutPlanMockMvc.perform(post("/api/stock-out-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutPlanRepository.findAll().size();
        // set the field null
        stockOutPlan.setStatus(null);

        // Create the StockOutPlan, which fails.
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);

        restStockOutPlanMockMvc.perform(post("/api/stock-out-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplyNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutPlanRepository.findAll().size();
        // set the field null
        stockOutPlan.setApplyNumber(null);

        // Create the StockOutPlan, which fails.
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);

        restStockOutPlanMockMvc.perform(post("/api/stock-out-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutPlans() throws Exception {
        // Initialize the database
        stockOutPlanRepository.saveAndFlush(stockOutPlan);

        // Get all the stockOutPlanList
        restStockOutPlanMockMvc.perform(get("/api/stock-out-plans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockOutPlanCode").value(hasItem(DEFAULT_STOCK_OUT_PLAN_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].applyNumber").value(hasItem(DEFAULT_APPLY_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getStockOutPlan() throws Exception {
        // Initialize the database
        stockOutPlanRepository.saveAndFlush(stockOutPlan);

        // Get the stockOutPlan
        restStockOutPlanMockMvc.perform(get("/api/stock-out-plans/{id}", stockOutPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutPlan.getId().intValue()))
            .andExpect(jsonPath("$.stockOutPlanCode").value(DEFAULT_STOCK_OUT_PLAN_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.applyNumber").value(DEFAULT_APPLY_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutPlan() throws Exception {
        // Get the stockOutPlan
        restStockOutPlanMockMvc.perform(get("/api/stock-out-plans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutPlan() throws Exception {
        // Initialize the database
        stockOutPlanRepository.saveAndFlush(stockOutPlan);
        int databaseSizeBeforeUpdate = stockOutPlanRepository.findAll().size();

        // Update the stockOutPlan
        StockOutPlan updatedStockOutPlan = stockOutPlanRepository.findOne(stockOutPlan.getId());
        updatedStockOutPlan
                .stockOutPlanCode(UPDATED_STOCK_OUT_PLAN_CODE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .applyNumber(UPDATED_APPLY_NUMBER);
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(updatedStockOutPlan);

        restStockOutPlanMockMvc.perform(put("/api/stock-out-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutPlan in the database
        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeUpdate);
        StockOutPlan testStockOutPlan = stockOutPlanList.get(stockOutPlanList.size() - 1);
        assertThat(testStockOutPlan.getStockOutPlanCode()).isEqualTo(UPDATED_STOCK_OUT_PLAN_CODE);
        assertThat(testStockOutPlan.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutPlan.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockOutPlan.getApplyNumber()).isEqualTo(UPDATED_APPLY_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutPlan() throws Exception {
        int databaseSizeBeforeUpdate = stockOutPlanRepository.findAll().size();

        // Create the StockOutPlan
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutPlanMockMvc.perform(put("/api/stock-out-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutPlanDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutPlan in the database
        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutPlan() throws Exception {
        // Initialize the database
        stockOutPlanRepository.saveAndFlush(stockOutPlan);
        int databaseSizeBeforeDelete = stockOutPlanRepository.findAll().size();

        // Get the stockOutPlan
        restStockOutPlanMockMvc.perform(delete("/api/stock-out-plans/{id}", stockOutPlan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutPlan> stockOutPlanList = stockOutPlanRepository.findAll();
        assertThat(stockOutPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutPlan.class);
    }
}
