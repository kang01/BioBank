package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutFrozenTubeService;
import org.fwoxford.service.dto.response.StockOutFrozenTubeForPlan;
import org.fwoxford.web.rest.util.BankUtil;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutFrozenTubeDTO;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockOutFrozenTube.
 */
@RestController
@RequestMapping("/api")
public class StockOutFrozenTubeResource {

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenTubeResource.class);

    private static final String ENTITY_NAME = "stockOutFrozenTube";

    private final StockOutFrozenTubeService stockOutFrozenTubeService;

    public StockOutFrozenTubeResource(StockOutFrozenTubeService stockOutFrozenTubeService) {
        this.stockOutFrozenTubeService = stockOutFrozenTubeService;
    }

    /**
     * POST  /stock-out-frozen-tubes : Create a new stockOutFrozenTube.
     *
     * @param stockOutFrozenTubeDTO the stockOutFrozenTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutFrozenTubeDTO, or with status 400 (Bad Request) if the stockOutFrozenTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutFrozenTubeDTO> createStockOutFrozenTube(@Valid @RequestBody StockOutFrozenTubeDTO stockOutFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutFrozenTube : {}", stockOutFrozenTubeDTO);
        if (stockOutFrozenTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutFrozenTube cannot already have an ID")).body(null);
        }
        StockOutFrozenTubeDTO result = stockOutFrozenTubeService.save(stockOutFrozenTubeDTO);
        return ResponseEntity.created(new URI("/api/stock-out-frozen-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-frozen-tubes : Updates an existing stockOutFrozenTube.
     *
     * @param stockOutFrozenTubeDTO the stockOutFrozenTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutFrozenTubeDTO,
     * or with status 400 (Bad Request) if the stockOutFrozenTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutFrozenTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-frozen-tubes")
    @Timed
    public ResponseEntity<StockOutFrozenTubeDTO> updateStockOutFrozenTube(@Valid @RequestBody StockOutFrozenTubeDTO stockOutFrozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutFrozenTube : {}", stockOutFrozenTubeDTO);
        if (stockOutFrozenTubeDTO.getId() == null) {
            return createStockOutFrozenTube(stockOutFrozenTubeDTO);
        }
        StockOutFrozenTubeDTO result = stockOutFrozenTubeService.save(stockOutFrozenTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutFrozenTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-frozen-tubes : get all the stockOutFrozenTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutFrozenTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-frozen-tubes")
    @Timed
    public ResponseEntity<List<StockOutFrozenTubeDTO>> getAllStockOutFrozenTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutFrozenTubes");
        Page<StockOutFrozenTubeDTO> page = stockOutFrozenTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-frozen-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-frozen-tubes/:id : get the "id" stockOutFrozenTube.
     *
     * @param id the id of the stockOutFrozenTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutFrozenTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<StockOutFrozenTubeDTO> getStockOutFrozenTube(@PathVariable Long id) {
        log.debug("REST request to get StockOutFrozenTube : {}", id);
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutFrozenTubeDTO));
    }

    /**
     * DELETE  /stock-out-frozen-tubes/:id : delete the "id" stockOutFrozenTube.
     *
     * @param id the id of the stockOutFrozenTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-frozen-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutFrozenTube(@PathVariable Long id) {
        log.debug("REST request to delete StockOutFrozenTube : {}", id);
        stockOutFrozenTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * 根据出库申请ID以及冻存盒ID查询出库的样本
     * @param applyId
     * @param frozenBoxId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-frozen-tubes/apply/{applyId}/frozenBox/{frozenBoxId}")
    @Timed
    public ResponseEntity<List<StockOutFrozenTubeForPlan>> getStockOutFrozenTubeForPlanByApplyAndBox(@PathVariable Long applyId,@PathVariable Long frozenBoxId) throws URISyntaxException {
        List<StockOutFrozenTubeForPlan> result = stockOutFrozenTubeService.getStockOutFrozenTubeForPlanByApplyAndBox(applyId,frozenBoxId);
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    /**
     * 根据出库需求ID以及冻存盒ID查询出库的样本
     * @param requirementIds
     * @param frozenBoxId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-frozen-tubes/requirements/{requirementIds}/frozenBox/{frozenBoxId}")
    @Timed
    public ResponseEntity<List<StockOutFrozenTubeForPlan>> getStockOutFrozenTubeForPlanByRequirementAndBox(@PathVariable List<Long> requirementIds,@PathVariable Long frozenBoxId) throws URISyntaxException {
        List<StockOutFrozenTubeForPlan> result = stockOutFrozenTubeService.getStockOutFrozenTubeForPlanByRequirementAndBox(requirementIds,frozenBoxId);
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
