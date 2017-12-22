package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.service.TranshipTubeService;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.TranshipTubeDTO;
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
 * REST controller for managing TranshipTube.
 */
@RestController
@RequestMapping("/api")
public class TranshipTubeResource {

    private final Logger log = LoggerFactory.getLogger(TranshipTubeResource.class);

    private static final String ENTITY_NAME = "transhipTube";
        
    private final TranshipTubeService transhipTubeService;

    public TranshipTubeResource(TranshipTubeService transhipTubeService) {
        this.transhipTubeService = transhipTubeService;
    }

    /**
     * POST  /tranship-tubes : Create a new transhipTube.
     *
     * @param transhipTubeDTO the transhipTubeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipTubeDTO, or with status 400 (Bad Request) if the transhipTube has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranship-tubes")
    @Timed
    public ResponseEntity<TranshipTubeDTO> createTranshipTube(@Valid @RequestBody TranshipTubeDTO transhipTubeDTO) throws URISyntaxException {
        log.debug("REST request to save TranshipTube : {}", transhipTubeDTO);
        if (transhipTubeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transhipTube cannot already have an ID")).body(null);
        }
        TranshipTubeDTO result = transhipTubeService.save(transhipTubeDTO);
        return ResponseEntity.created(new URI("/api/tranship-tubes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranship-tubes : Updates an existing transhipTube.
     *
     * @param transhipTubeDTO the transhipTubeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipTubeDTO,
     * or with status 400 (Bad Request) if the transhipTubeDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipTubeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranship-tubes")
    @Timed
    public ResponseEntity<TranshipTubeDTO> updateTranshipTube(@Valid @RequestBody TranshipTubeDTO transhipTubeDTO) throws URISyntaxException {
        log.debug("REST request to update TranshipTube : {}", transhipTubeDTO);
        if (transhipTubeDTO.getId() == null) {
            return createTranshipTube(transhipTubeDTO);
        }
        TranshipTubeDTO result = transhipTubeService.save(transhipTubeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipTubeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranship-tubes : get all the transhipTubes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transhipTubes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranship-tubes")
    @Timed
    public ResponseEntity<List<TranshipTubeDTO>> getAllTranshipTubes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TranshipTubes");
        Page<TranshipTubeDTO> page = transhipTubeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tranship-tubes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tranship-tubes/:id : get the "id" transhipTube.
     *
     * @param id the id of the transhipTubeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipTubeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tranship-tubes/{id}")
    @Timed
    public ResponseEntity<TranshipTubeDTO> getTranshipTube(@PathVariable Long id) {
        log.debug("REST request to get TranshipTube : {}", id);
        TranshipTubeDTO transhipTubeDTO = transhipTubeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipTubeDTO));
    }

    /**
     * DELETE  /tranship-tubes/:id : delete the "id" transhipTube.
     *
     * @param id the id of the transhipTubeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranship-tubes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranshipTube(@PathVariable Long id) {
        log.debug("REST request to delete TranshipTube : {}", id);
        transhipTubeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 销毁转运冻存管
     * @param transhipTubeDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/tranship-tubes/destroy/{boxId}")
    @Timed
    public ResponseEntity<TranshipBoxDTO> destroyTranshipTube(@Valid @RequestBody TranshipTubeDTO transhipTubeDTO , @PathVariable Long boxId) throws URISyntaxException {
        log.debug("REST request to destroy TranshipTubes : {}", transhipTubeDTO);
        TranshipBoxDTO transhipBoxDTO = transhipTubeService.destroyTranshipTube(transhipTubeDTO,boxId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipTubeDTO.getId().toString())).body(transhipBoxDTO);
    }

    /**
     * 销毁归还冻存管
     * @param transhipTubeDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/return-tubes/destroy/{boxId}")
    @Timed
    public ResponseEntity<TranshipBoxDTO> destroyTranshipTubeForReturnBack(@Valid @RequestBody TranshipTubeDTO transhipTubeDTO, @PathVariable Long boxId) throws URISyntaxException {
        log.debug("REST request to destroy TranshipTubes : {}", transhipTubeDTO);
        TranshipBoxDTO transhipBoxDTO =   transhipTubeService.destroyTranshipTube(transhipTubeDTO,boxId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipTubeDTO.getId().toString())).body(transhipBoxDTO);
    }
}
