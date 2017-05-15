package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutTask;
import org.fwoxford.domain.StockOutPlan;
import org.fwoxford.repository.StockOutTaskRepository;
import org.fwoxford.service.StockOutTaskService;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.mapper.StockOutTaskMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockOutTaskResource REST controller.
 *
 * @see StockOutTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutTaskResourceIntTest {

    private static final Long DEFAULT_STOCK_OUT_HEAD_ID_1 = 1L;
    private static final Long UPDATED_STOCK_OUT_HEAD_ID_1 = 2L;

    private static final Long DEFAULT_STOCK_OUT_HEAD_ID_2 = 1L;
    private static final Long UPDATED_STOCK_OUT_HEAD_ID_2 = 2L;

    private static final LocalDate DEFAULT_STOCK_OUT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STOCK_OUT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STOCK_OUT_TASK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_OUT_TASK_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_USED_TIME = 1;
    private static final Integer UPDATED_USED_TIME = 2;

    @Autowired
    private StockOutTaskRepository stockOutTaskRepository;

    @Autowired
    private StockOutTaskMapper stockOutTaskMapper;

    @Autowired
    private StockOutTaskService stockOutTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutTaskMockMvc;

    private StockOutTask stockOutTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutTaskResource stockOutTaskResource = new StockOutTaskResource(stockOutTaskService);
        this.restStockOutTaskMockMvc = MockMvcBuilders.standaloneSetup(stockOutTaskResource)
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
    public static StockOutTask createEntity(EntityManager em) {
        StockOutTask stockOutTask = new StockOutTask()
                .stockOutHeadId1(DEFAULT_STOCK_OUT_HEAD_ID_1)
                .stockOutHeadId2(DEFAULT_STOCK_OUT_HEAD_ID_2)
                .stockOutDate(DEFAULT_STOCK_OUT_DATE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .stockOutTaskCode(DEFAULT_STOCK_OUT_TASK_CODE)
                .usedTime(DEFAULT_USED_TIME);
        // Add required entity
        StockOutPlan stockOutPlan = StockOutPlanResourceIntTest.createEntity(em);
        em.persist(stockOutPlan);
        em.flush();
        stockOutTask.setStockOutPlan(stockOutPlan);
        return stockOutTask;
    }

    @Before
    public void initTest() {
        stockOutTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutTask() throws Exception {
        int databaseSizeBeforeCreate = stockOutTaskRepository.findAll().size();

        // Create the StockOutTask
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);

        restStockOutTaskMockMvc.perform(post("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutTask in the database
        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutTask testStockOutTask = stockOutTaskList.get(stockOutTaskList.size() - 1);
        assertThat(testStockOutTask.getStockOutHeadId1()).isEqualTo(DEFAULT_STOCK_OUT_HEAD_ID_1);
        assertThat(testStockOutTask.getStockOutHeadId2()).isEqualTo(DEFAULT_STOCK_OUT_HEAD_ID_2);
        assertThat(testStockOutTask.getStockOutDate()).isEqualTo(DEFAULT_STOCK_OUT_DATE);
        assertThat(testStockOutTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutTask.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockOutTask.getStockOutTaskCode()).isEqualTo(DEFAULT_STOCK_OUT_TASK_CODE);
        assertThat(testStockOutTask.getUsedTime()).isEqualTo(DEFAULT_USED_TIME);
    }

    @Test
    @Transactional
    public void createStockOutTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutTaskRepository.findAll().size();

        // Create the StockOutTask with an existing ID
        StockOutTask existingStockOutTask = new StockOutTask();
        existingStockOutTask.setId(1L);
        StockOutTaskDTO existingStockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(existingStockOutTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutTaskMockMvc.perform(post("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStockOutDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutTaskRepository.findAll().size();
        // set the field null
        stockOutTask.setStockOutDate(null);

        // Create the StockOutTask, which fails.
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);

        restStockOutTaskMockMvc.perform(post("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutTaskRepository.findAll().size();
        // set the field null
        stockOutTask.setStatus(null);

        // Create the StockOutTask, which fails.
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);

        restStockOutTaskMockMvc.perform(post("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockOutTaskCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutTaskRepository.findAll().size();
        // set the field null
        stockOutTask.setStockOutTaskCode(null);

        // Create the StockOutTask, which fails.
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);

        restStockOutTaskMockMvc.perform(post("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutTaskRepository.findAll().size();
        // set the field null
        stockOutTask.setUsedTime(null);

        // Create the StockOutTask, which fails.
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);

        restStockOutTaskMockMvc.perform(post("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutTasks() throws Exception {
        // Initialize the database
        stockOutTaskRepository.saveAndFlush(stockOutTask);

        // Get all the stockOutTaskList
        restStockOutTaskMockMvc.perform(get("/api/stock-out-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockOutHeadId1").value(hasItem(DEFAULT_STOCK_OUT_HEAD_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].stockOutHeadId2").value(hasItem(DEFAULT_STOCK_OUT_HEAD_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].stockOutDate").value(hasItem(DEFAULT_STOCK_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].stockOutTaskCode").value(hasItem(DEFAULT_STOCK_OUT_TASK_CODE.toString())))
            .andExpect(jsonPath("$.[*].usedTime").value(hasItem(DEFAULT_USED_TIME)));
    }

    @Test
    @Transactional
    public void getStockOutTask() throws Exception {
        // Initialize the database
        stockOutTaskRepository.saveAndFlush(stockOutTask);

        // Get the stockOutTask
        restStockOutTaskMockMvc.perform(get("/api/stock-out-tasks/{id}", stockOutTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutTask.getId().intValue()))
            .andExpect(jsonPath("$.stockOutHeadId1").value(DEFAULT_STOCK_OUT_HEAD_ID_1.intValue()))
            .andExpect(jsonPath("$.stockOutHeadId2").value(DEFAULT_STOCK_OUT_HEAD_ID_2.intValue()))
            .andExpect(jsonPath("$.stockOutDate").value(DEFAULT_STOCK_OUT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.stockOutTaskCode").value(DEFAULT_STOCK_OUT_TASK_CODE.toString()))
            .andExpect(jsonPath("$.usedTime").value(DEFAULT_USED_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutTask() throws Exception {
        // Get the stockOutTask
        restStockOutTaskMockMvc.perform(get("/api/stock-out-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutTask() throws Exception {
        // Initialize the database
        stockOutTaskRepository.saveAndFlush(stockOutTask);
        int databaseSizeBeforeUpdate = stockOutTaskRepository.findAll().size();

        // Update the stockOutTask
        StockOutTask updatedStockOutTask = stockOutTaskRepository.findOne(stockOutTask.getId());
        updatedStockOutTask
                .stockOutHeadId1(UPDATED_STOCK_OUT_HEAD_ID_1)
                .stockOutHeadId2(UPDATED_STOCK_OUT_HEAD_ID_2)
                .stockOutDate(UPDATED_STOCK_OUT_DATE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .stockOutTaskCode(UPDATED_STOCK_OUT_TASK_CODE)
                .usedTime(UPDATED_USED_TIME);
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(updatedStockOutTask);

        restStockOutTaskMockMvc.perform(put("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutTask in the database
        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeUpdate);
        StockOutTask testStockOutTask = stockOutTaskList.get(stockOutTaskList.size() - 1);
        assertThat(testStockOutTask.getStockOutHeadId1()).isEqualTo(UPDATED_STOCK_OUT_HEAD_ID_1);
        assertThat(testStockOutTask.getStockOutHeadId2()).isEqualTo(UPDATED_STOCK_OUT_HEAD_ID_2);
        assertThat(testStockOutTask.getStockOutDate()).isEqualTo(UPDATED_STOCK_OUT_DATE);
        assertThat(testStockOutTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutTask.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockOutTask.getStockOutTaskCode()).isEqualTo(UPDATED_STOCK_OUT_TASK_CODE);
        assertThat(testStockOutTask.getUsedTime()).isEqualTo(UPDATED_USED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutTask() throws Exception {
        int databaseSizeBeforeUpdate = stockOutTaskRepository.findAll().size();

        // Create the StockOutTask
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutTaskMockMvc.perform(put("/api/stock-out-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutTask in the database
        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutTask() throws Exception {
        // Initialize the database
        stockOutTaskRepository.saveAndFlush(stockOutTask);
        int databaseSizeBeforeDelete = stockOutTaskRepository.findAll().size();

        // Get the stockOutTask
        restStockOutTaskMockMvc.perform(delete("/api/stock-out-tasks/{id}", stockOutTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutTask> stockOutTaskList = stockOutTaskRepository.findAll();
        assertThat(stockOutTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutTask.class);
    }
}
