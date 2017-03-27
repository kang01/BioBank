package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FrozenBox entity.
 */
public class FrozenBoxDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 100)
    private String frozenBoxTypeCode;

    @NotNull
    @Size(max = 20)
    private String frozenBoxRows;

    @NotNull
    @Size(max = 20)
    private String frozenBoxColumns;

    @NotNull
    @Size(max = 100)
    private String projectCode;

    @NotNull
    @Size(max = 255)
    private String projectName;

    @NotNull
    @Size(max = 100)
    private String projectSiteCode;

    @NotNull
    @Size(max = 255)
    private String projectSiteName;

    @NotNull
    @Size(max = 100)
    private String equipmentCode;

    @NotNull
    @Size(max = 100)
    private String areaCode;

    @NotNull
    @Size(max = 100)
    private String supportRackCode;

    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    private String sampleTypeName;

    @NotNull
    private Integer sampleNumber;

    @NotNull
    @Size(max = 20)
    private String isSplit;

    @NotNull
    @Size(max = 20)
    private String rowsInShelf;

    @NotNull
    @Size(max = 20)
    private String columnsInShelf;

    @Size(max = 255)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    @NotNull
    @Max(value = 100)
    private Integer emptyTubeNumber;

    @NotNull
    @Max(value = 100)
    private Integer emptyHoleNumber;

    @NotNull
    @Max(value = 100)
    private Integer dislocationNumber;

    @NotNull
    @Size(max = 20)
    private String isRealData;

    private Long frozenBoxTypeId;

    private Long sampleTypeId;

    private Long projectId;

    private Long projectSiteId;

    private Long transhipId;

    private Long equipmentId;

    private Long areaId;

    private Long supportRackId;

    private List<FrozenTubeDTO> frozenTubeDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }
    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }
    public String getFrozenBoxRows() {
        return frozenBoxRows;
    }

    public void setFrozenBoxRows(String frozenBoxRows) {
        this.frozenBoxRows = frozenBoxRows;
    }
    public String getFrozenBoxColumns() {
        return frozenBoxColumns;
    }

    public void setFrozenBoxColumns(String frozenBoxColumns) {
        this.frozenBoxColumns = frozenBoxColumns;
    }
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }
    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
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
    public String getSupportRackCode() {
        return supportRackCode;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
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
    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }
    public String getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(String isSplit) {
        this.isSplit = isSplit;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }

    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }
    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }

    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }
    public Integer getDislocationNumber() {
        return dislocationNumber;
    }

    public void setDislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
    }
    public String getIsRealData() {
        return isRealData;
    }

    public void setIsRealData(String isRealData) {
        this.isRealData = isRealData;
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

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
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

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
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

    public Long getSupportRackId() {
        return supportRackId;
    }

    public void setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
    }

    public List<FrozenTubeDTO> getFrozenTubeDTOS() {
        return frozenTubeDTOS;
    }

    public void setFrozenTubeDTOS(List<FrozenTubeDTO> frozenTubeDTOS) {
        this.frozenTubeDTOS = frozenTubeDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FrozenBoxDTO frozenBoxDTO = (FrozenBoxDTO) o;

        if ( ! Objects.equals(id, frozenBoxDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenBoxDTO{" +
            "id=" + id +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + "'" +
            ", frozenBoxRows='" + frozenBoxRows + "'" +
            ", frozenBoxColumns='" + frozenBoxColumns + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectName='" + projectName + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", projectSiteName='" + projectSiteName + "'" +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", sampleNumber='" + sampleNumber + "'" +
            ", isSplit='" + isSplit + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", emptyTubeNumber='" + emptyTubeNumber + "'" +
            ", emptyHoleNumber='" + emptyHoleNumber + "'" +
            ", dislocationNumber='" + dislocationNumber + "'" +
            ", isRealData='" + isRealData + "'" +
            ", rowsInShelf='" + rowsInShelf + "'" +
            ", columnsInShelf='" + columnsInShelf + "'" +
            '}';
    }
}
