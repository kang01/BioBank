package org.fwoxford.service.impl;

import org.fwoxford.service.BoxAndTubeService;
import org.fwoxford.domain.BoxAndTube;
import org.fwoxford.repository.BoxAndTubeRepository;
import org.fwoxford.service.dto.BoxAndTubeDTO;
import org.fwoxford.service.mapper.BoxAndTubeMapper;
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
 * Service Implementation for managing BoxAndTube.
 */
@Service
@Transactional
public class BoxAndTubeServiceImpl implements BoxAndTubeService{

    private final Logger log = LoggerFactory.getLogger(BoxAndTubeServiceImpl.class);
    
    private final BoxAndTubeRepository boxAndTubeRepository;

    private final BoxAndTubeMapper boxAndTubeMapper;

    public BoxAndTubeServiceImpl(BoxAndTubeRepository boxAndTubeRepository, BoxAndTubeMapper boxAndTubeMapper) {
        this.boxAndTubeRepository = boxAndTubeRepository;
        this.boxAndTubeMapper = boxAndTubeMapper;
    }

    /**
     * Save a boxAndTube.
     *
     * @param boxAndTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BoxAndTubeDTO save(BoxAndTubeDTO boxAndTubeDTO) {
        log.debug("Request to save BoxAndTube : {}", boxAndTubeDTO);
        BoxAndTube boxAndTube = boxAndTubeMapper.boxAndTubeDTOToBoxAndTube(boxAndTubeDTO);
        boxAndTube = boxAndTubeRepository.save(boxAndTube);
        BoxAndTubeDTO result = boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(boxAndTube);
        return result;
    }

    /**
     *  Get all the boxAndTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BoxAndTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BoxAndTubes");
        Page<BoxAndTube> result = boxAndTubeRepository.findAll(pageable);
        return result.map(boxAndTube -> boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(boxAndTube));
    }

    /**
     *  Get one boxAndTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BoxAndTubeDTO findOne(Long id) {
        log.debug("Request to get BoxAndTube : {}", id);
        BoxAndTube boxAndTube = boxAndTubeRepository.findOne(id);
        BoxAndTubeDTO boxAndTubeDTO = boxAndTubeMapper.boxAndTubeToBoxAndTubeDTO(boxAndTube);
        return boxAndTubeDTO;
    }

    /**
     *  Delete the  boxAndTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BoxAndTube : {}", id);
        boxAndTubeRepository.delete(id);
    }
}
