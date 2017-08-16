package org.fwoxford.service.dto;


import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.domain.SampleClassification;
import org.fwoxford.domain.SampleType;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the StockInBox entity.
 */
public class StockInBoxDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
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
     * 所在冻存架行数
     */
    @Size(max = 20)
    private String rowsInShelf;
    /**
     * 所在冻存架列数
     */
    @Size(max = 20)
    private String columnsInShelf;
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
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
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
     * 冻存盒Id
     */
    private Long frozenBoxId;
    /**
     * 入库Id
     */
    private Long stockInId;
    /**
     * 入库编码
     */
    private String stockInCode;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
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

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockInBoxDTO stockInBoxDTO = (StockInBoxDTO) o;

        if ( ! Objects.equals(id, stockInBoxDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInBoxDTO{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", memo='" + memo + '\'' +
            ", status='" + status + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
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
            ", frozenBoxId=" + frozenBoxId +
            ", stockInId=" + stockInId +
            ", stockInCode='" + stockInCode + '\'' +
            ", equipmentId=" + equipmentId +
            ", supportRackId=" + supportRackId +
            ", areaId=" + areaId +
            '}';
    }
    /**
     * 冻存管列表
     */
    private List<StockInTubeDTO> frozenTubeDTOS;
    /**
     * 样本分类前景色
     */
    private String frontColorForClass;
    /**
     * 样本分类背景色
     */
    private String backColorForClass;
    /**
     * 样本类型是否混合
     */
    private Integer isMixed;

    /**
     * 样本类型前景色
     */
    private String frontColor;
    /**
     * 样本类型背景色
     */
    private String backColor;
    private FrozenBoxType frozenBoxType;
    private SampleClassification sampleClassification;
    private SampleType sampleType;
    public List<StockInTubeDTO> getFrozenTubeDTOS() {
        return frozenTubeDTOS;
    }

    public StockInBoxDTO setFrozenTubeDTOS(List<StockInTubeDTO> frozenTubeDTOS) {
        this.frozenTubeDTOS = frozenTubeDTOS;
        return this;
    }

    public String getFrontColorForClass() {
        return frontColorForClass;
    }

    public StockInBoxDTO setFrontColorForClass(String frontColorForClass) {
        this.frontColorForClass = frontColorForClass;
        return this;
    }

    public String getBackColorForClass() {
        return backColorForClass;
    }

    public StockInBoxDTO setBackColorForClass(String backColorForClass) {
        this.backColorForClass = backColorForClass;
        return this;
    }

    public Integer getIsMixed() {
        return isMixed;
    }

    public StockInBoxDTO setIsMixed(Integer isMixed) {
        this.isMixed = isMixed;
        return this;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public StockInBoxDTO setFrontColor(String frontColor) {
        this.frontColor = frontColor;
        return this;
    }

    public String getBackColor() {
        return backColor;
    }

    public StockInBoxDTO setBackColor(String backColor) {
        this.backColor = backColor;
        return this;
    }

    public FrozenBoxType getFrozenBoxType() {
        return frozenBoxType;
    }

    public StockInBoxDTO setFrozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
        return this;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }

    public StockInBoxDTO setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public StockInBoxDTO setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }
}
