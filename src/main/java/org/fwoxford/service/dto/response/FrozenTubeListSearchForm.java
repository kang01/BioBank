package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/6/22.
 */
public class FrozenTubeListSearchForm {
    private String position;
    private String[] frozenBoxCodeStr;
    private String[] sampleCodeStr;
    private String[] projectCodeStr;
    private String projectName;
    private String sex;
    private Integer age;
    private String diseaseType;
    private Boolean isHemolysis;
    private Boolean isBloodLipid;
    private String frozenTubeState;
    private String equipmentCode;
    private String areaCode;
    private String shelvesCode;
    private String rowsInShelf;
    private String columnsInShelf;
    private Long equipmentId;
    private Long areaId;
    private Long shelvesId;
    private Long sampleTypeId;
    private Long sampleClassificationId;
    private String tubeRows;
    private String tubeColumns;
    /**
     * 空间条件：1：已用：2：剩余
     */
    private Integer spaceType;
    /**
     * 比较类型：1：大于，2：大于等于，3：等于，4：小于，5：小于等于
     */
    private Integer compareType;
    /**
     * 数值
     */
    private Long number;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String[] getFrozenBoxCodeStr() {
        return frozenBoxCodeStr;
    }

    public void setFrozenBoxCodeStr(String[] frozenBoxCodeStr) {
        this.frozenBoxCodeStr = frozenBoxCodeStr;
    }

    public String[] getSampleCodeStr() {
        return sampleCodeStr;
    }

    public void setSampleCodeStr(String[] sampleCodeStr) {
        this.sampleCodeStr = sampleCodeStr;
    }

    public String[] getProjectCodeStr() {
        return projectCodeStr;
    }

    public void setProjectCodeStr(String[] projectCodeStr) {
        this.projectCodeStr = projectCodeStr;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public Boolean getHemolysis() {
        return isHemolysis;
    }

    public void setHemolysis(Boolean hemolysis) {
        this.isHemolysis = hemolysis;
    }

    public Boolean getBloodLipid() {
        return isBloodLipid;
    }

    public void setBloodLipid(Boolean bloodLipid) {
        this.isBloodLipid = bloodLipid;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }

    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
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

    public String getShelvesCode() {
        return shelvesCode;
    }

    public void setShelvesCode(String shelvesCode) {
        this.shelvesCode = shelvesCode;
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

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(Long shelvesId) {
        this.shelvesId = shelvesId;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public Integer getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(Integer spaceType) {
        this.spaceType = spaceType;
    }

    public Integer getCompareType() {
        return compareType;
    }

    public void setCompareType(Integer compareType) {
        this.compareType = compareType;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
