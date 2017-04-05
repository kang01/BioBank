package org.fwoxford.web.rest;

import org.fwoxford.domain.SampleType;

/**
 * Created by gengluying on 2017/4/5.
 */
public class FrozenBoxPositionDTO {
    private Long equipmentId;
    private String equipmentCode;
    private Long areaId;
    private String areaCode;
    private Long supportRackId;
    private String supportRackCode;
    private String rowsInShelf;
    private String columnsInShelf;

    @Override
    public String toString() {
        return "FrozenBoxPositionDTO{" +
            "equipmentId=" + equipmentId +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaId=" + areaId +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackId=" + supportRackId +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            '}';
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Long getSupportRackId() {
        return supportRackId;
    }

    public void setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
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
}
