package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TranshipBox.
 */
@Entity
@Table(name = "tranship_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TranshipBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false)
    private String equipmentCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "area_code", length = 100, nullable = false)
    private String areaCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100, nullable = false)
    private String supportRackCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "rows_in_shelf", length = 20, nullable = false)
    private String rowsInShelf;

    @NotNull
    @Size(max = 20)
    @Column(name = "columns_in_shelf", length = 20, nullable = false)
    private String columnsInShelf;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @ManyToOne(optional = false)
    @NotNull
    private Tranship tranship;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public TranshipBox frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public TranshipBox equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public TranshipBox areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public TranshipBox supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public TranshipBox rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public TranshipBox columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getMemo() {
        return memo;
    }

    public TranshipBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public TranshipBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tranship getTranship() {
        return tranship;
    }

    public TranshipBox tranship(Tranship tranship) {
        this.tranship = tranship;
        return this;
    }

    public void setTranship(Tranship tranship) {
        this.tranship = tranship;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public TranshipBox frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TranshipBox transhipBox = (TranshipBox) o;
        if (transhipBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transhipBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipBox{" +
            "id=" + id +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", rowsInShelf='" + rowsInShelf + "'" +
            ", columnsInShelf='" + columnsInShelf + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}