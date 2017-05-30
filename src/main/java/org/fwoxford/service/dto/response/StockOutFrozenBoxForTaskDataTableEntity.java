package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutFrozenBoxForTaskDataTableEntity {
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String frozenBoxCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String position;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long countOfSample;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String sampleTypeName;

    private Long countOfSampleAll;

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

    public Long getCountOfSampleAll() {
        return countOfSampleAll;
    }

    public void setCountOfSampleAll(Long countOfSampleAll) {
        this.countOfSampleAll = countOfSampleAll;
    }
}
