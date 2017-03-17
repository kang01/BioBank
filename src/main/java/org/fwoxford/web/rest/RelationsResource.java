package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.RelationsService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.RelationsDTO;
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
 * REST controller for managing Relations.
 */
@RestController
@RequestMapping("/api")
public class RelationsResource {

    private final Logger log = LoggerFactory.getLogger(RelationsResource.class);

    private static final String ENTITY_NAME = "relations";
        
    private final RelationsService relationsService;

    public RelationsResource(RelationsService relationsService) {
        this.relationsService = relationsService;
    }

    /**
     * POST  /relations : Create a new relations.
     *
     * @param relationsDTO the relationsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relationsDTO, or with status 400 (Bad Request) if the relations has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/relations")
    @Timed
    public ResponseEntity<RelationsDTO> createRelations(@Valid @RequestBody RelationsDTO relationsDTO) throws URISyntaxException {
        log.debug("REST request to save Relations : {}", relationsDTO);
        if (relationsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new relations cannot already have an ID")).body(null);
        }
        RelationsDTO result = relationsService.save(relationsDTO);
        return ResponseEntity.created(new URI("/api/relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /relations : Updates an existing relations.
     *
     * @param relationsDTO the relationsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relationsDTO,
     * or with status 400 (Bad Request) if the relationsDTO is not valid,
     * or with status 500 (Internal Server Error) if the relationsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/relations")
    @Timed
    public ResponseEntity<RelationsDTO> updateRelations(@Valid @RequestBody RelationsDTO relationsDTO) throws URISyntaxException {
        log.debug("REST request to update Relations : {}", relationsDTO);
        if (relationsDTO.getId() == null) {
            return createRelations(relationsDTO);
        }
        RelationsDTO result = relationsService.save(relationsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, relationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /relations : get all the relations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of relations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/relations")
    @Timed
    public ResponseEntity<List<RelationsDTO>> getAllRelations(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Relations");
        Page<RelationsDTO> page = relationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/relations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /relations/:id : get the "id" relations.
     *
     * @param id the id of the relationsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relationsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/relations/{id}")
    @Timed
    public ResponseEntity<RelationsDTO> getRelations(@PathVariable Long id) {
        log.debug("REST request to get Relations : {}", id);
        RelationsDTO relationsDTO = relationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(relationsDTO));
    }

    /**
     * DELETE  /relations/:id : delete the "id" relations.
     *
     * @param id the id of the relationsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/relations/{id}")
    @Timed
    public ResponseEntity<Void> deleteRelations(@PathVariable Long id) {
        log.debug("REST request to delete Relations : {}", id);
        relationsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
