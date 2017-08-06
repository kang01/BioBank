package org.fwoxford.service;

import org.fwoxford.service.dto.PositionChangeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing PositionChange.
 */
public interface PositionChangeService {

    /**
     * Save a positionChange.
     *
     * @param positionChangeDTO the entity to save
     * @return the persisted entity
     */
    PositionChangeDTO save(PositionChangeDTO positionChangeDTO);

    /**
     *  Get all the positionChanges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PositionChangeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" positionChange.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionChangeDTO findOne(Long id);

    /**
     *  Delete the "id" positionChange.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    PositionChangeDTO createChangePosition(PositionChangeDTO positionChangeDTO, String moveType);
}
