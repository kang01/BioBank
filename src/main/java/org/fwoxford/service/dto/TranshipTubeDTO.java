package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TranshipTube entity.
 */
public class TranshipTubeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String columnsInTube;

    @NotNull
    @Size(max = 20)
    private String rowsInTube;

    private Long transhipBoxId;

    private Long frozenTubeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getColumnsInTube() {
        return columnsInTube;
    }

    public void setColumnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
    }
    public String getRowsInTube() {
        return rowsInTube;
    }

    public void setRowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
    }

    public Long getTranshipBoxId() {
        return transhipBoxId;
    }

    public void setTranshipBoxId(Long transhipBoxId) {
        this.transhipBoxId = transhipBoxId;
    }

    public Long getFrozenTubeId() {
        return frozenTubeId;
    }

    public void setFrozenTubeId(Long frozenTubeId) {
        this.frozenTubeId = frozenTubeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranshipTubeDTO transhipTubeDTO = (TranshipTubeDTO) o;

        if ( ! Objects.equals(id, transhipTubeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipTubeDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", columnsInTube='" + columnsInTube + "'" +
            ", rowsInTube='" + rowsInTube + "'" +
            '}';
    }
}
