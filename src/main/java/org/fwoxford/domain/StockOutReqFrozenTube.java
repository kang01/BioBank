package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutReqFrozenTube.
 */
@Entity
@Table(name = "stock_out_req_frozen_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutReqFrozenTube  extends FrozenTubeLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_req_frozen_tube")
    @SequenceGenerator(name = "seq_stock_out_req_frozen_tube", sequenceName = "seq_stock_out_req_frozen_tube", allocationSize = 1, initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;

    @Column(name = "importing_sample_id")
    private Long importingSampleId;

    @Column(name = "repeal_reason")
    private String repealReason;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutRequirement stockOutRequirement;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public StockOutFrozenBox getStockOutFrozenBox() {
        return stockOutFrozenBox;
    }

    public StockOutReqFrozenTube stockOutFrozenBox(StockOutFrozenBox stockOutFrozenBox) {
        this.stockOutFrozenBox = stockOutFrozenBox;
        return this;
    }
    public void setStockOutFrozenBox(StockOutFrozenBox stockOutFrozenBox) {
        this.stockOutFrozenBox = stockOutFrozenBox;
    }

    public StockOutTask getStockOutTask() {
        return stockOutTask;
    }

    public StockOutReqFrozenTube stockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
        return this;
    }
    public void setStockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }
    public StockOutReqFrozenTube frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }
    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public StockOutReqFrozenTube projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }
    public StockOutReqFrozenTube projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }
    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }
    public StockOutReqFrozenTube frozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
        return this;
    }
    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }
    public StockOutReqFrozenTube sampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }
    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }
    public StockOutReqFrozenTube sampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }
    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }
    public StockOutReqFrozenTube frozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }
    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }
    public StockOutReqFrozenTube frozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }
    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }
    public StockOutReqFrozenTube sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }
    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }
    public StockOutReqFrozenTube sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }
    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }
    public StockOutReqFrozenTube sampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }
    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }
    public StockOutReqFrozenTube sampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }
    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }
    public StockOutReqFrozenTube sampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }
    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }
    public StockOutReqFrozenTube sampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
        return this;
    }
    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public Double getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }
    public StockOutReqFrozenTube frozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
        return this;
    }
    public void setFrozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }

    public Double getSampleVolumns() {
        return sampleVolumns;
    }
    public StockOutReqFrozenTube sampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
        return this;
    }
    public void setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
    }

    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }
    public StockOutReqFrozenTube frozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
        return this;
    }
    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
    }

    public String getErrorType() {
        return errorType;
    }
    public StockOutReqFrozenTube errorType(String errorType) {
        this.errorType = errorType;
        return this;
    }
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }
    public StockOutReqFrozenTube frozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
        return this;
    }
    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }
    public StockOutReqFrozenTube frozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
        return this;
    }
    public StockOutReqFrozenTube setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
        return this;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }
    public StockOutReqFrozenTube sampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
        return this;
    }
    public StockOutReqFrozenTube setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
        return this;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }
    public StockOutReqFrozenTube sampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
        return this;
    }
    public StockOutReqFrozenTube setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
        return this;
    }

    public Long getProjectId() {
        return projectId;
    }
    public StockOutReqFrozenTube projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }
    public StockOutReqFrozenTube setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }
    public StockOutReqFrozenTube projectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
        return this;
    }
    public StockOutReqFrozenTube setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
        return this;
    }

    @ManyToOne(optional = false)
    private StockOutFrozenBox stockOutFrozenBox;

    @ManyToOne(optional = false)
    private StockOutTask stockOutTask;

    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100)
    private String frozenBoxCode;

    @Size(max = 100)
    @Column(name = "frozen_box_code_1d", length = 100)
    private String frozenBoxCode1D;

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
    /**
     * 冻存管类型
     */
    @Column(name = "frozen_tube_type_id")
    private Long frozenTubeTypeId;
    /**
     * 样本类型
     */
    @Column(name = "sample_type_id")
    private Long sampleTypeId;
    /**
     * 样本分类
     */
    @Column(name = "sample_classification_id")
    private Long sampleClassificationId;
    /**
     * 项目
     */
    @Column(name = "project_id")
    private Long projectId;
    /**
     * 项目点
     */
    @Column(name = "project_site_id")
    private Long projectSiteId;

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public StockOutReqFrozenTube frozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
        return this;
    }


    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutReqFrozenTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutReqFrozenTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public StockOutReqFrozenTube tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public StockOutReqFrozenTube tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public Long getImportingSampleId() {
        return importingSampleId;
    }

    public String getRepealReason() {
        return repealReason;
    }

    public StockOutReqFrozenTube repealReason(String repealReason) {
        this.repealReason = repealReason;
        return this;
    }

    public void setRepealReason(String repealReason) {
        this.repealReason = repealReason;
    }

    public StockOutReqFrozenTube importingSampleId(Long importingSampleId) {
        this.importingSampleId = importingSampleId;
        return this;
    }

    public void setImportingSampleId(Long importingSampleId) {
        this.importingSampleId = importingSampleId;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public StockOutReqFrozenTube frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public StockOutReqFrozenTube frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    public StockOutRequirement getStockOutRequirement() {
        return stockOutRequirement;
    }

    public StockOutReqFrozenTube stockOutRequirement(StockOutRequirement stockOutRequirement) {
        this.stockOutRequirement = stockOutRequirement;
        return this;
    }

    public void setStockOutRequirement(StockOutRequirement stockOutRequirement) {
        this.stockOutRequirement = stockOutRequirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutReqFrozenTube stockOutReqFrozenTube = (StockOutReqFrozenTube) o;
        if (stockOutReqFrozenTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutReqFrozenTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutReqFrozenTube{" +
            "id=" + id +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", tubeRows='" + tubeRows + '\'' +
            ", tubeColumns='" + tubeColumns + '\'' +
            ", importingSampleId=" + importingSampleId +
            ", repealReason='" + repealReason + '\'' +
            ", frozenBox=" + frozenBox +
            ", frozenTube=" + frozenTube +
            ", stockOutRequirement=" + stockOutRequirement +
            ", stockOutFrozenBox=" + stockOutFrozenBox +
            ", stockOutTask=" + stockOutTask +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", frozenBoxCode1D='" + frozenBoxCode1D + '\'' +
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
            ", frozenTubeTypeId =" + frozenTubeTypeId +
            ", sampleTypeId =" + sampleTypeId +
            ", sampleClassificationId =" + sampleClassificationId  +
            ", projectId =" + projectId  +
            ", projectSiteId =" + projectSiteId  +
            '}';
    }
}
