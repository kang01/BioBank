package org.fwoxford.service;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockInBox;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

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
     * 批量保存冻存盒
     * @param frozenBoxDTOList
     */
    List<FrozenBox> saveBatch(List<FrozenBoxDTO> frozenBoxDTOList);

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

    List<StockInBoxForChangingPosition> getIncompleteFrozenBoxes(String projectCode, String sampleTypeCode,String transhipCode);

    DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipment(DataTablesInput input, String equipmentCode);

    DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipmentAndArea(DataTablesInput input, String equipmentCode, String areaCode);

    List<StockInBoxDetail> getFrozenBoxByEquipmentAndAreaAndShelves(String equipmentCode, String areaCode, String shelfCode);

    StockInBoxDetail getFrozenBoxByEquipmentAndAreaAndShelvesAndPosition(String equipmentCode, String areaCode, String shelfCode, String position);

    List<StockInBoxForDataTable> findFrozenBoxListByBoxCodeStr(List<String> frozenBoxCodeStr);

    Boolean isRepeatFrozenBoxCode(String frozenBoxCode);

    List<StockInBoxForChangingPosition> getIncompleteFrozenBoxesByStockIn(String projectCode, String sampleTypeCode, String stockInCode);

    List<StockInBoxForIncomplete> getIncompleteFrozenBoxeList(String frozenBoxCode, String stockInCode);

    FrozenBoxDTO getBoxAndTubeByForzenBoxCode(String frozenBoxCode);

    StockInBoxDetail createStockInBoxDetail(StockInBox frozenBox, String stockInCode);

    List<StockInBoxForDataTable> frozenBoxesToStockInBoxForDataTables(List<FrozenBox> frozenBoxes);

    /**
     * 查询冻存盒的上一次状态
     * @param id
     * @return
     */
    String findFrozenBoxHistory(Long id);

    /**
     * 从项目组导入样本
     * @param frozenBoxCodeStr
     * @return
     */
    List<FrozenBoxDTO> importFrozenBoxAndFrozenTube(String frozenBoxCodeStr);
}
