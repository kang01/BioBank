package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SupportRackType entity.
 */
public class SupportRackTypeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 冻存架类型编码
     */
    @NotNull
    @Size(max = 20)
    private String supportRackTypeCode;
    /**
     * 冻存架类型名称
     */
    @NotNull
    @Size(max = 20)
    private String supportRackTypeName;
    /**
     * 冻存架行数
     */
    @NotNull
    @Size(max = 20)
    private String supportRackRows;
    /**
     * 冻存架列数
     */
    @NotNull
    @Size(max = 20)
    private String supportRackColumns;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSupportRackTypeCode() {
        return supportRackTypeCode;
    }

    public void setSupportRackTypeCode(String supportRackTypeCode) {
        this.supportRackTypeCode = supportRackTypeCode;
    }

    public String getSupportRackTypeName() {
        return supportRackTypeName;
    }

    public void setSupportRackTypeName(String supportRackTypeName) {
        this.supportRackTypeName = supportRackTypeName;
    }

    public String getSupportRackRows() {
        return supportRackRows;
    }

    public void setSupportRackRows(String supportRackRows) {
        this.supportRackRows = supportRackRows;
    }
    public String getSupportRackColumns() {
        return supportRackColumns;
    }

    public void setSupportRackColumns(String supportRackColumns) {
        this.supportRackColumns = supportRackColumns;
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

        SupportRackTypeDTO supportRackTypeDTO = (SupportRackTypeDTO) o;

        if ( ! Objects.equals(id, supportRackTypeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupportRackTypeDTO{" +
            "id=" + id +
            ", supportRackTypeCode='" + supportRackTypeCode + "'" +
            ", supportRackTypeName='" + supportRackTypeName + "'" +
            ", supportRackRows='" + supportRackRows + "'" +
            ", supportRackColumns='" + supportRackColumns + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
