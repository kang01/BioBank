package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockOutReqFrozenTube.
 */
@RestController
@RequestMapping("/api")
public class StockOutReqFrozenTubeResource {

    private final Logger log = LoggerFactory.getLogger(StockOutReqFrozenTubeResource.class);

    private static final String ENTITY_NAME = "stockOutReqFrozenTube";
        
    private final StockOutReqFrozenTubeService stockOutReqFrozenTubeService;

    public StockOutReqFrozenTubeResource(StockOutReqFrozenTubeService stockOutReqFrozenTubeService) {
        this.stockOutReqFrozenTubeService = stockOutReqFrozenTubeService;
    }

    /**
     * POST  /stock-out-req-frozen-tubes : Create a new stockOutReqFrozenTube.
     *
     * @param stockOutReqFrozenTubeDTO the stockOutReqFrozenTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutReqFrozenTubeDTO, or with status 400 (Bad Request) if the stockOutReqFrozenTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-req-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutReqFrozenTubeDTO> createStockOutReqFrozenTube(@Valid @RequestBody StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutReqFrozenTube : {}", stockOutReqFrozenTubeDTO);
        if (stockOutReqFrozenTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutReqFrozenTube cannot already have an ID")).body(null);
        }
        StockOutReqFrozenTubeDTO result = stockOutReqFrozenTubeService.save(stockOutReqFrozenTubeDTO);
        return ResponseEntity.created(new URI("/api/stock-out-req-frozen-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-req-frozen-tubes : Updates an existing stockOutReqFrozenTube.
     *
     * @param stockOutReqFrozenTubeDTO the stockOutReqFrozenTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutReqFrozenTubeDTO,
     * or with status 400 (Bad Request) if the stockOutReqFrozenTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutReqFrozenTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-req-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutReqFrozenTubeDTO> updateStockOutReqFrozenTube(@Valid @RequestBody StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutReqFrozenTube : {}", stockOutReqFrozenTubeDTO);
        if (stockOutReqFrozenTubeDTO.getId() == null) {
            return createStockOutReqFrozenTube(stockOutReqFrozenTubeDTO);
        }
        StockOutReqFrozenTubeDTO result = stockOutReqFrozenTubeService.save(stockOutReqFrozenTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutReqFrozenTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-req-frozen-tubes : get all the stockOutReqFrozenTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutReqFrozenTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-req-frozen-tubes")
    @Timed
    public ResponseEntity<List<StockOutReqFrozenTubeDTO>> getAllStockOutReqFrozenTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutReqFrozenTubes");
        Page<StockOutReqFrozenTubeDTO> page = stockOutReqFrozenTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-req-frozen-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-req-frozen-tubes/:id : get the "id" stockOutReqFrozenTube.
     *
     * @param id the id of the stockOutReqFrozenTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutReqFrozenTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-req-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<StockOutReqFrozenTubeDTO> getStockOutReqFrozenTube(@PathVariable Long id) {
        log.debug("REST request to get StockOutReqFrozenTube : {}", id);
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutReqFrozenTubeDTO));
    }

    /**
     * DELETE  /stock-out-req-frozen-tubes/:id : delete the "id" stockOutReqFrozenTube.
     *
     * @param id the id of the stockOutReqFrozenTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-req-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutReqFrozenTube(@PathVariable Long id) {
        log.debug("REST request to delete StockOutReqFrozenTube : {}", id);
        stockOutReqFrozenTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
