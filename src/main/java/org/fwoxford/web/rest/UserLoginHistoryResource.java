package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.UserLoginHistoryService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.UserLoginHistoryDTO;
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
 * REST controller for managing UserLoginHistory.
 */
@RestController
@RequestMapping("/api")
public class UserLoginHistoryResource {

    private final Logger log = LoggerFactory.getLogger(UserLoginHistoryResource.class);

    private static final String ENTITY_NAME = "userLoginHistory";
        
    private final UserLoginHistoryService userLoginHistoryService;

    public UserLoginHistoryResource(UserLoginHistoryService userLoginHistoryService) {
        this.userLoginHistoryService = userLoginHistoryService;
    }

    /**
     * POST  /user-login-histories : Create a new userLoginHistory.
     *
     * @param userLoginHistoryDTO the userLoginHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userLoginHistoryDTO, or with status 400 (Bad Request) if the userLoginHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-login-histories")
    @Timed
    public ResponseEntity<UserLoginHistoryDTO> createUserLoginHistory(@Valid @RequestBody UserLoginHistoryDTO userLoginHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save UserLoginHistory : {}", userLoginHistoryDTO);
        if (userLoginHistoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userLoginHistory cannot already have an ID")).body(null);
        }
        UserLoginHistoryDTO result = userLoginHistoryService.save(userLoginHistoryDTO);
        return ResponseEntity.created(new URI("/api/user-login-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-login-histories : Updates an existing userLoginHistory.
     *
     * @param userLoginHistoryDTO the userLoginHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userLoginHistoryDTO,
     * or with status 400 (Bad Request) if the userLoginHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the userLoginHistoryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-login-histories")
    @Timed
    public ResponseEntity<UserLoginHistoryDTO> updateUserLoginHistory(@Valid @RequestBody UserLoginHistoryDTO userLoginHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update UserLoginHistory : {}", userLoginHistoryDTO);
        if (userLoginHistoryDTO.getId() == null) {
            return createUserLoginHistory(userLoginHistoryDTO);
        }
        UserLoginHistoryDTO result = userLoginHistoryService.save(userLoginHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userLoginHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-login-histories : get all the userLoginHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userLoginHistories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-login-histories")
    @Timed
    public ResponseEntity<List<UserLoginHistoryDTO>> getAllUserLoginHistories(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserLoginHistories");
        Page<UserLoginHistoryDTO> page = userLoginHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-login-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-login-histories/:id : get the "id" userLoginHistory.
     *
     * @param id the id of the userLoginHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userLoginHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-login-histories/{id}")
    @Timed
    public ResponseEntity<UserLoginHistoryDTO> getUserLoginHistory(@PathVariable Long id) {
        log.debug("REST request to get UserLoginHistory : {}", id);
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userLoginHistoryDTO));
    }

    /**
     * DELETE  /user-login-histories/:id : delete the "id" userLoginHistory.
     *
     * @param id the id of the userLoginHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-login-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserLoginHistory(@PathVariable Long id) {
        log.debug("REST request to delete UserLoginHistory : {}", id);
        userLoginHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
