package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.TranshipBoxService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.TranshipBoxDTO;
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
 * REST controller for managing TranshipBox.
 */
@RestController
@RequestMapping("/api")
public class TranshipBoxResource {

    private final Logger log = LoggerFactory.getLogger(TranshipBoxResource.class);

    private static final String ENTITY_NAME = "transhipBox";
        
    private final TranshipBoxService transhipBoxService;

    public TranshipBoxResource(TranshipBoxService transhipBoxService) {
        this.transhipBoxService = transhipBoxService;
    }

    /**
     * POST  /tranship-boxes : Create a new transhipBox.
     *
     * @param transhipBoxDTO the transhipBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipBoxDTO, or with status 400 (Bad Request) if the transhipBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranship-boxes")
    @Timed
    public ResponseEntity<TranshipBoxDTO> createTranshipBox(@Valid @RequestBody TranshipBoxDTO transhipBoxDTO) throws URISyntaxException {
        log.debug("REST request to save TranshipBox : {}", transhipBoxDTO);
        if (transhipBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transhipBox cannot already have an ID")).body(null);
        }
        TranshipBoxDTO result = transhipBoxService.save(transhipBoxDTO);
        return ResponseEntity.created(new URI("/api/tranship-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranship-boxes : Updates an existing transhipBox.
     *
     * @param transhipBoxDTO the transhipBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipBoxDTO,
     * or with status 400 (Bad Request) if the transhipBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranship-boxes")
    @Timed
    public ResponseEntity<TranshipBoxDTO> updateTranshipBox(@Valid @RequestBody TranshipBoxDTO transhipBoxDTO) throws URISyntaxException {
        log.debug("REST request to update TranshipBox : {}", transhipBoxDTO);
        if (transhipBoxDTO.getId() == null) {
            return createTranshipBox(transhipBoxDTO);
        }
        TranshipBoxDTO result = transhipBoxService.save(transhipBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranship-boxes : get all the transhipBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transhipBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranship-boxes")
    @Timed
    public ResponseEntity<List<TranshipBoxDTO>> getAllTranshipBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TranshipBoxes");
        Page<TranshipBoxDTO> page = transhipBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tranship-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tranship-boxes/:id : get the "id" transhipBox.
     *
     * @param id the id of the transhipBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tranship-boxes/{id}")
    @Timed
    public ResponseEntity<TranshipBoxDTO> getTranshipBox(@PathVariable Long id) {
        log.debug("REST request to get TranshipBox : {}", id);
        TranshipBoxDTO transhipBoxDTO = transhipBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipBoxDTO));
    }

    /**
     * DELETE  /tranship-boxes/:id : delete the "id" transhipBox.
     *
     * @param id the id of the transhipBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranship-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranshipBox(@PathVariable Long id) {
        log.debug("REST request to delete TranshipBox : {}", id);
        transhipBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}