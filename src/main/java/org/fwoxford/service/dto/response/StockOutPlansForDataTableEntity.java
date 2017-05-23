package org.fwoxford.service.dto.response;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutPlansForDataTableEntity {
    private Long id;
    /**
     * 申请单号
     */
    private String applyNumber;
    /**
     * 计划编号
     */
    private String stockOutPlanCode;
    /**
     * 计划时间
     */
    private LocalDate planDate;
    /**
     * 出库目的
     */
    private String purposeOfSample;
    /**
     * 计划样本量
     */
    private Long countOfStockOutPlanSample;
    /**
     * 出库任务量
     */
    private String countOfStockOutTask;
    /**
     * 交接进度
     */
    private String handOverSchedule;
    /**
     * 状态:1401:进行中，1402：已完成，1403：已作废
     */
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
