package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/5/15.
 */
public class StockOutApplyForDataTableEntity {
    /**
     * 申请ID
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    /**
     * 样本用途
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String purposeOfSample;
    /**
     * 申请时间
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String applyTime;
    /**
     * 委托人
     */
    @Size(max = 255)
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String applyPersonName;
    /**
     * 申请单号
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String applyCode;
    /**
     * 委托方名称
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String delegateName;
    /**
     * 样本类型字符串，如：血浆，血清，
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String sampleTypes;
    /**
     * 状态（1101：进行中，1102：待批准，1103：已批准，1104：已作废）
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String status;
    /**
     * 样本需求量
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long countOfSample;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
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

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getSampleTypes() {
        return sampleTypes;
    }

    public void setSampleTypes(String sampleTypes) {
        this.sampleTypes = sampleTypes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }
}
