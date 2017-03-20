package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.FrozenTubeRecordService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.FrozenTubeRecordDTO;
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
 * REST controller for managing FrozenTubeRecord.
 */
@RestController
@RequestMapping("/api")
public class FrozenTubeRecordResource {

    private final Logger log = LoggerFactory.getLogger(FrozenTubeRecordResource.class);

    private static final String ENTITY_NAME = "frozenTubeRecord";
        
    private final FrozenTubeRecordService frozenTubeRecordService;

    public FrozenTubeRecordResource(FrozenTubeRecordService frozenTubeRecordService) {
        this.frozenTubeRecordService = frozenTubeRecordService;
    }

    /**
     * POST  /frozen-tube-records : Create a new frozenTubeRecord.
     *
     * @param frozenTubeRecordDTO the frozenTubeRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frozenTubeRecordDTO, or with status 400 (Bad Request) if the frozenTubeRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frozen-tube-records")
    @Timed
    public ResponseEntity<FrozenTubeRecordDTO> createFrozenTubeRecord(@Valid @RequestBody FrozenTubeRecordDTO frozenTubeRecordDTO) throws URISyntaxException {
        log.debug("REST request to save FrozenTubeRecord : {}", frozenTubeRecordDTO);
        if (frozenTubeRecordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frozenTubeRecord cannot already have an ID")).body(null);
        }
        FrozenTubeRecordDTO result = frozenTubeRecordService.save(frozenTubeRecordDTO);
        return ResponseEntity.created(new URI("/api/frozen-tube-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frozen-tube-records : Updates an existing frozenTubeRecord.
     *
     * @param frozenTubeRecordDTO the frozenTubeRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frozenTubeRecordDTO,
     * or with status 400 (Bad Request) if the frozenTubeRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the frozenTubeRecordDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frozen-tube-records")
    @Timed
    public ResponseEntity<FrozenTubeRecordDTO> updateFrozenTubeRecord(@Valid @RequestBody FrozenTubeRecordDTO frozenTubeRecordDTO) throws URISyntaxException {
        log.debug("REST request to update FrozenTubeRecord : {}", frozenTubeRecordDTO);
        if (frozenTubeRecordDTO.getId() == null) {
            return createFrozenTubeRecord(frozenTubeRecordDTO);
        }
        FrozenTubeRecordDTO result = frozenTubeRecordService.save(frozenTubeRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenTubeRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frozen-tube-records : get all the frozenTubeRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frozenTubeRecords in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/frozen-tube-records")
    @Timed
    public ResponseEntity<List<FrozenTubeRecordDTO>> getAllFrozenTubeRecords(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FrozenTubeRecords");
        Page<FrozenTubeRecordDTO> page = frozenTubeRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frozen-tube-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frozen-tube-records/:id : get the "id" frozenTubeRecord.
     *
     * @param id the id of the frozenTubeRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frozenTubeRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-tube-records/{id}")
    @Timed
    public ResponseEntity<FrozenTubeRecordDTO> getFrozenTubeRecord(@PathVariable Long id) {
        log.debug("REST request to get FrozenTubeRecord : {}", id);
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeRecordDTO));
    }

    /**
     * DELETE  /frozen-tube-records/:id : delete the "id" frozenTubeRecord.
     *
     * @param id the id of the frozenTubeRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frozen-tube-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrozenTubeRecord(@PathVariable Long id) {
        log.debug("REST request to delete FrozenTubeRecord : {}", id);
        frozenTubeRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
