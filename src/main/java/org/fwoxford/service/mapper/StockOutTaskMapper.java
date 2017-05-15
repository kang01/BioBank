package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutTaskDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutTask and its DTO StockOutTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutTaskMapper {

    @Mapping(source = "stockOutPlan.id", target = "stockOutPlanId")
    StockOutTaskDTO stockOutTaskToStockOutTaskDTO(StockOutTask stockOutTask);

    List<StockOutTaskDTO> stockOutTasksToStockOutTaskDTOs(List<StockOutTask> stockOutTasks);

    @Mapping(source = "stockOutPlanId", target = "stockOutPlan")
    StockOutTask stockOutTaskDTOToStockOutTask(StockOutTaskDTO stockOutTaskDTO);

    List<StockOutTask> stockOutTaskDTOsToStockOutTasks(List<StockOutTaskDTO> stockOutTaskDTOs);

    default StockOutPlan stockOutPlanFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutPlan stockOutPlan = new StockOutPlan();
        stockOutPlan.setId(id);
        return stockOutPlan;
    }
}
