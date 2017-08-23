package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TranshipBoxPosition.
 */
@Entity
@Table(name = "tranship_box_pos")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TranshipBoxPosition extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tranship_box_pos")
    @SequenceGenerator(name = "seq_tranship_box_pos",sequenceName = "seq_tranship_box_pos",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @Size(max = 100)
    @Column(name = "equipment_code", length = 100)
    private String equipmentCode;

    @Size(max = 100)
    @Column(name = "area_code", length = 100)
    private String areaCode;

    @Column(name = "support_rack_code")
    private String supportRackCode;

    @Size(max = 100)
    @Column(name = "rows_in_shelf", length = 100)
    private String rowsInShelf;

    @Size(max = 100)
    @Column(name = "columns_in_shelf", length = 100)
    private String columnsInShelf;

    @ManyToOne
    private Equipment equipment;

    @ManyToOne
    private Area area;

    @ManyToOne
    private SupportRack supportRack;

    @ManyToOne(optional = false)
    @NotNull
    private TranshipBox transhipBox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public TranshipBoxPosition status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public TranshipBoxPosition memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public TranshipBoxPosition equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public TranshipBoxPosition areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public TranshipBoxPosition supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public TranshipBoxPosition rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public TranshipBoxPosition columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public TranshipBoxPosition equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Area getArea() {
        return area;
    }

    public TranshipBoxPosition area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }

    public TranshipBoxPosition supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }

    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public TranshipBox getTranshipBox() {
        return transhipBox;
    }

    public TranshipBoxPosition transhipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
        return this;
    }

    public void setTranshipBox(TranshipBox transhipBox) {
        this.transhipBox = transhipBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TranshipBoxPosition transhipBoxPosition = (TranshipBoxPosition) o;
        if (transhipBoxPosition.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transhipBoxPosition.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipBoxPosition{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", rowsInShelf='" + rowsInShelf + "'" +
            ", columnsInShelf='" + columnsInShelf + "'" +
            '}';
    }
}
