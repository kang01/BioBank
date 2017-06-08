package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.TranshipBoxPositionService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.TranshipBoxPositionDTO;
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
 * REST controller for managing TranshipBoxPosition.
 */
@RestController
@RequestMapping("/api")
public class TranshipBoxPositionResource {

    private final Logger log = LoggerFactory.getLogger(TranshipBoxPositionResource.class);

    private static final String ENTITY_NAME = "transhipBoxPosition";
        
    private final TranshipBoxPositionService transhipBoxPositionService;

    public TranshipBoxPositionResource(TranshipBoxPositionService transhipBoxPositionService) {
        this.transhipBoxPositionService = transhipBoxPositionService;
    }

    /**
     * POST  /tranship-box-positions : Create a new transhipBoxPosition.
     *
     * @param transhipBoxPositionDTO the transhipBoxPositionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipBoxPositionDTO, or with status 400 (Bad Request) if the transhipBoxPosition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranship-box-positions")
    @Timed
    public ResponseEntity<TranshipBoxPositionDTO> createTranshipBoxPosition(@Valid @RequestBody TranshipBoxPositionDTO transhipBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to save TranshipBoxPosition : {}", transhipBoxPositionDTO);
        if (transhipBoxPositionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transhipBoxPosition cannot already have an ID")).body(null);
        }
        TranshipBoxPositionDTO result = transhipBoxPositionService.save(transhipBoxPositionDTO);
        return ResponseEntity.created(new URI("/api/tranship-box-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranship-box-positions : Updates an existing transhipBoxPosition.
     *
     * @param transhipBoxPositionDTO the transhipBoxPositionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipBoxPositionDTO,
     * or with status 400 (Bad Request) if the transhipBoxPositionDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipBoxPositionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranship-box-positions")
    @Timed
    public ResponseEntity<TranshipBoxPositionDTO> updateTranshipBoxPosition(@Valid @RequestBody TranshipBoxPositionDTO transhipBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to update TranshipBoxPosition : {}", transhipBoxPositionDTO);
        if (transhipBoxPositionDTO.getId() == null) {
            return createTranshipBoxPosition(transhipBoxPositionDTO);
        }
        TranshipBoxPositionDTO result = transhipBoxPositionService.save(transhipBoxPositionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipBoxPositionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranship-box-positions : get all the transhipBoxPositions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transhipBoxPositions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranship-box-positions")
    @Timed
    public ResponseEntity<List<TranshipBoxPositionDTO>> getAllTranshipBoxPositions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TranshipBoxPositions");
        Page<TranshipBoxPositionDTO> page = transhipBoxPositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tranship-box-positions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tranship-box-positions/:id : get the "id" transhipBoxPosition.
     *
     * @param id the id of the transhipBoxPositionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipBoxPositionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tranship-box-positions/{id}")
    @Timed
    public ResponseEntity<TranshipBoxPositionDTO> getTranshipBoxPosition(@PathVariable Long id) {
        log.debug("REST request to get TranshipBoxPosition : {}", id);
        TranshipBoxPositionDTO transhipBoxPositionDTO = transhipBoxPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipBoxPositionDTO));
    }

    /**
     * DELETE  /tranship-box-positions/:id : delete the "id" transhipBoxPosition.
     *
     * @param id the id of the transhipBoxPositionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranship-box-positions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranshipBoxPosition(@PathVariable Long id) {
        log.debug("REST request to delete TranshipBoxPosition : {}", id);
        transhipBoxPositionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
