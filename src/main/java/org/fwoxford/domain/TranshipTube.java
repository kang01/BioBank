package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TranshipTube.
 */
@Entity
@Table(name = "tranship_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TranshipTube extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "columns_in_tube", length = 20, nullable = false)
    private String columnsInTube;

    @NotNull
    @Size(max = 20)
    @Column(name = "rows_in_tube", length = 20, nullable = false)
    private String rowsInTube;

    @ManyToOne(optional = false)
    @NotNull
    private TranshipBox transhipBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public TranshipTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public TranshipTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getColumnsInTube() {
        return columnsInTube;
    }

    public TranshipTube columnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
        return this;
    }

    public void setColumnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
    }

    public String getRowsInTube() {
        return rowsInTube;
    }

    public TranshipTube rowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
        return this;
    }

    public void setRowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
    }

    public TranshipBox getTranshipBox() {
        return transhipBox;
    }

    public TranshipTube transhipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
        return this;
    }

    public void setTranshipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public TranshipTube frozenTube(FrozenTube frozenTube) {
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
        TranshipTube transhipTube = (TranshipTube) o;
        if (transhipTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transhipTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipTube{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", columnsInTube='" + columnsInTube + "'" +
            ", rowsInTube='" + rowsInTube + "'" +
            '}';
    }
}
