package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.PositionMoveRecordService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.PositionMoveRecordDTO;
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
 * REST controller for managing PositionMoveRecord.
 */
@RestController
@RequestMapping("/api")
public class PositionMoveRecordResource {

    private final Logger log = LoggerFactory.getLogger(PositionMoveRecordResource.class);

    private static final String ENTITY_NAME = "positionMoveRecord";
        
    private final PositionMoveRecordService positionMoveRecordService;

    public PositionMoveRecordResource(PositionMoveRecordService positionMoveRecordService) {
        this.positionMoveRecordService = positionMoveRecordService;
    }

    /**
     * POST  /position-move-records : Create a new positionMoveRecord.
     *
     * @param positionMoveRecordDTO the positionMoveRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new positionMoveRecordDTO, or with status 400 (Bad Request) if the positionMoveRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/position-move-records")
    @Timed
    public ResponseEntity<PositionMoveRecordDTO> createPositionMoveRecord(@Valid @RequestBody PositionMoveRecordDTO positionMoveRecordDTO) throws URISyntaxException {
        log.debug("REST request to save PositionMoveRecord : {}", positionMoveRecordDTO);
        if (positionMoveRecordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionMoveRecord cannot already have an ID")).body(null);
        }
        PositionMoveRecordDTO result = positionMoveRecordService.save(positionMoveRecordDTO);
        return ResponseEntity.created(new URI("/api/position-move-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /position-move-records : Updates an existing positionMoveRecord.
     *
     * @param positionMoveRecordDTO the positionMoveRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated positionMoveRecordDTO,
     * or with status 400 (Bad Request) if the positionMoveRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the positionMoveRecordDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/position-move-records")
    @Timed
    public ResponseEntity<PositionMoveRecordDTO> updatePositionMoveRecord(@Valid @RequestBody PositionMoveRecordDTO positionMoveRecordDTO) throws URISyntaxException {
        log.debug("REST request to update PositionMoveRecord : {}", positionMoveRecordDTO);
        if (positionMoveRecordDTO.getId() == null) {
            return createPositionMoveRecord(positionMoveRecordDTO);
        }
        PositionMoveRecordDTO result = positionMoveRecordService.save(positionMoveRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, positionMoveRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /position-move-records : get all the positionMoveRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of positionMoveRecords in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/position-move-records")
    @Timed
    public ResponseEntity<List<PositionMoveRecordDTO>> getAllPositionMoveRecords(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PositionMoveRecords");
        Page<PositionMoveRecordDTO> page = positionMoveRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/position-move-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /position-move-records/:id : get the "id" positionMoveRecord.
     *
     * @param id the id of the positionMoveRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the positionMoveRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/position-move-records/{id}")
    @Timed
    public ResponseEntity<PositionMoveRecordDTO> getPositionMoveRecord(@PathVariable Long id) {
        log.debug("REST request to get PositionMoveRecord : {}", id);
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(positionMoveRecordDTO));
    }

    /**
     * DELETE  /position-move-records/:id : delete the "id" positionMoveRecord.
     *
     * @param id the id of the positionMoveRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/position-move-records/{id}")
    @Timed
    public ResponseEntity<Void> deletePositionMoveRecord(@PathVariable Long id) {
        log.debug("REST request to delete PositionMoveRecord : {}", id);
        positionMoveRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
