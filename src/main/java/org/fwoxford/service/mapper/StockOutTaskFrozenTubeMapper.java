package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutTaskFrozenTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutTaskFrozenTube and its DTO StockOutTaskFrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutTaskFrozenTubeMapper {

    @Mapping(source = "stockOutTask.id", target = "stockOutTaskId")
    @Mapping(source = "stockOutPlanFrozenTube.id", target = "stockOutPlanFrozenTubeId")
    StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(StockOutTaskFrozenTube stockOutTaskFrozenTube);

    List<StockOutTaskFrozenTubeDTO> stockOutTaskFrozenTubesToStockOutTaskFrozenTubeDTOs(List<StockOutTaskFrozenTube> stockOutTaskFrozenTubes);

    @Mapping(source = "stockOutTaskId", target = "stockOutTask")
    @Mapping(source = "stockOutPlanFrozenTubeId", target = "stockOutPlanFrozenTube")
    StockOutTaskFrozenTube stockOutTaskFrozenTubeDTOToStockOutTaskFrozenTube(StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO);

    List<StockOutTaskFrozenTube> stockOutTaskFrozenTubeDTOsToStockOutTaskFrozenTubes(List<StockOutTaskFrozenTubeDTO> stockOutTaskFrozenTubeDTOs);

    default StockOutTask stockOutTaskFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.setId(id);
        return stockOutTask;
    }

    default StockOutPlanFrozenTube stockOutPlanFrozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutPlanFrozenTube stockOutPlanFrozenTube = new StockOutPlanFrozenTube();
        stockOutPlanFrozenTube.setId(id);
        return stockOutPlanFrozenTube;
    }
}
