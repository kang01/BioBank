package org.fwoxford.service.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhuyu on 2017/5/15.
 */
public class StockOutApplyReportDTO {
    private Long id;

    private String applyNumber;

    public String getApplyCompany() {
        return applyCompany;
    }

    public void setApplyCompany(String applyCompany) {
        this.applyCompany = applyCompany;
    }

    private String applyCompany;
    private LocalDate startDate;
    private LocalDate endDate;

    private Integer countOfSample;
    private Integer countOfStockOutSample;

    private String purposeOfSample;

    private List<String> projects;

    private String memo;


    private String applicantName;
    private LocalDate applicationDate;

    private LocalDate recordDate;
    private String recorderName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public Integer getCountOfStockOutSample() {
        return countOfStockOutSample;
    }

    public void setCountOfStockOutSample(Integer countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
    }

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    public List<StockOutRequirementReportDTO> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<StockOutRequirementReportDTO> requirements) {
        this.requirements = requirements;
    }

    private List<StockOutRequirementReportDTO> requirements;
}
