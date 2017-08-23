package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutTaskFrozenTube.
 */
@Entity
@Table(name = "stock_out_task_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutTaskFrozenTube extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_task_tube")
    @SequenceGenerator(name = "seq_stock_out_task_tube",sequenceName = "seq_stock_out_task_tube",allocationSize = 1,initialValue = 1)
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
    private StockOutTask stockOutTask;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutPlanFrozenTube stockOutPlanFrozenTube;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutTaskFrozenTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutTaskFrozenTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public StockOutTask getStockOutTask() {
        return stockOutTask;
    }

    public StockOutTaskFrozenTube stockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
        return this;
    }

    public void setStockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
    }

    public StockOutPlanFrozenTube getStockOutPlanFrozenTube() {
        return stockOutPlanFrozenTube;
    }

    public StockOutTaskFrozenTube stockOutPlanFrozenTube(StockOutPlanFrozenTube stockOutPlanFrozenTube) {
        this.stockOutPlanFrozenTube = stockOutPlanFrozenTube;
        return this;
    }

    public void setStockOutPlanFrozenTube(StockOutPlanFrozenTube stockOutPlanFrozenTube) {
        this.stockOutPlanFrozenTube = stockOutPlanFrozenTube;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutTaskFrozenTube stockOutTaskFrozenTube = (StockOutTaskFrozenTube) o;
        if (stockOutTaskFrozenTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutTaskFrozenTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutTaskFrozenTube{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
