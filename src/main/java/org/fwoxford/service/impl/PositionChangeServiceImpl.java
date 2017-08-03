package org.fwoxford.service.impl;

import org.fwoxford.service.PositionChangeService;
import org.fwoxford.domain.PositionChange;
import org.fwoxford.repository.PositionChangeRepository;
import org.fwoxford.service.dto.PositionChangeDTO;
import org.fwoxford.service.mapper.PositionChangeMapper;
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
 * Service Implementation for managing PositionChange.
 */
@Service
@Transactional
public class PositionChangeServiceImpl implements PositionChangeService{

    private final Logger log = LoggerFactory.getLogger(PositionChangeServiceImpl.class);
    
    private final PositionChangeRepository positionChangeRepository;

    private final PositionChangeMapper positionChangeMapper;

    public PositionChangeServiceImpl(PositionChangeRepository positionChangeRepository, PositionChangeMapper positionChangeMapper) {
        this.positionChangeRepository = positionChangeRepository;
        this.positionChangeMapper = positionChangeMapper;
    }

    /**
     * Save a positionChange.
     *
     * @param positionChangeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionChangeDTO save(PositionChangeDTO positionChangeDTO) {
        log.debug("Request to save PositionChange : {}", positionChangeDTO);
        PositionChange positionChange = positionChangeMapper.positionChangeDTOToPositionChange(positionChangeDTO);
        positionChange = positionChangeRepository.save(positionChange);
        PositionChangeDTO result = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);
        return result;
    }

    /**
     *  Get all the positionChanges.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionChangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionChanges");
        Page<PositionChange> result = positionChangeRepository.findAll(pageable);
        return result.map(positionChange -> positionChangeMapper.positionChangeToPositionChangeDTO(positionChange));
    }

    /**
     *  Get one positionChange by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionChangeDTO findOne(Long id) {
        log.debug("Request to get PositionChange : {}", id);
        PositionChange positionChange = positionChangeRepository.findOne(id);
        PositionChangeDTO positionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);
        return positionChangeDTO;
    }

    /**
     *  Delete the  positionChange by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionChange : {}", id);
        positionChangeRepository.delete(id);
    }
}
