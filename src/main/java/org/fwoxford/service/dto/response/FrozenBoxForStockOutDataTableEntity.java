package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by gengluying on 2017/5/23.
 */
@Entity
@Table(name = "view_frozen_box_for_stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenBoxForStockOutDataTableEntity  extends StockOutFrozenBoxBaseDataTableEntity implements Serializable {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="frozen_box_code")
    private String frozenBoxCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="position")
    private String position;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="count_of_sample")
    private Long countOfSample;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_type_name")
    private String sampleTypeName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="status")
    private String status;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="memo")
    private String memo;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_classification_name")
    private String sampleClassificationName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="project_name")
    private String projectName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="project_code")
    private String projectCode;

    @Column(name ="stock_out_requirement_id")
    private Long stockOutRequirementId;

    @Column(name ="stock_out_plan_frozen_tube_id")
    private Long stockOutPlanFrozenTubeId;
    @Column(name ="equipment_code")
    private String equipmentCode;

    @Column(name ="area_code")
    private String areaCode;

    @Column(name ="support_rack_code")
    private String supportRackCode;

    @Column(name ="rows_in_shelf")
    private String rowsInShelf;

    @Column(name ="columns_in_shelf")
    private String columnsInShelf;

    public FrozenBoxForStockOutDataTableEntity() {
    }

    public FrozenBoxForStockOutDataTableEntity(Long id, String frozenBoxCode, String position, Long countOfSample, String sampleTypeName, String status, String memo, String sampleClassificationName, String projectName, String projectCode, Long stockOutRequirementId, Long stockOutPlanFrozenTubeId, String equipmentCode, String areaCode, String supportRackCode, String rowsInShelf, String columnsInShelf) {
        this.id = id;
        this.frozenBoxCode = frozenBoxCode;
        this.position = position;
        this.countOfSample = countOfSample;
        this.sampleTypeName = sampleTypeName;
        this.status = status;
        this.memo = memo;
        this.sampleClassificationName = sampleClassificationName;
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.stockOutRequirementId = stockOutRequirementId;
        this.stockOutPlanFrozenTubeId = stockOutPlanFrozenTubeId;
        this.equipmentCode = equipmentCode;
        this.areaCode = areaCode;
        this.supportRackCode = supportRackCode;
        this.rowsInShelf = rowsInShelf;
        this.columnsInShelf = columnsInShelf;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Long getStockOutRequirementId() {
        return stockOutRequirementId;
    }

    public void setStockOutRequirementId(Long stockOutRequirementId) {
        this.stockOutRequirementId = stockOutRequirementId;
    }

    public Long getStockOutPlanFrozenTubeId() {
        return stockOutPlanFrozenTubeId;
    }

    public void setStockOutPlanFrozenTubeId(Long stockOutPlanFrozenTubeId) {
        this.stockOutPlanFrozenTubeId = stockOutPlanFrozenTubeId;
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
}
