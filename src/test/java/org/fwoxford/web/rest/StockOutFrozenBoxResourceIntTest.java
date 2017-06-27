package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockOutBoxPosition;
import org.fwoxford.domain.StockOutTask;
import org.fwoxford.repository.StockOutFrozenBoxRepository;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import org.fwoxford.service.mapper.StockOutFrozenBoxMapper;
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
 * Test class for the StockOutFrozenBoxResource REST controller.
 *
 * @see StockOutFrozenBoxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutFrozenBoxResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutFrozenBoxRepository stockOutFrozenBoxRepository;

    @Autowired
    private StockOutFrozenBoxMapper stockOutFrozenBoxMapper;

    @Autowired
    private StockOutFrozenBoxService stockOutFrozenBoxService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutFrozenBoxMockMvc;

    private StockOutFrozenBox stockOutFrozenBox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutFrozenBoxResource stockOutFrozenBoxResource = new StockOutFrozenBoxResource(stockOutFrozenBoxService);
        this.restStockOutFrozenBoxMockMvc = MockMvcBuilders.standaloneSetup(stockOutFrozenBoxResource)
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
    public static StockOutFrozenBox createEntity(EntityManager em) {
        StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        stockOutFrozenBox.setFrozenBox(frozenBox);
        // Add enBox.setFrozenBox(frozenBox);
        // Add required entity
        StockOutTask stockOutTask = StockOutTaskResourceIntTest.createEntity(em);
        em.persist(stockOutTask);
        em.flush();
        stockOutFrozenBox.setStockOutTask(stockOutTask);
        return stockOutFrozenBox;
    }

    @Before
    public void initTest() {
        stockOutFrozenBox = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutFrozenBox() throws Exception {
        int databaseSizeBeforeCreate = stockOutFrozenBoxRepository.findAll().size();

        // Create the StockOutFrozenBox
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);

        restStockOutFrozenBoxMockMvc.perform(post("/api/stock-out-frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutFrozenBox in the database
        List<StockOutFrozenBox> stockOutFrozenBoxList = stockOutFrozenBoxRepository.findAll();
        assertThat(stockOutFrozenBoxList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutFrozenBox testStockOutFrozenBox = stockOutFrozenBoxList.get(stockOutFrozenBoxList.size() - 1);
        assertThat(testStockOutFrozenBox.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutFrozenBox.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutFrozenBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutFrozenBoxRepository.findAll().size();

        // Create the StockOutFrozenBox with an existing ID
        StockOutFrozenBox existingStockOutFrozenBox = new StockOutFrozenBox();
        existingStockOutFrozenBox.setId(1L);
        StockOutFrozenBoxDTO existingStockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(existingStockOutFrozenBox);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutFrozenBoxMockMvc.perform(post("/api/stock-out-frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutFrozenBoxDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutFrozenBox> stockOutFrozenBoxList = stockOutFrozenBoxRepository.findAll();
        assertThat(stockOutFrozenBoxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFrozenBoxRepository.findAll().size();
        // set the field null
        stockOutFrozenBox.setStatus(null);

        // Create the StockOutFrozenBox, which fails.
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);

        restStockOutFrozenBoxMockMvc.perform(post("/api/stock-out-frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenBoxDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFrozenBox> stockOutFrozenBoxList = stockOutFrozenBoxRepository.findAll();
        assertThat(stockOutFrozenBoxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutFrozenBoxes() throws Exception {
        // Initialize the database
        stockOutFrozenBoxRepository.saveAndFlush(stockOutFrozenBox);

        // Get all the stockOutFrozenBoxList
        restStockOutFrozenBoxMockMvc.perform(get("/api/stock-out-frozen-boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutFrozenBox.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutFrozenBox() throws Exception {
        // Initialize the database
        stockOutFrozenBoxRepository.saveAndFlush(stockOutFrozenBox);

        // Get the stockOutFrozenBox
        restStockOutFrozenBoxMockMvc.perform(get("/api/stock-out-frozen-boxes/{id}", stockOutFrozenBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutFrozenBox.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutFrozenBox() throws Exception {
        // Get the stockOutFrozenBox
        restStockOutFrozenBoxMockMvc.perform(get("/api/stock-out-frozen-boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutFrozenBox() throws Exception {
        // Initialize the database
        stockOutFrozenBoxRepository.saveAndFlush(stockOutFrozenBox);
        int databaseSizeBeforeUpdate = stockOutFrozenBoxRepository.findAll().size();

        // Update the stockOutFrozenBox
        StockOutFrozenBox updatedStockOutFrozenBox = stockOutFrozenBoxRepository.findOne(stockOutFrozenBox.getId());
        updatedStockOutFrozenBox
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(updatedStockOutFrozenBox);

        restStockOutFrozenBoxMockMvc.perform(put("/api/stock-out-frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenBoxDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutFrozenBox in the database
        List<StockOutFrozenBox> stockOutFrozenBoxList = stockOutFrozenBoxRepository.findAll();
        assertThat(stockOutFrozenBoxList).hasSize(databaseSizeBeforeUpdate);
        StockOutFrozenBox testStockOutFrozenBox = stockOutFrozenBoxList.get(stockOutFrozenBoxList.size() - 1);
        assertThat(testStockOutFrozenBox.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutFrozenBox.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutFrozenBox() throws Exception {
        int databaseSizeBeforeUpdate = stockOutFrozenBoxRepository.findAll().size();

        // Create the StockOutFrozenBox
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutFrozenBoxMockMvc.perform(put("/api/stock-out-frozen-boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFrozenBoxDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutFrozenBox in the database
        List<StockOutFrozenBox> stockOutFrozenBoxList = stockOutFrozenBoxRepository.findAll();
        assertThat(stockOutFrozenBoxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutFrozenBox() throws Exception {
        // Initialize the database
        stockOutFrozenBoxRepository.saveAndFlush(stockOutFrozenBox);
        int databaseSizeBeforeDelete = stockOutFrozenBoxRepository.findAll().size();

        // Get the stockOutFrozenBox
        restStockOutFrozenBoxMockMvc.perform(delete("/api/stock-out-frozen-boxes/{id}", stockOutFrozenBox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutFrozenBox> stockOutFrozenBoxList = stockOutFrozenBoxRepository.findAll();
        assertThat(stockOutFrozenBoxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutFrozenBox.class);
    }
}
