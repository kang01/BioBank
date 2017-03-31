package org.fwoxford.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FrozenTube entity.
 */
public class FrozenTubeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String projectCode;

    @NotNull
    @Size(max = 100)
    private String frozenTubeCode;

    @NotNull
    @Size(max = 100)
    private String sampleTempCode;

    @NotNull
    @Size(max = 100)
    private String sampleCode;

    @Size(max = 100)
    private String frozenTubeTypeCode;

    @NotNull
    @Size(max = 255)
    private String frozenTubeTypeName;

    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    private String sampleTypeName;

    @Max(value = 20)
    private Integer sampleUsedTimesMost;

    @Max(value = 20)
    private Integer sampleUsedTimes;

    @Max(value = 20)
    private Integer frozenTubeVolumns;

    @Size(max = 20)
    private String frozenTubeVolumnsUnit;

    @NotNull
    @Size(max = 20)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    private String tubeColumns;

    @Size(max = 1024)
    private String memo;

    @Size(max = 20)
    private String errorType;

    @NotNull
    @Size(max = 20)
    private String status;

    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;

    private Long patientId;

    private ZonedDateTime dob;

    @Size(max = 255)
    private String gender;

    @Size(max = 255)
    private String diseaseType;

    @Size(max = 255)
    private String visitType;

    private ZonedDateTime visitDate;

    private Long frozenTubeTypeId;

    private Long sampleTypeId;

    private Long projectId;

    private Long frozenBoxId;

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
    public Integer getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }

    public void setFrozenTubeVolumns(Integer frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }
    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }

    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
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
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", memo='" + memo + "'" +
            ", errorType='" + errorType + "'" +
            ", status='" + status + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", patientId='" + patientId + "'" +
            ", dob='" + dob + "'" +
            ", gender='" + gender + "'" +
            ", diseaseType='" + diseaseType + "'" +
            ", visitType='" + visitType + "'" +
            ", visitDate='" + visitDate + "'" +
            '}';
    }
}
