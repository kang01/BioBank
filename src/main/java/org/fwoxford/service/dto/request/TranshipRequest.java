package org.fwoxford.service.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Created by gengluying on 2017/3/24.
 */
public class TranshipRequest {
    private Long id;

    @NotNull
    private LocalDate transhipDate;

    @NotNull
    @Size(max = 100)
    private String projectCode;

    @NotNull
    @Size(max = 255)
    private String projectName;

    @NotNull
    @Size(max = 100)
    private String projectSiteCode;

    @NotNull
    @Size(max = 255)
    private String projectSiteName;

    @NotNull
    @Size(max = 100)
    private String trackNumber;

    @NotNull
    @Size(max = 100)
    private String transhipBatch;

    @NotNull
    @Size(max = 20)
    private String transhipState;

    @NotNull
    @Size(max = 100)
    private String receiver;

    @NotNull
    private LocalDate receiveDate;

    @NotNull
    @Max(value = 100)
    private Integer sampleNumber;

    @NotNull
    @Max(value = 20)
    private Integer frozenBoxNumber;

    @NotNull
    @Max(value = 20)
    private Integer emptyTubeNumber;

    @NotNull
    @Max(value = 20)
    private Integer emptyHoleNumber;

    @Max(value = 20)
    private Integer sampleSatisfaction;

    @NotNull
    @Max(value = 20)
    private Integer effectiveSampleNumber;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private Long projectId;

    private Long projectSiteId;

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
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }
    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }
    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }
    public String getTranshipBatch() {
        return transhipBatch;
    }

    public void setTranshipBatch(String transhipBatch) {
        this.transhipBatch = transhipBatch;
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
    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }
    public Integer getFrozenBoxNumber() {
        return frozenBoxNumber;
    }

    public void setFrozenBoxNumber(Integer frozenBoxNumber) {
        this.frozenBoxNumber = frozenBoxNumber;
    }
    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }

    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }
    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }

    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }
    public Integer getSampleSatisfaction() {
        return sampleSatisfaction;
    }

    public void setSampleSatisfaction(Integer sampleSatisfaction) {
        this.sampleSatisfaction = sampleSatisfaction;
    }
    public Integer getEffectiveSampleNumber() {
        return effectiveSampleNumber;
    }

    public void setEffectiveSampleNumber(Integer effectiveSampleNumber) {
        this.effectiveSampleNumber = effectiveSampleNumber;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }

    public void setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
    }


}
