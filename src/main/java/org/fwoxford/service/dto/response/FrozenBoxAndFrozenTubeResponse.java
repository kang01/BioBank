package org.fwoxford.service.dto.response;


import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.SampleClassification;
import org.fwoxford.domain.SampleType;
import org.fwoxford.service.dto.FrozenTubeDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * A DTO for the FrozenBox entity.
 */
public class FrozenBoxAndFrozenTubeResponse {

    private Long id;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 原盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode1D;
    /**
     * 是否分装（1：是；0：否）
     */
    private Integer isSplit;
    /**
     * 备注
     */
    @Size(max = 255)
    private String memo;
    /**
     * 状态：（2001：新建，2002：待入库，2003：已分装，2004：已入库，2090：已作废）
     */
    @Size(max = 20)
    private String status;
    /**
     * 冻存盒类型
     */
    private FrozenBoxType frozenBoxType;

    /**
     * 样本分类
     */
    private SampleClassification sampleClassification;
    /**
     * 样本类型
     */
    private SampleType sampleType;
    /**
     * 设备ID
     */
    private Long equipmentId;
    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 冻存架ID
     */
    private Long supportRackId;
    /**
     * 所在架子行数
     */
    private String rowsInShelf;
    /**
     * 所在架子列数
     */
    private String columnsInShelf;
    /**
     * 冻存管列表
     */
    private List<FrozenTubeDTO> frozenTubeDTOS;

    private Long projectId;
    private String projectCode;
    private String projectName;

    private Long frozenBoxTypeId;
    private String frozenBoxTypeName;
    private String frozenBoxTypeCode;
    private String frozenBoxTypeRows;
    private String frozenBoxTypeColumns;

    private Long sampleClassificationId;
    private String sampleClassificationName;
    private String sampleClassificationCode;
    private String frontColorForClass;
    private String backColorForClass;

    private Long sampleTypeId;
    private String sampleTypeCode;
    private String sampleTypeName;
    private Integer isMixed;
    private String frontColor;
    private String backColor;

    /**
     * 是否已导入样本数据：1：是，0：否
     */
    private Integer isRealData;
    private Integer countOfSample;

    private Long frozenBoxId;

    public Long getId() {
        return id;
    }

    public FrozenBoxAndFrozenTubeResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
        return this;
    }

    public Integer getIsSplit() {
        return isSplit;
    }

    public FrozenBoxAndFrozenTubeResponse setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenBoxAndFrozenTubeResponse setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public FrozenBoxAndFrozenTubeResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public FrozenBoxType getFrozenBoxType() {
        return frozenBoxType;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
        return this;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public FrozenBoxAndFrozenTubeResponse setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
        return this;
    }

    public Long getAreaId() {
        return areaId;
    }

    public FrozenBoxAndFrozenTubeResponse setAreaId(Long areaId) {
        this.areaId = areaId;
        return this;
    }

    public Long getSupportRackId() {
        return supportRackId;
    }

    public FrozenBoxAndFrozenTubeResponse setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
        return this;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public FrozenBoxAndFrozenTubeResponse setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public FrozenBoxAndFrozenTubeResponse setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public List<FrozenTubeDTO> getFrozenTubeDTOS() {
        return frozenTubeDTOS;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenTubeDTOS(List<FrozenTubeDTO> frozenTubeDTOS) {
        this.frozenTubeDTOS = frozenTubeDTOS;
        return this;
    }

    public Long getProjectId() {
        return projectId;
    }

    public FrozenBoxAndFrozenTubeResponse setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public FrozenBoxAndFrozenTubeResponse setProjectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public FrozenBoxAndFrozenTubeResponse setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
        return this;
    }

    public String getFrozenBoxTypeName() {
        return frozenBoxTypeName;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxTypeName(String frozenBoxTypeName) {
        this.frozenBoxTypeName = frozenBoxTypeName;
        return this;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
        return this;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
        return this;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }

    public FrozenBoxAndFrozenTubeResponse setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
        return this;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
        return this;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }

    public String getFrontColorForClass() {
        return frontColorForClass;
    }

    public FrozenBoxAndFrozenTubeResponse setFrontColorForClass(String frontColorForClass) {
        this.frontColorForClass = frontColorForClass;
        return this;
    }

    public String getBackColorForClass() {
        return backColorForClass;
    }

    public FrozenBoxAndFrozenTubeResponse setBackColorForClass(String backColorForClass) {
        this.backColorForClass = backColorForClass;
        return this;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
        return this;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public FrozenBoxAndFrozenTubeResponse setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public Integer getIsMixed() {
        return isMixed;
    }

    public FrozenBoxAndFrozenTubeResponse setIsMixed(Integer isMixed) {
        this.isMixed = isMixed;
        return this;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public FrozenBoxAndFrozenTubeResponse setFrontColor(String frontColor) {
        this.frontColor = frontColor;
        return this;
    }

    public String getBackColor() {
        return backColor;
    }

    public FrozenBoxAndFrozenTubeResponse setBackColor(String backColor) {
        this.backColor = backColor;
        return this;
    }

    public Integer getIsRealData() {
        return isRealData;
    }

    public FrozenBoxAndFrozenTubeResponse setIsRealData(Integer isRealData) {
        this.isRealData = isRealData;
        return this;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public FrozenBoxAndFrozenTubeResponse setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    @Override
    public String toString() {
        return "FrozenBoxAndFrozenTubeResponse{" +
            "id=" + id +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", frozenBoxCode1D='" + frozenBoxCode1D + '\'' +
            ", isSplit=" + isSplit +
            ", memo='" + memo + '\'' +
            ", status='" + status + '\'' +
            ", frozenBoxType=" + frozenBoxType +
            ", sampleClassification=" + sampleClassification +
            ", sampleType=" + sampleType +
            ", equipmentId=" + equipmentId +
            ", areaId=" + areaId +
            ", supportRackId=" + supportRackId +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", frozenTubeDTOS=" + frozenTubeDTOS +
            ", projectId=" + projectId +
            ", projectCode='" + projectCode + '\'' +
            ", projectName='" + projectName + '\'' +
            ", frozenBoxTypeId=" + frozenBoxTypeId +
            ", frozenBoxTypeName='" + frozenBoxTypeName + '\'' +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + '\'' +
            ", frozenBoxTypeRows='" + frozenBoxTypeRows + '\'' +
            ", frozenBoxTypeColumns='" + frozenBoxTypeColumns + '\'' +
            ", sampleClassificationId=" + sampleClassificationId +
            ", sampleClassificationName='" + sampleClassificationName + '\'' +
            ", sampleClassificationCode='" + sampleClassificationCode + '\'' +
            ", frontColorForClass='" + frontColorForClass + '\'' +
            ", backColorForClass='" + backColorForClass + '\'' +
            ", sampleTypeId=" + sampleTypeId +
            ", sampleTypeCode='" + sampleTypeCode + '\'' +
            ", sampleTypeName='" + sampleTypeName + '\'' +
            ", isMixed=" + isMixed +
            ", frontColor='" + frontColor + '\'' +
            ", backColor='" + backColor + '\'' +
            ", isRealData=" + isRealData +
            ", countOfSample=" + countOfSample +
            ", frozenBoxId=" + frozenBoxId +
            '}';
    }
}
