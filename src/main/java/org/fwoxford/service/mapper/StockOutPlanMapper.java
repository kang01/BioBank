package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutPlanDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutPlan and its DTO StockOutPlanDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutPlanMapper {

    @Mapping(source = "stockOutApply.id", target = "stockOutApplyId")
    StockOutPlanDTO stockOutPlanToStockOutPlanDTO(StockOutPlan stockOutPlan);

    List<StockOutPlanDTO> stockOutPlansToStockOutPlanDTOs(List<StockOutPlan> stockOutPlans);

    @Mapping(source = "stockOutApplyId", target = "stockOutApply")
    StockOutPlan stockOutPlanDTOToStockOutPlan(StockOutPlanDTO stockOutPlanDTO);

    List<StockOutPlan> stockOutPlanDTOsToStockOutPlans(List<StockOutPlanDTO> stockOutPlanDTOs);

    default StockOutApply stockOutApplyFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutApply stockOutApply = new StockOutApply();
        stockOutApply.setId(id);
        return stockOutApply;
    }
}
