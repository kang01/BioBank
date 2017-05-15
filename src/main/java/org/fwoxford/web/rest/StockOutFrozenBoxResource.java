package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
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
 * REST controller for managing StockOutFrozenBox.
 */
@RestController
@RequestMapping("/api")
public class StockOutFrozenBoxResource {

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenBoxResource.class);

    private static final String ENTITY_NAME = "stockOutFrozenBox";
        
    private final StockOutFrozenBoxService stockOutFrozenBoxService;

    public StockOutFrozenBoxResource(StockOutFrozenBoxService stockOutFrozenBoxService) {
        this.stockOutFrozenBoxService = stockOutFrozenBoxService;
    }

    /**
     * POST  /stock-out-frozen-boxes : Create a new stockOutFrozenBox.
     *
     * @param stockOutFrozenBoxDTO the stockOutFrozenBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutFrozenBoxDTO, or with status 400 (Bad Request) if the stockOutFrozenBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-frozen-boxes")
    @Timed
    public ResponseEntity<StockOutFrozenBoxDTO> createStockOutFrozenBox(@Valid @RequestBody StockOutFrozenBoxDTO stockOutFrozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        if (stockOutFrozenBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutFrozenBox cannot already have an ID")).body(null);
        }
        StockOutFrozenBoxDTO result = stockOutFrozenBoxService.save(stockOutFrozenBoxDTO);
        return ResponseEntity.created(new URI("/api/stock-out-frozen-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-frozen-boxes : Updates an existing stockOutFrozenBox.
     *
     * @param stockOutFrozenBoxDTO the stockOutFrozenBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutFrozenBoxDTO,
     * or with status 400 (Bad Request) if the stockOutFrozenBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutFrozenBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-frozen-boxes")
    @Timed
    public ResponseEntity<StockOutFrozenBoxDTO> updateStockOutFrozenBox(@Valid @RequestBody StockOutFrozenBoxDTO stockOutFrozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        if (stockOutFrozenBoxDTO.getId() == null) {
            return createStockOutFrozenBox(stockOutFrozenBoxDTO);
        }
        StockOutFrozenBoxDTO result = stockOutFrozenBoxService.save(stockOutFrozenBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutFrozenBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-frozen-boxes : get all the stockOutFrozenBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutFrozenBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-frozen-boxes")
    @Timed
    public ResponseEntity<List<StockOutFrozenBoxDTO>> getAllStockOutFrozenBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutFrozenBoxes");
        Page<StockOutFrozenBoxDTO> page = stockOutFrozenBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-frozen-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-frozen-boxes/:id : get the "id" stockOutFrozenBox.
     *
     * @param id the id of the stockOutFrozenBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutFrozenBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-frozen-boxes/{id}")
    @Timed
    public ResponseEntity<StockOutFrozenBoxDTO> getStockOutFrozenBox(@PathVariable Long id) {
        log.debug("REST request to get StockOutFrozenBox : {}", id);
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutFrozenBoxDTO));
    }

    /**
     * DELETE  /stock-out-frozen-boxes/:id : delete the "id" stockOutFrozenBox.
     *
     * @param id the id of the stockOutFrozenBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-frozen-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutFrozenBox(@PathVariable Long id) {
        log.debug("REST request to delete StockOutFrozenBox : {}", id);
        stockOutFrozenBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
