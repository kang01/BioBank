package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.fwoxford.domain.StockOutApply;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutRequirementService;
import org.fwoxford.service.dto.response.StockOutApplyDetail;
import org.fwoxford.service.dto.response.StockOutRequirementForApply;
import org.fwoxford.service.dto.response.StockOutRequirementForSave;
import org.fwoxford.service.dto.response.StockOutRequirementSampleDetail;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutRequirementDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockOutRequirement.
 */
@RestController
@RequestMapping("/api")
public class StockOutRequirementResource {

    private final Logger log = LoggerFactory.getLogger(StockOutRequirementResource.class);

    private static final String ENTITY_NAME = "stockOutRequirement";

    private final StockOutRequirementService stockOutRequirementService;

    public StockOutRequirementResource(StockOutRequirementService stockOutRequirementService) {
        this.stockOutRequirementService = stockOutRequirementService;
    }

    /**
     * POST  /stock-out-requirements : Create a new stockOutRequirement.
     *
     * @param stockOutRequirementDTO the stockOutRequirementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutRequirementDTO, or with status 400 (Bad Request) if the stockOutRequirement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-requirements")
    @Timed
    public ResponseEntity<StockOutRequirementDTO> createStockOutRequirement(@Valid @RequestBody StockOutRequirementDTO stockOutRequirementDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutRequirement : {}", stockOutRequirementDTO);
        if (stockOutRequirementDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutRequirement cannot already have an ID")).body(null);
        }
        StockOutRequirementDTO result = stockOutRequirementService.save(stockOutRequirementDTO);
        return ResponseEntity.created(new URI("/api/stock-out-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-requirements : Updates an existing stockOutRequirement.
     *
     * @param stockOutRequirementDTO the stockOutRequirementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutRequirementDTO,
     * or with status 400 (Bad Request) if the stockOutRequirementDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutRequirementDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-requirements")
    @Timed
    public ResponseEntity<StockOutRequirementDTO> updateStockOutRequirement(@Valid @RequestBody StockOutRequirementDTO stockOutRequirementDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutRequirement : {}", stockOutRequirementDTO);
        if (stockOutRequirementDTO.getId() == null) {
            return createStockOutRequirement(stockOutRequirementDTO);
        }
        StockOutRequirementDTO result = stockOutRequirementService.save(stockOutRequirementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutRequirementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-requirements : get all the stockOutRequirements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutRequirements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-requirements")
    @Timed
    public ResponseEntity<List<StockOutRequirementDTO>> getAllStockOutRequirements(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutRequirements");
        Page<StockOutRequirementDTO> page = stockOutRequirementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

//    /**
//     * GET  /stock-out-requirements/:id : get the "id" stockOutRequirement.
//     *
//     * @param id the id of the stockOutRequirementDTO to retrieve
//     * @return the ResponseEntity with status 200 (OK) and with body the stockOutRequirementDTO, or with status 404 (Not Found)
//     */
//    @GetMapping("/stock-out-requirements/{id}")
//    @Timed
//    public ResponseEntity<StockOutRequirementDTO> getStockOutRequirement(@PathVariable Long id) {
//        log.debug("REST request to get StockOutRequirement : {}", id);
//        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutRequirementDTO));
//    }

    /**
     * DELETE  /stock-out-requirements/:id : delete the "id" stockOutRequirement.
     *
     * @param id the id of the stockOutRequirementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-requirements/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutRequirement(@PathVariable Long id) {
        log.debug("REST request to delete StockOutRequirement : {}", id);
        stockOutRequirementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 保存出库申请需求
     *
     * @param stockOutApplyId
     * @param stockOutRequirement
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-requirements/stockOutApply/{stockOutApplyId}",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<StockOutRequirementForSave> saveStockOutRequirement(@PathVariable Long stockOutApplyId,
                                                                              @RequestBody StockOutRequirementForSave stockOutRequirement) throws URISyntaxException {
        log.debug("REST request to save StockOutRequirement : {}", stockOutRequirement);
        if (stockOutRequirement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutRequirement cannot already have an ID")).body(null);
        }
        StockOutRequirementForSave result = stockOutRequirementService.saveStockOutRequirement(stockOutRequirement, stockOutApplyId);

        return ResponseEntity.created(new URI("/api/stock-out-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 导入样本
     * @param stockOutApplyId
     * @param stockOutRequirement
     * @param file
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-requirements/stockOutApply/{stockOutApplyId}/upload",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<StockOutRequirementForApply> saveAndUploadStockOutRequirement(@PathVariable Long stockOutApplyId,
                                                                              @RequestParam(value = "stockOutRequirement") String stockOutRequirement,
                                                                              @RequestParam(value = "file",required = false) MultipartFile file) throws URISyntaxException {
        JSONObject jsonObject = JSONObject.fromObject(stockOutRequirement);
        StockOutRequirementForSave requirement = (StockOutRequirementForSave) JSONObject.toBean(jsonObject, StockOutRequirementForSave.class);
        log.debug("REST request to save StockOutRequirement : {}", requirement);
        if (requirement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutRequirement cannot already have an ID")).body(null);
        }
        StockOutRequirementForApply result = stockOutRequirementService.saveAndUploadStockOutRequirement(requirement, stockOutApplyId,file);

        return ResponseEntity.created(new URI("/api/stock-out-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * 根据需求ID获取需求详情
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-requirements/{id}")
    @Timed
    public ResponseEntity<StockOutRequirementForApply> getRequirement(@PathVariable Long id) throws URISyntaxException {
        StockOutRequirementForApply result = stockOutRequirementService.getRequirementById(id);
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 保存出库申请需求
     *
     * @param stockOutApplyId
     * @param stockOutRequirement
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-requirements/stockOutApply/{stockOutApplyId}",method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<StockOutRequirementForSave> updateStockOutRequirement(@PathVariable Long stockOutApplyId,
                                                                                @RequestBody StockOutRequirementForSave stockOutRequirement) throws URISyntaxException {
        if (stockOutRequirement.getId() == null) {
            throw new BankServiceException("出库需求ID不能为空！");
        }
        StockOutRequirementForSave result = stockOutRequirementService.saveStockOutRequirement(stockOutRequirement, stockOutApplyId);

        return ResponseEntity.created(new URI("/api/stock-out-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 核对样本
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-requirements/{id}/check",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<StockOutRequirementForApply> checkStockOutRequirement(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to check StockOutRequirement : {}", id);
        StockOutRequirementForApply result = stockOutRequirementService.checkStockOutRequirement(id);
        return ResponseEntity.created(new URI("/api/stock-out-requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 根据样本需求查询需求核对详情
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-requirements/getCheckDetail/{id}")
    @Timed
    public ResponseEntity<StockOutRequirementSampleDetail> getCheckDetail(@PathVariable Long id) throws URISyntaxException {
        StockOutRequirementSampleDetail result = stockOutRequirementService.getCheckDetail(id);
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @PutMapping("/stock-out-requirements/revert/{id}")
    @Timed
    public ResponseEntity<StockOutRequirementForApply> revertStockOutRequirement(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to revert StockOutRequirement : {}", id);
        StockOutRequirementForApply result = stockOutRequirementService.revertStockOutRequirement(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * 批量核对样本
     * @param ids
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-requirements/check/{ids}",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> batchCheckStockOutRequirement(@PathVariable List<Long> ids) throws URISyntaxException {
        log.debug("REST request to check StockOutRequirement : {}", ids);
        stockOutRequirementService.batchCheckStockOutRequirement(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, ids.toString())).build();
    }

}
