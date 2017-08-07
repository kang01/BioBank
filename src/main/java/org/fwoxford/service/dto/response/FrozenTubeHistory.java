package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.models.auth.In;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by gengluying on 2017/6/19.
 */
@Entity
@Table(name = "view_frozen_tube_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenTubeHistory {
    //样本ID
    @Id
    private Long id ;
    //转运ID
    @Column(name = "tranship_id")
    private Long transhipId ;
    //转运编码
    @Column(name = "tranship_code")
    private String transhipCode ;
    //入库ID
    @Column(name = "stock_in_id")
    private Long stockInId ;
    //入库编码
    @Column(name = "stock_in_code")
    private String stockInCode;
    //任务ID
    @Column(name = "stock_out_task_id")
    private Long stockOutTaskId ;
    //任务编码
    @Column(name = "stock_out_task_code")
    private String stockOutTaskCode;
    //交接ID
    @Column(name = "handover_id")
    private Long handoverId ;
    //交接编码
    @Column(name = "handover_code")
    private String handoverCode;
    //项目编码
    @Column(name = "project_code")
    private String projectCode;
    //样本编码
    @Column(name = "sample_code")
    private String  sampleCode;
    /**
     * 类型：101：转运，102：入库，103：出库，104：交接
     */
    @Column(name = "type")
    private String type;
    //样本状态
    @Column(name = "status")
    private String status;
    //冻存盒编码
    @Column(name = "frozen_box_code")
    private String frozenBoxCode;

    @Column(name ="position")
    private String position;

    @Column(name ="equipment_code")
    private String equipmentCode;

    @Column(name ="area_code")
    private String areaCode;

    @Column(name ="shelves_code")
    private String shelvesCode;

    @Column(name ="rows_in_shelf")
    private String rowsInShelf;

    @Column(name ="columns_in_shelf")
    private String columnsInShelf;

    @Column(name ="position_in_box")
    private String positionInBox;

    @Column(name ="equipment_id")
    private Long equipmentId;

    @Column(name ="area_id")
    private Long areaId;

    @Column(name ="shelves_id")
    private Long shelvesId;
    //盒内行数
    @Column(name = "tube_rows")
    private String tubeRows;
    //盒内列数
    @Column(name = "tube_columns")
    private String tubeColumns;
//    操作时间
    @Column(name = "operate_time")
    private ZonedDateTime operateTime;

    @Column(name = "frozen_tube_id")
    private Long frozenTubeId;

    @Column(name = "memo")
    private String memo;

    @Column(name = "operator")
    private String operator;

    public FrozenTubeHistory() {
    }


    public FrozenTubeHistory(Long id, Long transhipId, String transhipCode, Long stockInId, String stockInCode, Long stockOutTaskId, String stockOutTaskCode, Long handoverId, String handoverCode, String projectCode, String sampleCode, String type, String status, String frozenBoxCode, String position, String equipmentCode, String areaCode, String shelvesCode, String rowsInShelf, String columnsInShelf, String positionInBox, Long equipmentId, Long areaId, Long shelvesId, String tubeRows, String tubeColumns, ZonedDateTime operateTime, Long frozenTubeId, String memo, String operator
        , Long sampleTypeId, String sampleTypeCode, String sampleTypeName, Long frozenTubeTypeId, String frozenTubeTypeCode, String frozenTubeTypeName, Double frozenTubeVolumns, String frozenTubeVolumnsUnit, Integer sampleUsedTimesMost, Integer sampleUsedTimes, Double sampleVolumns, Long projectId, Long projectSiteId, String frozenSiteCode, String frozenTubeState, Long sampleClassificationId, Long frozenBoxId
    ) {
        this.id = id;
        this.transhipId = transhipId;
        this.transhipCode = transhipCode;
        this.stockInId = stockInId;
        this.stockInCode = stockInCode;
        this.stockOutTaskId = stockOutTaskId;
        this.stockOutTaskCode = stockOutTaskCode;
        this.handoverId = handoverId;
        this.handoverCode = handoverCode;
        this.projectCode = projectCode;
        this.sampleCode = sampleCode;
        this.type = type;
        this.status = status;
        this.frozenBoxCode = frozenBoxCode;
        this.position = position;
        this.equipmentCode = equipmentCode;
        this.areaCode = areaCode;
        this.shelvesCode = shelvesCode;
        this.rowsInShelf = rowsInShelf;
        this.columnsInShelf = columnsInShelf;
        this.positionInBox = positionInBox;
        this.equipmentId = equipmentId;
        this.areaId = areaId;
        this.shelvesId = shelvesId;
        this.tubeRows = tubeRows;
        this.tubeColumns = tubeColumns;
        this.operateTime = operateTime;
        this.frozenTubeId = frozenTubeId;
        this.memo = memo;
        this.operator = operator;
        this.sampleTypeId = sampleTypeId;
        this.sampleTypeCode = sampleTypeCode;
        this.sampleTypeName = sampleTypeName;
        this.frozenTubeTypeId = frozenTubeTypeId;
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        this.frozenTubeTypeName = frozenTubeTypeName;
        this.frozenTubeVolumns = frozenTubeVolumns;
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        this.sampleUsedTimes = sampleUsedTimes;
        this.sampleVolumns = sampleVolumns;
        this.projectId = projectId;
        this.projectSiteId = projectSiteId;
        this.frozenSiteCode = frozenSiteCode;
        this.frozenTubeState = frozenTubeState;
        this.sampleClassificationId = sampleClassificationId;
        this.frozenBoxId = frozenBoxId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public Long getStockInId() {
        return stockInId;
    }

    public void setStockInId(Long stockInId) {
        this.stockInId = stockInId;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public Long getStockOutTaskId() {
        return stockOutTaskId;
    }

    public void setStockOutTaskId(Long stockOutTaskId) {
        this.stockOutTaskId = stockOutTaskId;
    }

    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }

    public Long getHandoverId() {
        return handoverId;
    }

    public void setHandoverId(Long handoverId) {
        this.handoverId = handoverId;
    }

    public String getHandoverCode() {
        return handoverCode;
    }

    public void setHandoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getShelvesCode() {
        return shelvesCode;
    }

    public void setShelvesCode(String shelvesCode) {
        this.shelvesCode = shelvesCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getPositionInBox() {
        return positionInBox;
    }

    public void setPositionInBox(String positionInBox) {
        this.positionInBox = positionInBox;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(Long shelvesId) {
        this.shelvesId = shelvesId;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public ZonedDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(ZonedDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public Long getFrozenTubeId() {
        return frozenTubeId;
    }

    public void setFrozenTubeId(Long frozenTubeId) {
        this.frozenTubeId = frozenTubeId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    @Column(name = "sample_type_id")
    private Long sampleTypeId;
    @Column(name = "sample_type_code")
    private String sampleTypeCode;
    @Column(name = "sample_type_name")
    private String sampleTypeName;
    @Column(name = "frozen_tube_type_id")
    private Long frozenTubeTypeId;
    @Column(name = "frozen_tube_type_code")
    private String frozenTubeTypeCode;
    @Column(name = "frozen_tube_type_name")
    private String frozenTubeTypeName;
    @Column(name = "frozen_tube_volumns")
    private Double frozenTubeVolumns;
    @Column(name = "frozen_tube_volumns_unit")
    private String frozenTubeVolumnsUnit;
    @Column(name = "sample_used_times_most")
    private Integer sampleUsedTimesMost;
    @Column(name = "sample_used_times")
    private Integer sampleUsedTimes;
    @Column(name = "sample_volumns")
    private Double sampleVolumns;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "project_site_id")
    private Long projectSiteId;
    @Column(name = "project_site_code")
    private String frozenSiteCode;
    @Column(name = "frozen_tube_state")
    private String frozenTubeState;
    @Column(name = "sample_classification_id")
    private Long sampleClassificationId;
    @Column(name = "frozen_box_id")
    private Long frozenBoxId;
    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public void setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public Double getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }

    public void setFrozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }

    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }

    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public Double getSampleVolumns() {
        return sampleVolumns;
    }

    public void setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }

    public void setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
    }

    public String getFrozenSiteCode() {
        return frozenSiteCode;
    }

    public void setFrozenSiteCode(String frozenSiteCode) {
        this.frozenSiteCode = frozenSiteCode;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }

    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }
}
