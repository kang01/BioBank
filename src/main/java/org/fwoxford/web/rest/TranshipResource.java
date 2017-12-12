package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.AttachmentDTO;
import org.fwoxford.service.dto.response.StockInForDataDetail;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.TranshipToStockInDTO;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tranship.
 */
@RestController
@RequestMapping("/api")
public class TranshipResource {

    private final Logger log = LoggerFactory.getLogger(TranshipResource.class);

    private static final String ENTITY_NAME = "tranship";

    private final TranshipService transhipService;

    public TranshipResource(TranshipService transhipService) {
        this.transhipService = transhipService;
    }
    /**
     * GET  /tranships : get all the tranships.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tranships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranships")
    @Timed
    public ResponseEntity<List<TranshipDTO>> getAllTranships(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tranships");
        Page<TranshipDTO> page = transhipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tranships");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tranships/:id : get the "id" tranship. 根据转运ID查询转运和冻存盒的信息
     *
     * @param id the id of the transhipDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tranships/{id}")
    @Timed
    public ResponseEntity<TranshipByIdResponse> getTranship(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        TranshipByIdResponse transhipByIdResponse = transhipService.findTranshipAndFrozenBox(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipByIdResponse));
    }

    /**
     * 根据转运ID查询转运详情
     * @param id
     * @return
     */
    @GetMapping("/tranships/id/{id}")
    @Timed
    public ResponseEntity<TranshipDTO> getTranshipById(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        TranshipDTO transhipDTO = transhipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipDTO));
    }

    /**
     * DELETE  /tranships/:id : delete the "id" tranship.
     *
     * @param id the id of the transhipDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranships/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranship(@PathVariable Long id) {
        log.debug("REST request to delete Tranship : {}", id);
        transhipService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /tranships : get all the tranships. 获取转运记录
     *
     * @param input the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tranships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/tranships", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<TranshipResponse> getPageTranship(@RequestBody DataTablesInput input) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<TranshipResponse> transhipsTablesOutput = transhipService.findAllTranship(input, Constants.RECEIVE_TYPE_PROJECT_SITE);
        return transhipsTablesOutput;
    }

    /**
     * 添加转运记录
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/tranships/new-empty")
    @Timed
    public ResponseEntity<TranshipDTO> initTranship() throws URISyntaxException {
        log.debug("REST request to create Tranship first");
        TranshipDTO result = transhipService.initTranship();
        return ResponseEntity.created(new URI("/res/api/tranships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 添加转运记录
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/tranships/new-empty/{projectId}/{projectSiteId}")
    @Timed
    public ResponseEntity<TranshipDTO> initTranship(@PathVariable Long projectId, @PathVariable Long projectSiteId) throws URISyntaxException {
        log.debug("REST request to create Tranship first");
        TranshipDTO result = transhipService.initTranship(projectId, projectSiteId,null);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 修改保存转运记录单
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/tranships/update-object")
    @Timed
    public ResponseEntity<TranshipDTO> saveTranships(@Valid @RequestBody TranshipDTO transhipDTO) throws URISyntaxException {
        log.debug("REST request to update Tranship : {}", transhipDTO);
        if (transhipDTO.getId() == null) {
            return initTranship();
        }
        TranshipDTO result = transhipService.save(transhipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipDTO.getId().toString()))
            .body(result);
    }

    /**
     * 作废转运记录
     * @param transhipCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/tranships/invalid/{transhipCode}")
    @Timed
    public ResponseEntity<TranshipDTO> invalidTranship(@PathVariable String transhipCode) throws URISyntaxException {
        log.debug("REST request to save StockIn : {}", transhipCode);
        TranshipDTO result = transhipService.invalidTranship(transhipCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 判断转运单号是否已经存在  ----true：已经存在，false:不存在
     * @param trackNumber
     * @return
     */
    @RequestMapping(value = "/tranships/isRepeat/{transhipCode}/{trackNumber}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public Boolean isRepeatFrozenBoxCode(@PathVariable String transhipCode ,@PathVariable String trackNumber) {
        Boolean flag =  transhipService.isRepeatTrackNumber(transhipCode,trackNumber);
        return flag;
    }

    /**
     * 转运完成
     * @param transhipCode
     * @param transhipToStockInDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/tranships/{transhipCode}/completed")
    @Timed
    public ResponseEntity<StockInForDataDetail> completedTranship(@PathVariable String transhipCode, @RequestBody @Valid TranshipToStockInDTO transhipToStockInDTO) throws URISyntaxException {
        log.debug("REST request to save StockIn : {}", transhipCode);

        StockInForDataDetail result = transhipService.completedTranship(transhipCode,transhipToStockInDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 上传文件
     * @param transhipId
     * @param attachment
     * @param file
     * @param request
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/tranships/{transhipId}/upload",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<AttachmentDTO> saveAndUploadTranship(@PathVariable Long transhipId,
                                                               @RequestParam(value = "attachment") String attachment,
                                                               @RequestParam(value = "file") MultipartFile file,
                                                               HttpServletRequest request) throws URISyntaxException {
        JSONObject jsonObject = JSONObject.fromObject(attachment);
        AttachmentDTO attachmentDTO = (AttachmentDTO) JSONObject.toBean(jsonObject, AttachmentDTO.class);
        log.debug("REST request to upload tranship images : {}", attachmentDTO);
        AttachmentDTO result = transhipService.saveAndUploadTranship( attachmentDTO,transhipId,file,request);

        return ResponseEntity.created(new URI("/res/api/tranships/" + transhipId))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 归还记录列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/return-back", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<TranshipResponse> getPageTranshipForReturnBack(@RequestBody DataTablesInput input) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<TranshipResponse> transhipsTablesOutput = transhipService.findAllTranship(input, Constants.RECEIVE_TYPE_RETURN_BACK);
        return transhipsTablesOutput;
    }

    /**
     * 添加归还记录
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/return-back/new-empty/{stockOutApplyId}")
    @Timed
    public ResponseEntity<TranshipDTO> initReturnBack(@PathVariable Long stockOutApplyId) throws URISyntaxException {
        log.debug("REST request to create Tranship first");
        TranshipDTO result = transhipService.initReturnBack(stockOutApplyId);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 归还详情
     * @param id
     * @return
     */
    @GetMapping("/return-back/id/{id}")
    @Timed
    public ResponseEntity<TranshipDTO> getReturnBackById(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        TranshipDTO transhipDTO = transhipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipDTO));
    }
}
