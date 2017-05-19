package org.fwoxford.service.dto.response;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/19.
 */
public class StockOutApplyForApprove {
    private Long approverId;
    private String password;
    private LocalDate approveTime;

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(LocalDate approveTime) {
        this.approveTime = approveTime;
    }
}
