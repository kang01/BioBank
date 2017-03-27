package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TranshipBox entity.
 */
public class TranshipBoxDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 100)
    private String equipmentCode;

    @NotNull
    @Size(max = 100)
    private String areaCode;

    @NotNull
    @Size(max = 100)
    private String supportRackCode;

    @NotNull
    @Size(max = 20)
    private String rowsInShelf;

    @NotNull
    @Size(max = 20)
    private String columnsInShelf;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private Long transhipId;

    private Long frozenBoxId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
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

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranshipBoxDTO transhipBoxDTO = (TranshipBoxDTO) o;

        if ( ! Objects.equals(id, transhipBoxDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipBoxDTO{" +
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
