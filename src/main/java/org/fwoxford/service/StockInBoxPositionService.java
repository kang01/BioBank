package org.fwoxford.service;

import org.fwoxford.service.dto.StockInBoxPositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockInBoxPosition.
 */
public interface StockInBoxPositionService {

    /**
     * Save a stockInBoxPosition.
     *
     * @param stockInBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    StockInBoxPositionDTO save(StockInBoxPositionDTO stockInBoxPositionDTO);

    /**
     *  Get all the stockInBoxPositions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockInBoxPositionDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockInBoxPosition.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockInBoxPositionDTO findOne(Long id);

    /**
     *  Delete the "id" stockInBoxPosition.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
