package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.service.FrozenBoxImportService;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FrozenBox.
 */
@RestController
@RequestMapping("/api")
public class FrozenBoxResource {

    private final Logger log = LoggerFactory.getLogger(FrozenBoxResource.class);

    private static final String ENTITY_NAME = "frozenBox";

    private final FrozenBoxService frozenBoxService;

    public FrozenBoxResource(FrozenBoxService frozenBoxService) {
        this.frozenBoxService = frozenBoxService;
    }

    /**
     * POST  /frozen-boxes : Create a new frozenBox.
     *
     * @param frozenBoxDTO the frozenBoxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frozenBoxDTO, or with status 400 (Bad Request) if the frozenBox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frozen-boxes")
    @Timed
    public ResponseEntity<FrozenBoxDTO> createFrozenBox(@Valid @RequestBody FrozenBoxDTO frozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to save FrozenBox : {}", frozenBoxDTO);
        if (frozenBoxDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frozenBox cannot already have an ID")).body(null);
        }
        FrozenBoxDTO result = frozenBoxService.save(frozenBoxDTO);
        return ResponseEntity.created(new URI("/api/frozen-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frozen-boxes : Updates an existing frozenBox.
     *
     * @param frozenBoxDTO the frozenBoxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frozenBoxDTO,
     * or with status 400 (Bad Request) if the frozenBoxDTO is not valid,
     * or with status 500 (Internal Server Error) if the frozenBoxDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frozen-boxes")
    @Timed
    public ResponseEntity<FrozenBoxDTO> updateFrozenBox(@Valid @RequestBody FrozenBoxDTO frozenBoxDTO) throws URISyntaxException {
        log.debug("REST request to update FrozenBox : {}", frozenBoxDTO);
        if (frozenBoxDTO.getId() == null) {
            return createFrozenBox(frozenBoxDTO);
        }
        FrozenBoxDTO result = frozenBoxService.save(frozenBoxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frozenBoxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frozen-boxes : get all the frozenBoxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of frozenBoxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/frozen-boxes")
    @Timed
    public ResponseEntity<List<FrozenBoxDTO>> getAllFrozenBoxes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FrozenBoxes");
        Page<FrozenBoxDTO> page = frozenBoxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/frozen-boxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /frozen-boxes/:id : get the "id" frozenBox.
     *
     * @param id the id of the frozenBoxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frozenBoxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/frozen-boxes/{id}")
    @Timed
    public ResponseEntity<FrozenBoxDTO> getFrozenBox(@PathVariable Long id) {
        log.debug("REST request to get FrozenBox : {}", id);
        FrozenBoxDTO frozenBoxDTO = frozenBoxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenBoxDTO));
    }

    /**
     * DELETE  /frozen-boxes/:id : delete the "id" frozenBox.
     *
     * @param id the id of the frozenBoxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frozen-boxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrozenBox(@PathVariable Long id) {
        log.debug("REST request to delete FrozenBox : {}", id);
        frozenBoxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据冻存盒ID查询冻存盒和冻存管信息
     * @param id
     * @return
     */
    @GetMapping("/frozen-boxes/id/{id}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByForzenBoxId(@PathVariable Long id) {
        log.debug("REST request to get FrozenTube : {}", id);
        FrozenBoxAndFrozenTubeResponse res = frozenBoxService.findFrozenBoxAndTubeByBoxId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 根据冻存盒CODE查询冻存盒和冻存管信息（取现在的盒子的数据，若已分装过，则查询不出来）
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/frozen-boxes/code/{frozenBoxCode}")
    @Timed
    public ResponseEntity<FrozenBoxAndFrozenTubeResponse> getFrozenTubeByForzenBoxCode(@PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxAndFrozenTubeResponse res = frozenBoxService.findFrozenBoxAndTubeByBoxCode(frozenBoxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
    /**
     * 根据冻存盒CODE字符串查询冻存盒和冻存管信息
     * @param frozenBoxCodeStr
     * @return
     */
    @GetMapping("/frozen-boxes/codes/{frozenBoxCodeStr}")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> getFrozenBoxAndTubeListByBoxCodeStr(@PathVariable  String frozenBoxCodeStr) {
        log.debug("REST request to get FrozenBoxAndTube By codes : {}", frozenBoxCodeStr);
        List<FrozenBoxAndFrozenTubeResponse> res = frozenBoxService.findFrozenBoxAndTubeListByBoxCodeStr(frozenBoxCodeStr);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 根据转运编码查询冻存盒和冻存管列表
     * @param transhipCode
     * @return
     */
    @GetMapping("/frozen-boxes/tranship/{transhipCode}")
    @Timed
    public ResponseEntity<List<FrozenBoxAndFrozenTubeResponse>> getFrozenBoxAndTubeByTranshipCode(@PathVariable String transhipCode) {
        log.debug("REST request to get FrozenTube : {}", transhipCode);
        List<FrozenBoxAndFrozenTubeResponse> res = new ArrayList<>();
        res = frozenBoxService.getFrozenBoxAndTubeByTranshipCode(transhipCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
    /**
     * 输入设备编码，返回该设备下的所有盒子信息
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/frozen-boxes/pos/{equipmentCode}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipment(@RequestBody DataTablesInput input,@PathVariable String equipmentCode) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        return frozenBoxService.getPageFrozenBoxByEquipment(input,equipmentCode);
    }

    /**
     * 输入设备编码，区域编码，返回指定区域下的所有盒子信息
     * @param input
     * @param equipmentCode
     * @param areaCode
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/frozen-boxes/pos/{equipmentCode}/{areaCode}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipmentAndArea(@RequestBody DataTablesInput input,@PathVariable String equipmentCode,@PathVariable String areaCode) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        return frozenBoxService.getPageFrozenBoxByEquipmentAndArea(input,equipmentCode,areaCode);
    }

    /**
     * 输入设备编码，区域编码，架子编码，返回架子中的所有盒子信息
     * @param equipmentCode
     * @param areaCode
     * @param shelfCode
     * @return
     */
    @RequestMapping(value = "/frozen-boxes/pos/{equipmentCode}/{areaCode}/{shelfCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<StockInBoxDetail> getFrozenBoxByEquipmentAndAreaAndShelves(@PathVariable String equipmentCode, @PathVariable String areaCode, @PathVariable String shelfCode) {
        List<StockInBoxDetail> boxes =  frozenBoxService.getFrozenBoxByEquipmentAndAreaAndShelves(equipmentCode,areaCode,shelfCode);
        return boxes;
    }

    /**
     * 输入完整的位置信息，返回某个盒子的信息
     * @param equipmentCode
     * @param areaCode
     * @param shelfCode
     * @param position
     * @return
     */
    @RequestMapping(value = "/frozen-boxes/pos/{equipmentCode}/{areaCode}/{shelfCode}/{position}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public StockInBoxDetail getFrozenBoxByEquipmentAndAreaAndShelvesAndPosition(@PathVariable String equipmentCode, @PathVariable String areaCode, @PathVariable String shelfCode, @PathVariable String position) {
        StockInBoxDetail boxes =  frozenBoxService.getFrozenBoxByEquipmentAndAreaAndShelvesAndPosition(equipmentCode,areaCode,shelfCode,position);
        return boxes;
    }

    /**
     * 判断盒子编码是否已经存在  ----true：已经存在，false:不存在
     * @param frozenBoxCode
     * @return
     */
    @RequestMapping(value = "/frozen-boxes/isRepeat/{frozenBoxCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public Boolean isRepeatFrozenBoxCode(@PathVariable String frozenBoxCode) {
        Boolean boxes =  frozenBoxService.isRepeatFrozenBoxCode(frozenBoxCode);
        return boxes;
    }

    /**
     * 输入被分装的冻存盒编码和项目编码，返回该入库单的未装满的盒子的信息
     * @param frozenBoxCode
     * @param stockInCode
     * @return
     */
    @RequestMapping(value = "/frozen-boxes/incomplete-boxes/frozenBox/{frozenBoxCode}/stockIn/{stockInCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<StockInBoxForIncomplete> getIncompleteFrozenBoxeList( @PathVariable String frozenBoxCode, @PathVariable String stockInCode) {
        List<StockInBoxForIncomplete> boxes =  frozenBoxService.getIncompleteFrozenBoxeList(frozenBoxCode,stockInCode);
        return boxes;
    }

    /**
     * 冻存盒直接入库，取原冻存盒的信息
     * @param frozenBoxCode
     * @return
     */
    @GetMapping("/frozen-boxes/boxCode/{frozenBoxCode}/forStockIn")
    @Timed
    public ResponseEntity<FrozenBoxDTO> getBoxAndTubeByForzenBoxCode(@PathVariable String frozenBoxCode) {
        log.debug("REST request to get FrozenTube : {}", frozenBoxCode);
        FrozenBoxDTO res = frozenBoxService.getBoxAndTubeByForzenBoxCode(frozenBoxCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 生成一个未被使用的新冻存盒编码
     * @param projectId
     * @param sampleTypeId
     * @param sampleClassId
     * @return
     */
    @GetMapping("/frozen-boxes/{projectId}/{sampleTypeId}/{sampleClassId}/makeNewBoxCode")
    public ResponseEntity getNewBoxCode(@PathVariable Long projectId, @PathVariable Long sampleTypeId, @PathVariable Long sampleClassId) {
        log.debug("REST request to get newest frozen box code : {}", projectId + "/" + sampleTypeId + "/" + sampleClassId);
        String newCode = frozenBoxService.makeNewFrozenBoxCode(projectId, sampleTypeId, sampleClassId);
        HashMap<String, String> res = new HashMap<>();
        res.put("code", newCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }

    /**
     * 生成一个未被使用的新冻存盒编码
     * @param projectId
     * @param sampleTypeId
     * @return
     */
    @GetMapping("/frozen-boxes/{projectId}/{sampleTypeId}/makeNewBoxCode")
    public ResponseEntity getNewBoxCode(@PathVariable Long projectId, @PathVariable Long sampleTypeId) {
        log.debug("REST request to get newest frozen box code : {}", projectId + "/" + sampleTypeId);
        String newCode = frozenBoxService.makeNewFrozenBoxCode(projectId, sampleTypeId, null);
        HashMap<String, String> res = new HashMap<>();
        res.put("code", newCode);
        return ResponseEntity.ok(res);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
    /**
     * 获取指定冻存盒编码的冻存盒信息（包含本次入库单内待入库冻存盒，全部已入库未满，已出库，已交接冻存盒）
     * 如果冻存盒已满，提示冻存盒已满；
     * @param frozenBoxCode
     * @param stockInCode
     * @return
     */

    @RequestMapping(value = "/frozen-boxes/specify-unfull-frozenBox/{frozenBoxCode}/project/{projectId}/stockIn/{stockInCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public StockInBoxForIncomplete getIncompleteSpecifyFrozenBox( @PathVariable String frozenBoxCode, @PathVariable Long projectId, @PathVariable String stockInCode) {
        StockInBoxForIncomplete box =  frozenBoxService.getIncompleteSpecifyFrozenBox(frozenBoxCode,projectId,stockInCode);
        return box;
    }
}
