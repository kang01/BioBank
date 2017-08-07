package org.fwoxford.service;

import org.fwoxford.service.dto.StockInTranshipBoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockInTranshipBox.
 */
public interface StockInTranshipBoxService {

    /**
     * Save a stockInTranshipBox.
     *
     * @param stockInTranshipBoxDTO the entity to save
     * @return the persisted entity
     */
    StockInTranshipBoxDTO save(StockInTranshipBoxDTO stockInTranshipBoxDTO);

    /**
     *  Get all the stockInTranshipBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockInTranshipBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockInTranshipBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockInTranshipBoxDTO findOne(Long id);

    /**
     *  Delete the "id" stockInTranshipBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
