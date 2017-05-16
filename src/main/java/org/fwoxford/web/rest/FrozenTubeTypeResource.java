package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.FrozenTubeTypeService;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeTypeResponse;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.FrozenTubeTypeDTO;
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
 * REST controller for managing FrozenTubeType.
 */
@RestController
@RequestMapping("/api")
public class FrozenTubeTypeResource {

    private final Logger log = LoggerFactory.getLogger(FrozenTubeTypeResource.class);

    private static final String ENTITY_NAME = "frozenTubeType";

    private final FrozenTubeTypeService frozenTubeTypeService;

    public FrozenTubeTypeResource(FrozenTubeTypeService frozenTubeTypeService) {
        this.frozenTubeTypeService = frozenTubeTypeService;
    }

    /**
     * POST  /frozen-tube-types : Create a new frozenTubeType.
     *
     * @param frozenTubeTypeDTO the frozenTubeTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frozenTubeTypeDTO, or with status 400 (Bad Request) if the frozenTubeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frozen-tube-types")
    @Timed
    public ResponseEntity<FrozenTubeTypeDTO> createFrozenTubeType(@Valid @RequestBody FrozenTubeTypeDTO frozenTubeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save FrozenTubeType : {}", frozenTubeTypeDTO);
        if (frozenTubeTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frozenTubeType cannot already have an ID")).body(null);
        }
        FrozenTubeTypeDTO result = frozenTubeTypeService.save(frozenTubeTypeDTO);
        return ResponseEntity.created(new URI("/api/frozen-tube-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frozen-tube-types : Updates an existing frozenTubeType.
     *
     * @param frozenTubeTypeDTO the frozenTubeTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frozenTubeTypeDTO,
     * or with status 400 (Bad Request) if the frozenTubeTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the frozenTubeTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frozen-tube-types")
    @Timed
    public ResponseEntity<FrozenTubeTypeDTO> updateFrozenTubeType(@Valid @RequestBody FrozenTubeTypeDTO frozenTubeTypeDTO) throws URISyntaxException {
        log.debug("REST request to update FrozenTubeType : {}", frozenTubeTypeDTO);
        if (frozenTubeTypeDTO.getId() == null) {
            return createFrozenTubeType(frozenTubeTypeDTO);
        }
        FrozenTubeTypeDTO result = frozenTubeTypeService.save(frozenTubeTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenTubeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frozen-tube-types : get all the frozenTubeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frozenTubeTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/frozen-tube-types")
    @Timed
    public ResponseEntity<List<FrozenTubeTypeDTO>> getAllFrozenTubeTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FrozenTubeTypes");
        Page<FrozenTubeTypeDTO> page = frozenTubeTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frozen-tube-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frozen-tube-types/:id : get the "id" frozenTubeType.
     *
     * @param id the id of the frozenTubeTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frozenTubeTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-tube-types/{id}")
    @Timed
    public ResponseEntity<FrozenTubeTypeDTO> getFrozenTubeType(@PathVariable Long id) {
        log.debug("REST request to get FrozenTubeType : {}", id);
        FrozenTubeTypeDTO frozenTubeTypeDTO = frozenTubeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeTypeDTO));
    }

    /**
     * DELETE  /frozen-tube-types/:id : delete the "id" frozenTubeType.
     *
     * @param id the id of the frozenTubeTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frozen-tube-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrozenTubeType(@PathVariable Long id) {
        log.debug("REST request to delete FrozenTubeType : {}", id);
        frozenTubeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
    * 查询所有的有效冻存管类型
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/frozen-tube-types/all")
    @Timed
    public ResponseEntity<List<FrozenTubeTypeResponse>> getAllFrozenTubeTypeList() {
        log.debug("REST request to get all FrozenTubeTypeResponse");
        List<FrozenTubeTypeResponse> list = frozenTubeTypeService.getAllFrozenTubeTypeList();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }
}
