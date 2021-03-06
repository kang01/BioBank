package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.models.auth.In;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "view_stock_in_box_list")
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

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name="frozen_box_code_1d")
    private String frozenBoxCode1D;

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

    @Column(name="sample_type_code")
    @JsonView(DataTablesOutput.View.class)
    private String sampleTypeCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="sample_classification_code")
    private String sampleClassificationCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="tranship_code")
    private String transhipCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="project_site_code")
    private String projectSiteCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="order_no")
    private String orderNO;

    @Column(name="frozen_box_id")
    private Long frozenBoxId;

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
        return "StockInBoxForDataTableEntity{" +
            "id=" + id +
            ", countOfSample=" + countOfSample +
            ", status='" + status + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", sampleTypeName='" + sampleTypeName + '\'' +
            ", position='" + position + '\'' +
            ", isSplit=" + isSplit +
            ", sampleClassificationName='" + sampleClassificationName + '\'' +
            ", stockInCode='" + stockInCode + '\'' +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", sampleTypeCode='" + sampleTypeCode + '\'' +
            ", sampleClassificationCode='" + sampleClassificationCode + '\'' +
            ", transhipCode='" + transhipCode + '\'' +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public StockInBoxForDataTableEntity() {
    }

    public StockInBoxForDataTableEntity(Long id, Integer countOfSample, String status, String frozenBoxCode, String frozenBoxCode1D, String sampleTypeName, String position, Integer isSplit, String sampleClassificationName, String stockInCode,
                                        String equipmentCode, String areaCode, String supportRackCode, String rowsInShelf, String columnsInShelf, String sampleTypeCode, String sampleClassificationCode,
                                        String transhipCode, String projectSiteCode,String orderNO,Long frozenBoxId) {
        this.id = id;
        this.countOfSample = countOfSample;
        this.status = status;
        this.frozenBoxCode = frozenBoxCode;
        this.frozenBoxCode1D = frozenBoxCode1D;
        this.sampleTypeName = sampleTypeName;
        this.position = position;
        this.isSplit = isSplit;
        this.sampleClassificationName = sampleClassificationName;
        this.stockInCode = stockInCode;
        this.equipmentCode = equipmentCode;
        this.areaCode = areaCode;
        this.supportRackCode = supportRackCode;
        this.rowsInShelf = rowsInShelf;
        this.columnsInShelf = columnsInShelf;
        this.sampleTypeCode = sampleTypeCode;
        this.sampleClassificationCode = sampleClassificationCode;
        this.transhipCode = transhipCode;
        this.projectSiteCode = projectSiteCode;
        this.orderNO = orderNO;
        this.frozenBoxId = frozenBoxId;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
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

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
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

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }
}
