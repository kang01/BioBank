package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.PositionChangeRecord;
import org.fwoxford.domain.PositionChange;
import org.fwoxford.repository.PositionChangeRecordRepository;
import org.fwoxford.service.PositionChangeRecordService;
import org.fwoxford.service.dto.PositionChangeRecordDTO;
import org.fwoxford.service.mapper.PositionChangeRecordMapper;
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
 * Test class for the PositionChangeRecordResource REST controller.
 *
 * @see PositionChangeRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class PositionChangeRecordResourceIntTest {

    @Autowired
    private PositionChangeRecordRepository positionChangeRecordRepository;

    @Autowired
    private PositionChangeRecordMapper positionChangeRecordMapper;

    @Autowired
    private PositionChangeRecordService positionChangeRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPositionChangeRecordMockMvc;

    private PositionChangeRecord positionChangeRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionChangeRecordResource positionChangeRecordResource = new PositionChangeRecordResource(positionChangeRecordService);
        this.restPositionChangeRecordMockMvc = MockMvcBuilders.standaloneSetup(positionChangeRecordResource)
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
    public static PositionChangeRecord createEntity(EntityManager em) {
        PositionChangeRecord positionChangeRecord = new PositionChangeRecord();
        // Add required entity
        PositionChange positionChange = PositionChangeResourceIntTest.createEntity(em);
        em.persist(positionChange);
        em.flush();
        positionChangeRecord.setPositionChange(positionChange);
        return positionChangeRecord;
    }

    @Before
    public void initTest() {
        positionChangeRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createPositionChangeRecord() throws Exception {
        int databaseSizeBeforeCreate = positionChangeRecordRepository.findAll().size();

        // Create the PositionChangeRecord
        PositionChangeRecordDTO positionChangeRecordDTO = positionChangeRecordMapper.positionChangeRecordToPositionChangeRecordDTO(positionChangeRecord);

        restPositionChangeRecordMockMvc.perform(post("/api/position-change-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionChangeRecord in the database
        List<PositionChangeRecord> positionChangeRecordList = positionChangeRecordRepository.findAll();
        assertThat(positionChangeRecordList).hasSize(databaseSizeBeforeCreate + 1);
        PositionChangeRecord testPositionChangeRecord = positionChangeRecordList.get(positionChangeRecordList.size() - 1);
    }

    @Test
    @Transactional
    public void createPositionChangeRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionChangeRecordRepository.findAll().size();

        // Create the PositionChangeRecord with an existing ID
        PositionChangeRecord existingPositionChangeRecord = new PositionChangeRecord();
        existingPositionChangeRecord.setId(1L);
        PositionChangeRecordDTO existingPositionChangeRecordDTO = positionChangeRecordMapper.positionChangeRecordToPositionChangeRecordDTO(existingPositionChangeRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionChangeRecordMockMvc.perform(post("/api/position-change-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPositionChangeRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PositionChangeRecord> positionChangeRecordList = positionChangeRecordRepository.findAll();
        assertThat(positionChangeRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPositionChangeRecords() throws Exception {
        // Initialize the database
        positionChangeRecordRepository.saveAndFlush(positionChangeRecord);

        // Get all the positionChangeRecordList
        restPositionChangeRecordMockMvc.perform(get("/api/position-change-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionChangeRecord.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPositionChangeRecord() throws Exception {
        // Initialize the database
        positionChangeRecordRepository.saveAndFlush(positionChangeRecord);

        // Get the positionChangeRecord
        restPositionChangeRecordMockMvc.perform(get("/api/position-change-records/{id}", positionChangeRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(positionChangeRecord.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPositionChangeRecord() throws Exception {
        // Get the positionChangeRecord
        restPositionChangeRecordMockMvc.perform(get("/api/position-change-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePositionChangeRecord() throws Exception {
        // Initialize the database
        positionChangeRecordRepository.saveAndFlush(positionChangeRecord);
        int databaseSizeBeforeUpdate = positionChangeRecordRepository.findAll().size();

        // Update the positionChangeRecord
        PositionChangeRecord updatedPositionChangeRecord = positionChangeRecordRepository.findOne(positionChangeRecord.getId());
        PositionChangeRecordDTO positionChangeRecordDTO = positionChangeRecordMapper.positionChangeRecordToPositionChangeRecordDTO(updatedPositionChangeRecord);

        restPositionChangeRecordMockMvc.perform(put("/api/position-change-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeRecordDTO)))
            .andExpect(status().isOk());

        // Validate the PositionChangeRecord in the database
        List<PositionChangeRecord> positionChangeRecordList = positionChangeRecordRepository.findAll();
        assertThat(positionChangeRecordList).hasSize(databaseSizeBeforeUpdate);
        PositionChangeRecord testPositionChangeRecord = positionChangeRecordList.get(positionChangeRecordList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingPositionChangeRecord() throws Exception {
        int databaseSizeBeforeUpdate = positionChangeRecordRepository.findAll().size();

        // Create the PositionChangeRecord
        PositionChangeRecordDTO positionChangeRecordDTO = positionChangeRecordMapper.positionChangeRecordToPositionChangeRecordDTO(positionChangeRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPositionChangeRecordMockMvc.perform(put("/api/position-change-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionChangeRecord in the database
        List<PositionChangeRecord> positionChangeRecordList = positionChangeRecordRepository.findAll();
        assertThat(positionChangeRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePositionChangeRecord() throws Exception {
        // Initialize the database
        positionChangeRecordRepository.saveAndFlush(positionChangeRecord);
        int databaseSizeBeforeDelete = positionChangeRecordRepository.findAll().size();

        // Get the positionChangeRecord
        restPositionChangeRecordMockMvc.perform(delete("/api/position-change-records/{id}", positionChangeRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PositionChangeRecord> positionChangeRecordList = positionChangeRecordRepository.findAll();
        assertThat(positionChangeRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionChangeRecord.class);
    }
}
