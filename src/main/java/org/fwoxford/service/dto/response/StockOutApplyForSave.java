package org.fwoxford.service.dto.response;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Created by gengluying on 2017/5/16.
 */
public class StockOutApplyForSave {
    /**
     * 申请ID
     */
    private Long id;
    /**
     * 申请单号
     */
    private String applyCode;
    /**
     * 委托方ID
     */
    private Long delegateId;
    /**
     * 实际样本量
     */
    private Long countOfStockOutSample;
    /**
     * 样本量
     */
    private Long countOfSample;
    /**
     * 委托方名称
     */
    private String delegateName;
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
     * 记录人姓名
     */
    private String recorder;
    /**
     * 开始时间
     */
    private LocalDate startTime;
    /**
     * 记录时间
     */
    private LocalDate endTime;

    /**
     * 状态
     */
    private String status;
    /**
     * 授权的项目
     */
    private List<Long> projectIds;
    /**
     * 授权的项目编码
     */
    private String projectCodes;
    /**
     * 授权的项目名称
     */
    private String projectNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
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

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
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

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public Long getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(Long delegateId) {
        this.delegateId = delegateId;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public Long getCountOfStockOutSample() {
        return countOfStockOutSample;
    }

    public void setCountOfStockOutSample(Long countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getProjectCodes() {
        return projectCodes;
    }

    public void setProjectCodes(String projectCodes) {
        this.projectCodes = projectCodes;
    }

    public String getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(String projectNames) {
        this.projectNames = projectNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutApplyForSave stockOutApplyForSave = (StockOutApplyForSave) o;

        if ( ! Objects.equals(id, stockOutApplyForSave.id)) { return false; }

        return true;
    }

}
