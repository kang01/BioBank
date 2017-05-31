package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutHandover;
import org.fwoxford.domain.StockOutTask;
import org.fwoxford.domain.StockOutApply;
import org.fwoxford.repository.StockOutHandoverRepository;
import org.fwoxford.service.StockOutHandoverService;
import org.fwoxford.service.dto.StockOutHandoverDTO;
import org.fwoxford.service.mapper.StockOutHandoverMapper;
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
 * Test class for the StockOutHandOverResource REST controller.
 *
 * @see StockOutHandoverResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutHandoverResourceIntTest {

    private static final String DEFAULT_HANDOVER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_HANDOVER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER_ORGANIZATION = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER_ORGANIZATION = "BBBBBBBBBB";

    private static final Long DEFAULT_HANDOVER_PERSON_ID = 1L;
    private static final Long UPDATED_HANDOVER_PERSON_ID = 2L;

    private static final LocalDate DEFAULT_HANDOVER_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HANDOVER_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutHandoverRepository stockOutHandOverRepository;

    @Autowired
    private StockOutHandoverMapper stockOutHandOverMapper;

    @Autowired
    private StockOutHandoverService stockOutHandOverService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutHandOverMockMvc;

    private StockOutHandover stockOutHandOver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutHandoverResource stockOutHandOverResource = new StockOutHandoverResource(stockOutHandOverService);
        this.restStockOutHandOverMockMvc = MockMvcBuilders.standaloneSetup(stockOutHandOverResource)
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
    public static StockOutHandover createEntity(EntityManager em) {
        StockOutHandover stockOutHandOver = new StockOutHandover()
                .handoverCode(DEFAULT_HANDOVER_CODE)
                .receiverName(DEFAULT_RECEIVER_NAME)
                .receiverPhone(DEFAULT_RECEIVER_PHONE)
                .receiverOrganization(DEFAULT_RECEIVER_ORGANIZATION)
                .handoverPersonId(DEFAULT_HANDOVER_PERSON_ID)
                .handoverTime(DEFAULT_HANDOVER_TIME)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutTask stockOutTask = StockOutTaskResourceIntTest.createEntity(em);
        em.persist(stockOutTask);
        em.flush();
        stockOutHandOver.setStockOutTask(stockOutTask);
        // Add required entity
        StockOutApply stockOutApply = StockOutApplyResourceIntTest.createEntity(em);
        em.persist(stockOutApply);
        em.flush();
        stockOutHandOver.setStockOutApply(stockOutApply);
        return stockOutHandOver;
    }

    @Before
    public void initTest() {
        stockOutHandOver = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutHandOver() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandOverRepository.findAll().size();

        // Create the StockOutHandOver
        StockOutHandoverDTO stockOutHandOverDTO = stockOutHandOverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandOver);

        restStockOutHandOverMockMvc.perform(post("/api/stock-out-hand-overs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandOverDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandOver in the database
        List<StockOutHandover> stockOutHandOverList = stockOutHandOverRepository.findAll();
        assertThat(stockOutHandOverList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutHandover testStockOutHandOver = stockOutHandOverList.get(stockOutHandOverList.size() - 1);
        assertThat(testStockOutHandOver.getHandoverCode()).isEqualTo(DEFAULT_HANDOVER_CODE);
        assertThat(testStockOutHandOver.getReceiverName()).isEqualTo(DEFAULT_RECEIVER_NAME);
        assertThat(testStockOutHandOver.getReceiverPhone()).isEqualTo(DEFAULT_RECEIVER_PHONE);
        assertThat(testStockOutHandOver.getReceiverOrganization()).isEqualTo(DEFAULT_RECEIVER_ORGANIZATION);
        assertThat(testStockOutHandOver.getHandoverPersonId()).isEqualTo(DEFAULT_HANDOVER_PERSON_ID);
        assertThat(testStockOutHandOver.getHandoverTime()).isEqualTo(DEFAULT_HANDOVER_TIME);
        assertThat(testStockOutHandOver.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutHandOver.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutHandOverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandOverRepository.findAll().size();

        // Create the StockOutHandOver with an existing ID
        StockOutHandover existingStockOutHandOver = new StockOutHandover();
        existingStockOutHandOver.setId(1L);
        StockOutHandoverDTO existingStockOutHandOverDTO = stockOutHandOverMapper.stockOutHandOverToStockOutHandOverDTO(existingStockOutHandOver);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutHandOverMockMvc.perform(post("/api/stock-out-hand-overs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutHandOverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutHandover> stockOutHandOverList = stockOutHandOverRepository.findAll();
        assertThat(stockOutHandOverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHandoverCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutHandOverRepository.findAll().size();
        // set the field null
        stockOutHandOver.setHandoverCode(null);

        // Create the StockOutHandOver, which fails.
        StockOutHandoverDTO stockOutHandOverDTO = stockOutHandOverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandOver);

        restStockOutHandOverMockMvc.perform(post("/api/stock-out-hand-overs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandOverDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutHandover> stockOutHandOverList = stockOutHandOverRepository.findAll();
        assertThat(stockOutHandOverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutHandOverRepository.findAll().size();
        // set the field null
        stockOutHandOver.setStatus(null);

        // Create the StockOutHandOver, which fails.
        StockOutHandoverDTO stockOutHandOverDTO = stockOutHandOverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandOver);

        restStockOutHandOverMockMvc.perform(post("/api/stock-out-hand-overs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandOverDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutHandover> stockOutHandOverList = stockOutHandOverRepository.findAll();
        assertThat(stockOutHandOverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutHandOvers() throws Exception {
        // Initialize the database
        stockOutHandOverRepository.saveAndFlush(stockOutHandOver);

        // Get all the stockOutHandOverList
        restStockOutHandOverMockMvc.perform(get("/api/stock-out-hand-overs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutHandOver.getId().intValue())))
            .andExpect(jsonPath("$.[*].handoverCode").value(hasItem(DEFAULT_HANDOVER_CODE.toString())))
            .andExpect(jsonPath("$.[*].receiverName").value(hasItem(DEFAULT_RECEIVER_NAME.toString())))
            .andExpect(jsonPath("$.[*].receiverPhone").value(hasItem(DEFAULT_RECEIVER_PHONE.toString())))
            .andExpect(jsonPath("$.[*].receiverOrganization").value(hasItem(DEFAULT_RECEIVER_ORGANIZATION.toString())))
            .andExpect(jsonPath("$.[*].handoverPersonId").value(hasItem(DEFAULT_HANDOVER_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].handoverTime").value(hasItem(DEFAULT_HANDOVER_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutHandOver() throws Exception {
        // Initialize the database
        stockOutHandOverRepository.saveAndFlush(stockOutHandOver);

        // Get the stockOutHandOver
        restStockOutHandOverMockMvc.perform(get("/api/stock-out-hand-overs/{id}", stockOutHandOver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutHandOver.getId().intValue()))
            .andExpect(jsonPath("$.handoverCode").value(DEFAULT_HANDOVER_CODE.toString()))
            .andExpect(jsonPath("$.receiverName").value(DEFAULT_RECEIVER_NAME.toString()))
            .andExpect(jsonPath("$.receiverPhone").value(DEFAULT_RECEIVER_PHONE.toString()))
            .andExpect(jsonPath("$.receiverOrganization").value(DEFAULT_RECEIVER_ORGANIZATION.toString()))
            .andExpect(jsonPath("$.handoverPersonId").value(DEFAULT_HANDOVER_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.handoverTime").value(DEFAULT_HANDOVER_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutHandOver() throws Exception {
        // Get the stockOutHandOver
        restStockOutHandOverMockMvc.perform(get("/api/stock-out-hand-overs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutHandOver() throws Exception {
        // Initialize the database
        stockOutHandOverRepository.saveAndFlush(stockOutHandOver);
        int databaseSizeBeforeUpdate = stockOutHandOverRepository.findAll().size();

        // Update the stockOutHandOver
        StockOutHandover updatedStockOutHandOver = stockOutHandOverRepository.findOne(stockOutHandOver.getId());
        updatedStockOutHandOver
                .handoverCode(UPDATED_HANDOVER_CODE)
                .receiverName(UPDATED_RECEIVER_NAME)
                .receiverPhone(UPDATED_RECEIVER_PHONE)
                .receiverOrganization(UPDATED_RECEIVER_ORGANIZATION)
                .handoverPersonId(UPDATED_HANDOVER_PERSON_ID)
                .handoverTime(UPDATED_HANDOVER_TIME)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutHandoverDTO stockOutHandOverDTO = stockOutHandOverMapper.stockOutHandOverToStockOutHandOverDTO(updatedStockOutHandOver);

        restStockOutHandOverMockMvc.perform(put("/api/stock-out-hand-overs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandOverDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutHandOver in the database
        List<StockOutHandover> stockOutHandOverList = stockOutHandOverRepository.findAll();
        assertThat(stockOutHandOverList).hasSize(databaseSizeBeforeUpdate);
        StockOutHandover testStockOutHandOver = stockOutHandOverList.get(stockOutHandOverList.size() - 1);
        assertThat(testStockOutHandOver.getHandoverCode()).isEqualTo(UPDATED_HANDOVER_CODE);
        assertThat(testStockOutHandOver.getReceiverName()).isEqualTo(UPDATED_RECEIVER_NAME);
        assertThat(testStockOutHandOver.getReceiverPhone()).isEqualTo(UPDATED_RECEIVER_PHONE);
        assertThat(testStockOutHandOver.getReceiverOrganization()).isEqualTo(UPDATED_RECEIVER_ORGANIZATION);
        assertThat(testStockOutHandOver.getHandoverPersonId()).isEqualTo(UPDATED_HANDOVER_PERSON_ID);
        assertThat(testStockOutHandOver.getHandoverTime()).isEqualTo(UPDATED_HANDOVER_TIME);
        assertThat(testStockOutHandOver.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutHandOver.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutHandOver() throws Exception {
        int databaseSizeBeforeUpdate = stockOutHandOverRepository.findAll().size();

        // Create the StockOutHandOver
        StockOutHandoverDTO stockOutHandOverDTO = stockOutHandOverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandOver);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutHandOverMockMvc.perform(put("/api/stock-out-hand-overs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandOverDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandOver in the database
        List<StockOutHandover> stockOutHandOverList = stockOutHandOverRepository.findAll();
        assertThat(stockOutHandOverList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutHandOver() throws Exception {
        // Initialize the database
        stockOutHandOverRepository.saveAndFlush(stockOutHandOver);
        int databaseSizeBeforeDelete = stockOutHandOverRepository.findAll().size();

        // Get the stockOutHandOver
        restStockOutHandOverMockMvc.perform(delete("/api/stock-out-hand-overs/{id}", stockOutHandOver.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutHandover> stockOutHandOverList = stockOutHandOverRepository.findAll();
        assertThat(stockOutHandOverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutHandover.class);
    }
}
