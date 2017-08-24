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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_apply")
    @SequenceGenerator(name = "seq_stock_out_apply",sequenceName = "seq_stock_out_apply",allocationSize = 1,initialValue = 1)
    private Long id;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Size(max = 1024)
    @Column(name = "purpose_of_sample", length = 1024)
    private String purposeOfSample;

    @Column(name = "record_time")
    private LocalDate recordTime;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "parent_apply_id")
    private Long parentApplyId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "approve_time")
    private LocalDate approveTime;

    @Size(max = 1024)
    @Column(name = "invalid_reason", length = 1024)
    private String invalidReason;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @Size(max = 255)
    @Column(name = "apply_person_name", length = 255)
    private String applyPersonName;

    @NotNull
    @Size(max = 100)
    @Column(name = "apply_code", length = 100, nullable = false)
    private String applyCode;

    @Column(name = "apply_date")
    private LocalDate applyDate;

    @ManyToOne
    private Delegate delegate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getInvalidReason() {
        return invalidReason;
    }

    public StockOutApply invalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
        return this;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
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

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public StockOutApply applyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
        return this;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public StockOutApply applyCode(String applyCode) {
        this.applyCode = applyCode;
        return this;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public LocalDate getApplyDate() {
        return applyDate;
    }

    public  StockOutApply applyDate(LocalDate applyDate){
        this.applyDate = applyDate;
        return this;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
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
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", purposeOfSample='" + purposeOfSample + "'" +
            ", recordTime='" + recordTime + "'" +
            ", recordId='" + recordId + "'" +
            ", parentApplyId='" + parentApplyId + "'" +
            ", approverId='" + approverId + "'" +
            ", approveTime='" + approveTime + "'" +
            ", invalidReason='" + invalidReason + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", applyPersonName='" + applyPersonName + "'" +
            ", applyCode='" + applyCode + "'" +
            '}';
    }
}
