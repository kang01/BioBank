package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;

import org.fwoxford.domain.Tranship;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.web.rest.util.HeaderUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
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
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipDTO, or with status 400 (Bad Request) if the tranship has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranships")
    @Timed
    public ResponseEntity<JSONObject> createTranship(@Valid @RequestBody JSONObject jsonObject) throws URISyntaxException {
        log.debug("REST request to save Tranship : {}", jsonObject);
        return ResponseEntity.created(new URI("/api/tranships/" + 1))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, "1"))
            .body(jsonObject);
    }

    /**
     * PUT  /tranships : Updates an existing tranship.
     *
     * @param jsonObject the transhipDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipDTO,
     * or with status 400 (Bad Request) if the transhipDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranships")
    @Timed
    public ResponseEntity<JSONObject> updateTranship(@Valid @RequestBody JSONObject jsonObject) throws URISyntaxException {
        log.debug("REST request to update Tranship : {}", jsonObject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "1"))
            .body(jsonObject);
    }

    /**
     * GET  /tranships : get all the tranships.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tranships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
//    @GetMapping("/tranships")
//    @Timed
//    public ResponseEntity<JSONArray> getAllTranships(@ApiParam Pageable pageable)
//        throws URISyntaxException {
//        log.debug("REST request to get a page of Tranships");
//        //读取转运记录
//        String jsonString = "[\n" +
//            "  {\n" +
//            "    \"id\": 1,\n" +
//            "    \"projectCode\": \"P_00007\",\n" +
//            "    \"projectSiteCode\": \"PS_00007001\",\n" +
//            "    \"receiveDate\": \"2017-03-20\",\n" +
//            "    \"sampleSatisfaction\": 1,\n" +
//            "    \"transhipDate\": \"2017-03-20\",\n" +
//            "    \"transhipState\": \"01\"\n" +
//            "  }\n" +
//            "]";
//        List<Map<String,Object>> alist = new ArrayList<>();
//        for(int i = 0 ; i < 9 ; i++){
//            Map<String,Object> map = new HashMap<>();
//            map.put("id",i);
//            map.put("projectCode","P_00007"+i);
//            map.put("projectSiteCode","PS_0000700"+i);
//            map.put("receiveDate",new Date().getTime());
//            map.put("sampleSatisfaction",5);
//            map.put("transhipDate",new Date().getTime());
//            map.put("transhipReceive","李四");
//            map.put("transhipState","01");
//            map.put("receiver","高康康"+i);
//            alist.add(map);
//        }
//
//        String jsonStr = JSONArray.fromObject(alist).toString();
//        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
//
//        HttpHeaders headers = getHeaders("/api/temp/tranships");
//        return new ResponseEntity<>(jsonArray, headers, HttpStatus.OK);
//    }
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

    /**
     * 根据冻存盒CODE查询冻存盒和冻存管信息
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/frozen-boxes/code/{frozenBoxCode}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByForzenBoxCode(@PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        res.setStatus("2001");
        res.setId(1L);
        res.setFrozenBoxTypeId(1L);
        res.setSampleTypeId(5L);

        res.setEquipmentId(1L);
        res.setEquipmentCode("F3-01");
        res.setAreaId(1L);
        res.setAreaCode("S01");
        res.setSupportRackId(1L);
        res.setSupportRackCode("R01");

        res.setFrozenBoxCode(frozenBoxCode);
        res.setFrozenBoxColumns("A");
        res.setFrozenBoxRows("1");

        res.setIsSplit("0");
        res.setFrozenTubeResponseList(new ArrayList<>());
        for(int i = 0; i<100; ++i){
            FrozenTubeResponse tube = new FrozenTubeResponse();
            tube.setId((long)i);
            tube.setStatus("3001");
            tube.setProjectId(1L);
            tube.setProjectCode("1234567890");

            tube.setSampleTypeId(res.getSampleTypeId());
            tube.setSampleTypeCode("S_TYPE_00001");
            tube.setSampleTypeName("");

            tube.setFrozenTubeTypeId(1L);
            tube.setFrozenTubeTypeName("");

//            tube.setSampleCode("");
            tube.setFrozenTubeCode("");

            tube.setTubeColumns((i % 10 + 1) + "");
            tube.setTubeRows(String.valueOf((char) (65 + i / 10)));
            tube.setSampleCode(String.format("%s-%s%2s",  res.getFrozenBoxCode(), tube.getTubeRows(), tube.getTubeColumns()).replace(" ", "0").hashCode() + "");
//            tube.setSampleTempCode(String.format("%s-%s%2s",  res.getFrozenBoxCode(), tube.getTubeRows(), tube.getTubeColumns()).replace(" ", "0"));

            tube.setMemo("");
            tube.setErrorType("");
            res.getFrozenTubeResponseList().add(tube);
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 根据冻存盒CODE字符串查询冻存盒和冻存管信息
     * @param frozenBoxCodeStr
     * @return
     */
    @GetMapping("/findFrozenBoxAndTubeByBoxCodes/{frozenBoxCodeStr}")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> getFrozenBoxAndTubeListByBoxCodeStr(@PathVariable  String frozenBoxCodeStr) {
        log.debug("REST request to get FrozenBoxAndTube By codes : {}", frozenBoxCodeStr);
        List<FrozenBoxAndFrozenTubeResponse> res = new ArrayList<>();

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
}
