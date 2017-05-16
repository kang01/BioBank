package org.fwoxford.web.rest;

import org.fwoxford.BioBankApp;

import org.fwoxford.domain.Delegate;
import org.fwoxford.repository.DelegateRepository;
import org.fwoxford.service.DelegateService;
import org.fwoxford.service.dto.DelegateDTO;
import org.fwoxford.service.mapper.DelegateMapper;
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
 * Test class for the DelegateResource REST controller.
 *
 * @see DelegateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
public class DelegateResourceIntTest {

    private static final String DEFAULT_DELEGATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DELEGATE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DELEGATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DELEGATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    @Autowired
    private DelegateRepository delegateRepository;

    @Autowired
    private DelegateMapper delegateMapper;

    @Autowired
    private DelegateService delegateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDelegateMockMvc;

    private Delegate delegate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DelegateResource delegateResource = new DelegateResource(delegateService);
        this.restDelegateMockMvc = MockMvcBuilders.standaloneSetup(delegateResource)
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
    public static Delegate createEntity(EntityManager em) {
        Delegate delegate = new Delegate()
                .delegateCode(DEFAULT_DELEGATE_CODE)
                .delegateName(DEFAULT_DELEGATE_NAME)
                .status(DEFAULT_STATUS)
                .memo(DEFAULT_MEMO);
        return delegate;
    }

    @Before
    public void initTest() {
        delegate = createEntity(em);
    }

    @Test
    @Transactional
    public void createDelegate() throws Exception {
        int databaseSizeBeforeCreate = delegateRepository.findAll().size();

        // Create the Delegate
        DelegateDTO delegateDTO = delegateMapper.delegateToDelegateDTO(delegate);

        restDelegateMockMvc.perform(post("/api/delegates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delegateDTO)))
            .andExpect(status().isCreated());

        // Validate the Delegate in the database
        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeCreate + 1);
        Delegate testDelegate = delegateList.get(delegateList.size() - 1);
        assertThat(testDelegate.getDelegateCode()).isEqualTo(DEFAULT_DELEGATE_CODE);
        assertThat(testDelegate.getDelegateName()).isEqualTo(DEFAULT_DELEGATE_NAME);
        assertThat(testDelegate.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDelegate.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void createDelegateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = delegateRepository.findAll().size();

        // Create the Delegate with an existing ID
        Delegate existingDelegate = new Delegate();
        existingDelegate.setId(1L);
        DelegateDTO existingDelegateDTO = delegateMapper.delegateToDelegateDTO(existingDelegate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDelegateMockMvc.perform(post("/api/delegates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDelegateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDelegate_codeIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegateRepository.findAll().size();
        // set the field null
        delegate.setDelegateCode(null);

        // Create the Delegate, which fails.
        DelegateDTO delegateDTO = delegateMapper.delegateToDelegateDTO(delegate);

        restDelegateMockMvc.perform(post("/api/delegates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delegateDTO)))
            .andExpect(status().isBadRequest());

        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDelegate_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegateRepository.findAll().size();
        // set the field null
        delegate.setDelegateName(null);

        // Create the Delegate, which fails.
        DelegateDTO delegateDTO = delegateMapper.delegateToDelegateDTO(delegate);

        restDelegateMockMvc.perform(post("/api/delegates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delegateDTO)))
            .andExpect(status().isBadRequest());

        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegateRepository.findAll().size();
        // set the field null
        delegate.setStatus(null);

        // Create the Delegate, which fails.
        DelegateDTO delegateDTO = delegateMapper.delegateToDelegateDTO(delegate);

        restDelegateMockMvc.perform(post("/api/delegates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delegateDTO)))
            .andExpect(status().isBadRequest());

        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDelegates() throws Exception {
        // Initialize the database
        delegateRepository.saveAndFlush(delegate);

        // Get all the delegateList
        restDelegateMockMvc.perform(get("/api/delegates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delegate.getId().intValue())))
            .andExpect(jsonPath("$.[*].delegate_code").value(hasItem(DEFAULT_DELEGATE_CODE.toString())))
            .andExpect(jsonPath("$.[*].delegate_name").value(hasItem(DEFAULT_DELEGATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getDelegate() throws Exception {
        // Initialize the database
        delegateRepository.saveAndFlush(delegate);

        // Get the delegate
        restDelegateMockMvc.perform(get("/api/delegates/{id}", delegate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(delegate.getId().intValue()))
            .andExpect(jsonPath("$.delegate_code").value(DEFAULT_DELEGATE_CODE.toString()))
            .andExpect(jsonPath("$.delegate_name").value(DEFAULT_DELEGATE_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDelegate() throws Exception {
        // Get the delegate
        restDelegateMockMvc.perform(get("/api/delegates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDelegate() throws Exception {
        // Initialize the database
        delegateRepository.saveAndFlush(delegate);
        int databaseSizeBeforeUpdate = delegateRepository.findAll().size();

        // Update the delegate
        Delegate updatedDelegate = delegateRepository.findOne(delegate.getId());
        updatedDelegate
                .delegateCode(UPDATED_DELEGATE_CODE)
                .delegateName(UPDATED_DELEGATE_NAME)
                .status(UPDATED_STATUS)
                .memo(UPDATED_MEMO);
        DelegateDTO delegateDTO = delegateMapper.delegateToDelegateDTO(updatedDelegate);

        restDelegateMockMvc.perform(put("/api/delegates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delegateDTO)))
            .andExpect(status().isOk());

        // Validate the Delegate in the database
        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeUpdate);
        Delegate testDelegate = delegateList.get(delegateList.size() - 1);
        assertThat(testDelegate.getDelegateCode()).isEqualTo(UPDATED_DELEGATE_CODE);
        assertThat(testDelegate.getDelegateName()).isEqualTo(UPDATED_DELEGATE_NAME);
        assertThat(testDelegate.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDelegate.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void updateNonExistingDelegate() throws Exception {
        int databaseSizeBeforeUpdate = delegateRepository.findAll().size();

        // Create the Delegate
        DelegateDTO delegateDTO = delegateMapper.delegateToDelegateDTO(delegate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDelegateMockMvc.perform(put("/api/delegates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(delegateDTO)))
            .andExpect(status().isCreated());

        // Validate the Delegate in the database
        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDelegate() throws Exception {
        // Initialize the database
        delegateRepository.saveAndFlush(delegate);
        int databaseSizeBeforeDelete = delegateRepository.findAll().size();

        // Get the delegate
        restDelegateMockMvc.perform(delete("/api/delegates/{id}", delegate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Delegate> delegateList = delegateRepository.findAll();
        assertThat(delegateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Delegate.class);
    }
}
