package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;

import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.StockInForDataDetail;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInForDataTable;
import org.fwoxford.web.rest.util.HeaderUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(createSampleFrozenBoxAndFrozenTubeResponse(1L, frozenBoxCode)));
    }

    /**
     * 根据冻存盒CODE查询冻存盒和冻存管信息
     * @param transhipCode
     * @return
     */
    @GetMapping("/frozen-boxes/tranship/{transhipCode}")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> getFrozenTubeByTranshipCode(@PathVariable String transhipCode) {
        log.debug("REST request to get FrozenTube : {}", transhipCode);

        String[] frozenBoxCodes = {"1111111111", "2222222222", "3333333333", "4444444444", "5555555555", "6666666666", "7777777777", "8888888888", "9999999999", "0000000000"};
        List<FrozenBoxAndFrozenTubeResponse> res = new ArrayList<>();
        Long id = 1L;
        for(String code : frozenBoxCodes){
            res.add(createSampleFrozenBoxAndFrozenTubeResponse(id++, code));
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    private FrozenBoxAndFrozenTubeResponse createSampleFrozenBoxAndFrozenTubeResponse(Long id, String frozenBoxCode){
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        res.setStatus("2001");
        res.setId(id);
        res.setFrozenBoxTypeId(17L);
        res.setSampleTypeId(5L);

        res.setEquipmentId(23L);
        res.setEquipmentCode("F3-01");
        res.setAreaId(24L);
        res.setAreaCode("S01");
        res.setSupportRackId(29L);
        res.setSupportRackCode("R01");

        res.setFrozenBoxCode(frozenBoxCode);
        res.setFrozenBoxColumns("A");
        res.setFrozenBoxRows("1");

        res.setIsSplit(0);
        res.setFrozenTubeDTOS(new ArrayList<>());
        for(int i = 0; i<100; ++i){
            FrozenTubeResponse tube = new FrozenTubeResponse();
            tube.setId((id - 1) * 100 + i);
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
            res.getFrozenTubeDTOS().add(tube);
        }

        return res;
    }


    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-in", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockInForDataTable> getPageStockIn(@RequestBody DataTablesInput input) {
        List<StockInForDataTable> stockInList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockInForDataTable rowData = new StockInForDataTable();
            rowData.setId(0L + i + input.getStart());
            rowData.setRecordDate(LocalDate.now());
            rowData.setStockInDate(null);

            rowData.setTranshipCode("1234567890");
            rowData.setProjectCode("1234567890");
            rowData.setProjectSiteCode("12345");

            rowData.setCountOfBox(Math.round(100));
            rowData.setCountOfSample(rowData.getCountOfBox()*100);

            rowData.setStoreKeeper1("竹羽");
            rowData.setStoreKeeper2("景福");

            stockInList.add(rowData);
        }

        DataTablesOutput<StockInForDataTable> result = new DataTablesOutput<>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockInList);
        result.setRecordsFiltered(stockInList.size());
        result.setRecordsTotal(stockInList.size() * 10);

        return result;
    }

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-in-boxes/stock-in/{stockInCode}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockInBoxForDataTable> getPageStockInBoxes(@RequestBody DataTablesInput input, @PathVariable String stockInCode) {
        List<StockInBoxForDataTable> stockInList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockInBoxForDataTable rowData = new StockInBoxForDataTable();
            rowData.setId(0L + i + input.getStart());

//
//            rowData.setTranshipCode("1234567890");
//            rowData.setProjectCode("1234567890");
//            rowData.setProjectSiteCode("12345");
//
//            rowData.setCountOfBox(Math.round(100));
//            rowData.setCountOfSample(rowData.getCountOfBox()*100);
//
//            rowData.setStoreKeeper1("竹羽");
//            rowData.setStoreKeeper2("景福");

            stockInList.add(rowData);
        }

        DataTablesOutput<StockInBoxForDataTable> result = new DataTablesOutput<>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockInList);
        result.setRecordsFiltered(stockInList.size());
        result.setRecordsTotal(stockInList.size() * 10);

        return result;
    }

    /**
     * 根据转运code 查询入库的信息
     * @param transhipCode
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-in/tranship/{transhipCode}")
    @Timed
    public ResponseEntity<StockInForDataDetail> createStockIn(@Valid @RequestBody String transhipCode) throws URISyntaxException {
        log.debug("REST request to save stock-in : {}", transhipCode);
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        return ResponseEntity.created(new URI("/res/stock-in" + transhipCode))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, transhipCode))
            .body(stockInForDataDetail);
    }
}
