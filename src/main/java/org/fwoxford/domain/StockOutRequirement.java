package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutRequirement.
 */
@Entity
@Table(name = "stock_out_requirement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutRequirement extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "apply_number", length = 100, nullable = false)
    private String apply_number;

    @NotNull
    @Size(max = 100)
    @Column(name = "requirement_number", length = 100, nullable = false)
    private String requirementNumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "requirement_name", length = 255, nullable = false)
    private String requirementName;

    @Column(name = "count_of_sample")
    private Integer countOfSample;

    @Column(name = "sex")
    private String sex;

    @Column(name = "age_min")
    private Integer ageMin;

    @Column(name = "age_max")
    private Integer ageMax;

    @Column(name = "disease_type")
    private Integer diseaseType;

    @Column(name = "is_hemolysis")
    private Boolean isHemolysis;

    @Column(name = "is_blood_lipid")
    private Boolean isBloodLipid;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutApply stockOutApply;

    @ManyToOne
    private SampleType sampleType;

    @ManyToOne
    private SampleClassification sampleClassification;

    @ManyToOne
    private FrozenTubeType frozenTubeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApply_number() {
        return apply_number;
    }

    public StockOutRequirement apply_number(String apply_number) {
        this.apply_number = apply_number;
        return this;
    }

    public void setApply_number(String apply_number) {
        this.apply_number = apply_number;
    }

    public String getRequirementNumber() {
        return requirementNumber;
    }

    public StockOutRequirement requirementNumber(String requirementNumber) {
        this.requirementNumber = requirementNumber;
        return this;
    }

    public void setRequirementNumber(String requirementNumber) {
        this.requirementNumber = requirementNumber;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public StockOutRequirement requirementName(String requirementName) {
        this.requirementName = requirementName;
        return this;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public StockOutRequirement countOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getSex() {
        return sex;
    }

    public StockOutRequirement sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public StockOutRequirement ageMin(Integer ageMin) {
        this.ageMin = ageMin;
        return this;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public StockOutRequirement ageMax(Integer ageMax) {
        this.ageMax = ageMax;
        return this;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public Integer getDiseaseType() {
        return diseaseType;
    }

    public StockOutRequirement diseaseType(Integer diseaseType) {
        this.diseaseType = diseaseType;
        return this;
    }

    public void setDiseaseType(Integer diseaseType) {
        this.diseaseType = diseaseType;
    }

    public Boolean isIsHemolysis() {
        return isHemolysis;
    }

    public StockOutRequirement isHemolysis(Boolean isHemolysis) {
        this.isHemolysis = isHemolysis;
        return this;
    }

    public void setIsHemolysis(Boolean isHemolysis) {
        this.isHemolysis = isHemolysis;
    }

    public Boolean isIsBloodLipid() {
        return isBloodLipid;
    }

    public StockOutRequirement isBloodLipid(Boolean isBloodLipid) {
        this.isBloodLipid = isBloodLipid;
        return this;
    }

    public void setIsBloodLipid(Boolean isBloodLipid) {
        this.isBloodLipid = isBloodLipid;
    }

    public String getStatus() {
        return status;
    }

    public StockOutRequirement status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutRequirement memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public StockOutApply getStockOutApply() {
        return stockOutApply;
    }

    public StockOutRequirement stockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
        return this;
    }

    public void setStockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public StockOutRequirement sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }

    public StockOutRequirement sampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }

    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public FrozenTubeType getFrozenTubeType() {
        return frozenTubeType;
    }

    public StockOutRequirement frozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
        return this;
    }

    public void setFrozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutRequirement stockOutRequirement = (StockOutRequirement) o;
        if (stockOutRequirement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutRequirement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutRequirement{" +
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
