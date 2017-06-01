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
 * Created by gengluying on 2017/5/23.
 */
@Entity
@Table(name = "view_stock_out_handover_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutFrozenBoxForDataTableEntity {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="frozen_box_code")
    private String frozenBoxCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="position")
    private String position;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="count_of_sample")
    private Long countOfSample;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_type_name")
    private String sampleTypeName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="apply_id")
    private Long applyId;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="apply_code")
    private String applyCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="plan_id")
    private Long planId;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="plan_code")
    private String planCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="task_id")
    private Long taskId;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="task_code")
    private String taskCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="delegate_id")
    private Long delegateId;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="delegate")
    private String delegate;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="memo")
    private String memo;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Long getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(Long delegateId) {
        this.delegateId = delegateId;
    }

    public String getDelegate() {
        return delegate;
    }

    public void setDelegate(String delegate) {
        this.delegate = delegate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
}
