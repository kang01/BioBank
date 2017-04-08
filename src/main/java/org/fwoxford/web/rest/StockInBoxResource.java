package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.dto.SampleTypeDTO;
import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.dto.SupportRackDTO;
import org.fwoxford.service.dto.response.StockInBoxDetail;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
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
    public DataTablesOutput<StockInBoxForDataTable> getPageStockInBoxes(@RequestBody DataTablesInput input, @PathVariable String stockInCode) {
        return stockInBoxService.getPageStockInBoxes(input,stockInCode);
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
}
