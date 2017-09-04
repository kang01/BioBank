package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Coordinate;
import org.fwoxford.repository.CoordinateRepository;
import org.fwoxford.service.CoordinateService;
import org.fwoxford.service.dto.CoordinateDTO;
import org.fwoxford.service.mapper.CoordinateMapper;
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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoordinateResource REST controller.
 *
 * @see CoordinateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class CoordinateResourceIntTest {

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal("1.0");
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal("2.0");

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal("1.0");
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal("2.0");

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private CoordinateMapper coordinateMapper;

    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoordinateMockMvc;

    private Coordinate coordinate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoordinateResource coordinateResource = new CoordinateResource(coordinateService);
        this.restCoordinateMockMvc = MockMvcBuilders.standaloneSetup(coordinateResource)
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
    public static Coordinate createEntity(EntityManager em) {
        Coordinate coordinate = new Coordinate()
                .province(DEFAULT_PROVINCE)
                .city(DEFAULT_CITY)
                .longitude(DEFAULT_LONGITUDE)
                .latitude(DEFAULT_LATITUDE)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return coordinate;
    }

    @Before
    public void initTest() {
        coordinate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoordinate() throws Exception {
        int databaseSizeBeforeCreate = coordinateRepository.findAll().size();

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);

        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isCreated());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeCreate + 1);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testCoordinate.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCoordinate.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testCoordinate.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testCoordinate.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCoordinate.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createCoordinateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coordinateRepository.findAll().size();

        // Create the Coordinate with an existing ID
        Coordinate existingCoordinate = new Coordinate();
        existingCoordinate.setId(1L);
        CoordinateDTO existingCoordinateDTO = coordinateMapper.coordinateToCoordinateDTO(existingCoordinate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCoordinateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinateRepository.findAll().size();
        // set the field null
        coordinate.setProvince(null);

        // Create the Coordinate, which fails.
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);

        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isBadRequest());

        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinateRepository.findAll().size();
        // set the field null
        coordinate.setCity(null);

        // Create the Coordinate, which fails.
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);

        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isBadRequest());

        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinateRepository.findAll().size();
        // set the field null
        coordinate.setLongitude(null);

        // Create the Coordinate, which fails.
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);

        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isBadRequest());

        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinateRepository.findAll().size();
        // set the field null
        coordinate.setLatitude(null);

        // Create the Coordinate, which fails.
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);

        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isBadRequest());

        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinateRepository.findAll().size();
        // set the field null
        coordinate.setStatus(null);

        // Create the Coordinate, which fails.
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);

        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isBadRequest());

        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCoordinates() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        // Get all the coordinateList
        restCoordinateMockMvc.perform(get("/api/coordinates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coordinate.getId().intValue())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        // Get the coordinate
        restCoordinateMockMvc.perform(get("/api/coordinates/{id}", coordinate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coordinate.getId().intValue()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoordinate() throws Exception {
        // Get the coordinate
        restCoordinateMockMvc.perform(get("/api/coordinates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();

        // Update the coordinate
        Coordinate updatedCoordinate = coordinateRepository.findOne(coordinate.getId());
        updatedCoordinate
                .province(UPDATED_PROVINCE)
                .city(UPDATED_CITY)
                .longitude(UPDATED_LONGITUDE)
                .latitude(UPDATED_LATITUDE)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(updatedCoordinate);

        restCoordinateMockMvc.perform(put("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isOk());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testCoordinate.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCoordinate.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testCoordinate.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testCoordinate.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCoordinate.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();

        // Create the Coordinate
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoordinateMockMvc.perform(put("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinateDTO)))
            .andExpect(status().isCreated());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);
        int databaseSizeBeforeDelete = coordinateRepository.findAll().size();

        // Get the coordinate
        restCoordinateMockMvc.perform(delete("/api/coordinates/{id}", coordinate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coordinate.class);
    }
}
