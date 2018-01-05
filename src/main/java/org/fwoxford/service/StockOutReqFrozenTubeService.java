package org.fwoxford.service;

import net.sf.json.JSONObject;
import org.fwoxford.domain.StockOutRequiredSample;
import org.fwoxford.domain.StockOutRequirement;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;
import org.fwoxford.service.dto.response.StockOutFrozenTubeForPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutReqFrozenTube.
 */
public interface StockOutReqFrozenTubeService {

    /**
     * Save a stockOutReqFrozenTube.
     *
     * @param stockOutReqFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    StockOutReqFrozenTubeDTO save(StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO);

    /**
     *  Get all the stockOutReqFrozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutReqFrozenTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutReqFrozenTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutReqFrozenTubeDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutReqFrozenTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    String checkStockOutSampleByAppointedSample(List<StockOutRequiredSampleDTO> stockOutRequiredSamples, StockOutRequirement stockOutRequirement);

    /**
     * 核对录入部分的申请出库的样本
     * @param stockOutRequirement
     * @return
     */
    String checkStockOutSampleByRequirement(StockOutRequirement stockOutRequirement);

    /**
     * 核对指定的需求样本
     * @param jsonArray
     * @param stockOutRequirement
     * @return
     */
    String checkStockOutSampleByAppointedSampleOrAppointedBox(List<JSONObject> jsonArray, StockOutRequirement stockOutRequirement);

    /**
     * 撤销出库计划样本
     * @param frozenTubeDTOS
     * @return
     */
    List<StockOutFrozenTubeForPlan> repealStockOutFrozenTube(List<StockOutFrozenTubeForPlan> frozenTubeDTOS);
}
