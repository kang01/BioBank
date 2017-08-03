package org.fwoxford.service.impl;

import org.fwoxford.service.PositionDestroyService;
import org.fwoxford.domain.PositionDestroy;
import org.fwoxford.repository.PositionDestroyRepository;
import org.fwoxford.service.dto.PositionDestroyDTO;
import org.fwoxford.service.mapper.PositionDestroyMapper;
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
 * Service Implementation for managing PositionDestroy.
 */
@Service
@Transactional
public class PositionDestroyServiceImpl implements PositionDestroyService{

    private final Logger log = LoggerFactory.getLogger(PositionDestroyServiceImpl.class);
    
    private final PositionDestroyRepository positionDestroyRepository;

    private final PositionDestroyMapper positionDestroyMapper;

    public PositionDestroyServiceImpl(PositionDestroyRepository positionDestroyRepository, PositionDestroyMapper positionDestroyMapper) {
        this.positionDestroyRepository = positionDestroyRepository;
        this.positionDestroyMapper = positionDestroyMapper;
    }

    /**
     * Save a positionDestroy.
     *
     * @param positionDestroyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionDestroyDTO save(PositionDestroyDTO positionDestroyDTO) {
        log.debug("Request to save PositionDestroy : {}", positionDestroyDTO);
        PositionDestroy positionDestroy = positionDestroyMapper.positionDestroyDTOToPositionDestroy(positionDestroyDTO);
        positionDestroy = positionDestroyRepository.save(positionDestroy);
        PositionDestroyDTO result = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);
        return result;
    }

    /**
     *  Get all the positionDestroys.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionDestroyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionDestroys");
        Page<PositionDestroy> result = positionDestroyRepository.findAll(pageable);
        return result.map(positionDestroy -> positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy));
    }

    /**
     *  Get one positionDestroy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionDestroyDTO findOne(Long id) {
        log.debug("Request to get PositionDestroy : {}", id);
        PositionDestroy positionDestroy = positionDestroyRepository.findOne(id);
        PositionDestroyDTO positionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);
        return positionDestroyDTO;
    }

    /**
     *  Delete the  positionDestroy by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionDestroy : {}", id);
        positionDestroyRepository.delete(id);
    }
}
