package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutFrozenTubeForPlan {
    private Long id;
    private String sampleCode;
    private String status;
    private String SampleTypeName;
    private String sex;
    private Integer age;
    private Long sampleUsedTimes;
    private String memo;

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
        return SampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        SampleTypeName = sampleTypeName;
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

    public Long getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public void setSampleUsedTimes(Long sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
