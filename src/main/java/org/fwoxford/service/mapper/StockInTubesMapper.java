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

    @Mapping(source = "stockIn.id", target = "stockInId")
    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "frozenBoxPosition.id", target = "frozenBoxPositionId")
    StockInTubesDTO stockInTubesToStockInTubesDTO(StockInTubes stockInTubes);

    List<StockInTubesDTO> stockInTubesToStockInTubesDTOs(List<StockInTubes> stockInTubes);

    @Mapping(source = "stockInId", target = "stockIn")
    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "frozenBoxPositionId", target = "frozenBoxPosition")
    StockInTubes stockInTubesDTOToStockInTubes(StockInTubesDTO stockInTubesDTO);

    List<StockInTubes> stockInTubesDTOsToStockInTubes(List<StockInTubesDTO> stockInTubesDTOs);

    default StockIn stockInFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockIn stockIn = new StockIn();
        stockIn.setId(id);
        return stockIn;
    }

    default Tranship transhipFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tranship tranship = new Tranship();
        tranship.setId(id);
        return tranship;
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

    default FrozenBoxPosition frozenBoxPositionFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBoxPosition frozenBoxPosition = new FrozenBoxPosition();
        frozenBoxPosition.setId(id);
        return frozenBoxPosition;
    }
}
