package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StockOutApply.
 */
@Entity
@Table(name = "stock_out_apply")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutApply extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "apply_number", length = 100, nullable = false)
    private String applyNumber;

    @Size(max = 255)
    @Column(name = "delegate_persion_name", length = 255)
    private String delegatePersionName;

    @Column(name = "delegate_date")
    private LocalDate delegateDate;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Column(name = "count_of_sample")
    private Integer countOfSample;

    @Size(max = 1024)
    @Column(name = "purpose_of_sample", length = 1024)
    private String purposeOfSample;

    @Column(name = "record_time")
    private LocalDate recordTime;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "project_ids")
    private String projectIds;

    @Column(name = "parent_apply_id")
    private Long parentApplyId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "approve_time")
    private LocalDate approveTime;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private Delegate delegate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyNumber() {
        return applyNumber;
    }

    public StockOutApply applyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
        return this;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }

    public String getDelegatePersionName() {
        return delegatePersionName;
    }

    public StockOutApply delegatePersionName(String delegatePersionName) {
        this.delegatePersionName = delegatePersionName;
        return this;
    }

    public void setDelegatePersionName(String delegatePersionName) {
        this.delegatePersionName = delegatePersionName;
    }

    public LocalDate getDelegateDate() {
        return delegateDate;
    }

    public StockOutApply delegateDate(LocalDate delegateDate) {
        this.delegateDate = delegateDate;
        return this;
    }

    public void setDelegateDate(LocalDate delegateDate) {
        this.delegateDate = delegateDate;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public StockOutApply startTime(LocalDate startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public StockOutApply endTime(LocalDate endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public StockOutApply countOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public StockOutApply purposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
        return this;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
    }

    public LocalDate getRecordTime() {
        return recordTime;
    }

    public StockOutApply recordTime(LocalDate recordTime) {
        this.recordTime = recordTime;
        return this;
    }

    public void setRecordTime(LocalDate recordTime) {
        this.recordTime = recordTime;
    }

    public Long getRecordId() {
        return recordId;
    }

    public StockOutApply recordId(Long recordId) {
        this.recordId = recordId;
        return this;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getProjectIds() {
        return projectIds;
    }

    public StockOutApply projectIds(String projectIds) {
        this.projectIds = projectIds;
        return this;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public Long getParentApplyId() {
        return parentApplyId;
    }

    public StockOutApply parentApplyId(Long parentApplyId) {
        this.parentApplyId = parentApplyId;
        return this;
    }

    public void setParentApplyId(Long parentApplyId) {
        this.parentApplyId = parentApplyId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public StockOutApply approverId(Long approverId) {
        this.approverId = approverId;
        return this;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public LocalDate getApproveTime() {
        return approveTime;
    }

    public StockOutApply approveTime(LocalDate approveTime) {
        this.approveTime = approveTime;
        return this;
    }

    public void setApproveTime(LocalDate approveTime) {
        this.approveTime = approveTime;
    }

    public String getStatus() {
        return status;
    }

    public StockOutApply status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutApply memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Delegate getDelegate() {
        return delegate;
    }

    public StockOutApply delegate(Delegate delegate) {
        this.delegate = delegate;
        return this;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutApply stockOutApply = (StockOutApply) o;
        if (stockOutApply.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutApply.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutApply{" +
            "id=" + id +
            ", applyNumber='" + applyNumber + "'" +
            ", delegatePersionName='" + delegatePersionName + "'" +
            ", delegateDate='" + delegateDate + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", countOfSample='" + countOfSample + "'" +
            ", purposeOfSample='" + purposeOfSample + "'" +
            ", recordTime='" + recordTime + "'" +
            ", recordId='" + recordId + "'" +
            ", projectIds='" + projectIds + "'" +
            ", parentApplyId='" + parentApplyId + "'" +
            ", approverId='" + approverId + "'" +
            ", approveTime='" + approveTime + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
