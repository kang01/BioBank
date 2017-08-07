package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TranshipStockIn.
 */
@Entity
@Table(name = "tranship_stock_in")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TranshipStockIn extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "tranship_code", length = 100, nullable = false)
    private String transhipCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "stock_in_code", length = 100, nullable = false)
    private String stockInCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private Tranship tranship;

    @ManyToOne(optional = false)
    @NotNull
    private StockIn stockIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public TranshipStockIn transhipCode(String transhipCode) {
        this.transhipCode = transhipCode;
        return this;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public TranshipStockIn stockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
        return this;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public String getStatus() {
        return status;
    }

    public TranshipStockIn status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public TranshipStockIn memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Tranship getTranship() {
        return tranship;
    }

    public TranshipStockIn tranship(Tranship tranship) {
        this.tranship = tranship;
        return this;
    }

    public void setTranship(Tranship tranship) {
        this.tranship = tranship;
    }

    public StockIn getStockIn() {
        return stockIn;
    }

    public TranshipStockIn stockIn(StockIn stockIn) {
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
        TranshipStockIn transhipStockIn = (TranshipStockIn) o;
        if (transhipStockIn.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transhipStockIn.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipStockIn{" +
            "id=" + id +
            ", transhipCode='" + transhipCode + "'" +
            ", stockInCode='" + stockInCode + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
