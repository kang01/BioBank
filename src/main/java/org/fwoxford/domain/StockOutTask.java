package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A StockOutTask.
 */
@Entity
@Table(name = "stock_out_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutTask extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_task")
    @SequenceGenerator(name = "seq_stock_out_task",sequenceName = "seq_stock_out_task",allocationSize = 1,initialValue = 1)
    private Long id;

    @Column(name = "stock_out_head_id_1")
    private Long stockOutHeadId1;

    @Column(name = "stock_out_head_id_2")
    private Long stockOutHeadId2;

    @Column(name = "task_start_time")
    private ZonedDateTime taskStartTime;

    @Column(name = "task_end_time")
    private ZonedDateTime taskEndTime;

    @Column(name = "stock_out_date", nullable = false)
    private LocalDate stockOutDate;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    @Column(name = "stock_out_task_code", length = 100, nullable = false)
    private String stockOutTaskCode;

    @NotNull
    @Column(name = "used_time", nullable = false)
    private Integer usedTime;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutPlan stockOutPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockOutHeadId1() {
        return stockOutHeadId1;
    }

    public StockOutTask stockOutHeadId1(Long stockOutHeadId1) {
        this.stockOutHeadId1 = stockOutHeadId1;
        return this;
    }

    public void setStockOutHeadId1(Long stockOutHeadId1) {
        this.stockOutHeadId1 = stockOutHeadId1;
    }

    public Long getStockOutHeadId2() {
        return stockOutHeadId2;
    }

    public StockOutTask stockOutHeadId2(Long stockOutHeadId2) {
        this.stockOutHeadId2 = stockOutHeadId2;
        return this;
    }

    public void setStockOutHeadId2(Long stockOutHeadId2) {
        this.stockOutHeadId2 = stockOutHeadId2;
    }

    public ZonedDateTime getTaskStartTime() {
        return taskStartTime;
    }

    public StockOutTask taskStartTime(ZonedDateTime taskStartTime) {
        this.taskStartTime = taskStartTime;
        return this;
    }

    public void setTaskStartTime(ZonedDateTime taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public ZonedDateTime getTaskEndTime() {
        return taskEndTime;
    }

    public StockOutTask taskEndTime(ZonedDateTime taskEndTime) {
        this.taskEndTime = taskEndTime;
        return this;
    }
    public void setTaskEndTime(ZonedDateTime taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public LocalDate getStockOutDate() {
        return stockOutDate;
    }

    public StockOutTask stockOutDate(LocalDate stockOutDate) {
        this.stockOutDate = stockOutDate;
        return this;
    }

    public void setStockOutDate(LocalDate stockOutDate) {
        this.stockOutDate = stockOutDate;
    }

    public String getStatus() {
        return status;
    }

    public StockOutTask status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutTask memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public StockOutTask stockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
        return this;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }

    public Integer getUsedTime() {
        return usedTime;
    }

    public StockOutTask usedTime(Integer usedTime) {
        this.usedTime = usedTime;
        return this;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public StockOutPlan getStockOutPlan() {
        return stockOutPlan;
    }

    public StockOutTask stockOutPlan(StockOutPlan stockOutPlan) {
        this.stockOutPlan = stockOutPlan;
        return this;
    }

    public void setStockOutPlan(StockOutPlan stockOutPlan) {
        this.stockOutPlan = stockOutPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutTask stockOutTask = (StockOutTask) o;
        if (stockOutTask.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutTask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutTask{" +
            "id=" + id +
            ", stockOutHeadId1='" + stockOutHeadId1 + "'" +
            ", stockOutHeadId2='" + stockOutHeadId2 + "'" +
            ", stockOutDate='" + stockOutDate + "'" +
            ", taskStartTime='" + taskStartTime + "'" +
            ", taskEndTime='" + taskEndTime + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", stockOutTaskCode='" + stockOutTaskCode + "'" +
            ", usedTime='" + usedTime + "'" +
            '}';
    }

    /**
     * 出库样本量
     */
    @Column(name = "count_of_stock_out_sample")
    private Integer countOfStockOutSample;
    /**
     * 已交接样本量
     */
    @Column(name = "count_of_hand_over_sample")
    private Integer countOfHandOverSample;

    public Integer getCountOfStockOutSample() {
        return countOfStockOutSample;
    }

    public StockOutTask countOfStockOutSample(Integer countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
        return this;
    }

    public void setCountOfStockOutSample(Integer countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
    }

    public Integer getCountOfHandOverSample() {
        return countOfHandOverSample;
    }

    public StockOutTask countOfHandOverSample(Integer countOfHandOverSample) {
        this.countOfHandOverSample = countOfHandOverSample;
        return this;
    }

    public void setCountOfHandOverSample(Integer countOfHandOverSample) {
        this.countOfHandOverSample = countOfHandOverSample;
    }

    /**
     * 作废原因
     */
    @Column(name = "invalid_reason")
    private String invalidReason;

    public String getInvalidReason() {
        return invalidReason;
    }
    public StockOutTask invalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
        return this;
    }
    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }
}
