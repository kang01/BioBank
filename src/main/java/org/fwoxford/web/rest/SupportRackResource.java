package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.SupportRackService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.SupportRackDTO;
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
 * REST controller for managing SupportRack.
 */
@RestController
@RequestMapping("/api")
public class SupportRackResource {

    private final Logger log = LoggerFactory.getLogger(SupportRackResource.class);

    private static final String ENTITY_NAME = "supportRack";

    private final SupportRackService supportRackService;

    public SupportRackResource(SupportRackService supportRackService) {
        this.supportRackService = supportRackService;
    }

    /**
     * POST  /support-racks : Create a new supportRack.
     *
     * @param supportRackDTO the supportRackDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new supportRackDTO, or with status 400 (Bad Request) if the supportRack has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/support-racks")
    @Timed
    public ResponseEntity<SupportRackDTO> createSupportRack(@Valid @RequestBody SupportRackDTO supportRackDTO) throws URISyntaxException {
        log.debug("REST request to save SupportRack : {}", supportRackDTO);
        if (supportRackDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new supportRack cannot already have an ID")).body(null);
        }
        SupportRackDTO result = supportRackService.save(supportRackDTO);
        return ResponseEntity.created(new URI("/api/support-racks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /support-racks : Updates an existing supportRack.
     *
     * @param supportRackDTO the supportRackDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated supportRackDTO,
     * or with status 400 (Bad Request) if the supportRackDTO is not valid,
     * or with status 500 (Internal Server Error) if the supportRackDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/support-racks")
    @Timed
    public ResponseEntity<SupportRackDTO> updateSupportRack(@Valid @RequestBody SupportRackDTO supportRackDTO) throws URISyntaxException {
        log.debug("REST request to update SupportRack : {}", supportRackDTO);
        if (supportRackDTO.getId() == null) {
            return createSupportRack(supportRackDTO);
        }
        SupportRackDTO result = supportRackService.save(supportRackDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, supportRackDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /support-racks : get all the supportRacks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of supportRacks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/support-racks")
    @Timed
    public ResponseEntity<List<SupportRackDTO>> getAllSupportRacks(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SupportRacks");
        Page<SupportRackDTO> page = supportRackService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/support-racks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /support-racks/:id : get the "id" supportRack.
     *
     * @param id the id of the supportRackDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the supportRackDTO, or with status 404 (Not Found)
     */
    @GetMapping("/support-racks/{id}")
    @Timed
    public ResponseEntity<SupportRackDTO> getSupportRack(@PathVariable Long id) {
        log.debug("REST request to get SupportRack : {}", id);
        SupportRackDTO supportRackDTO = supportRackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supportRackDTO));
    }

    /**
     * DELETE  /support-racks/:id : delete the "id" supportRack.
     *
     * @param id the id of the supportRackDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/support-racks/{id}")
    @Timed
    public ResponseEntity<Void> deleteSupportRack(@PathVariable Long id) {
        log.debug("REST request to delete SupportRack : {}", id);
        supportRackService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根據區域ID查詢架子列表
     * @param areaId
     * @return
     */
    @GetMapping("/support-racks/area/{areaId}")
    @Timed
    public ResponseEntity<List<SupportRackDTO>> getSupportRackByAreaId(@PathVariable Long areaId) {
        log.debug("REST request to get SupportRack By AreaId : {}", areaId);
        List<SupportRackDTO> supportRackDTOs = supportRackService.findSupportRackByAreaId(areaId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supportRackDTOs));
    }

    @GetMapping("/support-racks/equipment/{equipmentId}/area/{areaId}")
    @Timed
    public ResponseEntity<List<SupportRackDTO>> getSupportRackByEquipmentAndArea(@PathVariable Long equipmentId ,@PathVariable Long areaId) {
        log.debug("REST request to get SupportRack By EquipmentAndArea : {}", areaId);
        List<SupportRackDTO> supportRackDTOs = supportRackService.getSupportRackByEquipmentAndArea(equipmentId,areaId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supportRackDTOs));
    }
}
