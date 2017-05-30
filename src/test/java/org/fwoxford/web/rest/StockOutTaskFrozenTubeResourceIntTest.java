package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutTaskFrozenTube;
import org.fwoxford.domain.StockOutTask;
import org.fwoxford.domain.StockOutPlanFrozenTube;
import org.fwoxford.repository.StockOutTaskFrozenTubeRepository;
import org.fwoxford.service.StockOutTaskFrozenTubeService;
import org.fwoxford.service.dto.StockOutTaskFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutTaskFrozenTubeMapper;
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
 * Test class for the StockOutTaskFrozenTubeResource REST controller.
 *
 * @see StockOutTaskFrozenTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutTaskFrozenTubeResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    @Autowired
    private StockOutTaskFrozenTubeMapper stockOutTaskFrozenTubeMapper;

    @Autowired
    private StockOutTaskFrozenTubeService stockOutTaskFrozenTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutTaskFrozenTubeMockMvc;

    private StockOutTaskFrozenTube stockOutTaskFrozenTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutTaskFrozenTubeResource stockOutTaskFrozenTubeResource = new StockOutTaskFrozenTubeResource(stockOutTaskFrozenTubeService);
        this.restStockOutTaskFrozenTubeMockMvc = MockMvcBuilders.standaloneSetup(stockOutTaskFrozenTubeResource)
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
    public static StockOutTaskFrozenTube createEntity(EntityManager em) {
        StockOutTaskFrozenTube stockOutTaskFrozenTube = new StockOutTaskFrozenTube()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutTask stockOutTask = StockOutTaskResourceIntTest.createEntity(em);
        em.persist(stockOutTask);
        em.flush();
        stockOutTaskFrozenTube.setStockOutTask(stockOutTask);
        // Add required entity
        StockOutPlanFrozenTube stockOutPlanFrozenTube = StockOutPlanFrozenTubeResourceIntTest.createEntity(em);
        em.persist(stockOutPlanFrozenTube);
        em.flush();
        stockOutTaskFrozenTube.setStockOutPlanFrozenTube(stockOutPlanFrozenTube);
        return stockOutTaskFrozenTube;
    }

    @Before
    public void initTest() {
        stockOutTaskFrozenTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutTaskFrozenTube() throws Exception {
        int databaseSizeBeforeCreate = stockOutTaskFrozenTubeRepository.findAll().size();

        // Create the StockOutTaskFrozenTube
        StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube);

        restStockOutTaskFrozenTubeMockMvc.perform(post("/api/stock-out-task-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutTaskFrozenTube in the database
        List<StockOutTaskFrozenTube> stockOutTaskFrozenTubeList = stockOutTaskFrozenTubeRepository.findAll();
        assertThat(stockOutTaskFrozenTubeList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutTaskFrozenTube testStockOutTaskFrozenTube = stockOutTaskFrozenTubeList.get(stockOutTaskFrozenTubeList.size() - 1);
        assertThat(testStockOutTaskFrozenTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutTaskFrozenTube.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutTaskFrozenTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutTaskFrozenTubeRepository.findAll().size();

        // Create the StockOutTaskFrozenTube with an existing ID
        StockOutTaskFrozenTube existingStockOutTaskFrozenTube = new StockOutTaskFrozenTube();
        existingStockOutTaskFrozenTube.setId(1L);
        StockOutTaskFrozenTubeDTO existingStockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(existingStockOutTaskFrozenTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutTaskFrozenTubeMockMvc.perform(post("/api/stock-out-task-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutTaskFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutTaskFrozenTube> stockOutTaskFrozenTubeList = stockOutTaskFrozenTubeRepository.findAll();
        assertThat(stockOutTaskFrozenTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutTaskFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutTaskFrozenTube.setStatus(null);

        // Create the StockOutTaskFrozenTube, which fails.
        StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube);

        restStockOutTaskFrozenTubeMockMvc.perform(post("/api/stock-out-task-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutTaskFrozenTube> stockOutTaskFrozenTubeList = stockOutTaskFrozenTubeRepository.findAll();
        assertThat(stockOutTaskFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutTaskFrozenTubes() throws Exception {
        // Initialize the database
        stockOutTaskFrozenTubeRepository.saveAndFlush(stockOutTaskFrozenTube);

        // Get all the stockOutTaskFrozenTubeList
        restStockOutTaskFrozenTubeMockMvc.perform(get("/api/stock-out-task-frozen-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutTaskFrozenTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutTaskFrozenTube() throws Exception {
        // Initialize the database
        stockOutTaskFrozenTubeRepository.saveAndFlush(stockOutTaskFrozenTube);

        // Get the stockOutTaskFrozenTube
        restStockOutTaskFrozenTubeMockMvc.perform(get("/api/stock-out-task-frozen-tubes/{id}", stockOutTaskFrozenTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutTaskFrozenTube.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutTaskFrozenTube() throws Exception {
        // Get the stockOutTaskFrozenTube
        restStockOutTaskFrozenTubeMockMvc.perform(get("/api/stock-out-task-frozen-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutTaskFrozenTube() throws Exception {
        // Initialize the database
        stockOutTaskFrozenTubeRepository.saveAndFlush(stockOutTaskFrozenTube);
        int databaseSizeBeforeUpdate = stockOutTaskFrozenTubeRepository.findAll().size();

        // Update the stockOutTaskFrozenTube
        StockOutTaskFrozenTube updatedStockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.findOne(stockOutTaskFrozenTube.getId());
        updatedStockOutTaskFrozenTube
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(updatedStockOutTaskFrozenTube);

        restStockOutTaskFrozenTubeMockMvc.perform(put("/api/stock-out-task-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskFrozenTubeDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutTaskFrozenTube in the database
        List<StockOutTaskFrozenTube> stockOutTaskFrozenTubeList = stockOutTaskFrozenTubeRepository.findAll();
        assertThat(stockOutTaskFrozenTubeList).hasSize(databaseSizeBeforeUpdate);
        StockOutTaskFrozenTube testStockOutTaskFrozenTube = stockOutTaskFrozenTubeList.get(stockOutTaskFrozenTubeList.size() - 1);
        assertThat(testStockOutTaskFrozenTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutTaskFrozenTube.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutTaskFrozenTube() throws Exception {
        int databaseSizeBeforeUpdate = stockOutTaskFrozenTubeRepository.findAll().size();

        // Create the StockOutTaskFrozenTube
        StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutTaskFrozenTubeMockMvc.perform(put("/api/stock-out-task-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutTaskFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutTaskFrozenTube in the database
        List<StockOutTaskFrozenTube> stockOutTaskFrozenTubeList = stockOutTaskFrozenTubeRepository.findAll();
        assertThat(stockOutTaskFrozenTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutTaskFrozenTube() throws Exception {
        // Initialize the database
        stockOutTaskFrozenTubeRepository.saveAndFlush(stockOutTaskFrozenTube);
        int databaseSizeBeforeDelete = stockOutTaskFrozenTubeRepository.findAll().size();

        // Get the stockOutTaskFrozenTube
        restStockOutTaskFrozenTubeMockMvc.perform(delete("/api/stock-out-task-frozen-tubes/{id}", stockOutTaskFrozenTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutTaskFrozenTube> stockOutTaskFrozenTubeList = stockOutTaskFrozenTubeRepository.findAll();
        assertThat(stockOutTaskFrozenTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutTaskFrozenTube.class);
    }
}
