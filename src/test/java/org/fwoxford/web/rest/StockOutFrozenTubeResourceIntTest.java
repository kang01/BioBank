package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutFrozenTube;
import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.StockOutFrozenTubeRepository;
import org.fwoxford.service.StockOutFrozenTubeService;
import org.fwoxford.service.dto.StockOutFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutFrozenTubeMapper;
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
 * Test class for the StockOutFrozenTubeResource REST controller.
 *
 * @see StockOutFrozenTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutFrozenTubeResourceIntTest {

    private static final String DEFAULT_TUBE_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_COLUMNS = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutFrozenTubeRepository stockOutFrozenTubeRepository;

    @Autowired
    private StockOutFrozenTubeMapper stockOutFrozenTubeMapper;

    @Autowired
    private StockOutFrozenTubeService stockOutFrozenTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutFrozenTubeMockMvc;

    private StockOutFrozenTube stockOutFrozenTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutFrozenTubeResource stockOutFrozenTubeResource = new StockOutFrozenTubeResource(stockOutFrozenTubeService);
        this.restStockOutFrozenTubeMockMvc = MockMvcBuilders.standaloneSetup(stockOutFrozenTubeResource)
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
    public static StockOutFrozenTube createEntity(EntityManager em) {
        StockOutFrozenTube stockOutFrozenTube = new StockOutFrozenTube()
                .tubeRows(DEFAULT_TUBE_ROWS)
                .tubeColumns(DEFAULT_TUBE_COLUMNS)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutFrozenBox stockOutFrozenBox = StockOutFrozenBoxResourceIntTest.createEntity(em);
        em.persist(stockOutFrozenBox);
        em.flush();
        stockOutFrozenTube.setStockOutFrozenBox(stockOutFrozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        stockOutFrozenTube.setFrozenTube(frozenTube);
        return stockOutFrozenTube;
    }

    @Before
    public void initTest() {
        stockOutFrozenTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutFrozenTube() throws Exception {
        int databaseSizeBeforeCreate = stockOutFrozenTubeRepository.findAll().size();

        // Create the StockOutFrozenTube
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);

        restStockOutFrozenTubeMockMvc.perform(post("/api/stock-out-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutFrozenTube in the database
        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutFrozenTube testStockOutFrozenTube = stockOutFrozenTubeList.get(stockOutFrozenTubeList.size() - 1);
        assertThat(testStockOutFrozenTube.getTubeRows()).isEqualTo(DEFAULT_TUBE_ROWS);
        assertThat(testStockOutFrozenTube.getTubeColumns()).isEqualTo(DEFAULT_TUBE_COLUMNS);
        assertThat(testStockOutFrozenTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutFrozenTube.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutFrozenTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutFrozenTubeRepository.findAll().size();

        // Create the StockOutFrozenTube with an existing ID
        StockOutFrozenTube existingStockOutFrozenTube = new StockOutFrozenTube();
        existingStockOutFrozenTube.setId(1L);
        StockOutFrozenTubeDTO existingStockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(existingStockOutFrozenTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutFrozenTubeMockMvc.perform(post("/api/stock-out-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTubeRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutFrozenTube.setTubeRows(null);

        // Create the StockOutFrozenTube, which fails.
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);

        restStockOutFrozenTubeMockMvc.perform(post("/api/stock-out-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutFrozenTube.setTubeColumns(null);

        // Create the StockOutFrozenTube, which fails.
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);

        restStockOutFrozenTubeMockMvc.perform(post("/api/stock-out-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutFrozenTube.setStatus(null);

        // Create the StockOutFrozenTube, which fails.
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);

        restStockOutFrozenTubeMockMvc.perform(post("/api/stock-out-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutFrozenTubes() throws Exception {
        // Initialize the database
        stockOutFrozenTubeRepository.saveAndFlush(stockOutFrozenTube);

        // Get all the stockOutFrozenTubeList
        restStockOutFrozenTubeMockMvc.perform(get("/api/stock-out-frozen-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutFrozenTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].tubeRows").value(hasItem(DEFAULT_TUBE_ROWS.toString())))
            .andExpect(jsonPath("$.[*].tubeColumns").value(hasItem(DEFAULT_TUBE_COLUMNS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutFrozenTube() throws Exception {
        // Initialize the database
        stockOutFrozenTubeRepository.saveAndFlush(stockOutFrozenTube);

        // Get the stockOutFrozenTube
        restStockOutFrozenTubeMockMvc.perform(get("/api/stock-out-frozen-tubes/{id}", stockOutFrozenTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutFrozenTube.getId().intValue()))
            .andExpect(jsonPath("$.tubeRows").value(DEFAULT_TUBE_ROWS.toString()))
            .andExpect(jsonPath("$.tubeColumns").value(DEFAULT_TUBE_COLUMNS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutFrozenTube() throws Exception {
        // Get the stockOutFrozenTube
        restStockOutFrozenTubeMockMvc.perform(get("/api/stock-out-frozen-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutFrozenTube() throws Exception {
        // Initialize the database
        stockOutFrozenTubeRepository.saveAndFlush(stockOutFrozenTube);
        int databaseSizeBeforeUpdate = stockOutFrozenTubeRepository.findAll().size();

        // Update the stockOutFrozenTube
        StockOutFrozenTube updatedStockOutFrozenTube = stockOutFrozenTubeRepository.findOne(stockOutFrozenTube.getId());
        updatedStockOutFrozenTube
                .tubeRows(UPDATED_TUBE_ROWS)
                .tubeColumns(UPDATED_TUBE_COLUMNS)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(updatedStockOutFrozenTube);

        restStockOutFrozenTubeMockMvc.perform(put("/api/stock-out-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenTubeDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutFrozenTube in the database
        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeUpdate);
        StockOutFrozenTube testStockOutFrozenTube = stockOutFrozenTubeList.get(stockOutFrozenTubeList.size() - 1);
        assertThat(testStockOutFrozenTube.getTubeRows()).isEqualTo(UPDATED_TUBE_ROWS);
        assertThat(testStockOutFrozenTube.getTubeColumns()).isEqualTo(UPDATED_TUBE_COLUMNS);
        assertThat(testStockOutFrozenTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutFrozenTube.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutFrozenTube() throws Exception {
        int databaseSizeBeforeUpdate = stockOutFrozenTubeRepository.findAll().size();

        // Create the StockOutFrozenTube
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutFrozenTubeMockMvc.perform(put("/api/stock-out-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutFrozenTube in the database
        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutFrozenTube() throws Exception {
        // Initialize the database
        stockOutFrozenTubeRepository.saveAndFlush(stockOutFrozenTube);
        int databaseSizeBeforeDelete = stockOutFrozenTubeRepository.findAll().size();

        // Get the stockOutFrozenTube
        restStockOutFrozenTubeMockMvc.perform(delete("/api/stock-out-frozen-tubes/{id}", stockOutFrozenTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutFrozenTube> stockOutFrozenTubeList = stockOutFrozenTubeRepository.findAll();
        assertThat(stockOutFrozenTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutFrozenTube.class);
    }
}
