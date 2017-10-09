package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutHandoverDetails.
 */
@Entity
@Table(name = "stock_out_handover_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutHandoverDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_handover_details")
    @SequenceGenerator(name = "seq_stock_out_handover_details",sequenceName = "seq_stock_out_handover_details",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

//    @ManyToOne(optional = false)
//    @NotNull
//    private StockOutHandover stockOutHandover;

//    @ManyToOne(optional = false)
//    @NotNull
//    private StockOutBoxTube stockOutBoxTube;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutReqFrozenTube stockOutReqFrozenTube;


    @ManyToOne(optional = false)
    @NotNull
    private StockOutHandoverBox stockOutHandoverBox;

    public StockOutHandoverBox getStockOutHandoverBox() {
        return stockOutHandoverBox;
    }

    public void setStockOutHandoverBox(StockOutHandoverBox stockOutHandoverBox) {
        this.stockOutHandoverBox = stockOutHandoverBox;
    }
    public StockOutHandoverDetails stockOutHandoverBox(StockOutHandoverBox stockOutHandoverBox) {
        this.stockOutHandoverBox = stockOutHandoverBox;
        return this;
    }

    public StockOutReqFrozenTube getStockOutReqFrozenTube() {
        return stockOutReqFrozenTube;
    }

    public void setStockOutReqFrozenTube(StockOutReqFrozenTube stockOutReqFrozenTube) {
        this.stockOutReqFrozenTube = stockOutReqFrozenTube;
    }
    public StockOutHandoverDetails stockOutReqFrozenTube(StockOutReqFrozenTube stockOutReqFrozenTube) {
        this.stockOutReqFrozenTube = stockOutReqFrozenTube;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutHandoverDetails status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutHandoverDetails memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

//    public StockOutHandover getStockOutHandover() {
//        return stockOutHandover;
//    }
//
//    public StockOutHandoverDetails stockOutHandover(StockOutHandover stockOutHandover) {
//        this.stockOutHandover = stockOutHandover;
//        return this;
//    }
//
//    public void setStockOutHandover(StockOutHandover stockOutHandover) {
//        this.stockOutHandover = stockOutHandover;
//    }
//
//    public StockOutBoxTube getStockOutBoxTube() {
//        return stockOutBoxTube;
//    }
//
//    public StockOutHandoverDetails stockOutBoxTube(StockOutBoxTube stockOutBoxTube) {
//        this.stockOutBoxTube = stockOutBoxTube;
//        return this;
//    }
//
//    public void setStockOutBoxTube(StockOutBoxTube stockOutBoxTube) {
//        this.stockOutBoxTube = stockOutBoxTube;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutHandoverDetails stockOutHandoverDetails = (StockOutHandoverDetails) o;
        if (stockOutHandoverDetails.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutHandoverDetails.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutHandoverDetails{" +
            "id=" + id +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", stockOutReqFrozenTube=" + stockOutReqFrozenTube +
            ", stockOutHandoverBox=" + stockOutHandoverBox +
            '}';
    }
}
