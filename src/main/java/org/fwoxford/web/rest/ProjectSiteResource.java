package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.ProjectSiteService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.ProjectSiteDTO;
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
 * REST controller for managing ProjectSite.
 */
@RestController
@RequestMapping("/api")
public class ProjectSiteResource {

    private final Logger log = LoggerFactory.getLogger(ProjectSiteResource.class);

    private static final String ENTITY_NAME = "projectSite";

    private final ProjectSiteService projectSiteService;

    public ProjectSiteResource(ProjectSiteService projectSiteService) {
        this.projectSiteService = projectSiteService;
    }

    /**
     * POST  /project-sites : Create a new projectSite.
     *
     * @param projectSiteDTO the projectSiteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectSiteDTO, or with status 400 (Bad Request) if the projectSite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-sites")
    @Timed
    public ResponseEntity<ProjectSiteDTO> createProjectSite(@Valid @RequestBody ProjectSiteDTO projectSiteDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectSite : {}", projectSiteDTO);
        if (projectSiteDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new projectSite cannot already have an ID")).body(null);
        }
        ProjectSiteDTO result = projectSiteService.save(projectSiteDTO);
        return ResponseEntity.created(new URI("/api/project-sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-sites : Updates an existing projectSite.
     *
     * @param projectSiteDTO the projectSiteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectSiteDTO,
     * or with status 400 (Bad Request) if the projectSiteDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectSiteDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-sites")
    @Timed
    public ResponseEntity<ProjectSiteDTO> updateProjectSite(@Valid @RequestBody ProjectSiteDTO projectSiteDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectSite : {}", projectSiteDTO);
        if (projectSiteDTO.getId() == null) {
            return createProjectSite(projectSiteDTO);
        }
        ProjectSiteDTO result = projectSiteService.save(projectSiteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectSiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-sites : get all the projectSites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectSites in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/project-sites")
    @Timed
    public ResponseEntity<List<ProjectSiteDTO>> getAllProjectSites(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectSites");
        Page<ProjectSiteDTO> page = projectSiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-sites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-sites/:id : get the "id" projectSite.
     *
     * @param id the id of the projectSiteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectSiteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-sites/{id}")
    @Timed
    public ResponseEntity<ProjectSiteDTO> getProjectSite(@PathVariable Long id) {
        log.debug("REST request to get ProjectSite : {}", id);
        ProjectSiteDTO projectSiteDTO = projectSiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectSiteDTO));
    }

    /**
     * DELETE  /project-sites/:id : delete the "id" projectSite.
     *
     * @param id the id of the projectSiteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-sites/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectSite(@PathVariable Long id) {
        log.debug("REST request to delete ProjectSite : {}", id);
        projectSiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据项目ID查询项目点列表
     * @param projectId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/project-sites/project/{projectId}")
    @Timed
    public ResponseEntity<List<ProjectSiteDTO>> getAllProjectSitesByProjectId(@PathVariable Long projectId)
        throws URISyntaxException {
        log.debug("REST request to get projectSite By ProjectId : ",projectId);
        List<ProjectSiteDTO> list = projectSiteService.findAllProjectSitesByProjectId(projectId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }
}
