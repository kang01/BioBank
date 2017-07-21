package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.domain.StockOutTask;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.FrozenBoxForSaveBatchDTO;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.web.rest.util.BankUtil;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StockOutFrozenBox.
 */
@RestController
@RequestMapping("/api")
public class StockOutFrozenBoxResource {

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenBoxResource.class);

    private static final String ENTITY_NAME = "stockOutFrozenBox";

    private final StockOutFrozenBoxService stockOutFrozenBoxService;

    public StockOutFrozenBoxResource(StockOutFrozenBoxService stockOutFrozenBoxService) {
        this.stockOutFrozenBoxService = stockOutFrozenBoxService;
    }

    /**
     * POST  /stock-out-frozen-boxes : Create a new stockOutFrozenBox.
     * @param stockOutFrozenBoxDTO the stockOutFrozenBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutFrozenBoxDTO, or with status 400 (Bad Request) if the stockOutFrozenBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-frozen-boxes")
    @Timed
    public ResponseEntity<StockOutFrozenBoxDTO> createStockOutFrozenBox(@Valid @RequestBody StockOutFrozenBoxDTO stockOutFrozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        if (stockOutFrozenBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutFrozenBox cannot already have an ID")).body(null);
        }
        StockOutFrozenBoxDTO result = stockOutFrozenBoxService.save(stockOutFrozenBoxDTO);
        return ResponseEntity.created(new URI("/api/stock-out-frozen-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-frozen-boxes : Updates an existing stockOutFrozenBox.
     *
     * @param stockOutFrozenBoxDTO the stockOutFrozenBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutFrozenBoxDTO,
     * or with status 400 (Bad Request) if the stockOutFrozenBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutFrozenBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-frozen-boxes")
    @Timed
    public ResponseEntity<StockOutFrozenBoxDTO> updateStockOutFrozenBox(@Valid @RequestBody StockOutFrozenBoxDTO stockOutFrozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        if (stockOutFrozenBoxDTO.getId() == null) {
            return createStockOutFrozenBox(stockOutFrozenBoxDTO);
        }
        StockOutFrozenBoxDTO result = stockOutFrozenBoxService.save(stockOutFrozenBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutFrozenBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-frozen-boxes : get all the stockOutFrozenBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutFrozenBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-frozen-boxes")
    @Timed
    public ResponseEntity<List<StockOutFrozenBoxDTO>> getAllStockOutFrozenBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutFrozenBoxes");
        Page<StockOutFrozenBoxDTO> page = stockOutFrozenBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-frozen-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-frozen-boxes/:id : get the "id" stockOutFrozenBox.
     *
     * @param id the id of the stockOutFrozenBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutFrozenBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-frozen-boxes/{id}")
    @Timed
    public ResponseEntity<StockOutFrozenBoxDTO> getStockOutFrozenBox(@PathVariable Long id) {
        log.debug("REST request to get StockOutFrozenBox : {}", id);
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutFrozenBoxDTO));
    }

    /**
     * DELETE  /stock-out-frozen-boxes/:id : delete the "id" stockOutFrozenBox.
     *
     * @param id the id of the stockOutFrozenBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-frozen-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutFrozenBox(@PathVariable Long id) {
        log.debug("REST request to delete StockOutFrozenBox : {}", id);
        stockOutFrozenBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 出库任务查看详情
     * @param input
     * @param id
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/task/{id}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenBoxForTaskDetailDataTableEntity> getPageStockOutBoxForTask(@RequestBody DataTablesInput input, @PathVariable Long id) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<StockOutFrozenBoxForTaskDetailDataTableEntity> result = stockOutFrozenBoxService.getPageByTask(id, input);
        return result;
    }

    /**
     * 根据需求查询核对通过的不在任务内的冻存盒（带分页）
     * @param input
     * @param ids
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/requirement/{ids}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<FrozenBoxForStockOutDataTableEntity> getPageStockOutPlan(@RequestBody DataTablesInput input, @PathVariable List<Long> ids) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<FrozenBoxForStockOutDataTableEntity> output =  stockOutFrozenBoxService.getPageByRequirementIds(ids, input);
        return output;
    }

    /**
     * 根据任务查询需要出库的冻存盒列表（根据出库任务冻存管统计出来的）
     * @param taskId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-frozen-boxes/task/{taskId}")
    @Timed
    public ResponseEntity<List<StockOutFrozenBoxForTaskDataTableEntity>> getAllStockOutFrozenBoxesByTask(@PathVariable Long taskId)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutFrozenBoxes");
        List<StockOutFrozenBoxForTaskDataTableEntity> list = stockOutFrozenBoxService.getAllStockOutFrozenBoxesByTask(taskId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }

    /**
     * 创建临时盒
     * @param frozenBoxDTO
     * @param taskId
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-frozen-boxes/task/{taskId}")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> createFrozenBoxForStockOut(@Valid @RequestBody List<FrozenBoxAndFrozenTubeResponse> frozenBoxDTO, @PathVariable Long taskId) throws URISyntaxException {
        log.debug("REST request to save FrozenBox : {}", frozenBoxDTO);
        List<FrozenBoxAndFrozenTubeResponse> result = stockOutFrozenBoxService.createFrozenBoxForStockOut(frozenBoxDTO,taskId);
        return ResponseEntity.created(new URI("/api/stock-out-frozen-boxes/" + taskId))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, taskId.toString()))
            .body(result);
    }

    /**
     * 取当前任务的临时盒
     * @param taskId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-frozen-boxes/temp-box/task/{taskId}")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> getAllStockOutTempFrozenBoxesByTask(@PathVariable Long taskId)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutFrozenBoxes");
        List<FrozenBoxAndFrozenTubeResponse> list = stockOutFrozenBoxService.getAllTempStockOutFrozenBoxesByTask(taskId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }


    /**
     * 根据任务查询需要出库的和已经出库的冻存盒列表（根据出库冻存管统计出来的）
     * @param taskId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-frozen-boxes/task-box/{taskId}")
    @Timed
    public ResponseEntity<List<StockOutFrozenBoxDataTableEntity>> getStockOutFrozenBoxesByTask(@PathVariable Long taskId)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutFrozenBoxes");
        List<StockOutFrozenBoxDataTableEntity> list = stockOutFrozenBoxService.getStockOutFrozenBoxesByTask(taskId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }

    /**
     * 样本出库
     * @param stockOutFrozenBoxPoisition
     * @param taskId
     * @param frozenBoxIds
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-frozen-boxes/task/{taskId}/frozen-boxes/{frozenBoxIds}")
    @Timed
    public ResponseEntity<StockOutTaskDTO> stockOut(@Valid @RequestBody StockOutFrozenBoxPoisition stockOutFrozenBoxPoisition, @PathVariable Long taskId, @PathVariable List<Long> frozenBoxIds) throws URISyntaxException {
        log.debug("REST request to update StockOutFrozenBox : {}", taskId);
        StockOutTaskDTO result = stockOutFrozenBoxService.stockOut(stockOutFrozenBoxPoisition,taskId,frozenBoxIds);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskId.toString()))
            .body(result);
    }

    /**
     * 冻存盒单个批注
     * @param stockOutFrozenBoxDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-frozen-boxes/note")
    @Timed
    public ResponseEntity<StockOutFrozenBoxDTO> stockOutNote(@Valid @RequestBody StockOutFrozenBoxDTO stockOutFrozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        StockOutFrozenBoxDTO result = stockOutFrozenBoxService.stockOutNote(stockOutFrozenBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 打印取盒单
     * @param taskId
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-frozen-boxes/task/{taskId}/print",method = RequestMethod.GET,
        produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Timed
    public ResponseEntity printStockOutFrozenBox(@PathVariable Long taskId) throws URISyntaxException {

        try {
            ByteArrayOutputStream result = stockOutFrozenBoxService.printStockOutFrozenBox(taskId);
            byte[] fileInByte = result.toByteArray();
            final HttpHeaders headers = new HttpHeaders();
            String fileReportName = "取盒单.xlsx";
            headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.set("Content-disposition", "attachment; filename="+ URLEncoder.encode(fileReportName, "UTF-8"));

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
     * 查询已交接冻存盒
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/handover/{id}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenBoxForDataTableEntity> getPageHandoverStockOutFrozenBoxes(@PathVariable Long id, @RequestBody DataTablesInput input) {
        List<Sort.Order> orders = new ArrayList<>();
        List<Column> columns = input.getColumns();
        input.getOrder().forEach(o -> {
            Column col = columns.get(o.getColumn());
            if(col.getName()!=null&&col.getName()!=""){
                Sort.Order order = new Sort.Order(Sort.Direction.fromString(o.getDir()), col.getName());
                orders.add(order);
            }
        });
        Sort.Order order = new Sort.Order(Sort.Direction.fromString("desc"), "id");
        orders.add(order);
        Sort sort = new Sort(orders);
        PageRequest pageRequest = new PageRequest(input.getStart() / input.getLength(), input.getLength(), sort);


        Page<StockOutFrozenBoxForDataTableEntity> entities = stockOutFrozenBoxService.getPageHandoverStockOutFrozenBoxes(id, pageRequest);
        List<StockOutFrozenBoxForDataTableEntity> stockOutApplyList =  entities == null ?
            new ArrayList<StockOutFrozenBoxForDataTableEntity>() : entities.getContent();

        DataTablesOutput<StockOutFrozenBoxForDataTableEntity> result = new DataTablesOutput<StockOutFrozenBoxForDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(entities.getTotalElements());
        return result;
    }

    /**
     * 查询待交接冻存盒
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/apply/{id}/waiting-handover", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenBoxForDataTableEntity> getPageStockOutFrozenBoxes(@PathVariable Long id, @RequestBody DataTablesInput input) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<StockOutFrozenBoxForDataTableEntity> result = stockOutFrozenBoxService.getPageWaitingHandOverStockOutFrozenBoxes(id,input);
        return result;
    }
}
