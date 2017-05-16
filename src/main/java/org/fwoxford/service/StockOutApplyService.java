package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutApplyDTO;
import org.fwoxford.service.dto.response.StockOutApplyForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutApplyForSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing StockOutApply.
 */
public interface StockOutApplyService {

    /**
     * Save a stockOutApply.
     *
     * @param stockOutApplyDTO the entity to save
     * @return the persisted entity
     */
    StockOutApplyDTO save(StockOutApplyDTO stockOutApplyDTO);

    /**
     *  Get all the stockOutApplies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutApplyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutApply.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutApplyDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutApply.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    DataTablesOutput<StockOutApplyForDataTableEntity> findStockOutApply(DataTablesInput input);

    StockOutApplyDTO initStockOutApply();

    StockOutApplyForSave saveStockOutApply(StockOutApplyForSave stockOutApplyForSave);
}
