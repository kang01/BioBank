package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.BoxAndTube;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.BoxAndTubeRepository;
import org.fwoxford.service.BoxAndTubeService;
import org.fwoxford.service.dto.BoxAndTubeDTO;
import org.fwoxford.service.mapper.BoxAndTubeMapper;
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
 * Test class for the BoxAndTubeResource REST controller.
 *
 * @see BoxAndTubeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class BoxAndTubeResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private BoxAndTubeRepository boxAndTubeRepository;

    @Autowired
    private BoxAndTubeMapper boxAndTubeMapper;

    @Autowired
    private BoxAndTubeService boxAndTubeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoxAndTubeMockMvc;

    private BoxAndTube boxAndTube;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoxAndTubeResource boxAndTubeResource = new BoxAndTubeResource(boxAndTubeService);
        this.restBoxAndTubeMockMvc = MockMvcBuilders.standaloneSetup(boxAndTubeResource)
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
    public static BoxAndTube createEntity(EntityManager em) {
        BoxAndTube boxAndTube = new BoxAndTube()
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        // Add required entity
        FrozenBox frozenBox = FrozenBoxResourceIntTest.createEntity(em);
        em.persist(frozenBox);
        em.flush();
        boxAndTube.setFrozenBox(frozenBox);
        // Add required entity
        FrozenTube frozenTube = FrozenTubeResourceIntTest.createEntity(em);
        em.persist(frozenTube);
        em.flush();
        boxAndTube.setFrozenTube(frozenTube);
        return boxAndTube;
    }

    @Before
    public void initTest() {
        boxAndTube = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoxAndTube() throws Exception {
        int databaseSizeBeforeCreate = boxAndTubeRepository.findAll().size();

        // Create the BoxAndTube
        BoxAndTubeDTO boxAndTubeDTO = boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(boxAndTube);

        restBoxAndTubeMockMvc.perform(post("/api/box-and-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxAndTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the BoxAndTube in the database
        List<BoxAndTube> boxAndTubeList = boxAndTubeRepository.findAll();
        assertThat(boxAndTubeList).hasSize(databaseSizeBeforeCreate + 1);
        BoxAndTube testBoxAndTube = boxAndTubeList.get(boxAndTubeList.size() - 1);
        assertThat(testBoxAndTube.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBoxAndTube.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createBoxAndTubeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxAndTubeRepository.findAll().size();

        // Create the BoxAndTube with an existing ID
        BoxAndTube existingBoxAndTube = new BoxAndTube();
        existingBoxAndTube.setId(1L);
        BoxAndTubeDTO existingBoxAndTubeDTO = boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(existingBoxAndTube);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoxAndTubeMockMvc.perform(post("/api/box-and-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBoxAndTubeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BoxAndTube> boxAndTubeList = boxAndTubeRepository.findAll();
        assertThat(boxAndTubeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = boxAndTubeRepository.findAll().size();
        // set the field null
        boxAndTube.setStatus(null);

        // Create the BoxAndTube, which fails.
        BoxAndTubeDTO boxAndTubeDTO = boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(boxAndTube);

        restBoxAndTubeMockMvc.perform(post("/api/box-and-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxAndTubeDTO)))
            .andExpect(status().isBadRequest());

        List<BoxAndTube> boxAndTubeList = boxAndTubeRepository.findAll();
        assertThat(boxAndTubeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoxAndTubes() throws Exception {
        // Initialize the database
        boxAndTubeRepository.saveAndFlush(boxAndTube);

        // Get all the boxAndTubeList
        restBoxAndTubeMockMvc.perform(get("/api/box-and-tubes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boxAndTube.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getBoxAndTube() throws Exception {
        // Initialize the database
        boxAndTubeRepository.saveAndFlush(boxAndTube);

        // Get the boxAndTube
        restBoxAndTubeMockMvc.perform(get("/api/box-and-tubes/{id}", boxAndTube.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boxAndTube.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBoxAndTube() throws Exception {
        // Get the boxAndTube
        restBoxAndTubeMockMvc.perform(get("/api/box-and-tubes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoxAndTube() throws Exception {
        // Initialize the database
        boxAndTubeRepository.saveAndFlush(boxAndTube);
        int databaseSizeBeforeUpdate = boxAndTubeRepository.findAll().size();

        // Update the boxAndTube
        BoxAndTube updatedBoxAndTube = boxAndTubeRepository.findOne(boxAndTube.getId());
        updatedBoxAndTube
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        BoxAndTubeDTO boxAndTubeDTO = boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(updatedBoxAndTube);

        restBoxAndTubeMockMvc.perform(put("/api/box-and-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxAndTubeDTO)))
            .andExpect(status().isOk());

        // Validate the BoxAndTube in the database
        List<BoxAndTube> boxAndTubeList = boxAndTubeRepository.findAll();
        assertThat(boxAndTubeList).hasSize(databaseSizeBeforeUpdate);
        BoxAndTube testBoxAndTube = boxAndTubeList.get(boxAndTubeList.size() - 1);
        assertThat(testBoxAndTube.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBoxAndTube.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingBoxAndTube() throws Exception {
        int databaseSizeBeforeUpdate = boxAndTubeRepository.findAll().size();

        // Create the BoxAndTube
        BoxAndTubeDTO boxAndTubeDTO = boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(boxAndTube);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoxAndTubeMockMvc.perform(put("/api/box-and-tubes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxAndTubeDTO)))
            .andExpect(status().isCreated());

        // Validate the BoxAndTube in the database
        List<BoxAndTube> boxAndTubeList = boxAndTubeRepository.findAll();
        assertThat(boxAndTubeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBoxAndTube() throws Exception {
        // Initialize the database
        boxAndTubeRepository.saveAndFlush(boxAndTube);
        int databaseSizeBeforeDelete = boxAndTubeRepository.findAll().size();

        // Get the boxAndTube
        restBoxAndTubeMockMvc.perform(delete("/api/box-and-tubes/{id}", boxAndTube.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BoxAndTube> boxAndTubeList = boxAndTubeRepository.findAll();
        assertThat(boxAndTubeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoxAndTube.class);
    }
}
