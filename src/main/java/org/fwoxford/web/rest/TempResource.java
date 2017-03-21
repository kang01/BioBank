package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing Tranship.
 */
@RestController
@RequestMapping("/api/temp")
public class TempResource {

    private final Logger log = LoggerFactory.getLogger(TempResource.class);

    private static final String ENTITY_NAME = "tranship";

    private final TranshipService transhipService;

    public TempResource(TranshipService transhipService) {
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
     * GET  /tranships : get all the tranships.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tranships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranships")
    @Timed
    public ResponseEntity<JSONArray> getAllTranships(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tranships");
        //读取转运记录
        String jsonString = "[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"projectCode\": \"P_00007\",\n" +
            "    \"projectSiteCode\": \"PS_00007001\",\n" +
            "    \"receiveDate\": \"2017-03-20\",\n" +
            "    \"sampleSatisfaction\": 1,\n" +
            "    \"transhipDate\": \"2017-03-20\",\n" +
            "    \"transhipState\": \"01\"\n" +
            "  }\n" +
            "]";
        List<Map<String,Object>> alist = new ArrayList<>();
        for(int i = 0 ; i < 9 ; i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id",i);
            map.put("projectCode","P_00007"+i);
            map.put("projectSiteCode","PS_0000700"+i);
            map.put("receiveDate",new Date().getTime());
            map.put("sampleSatisfaction",5);
            map.put("transhipDate",new Date().getTime());
            map.put("transhipState","01");
            alist.add(map);
        }

        String jsonStr = JSONArray.fromObject(alist).toString();
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);

        HttpHeaders headers = getHeaders("/api/temp/tranships");
        return new ResponseEntity<>(jsonArray, headers, HttpStatus.OK);
    }
    /**
     * GET  /tranships/:id : get the "id" tranship.
     *
     * @param id the id of the transhipDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipDTO, or with status 404 (Not Found)
     */
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

    private static HttpHeaders getHeaders( String baseUrl) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + Long.toString(10));
        String link = "";
        if ((10 + 1) < 1) {
            link = "<" + generateUri(baseUrl, 10 + 1, 1) + ">; rel=\"next\",";
        }
        // prev link
        if ((10) > 0) {
            link += "<" + generateUri(baseUrl, 10 - 1, 1) + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (10 > 0) {
            lastPage = 10 - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, 0) + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, 0) + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    private static String generateUri(String baseUrl, int page, int size) throws URISyntaxException {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString();
    }
}
