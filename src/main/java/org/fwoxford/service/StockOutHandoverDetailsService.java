package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutHandoverDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutHandoverDetails.
 */
public interface StockOutHandoverDetailsService {

    /**
     * Save a stockOutHandoverDetails.
     *
     * @param stockOutHandoverDetailsDTO the entity to save
     * @return the persisted entity
     */
    StockOutHandoverDetailsDTO save(StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO);

    /**
     *  Get all the stockOutHandoverDetails.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutHandoverDetailsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutHandoverDetails.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutHandoverDetailsDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutHandoverDetails.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
