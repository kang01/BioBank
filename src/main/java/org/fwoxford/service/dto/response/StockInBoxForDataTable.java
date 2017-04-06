package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.service.dto.SampleTypeDTO;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by zhuyu on 2017/3/31.
 */
public class StockInBoxForDataTable {
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Integer countOfSample;

    @NotNull
    @Size(max = 20)
    @JsonView(DataTablesOutput.View.class)
    private String status;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String frozenBoxCode;
    @JsonView(DataTablesOutput.View.class)
    private String sampleTypeName;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String position;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Integer isSplit;

    @JsonView(DataTablesOutput.View.class)
    private SampleTypeDTO sampleType;
    @JsonView(DataTablesOutput.View.class)
    private Integer frozenBoxRows;
    @JsonView(DataTablesOutput.View.class)
    private Integer frozenBoxColumns;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockInBoxForDataTable response = (StockInBoxForDataTable) o;

        if ( ! Objects.equals(getId(), response.getId())) { return false; }

        return true;
    }

    @Override
    public String toString() {
        return "StockInBoxForDataTable{" +
            "id=" + id +
            ", countOfSample=" + countOfSample +
            ", status='" + status + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", sampleTypeName='" + sampleTypeName + '\'' +
            ", position='" + position + '\'' +
            ", isSplit=" + isSplit +
            ", sampleType=" + sampleType +
            ", frozenBoxRows=" + frozenBoxRows +
            ", frozenBoxColumns=" + frozenBoxColumns +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
    }

    public SampleTypeDTO getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleTypeDTO sampleType) {
        this.sampleType = sampleType;
    }

    public Integer getFrozenBoxRows() {
        return frozenBoxRows;
    }

    public void setFrozenBoxRows(Integer frozenBoxRows) {
        this.frozenBoxRows = frozenBoxRows;
    }

    public Integer getFrozenBoxColumns() {
        return frozenBoxColumns;
    }

    public void setFrozenBoxColumns(Integer frozenBoxColumns) {
        this.frozenBoxColumns = frozenBoxColumns;
    }
}
