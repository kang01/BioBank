package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2017/6/22.
 */
public class FrozenBoxListSearchForm {

    private String[] projectCodeStr;
    /**
     * 空间条件：1：已用：2：剩余
     */
    private Integer spaceType;
    /**
     * 比较类型：1：大于，2：大于等于，3：等于，4：小于，5：小于等于
     */
    private Integer compareType;
    /**
     * 数值
     */
    private Long number;
    private String[] frozenBoxCodeStr;
    private String columnsInShelf;
    private String rowsInShelf;
    private String status;
    private Long equipmentId;
    private Long areaId;
    private Long shelvesId;
    private Long sampleTypeId;
    private Long sampleClassificationId;
    private Long frozenBoxTypeId;

    public String[] getProjectCodeStr() {
        return projectCodeStr;
    }

    public void setProjectCodeStr(String[] projectCodeStr) {
        this.projectCodeStr = projectCodeStr;
    }

    public Integer getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(Integer spaceType) {
        this.spaceType = spaceType;
    }

    public Integer getCompareType() {
        return compareType;
    }

    public void setCompareType(Integer compareType) {
        this.compareType = compareType;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String[] getFrozenBoxCodeStr() {
        return frozenBoxCodeStr;
    }

    public void setFrozenBoxCodeStr(String[] frozenBoxCodeStr) {
        this.frozenBoxCodeStr = frozenBoxCodeStr;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(Long shelvesId) {
        this.shelvesId = shelvesId;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }
}
