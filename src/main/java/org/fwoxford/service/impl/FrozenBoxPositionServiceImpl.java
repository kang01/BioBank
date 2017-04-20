package org.fwoxford.service.impl;

import org.fwoxford.service.FrozenBoxPositionService;
import org.fwoxford.domain.FrozenBoxPosition;
import org.fwoxford.repository.FrozenBoxPositionRepository;
import org.fwoxford.service.dto.FrozenBoxPositionDTO;
import org.fwoxford.service.mapper.FrozenBoxPositionMapper;
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
 * Service Implementation for managing FrozenBoxPosition.
 */
@Service
@Transactional
public class FrozenBoxPositionServiceImpl implements FrozenBoxPositionService{

    private final Logger log = LoggerFactory.getLogger(FrozenBoxPositionServiceImpl.class);
    
    private final FrozenBoxPositionRepository frozenBoxPositionRepository;

    private final FrozenBoxPositionMapper frozenBoxPositionMapper;

    public FrozenBoxPositionServiceImpl(FrozenBoxPositionRepository frozenBoxPositionRepository, FrozenBoxPositionMapper frozenBoxPositionMapper) {
        this.frozenBoxPositionRepository = frozenBoxPositionRepository;
        this.frozenBoxPositionMapper = frozenBoxPositionMapper;
    }

    /**
     * Save a frozenBoxPosition.
     *
     * @param frozenBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenBoxPositionDTO save(FrozenBoxPositionDTO frozenBoxPositionDTO) {
        log.debug("Request to save FrozenBoxPosition : {}", frozenBoxPositionDTO);
        FrozenBoxPosition frozenBoxPosition = frozenBoxPositionMapper.frozenBoxPositionDTOToFrozenBoxPosition(frozenBoxPositionDTO);
        frozenBoxPosition = frozenBoxPositionRepository.save(frozenBoxPosition);
        FrozenBoxPositionDTO result = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(frozenBoxPosition);
        return result;
    }

    /**
     *  Get all the frozenBoxPositions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenBoxPositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenBoxPositions");
        Page<FrozenBoxPosition> result = frozenBoxPositionRepository.findAll(pageable);
        return result.map(frozenBoxPosition -> frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(frozenBoxPosition));
    }

    /**
     *  Get one frozenBoxPosition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenBoxPositionDTO findOne(Long id) {
        log.debug("Request to get FrozenBoxPosition : {}", id);
        FrozenBoxPosition frozenBoxPosition = frozenBoxPositionRepository.findOne(id);
        FrozenBoxPositionDTO frozenBoxPositionDTO = frozenBoxPositionMapper.frozenBoxPositionToFrozenBoxPositionDTO(frozenBoxPosition);
        return frozenBoxPositionDTO;
    }

    /**
     *  Delete the  frozenBoxPosition by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenBoxPosition : {}", id);
        frozenBoxPositionRepository.delete(id);
    }
}
