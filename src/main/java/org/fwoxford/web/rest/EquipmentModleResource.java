package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.EquipmentModleService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.EquipmentModleDTO;
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
 * REST controller for managing EquipmentModle.
 */
@RestController
@RequestMapping("/api")
public class EquipmentModleResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentModleResource.class);

    private static final String ENTITY_NAME = "equipmentModle";
        
    private final EquipmentModleService equipmentModleService;

    public EquipmentModleResource(EquipmentModleService equipmentModleService) {
        this.equipmentModleService = equipmentModleService;
    }

    /**
     * POST  /equipment-modles : Create a new equipmentModle.
     *
     * @param equipmentModleDTO the equipmentModleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipmentModleDTO, or with status 400 (Bad Request) if the equipmentModle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/equipment-modles")
    @Timed
    public ResponseEntity<EquipmentModleDTO> createEquipmentModle(@Valid @RequestBody EquipmentModleDTO equipmentModleDTO) throws URISyntaxException {
        log.debug("REST request to save EquipmentModle : {}", equipmentModleDTO);
        if (equipmentModleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new equipmentModle cannot already have an ID")).body(null);
        }
        EquipmentModleDTO result = equipmentModleService.save(equipmentModleDTO);
        return ResponseEntity.created(new URI("/api/equipment-modles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipment-modles : Updates an existing equipmentModle.
     *
     * @param equipmentModleDTO the equipmentModleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipmentModleDTO,
     * or with status 400 (Bad Request) if the equipmentModleDTO is not valid,
     * or with status 500 (Internal Server Error) if the equipmentModleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/equipment-modles")
    @Timed
    public ResponseEntity<EquipmentModleDTO> updateEquipmentModle(@Valid @RequestBody EquipmentModleDTO equipmentModleDTO) throws URISyntaxException {
        log.debug("REST request to update EquipmentModle : {}", equipmentModleDTO);
        if (equipmentModleDTO.getId() == null) {
            return createEquipmentModle(equipmentModleDTO);
        }
        EquipmentModleDTO result = equipmentModleService.save(equipmentModleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, equipmentModleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipment-modles : get all the equipmentModles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of equipmentModles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/equipment-modles")
    @Timed
    public ResponseEntity<List<EquipmentModleDTO>> getAllEquipmentModles(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EquipmentModles");
        Page<EquipmentModleDTO> page = equipmentModleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipment-modles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /equipment-modles/:id : get the "id" equipmentModle.
     *
     * @param id the id of the equipmentModleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipmentModleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/equipment-modles/{id}")
    @Timed
    public ResponseEntity<EquipmentModleDTO> getEquipmentModle(@PathVariable Long id) {
        log.debug("REST request to get EquipmentModle : {}", id);
        EquipmentModleDTO equipmentModleDTO = equipmentModleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(equipmentModleDTO));
    }

    /**
     * DELETE  /equipment-modles/:id : delete the "id" equipmentModle.
     *
     * @param id the id of the equipmentModleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/equipment-modles/{id}")
    @Timed
    public ResponseEntity<Void> deleteEquipmentModle(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentModle : {}", id);
        equipmentModleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 获取所有的冻存架类型
     * @return
     */

    @GetMapping("/equipment-modles/equipment-type-list")
    @Timed
    public ResponseEntity<List<EquipmentModleDTO>> getAllEquipmentType() {
        log.debug("REST request to get all EquipmentType : {}");
        List<EquipmentModleDTO> sampleReportForShelvesLists = equipmentModleService.findAllEquipmentType();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sampleReportForShelvesLists));
    }
}
