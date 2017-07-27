package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockInTranshipBox;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.domain.StockIn;
import org.fwoxford.repository.StockInTranshipBoxRepository;
import org.fwoxford.service.StockInTranshipBoxService;
import org.fwoxford.service.dto.StockInTranshipBoxDTO;
import org.fwoxford.service.mapper.StockInTranshipBoxMapper;
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
 * Test class for the StockInTranshipBoxResource REST controller.
 *
 * @see StockInTranshipBoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockInTranshipBoxResourceIntTest {

    private static final String DEFAULT_STOCK_IN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_IN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSHIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSHIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FROZEN_BOX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FROZEN_BOX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockInTranshipBoxRepository stockInTranshipBoxRepository;

    @Autowired
    private StockInTranshipBoxMapper stockInTranshipBoxMapper;

    @Autowired
    private StockInTranshipBoxService stockInTranshipBoxService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockInTranshipBoxMockMvc;

    private StockInTranshipBox stockInTranshipBox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockInTranshipBoxResource stockInTranshipBoxResource = new StockInTranshipBoxResource(stockInTranshipBoxService);
        this.restStockInTranshipBoxMockMvc = MockMvcBuilders.standaloneSetup(stockInTranshipBoxResource)
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
    public static StockInTranshipBox createEntity(EntityManager em) {
        StockInTranshipBox stockInTranshipBox = new StockInTranshipBox()
                .stockInCode(DEFAULT_STOCK_IN_CODE)
                .transhipCode(DEFAULT_TRANSHIP_CODE)
                .frozenBoxCode(DEFAULT_FROZEN_BOX_CODE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        TranshipBox transhipBox = TranshipBoxResourceIntTest.createEntity(em);
        em.persist(transhipBox);
        em.flush();
        stockInTranshipBox.setTranshipBox(transhipBox);
        // Add required entity
        StockIn stockIn = StockInResourceIntTest.createEntity(em);
        em.persist(stockIn);
        em.flush();
        stockInTranshipBox.setStockIn(stockIn);
        return stockInTranshipBox;
    }

    @Before
    public void initTest() {
        stockInTranshipBox = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockInTranshipBox() throws Exception {
        int databaseSizeBeforeCreate = stockInTranshipBoxRepository.findAll().size();

        // Create the StockInTranshipBox
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);

        restStockInTranshipBoxMockMvc.perform(post("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTranshipBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInTranshipBox in the database
        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeCreate + 1);
        StockInTranshipBox testStockInTranshipBox = stockInTranshipBoxList.get(stockInTranshipBoxList.size() - 1);
        assertThat(testStockInTranshipBox.getStockInCode()).isEqualTo(DEFAULT_STOCK_IN_CODE);
        assertThat(testStockInTranshipBox.getTranshipCode()).isEqualTo(DEFAULT_TRANSHIP_CODE);
        assertThat(testStockInTranshipBox.getFrozenBoxCode()).isEqualTo(DEFAULT_FROZEN_BOX_CODE);
        assertThat(testStockInTranshipBox.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockInTranshipBox.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockInTranshipBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockInTranshipBoxRepository.findAll().size();

        // Create the StockInTranshipBox with an existing ID
        StockInTranshipBox existingStockInTranshipBox = new StockInTranshipBox();
        existingStockInTranshipBox.setId(1L);
        StockInTranshipBoxDTO existingStockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(existingStockInTranshipBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockInTranshipBoxMockMvc.perform(post("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockInTranshipBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStockInCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTranshipBoxRepository.findAll().size();
        // set the field null
        stockInTranshipBox.setStockInCode(null);

        // Create the StockInTranshipBox, which fails.
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);

        restStockInTranshipBoxMockMvc.perform(post("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTranshipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTranshipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTranshipBoxRepository.findAll().size();
        // set the field null
        stockInTranshipBox.setTranshipCode(null);

        // Create the StockInTranshipBox, which fails.
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);

        restStockInTranshipBoxMockMvc.perform(post("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTranshipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrozenBoxCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTranshipBoxRepository.findAll().size();
        // set the field null
        stockInTranshipBox.setFrozenBoxCode(null);

        // Create the StockInTranshipBox, which fails.
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);

        restStockInTranshipBoxMockMvc.perform(post("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTranshipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInTranshipBoxRepository.findAll().size();
        // set the field null
        stockInTranshipBox.setStatus(null);

        // Create the StockInTranshipBox, which fails.
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);

        restStockInTranshipBoxMockMvc.perform(post("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTranshipBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockInTranshipBoxes() throws Exception {
        // Initialize the database
        stockInTranshipBoxRepository.saveAndFlush(stockInTranshipBox);

        // Get all the stockInTranshipBoxList
        restStockInTranshipBoxMockMvc.perform(get("/api/stock-in-tranship-boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockInTranshipBox.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockInCode").value(hasItem(DEFAULT_STOCK_IN_CODE.toString())))
            .andExpect(jsonPath("$.[*].transhipCode").value(hasItem(DEFAULT_TRANSHIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].frozenBoxCode").value(hasItem(DEFAULT_FROZEN_BOX_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockInTranshipBox() throws Exception {
        // Initialize the database
        stockInTranshipBoxRepository.saveAndFlush(stockInTranshipBox);

        // Get the stockInTranshipBox
        restStockInTranshipBoxMockMvc.perform(get("/api/stock-in-tranship-boxes/{id}", stockInTranshipBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockInTranshipBox.getId().intValue()))
            .andExpect(jsonPath("$.stockInCode").value(DEFAULT_STOCK_IN_CODE.toString()))
            .andExpect(jsonPath("$.transhipCode").value(DEFAULT_TRANSHIP_CODE.toString()))
            .andExpect(jsonPath("$.frozenBoxCode").value(DEFAULT_FROZEN_BOX_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockInTranshipBox() throws Exception {
        // Get the stockInTranshipBox
        restStockInTranshipBoxMockMvc.perform(get("/api/stock-in-tranship-boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockInTranshipBox() throws Exception {
        // Initialize the database
        stockInTranshipBoxRepository.saveAndFlush(stockInTranshipBox);
        int databaseSizeBeforeUpdate = stockInTranshipBoxRepository.findAll().size();

        // Update the stockInTranshipBox
        StockInTranshipBox updatedStockInTranshipBox = stockInTranshipBoxRepository.findOne(stockInTranshipBox.getId());
        updatedStockInTranshipBox
                .stockInCode(UPDATED_STOCK_IN_CODE)
                .transhipCode(UPDATED_TRANSHIP_CODE)
                .frozenBoxCode(UPDATED_FROZEN_BOX_CODE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(updatedStockInTranshipBox);

        restStockInTranshipBoxMockMvc.perform(put("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTranshipBoxDTO)))
            .andExpect(status().isOk());

        // Validate the StockInTranshipBox in the database
        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeUpdate);
        StockInTranshipBox testStockInTranshipBox = stockInTranshipBoxList.get(stockInTranshipBoxList.size() - 1);
        assertThat(testStockInTranshipBox.getStockInCode()).isEqualTo(UPDATED_STOCK_IN_CODE);
        assertThat(testStockInTranshipBox.getTranshipCode()).isEqualTo(UPDATED_TRANSHIP_CODE);
        assertThat(testStockInTranshipBox.getFrozenBoxCode()).isEqualTo(UPDATED_FROZEN_BOX_CODE);
        assertThat(testStockInTranshipBox.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockInTranshipBox.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockInTranshipBox() throws Exception {
        int databaseSizeBeforeUpdate = stockInTranshipBoxRepository.findAll().size();

        // Create the StockInTranshipBox
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockInTranshipBoxMockMvc.perform(put("/api/stock-in-tranship-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInTranshipBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockInTranshipBox in the database
        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockInTranshipBox() throws Exception {
        // Initialize the database
        stockInTranshipBoxRepository.saveAndFlush(stockInTranshipBox);
        int databaseSizeBeforeDelete = stockInTranshipBoxRepository.findAll().size();

        // Get the stockInTranshipBox
        restStockInTranshipBoxMockMvc.perform(delete("/api/stock-in-tranship-boxes/{id}", stockInTranshipBox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockInTranshipBox> stockInTranshipBoxList = stockInTranshipBoxRepository.findAll();
        assertThat(stockInTranshipBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockInTranshipBox.class);
    }
}
