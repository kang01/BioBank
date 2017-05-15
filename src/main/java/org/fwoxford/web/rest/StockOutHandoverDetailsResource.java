package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutHandoverDetailsService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutHandoverDetailsDTO;
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
 * REST controller for managing StockOutHandoverDetails.
 */
@RestController
@RequestMapping("/api")
public class StockOutHandoverDetailsResource {

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverDetailsResource.class);

    private static final String ENTITY_NAME = "stockOutHandoverDetails";
        
    private final StockOutHandoverDetailsService stockOutHandoverDetailsService;

    public StockOutHandoverDetailsResource(StockOutHandoverDetailsService stockOutHandoverDetailsService) {
        this.stockOutHandoverDetailsService = stockOutHandoverDetailsService;
    }

    /**
     * POST  /stock-out-handover-details : Create a new stockOutHandoverDetails.
     *
     * @param stockOutHandoverDetailsDTO the stockOutHandoverDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutHandoverDetailsDTO, or with status 400 (Bad Request) if the stockOutHandoverDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-handover-details")
    @Timed
    public ResponseEntity<StockOutHandoverDetailsDTO> createStockOutHandoverDetails(@Valid @RequestBody StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutHandoverDetails : {}", stockOutHandoverDetailsDTO);
        if (stockOutHandoverDetailsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutHandoverDetails cannot already have an ID")).body(null);
        }
        StockOutHandoverDetailsDTO result = stockOutHandoverDetailsService.save(stockOutHandoverDetailsDTO);
        return ResponseEntity.created(new URI("/api/stock-out-handover-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-handover-details : Updates an existing stockOutHandoverDetails.
     *
     * @param stockOutHandoverDetailsDTO the stockOutHandoverDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutHandoverDetailsDTO,
     * or with status 400 (Bad Request) if the stockOutHandoverDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutHandoverDetailsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-handover-details")
    @Timed
    public ResponseEntity<StockOutHandoverDetailsDTO> updateStockOutHandoverDetails(@Valid @RequestBody StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutHandoverDetails : {}", stockOutHandoverDetailsDTO);
        if (stockOutHandoverDetailsDTO.getId() == null) {
            return createStockOutHandoverDetails(stockOutHandoverDetailsDTO);
        }
        StockOutHandoverDetailsDTO result = stockOutHandoverDetailsService.save(stockOutHandoverDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutHandoverDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-handover-details : get all the stockOutHandoverDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutHandoverDetails in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-handover-details")
    @Timed
    public ResponseEntity<List<StockOutHandoverDetailsDTO>> getAllStockOutHandoverDetails(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutHandoverDetails");
        Page<StockOutHandoverDetailsDTO> page = stockOutHandoverDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-handover-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-handover-details/:id : get the "id" stockOutHandoverDetails.
     *
     * @param id the id of the stockOutHandoverDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutHandoverDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-handover-details/{id}")
    @Timed
    public ResponseEntity<StockOutHandoverDetailsDTO> getStockOutHandoverDetails(@PathVariable Long id) {
        log.debug("REST request to get StockOutHandoverDetails : {}", id);
        StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO = stockOutHandoverDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutHandoverDetailsDTO));
    }

    /**
     * DELETE  /stock-out-handover-details/:id : delete the "id" stockOutHandoverDetails.
     *
     * @param id the id of the stockOutHandoverDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-handover-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutHandoverDetails(@PathVariable Long id) {
        log.debug("REST request to delete StockOutHandoverDetails : {}", id);
        stockOutHandoverDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
