package org.fwoxford.service.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by gengluying on 2017/5/15.
 */
public class StockOutApplyByOne {
    /**
       * 申请ID
     */
    private Long id;
    /**
     * 委托方ID
     */
    private Long delegateId;
    /**
     * 样本用途
     */
    private String purposeOfSample;
    /**
     * 委托人
     */
    private String applyPersonName;
    /**
     * 记录日期
     */
    private LocalDate recordTime;
    /**
     * 记录人ID
     */
    private Long recordId;
    /**
     * 开始时间
     */
    private LocalDate startTime;
    /**
     * 记录时间
     */
    private LocalDate endTime;
    /**
     * 申请单号
     */
    private String applyCode;
    /**
     * 状态
     */
    private String status;
    /**
     * 授权的项目
     */
    private List<ProjectResponse> projects;
    /**
     * 样本需求
     */
    private List<StockOutRequirementForApplyTable> stockOutRequirement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(Long delegateId) {
        this.delegateId = delegateId;
    }

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
    }
    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public LocalDate getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDate recordTime) {
        this.recordTime = recordTime;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProjectResponse> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectResponse> projects) {
        this.projects = projects;
    }

    public List<StockOutRequirementForApplyTable> getStockOutRequirement() {
        return stockOutRequirement;
    }

    public void setStockOutRequirement(List<StockOutRequirementForApplyTable> stockOutRequirement) {
        this.stockOutRequirement = stockOutRequirement;
    }
}
