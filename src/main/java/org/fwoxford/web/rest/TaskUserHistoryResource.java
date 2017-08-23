package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.TaskUserHistoryService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.TaskUserHistoryDTO;
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
 * REST controller for managing TaskUserHistory.
 */
@RestController
@RequestMapping("/api")
public class TaskUserHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TaskUserHistoryResource.class);

    private static final String ENTITY_NAME = "TaskUserHistory";

    private final TaskUserHistoryService taskUserHistoryService;

    public TaskUserHistoryResource(TaskUserHistoryService taskUserHistoryService) {
        this.taskUserHistoryService = taskUserHistoryService;
    }

    /**
     * POST  /task-user-histories : Create a new TaskUserHistory.
     *
     * @param taskUserHistoryDTO the TaskUserHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new TaskUserHistoryDTO, or with status 400 (Bad Request) if the TaskUserHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-user-histories")
    @Timed
    public ResponseEntity<TaskUserHistoryDTO> createTaskUserHistory(@Valid @RequestBody TaskUserHistoryDTO taskUserHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save TaskUserHistory : {}", taskUserHistoryDTO);
        if (taskUserHistoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new TaskUserHistory cannot already have an ID")).body(null);
        }
        TaskUserHistoryDTO result = taskUserHistoryService.save(taskUserHistoryDTO);
        return ResponseEntity.created(new URI("/api/task-user-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-user-histories : Updates an existing TaskUserHistory.
     *
     * @param taskUserHistoryDTO the TaskUserHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated TaskUserHistoryDTO,
     * or with status 400 (Bad Request) if the TaskUserHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the TaskUserHistoryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-usern-histories")
    @Timed
    public ResponseEntity<TaskUserHistoryDTO> updateTaskUserHistory(@Valid @RequestBody TaskUserHistoryDTO taskUserHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update TaskUserHistory : {}", taskUserHistoryDTO);
        if (taskUserHistoryDTO.getId() == null) {
            return createTaskUserHistory(taskUserHistoryDTO);
        }
        TaskUserHistoryDTO result = taskUserHistoryService.save(taskUserHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskUserHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-user-histories : get all the userLoginHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userLoginHistories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/task-user-histories")
    @Timed
    public ResponseEntity<List<TaskUserHistoryDTO>> getAllUserLoginHistories(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserLoginHistories");
        Page<TaskUserHistoryDTO> page = taskUserHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-user-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /task-user-histories/:id : get the "id" TaskUserHistory.
     *
     * @param id the id of the TaskUserHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the TaskUserHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/task-user-histories/{id}")
    @Timed
    public ResponseEntity<TaskUserHistoryDTO> getTaskUserHistory(@PathVariable Long id) {
        log.debug("REST request to get TaskUserHistory : {}", id);
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskUserHistoryDTO));
    }

    /**
     * DELETE  /task-user-histories/:id : delete the "id" TaskUserHistory.
     *
     * @param id the id of the TaskUserHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-user-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskUserHistory(@PathVariable Long id) {
        log.debug("REST request to delete TaskUserHistory : {}", id);
        taskUserHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
