package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TranshipBox entity.
 */
public class TranshipBoxDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 设备编码
     */
    @Size(max = 100)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @Size(max = 100)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @Size(max = 100)
    private String supportRackCode;
    /**
     * 所在架子行数
     */
    @Size(max = 20)
    private String rowsInShelf;
    /**
     * 所在架子列数
     */
    @Size(max = 20)
    private String columnsInShelf;
    /**
     * 样本数量
     */
    private Integer countOfSample;
    /**
     * 冻存盒类型编码
     */
    @Size(max = 100)
    private String frozenBoxTypeCode;
    /**
     * 冻存盒行数
     */
    @Size(max = 20)
    private String frozenBoxTypeRows;
    /**
     * 冻存盒列数
     */
    @Size(max = 20)
    private String frozenBoxTypeColumns;
    /**
     * 项目编码
     */
    @Size(max = 100)
    private String projectCode;
    /**
     * 项目名称
     */
    @Size(max = 255)
    private String projectName;
    /**
     * 项目点编码
     */
    @Size(max = 100)
    private String projectSiteCode;
    /**
     * 项目点名称
     */
    @Size(max = 255)
    private String projectSiteName;
    /**
     * 样本类型编码
     */
    @Size(max = 100)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @Size(max = 255)
    private String sampleTypeName;
    private String sampleClassificationCode;
    private String sampleClassificationName;
    /**
     * 是否分装：1：是，0：否
     */
    private Integer isSplit;
    /**
     * 空管数
     */
    private Integer emptyTubeNumber;
    /**
     * 空孔数
     */
    private Integer emptyHoleNumber;
    /**
     * 错位数
     */
    @Max(value = 100)
    private Integer dislocationNumber;
    /**
     * 是否已导入样本数据：1：是，0：否
     */
    private Integer isRealData;
    /**
     * 冻存盒类型ID
     */
    private Long frozenBoxTypeId;
    /**
     * 样本类型ID
     */
    private Long sampleTypeId;
    /**
     *样本分类ID
     */
    private Long sampleClassificationId;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目点ID
     */
    private Long projectSiteId;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态
     */
    @Size(max = 20)
    private String status;
    /**
     * 转运id
     */
    private Long transhipId;
    /**
     * 冻存盒id
     */
    private Long frozenBoxId;
    /**
     * 设备Id
     */
    private Long equipmentId;
    /**
     * 冻存架Id
     */
    private Long supportRackId;
    /**
     * 区域Id
     */
    private Long areaId;

    private List<FrozenTubeDTO> frozenTubeDTOList;

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

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }

    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }

    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
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

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public Integer getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
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

    public Integer getIsRealData() {
        return isRealData;
    }

    public void setIsRealData(Integer isRealData) {
        this.isRealData = isRealData;
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

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
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

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getSupportRackId() {
        return supportRackId;
    }

    public void setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public List<FrozenTubeDTO> getFrozenTubeDTOList() {
        return frozenTubeDTOList;
    }

    public void setFrozenTubeDTOList(List<FrozenTubeDTO> frozenTubeDTOList) {
        this.frozenTubeDTOList = frozenTubeDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranshipBoxDTO transhipBoxDTO = (TranshipBoxDTO) o;

        if ( ! Objects.equals(id, transhipBoxDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipBoxDTO{" +
            "id=" + id +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", countOfSample=" + countOfSample +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + '\'' +
            ", frozenBoxTypeRows='" + frozenBoxTypeRows + '\'' +
            ", frozenBoxTypeColumns='" + frozenBoxTypeColumns + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectName='" + projectName + '\'' +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", projectSiteName='" + projectSiteName + '\'' +
            ", sampleTypeCode='" + sampleTypeCode + '\'' +
            ", sampleTypeName='" + sampleTypeName + '\'' +
            ", sampleClassificationCode='" + sampleClassificationCode + '\'' +
            ", sampleClassificationName='" + sampleClassificationName + '\'' +
            ", isSplit=" + isSplit +
            ", emptyTubeNumber=" + emptyTubeNumber +
            ", emptyHoleNumber=" + emptyHoleNumber +
            ", dislocationNumber=" + dislocationNumber +
            ", isRealData=" + isRealData +
            ", frozenBoxTypeId=" + frozenBoxTypeId +
            ", sampleTypeId=" + sampleTypeId +
            ", sampleClassificationId=" + sampleClassificationId +
            ", projectId=" + projectId +
            ", projectSiteId=" + projectSiteId +
            ", memo='" + memo + '\'' +
            ", status='" + status + '\'' +
            ", transhipId=" + transhipId +
            ", frozenBoxId=" + frozenBoxId +
            ", equipmentId=" + equipmentId +
            ", supportRackId=" + supportRackId +
            ", areaId=" + areaId +
            ", frozenTubeDTOList=" + frozenTubeDTOList +
            '}';
    }
}
