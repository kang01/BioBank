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

    @NotNull
    @Size(max = 100)
    @Column(name = "stock_in_code", length = 100, nullable = false)
    private String stockInCode;

    @Size(max = 100)
    @Column(name = "tranship_code", length = 100)
    private String transhipCode;

    @Size(max = 100)
    @Column(name = "tranship_batch", length = 100)
    private String transhipBatch;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    @Size(max = 100)
    @Column(name = "sample_code", length = 100)
    private String sampleCode;


    @Size(max = 100)
    @Column(name = "sample_temp_code", length = 100)
    private String sampleTempCode;

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

    @ManyToOne(optional = false)
    @NotNull
    private StockIn stockIn;

    @ManyToOne
    private Tranship tranship;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBoxPosition frozenBoxPosition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public StockInTubes stockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
        return this;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public StockInTubes transhipCode(String transhipCode) {
        this.transhipCode = transhipCode;
        return this;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public String getTranshipBatch() {
        return transhipBatch;
    }

    public StockInTubes transhipBatch(String transhipBatch) {
        this.transhipBatch = transhipBatch;
        return this;
    }

    public void setTranshipBatch(String transhipBatch) {
        this.transhipBatch = transhipBatch;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public StockInTubes frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
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

    public StockIn getStockIn() {
        return stockIn;
    }

    public StockInTubes stockIn(StockIn stockIn) {
        this.stockIn = stockIn;
        return this;
    }

    public void setStockIn(StockIn stockIn) {
        this.stockIn = stockIn;
    }

    public Tranship getTranship() {
        return tranship;
    }

    public StockInTubes tranship(Tranship tranship) {
        this.tranship = tranship;
        return this;
    }

    public void setTranship(Tranship tranship) {
        this.tranship = tranship;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public StockInTubes frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
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
            ", stockInCode='" + stockInCode + "'" +
            ", transhipCode='" + transhipCode + "'" +
            ", transhipBatch='" + transhipBatch + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            ", rowsInTube='" + rowsInTube + "'" +
            ", columnsInTube='" + columnsInTube + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
