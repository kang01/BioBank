package org.fwoxford.service.dto;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutRequiredSample entity.
 */
public class StockOutRequiredSampleDTO extends AbstractAuditingDTO implements Serializable {

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @JsonView(DataTablesOutput.View.class)
    @NotNull
    @Size(max = 100)
    private String sampleCode;

    @JsonView(DataTablesOutput.View.class)
    @NotNull
    @Size(max = 100)
    private String sampleType;

    @NotNull
    @Size(max = 20)
    @JsonView(DataTablesOutput.View.class)
    private String status;

    @Size(max = 1024)
    @JsonView(DataTablesOutput.View.class)
    private String memo;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long stockOutRequirementId;

    public StockOutRequiredSampleDTO() {
    }

    public StockOutRequiredSampleDTO(Long id, String sampleCode, String sampleType, String status, String memo, Long stockOutRequirementId) {
        this.id = id;
        this.sampleCode = sampleCode;
        this.sampleType = sampleType;
        this.status = status;
        this.memo = memo;
        this.stockOutRequirementId = stockOutRequirementId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
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

    public Long getStockOutRequirementId() {
        return stockOutRequirementId;
    }

    public void setStockOutRequirementId(Long stockOutRequirementId) {
        this.stockOutRequirementId = stockOutRequirementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = (StockOutRequiredSampleDTO) o;

        if ( ! Objects.equals(id, stockOutRequiredSampleDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutRequiredSampleDTO{" +
            "id=" + id +
            ", sampleCode='" + sampleCode + "'" +
            ", sampleType='" + sampleType + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
