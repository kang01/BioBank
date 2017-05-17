package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.service.StockOutApplyService;
import org.fwoxford.service.dto.StockOutApplyDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
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
     * GET  /stock-out-applies : get all the stockOutApplies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutApplies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-applies")
    @Timed
    public ResponseEntity<List<StockOutApplyDTO>> getAllStockOutApplies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutApplies");
        Page<StockOutApplyDTO> page = stockOutApplyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-applies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-applies/:id : get the "id" stockOutApply.
     *
     * @param id the id of the stockOutApplyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutApplyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-applies/{id}")
    @Timed
    public ResponseEntity<StockOutApplyDTO> getStockOutApply(@PathVariable Long id) {
        log.debug("REST request to get StockOutApply : {}", id);
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutApplyDTO));
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
    public ResponseEntity<StockOutApplyDTO> initStockOutApply() throws URISyntaxException {
        log.debug("REST request to create StockOutApply first");
        StockOutApplyDTO result = stockOutApplyService.initStockOutApply();
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

}
