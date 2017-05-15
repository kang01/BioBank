package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutReqFrozenTube;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.StockOutRequirement;
import org.fwoxford.repository.StockOutReqFrozenTubeRepository;
import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutReqFrozenTubeMapper;
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
 * Test class for the StockOutReqFrozenTubeResource REST controller.
 *
 * @see StockOutReqFrozenTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutReqFrozenTubeResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_ROWS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_ROWS = "BBBBBBBBBB";

    private static final String DEFAULT_TUBE_COLUMNS = "AAAAAAAAAA";
    private static final String UPDATED_TUBE_COLUMNS = "BBBBBBBBBB";

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    private StockOutReqFrozenTubeMapper stockOutReqFrozenTubeMapper;

    @Autowired
    private StockOutReqFrozenTubeService stockOutReqFrozenTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutReqFrozenTubeMockMvc;

    private StockOutReqFrozenTube stockOutReqFrozenTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutReqFrozenTubeResource stockOutReqFrozenTubeResource = new StockOutReqFrozenTubeResource(stockOutReqFrozenTubeService);
        this.restStockOutReqFrozenTubeMockMvc = MockMvcBuilders.standaloneSetup(stockOutReqFrozenTubeResource)
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
    public static StockOutReqFrozenTube createEntity(EntityManager em) {
        StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .tubeRows(DEFAULT_TUBE_ROWS)
                .tubeColumns(DEFAULT_TUBE_COLUMNS);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        stockOutReqFrozenTube.setFrozenBox(frozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        stockOutReqFrozenTube.setFrozenTube(frozenTube);
        // Add required entity
        StockOutRequirement stockOutRequirement = StockOutRequirementResourceIntTest.createEntity(em);
        em.persist(stockOutRequirement);
        em.flush();
        stockOutReqFrozenTube.setStockOutRequirement(stockOutRequirement);
        return stockOutReqFrozenTube;
    }

    @Before
    public void initTest() {
        stockOutReqFrozenTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutReqFrozenTube() throws Exception {
        int databaseSizeBeforeCreate = stockOutReqFrozenTubeRepository.findAll().size();

        // Create the StockOutReqFrozenTube
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);

        restStockOutReqFrozenTubeMockMvc.perform(post("/api/stock-out-req-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutReqFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutReqFrozenTube in the database
        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutReqFrozenTube testStockOutReqFrozenTube = stockOutReqFrozenTubeList.get(stockOutReqFrozenTubeList.size() - 1);
        assertThat(testStockOutReqFrozenTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutReqFrozenTube.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testStockOutReqFrozenTube.getTubeRows()).isEqualTo(DEFAULT_TUBE_ROWS);
        assertThat(testStockOutReqFrozenTube.getTubeColumns()).isEqualTo(DEFAULT_TUBE_COLUMNS);
    }

    @Test
    @Transactional
    public void createStockOutReqFrozenTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutReqFrozenTubeRepository.findAll().size();

        // Create the StockOutReqFrozenTube with an existing ID
        StockOutReqFrozenTube existingStockOutReqFrozenTube = new StockOutReqFrozenTube();
        existingStockOutReqFrozenTube.setId(1L);
        StockOutReqFrozenTubeDTO existingStockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(existingStockOutReqFrozenTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutReqFrozenTubeMockMvc.perform(post("/api/stock-out-req-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutReqFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutReqFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutReqFrozenTube.setStatus(null);

        // Create the StockOutReqFrozenTube, which fails.
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);

        restStockOutReqFrozenTubeMockMvc.perform(post("/api/stock-out-req-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutReqFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeRowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutReqFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutReqFrozenTube.setTubeRows(null);

        // Create the StockOutReqFrozenTube, which fails.
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);

        restStockOutReqFrozenTubeMockMvc.perform(post("/api/stock-out-req-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutReqFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTubeColumnsIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutReqFrozenTubeRepository.findAll().size();
        // set the field null
        stockOutReqFrozenTube.setTubeColumns(null);

        // Create the StockOutReqFrozenTube, which fails.
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);

        restStockOutReqFrozenTubeMockMvc.perform(post("/api/stock-out-req-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutReqFrozenTubeDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutReqFrozenTubes() throws Exception {
        // Initialize the database
        stockOutReqFrozenTubeRepository.saveAndFlush(stockOutReqFrozenTube);

        // Get all the stockOutReqFrozenTubeList
        restStockOutReqFrozenTubeMockMvc.perform(get("/api/stock-out-req-frozen-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutReqFrozenTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].tubeRows").value(hasItem(DEFAULT_TUBE_ROWS.toString())))
            .andExpect(jsonPath("$.[*].tubeColumns").value(hasItem(DEFAULT_TUBE_COLUMNS.toString())));
    }

    @Test
    @Transactional
    public void getStockOutReqFrozenTube() throws Exception {
        // Initialize the database
        stockOutReqFrozenTubeRepository.saveAndFlush(stockOutReqFrozenTube);

        // Get the stockOutReqFrozenTube
        restStockOutReqFrozenTubeMockMvc.perform(get("/api/stock-out-req-frozen-tubes/{id}", stockOutReqFrozenTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutReqFrozenTube.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.tubeRows").value(DEFAULT_TUBE_ROWS.toString()))
            .andExpect(jsonPath("$.tubeColumns").value(DEFAULT_TUBE_COLUMNS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutReqFrozenTube() throws Exception {
        // Get the stockOutReqFrozenTube
        restStockOutReqFrozenTubeMockMvc.perform(get("/api/stock-out-req-frozen-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutReqFrozenTube() throws Exception {
        // Initialize the database
        stockOutReqFrozenTubeRepository.saveAndFlush(stockOutReqFrozenTube);
        int databaseSizeBeforeUpdate = stockOutReqFrozenTubeRepository.findAll().size();

        // Update the stockOutReqFrozenTube
        StockOutReqFrozenTube updatedStockOutReqFrozenTube = stockOutReqFrozenTubeRepository.findOne(stockOutReqFrozenTube.getId());
        updatedStockOutReqFrozenTube
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .tubeRows(UPDATED_TUBE_ROWS)
                .tubeColumns(UPDATED_TUBE_COLUMNS);
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(updatedStockOutReqFrozenTube);

        restStockOutReqFrozenTubeMockMvc.perform(put("/api/stock-out-req-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutReqFrozenTubeDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutReqFrozenTube in the database
        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeUpdate);
        StockOutReqFrozenTube testStockOutReqFrozenTube = stockOutReqFrozenTubeList.get(stockOutReqFrozenTubeList.size() - 1);
        assertThat(testStockOutReqFrozenTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutReqFrozenTube.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testStockOutReqFrozenTube.getTubeRows()).isEqualTo(UPDATED_TUBE_ROWS);
        assertThat(testStockOutReqFrozenTube.getTubeColumns()).isEqualTo(UPDATED_TUBE_COLUMNS);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutReqFrozenTube() throws Exception {
        int databaseSizeBeforeUpdate = stockOutReqFrozenTubeRepository.findAll().size();

        // Create the StockOutReqFrozenTube
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutReqFrozenTubeMockMvc.perform(put("/api/stock-out-req-frozen-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutReqFrozenTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutReqFrozenTube in the database
        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutReqFrozenTube() throws Exception {
        // Initialize the database
        stockOutReqFrozenTubeRepository.saveAndFlush(stockOutReqFrozenTube);
        int databaseSizeBeforeDelete = stockOutReqFrozenTubeRepository.findAll().size();

        // Get the stockOutReqFrozenTube
        restStockOutReqFrozenTubeMockMvc.perform(delete("/api/stock-out-req-frozen-tubes/{id}", stockOutReqFrozenTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = stockOutReqFrozenTubeRepository.findAll();
        assertThat(stockOutReqFrozenTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutReqFrozenTube.class);
    }
}
