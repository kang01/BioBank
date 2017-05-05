package org.fwoxford.domain;

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
import java.util.Objects;

@Entity
@Table(name = "view_stock_in_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockInBoxForDataTableEntity {

    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name="count_of_sample")
    private Integer countOfSample;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name="status")
    private String status;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name="frozen_box_code")
    private String frozenBoxCode;

    @Column(name="sample_type_name")
    @JsonView(DataTablesOutput.View.class)
    private String sampleTypeName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name="position")
    private String position;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name="is_split")
    private Integer isSplit;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="sample_classification_name")
    private String sampleClassificationName;

    @Column(name="stock_in_code")
    private String stockInCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockInBoxForDataTableEntity response = (StockInBoxForDataTableEntity) o;

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
            ", sampleClassificationName=" + sampleClassificationName +
            ", stockInCode=" + stockInCode +
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

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }
}
