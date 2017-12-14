package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by gengluying on 2017/3/22.
 */
@Entity
@Table(name = "view_tranship")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TranshipResponse {
    /**
     * 转运ID
     */
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    /**
     * 转运日期
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "tranship_date")
    private LocalDate transhipDate;
    /**
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "project_code")
    private String projectCode;

    /**
     * 项目点编码
     */
    @NotNull
    @Size(max = 100)
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "project_site_code")
    private String projectSiteCode;

    /**
     * 转运状态：1001：进行中，1002：待入库，1003：已入库，1090：已作废
     */
    @NotNull
    @Size(max = 20)
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "tranship_state")
    private String transhipState;
    /**
     * 接收人
     */
    @NotNull
    @Size(max = 100)
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "receiver")
    private String receiver;
    /**
     * 接收日期
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "receive_date")
    private LocalDate receiveDate;
    /**
     * 样本满意度
     */
    @Max(value = 20)
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "sample_satisfaction")
    private Integer sampleSatisfaction;
    /**
     * 转运编码
     */
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "tranship_code")
    private String transhipCode;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "track_number")
    private String trackNumber;

    @Column(name="receive_type")
    private String receiveType;
    /**
     * 申请编码
     */
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "apply_code")
    private String applyCode;
    /**
     * 归还人
     */
    @JsonView(DataTablesOutput.View.class)
    @Column(name="apply_person_name")
    private String applyPersonName;
    /**
     * 委托方名称
     */
    @JsonView(DataTablesOutput.View.class)
    @Column(name="delegate_name")
    private String delegateName;
    /**
     * 项目ID
     */
    @JsonView(DataTablesOutput.View.class)
    @Column(name="project_id")
    private Long projectId;

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

    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranshipResponse response = (TranshipResponse) o;

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
            ", trackNumber='" + sampleSatisfaction + "'" +
            ", receiveType='" + receiveType + "'" +
            ", applyCode='" + applyCode + "'" +
            ", applyPersonName='" + applyPersonName + "'" +
            ", delegateName='" + delegateName + "'" +
            ", projectId='" + projectId + "'" +
            '}';
    }

}
