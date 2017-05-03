package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.ProjectSampleClass;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.SampleType;
import org.fwoxford.repository.ProjectSampleClassRepository;
import org.fwoxford.service.ProjectSampleClassService;
import org.fwoxford.service.dto.ProjectSampleClassDTO;
import org.fwoxford.service.mapper.ProjectSampleClassMapper;
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
 * Test class for the ProjectSampleClassResource REST controller.
 *
 * @see ProjectSampleClassResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class ProjectSampleClassResourceIntTest {

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMNS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COLUMNS_NUMBER = "BBBBBBBBBB";

    @Autowired
    private ProjectSampleClassRepository projectSampleClassRepository;

    @Autowired
    private ProjectSampleClassMapper projectSampleClassMapper;

    @Autowired
    private ProjectSampleClassService projectSampleClassService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectSampleClassMockMvc;

    private ProjectSampleClass projectSampleClass;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectSampleClassResource projectSampleClassResource = new ProjectSampleClassResource(projectSampleClassService);
        this.restProjectSampleClassMockMvc = MockMvcBuilders.standaloneSetup(projectSampleClassResource)
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
    public static ProjectSampleClass createEntity(EntityManager em) {
        ProjectSampleClass projectSampleClass = new ProjectSampleClass()
                .projectCode(DEFAULT_PROJECT_CODE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO)
                .columnsNumber(DEFAULT_COLUMNS_NUMBER);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectSampleClass.setProject(project);
        // Add required entity
        SampleType sampleType = SampleTypeResourceIntTest.createEntity(em);
        em.persist(sampleType);
        em.flush();
        projectSampleClass.setSampleType(sampleType);
        return projectSampleClass;
    }

    @Before
    public void initTest() {
        projectSampleClass = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectSampleClass() throws Exception {
        int databaseSizeBeforeCreate = projectSampleClassRepository.findAll().size();

        // Create the ProjectSampleClass
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);

        restProjectSampleClassMockMvc.perform(post("/api/project-sample-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSampleClassDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectSampleClass in the database
        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findAll();
        assertThat(projectSampleClassList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectSampleClass testProjectSampleClass = projectSampleClassList.get(projectSampleClassList.size() - 1);
        assertThat(testProjectSampleClass.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testProjectSampleClass.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProjectSampleClass.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testProjectSampleClass.getColumnsNumber()).isEqualTo(DEFAULT_COLUMNS_NUMBER);
    }

    @Test
    @Transactional
    public void createProjectSampleClassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectSampleClassRepository.findAll().size();

        // Create the ProjectSampleClass with an existing ID
        ProjectSampleClass existingProjectSampleClass = new ProjectSampleClass();
        existingProjectSampleClass.setId(1L);
        ProjectSampleClassDTO existingProjectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(existingProjectSampleClass);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectSampleClassMockMvc.perform(post("/api/project-sample-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProjectSampleClassDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findAll();
        assertThat(projectSampleClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectSampleClassRepository.findAll().size();
        // set the field null
        projectSampleClass.setProjectCode(null);

        // Create the ProjectSampleClass, which fails.
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);

        restProjectSampleClassMockMvc.perform(post("/api/project-sample-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSampleClassDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findAll();
        assertThat(projectSampleClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectSampleClassRepository.findAll().size();
        // set the field null
        projectSampleClass.setStatus(null);

        // Create the ProjectSampleClass, which fails.
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);

        restProjectSampleClassMockMvc.perform(post("/api/project-sample-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSampleClassDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findAll();
        assertThat(projectSampleClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectSampleClasses() throws Exception {
        // Initialize the database
        projectSampleClassRepository.saveAndFlush(projectSampleClass);

        // Get all the projectSampleClassList
        restProjectSampleClassMockMvc.perform(get("/api/project-sample-classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectSampleClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].columnsNumber").value(hasItem(DEFAULT_COLUMNS_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getProjectSampleClass() throws Exception {
        // Initialize the database
        projectSampleClassRepository.saveAndFlush(projectSampleClass);

        // Get the projectSampleClass
        restProjectSampleClassMockMvc.perform(get("/api/project-sample-classes/{id}", projectSampleClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectSampleClass.getId().intValue()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.columnsNumber").value(DEFAULT_COLUMNS_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectSampleClass() throws Exception {
        // Get the projectSampleClass
        restProjectSampleClassMockMvc.perform(get("/api/project-sample-classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectSampleClass() throws Exception {
        // Initialize the database
        projectSampleClassRepository.saveAndFlush(projectSampleClass);
        int databaseSizeBeforeUpdate = projectSampleClassRepository.findAll().size();

        // Update the projectSampleClass
        ProjectSampleClass updatedProjectSampleClass = projectSampleClassRepository.findOne(projectSampleClass.getId());
        updatedProjectSampleClass
                .projectCode(UPDATED_PROJECT_CODE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO)
                .columnsNumber(UPDATED_COLUMNS_NUMBER);
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(updatedProjectSampleClass);

        restProjectSampleClassMockMvc.perform(put("/api/project-sample-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSampleClassDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectSampleClass in the database
        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findAll();
        assertThat(projectSampleClassList).hasSize(databaseSizeBeforeUpdate);
        ProjectSampleClass testProjectSampleClass = projectSampleClassList.get(projectSampleClassList.size() - 1);
        assertThat(testProjectSampleClass.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testProjectSampleClass.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProjectSampleClass.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testProjectSampleClass.getColumnsNumber()).isEqualTo(UPDATED_COLUMNS_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectSampleClass() throws Exception {
        int databaseSizeBeforeUpdate = projectSampleClassRepository.findAll().size();

        // Create the ProjectSampleClass
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectSampleClassMockMvc.perform(put("/api/project-sample-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectSampleClassDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectSampleClass in the database
        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findAll();
        assertThat(projectSampleClassList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectSampleClass() throws Exception {
        // Initialize the database
        projectSampleClassRepository.saveAndFlush(projectSampleClass);
        int databaseSizeBeforeDelete = projectSampleClassRepository.findAll().size();

        // Get the projectSampleClass
        restProjectSampleClassMockMvc.perform(delete("/api/project-sample-classes/{id}", projectSampleClass.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findAll();
        assertThat(projectSampleClassList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectSampleClass.class);
    }
}
