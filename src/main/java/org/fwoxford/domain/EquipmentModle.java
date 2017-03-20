package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EquipmentModle.
 */
@Entity
@Table(name = "equipment_modle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EquipmentModle extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_model_code", length = 100, nullable = false)
    private String equipmentModelCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "equipment_model_name", length = 255, nullable = false)
    private String equipmentModelName;

    @NotNull
    @Size(max = 20)
    @Column(name = "equipment_type", length = 20, nullable = false)
    private String equipmentType;

    @NotNull
    @Max(value = 100)
    @Column(name = "area_number", nullable = false)
    private Integer areaNumber;

    @NotNull
    @Max(value = 100)
    @Column(name = "shelve_number_in_area", nullable = false)
    private Integer shelveNumberInArea;

    @Column(name = "memo")
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Max(value = 5000)
    @Column(name = "temperature_max")
    private Integer temperatureMax;

    @Max(value = 5000)
    @Column(name = "temperature_min")
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

    public EquipmentModle equipmentModelCode(String equipmentModelCode) {
        this.equipmentModelCode = equipmentModelCode;
        return this;
    }

    public void setEquipmentModelCode(String equipmentModelCode) {
        this.equipmentModelCode = equipmentModelCode;
    }

    public String getEquipmentModelName() {
        return equipmentModelName;
    }

    public EquipmentModle equipmentModelName(String equipmentModelName) {
        this.equipmentModelName = equipmentModelName;
        return this;
    }

    public void setEquipmentModelName(String equipmentModelName) {
        this.equipmentModelName = equipmentModelName;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public EquipmentModle equipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
        return this;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Integer getAreaNumber() {
        return areaNumber;
    }

    public EquipmentModle areaNumber(Integer areaNumber) {
        this.areaNumber = areaNumber;
        return this;
    }

    public void setAreaNumber(Integer areaNumber) {
        this.areaNumber = areaNumber;
    }

    public Integer getShelveNumberInArea() {
        return shelveNumberInArea;
    }

    public EquipmentModle shelveNumberInArea(Integer shelveNumberInArea) {
        this.shelveNumberInArea = shelveNumberInArea;
        return this;
    }

    public void setShelveNumberInArea(Integer shelveNumberInArea) {
        this.shelveNumberInArea = shelveNumberInArea;
    }

    public String getMemo() {
        return memo;
    }

    public EquipmentModle memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public EquipmentModle status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTemperatureMax() {
        return temperatureMax;
    }

    public EquipmentModle temperatureMax(Integer temperatureMax) {
        this.temperatureMax = temperatureMax;
        return this;
    }

    public void setTemperatureMax(Integer temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Integer getTemperatureMin() {
        return temperatureMin;
    }

    public EquipmentModle temperatureMin(Integer temperatureMin) {
        this.temperatureMin = temperatureMin;
        return this;
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
        EquipmentModle equipmentModle = (EquipmentModle) o;
        if (equipmentModle.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, equipmentModle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentModle{" +
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
