package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FrozenBoxType entity.
 */
public class FrozenBoxTypeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 冻存盒类型编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxTypeCode;
    /**
     * 冻存盒类型名称
     */
    @NotNull
    @Size(max = 255)
    private String frozenBoxTypeName;
    /**
     * 冻存盒行数
     */
    @NotNull
    @Size(max = 20)
    private String frozenBoxTypeRows;
    /**
     * 冻存盒列数
     */
    @NotNull
    @Size(max = 20)
    private String frozenBoxTypeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态
     */
    @Size(max = 20)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }
    public String getFrozenBoxTypeName() {
        return frozenBoxTypeName;
    }

    public void setFrozenBoxTypeName(String frozenBoxTypeName) {
        this.frozenBoxTypeName = frozenBoxTypeName;
    }
    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }

    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
    }
    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }

    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FrozenBoxTypeDTO frozenBoxTypeDTO = (FrozenBoxTypeDTO) o;

        if ( ! Objects.equals(id, frozenBoxTypeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenBoxTypeDTO{" +
            "id=" + id +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + "'" +
            ", frozenBoxTypeName='" + frozenBoxTypeName + "'" +
            ", frozenBoxTypeRows='" + frozenBoxTypeRows + "'" +
            ", frozenBoxTypeColumns='" + frozenBoxTypeColumns + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
