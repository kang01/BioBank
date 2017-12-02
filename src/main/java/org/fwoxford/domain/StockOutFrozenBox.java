package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StockOutFrozenBox.
 */
@Entity
@Table(name = "stock_out_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutFrozenBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_box")
    @SequenceGenerator(name = "seq_stock_out_box",sequenceName = "seq_stock_out_box",allocationSize = 1,initialValue = 1)
    private Long id;

    @Column(name = "equipment_code")
    private String equipmentCode;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "support_rack_code")
    private String supportRackCode;

    @Column(name = "rows_in_shelf")
    private String rowsInShelf;

    @Column(name = "columns_in_shelf")
    private String columnsInShelf;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;

    @ManyToOne
    private Equipment equipment;

    @ManyToOne
    private Area area;

    @ManyToOne
    private SupportRack supportRack;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutTask stockOutTask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }
    public StockOutFrozenBox equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }
    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }
    public StockOutFrozenBox areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }
    public StockOutFrozenBox supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }
    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }
    public StockOutFrozenBox rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }
    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }
    public StockOutFrozenBox columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }
    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getStatus() {
        return status;
    }

    public StockOutFrozenBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutFrozenBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public StockOutFrozenBox frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public Equipment getEquipment() {
        return equipment;
    }
    public StockOutFrozenBox equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Area getArea() {
        return area;
    }
    public StockOutFrozenBox area(Area area) {
        this.area = area;
        return this;
    }
    public void setArea(Area area) {
        this.area = area;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }
    public StockOutFrozenBox supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }
    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public StockOutTask getStockOutTask() {
        return stockOutTask;
    }

    public StockOutFrozenBox stockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
        return this;
    }

    public void setStockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutFrozenBox stockOutFrozenBox = (StockOutFrozenBox) o;
        if (stockOutFrozenBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutFrozenBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutFrozenBox{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", frozenBox=" + frozenBox +
            ", equipment=" + equipment +
            ", area=" + area +
            ", supportRack=" + supportRack +
            ", stockOutTask=" + stockOutTask +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
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
            ", frozenBoxType=" + frozenBoxType +
            ", sampleType=" + sampleType +
            ", sampleClassification=" + sampleClassification +
            ", project=" + project +
            ", projectSite=" + projectSite +
            '}';
    }

    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;
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
    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public StockOutFrozenBox frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }
    public StockOutFrozenBox frozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
        return this;
    }
    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }
    public StockOutFrozenBox frozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
        return this;
    }
    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }
    public StockOutFrozenBox frozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
        return this;
    }
    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
    }

    public String getProjectCode() {
        return projectCode;
    }
    public StockOutFrozenBox projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }
    public StockOutFrozenBox projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }
    public StockOutFrozenBox projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }
    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }
    public StockOutFrozenBox projectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
        return this;
    }
    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }
    public StockOutFrozenBox sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }
    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }
    public StockOutFrozenBox sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }
    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }
    public StockOutFrozenBox sampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }
    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }
    public StockOutFrozenBox sampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }
    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public Integer getIsSplit() {
        return isSplit;
    }
    public StockOutFrozenBox isSplit(Integer isSplit) {
        this.isSplit = isSplit;
        return this;
    }
    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
    }

    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }
    public StockOutFrozenBox emptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
        return this;
    }
    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }

    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }
    public StockOutFrozenBox emptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
        return this;
    }
    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }

    public Integer getDislocationNumber() {
        return dislocationNumber;
    }
    public StockOutFrozenBox dislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
        return this;
    }
    public void setDislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
    }

    public Integer getIsRealData() {
        return isRealData;
    }
    public StockOutFrozenBox isRealData(Integer isRealData) {
        this.isRealData = isRealData;
        return this;
    }
    public void setIsRealData(Integer isRealData) {
        this.isRealData = isRealData;
    }
    public FrozenBoxType getFrozenBoxType() {
        return frozenBoxType;
    }
    public StockOutFrozenBox frozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
        return this;
    }
    public void setFrozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }
    public StockOutFrozenBox sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }
    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }
    public StockOutFrozenBox sampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }
    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public Project getProject() {
        return project;
    }
    public StockOutFrozenBox project(Project project) {
        this.project = project;
        return this;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }
    public StockOutFrozenBox projectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
        return this;
    }
    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }
    @Size(max = 100)
    @Column(name = "frozen_box_code_1d", length = 100)
    private String frozenBoxCode1D;

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }
    public StockOutFrozenBox frozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
        return this;
    }
    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
    }

    /**
     * 交接时间
     */
    @Column(name = "handover_time")
    private LocalDate handoverTime;
    /**
     * 交接样本量
     */
    @Column(name = "count_of_sample")
    private Integer countOfSample;

    public LocalDate getHandoverTime() {
        return handoverTime;
    }

    public StockOutFrozenBox handoverTime(LocalDate handoverTime){
        this.handoverTime = handoverTime;
        return this;
    }
    public StockOutFrozenBox setHandoverTime(LocalDate handoverTime) {
        this.handoverTime = handoverTime;
        return this;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }
    public StockOutFrozenBox countOfSample(Integer countOfSample){
        this.countOfSample = countOfSample;
        return this;
    }
    public StockOutFrozenBox setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }
}
