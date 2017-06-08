package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockInTubeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockInTubeDTO;
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
 * REST controller for managing StockInTube.
 */
@RestController
@RequestMapping("/api")
public class StockInTubeResource {

    private final Logger log = LoggerFactory.getLogger(StockInTubeResource.class);

    private static final String ENTITY_NAME = "stockInTube";
        
    private final StockInTubeService stockInTubeService;

    public StockInTubeResource(StockInTubeService stockInTubeService) {
        this.stockInTubeService = stockInTubeService;
    }

    /**
     * POST  /stock-in-tubes : Create a new stockInTube.
     *
     * @param stockInTubeDTO the stockInTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockInTubeDTO, or with status 400 (Bad Request) if the stockInTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-in-tubes")
    @Timed
    public ResponseEntity<StockInTubeDTO> createStockInTube(@Valid @RequestBody StockInTubeDTO stockInTubeDTO) throws URISyntaxException {
        log.debug("REST request to save StockInTube : {}", stockInTubeDTO);
        if (stockInTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockInTube cannot already have an ID")).body(null);
        }
        StockInTubeDTO result = stockInTubeService.save(stockInTubeDTO);
        return ResponseEntity.created(new URI("/api/stock-in-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-in-tubes : Updates an existing stockInTube.
     *
     * @param stockInTubeDTO the stockInTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockInTubeDTO,
     * or with status 400 (Bad Request) if the stockInTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockInTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-in-tubes")
    @Timed
    public ResponseEntity<StockInTubeDTO> updateStockInTube(@Valid @RequestBody StockInTubeDTO stockInTubeDTO) throws URISyntaxException {
        log.debug("REST request to update StockInTube : {}", stockInTubeDTO);
        if (stockInTubeDTO.getId() == null) {
            return createStockInTube(stockInTubeDTO);
        }
        StockInTubeDTO result = stockInTubeService.save(stockInTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-in-tubes : get all the stockInTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockInTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-in-tubes")
    @Timed
    public ResponseEntity<List<StockInTubeDTO>> getAllStockInTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockInTubes");
        Page<StockInTubeDTO> page = stockInTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-in-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-in-tubes/:id : get the "id" stockInTube.
     *
     * @param id the id of the stockInTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockInTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-in-tubes/{id}")
    @Timed
    public ResponseEntity<StockInTubeDTO> getStockInTube(@PathVariable Long id) {
        log.debug("REST request to get StockInTube : {}", id);
        StockInTubeDTO stockInTubeDTO = stockInTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInTubeDTO));
    }

    /**
     * DELETE  /stock-in-tubes/:id : delete the "id" stockInTube.
     *
     * @param id the id of the stockInTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-in-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockInTube(@PathVariable Long id) {
        log.debug("REST request to delete StockInTube : {}", id);
        stockInTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
