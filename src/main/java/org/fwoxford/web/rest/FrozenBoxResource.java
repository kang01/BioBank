package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FrozenBox.
 */
@RestController
@RequestMapping("/api")
public class FrozenBoxResource {

    private final Logger log = LoggerFactory.getLogger(FrozenBoxResource.class);

    private static final String ENTITY_NAME = "frozenBox";

    private final FrozenBoxService frozenBoxService;

    public FrozenBoxResource(FrozenBoxService frozenBoxService) {
        this.frozenBoxService = frozenBoxService;
    }

    /**
     * POST  /frozen-boxes : Create a new frozenBox.
     *
     * @param frozenBoxDTO the frozenBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frozenBoxDTO, or with status 400 (Bad Request) if the frozenBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frozen-boxes")
    @Timed
    public ResponseEntity<FrozenBoxDTO> createFrozenBox(@Valid @RequestBody FrozenBoxDTO frozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to save FrozenBox : {}", frozenBoxDTO);
        if (frozenBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frozenBox cannot already have an ID")).body(null);
        }
        FrozenBoxDTO result = frozenBoxService.save(frozenBoxDTO);
        return ResponseEntity.created(new URI("/api/frozen-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frozen-boxes : Updates an existing frozenBox.
     *
     * @param frozenBoxDTO the frozenBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frozenBoxDTO,
     * or with status 400 (Bad Request) if the frozenBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the frozenBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frozen-boxes")
    @Timed
    public ResponseEntity<FrozenBoxDTO> updateFrozenBox(@Valid @RequestBody FrozenBoxDTO frozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to update FrozenBox : {}", frozenBoxDTO);
        if (frozenBoxDTO.getId() == null) {
            return createFrozenBox(frozenBoxDTO);
        }
        FrozenBoxDTO result = frozenBoxService.save(frozenBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frozen-boxes : get all the frozenBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frozenBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/frozen-boxes")
    @Timed
    public ResponseEntity<List<FrozenBoxDTO>> getAllFrozenBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FrozenBoxes");
        Page<FrozenBoxDTO> page = frozenBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frozen-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frozen-boxes/:id : get the "id" frozenBox.
     *
     * @param id the id of the frozenBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frozenBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-boxes/{id}")
    @Timed
    public ResponseEntity<FrozenBoxDTO> getFrozenBox(@PathVariable Long id) {
        log.debug("REST request to get FrozenBox : {}", id);
        FrozenBoxDTO frozenBoxDTO = frozenBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenBoxDTO));
    }

    /**
     * DELETE  /frozen-boxes/:id : delete the "id" frozenBox.
     *
     * @param id the id of the frozenBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frozen-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrozenBox(@PathVariable Long id) {
        log.debug("REST request to delete FrozenBox : {}", id);
        frozenBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据冻存盒ID查询冻存盒和冻存管信息
     * @param id
     * @return
     */
    @GetMapping("/findFrozenBoxAndTubeByBoxId/{id}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByForzenBoxId(@PathVariable Long id) {
        log.debug("REST request to get FrozenTube : {}", id);
        FrozenBoxAndFrozenTubeResponse res = frozenBoxService.findFrozenBoxAndTubeByBoxId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 根据冻存盒CODE查询冻存盒和冻存管信息
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/findFrozenBoxAndTubeByBoxCode/{frozenBoxCode}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByForzenBoxCode(@PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxAndFrozenTubeResponse res = frozenBoxService.findFrozenBoxAndTubeByBoxCode(frozenBoxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
    /**
     * 根据冻存盒CODE字符串查询冻存盒和冻存管信息
     * @param frozenBoxCodeStr
     * @return
     */
    @GetMapping("/findFrozenBoxAndTubeByBoxCodes/{frozenBoxCodeStr}")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> getFrozenBoxAndTubeListByBoxCodeStr(@PathVariable  String frozenBoxCodeStr) {
        log.debug("REST request to get FrozenBoxAndTube By codes : {}", frozenBoxCodeStr);
        List<FrozenBoxAndFrozenTubeResponse> res = frozenBoxService.findFrozenBoxAndTubeListByBoxCodeStr(frozenBoxCodeStr);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
}
