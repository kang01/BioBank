package org.fwoxford.service;

import org.fwoxford.domain.TranshipBox;
import org.fwoxford.domain.TranshipBoxPosition;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.TranshipBoxPositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing TranshipBoxPosition.
 */
public interface TranshipBoxPositionService {

    /**
     * Save a transhipBoxPosition.
     *
     * @param transhipBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    TranshipBoxPositionDTO save(TranshipBoxPositionDTO transhipBoxPositionDTO);

    /**
     *  Get all the transhipBoxPositions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipBoxPositionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transhipBoxPosition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipBoxPositionDTO findOne(Long id);

    /**
     *  Delete the "id" transhipBoxPosition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    TranshipBoxPosition saveTranshipBoxPosition(TranshipBox transhipBox);
}
