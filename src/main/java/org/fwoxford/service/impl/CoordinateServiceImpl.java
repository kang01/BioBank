package org.fwoxford.service.impl;

import org.fwoxford.service.CoordinateService;
import org.fwoxford.domain.Coordinate;
import org.fwoxford.repository.CoordinateRepository;
import org.fwoxford.service.dto.CoordinateDTO;
import org.fwoxford.service.mapper.CoordinateMapper;
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
 * Service Implementation for managing Coordinate.
 */
@Service
@Transactional
public class CoordinateServiceImpl implements CoordinateService{

    private final Logger log = LoggerFactory.getLogger(CoordinateServiceImpl.class);
    
    private final CoordinateRepository coordinateRepository;

    private final CoordinateMapper coordinateMapper;

    public CoordinateServiceImpl(CoordinateRepository coordinateRepository, CoordinateMapper coordinateMapper) {
        this.coordinateRepository = coordinateRepository;
        this.coordinateMapper = coordinateMapper;
    }

    /**
     * Save a coordinate.
     *
     * @param coordinateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CoordinateDTO save(CoordinateDTO coordinateDTO) {
        log.debug("Request to save Coordinate : {}", coordinateDTO);
        Coordinate coordinate = coordinateMapper.coordinateDTOToCoordinate(coordinateDTO);
        coordinate = coordinateRepository.save(coordinate);
        CoordinateDTO result = coordinateMapper.coordinateToCoordinateDTO(coordinate);
        return result;
    }

    /**
     *  Get all the coordinates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoordinateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Coordinates");
        Page<Coordinate> result = coordinateRepository.findAll(pageable);
        return result.map(coordinate -> coordinateMapper.coordinateToCoordinateDTO(coordinate));
    }

    /**
     *  Get one coordinate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CoordinateDTO findOne(Long id) {
        log.debug("Request to get Coordinate : {}", id);
        Coordinate coordinate = coordinateRepository.findOne(id);
        CoordinateDTO coordinateDTO = coordinateMapper.coordinateToCoordinateDTO(coordinate);
        return coordinateDTO;
    }

    /**
     *  Delete the  coordinate by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coordinate : {}", id);
        coordinateRepository.delete(id);
    }
}
