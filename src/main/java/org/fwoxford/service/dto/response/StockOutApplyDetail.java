package org.fwoxford.service.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by gengluying on 2017/5/15.
 */
public class StockOutApplyDetail extends StockOutApplyForSave{
    /**
     * 样本需求
     */
    private List<StockOutRequirementForApplyTable> stockOutRequirement;

    public List<StockOutRequirementForApplyTable> getStockOutRequirement() {
        return stockOutRequirement;
    }

    public void setStockOutRequirement(List<StockOutRequirementForApplyTable> stockOutRequirement) {
        this.stockOutRequirement = stockOutRequirement;
    }
}
