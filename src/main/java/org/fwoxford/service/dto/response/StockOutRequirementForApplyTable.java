package org.fwoxford.service.dto.response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/5/15.
 */
public class StockOutRequirementForApplyTable {
    /**
     * 需求ID
     */
    private Long id;
    /**
     * 需求名称
     */
    private String requirementName;
    /**
     * 样本量
     */
    private Integer countOfSample;
    /**
     * 性别：女，男
     */
    private String sex;
    /**
     * 年龄段 ：10-20岁
     */
    private String age;
    /**
     * 疾病类型：1：2：3：
     */
    private String diseaseTypeId;
    /**
     * 是否溶血 ：true:是  false:否
     */
    private Boolean isHemolysis;
    /**
     * 是否脂质血 ：true:是  false:否
     */
    private Boolean isBloodLipid;

    /**
     * 样本类型
     */
    private String sampleTypeName;
    /**
     * 状态：1201：待核对，1202：库存不够，1203：库存满足
     */
    private String status;
    /**
     * 冻存管类型
     */
    private String frozenTubeTypeName;
    /**
     * 指定的样本名称
     */
    private String samples;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public String getSamples() {
        return samples;
    }

    public void setSamples(String samples) {
        this.samples = samples;
    }
}
