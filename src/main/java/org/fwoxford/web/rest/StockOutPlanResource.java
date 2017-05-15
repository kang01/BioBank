package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutPlanService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutPlanDTO;
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
 * REST controller for managing StockOutPlan.
 */
@RestController
@RequestMapping("/api")
public class StockOutPlanResource {

    private final Logger log = LoggerFactory.getLogger(StockOutPlanResource.class);

    private static final String ENTITY_NAME = "stockOutPlan";
        
    private final StockOutPlanService stockOutPlanService;

    public StockOutPlanResource(StockOutPlanService stockOutPlanService) {
        this.stockOutPlanService = stockOutPlanService;
    }

    /**
     * POST  /stock-out-plans : Create a new stockOutPlan.
     *
     * @param stockOutPlanDTO the stockOutPlanDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutPlanDTO, or with status 400 (Bad Request) if the stockOutPlan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-plans")
    @Timed
    public ResponseEntity<StockOutPlanDTO> createStockOutPlan(@Valid @RequestBody StockOutPlanDTO stockOutPlanDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutPlan : {}", stockOutPlanDTO);
        if (stockOutPlanDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutPlan cannot already have an ID")).body(null);
        }
        StockOutPlanDTO result = stockOutPlanService.save(stockOutPlanDTO);
        return ResponseEntity.created(new URI("/api/stock-out-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-plans : Updates an existing stockOutPlan.
     *
     * @param stockOutPlanDTO the stockOutPlanDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutPlanDTO,
     * or with status 400 (Bad Request) if the stockOutPlanDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutPlanDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-plans")
    @Timed
    public ResponseEntity<StockOutPlanDTO> updateStockOutPlan(@Valid @RequestBody StockOutPlanDTO stockOutPlanDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutPlan : {}", stockOutPlanDTO);
        if (stockOutPlanDTO.getId() == null) {
            return createStockOutPlan(stockOutPlanDTO);
        }
        StockOutPlanDTO result = stockOutPlanService.save(stockOutPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutPlanDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-plans : get all the stockOutPlans.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutPlans in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-plans")
    @Timed
    public ResponseEntity<List<StockOutPlanDTO>> getAllStockOutPlans(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutPlans");
        Page<StockOutPlanDTO> page = stockOutPlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-plans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-plans/:id : get the "id" stockOutPlan.
     *
     * @param id the id of the stockOutPlanDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutPlanDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-plans/{id}")
    @Timed
    public ResponseEntity<StockOutPlanDTO> getStockOutPlan(@PathVariable Long id) {
        log.debug("REST request to get StockOutPlan : {}", id);
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutPlanDTO));
    }

    /**
     * DELETE  /stock-out-plans/:id : delete the "id" stockOutPlan.
     *
     * @param id the id of the stockOutPlanDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-plans/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutPlan(@PathVariable Long id) {
        log.debug("REST request to delete StockOutPlan : {}", id);
        stockOutPlanService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
