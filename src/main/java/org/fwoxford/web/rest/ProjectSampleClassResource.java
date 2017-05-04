package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.ProjectSampleClassService;
import org.fwoxford.service.dto.ProjectSampleClassificationDTO;
import org.fwoxford.service.dto.ProjectSampleTypeDTO;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.ProjectSampleClassDTO;
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
 * REST controller for managing ProjectSampleClass.
 */
@RestController
@RequestMapping("/api")
public class ProjectSampleClassResource {

    private final Logger log = LoggerFactory.getLogger(ProjectSampleClassResource.class);

    private static final String ENTITY_NAME = "projectSampleClass";

    private final ProjectSampleClassService projectSampleClassService;

    public ProjectSampleClassResource(ProjectSampleClassService projectSampleClassService) {
        this.projectSampleClassService = projectSampleClassService;
    }

    /**
     * POST  /project-sample-classes : Create a new projectSampleClass.
     *
     * @param projectSampleClassDTO the projectSampleClassDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectSampleClassDTO, or with status 400 (Bad Request) if the projectSampleClass has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-sample-classes")
    @Timed
    public ResponseEntity<ProjectSampleClassDTO> createProjectSampleClass(@Valid @RequestBody ProjectSampleClassDTO projectSampleClassDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectSampleClass : {}", projectSampleClassDTO);
        if (projectSampleClassDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new projectSampleClass cannot already have an ID")).body(null);
        }
        ProjectSampleClassDTO result = projectSampleClassService.save(projectSampleClassDTO);
        return ResponseEntity.created(new URI("/api/project-sample-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-sample-classes : Updates an existing projectSampleClass.
     *
     * @param projectSampleClassDTO the projectSampleClassDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectSampleClassDTO,
     * or with status 400 (Bad Request) if the projectSampleClassDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectSampleClassDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-sample-classes")
    @Timed
    public ResponseEntity<ProjectSampleClassDTO> updateProjectSampleClass(@Valid @RequestBody ProjectSampleClassDTO projectSampleClassDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectSampleClass : {}", projectSampleClassDTO);
        if (projectSampleClassDTO.getId() == null) {
            return createProjectSampleClass(projectSampleClassDTO);
        }
        ProjectSampleClassDTO result = projectSampleClassService.save(projectSampleClassDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectSampleClassDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-sample-classes : get all the projectSampleClasses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectSampleClasses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/project-sample-classes")
    @Timed
    public ResponseEntity<List<ProjectSampleClassDTO>> getAllProjectSampleClasses(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectSampleClasses");
        Page<ProjectSampleClassDTO> page = projectSampleClassService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-sample-classes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-sample-classes/:id : get the "id" projectSampleClass.
     *
     * @param id the id of the projectSampleClassDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectSampleClassDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-sample-classes/{id}")
    @Timed
    public ResponseEntity<ProjectSampleClassDTO> getProjectSampleClass(@PathVariable Long id) {
        log.debug("REST request to get ProjectSampleClass : {}", id);
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectSampleClassDTO));
    }

    /**
     * DELETE  /project-sample-classes/:id : delete the "id" projectSampleClass.
     *
     * @param id the id of the projectSampleClassDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-sample-classes/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectSampleClass(@PathVariable Long id) {
        log.debug("REST request to delete ProjectSampleClass : {}", id);
        projectSampleClassService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    //如果样本类型为99,则不需要查询样本分类,直接返回样本分类,若不是99,则需要查询样本分类,再根据项目编码,样本类型,样本分类,查询样本类型信息

    /**
     * 根据项目编码查询样本类型
     * @param projectCode
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/project-sample-classes/projectCode/{projectCode}")
    @Timed
    public ResponseEntity<List<ProjectSampleTypeDTO>> getSampleTypeByProjectCode(@PathVariable String projectCode)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectSampleClasses");
        List<ProjectSampleTypeDTO> result = projectSampleClassService.getSampleTypeByProjectCode(projectCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 根据项目编码，样本类型ID，查询样本分类
     * @param projectCode
     * @param sampleTypeId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/project-sample-classes/projectCode/{projectCode}/sampleTypeId/{sampleTypeId}")
    @Timed
    public ResponseEntity<List<ProjectSampleClassificationDTO>> getSampleClassificationByProjectCodeAndsampleTypeId(@PathVariable String projectCode, @PathVariable Long sampleTypeId)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProjectSampleClasses");
        List<ProjectSampleClassificationDTO> result = projectSampleClassService.getSampleClassificationByProjectCodeAndsampleTypeId(projectCode,sampleTypeId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
