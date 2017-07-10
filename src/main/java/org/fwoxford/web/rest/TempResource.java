package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.Area;
import org.fwoxford.domain.Equipment;
import org.fwoxford.domain.SampleClassification;
import org.fwoxford.domain.StockOutHandover;
import org.fwoxford.service.ProjectService;
import org.fwoxford.service.SampleTypeService;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.SampleTypeMapper;
import org.fwoxford.web.rest.util.BankUtil;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInForDataTable;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private BankUtil bankUtil;
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
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(createSampleFrozenBoxAndFrozenTubeResponse(1L, frozenBoxCode,null, 100)));
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
            res.add(createSampleFrozenBoxAndFrozenTubeResponse(id++, code, null, 100));
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    private FrozenBoxAndFrozenTubeResponse createSampleFrozenBoxAndFrozenTubeResponse(Long id, String frozenBoxCode, String sampleTypeCode, int countOfSample){
        List<SampleTypeDTO> types = sampleTypeService.findAllSampleTypes();
        SampleTypeDTO typeDTO = types.get(new Random().nextInt(10));
        if (sampleTypeCode == null){
            typeDTO = types.stream().filter(t->t.getSampleTypeCode() != null && t.getSampleTypeCode().equals(sampleTypeCode)).findFirst().orElse(null);
        }

        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        res.setStatus("2001");
        res.setId(id);
//        res.setFrozenBoxType();
//        res.setSampleType(typeDTO);
        res.setEquipmentId(23L);
        res.setAreaId(24L);
        res.setSupportRackId(29L);

        res.setFrozenBoxCode(frozenBoxCode);

        res.setIsSplit(0);
        res.setFrozenTubeDTOS(new ArrayList<>());
        for(int i = 0; i<countOfSample; ++i){
            FrozenTubeResponse tube = new FrozenTubeResponse();
            tube.setId((id - 1) * 100 + i);
            tube.setStatus("3001");

//            tube.setSampleType(typeDTO);
//            tube.setFrozenTubeType(1L);

            tube.setTubeColumns((i % 10 + 1) + "");
            tube.setTubeRows(String.valueOf((char) (65 + i / 10)));
            tube.setSampleCode(String.format("%s-%s%2s",  res.getFrozenBoxCode(), tube.getTubeRows(), tube.getTubeColumns()).replace(" ", "0").hashCode() + "");
//            tube.setSampleTempCode(String.format("%s-%s%2s",  res.getFrozenBoxCode(), tube.getTubeRows(), tube.getTubeColumns()).replace(" ", "0"));

            tube.setMemo("");
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

    /**
     * 根据入库单编码，查询入库单的盒子
     * @param input
     * @param stockInCode
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-in-boxes/stock-in/{stockInCode}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockInBoxForDataTable> getPageStockInBoxes(@RequestBody DataTablesInput input, @PathVariable String stockInCode) {
        List<StockInBoxForDataTable> stockInList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockInBoxForDataTable rowData = new StockInBoxForDataTable();
            rowData.setId(0L + i + input.getStart());

            rowData.setFrozenBoxCode("1234567890-"+i);
            rowData.setCountOfSample(100);
            rowData.setIsSplit(0);
            rowData.setPosition("F3-71.S01");
            rowData.setSampleTypeName("99");
            rowData.setStatus("2002");
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

    @Autowired
    private SampleTypeService sampleTypeService;

    /**
     * 输入项目编码和样本类型编码，返回该入库单的某个盒子的信息
     * @param projectCode
     * @param sampleTypeCode
     * @return
     */
    @RequestMapping(value = "/frozen-boxes/incomplete-boxes/project/{projectCode}/type/{sampleTypeCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<StockInBoxForChangingPosition> getIncompleteFrozenBoxes(@PathVariable String projectCode, @PathVariable String sampleTypeCode) {
        List<StockInBoxForChangingPosition> boxes =  new ArrayList<>();
        Random random = new Random();

        StockInBoxForChangingPosition rowData = new StockInBoxForChangingPosition();
        rowData = createStockInBoxForDataMoved(random.nextLong(), "1234567890", sampleTypeCode, 90);
        boxes.add(rowData);

        return boxes;
    }
    @Autowired
    private SampleTypeMapper sampleTypeMapper;
    private StockInBoxForChangingPosition createStockInBoxForDataMoved(long id, String frozenBoxCode, String sampleTypeCode, int countOfSample) {
        StockInBoxForChangingPosition res = new StockInBoxForChangingPosition();
        List<SampleTypeDTO> types = sampleTypeService.findAllSampleTypes();
        SampleTypeDTO typeDTO = new SampleTypeDTO();
        for(SampleTypeDTO type :types){
            if(sampleTypeCode.equals(type.getSampleTypeCode())){
                typeDTO = type;
            }
        }
//        SampleTypeDTO typeDTO = types.get(new Random().nextInt(10));
        if (sampleTypeCode == null){
            typeDTO = types.stream().filter(t->t.getSampleTypeCode() != null && t.getSampleTypeCode().equals(sampleTypeCode)).findFirst().orElse(null);
        }
        res.setSampleType(typeDTO);
        res.setCountOfSample(countOfSample);
        res.setFrozenBoxId(id);
        res.setFrozenBoxCode(frozenBoxCode);
        res.setFrozenBoxTypeColumns("10");
        res.setFrozenBoxTypeRows("10");
        res.setIsSplit(1);
        res.setStockInFrozenTubeList(new ArrayList<>());
        for(int i = 0; i<countOfSample; ++i){
            StockInTubeForBox tube = new StockInTubeForBox();
            tube.setId((id - 1) * 100 + i);
            tube.setFrozenBoxCode(frozenBoxCode);
            tube.setTubeColumns((i % 10 + 1) + "");
            tube.setTubeRows(String.valueOf((char) (65 + i / 10)));
            res.getStockInFrozenTubeList().add(tube);
        }

        return res;
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
        stockInForDataDetail.setId(1L);
        stockInForDataDetail.setTranshipCode(transhipCode);
        stockInForDataDetail.setProjectCode("P00001");
        stockInForDataDetail.setProjectSiteCode("PS00001");
        stockInForDataDetail.setReceiveDate(LocalDate.now());
        stockInForDataDetail.setReceiver("小高");
        stockInForDataDetail.setStatus("7001");
        stockInForDataDetail.setStockInCode(bankUtil.getUniqueID("B"));
        stockInForDataDetail.setStockInDate(LocalDate.now());
        return ResponseEntity.created(new URI("/res/stock-in" + stockInForDataDetail.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, stockInForDataDetail.getId().toString()))
            .body(stockInForDataDetail);
    }

    /**
     * 输入入库单编码，返回入库信息---入库完成
     * @param stockInCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-in/{stockInCode}/completed")
    @Timed
    public ResponseEntity<StockInForDataDetail> completedStockIn(@Valid @RequestBody String stockInCode) throws URISyntaxException {
        log.debug("REST request to update StockIn : {}", stockInCode);
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        stockInForDataDetail.setId(1L);
        stockInForDataDetail.setTranshipCode(bankUtil.getUniqueID("A"));
        stockInForDataDetail.setProjectCode("P00001");
        stockInForDataDetail.setProjectSiteCode("PS00001");
        stockInForDataDetail.setReceiveDate(LocalDate.now());
        stockInForDataDetail.setReceiver("小高");
        stockInForDataDetail.setStatus("7002");
        stockInForDataDetail.setStockInCode(stockInCode);
        stockInForDataDetail.setStockInDate(LocalDate.now());
        stockInForDataDetail.setStoreKeeper1("小张");
        stockInForDataDetail.setStoreKeeper2("小黄");
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInForDataDetail.getId().toString()))
            .body(stockInForDataDetail);
    }

    /**
     * 输入入库单ID，返回入库信息
     * @param id
     * @return
     */
    @GetMapping("/stock-in/{id}")
    @Timed
    public ResponseEntity<StockInForDataDetail> getStockIn(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        stockInForDataDetail.setId(id);
        stockInForDataDetail.setTranshipCode(bankUtil.getUniqueID("A"));
        stockInForDataDetail.setProjectCode("P00001");
        stockInForDataDetail.setProjectSiteCode("PS00001");
        stockInForDataDetail.setReceiveDate(LocalDate.now());
        stockInForDataDetail.setReceiver("小高");
        stockInForDataDetail.setStatus("7002");
        stockInForDataDetail.setStockInCode(bankUtil.getUniqueID("B"));
        stockInForDataDetail.setStockInDate(LocalDate.now());
        stockInForDataDetail.setStoreKeeper1("小张");
        stockInForDataDetail.setStoreKeeper2("小黄");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInForDataDetail));
    }

    /**
     * 输入转运编码，返回相关入库信息
     * @param transhipCode
     * @return
     */
    @GetMapping("/stock-in/tranship/{transhipCode}")
    @Timed
    public ResponseEntity<StockInForDataDetail> getStockIn(@PathVariable String transhipCode) {
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        stockInForDataDetail.setId(1L);
        stockInForDataDetail.setTranshipCode(transhipCode);
        stockInForDataDetail.setProjectCode("P00001");
        stockInForDataDetail.setProjectSiteCode("PS00001");
        stockInForDataDetail.setReceiveDate(LocalDate.now());
        stockInForDataDetail.setReceiver("小高");
        stockInForDataDetail.setStatus("7002");
        stockInForDataDetail.setStockInCode(bankUtil.getUniqueID("B"));
        stockInForDataDetail.setStockInDate(LocalDate.now());
        stockInForDataDetail.setStoreKeeper1("小张");
        stockInForDataDetail.setStoreKeeper2("小黄");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInForDataDetail));
    }
    /**
     * 输入入库单编码和盒子编码，返回该入库单的某个盒子的信息
     * @param stockInCode
     * @param boxCode
     * @return
     */
    @GetMapping("/stock-in-boxes/stock-in/{stockInCode}/box/{boxCode}")
    @Timed
    public ResponseEntity<StockInBoxDetail> getStockIn(@PathVariable String stockInCode, @PathVariable String boxCode) {
        List<SupportRackDTO> shelfs = createSupportRackList(1L, "F3-71", "S01");
        SupportRackDTO shelf = shelfs.get(0);
        SampleTypeDTO sampleType = sampleTypeService.findAllSampleTypes().get(0);
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        stockInBoxDetail.setFrozenBoxId(1L);
        stockInBoxDetail.setFrozenBoxCode(boxCode);
        stockInBoxDetail.setCountOfSample(100);
        stockInBoxDetail.setIsSplit(0);
        stockInBoxDetail.setSampleType(sampleType);
        stockInBoxDetail.setStatus("2002");
        stockInBoxDetail.setMemo("");
        stockInBoxDetail.setStockInCode(stockInCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInBoxDetail));
    }
    /**
     * 输入入库单编码和盒子编码，以及分装后的盒子，返回保存好的分装后盒子的信息。
     * @param stockInCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-in-boxes/stock-in/{stockInCode}/box/{boxCode}/splited")
    @Timed
    public ResponseEntity<StockInBoxSplit> splitedStockIn(@Valid @RequestBody String stockInCode, String boxCode, StockInBoxSplit stockInBoxForDataSplit) throws URISyntaxException {
        StockInBoxSplit detail = stockInBoxForDataSplit;
        stockInBoxForDataSplit.setFrozenBoxId(1L);
        List<StockInTubeDTO> tubeDTOS = stockInBoxForDataSplit.getStockInFrozenTubeList();
        List<StockInTubeDTO> tubeDTOList = new ArrayList<>();
        for(int i = 0 ; i < tubeDTOList.size();i++){
            tubeDTOList.get(i).setId(0L+i);
            tubeDTOList.add(tubeDTOList.get(i));
        }
        detail.setStockInFrozenTubeList(tubeDTOList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, detail.getFrozenBoxId().toString()))
            .body(detail);
    }
    /**
     * 入库盒子上架 :输入入库单编码和盒子编码，以及冻存位置信息，返回保存后的盒子信息
     * @param stockInCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-in-boxes/stock-in/{stockInCode}/box/{boxCode}/moved")
    @Timed
    public ResponseEntity<StockInBoxDetail> movedStockIn(@Valid @RequestBody String stockInCode, String boxCode, FrozenBoxPositionDTO boxPositionDTO) throws URISyntaxException {
        List<SupportRackDTO> shelfs = createSupportRackList(1L, "F3-71", "S01");
        SupportRackDTO shelf = shelfs.get(0);
        SampleTypeDTO sampleType = sampleTypeService.findAllSampleTypes().get(0);
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        stockInBoxDetail.setFrozenBoxId(1L);
        stockInBoxDetail.setFrozenBoxCode(boxCode);
        stockInBoxDetail.setCountOfSample(100);
        stockInBoxDetail.setIsSplit(0);
        stockInBoxDetail.setSupportRackId(shelf.getId());
        stockInBoxDetail.setAreaId(shelf.getAreaId());
        stockInBoxDetail.setEquipmentId(shelf.getArea().getEquipment().getId());
        stockInBoxDetail.setSampleType(sampleType);
        stockInBoxDetail.setStatus("2002");
        stockInBoxDetail.setMemo("");
        stockInBoxDetail.setStockInCode(stockInCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInBoxDetail.getFrozenBoxId().toString()))
            .body(stockInBoxDetail);
    }
    /**
     * 输入设备编码，返回该设备下的所有架子
     * @param equipmentCode
     * @return
     */
    @GetMapping("/frozen-pos/shelves/{equipmentCode}")
    @Timed
    public ResponseEntity<List<SupportRackDTO>> getSupportRackList(@PathVariable String equipmentCode) {
        List<SupportRackDTO> supportRackDTOS = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            SupportRackDTO supportRackDTO = new SupportRackDTO();
            supportRackDTO.setId(0L+i);
            supportRackDTO.setStatus("0001");
            supportRackDTO.setSupportRackCode("R-"+i);
            supportRackDTO.setAreaId(1L);
            supportRackDTO.setAreaCode("S1");
            supportRackDTO.setSupportRackTypeId(26L);
            supportRackDTOS.add(supportRackDTO);
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supportRackDTOS));
    }

    @RequestMapping(value = "/frozen-pos/incomplete-shelves/{equipmentCode}/{areaCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<SupportRackDTO> getIncompleteShelves(@PathVariable String equipmentCode, @PathVariable String areaCode) {
        List<SupportRackDTO> result =  new ArrayList<>();
        Random random = new Random();

        for (int i=0;i<1;++i) {
            result.addAll(createSupportRackList((long)i, equipmentCode, areaCode));
        }

        return result;
    }

    @RequestMapping(value = "/frozen-pos/incomplete-shelves/{equipmentCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<SupportRackDTO> getIncompleteShelves(@PathVariable String equipmentCode) {
        List<SupportRackDTO> result =  new ArrayList<>();
        Random random = new Random();

        for (int i=0;i<1;++i) {
            result.addAll(createSupportRackList((long)i, equipmentCode, null));
        }

        return result;
    }

    private List<SupportRackDTO> createSupportRackList(Long equipmentId, String equipmentCode, String areaCode){
        List<SupportRackDTO> result =  new ArrayList<>();
        Random random = new Random();

        Equipment equipment = new Equipment();
        equipment.setId(equipmentId);
        equipment.setEquipmentCode(equipmentCode);
        for (int j=1;j<=10;++j) {
            Area area = new Area();
            area.setId(j+equipmentId*10);
            area.setEquipment(equipment);
            area.setEquipmentCode(equipment.getEquipmentCode());
            area.setFreezeFrameNumber(10);
            for (int k = 1; k <= 10; ++k) {
                SupportRackDTO shelf = new SupportRackDTO();
                shelf.setId(k+j*10+equipmentId*100);
                shelf.setArea(area);
                shelf.setAreaId(area.getId());
                shelf.setAreaCode(area.getAreaCode());

                shelf.setStatus("01");
                shelf.setMemo("");
                shelf.setSupportRackCode(String.format("R%2s", k).replace(" ", "0"));
                shelf.setSupportRackTypeId(26L);
                shelf.setSupportRackTypeCode("S_RACK_0001");



                result.add(shelf);
            }

            if (areaCode != null){
                area.setAreaCode(areaCode);
                break;
            } else {
                area.setAreaCode(String.format("S%2s", j).replace(" ", "0"));
            }

        }

        return result;
    }


    @GetMapping("/frozen-boxes/pos/{equipmentCode}/{areaCode}/{shelfCode}")
    @Timed
    public ResponseEntity<List<StockInBoxDetail>> movedStockIn(@PathVariable String equipmentCode, @PathVariable String areaCode, @PathVariable String shelfCode) throws URISyntaxException {
        List<SupportRackDTO> shelves = createSupportRackList(1L, equipmentCode, areaCode);
        SupportRackDTO shelf = shelves.get(0);
        shelf.setSupportRackCode(shelfCode);

        SampleTypeDTO sampleType = sampleTypeService.findAllSampleTypes().get(0);
        List<StockInBoxDetail> result = new ArrayList<>();
        Random random = new Random();
        int maxBox = random.nextInt(10);
        for (int i=0;i<maxBox;++i) {
            StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
            stockInBoxDetail.setFrozenBoxId(1L);
            stockInBoxDetail.setFrozenBoxCode("1234567890");
            stockInBoxDetail.setCountOfSample(100);
            stockInBoxDetail.setIsSplit(0);
            stockInBoxDetail.setSupportRackId(shelf.getId());
            stockInBoxDetail.setAreaId(shelf.getAreaId());
            stockInBoxDetail.setEquipmentId(shelf.getArea().getEquipment().getId());
            stockInBoxDetail.setSampleType(sampleType);
            stockInBoxDetail.setStatus("2002");
            stockInBoxDetail.setMemo("");
            stockInBoxDetail.setStockInCode("1234567890");
            stockInBoxDetail.setColumnsInShelf(String.valueOf((char) (65 + i / 4)));
            stockInBoxDetail.setRowsInShelf(i % 4 + "");

            result.add(stockInBoxDetail);
        }
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * 查询出库申请列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-applies", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutApplyForDataTableEntity> getPageStockOutApply(@RequestBody DataTablesInput input) {
        List<StockOutApplyForDataTableEntity> stockOutApplyList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutApplyForDataTableEntity rowData = new StockOutApplyForDataTableEntity();
            rowData.setId(0L + i + input.getStart());

            rowData.setApplyCode(bankUtil.getUniqueID("C"));
            rowData.setCountOfSample(100L);
            rowData.setStatus("1101");
            rowData.setApplyPersonName("王东东");
            rowData.setApplyTime("2017-07-07至2017-07-17");
            rowData.setDelegateName("实验室");
            rowData.setPurposeOfSample("实验");
            rowData.setSampleTypes("血清，血浆");
            stockOutApplyList.add(rowData);
        }

        DataTablesOutput<StockOutApplyForDataTableEntity> result = new DataTablesOutput<StockOutApplyForDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(stockOutApplyList.size() * 10);
        return result;
    }

    /**
     * 根据上一级申请ID，取下一级出库申请列表
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-applies/parentApply/{id}")
    @Timed
    public ResponseEntity<List<StockOutApplyForDataTableEntity>> getNextStockOutApplyList(@PathVariable Long id) throws URISyntaxException {
        List<StockOutApplyForDataTableEntity> result =  new ArrayList<>();

        for (int i = 0; i < 10; ++i){
            StockOutApplyForDataTableEntity rowData = new StockOutApplyForDataTableEntity();
            rowData.setId(0L + i);
            rowData.setApplyCode(bankUtil.getUniqueID("C"));
            rowData.setCountOfSample(200L);
            rowData.setStatus("1101");
            rowData.setApplyPersonName("王西西");
            rowData.setApplyTime("2017-07-07至2017-07-17");
            rowData.setDelegateName("实验室");
            rowData.setPurposeOfSample("实验");
            rowData.setSampleTypes("血清，血浆");
            result.add(rowData);
        }
        return ResponseEntity.ok()
            .body(result);
    }

    @Autowired
    private ProjectService projectService;
    /**
     * 出库申请，查看详情接口
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-applies/{id}")
    @Timed
    public ResponseEntity<StockOutApplyDetail> get(@PathVariable Long id) throws URISyntaxException {
        StockOutApplyDetail result = new StockOutApplyDetail();
        result.setId(id);
        result.setPurposeOfSample("实验");
        result.setApplyPersonName("王东东");
        result.setStatus("1101");
        result.setApplyCode(bankUtil.getUniqueID("C"));
        result.setDelegateId(1L);
        result.setRecordId(5L);
        result.setStartTime(LocalDate.parse("2017-07-07"));
        result.setEndTime(LocalDate.parse("2017-07-17"));
        result.setRecordTime(LocalDate.now());
        List<Long> projectIds = new ArrayList<Long>();
        projectIds.add(1L);projectIds.add(2L);projectIds.add(3L);
        result.setProjectIds(projectIds);
        List<StockOutRequirementForApplyTable> stockOutRequirementForApplyTables = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            stockOutRequirementForApplyTables.add(createStockOutRequirementForApplyTable(i));
        }
        for (int j = 0; j < 3; j++) {
            stockOutRequirementForApplyTables.add(createStockOutRequirementForSureApplyTable(j));
        }
        result.setStockOutRequirement(stockOutRequirementForApplyTables);
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    private StockOutRequirementForApplyTable createStockOutRequirementForSureApplyTable(int i) {
        StockOutRequirementForApplyTable res = new StockOutRequirementForApplyTable();
        String samples = new String();
        res.setStatus("1201");
        res.setId(1L+i);
        res.setCountOfSample(10000);
        for(int j = 1 ;j<=10000;j++){
            samples+="1234567890-"+j+"-血浆";
            if(j<10000){samples+=",";};
        }
        res.setSamples(samples);
        return res;
    }

    private StockOutRequirementForApplyTable createStockOutRequirementForApplyTable(int i) {
        StockOutRequirementForApplyTable res = new StockOutRequirementForApplyTable();
        res.setStatus("1201");
        res.setId(1L+i);
        res.setAge(10+i+"-30岁");
        res.setCountOfSample(10+i*100);
        res.setDiseaseTypeId("1");
        res.setIsHemolysis(true);
        res.setIsHemolysis(false);
        res.setFrozenTubeTypeName("5L");
        res.setSampleTypeName("血浆");
        res.setSex("男");
        return res;
    }

    /**
     * 根据需求ID获取需求详情
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-requirements/{id}")
    @Timed
    public ResponseEntity<StockOutRequirementForApply> getRequirement(@PathVariable Long id) throws URISyntaxException {
        StockOutRequirementForApply result = new StockOutRequirementForApply();
        result.setId(id);
        result.setStatus("1201");
        if(id<=3){
            result.setSex("m");
            result.setAge("10;20");
            result.setIsBloodLipid(true);
            result.setCountOfSample(100);
            result.setFrozenTubeTypeId(1L);
            result.setDiseaseTypeId("1");
            result.setIsHemolysis(true);
            result.setMemo("待核对样本");
            result.setSampleTypeId(1L);
            result.setSampleClassificationId(1L);
            result.setRequirementName("100只 男性 15岁 血浆样本");
        }else{
            String samples = new String();
            for(int j = 1 ;j<=10000;j++){
                samples+="1234567890-"+j+"-血浆";
                if(j<10000){samples+=",";};
            }
            result.setSamples(samples);
        }

        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 查询出库计划列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-plans", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutPlansForDataTableEntity> getPageStockOutPlan(@RequestBody DataTablesInput input) {
        List<StockOutPlansForDataTableEntity> stockOutApplyList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutPlansForDataTableEntity rowData = new StockOutPlansForDataTableEntity();
            rowData.setId(0L + i + input.getStart());
            rowData.setApplyNumber(bankUtil.getUniqueID("C"));
            rowData.setStockOutPlanCode(bankUtil.getUniqueID("E"));
            rowData.setPlanDate(LocalDate.now());
            rowData.setPurposeOfSample("实验");
            rowData.setCountOfStockOutPlanSample(1000L);
            rowData.setCountOfStockOutTask("01/10");
            rowData.setStatus("1401");
            stockOutApplyList.add(rowData);
        }

        DataTablesOutput<StockOutPlansForDataTableEntity> result = new DataTablesOutput<StockOutPlansForDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(stockOutApplyList.size() * 10);
        return result;
    }

    /**
     * 计划查看详情
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-plans/{id}")
    @Timed
    public ResponseEntity<StockOutPlanDetail> getStockOutPlanDetail(@PathVariable Long id) throws URISyntaxException {
        StockOutPlanDetail result = new StockOutPlanDetail();
        result.setId(id);
        result.setApplyNumber(bankUtil.getUniqueID("C"));
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 根据申请单号查询申请单详情
     * @param applyNumber
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-plans/applyNumber/{applyNumber}")
    @Timed
    public ResponseEntity<List<StockOutApplyForPlan>> getStockOutApplyDetailByNumber(@PathVariable String applyNumber) throws URISyntaxException {
        List<StockOutApplyForPlan> alist = new ArrayList<StockOutApplyForPlan>();
        for(int i = 0;i<50;i++){
            StockOutApplyForPlan result = new StockOutApplyForPlan();
            result.setId(1L+i);
            result.setApplyNumber(applyNumber+i);
            alist.add(result);
        }
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(alist));
    }

    /**
     * 根据需求查询核对通过的不在任务内的冻存盒（带分页）
     * @param input
     * @param ids
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/requirement/{ids}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenBoxForTaskDataTableEntity> getPageStockOutFrozenBox(@RequestBody DataTablesInput input,@PathVariable List<Long> ids) {
        List<StockOutFrozenBoxForTaskDataTableEntity> stockOutApplyList =  new ArrayList<StockOutFrozenBoxForTaskDataTableEntity>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutFrozenBoxForTaskDataTableEntity rowData = new StockOutFrozenBoxForTaskDataTableEntity();
            rowData.setId(0L + i + input.getStart());
            rowData.setCountOfSample(20L);
            rowData.setFrozenBoxCode("98765432"+i);
            rowData.setPosition("F3-71.S01");
            rowData.setSampleTypeName("血浆");
            stockOutApplyList.add(rowData);
        }

        DataTablesOutput<StockOutFrozenBoxForTaskDataTableEntity> result = new DataTablesOutput<StockOutFrozenBoxForTaskDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(stockOutApplyList.size() * 10);
        return result;
    }

    /**
     * 根据出库申请ID以及冻存盒ID查询出库的样本
     * @param applyId
     * @param frozenBoxId
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-frozen-tubes/apply/{applyId}/frozenBox/{frozenBoxId}")
    @Timed
    public ResponseEntity<List<StockOutFrozenTubeForPlan>> getStockOutFrozenTubeForPlanByApplyAndBox(@PathVariable Long applyId,@PathVariable Long frozenBoxId) throws URISyntaxException {
       List<StockOutFrozenTubeForPlan> result = new ArrayList<StockOutFrozenTubeForPlan>();
       for(int i = 0 ; i<20 ;i++){
           StockOutFrozenTubeForPlan tube = new StockOutFrozenTubeForPlan();
           tube.setId(1L+i);
           tube.setSampleTypeName("血浆");
           tube.setStatus("3001");
           tube.setAge(65);
           tube.setSex("男");
           tube.setSampleUsedTimes(0L);
           tube.setSampleCode(String.valueOf(new Date().getTime()));
           tube.setMemo("正常");
           result.add(tube);
       }
        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 新增保存出库计划
     * @param stockOutPlanForSave
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-plans")
    @Timed
    public ResponseEntity<StockOutPlanForSave> createStockOutPlan(@Valid @RequestBody StockOutPlanForSave stockOutPlanForSave) throws URISyntaxException {
        // zhuyu 做过了 in StockOutPlanResource
        log.debug("REST request to save StockOutPlan : {}", stockOutPlanForSave);
        StockOutPlanForSave result = stockOutPlanForSave;
        return ResponseEntity.created(new URI("/api/stock-out-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 修改保存出库计划
     * @param stockOutPlanForSave
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-plans")
    @Timed
    public ResponseEntity<StockOutPlanForSave> updateStockOutPlan(@Valid @RequestBody StockOutPlanForSave stockOutPlanForSave) throws URISyntaxException {
        log.debug("REST request to save StockOutPlan : {}", stockOutPlanForSave);
        StockOutPlanForSave result = stockOutPlanForSave;
        return ResponseEntity.created(new URI("/api/stock-out-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 作废计划
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-plans/{id}/invalid")
    @Timed
    public ResponseEntity<StockOutPlanForSave> invalidStockOutPlan(@PathVariable Long id ) throws URISyntaxException {
        StockOutPlanForSave result = new StockOutPlanForSave();
        result.setId(id);
        result.setStatus(Constants.STOCK_OUT_PLAN_INVALID);
        result.setApplyId(1L);
        return ResponseEntity.created(new URI("/api/stock-out-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 新增保存任务
     * @param id
     * @param boxIds
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-tasks/plan/{id}/frozenBox/{boxIds}")
    @Timed
    public ResponseEntity<StockOutTaskDTO> createStockOutTask(@PathVariable Long id,@PathVariable List<Long> boxIds) throws URISyntaxException {
        // zhuyu 做过了 in StockOutTaskResource
        StockOutTaskDTO result = new StockOutTaskDTO();
        result.setId(1L);
        result.setUsedTime(0);
        result.setStockOutTaskCode(bankUtil.getUniqueID("F"));
        result.setStatus("");
        return ResponseEntity.created(new URI("/api/stock-out-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 删除任务
     * @param id
     * @return
     */
    @DeleteMapping("/stock-out-tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutTask(@PathVariable Long id) {
        // zhuyu 做过了 in StockOutTaskResource
        log.debug("REST request to delete StockOutTask : {}", id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 获取出库任务列表
     * @param input
     * @param id
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-tasks/plan/{id}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutTaskForPlanDataTableEntity> getPageStockTaskByPlan(@RequestBody DataTablesInput input,@PathVariable Long id) {
        List<StockOutTaskForPlanDataTableEntity> stockOutApplyList =  new ArrayList<StockOutTaskForPlanDataTableEntity>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutTaskForPlanDataTableEntity rowData = new StockOutTaskForPlanDataTableEntity();
            rowData.setId(0L + i + input.getStart());
            rowData.setCountOfSample(20L);
            rowData.setStatus("1501");
            rowData.setStockOutTaskCode(bankUtil.getUniqueID("F"));
            rowData.setMemo("");
            rowData.setCountOfFrozenBox(1L);
            rowData.setCreateDate(LocalDate.now());
            rowData.setStockOutDate(LocalDate.now());
            rowData.setOperators("小张，小王");
            stockOutApplyList.add(rowData);
        }

        DataTablesOutput<StockOutTaskForPlanDataTableEntity> result = new DataTablesOutput<StockOutTaskForPlanDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(stockOutApplyList.size() * 10);
        return result;
    }

    /**
     * 出库任务查看详情
     * @param input
     * @param id
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/task/{id}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenBoxForTaskDataTableEntity> getPageStockOutBoxForTask(@RequestBody DataTablesInput input,@PathVariable Long id) {
        List<StockOutFrozenBoxForTaskDataTableEntity> stockOutApplyList =  new ArrayList<StockOutFrozenBoxForTaskDataTableEntity>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutFrozenBoxForTaskDataTableEntity rowData = new StockOutFrozenBoxForTaskDataTableEntity();
            rowData.setId(0L + i + input.getStart());
            rowData.setCountOfSample(20L);
            rowData.setFrozenBoxCode("98765432"+i);
            rowData.setPosition("F3-71.S01");
            rowData.setSampleTypeName("血浆");
            stockOutApplyList.add(rowData);
        }

        DataTablesOutput<StockOutFrozenBoxForTaskDataTableEntity> result = new DataTablesOutput<StockOutFrozenBoxForTaskDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(stockOutApplyList);
        result.setRecordsFiltered(stockOutApplyList.size());
        result.setRecordsTotal(stockOutApplyList.size() * 10);
        return result;
    }


    /**
     * 查询出库交接列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-handovers", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutHandoverForDataTableEntity> getPageStockOutHandOver(@RequestBody DataTablesInput input) {
        List<StockOutHandoverForDataTableEntity> dataList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutHandoverForDataTableEntity rowData = new StockOutHandoverForDataTableEntity();
            rowData.setId(0L + i + input.getStart());

            rowData.setApplyCode(bankUtil.getUniqueID("C"));
            rowData.setHandoverCode(bankUtil.getUniqueID("G"));
            rowData.setCountOfSample(100L);
            rowData.setStatus("1101");
            rowData.setDeliverName("王东东");
            rowData.setReceiveDate(LocalDate.now());
            rowData.setReceiver("实验室");
            rowData.setUsage("实验");
            dataList.add(rowData);
        }

        DataTablesOutput<StockOutHandoverForDataTableEntity> result = new DataTablesOutput<>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(dataList);
        result.setRecordsFiltered(dataList.size());
        result.setRecordsTotal(dataList.size() * 10);
        return result;
    }
    /**
     * 查询已交接冻存盒
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/handover/{id}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenBoxForDataTableEntity> getPageHandoverStockOutFrozenBoxes(@PathVariable Long id, @RequestBody DataTablesInput input) {
        List<StockOutFrozenBoxForDataTableEntity> dataList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutFrozenBoxForDataTableEntity rowData = new StockOutFrozenBoxForDataTableEntity();
            rowData.setId(0L + i + input.getStart());

            rowData.setFrozenBoxCode(String.valueOf(new Date().getTime()));
            rowData.setApplyId(1L);
            rowData.setApplyCode(bankUtil.getUniqueID("C"));
            rowData.setPlanId(1L);
            rowData.setPlanCode(bankUtil.getUniqueID("E"));
            rowData.setCountOfSample(100L);
            rowData.setStatus("1101");
            rowData.setDelegateId(1L);
            rowData.setDelegate("王东东");
            rowData.setMemo("实验");
            dataList.add(rowData);
        }

        DataTablesOutput<StockOutFrozenBoxForDataTableEntity> result = new DataTablesOutput<>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(dataList);
        result.setRecordsFiltered(dataList.size());
        result.setRecordsTotal(dataList.size() * 10);
        return result;
    }

    /**
     * 查询待交接冻存盒
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-frozen-boxes/apply/{id}/waiting-handover", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutFrozenBoxForDataTableEntity> getPageStockOutFrozenBoxes(@PathVariable Long id, @RequestBody DataTablesInput input) {
        List<StockOutFrozenBoxForDataTableEntity> dataList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutFrozenBoxForDataTableEntity rowData = new StockOutFrozenBoxForDataTableEntity();
            rowData.setId(0L + i + input.getStart());

            rowData.setFrozenBoxCode(String.valueOf(new Date().getTime()));
            rowData.setApplyId(1L);
            rowData.setApplyCode(bankUtil.getUniqueID("C"));
            rowData.setPlanId(1L);
            rowData.setPlanCode(bankUtil.getUniqueID("E"));
            rowData.setTaskId(1L);
            rowData.setTaskCode(bankUtil.getUniqueID("F"));
            rowData.setCountOfSample(100L);
            rowData.setStatus("1101");
            rowData.setDelegateId(1L);
            rowData.setDelegate("王东东");
            rowData.setMemo("实验");
            dataList.add(rowData);
        }

        DataTablesOutput<StockOutFrozenBoxForDataTableEntity> result = new DataTablesOutput<>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(dataList);
        result.setRecordsFiltered(dataList.size());
        result.setRecordsTotal(dataList.size() * 10);
        return result;
    }

    /**
     * 获取交接单详情
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-handovers/{id}")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> getStockOutHandoverDetail(@PathVariable Long id) throws URISyntaxException {
        StockOutHandoverDTO result = new StockOutHandoverDTO();
        result.setId(id);
        result.setStockOutApplyId(id);
        result.setStockOutPlanId(id);
        result.setStockOutTaskId(id);
        result.setHandoverPersonId(id);
        result.setHandoverCode(bankUtil.getUniqueID("G"));
        result.setHandoverTime(LocalDate.parse("2017-05-05"));
        result.setReceiverName("实验员");
        result.setReceiverOrganization("实验室");
        result.setReceiverPhone("1234567890123");

        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 获取出库申请
     * @param code
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-applies/code/{code}")
    @Timed
    public ResponseEntity<List<StockOutApplyDTO>> getStockOutApplies(@PathVariable String code) throws URISyntaxException {
        List<StockOutApplyDTO> data = new ArrayList<>();

        for (int i=0; i<10; ++i){
            StockOutApplyDTO result = new StockOutApplyDTO();
            result.setId((long)i);
            result.setApplyCode(bankUtil.getUniqueID("C"));

            data.add(result);
        }

        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(data));
    }
//    /**
//     * 获取出库计划
//     * @param id
//     * @return
//     * @throws URISyntaxException
//     */
//    @GetMapping("/stock-out-plans/apply/{id}")
//    @Timed
//    public ResponseEntity<List<StockOutPlanDTO>> getStockOutPlans(@PathVariable Long id) throws URISyntaxException {
//        List<StockOutPlanDTO> data = new ArrayList<>();
//
//        for (int i=0; i<10; ++i){
//            StockOutPlanDTO result = new StockOutPlanDTO();
//            result.setId((long)i);
//            result.setStockOutApplyId(id);
//            result.setStockOutPlanCode(bankUtil.getUniqueID());
//
//            data.add(result);
//        }
//
//        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(data));
//    }

    /**
     * 获取出库计划
     * @param code
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-plans/code/{code}")
    @Timed
    public ResponseEntity<List<StockOutPlanDTO>> getStockOutPlans(@PathVariable String code) throws URISyntaxException {
        List<StockOutPlanDTO> data = new ArrayList<>();

        for (int i=0; i<10; ++i){
            StockOutPlanDTO result = new StockOutPlanDTO();
            result.setId((long)i);
            result.setStockOutApplyId((long)i);
            result.setStockOutPlanCode(bankUtil.getUniqueID("E"));

            data.add(result);
        }

        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(data));
    }

    /**
     * 获取出库任务
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-tasks/plan/{id}")
    @Timed
    public ResponseEntity<List<StockOutTaskDTO>> getStockOutTasks(@PathVariable Long id) throws URISyntaxException {
        List<StockOutTaskDTO> data = new ArrayList<>();

        for (int i=0; i<10; ++i){
            StockOutTaskDTO result = new StockOutTaskDTO();
            result.setId((long)i);
            result.setStockOutPlanId(id);
            result.setStockOutTaskCode(bankUtil.getUniqueID("F"));

            data.add(result);
        }

        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(data));
    }

    /**
     * 获取出库任务
     * @param code
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/stock-out-tasks/code/{code}")
    @Timed
    public ResponseEntity<List<StockOutTaskDTO>> getStockOutTasks(@PathVariable String code) throws URISyntaxException {
        List<StockOutTaskDTO> data = new ArrayList<>();

        for (int i=0; i<10; ++i){
            StockOutTaskDTO result = new StockOutTaskDTO();
            result.setId((long)i);
            result.setStockOutPlanId((long)i);
            result.setStockOutTaskCode(bankUtil.getUniqueID("F"));

            data.add(result);
        }

        return  ResponseUtil.wrapOrNotFound(Optional.ofNullable(data));
    }

    /**
     * 创建交接单根据指定任务
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-handovers/task/{id}")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> creatStockOutHandoverByTask(@PathVariable Long id) throws URISyntaxException {
        StockOutHandoverDTO result = new StockOutHandoverDTO();
        result.setId(id);
        result.setStockOutApplyId(id);
        result.setStockOutPlanId(id);
        result.setStockOutTaskId(id);
//        result.setHandoverPersonId(id);
        result.setHandoverCode(bankUtil.getUniqueID("G"));
//        result.setHandoverTime(LocalDate.parse("2017-05-05"));
//        result.setReceiverName("实验员");
//        result.setReceiverOrganization("实验室");
//        result.setReceiverPhone("1234567890123");

        return ResponseEntity.created(new URI("/api/stock-out-handovers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 修改交接单
     * @param stockOutHandoverDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-handovers")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> updateStockOutHandover(@Valid @RequestBody StockOutHandoverDTO stockOutHandoverDTO) throws URISyntaxException {
        StockOutHandoverDTO result = new StockOutHandoverDTO();
        result.setId(1L);
        result.setStockOutApplyId(1L);
        result.setStockOutPlanId(1L);
        result.setStockOutTaskId(1L);
//        result.setHandoverPersonId(id);
        result.setHandoverCode(bankUtil.getUniqueID("G"));
//        result.setHandoverTime(LocalDate.parse("2017-05-05"));
//        result.setReceiverName("实验员");
//        result.setReceiverOrganization("实验室");
//        result.setReceiverPhone("1234567890123");

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "1"))
            .body(result);
    }

    /**
     * 获取出库任务列表
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-tasks", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE}) public DataTablesOutput<StockOutTaskForDataTableEntity> getDataTableStockOutTask(@RequestBody DataTablesInput input) {
        List<StockOutTaskForDataTableEntity> dataList =  new ArrayList<>();

        for (int i = 0; i < input.getLength(); ++i){
            StockOutTaskForDataTableEntity rowData = new StockOutTaskForDataTableEntity();
            rowData.setId(0L + i + input.getStart());
            rowData.setCountOfStockOutSample(100L);
            rowData.setCountOfHandOverSample(20L);
            rowData.setHandOverTimes(1L);
            rowData.setPurposeOfSample("实验");
            rowData.setStockOutDate(LocalDate.now());
            rowData.setStockOutPlanCode(bankUtil.getUniqueID("E"));
            rowData.setStockOutTaskCode(bankUtil.getUniqueID("F"));
            rowData.setStatus("1601");
            dataList.add(rowData);
        }

        DataTablesOutput<StockOutTaskForDataTableEntity> result = new DataTablesOutput<StockOutTaskForDataTableEntity>();
        result.setDraw(input.getDraw());
        result.setError("");
        result.setData(dataList);
        result.setRecordsFiltered(dataList.size());
        result.setRecordsTotal(dataList.size() * 10);
        return result;
    }

    @GetMapping("/stock-out-tasks/{id}")
    @Timed
    public ResponseEntity<StockOutTaskDTO> getStockOutTask(@PathVariable Long id) {
        log.debug("REST request to get StockOutTask : {}", id);
        StockOutTaskDTO stockOutTaskDTO = new StockOutTaskDTO();

        stockOutTaskDTO.setStockOutPlanId(1L );
        stockOutTaskDTO.setId( id );
        stockOutTaskDTO.setStockOutHeadId1( 5L );
        stockOutTaskDTO.setStockOutHeadId2( 6L );
        stockOutTaskDTO.setStockOutDate( LocalDate.now() );
        stockOutTaskDTO.setStatus( "1601" );
        stockOutTaskDTO.setMemo("" );
        stockOutTaskDTO.setStockOutTaskCode( bankUtil.getUniqueID("F") );
        stockOutTaskDTO.setUsedTime( 1 );
        stockOutTaskDTO.setStockOutPlanCode(bankUtil.getUniqueID("E"));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutTaskDTO));
    }
}
