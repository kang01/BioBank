package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutApply;
import org.fwoxford.domain.Delegate;
import org.fwoxford.repository.StockOutApplyRepository;
import org.fwoxford.service.StockOutApplyService;
import org.fwoxford.service.dto.StockOutApplyDTO;
import org.fwoxford.service.mapper.StockOutApplyMapper;
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
 * Test class for the StockOutApplyResource REST controller.
 *
 * @see StockOutApplyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutApplyResourceIntTest {

    private static final LocalDate DEFAULT_START_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PURPOSE_OF_SAMPLE = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE_OF_SAMPLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RECORD_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORD_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_RECORD_ID = 1L;
    private static final Long UPDATED_RECORD_ID = 2L;

    private static final Long DEFAULT_PARENT_APPLY_ID = 1L;
    private static final Long UPDATED_PARENT_APPLY_ID = 2L;

    private static final Long DEFAULT_APPROVER_ID = 1L;
    private static final Long UPDATED_APPROVER_ID = 2L;

    private static final LocalDate DEFAULT_APPROVE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPROVE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_APPLY_PERSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPLY_PERSON_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APPLY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_APPLY_CODE = "BBBBBBBBBB";

    @Autowired
    private StockOutApplyRepository stockOutApplyRepository;

    @Autowired
    private StockOutApplyMapper stockOutApplyMapper;

    @Autowired
    private StockOutApplyService stockOutApplyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutApplyMockMvc;

    private StockOutApply stockOutApply;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutApplyResource stockOutApplyResource = new StockOutApplyResource(stockOutApplyService);
        this.restStockOutApplyMockMvc = MockMvcBuilders.standaloneSetup(stockOutApplyResource)
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
    public static StockOutApply createEntity(EntityManager em) {
        StockOutApply stockOutApply = new StockOutApply()
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME)
                .purposeOfSample(DEFAULT_PURPOSE_OF_SAMPLE)
                .recordTime(DEFAULT_RECORD_TIME)
                .recordId(DEFAULT_RECORD_ID)
                .parentApplyId(DEFAULT_PARENT_APPLY_ID)
                .approverId(DEFAULT_APPROVER_ID)
                .approveTime(DEFAULT_APPROVE_TIME)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .applyPersonName(DEFAULT_APPLY_PERSON_NAME)
                .applyCode(DEFAULT_APPLY_CODE);
        // Add required entity
        Delegate delegate = DelegateResourceIntTest.createEntity(em);
        em.persist(delegate);
        em.flush();
        stockOutApply.setDelegate(delegate);
        return stockOutApply;
    }

    @Before
    public void initTest() {
        stockOutApply = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutApply() throws Exception {
        int databaseSizeBeforeCreate = stockOutApplyRepository.findAll().size();

        // Create the StockOutApply
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);

        restStockOutApplyMockMvc.perform(post("/api/stock-out-applies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutApply in the database
        List<StockOutApply> stockOutApplyList = stockOutApplyRepository.findAll();
        assertThat(stockOutApplyList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutApply testStockOutApply = stockOutApplyList.get(stockOutApplyList.size() - 1);
        assertThat(testStockOutApply.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testStockOutApply.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testStockOutApply.getPurposeOfSample()).isEqualTo(DEFAULT_PURPOSE_OF_SAMPLE);
        assertThat(testStockOutApply.getRecordTime()).isEqualTo(DEFAULT_RECORD_TIME);
        assertThat(testStockOutApply.getRecordId()).isEqualTo(DEFAULT_RECORD_ID);
        assertThat(testStockOutApply.getParentApplyId()).isEqualTo(DEFAULT_PARENT_APPLY_ID);
        assertThat(testStockOutApply.getApproverId()).isEqualTo(DEFAULT_APPROVER_ID);
        assertThat(testStockOutApply.getApproveTime()).isEqualTo(DEFAULT_APPROVE_TIME);
        assertThat(testStockOutApply.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutApply.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockOutApply.getApplyPersonName()).isEqualTo(DEFAULT_APPLY_PERSON_NAME);
        assertThat(testStockOutApply.getApplyCode()).isEqualTo(DEFAULT_APPLY_CODE);
    }

    @Test
    @Transactional
    public void createStockOutApplyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutApplyRepository.findAll().size();

        // Create the StockOutApply with an existing ID
        StockOutApply existingStockOutApply = new StockOutApply();
        existingStockOutApply.setId(1L);
        StockOutApplyDTO existingStockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(existingStockOutApply);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutApplyMockMvc.perform(post("/api/stock-out-applies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutApplyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutApply> stockOutApplyList = stockOutApplyRepository.findAll();
        assertThat(stockOutApplyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutApplyRepository.findAll().size();
        // set the field null
        stockOutApply.setStatus(null);

        // Create the StockOutApply, which fails.
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);

        restStockOutApplyMockMvc.perform(post("/api/stock-out-applies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutApply> stockOutApplyList = stockOutApplyRepository.findAll();
        assertThat(stockOutApplyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutApplyRepository.findAll().size();
        // set the field null
        stockOutApply.setApplyCode(null);

        // Create the StockOutApply, which fails.
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);

        restStockOutApplyMockMvc.perform(post("/api/stock-out-applies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutApply> stockOutApplyList = stockOutApplyRepository.findAll();
        assertThat(stockOutApplyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutApplies() throws Exception {
        // Initialize the database
        stockOutApplyRepository.saveAndFlush(stockOutApply);

        // Get all the stockOutApplyList
        restStockOutApplyMockMvc.perform(get("/api/stock-out-applies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutApply.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].purposeOfSample").value(hasItem(DEFAULT_PURPOSE_OF_SAMPLE.toString())))
            .andExpect(jsonPath("$.[*].recordTime").value(hasItem(DEFAULT_RECORD_TIME.toString())))
            .andExpect(jsonPath("$.[*].recordId").value(hasItem(DEFAULT_RECORD_ID.intValue())))
            .andExpect(jsonPath("$.[*].parentApplyId").value(hasItem(DEFAULT_PARENT_APPLY_ID.intValue())))
            .andExpect(jsonPath("$.[*].approverId").value(hasItem(DEFAULT_APPROVER_ID.intValue())))
            .andExpect(jsonPath("$.[*].approveTime").value(hasItem(DEFAULT_APPROVE_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].applyPersonName").value(hasItem(DEFAULT_APPLY_PERSON_NAME.toString())))
            .andExpect(jsonPath("$.[*].applyCode").value(hasItem(DEFAULT_APPLY_CODE.toString())));
    }

    @Test
    @Transactional
    public void getStockOutApply() throws Exception {
        // Initialize the database
        stockOutApplyRepository.saveAndFlush(stockOutApply);

        // Get the stockOutApply
        restStockOutApplyMockMvc.perform(get("/api/stock-out-applies/{id}", stockOutApply.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutApply.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.purposeOfSample").value(DEFAULT_PURPOSE_OF_SAMPLE.toString()))
            .andExpect(jsonPath("$.recordTime").value(DEFAULT_RECORD_TIME.toString()))
            .andExpect(jsonPath("$.recordId").value(DEFAULT_RECORD_ID.intValue()))
            .andExpect(jsonPath("$.parentApplyId").value(DEFAULT_PARENT_APPLY_ID.intValue()))
            .andExpect(jsonPath("$.approverId").value(DEFAULT_APPROVER_ID.intValue()))
            .andExpect(jsonPath("$.approveTime").value(DEFAULT_APPROVE_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.applyPersonName").value(DEFAULT_APPLY_PERSON_NAME.toString()))
            .andExpect(jsonPath("$.applyCode").value(DEFAULT_APPLY_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutApply() throws Exception {
        // Get the stockOutApply
        restStockOutApplyMockMvc.perform(get("/api/stock-out-applies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutApply() throws Exception {
        // Initialize the database
        stockOutApplyRepository.saveAndFlush(stockOutApply);
        int databaseSizeBeforeUpdate = stockOutApplyRepository.findAll().size();

        // Update the stockOutApply
        StockOutApply updatedStockOutApply = stockOutApplyRepository.findOne(stockOutApply.getId());
        updatedStockOutApply
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME)
                .purposeOfSample(UPDATED_PURPOSE_OF_SAMPLE)
                .recordTime(UPDATED_RECORD_TIME)
                .recordId(UPDATED_RECORD_ID)
                .parentApplyId(UPDATED_PARENT_APPLY_ID)
                .approverId(UPDATED_APPROVER_ID)
                .approveTime(UPDATED_APPROVE_TIME)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .applyPersonName(UPDATED_APPLY_PERSON_NAME)
                .applyCode(UPDATED_APPLY_CODE);
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(updatedStockOutApply);

        restStockOutApplyMockMvc.perform(put("/api/stock-out-applies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutApply in the database
        List<StockOutApply> stockOutApplyList = stockOutApplyRepository.findAll();
        assertThat(stockOutApplyList).hasSize(databaseSizeBeforeUpdate);
        StockOutApply testStockOutApply = stockOutApplyList.get(stockOutApplyList.size() - 1);
        assertThat(testStockOutApply.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testStockOutApply.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testStockOutApply.getPurposeOfSample()).isEqualTo(UPDATED_PURPOSE_OF_SAMPLE);
        assertThat(testStockOutApply.getRecordTime()).isEqualTo(UPDATED_RECORD_TIME);
        assertThat(testStockOutApply.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
        assertThat(testStockOutApply.getParentApplyId()).isEqualTo(UPDATED_PARENT_APPLY_ID);
        assertThat(testStockOutApply.getApproverId()).isEqualTo(UPDATED_APPROVER_ID);
        assertThat(testStockOutApply.getApproveTime()).isEqualTo(UPDATED_APPROVE_TIME);
        assertThat(testStockOutApply.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutApply.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockOutApply.getApplyPersonName()).isEqualTo(UPDATED_APPLY_PERSON_NAME);
        assertThat(testStockOutApply.getApplyCode()).isEqualTo(UPDATED_APPLY_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutApply() throws Exception {
        int databaseSizeBeforeUpdate = stockOutApplyRepository.findAll().size();

        // Create the StockOutApply
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutApplyMockMvc.perform(put("/api/stock-out-applies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutApply in the database
        List<StockOutApply> stockOutApplyList = stockOutApplyRepository.findAll();
        assertThat(stockOutApplyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutApply() throws Exception {
        // Initialize the database
        stockOutApplyRepository.saveAndFlush(stockOutApply);
        int databaseSizeBeforeDelete = stockOutApplyRepository.findAll().size();

        // Get the stockOutApply
        restStockOutApplyMockMvc.perform(delete("/api/stock-out-applies/{id}", stockOutApply.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutApply> stockOutApplyList = stockOutApplyRepository.findAll();
        assertThat(stockOutApplyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutApply.class);
    }
}
