package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutPlanDTO;
import org.fwoxford.service.dto.response.StockOutPlansForDataTableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing StockOutPlan.
 */
public interface StockOutPlanService {

    /**
     * Save a stockOutPlan.
     *
     * @param stockOutPlanDTO the entity to save
     * @return the persisted entity
     */
    StockOutPlanDTO save(StockOutPlanDTO stockOutPlanDTO);

    StockOutPlanDTO save(Long applyId);

    /**
     *  Get all the stockOutPlans.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutPlanDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutPlan.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutPlanDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutPlan.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 查询出库计划列表
     * @param input
     * @return
     */
    DataTablesOutput<StockOutPlansForDataTableEntity> findAllStockOutPlan(DataTablesInput input);

    /**
     * 根据申请ID查询出库计划
     * @param id
     * @return
     */
    List<StockOutPlanDTO> getAllStockOutPlansByApplyId(Long id);
}
