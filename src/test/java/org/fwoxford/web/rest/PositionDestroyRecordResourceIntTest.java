package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.PositionDestroyRecord;
import org.fwoxford.repository.PositionDestroyRecordRepository;
import org.fwoxford.service.PositionDestroyRecordService;
import org.fwoxford.service.dto.PositionDestroyRecordDTO;
import org.fwoxford.service.mapper.PositionDestroyRecordMapper;
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
 * Test class for the PositionDestroyRecordResource REST controller.
 *
 * @see PositionDestroyRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class PositionDestroyRecordResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private PositionDestroyRecordRepository positionDestroyRecordRepository;

    @Autowired
    private PositionDestroyRecordMapper positionDestroyRecordMapper;

    @Autowired
    private PositionDestroyRecordService positionDestroyRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPositionDestroyRecordMockMvc;

    private PositionDestroyRecord positionDestroyRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionDestroyRecordResource positionDestroyRecordResource = new PositionDestroyRecordResource(positionDestroyRecordService);
        this.restPositionDestroyRecordMockMvc = MockMvcBuilders.standaloneSetup(positionDestroyRecordResource)
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
    public static PositionDestroyRecord createEntity(EntityManager em) {
        PositionDestroyRecord positionDestroyRecord = new PositionDestroyRecord()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return positionDestroyRecord;
    }

    @Before
    public void initTest() {
        positionDestroyRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createPositionDestroyRecord() throws Exception {
        int databaseSizeBeforeCreate = positionDestroyRecordRepository.findAll().size();

        // Create the PositionDestroyRecord
        PositionDestroyRecordDTO positionDestroyRecordDTO = positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(positionDestroyRecord);

        restPositionDestroyRecordMockMvc.perform(post("/api/position-destroy-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionDestroyRecord in the database
        List<PositionDestroyRecord> positionDestroyRecordList = positionDestroyRecordRepository.findAll();
        assertThat(positionDestroyRecordList).hasSize(databaseSizeBeforeCreate + 1);
        PositionDestroyRecord testPositionDestroyRecord = positionDestroyRecordList.get(positionDestroyRecordList.size() - 1);
        assertThat(testPositionDestroyRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPositionDestroyRecord.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createPositionDestroyRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionDestroyRecordRepository.findAll().size();

        // Create the PositionDestroyRecord with an existing ID
        PositionDestroyRecord existingPositionDestroyRecord = new PositionDestroyRecord();
        existingPositionDestroyRecord.setId(1L);
        PositionDestroyRecordDTO existingPositionDestroyRecordDTO = positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(existingPositionDestroyRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionDestroyRecordMockMvc.perform(post("/api/position-destroy-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPositionDestroyRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PositionDestroyRecord> positionDestroyRecordList = positionDestroyRecordRepository.findAll();
        assertThat(positionDestroyRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionDestroyRecordRepository.findAll().size();
        // set the field null
        positionDestroyRecord.setStatus(null);

        // Create the PositionDestroyRecord, which fails.
        PositionDestroyRecordDTO positionDestroyRecordDTO = positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(positionDestroyRecord);

        restPositionDestroyRecordMockMvc.perform(post("/api/position-destroy-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyRecordDTO)))
            .andExpect(status().isBadRequest());

        List<PositionDestroyRecord> positionDestroyRecordList = positionDestroyRecordRepository.findAll();
        assertThat(positionDestroyRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPositionDestroyRecords() throws Exception {
        // Initialize the database
        positionDestroyRecordRepository.saveAndFlush(positionDestroyRecord);

        // Get all the positionDestroyRecordList
        restPositionDestroyRecordMockMvc.perform(get("/api/position-destroy-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionDestroyRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getPositionDestroyRecord() throws Exception {
        // Initialize the database
        positionDestroyRecordRepository.saveAndFlush(positionDestroyRecord);

        // Get the positionDestroyRecord
        restPositionDestroyRecordMockMvc.perform(get("/api/position-destroy-records/{id}", positionDestroyRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(positionDestroyRecord.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPositionDestroyRecord() throws Exception {
        // Get the positionDestroyRecord
        restPositionDestroyRecordMockMvc.perform(get("/api/position-destroy-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePositionDestroyRecord() throws Exception {
        // Initialize the database
        positionDestroyRecordRepository.saveAndFlush(positionDestroyRecord);
        int databaseSizeBeforeUpdate = positionDestroyRecordRepository.findAll().size();

        // Update the positionDestroyRecord
        PositionDestroyRecord updatedPositionDestroyRecord = positionDestroyRecordRepository.findOne(positionDestroyRecord.getId());
        updatedPositionDestroyRecord
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        PositionDestroyRecordDTO positionDestroyRecordDTO = positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(updatedPositionDestroyRecord);

        restPositionDestroyRecordMockMvc.perform(put("/api/position-destroy-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyRecordDTO)))
            .andExpect(status().isOk());

        // Validate the PositionDestroyRecord in the database
        List<PositionDestroyRecord> positionDestroyRecordList = positionDestroyRecordRepository.findAll();
        assertThat(positionDestroyRecordList).hasSize(databaseSizeBeforeUpdate);
        PositionDestroyRecord testPositionDestroyRecord = positionDestroyRecordList.get(positionDestroyRecordList.size() - 1);
        assertThat(testPositionDestroyRecord.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPositionDestroyRecord.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingPositionDestroyRecord() throws Exception {
        int databaseSizeBeforeUpdate = positionDestroyRecordRepository.findAll().size();

        // Create the PositionDestroyRecord
        PositionDestroyRecordDTO positionDestroyRecordDTO = positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(positionDestroyRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPositionDestroyRecordMockMvc.perform(put("/api/position-destroy-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionDestroyRecord in the database
        List<PositionDestroyRecord> positionDestroyRecordList = positionDestroyRecordRepository.findAll();
        assertThat(positionDestroyRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePositionDestroyRecord() throws Exception {
        // Initialize the database
        positionDestroyRecordRepository.saveAndFlush(positionDestroyRecord);
        int databaseSizeBeforeDelete = positionDestroyRecordRepository.findAll().size();

        // Get the positionDestroyRecord
        restPositionDestroyRecordMockMvc.perform(delete("/api/position-destroy-records/{id}", positionDestroyRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PositionDestroyRecord> positionDestroyRecordList = positionDestroyRecordRepository.findAll();
        assertThat(positionDestroyRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionDestroyRecord.class);
    }
}
