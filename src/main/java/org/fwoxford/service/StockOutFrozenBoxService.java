package org.fwoxford.service;

import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.web.rest.StockOutFrozenBoxPoisition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.io.ByteArrayOutputStream;
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

    List<StockOutFrozenBoxForTaskDataTableEntity> getAllStockOutFrozenBoxesByTask(Long taskId);

    List<StockOutFrozenBoxDTO> createFrozenBoxForStockOut(List<FrozenBoxAndFrozenTubeResponse> frozenBoxDTO, Long taskId);

    List<FrozenBoxAndFrozenTubeResponse> getAllTempStockOutFrozenBoxesByTask(Long taskId);

    List<StockOutFrozenBoxDataTableEntity> getStockOutFrozenBoxesByTask(Long taskId);

    StockOutTaskDTO stockOut(StockOutFrozenBoxPoisition stockOutFrozenBoxPoisition, Long taskId, List<Long> frozenBoxIds);

    StockOutFrozenBoxDTO stockOutNote(StockOutFrozenBoxDTO stockOutFrozenBoxDTO);

    ByteArrayOutputStream printStockOutFrozenBox(Long taskId);

    Page<StockOutFrozenBoxForDataTableEntity> getPageHandoverStockOutFrozenBoxes(Long id, Pageable pageRequest);

    DataTablesOutput<StockOutFrozenBoxForDataTableEntity> getPageWaitingHandOverStockOutFrozenBoxes(Long id, DataTablesInput input);

    DataTablesOutput<FrozenBoxForStockOutDataTableEntity> getPageByRequirementIds(List<Long> ids, DataTablesInput input);

    DataTablesOutput<StockOutFrozenBoxForTaskDetailDataTableEntity> getPageByTask(Long id, DataTablesInput input);
}
