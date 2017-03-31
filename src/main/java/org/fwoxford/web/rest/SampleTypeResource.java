package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.SampleTypeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.SampleTypeDTO;
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
 * REST controller for managing SampleType.
 */
@RestController
@RequestMapping("/api")
public class SampleTypeResource {

    private final Logger log = LoggerFactory.getLogger(SampleTypeResource.class);

    private static final String ENTITY_NAME = "sampleType";

    private final SampleTypeService sampleTypeService;

    public SampleTypeResource(SampleTypeService sampleTypeService) {
        this.sampleTypeService = sampleTypeService;
    }

    /**
     * POST  /sample-types : Create a new sampleType.
     *
     * @param sampleTypeDTO the sampleTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sampleTypeDTO, or with status 400 (Bad Request) if the sampleType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sample-types")
    @Timed
    public ResponseEntity<SampleTypeDTO> createSampleType(@Valid @RequestBody SampleTypeDTO sampleTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SampleType : {}", sampleTypeDTO);
        if (sampleTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sampleType cannot already have an ID")).body(null);
        }
        SampleTypeDTO result = sampleTypeService.save(sampleTypeDTO);
        return ResponseEntity.created(new URI("/api/sample-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sample-types : Updates an existing sampleType.
     *
     * @param sampleTypeDTO the sampleTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sampleTypeDTO,
     * or with status 400 (Bad Request) if the sampleTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the sampleTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sample-types")
    @Timed
    public ResponseEntity<SampleTypeDTO> updateSampleType(@Valid @RequestBody SampleTypeDTO sampleTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SampleType : {}", sampleTypeDTO);
        if (sampleTypeDTO.getId() == null) {
            return createSampleType(sampleTypeDTO);
        }
        SampleTypeDTO result = sampleTypeService.save(sampleTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sampleTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sample-types : get all the sampleTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sampleTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/sample-types")
    @Timed
    public ResponseEntity<List<SampleTypeDTO>> getAllSampleTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SampleTypes");
        Page<SampleTypeDTO> page = sampleTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sample-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sample-types/:id : get the "id" sampleType.
     *
     * @param id the id of the sampleTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sampleTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sample-types/{id}")
    @Timed
    public ResponseEntity<SampleTypeDTO> getSampleType(@PathVariable Long id) {
        log.debug("REST request to get SampleType : {}", id);
        SampleTypeDTO sampleTypeDTO = sampleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sampleTypeDTO));
    }

    /**
     * DELETE  /sample-types/:id : delete the "id" sampleType.
     *
     * @param id the id of the sampleTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sample-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteSampleType(@PathVariable Long id) {
        log.debug("REST request to delete SampleType : {}", id);
        sampleTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * 查詢所有的有效樣本類型，不帶分頁
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/sample-types/all")
    @Timed
    public ResponseEntity<List<SampleTypeDTO>> getAllSampleTypeList() {
        log.debug("REST request to get all SampleTypeTypes");
        List<SampleTypeDTO> list = sampleTypeService.findAllSampleTypes();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }
}
