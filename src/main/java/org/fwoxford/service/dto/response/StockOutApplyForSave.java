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
     * 申请编码
     */
    @Size(max = 100)
    private String applyCode;
    /**
     * 开始时间
     */
    private LocalDate startTime;
    /**
     * 结束时间
     */
    private LocalDate endTime;
    /**
     * 样本用途
     */
    @Size(max = 1024)
    private String purposeOfSample;
    /**
     * 记录日期
     */
    private LocalDate recordTime;
    /**
     * 记录人ID
     */
    private Long recordId;
    /**
     * 委托人
     */
    @Size(max = 255)
    private String applyPersonName;
    /**
     * 委托方ID
     */
    private Long delegateId;
    /**
     * 项目IDs
     */
    private List<Long> projectIds;

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

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutApplyDTO{" +
            "id=" + id +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", purposeOfSample='" + purposeOfSample + "'" +
            ", recordTime='" + recordTime + "'" +
            ", recordId='" + recordId + "'" +
            ", applyPersonName='" + applyPersonName + "'" +
            ", applyCode='" + applyCode + "'" +
            '}';
    }
}
