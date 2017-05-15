package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutBoxPositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutBoxPosition.
 */
public interface StockOutBoxPositionService {

    /**
     * Save a stockOutBoxPosition.
     *
     * @param stockOutBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    StockOutBoxPositionDTO save(StockOutBoxPositionDTO stockOutBoxPositionDTO);

    /**
     *  Get all the stockOutBoxPositions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutBoxPositionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutBoxPosition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutBoxPositionDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutBoxPosition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
