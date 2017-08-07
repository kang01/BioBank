package org.fwoxford.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A FrozenBox.
 */
@Entity
@Table(name = "frozen_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
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
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_type_code", length = 100, nullable = false)
    private String frozenBoxTypeCode;
    /**
     * 冻存盒行数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_box_rows", length = 20, nullable = false)
    private String frozenBoxTypeRows;
    /**
     * 冻存盒列数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_box_columns", length = 20, nullable = false)
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
     * 设备编码
     */
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100)
    private String equipmentCode;
    /**
     * 区域编码
     */
    @Size(max = 100)
    @Column(name = "area_code", length = 100)
    private String areaCode;
    /**
     * 冻存架编码
     */
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100)
    private String supportRackCode;
    /**
     * 样本类型编码
     */
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100, nullable = false)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255, nullable = false)
    private String sampleTypeName;
    /**
     * 是否分装：1：是，0：否
     */
    @Column(name = "is_split", nullable = false)
    private Integer isSplit;
    /**
     * 所在架子行数
     */
    @Size(max = 20)
    @Column(name = "rows_in_shelf", length = 20)
    private String rowsInShelf;
    /**
     * 所在架子列数
     */
    @Size(max = 20)
    @Column(name = "columns_in_shelf", length = 20)
    private String columnsInShelf;
    /**
     * 备注
     */
    @Size(max = 255)
    @Column(name = "memo", length = 255)
    private String memo;
    /**
     * 状态：2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废，0000：无效
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
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

    @ManyToOne
    private Equipment equipment;

    @ManyToOne
    private Area area;

    @ManyToOne
    private SupportRack supportRack;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public FrozenBox frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public FrozenBox frozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
        return this;
    }

    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }

    public FrozenBox frozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
        return this;
    }

    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }

    public FrozenBox frozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
        return this;
    }

    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public FrozenBox projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public FrozenBox projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public FrozenBox projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public FrozenBox projectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
        return this;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public FrozenBox equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public FrozenBox areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public FrozenBox supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public FrozenBox sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public FrozenBox sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

//    public Integer getSampleNumber() {
//        return sampleNumber;
//    }
//
//    public FrozenBox sampleNumber(Integer sampleNumber) {
//        this.sampleNumber = sampleNumber;
//        return this;
//    }
//
//    public void setSampleNumber(Integer sampleNumber) {
//        this.sampleNumber = sampleNumber;
//    }

    public Integer getIsSplit() {
        return isSplit;
    }

    public FrozenBox isSplit(Integer isSplit) {
        this.isSplit = isSplit;
        return this;
    }

    public void setIsSplit(Integer isSplit) {
        this.isSplit = isSplit;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public FrozenBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }

    public FrozenBox emptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
        return this;
    }

    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }

    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }

    public FrozenBox emptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
        return this;
    }

    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }

    public Integer getDislocationNumber() {
        return dislocationNumber;
    }

    public FrozenBox dislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
        return this;
    }

    public void setDislocationNumber(Integer dislocationNumber) {
        this.dislocationNumber = dislocationNumber;
    }

    public Integer getIsRealData() {
        return isRealData;
    }

    public FrozenBox isRealData(Integer isRealData) {
        this.isRealData = isRealData;
        return this;
    }

    public void setIsRealData(Integer isRealData) {
        this.isRealData = isRealData;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }
    public FrozenBox rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }
    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }
    public FrozenBox columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public FrozenBoxType getFrozenBoxType() {
        return frozenBoxType;
    }

    public FrozenBox frozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
        return this;
    }

    public void setFrozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public FrozenBox sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }
    public FrozenBox sampleClassification(SampleClassification sampleClassification){
        this.sampleClassification = sampleClassification;
        return this;
    }
    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public Project getProject() {
        return project;
    }

    public FrozenBox project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public FrozenBox projectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
        return this;
    }

    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public FrozenBox equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Area getArea() {
        return area;
    }

    public FrozenBox area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }

    public FrozenBox supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }

    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FrozenBox frozenBox = (FrozenBox) o;
        if (frozenBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, frozenBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenBox{" +
            "id=" + id +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + "'" +
            ", frozenBoxTypeRows='" + frozenBoxTypeRows + "'" +
            ", frozenBoxTypeColumns='" + frozenBoxTypeColumns + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectName='" + projectName + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", projectSiteName='" + projectSiteName + "'" +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
//            ", sampleNumber='" + sampleNumber + "'" +
            ", isSplit='" + isSplit + "'" +
            ", rowsInShelf='" + rowsInShelf + "'" +
            ", columnsInShelf='" + columnsInShelf + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", emptyTubeNumber='" + emptyTubeNumber + "'" +
            ", emptyHoleNumber='" + emptyHoleNumber + "'" +
            ", dislocationNumber='" + dislocationNumber + "'" +
            ", isRealData='" + isRealData + "'" +
            '}';
    }
}
