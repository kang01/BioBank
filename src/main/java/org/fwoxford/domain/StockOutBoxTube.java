package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutBoxTube.
 */
@Entity
@Table(name = "stock_out_box_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutBoxTube extends AbstractAuditingEntity implements Serializable {

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
    private StockOutFrozenBox stockOutFrozenBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutTaskFrozenTube stockOutTaskFrozenTube;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutBoxTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutBoxTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public StockOutFrozenBox getStockOutFrozenBox() {
        return stockOutFrozenBox;
    }

    public StockOutBoxTube stockOutFrozenBox(StockOutFrozenBox stockOutFrozenBox) {
        this.stockOutFrozenBox = stockOutFrozenBox;
        return this;
    }

    public void setStockOutFrozenBox(StockOutFrozenBox stockOutFrozenBox) {
        this.stockOutFrozenBox = stockOutFrozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public StockOutBoxTube frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    public StockOutTaskFrozenTube getStockOutTaskFrozenTube() {
        return stockOutTaskFrozenTube;
    }

    public StockOutBoxTube stockOutTaskFrozenTube(StockOutTaskFrozenTube stockOutTaskFrozenTube) {
        this.stockOutTaskFrozenTube = stockOutTaskFrozenTube;
        return this;
    }

    public void setStockOutTaskFrozenTube(StockOutTaskFrozenTube stockOutTaskFrozenTube) {
        this.stockOutTaskFrozenTube = stockOutTaskFrozenTube;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutBoxTube stockOutBoxTube = (StockOutBoxTube) o;
        if (stockOutBoxTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutBoxTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutBoxTube{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
