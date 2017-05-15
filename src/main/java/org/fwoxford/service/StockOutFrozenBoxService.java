package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutFrozenBox.
 */
public interface StockOutFrozenBoxService {

    /**
     * Save a stockOutFrozenBox.
     *
     * @param stockOutFrozenBoxDTO the entity to save
     * @return the persisted entity
     */
    StockOutFrozenBoxDTO save(StockOutFrozenBoxDTO stockOutFrozenBoxDTO);

    /**
     *  Get all the stockOutFrozenBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutFrozenBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutFrozenBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutFrozenBoxDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutFrozenBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
