package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.DelegateService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.DelegateDTO;
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
 * REST controller for managing Delegate.
 */
@RestController
@RequestMapping("/api")
public class DelegateResource {

    private final Logger log = LoggerFactory.getLogger(DelegateResource.class);

    private static final String ENTITY_NAME = "delegate";
        
    private final DelegateService delegateService;

    public DelegateResource(DelegateService delegateService) {
        this.delegateService = delegateService;
    }

    /**
     * POST  /delegates : Create a new delegate.
     *
     * @param delegateDTO the delegateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new delegateDTO, or with status 400 (Bad Request) if the delegate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/delegates")
    @Timed
    public ResponseEntity<DelegateDTO> createDelegate(@Valid @RequestBody DelegateDTO delegateDTO) throws URISyntaxException {
        log.debug("REST request to save Delegate : {}", delegateDTO);
        if (delegateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new delegate cannot already have an ID")).body(null);
        }
        DelegateDTO result = delegateService.save(delegateDTO);
        return ResponseEntity.created(new URI("/api/delegates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /delegates : Updates an existing delegate.
     *
     * @param delegateDTO the delegateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated delegateDTO,
     * or with status 400 (Bad Request) if the delegateDTO is not valid,
     * or with status 500 (Internal Server Error) if the delegateDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/delegates")
    @Timed
    public ResponseEntity<DelegateDTO> updateDelegate(@Valid @RequestBody DelegateDTO delegateDTO) throws URISyntaxException {
        log.debug("REST request to update Delegate : {}", delegateDTO);
        if (delegateDTO.getId() == null) {
            return createDelegate(delegateDTO);
        }
        DelegateDTO result = delegateService.save(delegateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, delegateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /delegates : get all the delegates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of delegates in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/delegates")
    @Timed
    public ResponseEntity<List<DelegateDTO>> getAllDelegates(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Delegates");
        Page<DelegateDTO> page = delegateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/delegates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /delegates/:id : get the "id" delegate.
     *
     * @param id the id of the delegateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the delegateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/delegates/{id}")
    @Timed
    public ResponseEntity<DelegateDTO> getDelegate(@PathVariable Long id) {
        log.debug("REST request to get Delegate : {}", id);
        DelegateDTO delegateDTO = delegateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(delegateDTO));
    }

    /**
     * DELETE  /delegates/:id : delete the "id" delegate.
     *
     * @param id the id of the delegateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/delegates/{id}")
    @Timed
    public ResponseEntity<Void> deleteDelegate(@PathVariable Long id) {
        log.debug("REST request to delete Delegate : {}", id);
        delegateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
