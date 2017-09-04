package org.fwoxford.service;

import org.fwoxford.service.dto.CoordinateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Coordinate.
 */
public interface CoordinateService {

    /**
     * Save a coordinate.
     *
     * @param coordinateDTO the entity to save
     * @return the persisted entity
     */
    CoordinateDTO save(CoordinateDTO coordinateDTO);

    /**
     *  Get all the coordinates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CoordinateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" coordinate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CoordinateDTO findOne(Long id);

    /**
     *  Delete the "id" coordinate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
