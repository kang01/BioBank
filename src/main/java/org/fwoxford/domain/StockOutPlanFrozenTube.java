package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutPlanFrozenTube.
 */
@Entity
@Table(name = "stock_out_plan_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutPlanFrozenTube extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_plan_tube")
    @SequenceGenerator(name = "seq_stock_out_plan_tube",sequenceName = "seq_stock_out_plan_tube",allocationSize = 1,initialValue = 1)
    private Long id;

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

    @ManyToOne(optional = false)
    @NotNull
    private StockOutReqFrozenTube stockOutReqFrozenTube;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutPlanFrozenTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutPlanFrozenTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public StockOutPlan getStockOutPlan() {
        return stockOutPlan;
    }

    public StockOutPlanFrozenTube stockOutPlan(StockOutPlan stockOutPlan) {
        this.stockOutPlan = stockOutPlan;
        return this;
    }

    public void setStockOutPlan(StockOutPlan stockOutPlan) {
        this.stockOutPlan = stockOutPlan;
    }

    public StockOutReqFrozenTube getStockOutReqFrozenTube() {
        return stockOutReqFrozenTube;
    }

    public StockOutPlanFrozenTube stockOutReqFrozenTube(StockOutReqFrozenTube stockOutReqFrozenTube) {
        this.stockOutReqFrozenTube = stockOutReqFrozenTube;
        return this;
    }

    public void setStockOutReqFrozenTube(StockOutReqFrozenTube stockOutReqFrozenTube) {
        this.stockOutReqFrozenTube = stockOutReqFrozenTube;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutPlanFrozenTube stockOutPlanFrozenTube = (StockOutPlanFrozenTube) o;
        if (stockOutPlanFrozenTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutPlanFrozenTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutPlanFrozenTube{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
