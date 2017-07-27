package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockInTranshipBoxService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockInTranshipBoxDTO;
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
 * REST controller for managing StockInTranshipBox.
 */
@RestController
@RequestMapping("/api")
public class StockInTranshipBoxResource {

    private final Logger log = LoggerFactory.getLogger(StockInTranshipBoxResource.class);

    private static final String ENTITY_NAME = "stockInTranshipBox";
        
    private final StockInTranshipBoxService stockInTranshipBoxService;

    public StockInTranshipBoxResource(StockInTranshipBoxService stockInTranshipBoxService) {
        this.stockInTranshipBoxService = stockInTranshipBoxService;
    }

    /**
     * POST  /stock-in-tranship-boxes : Create a new stockInTranshipBox.
     *
     * @param stockInTranshipBoxDTO the stockInTranshipBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockInTranshipBoxDTO, or with status 400 (Bad Request) if the stockInTranshipBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-in-tranship-boxes")
    @Timed
    public ResponseEntity<StockInTranshipBoxDTO> createStockInTranshipBox(@Valid @RequestBody StockInTranshipBoxDTO stockInTranshipBoxDTO) throws URISyntaxException {
        log.debug("REST request to save StockInTranshipBox : {}", stockInTranshipBoxDTO);
        if (stockInTranshipBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockInTranshipBox cannot already have an ID")).body(null);
        }
        StockInTranshipBoxDTO result = stockInTranshipBoxService.save(stockInTranshipBoxDTO);
        return ResponseEntity.created(new URI("/api/stock-in-tranship-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-in-tranship-boxes : Updates an existing stockInTranshipBox.
     *
     * @param stockInTranshipBoxDTO the stockInTranshipBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockInTranshipBoxDTO,
     * or with status 400 (Bad Request) if the stockInTranshipBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockInTranshipBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-in-tranship-boxes")
    @Timed
    public ResponseEntity<StockInTranshipBoxDTO> updateStockInTranshipBox(@Valid @RequestBody StockInTranshipBoxDTO stockInTranshipBoxDTO) throws URISyntaxException {
        log.debug("REST request to update StockInTranshipBox : {}", stockInTranshipBoxDTO);
        if (stockInTranshipBoxDTO.getId() == null) {
            return createStockInTranshipBox(stockInTranshipBoxDTO);
        }
        StockInTranshipBoxDTO result = stockInTranshipBoxService.save(stockInTranshipBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInTranshipBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-in-tranship-boxes : get all the stockInTranshipBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockInTranshipBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-in-tranship-boxes")
    @Timed
    public ResponseEntity<List<StockInTranshipBoxDTO>> getAllStockInTranshipBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockInTranshipBoxes");
        Page<StockInTranshipBoxDTO> page = stockInTranshipBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-in-tranship-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-in-tranship-boxes/:id : get the "id" stockInTranshipBox.
     *
     * @param id the id of the stockInTranshipBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockInTranshipBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-in-tranship-boxes/{id}")
    @Timed
    public ResponseEntity<StockInTranshipBoxDTO> getStockInTranshipBox(@PathVariable Long id) {
        log.debug("REST request to get StockInTranshipBox : {}", id);
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInTranshipBoxDTO));
    }

    /**
     * DELETE  /stock-in-tranship-boxes/:id : delete the "id" stockInTranshipBox.
     *
     * @param id the id of the stockInTranshipBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-in-tranship-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockInTranshipBox(@PathVariable Long id) {
        log.debug("REST request to delete StockInTranshipBox : {}", id);
        stockInTranshipBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
