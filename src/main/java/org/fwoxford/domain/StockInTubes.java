package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockInTubes.
 */
@Entity
@Table(name = "stock_in_tubes")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockInTubes extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "sample_code", length = 100)
    private String sampleCode;

    @Size(max = 100)
    @Column(name = "frozen_tube_code", length = 100)
    private String frozenTubeCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "rows_in_tube", length = 20, nullable = false)
    private String rowsInTube;

    @NotNull
    @Size(max = 20)
    @Column(name = "columns_in_tube", length = 20, nullable = false)
    private String columnsInTube;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 100)
    @Column(name = "sample_temp_code", length = 100)
    private String sampleTempCode;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBoxPosition frozenBoxPosition;

    @ManyToOne
    private TranshipBox transhipBox;

    @ManyToOne(optional = false)
    @NotNull
    private StockInBox stockInBox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public StockInTubes sampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public StockInTubes frozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
        return this;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getRowsInTube() {
        return rowsInTube;
    }

    public StockInTubes rowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
        return this;
    }

    public void setRowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
    }

    public String getColumnsInTube() {
        return columnsInTube;
    }

    public StockInTubes columnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
        return this;
    }

    public void setColumnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
    }

    public String getMemo() {
        return memo;
    }

    public StockInTubes memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public StockInTubes status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public StockInTubes sampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public StockInTubes frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    public FrozenBoxPosition getFrozenBoxPosition() {
        return frozenBoxPosition;
    }

    public StockInTubes frozenBoxPosition(FrozenBoxPosition frozenBoxPosition) {
        this.frozenBoxPosition = frozenBoxPosition;
        return this;
    }

    public void setFrozenBoxPosition(FrozenBoxPosition frozenBoxPosition) {
        this.frozenBoxPosition = frozenBoxPosition;
    }

    public TranshipBox getTranshipBox() {
        return transhipBox;
    }

    public StockInTubes transhipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
        return this;
    }

    public void setTranshipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
    }

    public StockInBox getStockInBox() {
        return stockInBox;
    }

    public StockInTubes stockInBox(StockInBox stockInBox) {
        this.stockInBox = stockInBox;
        return this;
    }

    public void setStockInBox(StockInBox stockInBox) {
        this.stockInBox = stockInBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockInTubes stockInTubes = (StockInTubes) o;
        if (stockInTubes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockInTubes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInTubes{" +
            "id=" + id +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", rowsInTube='" + rowsInTube + "'" +
            ", columnsInTube='" + columnsInTube + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            '}';
    }
}
