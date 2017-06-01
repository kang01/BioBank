package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutHandoverDTO;
import org.fwoxford.service.dto.response.StockOutHandoverForDataTableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing StockOutHandover.
 */
public interface StockOutHandoverService {

    /**
     * Save a stockOutHandover.
     *
     * @param stockOutHandoverDTO the entity to save
     * @return the persisted entity
     */
    StockOutHandoverDTO save(StockOutHandoverDTO stockOutHandoverDTO);

    /**
     *  Get all the stockOutHandovers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutHandoverDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutHandover.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutHandoverDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutHandover.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    StockOutHandoverDTO saveByTask(Long taskId);

    Page<StockOutHandoverForDataTableEntity> getPageStockOutHandOver(Pageable pageRequest);
}
