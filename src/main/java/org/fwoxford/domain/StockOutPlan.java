package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutPlan.
 */
@Entity
@Table(name = "stock_out_plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutPlan extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;


    @NotNull
    @Size(max = 100)
    @Column(name = "stock_out_plan_code", length = 100, nullable = false)
    private String stockOutPlanCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    @Column(name = "apply_number", length = 100, nullable = false)
    private String applyNumber;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutApply stockOutApply;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockOutPlanCode() {
        return stockOutPlanCode;
    }

    public StockOutPlan stockOutPlanCode(String stockOutPlanCode) {
        this.stockOutPlanCode = stockOutPlanCode;
        return this;
    }

    public void setStockOutPlanCode(String stockOutPlanCode) {
        this.stockOutPlanCode = stockOutPlanCode;
    }

    public String getStatus() {
        return status;
    }

    public StockOutPlan status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutPlan memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getApplyNumber() {
        return applyNumber;
    }

    public StockOutPlan applyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
        return this;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }

    public StockOutApply getStockOutApply() {
        return stockOutApply;
    }

    public StockOutPlan stockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
        return this;
    }

    public void setStockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutPlan stockOutPlan = (StockOutPlan) o;
        if (stockOutPlan.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutPlan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutPlan{" +
            "id=" + id +
            ", stockOutPlanCode='" + stockOutPlanCode + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", applyNumber='" + applyNumber + "'" +
            '}';
    }
}
