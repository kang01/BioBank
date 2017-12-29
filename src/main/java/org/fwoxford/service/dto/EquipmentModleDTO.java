package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the EquipmentModle entity.
 */
public class EquipmentModleDTO implements Serializable {

    private Long id;
    /**
     * 设备型号编码
     */
    @NotNull
    @Size(max = 100)
    private String equipmentModelCode;
    /**
     * 设备型号名称
     */
    @NotNull
    @Size(max = 255)
    private String equipmentModelName;
    /**
     * 设备类型：液氮，冰箱。
     */
    @NotNull
    @Size(max = 20)
    private String equipmentType;
    /**
     * 区域数量
     */
    @NotNull
    @Max(value = 255)
    private Integer areaNumber;
    /**
     * 区域内架子数量
     */
    @NotNull
    @Max(value = 500)
    private Integer shelveNumberInArea;
    /**
     * 备注
     */
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 最高温度
     */
    @Max(value = 100)
    private Integer temperatureMax;
    /**
     * 最低温度
     */
    @Max(value = 100)
    private Integer temperatureMin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getEquipmentModelCode() {
        return equipmentModelCode;
    }

    public void setEquipmentModelCode(String equipmentModelCode) {
        this.equipmentModelCode = equipmentModelCode;
    }
    public String getEquipmentModelName() {
        return equipmentModelName;
    }

    public void setEquipmentModelName(String equipmentModelName) {
        this.equipmentModelName = equipmentModelName;
    }
    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }
    public Integer getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(Integer areaNumber) {
        this.areaNumber = areaNumber;
    }
    public Integer getShelveNumberInArea() {
        return shelveNumberInArea;
    }

    public void setShelveNumberInArea(Integer shelveNumberInArea) {
        this.shelveNumberInArea = shelveNumberInArea;
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
    public Integer getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Integer temperatureMax) {
        this.temperatureMax = temperatureMax;
    }
    public Integer getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Integer temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EquipmentModleDTO equipmentModleDTO = (EquipmentModleDTO) o;

        if ( ! Objects.equals(id, equipmentModleDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentModleDTO{" +
            "id=" + id +
            ", equipmentModelCode='" + equipmentModelCode + "'" +
            ", equipmentModelName='" + equipmentModelName + "'" +
            ", equipmentType='" + equipmentType + "'" +
            ", areaNumber='" + areaNumber + "'" +
            ", shelveNumberInArea='" + shelveNumberInArea + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", temperatureMax='" + temperatureMax + "'" +
            ", temperatureMin='" + temperatureMin + "'" +
            '}';
    }
}
