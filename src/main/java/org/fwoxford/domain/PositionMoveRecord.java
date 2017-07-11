package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PositionMoveRecord.
 */
@Entity
@Table(name = "position_move_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PositionMoveRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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

    @Size(max = 1024)
    @Column(name = "move_reason", length = 1024)
    private String moveReason;

    @Size(max = 1024)
    @Column(name = "move_affect", length = 1024)
    private String moveAffect;

    @Column(name = "whether_freezing_and_thawing")
    private Boolean whetherFreezingAndThawing;

    @NotNull
    @Size(max = 20)
    @Column(name = "move_type", length = 20, nullable = false)
    private String moveType;

    @Column(name = "operator_1")
    private Long operator1;

    @Column(name = "operator_2")
    private Long operator2;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public PositionMoveRecord equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public PositionMoveRecord areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public PositionMoveRecord supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getRowsInShelf() {
        return rowsInShelf;
    }

    public PositionMoveRecord rowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
        return this;
    }

    public void setRowsInShelf(String rowsInShelf) {
        this.rowsInShelf = rowsInShelf;
    }

    public String getColumnsInShelf() {
        return columnsInShelf;
    }

    public PositionMoveRecord columnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
        return this;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        this.columnsInShelf = columnsInShelf;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public PositionMoveRecord frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public PositionMoveRecord tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public PositionMoveRecord tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getMoveReason() {
        return moveReason;
    }

    public PositionMoveRecord moveReason(String moveReason) {
        this.moveReason = moveReason;
        return this;
    }

    public void setMoveReason(String moveReason) {
        this.moveReason = moveReason;
    }

    public String getMoveAffect() {
        return moveAffect;
    }

    public PositionMoveRecord moveAffect(String moveAffect) {
        this.moveAffect = moveAffect;
        return this;
    }

    public void setMoveAffect(String moveAffect) {
        this.moveAffect = moveAffect;
    }

    public Boolean isWhetherFreezingAndThawing() {
        return whetherFreezingAndThawing;
    }

    public PositionMoveRecord whetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
        return this;
    }

    public void setWhetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
    }

    public String getMoveType() {
        return moveType;
    }

    public PositionMoveRecord moveType(String moveType) {
        this.moveType = moveType;
        return this;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public Long getOperator1() {
        return operator1;
    }

    public PositionMoveRecord operator1(Long operator1) {
        this.operator1 = operator1;
        return this;
    }

    public void setOperator1(Long operator1) {
        this.operator1 = operator1;
    }

    public Long getOperator2() {
        return operator2;
    }

    public PositionMoveRecord operator2(Long operator2) {
        this.operator2 = operator2;
        return this;
    }

    public void setOperator2(Long operator2) {
        this.operator2 = operator2;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public PositionMoveRecord projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public PositionMoveRecord projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getStatus() {
        return status;
    }

    public PositionMoveRecord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public PositionMoveRecord memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public PositionMoveRecord equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Area getArea() {
        return area;
    }

    public PositionMoveRecord area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public SupportRack getSupportRack() {
        return supportRack;
    }

    public PositionMoveRecord supportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
        return this;
    }

    public void setSupportRack(SupportRack supportRack) {
        this.supportRack = supportRack;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public PositionMoveRecord frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public PositionMoveRecord frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    public Project getProject() {
        return project;
    }

    public PositionMoveRecord project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public PositionMoveRecord projectSite(ProjectSite projectSite) {
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
        PositionMoveRecord positionMoveRecord = (PositionMoveRecord) o;
        if (positionMoveRecord.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, positionMoveRecord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionMoveRecord{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", rowsInShelf='" + rowsInShelf + "'" +
            ", columnsInShelf='" + columnsInShelf + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", moveReason='" + moveReason + "'" +
            ", moveAffect='" + moveAffect + "'" +
            ", whetherFreezingAndThawing='" + whetherFreezingAndThawing + "'" +
            ", moveType='" + moveType + "'" +
            ", operator1='" + operator1 + "'" +
            ", operator2='" + operator2 + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
