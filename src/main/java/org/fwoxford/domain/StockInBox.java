package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockInBox.
 */
@Entity
@Table(name = "stock_in_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockInBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_in_box")
    @SequenceGenerator(name = "seq_stock_in_box",sequenceName = "seq_stock_in_box",allocationSize = 1,initialValue = 1)
    private Long id;
    /**
     * 设备编码
     */
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @Size(max = 100)
    @Column(name = "area_code", length = 100, nullable = false)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100, nullable = false)
    private String supportRackCode;
    /**
     * 所在冻存架行数
     */
    @Size(max = 20)
    @Column(name = "rows_in_shelf", length = 20, nullable = false)
    private String rowsInShelf;
    /**
     * 所在冻存架列数
     */
    @Size(max = 20)
    @Column(name = "columns_in_shelf", length = 20, nullable = false)
    private String columnsInShelf;

    /**
     * 样本数量
     */
    @NotNull
    @Column(name = "count_of_sample", nullable = false)
    private Integer countOfSample;
    /**
     * 冻存盒类型编码
     */
    @Size(max = 100)
    @Column(name = "frozen_box_type_code", length = 100)
    private String frozenBoxTypeCode;
    /**
     * 冻存盒行数
     */
    @Size(max = 20)
    @Column(name = "frozen_box_rows", length = 20)
    private String frozenBoxTypeRows;
    /**
     * 冻存盒列数
     */
    @Size(max = 20)
    @Column(name = "frozen_box_columns", length = 20)
    private String frozenBoxTypeColumns;
    /**
     * 项目编码
     */
    @Size(max = 100)
    @Column(name = "project_code", length = 100)
    private String projectCode;
    /**
     * 项目名称
     */
    @Size(max = 255)
    @Column(name = "project_name", length = 255)
    private String projectName;
    /**
     * 项目点编码
     */
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100)
    private String projectSiteCode;
    /**
     * 项目点名称
     */
    @Size(max = 255)
    @Column(name = "project_site_name", length = 255)
    private String projectSiteName;
    /**
     * 样本类型编码
     */
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255)
    private String sampleTypeName;
    /**
     * 样本分类编码
     */
    @Size(max = 100)
    @Column(name = "sample_classification_code", length = 100)
    private String sampleClassificationCode;
    /**
     * 样本分类名称
     */
    @Size(max = 255)
    @Column(name = "sample_classification_name", length = 255)
    private String sampleClassificationName;
    /**
     * 是否分装：1：是，0：否
     */
    @Column(name = "is_split", nullable = false)
    private Integer isSplit;
    /**
     * 空管数
     */
    @Column(name = "empty_tube_number")
    private Integer emptyTubeNumber;
    /**
     * 空孔数
     */
    @Column(name = "empty_hole_number")
    private Integer emptyHoleNumber;
    /**
     * 错位数
     */
    @Column(name = "dislocation_number")
    private Integer dislocationNumber;
    /**
     * 是否已导入样本数据：1：是，0：否
     */
    @Column(name = "is_real_data")
    private Integer isRealData;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    /**
     * 冻存盒
     */
    @ManyToOne(optional = false)
    private FrozenBox frozenBox;

    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    /**
     * 入库编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "stock_in_code", length = 100, nullable = false)
    private String stockInCode;

    /**
     * 入库
     */
    @ManyToOne(optional = false)
    @NotNull
    private StockIn stockIn;
    /**
     * 设备
     */
    @ManyToOne
    private Equipment equipment;
    /**
     * 冻存架
     */
    @ManyToOne
    private SupportRack supportRack;
    /**
     * 区域
     */
    @ManyToOne
    private Area area;
    @ManyToOne
    private FrozenBoxType frozenBoxType;

    @ManyToOne
    private SampleType sampleType;

    @ManyToOne
    private SampleClassification sampleClassification;

    @ManyToOne
    private Project project;

    @ManyToOne
    private ProjectSite projectSite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public StockInBox equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public StockInBox areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public StockInBox supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public StockInBox rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public StockInBox columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }
    public StockInBox frozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
        return this;
    }
    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }
    public StockInBox frozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
        return this;
    }
    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }
    public StockInBox frozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
        return this;
    }
    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
    }

    public String getProjectCode() {
        return projectCode;
    }
    public StockInBox projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }
    public StockInBox projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }
    public StockInBox projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }
    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }
    public StockInBox projectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
        return this;
    }
    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }
    public StockInBox sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }
    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }
    public StockInBox sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }
    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }
    public StockInBox sampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }
    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }
    public StockInBox sampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }
    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public Integer getIsSplit() {
        return isSplit;
    }
    public StockInBox isSplit(Integer isSplit) {
        this.isSplit = isSplit;
        return this;
    }
    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
    }

    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }
    public StockInBox emptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
        return this;
    }
    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }

    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }
    public StockInBox emptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
        return this;
    }
    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }

    public Integer getDislocationNumber() {
        return dislocationNumber;
    }
    public StockInBox dislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
        return this;
    }
    public void setDislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
    }

    public Integer getIsRealData() {
        return isRealData;
    }
    public StockInBox isRealData(Integer isRealData) {
        this.isRealData = isRealData;
        return this;
    }
    public void setIsRealData(Integer isRealData) {
        this.isRealData = isRealData;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public StockInBox stockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
        return this;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public String getMemo() {
        return memo;
    }

    public StockInBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public StockInBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }
    public StockInBox frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }
    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public StockInBox frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }
    public StockInBox countOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }
    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }
    public StockIn getStockIn() {
        return stockIn;
    }

    public StockInBox stockIn(StockIn stockIn) {
        this.stockIn = stockIn;
        return this;
    }

    public void setStockIn(StockIn stockIn) {
        this.stockIn = stockIn;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public StockInBox equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }

    public StockInBox supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }

    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public Area getArea() {
        return area;
    }

    public StockInBox area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public FrozenBoxType getFrozenBoxType() {
        return frozenBoxType;
    }
    public StockInBox frozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
        return this;
    }
    public void setFrozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }
    public StockInBox sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }
    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }
    public StockInBox sampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }
    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public Project getProject() {
        return project;
    }
    public StockInBox project(Project project) {
        this.project = project;
        return this;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }
    public StockInBox projectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
        return this;
    }
    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockInBox stockInBox = (StockInBox) o;
        if (stockInBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockInBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInBox{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", countOfSample=" + countOfSample +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + '\'' +
            ", frozenBoxTypeRows='" + frozenBoxTypeRows + '\'' +
            ", frozenBoxTypeColumns='" + frozenBoxTypeColumns + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectName='" + projectName + '\'' +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", projectSiteName='" + projectSiteName + '\'' +
            ", sampleTypeCode='" + sampleTypeCode + '\'' +
            ", sampleTypeName='" + sampleTypeName + '\'' +
            ", sampleClassificationCode='" + sampleClassificationCode + '\'' +
            ", sampleClassificationName='" + sampleClassificationName + '\'' +
            ", isSplit=" + isSplit +
            ", emptyTubeNumber=" + emptyTubeNumber +
            ", emptyHoleNumber=" + emptyHoleNumber +
            ", dislocationNumber=" + dislocationNumber +
            ", isRealData=" + isRealData +
            ", memo='" + memo + '\'' +
            ", status='" + status + '\'' +
            ", frozenBox=" + frozenBox +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", stockInCode='" + stockInCode + '\'' +
            ", stockIn=" + stockIn +
            ", equipment=" + equipment +
            ", supportRack=" + supportRack +
            ", area=" + area +
            ", frozenBoxType=" + frozenBoxType +
            ", sampleType=" + sampleType +
            ", sampleClassification=" + sampleClassification +
            ", project=" + project +
            ", projectSite=" + projectSite +
            '}';
    }
}
