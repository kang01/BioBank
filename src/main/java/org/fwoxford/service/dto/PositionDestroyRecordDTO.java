package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PositionDestroyRecord entity.
 */
public class PositionDestroyRecordDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String equipmentCode;

    @Size(max = 100)
    private String areaCode;

    @Size(max = 100)
    private String supportRackCode;

    @Size(max = 100)
    private String rowsInShelf;

    @Size(max = 100)
    private String columnsInShelf;

    @Size(max = 100)
    private String frozenBoxCode;

    @Size(max = 100)
    private String tubeRows;

    @Size(max = 100)
    private String tubeColumns;

    @Size(max = 20)
    private String destroyType;

    @Size(max = 100)
    private String projectCode;

    @Size(max = 100)
    private String projectSiteCode;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long equipmentId;

    private Long areaId;

    private Long supportRackId;

    private Long supportRackOldId;

    private Long frozenBoxId;

    private Long frozenTubeId;

    private Long projectId;

    private Long projectSiteId;

    private Long positionDestroyId;

    @Size(max = 100)
    private String frozenTubeCode;
    @Size(max = 100)
    private String sampleTempCode;
    @Size(max = 100)
    private String sampleCode;
    @Size(max = 100)
    private String frozenTubeTypeCode;
    @Size(max = 255)
    private String frozenTubeTypeName;
    @Size(max = 100)
    private String sampleTypeCode;
    @Size(max = 255)
    private String sampleTypeName;
    @Max(value = 20)
    private Integer sampleUsedTimesMost;
    @Max(value = 20)
    private Integer sampleUsedTimes;
    private Double frozenTubeVolumns;
    private String frozenTubeVolumnsUnit;
    private Double sampleVolumns;
    @Size(max = 20)
    private String errorType;
    @Size(max = 20)
    private String frozenTubeState;
    private Long frozenTubeTypeId;
    private Long sampleTypeId;
    private Long sampleClassificationId;
    private String sampleClassificationCode;
    private String sampleClassificationName;

    public Long getId() {
        return id;
    }

    public PositionDestroyRecordDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public PositionDestroyRecordDTO setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public PositionDestroyRecordDTO setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public PositionDestroyRecordDTO setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public PositionDestroyRecordDTO setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public PositionDestroyRecordDTO setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public PositionDestroyRecordDTO setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public PositionDestroyRecordDTO setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public PositionDestroyRecordDTO setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public String getDestroyType() {
        return destroyType;
    }

    public PositionDestroyRecordDTO setDestroyType(String destroyType) {
        this.destroyType = destroyType;
        return this;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public PositionDestroyRecordDTO setProjectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public PositionDestroyRecordDTO setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public PositionDestroyRecordDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public PositionDestroyRecordDTO setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public PositionDestroyRecordDTO setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
        return this;
    }

    public Long getAreaId() {
        return areaId;
    }

    public PositionDestroyRecordDTO setAreaId(Long areaId) {
        this.areaId = areaId;
        return this;
    }

    public Long getSupportRackId() {
        return supportRackId;
    }

    public PositionDestroyRecordDTO setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
        return this;
    }

    public Long getSupportRackOldId() {
        return supportRackOldId;
    }

    public PositionDestroyRecordDTO setSupportRackOldId(Long supportRackOldId) {
        this.supportRackOldId = supportRackOldId;
        return this;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public PositionDestroyRecordDTO setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
        return this;
    }

    public Long getFrozenTubeId() {
        return frozenTubeId;
    }

    public PositionDestroyRecordDTO setFrozenTubeId(Long frozenTubeId) {
        this.frozenTubeId = frozenTubeId;
        return this;
    }

    public Long getProjectId() {
        return projectId;
    }

    public PositionDestroyRecordDTO setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }

    public PositionDestroyRecordDTO setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
        return this;
    }

    public Long getPositionDestroyId() {
        return positionDestroyId;
    }

    public PositionDestroyRecordDTO setPositionDestroyId(Long positionDestroyId) {
        this.positionDestroyId = positionDestroyId;
        return this;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public PositionDestroyRecordDTO setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
        return this;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public PositionDestroyRecordDTO setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public PositionDestroyRecordDTO setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public PositionDestroyRecordDTO setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public PositionDestroyRecordDTO setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public PositionDestroyRecordDTO setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public PositionDestroyRecordDTO setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public PositionDestroyRecordDTO setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public PositionDestroyRecordDTO setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
        return this;
    }

    public Double getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }

    public PositionDestroyRecordDTO setFrozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
        return this;
    }

    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }

    public PositionDestroyRecordDTO setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
        return this;
    }

    public Double getSampleVolumns() {
        return sampleVolumns;
    }

    public PositionDestroyRecordDTO setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
        return this;
    }

    public String getErrorType() {
        return errorType;
    }

    public PositionDestroyRecordDTO setErrorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }

    public PositionDestroyRecordDTO setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
        return this;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public PositionDestroyRecordDTO setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
        return this;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public PositionDestroyRecordDTO setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
        return this;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public PositionDestroyRecordDTO setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
        return this;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public PositionDestroyRecordDTO setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public PositionDestroyRecordDTO setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PositionDestroyRecordDTO positionDestroyRecordDTO = (PositionDestroyRecordDTO) o;

        if ( ! Objects.equals(id, positionDestroyRecordDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionDestroyRecordDTO{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", tubeRows='" + tubeRows + '\'' +
            ", tubeColumns='" + tubeColumns + '\'' +
            ", destroyType='" + destroyType + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", equipmentId=" + equipmentId +
            ", areaId=" + areaId +
            ", supportRackId=" + supportRackId +
            ", supportRackOldId=" + supportRackOldId +
            ", frozenBoxId=" + frozenBoxId +
            ", frozenTubeId=" + frozenTubeId +
            ", projectId=" + projectId +
            ", projectSiteId=" + projectSiteId +
            ", positionDestroyId=" + positionDestroyId +
            ", frozenTubeCode='" + frozenTubeCode + '\'' +
            ", sampleTempCode='" + sampleTempCode + '\'' +
            ", sampleCode='" + sampleCode + '\'' +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + '\'' +
            ", frozenTubeTypeName='" + frozenTubeTypeName + '\'' +
            ", sampleTypeCode='" + sampleTypeCode + '\'' +
            ", sampleTypeName='" + sampleTypeName + '\'' +
            ", sampleUsedTimesMost=" + sampleUsedTimesMost +
            ", sampleUsedTimes=" + sampleUsedTimes +
            ", frozenTubeVolumns=" + frozenTubeVolumns +
            ", frozenTubeVolumnsUnit='" + frozenTubeVolumnsUnit + '\'' +
            ", sampleVolumns=" + sampleVolumns +
            ", errorType='" + errorType + '\'' +
            ", frozenTubeState='" + frozenTubeState + '\'' +
            ", frozenTubeTypeId=" + frozenTubeTypeId +
            ", sampleTypeId=" + sampleTypeId +
            ", sampleClassificationId=" + sampleClassificationId +
            ", sampleClassificationCode='" + sampleClassificationCode + '\'' +
            ", sampleClassificationName='" + sampleClassificationName + '\'' +
            '}';
    }
}
