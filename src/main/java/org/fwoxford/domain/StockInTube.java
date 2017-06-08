package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockInTube.
 */
@Entity
@Table(name = "stock_in_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockInTube extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "rows_in_tube", length = 20, nullable = false)
    private String rowsInTube;

    @NotNull
    @Size(max = 20)
    @Column(name = "columns_in_tube", length = 20, nullable = false)
    private String columnsInTube;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    @ManyToOne(optional = false)
    @NotNull
    private StockInBox stockInBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRowsInTube() {
        return rowsInTube;
    }

    public StockInTube rowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
        return this;
    }

    public void setRowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
    }

    public String getColumnsInTube() {
        return columnsInTube;
    }

    public StockInTube columnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
        return this;
    }

    public void setColumnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
    }

    public String getStatus() {
        return status;
    }

    public StockInTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockInTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public StockInTube frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public StockInBox getStockInBox() {
        return stockInBox;
    }

    public StockInTube stockInBox(StockInBox stockInBox) {
        this.stockInBox = stockInBox;
        return this;
    }

    public void setStockInBox(StockInBox stockInBox) {
        this.stockInBox = stockInBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public StockInTube frozenTube(FrozenTube frozenTube) {
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
        StockInTube stockInTube = (StockInTube) o;
        if (stockInTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockInTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInTube{" +
            "id=" + id +
            ", rowsInTube='" + rowsInTube + "'" +
            ", columnsInTube='" + columnsInTube + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            '}';
    }
}
