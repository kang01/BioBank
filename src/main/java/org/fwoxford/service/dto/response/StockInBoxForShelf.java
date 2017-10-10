package org.fwoxford.service.dto.response;

import org.fwoxford.domain.Area;
import org.fwoxford.domain.SampleType;
import org.fwoxford.domain.SupportRack;
import org.fwoxford.service.dto.*;

import java.util.List;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxForShelf {
    private Long frozenBoxId;
    private String frozenBoxCode;
    private String frozenBoxCode1D;
    private Integer isSplit;
    private String status;

    private Integer countOfSample;
    private String frozenBoxTypeColumns;
    private String frozenBoxTypeRows;

    private Long equipmentId;
    private Long areaId;
    private Long supportRackId;
    private Long frozenBoxTypeId;
    private String sampleTypeCode;


    private SampleTypeDTO sampleType;
    private EquipmentDTO equipment;
    private AreaDTO area;
    private SupportRackDTO shelf;
    private FrozenBoxTypeDTO frozenBoxType;

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Integer getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }

    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }

    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
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

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
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

    public SampleTypeDTO getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleTypeDTO sampleType) {
        this.sampleType = sampleType;
    }

    public EquipmentDTO getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentDTO equipment) {
        this.equipment = equipment;
    }

    public AreaDTO getArea() {
        return area;
    }

    public void setArea(AreaDTO area) {
        this.area = area;
    }

    public SupportRackDTO getShelf() {
        return shelf;
    }

    public void setShelf(SupportRackDTO shelf) {
        this.shelf = shelf;
    }

    public FrozenBoxTypeDTO getFrozenBoxType() {
        return frozenBoxType;
    }

    public void setFrozenBoxType(FrozenBoxTypeDTO frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
    }
}
