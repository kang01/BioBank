package org.fwoxford.service.impl;

import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.mapper.FrozenBoxMapper;
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
 * Service Implementation for managing FrozenBox.
 */
@Service
@Transactional
public class FrozenBoxServiceImpl implements FrozenBoxService{

    private final Logger log = LoggerFactory.getLogger(FrozenBoxServiceImpl.class);
    
    private final FrozenBoxRepository frozenBoxRepository;

    private final FrozenBoxMapper frozenBoxMapper;

    public FrozenBoxServiceImpl(FrozenBoxRepository frozenBoxRepository, FrozenBoxMapper frozenBoxMapper) {
        this.frozenBoxRepository = frozenBoxRepository;
        this.frozenBoxMapper = frozenBoxMapper;
    }

    /**
     * Save a frozenBox.
     *
     * @param frozenBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenBoxDTO save(FrozenBoxDTO frozenBoxDTO) {
        log.debug("Request to save FrozenBox : {}", frozenBoxDTO);
        FrozenBox frozenBox = frozenBoxMapper.frozenBoxDTOToFrozenBox(frozenBoxDTO);
        frozenBox = frozenBoxRepository.save(frozenBox);
        FrozenBoxDTO result = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);
        return result;
    }

    /**
     *  Get all the frozenBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenBoxes");
        Page<FrozenBox> result = frozenBoxRepository.findAll(pageable);
        return result.map(frozenBox -> frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox));
    }

    /**
     *  Get one frozenBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenBoxDTO findOne(Long id) {
        log.debug("Request to get FrozenBox : {}", id);
        FrozenBox frozenBox = frozenBoxRepository.findOne(id);
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);
        return frozenBoxDTO;
    }

    /**
     *  Delete the  frozenBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenBox : {}", id);
        frozenBoxRepository.delete(id);
    }
}
