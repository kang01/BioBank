package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2017/5/18.
 */
@Entity
@Table(name = "view_requirement_sample")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutRequirementFrozenTubeDetail {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_code")
    private String sampleCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "status")
    private String status;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_type_name")
    private String sampleTypeName;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sex")
    private String sex;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "age")
    private Integer age;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "project_code")
    private String projectCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "disease_type_id")
    private String diseaseTypeId;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "is_hemolysis")
    private Boolean isHemolysis;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "is_blood_lipid")
    private Boolean isBloodLipid;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_used_times")
    private Integer sampleUsedTimes;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "memo")
    private String memo;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "stock_out_requirement_id")
    private Long stockOutRequirementId;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "frozen_box_code_1d")
    private String frozenBoxCode1D;
    @Column(name = "frozen_box_code")
    private String frozenBoxCode;
    @Column(name = "frozen_box_id")
    private Long frozenBoxId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getDiseaseTypeId() {
        return diseaseTypeId;
    }

    public void setDiseaseTypeId(String diseaseTypeId) {
        this.diseaseTypeId = diseaseTypeId;
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

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getStockOutRequirementId() {
        return stockOutRequirementId;
    }

    public void setStockOutRequirementId(Long stockOutRequirementId) {
        this.stockOutRequirementId = stockOutRequirementId;
    }

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }
}
