package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutRequiredSample;
import org.fwoxford.domain.StockOutRequirement;
import org.fwoxford.repository.StockOutRequiredSampleRepository;
import org.fwoxford.service.StockOutRequiredSampleService;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;
import org.fwoxford.service.mapper.StockOutRequiredSampleMapper;
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
 * Test class for the StockOutRequiredSampleResource REST controller.
 *
 * @see StockOutRequiredSampleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutRequiredSampleResourceIntTest {

    private static final String DEFAULT_SAMPLE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutRequiredSampleRepository stockOutRequiredSampleRepository;

    @Autowired
    private StockOutRequiredSampleMapper stockOutRequiredSampleMapper;

    @Autowired
    private StockOutRequiredSampleService stockOutRequiredSampleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutRequiredSampleMockMvc;

    private StockOutRequiredSample stockOutRequiredSample;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutRequiredSampleResource stockOutRequiredSampleResource = new StockOutRequiredSampleResource(stockOutRequiredSampleService);
        this.restStockOutRequiredSampleMockMvc = MockMvcBuilders.standaloneSetup(stockOutRequiredSampleResource)
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
    public static StockOutRequiredSample createEntity(EntityManager em) {
        StockOutRequiredSample stockOutRequiredSample = new StockOutRequiredSample()
                .sampleCode(DEFAULT_SAMPLE_CODE)
                .sampleType(DEFAULT_SAMPLE_TYPE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutRequirement stockOutRequirement = StockOutRequirementResourceIntTest.createEntity(em);
        em.persist(stockOutRequirement);
        em.flush();
        stockOutRequiredSample.setStockOutRequirement(stockOutRequirement);
        return stockOutRequiredSample;
    }

    @Before
    public void initTest() {
        stockOutRequiredSample = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutRequiredSample() throws Exception {
        int databaseSizeBeforeCreate = stockOutRequiredSampleRepository.findAll().size();

        // Create the StockOutRequiredSample
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample);

        restStockOutRequiredSampleMockMvc.perform(post("/api/stock-out-required-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequiredSampleDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutRequiredSample in the database
        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutRequiredSample testStockOutRequiredSample = stockOutRequiredSampleList.get(stockOutRequiredSampleList.size() - 1);
        assertThat(testStockOutRequiredSample.getSampleCode()).isEqualTo(DEFAULT_SAMPLE_CODE);
        assertThat(testStockOutRequiredSample.getSampleType()).isEqualTo(DEFAULT_SAMPLE_TYPE);
        assertThat(testStockOutRequiredSample.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutRequiredSample.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutRequiredSampleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutRequiredSampleRepository.findAll().size();

        // Create the StockOutRequiredSample with an existing ID
        StockOutRequiredSample existingStockOutRequiredSample = new StockOutRequiredSample();
        existingStockOutRequiredSample.setId(1L);
        StockOutRequiredSampleDTO existingStockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(existingStockOutRequiredSample);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutRequiredSampleMockMvc.perform(post("/api/stock-out-required-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutRequiredSampleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSampleCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutRequiredSampleRepository.findAll().size();
        // set the field null
        stockOutRequiredSample.setSampleCode(null);

        // Create the StockOutRequiredSample, which fails.
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample);

        restStockOutRequiredSampleMockMvc.perform(post("/api/stock-out-required-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequiredSampleDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutRequiredSampleRepository.findAll().size();
        // set the field null
        stockOutRequiredSample.setSampleType(null);

        // Create the StockOutRequiredSample, which fails.
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample);

        restStockOutRequiredSampleMockMvc.perform(post("/api/stock-out-required-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequiredSampleDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutRequiredSampleRepository.findAll().size();
        // set the field null
        stockOutRequiredSample.setStatus(null);

        // Create the StockOutRequiredSample, which fails.
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample);

        restStockOutRequiredSampleMockMvc.perform(post("/api/stock-out-required-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequiredSampleDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutRequiredSamples() throws Exception {
        // Initialize the database
        stockOutRequiredSampleRepository.saveAndFlush(stockOutRequiredSample);

        // Get all the stockOutRequiredSampleList
        restStockOutRequiredSampleMockMvc.perform(get("/api/stock-out-required-samples?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutRequiredSample.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleCode").value(hasItem(DEFAULT_SAMPLE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sampleType").value(hasItem(DEFAULT_SAMPLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutRequiredSample() throws Exception {
        // Initialize the database
        stockOutRequiredSampleRepository.saveAndFlush(stockOutRequiredSample);

        // Get the stockOutRequiredSample
        restStockOutRequiredSampleMockMvc.perform(get("/api/stock-out-required-samples/{id}", stockOutRequiredSample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutRequiredSample.getId().intValue()))
            .andExpect(jsonPath("$.sampleCode").value(DEFAULT_SAMPLE_CODE.toString()))
            .andExpect(jsonPath("$.sampleType").value(DEFAULT_SAMPLE_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutRequiredSample() throws Exception {
        // Get the stockOutRequiredSample
        restStockOutRequiredSampleMockMvc.perform(get("/api/stock-out-required-samples/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutRequiredSample() throws Exception {
        // Initialize the database
        stockOutRequiredSampleRepository.saveAndFlush(stockOutRequiredSample);
        int databaseSizeBeforeUpdate = stockOutRequiredSampleRepository.findAll().size();

        // Update the stockOutRequiredSample
        StockOutRequiredSample updatedStockOutRequiredSample = stockOutRequiredSampleRepository.findOne(stockOutRequiredSample.getId());
        updatedStockOutRequiredSample
                .sampleCode(UPDATED_SAMPLE_CODE)
                .sampleType(UPDATED_SAMPLE_TYPE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(updatedStockOutRequiredSample);

        restStockOutRequiredSampleMockMvc.perform(put("/api/stock-out-required-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequiredSampleDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutRequiredSample in the database
        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeUpdate);
        StockOutRequiredSample testStockOutRequiredSample = stockOutRequiredSampleList.get(stockOutRequiredSampleList.size() - 1);
        assertThat(testStockOutRequiredSample.getSampleCode()).isEqualTo(UPDATED_SAMPLE_CODE);
        assertThat(testStockOutRequiredSample.getSampleType()).isEqualTo(UPDATED_SAMPLE_TYPE);
        assertThat(testStockOutRequiredSample.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutRequiredSample.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutRequiredSample() throws Exception {
        int databaseSizeBeforeUpdate = stockOutRequiredSampleRepository.findAll().size();

        // Create the StockOutRequiredSample
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutRequiredSampleMockMvc.perform(put("/api/stock-out-required-samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutRequiredSampleDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutRequiredSample in the database
        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutRequiredSample() throws Exception {
        // Initialize the database
        stockOutRequiredSampleRepository.saveAndFlush(stockOutRequiredSample);
        int databaseSizeBeforeDelete = stockOutRequiredSampleRepository.findAll().size();

        // Get the stockOutRequiredSample
        restStockOutRequiredSampleMockMvc.perform(delete("/api/stock-out-required-samples/{id}", stockOutRequiredSample.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutRequiredSample> stockOutRequiredSampleList = stockOutRequiredSampleRepository.findAll();
        assertThat(stockOutRequiredSampleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutRequiredSample.class);
    }
}
