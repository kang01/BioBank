package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutRequirementDTO;
import org.fwoxford.service.dto.response.StockOutRequirementForApply;
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

    StockOutRequirementForApply saveStockOutRequirement(StockOutRequirementForApply stockOutRequirement, Long stockOutApplyId);

    StockOutRequirementForApply saveAndUploadStockOutRequirement(StockOutRequirementForApply stockOutRequirement, Long stockOutApplyId, MultipartFile file, HttpServletRequest request);

    StockOutRequirementForApply getRequirementById(Long id);

    StockOutRequirementForApply checkStockOutRequirement(Long id);

    StockOutRequirementSampleDetail getCheckDetail(Long id);

    StockOutRequirementForApply revertStockOutRequirement(Long id);

    void batchCheckStockOutRequirement(List<Long> ids);

    ByteArrayOutputStream printStockOutRequirementDetailReport(Long id);

    DataTablesOutput<StockOutRequirementFrozenTubeDetail> getCheckDetailByRequirement(Long id, DataTablesInput input);
}
