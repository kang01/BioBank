package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.CheckTypeService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.CheckTypeDTO;
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
 * REST controller for managing CheckType.
 */
@RestController
@RequestMapping("/api")
public class CheckTypeResource {

    private final Logger log = LoggerFactory.getLogger(CheckTypeResource.class);

    private static final String ENTITY_NAME = "checkType";

    private final CheckTypeService checkTypeService;

    public CheckTypeResource(CheckTypeService checkTypeService) {
        this.checkTypeService = checkTypeService;
    }

    /**
     * POST  /check-types : Create a new checkType.
     *
     * @param checkTypeDTO the checkTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checkTypeDTO, or with status 400 (Bad Request) if the checkType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/check-types")
    @Timed
    public ResponseEntity<CheckTypeDTO> createCheckType(@Valid @RequestBody CheckTypeDTO checkTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CheckType : {}", checkTypeDTO);
        if (checkTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new checkType cannot already have an ID")).body(null);
        }
        CheckTypeDTO result = checkTypeService.save(checkTypeDTO);
        return ResponseEntity.created(new URI("/api/check-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /check-types : Updates an existing checkType.
     *
     * @param checkTypeDTO the checkTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checkTypeDTO,
     * or with status 400 (Bad Request) if the checkTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the checkTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/check-types")
    @Timed
    public ResponseEntity<CheckTypeDTO> updateCheckType(@Valid @RequestBody CheckTypeDTO checkTypeDTO) throws URISyntaxException {
        log.debug("REST request to update CheckType : {}", checkTypeDTO);
        if (checkTypeDTO.getId() == null) {
            return createCheckType(checkTypeDTO);
        }
        CheckTypeDTO result = checkTypeService.save(checkTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, checkTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /check-types : get all the checkTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checkTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/check-types")
    @Timed
    public ResponseEntity<List<CheckTypeDTO>> getAllCheckTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CheckTypes");
        Page<CheckTypeDTO> page = checkTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/check-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /check-types/:id : get the "id" checkType.
     *
     * @param id the id of the checkTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checkTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/check-types/{id}")
    @Timed
    public ResponseEntity<CheckTypeDTO> getCheckType(@PathVariable Long id) {
        log.debug("REST request to get CheckType : {}", id);
        CheckTypeDTO checkTypeDTO = checkTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(checkTypeDTO));
    }

    /**
     * DELETE  /check-types/:id : delete the "id" checkType.
     *
     * @param id the id of the checkTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/check-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCheckType(@PathVariable Long id) {
        log.debug("REST request to delete CheckType : {}", id);
        checkTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 获取所有有效的检测类型
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/check-types/all")
    @Timed
    public ResponseEntity<List<CheckTypeDTO>> getAllCheckTypeList()
        throws URISyntaxException {
        log.debug("REST request to get all CheckTypes");
        List<CheckTypeDTO> checkTypeDTOList = checkTypeService.findAllCheckTypeList();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(checkTypeDTOList));
    }

}
