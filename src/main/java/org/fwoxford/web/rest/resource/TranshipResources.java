package org.fwoxford.web.rest.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.response.TranshipResponse;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing Tranship.
 */
@RestController
@RequestMapping("/api/res")
public class TranshipResources {

    private final Logger log = LoggerFactory.getLogger(TranshipResources.class);

    private static final String ENTITY_NAME = "tranship";

    private final TranshipService transhipService;

    public TranshipResources(TranshipService transhipService) {
        this.transhipService = transhipService;
    }

    /**
     * POST  /tranships : Create a new tranship.
     *
     * @param transhipDTO the transhipDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipDTO, or with status 400 (Bad Request) if the tranship has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranships")
    @Timed
    public ResponseEntity<TranshipDTO> createTranship(@Valid @RequestBody TranshipDTO transhipDTO) throws URISyntaxException {
        log.debug("REST request to save Tranship : {}", transhipDTO);
        if (transhipDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tranship cannot already have an ID")).body(null);
        }
        TranshipDTO result = transhipService.save(transhipDTO);
        return ResponseEntity.created(new URI("/api/tranships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranships : Updates an existing tranship.
     *
     * @param transhipDTO the transhipDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipDTO,
     * or with status 400 (Bad Request) if the transhipDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranships")
    @Timed
    public ResponseEntity<TranshipDTO> updateTranship(@Valid @RequestBody TranshipDTO transhipDTO) throws URISyntaxException {
        log.debug("REST request to update Tranship : {}", transhipDTO);
        if (transhipDTO.getId() == null) {
            return createTranship(transhipDTO);
        }
        TranshipDTO result = transhipService.save(transhipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranships : get all the tranships. 获取转运记录
     *
     * @param input the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tranships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "tranships", method = RequestMethod.GET)
    public DataTablesOutput<TranshipResponse> getUsers( DataTablesInput input) {
        return transhipService.findAllTranship(input);
    }
    /**
     * GET  /tranships/:id : get the "id" tranship.
     *
     * @param id the id of the transhipDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipDTO, or with status 404 (Not Found)
     */
//    @GetMapping("/tranships/{id}")
//    @Timed
//    public ResponseEntity<TranshipDTO> getTranship(@PathVariable Long id) {
//        log.debug("REST request to get Tranship : {}", id);
//        TranshipDTO transhipDTO = transhipService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipDTO));
//    }
    @GetMapping("/tranships/{id}")
    @Timed
    public ResponseEntity<TranshipDTO> getTranship(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        TranshipDTO transhipDTO = transhipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipDTO));
    }
    /**
     * DELETE  /tranships/:id : delete the "id" tranship.
     *
     * @param id the id of the transhipDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranships/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranship(@PathVariable Long id) {
        log.debug("REST request to delete Tranship : {}", id);
        transhipService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
