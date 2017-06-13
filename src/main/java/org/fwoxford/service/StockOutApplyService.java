package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutApplyDTO;
import org.fwoxford.service.dto.response.StockOutApplyDetail;
import org.fwoxford.service.dto.response.StockOutApplyForApprove;
import org.fwoxford.service.dto.response.StockOutApplyForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutApplyForSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.io.ByteArrayOutputStream;
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

    StockOutApplyForSave initStockOutApply();

    StockOutApplyForSave saveStockOutApply(StockOutApplyForSave stockOutApplyForSave);

    List<StockOutApplyForDataTableEntity> getNextStockOutApplyList(Long id);

    StockOutApplyDetail getStockOutDetailAndRequirement(Long id);

    StockOutApplyDTO additionalApply(Long parentApplyId);

    StockOutApplyDTO approveStockOutApply(Long id, StockOutApplyForApprove stockOutApplyForApprove);

    ByteArrayOutputStream printStockOutApply(Long id);

    void revertStockOutRequirementCheck(Long id);

    StockOutApplyDetail getStockOutDetailAndRequirementByPlanId(Long id);

    /**
     * 获取全部未交接的申请
     * @return
     */
    List<StockOutApplyDTO> getAllStockOutApplies();

    /**
     * 作废计划
     * @param id
     * @param stockOutApplyDTO
     * @return
     */
    StockOutApplyDTO invalidStockOutDetail(Long id, StockOutApplyDTO stockOutApplyDTO);
}
