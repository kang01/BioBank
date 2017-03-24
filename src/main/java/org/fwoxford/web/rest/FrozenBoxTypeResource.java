package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.FrozenBoxTypeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.FrozenBoxTypeDTO;
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
 * REST controller for managing FrozenBoxType.
 */
@RestController
@RequestMapping("/api")
public class FrozenBoxTypeResource {

    private final Logger log = LoggerFactory.getLogger(FrozenBoxTypeResource.class);

    private static final String ENTITY_NAME = "frozenBoxType";

    private final FrozenBoxTypeService frozenBoxTypeService;

    public FrozenBoxTypeResource(FrozenBoxTypeService frozenBoxTypeService) {
        this.frozenBoxTypeService = frozenBoxTypeService;
    }

    /**
     * POST  /frozen-box-types : Create a new frozenBoxType.
     *
     * @param frozenBoxTypeDTO the frozenBoxTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frozenBoxTypeDTO, or with status 400 (Bad Request) if the frozenBoxType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frozen-box-types")
    @Timed
    public ResponseEntity<FrozenBoxTypeDTO> createFrozenBoxType(@Valid @RequestBody FrozenBoxTypeDTO frozenBoxTypeDTO) throws URISyntaxException {
        log.debug("REST request to save FrozenBoxType : {}", frozenBoxTypeDTO);
        if (frozenBoxTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frozenBoxType cannot already have an ID")).body(null);
        }
        FrozenBoxTypeDTO result = frozenBoxTypeService.save(frozenBoxTypeDTO);
        return ResponseEntity.created(new URI("/api/frozen-box-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frozen-box-types : Updates an existing frozenBoxType.
     *
     * @param frozenBoxTypeDTO the frozenBoxTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frozenBoxTypeDTO,
     * or with status 400 (Bad Request) if the frozenBoxTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the frozenBoxTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frozen-box-types")
    @Timed
    public ResponseEntity<FrozenBoxTypeDTO> updateFrozenBoxType(@Valid @RequestBody FrozenBoxTypeDTO frozenBoxTypeDTO) throws URISyntaxException {
        log.debug("REST request to update FrozenBoxType : {}", frozenBoxTypeDTO);
        if (frozenBoxTypeDTO.getId() == null) {
            return createFrozenBoxType(frozenBoxTypeDTO);
        }
        FrozenBoxTypeDTO result = frozenBoxTypeService.save(frozenBoxTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenBoxTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frozen-box-types : get all the frozenBoxTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frozenBoxTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/frozen-box-types")
    @Timed
    public ResponseEntity<List<FrozenBoxTypeDTO>> getAllFrozenBoxTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FrozenBoxTypes");
        Page<FrozenBoxTypeDTO> page = frozenBoxTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frozen-box-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frozen-box-types/:id : get the "id" frozenBoxType.
     *
     * @param id the id of the frozenBoxTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frozenBoxTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-box-types/{id}")
    @Timed
    public ResponseEntity<FrozenBoxTypeDTO> getFrozenBoxType(@PathVariable Long id) {
        log.debug("REST request to get FrozenBoxType : {}", id);
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenBoxTypeDTO));
    }

    /**
     * DELETE  /frozen-box-types/:id : delete the "id" frozenBoxType.
     *
     * @param id the id of the frozenBoxTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frozen-box-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrozenBoxType(@PathVariable Long id) {
        log.debug("REST request to delete FrozenBoxType : {}", id);
        frozenBoxTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 查詢所有的有效凍存盒類型，不帶分頁
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/frozenBoxTypes")
    @Timed
    public ResponseEntity<List<FrozenBoxTypeDTO>> getAllFrozenBoxTypes() {
        log.debug("REST request to get all FrozenBoxTypes");
        List<FrozenBoxTypeDTO> list = frozenBoxTypeService.findAllFrozenBoxTypes();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }
}
