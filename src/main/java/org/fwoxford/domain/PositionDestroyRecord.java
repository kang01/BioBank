package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PositionDestroyRecord.
 */
@Entity
@Table(name = "position_destroy_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PositionDestroyRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_position_destroy_record")
    @SequenceGenerator(name = "seq_position_destroy_record",sequenceName = "seq_position_destroy_record",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "sample_code", length = 100, nullable = false)
    private String sampleCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false)
    private String equipmentCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "area_code", length = 100, nullable = false)
    private String areaCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100, nullable = false)
    private String supportRackCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "rows_in_shelf", length = 100, nullable = false)
    private String rowsInShelf;

    @NotNull
    @Size(max = 100)
    @Column(name = "columns_in_shelf", length = 100, nullable = false)
    private String columnsInShelf;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "tube_rows", length = 100, nullable = false)
    private String tubeRows;

    @NotNull
    @Size(max = 100)
    @Column(name = "tube_columns", length = 100, nullable = false)
    private String tubeColumns;

    @NotNull
    @Size(max = 20)
    @Column(name = "destroy_type", length = 20, nullable = false)
    private String destroyType;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;

    @Size(max = 100)
    @Column(name = "project_site_code", length = 100)
    private String projectSiteCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private Equipment equipment;

    @ManyToOne(optional = false)
    @NotNull
    private Area area;

    @ManyToOne(optional = false)
    @NotNull
    private SupportRack supportRack;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

    @ManyToOne
    private ProjectSite projectSite;
    @ManyToOne(optional = false)
    @NotNull
    private PositionDestroy positionDestroy;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleCode() {
        return sampleCode;
    }
    public PositionDestroyRecord sampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }
    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }
    public PositionDestroyRecord equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }
    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }
    public PositionDestroyRecord areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }
    public PositionDestroyRecord supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }
    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }
    public PositionDestroyRecord rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }
    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }
    public PositionDestroyRecord columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }
    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }
    public PositionDestroyRecord frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }
    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getTubeRows() {
        return tubeRows;
    }
    public PositionDestroyRecord tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }
    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }
    public PositionDestroyRecord tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }
    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getDestroyType() {
        return destroyType;
    }
    public PositionDestroyRecord destroyType(String destroyType) {
        this.destroyType = destroyType;
        return this;
    }
    public PositionDestroyRecord setDestroyType(String destroyType) {
        this.destroyType = destroyType;
        return this;
    }

    public String getProjectCode() {
        return projectCode;
    }
    public PositionDestroyRecord projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }
    public PositionDestroyRecord projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }
    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getStatus() {
        return status;
    }
    public PositionDestroyRecord status(String status) {
        this.status = status;
        return this;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }
    public PositionDestroyRecord memo(String memo) {
        this.memo = memo;
        return this;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Equipment getEquipment() {
        return equipment;
    }
    public PositionDestroyRecord equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Area getArea() {
        return area;
    }
    public PositionDestroyRecord area(Area area) {
        this.area = area;
        return this;
    }
    public void setArea(Area area) {
        this.area = area;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }
    public PositionDestroyRecord supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }
    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }
    public PositionDestroyRecord frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }
    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }
    public PositionDestroyRecord frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }
    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    public Project getProject() {
        return project;
    }
    public PositionDestroyRecord project(Project project) {
        this.project = project;
        return this;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }
    public PositionDestroyRecord projectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
        return this;
    }
    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }
    public PositionDestroyRecord frozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
        return this;
    }
    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }
    public PositionDestroyRecord sampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }
    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }
    public PositionDestroyRecord frozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }
    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }
    public PositionDestroyRecord frozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }
    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }
    public PositionDestroyRecord sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }
    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }
    public PositionDestroyRecord sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }
    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }
    public PositionDestroyRecord sampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }
    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }
    public PositionDestroyRecord sampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }
    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }
    public PositionDestroyRecord sampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }
    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }
    public PositionDestroyRecord sampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
        return this;
    }
    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public Double getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }
    public PositionDestroyRecord frozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
        return this;
    }
    public void setFrozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }

    public Double getSampleVolumns() {
        return sampleVolumns;
    }
    public PositionDestroyRecord sampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
        return this;
    }
    public void setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
    }

    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }
    public PositionDestroyRecord frozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
        return this;
    }
    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
    }

    public String getErrorType() {
        return errorType;
    }
    public PositionDestroyRecord errorType(String errorType) {
        this.errorType = errorType;
        return this;
    }
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }
    public PositionDestroyRecord frozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
        return this;
    }
    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
    }

    public FrozenTubeType getFrozenTubeType() {
        return frozenTubeType;
    }
    public PositionDestroyRecord frozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
        return this;
    }
    public void setFrozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }
    public PositionDestroyRecord sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }
    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }
    public PositionDestroyRecord sampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }
    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public PositionDestroy getPositionDestroy() {
        return positionDestroy;
    }

    public PositionDestroyRecord positionDestroy(PositionDestroy positionDestroy) {
        this.positionDestroy = positionDestroy;
        return this;
    }
    public PositionDestroyRecord setPositionDestroy(PositionDestroy positionDestroy) {
        this.positionDestroy = positionDestroy;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PositionDestroyRecord positionDestroyRecord = (PositionDestroyRecord) o;
        if (positionDestroyRecord.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, positionDestroyRecord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionDestroyRecord{" +
            "id=" + id +
            ", sampleCode='" + sampleCode + '\'' +
            ", equipmentCode='" + equipmentCode + '\'' +
            ", areaCode='" + areaCode + '\'' +
            ", supportRackCode='" + supportRackCode + '\'' +
            ", rowsInShelf='" + rowsInShelf + '\'' +
            ", columnsInShelf='" + columnsInShelf + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", tubeRows='" + tubeRows + '\'' +
            ", tubeColumns='" + tubeColumns + '\'' +
            ", destroyType='" + destroyType + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", equipment=" + equipment +
            ", area=" + area +
            ", supportRack=" + supportRack +
            ", frozenBox=" + frozenBox +
            ", frozenTube=" + frozenTube +
            ", project=" + project +
            ", projectSite=" + projectSite +
            ", positionDestroy=" + positionDestroy +
            ", frozenTubeCode='" + frozenTubeCode + '\'' +
            ", sampleTempCode='" + sampleTempCode + '\'' +
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
            ", frozenTubeType=" + frozenTubeType +
            ", sampleType=" + sampleType +
            ", sampleClassification=" + sampleClassification +
            '}';
    }
}
