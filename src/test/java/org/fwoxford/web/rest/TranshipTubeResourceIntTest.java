package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.TranshipTube;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.TranshipTubeRepository;
import org.fwoxford.service.TranshipTubeService;
import org.fwoxford.service.dto.TranshipTubeDTO;
import org.fwoxford.service.mapper.TranshipTubeMapper;
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
 * Test class for the TranshipTubeResource REST controller.
 *
 * @see TranshipTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class TranshipTubeResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMNS_IN_TUBE = "AAAAAAAAAA";
    private static final String UPDATED_COLUMNS_IN_TUBE = "BBBBBBBBBB";

    private static final String DEFAULT_ROWS_IN_TUBE = "AAAAAAAAAA";
    private static final String UPDATED_ROWS_IN_TUBE = "BBBBBBBBBB";

    @Autowired
    private TranshipTubeRepository transhipTubeRepository;

    @Autowired
    private TranshipTubeMapper transhipTubeMapper;

    @Autowired
    private TranshipTubeService transhipTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTranshipTubeMockMvc;

    private TranshipTube transhipTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TranshipTubeResource transhipTubeResource = new TranshipTubeResource(transhipTubeService);
        this.restTranshipTubeMockMvc = MockMvcBuilders.standaloneSetup(transhipTubeResource)
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
    public static TranshipTube createEntity(EntityManager em) {
        TranshipTube transhipTube = new TranshipTube()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .columnsInTube(DEFAULT_COLUMNS_IN_TUBE)
                .rowsInTube(DEFAULT_ROWS_IN_TUBE);
        // Add required entity
        TranshipBox transhipBox = TranshipBoxResourceIntTest.createEntity(em);
        em.persist(transhipBox);
        em.flush();
        transhipTube.setTranshipBox(transhipBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        transhipTube.setFrozenTube(frozenTube);
        return transhipTube;
    }

    @Before
    public void initTest() {
        transhipTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranshipTube() throws Exception {
        int databaseSizeBeforeCreate = transhipTubeRepository.findAll().size();

        // Create the TranshipTube
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);

        restTranshipTubeMockMvc.perform(post("/api/tranship-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipTube in the database
        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeCreate + 1);
        TranshipTube testTranshipTube = transhipTubeList.get(transhipTubeList.size() - 1);
        assertThat(testTranshipTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTranshipTube.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testTranshipTube.getColumnsInTube()).isEqualTo(DEFAULT_COLUMNS_IN_TUBE);
        assertThat(testTranshipTube.getRowsInTube()).isEqualTo(DEFAULT_ROWS_IN_TUBE);
    }

    @Test
    @Transactional
    public void createTranshipTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transhipTubeRepository.findAll().size();

        // Create the TranshipTube with an existing ID
        TranshipTube existingTranshipTube = new TranshipTube();
        existingTranshipTube.setId(1L);
        TranshipTubeDTO existingTranshipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(existingTranshipTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranshipTubeMockMvc.perform(post("/api/tranship-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTranshipTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipTubeRepository.findAll().size();
        // set the field null
        transhipTube.setStatus(null);

        // Create the TranshipTube, which fails.
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);

        restTranshipTubeMockMvc.perform(post("/api/tranship-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipTubeDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColumnsInTubeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipTubeRepository.findAll().size();
        // set the field null
        transhipTube.setColumnsInTube(null);

        // Create the TranshipTube, which fails.
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);

        restTranshipTubeMockMvc.perform(post("/api/tranship-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipTubeDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRowsInTubeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transhipTubeRepository.findAll().size();
        // set the field null
        transhipTube.setRowsInTube(null);

        // Create the TranshipTube, which fails.
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);

        restTranshipTubeMockMvc.perform(post("/api/tranship-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipTubeDTO)))
            .andExpect(status().isBadRequest());

        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTranshipTubes() throws Exception {
        // Initialize the database
        transhipTubeRepository.saveAndFlush(transhipTube);

        // Get all the transhipTubeList
        restTranshipTubeMockMvc.perform(get("/api/tranship-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transhipTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].columnsInTube").value(hasItem(DEFAULT_COLUMNS_IN_TUBE.toString())))
            .andExpect(jsonPath("$.[*].rowsInTube").value(hasItem(DEFAULT_ROWS_IN_TUBE.toString())));
    }

    @Test
    @Transactional
    public void getTranshipTube() throws Exception {
        // Initialize the database
        transhipTubeRepository.saveAndFlush(transhipTube);

        // Get the transhipTube
        restTranshipTubeMockMvc.perform(get("/api/tranship-tubes/{id}", transhipTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transhipTube.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.columnsInTube").value(DEFAULT_COLUMNS_IN_TUBE.toString()))
            .andExpect(jsonPath("$.rowsInTube").value(DEFAULT_ROWS_IN_TUBE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTranshipTube() throws Exception {
        // Get the transhipTube
        restTranshipTubeMockMvc.perform(get("/api/tranship-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranshipTube() throws Exception {
        // Initialize the database
        transhipTubeRepository.saveAndFlush(transhipTube);
        int databaseSizeBeforeUpdate = transhipTubeRepository.findAll().size();

        // Update the transhipTube
        TranshipTube updatedTranshipTube = transhipTubeRepository.findOne(transhipTube.getId());
        updatedTranshipTube
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .columnsInTube(UPDATED_COLUMNS_IN_TUBE)
                .rowsInTube(UPDATED_ROWS_IN_TUBE);
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(updatedTranshipTube);

        restTranshipTubeMockMvc.perform(put("/api/tranship-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipTubeDTO)))
            .andExpect(status().isOk());

        // Validate the TranshipTube in the database
        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeUpdate);
        TranshipTube testTranshipTube = transhipTubeList.get(transhipTubeList.size() - 1);
        assertThat(testTranshipTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTranshipTube.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testTranshipTube.getColumnsInTube()).isEqualTo(UPDATED_COLUMNS_IN_TUBE);
        assertThat(testTranshipTube.getRowsInTube()).isEqualTo(UPDATED_ROWS_IN_TUBE);
    }

    @Test
    @Transactional
    public void updateNonExistingTranshipTube() throws Exception {
        int databaseSizeBeforeUpdate = transhipTubeRepository.findAll().size();

        // Create the TranshipTube
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTranshipTubeMockMvc.perform(put("/api/tranship-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transhipTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the TranshipTube in the database
        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTranshipTube() throws Exception {
        // Initialize the database
        transhipTubeRepository.saveAndFlush(transhipTube);
        int databaseSizeBeforeDelete = transhipTubeRepository.findAll().size();

        // Get the transhipTube
        restTranshipTubeMockMvc.perform(delete("/api/tranship-tubes/{id}", transhipTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TranshipTube> transhipTubeList = transhipTubeRepository.findAll();
        assertThat(transhipTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TranshipTube.class);
    }
}
