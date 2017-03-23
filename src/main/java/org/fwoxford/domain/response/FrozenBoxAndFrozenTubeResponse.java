package org.fwoxford.domain.response;


import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.domain.SampleType;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.dto.AbstractAuditingDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    @NotNull
    @Size(max = 20)
    private String frozenBoxRows;

    @NotNull
    @Size(max = 20)
    private String frozenBoxColumns;

    @NotNull
    @Size(max = 100)
    private String equipmentCode;

    @NotNull
    @Size(max = 100)
    private String areaCode;

    @NotNull
    @Size(max = 100)
    private String supportRackCode;

    @NotNull
    @Size(max = 20)
    private String isSplit;

    @Size(max = 255)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private Integer frozenBoxTypeId;

    private Integer sampleTypeId;

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

    public String getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(String isSplit) {
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

    public Integer getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Integer frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public Integer getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Integer sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }
}
