package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "stock_out_head_id_1")
    private Long stockOutHeadId1;

    @Column(name = "stock_out_head_id_2")
    private Long stockOutHeadId2;

    @NotNull
    @Column(name = "stock_out_date", nullable = false)
    private LocalDate stockOutDate;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

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
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
