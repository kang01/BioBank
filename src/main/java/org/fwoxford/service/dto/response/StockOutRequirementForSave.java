package org.fwoxford.service.dto.response;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/5/16.
 */
public class StockOutRequirementForSave {
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
    private Integer diseaseTypeId;
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
     * 样本分类ID
     */
    private Long sampleClassificationId;
    /**
     * 冻存管类型ID
     */
    private Long frozenTubeTypeId;

    private MultipartFile file;

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

    public Integer getDiseaseTypeId() {
        return diseaseTypeId;
    }

    public void setDiseaseTypeId(Integer diseaseTypeId) {
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

    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
