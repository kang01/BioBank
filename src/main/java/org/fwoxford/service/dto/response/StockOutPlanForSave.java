package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutPlanForSave {
    private Long id;
    private Long applyId;
    private String Status;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
