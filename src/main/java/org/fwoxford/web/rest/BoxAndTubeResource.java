package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.BoxAndTubeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.BoxAndTubeDTO;
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
 * REST controller for managing BoxAndTube.
 */
@RestController
@RequestMapping("/api")
public class BoxAndTubeResource {

    private final Logger log = LoggerFactory.getLogger(BoxAndTubeResource.class);

    private static final String ENTITY_NAME = "boxAndTube";
        
    private final BoxAndTubeService boxAndTubeService;

    public BoxAndTubeResource(BoxAndTubeService boxAndTubeService) {
        this.boxAndTubeService = boxAndTubeService;
    }

    /**
     * POST  /box-and-tubes : Create a new boxAndTube.
     *
     * @param boxAndTubeDTO the boxAndTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boxAndTubeDTO, or with status 400 (Bad Request) if the boxAndTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/box-and-tubes")
    @Timed
    public ResponseEntity<BoxAndTubeDTO> createBoxAndTube(@Valid @RequestBody BoxAndTubeDTO boxAndTubeDTO) throws URISyntaxException {
        log.debug("REST request to save BoxAndTube : {}", boxAndTubeDTO);
        if (boxAndTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new boxAndTube cannot already have an ID")).body(null);
        }
        BoxAndTubeDTO result = boxAndTubeService.save(boxAndTubeDTO);
        return ResponseEntity.created(new URI("/api/box-and-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /box-and-tubes : Updates an existing boxAndTube.
     *
     * @param boxAndTubeDTO the boxAndTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boxAndTubeDTO,
     * or with status 400 (Bad Request) if the boxAndTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the boxAndTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/box-and-tubes")
    @Timed
    public ResponseEntity<BoxAndTubeDTO> updateBoxAndTube(@Valid @RequestBody BoxAndTubeDTO boxAndTubeDTO) throws URISyntaxException {
        log.debug("REST request to update BoxAndTube : {}", boxAndTubeDTO);
        if (boxAndTubeDTO.getId() == null) {
            return createBoxAndTube(boxAndTubeDTO);
        }
        BoxAndTubeDTO result = boxAndTubeService.save(boxAndTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boxAndTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /box-and-tubes : get all the boxAndTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of boxAndTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/box-and-tubes")
    @Timed
    public ResponseEntity<List<BoxAndTubeDTO>> getAllBoxAndTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BoxAndTubes");
        Page<BoxAndTubeDTO> page = boxAndTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/box-and-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /box-and-tubes/:id : get the "id" boxAndTube.
     *
     * @param id the id of the boxAndTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boxAndTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/box-and-tubes/{id}")
    @Timed
    public ResponseEntity<BoxAndTubeDTO> getBoxAndTube(@PathVariable Long id) {
        log.debug("REST request to get BoxAndTube : {}", id);
        BoxAndTubeDTO boxAndTubeDTO = boxAndTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(boxAndTubeDTO));
    }

    /**
     * DELETE  /box-and-tubes/:id : delete the "id" boxAndTube.
     *
     * @param id the id of the boxAndTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/box-and-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoxAndTube(@PathVariable Long id) {
        log.debug("REST request to delete BoxAndTube : {}", id);
        boxAndTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
