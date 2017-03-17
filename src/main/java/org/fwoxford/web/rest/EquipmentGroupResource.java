package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.EquipmentGroupService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.EquipmentGroupDTO;
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
 * REST controller for managing EquipmentGroup.
 */
@RestController
@RequestMapping("/api")
public class EquipmentGroupResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentGroupResource.class);

    private static final String ENTITY_NAME = "equipmentGroup";
        
    private final EquipmentGroupService equipmentGroupService;

    public EquipmentGroupResource(EquipmentGroupService equipmentGroupService) {
        this.equipmentGroupService = equipmentGroupService;
    }

    /**
     * POST  /equipment-groups : Create a new equipmentGroup.
     *
     * @param equipmentGroupDTO the equipmentGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipmentGroupDTO, or with status 400 (Bad Request) if the equipmentGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/equipment-groups")
    @Timed
    public ResponseEntity<EquipmentGroupDTO> createEquipmentGroup(@Valid @RequestBody EquipmentGroupDTO equipmentGroupDTO) throws URISyntaxException {
        log.debug("REST request to save EquipmentGroup : {}", equipmentGroupDTO);
        if (equipmentGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new equipmentGroup cannot already have an ID")).body(null);
        }
        EquipmentGroupDTO result = equipmentGroupService.save(equipmentGroupDTO);
        return ResponseEntity.created(new URI("/api/equipment-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipment-groups : Updates an existing equipmentGroup.
     *
     * @param equipmentGroupDTO the equipmentGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipmentGroupDTO,
     * or with status 400 (Bad Request) if the equipmentGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the equipmentGroupDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/equipment-groups")
    @Timed
    public ResponseEntity<EquipmentGroupDTO> updateEquipmentGroup(@Valid @RequestBody EquipmentGroupDTO equipmentGroupDTO) throws URISyntaxException {
        log.debug("REST request to update EquipmentGroup : {}", equipmentGroupDTO);
        if (equipmentGroupDTO.getId() == null) {
            return createEquipmentGroup(equipmentGroupDTO);
        }
        EquipmentGroupDTO result = equipmentGroupService.save(equipmentGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, equipmentGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipment-groups : get all the equipmentGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of equipmentGroups in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/equipment-groups")
    @Timed
    public ResponseEntity<List<EquipmentGroupDTO>> getAllEquipmentGroups(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EquipmentGroups");
        Page<EquipmentGroupDTO> page = equipmentGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipment-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /equipment-groups/:id : get the "id" equipmentGroup.
     *
     * @param id the id of the equipmentGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipmentGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/equipment-groups/{id}")
    @Timed
    public ResponseEntity<EquipmentGroupDTO> getEquipmentGroup(@PathVariable Long id) {
        log.debug("REST request to get EquipmentGroup : {}", id);
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(equipmentGroupDTO));
    }

    /**
     * DELETE  /equipment-groups/:id : delete the "id" equipmentGroup.
     *
     * @param id the id of the equipmentGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/equipment-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteEquipmentGroup(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentGroup : {}", id);
        equipmentGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
