package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutApplyProjectService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutApplyProjectDTO;
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
 * REST controller for managing StockOutApplyProject.
 */
@RestController
@RequestMapping("/api")
public class StockOutApplyProjectResource {

    private final Logger log = LoggerFactory.getLogger(StockOutApplyProjectResource.class);

    private static final String ENTITY_NAME = "stockOutApplyProject";
        
    private final StockOutApplyProjectService stockOutApplyProjectService;

    public StockOutApplyProjectResource(StockOutApplyProjectService stockOutApplyProjectService) {
        this.stockOutApplyProjectService = stockOutApplyProjectService;
    }

    /**
     * POST  /stock-out-apply-projects : Create a new stockOutApplyProject.
     *
     * @param stockOutApplyProjectDTO the stockOutApplyProjectDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutApplyProjectDTO, or with status 400 (Bad Request) if the stockOutApplyProject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-apply-projects")
    @Timed
    public ResponseEntity<StockOutApplyProjectDTO> createStockOutApplyProject(@Valid @RequestBody StockOutApplyProjectDTO stockOutApplyProjectDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutApplyProject : {}", stockOutApplyProjectDTO);
        if (stockOutApplyProjectDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutApplyProject cannot already have an ID")).body(null);
        }
        StockOutApplyProjectDTO result = stockOutApplyProjectService.save(stockOutApplyProjectDTO);
        return ResponseEntity.created(new URI("/api/stock-out-apply-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-apply-projects : Updates an existing stockOutApplyProject.
     *
     * @param stockOutApplyProjectDTO the stockOutApplyProjectDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutApplyProjectDTO,
     * or with status 400 (Bad Request) if the stockOutApplyProjectDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutApplyProjectDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-apply-projects")
    @Timed
    public ResponseEntity<StockOutApplyProjectDTO> updateStockOutApplyProject(@Valid @RequestBody StockOutApplyProjectDTO stockOutApplyProjectDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutApplyProject : {}", stockOutApplyProjectDTO);
        if (stockOutApplyProjectDTO.getId() == null) {
            return createStockOutApplyProject(stockOutApplyProjectDTO);
        }
        StockOutApplyProjectDTO result = stockOutApplyProjectService.save(stockOutApplyProjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutApplyProjectDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-apply-projects : get all the stockOutApplyProjects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutApplyProjects in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-apply-projects")
    @Timed
    public ResponseEntity<List<StockOutApplyProjectDTO>> getAllStockOutApplyProjects(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutApplyProjects");
        Page<StockOutApplyProjectDTO> page = stockOutApplyProjectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-apply-projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-apply-projects/:id : get the "id" stockOutApplyProject.
     *
     * @param id the id of the stockOutApplyProjectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutApplyProjectDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-apply-projects/{id}")
    @Timed
    public ResponseEntity<StockOutApplyProjectDTO> getStockOutApplyProject(@PathVariable Long id) {
        log.debug("REST request to get StockOutApplyProject : {}", id);
        StockOutApplyProjectDTO stockOutApplyProjectDTO = stockOutApplyProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutApplyProjectDTO));
    }

    /**
     * DELETE  /stock-out-apply-projects/:id : delete the "id" stockOutApplyProject.
     *
     * @param id the id of the stockOutApplyProjectDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-apply-projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutApplyProject(@PathVariable Long id) {
        log.debug("REST request to delete StockOutApplyProject : {}", id);
        stockOutApplyProjectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
