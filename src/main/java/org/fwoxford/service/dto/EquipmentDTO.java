package org.fwoxford.service.dto;


import io.swagger.models.auth.In;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Equipment entity.
 */
public class EquipmentDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    private String equipmentCode;
    /**
     * 工作温度
     */
    @NotNull
    @Max(value = 100)
    private Integer temperature;
    /**
     * 设备地址
     */
    @NotNull
    @Size(max = 255)
    private String equipmentAddress;
    /**
     * 存放最大冻存管数量
     */
    @NotNull
    private Integer ampoulesMax;
    /**
     * 存放最小冻存管数量
     */
    @NotNull
    private Integer ampoulesMin;
    /**
     * 标签1
     */
    @Size(max = 100)
    private String label1;
    /**
     * 标签2
     */
    @Size(max = 100)
    private String label2;
    /**
     * 标签3
     */
    @Size(max = 100)
    private String label3;
    /**
     * 标签4
     */
    @Size(max = 100)
    private String label4;
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

    private Long equipmentGroupId;

    private Long equipmentModleId;

    private String equipmentType;
    private Integer restOfSpace;
    private String projectCodes;

    private List<AreaDTO> areaDTOList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }
    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
    public String getEquipmentAddress() {
        return equipmentAddress;
    }

    public void setEquipmentAddress(String equipmentAddress) {
        this.equipmentAddress = equipmentAddress;
    }
    public Integer getAmpoulesMax() {
        return ampoulesMax;
    }

    public void setAmpoulesMax(Integer ampoulesMax) {
        this.ampoulesMax = ampoulesMax;
    }
    public Integer getAmpoulesMin() {
        return ampoulesMin;
    }

    public void setAmpoulesMin(Integer ampoulesMin) {
        this.ampoulesMin = ampoulesMin;
    }
    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }
    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }
    public String getLabel4() {
        return label4;
    }

    public void setLabel4(String label4) {
        this.label4 = label4;
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
    public String getLabel3() {
        return label3;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    public Long getEquipmentGroupId() {
        return equipmentGroupId;
    }

    public void setEquipmentGroupId(Long equipmentGroupId) {
        this.equipmentGroupId = equipmentGroupId;
    }

    public Long getEquipmentModleId() {
        return equipmentModleId;
    }

    public void setEquipmentModleId(Long equipmentModleId) {
        this.equipmentModleId = equipmentModleId;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Integer getRestOfSpace() {
        return restOfSpace;
    }

    public void setRestOfSpace(Integer restOfSpace) {
        this.restOfSpace = restOfSpace;
    }

    public String getProjectCodes() {
        return projectCodes;
    }

    public void setProjectCodes(String projectCodes) {
        this.projectCodes = projectCodes;
    }

    public List<AreaDTO> getAreaDTOList() {
        return areaDTOList;
    }

    public void setAreaDTOList(List<AreaDTO> areaDTOList) {
        this.areaDTOList = areaDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EquipmentDTO equipmentDTO = (EquipmentDTO) o;

        if ( ! Objects.equals(id, equipmentDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentDTO{" +
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
