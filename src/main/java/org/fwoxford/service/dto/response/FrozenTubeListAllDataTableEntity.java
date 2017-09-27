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
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by gengluying on 2017/6/22.
 */
@Entity
@Table(name = "view_sample_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenTubeListAllDataTableEntity implements Serializable {
    @Id
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="position")
    private String position;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="frozen_box_code")
    private String frozenBoxCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_code")
    private String sampleCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="project_code")
    private String projectCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="project_name")
    private String projectName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_type")
    private String sampleType;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="sample_classification")
    private String sampleClassification;

    @Column(name ="sex")
    @JsonView(DataTablesOutput.View.class)
    private String sex;

    @Column(name = "age")
    @JsonView(DataTablesOutput.View.class)
    private Integer age;

    @Column(name = "disease_type")
    @JsonView(DataTablesOutput.View.class)
    private String diseaseType;

    @Column(name = "is_hemolysis")
    @JsonView(DataTablesOutput.View.class)
    private Boolean isHemolysis;

    @Column(name = "is_blood_lipid")
    @JsonView(DataTablesOutput.View.class)
    private Boolean isBloodLipid;

    @Column(name = "sample_used_times")
    private Long sampleUsedTimes;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="status")
    private String status;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="frozen_tube_state")
    private String frozenTubeState;

    @Column(name ="equipment_code")
    private String equipmentCode;

    @Column(name ="area_code")
    private String areaCode;

    @Column(name ="shelves_code")
    private String shelvesCode;

    @Column(name ="rows_in_shelf")
    private String rowsInShelf;

    @Column(name ="columns_in_shelf")
    private String columnsInShelf;

    @Column(name = "tube_rows")
    private String tubeRows;

    @Column(name = "tube_columns")
    private String tubeColumns;

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


    @JsonView(DataTablesOutput.View.class)
    @Column(name ="position_in_box")
    private String positionInBox;

    @Column(name = "sample_temp_code", length = 100)
    private String sampleTempCode;

    public FrozenTubeListAllDataTableEntity() {
    }

    public FrozenTubeListAllDataTableEntity(Long id, String position, String frozenBoxCode, String sampleCode, String projectCode, String projectName, String sampleType, String sampleClassification, String sex, Integer age, String diseaseType, Boolean isHemolysis, Boolean isBloodLipid, Long sampleUsedTimes, String status, String frozenTubeState, String equipmentCode, String areaCode, String shelvesCode, String rowsInShelf, String columnsInShelf, String tubeRows, String tubeColumns, Long equipmentId, Long areaId, Long shelvesId, Long sampleTypeId, Long sampleClassificationId, String positionInBox, String sampleTempCode) {
        this.id = id;
        this.position = position;
        this.frozenBoxCode = frozenBoxCode;
        this.sampleCode = sampleCode;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.sampleType = sampleType;
        this.sampleClassification = sampleClassification;
        this.sex = sex;
        this.age = age;
        this.diseaseType = diseaseType;
        this.isHemolysis = isHemolysis;
        this.isBloodLipid = isBloodLipid;
        this.sampleUsedTimes = sampleUsedTimes;
        this.status = status;
        this.frozenTubeState = frozenTubeState;
        this.equipmentCode = equipmentCode;
        this.areaCode = areaCode;
        this.shelvesCode = shelvesCode;
        this.rowsInShelf = rowsInShelf;
        this.columnsInShelf = columnsInShelf;
        this.tubeRows = tubeRows;
        this.tubeColumns = tubeColumns;
        this.equipmentId = equipmentId;
        this.areaId = areaId;
        this.shelvesId = shelvesId;
        this.sampleTypeId = sampleTypeId;
        this.sampleClassificationId = sampleClassificationId;
        this.positionInBox = positionInBox;
        this.sampleTempCode = sampleTempCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public Boolean getHemolysis() {
        return isHemolysis;
    }

    public void setHemolysis(Boolean hemolysis) {
        this.isHemolysis = hemolysis;
    }

    public Boolean getBloodLipid() {
        return isBloodLipid;
    }

    public void setBloodLipid(Boolean bloodLipid) {
        this.isBloodLipid = bloodLipid;
    }

    public Long getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public void setSampleUsedTimes(Long sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }

    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
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

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
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

    public String getPositionInBox() {
        return positionInBox;
    }

    public void setPositionInBox(String positionInBox) {
        this.positionInBox = positionInBox;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }
}
