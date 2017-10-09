package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutHandoverBoxService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutHandoverBoxDTO;
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
 * REST controller for managing StockOutHandoverBox.
 */
@RestController
@RequestMapping("/api")
public class StockOutHandoverBoxResource {

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverBoxResource.class);

    private static final String ENTITY_NAME = "stockOutHandoverBox";
        
    private final StockOutHandoverBoxService stockOutHandoverBoxService;

    public StockOutHandoverBoxResource(StockOutHandoverBoxService stockOutHandoverBoxService) {
        this.stockOutHandoverBoxService = stockOutHandoverBoxService;
    }

    /**
     * POST  /stock-out-handover-boxes : Create a new stockOutHandoverBox.
     *
     * @param stockOutHandoverBoxDTO the stockOutHandoverBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutHandoverBoxDTO, or with status 400 (Bad Request) if the stockOutHandoverBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-handover-boxes")
    @Timed
    public ResponseEntity<StockOutHandoverBoxDTO> createStockOutHandoverBox(@Valid @RequestBody StockOutHandoverBoxDTO stockOutHandoverBoxDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutHandoverBox : {}", stockOutHandoverBoxDTO);
        if (stockOutHandoverBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutHandoverBox cannot already have an ID")).body(null);
        }
        StockOutHandoverBoxDTO result = stockOutHandoverBoxService.save(stockOutHandoverBoxDTO);
        return ResponseEntity.created(new URI("/api/stock-out-handover-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-handover-boxes : Updates an existing stockOutHandoverBox.
     *
     * @param stockOutHandoverBoxDTO the stockOutHandoverBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutHandoverBoxDTO,
     * or with status 400 (Bad Request) if the stockOutHandoverBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutHandoverBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-handover-boxes")
    @Timed
    public ResponseEntity<StockOutHandoverBoxDTO> updateStockOutHandoverBox(@Valid @RequestBody StockOutHandoverBoxDTO stockOutHandoverBoxDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutHandoverBox : {}", stockOutHandoverBoxDTO);
        if (stockOutHandoverBoxDTO.getId() == null) {
            return createStockOutHandoverBox(stockOutHandoverBoxDTO);
        }
        StockOutHandoverBoxDTO result = stockOutHandoverBoxService.save(stockOutHandoverBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutHandoverBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-handover-boxes : get all the stockOutHandoverBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutHandoverBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-handover-boxes")
    @Timed
    public ResponseEntity<List<StockOutHandoverBoxDTO>> getAllStockOutHandoverBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutHandoverBoxes");
        Page<StockOutHandoverBoxDTO> page = stockOutHandoverBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-handover-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-handover-boxes/:id : get the "id" stockOutHandoverBox.
     *
     * @param id the id of the stockOutHandoverBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutHandoverBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-handover-boxes/{id}")
    @Timed
    public ResponseEntity<StockOutHandoverBoxDTO> getStockOutHandoverBox(@PathVariable Long id) {
        log.debug("REST request to get StockOutHandoverBox : {}", id);
        StockOutHandoverBoxDTO stockOutHandoverBoxDTO = stockOutHandoverBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutHandoverBoxDTO));
    }

    /**
     * DELETE  /stock-out-handover-boxes/:id : delete the "id" stockOutHandoverBox.
     *
     * @param id the id of the stockOutHandoverBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-handover-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutHandoverBox(@PathVariable Long id) {
        log.debug("REST request to delete StockOutHandoverBox : {}", id);
        stockOutHandoverBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
