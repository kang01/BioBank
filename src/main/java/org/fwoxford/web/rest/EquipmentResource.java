package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.domain.Equipment;
import org.fwoxford.service.EquipmentService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.EquipmentDTO;
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
 * REST controller for managing Equipment.
 */
@RestController
@RequestMapping("/api")
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);

    private static final String ENTITY_NAME = "equipment";

    private final EquipmentService equipmentService;

    public EquipmentResource(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    /**
     * POST  /equipment : Create a new equipment.
     *
     * @param equipmentDTO the equipmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipmentDTO, or with status 400 (Bad Request) if the equipment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/equipment")
    @Timed
    public ResponseEntity<EquipmentDTO> createEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new equipment cannot already have an ID")).body(null);
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.created(new URI("/api/equipment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipment : Updates an existing equipment.
     *
     * @param equipmentDTO the equipmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipmentDTO,
     * or with status 400 (Bad Request) if the equipmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the equipmentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/equipment")
    @Timed
    public ResponseEntity<EquipmentDTO> updateEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() == null) {
            return createEquipment(equipmentDTO);
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, equipmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipment : get all the equipment.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of equipment in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/equipment")
    @Timed
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Equipment");
        Page<EquipmentDTO> page = equipmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipment");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /equipment/:id : get the "id" equipment.
     *
     * @param id the id of the equipmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipmentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/equipment/{id}")
    @Timed
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        EquipmentDTO equipmentDTO = equipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(equipmentDTO));
    }

    /**
     * DELETE  /equipment/:id : delete the "id" equipment.
     *
     * @param id the id of the equipmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/equipment/{id}")
    @Timed
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    /**
     * 查詢所有的有效設備列表，不帶分頁
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/equipment/all")
    @Timed
    public ResponseEntity<List<EquipmentDTO>> getAllEquipmentList() {
        log.debug("REST request to get all Equipments");
        List<EquipmentDTO> list = equipmentService.findAllEquipments();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }
}
