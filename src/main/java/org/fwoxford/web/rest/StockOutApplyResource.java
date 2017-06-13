package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.service.StockOutApplyService;
import org.fwoxford.service.dto.StockOutApplyDTO;
import org.fwoxford.service.dto.response.StockOutApplyDetail;
import org.fwoxford.service.dto.response.StockOutApplyForApprove;
import org.fwoxford.service.dto.response.StockOutApplyForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutApplyForSave;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StockOutApply.
 */
@RestController
@RequestMapping("/api")
public class StockOutApplyResource {

    private final Logger log = LoggerFactory.getLogger(StockOutApplyResource.class);

    private static final String ENTITY_NAME = "stockOutApply";

    private final StockOutApplyService stockOutApplyService;

    public StockOutApplyResource(StockOutApplyService stockOutApplyService) {
        this.stockOutApplyService = stockOutApplyService;
    }

    /**
     * POST  /stock-out-applies : Create a new stockOutApply.
     *
     * @param stockOutApplyDTO the stockOutApplyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutApplyDTO, or with status 400 (Bad Request) if the stockOutApply has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-applies")
    @Timed
    public ResponseEntity<StockOutApplyDTO> createStockOutApply(@Valid @RequestBody StockOutApplyDTO stockOutApplyDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutApply : {}", stockOutApplyDTO);
        if (stockOutApplyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutApply cannot already have an ID")).body(null);
        }
        StockOutApplyDTO result = stockOutApplyService.save(stockOutApplyDTO);
        return ResponseEntity.created(new URI("/api/stock-out-applies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-applies : Updates an existing stockOutApply.
     *
     * @param stockOutApplyDTO the stockOutApplyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutApplyDTO,
     * or with status 400 (Bad Request) if the stockOutApplyDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutApplyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-applies")
    @Timed
    public ResponseEntity<StockOutApplyDTO> updateStockOutApply(@Valid @RequestBody StockOutApplyDTO stockOutApplyDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutApply : {}", stockOutApplyDTO);
        if (stockOutApplyDTO.getId() == null) {
            return createStockOutApply(stockOutApplyDTO);
        }
        StockOutApplyDTO result = stockOutApplyService.save(stockOutApplyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutApplyDTO.getId().toString()))
            .body(result);
    }

    /**
     * 获取所有的未交接的申请
     * GET  /stock-out-applies : get all the stockOutApplies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutApplies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-applies")
    @Timed
    public ResponseEntity<List<StockOutApplyDTO>> getAllStockOutApplies()
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutApplies");
        List<StockOutApplyDTO> stockOutApplyDTOS = stockOutApplyService.getAllStockOutApplies();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutApplyDTOS));
    }

    /**
     * DELETE  /stock-out-applies/:id : delete the "id" stockOutApply.
     *
     * @param id the id of the stockOutApplyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-applies/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutApply(@PathVariable Long id) {
        log.debug("REST request to delete StockOutApply : {}", id);
        stockOutApplyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * 查询出库申请列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-applies", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutApplyForDataTableEntity> getPageStockOutApply(@RequestBody DataTablesInput input) {
        return stockOutApplyService.findStockOutApply(input);
    }

    /**
     * 添加出库申请
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-applies/new-empty")
    @Timed
    public ResponseEntity<StockOutApplyForSave> initStockOutApply() throws URISyntaxException {
        log.debug("REST request to create StockOutApply first");
        StockOutApplyForSave result = stockOutApplyService.initStockOutApply();
        return ResponseEntity.created(new URI("/api/stock-out-applies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 修改保存出库申请单
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-applies/update-object")
    @Timed
    public ResponseEntity<StockOutApplyForSave> saveStockOutApply(@Valid @RequestBody StockOutApplyForSave stockOutApplyForSave) throws URISyntaxException {
        log.debug("REST request to update StockOutApply : {}", stockOutApplyForSave);
        StockOutApplyForSave result = stockOutApplyService.saveStockOutApply(stockOutApplyForSave);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutApplyForSave.getId().toString()))
            .body(result);
    }
    /**
     * 根据上一级申请ID，取下一级出库申请列表
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-applies/parentApply/{id}")
    @Timed
    public ResponseEntity<List<StockOutApplyForDataTableEntity>> getNextStockOutApplyList(@PathVariable Long id) throws URISyntaxException {
        List<StockOutApplyForDataTableEntity> result =  stockOutApplyService.getNextStockOutApplyList(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 出库申请，查看详情接口
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-applies/{id}")
    @Timed
    public ResponseEntity<StockOutApplyDetail> get(@PathVariable Long id) throws URISyntaxException {
        StockOutApplyDetail result = stockOutApplyService.getStockOutDetailAndRequirement(id);
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    /**
     * 附加出库申请
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-applies/additionalApply/{parentApplyId}")
    @Timed
    public ResponseEntity<StockOutApplyDTO> additionalApply(@PathVariable Long parentApplyId) throws URISyntaxException {
        log.debug("REST request to create StockOutApply by parentApplyId");
        StockOutApplyDTO result = stockOutApplyService.additionalApply(parentApplyId);
        return ResponseEntity.created(new URI("/api/stock-out-applies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 出库批准
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-applies/approve/{id}")
    @Timed
    public ResponseEntity<StockOutApplyDTO> approveStockOutApply(@PathVariable Long id, @RequestBody StockOutApplyForApprove stockOutApplyForApprove) throws URISyntaxException {
        log.debug("REST request to approve StockOutApply : {}", id);
        StockOutApplyDTO result = stockOutApplyService.approveStockOutApply(id ,stockOutApplyForApprove);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * 打印出库申请
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-applies/print/{id}",method = RequestMethod.GET,
        produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Timed
    public ResponseEntity printStockOutApply(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to create StockOutApply by parentApplyId");

        try {
                ByteArrayOutputStream result = stockOutApplyService.printStockOutApply(id);
                byte[] fileInByte = result.toByteArray();
                final HttpHeaders headers = new HttpHeaders();
                String fileReportName = "出库申请.xlsx";
                headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.set("Content-disposition", "attachment; filename="+URLEncoder.encode(fileReportName, "UTF-8"));

//            File dir = new File(".");
//            OutputStream ofs = null;
//            ofs = new FileOutputStream(dir.getCanonicalPath() + "/" + result.hashCode() + ".xlsx");
//            result.writeTo(ofs);
//
//            ofs.close();

                return new ResponseEntity(fileInByte, headers, HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 复原核对
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-applies/revert/{id}")
    @Timed
    public ResponseEntity<Void> revertStockOutRequirement(@PathVariable Long id) {
        log.debug("REST request to revert StockOutRequirementCheck : {}", id);
        stockOutApplyService.revertStockOutRequirementCheck(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据计划ID查看出库申请
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-applies/plan/{id}")
    @Timed
    public ResponseEntity<StockOutApplyDetail> getApplyByPlanId(@PathVariable Long id) throws URISyntaxException {
        StockOutApplyDetail result = stockOutApplyService.getStockOutDetailAndRequirementByPlanId(id);
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 作废申请
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-applies/{id}/invalid")
    @Timed
    public ResponseEntity<StockOutApplyDTO> invalidStockOutDetail(@PathVariable Long id,@RequestBody StockOutApplyDTO stockOutApplyDTO ) throws URISyntaxException {

        StockOutApplyDTO result = stockOutApplyService.invalidStockOutDetail(id,stockOutApplyDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
