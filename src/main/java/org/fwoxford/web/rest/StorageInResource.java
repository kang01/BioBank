package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StorageInService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StorageInDTO;
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
 * REST controller for managing StorageIn.
 */
@RestController
@RequestMapping("/api")
public class StorageInResource {

    private final Logger log = LoggerFactory.getLogger(StorageInResource.class);

    private static final String ENTITY_NAME = "storageIn";
        
    private final StorageInService storageInService;

    public StorageInResource(StorageInService storageInService) {
        this.storageInService = storageInService;
    }

    /**
     * POST  /storage-ins : Create a new storageIn.
     *
     * @param storageInDTO the storageInDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storageInDTO, or with status 400 (Bad Request) if the storageIn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/storage-ins")
    @Timed
    public ResponseEntity<StorageInDTO> createStorageIn(@Valid @RequestBody StorageInDTO storageInDTO) throws URISyntaxException {
        log.debug("REST request to save StorageIn : {}", storageInDTO);
        if (storageInDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new storageIn cannot already have an ID")).body(null);
        }
        StorageInDTO result = storageInService.save(storageInDTO);
        return ResponseEntity.created(new URI("/api/storage-ins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /storage-ins : Updates an existing storageIn.
     *
     * @param storageInDTO the storageInDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storageInDTO,
     * or with status 400 (Bad Request) if the storageInDTO is not valid,
     * or with status 500 (Internal Server Error) if the storageInDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/storage-ins")
    @Timed
    public ResponseEntity<StorageInDTO> updateStorageIn(@Valid @RequestBody StorageInDTO storageInDTO) throws URISyntaxException {
        log.debug("REST request to update StorageIn : {}", storageInDTO);
        if (storageInDTO.getId() == null) {
            return createStorageIn(storageInDTO);
        }
        StorageInDTO result = storageInService.save(storageInDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storageInDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /storage-ins : get all the storageIns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storageIns in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/storage-ins")
    @Timed
    public ResponseEntity<List<StorageInDTO>> getAllStorageIns(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StorageIns");
        Page<StorageInDTO> page = storageInService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/storage-ins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /storage-ins/:id : get the "id" storageIn.
     *
     * @param id the id of the storageInDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storageInDTO, or with status 404 (Not Found)
     */
    @GetMapping("/storage-ins/{id}")
    @Timed
    public ResponseEntity<StorageInDTO> getStorageIn(@PathVariable Long id) {
        log.debug("REST request to get StorageIn : {}", id);
        StorageInDTO storageInDTO = storageInService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(storageInDTO));
    }

    /**
     * DELETE  /storage-ins/:id : delete the "id" storageIn.
     *
     * @param id the id of the storageInDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/storage-ins/{id}")
    @Timed
    public ResponseEntity<Void> deleteStorageIn(@PathVariable Long id) {
        log.debug("REST request to delete StorageIn : {}", id);
        storageInService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
