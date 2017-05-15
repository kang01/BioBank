package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutBoxPositionService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutBoxPositionDTO;
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
 * REST controller for managing StockOutBoxPosition.
 */
@RestController
@RequestMapping("/api")
public class StockOutBoxPositionResource {

    private final Logger log = LoggerFactory.getLogger(StockOutBoxPositionResource.class);

    private static final String ENTITY_NAME = "stockOutBoxPosition";
        
    private final StockOutBoxPositionService stockOutBoxPositionService;

    public StockOutBoxPositionResource(StockOutBoxPositionService stockOutBoxPositionService) {
        this.stockOutBoxPositionService = stockOutBoxPositionService;
    }

    /**
     * POST  /stock-out-box-positions : Create a new stockOutBoxPosition.
     *
     * @param stockOutBoxPositionDTO the stockOutBoxPositionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutBoxPositionDTO, or with status 400 (Bad Request) if the stockOutBoxPosition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-box-positions")
    @Timed
    public ResponseEntity<StockOutBoxPositionDTO> createStockOutBoxPosition(@Valid @RequestBody StockOutBoxPositionDTO stockOutBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutBoxPosition : {}", stockOutBoxPositionDTO);
        if (stockOutBoxPositionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutBoxPosition cannot already have an ID")).body(null);
        }
        StockOutBoxPositionDTO result = stockOutBoxPositionService.save(stockOutBoxPositionDTO);
        return ResponseEntity.created(new URI("/api/stock-out-box-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-box-positions : Updates an existing stockOutBoxPosition.
     *
     * @param stockOutBoxPositionDTO the stockOutBoxPositionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutBoxPositionDTO,
     * or with status 400 (Bad Request) if the stockOutBoxPositionDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutBoxPositionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-box-positions")
    @Timed
    public ResponseEntity<StockOutBoxPositionDTO> updateStockOutBoxPosition(@Valid @RequestBody StockOutBoxPositionDTO stockOutBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutBoxPosition : {}", stockOutBoxPositionDTO);
        if (stockOutBoxPositionDTO.getId() == null) {
            return createStockOutBoxPosition(stockOutBoxPositionDTO);
        }
        StockOutBoxPositionDTO result = stockOutBoxPositionService.save(stockOutBoxPositionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutBoxPositionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-box-positions : get all the stockOutBoxPositions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutBoxPositions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-box-positions")
    @Timed
    public ResponseEntity<List<StockOutBoxPositionDTO>> getAllStockOutBoxPositions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutBoxPositions");
        Page<StockOutBoxPositionDTO> page = stockOutBoxPositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-box-positions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-box-positions/:id : get the "id" stockOutBoxPosition.
     *
     * @param id the id of the stockOutBoxPositionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutBoxPositionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-box-positions/{id}")
    @Timed
    public ResponseEntity<StockOutBoxPositionDTO> getStockOutBoxPosition(@PathVariable Long id) {
        log.debug("REST request to get StockOutBoxPosition : {}", id);
        StockOutBoxPositionDTO stockOutBoxPositionDTO = stockOutBoxPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutBoxPositionDTO));
    }

    /**
     * DELETE  /stock-out-box-positions/:id : delete the "id" stockOutBoxPosition.
     *
     * @param id the id of the stockOutBoxPositionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-box-positions/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutBoxPosition(@PathVariable Long id) {
        log.debug("REST request to delete StockOutBoxPosition : {}", id);
        stockOutBoxPositionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
