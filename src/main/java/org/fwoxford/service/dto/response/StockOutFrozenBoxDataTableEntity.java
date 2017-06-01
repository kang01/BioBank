package org.fwoxford.service.dto.response;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/31.
 */
public class StockOutFrozenBoxDataTableEntity extends StockOutFrozenBoxForTaskDataTableEntity{
    private String status;
    private String memo;
    private LocalDate stockOutHandoverTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDate getStockOutHandoverTime() {
        return stockOutHandoverTime;
    }

    public void setStockOutHandoverTime(LocalDate stockOutHandoverTime) {
        this.stockOutHandoverTime = stockOutHandoverTime;
    }
}
