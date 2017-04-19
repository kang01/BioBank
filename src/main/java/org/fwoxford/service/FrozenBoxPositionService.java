package org.fwoxford.service;

import org.fwoxford.service.dto.FrozenBoxPositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing FrozenBoxPosition.
 */
public interface FrozenBoxPositionService {

    /**
     * Save a frozenBoxPosition.
     *
     * @param frozenBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    FrozenBoxPositionDTO save(FrozenBoxPositionDTO frozenBoxPositionDTO);

    /**
     *  Get all the frozenBoxPositions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenBoxPositionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenBoxPosition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenBoxPositionDTO findOne(Long id);

    /**
     *  Delete the "id" frozenBoxPosition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
