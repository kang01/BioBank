package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.UserLoginHistory;
import org.fwoxford.repository.UserLoginHistoryRepository;
import org.fwoxford.service.UserLoginHistoryService;
import org.fwoxford.service.dto.UserLoginHistoryDTO;
import org.fwoxford.service.mapper.UserLoginHistoryMapper;
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
 * Test class for the UserLoginHistoryResource REST controller.
 *
 * @see UserLoginHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class UserLoginHistoryResourceIntTest {

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
    private UserLoginHistoryRepository userLoginHistoryRepository;

    @Autowired
    private UserLoginHistoryMapper userLoginHistoryMapper;

    @Autowired
    private UserLoginHistoryService userLoginHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserLoginHistoryMockMvc;

    private UserLoginHistory userLoginHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserLoginHistoryResource userLoginHistoryResource = new UserLoginHistoryResource(userLoginHistoryService);
        this.restUserLoginHistoryMockMvc = MockMvcBuilders.standaloneSetup(userLoginHistoryResource)
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
    public static UserLoginHistory createEntity(EntityManager em) {
        UserLoginHistory userLoginHistory = new UserLoginHistory()
                .userToken(DEFAULT_USER_TOKEN)
                .loginUserId(DEFAULT_LOGIN_USER_ID)
                .businessName(DEFAULT_BUSINESS_NAME)
                .invalidDate(DEFAULT_INVALID_DATE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return userLoginHistory;
    }

    @Before
    public void initTest() {
        userLoginHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserLoginHistory() throws Exception {
        int databaseSizeBeforeCreate = userLoginHistoryRepository.findAll().size();

        // Create the UserLoginHistory
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);

        restUserLoginHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLoginHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the UserLoginHistory in the database
        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        UserLoginHistory testUserLoginHistory = userLoginHistoryList.get(userLoginHistoryList.size() - 1);
        assertThat(testUserLoginHistory.getUserToken()).isEqualTo(DEFAULT_USER_TOKEN);
        assertThat(testUserLoginHistory.getLoginUserId()).isEqualTo(DEFAULT_LOGIN_USER_ID);
        assertThat(testUserLoginHistory.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
        assertThat(testUserLoginHistory.getInvalidDate()).isEqualTo(DEFAULT_INVALID_DATE);
        assertThat(testUserLoginHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserLoginHistory.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createUserLoginHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userLoginHistoryRepository.findAll().size();

        // Create the UserLoginHistory with an existing ID
        UserLoginHistory existingUserLoginHistory = new UserLoginHistory();
        existingUserLoginHistory.setId(1L);
        UserLoginHistoryDTO existingUserLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(existingUserLoginHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserLoginHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserLoginHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLoginUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLoginHistoryRepository.findAll().size();
        // set the field null
        userLoginHistory.setLoginUserId(null);

        // Create the UserLoginHistory, which fails.
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);

        restUserLoginHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLoginHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBusinessNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLoginHistoryRepository.findAll().size();
        // set the field null
        userLoginHistory.setBusinessName(null);

        // Create the UserLoginHistory, which fails.
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);

        restUserLoginHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLoginHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvalidDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLoginHistoryRepository.findAll().size();
        // set the field null
        userLoginHistory.setInvalidDate(null);

        // Create the UserLoginHistory, which fails.
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);

        restUserLoginHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLoginHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLoginHistoryRepository.findAll().size();
        // set the field null
        userLoginHistory.setStatus(null);

        // Create the UserLoginHistory, which fails.
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);

        restUserLoginHistoryMockMvc.perform(post("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLoginHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserLoginHistories() throws Exception {
        // Initialize the database
        userLoginHistoryRepository.saveAndFlush(userLoginHistory);

        // Get all the userLoginHistoryList
        restUserLoginHistoryMockMvc.perform(get("/api/user-login-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userLoginHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].userToken").value(hasItem(DEFAULT_USER_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].loginUserId").value(hasItem(DEFAULT_LOGIN_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())))
            .andExpect(jsonPath("$.[*].invalidDate").value(hasItem(sameInstant(DEFAULT_INVALID_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getUserLoginHistory() throws Exception {
        // Initialize the database
        userLoginHistoryRepository.saveAndFlush(userLoginHistory);

        // Get the userLoginHistory
        restUserLoginHistoryMockMvc.perform(get("/api/user-login-histories/{id}", userLoginHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userLoginHistory.getId().intValue()))
            .andExpect(jsonPath("$.userToken").value(DEFAULT_USER_TOKEN.toString()))
            .andExpect(jsonPath("$.loginUserId").value(DEFAULT_LOGIN_USER_ID.intValue()))
            .andExpect(jsonPath("$.businessName").value(DEFAULT_BUSINESS_NAME.toString()))
            .andExpect(jsonPath("$.invalidDate").value(sameInstant(DEFAULT_INVALID_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserLoginHistory() throws Exception {
        // Get the userLoginHistory
        restUserLoginHistoryMockMvc.perform(get("/api/user-login-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserLoginHistory() throws Exception {
        // Initialize the database
        userLoginHistoryRepository.saveAndFlush(userLoginHistory);
        int databaseSizeBeforeUpdate = userLoginHistoryRepository.findAll().size();

        // Update the userLoginHistory
        UserLoginHistory updatedUserLoginHistory = userLoginHistoryRepository.findOne(userLoginHistory.getId());
        updatedUserLoginHistory
                .userToken(UPDATED_USER_TOKEN)
                .loginUserId(UPDATED_LOGIN_USER_ID)
                .businessName(UPDATED_BUSINESS_NAME)
                .invalidDate(UPDATED_INVALID_DATE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(updatedUserLoginHistory);

        restUserLoginHistoryMockMvc.perform(put("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLoginHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the UserLoginHistory in the database
        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeUpdate);
        UserLoginHistory testUserLoginHistory = userLoginHistoryList.get(userLoginHistoryList.size() - 1);
        assertThat(testUserLoginHistory.getUserToken()).isEqualTo(UPDATED_USER_TOKEN);
        assertThat(testUserLoginHistory.getLoginUserId()).isEqualTo(UPDATED_LOGIN_USER_ID);
        assertThat(testUserLoginHistory.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
        assertThat(testUserLoginHistory.getInvalidDate()).isEqualTo(UPDATED_INVALID_DATE);
        assertThat(testUserLoginHistory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserLoginHistory.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingUserLoginHistory() throws Exception {
        int databaseSizeBeforeUpdate = userLoginHistoryRepository.findAll().size();

        // Create the UserLoginHistory
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserLoginHistoryMockMvc.perform(put("/api/user-login-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userLoginHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the UserLoginHistory in the database
        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserLoginHistory() throws Exception {
        // Initialize the database
        userLoginHistoryRepository.saveAndFlush(userLoginHistory);
        int databaseSizeBeforeDelete = userLoginHistoryRepository.findAll().size();

        // Get the userLoginHistory
        restUserLoginHistoryMockMvc.perform(delete("/api/user-login-histories/{id}", userLoginHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll();
        assertThat(userLoginHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLoginHistory.class);
    }
}
