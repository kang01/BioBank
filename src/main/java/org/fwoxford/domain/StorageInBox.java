package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StorageInBox.
 */
@Entity
@Table(name = "storage_in_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StorageInBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "area_code", length = 100, nullable = false)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100, nullable = false)
    private String supportRackCode;
    /**
     * 所在冻存架行数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "rows_in_shelf", length = 20, nullable = false)
    private String rowsInShelf;
    /**
     * 所在冻存架列数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "columns_in_shelf", length = 20, nullable = false)
    private String columnsInShelf;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;
    /**
     * 入库
     */
    @ManyToOne(optional = false)
    @NotNull
    private StorageIn storageIn;
    /**
     * 设备
     */
    @ManyToOne(optional = false)
    @NotNull
    private Equipment equipment;
    /**
     * 冻存架
     */
    @ManyToOne(optional = false)
    @NotNull
    private SupportRack supportRack;
    /**
     * 区域
     */
    @ManyToOne(optional = false)
    @NotNull
    private Area area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public StorageInBox equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public StorageInBox areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public StorageInBox supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public StorageInBox rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public StorageInBox columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getMemo() {
        return memo;
    }

    public StorageInBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public StorageInBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public StorageInBox frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public StorageIn getStorageIn() {
        return storageIn;
    }

    public StorageInBox storageIn(StorageIn storageIn) {
        this.storageIn = storageIn;
        return this;
    }

    public void setStorageIn(StorageIn storageIn) {
        this.storageIn = storageIn;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public StorageInBox equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }

    public StorageInBox supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }

    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public Area getArea() {
        return area;
    }

    public StorageInBox area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageInBox storageInBox = (StorageInBox) o;
        if (storageInBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storageInBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StorageInBox{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", rowsInShelf='" + rowsInShelf + "'" +
            ", columnsInShelf='" + columnsInShelf + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            '}';
    }
}
