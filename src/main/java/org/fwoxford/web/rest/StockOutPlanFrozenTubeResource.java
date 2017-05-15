package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutPlanFrozenTubeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutPlanFrozenTubeDTO;
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
 * REST controller for managing StockOutPlanFrozenTube.
 */
@RestController
@RequestMapping("/api")
public class StockOutPlanFrozenTubeResource {

    private final Logger log = LoggerFactory.getLogger(StockOutPlanFrozenTubeResource.class);

    private static final String ENTITY_NAME = "stockOutPlanFrozenTube";
        
    private final StockOutPlanFrozenTubeService stockOutPlanFrozenTubeService;

    public StockOutPlanFrozenTubeResource(StockOutPlanFrozenTubeService stockOutPlanFrozenTubeService) {
        this.stockOutPlanFrozenTubeService = stockOutPlanFrozenTubeService;
    }

    /**
     * POST  /stock-out-plan-frozen-tubes : Create a new stockOutPlanFrozenTube.
     *
     * @param stockOutPlanFrozenTubeDTO the stockOutPlanFrozenTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutPlanFrozenTubeDTO, or with status 400 (Bad Request) if the stockOutPlanFrozenTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-plan-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutPlanFrozenTubeDTO> createStockOutPlanFrozenTube(@Valid @RequestBody StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutPlanFrozenTube : {}", stockOutPlanFrozenTubeDTO);
        if (stockOutPlanFrozenTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutPlanFrozenTube cannot already have an ID")).body(null);
        }
        StockOutPlanFrozenTubeDTO result = stockOutPlanFrozenTubeService.save(stockOutPlanFrozenTubeDTO);
        return ResponseEntity.created(new URI("/api/stock-out-plan-frozen-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-plan-frozen-tubes : Updates an existing stockOutPlanFrozenTube.
     *
     * @param stockOutPlanFrozenTubeDTO the stockOutPlanFrozenTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutPlanFrozenTubeDTO,
     * or with status 400 (Bad Request) if the stockOutPlanFrozenTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutPlanFrozenTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-plan-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutPlanFrozenTubeDTO> updateStockOutPlanFrozenTube(@Valid @RequestBody StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutPlanFrozenTube : {}", stockOutPlanFrozenTubeDTO);
        if (stockOutPlanFrozenTubeDTO.getId() == null) {
            return createStockOutPlanFrozenTube(stockOutPlanFrozenTubeDTO);
        }
        StockOutPlanFrozenTubeDTO result = stockOutPlanFrozenTubeService.save(stockOutPlanFrozenTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutPlanFrozenTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-plan-frozen-tubes : get all the stockOutPlanFrozenTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutPlanFrozenTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-plan-frozen-tubes")
    @Timed
    public ResponseEntity<List<StockOutPlanFrozenTubeDTO>> getAllStockOutPlanFrozenTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutPlanFrozenTubes");
        Page<StockOutPlanFrozenTubeDTO> page = stockOutPlanFrozenTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-plan-frozen-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-plan-frozen-tubes/:id : get the "id" stockOutPlanFrozenTube.
     *
     * @param id the id of the stockOutPlanFrozenTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutPlanFrozenTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-plan-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<StockOutPlanFrozenTubeDTO> getStockOutPlanFrozenTube(@PathVariable Long id) {
        log.debug("REST request to get StockOutPlanFrozenTube : {}", id);
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutPlanFrozenTubeDTO));
    }

    /**
     * DELETE  /stock-out-plan-frozen-tubes/:id : delete the "id" stockOutPlanFrozenTube.
     *
     * @param id the id of the stockOutPlanFrozenTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-plan-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutPlanFrozenTube(@PathVariable Long id) {
        log.debug("REST request to delete StockOutPlanFrozenTube : {}", id);
        stockOutPlanFrozenTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
