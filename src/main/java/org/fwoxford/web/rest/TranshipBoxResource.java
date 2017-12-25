package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.service.FrozenBoxImportService;
import org.fwoxford.service.TranshipBoxService;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TranshipBox.
 */
@RestController
@RequestMapping("/api")
public class TranshipBoxResource {

    private final Logger log = LoggerFactory.getLogger(TranshipBoxResource.class);

    private static final String ENTITY_NAME = "transhipBox";

    private final TranshipBoxService transhipBoxService;

    @Autowired
    private FrozenBoxImportService frozenBoxImportService;

    public TranshipBoxResource(TranshipBoxService transhipBoxService) {
        this.transhipBoxService = transhipBoxService;
    }

    /**
     * POST  /tranship-boxes : Create a new transhipBox.
     *
     * @param transhipBoxDTO the transhipBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipBoxDTO, or with status 400 (Bad Request) if the transhipBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranship-boxes")
    @Timed
    public ResponseEntity<TranshipBoxDTO> createTranshipBox(@Valid @RequestBody TranshipBoxDTO transhipBoxDTO) throws URISyntaxException {
        log.debug("REST request to save TranshipBox : {}", transhipBoxDTO);
        if (transhipBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transhipBox cannot already have an ID")).body(null);
        }
        TranshipBoxDTO result = transhipBoxService.save(transhipBoxDTO);
        return ResponseEntity.created(new URI("/api/tranship-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranship-boxes : Updates an existing transhipBox.
     *
     * @param transhipBoxListDTO the transhipBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipBoxDTO,
     * or with status 400 (Bad Request) if the transhipBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranship-boxes/batch")
    @Timed
    public ResponseEntity<TranshipBoxListForSaveBatchDTO> updateTranshipBox(@Valid @RequestBody TranshipBoxListDTO transhipBoxListDTO) throws URISyntaxException {
        log.debug("REST request to update TranshipBox : {}", transhipBoxListDTO);
        if (transhipBoxListDTO.getTranshipId() == null) {
            throw new BankServiceException("转运ID不能为空！");
        }
        TranshipBoxListForSaveBatchDTO result = transhipBoxService.saveBatchTranshipBox(transhipBoxListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipBoxListDTO.getTranshipId().toString()))
            .body(result);
    }

    /**
     * GET  /tranship-boxes : get all the transhipBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transhipBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranship-boxes")
    @Timed
    public ResponseEntity<List<TranshipBoxDTO>> getAllTranshipBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TranshipBoxes");
        Page<TranshipBoxDTO> page = transhipBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tranship-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tranship-boxes/:id : get the "id" transhipBox.
     *
     * @param id the id of the transhipBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tranship-boxes/{id}")
    @Timed
    public ResponseEntity<TranshipBoxDTO> getTranshipBox(@PathVariable Long id) {
        log.debug("REST request to get TranshipBox : {}", id);
        TranshipBoxDTO transhipBoxDTO = transhipBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipBoxDTO));
    }

    /**
     * DELETE  /tranship-boxes/:id : delete the "id" transhipBox.
     *
     * @param id the id of the transhipBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranship-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranshipBox(@PathVariable Long id) {
        log.debug("REST request to delete TranshipBox : {}", id);
        transhipBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * POST  /tranship-boxes : Create a new transhipBox.
     *
     * @param transhipBoxListDTO the transhipBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipBoxDTO, or with status 400 (Bad Request) if the transhipBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranship-boxes/batch")
    @Timed
    public ResponseEntity<TranshipBoxListForSaveBatchDTO> createTranshipBox(@Valid @RequestBody TranshipBoxListDTO transhipBoxListDTO) throws URISyntaxException {
        log.debug("REST request to save TranshipBox : {}", transhipBoxListDTO);
        TranshipBoxListForSaveBatchDTO result = transhipBoxService.saveBatchTranshipBox(transhipBoxListDTO);
        return ResponseEntity.created(new URI("/api/tranships/id/" + result.getTranshipId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getTranshipId().toString()))
            .body(result);
    }
    /**
     * 根据冻存盒CODE查询冻存盒和冻存管信息（取转运的冻存盒以及冻存管数据）
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/tranship-boxes/code/{frozenBoxCode}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByForzenBoxCode(@PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxAndFrozenTubeResponse res = transhipBoxService.findFrozenBoxAndTubeByBoxCode(frozenBoxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 根据冻存盒编码删除转运中的冻存盒以及冻存管
     * @param frozenBoxCode
     * @return
     */
    @DeleteMapping("/tranship-boxes/frozenBox/{frozenBoxCode}")
    @Timed
    public ResponseEntity<Void> deleteTranshipBoxByFrozenBox(@PathVariable String frozenBoxCode) {
        log.debug("REST request to delete TranshipBox : {}", frozenBoxCode);
        transhipBoxService.deleteTranshipBoxByFrozenBox(frozenBoxCode);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, frozenBoxCode)).build();
    }

    /**
     * 根据转运编码查询冻存盒编码List
     * @param transhipCode
     * @return
     */
    @GetMapping("/tranship-boxes/transhipCode/{transhipCode}")
    @Timed
    public ResponseEntity<List<FrozenBoxCodeForTranshipDTO>> getFrozenBoxCodeByTranshipCode(@PathVariable String transhipCode) {
        log.debug("REST request to get FrozenBox : {}", transhipCode);
        List<FrozenBoxCodeForTranshipDTO> res = transhipBoxService.getFrozenBoxCodeByTranshipCode(transhipCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 分页查询转运冻存盒
     * @param transhipCode
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/tranship-boxes/transhipCode/{transhipCode}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<FrozenBoxCodeForTranshipDTO> getFrozenBoxCodeByTranshipCode(@PathVariable String transhipCode, @RequestBody DataTablesInput input) {
        return transhipBoxService.getPageFrozenBoxCodeByTranshipCode(transhipCode,input);
    }
    /**
     * 从项目组导入样本
     * @param frozenBoxCodeStr
     * @return
     */

    @GetMapping("/tranship-boxes/frozenBoxCode/{frozenBoxCodeStr}/{sampleType}/{boxType}/import")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> importFrozenBoxAndFrozenTube(@PathVariable String frozenBoxCodeStr, @PathVariable Long sampleType, @PathVariable Long boxType) {
        log.debug("REST request to import FrozenBox And FrozenTubeDTOs From project group: {}", frozenBoxCodeStr);
        List<FrozenBoxAndFrozenTubeResponse> res = frozenBoxImportService.importFrozenBoxAndFrozenTube(frozenBoxCodeStr, sampleType, boxType);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
    /**
     * excel导入样本
     * @param file
     * @param request
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/tranship-boxes/projectCode/{projectCode}/upload",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> saveAndUploadStockOutRequirement(@PathVariable String projectCode,
                                                                                                 @RequestParam(value = "file") MultipartFile file,
                                                                                                 HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save FrozenBoxAndFrozenTubeResponse : {}");
        List<FrozenBoxAndFrozenTubeResponse> result = frozenBoxImportService.saveAndUploadFrozenBoxAndTube(projectCode,file,request);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 根据转运编码查询冻存盒编码List
     * @param transhipCode
     * @return
     */
    @GetMapping("/return-boxes/transhipCode/{transhipCode}")
    @Timed
    public ResponseEntity<List<FrozenBoxCodeForTranshipDTO>> getFrozenBoxCodeByTranshipCodeForReturnBack(@PathVariable String transhipCode) {
        log.debug("REST request to get FrozenBox : {}", transhipCode);
        List<FrozenBoxCodeForTranshipDTO> res = transhipBoxService.getFrozenBoxCodeByTranshipCode(transhipCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 分页查询转运冻存盒
     * @param transhipCode
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/return-boxes/transhipCode/{transhipCode}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<FrozenBoxCodeForTranshipDTO> getFrozenBoxCodeByTranshipCodeForReturnBack(@PathVariable String transhipCode, @RequestBody DataTablesInput input) {
        return transhipBoxService.getPageFrozenBoxCodeByTranshipCode(transhipCode,input);
    }

    /**
     * 归还冻存盒的保存
     * @param transhipBoxDTOS
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/return-boxes/batch/return-back/{id}")
    @Timed
    public ResponseEntity<List<TranshipBoxDTO>> createTranshipBoxForReturn(@PathVariable Long id ,@Valid @RequestBody List<TranshipBoxDTO> transhipBoxDTOS) throws URISyntaxException {
        log.debug("REST request to save TranshipBox For return back box: {}", transhipBoxDTOS);
        List<TranshipBoxDTO> result = transhipBoxService.saveBatchTranshipBoxForReturn(id,transhipBoxDTOS);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 根据出库申请编码和冻存盒编码串获取出库冻存盒和样本信息
     * @param applyCode
     * @param frozenBoxCodeStr
     * @return
     */
    @GetMapping("/return-boxes/stockOutApply/{applyCode}/frozenBoxCode/{frozenBoxCodeStr}")
    @Timed
    public ResponseEntity<List<TranshipBoxDTO>> getStockOutFrozenBoxAndSample(@PathVariable String applyCode, @PathVariable String frozenBoxCodeStr) {
        log.debug("REST request to import FrozenBox And FrozenTubeDTOs From StockOutBox: {}", frozenBoxCodeStr);
        List<TranshipBoxDTO> res = transhipBoxService.getStockOutFrozenBoxAndSample(applyCode, frozenBoxCodeStr);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 根据冻存盒获取归还冻存盒和归还样本的信息
     * @param id
     * @return
     */
    @GetMapping("/return-boxes/{id}")
    @Timed
    public ResponseEntity<TranshipBoxDTO> getTranshipBoxAndSampleByTranshipBoxId(@PathVariable Long id) {
        log.debug("REST request to get TranshipBoxDTO And TranshipTube : {}", id);
        TranshipBoxDTO res = transhipBoxService.findTranshipBoxAndSampleByTranshipBoxId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 修改保存归还冻存盒
     * @param id
     * @param transhipBoxDTOS
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/return-boxes/batch/return-back/{id}")
    @Timed
    public ResponseEntity<List<TranshipBoxDTO>> updateTranshipBoxForReturn(@PathVariable Long id ,@Valid @RequestBody List<TranshipBoxDTO> transhipBoxDTOS) throws URISyntaxException {
        log.debug("REST request to save TranshipBox For return back box: {}", transhipBoxDTOS);
        transhipBoxDTOS.forEach(s->{
            if(s.getId() == null){
                throw new BankServiceException("归还冻存盒的ID不能为空！");
            }
        });
        List<TranshipBoxDTO> result = transhipBoxService.saveBatchTranshipBoxForReturn(id,transhipBoxDTOS);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 删除归还冻存盒
     * @param id
     * @return
     */
    @DeleteMapping("/return-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranshipBoxForReturnBack(@PathVariable Long id) {
        log.debug("REST request to delete ReturnBackBox : {}", id);
        transhipBoxService.deleteReturnBackBox(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据转运单编码和冻存盒编码查询转运单的详情
     * @param transhipCode
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/tranship-boxes/tranship/{transhipCode}/frozenBox/{frozenBoxCode}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByTranshipCodeAndForzenBoxCode(@PathVariable String transhipCode, @PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxAndFrozenTubeResponse res = transhipBoxService.findTranshipBoxAndSampleByTranshipCodeAndFrozenBoxCode(transhipCode,frozenBoxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 根据转运单编码和冻存盒编码查询转运单的详情---归还
     * @param transhipCode
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/return-boxes/tranship/{transhipCode}/frozenBox/{frozenBoxCode}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByTranshipCodeAndForzenBoxCodeForReturnBack(@PathVariable String transhipCode, @PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxAndFrozenTubeResponse res = transhipBoxService.findTranshipBoxAndSampleByTranshipCodeAndFrozenBoxCode(transhipCode,frozenBoxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
}
