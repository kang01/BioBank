package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.domain.StockInBoxForDataTableEntity;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.StockInBoxDetail;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInBoxForSplit;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing StockInBox.
 */
@RestController
@RequestMapping("/api")
public class StockInBoxResource {

    private final Logger log = LoggerFactory.getLogger(StockInBoxResource.class);

    private static final String ENTITY_NAME = "stockInBox";

    private final StockInBoxService stockInBoxService;

    public StockInBoxResource(StockInBoxService stockInBoxService) {
        this.stockInBoxService = stockInBoxService;
    }

    /**
     * POST  /stock-in-boxes : Create a new stockInBox.
     *
     * @param stockInBoxDTO the stockInBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockInBoxDTO, or with status 400 (Bad Request) if the stockInBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-in-boxes")
    @Timed
    public ResponseEntity<StockInBoxDTO> createStockInBox(@Valid @RequestBody StockInBoxDTO stockInBoxDTO) throws URISyntaxException {
        log.debug("REST request to save StockInBox : {}", stockInBoxDTO);
        if (stockInBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockInBox cannot already have an ID")).body(null);
        }
        StockInBoxDTO result = stockInBoxService.save(stockInBoxDTO);
        return ResponseEntity.created(new URI("/api/stock-in-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-in-boxes : Updates an existing stockInBox.
     *
     * @param stockInBoxDTO the stockInBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockInBoxDTO,
     * or with status 400 (Bad Request) if the stockInBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockInBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-in-boxes")
    @Timed
    public ResponseEntity<StockInBoxDTO> updateStockInBox(@Valid @RequestBody StockInBoxDTO stockInBoxDTO) throws URISyntaxException {
        log.debug("REST request to update StockInBox : {}", stockInBoxDTO);
        if (stockInBoxDTO.getId() == null) {
            return createStockInBox(stockInBoxDTO);
        }
        StockInBoxDTO result = stockInBoxService.save(stockInBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-in-boxes : get all the stockInBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockInBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-in-boxes")
    @Timed
    public ResponseEntity<List<StockInBoxDTO>> getAllStockInBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockInBoxes");
        Page<StockInBoxDTO> page = stockInBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-in-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-in-boxes/:id : get the "id" stockInBox.
     *
     * @param id the id of the stockInBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockInBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-in-boxes/{id}")
    @Timed
    public ResponseEntity<StockInBoxDTO> getStockInBox(@PathVariable Long id) {
        log.debug("REST request to get StockInBox : {}", id);
        StockInBoxDTO stockInBoxDTO = stockInBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInBoxDTO));
    }

    /**
     * DELETE  /stock-in-boxes/:id : delete the "id" stockInBox.
     *
     * @param id the id of the stockInBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-in-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockInBox(@PathVariable Long id) {
        log.debug("REST request to delete StockInBox : {}", id);
        stockInBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据入库单编码，查询入库单的盒子
     * @param input
     * @param stockInCode
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-in-boxes/stock-in/{stockInCode}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockInBoxForDataTableEntity> getPageStockInBoxes(@RequestBody DataTablesInput input, @PathVariable String stockInCode) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<StockInBoxForDataTableEntity> result = stockInBoxService.getPageStockInBoxes(stockInCode,input);
        return result;
    }

    /**
     * 输入入库单编码和盒子编码，返回该入库单的某个盒子的信息
     * @param stockInCode
     * @param boxCode
     * @return
     */
    @GetMapping("/stock-in-boxes/stock-in/{stockInCode}/box/{boxCode}")
    @Timed
    public ResponseEntity<StockInBoxDetail> getStockInBoxDetail(@PathVariable String stockInCode, @PathVariable String boxCode) {
        StockInBoxDetail stockInBoxDetail = stockInBoxService.getStockInBoxDetail(stockInCode,boxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInBoxDetail));
    }

    /**
     * 输入入库单编码和盒子编码，以及分装后的盒子，返回保存好的分装后盒子的信息。
     * @param stockInCode 入库单编码
     * @param boxCode 盒子编码
     * @param stockInBoxForDataSplit 分装后的盒子
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-in-boxes/stock-in/{stockInCode}/box/{boxCode}/splited", method = RequestMethod.PUT, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<StockInBoxForSplit> splitedStockIn(@PathVariable String stockInCode,@PathVariable  String boxCode,@Valid @RequestBody  List<StockInBoxForSplit> stockInBoxForDataSplit) throws URISyntaxException {
        List<StockInBoxForSplit> detail = stockInBoxService.splitedStockIn(stockInCode,boxCode,stockInBoxForDataSplit);
        return detail;
    }
    /**
     * 上架保存-输入入库单编码和盒子编码，以及冻存位置信息，返回保存后的盒子信息
     * @param stockInCode
     * @param boxCode
     * @param boxPositionDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-in-boxes/stock-in/{stockInCode}/box/{boxCode}/moved")
    @Timed
    public ResponseEntity<StockInBoxDetail> movedStockIn(@PathVariable String stockInCode,@PathVariable  String boxCode,@Valid @RequestBody  FrozenBoxPositionDTO boxPositionDTO) throws URISyntaxException {
        StockInBoxDetail stockInBoxDetail = stockInBoxService.movedStockIn(stockInCode,boxCode,boxPositionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInBoxDetail.getFrozenBoxId().toString()))
            .body(stockInBoxDetail);
    }

    /**
     * 根据冻存盒code串查询待入库和已入库的盒子信息
     * @param frozenBoxCodeStr
     * @return
     */
    @GetMapping("/stock-in-boxes/boxCodes/{frozenBoxCodeStr}")
    @Timed
    public ResponseEntity<List<StockInBoxForDataTable>> getFrozenBoxByBoxCodeStr(@PathVariable  List<String> frozenBoxCodeStr) {
        log.debug("REST request to get FrozenBox By codes : {}", frozenBoxCodeStr);
        List<StockInBoxForDataTable> res = stockInBoxService.findFrozenBoxListByBoxCodeStr(frozenBoxCodeStr);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 撤销上架
     * @param stockInCode
     * @param boxCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-in-boxes/stock-in/{stockInCode}/box/{boxCode}/moveDown")
    @Timed
    public ResponseEntity<StockInBoxDetail> movedDownStockIn(@PathVariable String stockInCode,@PathVariable  String boxCode) throws URISyntaxException {
        StockInBoxDetail stockInBoxDetail = stockInBoxService.movedDownStockIn(stockInCode,boxCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInBoxDetail.getFrozenBoxId().toString()))
            .body(stockInBoxDetail);
    }

    /**
     * 根据冻存盒编码查询入库冻存盒
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/stock-in-boxes/boxCode/{frozenBoxCode}")
    @Timed
    public ResponseEntity<FrozenBoxDTO> getBoxAndTubeByForzenBoxCode(@PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxDTO res = stockInBoxService.getFrozenBoxAndTubeByForzenBoxCode(frozenBoxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 创建入库盒
     * @param frozenBoxDTO
     * @param stockInCode
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-in-boxes/stockInCode/{stockInCode}")
    @Timed
    public ResponseEntity<FrozenBoxDTO> createBoxByStockIn(@Valid @RequestBody FrozenBoxDTO frozenBoxDTO,@PathVariable String stockInCode) throws URISyntaxException {
        log.debug("REST request to save StockInBox : {}", frozenBoxDTO);
        FrozenBoxDTO result = stockInBoxService.createBoxByStockIn(frozenBoxDTO,stockInCode);
        return ResponseEntity.created(new URI("/api/stock-in-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * 编辑保存入库盒
     * @param frozenBoxDTO
     * @param stockInCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-in-boxes/stockInCode/{stockInCode}")
    @Timed
    public ResponseEntity<FrozenBoxDTO> updateBoxByStockIn(@Valid @RequestBody FrozenBoxDTO frozenBoxDTO,@PathVariable String stockInCode) throws URISyntaxException {
        log.debug("REST request to save StockInBox : {}", frozenBoxDTO);
        if(frozenBoxDTO.getId() == null){
            throw new BankServiceException("冻存盒ID不能为空！");
        }
        FrozenBoxDTO result = stockInBoxService.createBoxByStockIn(frozenBoxDTO,stockInCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
