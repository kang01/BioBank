package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.PositionChange;
import org.fwoxford.repository.PositionChangeRepository;
import org.fwoxford.service.PositionChangeService;
import org.fwoxford.service.dto.PositionChangeDTO;
import org.fwoxford.service.mapper.PositionChangeMapper;
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
 * Test class for the PositionChangeResource REST controller.
 *
 * @see PositionChangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class PositionChangeResourceIntTest {

    private static final String DEFAULT_CHANGE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WHETHER_FREEZING_AND_THAWING = false;
    private static final Boolean UPDATED_WHETHER_FREEZING_AND_THAWING = true;

    private static final Long DEFAULT_OPERATOR_ID_1 = 1L;
    private static final Long UPDATED_OPERATOR_ID_1 = 2L;

    private static final Long DEFAULT_OPERATOR_ID_2 = 1L;
    private static final Long UPDATED_OPERATOR_ID_2 = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private PositionChangeRepository positionChangeRepository;

    @Autowired
    private PositionChangeMapper positionChangeMapper;

    @Autowired
    private PositionChangeService positionChangeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPositionChangeMockMvc;

    private PositionChange positionChange;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionChangeResource positionChangeResource = new PositionChangeResource(positionChangeService);
        this.restPositionChangeMockMvc = MockMvcBuilders.standaloneSetup(positionChangeResource)
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
    public static PositionChange createEntity(EntityManager em) {
        PositionChange positionChange = new PositionChange()
                .changeReason(DEFAULT_CHANGE_REASON)
                .changeType(DEFAULT_CHANGE_TYPE)
                .whetherFreezingAndThawing(DEFAULT_WHETHER_FREEZING_AND_THAWING)
                .operatorId1(DEFAULT_OPERATOR_ID_1)
                .operatorId2(DEFAULT_OPERATOR_ID_2)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return positionChange;
    }

    @Before
    public void initTest() {
        positionChange = createEntity(em);
    }

    @Test
    @Transactional
    public void createPositionChange() throws Exception {
        int databaseSizeBeforeCreate = positionChangeRepository.findAll().size();

        // Create the PositionChange
        PositionChangeDTO positionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);

        restPositionChangeMockMvc.perform(post("/api/position-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionChange in the database
        List<PositionChange> positionChangeList = positionChangeRepository.findAll();
        assertThat(positionChangeList).hasSize(databaseSizeBeforeCreate + 1);
        PositionChange testPositionChange = positionChangeList.get(positionChangeList.size() - 1);
        assertThat(testPositionChange.getChangeReason()).isEqualTo(DEFAULT_CHANGE_REASON);
        assertThat(testPositionChange.getChangeType()).isEqualTo(DEFAULT_CHANGE_TYPE);
        assertThat(testPositionChange.isWhetherFreezingAndThawing()).isEqualTo(DEFAULT_WHETHER_FREEZING_AND_THAWING);
        assertThat(testPositionChange.getOperatorId1()).isEqualTo(DEFAULT_OPERATOR_ID_1);
        assertThat(testPositionChange.getOperatorId2()).isEqualTo(DEFAULT_OPERATOR_ID_2);
        assertThat(testPositionChange.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPositionChange.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createPositionChangeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionChangeRepository.findAll().size();

        // Create the PositionChange with an existing ID
        PositionChange existingPositionChange = new PositionChange();
        existingPositionChange.setId(1L);
        PositionChangeDTO existingPositionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(existingPositionChange);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionChangeMockMvc.perform(post("/api/position-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPositionChangeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PositionChange> positionChangeList = positionChangeRepository.findAll();
        assertThat(positionChangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkChangeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionChangeRepository.findAll().size();
        // set the field null
        positionChange.setChangeType(null);

        // Create the PositionChange, which fails.
        PositionChangeDTO positionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);

        restPositionChangeMockMvc.perform(post("/api/position-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeDTO)))
            .andExpect(status().isBadRequest());

        List<PositionChange> positionChangeList = positionChangeRepository.findAll();
        assertThat(positionChangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionChangeRepository.findAll().size();
        // set the field null
        positionChange.setStatus(null);

        // Create the PositionChange, which fails.
        PositionChangeDTO positionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);

        restPositionChangeMockMvc.perform(post("/api/position-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeDTO)))
            .andExpect(status().isBadRequest());

        List<PositionChange> positionChangeList = positionChangeRepository.findAll();
        assertThat(positionChangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPositionChanges() throws Exception {
        // Initialize the database
        positionChangeRepository.saveAndFlush(positionChange);

        // Get all the positionChangeList
        restPositionChangeMockMvc.perform(get("/api/position-changes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].changeReason").value(hasItem(DEFAULT_CHANGE_REASON.toString())))
            .andExpect(jsonPath("$.[*].changeType").value(hasItem(DEFAULT_CHANGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].whetherFreezingAndThawing").value(hasItem(DEFAULT_WHETHER_FREEZING_AND_THAWING.booleanValue())))
            .andExpect(jsonPath("$.[*].operatorId1").value(hasItem(DEFAULT_OPERATOR_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].operatorId2").value(hasItem(DEFAULT_OPERATOR_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getPositionChange() throws Exception {
        // Initialize the database
        positionChangeRepository.saveAndFlush(positionChange);

        // Get the positionChange
        restPositionChangeMockMvc.perform(get("/api/position-changes/{id}", positionChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(positionChange.getId().intValue()))
            .andExpect(jsonPath("$.changeReason").value(DEFAULT_CHANGE_REASON.toString()))
            .andExpect(jsonPath("$.changeType").value(DEFAULT_CHANGE_TYPE.toString()))
            .andExpect(jsonPath("$.whetherFreezingAndThawing").value(DEFAULT_WHETHER_FREEZING_AND_THAWING.booleanValue()))
            .andExpect(jsonPath("$.operatorId1").value(DEFAULT_OPERATOR_ID_1.intValue()))
            .andExpect(jsonPath("$.operatorId2").value(DEFAULT_OPERATOR_ID_2.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPositionChange() throws Exception {
        // Get the positionChange
        restPositionChangeMockMvc.perform(get("/api/position-changes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePositionChange() throws Exception {
        // Initialize the database
        positionChangeRepository.saveAndFlush(positionChange);
        int databaseSizeBeforeUpdate = positionChangeRepository.findAll().size();

        // Update the positionChange
        PositionChange updatedPositionChange = positionChangeRepository.findOne(positionChange.getId());
        updatedPositionChange
                .changeReason(UPDATED_CHANGE_REASON)
                .changeType(UPDATED_CHANGE_TYPE)
                .whetherFreezingAndThawing(UPDATED_WHETHER_FREEZING_AND_THAWING)
                .operatorId1(UPDATED_OPERATOR_ID_1)
                .operatorId2(UPDATED_OPERATOR_ID_2)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        PositionChangeDTO positionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(updatedPositionChange);

        restPositionChangeMockMvc.perform(put("/api/position-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeDTO)))
            .andExpect(status().isOk());

        // Validate the PositionChange in the database
        List<PositionChange> positionChangeList = positionChangeRepository.findAll();
        assertThat(positionChangeList).hasSize(databaseSizeBeforeUpdate);
        PositionChange testPositionChange = positionChangeList.get(positionChangeList.size() - 1);
        assertThat(testPositionChange.getChangeReason()).isEqualTo(UPDATED_CHANGE_REASON);
        assertThat(testPositionChange.getChangeType()).isEqualTo(UPDATED_CHANGE_TYPE);
        assertThat(testPositionChange.isWhetherFreezingAndThawing()).isEqualTo(UPDATED_WHETHER_FREEZING_AND_THAWING);
        assertThat(testPositionChange.getOperatorId1()).isEqualTo(UPDATED_OPERATOR_ID_1);
        assertThat(testPositionChange.getOperatorId2()).isEqualTo(UPDATED_OPERATOR_ID_2);
        assertThat(testPositionChange.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPositionChange.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingPositionChange() throws Exception {
        int databaseSizeBeforeUpdate = positionChangeRepository.findAll().size();

        // Create the PositionChange
        PositionChangeDTO positionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPositionChangeMockMvc.perform(put("/api/position-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionChangeDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionChange in the database
        List<PositionChange> positionChangeList = positionChangeRepository.findAll();
        assertThat(positionChangeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePositionChange() throws Exception {
        // Initialize the database
        positionChangeRepository.saveAndFlush(positionChange);
        int databaseSizeBeforeDelete = positionChangeRepository.findAll().size();

        // Get the positionChange
        restPositionChangeMockMvc.perform(delete("/api/position-changes/{id}", positionChange.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PositionChange> positionChangeList = positionChangeRepository.findAll();
        assertThat(positionChangeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionChange.class);
    }
}
