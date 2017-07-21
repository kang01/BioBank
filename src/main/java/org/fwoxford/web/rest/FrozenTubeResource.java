package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.FrozenTubeDTO;
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
 * REST controller for managing FrozenTube.
 */
@RestController
@RequestMapping("/api")
public class FrozenTubeResource {

    private final Logger log = LoggerFactory.getLogger(FrozenTubeResource.class);

    private static final String ENTITY_NAME = "frozenTube";

    private final FrozenTubeService frozenTubeService;

    public FrozenTubeResource(FrozenTubeService frozenTubeService) {
        this.frozenTubeService = frozenTubeService;
    }

    /**
     * POST  /frozen-tubes : Create a new frozenTube.
     *
     * @param frozenTubeDTO the frozenTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frozenTubeDTO, or with status 400 (Bad Request) if the frozenTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frozen-tubes")
    @Timed
    public ResponseEntity<FrozenTubeDTO> createFrozenTube(@Valid @RequestBody FrozenTubeDTO frozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to save FrozenTube : {}", frozenTubeDTO);
        if (frozenTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frozenTube cannot already have an ID")).body(null);
        }
        FrozenTubeDTO result = frozenTubeService.save(frozenTubeDTO);
        return ResponseEntity.created(new URI("/api/frozen-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frozen-tubes : Updates an existing frozenTube.
     *
     * @param frozenTubeDTO the frozenTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frozenTubeDTO,
     * or with status 400 (Bad Request) if the frozenTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the frozenTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frozen-tubes")
    @Timed
    public ResponseEntity<FrozenTubeDTO> updateFrozenTube(@Valid @RequestBody FrozenTubeDTO frozenTubeDTO) throws URISyntaxException {
        log.debug("REST request to update FrozenTube : {}", frozenTubeDTO);
        if (frozenTubeDTO.getId() == null) {
            return createFrozenTube(frozenTubeDTO);
        }
        FrozenTubeDTO result = frozenTubeService.save(frozenTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frozen-tubes : get all the frozenTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frozenTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/frozen-tubes")
    @Timed
    public ResponseEntity<List<FrozenTubeDTO>> getAllFrozenTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FrozenTubes");
        Page<FrozenTubeDTO> page = frozenTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frozen-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frozen-tubes/:id : get the "id" frozenTube.
     *
     * @param id the id of the frozenTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frozenTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-tubes/{id}")
    @Timed
    public ResponseEntity<FrozenTubeDTO> getFrozenTube(@PathVariable Long id) {
        log.debug("REST request to get FrozenTube : {}", id);
        FrozenTubeDTO frozenTubeDTO = frozenTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeDTO));
    }

    /**
     * DELETE  /frozen-tubes/:id : delete the "id" frozenTube.
     *
     * @param id the id of the frozenTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frozen-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrozenTube(@PathVariable Long id) {
        log.debug("REST request to delete FrozenTube : {}", id);
        frozenTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * GET  /frozen-tubes/:id : get the "id" frozenTube.
     * 根据冻存盒code取冻存管，以及冻存管的出库标识
     * @param frozenBoxCode
     * @return the ResponseEntity with status 200 (OK) and with body the frozenTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-tubes/frozenBox/{frozenBoxCode}/task/{id}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByFrozenBoxCode(@PathVariable String frozenBoxCode,@PathVariable Long id) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxAndFrozenTubeResponse frozenTubeList = frozenTubeService.getFrozenTubeByFrozenBoxCode(frozenBoxCode,id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeList));
    }

    /**
     * 根据样本编码，项目编码，样本类型查询样本信息
     * @param sampleCode
     * @param projectCode
     * @param id
     * @return
     */
    @GetMapping("/frozen-tubes/sample/{sampleCode}/project/{projectCode}/sampleType/{id}")
    @Timed
    public ResponseEntity<List<FrozenTubeDTO>> getFrozenTubeBySampleCodeNadProjectAndSampleType(@PathVariable String sampleCode,@PathVariable String projectCode,@PathVariable Long id) {
        log.debug("REST request to get FrozenTube : {}", sampleCode);
        List<FrozenTubeDTO> frozenTubeDTO = frozenTubeService.getFrozenTubeBySampleCode(sampleCode,projectCode,id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeDTO));
    }

    /**
     * 根据样本编码，项目编码，样本类型，样本分类查询样本信息
     * @param sampleCode
     * @param projectCode
     * @param sampleTypeId
     * @param sampleClassificationId
     * @return
     */
    @GetMapping("/frozen-tubes/sample/{sampleCode}/project/{projectCode}/sampleType/{sampleTypeId}/sampleClassification/{sampleClassificationId}")
    @Timed
    public ResponseEntity<List<FrozenTubeDTO>> getFrozenTubeBySampleCodeNadProjectAndSampleTypeAndSampleClassifacition(@PathVariable String sampleCode,@PathVariable String projectCode,@PathVariable Long sampleTypeId,
                                                                                                                       @PathVariable Long sampleClassificationId) {
        log.debug("REST request to get FrozenTube : {}", sampleCode);
        List<FrozenTubeDTO> frozenTubeDTO = frozenTubeService.findFrozenTubeBySampleCodeNadProjectAndSampleTypeAndSampleClassifacition(sampleCode,projectCode,sampleTypeId,sampleClassificationId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeDTO));
    }

    /**
     * 根据样本ID字符串获取样本信息
     * @param ids
     * @return
     */
    @GetMapping("/frozen-tubes/ids/{ids}")
    @Timed
    public ResponseEntity<List<FrozenTubeDTO>> getFrozenTubeByIds(@PathVariable String ids) {
        log.debug("REST request to get FrozenTubeList : {}", ids);
        List<FrozenTubeDTO> frozenTubeDTO = frozenTubeService.getFrozenTubeByIds(ids);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeDTO));
    }

    /**
     * 根据样本编码，项目编码，冻存盒编码样本类型，样本分类查询样本信息
     * @param sampleCode
     * @param projectCode
     * @param sampleTypeId
     * @param sampleClassificationId
     * @return
     */
    @GetMapping("/frozen-tubes/sample/{sampleCode}/project/{projectCode}/frozenBox/{frozenBoxId}/sampleType/{sampleTypeId}/sampleClassification/{sampleClassificationId}")
    @Timed
    public ResponseEntity<List<FrozenTubeDTO>> getFrozenTubeBySampleCodeAnddProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(@PathVariable String sampleCode,@PathVariable String projectCode,@PathVariable Long frozenBoxId,@PathVariable Long sampleTypeId,
                                                                                                                       @PathVariable Long sampleClassificationId) {
        log.debug("REST request to get FrozenTube : {}", sampleCode);
        List<FrozenTubeDTO> frozenTubeDTO = frozenTubeService.findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(sampleCode,projectCode,frozenBoxId,sampleTypeId,sampleClassificationId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeDTO));
    }
}
