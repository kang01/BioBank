package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/23.
 */

//@Entity
//@Table(name = "view_stock_out_plan")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutPlansForDataTableEntity {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    /**
     * 申请单号
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String applyNumber;
    /**
     * 计划编号
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String stockOutPlanCode;
    /**
     * 计划时间
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private LocalDate planDate;
    /**
     * 出库目的
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String purposeOfSample;
    /**
     * 计划样本量
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long countOfStockOutPlanSample;
    /**
     * 出库任务量
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String countOfStockOutTask;
    /**
     * 交接进度
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String handOverSchedule;
    /**
     * 状态:1401:进行中，1402：已完成，1403：已作废
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String status;

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

    public String getStockOutPlanCode() {
        return stockOutPlanCode;
    }

    public void setStockOutPlanCode(String stockOutPlanCode) {
        this.stockOutPlanCode = stockOutPlanCode;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
    }

    public Long getCountOfStockOutPlanSample() {
        return countOfStockOutPlanSample;
    }

    public void setCountOfStockOutPlanSample(Long countOfStockOutPlanSample) {
        this.countOfStockOutPlanSample = countOfStockOutPlanSample;
    }

    public String getCountOfStockOutTask() {
        return countOfStockOutTask;
    }

    public void setCountOfStockOutTask(String countOfStockOutTask) {
        this.countOfStockOutTask = countOfStockOutTask;
    }

    public String getHandOverSchedule() {
        return handOverSchedule;
    }

    public void setHandOverSchedule(String handOverSchedule) {
        this.handOverSchedule = handOverSchedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
