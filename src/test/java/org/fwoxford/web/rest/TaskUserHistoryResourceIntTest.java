package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.TaskUserHistory;
import org.fwoxford.repository.TaskUserHistoryRepository;
import org.fwoxford.service.TaskUserHistoryService;
import org.fwoxford.service.dto.TaskUserHistoryDTO;
import org.fwoxford.service.mapper.TaskUserHistoryMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.fwoxford.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TaskUserHistoryResource REST controller.
 *
 * @see TaskUserHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class TaskUserHistoryResourceIntTest {

    private static final String DEFAULT_USER_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_USER_TOKEN = "BBBBBBBBBB";

    private static final Long DEFAULT_LOGIN_USER_ID = 1L;
    private static final Long UPDATED_LOGIN_USER_ID = 2L;

    private static final String DEFAULT_BUSINESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_INVALID_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INVALID_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private TaskUserHistoryRepository taskUserHistoryRepository;

    @Autowired
    private TaskUserHistoryMapper taskUserHistoryMapper;

    @Autowired
    private TaskUserHistoryService taskUserHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskUserHistoryMockMvc;

    private TaskUserHistory taskUserHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskUserHistoryResource taskUserHistoryResource = new TaskUserHistoryResource(taskUserHistoryService);
        this.restTaskUserHistoryMockMvc = MockMvcBuilders.standaloneSetup(taskUserHistoryResource)
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
    public static TaskUserHistory createEntity(EntityManager em) {
        TaskUserHistory taskUserHistory = new TaskUserHistory()
                .userToken(DEFAULT_USER_TOKEN)
                .loginUserId(DEFAULT_LOGIN_USER_ID)
                .businessName(DEFAULT_BUSINESS_NAME)
                .invalidDate(DEFAULT_INVALID_DATE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return taskUserHistory;
    }

    @Before
    public void initTest() {
        taskUserHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskUserHistory() throws Exception {
        int databaseSizeBeforeCreate = taskUserHistoryRepository.findAll().size();

        // Create the TaskUserHistory
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);

        restTaskUserHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskUserHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskUserHistory in the database
        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TaskUserHistory testTaskUserHistory = taskUserHistoryList.get(taskUserHistoryList.size() - 1);
        assertThat(testTaskUserHistory.getUserToken()).isEqualTo(DEFAULT_USER_TOKEN);
        assertThat(testTaskUserHistory.getLoginUserId()).isEqualTo(DEFAULT_LOGIN_USER_ID);
        assertThat(testTaskUserHistory.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
        assertThat(testTaskUserHistory.getInvalidDate()).isEqualTo(DEFAULT_INVALID_DATE);
        assertThat(testTaskUserHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTaskUserHistory.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createTaskUserHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskUserHistoryRepository.findAll().size();

        // Create the TaskUserHistory with an existing ID
        TaskUserHistory existingTaskUserHistory = new TaskUserHistory();
        existingTaskUserHistory.setId(1L);
        TaskUserHistoryDTO existingTaskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(existingTaskUserHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskUserHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTaskUserHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLoginUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskUserHistoryRepository.findAll().size();
        // set the field null
        taskUserHistory.setLoginUserId(null);

        // Create the TaskUserHistory, which fails.
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);

        restTaskUserHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskUserHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBusinessNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskUserHistoryRepository.findAll().size();
        // set the field null
        taskUserHistory.setBusinessName(null);

        // Create the TaskUserHistory, which fails.
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);

        restTaskUserHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskUserHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvalidDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskUserHistoryRepository.findAll().size();
        // set the field null
        taskUserHistory.setInvalidDate(null);

        // Create the TaskUserHistory, which fails.
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);

        restTaskUserHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskUserHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskUserHistoryRepository.findAll().size();
        // set the field null
        taskUserHistory.setStatus(null);

        // Create the TaskUserHistory, which fails.
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);

        restTaskUserHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskUserHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaskUserHistories() throws Exception {
        // Initialize the database
        taskUserHistoryRepository.saveAndFlush(taskUserHistory);

        // Get all the taskUserHistoryList
        restTaskUserHistoryMockMvc.perform(get("/api/user-login-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskUserHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].userToken").value(hasItem(DEFAULT_USER_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].loginUserId").value(hasItem(DEFAULT_LOGIN_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())))
            .andExpect(jsonPath("$.[*].invalidDate").value(hasItem(sameInstant(DEFAULT_INVALID_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getTaskUserHistory() throws Exception {
        // Initialize the database
        taskUserHistoryRepository.saveAndFlush(taskUserHistory);

        // Get the taskUserHistory
        restTaskUserHistoryMockMvc.perform(get("/api/user-login-histories/{id}", taskUserHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskUserHistory.getId().intValue()))
            .andExpect(jsonPath("$.userToken").value(DEFAULT_USER_TOKEN.toString()))
            .andExpect(jsonPath("$.loginUserId").value(DEFAULT_LOGIN_USER_ID.intValue()))
            .andExpect(jsonPath("$.businessName").value(DEFAULT_BUSINESS_NAME.toString()))
            .andExpect(jsonPath("$.invalidDate").value(sameInstant(DEFAULT_INVALID_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskUserHistory() throws Exception {
        // Get the taskUserHistory
        restTaskUserHistoryMockMvc.perform(get("/api/user-login-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskUserHistory() throws Exception {
        // Initialize the database
        taskUserHistoryRepository.saveAndFlush(taskUserHistory);
        int databaseSizeBeforeUpdate = taskUserHistoryRepository.findAll().size();

        // Update the taskUserHistory
        TaskUserHistory updatedTaskUserHistory = taskUserHistoryRepository.findOne(taskUserHistory.getId());
        updatedTaskUserHistory
                .userToken(UPDATED_USER_TOKEN)
                .loginUserId(UPDATED_LOGIN_USER_ID)
                .businessName(UPDATED_BUSINESS_NAME)
                .invalidDate(UPDATED_INVALID_DATE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(updatedTaskUserHistory);

        restTaskUserHistoryMockMvc.perform(put("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskUserHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the TaskUserHistory in the database
        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeUpdate);
        TaskUserHistory testTaskUserHistory = taskUserHistoryList.get(taskUserHistoryList.size() - 1);
        assertThat(testTaskUserHistory.getUserToken()).isEqualTo(UPDATED_USER_TOKEN);
        assertThat(testTaskUserHistory.getLoginUserId()).isEqualTo(UPDATED_LOGIN_USER_ID);
        assertThat(testTaskUserHistory.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
        assertThat(testTaskUserHistory.getInvalidDate()).isEqualTo(UPDATED_INVALID_DATE);
        assertThat(testTaskUserHistory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTaskUserHistory.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskUserHistory() throws Exception {
        int databaseSizeBeforeUpdate = taskUserHistoryRepository.findAll().size();

        // Create the TaskUserHistory
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskUserHistoryMockMvc.perform(put("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskUserHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskUserHistory in the database
        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskUserHistory() throws Exception {
        // Initialize the database
        taskUserHistoryRepository.saveAndFlush(taskUserHistory);
        int databaseSizeBeforeDelete = taskUserHistoryRepository.findAll().size();

        // Get the taskUserHistory
        restTaskUserHistoryMockMvc.perform(delete("/api/user-login-histories/{id}", taskUserHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskUserHistory> taskUserHistoryList = taskUserHistoryRepository.findAll();
        assertThat(taskUserHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskUserHistory.class);
    }
}
