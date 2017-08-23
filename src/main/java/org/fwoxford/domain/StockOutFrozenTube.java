package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutFrozenTube.
 */
@Entity
@Table(name = "stock_out_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutFrozenTube extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_tube")
    @SequenceGenerator(name = "seq_stock_out_tube",sequenceName = "seq_stock_out_tube",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public StockOutFrozenTube tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public StockOutFrozenTube tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getStatus() {
        return status;
    }

    public StockOutFrozenTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutFrozenTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public StockOutFrozenBox getStockOutFrozenBox() {
        return stockOutFrozenBox;
    }

    public StockOutFrozenTube stockOutFrozenBox(StockOutFrozenBox stockOutFrozenBox) {
        this.stockOutFrozenBox = stockOutFrozenBox;
        return this;
    }

    public void setStockOutFrozenBox(StockOutFrozenBox stockOutFrozenBox) {
        this.stockOutFrozenBox = stockOutFrozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public StockOutFrozenTube frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutFrozenTube stockOutFrozenTube = (StockOutFrozenTube) o;
        if (stockOutFrozenTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutFrozenTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutFrozenTube{" +
            "id=" + id +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
