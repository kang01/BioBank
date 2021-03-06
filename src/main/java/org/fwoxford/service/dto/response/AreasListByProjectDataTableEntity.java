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

/**
 * Created by gengluying on 2017/7/13.
 */
@Entity
@Table(name = "view_areas_list_by_project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AreasListByProjectDataTableEntity {
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
    @Column(name ="status")
    private String status;

    @Column(name ="equipment_type_id")
    private Long equipmentTypeId;

    @Column(name ="equipment_id")
    @JsonView(DataTablesOutput.View.class)
    private Long equipmentId;

    @Column(name ="position")
    @JsonView(DataTablesOutput.View.class)
    private String position;

    @Column(name ="freeze_frame_number")
    @JsonView(DataTablesOutput.View.class)
    private String freezeFrameNumber;

    @Column(name ="count_of_rest")
    @JsonView(DataTablesOutput.View.class)
    private Integer countOfRest;

    @Column(name ="count_of_used")
    @JsonView(DataTablesOutput.View.class)
    private Integer countOfUsed;

    @Column(name ="count_of_shelves")
    @JsonView(DataTablesOutput.View.class)
    private Integer countOfShelves;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFreezeFrameNumber() {
        return freezeFrameNumber;
    }

    public void setFreezeFrameNumber(String freezeFrameNumber) {
        this.freezeFrameNumber = freezeFrameNumber;
    }

    public Integer getCountOfRest() {
        return countOfRest;
    }

    public void setCountOfRest(Integer countOfRest) {
        this.countOfRest = countOfRest;
    }

    public Integer getCountOfUsed() {
        return countOfUsed;
    }

    public void setCountOfUsed(Integer countOfUsed) {
        this.countOfUsed = countOfUsed;
    }

    public Integer getCountOfShelves() {
        return countOfShelves;
    }

    public void setCountOfShelves(Integer countOfShelves) {
        this.countOfShelves = countOfShelves;
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
