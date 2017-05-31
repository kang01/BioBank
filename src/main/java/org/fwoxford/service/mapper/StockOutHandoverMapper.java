package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutHandoverDTO;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutHandOver and its DTO StockOutHandOverDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutHandoverMapper {

    @Mapping(source = "stockOutTask.id", target = "stockOutTaskId")
    @Mapping(source = "stockOutApply.id", target = "stockOutApplyId")
    @Mapping(source = "stockOutPlan.id", target = "stockOutPlanId")
    StockOutHandoverDTO stockOutHandOverToStockOutHandOverDTO(StockOutHandover stockOutHandOver);

    List<StockOutHandoverDTO> stockOutHandOversToStockOutHandOverDTOs(List<StockOutHandover> stockOutHandOvers);

    @Mapping(source = "stockOutTaskId", target = "stockOutTask")
    @Mapping(source = "stockOutApplyId", target = "stockOutApply")
    @Mapping(source = "stockOutPlanId", target = "stockOutPlan")
    StockOutHandover stockOutHandOverDTOToStockOutHandOver(StockOutHandoverDTO stockOutHandOverDTO);

    List<StockOutHandover> stockOutHandOverDTOsToStockOutHandOvers(List<StockOutHandoverDTO> stockOutHandOverDTOs);

    default StockOutTask stockOutTaskFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.setId(id);
        return stockOutTask;
    }

    default StockOutApply stockOutApplyFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutApply stockOutApply = new StockOutApply();
        stockOutApply.setId(id);
        return stockOutApply;
    }

    default StockOutPlan stockOutPlanFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutPlan stockOutPlan = new StockOutPlan();
        stockOutPlan.setId(id);
        return stockOutPlan;
    }
}
