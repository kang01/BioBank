package org.fwoxford.service.dto.response;

import java.util.List;

/**
 * Created by zhuyu on 2017/5/16.
 */
public class StockOutHandoverReportDTO {
    Long id;
    String handOverNumber;
    String applicationNumber;
    String planNumber;
    String taskNumber;
    String receiverCompany;
    String receiver;
    String receiverContact;
    String deliver;
    String handoverDate;
    Integer countOfSample;
    Integer countOfBox;
    String memo;
    String receiveDate;
    String deliverDate;
    String projectCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandOverNumber() {
        return handOverNumber;
    }

    public void setHandOverNumber(String handOverNumber) {
        this.handOverNumber = handOverNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getReceiverCompany() {
        return receiverCompany;
    }

    public void setReceiverCompany(String receiverCompany) {
        this.receiverCompany = receiverCompany;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(String handoverDate) {
        this.handoverDate = handoverDate;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public Integer getCountOfBox() {
        return countOfBox;
    }

    public void setCountOfBox(Integer countOfBox) {
        this.countOfBox = countOfBox;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public List<StockOutHandoverSampleReportDTO> getSamples() {
        return samples;
    }

    public void setSamples(List<StockOutHandoverSampleReportDTO> samples) {
        this.samples = samples;
    }

    List<StockOutHandoverSampleReportDTO> samples;
}
