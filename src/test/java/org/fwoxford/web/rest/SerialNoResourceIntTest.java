package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.SerialNo;
import org.fwoxford.repository.SerialNoRepository;
import org.fwoxford.service.SerialNoService;
import org.fwoxford.service.dto.SerialNoDTO;
import org.fwoxford.service.mapper.SerialNoMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SerialNoResource REST controller.
 *
 * @see SerialNoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class SerialNoResourceIntTest {

    private static final String DEFAULT_SERIAL_NO = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NO = "BBBBBBBBBB";

    private static final String DEFAULT_MACHINE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MACHINE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_USED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_USED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SerialNoRepository serialNoRepository;

    @Autowired
    private SerialNoMapper serialNoMapper;

    @Autowired
    private SerialNoService serialNoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSerialNoMockMvc;

    private SerialNo serialNo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SerialNoResource serialNoResource = new SerialNoResource(serialNoService);
        this.restSerialNoMockMvc = MockMvcBuilders.standaloneSetup(serialNoResource)
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
    public static SerialNo createEntity(EntityManager em) {
        SerialNo serialNo = new SerialNo()
                .serialNo(DEFAULT_SERIAL_NO)
                .machineNo(DEFAULT_MACHINE_NO)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .usedDate(DEFAULT_USED_DATE);
        return serialNo;
    }

    @Before
    public void initTest() {
        serialNo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSerialNo() throws Exception {
        int databaseSizeBeforeCreate = serialNoRepository.findAll().size();

        // Create the SerialNo
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(serialNo);

        restSerialNoMockMvc.perform(post("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serialNoDTO)))
            .andExpect(status().isCreated());

        // Validate the SerialNo in the database
        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeCreate + 1);
        SerialNo testSerialNo = serialNoList.get(serialNoList.size() - 1);
        assertThat(testSerialNo.getSerialNo()).isEqualTo(DEFAULT_SERIAL_NO);
        assertThat(testSerialNo.getMachineNo()).isEqualTo(DEFAULT_MACHINE_NO);
        assertThat(testSerialNo.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSerialNo.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testSerialNo.getUsedDate()).isEqualTo(DEFAULT_USED_DATE);
    }

    @Test
    @Transactional
    public void createSerialNoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serialNoRepository.findAll().size();

        // Create the SerialNo with an existing ID
        SerialNo existingSerialNo = new SerialNo();
        existingSerialNo.setId(1L);
        SerialNoDTO existingSerialNoDTO = serialNoMapper.serialNoToSerialNoDTO(existingSerialNo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSerialNoMockMvc.perform(post("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSerialNoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSerialNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = serialNoRepository.findAll().size();
        // set the field null
        serialNo.setSerialNo(null);

        // Create the SerialNo, which fails.
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(serialNo);

        restSerialNoMockMvc.perform(post("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serialNoDTO)))
            .andExpect(status().isBadRequest());

        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMachineNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = serialNoRepository.findAll().size();
        // set the field null
        serialNo.setMachineNo(null);

        // Create the SerialNo, which fails.
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(serialNo);

        restSerialNoMockMvc.perform(post("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serialNoDTO)))
            .andExpect(status().isBadRequest());

        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = serialNoRepository.findAll().size();
        // set the field null
        serialNo.setStatus(null);

        // Create the SerialNo, which fails.
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(serialNo);

        restSerialNoMockMvc.perform(post("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serialNoDTO)))
            .andExpect(status().isBadRequest());

        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = serialNoRepository.findAll().size();
        // set the field null
        serialNo.setUsedDate(null);

        // Create the SerialNo, which fails.
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(serialNo);

        restSerialNoMockMvc.perform(post("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serialNoDTO)))
            .andExpect(status().isBadRequest());

        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSerialNos() throws Exception {
        // Initialize the database
        serialNoRepository.saveAndFlush(serialNo);

        // Get all the serialNoList
        restSerialNoMockMvc.perform(get("/api/serial-nos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serialNo.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNo").value(hasItem(DEFAULT_SERIAL_NO.toString())))
            .andExpect(jsonPath("$.[*].machineNo").value(hasItem(DEFAULT_MACHINE_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].usedDate").value(hasItem(DEFAULT_USED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSerialNo() throws Exception {
        // Initialize the database
        serialNoRepository.saveAndFlush(serialNo);

        // Get the serialNo
        restSerialNoMockMvc.perform(get("/api/serial-nos/{id}", serialNo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serialNo.getId().intValue()))
            .andExpect(jsonPath("$.serialNo").value(DEFAULT_SERIAL_NO.toString()))
            .andExpect(jsonPath("$.machineNo").value(DEFAULT_MACHINE_NO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.usedDate").value(DEFAULT_USED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSerialNo() throws Exception {
        // Get the serialNo
        restSerialNoMockMvc.perform(get("/api/serial-nos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSerialNo() throws Exception {
        // Initialize the database
        serialNoRepository.saveAndFlush(serialNo);
        int databaseSizeBeforeUpdate = serialNoRepository.findAll().size();

        // Update the serialNo
        SerialNo updatedSerialNo = serialNoRepository.findOne(serialNo.getId());
        updatedSerialNo
                .serialNo(UPDATED_SERIAL_NO)
                .machineNo(UPDATED_MACHINE_NO)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .usedDate(UPDATED_USED_DATE);
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(updatedSerialNo);

        restSerialNoMockMvc.perform(put("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serialNoDTO)))
            .andExpect(status().isOk());

        // Validate the SerialNo in the database
        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeUpdate);
        SerialNo testSerialNo = serialNoList.get(serialNoList.size() - 1);
        assertThat(testSerialNo.getSerialNo()).isEqualTo(UPDATED_SERIAL_NO);
        assertThat(testSerialNo.getMachineNo()).isEqualTo(UPDATED_MACHINE_NO);
        assertThat(testSerialNo.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSerialNo.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testSerialNo.getUsedDate()).isEqualTo(UPDATED_USED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSerialNo() throws Exception {
        int databaseSizeBeforeUpdate = serialNoRepository.findAll().size();

        // Create the SerialNo
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(serialNo);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSerialNoMockMvc.perform(put("/api/serial-nos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serialNoDTO)))
            .andExpect(status().isCreated());

        // Validate the SerialNo in the database
        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSerialNo() throws Exception {
        // Initialize the database
        serialNoRepository.saveAndFlush(serialNo);
        int databaseSizeBeforeDelete = serialNoRepository.findAll().size();

        // Get the serialNo
        restSerialNoMockMvc.perform(delete("/api/serial-nos/{id}", serialNo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SerialNo> serialNoList = serialNoRepository.findAll();
        assertThat(serialNoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SerialNo.class);
    }
}
