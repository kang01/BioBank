package org.fwoxford.service.dto.response;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutPlanDetail {
    private Long id;
    private String applyNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }
}
