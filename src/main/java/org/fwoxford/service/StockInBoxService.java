package org.fwoxford.service;

import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.dto.response.StockInBoxDetail;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInBoxSplit;
import org.fwoxford.web.rest.FrozenBoxPositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing StockInBox.
 */
public interface StockInBoxService {

    /**
     * Save a stockInBox.
     *
     * @param stockInBoxDTO the entity to save
     * @return the persisted entity
     */
    StockInBoxDTO save(StockInBoxDTO stockInBoxDTO);

    /**
     *  Get all the stockInBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockInBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockInBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockInBoxDTO findOne(Long id);

    /**
     *  Delete the "id" stockInBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<StockInBoxDTO> saveBatch(List<StockInBoxDTO> stockInBoxDTOS);

    DataTablesOutput<StockInBoxForDataTable> getPageStockInBoxes(DataTablesInput input, String stockInCode);

    StockInBoxDetail getStockInBoxDetail(String stockInCode, String boxCode);

    StockInBoxSplit splitedStockIn(String stockInCode, String boxCode, StockInBoxSplit stockInBoxForDataSplit);

    StockInBoxDetail movedStockIn(String stockInCode, String boxCode, FrozenBoxPositionDTO boxPositionDTO);
}
