package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutHandoverDetails;
import org.fwoxford.domain.StockOutHandover;
import org.fwoxford.domain.StockOutBoxTube;
import org.fwoxford.repository.StockOutHandoverDetailsRepository;
import org.fwoxford.service.StockOutHandoverDetailsService;
import org.fwoxford.service.dto.StockOutHandoverDetailsDTO;
import org.fwoxford.service.mapper.StockOutHandoverDetailsMapper;
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
 * Test class for the StockOutHandoverDetailsResource REST controller.
 *
 * @see StockOutHandoverDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutHandoverDetailsResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutHandoverDetailsRepository stockOutHandoverDetailsRepository;

    @Autowired
    private StockOutHandoverDetailsMapper stockOutHandoverDetailsMapper;

    @Autowired
    private StockOutHandoverDetailsService stockOutHandoverDetailsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutHandoverDetailsMockMvc;

    private StockOutHandoverDetails stockOutHandoverDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutHandoverDetailsResource stockOutHandoverDetailsResource = new StockOutHandoverDetailsResource(stockOutHandoverDetailsService);
        this.restStockOutHandoverDetailsMockMvc = MockMvcBuilders.standaloneSetup(stockOutHandoverDetailsResource)
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
    public static StockOutHandoverDetails createEntity(EntityManager em) {
        StockOutHandoverDetails stockOutHandoverDetails = new StockOutHandoverDetails()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutHandover stockOutHandover = StockOutHandoverResourceIntTest.createEntity(em);
        em.persist(stockOutHandover);
        em.flush();
//        stockOutHandoverDetails.setStockOutHandover(stockOutHandover);
        // Add required entity
        StockOutBoxTube stockOutBoxTube = StockOutBoxTubeResourceIntTest.createEntity(em);
        em.persist(stockOutBoxTube);
        em.flush();
//        stockOutHandoverDetails.setStockOutBoxTube(stockOutBoxTube);
        return stockOutHandoverDetails;
    }

    @Before
    public void initTest() {
        stockOutHandoverDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutHandoverDetails() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandoverDetailsRepository.findAll().size();

        // Create the StockOutHandoverDetails
        StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO = stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(stockOutHandoverDetails);

        restStockOutHandoverDetailsMockMvc.perform(post("/api/stock-out-handover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandoverDetails in the database
        List<StockOutHandoverDetails> stockOutHandoverDetailsList = stockOutHandoverDetailsRepository.findAll();
        assertThat(stockOutHandoverDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutHandoverDetails testStockOutHandoverDetails = stockOutHandoverDetailsList.get(stockOutHandoverDetailsList.size() - 1);
        assertThat(testStockOutHandoverDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutHandoverDetails.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutHandoverDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutHandoverDetailsRepository.findAll().size();

        // Create the StockOutHandoverDetails with an existing ID
        StockOutHandoverDetails existingStockOutHandoverDetails = new StockOutHandoverDetails();
        existingStockOutHandoverDetails.setId(1L);
        StockOutHandoverDetailsDTO existingStockOutHandoverDetailsDTO = stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(existingStockOutHandoverDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutHandoverDetailsMockMvc.perform(post("/api/stock-out-handover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutHandoverDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutHandoverDetails> stockOutHandoverDetailsList = stockOutHandoverDetailsRepository.findAll();
        assertThat(stockOutHandoverDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutHandoverDetailsRepository.findAll().size();
        // set the field null
        stockOutHandoverDetails.setStatus(null);

        // Create the StockOutHandoverDetails, which fails.
        StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO = stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(stockOutHandoverDetails);

        restStockOutHandoverDetailsMockMvc.perform(post("/api/stock-out-handover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutHandoverDetails> stockOutHandoverDetailsList = stockOutHandoverDetailsRepository.findAll();
        assertThat(stockOutHandoverDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutHandoverDetails() throws Exception {
        // Initialize the database
        stockOutHandoverDetailsRepository.saveAndFlush(stockOutHandoverDetails);

        // Get all the stockOutHandoverDetailsList
        restStockOutHandoverDetailsMockMvc.perform(get("/api/stock-out-handover-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutHandoverDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutHandoverDetails() throws Exception {
        // Initialize the database
        stockOutHandoverDetailsRepository.saveAndFlush(stockOutHandoverDetails);

        // Get the stockOutHandoverDetails
        restStockOutHandoverDetailsMockMvc.perform(get("/api/stock-out-handover-details/{id}", stockOutHandoverDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutHandoverDetails.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutHandoverDetails() throws Exception {
        // Get the stockOutHandoverDetails
        restStockOutHandoverDetailsMockMvc.perform(get("/api/stock-out-handover-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutHandoverDetails() throws Exception {
        // Initialize the database
        stockOutHandoverDetailsRepository.saveAndFlush(stockOutHandoverDetails);
        int databaseSizeBeforeUpdate = stockOutHandoverDetailsRepository.findAll().size();

        // Update the stockOutHandoverDetails
        StockOutHandoverDetails updatedStockOutHandoverDetails = stockOutHandoverDetailsRepository.findOne(stockOutHandoverDetails.getId());
        updatedStockOutHandoverDetails
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO = stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(updatedStockOutHandoverDetails);

        restStockOutHandoverDetailsMockMvc.perform(put("/api/stock-out-handover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutHandoverDetails in the database
        List<StockOutHandoverDetails> stockOutHandoverDetailsList = stockOutHandoverDetailsRepository.findAll();
        assertThat(stockOutHandoverDetailsList).hasSize(databaseSizeBeforeUpdate);
        StockOutHandoverDetails testStockOutHandoverDetails = stockOutHandoverDetailsList.get(stockOutHandoverDetailsList.size() - 1);
        assertThat(testStockOutHandoverDetails.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutHandoverDetails.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutHandoverDetails() throws Exception {
        int databaseSizeBeforeUpdate = stockOutHandoverDetailsRepository.findAll().size();

        // Create the StockOutHandoverDetails
        StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO = stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(stockOutHandoverDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutHandoverDetailsMockMvc.perform(put("/api/stock-out-handover-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutHandoverDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutHandoverDetails in the database
        List<StockOutHandoverDetails> stockOutHandoverDetailsList = stockOutHandoverDetailsRepository.findAll();
        assertThat(stockOutHandoverDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutHandoverDetails() throws Exception {
        // Initialize the database
        stockOutHandoverDetailsRepository.saveAndFlush(stockOutHandoverDetails);
        int databaseSizeBeforeDelete = stockOutHandoverDetailsRepository.findAll().size();

        // Get the stockOutHandoverDetails
        restStockOutHandoverDetailsMockMvc.perform(delete("/api/stock-out-handover-details/{id}", stockOutHandoverDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutHandoverDetails> stockOutHandoverDetailsList = stockOutHandoverDetailsRepository.findAll();
        assertThat(stockOutHandoverDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutHandoverDetails.class);
    }
}
