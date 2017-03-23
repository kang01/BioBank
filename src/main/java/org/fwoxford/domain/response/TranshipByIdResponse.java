package org.fwoxford.domain.response;

import org.fwoxford.service.dto.FrozenBoxDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Created by gengluying on 2017/3/22.
 */
public class TranshipByIdResponse {

    private Long id;
    /**
     * 转运日期
     */
    @NotNull
    private LocalDate transhipDate;
    /**
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    private String projectCode;
    /**
     * 项目点编码
     */
    @NotNull
    @Size(max = 100)
    private String projectSiteCode;
    /**
     * 转运状态
     */
    @NotNull
    @Size(max = 20)
    private String transhipState;
    /**
     * 接收人
     */
    @NotNull
    @Size(max = 100)
    private String receiver;
    /**
     * 接收日期
     */
    @NotNull
    private LocalDate receiveDate;
    /**
     * 样本满意度
     */
    @Max(value = 20)
    private Integer sampleSatisfaction;
    /**
     * 冻存盒列表
     */
    private List<FrozenBoxResponse> frozenBoxResponseList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTranshipDate() {
        return transhipDate;
    }

    public void setTranshipDate(LocalDate transhipDate) {
        this.transhipDate = transhipDate;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getTranshipState() {
        return transhipState;
    }

    public void setTranshipState(String transhipState) {
        this.transhipState = transhipState;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getSampleSatisfaction() {
        return sampleSatisfaction;
    }

    public void setSampleSatisfaction(Integer sampleSatisfaction) {
        this.sampleSatisfaction = sampleSatisfaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranshipByIdResponse response = (TranshipByIdResponse) o;

        if ( ! Objects.equals(id, response.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipResponse{" +
            "id=" + id +
            ", transhipDate='" + transhipDate + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", transhipState='" + transhipState + "'" +
            ", receiver='" + receiver + "'" +
            ", receiveDate='" + receiveDate + "'" +
            ", sampleSatisfaction='" + sampleSatisfaction + "'" +
            '}';
    }

}
