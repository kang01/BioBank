package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutRequirement entity.
 */
public class StockOutRequirementDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String apply_number;

    @NotNull
    @Size(max = 100)
    private String requirementNumber;

    @NotNull
    @Size(max = 255)
    private String requirementName;

    private Integer countOfSample;

    private String sex;

    private Integer ageMin;

    private Integer ageMax;

    private Integer diseaseType;

    private Boolean isHemolysis;

    private Boolean isBloodLipid;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long stockOutApplyId;

    private Long sampleTypeId;

    private Long sampleClassificationId;

    private Long frozenTubeTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getApply_number() {
        return apply_number;
    }

    public void setApply_number(String apply_number) {
        this.apply_number = apply_number;
    }
    public String getRequirementNumber() {
        return requirementNumber;
    }

    public void setRequirementNumber(String requirementNumber) {
        this.requirementNumber = requirementNumber;
    }
    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }
    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }
    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }
    public Integer getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(Integer diseaseType) {
        this.diseaseType = diseaseType;
    }
    public Boolean getIsHemolysis() {
        return isHemolysis;
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

    public Long getStockOutApplyId() {
        return stockOutApplyId;
    }

    public void setStockOutApplyId(Long stockOutApplyId) {
        this.stockOutApplyId = stockOutApplyId;
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

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public void setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutRequirementDTO stockOutRequirementDTO = (StockOutRequirementDTO) o;

        if ( ! Objects.equals(id, stockOutRequirementDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutRequirementDTO{" +
            "id=" + id +
            ", apply_number='" + apply_number + "'" +
            ", requirementNumber='" + requirementNumber + "'" +
            ", requirementName='" + requirementName + "'" +
            ", countOfSample='" + countOfSample + "'" +
            ", sex='" + sex + "'" +
            ", ageMin='" + ageMin + "'" +
            ", ageMax='" + ageMax + "'" +
            ", diseaseType='" + diseaseType + "'" +
            ", isHemolysis='" + isHemolysis + "'" +
            ", isBloodLipid='" + isBloodLipid + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
