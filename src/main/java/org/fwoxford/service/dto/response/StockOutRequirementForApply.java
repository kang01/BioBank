package org.fwoxford.service.dto.response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/5/15.
 */
public class StockOutRequirementForApply {
    /**
     * 需求ID
     */
    private Long id;
    /**
     * 需求名称
     */
    @NotNull
    @Size(max = 255)
    private String requirementName;
    /**
     * 样本量
     */
    private Integer countOfSample;
    /**
     * 性别：f:女，m:男,n:不详
     */
    private String sex;
    /**
     * 年龄段 ：10；20
     */
    private String age;
    /**
     * 疾病类型：1：2：3：
     */
    private Integer diseaseType;
    /**
     * 是否溶血 ：true:是  false:否
     */
    private Boolean isHemolysis;
    /**
     * 是否脂质血 ：true:是  false:否
     */
    private Boolean isBloodLipid;

    @Size(max = 1024)
    private String memo;
    /**
     * 样本类型ID
     */
    private Long sampleTypeId;
    /**
     * 状态：1201：待核对，1202：库存不够，1203：库存满足
     */
    private String status;
    /**
     * 样本分类ID
     */
    private Long sampleClassificationId;
    /**
     * 冻存管类型ID
     */
    private Long frozenTubeTypeId;
    /**
     * 指定的样本：code+类型，如：1234567890-血浆，1234567891-血清
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

    public Integer getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(Integer diseaseType) {
        this.diseaseType = diseaseType;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSamples() {
        return samples;
    }

    public void setSamples(String samples) {
        this.samples = samples;
    }
}
