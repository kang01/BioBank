package org.fwoxford.service.dto;


import io.swagger.models.auth.In;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FrozenTube entity.
 */
public class FrozenTubeDTO extends FrozenTubeLabelDTO  implements Serializable {

    private Long id;
    /**
     * 项目编码
     */
    @NotNull
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
    @NotNull
    @Size(max = 255)
    private String frozenTubeTypeName;
    /**
     * 样本类型编码
     */
    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @NotNull
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
     * 行数
     */
    @NotNull
    @Size(max = 20)
    private String tubeRows;
    /**
     * 列数
     */
    @NotNull
    @Size(max = 20)
    private String tubeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
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
     * 状态：3001：正常，3002：空管，3003：空孔；3004：异常;3005:半管
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 冻存盒编码
     */
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 项目组中患者ID
     */
    private Long patientId;
    /**
     * 患者出生日期
     */
    private ZonedDateTime dob;
    /**
     * 患者性别
     */
    @Size(max = 255)
    private String gender;
    /**
     * 疾病类型
     */
    @Size(max = 255)
    private String diseaseType;

    private Boolean isHemolysis;

    private Boolean isBloodLipid;
    /**
     * 就诊类型
     */
    @Size(max = 255)
    private String visitType;
    /**
     * 就诊日期
     */
    private ZonedDateTime visitDate;
    /**
     * 年龄
     */
    private Integer age;
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
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 冻存盒ID
     */
    private Long frozenBoxId;

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
    /**
     * 样本分类名称
     */
    private String sampleClassificationName;
    /**
     * 项目点ID
     */
    private Long projectSiteId;
    /**
     * 项目点编码
     */
    private String projectSiteCode;

    private String position;
    /**
     * 样本分期
     */
    private String sampleStage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    public ZonedDateTime getDob() {
        return dob;
    }

    public void setDob(ZonedDateTime dob) {
        this.dob = dob;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }
    public void setIsHemolysis(Boolean isHemolysis) {
        this.isHemolysis = isHemolysis;
    }
    public Boolean getIsBloodLipid() {
        return isBloodLipid;
    }

    public void setIsBloodLipid(Boolean isBloodLipid) {
        this.isBloodLipid = isBloodLipid;
    }
    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }
    public ZonedDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(ZonedDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public Boolean getHemolysis() {
        return isHemolysis;
    }

    public void setHemolysis(Boolean hemolysis) {
        this.isHemolysis = hemolysis;
    }

    public Boolean getBloodLipid() {
        return isBloodLipid;
    }

    public void setBloodLipid(Boolean bloodLipid) {
        this.isBloodLipid = bloodLipid;
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

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSampleStage() {
        return sampleStage;
    }

    public void setSampleStage(String sampleStage) {
        this.sampleStage = sampleStage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FrozenTubeDTO frozenTubeDTO = (FrozenTubeDTO) o;

        if ( ! Objects.equals(id, frozenTubeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenTubeDTO{" +
            "id=" + id +
            ", projectCode='" + projectCode + "'" +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + "'" +
            ", frozenTubeTypeName='" + frozenTubeTypeName + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", sampleUsedTimesMost='" + sampleUsedTimesMost + "'" +
            ", sampleUsedTimes='" + sampleUsedTimes + "'" +
            ", frozenTubeVolumns='" + frozenTubeVolumns + "'" +
            ", frozenTubeVolumnsUnit='" + frozenTubeVolumnsUnit + "'" +
            ", sampleVolumns='" + sampleVolumns + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", memo='" + memo + "'" +
            ", errorType='" + errorType + "'" +
            ", frozenTubeState ='"+ frozenTubeState + "'" +
            ", status='" + status + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", patientId='" + patientId + "'" +
            ", dob='" + dob + "'" +
            ", gender='" + gender + "'" +
            ", diseaseType='" + diseaseType + "'" +
            ", isHemolysis='" + isHemolysis + "'" +
            ", isBloodLipid='" + isBloodLipid + "'" +
            ", visitType='" + visitType + "'" +
            ", visitDate='" + visitDate + "'" +
            ", age='" + age + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", position='" + position + "'" +
            ", sampleStage='" + sampleStage + "'" +
            '}';
    }

    /**
     * 管子标识，2：原盒原库存，1：盒内新加入的冻存管
     */
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * 出库标识:1:出库,2:取消出库
     */
    private Integer stockOutFlag;
    /**
     * 取消出库的原因
     */
    private String repealReason;

    public Integer getStockOutFlag() {
        return stockOutFlag;
    }

    public void setStockOutFlag(Integer stockOutFlag) {
        this.stockOutFlag = stockOutFlag;
    }

    public String getRepealReason() {
        return repealReason;
    }

    public void setRepealReason(String repealReason) {
        this.repealReason = repealReason;
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

    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
