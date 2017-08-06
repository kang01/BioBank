package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.config.Constants;
import org.fwoxford.service.PositionChangeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.PositionChangeDTO;
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
 * REST controller for managing PositionChange.
 */
@RestController
@RequestMapping("/api")
public class PositionChangeResource {

    private final Logger log = LoggerFactory.getLogger(PositionChangeResource.class);

    private static final String ENTITY_NAME = "positionChange";

    private final PositionChangeService positionChangeService;

    public PositionChangeResource(PositionChangeService positionChangeService) {
        this.positionChangeService = positionChangeService;
    }

    /**
     * POST  /position-changes : Create a new positionChange.
     *
     * @param positionChangeDTO the positionChangeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new positionChangeDTO, or with status 400 (Bad Request) if the positionChange has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/position-changes")
    @Timed
    public ResponseEntity<PositionChangeDTO> createPositionChange(@Valid @RequestBody PositionChangeDTO positionChangeDTO) throws URISyntaxException {
        log.debug("REST request to save PositionChange : {}", positionChangeDTO);
        if (positionChangeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionChange cannot already have an ID")).body(null);
        }
        PositionChangeDTO result = positionChangeService.save(positionChangeDTO);
        return ResponseEntity.created(new URI("/api/position-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /position-changes : Updates an existing positionChange.
     *
     * @param positionChangeDTO the positionChangeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated positionChangeDTO,
     * or with status 400 (Bad Request) if the positionChangeDTO is not valid,
     * or with status 500 (Internal Server Error) if the positionChangeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/position-changes")
    @Timed
    public ResponseEntity<PositionChangeDTO> updatePositionChange(@Valid @RequestBody PositionChangeDTO positionChangeDTO) throws URISyntaxException {
        log.debug("REST request to update PositionChange : {}", positionChangeDTO);
        if (positionChangeDTO.getId() == null) {
            return createPositionChange(positionChangeDTO);
        }
        PositionChangeDTO result = positionChangeService.save(positionChangeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, positionChangeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /position-changes : get all the positionChanges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of positionChanges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/position-changes")
    @Timed
    public ResponseEntity<List<PositionChangeDTO>> getAllPositionChanges(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PositionChanges");
        Page<PositionChangeDTO> page = positionChangeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/position-changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /position-changes/:id : get the "id" positionChange.
     *
     * @param id the id of the positionChangeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the positionChangeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/position-changes/{id}")
    @Timed
    public ResponseEntity<PositionChangeDTO> getPositionChange(@PathVariable Long id) {
        log.debug("REST request to get PositionChange : {}", id);
        PositionChangeDTO positionChangeDTO = positionChangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(positionChangeDTO));
    }

    /**
     * DELETE  /position-changes/:id : delete the "id" positionChange.
     *
     * @param id the id of the positionChangeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/position-changes/{id}")
    @Timed
    public ResponseEntity<Void> deletePositionChange(@PathVariable Long id) {
        log.debug("REST request to delete PositionChange : {}", id);
        positionChangeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 样本换位
     * @param positionChangeDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-changes/forSample")
    @Timed
    public ResponseEntity<PositionChangeDTO> createPositionChangeForSample(@Valid @RequestBody PositionChangeDTO positionChangeDTO) throws URISyntaxException {
        log.debug("REST request to save PositionChange : {}", positionChangeDTO);
        if (positionChangeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMove cannot already have an ID")).body(null);
        }
        PositionChangeDTO result = positionChangeService.createChangePosition(positionChangeDTO, Constants.MOVE_TYPE_1);
        return ResponseEntity.created(new URI("/api/position-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 冻存盒换位
     * @param positionChangeDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-changes/forBox")
    @Timed
    public ResponseEntity<PositionChangeDTO> createPositionChangeForBox(@Valid @RequestBody PositionChangeDTO positionChangeDTO) throws URISyntaxException {
        log.debug("REST request to save PositionChange : {}", positionChangeDTO);
        if (positionChangeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMove cannot already have an ID")).body(null);
        }
        PositionChangeDTO result = positionChangeService.createChangePosition(positionChangeDTO, Constants.MOVE_TYPE_2);
        return ResponseEntity.created(new URI("/api/position-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 冻存架换位
     * @param positionChangeDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-changes/forShelf")
    @Timed
    public ResponseEntity<PositionChangeDTO> createPositionChangeForShelf(@Valid @RequestBody PositionChangeDTO positionChangeDTO) throws URISyntaxException {
        log.debug("REST request to save PositionChange : {}", positionChangeDTO);
        if (positionChangeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMove cannot already have an ID")).body(null);
        }
        PositionChangeDTO result = positionChangeService.createChangePosition(positionChangeDTO, Constants.MOVE_TYPE_3);
        return ResponseEntity.created(new URI("/api/position-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
