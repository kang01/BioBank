package org.fwoxford.service.dto;


import org.fwoxford.domain.Equipment;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Area entity.
 */
public class AreaDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 区域编码
     */
    @NotNull
    @Size(max = 100)
    private String areaCode;
    /**
     * 区域内冻存架数量
     */
    @NotNull
    @Max(value = 100)
    private Integer freezeFrameNumber;
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
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    private String equipmentCode;
    /**
     * 设备ID
     */
    private Long equipmentId;

    @NotNull
    private Equipment equipment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public Integer getFreezeFrameNumber() {
        return freezeFrameNumber;
    }

    public void setFreezeFrameNumber(Integer freezeFrameNumber) {
        this.freezeFrameNumber = freezeFrameNumber;
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
    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        if (this.equipment == null){
            this.equipment = new Equipment();
        }
        this.equipmentCode = equipmentCode;
    }

    public Long getEquipmentId() {
        return equipment.getId();
    }

    public void setEquipmentId(Long equipmentId) {
        if (this.equipment == null){
            this.equipment = new Equipment();
        }
        this.equipment.setId(equipmentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AreaDTO areaDTO = (AreaDTO) o;

        if ( ! Objects.equals(id, areaDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AreaDTO{" +
            "id=" + id +
            ", areaCode='" + areaCode + '\'' +
            ", freezeFrameNumber=" + freezeFrameNumber +
            ", memo='" + memo + '\'' +
            ", status='" + status + '\'' +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", equipmentId=" + equipmentId +
            ", equipment=" + equipment +
            '}';
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
