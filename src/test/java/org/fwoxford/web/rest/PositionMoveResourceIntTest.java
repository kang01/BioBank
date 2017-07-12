package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.PositionMove;
import org.fwoxford.repository.PositionMoveRepository;
import org.fwoxford.service.PositionMoveService;
import org.fwoxford.service.dto.PositionMoveDTO;
import org.fwoxford.service.mapper.PositionMoveMapper;
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
 * Test class for the PositionMoveResource REST controller.
 *
 * @see PositionMoveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class PositionMoveResourceIntTest {

    private static final String DEFAULT_MOVE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_MOVE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_MOVE_AFFECT = "AAAAAAAAAA";
    private static final String UPDATED_MOVE_AFFECT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WHETHER_FREEZING_AND_THAWING = false;
    private static final Boolean UPDATED_WHETHER_FREEZING_AND_THAWING = true;

    private static final String DEFAULT_MOVE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MOVE_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_OPERATOR_ID_1 = 1L;
    private static final Long UPDATED_OPERATOR_ID_1 = 2L;

    private static final Long DEFAULT_OPERATOR_ID_2 = 1L;
    private static final Long UPDATED_OPERATOR_ID_2 = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private PositionMoveRepository positionMoveRepository;

    @Autowired
    private PositionMoveMapper positionMoveMapper;

    @Autowired
    private PositionMoveService positionMoveService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPositionMoveMockMvc;

    private PositionMove positionMove;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionMoveResource positionMoveResource = new PositionMoveResource(positionMoveService);
        this.restPositionMoveMockMvc = MockMvcBuilders.standaloneSetup(positionMoveResource)
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
    public static PositionMove createEntity(EntityManager em) {
        PositionMove positionMove = new PositionMove()
                .moveReason(DEFAULT_MOVE_REASON)
                .moveAffect(DEFAULT_MOVE_AFFECT)
                .whetherFreezingAndThawing(DEFAULT_WHETHER_FREEZING_AND_THAWING)
                .moveType(DEFAULT_MOVE_TYPE)
                .operatorId1(DEFAULT_OPERATOR_ID_1)
                .operatorId2(DEFAULT_OPERATOR_ID_2)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return positionMove;
    }

    @Before
    public void initTest() {
        positionMove = createEntity(em);
    }

    @Test
    @Transactional
    public void createPositionMove() throws Exception {
        int databaseSizeBeforeCreate = positionMoveRepository.findAll().size();

        // Create the PositionMove
        PositionMoveDTO positionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);

        restPositionMoveMockMvc.perform(post("/api/position-moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionMove in the database
        List<PositionMove> positionMoveList = positionMoveRepository.findAll();
        assertThat(positionMoveList).hasSize(databaseSizeBeforeCreate + 1);
        PositionMove testPositionMove = positionMoveList.get(positionMoveList.size() - 1);
        assertThat(testPositionMove.getMoveReason()).isEqualTo(DEFAULT_MOVE_REASON);
        assertThat(testPositionMove.getMoveAffect()).isEqualTo(DEFAULT_MOVE_AFFECT);
        assertThat(testPositionMove.isWhetherFreezingAndThawing()).isEqualTo(DEFAULT_WHETHER_FREEZING_AND_THAWING);
        assertThat(testPositionMove.getMoveType()).isEqualTo(DEFAULT_MOVE_TYPE);
        assertThat(testPositionMove.getOperatorId1()).isEqualTo(DEFAULT_OPERATOR_ID_1);
        assertThat(testPositionMove.getOperatorId2()).isEqualTo(DEFAULT_OPERATOR_ID_2);
        assertThat(testPositionMove.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPositionMove.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createPositionMoveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionMoveRepository.findAll().size();

        // Create the PositionMove with an existing ID
        PositionMove existingPositionMove = new PositionMove();
        existingPositionMove.setId(1L);
        PositionMoveDTO existingPositionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(existingPositionMove);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMoveMockMvc.perform(post("/api/position-moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPositionMoveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PositionMove> positionMoveList = positionMoveRepository.findAll();
        assertThat(positionMoveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMoveTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRepository.findAll().size();
        // set the field null
        positionMove.setMoveType(null);

        // Create the PositionMove, which fails.
        PositionMoveDTO positionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);

        restPositionMoveMockMvc.perform(post("/api/position-moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMove> positionMoveList = positionMoveRepository.findAll();
        assertThat(positionMoveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMoveRepository.findAll().size();
        // set the field null
        positionMove.setStatus(null);

        // Create the PositionMove, which fails.
        PositionMoveDTO positionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);

        restPositionMoveMockMvc.perform(post("/api/position-moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMove> positionMoveList = positionMoveRepository.findAll();
        assertThat(positionMoveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPositionMoves() throws Exception {
        // Initialize the database
        positionMoveRepository.saveAndFlush(positionMove);

        // Get all the positionMoveList
        restPositionMoveMockMvc.perform(get("/api/position-moves?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionMove.getId().intValue())))
            .andExpect(jsonPath("$.[*].moveReason").value(hasItem(DEFAULT_MOVE_REASON.toString())))
            .andExpect(jsonPath("$.[*].moveAffect").value(hasItem(DEFAULT_MOVE_AFFECT.toString())))
            .andExpect(jsonPath("$.[*].whetherFreezingAndThawing").value(hasItem(DEFAULT_WHETHER_FREEZING_AND_THAWING.booleanValue())))
            .andExpect(jsonPath("$.[*].moveType").value(hasItem(DEFAULT_MOVE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].operatorId1").value(hasItem(DEFAULT_OPERATOR_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].operatorId2").value(hasItem(DEFAULT_OPERATOR_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getPositionMove() throws Exception {
        // Initialize the database
        positionMoveRepository.saveAndFlush(positionMove);

        // Get the positionMove
        restPositionMoveMockMvc.perform(get("/api/position-moves/{id}", positionMove.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(positionMove.getId().intValue()))
            .andExpect(jsonPath("$.moveReason").value(DEFAULT_MOVE_REASON.toString()))
            .andExpect(jsonPath("$.moveAffect").value(DEFAULT_MOVE_AFFECT.toString()))
            .andExpect(jsonPath("$.whetherFreezingAndThawing").value(DEFAULT_WHETHER_FREEZING_AND_THAWING.booleanValue()))
            .andExpect(jsonPath("$.moveType").value(DEFAULT_MOVE_TYPE.toString()))
            .andExpect(jsonPath("$.operatorId1").value(DEFAULT_OPERATOR_ID_1.intValue()))
            .andExpect(jsonPath("$.operatorId2").value(DEFAULT_OPERATOR_ID_2.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPositionMove() throws Exception {
        // Get the positionMove
        restPositionMoveMockMvc.perform(get("/api/position-moves/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePositionMove() throws Exception {
        // Initialize the database
        positionMoveRepository.saveAndFlush(positionMove);
        int databaseSizeBeforeUpdate = positionMoveRepository.findAll().size();

        // Update the positionMove
        PositionMove updatedPositionMove = positionMoveRepository.findOne(positionMove.getId());
        updatedPositionMove
                .moveReason(UPDATED_MOVE_REASON)
                .moveAffect(UPDATED_MOVE_AFFECT)
                .whetherFreezingAndThawing(UPDATED_WHETHER_FREEZING_AND_THAWING)
                .moveType(UPDATED_MOVE_TYPE)
                .operatorId1(UPDATED_OPERATOR_ID_1)
                .operatorId2(UPDATED_OPERATOR_ID_2)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        PositionMoveDTO positionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(updatedPositionMove);

        restPositionMoveMockMvc.perform(put("/api/position-moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveDTO)))
            .andExpect(status().isOk());

        // Validate the PositionMove in the database
        List<PositionMove> positionMoveList = positionMoveRepository.findAll();
        assertThat(positionMoveList).hasSize(databaseSizeBeforeUpdate);
        PositionMove testPositionMove = positionMoveList.get(positionMoveList.size() - 1);
        assertThat(testPositionMove.getMoveReason()).isEqualTo(UPDATED_MOVE_REASON);
        assertThat(testPositionMove.getMoveAffect()).isEqualTo(UPDATED_MOVE_AFFECT);
        assertThat(testPositionMove.isWhetherFreezingAndThawing()).isEqualTo(UPDATED_WHETHER_FREEZING_AND_THAWING);
        assertThat(testPositionMove.getMoveType()).isEqualTo(UPDATED_MOVE_TYPE);
        assertThat(testPositionMove.getOperatorId1()).isEqualTo(UPDATED_OPERATOR_ID_1);
        assertThat(testPositionMove.getOperatorId2()).isEqualTo(UPDATED_OPERATOR_ID_2);
        assertThat(testPositionMove.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPositionMove.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingPositionMove() throws Exception {
        int databaseSizeBeforeUpdate = positionMoveRepository.findAll().size();

        // Create the PositionMove
        PositionMoveDTO positionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPositionMoveMockMvc.perform(put("/api/position-moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMoveDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionMove in the database
        List<PositionMove> positionMoveList = positionMoveRepository.findAll();
        assertThat(positionMoveList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePositionMove() throws Exception {
        // Initialize the database
        positionMoveRepository.saveAndFlush(positionMove);
        int databaseSizeBeforeDelete = positionMoveRepository.findAll().size();

        // Get the positionMove
        restPositionMoveMockMvc.perform(delete("/api/position-moves/{id}", positionMove.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PositionMove> positionMoveList = positionMoveRepository.findAll();
        assertThat(positionMoveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionMove.class);
    }
}
