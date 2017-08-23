package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockInTranshipBox.
 */
@Entity
@Table(name = "stock_in_tranship_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockInTranshipBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_in_tranship_box")
    @SequenceGenerator(name = "seq_stock_in_tranship_box",sequenceName = "seq_stock_in_tranship_box",allocationSize = 1,initialValue = 1)
    private Long id;


    @NotNull
    @Size(max = 100)
    @Column(name = "stock_in_code", length = 100, nullable = false)
    private String stockInCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "tranship_code", length = 100, nullable = false)
    private String transhipCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private TranshipBox transhipBox;

    @ManyToOne(optional = false)
    @NotNull
    private StockIn stockIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public StockInTranshipBox stockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
        return this;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public StockInTranshipBox transhipCode(String transhipCode) {
        this.transhipCode = transhipCode;
        return this;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public StockInTranshipBox frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getStatus() {
        return status;
    }

    public StockInTranshipBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockInTranshipBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public TranshipBox getTranshipBox() {
        return transhipBox;
    }

    public StockInTranshipBox transhipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
        return this;
    }

    public void setTranshipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
    }

    public StockIn getStockIn() {
        return stockIn;
    }

    public StockInTranshipBox stockIn(StockIn stockIn) {
        this.stockIn = stockIn;
        return this;
    }

    public void setStockIn(StockIn stockIn) {
        this.stockIn = stockIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockInTranshipBox stockInTranshipBox = (StockInTranshipBox) o;
        if (stockInTranshipBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockInTranshipBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInTranshipBox{" +
            "id=" + id +
            ", stockInCode='" + stockInCode + "'" +
            ", transhipCode='" + transhipCode + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
