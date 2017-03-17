package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EquipmentGroup.
 */
@Entity
@Table(name = "equipment_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EquipmentGroup extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_group_name", length = 100, nullable = false)
    private String equipmentGroupName;

    @NotNull
    @Max(value = 100)
    @Column(name = "equipment_group_manager_id", nullable = false)
    private Long equipmentGroupManagerId;

    @NotNull
    @Size(max = 256)
    @Column(name = "equipment_manager_name", length = 256, nullable = false)
    private String equipmentManagerName;

    @NotNull
    @Size(max = 256)
    @Column(name = "equipment_group_address", length = 256, nullable = false)
    private String equipmentGroupAddress;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
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

    public EquipmentGroup equipmentGroupName(String equipmentGroupName) {
        this.equipmentGroupName = equipmentGroupName;
        return this;
    }

    public void setEquipmentGroupName(String equipmentGroupName) {
        this.equipmentGroupName = equipmentGroupName;
    }

    public Long getEquipmentGroupManagerId() {
        return equipmentGroupManagerId;
    }

    public EquipmentGroup equipmentGroupManagerId(Long equipmentGroupManagerId) {
        this.equipmentGroupManagerId = equipmentGroupManagerId;
        return this;
    }

    public void setEquipmentGroupManagerId(Long equipmentGroupManagerId) {
        this.equipmentGroupManagerId = equipmentGroupManagerId;
    }

    public String getEquipmentManagerName() {
        return equipmentManagerName;
    }

    public EquipmentGroup equipmentManagerName(String equipmentManagerName) {
        this.equipmentManagerName = equipmentManagerName;
        return this;
    }

    public void setEquipmentManagerName(String equipmentManagerName) {
        this.equipmentManagerName = equipmentManagerName;
    }

    public String getEquipmentGroupAddress() {
        return equipmentGroupAddress;
    }

    public EquipmentGroup equipmentGroupAddress(String equipmentGroupAddress) {
        this.equipmentGroupAddress = equipmentGroupAddress;
        return this;
    }

    public void setEquipmentGroupAddress(String equipmentGroupAddress) {
        this.equipmentGroupAddress = equipmentGroupAddress;
    }

    public String getStatus() {
        return status;
    }

    public EquipmentGroup status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public EquipmentGroup memo(String memo) {
        this.memo = memo;
        return this;
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
        EquipmentGroup equipmentGroup = (EquipmentGroup) o;
        if (equipmentGroup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, equipmentGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentGroup{" +
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
