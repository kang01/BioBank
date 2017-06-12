package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.service.StockOutTaskService;
import org.fwoxford.service.dto.response.StockOutFrozenBoxForTaskDataTableEntity;
import org.fwoxford.service.dto.response.StockOutTaskForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutTaskForPlanDataTableEntity;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutTaskDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockOutTask.
 */
@RestController
@RequestMapping("/api")
public class StockOutTaskResource {

    private final Logger log = LoggerFactory.getLogger(StockOutTaskResource.class);

    private static final String ENTITY_NAME = "stockOutTask";

    private final StockOutTaskService stockOutTaskService;

    public StockOutTaskResource(StockOutTaskService stockOutTaskService) {
        this.stockOutTaskService = stockOutTaskService;
    }

    /**
     * POST  /stock-out-tasks : Create a new stockOutTask.
     *
     * @param stockOutTaskDTO the stockOutTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutTaskDTO, or with status 400 (Bad Request) if the stockOutTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-tasks")
    @Timed
    public ResponseEntity<StockOutTaskDTO> createStockOutTask(@Valid @RequestBody StockOutTaskDTO stockOutTaskDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutTask : {}", stockOutTaskDTO);
        if (stockOutTaskDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutTask cannot already have an ID")).body(null);
        }
        StockOutTaskDTO result = stockOutTaskService.save(stockOutTaskDTO);
        return ResponseEntity.created(new URI("/api/stock-out-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-tasks : Updates an existing stockOutTask.
     * 任务修改保存
     * @param stockOutTaskDTO the stockOutTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutTaskDTO,
     * or with status 400 (Bad Request) if the stockOutTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutTaskDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-tasks")
    @Timed
    public ResponseEntity<StockOutTaskDTO> updateStockOutTask(@Valid @RequestBody StockOutTaskDTO stockOutTaskDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutTask : {}", stockOutTaskDTO);
        if (stockOutTaskDTO.getId() == null) {
            throw new BankServiceException("任务ID不能为空！");
        }
        StockOutTaskDTO result = stockOutTaskService.save(stockOutTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-tasks : get all the stockOutTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutTasks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-tasks")
    @Timed
    public ResponseEntity<List<StockOutTaskDTO>> getAllStockOutTasks(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutTasks");
        Page<StockOutTaskDTO> page = stockOutTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-tasks/:id : get the "id" stockOutTask.
     *
     * @param id the id of the stockOutTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-tasks/{id}")
    @Timed
    public ResponseEntity<StockOutTaskDTO> getStockOutTask(@PathVariable Long id) {
        log.debug("REST request to get StockOutTask : {}", id);
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutTaskDTO));
    }

    /**
     * 新增保存任务
     * @param id
     * @param boxIds
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-tasks/plan/{id}/frozenBox/{boxIds}")
    @Timed
    public ResponseEntity<StockOutTaskDTO> createStockOutTask(@PathVariable Long id,@PathVariable List<Long> boxIds) throws URISyntaxException {
        StockOutTaskDTO result = stockOutTaskService.save(id, boxIds);
        return ResponseEntity.created(new URI("/api/stock-out-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 删除任务
     * @param id
     * @return
     */
    @DeleteMapping("/stock-out-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutTask(@PathVariable Long id) {
        log.debug("REST request to delete StockOutTask : {}", id);

        stockOutTaskService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * 根据计划ID获取出库任务列表
     * @param input
     * @param id
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-tasks/plan/{id}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutTaskForPlanDataTableEntity> getPageStockOutPlan(@RequestBody DataTablesInput input, @PathVariable Long id) {
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


        Page<StockOutTaskForPlanDataTableEntity> entities = stockOutTaskService.findAllByPlan(id, pageRequest);
        List<StockOutTaskForPlanDataTableEntity> stockOutApplyList =  entities == null ?
            new ArrayList<StockOutTaskForPlanDataTableEntity>() : entities.getContent();

        DataTablesOutput<StockOutTaskForPlanDataTableEntity> result = new DataTablesOutput<StockOutTaskForPlanDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(entities.getTotalElements());
        return result;
    }

    /**
     * 获取出库任务列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-tasks", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutTaskForDataTableEntity> getDataTableStockOutTask(@RequestBody DataTablesInput input) {
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


        Page<StockOutTaskForDataTableEntity> entities = stockOutTaskService.getDataTableStockOutTask(pageRequest);
        List<StockOutTaskForDataTableEntity> stockOutApplyList =  entities == null ?
            new ArrayList<StockOutTaskForDataTableEntity>() : entities.getContent();

        DataTablesOutput<StockOutTaskForDataTableEntity> result = new DataTablesOutput<StockOutTaskForDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(entities.getTotalElements());
        return result;
    }

    /**
     * 根据计划ID查询任务
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-tasks/plan/{id}")
    @Timed
    public ResponseEntity<List<StockOutTaskDTO>> getAllStockOutTasksByPlanId(@PathVariable Long id)
        throws URISyntaxException {
        List<StockOutTaskDTO> stockOutTaskDTOS = stockOutTaskService.getAllStockOutTasksByPlanId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutTaskDTOS));
    }

    /**
     * 任务开始
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-tasks/{id}/begin")
    @Timed
    public ResponseEntity<StockOutTaskDTO> startStockOutTask(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to begin StockOutTask : {}", id);
        StockOutTaskDTO result = stockOutTaskService.startStockOutTask(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString()))
            .body(result);
    }
}
