package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.config.Constants;
import org.fwoxford.service.PositionDestroyService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.PositionDestroyDTO;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PositionDestroy.
 */
@RestController
@RequestMapping("/api")
public class PositionDestroyResource {

    private final Logger log = LoggerFactory.getLogger(PositionDestroyResource.class);

    private static final String ENTITY_NAME = "positionDestroy";

    private final PositionDestroyService positionDestroyService;

    public PositionDestroyResource(PositionDestroyService positionDestroyService) {
        this.positionDestroyService = positionDestroyService;
    }

    /**
     * POST  /position-destroys : Create a new positionDestroy.
     *
     * @param positionDestroyDTO the positionDestroyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new positionDestroyDTO, or with status 400 (Bad Request) if the positionDestroy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/position-destroys")
    @Timed
    public ResponseEntity<PositionDestroyDTO> createPositionDestroy(@Valid @RequestBody PositionDestroyDTO positionDestroyDTO) throws URISyntaxException {
        log.debug("REST request to save PositionDestroy : {}", positionDestroyDTO);
        if (positionDestroyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionDestroy cannot already have an ID")).body(null);
        }
        PositionDestroyDTO result = positionDestroyService.save(positionDestroyDTO);
        return ResponseEntity.created(new URI("/api/position-destroys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /position-destroys : Updates an existing positionDestroy.
     *
     * @param positionDestroyDTO the positionDestroyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated positionDestroyDTO,
     * or with status 400 (Bad Request) if the positionDestroyDTO is not valid,
     * or with status 500 (Internal Server Error) if the positionDestroyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/position-destroys")
    @Timed
    public ResponseEntity<PositionDestroyDTO> updatePositionDestroy(@Valid @RequestBody PositionDestroyDTO positionDestroyDTO) throws URISyntaxException {
        log.debug("REST request to update PositionDestroy : {}", positionDestroyDTO);
        if (positionDestroyDTO.getId() == null) {
            return createPositionDestroy(positionDestroyDTO);
        }
        PositionDestroyDTO result = positionDestroyService.save(positionDestroyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, positionDestroyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /position-destroys : get all the positionDestroys.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of positionDestroys in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/position-destroys")
    @Timed
    public ResponseEntity<List<PositionDestroyDTO>> getAllPositionDestroys(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PositionDestroys");
        Page<PositionDestroyDTO> page = positionDestroyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/position-destroys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /position-destroys/:id : get the "id" positionDestroy.
     *
     * @param id the id of the positionDestroyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the positionDestroyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/position-destroys/{id}")
    @Timed
    public ResponseEntity<PositionDestroyDTO> getPositionDestroy(@PathVariable Long id) {
        log.debug("REST request to get PositionDestroy : {}", id);
        PositionDestroyDTO positionDestroyDTO = positionDestroyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(positionDestroyDTO));
    }

    /**
     * DELETE  /position-destroys/:id : delete the "id" positionDestroy.
     *
     * @param id the id of the positionDestroyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/position-destroys/{id}")
    @Timed
    public ResponseEntity<Void> deletePositionDestroy(@PathVariable Long id) {
        log.debug("REST request to delete PositionDestroy : {}", id);
        positionDestroyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * 样本销毁
     * @param positionDestroyDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-destroys/forSample")
    @Timed
    public ResponseEntity<PositionDestroyDTO> createPositionDestroyForSample(@Valid @RequestBody PositionDestroyDTO positionDestroyDTO) throws URISyntaxException {
        log.debug("REST request to save PositionChange : {}", positionDestroyDTO);
        if (positionDestroyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionDestroy cannot already have an ID")).body(null);
        }
        PositionDestroyDTO result = positionDestroyService.createDestroyPosition(positionDestroyDTO, Constants.MOVE_TYPE_FOR_TUBE);
        return ResponseEntity.created(new URI("/api/position-destroys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 冻存盒销毁
     * @param positionDestroyDTO
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/position-destroys/forBox")
    @Timed
    public ResponseEntity<PositionDestroyDTO> createPositionDestroyForBox(@Valid @RequestBody PositionDestroyDTO positionDestroyDTO) throws URISyntaxException {
        log.debug("REST request to save PositionChange : {}", positionDestroyDTO);
        if (positionDestroyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionDestroy cannot already have an ID")).body(null);
        }
        PositionDestroyDTO result = positionDestroyService.createDestroyPosition(positionDestroyDTO, Constants.MOVE_TYPE_FOR_BOX);
        return ResponseEntity.created(new URI("/api/position-destroys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
