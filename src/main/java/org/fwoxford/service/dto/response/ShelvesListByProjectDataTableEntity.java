package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.domain.FrozenBox;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2017/7/13.
 */
@Entity
@Table(name = "view_shelves_list_by_project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShelvesListByProjectDataTableEntity {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="equipment_type")
    private String equipmentType;

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
    @Column(name ="shelves_type")
    private String shelvesType;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="count_of_used")
    private Long countOfUsed;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="count_of_rest")
    private Long countOfRest;

    @JsonView(DataTablesOutput.View.class)
    @Column(name ="status")
    private String status;

    @Column(name ="equipment_type_id")
    private Long equipmentTypeId;

    @Column(name ="equipment_id")
    @JsonView(DataTablesOutput.View.class)
    private Long equipmentId;

    @Column(name ="area_id")
    @JsonView(DataTablesOutput.View.class)
    private Long areaId;

    @Column(name ="shelves_id")
    @JsonView(DataTablesOutput.View.class)
    private Long shelvesId;

    @Column(name ="shelves_type_id")
    private Long shelvesTypeId;

    @Column(name ="position")
    @JsonView(DataTablesOutput.View.class)
    private String position;

    @Column(name ="memo")
    @JsonView(DataTablesOutput.View.class)
    private String memo;

    @Column(name ="project_id")
    private String projectId;

    @Column(name ="project_code")
    private String projectCode;

    @Column(name ="project_name")
    private String projectName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
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

    public String getShelvesType() {
        return shelvesType;
    }

    public void setShelvesType(String shelvesType) {
        this.shelvesType = shelvesType;
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

    public Long getEquipmentTypeId() {
        return equipmentTypeId;
    }

    public void setEquipmentTypeId(Long equipmentTypeId) {
        this.equipmentTypeId = equipmentTypeId;
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

    public Long getShelvesTypeId() {
        return shelvesTypeId;
    }

    public void setShelvesTypeId(Long shelvesTypeId) {
        this.shelvesTypeId = shelvesTypeId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
}
