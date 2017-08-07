package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.PositionChangeRecordService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.PositionChangeRecordDTO;
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
 * REST controller for managing PositionChangeRecord.
 */
@RestController
@RequestMapping("/api")
public class PositionChangeRecordResource {

    private final Logger log = LoggerFactory.getLogger(PositionChangeRecordResource.class);

    private static final String ENTITY_NAME = "positionChangeRecord";
        
    private final PositionChangeRecordService positionChangeRecordService;

    public PositionChangeRecordResource(PositionChangeRecordService positionChangeRecordService) {
        this.positionChangeRecordService = positionChangeRecordService;
    }

    /**
     * POST  /position-change-records : Create a new positionChangeRecord.
     *
     * @param positionChangeRecordDTO the positionChangeRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new positionChangeRecordDTO, or with status 400 (Bad Request) if the positionChangeRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/position-change-records")
    @Timed
    public ResponseEntity<PositionChangeRecordDTO> createPositionChangeRecord(@Valid @RequestBody PositionChangeRecordDTO positionChangeRecordDTO) throws URISyntaxException {
        log.debug("REST request to save PositionChangeRecord : {}", positionChangeRecordDTO);
        if (positionChangeRecordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionChangeRecord cannot already have an ID")).body(null);
        }
        PositionChangeRecordDTO result = positionChangeRecordService.save(positionChangeRecordDTO);
        return ResponseEntity.created(new URI("/api/position-change-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /position-change-records : Updates an existing positionChangeRecord.
     *
     * @param positionChangeRecordDTO the positionChangeRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated positionChangeRecordDTO,
     * or with status 400 (Bad Request) if the positionChangeRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the positionChangeRecordDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/position-change-records")
    @Timed
    public ResponseEntity<PositionChangeRecordDTO> updatePositionChangeRecord(@Valid @RequestBody PositionChangeRecordDTO positionChangeRecordDTO) throws URISyntaxException {
        log.debug("REST request to update PositionChangeRecord : {}", positionChangeRecordDTO);
        if (positionChangeRecordDTO.getId() == null) {
            return createPositionChangeRecord(positionChangeRecordDTO);
        }
        PositionChangeRecordDTO result = positionChangeRecordService.save(positionChangeRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, positionChangeRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /position-change-records : get all the positionChangeRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of positionChangeRecords in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/position-change-records")
    @Timed
    public ResponseEntity<List<PositionChangeRecordDTO>> getAllPositionChangeRecords(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PositionChangeRecords");
        Page<PositionChangeRecordDTO> page = positionChangeRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/position-change-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /position-change-records/:id : get the "id" positionChangeRecord.
     *
     * @param id the id of the positionChangeRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the positionChangeRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/position-change-records/{id}")
    @Timed
    public ResponseEntity<PositionChangeRecordDTO> getPositionChangeRecord(@PathVariable Long id) {
        log.debug("REST request to get PositionChangeRecord : {}", id);
        PositionChangeRecordDTO positionChangeRecordDTO = positionChangeRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(positionChangeRecordDTO));
    }

    /**
     * DELETE  /position-change-records/:id : delete the "id" positionChangeRecord.
     *
     * @param id the id of the positionChangeRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/position-change-records/{id}")
    @Timed
    public ResponseEntity<Void> deletePositionChangeRecord(@PathVariable Long id) {
        log.debug("REST request to delete PositionChangeRecord : {}", id);
        positionChangeRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
