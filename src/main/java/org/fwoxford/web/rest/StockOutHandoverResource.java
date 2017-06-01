package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.domain.StockOutHandover;
import org.fwoxford.service.StockOutHandoverService;
import org.fwoxford.service.dto.response.StockOutFrozenBoxForTaskDataTableEntity;
import org.fwoxford.service.dto.response.StockOutHandoverDataTableEntity;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutHandoverDTO;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockOutHandover.
 */
@RestController
@RequestMapping("/api")
public class StockOutHandoverResource {

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverResource.class);

    private static final String ENTITY_NAME = "stockOutHandover";

    private final StockOutHandoverService stockOutHandoverService;

    public StockOutHandoverResource(StockOutHandoverService stockOutHandoverService) {
        this.stockOutHandoverService = stockOutHandoverService;
    }

    /**
     * POST  /stock-out-handovers : Create a new stockOutHandover.
     *
     * @param stockOutHandoverDTO the stockOutHandoverDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutHandoverDTO, or with status 400 (Bad Request) if the stockOutHandover has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-handovers")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> createStockOutHandover(@Valid @RequestBody StockOutHandoverDTO stockOutHandoverDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutHandover : {}", stockOutHandoverDTO);
        if (stockOutHandoverDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutHandover cannot already have an ID")).body(null);
        }
        StockOutHandoverDTO result = stockOutHandoverService.save(stockOutHandoverDTO);
        return ResponseEntity.created(new URI("/api/stock-out-handovers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-handovers : Updates an existing stockOutHandover.
     *
     * @param stockOutHandoverDTO the stockOutHandoverDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutHandoverDTO,
     * or with status 400 (Bad Request) if the stockOutHandoverDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutHandoverDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-handovers")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> updateStockOutHandover(@Valid @RequestBody StockOutHandoverDTO stockOutHandoverDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutHandover : {}", stockOutHandoverDTO);
        if (stockOutHandoverDTO.getId() == null) {
            return createStockOutHandover(stockOutHandoverDTO);
        }
        StockOutHandoverDTO result = stockOutHandoverService.save(stockOutHandoverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutHandoverDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-handovers : get all the stockOutHandovers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutHandovers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-handovers")
    @Timed
    public ResponseEntity<List<StockOutHandoverDTO>> getAllStockOutHandovers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutHandovers");
        Page<StockOutHandoverDTO> page = stockOutHandoverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-handovers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-handovers/:id : get the "id" stockOutHandover.
     *
     * @param id the id of the stockOutHandoverDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutHandoverDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-handovers/{id}")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> getStockOutHandover(@PathVariable Long id) {
        log.debug("REST request to get StockOutHandover : {}", id);
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutHandoverDTO));
    }

    /**
     * DELETE  /stock-out-handovers/:id : delete the "id" stockOutHandover.
     *
     * @param id the id of the stockOutHandoverDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-handovers/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutHandover(@PathVariable Long id) {
        log.debug("REST request to delete StockOutHandover : {}", id);
        stockOutHandoverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据任务创建交接单
     * @param taskId
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-handovers/{taskId}")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> createStockOutHandover(@PathVariable Long taskId) throws URISyntaxException {
        log.debug("REST request to save StockOutHandover : {}", taskId);
        StockOutHandoverDTO result = stockOutHandoverService.saveByTask(taskId);
        return ResponseEntity.created(new URI("/api/stock-out-handovers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-handovers", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutHandoverDataTableEntity> getPageStockOutHandOver(@RequestBody DataTablesInput input) {
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


        Page<StockOutHandoverDataTableEntity> entities = stockOutHandoverService.getPageStockOutHandOver(pageRequest);
        List<StockOutHandoverDataTableEntity> stockOutHandOverList =  entities == null ?
            new ArrayList<StockOutHandoverDataTableEntity>() : entities.getContent();

        DataTablesOutput<StockOutHandoverDataTableEntity> result = new DataTablesOutput<StockOutHandoverDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutHandOverList);
        result.setRecordsFiltered(stockOutHandOverList.size());
        result.setRecordsTotal(entities.getTotalElements());
        return result;
    }
}
