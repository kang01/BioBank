package org.fwoxford.service.impl;

import org.fwoxford.service.FrozenTubeTypeService;
import org.fwoxford.domain.FrozenTubeType;
import org.fwoxford.repository.FrozenTubeTypeRepository;
import org.fwoxford.service.dto.FrozenTubeTypeDTO;
import org.fwoxford.service.mapper.FrozenTubeTypeMapper;
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
 * Service Implementation for managing FrozenTubeType.
 */
@Service
@Transactional
public class FrozenTubeTypeServiceImpl implements FrozenTubeTypeService{

    private final Logger log = LoggerFactory.getLogger(FrozenTubeTypeServiceImpl.class);
    
    private final FrozenTubeTypeRepository frozenTubeTypeRepository;

    private final FrozenTubeTypeMapper frozenTubeTypeMapper;

    public FrozenTubeTypeServiceImpl(FrozenTubeTypeRepository frozenTubeTypeRepository, FrozenTubeTypeMapper frozenTubeTypeMapper) {
        this.frozenTubeTypeRepository = frozenTubeTypeRepository;
        this.frozenTubeTypeMapper = frozenTubeTypeMapper;
    }

    /**
     * Save a frozenTubeType.
     *
     * @param frozenTubeTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenTubeTypeDTO save(FrozenTubeTypeDTO frozenTubeTypeDTO) {
        log.debug("Request to save FrozenTubeType : {}", frozenTubeTypeDTO);
        FrozenTubeType frozenTubeType = frozenTubeTypeMapper.frozenTubeTypeDTOToFrozenTubeType(frozenTubeTypeDTO);
        frozenTubeType = frozenTubeTypeRepository.save(frozenTubeType);
        FrozenTubeTypeDTO result = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);
        return result;
    }

    /**
     *  Get all the frozenTubeTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenTubeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenTubeTypes");
        Page<FrozenTubeType> result = frozenTubeTypeRepository.findAll(pageable);
        return result.map(frozenTubeType -> frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType));
    }

    /**
     *  Get one frozenTubeType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenTubeTypeDTO findOne(Long id) {
        log.debug("Request to get FrozenTubeType : {}", id);
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findOne(id);
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeMapper.frozenTubeTypeToFrozenTubeTypeDTO(frozenTubeType);
        return frozenTubeTypeDTO;
    }

    /**
     *  Delete the  frozenTubeType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenTubeType : {}", id);
        frozenTubeTypeRepository.delete(id);
    }
}
