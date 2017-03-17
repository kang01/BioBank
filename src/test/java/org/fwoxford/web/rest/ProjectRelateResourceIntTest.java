package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.ProjectRelate;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.ProjectSite;
import org.fwoxford.repository.ProjectRelateRepository;
import org.fwoxford.service.ProjectRelateService;
import org.fwoxford.service.dto.ProjectRelateDTO;
import org.fwoxford.service.mapper.ProjectRelateMapper;
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
 * Test class for the ProjectRelateResource REST controller.
 *
 * @see ProjectRelateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class ProjectRelateResourceIntTest {

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ProjectRelateRepository projectRelateRepository;

    @Autowired
    private ProjectRelateMapper projectRelateMapper;

    @Autowired
    private ProjectRelateService projectRelateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectRelateMockMvc;

    private ProjectRelate projectRelate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectRelateResource projectRelateResource = new ProjectRelateResource(projectRelateService);
        this.restProjectRelateMockMvc = MockMvcBuilders.standaloneSetup(projectRelateResource)
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
    public static ProjectRelate createEntity(EntityManager em) {
        ProjectRelate projectRelate = new ProjectRelate()
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectRelate.setProject(project);
        // Add required entity
        ProjectSite projectSite = ProjectSiteResourceIntTest.createEntity(em);
        em.persist(projectSite);
        em.flush();
        projectRelate.setProjectSite(projectSite);
        return projectRelate;
    }

    @Before
    public void initTest() {
        projectRelate = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectRelate() throws Exception {
        int databaseSizeBeforeCreate = projectRelateRepository.findAll().size();

        // Create the ProjectRelate
        ProjectRelateDTO projectRelateDTO = projectRelateMapper.projectRelateToProjectRelateDTO(projectRelate);

        restProjectRelateMockMvc.perform(post("/api/project-relates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectRelateDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectRelate in the database
        List<ProjectRelate> projectRelateList = projectRelateRepository.findAll();
        assertThat(projectRelateList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectRelate testProjectRelate = projectRelateList.get(projectRelateList.size() - 1);
        assertThat(testProjectRelate.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testProjectRelate.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createProjectRelateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRelateRepository.findAll().size();

        // Create the ProjectRelate with an existing ID
        ProjectRelate existingProjectRelate = new ProjectRelate();
        existingProjectRelate.setId(1L);
        ProjectRelateDTO existingProjectRelateDTO = projectRelateMapper.projectRelateToProjectRelateDTO(existingProjectRelate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectRelateMockMvc.perform(post("/api/project-relates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProjectRelateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProjectRelate> projectRelateList = projectRelateRepository.findAll();
        assertThat(projectRelateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRelateRepository.findAll().size();
        // set the field null
        projectRelate.setStatus(null);

        // Create the ProjectRelate, which fails.
        ProjectRelateDTO projectRelateDTO = projectRelateMapper.projectRelateToProjectRelateDTO(projectRelate);

        restProjectRelateMockMvc.perform(post("/api/project-relates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectRelateDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectRelate> projectRelateList = projectRelateRepository.findAll();
        assertThat(projectRelateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectRelates() throws Exception {
        // Initialize the database
        projectRelateRepository.saveAndFlush(projectRelate);

        // Get all the projectRelateList
        restProjectRelateMockMvc.perform(get("/api/project-relates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectRelate.getId().intValue())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getProjectRelate() throws Exception {
        // Initialize the database
        projectRelateRepository.saveAndFlush(projectRelate);

        // Get the projectRelate
        restProjectRelateMockMvc.perform(get("/api/project-relates/{id}", projectRelate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectRelate.getId().intValue()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectRelate() throws Exception {
        // Get the projectRelate
        restProjectRelateMockMvc.perform(get("/api/project-relates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectRelate() throws Exception {
        // Initialize the database
        projectRelateRepository.saveAndFlush(projectRelate);
        int databaseSizeBeforeUpdate = projectRelateRepository.findAll().size();

        // Update the projectRelate
        ProjectRelate updatedProjectRelate = projectRelateRepository.findOne(projectRelate.getId());
        updatedProjectRelate
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        ProjectRelateDTO projectRelateDTO = projectRelateMapper.projectRelateToProjectRelateDTO(updatedProjectRelate);

        restProjectRelateMockMvc.perform(put("/api/project-relates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectRelateDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectRelate in the database
        List<ProjectRelate> projectRelateList = projectRelateRepository.findAll();
        assertThat(projectRelateList).hasSize(databaseSizeBeforeUpdate);
        ProjectRelate testProjectRelate = projectRelateList.get(projectRelateList.size() - 1);
        assertThat(testProjectRelate.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testProjectRelate.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectRelate() throws Exception {
        int databaseSizeBeforeUpdate = projectRelateRepository.findAll().size();

        // Create the ProjectRelate
        ProjectRelateDTO projectRelateDTO = projectRelateMapper.projectRelateToProjectRelateDTO(projectRelate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectRelateMockMvc.perform(put("/api/project-relates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectRelateDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectRelate in the database
        List<ProjectRelate> projectRelateList = projectRelateRepository.findAll();
        assertThat(projectRelateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectRelate() throws Exception {
        // Initialize the database
        projectRelateRepository.saveAndFlush(projectRelate);
        int databaseSizeBeforeDelete = projectRelateRepository.findAll().size();

        // Get the projectRelate
        restProjectRelateMockMvc.perform(delete("/api/project-relates/{id}", projectRelate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectRelate> projectRelateList = projectRelateRepository.findAll();
        assertThat(projectRelateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectRelate.class);
    }
}
