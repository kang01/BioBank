package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxDetail extends StockInBoxForShelf{
    private String stockInCode;
    private String memo;

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
