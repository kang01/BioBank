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
 * Created by gengluying on 2017/5/31.
 */
@Entity
@Table(name = "view_stock_out_task_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutFrozenTubeDataTableEntity {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "frozen_box_code")
    private String frozenBoxCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_type_name")
    private String sampleTypeName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_code")
    private String sampleCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "gender")
    private String sex;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "age")
    private Integer age;
    /**
     * 疾病类型：1：2：3：
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "disease_type")
    private String diseaseTypeId;

    /**
     * 是否溶血 ：true:是  false:否
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "is_hemolysis")
    private Boolean isHemolysis;
    /**
     * 是否脂质血 ：true:是  false:否
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "is_blood_lipid")
    private Boolean isBloodLipid;

    @Column(name = "stock_out_frozen_box_id")
    private Long stockOutFrozenBoxId;

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

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
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

    public String getDiseaseTypeId() {
        return diseaseTypeId;
    }

    public void setDiseaseTypeId(String diseaseTypeId) {
        this.diseaseTypeId = diseaseTypeId;
    }

    public Boolean getHemolysis() {
        return isHemolysis;
    }

    public void setHemolysis(Boolean hemolysis) {
        isHemolysis = hemolysis;
    }

    public Boolean getBloodLipid() {
        return isBloodLipid;
    }

    public void setBloodLipid(Boolean bloodLipid) {
        isBloodLipid = bloodLipid;
    }

    public Long getStockOutFrozenBoxId() {
        return stockOutFrozenBoxId;
    }

    public void setStockOutFrozenBoxId(Long stockOutFrozenBoxId) {
        this.stockOutFrozenBoxId = stockOutFrozenBoxId;
    }

    public StockOutFrozenTubeDataTableEntity() {
    }

    public StockOutFrozenTubeDataTableEntity(Long id, String frozenBoxCode, String sampleTypeName, String sampleCode, String sex, Integer age, String diseaseTypeId, Boolean isHemolysis, Boolean isBloodLipid, Long stockOutFrozenBoxId) {
        this.id = id;
        this.frozenBoxCode = frozenBoxCode;
        this.sampleTypeName = sampleTypeName;
        this.sampleCode = sampleCode;
        this.sex = sex;
        this.age = age;
        this.diseaseTypeId = diseaseTypeId;
        this.isHemolysis = isHemolysis;
        this.isBloodLipid = isBloodLipid;
        this.stockOutFrozenBoxId = stockOutFrozenBoxId;
    }
}
