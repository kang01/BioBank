package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.PositionDestroy;
import org.fwoxford.repository.PositionDestroyRepository;
import org.fwoxford.service.PositionDestroyService;
import org.fwoxford.service.dto.PositionDestroyDTO;
import org.fwoxford.service.mapper.PositionDestroyMapper;
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
 * Test class for the PositionDestroyResource REST controller.
 *
 * @see PositionDestroyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class PositionDestroyResourceIntTest {

    private static final String DEFAULT_DESTROY_REASON = "AAAAAAAAAA";
    private static final String UPDATED_DESTROY_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_DESTROY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DESTROY_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_OPERATOR_ID_1 = 1L;
    private static final Long UPDATED_OPERATOR_ID_1 = 2L;

    private static final Long DEFAULT_OPERATOR_ID_2 = 1L;
    private static final Long UPDATED_OPERATOR_ID_2 = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private PositionDestroyRepository positionDestroyRepository;

    @Autowired
    private PositionDestroyMapper positionDestroyMapper;

    @Autowired
    private PositionDestroyService positionDestroyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPositionDestroyMockMvc;

    private PositionDestroy positionDestroy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionDestroyResource positionDestroyResource = new PositionDestroyResource(positionDestroyService);
        this.restPositionDestroyMockMvc = MockMvcBuilders.standaloneSetup(positionDestroyResource)
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
    public static PositionDestroy createEntity(EntityManager em) {
        PositionDestroy positionDestroy = new PositionDestroy()
                .destroyReason(DEFAULT_DESTROY_REASON)
                .destroyType(DEFAULT_DESTROY_TYPE)
                .operatorId1(DEFAULT_OPERATOR_ID_1)
                .operatorId2(DEFAULT_OPERATOR_ID_2)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return positionDestroy;
    }

    @Before
    public void initTest() {
        positionDestroy = createEntity(em);
    }

    @Test
    @Transactional
    public void createPositionDestroy() throws Exception {
        int databaseSizeBeforeCreate = positionDestroyRepository.findAll().size();

        // Create the PositionDestroy
        PositionDestroyDTO positionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);

        restPositionDestroyMockMvc.perform(post("/api/position-destroys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionDestroy in the database
        List<PositionDestroy> positionDestroyList = positionDestroyRepository.findAll();
        assertThat(positionDestroyList).hasSize(databaseSizeBeforeCreate + 1);
        PositionDestroy testPositionDestroy = positionDestroyList.get(positionDestroyList.size() - 1);
        assertThat(testPositionDestroy.getDestroyReason()).isEqualTo(DEFAULT_DESTROY_REASON);
        assertThat(testPositionDestroy.getDestroyType()).isEqualTo(DEFAULT_DESTROY_TYPE);
        assertThat(testPositionDestroy.getOperatorId1()).isEqualTo(DEFAULT_OPERATOR_ID_1);
        assertThat(testPositionDestroy.getOperatorId2()).isEqualTo(DEFAULT_OPERATOR_ID_2);
        assertThat(testPositionDestroy.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPositionDestroy.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createPositionDestroyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionDestroyRepository.findAll().size();

        // Create the PositionDestroy with an existing ID
        PositionDestroy existingPositionDestroy = new PositionDestroy();
        existingPositionDestroy.setId(1L);
        PositionDestroyDTO existingPositionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(existingPositionDestroy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionDestroyMockMvc.perform(post("/api/position-destroys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPositionDestroyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PositionDestroy> positionDestroyList = positionDestroyRepository.findAll();
        assertThat(positionDestroyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDestroyTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionDestroyRepository.findAll().size();
        // set the field null
        positionDestroy.setDestroyType(null);

        // Create the PositionDestroy, which fails.
        PositionDestroyDTO positionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);

        restPositionDestroyMockMvc.perform(post("/api/position-destroys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyDTO)))
            .andExpect(status().isBadRequest());

        List<PositionDestroy> positionDestroyList = positionDestroyRepository.findAll();
        assertThat(positionDestroyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionDestroyRepository.findAll().size();
        // set the field null
        positionDestroy.setStatus(null);

        // Create the PositionDestroy, which fails.
        PositionDestroyDTO positionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);

        restPositionDestroyMockMvc.perform(post("/api/position-destroys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyDTO)))
            .andExpect(status().isBadRequest());

        List<PositionDestroy> positionDestroyList = positionDestroyRepository.findAll();
        assertThat(positionDestroyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPositionDestroys() throws Exception {
        // Initialize the database
        positionDestroyRepository.saveAndFlush(positionDestroy);

        // Get all the positionDestroyList
        restPositionDestroyMockMvc.perform(get("/api/position-destroys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionDestroy.getId().intValue())))
            .andExpect(jsonPath("$.[*].destroyReason").value(hasItem(DEFAULT_DESTROY_REASON.toString())))
            .andExpect(jsonPath("$.[*].destroyType").value(hasItem(DEFAULT_DESTROY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].operatorId1").value(hasItem(DEFAULT_OPERATOR_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].operatorId2").value(hasItem(DEFAULT_OPERATOR_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getPositionDestroy() throws Exception {
        // Initialize the database
        positionDestroyRepository.saveAndFlush(positionDestroy);

        // Get the positionDestroy
        restPositionDestroyMockMvc.perform(get("/api/position-destroys/{id}", positionDestroy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(positionDestroy.getId().intValue()))
            .andExpect(jsonPath("$.destroyReason").value(DEFAULT_DESTROY_REASON.toString()))
            .andExpect(jsonPath("$.destroyType").value(DEFAULT_DESTROY_TYPE.toString()))
            .andExpect(jsonPath("$.operatorId1").value(DEFAULT_OPERATOR_ID_1.intValue()))
            .andExpect(jsonPath("$.operatorId2").value(DEFAULT_OPERATOR_ID_2.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPositionDestroy() throws Exception {
        // Get the positionDestroy
        restPositionDestroyMockMvc.perform(get("/api/position-destroys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePositionDestroy() throws Exception {
        // Initialize the database
        positionDestroyRepository.saveAndFlush(positionDestroy);
        int databaseSizeBeforeUpdate = positionDestroyRepository.findAll().size();

        // Update the positionDestroy
        PositionDestroy updatedPositionDestroy = positionDestroyRepository.findOne(positionDestroy.getId());
        updatedPositionDestroy
                .destroyReason(UPDATED_DESTROY_REASON)
                .destroyType(UPDATED_DESTROY_TYPE)
                .operatorId1(UPDATED_OPERATOR_ID_1)
                .operatorId2(UPDATED_OPERATOR_ID_2)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        PositionDestroyDTO positionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(updatedPositionDestroy);

        restPositionDestroyMockMvc.perform(put("/api/position-destroys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyDTO)))
            .andExpect(status().isOk());

        // Validate the PositionDestroy in the database
        List<PositionDestroy> positionDestroyList = positionDestroyRepository.findAll();
        assertThat(positionDestroyList).hasSize(databaseSizeBeforeUpdate);
        PositionDestroy testPositionDestroy = positionDestroyList.get(positionDestroyList.size() - 1);
        assertThat(testPositionDestroy.getDestroyReason()).isEqualTo(UPDATED_DESTROY_REASON);
        assertThat(testPositionDestroy.getDestroyType()).isEqualTo(UPDATED_DESTROY_TYPE);
        assertThat(testPositionDestroy.getOperatorId1()).isEqualTo(UPDATED_OPERATOR_ID_1);
        assertThat(testPositionDestroy.getOperatorId2()).isEqualTo(UPDATED_OPERATOR_ID_2);
        assertThat(testPositionDestroy.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPositionDestroy.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingPositionDestroy() throws Exception {
        int databaseSizeBeforeUpdate = positionDestroyRepository.findAll().size();

        // Create the PositionDestroy
        PositionDestroyDTO positionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPositionDestroyMockMvc.perform(put("/api/position-destroys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionDestroyDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionDestroy in the database
        List<PositionDestroy> positionDestroyList = positionDestroyRepository.findAll();
        assertThat(positionDestroyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePositionDestroy() throws Exception {
        // Initialize the database
        positionDestroyRepository.saveAndFlush(positionDestroy);
        int databaseSizeBeforeDelete = positionDestroyRepository.findAll().size();

        // Get the positionDestroy
        restPositionDestroyMockMvc.perform(delete("/api/position-destroys/{id}", positionDestroy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PositionDestroy> positionDestroyList = positionDestroyRepository.findAll();
        assertThat(positionDestroyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionDestroy.class);
    }
}
