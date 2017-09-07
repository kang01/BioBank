package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Province;
import org.fwoxford.repository.ProvinceRepository;
import org.fwoxford.service.ProvinceService;
import org.fwoxford.service.dto.ProvinceDTO;
import org.fwoxford.service.mapper.ProvinceMapper;
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
 * Test class for the ProvinceResource REST controller.
 *
 * @see ProvinceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class ProvinceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProvinceMockMvc;

    private Province province;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProvinceResource provinceResource = new ProvinceResource(provinceService);
        this.restProvinceMockMvc = MockMvcBuilders.standaloneSetup(provinceResource)
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
    public static Province createEntity(EntityManager em) {
        Province province = new Province()
                .name(DEFAULT_NAME)
                .code(DEFAULT_CODE)
                .longitude(DEFAULT_LONGITUDE)
                .latitude(DEFAULT_LATITUDE)
                .status(DEFAULT_STATUS);
        return province;
    }

    @Before
    public void initTest() {
        province = createEntity(em);
    }

    @Test
    @Transactional
    public void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);

        restProvinceMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvince.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProvince.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testProvince.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testProvince.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createProvinceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // Create the Province with an existing ID
        Province existingProvince = new Province();
        existingProvince.setId(1L);
        ProvinceDTO existingProvinceDTO = provinceMapper.provinceToProvinceDTO(existingProvince);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinceMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProvinceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setName(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);

        restProvinceMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setCode(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);

        restProvinceMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setLongitude(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);

        restProvinceMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setLatitude(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);

        restProvinceMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setStatus(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);

        restProvinceMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList
        restProvinceMockMvc.perform(get("/api/provinces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        Province updatedProvince = provinceRepository.findOne(province.getId());
        updatedProvince
                .name(UPDATED_NAME)
                .code(UPDATED_CODE)
                .longitude(UPDATED_LONGITUDE)
                .latitude(UPDATED_LATITUDE)
                .status(UPDATED_STATUS);
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(updatedProvince);

        restProvinceMockMvc.perform(put("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProvince.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testProvince.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testProvince.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProvinceMockMvc.perform(put("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinceDTO)))
            .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);
        int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Get the province
        restProvinceMockMvc.perform(delete("/api/provinces/{id}", province.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Province.class);
    }
}
