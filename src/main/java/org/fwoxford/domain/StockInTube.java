package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockInTube.
 */
@Entity
@Table(name = "stock_in_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockInTube extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_in_tube")
    @SequenceGenerator(name = "seq_stock_in_tube",sequenceName = "seq_stock_in_tube",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    /**
     * 项目编码
     */
    @Size(max = 100)
    @Column(name = "project_code", length = 100)
    private String projectCode;

    /**
     * 项目点编码
     */
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100)
    private String projectSiteCode;
    /**
     * 冻存管编码
     */
    @Size(max = 100)
    @Column(name = "frozen_tube_code", length = 100)
    private String frozenTubeCode;
    /**
     * 样本临时编码
     */
    @Size(max = 100)
    @Column(name = "sample_temp_code", length = 100)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @Size(max = 100)
    @Column(name = "sample_code", length = 100)
    private String sampleCode;
    /**
     * 冻存管类型编码
     */
    @Size(max = 100)
    @Column(name = "frozen_tube_type_code", length = 100)
    private String frozenTubeTypeCode;
    /**
     * 冻存管类型名称
     */
    @Size(max = 255)
    @Column(name = "frozen_tube_type_name", length = 255)
    private String frozenTubeTypeName;
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
     * 样本最多使用次数
     */
    @Max(value = 20)
    @Column(name = "sample_used_times_most")
    private Integer sampleUsedTimesMost;
    /**
     * 样本已使用次数
     */
    @Max(value = 20)
    @Column(name = "sample_used_times")
    private Integer sampleUsedTimes;
    /**
     * 冻存管容量值
     */

    @Column(name = "frozen_tube_volumns")
    private Double frozenTubeVolumns;

    /**
     * 样本容量值
     */
    @Column(name = "sample_volumns")
    private Double sampleVolumns;

    /**
     * 冻存管容量值单位
     */
    @Size(max = 20)
    @Column(name = "frozen_tube_volumns_unit", length = 20)
    private String frozenTubeVolumnsUnit;
    /**
     * 错误类型：6001：位置错误，6002：样本类型错误，6003：其他
     */
    @Size(max = 20)
    @Column(name = "error_type", length = 20)
    private String errorType;
    /**
     * 状态
     */
    @Size(max = 20)
    @Column(name = "frozen_tube_state", length = 20)
    private String frozenTubeState;
    @ManyToOne(optional = false)
    @NotNull
    private StockInBox stockInBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;
    /**
     * 冻存管类型
     */
    @ManyToOne(optional = false)
    private FrozenTubeType frozenTubeType;
    /**
     * 样本类型
     */
    @ManyToOne(optional = false)
    private SampleType sampleType;
    /**
     * 样本分类
     */
    @ManyToOne
    private SampleClassification sampleClassification;
    /**
     * 项目
     */
    @ManyToOne
    private Project project;
    /**
     * 项目点
     */
    @ManyToOne
    private ProjectSite projectSite;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTubeRows() {
        return tubeRows;
    }
    public StockInTube tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }
    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public StockInTube tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }
    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getStatus() {
        return status;
    }

    public StockInTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockInTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public StockInTube frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getProjectCode() {
        return projectCode;
    }
    public StockInTube projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }
    public StockInTube projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }
    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }
    public StockInTube frozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
        return this;
    }
    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }
    public StockInTube sampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }
    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }
    public StockInTube sampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }
    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }
    public StockInTube frozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }
    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }
    public StockInTube frozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }
    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }
    public StockInTube sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }
    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }
    public StockInTube sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }
    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }
    public StockInTube sampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }
    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }
    public StockInTube sampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }
    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }
    public StockInTube sampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }
    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }
    public StockInTube sampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
        return this;
    }
    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public Double getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }
    public StockInTube frozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
        return this;
    }
    public void setFrozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }

    public Double getSampleVolumns() {
        return sampleVolumns;
    }
    public StockInTube sampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
        return this;
    }
    public void setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
    }

    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }
    public StockInTube frozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
        return this;
    }
    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
    }

    public String getErrorType() {
        return errorType;
    }
    public StockInTube errorType(String errorType) {
        this.errorType = errorType;
        return this;
    }
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }
    public StockInTube frozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
        return this;
    }
    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
    }

    public StockInBox getStockInBox() {
        return stockInBox;
    }

    public StockInTube stockInBox(StockInBox stockInBox) {
        this.stockInBox = stockInBox;
        return this;
    }

    public void setStockInBox(StockInBox stockInBox) {
        this.stockInBox = stockInBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public StockInTube frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    public FrozenTubeType getFrozenTubeType() {
        return frozenTubeType;
    }
    public StockInTube frozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
        return this;
    }
    public void setFrozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }
    public StockInTube sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }
    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }
    public StockInTube sampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }
    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public Project getProject() {
        return project;
    }
    public StockInTube project(Project project) {
        this.project = project;
        return this;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }
    public StockInTube projectSite(ProjectSite projectSite) {
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
        StockInTube stockInTube = (StockInTube) o;
        if (stockInTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockInTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInTube{" +
            "id=" + id +
            ", tubeRows='" + tubeRows + '\'' +
            ", tubeColumns='" + tubeColumns + '\'' +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", frozenTubeCode='" + frozenTubeCode + '\'' +
            ", sampleTempCode='" + sampleTempCode + '\'' +
            ", sampleCode='" + sampleCode + '\'' +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + '\'' +
            ", frozenTubeTypeName='" + frozenTubeTypeName + '\'' +
            ", sampleTypeCode='" + sampleTypeCode + '\'' +
            ", sampleTypeName='" + sampleTypeName + '\'' +
            ", sampleClassificationCode='" + sampleClassificationCode + '\'' +
            ", sampleClassificationName='" + sampleClassificationName + '\'' +
            ", sampleUsedTimesMost=" + sampleUsedTimesMost +
            ", sampleUsedTimes=" + sampleUsedTimes +
            ", frozenTubeVolumns=" + frozenTubeVolumns +
            ", sampleVolumns=" + sampleVolumns +
            ", frozenTubeVolumnsUnit='" + frozenTubeVolumnsUnit + '\'' +
            ", errorType='" + errorType + '\'' +
            ", frozenTubeState='" + frozenTubeState + '\'' +
            ", stockInBox=" + stockInBox +
            ", frozenTube=" + frozenTube +
            ", frozenTubeType=" + frozenTubeType +
            ", sampleType=" + sampleType +
            ", sampleClassification=" + sampleClassification +
            ", project=" + project +
            ", projectSite=" + projectSite +
            '}';
    }
}
