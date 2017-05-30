package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutBoxTube;
import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.StockOutTaskFrozenTube;
import org.fwoxford.repository.StockOutBoxTubeRepository;
import org.fwoxford.service.StockOutBoxTubeService;
import org.fwoxford.service.dto.StockOutBoxTubeDTO;
import org.fwoxford.service.mapper.StockOutBoxTubeMapper;
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
 * Test class for the StockOutBoxTubeResource REST controller.
 *
 * @see StockOutBoxTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutBoxTubeResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutBoxTubeRepository stockOutBoxTubeRepository;

    @Autowired
    private StockOutBoxTubeMapper stockOutBoxTubeMapper;

    @Autowired
    private StockOutBoxTubeService stockOutBoxTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutBoxTubeMockMvc;

    private StockOutBoxTube stockOutBoxTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutBoxTubeResource stockOutBoxTubeResource = new StockOutBoxTubeResource(stockOutBoxTubeService);
        this.restStockOutBoxTubeMockMvc = MockMvcBuilders.standaloneSetup(stockOutBoxTubeResource)
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
    public static StockOutBoxTube createEntity(EntityManager em) {
        StockOutBoxTube stockOutBoxTube = new StockOutBoxTube()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutFrozenBox stockOutFrozenBox = StockOutFrozenBoxResourceIntTest.createEntity(em);
        em.persist(stockOutFrozenBox);
        em.flush();
        stockOutBoxTube.setStockOutFrozenBox(stockOutFrozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        stockOutBoxTube.setFrozenTube(frozenTube);
        // Add required entity
        StockOutTaskFrozenTube stockOutTaskFrozenTube = StockOutTaskFrozenTubeResourceIntTest.createEntity(em);
        em.persist(stockOutTaskFrozenTube);
        em.flush();
        stockOutBoxTube.setStockOutTaskFrozenTube(stockOutTaskFrozenTube);
        return stockOutBoxTube;
    }

    @Before
    public void initTest() {
        stockOutBoxTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutBoxTube() throws Exception {
        int databaseSizeBeforeCreate = stockOutBoxTubeRepository.findAll().size();

        // Create the StockOutBoxTube
        StockOutBoxTubeDTO stockOutBoxTubeDTO = stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(stockOutBoxTube);

        restStockOutBoxTubeMockMvc.perform(post("/api/stock-out-box-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutBoxTube in the database
        List<StockOutBoxTube> stockOutBoxTubeList = stockOutBoxTubeRepository.findAll();
        assertThat(stockOutBoxTubeList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutBoxTube testStockOutBoxTube = stockOutBoxTubeList.get(stockOutBoxTubeList.size() - 1);
        assertThat(testStockOutBoxTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutBoxTube.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutBoxTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutBoxTubeRepository.findAll().size();

        // Create the StockOutBoxTube with an existing ID
        StockOutBoxTube existingStockOutBoxTube = new StockOutBoxTube();
        existingStockOutBoxTube.setId(1L);
        StockOutBoxTubeDTO existingStockOutBoxTubeDTO = stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(existingStockOutBoxTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutBoxTubeMockMvc.perform(post("/api/stock-out-box-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutBoxTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutBoxTube> stockOutBoxTubeList = stockOutBoxTubeRepository.findAll();
        assertThat(stockOutBoxTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutBoxTubeRepository.findAll().size();
        // set the field null
        stockOutBoxTube.setStatus(null);

        // Create the StockOutBoxTube, which fails.
        StockOutBoxTubeDTO stockOutBoxTubeDTO = stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(stockOutBoxTube);

        restStockOutBoxTubeMockMvc.perform(post("/api/stock-out-box-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutBoxTube> stockOutBoxTubeList = stockOutBoxTubeRepository.findAll();
        assertThat(stockOutBoxTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutBoxTubes() throws Exception {
        // Initialize the database
        stockOutBoxTubeRepository.saveAndFlush(stockOutBoxTube);

        // Get all the stockOutBoxTubeList
        restStockOutBoxTubeMockMvc.perform(get("/api/stock-out-box-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutBoxTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutBoxTube() throws Exception {
        // Initialize the database
        stockOutBoxTubeRepository.saveAndFlush(stockOutBoxTube);

        // Get the stockOutBoxTube
        restStockOutBoxTubeMockMvc.perform(get("/api/stock-out-box-tubes/{id}", stockOutBoxTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutBoxTube.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutBoxTube() throws Exception {
        // Get the stockOutBoxTube
        restStockOutBoxTubeMockMvc.perform(get("/api/stock-out-box-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutBoxTube() throws Exception {
        // Initialize the database
        stockOutBoxTubeRepository.saveAndFlush(stockOutBoxTube);
        int databaseSizeBeforeUpdate = stockOutBoxTubeRepository.findAll().size();

        // Update the stockOutBoxTube
        StockOutBoxTube updatedStockOutBoxTube = stockOutBoxTubeRepository.findOne(stockOutBoxTube.getId());
        updatedStockOutBoxTube
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutBoxTubeDTO stockOutBoxTubeDTO = stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(updatedStockOutBoxTube);

        restStockOutBoxTubeMockMvc.perform(put("/api/stock-out-box-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxTubeDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutBoxTube in the database
        List<StockOutBoxTube> stockOutBoxTubeList = stockOutBoxTubeRepository.findAll();
        assertThat(stockOutBoxTubeList).hasSize(databaseSizeBeforeUpdate);
        StockOutBoxTube testStockOutBoxTube = stockOutBoxTubeList.get(stockOutBoxTubeList.size() - 1);
        assertThat(testStockOutBoxTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutBoxTube.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutBoxTube() throws Exception {
        int databaseSizeBeforeUpdate = stockOutBoxTubeRepository.findAll().size();

        // Create the StockOutBoxTube
        StockOutBoxTubeDTO stockOutBoxTubeDTO = stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(stockOutBoxTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutBoxTubeMockMvc.perform(put("/api/stock-out-box-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutBoxTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutBoxTube in the database
        List<StockOutBoxTube> stockOutBoxTubeList = stockOutBoxTubeRepository.findAll();
        assertThat(stockOutBoxTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutBoxTube() throws Exception {
        // Initialize the database
        stockOutBoxTubeRepository.saveAndFlush(stockOutBoxTube);
        int databaseSizeBeforeDelete = stockOutBoxTubeRepository.findAll().size();

        // Get the stockOutBoxTube
        restStockOutBoxTubeMockMvc.perform(delete("/api/stock-out-box-tubes/{id}", stockOutBoxTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutBoxTube> stockOutBoxTubeList = stockOutBoxTubeRepository.findAll();
        assertThat(stockOutBoxTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutBoxTube.class);
    }
}
