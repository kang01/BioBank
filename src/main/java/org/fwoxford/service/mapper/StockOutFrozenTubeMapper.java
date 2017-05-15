package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutFrozenTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutFrozenTube and its DTO StockOutFrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutFrozenTubeMapper {

    @Mapping(source = "stockOutFrozenBox.id", target = "stockOutFrozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    StockOutFrozenTubeDTO stockOutFrozenTubeToStockOutFrozenTubeDTO(StockOutFrozenTube stockOutFrozenTube);

    List<StockOutFrozenTubeDTO> stockOutFrozenTubesToStockOutFrozenTubeDTOs(List<StockOutFrozenTube> stockOutFrozenTubes);

    @Mapping(source = "stockOutFrozenBoxId", target = "stockOutFrozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    StockOutFrozenTube stockOutFrozenTubeDTOToStockOutFrozenTube(StockOutFrozenTubeDTO stockOutFrozenTubeDTO);

    List<StockOutFrozenTube> stockOutFrozenTubeDTOsToStockOutFrozenTubes(List<StockOutFrozenTubeDTO> stockOutFrozenTubeDTOs);

    default StockOutFrozenBox stockOutFrozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
        stockOutFrozenBox.setId(id);
        return stockOutFrozenBox;
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
