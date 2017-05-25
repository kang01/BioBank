package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutPlanFrozenTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutPlanFrozenTube and its DTO StockOutPlanFrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutPlanFrozenTubeMapper {

    @Mapping(source = "stockOutPlan.id", target = "stockOutPlanId")
    @Mapping(source = "stockOutReqFrozenTube.id", target = "stockOutReqFrozenTubeId")
    StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(StockOutPlanFrozenTube stockOutPlanFrozenTube);

    List<StockOutPlanFrozenTubeDTO> stockOutPlanFrozenTubesToStockOutPlanFrozenTubeDTOs(List<StockOutPlanFrozenTube> stockOutPlanFrozenTubes);

    @Mapping(source = "stockOutPlanId", target = "stockOutPlan")
    @Mapping(source = "stockOutReqFrozenTubeId", target = "stockOutReqFrozenTube")
    StockOutPlanFrozenTube stockOutPlanFrozenTubeDTOToStockOutPlanFrozenTube(StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO);

    List<StockOutPlanFrozenTube> stockOutPlanFrozenTubeDTOsToStockOutPlanFrozenTubes(List<StockOutPlanFrozenTubeDTO> stockOutPlanFrozenTubeDTOs);

    default StockOutPlan stockOutPlanFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutPlan stockOutPlan = new StockOutPlan();
        stockOutPlan.setId(id);
        return stockOutPlan;
    }

    default StockOutReqFrozenTube stockOutReqFrozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
        stockOutReqFrozenTube.setId(id);
        return stockOutReqFrozenTube;
    }
}
