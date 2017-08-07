package org.fwoxford.service;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.StockInForDataDetail;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing Tranship.
 */
public interface TranshipService {

    /**
     * Save a tranship.
     *
     * @param transhipDTO the entity to save
     * @return the persisted entity
     */
    TranshipDTO save(TranshipDTO transhipDTO);

    /**
     *  Get all the tranships.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tranship.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipDTO findOne(Long id);

    /**
     *  Delete the "id" tranship.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 查询转运记录列表
     * @param input
     * @return
     */
    DataTablesOutput<TranshipResponse> findAllTranship(DataTablesInput input);

    /**
     * 根据转运记录ID查询转运记录以及冻存盒信息
     * @param id
     * @return
     */
    TranshipByIdResponse findTranshipAndFrozenBox(Long id);

    /**
     * 初始化转运记录
     * @return
     */
    TranshipDTO initTranship();

    /**
     * 作废转运记录
     * @param transhipCode
     * @return
     */
    TranshipDTO invalidTranship(String transhipCode);

    Boolean isRepeatTrackNumber(String transhipCode, String trackNumber);

    /**
     * 转运完成
     * @param transhipCode
     * @param transhipToStockInDTO
     * @return
     */
    StockInForDataDetail completedTranship(String transhipCode, TranshipToStockInDTO transhipToStockInDTO);
}
