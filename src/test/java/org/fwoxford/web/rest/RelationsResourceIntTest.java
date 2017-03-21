package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Relations;
import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.domain.FrozenTubeType;
import org.fwoxford.domain.SampleType;
import org.fwoxford.repository.RelationsRepository;
import org.fwoxford.service.RelationsService;
import org.fwoxford.service.dto.RelationsDTO;
import org.fwoxford.service.mapper.RelationsMapper;
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
 * Test class for the RelationsResource REST controller.
 *
 * @see RelationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class RelationsResourceIntTest {

    private static final String DEFAULT_PROJECT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private RelationsRepository relationsRepository;

    @Autowired
    private RelationsMapper relationsMapper;

    @Autowired
    private RelationsService relationsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRelationsMockMvc;

    private Relations relations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RelationsResource relationsResource = new RelationsResource(relationsService);
        this.restRelationsMockMvc = MockMvcBuilders.standaloneSetup(relationsResource)
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
    public static Relations createEntity(EntityManager em) {
        Relations relations = new Relations()
                .projectCode(DEFAULT_PROJECT_CODE)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS);
        // Add required entity
        FrozenBoxType frozenBoxType = FrozenBoxTypeResourceIntTest.createEntity(em);
        em.persist(frozenBoxType);
        em.flush();
        relations.setFrozenBoxType(frozenBoxType);
        // Add required entity
        FrozenTubeType frozenTubeType = FrozenTubeTypeResourceIntTest.createEntity(em);
        em.persist(frozenTubeType);
        em.flush();
        relations.setFrozenTubeType(frozenTubeType);
        // Add required entity
        SampleType sampleType = SampleTypeResourceIntTest.createEntity(em);
        em.persist(sampleType);
        em.flush();
        relations.setSampleType(sampleType);
        return relations;
    }

    @Before
    public void initTest() {
        relations = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelations() throws Exception {
        int databaseSizeBeforeCreate = relationsRepository.findAll().size();

        // Create the Relations
        RelationsDTO relationsDTO = relationsMapper.relationsToRelationsDTO(relations);

        restRelationsMockMvc.perform(post("/api/relations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationsDTO)))
            .andExpect(status().isCreated());

        // Validate the Relations in the database
        List<Relations> relationsList = relationsRepository.findAll();
        assertThat(relationsList).hasSize(databaseSizeBeforeCreate + 1);
        Relations testRelations = relationsList.get(relationsList.size() - 1);
        assertThat(testRelations.getProjectCode()).isEqualTo(DEFAULT_PROJECT_CODE);
        assertThat(testRelations.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testRelations.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRelationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = relationsRepository.findAll().size();

        // Create the Relations with an existing ID
        Relations existingRelations = new Relations();
        existingRelations.setId(1L);
        RelationsDTO existingRelationsDTO = relationsMapper.relationsToRelationsDTO(existingRelations);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelationsMockMvc.perform(post("/api/relations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRelationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Relations> relationsList = relationsRepository.findAll();
        assertThat(relationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = relationsRepository.findAll().size();
        // set the field null
        relations.setProjectCode(null);

        // Create the Relations, which fails.
        RelationsDTO relationsDTO = relationsMapper.relationsToRelationsDTO(relations);

        restRelationsMockMvc.perform(post("/api/relations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationsDTO)))
            .andExpect(status().isBadRequest());

        List<Relations> relationsList = relationsRepository.findAll();
        assertThat(relationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = relationsRepository.findAll().size();
        // set the field null
        relations.setStatus(null);

        // Create the Relations, which fails.
        RelationsDTO relationsDTO = relationsMapper.relationsToRelationsDTO(relations);

        restRelationsMockMvc.perform(post("/api/relations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationsDTO)))
            .andExpect(status().isBadRequest());

        List<Relations> relationsList = relationsRepository.findAll();
        assertThat(relationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRelations() throws Exception {
        // Initialize the database
        relationsRepository.saveAndFlush(relations);

        // Get all the relationsList
        restRelationsMockMvc.perform(get("/api/relations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relations.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectCode").value(hasItem(DEFAULT_PROJECT_CODE.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getRelations() throws Exception {
        // Initialize the database
        relationsRepository.saveAndFlush(relations);

        // Get the relations
        restRelationsMockMvc.perform(get("/api/relations/{id}", relations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(relations.getId().intValue()))
            .andExpect(jsonPath("$.projectCode").value(DEFAULT_PROJECT_CODE.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRelations() throws Exception {
        // Get the relations
        restRelationsMockMvc.perform(get("/api/relations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelations() throws Exception {
        // Initialize the database
        relationsRepository.saveAndFlush(relations);
        int databaseSizeBeforeUpdate = relationsRepository.findAll().size();

        // Update the relations
        Relations updatedRelations = relationsRepository.findOne(relations.getId());
        updatedRelations
                .projectCode(UPDATED_PROJECT_CODE)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS);
        RelationsDTO relationsDTO = relationsMapper.relationsToRelationsDTO(updatedRelations);

        restRelationsMockMvc.perform(put("/api/relations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationsDTO)))
            .andExpect(status().isOk());

        // Validate the Relations in the database
        List<Relations> relationsList = relationsRepository.findAll();
        assertThat(relationsList).hasSize(databaseSizeBeforeUpdate);
        Relations testRelations = relationsList.get(relationsList.size() - 1);
        assertThat(testRelations.getProjectCode()).isEqualTo(UPDATED_PROJECT_CODE);
        assertThat(testRelations.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testRelations.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRelations() throws Exception {
        int databaseSizeBeforeUpdate = relationsRepository.findAll().size();

        // Create the Relations
        RelationsDTO relationsDTO = relationsMapper.relationsToRelationsDTO(relations);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRelationsMockMvc.perform(put("/api/relations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationsDTO)))
            .andExpect(status().isCreated());

        // Validate the Relations in the database
        List<Relations> relationsList = relationsRepository.findAll();
        assertThat(relationsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRelations() throws Exception {
        // Initialize the database
        relationsRepository.saveAndFlush(relations);
        int databaseSizeBeforeDelete = relationsRepository.findAll().size();

        // Get the relations
        restRelationsMockMvc.perform(delete("/api/relations/{id}", relations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Relations> relationsList = relationsRepository.findAll();
        assertThat(relationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Relations.class);
    }
}
