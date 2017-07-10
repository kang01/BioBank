package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.SerialNoService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.SerialNoDTO;
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
 * REST controller for managing SerialNo.
 */
@RestController
@RequestMapping("/api")
public class SerialNoResource {

    private final Logger log = LoggerFactory.getLogger(SerialNoResource.class);

    private static final String ENTITY_NAME = "serialNo";
        
    private final SerialNoService serialNoService;

    public SerialNoResource(SerialNoService serialNoService) {
        this.serialNoService = serialNoService;
    }

    /**
     * POST  /serial-nos : Create a new serialNo.
     *
     * @param serialNoDTO the serialNoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serialNoDTO, or with status 400 (Bad Request) if the serialNo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/serial-nos")
    @Timed
    public ResponseEntity<SerialNoDTO> createSerialNo(@Valid @RequestBody SerialNoDTO serialNoDTO) throws URISyntaxException {
        log.debug("REST request to save SerialNo : {}", serialNoDTO);
        if (serialNoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new serialNo cannot already have an ID")).body(null);
        }
        SerialNoDTO result = serialNoService.save(serialNoDTO);
        return ResponseEntity.created(new URI("/api/serial-nos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /serial-nos : Updates an existing serialNo.
     *
     * @param serialNoDTO the serialNoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serialNoDTO,
     * or with status 400 (Bad Request) if the serialNoDTO is not valid,
     * or with status 500 (Internal Server Error) if the serialNoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/serial-nos")
    @Timed
    public ResponseEntity<SerialNoDTO> updateSerialNo(@Valid @RequestBody SerialNoDTO serialNoDTO) throws URISyntaxException {
        log.debug("REST request to update SerialNo : {}", serialNoDTO);
        if (serialNoDTO.getId() == null) {
            return createSerialNo(serialNoDTO);
        }
        SerialNoDTO result = serialNoService.save(serialNoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serialNoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /serial-nos : get all the serialNos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serialNos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/serial-nos")
    @Timed
    public ResponseEntity<List<SerialNoDTO>> getAllSerialNos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SerialNos");
        Page<SerialNoDTO> page = serialNoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/serial-nos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /serial-nos/:id : get the "id" serialNo.
     *
     * @param id the id of the serialNoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serialNoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/serial-nos/{id}")
    @Timed
    public ResponseEntity<SerialNoDTO> getSerialNo(@PathVariable Long id) {
        log.debug("REST request to get SerialNo : {}", id);
        SerialNoDTO serialNoDTO = serialNoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(serialNoDTO));
    }

    /**
     * DELETE  /serial-nos/:id : delete the "id" serialNo.
     *
     * @param id the id of the serialNoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/serial-nos/{id}")
    @Timed
    public ResponseEntity<Void> deleteSerialNo(@PathVariable Long id) {
        log.debug("REST request to delete SerialNo : {}", id);
        serialNoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
