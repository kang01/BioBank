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
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(StockOutPlanFrozenTube stockOutPlanFrozenTube);

    List<StockOutPlanFrozenTubeDTO> stockOutPlanFrozenTubesToStockOutPlanFrozenTubeDTOs(List<StockOutPlanFrozenTube> stockOutPlanFrozenTubes);

    @Mapping(source = "stockOutPlanId", target = "stockOutPlan")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
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

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }

    default FrozenTube frozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(id);
        return frozenTube;
    }
}
