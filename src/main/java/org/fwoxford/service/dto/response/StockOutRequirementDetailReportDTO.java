package org.fwoxford.service.dto.response;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuyu on 2017/5/15.
 */
public class StockOutRequirementDetailReportDTO {

    private Long id;

    private String requirementName;

    private Integer countOfSample;
    private Integer countOfStockOutSample;

    private String sampleType;
    private String tubeType;
    private String sex;
    private String ages;
    private String diseaseType;
    private String projects;

    private String memo;

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

    public List<StockOutSampleCheckResultDTO> getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(List<StockOutSampleCheckResultDTO> checkResults) {
        this.checkResults = checkResults;
    }

    private List<StockOutSampleCheckResultDTO> checkResults;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Map<String, String> getErrorSamples() {
        return errorSamples;
    }

    public void setErrorSamples(Map<String, String> errorSamples) {
        this.errorSamples = errorSamples;
    }

    private Map<String, String> errorSamples;
}
