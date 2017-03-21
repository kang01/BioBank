package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.StorageInBoxService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StorageInBoxDTO;
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
 * REST controller for managing StorageInBox.
 */
@RestController
@RequestMapping("/api")
public class StorageInBoxResource {

    private final Logger log = LoggerFactory.getLogger(StorageInBoxResource.class);

    private static final String ENTITY_NAME = "storageInBox";
        
    private final StorageInBoxService storageInBoxService;

    public StorageInBoxResource(StorageInBoxService storageInBoxService) {
        this.storageInBoxService = storageInBoxService;
    }

    /**
     * POST  /storage-in-boxes : Create a new storageInBox.
     *
     * @param storageInBoxDTO the storageInBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storageInBoxDTO, or with status 400 (Bad Request) if the storageInBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/storage-in-boxes")
    @Timed
    public ResponseEntity<StorageInBoxDTO> createStorageInBox(@Valid @RequestBody StorageInBoxDTO storageInBoxDTO) throws URISyntaxException {
        log.debug("REST request to save StorageInBox : {}", storageInBoxDTO);
        if (storageInBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new storageInBox cannot already have an ID")).body(null);
        }
        StorageInBoxDTO result = storageInBoxService.save(storageInBoxDTO);
        return ResponseEntity.created(new URI("/api/storage-in-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /storage-in-boxes : Updates an existing storageInBox.
     *
     * @param storageInBoxDTO the storageInBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storageInBoxDTO,
     * or with status 400 (Bad Request) if the storageInBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the storageInBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/storage-in-boxes")
    @Timed
    public ResponseEntity<StorageInBoxDTO> updateStorageInBox(@Valid @RequestBody StorageInBoxDTO storageInBoxDTO) throws URISyntaxException {
        log.debug("REST request to update StorageInBox : {}", storageInBoxDTO);
        if (storageInBoxDTO.getId() == null) {
            return createStorageInBox(storageInBoxDTO);
        }
        StorageInBoxDTO result = storageInBoxService.save(storageInBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storageInBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /storage-in-boxes : get all the storageInBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storageInBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/storage-in-boxes")
    @Timed
    public ResponseEntity<List<StorageInBoxDTO>> getAllStorageInBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StorageInBoxes");
        Page<StorageInBoxDTO> page = storageInBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/storage-in-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /storage-in-boxes/:id : get the "id" storageInBox.
     *
     * @param id the id of the storageInBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storageInBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/storage-in-boxes/{id}")
    @Timed
    public ResponseEntity<StorageInBoxDTO> getStorageInBox(@PathVariable Long id) {
        log.debug("REST request to get StorageInBox : {}", id);
        StorageInBoxDTO storageInBoxDTO = storageInBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(storageInBoxDTO));
    }

    /**
     * DELETE  /storage-in-boxes/:id : delete the "id" storageInBox.
     *
     * @param id the id of the storageInBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/storage-in-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteStorageInBox(@PathVariable Long id) {
        log.debug("REST request to delete StorageInBox : {}", id);
        storageInBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
