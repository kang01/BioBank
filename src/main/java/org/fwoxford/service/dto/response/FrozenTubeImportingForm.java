package org.fwoxford.service.dto.response;

import java.util.Date;

/**
 * Created by gengluying on 2017/8/14.
 */
public class FrozenTubeImportingForm {
    //样本编码
    private String specimenCode;
    //patient ID
    private String bloodCode;
    //A = EDTA Plasma 血浆; W = Buffy Coat; R = Red Cell红细胞; E = Urine尿  无血清，无RNA
    private String specimenType;
    private String specimenStatus;
    private String frozenBoxCode;
    private String frozenBoxType;
    //盒内行
    private String rowOfSpecimenPos;
    //盒内列
    private String colOfSpecimenPos;
    //项目编码
    private String projectCode;
    //项目点编码
    private String lccId;
    //是否溶血
    private String isHemolysis;
    //是否脂质血
    private String isLipid;
    //是否为空
    private String isEmpty;
    //创建时间
    private Date createDate;
    //创建人
    private String createBy;
    //{ "specimenCode": "20206670567", "bloodCode": "65040114688", // patient ID
    // "specimenType": "E", // A = EDTA Plasma; W = Buffy Coat; R = Red Cell; E = Urine
    // "specimenStatus": null, "frozenBoxCode": "650420120", "frozenBoxType": "E",
    // "rowOfSpecimenPos": 1, "colOfSpecimenPos": 9, "projectCode": "006", "lccId": "650403",
    // "isHemolysis": 2, "isLipid": 2, "isEmpty": 2, "createDate": "2017-01-28", "createBy": "asiguliremaiti" }

    public String getSpecimenCode() {
        return specimenCode;
    }

    public void setSpecimenCode(String specimenCode) {
        this.specimenCode = specimenCode;
    }

    public String getBloodCode() {
        return bloodCode;
    }

    public void setBloodCode(String bloodCode) {
        this.bloodCode = bloodCode;
    }

    public String getSpecimenType() {
        return specimenType;
    }

    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }

    public String getSpecimenStatus() {
        return specimenStatus;
    }

    public void setSpecimenStatus(String specimenStatus) {
        this.specimenStatus = specimenStatus;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getFrozenBoxType() {
        return frozenBoxType;
    }

    public void setFrozenBoxType(String frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public String getRowOfSpecimenPos() {
        return rowOfSpecimenPos;
    }

    public void setRowOfSpecimenPos(String rowOfSpecimenPos) {
        this.rowOfSpecimenPos = rowOfSpecimenPos;
    }

    public String getColOfSpecimenPos() {
        return colOfSpecimenPos;
    }

    public void setColOfSpecimenPos(String colOfSpecimenPos) {
        this.colOfSpecimenPos = colOfSpecimenPos;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getLccId() {
        return lccId;
    }

    public void setLccId(String lccId) {
        this.lccId = lccId;
    }

    public String getIsHemolysis() {
        return isHemolysis;
    }

    public void setIsHemolysis(String isHemolysis) {
        this.isHemolysis = isHemolysis;
    }

    public String getIsLipid() {
        return isLipid;
    }

    public void setIsLipid(String isLipid) {
        this.isLipid = isLipid;
    }

    public String getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(String isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public String toString() {
        return "FrozenTubeImportingForm{" +
            "specimenCode='" + specimenCode + '\'' +
            ", bloodCode='" + bloodCode + '\'' +
            ", specimenType='" + specimenType + '\'' +
            ", specimenStatus='" + specimenStatus + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", frozenBoxType='" + frozenBoxType + '\'' +
            ", rowOfSpecimenPos='" + rowOfSpecimenPos + '\'' +
            ", colOfSpecimenPos='" + colOfSpecimenPos + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", lccId='" + lccId + '\'' +
            ", isHemolysis='" + isHemolysis + '\'' +
            ", isLipid='" + isLipid + '\'' +
            ", isEmpty='" + isEmpty + '\'' +
            ", createDate=" + createDate +
            ", createBy='" + createBy + '\'' +
            '}';
    }
}
