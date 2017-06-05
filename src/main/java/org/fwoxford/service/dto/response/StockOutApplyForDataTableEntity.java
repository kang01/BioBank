package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.models.auth.In;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by gengluying on 2017/5/15.
 */
@Entity
@Table(name = "view_stock_out_apply")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutApplyForDataTableEntity {
    /**
     * 申请ID
     */
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    /**
     * 样本用途
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "purpose_of_sample")
    private String purposeOfSample;
    /**
     * 申请时间
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "apply_time")
    private String applyTime;
    /**
     * 委托人
     */
    @Size(max = 255)
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "apply_person_name")
    private String applyPersonName;
    /**
     * 申请单号
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "apply_code")
    private String applyCode;
    /**
     * 委托方名称
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "delegate_name")
    private String delegateName;
    /**
     * 样本类型字符串，如：血浆，血清，
     */
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_types")
    private String sampleTypes;
    /**
     * 状态（1101：进行中，1102：待批准，1103：已批准，1104：已作废）
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "status")
    private String status;
    /**
     * 样本需求量
     */
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "count_of_sample")
    private Long countOfSample;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "level_no")
    private Integer levelNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutApplyForDataTableEntity response = (StockOutApplyForDataTableEntity) o;

        if ( ! Objects.equals(getId(), response.getId())) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

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

    public Integer getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }
}
