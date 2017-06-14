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
import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/26.
 */
@Entity
@Table(name = "view_stock_out_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutTaskForDataTableEntity {

    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    /**
     * 任务编码
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "stock_out_task_code")
    private String stockOutTaskCode;
    /**
     * 计划编码
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "stock_out_plan_code")
    private String stockOutPlanCode;
    /**
     * 出库时间
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "stock_out_date")
    private LocalDate stockOutDate;
    /**
     * 任务样本量
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "count_of_stock_out_sample")
    private Long countOfStockOutSample;
    /**
     * 已交接样本量
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "count_of_hand_over_sample")
    private Long countOfHandOverSample;
    /**
     * 交接次数
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "hand_over_times")
    private Long handOverTimes;
    /**
     * 出库目的
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "purpose_of_sample")
    private String purposeOfSample;
    /**
     * 出库任务状态：1601：待出库，1602：进行中，1603：已出库，1604：异常出库，1605：已作废
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

    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }

    public String getStockOutPlanCode() {
        return stockOutPlanCode;
    }

    public void setStockOutPlanCode(String stockOutPlanCode) {
        this.stockOutPlanCode = stockOutPlanCode;
    }

    public LocalDate getStockOutDate() {
        return stockOutDate;
    }

    public void setStockOutDate(LocalDate stockOutDate) {
        this.stockOutDate = stockOutDate;
    }

    public Long getCountOfStockOutSample() {
        return countOfStockOutSample;
    }

    public void setCountOfStockOutSample(Long countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
    }

    public Long getCountOfHandOverSample() {
        return countOfHandOverSample;
    }

    public void setCountOfHandOverSample(Long countOfHandOverSample) {
        this.countOfHandOverSample = countOfHandOverSample;
    }

    public Long getHandOverTimes() {
        return handOverTimes;
    }

    public void setHandOverTimes(Long handOverTimes) {
        this.handOverTimes = handOverTimes;
    }

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
