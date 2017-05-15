package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutHandover;
import org.fwoxford.domain.StockOutTask;
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
 * Test class for the StockOutHandoverResource REST controller.
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
    private StockOutHandoverRepository stockOutHandoverRepository;

    @Autowired
    private StockOutHandoverMapper stockOutHandoverMapper;

    @Autowired
    private StockOutHandoverService stockOutHandoverService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutHandoverMockMvc;

    private StockOutHandover stockOutHandover;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutHandoverResource stockOutHandoverResource = new StockOutHandoverResource(stockOutHandoverService);
        this.restStockOutHandoverMockMvc = MockMvcBuilders.standaloneSetup(stockOutHandoverResource)
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
        StockOutHandover stockOutHandover = new StockOutHandover()
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
        stockOutHandover.setStockOutTask(stockOutTask);
        return stockOutHandover;
    }

    @Before
    public void initTest() {
        stockOutHandover = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutHandover() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandoverRepository.findAll().size();

        // Create the StockOutHandover
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(stockOutHandover);

        restStockOutHandoverMockMvc.perform(post("/api/stock-out-handovers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandover in the database
        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
        assertThat(stockOutHandoverList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutHandover testStockOutHandover = stockOutHandoverList.get(stockOutHandoverList.size() - 1);
        assertThat(testStockOutHandover.getHandoverCode()).isEqualTo(DEFAULT_HANDOVER_CODE);
        assertThat(testStockOutHandover.getReceiverName()).isEqualTo(DEFAULT_RECEIVER_NAME);
        assertThat(testStockOutHandover.getReceiverPhone()).isEqualTo(DEFAULT_RECEIVER_PHONE);
        assertThat(testStockOutHandover.getReceiverOrganization()).isEqualTo(DEFAULT_RECEIVER_ORGANIZATION);
        assertThat(testStockOutHandover.getHandoverPersonId()).isEqualTo(DEFAULT_HANDOVER_PERSON_ID);
        assertThat(testStockOutHandover.getHandoverTime()).isEqualTo(DEFAULT_HANDOVER_TIME);
        assertThat(testStockOutHandover.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutHandover.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutHandoverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandoverRepository.findAll().size();

        // Create the StockOutHandover with an existing ID
        StockOutHandover existingStockOutHandover = new StockOutHandover();
        existingStockOutHandover.setId(1L);
        StockOutHandoverDTO existingStockOutHandoverDTO = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(existingStockOutHandover);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutHandoverMockMvc.perform(post("/api/stock-out-handovers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutHandoverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
        assertThat(stockOutHandoverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHandoverCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutHandoverRepository.findAll().size();
        // set the field null
        stockOutHandover.setHandoverCode(null);

        // Create the StockOutHandover, which fails.
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(stockOutHandover);

        restStockOutHandoverMockMvc.perform(post("/api/stock-out-handovers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
        assertThat(stockOutHandoverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutHandoverRepository.findAll().size();
        // set the field null
        stockOutHandover.setStatus(null);

        // Create the StockOutHandover, which fails.
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(stockOutHandover);

        restStockOutHandoverMockMvc.perform(post("/api/stock-out-handovers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
        assertThat(stockOutHandoverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutHandovers() throws Exception {
        // Initialize the database
        stockOutHandoverRepository.saveAndFlush(stockOutHandover);

        // Get all the stockOutHandoverList
        restStockOutHandoverMockMvc.perform(get("/api/stock-out-handovers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutHandover.getId().intValue())))
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
    public void getStockOutHandover() throws Exception {
        // Initialize the database
        stockOutHandoverRepository.saveAndFlush(stockOutHandover);

        // Get the stockOutHandover
        restStockOutHandoverMockMvc.perform(get("/api/stock-out-handovers/{id}", stockOutHandover.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutHandover.getId().intValue()))
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
    public void getNonExistingStockOutHandover() throws Exception {
        // Get the stockOutHandover
        restStockOutHandoverMockMvc.perform(get("/api/stock-out-handovers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutHandover() throws Exception {
        // Initialize the database
        stockOutHandoverRepository.saveAndFlush(stockOutHandover);
        int databaseSizeBeforeUpdate = stockOutHandoverRepository.findAll().size();

        // Update the stockOutHandover
        StockOutHandover updatedStockOutHandover = stockOutHandoverRepository.findOne(stockOutHandover.getId());
        updatedStockOutHandover
                .handoverCode(UPDATED_HANDOVER_CODE)
                .receiverName(UPDATED_RECEIVER_NAME)
                .receiverPhone(UPDATED_RECEIVER_PHONE)
                .receiverOrganization(UPDATED_RECEIVER_ORGANIZATION)
                .handoverPersonId(UPDATED_HANDOVER_PERSON_ID)
                .handoverTime(UPDATED_HANDOVER_TIME)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(updatedStockOutHandover);

        restStockOutHandoverMockMvc.perform(put("/api/stock-out-handovers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutHandover in the database
        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
        assertThat(stockOutHandoverList).hasSize(databaseSizeBeforeUpdate);
        StockOutHandover testStockOutHandover = stockOutHandoverList.get(stockOutHandoverList.size() - 1);
        assertThat(testStockOutHandover.getHandoverCode()).isEqualTo(UPDATED_HANDOVER_CODE);
        assertThat(testStockOutHandover.getReceiverName()).isEqualTo(UPDATED_RECEIVER_NAME);
        assertThat(testStockOutHandover.getReceiverPhone()).isEqualTo(UPDATED_RECEIVER_PHONE);
        assertThat(testStockOutHandover.getReceiverOrganization()).isEqualTo(UPDATED_RECEIVER_ORGANIZATION);
        assertThat(testStockOutHandover.getHandoverPersonId()).isEqualTo(UPDATED_HANDOVER_PERSON_ID);
        assertThat(testStockOutHandover.getHandoverTime()).isEqualTo(UPDATED_HANDOVER_TIME);
        assertThat(testStockOutHandover.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutHandover.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutHandover() throws Exception {
        int databaseSizeBeforeUpdate = stockOutHandoverRepository.findAll().size();

        // Create the StockOutHandover
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(stockOutHandover);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutHandoverMockMvc.perform(put("/api/stock-out-handovers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandover in the database
        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
        assertThat(stockOutHandoverList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutHandover() throws Exception {
        // Initialize the database
        stockOutHandoverRepository.saveAndFlush(stockOutHandover);
        int databaseSizeBeforeDelete = stockOutHandoverRepository.findAll().size();

        // Get the stockOutHandover
        restStockOutHandoverMockMvc.perform(delete("/api/stock-out-handovers/{id}", stockOutHandover.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
        assertThat(stockOutHandoverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutHandover.class);
    }
}
