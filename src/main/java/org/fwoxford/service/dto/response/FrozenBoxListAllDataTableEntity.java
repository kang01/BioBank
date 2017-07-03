package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by gengluying on 2017/6/22.
 */
@Entity
@Table(name = "view_frozen_box_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenBoxListAllDataTableEntity  implements Serializable {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="frozen_box_code")
    private String frozenBoxCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="equipment_code")
    private String equipmentCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="area_code")
    private String areaCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="shelves_code")
    private String shelvesCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="rows_in_shelf")
    private String rowsInShelf;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="columns_in_shelf")
    private String columnsInShelf;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="position")
    private String position;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_type")
    private String sampleType;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_classification")
    private String sampleClassification;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="frozen_box_type")
    private String frozenBoxType;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="count_of_used")
    private Long countOfUsed;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="count_of_rest")
    private Long countOfRest;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="status")
    private String status;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="project_name")
    private String projectName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name ="project_code")
    private String projectCode;

    @Column(name ="equipment_id")
    private Long equipmentId;

    @Column(name ="area_id")
    private Long areaId;

    @Column(name ="shelves_id")
    private Long shelvesId;


    @Column(name ="sample_type_id")
    private Long sampleTypeId;


    @Column(name ="sample_classification_id")
    private Long sampleClassificationId;


    @Column(name ="frozen_box_type_id")
    private Long frozenBoxTypeId;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="memo")
    private String memo;
    public FrozenBoxListAllDataTableEntity() {
    }

    public FrozenBoxListAllDataTableEntity(Long id, String frozenBoxCode, String equipmentCode, String areaCode, String shelvesCode, String rowsInShelf, String columnsInShelf, String position, String sampleType, String sampleClassification, String frozenBoxType, Long countOfUsed, Long countOfRest, String status, String projectName, String projectCode, Long equipmentId, Long areaId, Long shelvesId, Long sampleTypeId, Long sampleClassificationId, Long frozenBoxTypeId, String memo) {
        this.id = id;
        this.frozenBoxCode = frozenBoxCode;
        this.equipmentCode = equipmentCode;
        this.areaCode = areaCode;
        this.shelvesCode = shelvesCode;
        this.rowsInShelf = rowsInShelf;
        this.columnsInShelf = columnsInShelf;
        this.position = position;
        this.sampleType = sampleType;
        this.sampleClassification = sampleClassification;
        this.frozenBoxType = frozenBoxType;
        this.countOfUsed = countOfUsed;
        this.countOfRest = countOfRest;
        this.status = status;
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.equipmentId = equipmentId;
        this.areaId = areaId;
        this.shelvesId = shelvesId;
        this.sampleTypeId = sampleTypeId;
        this.sampleClassificationId = sampleClassificationId;
        this.frozenBoxTypeId = frozenBoxTypeId;
        this.memo = memo;
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

    public String getShelvesCode() {
        return shelvesCode;
    }

    public void setShelvesCode(String shelvesCode) {
        this.shelvesCode = shelvesCode;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSampleClassification() {
        return sampleClassification;
    }

    public void setSampleClassification(String sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public String getFrozenBoxType() {
        return frozenBoxType;
    }

    public void setFrozenBoxType(String frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public Long getCountOfUsed() {
        return countOfUsed;
    }

    public void setCountOfUsed(Long countOfUsed) {
        this.countOfUsed = countOfUsed;
    }

    public Long getCountOfRest() {
        return countOfRest;
    }

    public void setCountOfRest(Long countOfRest) {
        this.countOfRest = countOfRest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(Long shelvesId) {
        this.shelvesId = shelvesId;
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

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
