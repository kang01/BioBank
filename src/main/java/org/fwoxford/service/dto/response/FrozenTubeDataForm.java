package org.fwoxford.service.dto.response;

import java.util.Date;

/**
 * Created by gengluying on 2017/10/20.
 */
public class FrozenTubeDataForm {
    private String projectId;
    private String boxCode;
    private String boxType;
    private String boxColno;
    private String  boxRowno;
    private String tubeCode;
    private String tubeType;
    private Date scanDate;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getBoxColno() {
        return boxColno;
    }

    public void setBoxColno(String boxColno) {
        this.boxColno = boxColno;
    }

    public String getBoxRowno() {
        return boxRowno;
    }

    public void setBoxRowno(String boxRowno) {
        this.boxRowno = boxRowno;
    }

    public String getTubeCode() {
        return tubeCode;
    }

    public void setTubeCode(String tubeCode) {
        this.tubeCode = tubeCode;
    }

    public String getTubeType() {
        return tubeType;
    }

    public void setTubeType(String tubeType) {
        this.tubeType = tubeType;
    }

    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }
}
