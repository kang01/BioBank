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
     * 疾病
     */
    private String disease;
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
     * 指定的样本：code+类型，如：1234567890-血浆，1234567891-血清
     */
    private String samples;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
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
