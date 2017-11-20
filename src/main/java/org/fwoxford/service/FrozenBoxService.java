package org.fwoxford.service;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockInBox;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Service Interface for managing FrozenBox.
 */
public interface FrozenBoxService {

    /**
     * Save a frozenBox.
     *
     * @param frozenBoxDTO the entity to save
     * @return the persisted entity
     */
    FrozenBoxDTO save(FrozenBoxDTO frozenBoxDTO);

    /**
     *  Get all the frozenBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenBoxDTO findOne(Long id);

    /**
     *  Delete the "id" frozenBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 根据转运记录ID 查询冻存盒列表
     * @param transhipId 转运记录ID
     * @return
     */
    List<FrozenBoxDTO> findAllFrozenBoxByTranshipId(Long transhipId);

    /**
     * 根据冻存盒ID查询冻存管信息
     * @param id 冻存盒ID
     * @return
     */
    FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxId(Long id);

    /**
     * 根据冻存盒CODE查询冻存管信息
     * @param frozenBoxCode 冻存盒CODE
     * @return
     */
    FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxCode(String frozenBoxCode);

    /**
     * 根据冻存盒code查询冻存盒基本信息
     * @param frozenBoxCode
     * @return
     */
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);
    /**
     * 根据冻存盒code串查询冻存盒以及冻存管的信息
     * @param frozenBoxCodeStr
     * @return
     */
    List<FrozenBoxAndFrozenTubeResponse> findFrozenBoxAndTubeListByBoxCodeStr(String frozenBoxCodeStr);

    /**
     * 判断某设备某区域某架子某行某列是否有盒子存在
     * @param equipmentId
     * @param areaId
     * @param supportRackId
     * @param column
     * @param row
     * @return
     */
    List<FrozenBoxDTO>  findByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    /**
     * 根据转运编码查询冻存盒列表
     * @param transhipCode
     * @return
     */
    List<FrozenBoxAndFrozenTubeResponse> getFrozenBoxAndTubeByTranshipCode(String transhipCode);

    /**
     * 输入设备编码，返回该设备下的所有盒子信息
     * @param input
     * @param equipmentCode
     * @return
     */
    DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipment(DataTablesInput input, String equipmentCode);
    /**
     * 输入设备编码，区域编码，返回指定区域下的所有盒子信息
     * @param input
     * @param equipmentCode
     * @param areaCode
     * @return
     */
    DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipmentAndArea(DataTablesInput input, String equipmentCode, String areaCode);

    /**
     * 输入设备编码，区域编码，架子编码，返回架子中的所有盒子信息
     * @param equipmentCode
     * @param areaCode
     * @param shelfCode
     * @return
     */
    List<StockInBoxDetail> getFrozenBoxByEquipmentAndAreaAndShelves(String equipmentCode, String areaCode, String shelfCode);

    /**
     * 输入完整的位置信息，返回某个盒子的信息
     * @param equipmentCode
     * @param areaCode
     * @param shelfCode
     * @param position
     * @return
     */
    StockInBoxDetail getFrozenBoxByEquipmentAndAreaAndShelvesAndPosition(String equipmentCode, String areaCode, String shelfCode, String position);

    /**
     * 判断盒子编码是否已经存在  ----true：已经存在，false:不存在
     * @param frozenBoxCode
     * @return
     */
    Boolean isRepeatFrozenBoxCode(String frozenBoxCode);

    /**
     *  获取未满冻存盒
     * @param frozenBoxCode
     * @param stockInCode
     * @return
     */
    List<StockInBoxForIncomplete> getIncompleteFrozenBoxeList(String frozenBoxCode, String stockInCode);

    /**
     * 冻存盒直接入库，取原冻存盒的信息
     * @param frozenBoxCode
     * @return
     */
    FrozenBoxDTO getBoxAndTubeByForzenBoxCode(String frozenBoxCode);

    /**
     * 构造入库盒
     * @param frozenBox
     * @param stockInCode
     * @return
     */
    StockInBoxDetail createStockInBoxDetail(StockInBox frozenBox, String stockInCode);

    /**
     * 根据冻存盒编码字符串返回入库盒信息
     * @param frozenBoxes
     * @return
     */
    List<StockInBoxForDataTable> frozenBoxesToStockInBoxForDataTables(List<FrozenBox> frozenBoxes);

    /**
     * 查询冻存盒的上一次状态
     * @param id
     * @return
     */
    String findFrozenBoxHistory(Long id);

    String makeNewFrozenBoxCode(Long projectId, Long sampleId, Long sampleClassId);

    /**
     * 获取指定冻存盒编码的冻存盒信息（包含本次入库单内待入库冻存盒，全部已入库未满，已出库，已交接冻存盒）
     * @param frozenBoxCode
     * @param stockInCode
     * @return
     */
    StockInBoxForIncomplete getIncompleteSpecifyFrozenBox(String frozenBoxCode, String projectCode,String stockInCode);
}
