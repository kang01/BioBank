package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.ProjectSite;
import org.fwoxford.repository.ProjectSiteRepository;
import org.fwoxford.service.ProjectSiteService;
import org.fwoxford.service.dto.ProjectSiteDTO;
import org.fwoxford.service.mapper.ProjectSiteMapper;
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
 * Test class for the ProjectSiteResource REST controller.
 *
 * @see ProjectSiteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class ProjectSiteResourceIntTest {

    private static final String DEFAULT_PROJECT_SITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_SITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_SITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ProjectSiteRepository projectSiteRepository;

    @Autowired
    private ProjectSiteMapper projectSiteMapper;

    @Autowired
    private ProjectSiteService projectSiteService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectSiteMockMvc;

    private ProjectSite projectSite;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectSiteResource projectSiteResource = new ProjectSiteResource(projectSiteService);
        this.restProjectSiteMockMvc = MockMvcBuilders.standaloneSetup(projectSiteResource)
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
    public static ProjectSite createEntity(EntityManager em) {
        ProjectSite projectSite = new ProjectSite()
                .projectSiteCode(DEFAULT_PROJECT_SITE_CODE)
                .projectSiteName(DEFAULT_PROJECT_SITE_NAME)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        return projectSite;
    }

    @Before
    public void initTest() {
        projectSite = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectSite() throws Exception {
        int databaseSizeBeforeCreate = projectSiteRepository.findAll().size();

        // Create the ProjectSite
        ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(projectSite);

        restProjectSiteMockMvc.perform(post("/api/project-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSiteDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectSite in the database
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectSite testProjectSite = projectSiteList.get(projectSiteList.size() - 1);
        assertThat(testProjectSite.getProjectSiteCode()).isEqualTo(DEFAULT_PROJECT_SITE_CODE);
        assertThat(testProjectSite.getProjectSiteName()).isEqualTo(DEFAULT_PROJECT_SITE_NAME);
        assertThat(testProjectSite.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testProjectSite.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createProjectSiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectSiteRepository.findAll().size();

        // Create the ProjectSite with an existing ID
        ProjectSite existingProjectSite = new ProjectSite();
        existingProjectSite.setId(1L);
        ProjectSiteDTO existingProjectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(existingProjectSite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectSiteMockMvc.perform(post("/api/project-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProjectSiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectSiteCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectSiteRepository.findAll().size();
        // set the field null
        projectSite.setProjectSiteCode(null);

        // Create the ProjectSite, which fails.
        ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(projectSite);

        restProjectSiteMockMvc.perform(post("/api/project-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSiteDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectSiteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectSiteRepository.findAll().size();
        // set the field null
        projectSite.setProjectSiteName(null);

        // Create the ProjectSite, which fails.
        ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(projectSite);

        restProjectSiteMockMvc.perform(post("/api/project-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSiteDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectSiteRepository.findAll().size();
        // set the field null
        projectSite.setStatus(null);

        // Create the ProjectSite, which fails.
        ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(projectSite);

        restProjectSiteMockMvc.perform(post("/api/project-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSiteDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectSites() throws Exception {
        // Initialize the database
        projectSiteRepository.saveAndFlush(projectSite);

        // Get all the projectSiteList
        restProjectSiteMockMvc.perform(get("/api/project-sites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectSite.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectSiteCode").value(hasItem(DEFAULT_PROJECT_SITE_CODE.toString())))
            .andExpect(jsonPath("$.[*].projectSiteName").value(hasItem(DEFAULT_PROJECT_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getProjectSite() throws Exception {
        // Initialize the database
        projectSiteRepository.saveAndFlush(projectSite);

        // Get the projectSite
        restProjectSiteMockMvc.perform(get("/api/project-sites/{id}", projectSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectSite.getId().intValue()))
            .andExpect(jsonPath("$.projectSiteCode").value(DEFAULT_PROJECT_SITE_CODE.toString()))
            .andExpect(jsonPath("$.projectSiteName").value(DEFAULT_PROJECT_SITE_NAME.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectSite() throws Exception {
        // Get the projectSite
        restProjectSiteMockMvc.perform(get("/api/project-sites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectSite() throws Exception {
        // Initialize the database
        projectSiteRepository.saveAndFlush(projectSite);
        int databaseSizeBeforeUpdate = projectSiteRepository.findAll().size();

        // Update the projectSite
        ProjectSite updatedProjectSite = projectSiteRepository.findOne(projectSite.getId());
        updatedProjectSite
                .projectSiteCode(UPDATED_PROJECT_SITE_CODE)
                .projectSiteName(UPDATED_PROJECT_SITE_NAME)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(updatedProjectSite);

        restProjectSiteMockMvc.perform(put("/api/project-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSiteDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectSite in the database
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeUpdate);
        ProjectSite testProjectSite = projectSiteList.get(projectSiteList.size() - 1);
        assertThat(testProjectSite.getProjectSiteCode()).isEqualTo(UPDATED_PROJECT_SITE_CODE);
        assertThat(testProjectSite.getProjectSiteName()).isEqualTo(UPDATED_PROJECT_SITE_NAME);
        assertThat(testProjectSite.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testProjectSite.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectSite() throws Exception {
        int databaseSizeBeforeUpdate = projectSiteRepository.findAll().size();

        // Create the ProjectSite
        ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(projectSite);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectSiteMockMvc.perform(put("/api/project-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSiteDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectSite in the database
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectSite() throws Exception {
        // Initialize the database
        projectSiteRepository.saveAndFlush(projectSite);
        int databaseSizeBeforeDelete = projectSiteRepository.findAll().size();

        // Get the projectSite
        restProjectSiteMockMvc.perform(delete("/api/project-sites/{id}", projectSite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        assertThat(projectSiteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectSite.class);
    }
}
