package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutFrozenBox.
 */
@Entity
@Table(name = "stock_out_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutFrozenBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
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
    private FrozenBox frozenBox;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutBoxPosition stockOutBoxPosition;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutTask stockOutTask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutFrozenBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutFrozenBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public StockOutFrozenBox frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public StockOutBoxPosition getStockOutBoxPosition() {
        return stockOutBoxPosition;
    }

    public StockOutFrozenBox stockOutBoxPosition(StockOutBoxPosition stockOutBoxPosition) {
        this.stockOutBoxPosition = stockOutBoxPosition;
        return this;
    }

    public void setStockOutBoxPosition(StockOutBoxPosition stockOutBoxPosition) {
        this.stockOutBoxPosition = stockOutBoxPosition;
    }

    public StockOutTask getStockOutTask() {
        return stockOutTask;
    }

    public StockOutFrozenBox stockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
        return this;
    }

    public void setStockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutFrozenBox stockOutFrozenBox = (StockOutFrozenBox) o;
        if (stockOutFrozenBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutFrozenBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutFrozenBox{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
