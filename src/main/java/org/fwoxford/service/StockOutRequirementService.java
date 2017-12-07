package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutRequirementDTO;
import org.fwoxford.service.dto.response.StockOutRequirementForApply;
import org.fwoxford.service.dto.response.StockOutRequirementForApplyTable;
import org.fwoxford.service.dto.response.StockOutRequirementFrozenTubeDetail;
import org.fwoxford.service.dto.response.StockOutRequirementSampleDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Service Interface for managing StockOutRequirement.
 */
public interface StockOutRequirementService {

    /**
     * Save a stockOutRequirement.
     *
     * @param stockOutRequirementDTO the entity to save
     * @return the persisted entity
     */
    StockOutRequirementDTO save(StockOutRequirementDTO stockOutRequirementDTO);

    /**
     *  Get all the stockOutRequirements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutRequirementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutRequirement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutRequirementDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutRequirement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 保存样本需求
     * @param stockOutRequirement
     * @param stockOutApplyId
     * @return
     */
    StockOutRequirementForApply saveStockOutRequirement(StockOutRequirementForApply stockOutRequirement, Long stockOutApplyId);

    /**
     * 上传并保存样本需求
     * @param stockOutRequirement
     * @param stockOutApplyId
     * @param file
     * @param request
     * @return
     */
    StockOutRequirementForApply saveAndUploadStockOutRequirement(StockOutRequirementForApply stockOutRequirement, Long stockOutApplyId, MultipartFile file, HttpServletRequest request);

    /**
     * 获取需求详情
     * @param id
     * @return
     */
    StockOutRequirementForApply getRequirementById(Long id);

    /**
     * 核对样本（单个）
     * @param id
     * @return
     */
    StockOutRequirementForApplyTable checkStockOutRequirement(Long id);

    /**
     * 获取核对详情
     * @param id
     * @return
     */
    StockOutRequirementSampleDetail getCheckDetail(Long id);

    /**
     * 复原核对结果
     * @param id
     * @return
     */
    StockOutRequirementForApplyTable revertStockOutRequirement(Long id);

    /**
     * 批量核对样本
     * @param ids
     * @return
     */
    List<StockOutRequirementForApplyTable> batchCheckStockOutRequirement(List<Long> ids);

    /**
     * 打印需求详情
     * @param id
     * @return
     */
    ByteArrayOutputStream printStockOutRequirementDetailReport(Long id);

    /**
     * 根据申请需求需求列表
     * @param id
     * @param input
     * @return
     */
    DataTablesOutput<StockOutRequirementFrozenTubeDetail> getCheckDetailByRequirement(Long id, DataTablesInput input);
}
