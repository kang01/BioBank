package org.fwoxford.service.dto;


import org.fwoxford.domain.Area;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
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
     * 区域
     */
    @NotNull
    private Area area;

    private List<FrozenBoxDTO> frozenBoxDTOList;

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
        return area.getAreaCode();
    }

    public void setAreaCode(String areaCode) {
        if (this.area == null){
            this.area = new Area();
        }
        this.area.setAreaCode(areaCode);
    }
    public String getEquipmentCode() {
        return area.getEquipmentCode();
    }

    public void setEquipmentCode(String equipmentCode) {
        if (this.area == null){
            this.area = new Area();
        }
        this.area.setEquipmentCode(equipmentCode);
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
        return area.getId();
    }

    public void setAreaId(Long areaId) {
        if (this.area == null){
            this.area = new Area();
        }
        this.area.setId(areaId);
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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
            ", supportRackTypeCode='" + supportRackTypeCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", memo='" + memo + '\'' +
            ", status='" + status + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", supportRackTypeId=" + supportRackTypeId +
            ", area=" + area +
            '}';
    }

    /**
     * 架子上的盒子
     */
    public List<FrozenBoxDTO> getFrozenBoxDTOList() {
        return frozenBoxDTOList;
    }

    public void setFrozenBoxDTOList(List<FrozenBoxDTO> frozenBoxDTOList) {
        this.frozenBoxDTOList = frozenBoxDTOList;
    }
}
