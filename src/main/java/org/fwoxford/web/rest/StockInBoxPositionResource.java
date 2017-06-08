package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockInBoxPositionService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockInBoxPositionDTO;
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
 * REST controller for managing StockInBoxPosition.
 */
@RestController
@RequestMapping("/api")
public class StockInBoxPositionResource {

    private final Logger log = LoggerFactory.getLogger(StockInBoxPositionResource.class);

    private static final String ENTITY_NAME = "stockInBoxPosition";
        
    private final StockInBoxPositionService stockInBoxPositionService;

    public StockInBoxPositionResource(StockInBoxPositionService stockInBoxPositionService) {
        this.stockInBoxPositionService = stockInBoxPositionService;
    }

    /**
     * POST  /stock-in-box-positions : Create a new stockInBoxPosition.
     *
     * @param stockInBoxPositionDTO the stockInBoxPositionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockInBoxPositionDTO, or with status 400 (Bad Request) if the stockInBoxPosition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-in-box-positions")
    @Timed
    public ResponseEntity<StockInBoxPositionDTO> createStockInBoxPosition(@Valid @RequestBody StockInBoxPositionDTO stockInBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to save StockInBoxPosition : {}", stockInBoxPositionDTO);
        if (stockInBoxPositionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockInBoxPosition cannot already have an ID")).body(null);
        }
        StockInBoxPositionDTO result = stockInBoxPositionService.save(stockInBoxPositionDTO);
        return ResponseEntity.created(new URI("/api/stock-in-box-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-in-box-positions : Updates an existing stockInBoxPosition.
     *
     * @param stockInBoxPositionDTO the stockInBoxPositionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockInBoxPositionDTO,
     * or with status 400 (Bad Request) if the stockInBoxPositionDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockInBoxPositionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-in-box-positions")
    @Timed
    public ResponseEntity<StockInBoxPositionDTO> updateStockInBoxPosition(@Valid @RequestBody StockInBoxPositionDTO stockInBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to update StockInBoxPosition : {}", stockInBoxPositionDTO);
        if (stockInBoxPositionDTO.getId() == null) {
            return createStockInBoxPosition(stockInBoxPositionDTO);
        }
        StockInBoxPositionDTO result = stockInBoxPositionService.save(stockInBoxPositionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInBoxPositionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-in-box-positions : get all the stockInBoxPositions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockInBoxPositions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-in-box-positions")
    @Timed
    public ResponseEntity<List<StockInBoxPositionDTO>> getAllStockInBoxPositions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockInBoxPositions");
        Page<StockInBoxPositionDTO> page = stockInBoxPositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-in-box-positions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-in-box-positions/:id : get the "id" stockInBoxPosition.
     *
     * @param id the id of the stockInBoxPositionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockInBoxPositionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-in-box-positions/{id}")
    @Timed
    public ResponseEntity<StockInBoxPositionDTO> getStockInBoxPosition(@PathVariable Long id) {
        log.debug("REST request to get StockInBoxPosition : {}", id);
        StockInBoxPositionDTO stockInBoxPositionDTO = stockInBoxPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInBoxPositionDTO));
    }

    /**
     * DELETE  /stock-in-box-positions/:id : delete the "id" stockInBoxPosition.
     *
     * @param id the id of the stockInBoxPositionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-in-box-positions/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockInBoxPosition(@PathVariable Long id) {
        log.debug("REST request to delete StockInBoxPosition : {}", id);
        stockInBoxPositionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
