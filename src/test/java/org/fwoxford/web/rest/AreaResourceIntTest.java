package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Area;
import org.fwoxford.domain.Equipment;
import org.fwoxford.repository.AreaRepository;
import org.fwoxford.service.AreaService;
import org.fwoxford.service.dto.AreaDTO;
import org.fwoxford.service.mapper.AreaMapper;
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
 * Test class for the AreaResource REST controller.
 *
 * @see AreaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class AreaResourceIntTest {

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FREEZE_FRAME_NUMBER = 100;
    private static final Integer UPDATED_FREEZE_FRAME_NUMBER = 99;

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_CODE = "BBBBBBBBBB";

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private AreaService areaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAreaMockMvc;

    private Area area;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AreaResource areaResource = new AreaResource(areaService);
        this.restAreaMockMvc = MockMvcBuilders.standaloneSetup(areaResource)
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
    public static Area createEntity(EntityManager em) {
        Area area = new Area()
                .areaCode(DEFAULT_AREA_CODE)
                .freezeFrameNumber(DEFAULT_FREEZE_FRAME_NUMBER)
                .memo(DEFAULT_MEMO)
                .status(DEFAULT_STATUS)
                .equipmentCode(DEFAULT_EQUIPMENT_CODE);
        // Add required entity
        Equipment equipment = EquipmentResourceIntTest.createEntity(em);
        em.persist(equipment);
        em.flush();
        area.setEquipment(equipment);
        return area;
    }

    @Before
    public void initTest() {
        area = createEntity(em);
    }

    @Test
    @Transactional
    public void createArea() throws Exception {
        int databaseSizeBeforeCreate = areaRepository.findAll().size();

        // Create the Area
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(area);

        restAreaMockMvc.perform(post("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(areaDTO)))
            .andExpect(status().isCreated());

        // Validate the Area in the database
        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeCreate + 1);
        Area testArea = areaList.get(areaList.size() - 1);
        assertThat(testArea.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testArea.getFreezeFrameNumber()).isEqualTo(DEFAULT_FREEZE_FRAME_NUMBER);
        assertThat(testArea.getMemo()).isEqualTo(DEFAULT_MEMO);
        assertThat(testArea.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testArea.getEquipmentCode()).isEqualTo(DEFAULT_EQUIPMENT_CODE);
    }

    @Test
    @Transactional
    public void createAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = areaRepository.findAll().size();

        // Create the Area with an existing ID
        Area existingArea = new Area();
        existingArea.setId(1L);
        AreaDTO existingAreaDTO = areaMapper.areaToAreaDTO(existingArea);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAreaMockMvc.perform(post("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAreaCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = areaRepository.findAll().size();
        // set the field null
        area.setAreaCode(null);

        // Create the Area, which fails.
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(area);

        restAreaMockMvc.perform(post("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(areaDTO)))
            .andExpect(status().isBadRequest());

        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFreezeFrameNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = areaRepository.findAll().size();
        // set the field null
        area.setFreezeFrameNumber(null);

        // Create the Area, which fails.
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(area);

        restAreaMockMvc.perform(post("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(areaDTO)))
            .andExpect(status().isBadRequest());

        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = areaRepository.findAll().size();
        // set the field null
        area.setStatus(null);

        // Create the Area, which fails.
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(area);

        restAreaMockMvc.perform(post("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(areaDTO)))
            .andExpect(status().isBadRequest());

        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = areaRepository.findAll().size();
        // set the field null
        area.setEquipmentCode(null);

        // Create the Area, which fails.
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(area);

        restAreaMockMvc.perform(post("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(areaDTO)))
            .andExpect(status().isBadRequest());

        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAreas() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);

        // Get all the areaList
        restAreaMockMvc.perform(get("/api/areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(area.getId().intValue())))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE.toString())))
            .andExpect(jsonPath("$.[*].freezeFrameNumber").value(hasItem(DEFAULT_FREEZE_FRAME_NUMBER)))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].equipmentCode").value(hasItem(DEFAULT_EQUIPMENT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getArea() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);

        // Get the area
        restAreaMockMvc.perform(get("/api/areas/{id}", area.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(area.getId().intValue()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE.toString()))
            .andExpect(jsonPath("$.freezeFrameNumber").value(DEFAULT_FREEZE_FRAME_NUMBER))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.equipmentCode").value(DEFAULT_EQUIPMENT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArea() throws Exception {
        // Get the area
        restAreaMockMvc.perform(get("/api/areas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArea() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);
        int databaseSizeBeforeUpdate = areaRepository.findAll().size();

        // Update the area
        Area updatedArea = areaRepository.findOne(area.getId());
        updatedArea
                .areaCode(UPDATED_AREA_CODE)
                .freezeFrameNumber(UPDATED_FREEZE_FRAME_NUMBER)
                .memo(UPDATED_MEMO)
                .status(UPDATED_STATUS)
                .equipmentCode(UPDATED_EQUIPMENT_CODE);
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(updatedArea);

        restAreaMockMvc.perform(put("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(areaDTO)))
            .andExpect(status().isOk());

        // Validate the Area in the database
        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeUpdate);
        Area testArea = areaList.get(areaList.size() - 1);
        assertThat(testArea.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testArea.getFreezeFrameNumber()).isEqualTo(UPDATED_FREEZE_FRAME_NUMBER);
        assertThat(testArea.getMemo()).isEqualTo(UPDATED_MEMO);
        assertThat(testArea.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testArea.getEquipmentCode()).isEqualTo(UPDATED_EQUIPMENT_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingArea() throws Exception {
        int databaseSizeBeforeUpdate = areaRepository.findAll().size();

        // Create the Area
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(area);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAreaMockMvc.perform(put("/api/areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(areaDTO)))
            .andExpect(status().isCreated());

        // Validate the Area in the database
        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArea() throws Exception {
        // Initialize the database
        areaRepository.saveAndFlush(area);
        int databaseSizeBeforeDelete = areaRepository.findAll().size();

        // Get the area
        restAreaMockMvc.perform(delete("/api/areas/{id}", area.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Area> areaList = areaRepository.findAll();
        assertThat(areaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Area.class);
    }
}
