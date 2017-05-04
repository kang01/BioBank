package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.SampleClassificationService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.SampleClassificationDTO;
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
 * REST controller for managing SampleClassification.
 */
@RestController
@RequestMapping("/api")
public class SampleClassificationResource {

    private final Logger log = LoggerFactory.getLogger(SampleClassificationResource.class);

    private static final String ENTITY_NAME = "sampleClassification";
        
    private final SampleClassificationService sampleClassificationService;

    public SampleClassificationResource(SampleClassificationService sampleClassificationService) {
        this.sampleClassificationService = sampleClassificationService;
    }

    /**
     * POST  /sample-classifications : Create a new sampleClassification.
     *
     * @param sampleClassificationDTO the sampleClassificationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sampleClassificationDTO, or with status 400 (Bad Request) if the sampleClassification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sample-classifications")
    @Timed
    public ResponseEntity<SampleClassificationDTO> createSampleClassification(@Valid @RequestBody SampleClassificationDTO sampleClassificationDTO) throws URISyntaxException {
        log.debug("REST request to save SampleClassification : {}", sampleClassificationDTO);
        if (sampleClassificationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sampleClassification cannot already have an ID")).body(null);
        }
        SampleClassificationDTO result = sampleClassificationService.save(sampleClassificationDTO);
        return ResponseEntity.created(new URI("/api/sample-classifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sample-classifications : Updates an existing sampleClassification.
     *
     * @param sampleClassificationDTO the sampleClassificationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sampleClassificationDTO,
     * or with status 400 (Bad Request) if the sampleClassificationDTO is not valid,
     * or with status 500 (Internal Server Error) if the sampleClassificationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sample-classifications")
    @Timed
    public ResponseEntity<SampleClassificationDTO> updateSampleClassification(@Valid @RequestBody SampleClassificationDTO sampleClassificationDTO) throws URISyntaxException {
        log.debug("REST request to update SampleClassification : {}", sampleClassificationDTO);
        if (sampleClassificationDTO.getId() == null) {
            return createSampleClassification(sampleClassificationDTO);
        }
        SampleClassificationDTO result = sampleClassificationService.save(sampleClassificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sampleClassificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sample-classifications : get all the sampleClassifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sampleClassifications in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/sample-classifications")
    @Timed
    public ResponseEntity<List<SampleClassificationDTO>> getAllSampleClassifications(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SampleClassifications");
        Page<SampleClassificationDTO> page = sampleClassificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sample-classifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sample-classifications/:id : get the "id" sampleClassification.
     *
     * @param id the id of the sampleClassificationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sampleClassificationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sample-classifications/{id}")
    @Timed
    public ResponseEntity<SampleClassificationDTO> getSampleClassification(@PathVariable Long id) {
        log.debug("REST request to get SampleClassification : {}", id);
        SampleClassificationDTO sampleClassificationDTO = sampleClassificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sampleClassificationDTO));
    }

    /**
     * DELETE  /sample-classifications/:id : delete the "id" sampleClassification.
     *
     * @param id the id of the sampleClassificationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sample-classifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteSampleClassification(@PathVariable Long id) {
        log.debug("REST request to delete SampleClassification : {}", id);
        sampleClassificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
