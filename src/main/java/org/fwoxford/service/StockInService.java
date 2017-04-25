package org.fwoxford.service;

import org.fwoxford.service.dto.StockInDTO;
import org.fwoxford.service.dto.StockInForDataDetail;
import org.fwoxford.service.dto.response.StockInForDataTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.time.LocalDate;

/**
 * Service Interface for managing StockIn.
 */
public interface StockInService {

    /**
     * Save a stockIn.
     *
     * @param stockInDTO the entity to save
     * @return the persisted entity
     */
    StockInDTO save(StockInDTO stockInDTO);

    /**
     *  Get all the stockIns.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockInDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockIn.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockInDTO findOne(Long id);

    /**
     *  Delete the "id" stockIn.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 入库保存
     * @param transhipCode
     * @return
     */
    StockInForDataDetail  saveStockIns(String transhipCode,String receiver,LocalDate receiveDate);

    /**
     * 查询入库单信息
     * @param input
     * @return
     */
    DataTablesOutput<StockInForDataTable> findStockIn(DataTablesInput input);

    /**
     * 入库完成
     * @param stockInCode
     * @return
     */
    StockInForDataDetail completedStockIn(String stockInCode,String loginName1,String loginName2,LocalDate stockInDate);

    StockInForDataDetail getStockInById(Long id);

    StockInForDataDetail getStockInByTranshipCode(String transhipCode);
}
