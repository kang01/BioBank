package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.PositionDestroyRecordService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.PositionDestroyRecordDTO;
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
 * REST controller for managing PositionDestroyRecord.
 */
@RestController
@RequestMapping("/api")
public class PositionDestroyRecordResource {

    private final Logger log = LoggerFactory.getLogger(PositionDestroyRecordResource.class);

    private static final String ENTITY_NAME = "positionDestroyRecord";
        
    private final PositionDestroyRecordService positionDestroyRecordService;

    public PositionDestroyRecordResource(PositionDestroyRecordService positionDestroyRecordService) {
        this.positionDestroyRecordService = positionDestroyRecordService;
    }

    /**
     * POST  /position-destroy-records : Create a new positionDestroyRecord.
     *
     * @param positionDestroyRecordDTO the positionDestroyRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new positionDestroyRecordDTO, or with status 400 (Bad Request) if the positionDestroyRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/position-destroy-records")
    @Timed
    public ResponseEntity<PositionDestroyRecordDTO> createPositionDestroyRecord(@Valid @RequestBody PositionDestroyRecordDTO positionDestroyRecordDTO) throws URISyntaxException {
        log.debug("REST request to save PositionDestroyRecord : {}", positionDestroyRecordDTO);
        if (positionDestroyRecordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new positionDestroyRecord cannot already have an ID")).body(null);
        }
        PositionDestroyRecordDTO result = positionDestroyRecordService.save(positionDestroyRecordDTO);
        return ResponseEntity.created(new URI("/api/position-destroy-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /position-destroy-records : Updates an existing positionDestroyRecord.
     *
     * @param positionDestroyRecordDTO the positionDestroyRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated positionDestroyRecordDTO,
     * or with status 400 (Bad Request) if the positionDestroyRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the positionDestroyRecordDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/position-destroy-records")
    @Timed
    public ResponseEntity<PositionDestroyRecordDTO> updatePositionDestroyRecord(@Valid @RequestBody PositionDestroyRecordDTO positionDestroyRecordDTO) throws URISyntaxException {
        log.debug("REST request to update PositionDestroyRecord : {}", positionDestroyRecordDTO);
        if (positionDestroyRecordDTO.getId() == null) {
            return createPositionDestroyRecord(positionDestroyRecordDTO);
        }
        PositionDestroyRecordDTO result = positionDestroyRecordService.save(positionDestroyRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, positionDestroyRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /position-destroy-records : get all the positionDestroyRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of positionDestroyRecords in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/position-destroy-records")
    @Timed
    public ResponseEntity<List<PositionDestroyRecordDTO>> getAllPositionDestroyRecords(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PositionDestroyRecords");
        Page<PositionDestroyRecordDTO> page = positionDestroyRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/position-destroy-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /position-destroy-records/:id : get the "id" positionDestroyRecord.
     *
     * @param id the id of the positionDestroyRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the positionDestroyRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/position-destroy-records/{id}")
    @Timed
    public ResponseEntity<PositionDestroyRecordDTO> getPositionDestroyRecord(@PathVariable Long id) {
        log.debug("REST request to get PositionDestroyRecord : {}", id);
        PositionDestroyRecordDTO positionDestroyRecordDTO = positionDestroyRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(positionDestroyRecordDTO));
    }

    /**
     * DELETE  /position-destroy-records/:id : delete the "id" positionDestroyRecord.
     *
     * @param id the id of the positionDestroyRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/position-destroy-records/{id}")
    @Timed
    public ResponseEntity<Void> deletePositionDestroyRecord(@PathVariable Long id) {
        log.debug("REST request to delete PositionDestroyRecord : {}", id);
        positionDestroyRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
