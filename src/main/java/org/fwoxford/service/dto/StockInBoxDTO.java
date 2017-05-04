package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockInBox entity.
 */
public class StockInBoxDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 设备编码
     */
    @Size(max = 100)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @Size(max = 100)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @Size(max = 100)
    private String supportRackCode;
    /**
     * 所在冻存架行数
     */
    @Size(max = 20)
    private String rowsInShelf;
    /**
     * 所在冻存架列数
     */
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
     * 样本数量
     */
    private Integer countOfSample;
    /**
     * 入库Id
     */
    private Long stockInId;
    /**
     * 入库编码
     */
    private String stockInCode;
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

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public Long getStockInId() {
        return stockInId;
    }

    public void setStockInId(Long stockInId) {
        this.stockInId = stockInId;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
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

        StockInBoxDTO stockInBoxDTO = (StockInBoxDTO) o;

        if ( ! Objects.equals(id, stockInBoxDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInBoxDTO{" +
            "id=" + id +
            ", stockInCode='" + stockInCode + "'" +
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
