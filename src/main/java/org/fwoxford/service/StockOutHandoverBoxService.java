package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutHandoverBoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutHandoverBox.
 */
public interface StockOutHandoverBoxService {

    /**
     * Save a stockOutHandoverBox.
     *
     * @param stockOutHandoverBoxDTO the entity to save
     * @return the persisted entity
     */
    StockOutHandoverBoxDTO save(StockOutHandoverBoxDTO stockOutHandoverBoxDTO);

    /**
     *  Get all the stockOutHandoverBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutHandoverBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutHandoverBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutHandoverBoxDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutHandoverBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
