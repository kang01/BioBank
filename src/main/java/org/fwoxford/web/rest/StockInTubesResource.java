package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockInTubesService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockInTubesDTO;
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
 * REST controller for managing StockInTubes.
 */
@RestController
@RequestMapping("/api")
public class StockInTubesResource {

    private final Logger log = LoggerFactory.getLogger(StockInTubesResource.class);

    private static final String ENTITY_NAME = "stockInTubes";

    private final StockInTubesService stockInTubesService;

    public StockInTubesResource(StockInTubesService stockInTubesService) {
        this.stockInTubesService = stockInTubesService;
    }

    /**
     * POST  /stock-in-tubes : Create a new stockInTubes.
     *
     * @param stockInTubesDTO the stockInTubesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockInTubesDTO, or with status 400 (Bad Request) if the stockInTubes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-in-tube")
    @Timed
    public ResponseEntity<StockInTubesDTO> createStockInTubes(@Valid @RequestBody StockInTubesDTO stockInTubesDTO) throws URISyntaxException {
        log.debug("REST request to save StockInTubes : {}", stockInTubesDTO);
        if (stockInTubesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockInTubes cannot already have an ID")).body(null);
        }
        StockInTubesDTO result = stockInTubesService.save(stockInTubesDTO);
        return ResponseEntity.created(new URI("/api/stock-in-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-in-tubes : Updates an existing stockInTubes.
     *
     * @param stockInTubesDTO the stockInTubesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockInTubesDTO,
     * or with status 400 (Bad Request) if the stockInTubesDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockInTubesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-in-tube")
    @Timed
    public ResponseEntity<StockInTubesDTO> updateStockInTubes(@Valid @RequestBody StockInTubesDTO stockInTubesDTO) throws URISyntaxException {
        log.debug("REST request to update StockInTubes : {}", stockInTubesDTO);
        if (stockInTubesDTO.getId() == null) {
            return createStockInTubes(stockInTubesDTO);
        }
        StockInTubesDTO result = stockInTubesService.save(stockInTubesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInTubesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-in-tubes : get all the stockInTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockInTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-in-tube")
    @Timed
    public ResponseEntity<List<StockInTubesDTO>> getAllStockInTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockInTubes");
        Page<StockInTubesDTO> page = stockInTubesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-in-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-in-tubes/:id : get the "id" stockInTubes.
     *
     * @param id the id of the stockInTubesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockInTubesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-in-tube/{id}")
    @Timed
    public ResponseEntity<StockInTubesDTO> getStockInTubes(@PathVariable Long id) {
        log.debug("REST request to get StockInTubes : {}", id);
        StockInTubesDTO stockInTubesDTO = stockInTubesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInTubesDTO));
    }

    /**
     * DELETE  /stock-in-tubes/:id : delete the "id" stockInTubes.
     *
     * @param id the id of the stockInTubesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-in-tube/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockInTubes(@PathVariable Long id) {
        log.debug("REST request to delete StockInTubes : {}", id);
        stockInTubesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
