package org.fwoxford.service.impl;

import org.fwoxford.service.SampleClassificationService;
import org.fwoxford.domain.SampleClassification;
import org.fwoxford.repository.SampleClassificationRepository;
import org.fwoxford.service.dto.SampleClassificationDTO;
import org.fwoxford.service.mapper.SampleClassificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SampleClassification.
 */
@Service
@Transactional
public class SampleClassificationServiceImpl implements SampleClassificationService{

    private final Logger log = LoggerFactory.getLogger(SampleClassificationServiceImpl.class);
    
    private final SampleClassificationRepository sampleClassificationRepository;

    private final SampleClassificationMapper sampleClassificationMapper;

    public SampleClassificationServiceImpl(SampleClassificationRepository sampleClassificationRepository, SampleClassificationMapper sampleClassificationMapper) {
        this.sampleClassificationRepository = sampleClassificationRepository;
        this.sampleClassificationMapper = sampleClassificationMapper;
    }

    /**
     * Save a sampleClassification.
     *
     * @param sampleClassificationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SampleClassificationDTO save(SampleClassificationDTO sampleClassificationDTO) {
        log.debug("Request to save SampleClassification : {}", sampleClassificationDTO);
        SampleClassification sampleClassification = sampleClassificationMapper.sampleClassificationDTOToSampleClassification(sampleClassificationDTO);
        sampleClassification = sampleClassificationRepository.save(sampleClassification);
        SampleClassificationDTO result = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);
        return result;
    }

    /**
     *  Get all the sampleClassifications.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SampleClassificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SampleClassifications");
        Page<SampleClassification> result = sampleClassificationRepository.findAll(pageable);
        return result.map(sampleClassification -> sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification));
    }

    /**
     *  Get one sampleClassification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SampleClassificationDTO findOne(Long id) {
        log.debug("Request to get SampleClassification : {}", id);
        SampleClassification sampleClassification = sampleClassificationRepository.findOne(id);
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(sampleClassification);
        return sampleClassificationDTO;
    }

    /**
     *  Delete the  sampleClassification by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SampleClassification : {}", id);
        sampleClassificationRepository.delete(id);
    }
}
