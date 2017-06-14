package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutHandoverDTO;
import org.fwoxford.service.dto.response.StockOutHandoverForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutHandoverSampleReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.io.ByteArrayOutputStream;
import java.util.List;

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

    ByteArrayOutputStream printStockOutHandover(Long id);

    StockOutHandoverDTO completeStockOutHandover(List<Long> ids, StockOutHandoverDTO stockOutHandoverDTO);

    StockOutHandoverDTO getStockOutHandoverDetail(Long id);

    Page<StockOutHandoverSampleReportDTO> getStockOutHandoverSamples(Long id, Pageable pageable);

    /**
     * 作废交接
     * @param id
     * @param stockOutHandoverDTO
     * @return
     */
    StockOutHandoverDTO invalidStockOutHandover(Long id, StockOutHandoverDTO stockOutHandoverDTO);

    DataTablesOutput<StockOutHandoverForDataTableEntity> getPageDataStockOutHandOver(DataTablesInput input);

    DataTablesOutput<StockOutHandoverSampleReportDTO> getPageStockOutHandoverSample(Long id, DataTablesInput input);
}
