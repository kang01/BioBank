package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.SupportRackTypeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.SupportRackTypeDTO;
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
 * REST controller for managing SupportRackType.
 */
@RestController
@RequestMapping("/api")
public class SupportRackTypeResource {

    private final Logger log = LoggerFactory.getLogger(SupportRackTypeResource.class);

    private static final String ENTITY_NAME = "supportRackType";
        
    private final SupportRackTypeService supportRackTypeService;

    public SupportRackTypeResource(SupportRackTypeService supportRackTypeService) {
        this.supportRackTypeService = supportRackTypeService;
    }

    /**
     * POST  /support-rack-types : Create a new supportRackType.
     *
     * @param supportRackTypeDTO the supportRackTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new supportRackTypeDTO, or with status 400 (Bad Request) if the supportRackType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/support-rack-types")
    @Timed
    public ResponseEntity<SupportRackTypeDTO> createSupportRackType(@Valid @RequestBody SupportRackTypeDTO supportRackTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SupportRackType : {}", supportRackTypeDTO);
        if (supportRackTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new supportRackType cannot already have an ID")).body(null);
        }
        SupportRackTypeDTO result = supportRackTypeService.save(supportRackTypeDTO);
        return ResponseEntity.created(new URI("/api/support-rack-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /support-rack-types : Updates an existing supportRackType.
     *
     * @param supportRackTypeDTO the supportRackTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated supportRackTypeDTO,
     * or with status 400 (Bad Request) if the supportRackTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the supportRackTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/support-rack-types")
    @Timed
    public ResponseEntity<SupportRackTypeDTO> updateSupportRackType(@Valid @RequestBody SupportRackTypeDTO supportRackTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SupportRackType : {}", supportRackTypeDTO);
        if (supportRackTypeDTO.getId() == null) {
            return createSupportRackType(supportRackTypeDTO);
        }
        SupportRackTypeDTO result = supportRackTypeService.save(supportRackTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, supportRackTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /support-rack-types : get all the supportRackTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of supportRackTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/support-rack-types")
    @Timed
    public ResponseEntity<List<SupportRackTypeDTO>> getAllSupportRackTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SupportRackTypes");
        Page<SupportRackTypeDTO> page = supportRackTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/support-rack-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /support-rack-types/:id : get the "id" supportRackType.
     *
     * @param id the id of the supportRackTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the supportRackTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/support-rack-types/{id}")
    @Timed
    public ResponseEntity<SupportRackTypeDTO> getSupportRackType(@PathVariable Long id) {
        log.debug("REST request to get SupportRackType : {}", id);
        SupportRackTypeDTO supportRackTypeDTO = supportRackTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supportRackTypeDTO));
    }

    /**
     * DELETE  /support-rack-types/:id : delete the "id" supportRackType.
     *
     * @param id the id of the supportRackTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/support-rack-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteSupportRackType(@PathVariable Long id) {
        log.debug("REST request to delete SupportRackType : {}", id);
        supportRackTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
