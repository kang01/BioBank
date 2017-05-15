package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StockOutFilesService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutFilesDTO;
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
 * REST controller for managing StockOutFiles.
 */
@RestController
@RequestMapping("/api")
public class StockOutFilesResource {

    private final Logger log = LoggerFactory.getLogger(StockOutFilesResource.class);

    private static final String ENTITY_NAME = "stockOutFiles";
        
    private final StockOutFilesService stockOutFilesService;

    public StockOutFilesResource(StockOutFilesService stockOutFilesService) {
        this.stockOutFilesService = stockOutFilesService;
    }

    /**
     * POST  /stock-out-files : Create a new stockOutFiles.
     *
     * @param stockOutFilesDTO the stockOutFilesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutFilesDTO, or with status 400 (Bad Request) if the stockOutFiles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-files")
    @Timed
    public ResponseEntity<StockOutFilesDTO> createStockOutFiles(@Valid @RequestBody StockOutFilesDTO stockOutFilesDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutFiles : {}", stockOutFilesDTO);
        if (stockOutFilesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutFiles cannot already have an ID")).body(null);
        }
        StockOutFilesDTO result = stockOutFilesService.save(stockOutFilesDTO);
        return ResponseEntity.created(new URI("/api/stock-out-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-files : Updates an existing stockOutFiles.
     *
     * @param stockOutFilesDTO the stockOutFilesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutFilesDTO,
     * or with status 400 (Bad Request) if the stockOutFilesDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutFilesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-files")
    @Timed
    public ResponseEntity<StockOutFilesDTO> updateStockOutFiles(@Valid @RequestBody StockOutFilesDTO stockOutFilesDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutFiles : {}", stockOutFilesDTO);
        if (stockOutFilesDTO.getId() == null) {
            return createStockOutFiles(stockOutFilesDTO);
        }
        StockOutFilesDTO result = stockOutFilesService.save(stockOutFilesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutFilesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-files : get all the stockOutFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutFiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-files")
    @Timed
    public ResponseEntity<List<StockOutFilesDTO>> getAllStockOutFiles(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutFiles");
        Page<StockOutFilesDTO> page = stockOutFilesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-files/:id : get the "id" stockOutFiles.
     *
     * @param id the id of the stockOutFilesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutFilesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-files/{id}")
    @Timed
    public ResponseEntity<StockOutFilesDTO> getStockOutFiles(@PathVariable Long id) {
        log.debug("REST request to get StockOutFiles : {}", id);
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutFilesDTO));
    }

    /**
     * DELETE  /stock-out-files/:id : delete the "id" stockOutFiles.
     *
     * @param id the id of the stockOutFilesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutFiles(@PathVariable Long id) {
        log.debug("REST request to delete StockOutFiles : {}", id);
        stockOutFilesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
