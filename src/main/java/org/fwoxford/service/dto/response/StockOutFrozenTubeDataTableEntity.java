package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2017/5/31.
 */
public class StockOutFrozenTubeDataTableEntity {
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String frozenBoxCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String sampleTypeName;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String sampleCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String sex;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Integer age;
    /**
     * 疾病类型：1：2：3：
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String diseaseTypeId;
    /**
     * 是否溶血 ：true:是  false:否
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Boolean isHemolysis;
    /**
     * 是否脂质血 ：true:是  false:否
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Boolean isBloodLipid;

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
}
