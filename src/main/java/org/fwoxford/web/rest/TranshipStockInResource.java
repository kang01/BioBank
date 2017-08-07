package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.TranshipStockInService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.TranshipStockInDTO;
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
 * REST controller for managing TranshipStockIn.
 */
@RestController
@RequestMapping("/api")
public class TranshipStockInResource {

    private final Logger log = LoggerFactory.getLogger(TranshipStockInResource.class);

    private static final String ENTITY_NAME = "transhipStockIn";
        
    private final TranshipStockInService transhipStockInService;

    public TranshipStockInResource(TranshipStockInService transhipStockInService) {
        this.transhipStockInService = transhipStockInService;
    }

    /**
     * POST  /tranship-stock-ins : Create a new transhipStockIn.
     *
     * @param transhipStockInDTO the transhipStockInDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipStockInDTO, or with status 400 (Bad Request) if the transhipStockIn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranship-stock-ins")
    @Timed
    public ResponseEntity<TranshipStockInDTO> createTranshipStockIn(@Valid @RequestBody TranshipStockInDTO transhipStockInDTO) throws URISyntaxException {
        log.debug("REST request to save TranshipStockIn : {}", transhipStockInDTO);
        if (transhipStockInDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transhipStockIn cannot already have an ID")).body(null);
        }
        TranshipStockInDTO result = transhipStockInService.save(transhipStockInDTO);
        return ResponseEntity.created(new URI("/api/tranship-stock-ins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranship-stock-ins : Updates an existing transhipStockIn.
     *
     * @param transhipStockInDTO the transhipStockInDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipStockInDTO,
     * or with status 400 (Bad Request) if the transhipStockInDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipStockInDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranship-stock-ins")
    @Timed
    public ResponseEntity<TranshipStockInDTO> updateTranshipStockIn(@Valid @RequestBody TranshipStockInDTO transhipStockInDTO) throws URISyntaxException {
        log.debug("REST request to update TranshipStockIn : {}", transhipStockInDTO);
        if (transhipStockInDTO.getId() == null) {
            return createTranshipStockIn(transhipStockInDTO);
        }
        TranshipStockInDTO result = transhipStockInService.save(transhipStockInDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipStockInDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranship-stock-ins : get all the transhipStockIns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transhipStockIns in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranship-stock-ins")
    @Timed
    public ResponseEntity<List<TranshipStockInDTO>> getAllTranshipStockIns(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TranshipStockIns");
        Page<TranshipStockInDTO> page = transhipStockInService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tranship-stock-ins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tranship-stock-ins/:id : get the "id" transhipStockIn.
     *
     * @param id the id of the transhipStockInDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipStockInDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tranship-stock-ins/{id}")
    @Timed
    public ResponseEntity<TranshipStockInDTO> getTranshipStockIn(@PathVariable Long id) {
        log.debug("REST request to get TranshipStockIn : {}", id);
        TranshipStockInDTO transhipStockInDTO = transhipStockInService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipStockInDTO));
    }

    /**
     * DELETE  /tranship-stock-ins/:id : delete the "id" transhipStockIn.
     *
     * @param id the id of the transhipStockInDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranship-stock-ins/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranshipStockIn(@PathVariable Long id) {
        log.debug("REST request to delete TranshipStockIn : {}", id);
        transhipStockInService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
