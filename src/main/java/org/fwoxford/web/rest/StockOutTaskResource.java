package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutTaskService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutTaskDTO;
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
 * REST controller for managing StockOutTask.
 */
@RestController
@RequestMapping("/api")
public class StockOutTaskResource {

    private final Logger log = LoggerFactory.getLogger(StockOutTaskResource.class);

    private static final String ENTITY_NAME = "stockOutTask";
        
    private final StockOutTaskService stockOutTaskService;

    public StockOutTaskResource(StockOutTaskService stockOutTaskService) {
        this.stockOutTaskService = stockOutTaskService;
    }

    /**
     * POST  /stock-out-tasks : Create a new stockOutTask.
     *
     * @param stockOutTaskDTO the stockOutTaskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutTaskDTO, or with status 400 (Bad Request) if the stockOutTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-tasks")
    @Timed
    public ResponseEntity<StockOutTaskDTO> createStockOutTask(@Valid @RequestBody StockOutTaskDTO stockOutTaskDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutTask : {}", stockOutTaskDTO);
        if (stockOutTaskDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutTask cannot already have an ID")).body(null);
        }
        StockOutTaskDTO result = stockOutTaskService.save(stockOutTaskDTO);
        return ResponseEntity.created(new URI("/api/stock-out-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-tasks : Updates an existing stockOutTask.
     *
     * @param stockOutTaskDTO the stockOutTaskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutTaskDTO,
     * or with status 400 (Bad Request) if the stockOutTaskDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutTaskDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-tasks")
    @Timed
    public ResponseEntity<StockOutTaskDTO> updateStockOutTask(@Valid @RequestBody StockOutTaskDTO stockOutTaskDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutTask : {}", stockOutTaskDTO);
        if (stockOutTaskDTO.getId() == null) {
            return createStockOutTask(stockOutTaskDTO);
        }
        StockOutTaskDTO result = stockOutTaskService.save(stockOutTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-tasks : get all the stockOutTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutTasks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-tasks")
    @Timed
    public ResponseEntity<List<StockOutTaskDTO>> getAllStockOutTasks(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutTasks");
        Page<StockOutTaskDTO> page = stockOutTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-tasks/:id : get the "id" stockOutTask.
     *
     * @param id the id of the stockOutTaskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutTaskDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-tasks/{id}")
    @Timed
    public ResponseEntity<StockOutTaskDTO> getStockOutTask(@PathVariable Long id) {
        log.debug("REST request to get StockOutTask : {}", id);
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutTaskDTO));
    }

    /**
     * DELETE  /stock-out-tasks/:id : delete the "id" stockOutTask.
     *
     * @param id the id of the stockOutTaskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutTask(@PathVariable Long id) {
        log.debug("REST request to delete StockOutTask : {}", id);
        stockOutTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
