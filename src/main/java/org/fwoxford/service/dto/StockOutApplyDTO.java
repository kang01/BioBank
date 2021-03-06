package org.fwoxford.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutApply entity.
 */
public class StockOutApplyDTO implements Serializable {

    private Long id;
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

    private Long parentApplyId;

    private Long approverId;

    private LocalDate approveTime;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @Size(max = 255)
    private String applyPersonName;

    @NotNull
    @Size(max = 100)
    private String applyCode;

    private LocalDate applyDate;
    /**
     * 委托方ID
     */
    private Long delegateId;


    private String invalidReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Long getParentApplyId() {
        return parentApplyId;
    }

    public void setParentApplyId(Long parentApplyId) {
        this.parentApplyId = parentApplyId;
    }
    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }
    public LocalDate getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(LocalDate approveTime) {
        this.approveTime = approveTime;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }
    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public LocalDate getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
    }

    public Long getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(Long delegateId) {
        this.delegateId = delegateId;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutApplyDTO stockOutApplyDTO = (StockOutApplyDTO) o;

        if ( ! Objects.equals(id, stockOutApplyDTO.id)) { return false; }

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
            ", parentApplyId='" + parentApplyId + "'" +
            ", approverId='" + approverId + "'" +
            ", approveTime='" + approveTime + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", applyPersonName='" + applyPersonName + "'" +
            ", applyCode='" + applyCode + "'" +
            ", applyDate='" + applyDate + "'" +
            ", invalidReason='" + invalidReason + "'" +
            ", countOfStockSample='" + countOfStockSample + "'" +
            ", countOfHandOverSample='" + countOfHandOverSample + "'" +
            ", checkTypeId='" + checkTypeId + "'" +
            ", delegateName='" + delegateName + "'" +
            ", checkTypeName='" + checkTypeName + "'" +
            '}';
    }
    private Integer countOfStockSample;
    private Integer countOfHandOverSample;
    private Long checkTypeId;

    public Integer getCountOfStockSample() {
        return countOfStockSample;
    }

    public void setCountOfStockSample(Integer countOfStockSample) {
        this.countOfStockSample = countOfStockSample;
    }

    public Integer getCountOfHandOverSample() {
        return countOfHandOverSample;
    }

    public void setCountOfHandOverSample(Integer countOfHandOverSample) {
        this.countOfHandOverSample = countOfHandOverSample;
    }

    public Long getCheckTypeId() {
        return checkTypeId;
    }

    public void setCheckTypeId(Long checkTypeId) {
        this.checkTypeId = checkTypeId;
    }

    /**
     * 委托方名称
     */
    private String delegateName;
    /**
     * 检测类型名称
     */
    private String checkTypeName;

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getCheckTypeName() {
        return checkTypeName;
    }

    public void setCheckTypeName(String checkTypeName) {
        this.checkTypeName = checkTypeName;
    }

    private List<ProjectDTO> projectDTOS ;

    public List<ProjectDTO> getProjectDTOS() {
        return projectDTOS;
    }

    public void setProjectDTOS(List<ProjectDTO> projectDTOS) {
        this.projectDTOS = projectDTOS;
    }
}
