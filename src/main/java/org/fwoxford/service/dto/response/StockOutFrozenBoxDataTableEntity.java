package org.fwoxford.service.dto.response;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/31.
 */
public class StockOutFrozenBoxDataTableEntity extends StockOutFrozenBoxForTaskDataTableEntity{
    private String stauts;
    private String memo;
    private LocalDate stockOutHandoverTime;

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
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
