package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.service.StockOutTaskFrozenTubeService;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutTaskFrozenTubeDTO;
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
 * REST controller for managing StockOutTaskFrozenTube.
 */
@RestController
@RequestMapping("/api")
public class StockOutTaskFrozenTubeResource {

    private final Logger log = LoggerFactory.getLogger(StockOutTaskFrozenTubeResource.class);

    private static final String ENTITY_NAME = "stockOutTaskFrozenTube";

    private final StockOutTaskFrozenTubeService stockOutTaskFrozenTubeService;

    public StockOutTaskFrozenTubeResource(StockOutTaskFrozenTubeService stockOutTaskFrozenTubeService) {
        this.stockOutTaskFrozenTubeService = stockOutTaskFrozenTubeService;
    }

    /**
     * POST  /stock-out-task-frozen-tubes : Create a new stockOutTaskFrozenTube.
     *
     * @param stockOutTaskFrozenTubeDTO the stockOutTaskFrozenTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutTaskFrozenTubeDTO, or with status 400 (Bad Request) if the stockOutTaskFrozenTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-task-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutTaskFrozenTubeDTO> createStockOutTaskFrozenTube(@Valid @RequestBody StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutTaskFrozenTube : {}", stockOutTaskFrozenTubeDTO);
        if (stockOutTaskFrozenTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutTaskFrozenTube cannot already have an ID")).body(null);
        }
        StockOutTaskFrozenTubeDTO result = stockOutTaskFrozenTubeService.save(stockOutTaskFrozenTubeDTO);
        return ResponseEntity.created(new URI("/api/stock-out-task-frozen-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-task-frozen-tubes : Updates an existing stockOutTaskFrozenTube.
     *
     * @param stockOutTaskFrozenTubeDTO the stockOutTaskFrozenTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutTaskFrozenTubeDTO,
     * or with status 400 (Bad Request) if the stockOutTaskFrozenTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutTaskFrozenTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-task-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutTaskFrozenTubeDTO> updateStockOutTaskFrozenTube(@Valid @RequestBody StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutTaskFrozenTube : {}", stockOutTaskFrozenTubeDTO);
        if (stockOutTaskFrozenTubeDTO.getId() == null) {
            return createStockOutTaskFrozenTube(stockOutTaskFrozenTubeDTO);
        }
        StockOutTaskFrozenTubeDTO result = stockOutTaskFrozenTubeService.save(stockOutTaskFrozenTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutTaskFrozenTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-task-frozen-tubes : get all the stockOutTaskFrozenTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutTaskFrozenTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-task-frozen-tubes")
    @Timed
    public ResponseEntity<List<StockOutTaskFrozenTubeDTO>> getAllStockOutTaskFrozenTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutTaskFrozenTubes");
        Page<StockOutTaskFrozenTubeDTO> page = stockOutTaskFrozenTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-task-frozen-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-task-frozen-tubes/:id : get the "id" stockOutTaskFrozenTube.
     *
     * @param id the id of the stockOutTaskFrozenTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutTaskFrozenTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-task-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<StockOutTaskFrozenTubeDTO> getStockOutTaskFrozenTube(@PathVariable Long id) {
        log.debug("REST request to get StockOutTaskFrozenTube : {}", id);
        StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutTaskFrozenTubeDTO));
    }

    /**
     * DELETE  /stock-out-task-frozen-tubes/:id : delete the "id" stockOutTaskFrozenTube.
     *
     * @param id the id of the stockOutTaskFrozenTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-task-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutTaskFrozenTube(@PathVariable Long id) {
        log.debug("REST request to delete StockOutTaskFrozenTube : {}", id);
        stockOutTaskFrozenTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 异常保存
     * @param frozenTubeDTOS
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-task-frozen-tubes/abnormal")
    @Timed
    public ResponseEntity<List<FrozenTubeResponse>> abnormalStockOutTaskFrozenTube(@Valid @RequestBody List<FrozenTubeResponse> frozenTubeDTOS) throws URISyntaxException {
        log.debug("REST request to abnormal StockOutTaskFrozenTube : {}", frozenTubeDTOS);
        List<FrozenTubeResponse> result = stockOutTaskFrozenTubeService.abnormalStockOutTaskFrozenTube(frozenTubeDTOS);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenTubeDTOS.toString()))
            .body(result);
    }
    /**
     * 撤销保存
     * @param frozenTubeDTOS
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-task-frozen-tubes/repeal/task/{taskId}")
    @Timed
    public ResponseEntity<List<FrozenTubeResponse>> repealStockOutTaskFrozenTube(@Valid @RequestBody List<FrozenTubeResponse> frozenTubeDTOS,@PathVariable Long taskId) throws URISyntaxException {
        log.debug("REST request to repeal StockOutTaskFrozenTube : {}", frozenTubeDTOS);
        List<FrozenTubeResponse> result = stockOutTaskFrozenTubeService.repealStockOutTaskFrozenTube(frozenTubeDTOS,taskId);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenTubeDTOS.toString()))
            .body(result);
    }
    /**
     * 批注保存
     * @param frozenTubeDTOS
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-task-frozen-tubes/note")
    @Timed
    public ResponseEntity< List<FrozenTubeResponse>> noteStockOutTaskFrozenTube(@Valid @RequestBody List<FrozenTubeResponse> frozenTubeDTOS) throws URISyntaxException {
        log.debug("REST request to note StockOutTaskFrozenTube : {}", frozenTubeDTOS);
        List<FrozenTubeResponse> result = stockOutTaskFrozenTubeService.noteStockOutTaskFrozenTube(frozenTubeDTOS);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenTubeDTOS.toString()))
            .body(result);
    }
}
