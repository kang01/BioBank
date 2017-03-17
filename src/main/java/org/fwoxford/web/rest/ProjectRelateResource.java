package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.ProjectRelateService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.ProjectRelateDTO;
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
 * REST controller for managing ProjectRelate.
 */
@RestController
@RequestMapping("/api")
public class ProjectRelateResource {

    private final Logger log = LoggerFactory.getLogger(ProjectRelateResource.class);

    private static final String ENTITY_NAME = "projectRelate";
        
    private final ProjectRelateService projectRelateService;

    public ProjectRelateResource(ProjectRelateService projectRelateService) {
        this.projectRelateService = projectRelateService;
    }

    /**
     * POST  /project-relates : Create a new projectRelate.
     *
     * @param projectRelateDTO the projectRelateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectRelateDTO, or with status 400 (Bad Request) if the projectRelate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-relates")
    @Timed
    public ResponseEntity<ProjectRelateDTO> createProjectRelate(@Valid @RequestBody ProjectRelateDTO projectRelateDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectRelate : {}", projectRelateDTO);
        if (projectRelateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new projectRelate cannot already have an ID")).body(null);
        }
        ProjectRelateDTO result = projectRelateService.save(projectRelateDTO);
        return ResponseEntity.created(new URI("/api/project-relates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-relates : Updates an existing projectRelate.
     *
     * @param projectRelateDTO the projectRelateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectRelateDTO,
     * or with status 400 (Bad Request) if the projectRelateDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectRelateDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-relates")
    @Timed
    public ResponseEntity<ProjectRelateDTO> updateProjectRelate(@Valid @RequestBody ProjectRelateDTO projectRelateDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectRelate : {}", projectRelateDTO);
        if (projectRelateDTO.getId() == null) {
            return createProjectRelate(projectRelateDTO);
        }
        ProjectRelateDTO result = projectRelateService.save(projectRelateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectRelateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-relates : get all the projectRelates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectRelates in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/project-relates")
    @Timed
    public ResponseEntity<List<ProjectRelateDTO>> getAllProjectRelates(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectRelates");
        Page<ProjectRelateDTO> page = projectRelateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-relates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-relates/:id : get the "id" projectRelate.
     *
     * @param id the id of the projectRelateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectRelateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-relates/{id}")
    @Timed
    public ResponseEntity<ProjectRelateDTO> getProjectRelate(@PathVariable Long id) {
        log.debug("REST request to get ProjectRelate : {}", id);
        ProjectRelateDTO projectRelateDTO = projectRelateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectRelateDTO));
    }

    /**
     * DELETE  /project-relates/:id : delete the "id" projectRelate.
     *
     * @param id the id of the projectRelateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-relates/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectRelate(@PathVariable Long id) {
        log.debug("REST request to delete ProjectRelate : {}", id);
        projectRelateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
