package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInTubesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockInTubes and its DTO StockInTubesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInTubesMapper {

    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "frozenBoxPosition.id", target = "frozenBoxPositionId")
    @Mapping(source = "transhipBox.id", target = "transhipBoxId")
    @Mapping(source = "stockInBox.id", target = "stockInBoxId")
    StockInTubesDTO stockInTubesToStockInTubesDTO(StockInTubes stockInTubes);

    List<StockInTubesDTO> stockInTubesToStockInTubesDTOs(List<StockInTubes> stockInTubes);

    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "frozenBoxPositionId", target = "frozenBoxPosition")
    @Mapping(source = "transhipBoxId", target = "transhipBox")
    @Mapping(source = "stockInBoxId", target = "stockInBox")
    StockInTubes stockInTubesDTOToStockInTubes(StockInTubesDTO stockInTubesDTO);

    List<StockInTubes> stockInTubesDTOsToStockInTubes(List<StockInTubesDTO> stockInTubesDTOs);

    default FrozenTube frozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(id);
        return frozenTube;
    }

    default FrozenBoxPosition frozenBoxPositionFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBoxPosition frozenBoxPosition = new FrozenBoxPosition();
        frozenBoxPosition.setId(id);
        return frozenBoxPosition;
    }

    default TranshipBox transhipBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        TranshipBox transhipBox = new TranshipBox();
        transhipBox.setId(id);
        return transhipBox;
    }

    default StockInBox stockInBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInBox stockInBox = new StockInBox();
        stockInBox.setId(id);
        return stockInBox;
    }
}
