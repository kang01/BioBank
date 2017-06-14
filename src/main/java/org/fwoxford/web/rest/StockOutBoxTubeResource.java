package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.service.StockOutBoxTubeService;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.response.StockOutFrozenTubeDataTableEntity;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutBoxTubeDTO;
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
 * REST controller for managing StockOutBoxTube.
 */
@RestController
@RequestMapping("/api")
public class StockOutBoxTubeResource {

    private final Logger log = LoggerFactory.getLogger(StockOutBoxTubeResource.class);

    private static final String ENTITY_NAME = "stockOutBoxTube";

    private final StockOutBoxTubeService stockOutBoxTubeService;

    public StockOutBoxTubeResource(StockOutBoxTubeService stockOutBoxTubeService) {
        this.stockOutBoxTubeService = stockOutBoxTubeService;
    }

    /**
     * POST  /stock-out-box-tubes : Create a new stockOutBoxTube.
     *
     * @param stockOutBoxTubeDTO the stockOutBoxTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutBoxTubeDTO, or with status 400 (Bad Request) if the stockOutBoxTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-box-tubes")
    @Timed
    public ResponseEntity<StockOutBoxTubeDTO> createStockOutBoxTube(@Valid @RequestBody StockOutBoxTubeDTO stockOutBoxTubeDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutBoxTube : {}", stockOutBoxTubeDTO);
        if (stockOutBoxTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutBoxTube cannot already have an ID")).body(null);
        }
        StockOutBoxTubeDTO result = stockOutBoxTubeService.save(stockOutBoxTubeDTO);
        return ResponseEntity.created(new URI("/api/stock-out-box-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-box-tubes : Updates an existing stockOutBoxTube.
     *
     * @param stockOutBoxTubeDTO the stockOutBoxTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutBoxTubeDTO,
     * or with status 400 (Bad Request) if the stockOutBoxTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutBoxTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-box-tubes")
    @Timed
    public ResponseEntity<StockOutBoxTubeDTO> updateStockOutBoxTube(@Valid @RequestBody StockOutBoxTubeDTO stockOutBoxTubeDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutBoxTube : {}", stockOutBoxTubeDTO);
        if (stockOutBoxTubeDTO.getId() == null) {
            return createStockOutBoxTube(stockOutBoxTubeDTO);
        }
        StockOutBoxTubeDTO result = stockOutBoxTubeService.save(stockOutBoxTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutBoxTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-box-tubes : get all the stockOutBoxTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutBoxTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-box-tubes")
    @Timed
    public ResponseEntity<List<StockOutBoxTubeDTO>> getAllStockOutBoxTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutBoxTubes");
        Page<StockOutBoxTubeDTO> page = stockOutBoxTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-box-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-box-tubes/:id : get the "id" stockOutBoxTube.
     *
     * @param id the id of the stockOutBoxTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutBoxTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-box-tubes/{id}")
    @Timed
    public ResponseEntity<StockOutBoxTubeDTO> getStockOutBoxTube(@PathVariable Long id) {
        log.debug("REST request to get StockOutBoxTube : {}", id);
        StockOutBoxTubeDTO stockOutBoxTubeDTO = stockOutBoxTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutBoxTubeDTO));
    }

    /**
     * DELETE  /stock-out-box-tubes/:id : delete the "id" stockOutBoxTube.
     *
     * @param id the id of the stockOutBoxTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-box-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutBoxTube(@PathVariable Long id) {
        log.debug("REST request to delete StockOutBoxTube : {}", id);
        stockOutBoxTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据出库盒ID串取出库样本
     * @param ids
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-box-tubes/stockOutBox/{ids}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenTubeDataTableEntity> getPageStockOutPlan(@RequestBody DataTablesInput input, @PathVariable List<Long> ids) {
        DataTablesOutput<StockOutFrozenTubeDataTableEntity> result =stockOutBoxTubeService.getPageStockOutTubeByStockOutBoxIds(ids, input);
        return result;
    }
}
