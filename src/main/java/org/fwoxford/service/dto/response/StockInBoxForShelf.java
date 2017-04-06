package org.fwoxford.service.dto.response;

import org.fwoxford.domain.SampleType;
import org.fwoxford.domain.SupportRack;
import org.fwoxford.service.dto.SampleTypeDTO;
import org.fwoxford.service.dto.SupportRackDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxForShelf {

    private Long id;

    private Integer isSplit;
    private Long equipmentId;
    private Long areaId;
    private Long supportRackId;

    private String status;

    private Long frozenBoxId;
    private String frozenBoxCode;
    private Integer countOfSample;
    private String frozenBoxColumns;
    private String frozenBoxRows;
    private SampleTypeDTO sampleType;
    private SupportRackDTO shelf;
//    private List<StockInTubeForBox> stockInFrozenTubeList;
    public Integer getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
    }

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

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getFrozenBoxColumns() {
        return frozenBoxColumns;
    }

    public void setFrozenBoxColumns(String frozenBoxColumns) {
        this.frozenBoxColumns = frozenBoxColumns;
    }

    public String getFrozenBoxRows() {
        return frozenBoxRows;
    }

    public void setFrozenBoxRows(String frozenBoxRows) {
        this.frozenBoxRows = frozenBoxRows;
    }

    public SampleTypeDTO getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleTypeDTO sampleType) {
        this.sampleType = sampleType;
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

    public SupportRackDTO getShelf() {
        return shelf;
    }

    public void setShelf(SupportRackDTO shelf) {
        this.shelf = shelf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
