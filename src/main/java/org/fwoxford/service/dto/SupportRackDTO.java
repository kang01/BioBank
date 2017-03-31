package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the SupportRack entity.
 */
public class SupportRackDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 冻存架类型编码
     */
    @NotNull
    @Size(max = 100)
    private String supportRackTypeCode;
    /**
     * 区域编码
     */
    @NotNull
    @Size(max = 100)
    private String areaCode;
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
     * 冻存架编码
     */
    @NotNull
    @Size(max = 100)
    private String supportRackCode;
    /**
     * 冻存架类型Id
     */
    private Long supportRackTypeId;
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
    public String getSupportRackTypeCode() {
        return supportRackTypeCode;
    }

    public void setSupportRackTypeCode(String supportRackTypeCode) {
        this.supportRackTypeCode = supportRackTypeCode;
    }
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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
    public String getSupportRackCode() {
        return supportRackCode;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public Long getSupportRackTypeId() {
        return supportRackTypeId;
    }

    public void setSupportRackTypeId(Long supportRackTypeId) {
        this.supportRackTypeId = supportRackTypeId;
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

        SupportRackDTO supportRackDTO = (SupportRackDTO) o;

        if ( ! Objects.equals(id, supportRackDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupportRackDTO{" +
            "id=" + id +
            ", supportRackTypeCode='" + supportRackTypeCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            '}';
    }
}
