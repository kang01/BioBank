package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TranshipTube entity.
 */
public class TranshipTubeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String tubeColumns;

    @NotNull
    @Size(max = 20)
    private String tubeRows;
    /**
     * 项目编码
     */
    @Size(max = 100)
    private String projectCode;
    /**
     * 冻存管编码
     */
    @Size(max = 100)
    private String frozenTubeCode;
    /**
     * 样本临时编码
     */
    @Size(max = 100)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @Size(max = 100)
    private String sampleCode;
    /**
     * 冻存管类型编码
     */
    @Size(max = 100)
    private String frozenTubeTypeCode;
    /**
     * 冻存管类型名称
     */
    @Size(max = 255)
    private String frozenTubeTypeName;
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
    /**
     * 样本最多使用次数
     */
    @Max(value = 20)
    private Integer sampleUsedTimesMost;
    /**
     * 样本已使用次数
     */
    @Max(value = 20)
    private Integer sampleUsedTimes;
    /**
     * 冻存管容量值
     */
    @Max(value = 20)
    private Double frozenTubeVolumns;
    /**
     * 冻存管容量值单位
     */
    @Size(max = 20)
    private String frozenTubeVolumnsUnit;

    private Double sampleVolumns;
    /**
     * 错误类型：6001：位置错误，6002：样本类型错误，6003：其他
     */
    @Size(max = 20)
    private String errorType;
    /**
     * 状态
     */
    @Size(max = 20)
    private String frozenTubeState;
    /**
     * 冻存盒编码
     */
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 冻存管类型ID
     */
    private Long frozenTubeTypeId;
    /**
     * 样本类型ID
     */
    private Long sampleTypeId;
    /**
     *样本分类ID
     */
    private Long sampleClassificationId;
    private String sampleClassificationCode;
    private String sampleClassificationName;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目点ID
     */
    private Long projectSiteId;
    /**
     * 项目点编码
     */
    private String projectSiteCode;
    private Long transhipBoxId;

    private Long frozenTubeId;

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

    private Long frozenBoxId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
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

    public Double getSampleVolumns() {
        return sampleVolumns;
    }

    public void setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }

    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public void setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
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

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public Long getTranshipBoxId() {
        return transhipBoxId;
    }

    public void setTranshipBoxId(Long transhipBoxId) {
        this.transhipBoxId = transhipBoxId;
    }

    public Long getFrozenTubeId() {
        return frozenTubeId;
    }

    public void setFrozenTubeId(Long frozenTubeId) {
        this.frozenTubeId = frozenTubeId;
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

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranshipTubeDTO transhipTubeDTO = (TranshipTubeDTO) o;

        if ( ! Objects.equals(id, transhipTubeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipTubeDTO{" +
            "id=" + id +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", tubeColumns='" + tubeColumns + '\'' +
            ", tubeRows='" + tubeRows + '\'' +
            ", projectCode='" + projectCode + '\'' +
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
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", frozenTubeTypeId=" + frozenTubeTypeId +
            ", sampleTypeId=" + sampleTypeId +
            ", sampleClassificationId=" + sampleClassificationId +
            ", sampleClassificationCode='" + sampleClassificationCode + '\'' +
            ", sampleClassificationName='" + sampleClassificationName + '\'' +
            ", projectId=" + projectId +
            ", projectSiteId=" + projectSiteId +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", transhipBoxId=" + transhipBoxId +
            ", frozenTubeId=" + frozenTubeId +
            '}';
    }

    private Long parentSampleId;
    private String parentSampleCode;

    public Long getParentSampleId() {
        return parentSampleId;
    }

    public void setParentSampleId(Long parentSampleId) {
        this.parentSampleId = parentSampleId;
    }

    public String getParentSampleCode() {
        return parentSampleCode;
    }

    public void setParentSampleCode(String parentSampleCode) {
        this.parentSampleCode = parentSampleCode;
    }

}
