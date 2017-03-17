package org.fwoxford.service.impl;

import org.fwoxford.service.SampleTypeService;
import org.fwoxford.domain.SampleType;
import org.fwoxford.repository.SampleTypeRepository;
import org.fwoxford.service.dto.SampleTypeDTO;
import org.fwoxford.service.mapper.SampleTypeMapper;
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
 * Service Implementation for managing SampleType.
 */
@Service
@Transactional
public class SampleTypeServiceImpl implements SampleTypeService{

    private final Logger log = LoggerFactory.getLogger(SampleTypeServiceImpl.class);
    
    private final SampleTypeRepository sampleTypeRepository;

    private final SampleTypeMapper sampleTypeMapper;

    public SampleTypeServiceImpl(SampleTypeRepository sampleTypeRepository, SampleTypeMapper sampleTypeMapper) {
        this.sampleTypeRepository = sampleTypeRepository;
        this.sampleTypeMapper = sampleTypeMapper;
    }

    /**
     * Save a sampleType.
     *
     * @param sampleTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SampleTypeDTO save(SampleTypeDTO sampleTypeDTO) {
        log.debug("Request to save SampleType : {}", sampleTypeDTO);
        SampleType sampleType = sampleTypeMapper.sampleTypeDTOToSampleType(sampleTypeDTO);
        sampleType = sampleTypeRepository.save(sampleType);
        SampleTypeDTO result = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);
        return result;
    }

    /**
     *  Get all the sampleTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SampleTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SampleTypes");
        Page<SampleType> result = sampleTypeRepository.findAll(pageable);
        return result.map(sampleType -> sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType));
    }

    /**
     *  Get one sampleType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SampleTypeDTO findOne(Long id) {
        log.debug("Request to get SampleType : {}", id);
        SampleType sampleType = sampleTypeRepository.findOne(id);
        SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType);
        return sampleTypeDTO;
    }

    /**
     *  Delete the  sampleType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SampleType : {}", id);
        sampleTypeRepository.delete(id);
    }
}
