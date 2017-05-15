package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutFiles;
import org.fwoxford.repository.StockOutFilesRepository;
import org.fwoxford.service.StockOutFilesService;
import org.fwoxford.service.dto.StockOutFilesDTO;
import org.fwoxford.service.mapper.StockOutFilesMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockOutFilesResource REST controller.
 *
 * @see StockOutFilesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutFilesResourceIntTest {

    private static final Long DEFAULT_BUSINESS_ID = 1L;
    private static final Long UPDATED_BUSINESS_ID = 2L;

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;

    private static final byte[] DEFAULT_FILES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILES = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILES_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutFilesRepository stockOutFilesRepository;

    @Autowired
    private StockOutFilesMapper stockOutFilesMapper;

    @Autowired
    private StockOutFilesService stockOutFilesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutFilesMockMvc;

    private StockOutFiles stockOutFiles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutFilesResource stockOutFilesResource = new StockOutFilesResource(stockOutFilesService);
        this.restStockOutFilesMockMvc = MockMvcBuilders.standaloneSetup(stockOutFilesResource)
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
    public static StockOutFiles createEntity(EntityManager em) {
        StockOutFiles stockOutFiles = new StockOutFiles()
                .businessId(DEFAULT_BUSINESS_ID)
                .filePath(DEFAULT_FILE_PATH)
                .fileName(DEFAULT_FILE_NAME)
                .fileType(DEFAULT_FILE_TYPE)
                .fileSize(DEFAULT_FILE_SIZE)
                .files(DEFAULT_FILES)
                .filesContentType(DEFAULT_FILES_CONTENT_TYPE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return stockOutFiles;
    }

    @Before
    public void initTest() {
        stockOutFiles = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutFiles() throws Exception {
        int databaseSizeBeforeCreate = stockOutFilesRepository.findAll().size();

        // Create the StockOutFiles
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);

        restStockOutFilesMockMvc.perform(post("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutFiles in the database
        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutFiles testStockOutFiles = stockOutFilesList.get(stockOutFilesList.size() - 1);
        assertThat(testStockOutFiles.getBusinessId()).isEqualTo(DEFAULT_BUSINESS_ID);
        assertThat(testStockOutFiles.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testStockOutFiles.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testStockOutFiles.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testStockOutFiles.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testStockOutFiles.getFiles()).isEqualTo(DEFAULT_FILES);
        assertThat(testStockOutFiles.getFilesContentType()).isEqualTo(DEFAULT_FILES_CONTENT_TYPE);
        assertThat(testStockOutFiles.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutFiles.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutFilesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutFilesRepository.findAll().size();

        // Create the StockOutFiles with an existing ID
        StockOutFiles existingStockOutFiles = new StockOutFiles();
        existingStockOutFiles.setId(1L);
        StockOutFilesDTO existingStockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(existingStockOutFiles);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutFilesMockMvc.perform(post("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutFilesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBusinessIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFilesRepository.findAll().size();
        // set the field null
        stockOutFiles.setBusinessId(null);

        // Create the StockOutFiles, which fails.
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);

        restStockOutFilesMockMvc.perform(post("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFilesRepository.findAll().size();
        // set the field null
        stockOutFiles.setFilePath(null);

        // Create the StockOutFiles, which fails.
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);

        restStockOutFilesMockMvc.perform(post("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFilesRepository.findAll().size();
        // set the field null
        stockOutFiles.setFileName(null);

        // Create the StockOutFiles, which fails.
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);

        restStockOutFilesMockMvc.perform(post("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFilesRepository.findAll().size();
        // set the field null
        stockOutFiles.setFileType(null);

        // Create the StockOutFiles, which fails.
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);

        restStockOutFilesMockMvc.perform(post("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutFilesRepository.findAll().size();
        // set the field null
        stockOutFiles.setStatus(null);

        // Create the StockOutFiles, which fails.
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);

        restStockOutFilesMockMvc.perform(post("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutFiles() throws Exception {
        // Initialize the database
        stockOutFilesRepository.saveAndFlush(stockOutFiles);

        // Get all the stockOutFilesList
        restStockOutFilesMockMvc.perform(get("/api/stock-out-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutFiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessId").value(hasItem(DEFAULT_BUSINESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].filesContentType").value(hasItem(DEFAULT_FILES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].files").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILES))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutFiles() throws Exception {
        // Initialize the database
        stockOutFilesRepository.saveAndFlush(stockOutFiles);

        // Get the stockOutFiles
        restStockOutFilesMockMvc.perform(get("/api/stock-out-files/{id}", stockOutFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutFiles.getId().intValue()))
            .andExpect(jsonPath("$.businessId").value(DEFAULT_BUSINESS_ID.intValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.filesContentType").value(DEFAULT_FILES_CONTENT_TYPE))
            .andExpect(jsonPath("$.files").value(Base64Utils.encodeToString(DEFAULT_FILES)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutFiles() throws Exception {
        // Get the stockOutFiles
        restStockOutFilesMockMvc.perform(get("/api/stock-out-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutFiles() throws Exception {
        // Initialize the database
        stockOutFilesRepository.saveAndFlush(stockOutFiles);
        int databaseSizeBeforeUpdate = stockOutFilesRepository.findAll().size();

        // Update the stockOutFiles
        StockOutFiles updatedStockOutFiles = stockOutFilesRepository.findOne(stockOutFiles.getId());
        updatedStockOutFiles
                .businessId(UPDATED_BUSINESS_ID)
                .filePath(UPDATED_FILE_PATH)
                .fileName(UPDATED_FILE_NAME)
                .fileType(UPDATED_FILE_TYPE)
                .fileSize(UPDATED_FILE_SIZE)
                .files(UPDATED_FILES)
                .filesContentType(UPDATED_FILES_CONTENT_TYPE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(updatedStockOutFiles);

        restStockOutFilesMockMvc.perform(put("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutFiles in the database
        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeUpdate);
        StockOutFiles testStockOutFiles = stockOutFilesList.get(stockOutFilesList.size() - 1);
        assertThat(testStockOutFiles.getBusinessId()).isEqualTo(UPDATED_BUSINESS_ID);
        assertThat(testStockOutFiles.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testStockOutFiles.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testStockOutFiles.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testStockOutFiles.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testStockOutFiles.getFiles()).isEqualTo(UPDATED_FILES);
        assertThat(testStockOutFiles.getFilesContentType()).isEqualTo(UPDATED_FILES_CONTENT_TYPE);
        assertThat(testStockOutFiles.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutFiles.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutFiles() throws Exception {
        int databaseSizeBeforeUpdate = stockOutFilesRepository.findAll().size();

        // Create the StockOutFiles
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutFilesMockMvc.perform(put("/api/stock-out-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutFilesDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutFiles in the database
        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutFiles() throws Exception {
        // Initialize the database
        stockOutFilesRepository.saveAndFlush(stockOutFiles);
        int databaseSizeBeforeDelete = stockOutFilesRepository.findAll().size();

        // Get the stockOutFiles
        restStockOutFilesMockMvc.perform(delete("/api/stock-out-files/{id}", stockOutFiles.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutFiles> stockOutFilesList = stockOutFilesRepository.findAll();
        assertThat(stockOutFilesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutFiles.class);
    }
}
