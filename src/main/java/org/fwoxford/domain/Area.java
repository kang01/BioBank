package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Area.
 */
@Entity
@Table(name = "area")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Area extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_area")
    @SequenceGenerator(name = "seq_area",sequenceName = "seq_area",allocationSize = 1,initialValue = 1)
    private Long id;
    /**
     * 区域编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "area_code", length = 100, nullable = false)
    private String areaCode;
    /**
     * 区域内冻存架数量
     */
    @NotNull
    @Max(value = 100)
    @Column(name = "freeze_frame_number", nullable = false)
    private Integer freezeFrameNumber;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false)
    private String equipmentCode;
    /**
     * 设备
     */
    @ManyToOne(optional = false)
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

    public Area areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getFreezeFrameNumber() {
        return freezeFrameNumber;
    }

    public Area freezeFrameNumber(Integer freezeFrameNumber) {
        this.freezeFrameNumber = freezeFrameNumber;
        return this;
    }

    public void setFreezeFrameNumber(Integer freezeFrameNumber) {
        this.freezeFrameNumber = freezeFrameNumber;
    }

    public String getMemo() {
        return memo;
    }

    public Area memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public Area status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public Area equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Area equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Area area = (Area) o;
        if (area.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, area.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Area{" +
            "id=" + id +
            ", areaCode='" + areaCode + "'" +
            ", freezeFrameNumber='" + freezeFrameNumber + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", equipmentCode='" + equipmentCode + "'" +
            '}';
    }
}
