package org.fwoxford.service.dto;

/**
 * Created by gengluying on 2017/5/4.
 */
public class ProjectSampleClassificationDTO {
    private Long sampleClassificationId;
    private String sampleClassficationName;
    private String columnsNumber;
    private String frontColor;
    private String backColor;
    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }

    public String getSampleClassficationName() {
        return sampleClassficationName;
    }

    public void setSampleClassficationName(String sampleClassficationName) {
        this.sampleClassficationName = sampleClassficationName;
    }

    public String getColumnsNumber() {
        return columnsNumber;
    }

    public void setColumnsNumber(String columnsNumber) {
        this.columnsNumber = columnsNumber;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(String frontColor) {
        this.frontColor = frontColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }
}
