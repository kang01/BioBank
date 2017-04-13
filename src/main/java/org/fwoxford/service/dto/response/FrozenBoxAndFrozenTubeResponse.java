package org.fwoxford.service.dto.response;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * A DTO for the FrozenBox entity.
 */
public class FrozenBoxAndFrozenTubeResponse {

    private Long id;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 冻存盒行数
     */
    @NotNull
    @Size(max = 20)
    private String frozenBoxRows;
    /**
     * 冻存盒列数
     */
    @NotNull
    @Size(max = 20)
    private String frozenBoxColumns;
    /**
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @NotNull
    @Size(max = 100)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @NotNull
    @Size(max = 100)
    private String supportRackCode;
    /**
     * 是否分装（1：是；0：否）
     */
    @NotNull
    private Integer isSplit;
    /**
     * 备注
     */
    @Size(max = 255)
    private String memo;
    /**
     * 状态：（2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废）
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 冻存盒类型ID
     */
    private Long frozenBoxTypeId;
    /**
     * 样本类型ID
     */
    private Long sampleTypeId;

    private String sampleTypeCode;
    /**
     * 设备ID
     */
    private Long equipmentId;
    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 冻存架ID
     */
    private Long supportRackId;
    /**
     * 冻存管列表
     */
    private List<FrozenTubeResponse> frozenTubeDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getFrozenBoxRows() {
        return frozenBoxRows;
    }

    public void setFrozenBoxRows(String frozenBoxRows) {
        this.frozenBoxRows = frozenBoxRows;
    }

    public String getFrozenBoxColumns() {
        return frozenBoxColumns;
    }

    public void setFrozenBoxColumns(String frozenBoxColumns) {
        this.frozenBoxColumns = frozenBoxColumns;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public Integer getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
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

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public List<FrozenTubeResponse> getFrozenTubeDTOS() {
        return frozenTubeDTOS;
    }

    public void setFrozenTubeDTOS(List<FrozenTubeResponse> frozenTubeDTOS) {
        this.frozenTubeDTOS = frozenTubeDTOS;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getSupportRackId() {
        return supportRackId;
    }

    public void setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }
}
