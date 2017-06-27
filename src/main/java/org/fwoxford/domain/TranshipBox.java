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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;
    /**
     * 设备编码
     */
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @Size(max = 100)
    @Column(name = "area_code", length = 100)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100)
    private String supportRackCode;
    /**
     * 所在架子行数
     */
    @Size(max = 20)
    @Column(name = "rows_in_shelf", length = 20)
    private String rowsInShelf;
    /**
     * 所在架子列数
     */
    @Size(max = 20)
    @Column(name = "columns_in_shelf", length = 20)
    private String columnsInShelf;

    /**
     * 样本数量
     */
    @NotNull
    @Column(name = "count_of_sample", nullable = false)
    private Integer countOfSample;

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
     * 转运
     */
    @ManyToOne(optional = false)
    private Tranship tranship;
    /**
     * 冻存盒
     */
    @ManyToOne(optional = false)
    private FrozenBox frozenBox;

    /**
     * 设备
     */
    @ManyToOne
    private Equipment equipment;
    /**
     * 冻存架
     */
    @ManyToOne
    private SupportRack supportRack;
    /**
     * 区域
     */
    @ManyToOne
    private Area area;

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

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public TranshipBox countOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
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

    public Equipment getEquipment() {
        return equipment;
    }
    public TranshipBox equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }
    public TranshipBox supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }
    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public Area getArea() {
        return area;
    }
    public TranshipBox area(Area area) {
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
            ", countOfSample='" + countOfSample + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
