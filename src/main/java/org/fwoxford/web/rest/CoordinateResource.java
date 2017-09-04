package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.CoordinateService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.CoordinateDTO;
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
 * REST controller for managing Coordinate.
 */
@RestController
@RequestMapping("/api")
public class CoordinateResource {

    private final Logger log = LoggerFactory.getLogger(CoordinateResource.class);

    private static final String ENTITY_NAME = "coordinate";
        
    private final CoordinateService coordinateService;

    public CoordinateResource(CoordinateService coordinateService) {
        this.coordinateService = coordinateService;
    }

    /**
     * POST  /coordinates : Create a new coordinate.
     *
     * @param coordinateDTO the coordinateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coordinateDTO, or with status 400 (Bad Request) if the coordinate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coordinates")
    @Timed
    public ResponseEntity<CoordinateDTO> createCoordinate(@Valid @RequestBody CoordinateDTO coordinateDTO) throws URISyntaxException {
        log.debug("REST request to save Coordinate : {}", coordinateDTO);
        if (coordinateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new coordinate cannot already have an ID")).body(null);
        }
        CoordinateDTO result = coordinateService.save(coordinateDTO);
        return ResponseEntity.created(new URI("/api/coordinates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coordinates : Updates an existing coordinate.
     *
     * @param coordinateDTO the coordinateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coordinateDTO,
     * or with status 400 (Bad Request) if the coordinateDTO is not valid,
     * or with status 500 (Internal Server Error) if the coordinateDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coordinates")
    @Timed
    public ResponseEntity<CoordinateDTO> updateCoordinate(@Valid @RequestBody CoordinateDTO coordinateDTO) throws URISyntaxException {
        log.debug("REST request to update Coordinate : {}", coordinateDTO);
        if (coordinateDTO.getId() == null) {
            return createCoordinate(coordinateDTO);
        }
        CoordinateDTO result = coordinateService.save(coordinateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coordinateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coordinates : get all the coordinates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coordinates in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/coordinates")
    @Timed
    public ResponseEntity<List<CoordinateDTO>> getAllCoordinates(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Coordinates");
        Page<CoordinateDTO> page = coordinateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coordinates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coordinates/:id : get the "id" coordinate.
     *
     * @param id the id of the coordinateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coordinateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coordinates/{id}")
    @Timed
    public ResponseEntity<CoordinateDTO> getCoordinate(@PathVariable Long id) {
        log.debug("REST request to get Coordinate : {}", id);
        CoordinateDTO coordinateDTO = coordinateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coordinateDTO));
    }

    /**
     * DELETE  /coordinates/:id : delete the "id" coordinate.
     *
     * @param id the id of the coordinateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coordinates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoordinate(@PathVariable Long id) {
        log.debug("REST request to delete Coordinate : {}", id);
        coordinateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
