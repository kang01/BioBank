package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutReqFrozenTube and its DTO StockOutReqFrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutReqFrozenTubeMapper {

    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "stockOutRequirement.id", target = "stockOutRequirementId")
    StockOutReqFrozenTubeDTO stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(StockOutReqFrozenTube stockOutReqFrozenTube);

    List<StockOutReqFrozenTubeDTO> stockOutReqFrozenTubesToStockOutReqFrozenTubeDTOs(List<StockOutReqFrozenTube> stockOutReqFrozenTubes);

    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "stockOutRequirementId", target = "stockOutRequirement")
    StockOutReqFrozenTube stockOutReqFrozenTubeDTOToStockOutReqFrozenTube(StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO);

    List<StockOutReqFrozenTube> stockOutReqFrozenTubeDTOsToStockOutReqFrozenTubes(List<StockOutReqFrozenTubeDTO> stockOutReqFrozenTubeDTOs);

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

    default StockOutRequirement stockOutRequirementFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutRequirement stockOutRequirement = new StockOutRequirement();
        stockOutRequirement.setId(id);
        return stockOutRequirement;
    }
}
