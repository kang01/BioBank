package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;

/**
 * Created by zhuyu on 2017/5/16.
 */
public class StockOutHandoverSampleReportDTO {
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    Long id;
    Long no;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    String boxCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    String location;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    String sampleCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    String sampleType;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    String sex;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    String age;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    String diseaseType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
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

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    String projectCode;
}
