package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the EquipmentGroup entity.
 */
public class EquipmentGroupDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 设备组名称
     */
    @NotNull
    @Size(max = 100)
    private String equipmentGroupName;
    /**
     * 设备组负责人ID
     */
    @NotNull
    @Max(value = 100)
    private Long equipmentGroupManagerId;
    /**
     * 设备组负责人名称
     */
    @NotNull
    @Size(max = 256)
    private String equipmentManagerName;
    /**
     * 设备组地址
     */
    @NotNull
    @Size(max = 256)
    private String equipmentGroupAddress;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getEquipmentGroupName() {
        return equipmentGroupName;
    }

    public void setEquipmentGroupName(String equipmentGroupName) {
        this.equipmentGroupName = equipmentGroupName;
    }
    public Long getEquipmentGroupManagerId() {
        return equipmentGroupManagerId;
    }

    public void setEquipmentGroupManagerId(Long equipmentGroupManagerId) {
        this.equipmentGroupManagerId = equipmentGroupManagerId;
    }
    public String getEquipmentManagerName() {
        return equipmentManagerName;
    }

    public void setEquipmentManagerName(String equipmentManagerName) {
        this.equipmentManagerName = equipmentManagerName;
    }
    public String getEquipmentGroupAddress() {
        return equipmentGroupAddress;
    }

    public void setEquipmentGroupAddress(String equipmentGroupAddress) {
        this.equipmentGroupAddress = equipmentGroupAddress;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EquipmentGroupDTO equipmentGroupDTO = (EquipmentGroupDTO) o;

        if ( ! Objects.equals(id, equipmentGroupDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentGroupDTO{" +
            "id=" + id +
            ", equipmentGroupName='" + equipmentGroupName + "'" +
            ", equipmentGroupManagerId='" + equipmentGroupManagerId + "'" +
            ", equipmentManagerName='" + equipmentManagerName + "'" +
            ", equipmentGroupAddress='" + equipmentGroupAddress + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
