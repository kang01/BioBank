package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.StockIn;
import org.fwoxford.domain.StockInBox;
import org.fwoxford.domain.StockInForDataTableEntity;
import org.fwoxford.repository.StockInBoxRepository;
import org.fwoxford.service.StockInService;
import org.fwoxford.service.UserService;
import org.fwoxford.service.dto.StockInCompleteDTO;
import org.fwoxford.service.dto.StockInDTO;
import org.fwoxford.service.dto.StockInForDataDetail;
import org.fwoxford.service.dto.TranshipToStockInDTO;
import org.fwoxford.service.mapper.StockInMapper;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing StockIn.
 */
@RestController
@RequestMapping("/api")
public class StockInResource {

    private final Logger log = LoggerFactory.getLogger(StockInResource.class);

    private static final String ENTITY_NAME = "stockIn";

    private final StockInService stockInService;

    @Autowired
    private UserService userService;

    @Autowired
    private StockInMapper stockInMapper;

    @Autowired
    private StockInBoxRepository stockInBoxRepository;

    public StockInResource(StockInService stockInService) {
        this.stockInService = stockInService;
    }

    /**
     * POST  /stock-ins : Create a new stockIn.
     *
     * @param stockInDTO the stockInDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockInDTO, or with status 400 (Bad Request) if the stockIn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-ins")
    @Timed
    public ResponseEntity<StockInDTO> createStockIn(@Valid @RequestBody StockInDTO stockInDTO) throws URISyntaxException {
        log.debug("REST request to save StockIn : {}", stockInDTO);
        if (stockInDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockIn cannot already have an ID")).body(null);
        }
        StockInDTO result = stockInService.createStockIn(stockInDTO);
        return ResponseEntity.created(new URI("/api/stock-ins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 编辑入库
     * @param stockInDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-ins")
    @Timed
    public ResponseEntity<StockInForDataDetail> updateStockIns(@Valid @RequestBody StockInForDataDetail stockInDTO) throws URISyntaxException {
        log.debug("REST request to update StockIn : {}", stockInDTO);
        StockInForDataDetail result = stockInService.updateStockIns(stockInDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInDTO.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /stock-ins : Updates an existing stockIn.
     *
     * @param stockInDTO the stockInDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockInDTO,
     * or with status 400 (Bad Request) if the stockInDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockInDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-in")
    @Timed
    public ResponseEntity<StockInDTO> updateStockIn(@Valid @RequestBody StockInDTO stockInDTO) throws URISyntaxException {
        log.debug("REST request to update StockIn : {}", stockInDTO);
        if (stockInDTO.getId() == null) {
            return createStockIn(stockInDTO);
        }
        StockInDTO result = stockInService.save(stockInDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-ins : get all the stockIns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockIns in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-ins")
    @Timed
    public ResponseEntity<List<StockInDTO>> getAllStockIns(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockIns");
        Page<StockInDTO> page = stockInService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-ins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-ins/:id : get the "id" stockIn.
     *
     * @param id the id of the stockInDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockInDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-ins/{id}")
    @Timed
    public ResponseEntity<StockInDTO> getStockIn(@PathVariable Long id) {
        log.debug("REST request to get StockIn : {}", id);
        StockInDTO stockInDTO = stockInService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInDTO));
    }

    /**
     * DELETE  /stock-ins/:id : delete the "id" stockIn.
     *
     * @param id the id of the stockInDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-ins/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockIn(@PathVariable Long id) {
        log.debug("REST request to delete StockIn : {}", id);
        stockInService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 转运单 到 入库
     * @param transhipCode
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-in/tranship/{transhipCode}")
    @Timed
    public ResponseEntity<StockInForDataDetail> createStockIns(@PathVariable String transhipCode, @RequestBody @Valid TranshipToStockInDTO transhipToStockInDTO) throws URISyntaxException {
        log.debug("REST request to save StockIn : {}", transhipCode);

        StockInForDataDetail result = stockInService.saveStockIns(transhipCode,transhipToStockInDTO);
        return ResponseEntity.created(new URI("/api/res/stock-in" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 查询入库列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-in", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockInForDataTableEntity> getPageStockIn(@RequestBody DataTablesInput input) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        return stockInService.findStockIn(input);
    }

    /**
     * 输入入库单编码，返回入库信息---入库完成
     * @param stockInCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-in/{stockInCode}/completed")
    @Timed
    public ResponseEntity<StockInForDataDetail> completedStockIn(@PathVariable String stockInCode ,@RequestBody @Valid StockInCompleteDTO stockInCompleteDTO) throws URISyntaxException {
        log.debug("REST request to update StockIn : {}", stockInCode);

        StockInForDataDetail result = stockInService.completedStockIn(stockInCode,stockInCompleteDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * 输入入库单ID，返回入库信息
     * @param id
     * @return
     */
    @GetMapping("/stock-in/{id}")
    @Timed
    public ResponseEntity<StockInForDataDetail> getStockInById(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        StockInForDataDetail stockInForDataDetail = stockInService.getStockInById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInForDataDetail));
    }

    /**
     * 输入转运编码，返回相关入库信息
     * @param transhipCode
     * @return
     */
    @GetMapping("/stock-in/tranship/{transhipCode}")
    @Timed
    public ResponseEntity<StockInForDataDetail> getStockIn(@PathVariable String transhipCode) {
        StockInForDataDetail stockInForDataDetail = stockInService.getStockInByTranshipCode(transhipCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInForDataDetail));
    }
}
