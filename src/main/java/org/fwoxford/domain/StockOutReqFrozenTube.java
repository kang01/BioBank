package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutReqFrozenTube.
 */
@Entity
@Table(name = "stock_out_req_frozen_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutReqFrozenTube extends AbstractAuditingEntity implements Serializable {

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

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;

    @Column(name = "importing_sample_id")
    private Long importingSampleId;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutRequirement stockOutRequirement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutReqFrozenTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutReqFrozenTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public StockOutReqFrozenTube tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public StockOutReqFrozenTube tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public Long getImportingSampleId() {
        return importingSampleId;
    }

    public StockOutReqFrozenTube importingSampleId(Long importingSampleId) {
        this.importingSampleId = importingSampleId;
        return this;
    }

    public void setImportingSampleId(Long importingSampleId) {
        this.importingSampleId = importingSampleId;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public StockOutReqFrozenTube frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public StockOutReqFrozenTube frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    public StockOutRequirement getStockOutRequirement() {
        return stockOutRequirement;
    }

    public StockOutReqFrozenTube stockOutRequirement(StockOutRequirement stockOutRequirement) {
        this.stockOutRequirement = stockOutRequirement;
        return this;
    }

    public void setStockOutRequirement(StockOutRequirement stockOutRequirement) {
        this.stockOutRequirement = stockOutRequirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutReqFrozenTube stockOutReqFrozenTube = (StockOutReqFrozenTube) o;
        if (stockOutReqFrozenTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutReqFrozenTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutReqFrozenTube{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", importingSampleId='" + importingSampleId + "'" +
            '}';
    }
}
