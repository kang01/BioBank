package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Equipment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false ,unique = true)
    private String equipmentCode;
    /**
     * 工作温度
     */
    @NotNull
    @Max(value = 100)
    @Column(name = "temperature", nullable = false)
    private Integer temperature;
    /**
     * 设备地址
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "equipment_address", length = 255, nullable = false)
    private String equipmentAddress;
    /**
     * 存放最大冻存管数量
     */
    @NotNull
    @Column(name = "ampoules_max", nullable = false)
    private Integer ampoulesMax;
    /**
     * 存放最小冻存管数量
     */
    @NotNull
    @Column(name = "ampoules_min", nullable = false)
    private Integer ampoulesMin;
    /**
     * 标签1
     */
    @Size(max = 100)
    @Column(name = "label_1", length = 100)
    private String label1;
    /**
     * 标签2
     */
    @Size(max = 100)
    @Column(name = "label_2", length = 100)
    private String label2;
    /**
     * 标签3
     */
    @Size(max = 100)
    @Column(name = "label_3", length = 100)
    private String label3;
    /**
     * 标签4
     */
    @Size(max = 100)
    @Column(name = "label_4", length = 100)
    private String label4;
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
     * 设备组
     */
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "equipment_group_id")
    private EquipmentGroup equipmentGroup;
    /**
     * 设备型号
     */
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "equipment_modle_id")
    private EquipmentModle equipmentModle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public Equipment equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Equipment temperature(Integer temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getEquipmentAddress() {
        return equipmentAddress;
    }

    public Equipment equipmentAddress(String equipmentAddress) {
        this.equipmentAddress = equipmentAddress;
        return this;
    }

    public void setEquipmentAddress(String equipmentAddress) {
        this.equipmentAddress = equipmentAddress;
    }

    public Integer getAmpoulesMax() {
        return ampoulesMax;
    }

    public Equipment ampoulesMax(Integer ampoulesMax) {
        this.ampoulesMax = ampoulesMax;
        return this;
    }

    public void setAmpoulesMax(Integer ampoulesMax) {
        this.ampoulesMax = ampoulesMax;
    }

    public Integer getAmpoulesMin() {
        return ampoulesMin;
    }

    public Equipment ampoulesMin(Integer ampoulesMin) {
        this.ampoulesMin = ampoulesMin;
        return this;
    }

    public void setAmpoulesMin(Integer ampoulesMin) {
        this.ampoulesMin = ampoulesMin;
    }

    public String getLabel1() {
        return label1;
    }

    public Equipment label1(String label1) {
        this.label1 = label1;
        return this;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public Equipment label2(String label2) {
        this.label2 = label2;
        return this;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getLabel4() {
        return label4;
    }

    public Equipment label4(String label4) {
        this.label4 = label4;
        return this;
    }

    public void setLabel4(String label4) {
        this.label4 = label4;
    }

    public String getMemo() {
        return memo;
    }

    public Equipment memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public Equipment status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel3() {
        return label3;
    }

    public Equipment label3(String label3) {
        this.label3 = label3;
        return this;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    public EquipmentGroup getEquipmentGroup() {
        return equipmentGroup;
    }

    public Equipment equipmentGroup(EquipmentGroup equipmentGroup) {
        this.equipmentGroup = equipmentGroup;
        return this;
    }

    public void setEquipmentGroup(EquipmentGroup equipmentGroup) {
        this.equipmentGroup = equipmentGroup;
    }

    public EquipmentModle getEquipmentModle() {
        return equipmentModle;
    }

    public Equipment equipmentModle(EquipmentModle equipmentModle) {
        this.equipmentModle = equipmentModle;
        return this;
    }

    public void setEquipmentModle(EquipmentModle equipmentModle) {
        this.equipmentModle = equipmentModle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Equipment equipment = (Equipment) o;
        if (equipment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + "'" +
            ", temperature='" + temperature + "'" +
            ", equipmentAddress='" + equipmentAddress + "'" +
            ", ampoulesMax='" + ampoulesMax + "'" +
            ", ampoulesMin='" + ampoulesMin + "'" +
            ", label1='" + label1 + "'" +
            ", label2='" + label2 + "'" +
            ", label4='" + label4 + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", label3='" + label3 + "'" +
            '}';
    }
}
