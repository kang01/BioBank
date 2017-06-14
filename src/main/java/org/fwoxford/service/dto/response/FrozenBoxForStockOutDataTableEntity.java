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
@Table(name = "view_frozen_box_for_stock_out")
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
}
