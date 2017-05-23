package org.fwoxford.service.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutApplyForPlanDetail {
    private Long id;
    private String applyNumber;
    /**
     * 委托方名称
     */
    private String delegateName;
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
    private String purposeOfSample;
    /**
     * 出库样本量
     */
    private Long countOfSample;
    /**
     * 实际样本量
     */
    private Long countOfStockOutSample;

    public List<StockOutRequirementForPlan> requirements;
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

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
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

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public Long getCountOfStockOutSample() {
        return countOfStockOutSample;
    }

    public void setCountOfStockOutSample(Long countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
    }

    public List<StockOutRequirementForPlan> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<StockOutRequirementForPlan> requirements) {
        this.requirements = requirements;
    }
}
