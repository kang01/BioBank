package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.FrozenBoxPositionService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.FrozenBoxPositionDTO;
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
 * REST controller for managing FrozenBoxPosition.
 */
@RestController
@RequestMapping("/api")
public class FrozenBoxPositionResource {

    private final Logger log = LoggerFactory.getLogger(FrozenBoxPositionResource.class);

    private static final String ENTITY_NAME = "frozenBoxPosition";
        
    private final FrozenBoxPositionService frozenBoxPositionService;

    public FrozenBoxPositionResource(FrozenBoxPositionService frozenBoxPositionService) {
        this.frozenBoxPositionService = frozenBoxPositionService;
    }

    /**
     * POST  /frozen-box-positions : Create a new frozenBoxPosition.
     *
     * @param frozenBoxPositionDTO the frozenBoxPositionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frozenBoxPositionDTO, or with status 400 (Bad Request) if the frozenBoxPosition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frozen-box-positions")
    @Timed
    public ResponseEntity<FrozenBoxPositionDTO> createFrozenBoxPosition(@Valid @RequestBody FrozenBoxPositionDTO frozenBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to save FrozenBoxPosition : {}", frozenBoxPositionDTO);
        if (frozenBoxPositionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frozenBoxPosition cannot already have an ID")).body(null);
        }
        FrozenBoxPositionDTO result = frozenBoxPositionService.save(frozenBoxPositionDTO);
        return ResponseEntity.created(new URI("/api/frozen-box-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frozen-box-positions : Updates an existing frozenBoxPosition.
     *
     * @param frozenBoxPositionDTO the frozenBoxPositionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frozenBoxPositionDTO,
     * or with status 400 (Bad Request) if the frozenBoxPositionDTO is not valid,
     * or with status 500 (Internal Server Error) if the frozenBoxPositionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frozen-box-positions")
    @Timed
    public ResponseEntity<FrozenBoxPositionDTO> updateFrozenBoxPosition(@Valid @RequestBody FrozenBoxPositionDTO frozenBoxPositionDTO) throws URISyntaxException {
        log.debug("REST request to update FrozenBoxPosition : {}", frozenBoxPositionDTO);
        if (frozenBoxPositionDTO.getId() == null) {
            return createFrozenBoxPosition(frozenBoxPositionDTO);
        }
        FrozenBoxPositionDTO result = frozenBoxPositionService.save(frozenBoxPositionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenBoxPositionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frozen-box-positions : get all the frozenBoxPositions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frozenBoxPositions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/frozen-box-positions")
    @Timed
    public ResponseEntity<List<FrozenBoxPositionDTO>> getAllFrozenBoxPositions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FrozenBoxPositions");
        Page<FrozenBoxPositionDTO> page = frozenBoxPositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frozen-box-positions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frozen-box-positions/:id : get the "id" frozenBoxPosition.
     *
     * @param id the id of the frozenBoxPositionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frozenBoxPositionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-box-positions/{id}")
    @Timed
    public ResponseEntity<FrozenBoxPositionDTO> getFrozenBoxPosition(@PathVariable Long id) {
        log.debug("REST request to get FrozenBoxPosition : {}", id);
        FrozenBoxPositionDTO frozenBoxPositionDTO = frozenBoxPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenBoxPositionDTO));
    }

    /**
     * DELETE  /frozen-box-positions/:id : delete the "id" frozenBoxPosition.
     *
     * @param id the id of the frozenBoxPositionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frozen-box-positions/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrozenBoxPosition(@PathVariable Long id) {
        log.debug("REST request to delete FrozenBoxPosition : {}", id);
        frozenBoxPositionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
