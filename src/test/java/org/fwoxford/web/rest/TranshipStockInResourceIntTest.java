package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.TranshipStockIn;
import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.StockIn;
import org.fwoxford.repository.TranshipStockInRepository;
import org.fwoxford.service.TranshipStockInService;
import org.fwoxford.service.dto.TranshipStockInDTO;
import org.fwoxford.service.mapper.TranshipStockInMapper;
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
 * Test class for the TranshipStockInResource REST controller.
 *
 * @see TranshipStockInResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class TranshipStockInResourceIntTest {

    private static final String DEFAULT_TRANSHIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSHIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STOCK_IN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_IN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private TranshipStockInRepository transhipStockInRepository;

    @Autowired
    private TranshipStockInMapper transhipStockInMapper;

    @Autowired
    private TranshipStockInService transhipStockInService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTranshipStockInMockMvc;

    private TranshipStockIn transhipStockIn;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TranshipStockInResource transhipStockInResource = new TranshipStockInResource(transhipStockInService);
        this.restTranshipStockInMockMvc = MockMvcBuilders.standaloneSetup(transhipStockInResource)
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
    public static TranshipStockIn createEntity(EntityManager em) {
        TranshipStockIn transhipStockIn = new TranshipStockIn()
                .transhipCode(DEFAULT_TRANSHIP_CODE)
                .stockInCode(DEFAULT_STOCK_IN_CODE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        Tranship tranship = TranshipResourceIntTest.createEntity(em);
        em.persist(tranship);
        em.flush();
        transhipStockIn.setTranship(tranship);
        // Add required entity
        StockIn stockIn = StockInResourceIntTest.createEntity(em);
        em.persist(stockIn);
        em.flush();
        transhipStockIn.setStockIn(stockIn);
        return transhipStockIn;
    }

    @Before
    public void initTest() {
        transhipStockIn = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranshipStockIn() throws Exception {
        int databaseSizeBeforeCreate = transhipStockInRepository.findAll().size();

        // Create the TranshipStockIn
        TranshipStockInDTO transhipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn);

        restTranshipStockInMockMvc.perform(post("/api/tranship-stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipStockInDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipStockIn in the database
        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeCreate + 1);
        TranshipStockIn testTranshipStockIn = transhipStockInList.get(transhipStockInList.size() - 1);
        assertThat(testTranshipStockIn.getTranshipCode()).isEqualTo(DEFAULT_TRANSHIP_CODE);
        assertThat(testTranshipStockIn.getStockInCode()).isEqualTo(DEFAULT_STOCK_IN_CODE);
        assertThat(testTranshipStockIn.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTranshipStockIn.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createTranshipStockInWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transhipStockInRepository.findAll().size();

        // Create the TranshipStockIn with an existing ID
        TranshipStockIn existingTranshipStockIn = new TranshipStockIn();
        existingTranshipStockIn.setId(1L);
        TranshipStockInDTO existingTranshipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(existingTranshipStockIn);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranshipStockInMockMvc.perform(post("/api/tranship-stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTranshipStockInDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTranshipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipStockInRepository.findAll().size();
        // set the field null
        transhipStockIn.setTranshipCode(null);

        // Create the TranshipStockIn, which fails.
        TranshipStockInDTO transhipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn);

        restTranshipStockInMockMvc.perform(post("/api/tranship-stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipStockInDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockInCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipStockInRepository.findAll().size();
        // set the field null
        transhipStockIn.setStockInCode(null);

        // Create the TranshipStockIn, which fails.
        TranshipStockInDTO transhipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn);

        restTranshipStockInMockMvc.perform(post("/api/tranship-stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipStockInDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipStockInRepository.findAll().size();
        // set the field null
        transhipStockIn.setStatus(null);

        // Create the TranshipStockIn, which fails.
        TranshipStockInDTO transhipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn);

        restTranshipStockInMockMvc.perform(post("/api/tranship-stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipStockInDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTranshipStockIns() throws Exception {
        // Initialize the database
        transhipStockInRepository.saveAndFlush(transhipStockIn);

        // Get all the transhipStockInList
        restTranshipStockInMockMvc.perform(get("/api/tranship-stock-ins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transhipStockIn.getId().intValue())))
            .andExpect(jsonPath("$.[*].transhipCode").value(hasItem(DEFAULT_TRANSHIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].stockInCode").value(hasItem(DEFAULT_STOCK_IN_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getTranshipStockIn() throws Exception {
        // Initialize the database
        transhipStockInRepository.saveAndFlush(transhipStockIn);

        // Get the transhipStockIn
        restTranshipStockInMockMvc.perform(get("/api/tranship-stock-ins/{id}", transhipStockIn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transhipStockIn.getId().intValue()))
            .andExpect(jsonPath("$.transhipCode").value(DEFAULT_TRANSHIP_CODE.toString()))
            .andExpect(jsonPath("$.stockInCode").value(DEFAULT_STOCK_IN_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranshipStockIn() throws Exception {
        // Get the transhipStockIn
        restTranshipStockInMockMvc.perform(get("/api/tranship-stock-ins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranshipStockIn() throws Exception {
        // Initialize the database
        transhipStockInRepository.saveAndFlush(transhipStockIn);
        int databaseSizeBeforeUpdate = transhipStockInRepository.findAll().size();

        // Update the transhipStockIn
        TranshipStockIn updatedTranshipStockIn = transhipStockInRepository.findOne(transhipStockIn.getId());
        updatedTranshipStockIn
                .transhipCode(UPDATED_TRANSHIP_CODE)
                .stockInCode(UPDATED_STOCK_IN_CODE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        TranshipStockInDTO transhipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(updatedTranshipStockIn);

        restTranshipStockInMockMvc.perform(put("/api/tranship-stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipStockInDTO)))
            .andExpect(status().isOk());

        // Validate the TranshipStockIn in the database
        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeUpdate);
        TranshipStockIn testTranshipStockIn = transhipStockInList.get(transhipStockInList.size() - 1);
        assertThat(testTranshipStockIn.getTranshipCode()).isEqualTo(UPDATED_TRANSHIP_CODE);
        assertThat(testTranshipStockIn.getStockInCode()).isEqualTo(UPDATED_STOCK_IN_CODE);
        assertThat(testTranshipStockIn.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTranshipStockIn.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingTranshipStockIn() throws Exception {
        int databaseSizeBeforeUpdate = transhipStockInRepository.findAll().size();

        // Create the TranshipStockIn
        TranshipStockInDTO transhipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTranshipStockInMockMvc.perform(put("/api/tranship-stock-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipStockInDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipStockIn in the database
        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTranshipStockIn() throws Exception {
        // Initialize the database
        transhipStockInRepository.saveAndFlush(transhipStockIn);
        int databaseSizeBeforeDelete = transhipStockInRepository.findAll().size();

        // Get the transhipStockIn
        restTranshipStockInMockMvc.perform(delete("/api/tranship-stock-ins/{id}", transhipStockIn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findAll();
        assertThat(transhipStockInList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TranshipStockIn.class);
    }
}
