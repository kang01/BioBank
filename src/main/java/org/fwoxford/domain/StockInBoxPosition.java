package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockInBoxPosition.
 */
@Entity
@Table(name = "stock_in_box_pos")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockInBoxPosition extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_in_box_pos")
    @SequenceGenerator(name = "seq_stock_in_box_pos",sequenceName = "seq_stock_in_box_pos",allocationSize = 1,initialValue = 1)
    private Long id;

    @Size(max = 100)
    @Column(name = "equipment_code", length = 100)
    private String equipmentCode;

    @Size(max = 100)
    @Column(name = "area_code", length = 100)
    private String areaCode;

    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100)
    private String supportRackCode;

    @Size(max = 20)
    @Column(name = "rows_in_shelf", length = 20)
    private String rowsInShelf;

    @Size(max = 20)
    @Column(name = "columns_in_shelf", length = 20)
    private String columnsInShelf;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne
    private Equipment equipment;

    @ManyToOne
    private Area area;

    @ManyToOne
    private SupportRack supportRack;

    @ManyToOne(optional = false)
    @NotNull
    private StockInBox stockInBox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public StockInBoxPosition equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public StockInBoxPosition areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public StockInBoxPosition supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public StockInBoxPosition rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public StockInBoxPosition columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getStatus() {
        return status;
    }

    public StockInBoxPosition status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockInBoxPosition memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public StockInBoxPosition equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Area getArea() {
        return area;
    }

    public StockInBoxPosition area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }

    public StockInBoxPosition supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }

    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public StockInBox getStockInBox() {
        return stockInBox;
    }

    public StockInBoxPosition stockInBox(StockInBox stockInBox) {
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
        StockInBoxPosition stockInBoxPosition = (StockInBoxPosition) o;
        if (stockInBoxPosition.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockInBoxPosition.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInBoxPosition{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", rowsInShelf='" + rowsInShelf + "'" +
            ", columnsInShelf='" + columnsInShelf + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
