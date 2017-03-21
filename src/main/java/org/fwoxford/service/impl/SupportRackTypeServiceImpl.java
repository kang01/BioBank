package org.fwoxford.service.impl;

import org.fwoxford.service.SupportRackTypeService;
import org.fwoxford.domain.SupportRackType;
import org.fwoxford.repository.SupportRackTypeRepository;
import org.fwoxford.service.dto.SupportRackTypeDTO;
import org.fwoxford.service.mapper.SupportRackTypeMapper;
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
 * Service Implementation for managing SupportRackType.
 */
@Service
@Transactional
public class SupportRackTypeServiceImpl implements SupportRackTypeService{

    private final Logger log = LoggerFactory.getLogger(SupportRackTypeServiceImpl.class);
    
    private final SupportRackTypeRepository supportRackTypeRepository;

    private final SupportRackTypeMapper supportRackTypeMapper;

    public SupportRackTypeServiceImpl(SupportRackTypeRepository supportRackTypeRepository, SupportRackTypeMapper supportRackTypeMapper) {
        this.supportRackTypeRepository = supportRackTypeRepository;
        this.supportRackTypeMapper = supportRackTypeMapper;
    }

    /**
     * Save a supportRackType.
     *
     * @param supportRackTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SupportRackTypeDTO save(SupportRackTypeDTO supportRackTypeDTO) {
        log.debug("Request to save SupportRackType : {}", supportRackTypeDTO);
        SupportRackType supportRackType = supportRackTypeMapper.supportRackTypeDTOToSupportRackType(supportRackTypeDTO);
        supportRackType = supportRackTypeRepository.save(supportRackType);
        SupportRackTypeDTO result = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);
        return result;
    }

    /**
     *  Get all the supportRackTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SupportRackTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SupportRackTypes");
        Page<SupportRackType> result = supportRackTypeRepository.findAll(pageable);
        return result.map(supportRackType -> supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType));
    }

    /**
     *  Get one supportRackType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SupportRackTypeDTO findOne(Long id) {
        log.debug("Request to get SupportRackType : {}", id);
        SupportRackType supportRackType = supportRackTypeRepository.findOne(id);
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeMapper.supportRackTypeToSupportRackTypeDTO(supportRackType);
        return supportRackTypeDTO;
    }

    /**
     *  Delete the  supportRackType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SupportRackType : {}", id);
        supportRackTypeRepository.delete(id);
    }
}
