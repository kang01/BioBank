package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.PositionMoveService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.PositionMoveDTO;
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
 * REST controller for managing PositionMove.
 */
@RestController
@RequestMapping("/api")
public class PositionMoveResource {

    private final Logger log = LoggerFactory.getLogger(PositionMoveResource.class);

    private static final String ENTITY_NAME = "positionMove";

    private final PositionMoveService positionMoveService;

    public PositionMoveResource(PositionMoveService positionMoveService) {
        this.positionMoveService = positionMoveService;
    }

    /**
     * POST  /position-moves : Create a new positionMove.
     *
     * @param positionMoveDTO the positionMoveDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new positionMoveDTO, or with status 400 (Bad Request) if the positionMove has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/position-moves")
    @Timed
    public ResponseEntity<PositionMoveDTO> createPositionMove(@Valid @RequestBody PositionMoveDTO positionMoveDTO) throws URISyntaxException {
        log.debug("REST request to save PositionMove : {}", positionMoveDTO);
        if (positionMoveDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMove cannot already have an ID")).body(null);
        }
        PositionMoveDTO result = positionMoveService.save(positionMoveDTO);
        return ResponseEntity.created(new URI("/api/position-moves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /position-moves : Updates an existing positionMove.
     *
     * @param positionMoveDTO the positionMoveDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated positionMoveDTO,
     * or with status 400 (Bad Request) if the positionMoveDTO is not valid,
     * or with status 500 (Internal Server Error) if the positionMoveDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/position-moves")
    @Timed
    public ResponseEntity<PositionMoveDTO> updatePositionMove(@Valid @RequestBody PositionMoveDTO positionMoveDTO) throws URISyntaxException {
        log.debug("REST request to update PositionMove : {}", positionMoveDTO);
        if (positionMoveDTO.getId() == null) {
            return createPositionMove(positionMoveDTO);
        }
        PositionMoveDTO result = positionMoveService.save(positionMoveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, positionMoveDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /position-moves : get all the positionMoves.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of positionMoves in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/position-moves")
    @Timed
    public ResponseEntity<List<PositionMoveDTO>> getAllPositionMoves(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PositionMoves");
        Page<PositionMoveDTO> page = positionMoveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/position-moves");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /position-moves/:id : get the "id" positionMove.
     *
     * @param id the id of the positionMoveDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the positionMoveDTO, or with status 404 (Not Found)
     */
    @GetMapping("/position-moves/{id}")
    @Timed
    public ResponseEntity<PositionMoveDTO> getPositionMove(@PathVariable Long id) {
        log.debug("REST request to get PositionMove : {}", id);
        PositionMoveDTO positionMoveDTO = positionMoveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(positionMoveDTO));
    }

    /**
     * DELETE  /position-moves/:id : delete the "id" positionMove.
     *
     * @param id the id of the positionMoveDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/position-moves/{id}")
    @Timed
    public ResponseEntity<Void> deletePositionMove(@PathVariable Long id) {
        log.debug("REST request to delete PositionMove : {}", id);
        positionMoveService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 样本移位
     * @param positionMoveDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-moves/forSample")
    @Timed
    public ResponseEntity<PositionMoveDTO> createPositionMoveForSample(@Valid @RequestBody PositionMoveDTO positionMoveDTO) throws URISyntaxException {
        log.debug("REST request to save PositionMove : {}", positionMoveDTO);
        if (positionMoveDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMove cannot already have an ID")).body(null);
        }
        PositionMoveDTO result = positionMoveService.moveSamplePosition(positionMoveDTO);
        return ResponseEntity.created(new URI("/api/position-moves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/position-moves/forSample")
    @Timed
    public ResponseEntity<PositionMoveDTO> updatePositionMoveForSample(@Valid @RequestBody PositionMoveDTO positionMoveDTO) throws URISyntaxException {
        log.debug("REST request to save PositionMove : {}", positionMoveDTO);
        if (positionMoveDTO.getId() == null) {
            return createPositionMoveForSample(positionMoveDTO);
        }
        PositionMoveDTO result = positionMoveService.moveSamplePosition(positionMoveDTO);
        return ResponseEntity.created(new URI("/api/position-moves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * 冻存盒移位
     * @param positionMoveDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-moves/forBox")
    @Timed
    public ResponseEntity<PositionMoveDTO> createPositionMoveForBox(@Valid @RequestBody PositionMoveDTO positionMoveDTO) throws URISyntaxException {
        log.debug("REST request to save PositionMove : {}", positionMoveDTO);
        if (positionMoveDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMove cannot already have an ID")).body(null);
        }
        PositionMoveDTO result = positionMoveService.save(positionMoveDTO);
        return ResponseEntity.created(new URI("/api/position-moves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 冻存架移位
     * @param positionMoveDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-moves/forShelf")
    @Timed
    public ResponseEntity<PositionMoveDTO> createPositionMoveForShelf(@Valid @RequestBody PositionMoveDTO positionMoveDTO) throws URISyntaxException {
        log.debug("REST request to save PositionMove : {}", positionMoveDTO);
        if (positionMoveDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMove cannot already have an ID")).body(null);
        }
        PositionMoveDTO result = positionMoveService.save(positionMoveDTO);
        return ResponseEntity.created(new URI("/api/position-moves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
