package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.StockOutApplyProject;
import org.fwoxford.domain.StockOutApply;
import org.fwoxford.domain.Project;
import org.fwoxford.repository.StockOutApplyProjectRepository;
import org.fwoxford.service.StockOutApplyProjectService;
import org.fwoxford.service.dto.StockOutApplyProjectDTO;
import org.fwoxford.service.mapper.StockOutApplyProjectMapper;
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
 * Test class for the StockOutApplyProjectResource REST controller.
 *
 * @see StockOutApplyProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class StockOutApplyProjectResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private StockOutApplyProjectRepository stockOutApplyProjectRepository;

    @Autowired
    private StockOutApplyProjectMapper stockOutApplyProjectMapper;

    @Autowired
    private StockOutApplyProjectService stockOutApplyProjectService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOutApplyProjectMockMvc;

    private StockOutApplyProject stockOutApplyProject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockOutApplyProjectResource stockOutApplyProjectResource = new StockOutApplyProjectResource(stockOutApplyProjectService);
        this.restStockOutApplyProjectMockMvc = MockMvcBuilders.standaloneSetup(stockOutApplyProjectResource)
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
    public static StockOutApplyProject createEntity(EntityManager em) {
        StockOutApplyProject stockOutApplyProject = new StockOutApplyProject()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        StockOutApply stockOutApply = StockOutApplyResourceIntTest.createEntity(em);
        em.persist(stockOutApply);
        em.flush();
        stockOutApplyProject.setStockOutApply(stockOutApply);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        stockOutApplyProject.setProject(project);
        return stockOutApplyProject;
    }

    @Before
    public void initTest() {
        stockOutApplyProject = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOutApplyProject() throws Exception {
        int databaseSizeBeforeCreate = stockOutApplyProjectRepository.findAll().size();

        // Create the StockOutApplyProject
        StockOutApplyProjectDTO stockOutApplyProjectDTO = stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(stockOutApplyProject);

        restStockOutApplyProjectMockMvc.perform(post("/api/stock-out-apply-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyProjectDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutApplyProject in the database
        List<StockOutApplyProject> stockOutApplyProjectList = stockOutApplyProjectRepository.findAll();
        assertThat(stockOutApplyProjectList).hasSize(databaseSizeBeforeCreate + 1);
        StockOutApplyProject testStockOutApplyProject = stockOutApplyProjectList.get(stockOutApplyProjectList.size() - 1);
        assertThat(testStockOutApplyProject.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOutApplyProject.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createStockOutApplyProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOutApplyProjectRepository.findAll().size();

        // Create the StockOutApplyProject with an existing ID
        StockOutApplyProject existingStockOutApplyProject = new StockOutApplyProject();
        existingStockOutApplyProject.setId(1L);
        StockOutApplyProjectDTO existingStockOutApplyProjectDTO = stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(existingStockOutApplyProject);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOutApplyProjectMockMvc.perform(post("/api/stock-out-apply-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStockOutApplyProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StockOutApplyProject> stockOutApplyProjectList = stockOutApplyProjectRepository.findAll();
        assertThat(stockOutApplyProjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOutApplyProjectRepository.findAll().size();
        // set the field null
        stockOutApplyProject.setStatus(null);

        // Create the StockOutApplyProject, which fails.
        StockOutApplyProjectDTO stockOutApplyProjectDTO = stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(stockOutApplyProject);

        restStockOutApplyProjectMockMvc.perform(post("/api/stock-out-apply-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyProjectDTO)))
            .andExpect(status().isBadRequest());

        List<StockOutApplyProject> stockOutApplyProjectList = stockOutApplyProjectRepository.findAll();
        assertThat(stockOutApplyProjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOutApplyProjects() throws Exception {
        // Initialize the database
        stockOutApplyProjectRepository.saveAndFlush(stockOutApplyProject);

        // Get all the stockOutApplyProjectList
        restStockOutApplyProjectMockMvc.perform(get("/api/stock-out-apply-projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOutApplyProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getStockOutApplyProject() throws Exception {
        // Initialize the database
        stockOutApplyProjectRepository.saveAndFlush(stockOutApplyProject);

        // Get the stockOutApplyProject
        restStockOutApplyProjectMockMvc.perform(get("/api/stock-out-apply-projects/{id}", stockOutApplyProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOutApplyProject.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOutApplyProject() throws Exception {
        // Get the stockOutApplyProject
        restStockOutApplyProjectMockMvc.perform(get("/api/stock-out-apply-projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOutApplyProject() throws Exception {
        // Initialize the database
        stockOutApplyProjectRepository.saveAndFlush(stockOutApplyProject);
        int databaseSizeBeforeUpdate = stockOutApplyProjectRepository.findAll().size();

        // Update the stockOutApplyProject
        StockOutApplyProject updatedStockOutApplyProject = stockOutApplyProjectRepository.findOne(stockOutApplyProject.getId());
        updatedStockOutApplyProject
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        StockOutApplyProjectDTO stockOutApplyProjectDTO = stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(updatedStockOutApplyProject);

        restStockOutApplyProjectMockMvc.perform(put("/api/stock-out-apply-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyProjectDTO)))
            .andExpect(status().isOk());

        // Validate the StockOutApplyProject in the database
        List<StockOutApplyProject> stockOutApplyProjectList = stockOutApplyProjectRepository.findAll();
        assertThat(stockOutApplyProjectList).hasSize(databaseSizeBeforeUpdate);
        StockOutApplyProject testStockOutApplyProject = stockOutApplyProjectList.get(stockOutApplyProjectList.size() - 1);
        assertThat(testStockOutApplyProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOutApplyProject.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOutApplyProject() throws Exception {
        int databaseSizeBeforeUpdate = stockOutApplyProjectRepository.findAll().size();

        // Create the StockOutApplyProject
        StockOutApplyProjectDTO stockOutApplyProjectDTO = stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(stockOutApplyProject);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockOutApplyProjectMockMvc.perform(put("/api/stock-out-apply-projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOutApplyProjectDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOutApplyProject in the database
        List<StockOutApplyProject> stockOutApplyProjectList = stockOutApplyProjectRepository.findAll();
        assertThat(stockOutApplyProjectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockOutApplyProject() throws Exception {
        // Initialize the database
        stockOutApplyProjectRepository.saveAndFlush(stockOutApplyProject);
        int databaseSizeBeforeDelete = stockOutApplyProjectRepository.findAll().size();

        // Get the stockOutApplyProject
        restStockOutApplyProjectMockMvc.perform(delete("/api/stock-out-apply-projects/{id}", stockOutApplyProject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOutApplyProject> stockOutApplyProjectList = stockOutApplyProjectRepository.findAll();
        assertThat(stockOutApplyProjectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOutApplyProject.class);
    }
}
