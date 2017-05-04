package org.fwoxford.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by gengluying on 2017/5/4.
 */
public class FrozenBoxForSaveBatchDTO {
    private Long id;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 样本数量
     */
    @NotNull
    private Integer sampleNumber;
    /**
     * 是否分装：1：是，0：否
     */
    @NotNull
    private Integer isSplit;
    /**
     * 所在架子行数
     */
    @NotNull
    @Size(max = 20)
    private String rowsInShelf;
    /**
     * 所在架子列数
     */
    @NotNull
    @Size(max = 20)
    private String columnsInShelf;
    /**
     * 备注
     */
    @Size(max = 255)
    private String memo;
    /**
     * 状态：2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 空管数
     */
    @NotNull
    private Integer emptyTubeNumber;
    /**
     * 空孔数
     */
    @NotNull
    private Integer emptyHoleNumber;
    /**
     * 错位数
     */
    @NotNull
    @Max(value = 100)
    private Integer dislocationNumber;
    /**
     * 是否已导入样本数据：1：是，0：否
     */
    @NotNull
    private Integer isRealData;
    /**
     * 冻存盒类型ID
     */
    private Long frozenBoxTypeId;
    /**
     * 样本类型ID
     */
    private Long sampleTypeId;
    /**
     *样本分类ID
     */
    private Long sampleClassificationId;
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
    private List<FrozenTubeForSaveBatchDTO> frozenTubeDTOS;

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

    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public Integer getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
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

    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }

    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }

    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }

    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }

    public Integer getDislocationNumber() {
        return dislocationNumber;
    }

    public void setDislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
    }

    public Integer getIsRealData() {
        return isRealData;
    }

    public void setIsRealData(Integer isRealData) {
        this.isRealData = isRealData;
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

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
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

    public List<FrozenTubeForSaveBatchDTO> getFrozenTubeDTOS() {
        return frozenTubeDTOS;
    }

    public void setFrozenTubeDTOS(List<FrozenTubeForSaveBatchDTO> frozenTubeDTOS) {
        this.frozenTubeDTOS = frozenTubeDTOS;
    }
}
