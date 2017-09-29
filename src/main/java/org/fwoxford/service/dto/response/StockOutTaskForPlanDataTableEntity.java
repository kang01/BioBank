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
import java.lang.ref.SoftReference;
import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/23.
 */
@Entity
@Table(name = "view_stock_out_task_for_plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutTaskForPlanDataTableEntity {

    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "stock_out_task_code")
    private String stockOutTaskCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "count_of_frozen_box")
    private Long countOfFrozenBox;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "count_of_sample")
    private Long countOfSample;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "created_date")
    private LocalDate createDate;
    @NotNull
    @JsonView(DataTablesOutput.View.class)

    @Column(name = "stock_out_date")
    private LocalDate stockOutDate;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "operators")
    private String operators;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "status")
    private String status;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "memo")
    private String memo;

    @Column(name = "stock_out_plan_id")
    private Long stockOutPlanId;

    @Column(name = "stock_out_head_id_1")
    private Long stockOutHeadId1;

    @Column(name = "stock_out_head_id_2")
    private Long stockOutHeadId2;

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

    public Long getCountOfFrozenBox() {
        return countOfFrozenBox;
    }

    public void setCountOfFrozenBox(Long countOfFrozenBox) {
        this.countOfFrozenBox = countOfFrozenBox;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getStockOutDate() {
        return stockOutDate;
    }

    public void setStockOutDate(LocalDate stockOutDate) {
        this.stockOutDate = stockOutDate;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
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

    public Long getStockOutPlanId() {
        return stockOutPlanId;
    }

    public void setStockOutPlanId(Long stockOutPlanId) {
        this.stockOutPlanId = stockOutPlanId;
    }

    public Long getStockOutHeadId1() {
        return stockOutHeadId1;
    }

    public void setStockOutHeadId1(Long stockOutHeadId1) {
        this.stockOutHeadId1 = stockOutHeadId1;
    }

    public Long getStockOutHeadId2() {
        return stockOutHeadId2;
    }

    public void setStockOutHeadId2(Long stockOutHeadId2) {
        this.stockOutHeadId2 = stockOutHeadId2;
    }
}
