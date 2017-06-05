package org.fwoxford.service.dto.response;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/31.
 */
public class StockOutFrozenBoxDataTableEntity extends StockOutFrozenBoxForTaskDataTableEntity{

    private LocalDate stockOutHandoverTime;

    public LocalDate getStockOutHandoverTime() {
        return stockOutHandoverTime;
    }

    public void setStockOutHandoverTime(LocalDate stockOutHandoverTime) {
        this.stockOutHandoverTime = stockOutHandoverTime;
    }
}
