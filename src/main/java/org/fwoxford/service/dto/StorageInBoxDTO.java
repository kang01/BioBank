package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StorageInBox entity.
 */
public class StorageInBoxDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @NotNull
    @Size(max = 100)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @NotNull
    @Size(max = 100)
    private String supportRackCode;
    /**
     * 所在冻存架行数
     */
    @NotNull
    @Size(max = 20)
    private String rowsInShelf;
    /**
     * 所在冻存架列数
     */
    @NotNull
    @Size(max = 20)
    private String columnsInShelf;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 入库Id
     */
    private Long storageInId;
    /**
     * 设备Id
     */
    private Long equipmentId;
    /**
     * 冻存架Id
     */
    private Long supportRackId;
    /**
     * 区域Id
     */
    private Long areaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getSupportRackCode() {
        return supportRackCode;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }
    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }
    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getStorageInId() {
        return storageInId;
    }

    public void setStorageInId(Long storageInId) {
        this.storageInId = storageInId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getSupportRackId() {
        return supportRackId;
    }

    public void setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StorageInBoxDTO storageInBoxDTO = (StorageInBoxDTO) o;

        if ( ! Objects.equals(id, storageInBoxDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StorageInBoxDTO{" +
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
