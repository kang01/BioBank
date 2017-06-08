package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockInTube and its DTO StockInTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInTubeMapper {

    @Mapping(source = "stockInBox.id", target = "stockInBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    StockInTubeDTO stockInTubeToStockInTubeDTO(StockInTube stockInTube);

    List<StockInTubeDTO> stockInTubesToStockInTubeDTOs(List<StockInTube> stockInTubes);

    @Mapping(source = "stockInBoxId", target = "stockInBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    StockInTube stockInTubeDTOToStockInTube(StockInTubeDTO stockInTubeDTO);

    List<StockInTube> stockInTubeDTOsToStockInTubes(List<StockInTubeDTO> stockInTubeDTOs);

    default StockInBox stockInBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInBox stockInBox = new StockInBox();
        stockInBox.setId(id);
        return stockInBox;
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
