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
 * Created by zhuyu on 2017/5/16.
 */
@Entity
@Table(name = "view_stock_out_handover_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutHandoverSampleReportDTO {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    private Long no;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "frozen_box_code")
    private String boxCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "location")
    private String location;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_code")
    private String sampleCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_type_name")
    private String sampleType;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "gender")
    private String sex;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "age")
    private String age;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "disease_type")
    private String diseaseType;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "stock_out_handover_id")
    private Long stockOutHandoverId;

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

    public Long getStockOutHandoverId() {
        return stockOutHandoverId;
    }

    public void setStockOutHandoverId(Long stockOutHandoverId) {
        this.stockOutHandoverId = stockOutHandoverId;
    }
}
