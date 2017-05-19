package org.fwoxford.service.dto.response;

import io.swagger.models.auth.In;

import java.util.List;

/**
 * Created by gengluying on 2017/5/18.
 */
public class StockOutRequirementSampleDetail {

    private Long id;
    private Integer countOfSample;
    private Integer countOfStockOutSample;

    private String sampleType;
    private String tubeType;
    private String sex;
    private String ages;
    private String diseaseType;
    private String projects;
    private String memo;
    private Boolean isHemolysis;
    private Boolean isBloodLipid;

    private List<StockOutRequirementFrozenTubeDetail> frozenTubeList;

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

    public Integer getCountOfStockOutSample() {
        return countOfStockOutSample;
    }

    public void setCountOfStockOutSample(Integer countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getTubeType() {
        return tubeType;
    }

    public void setTubeType(String tubeType) {
        this.tubeType = tubeType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAges() {
        return ages;
    }

    public void setAges(String ages) {
        this.ages = ages;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getIsHemolysis() {
        return isHemolysis;
    }

    public void setIsHemolysis(Boolean isHemolysis) {
        isHemolysis = isHemolysis;
    }

    public Boolean getIsBloodLipid() {
        return isBloodLipid;
    }

    public void setIsBloodLipid(Boolean isBloodLipid) {
        isBloodLipid = isBloodLipid;
    }

    public List<StockOutRequirementFrozenTubeDetail> getFrozenTubeList() {
        return frozenTubeList;
    }

    public void setFrozenTubeList(List<StockOutRequirementFrozenTubeDetail> frozenTubeList) {
        this.frozenTubeList = frozenTubeList;
    }
}
