package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutBoxTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutBoxTube and its DTO StockOutBoxTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutBoxTubeMapper {

    @Mapping(source = "stockOutFrozenBox.id", target = "stockOutFrozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "stockOutTaskFrozenTube.id", target = "stockOutTaskFrozenTubeId")
    StockOutBoxTubeDTO stockOutBoxTubeToStockOutBoxTubeDTO(StockOutBoxTube stockOutBoxTube);

    List<StockOutBoxTubeDTO> stockOutBoxTubesToStockOutBoxTubeDTOs(List<StockOutBoxTube> stockOutBoxTubes);

    @Mapping(source = "stockOutFrozenBoxId", target = "stockOutFrozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "stockOutTaskFrozenTubeId", target = "stockOutTaskFrozenTube")
    StockOutBoxTube stockOutBoxTubeDTOToStockOutBoxTube(StockOutBoxTubeDTO stockOutBoxTubeDTO);

    List<StockOutBoxTube> stockOutBoxTubeDTOsToStockOutBoxTubes(List<StockOutBoxTubeDTO> stockOutBoxTubeDTOs);

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

    default StockOutTaskFrozenTube stockOutTaskFrozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTaskFrozenTube stockOutTaskFrozenTube = new StockOutTaskFrozenTube();
        stockOutTaskFrozenTube.setId(id);
        return stockOutTaskFrozenTube;
    }
}
