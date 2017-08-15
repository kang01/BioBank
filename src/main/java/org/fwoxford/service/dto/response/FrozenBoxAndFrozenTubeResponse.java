package org.fwoxford.service.dto.response;


import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.domain.SampleClassification;
import org.fwoxford.domain.SampleType;

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
    private List<FrozenTubeResponse> frozenTubeDTOS;

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

    public Integer getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Integer isSplit) {
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

    public List<FrozenTubeResponse> getFrozenTubeDTOS() {
        return frozenTubeDTOS;
    }

    public void setFrozenTubeDTOS(List<FrozenTubeResponse> frozenTubeDTOS) {
        this.frozenTubeDTOS = frozenTubeDTOS;
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

    public FrozenBoxType getFrozenBoxType() {
        return frozenBoxType;
    }

    public void setFrozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }

    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public String getFrozenBoxTypeName() {
        return frozenBoxTypeName;
    }

    public void setFrozenBoxTypeName(String frozenBoxTypeName) {
        this.frozenBoxTypeName = frozenBoxTypeName;
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

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getFrontColorForClass() {
        return frontColorForClass;
    }

    public void setFrontColorForClass(String frontColorForClass) {
        this.frontColorForClass = frontColorForClass;
    }

    public String getBackColorForClass() {
        return backColorForClass;
    }

    public void setBackColorForClass(String backColorForClass) {
        this.backColorForClass = backColorForClass;
    }

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

    public Integer getIsMixed() {
        return isMixed;
    }

    public void setIsMixed(Integer isMixed) {
        this.isMixed = isMixed;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(String frontColor) {
        this.frontColor = frontColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }
    /**
     * 是否已导入样本数据：1：是，0：否
     */
    private Integer isRealData;
    private Integer countOfSample;

    public Integer getIsRealData() {
        return isRealData;
    }

    public void setIsRealData(Integer isRealData) {
        this.isRealData = isRealData;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }
}
