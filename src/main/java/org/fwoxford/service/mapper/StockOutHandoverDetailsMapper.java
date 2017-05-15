package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutHandoverDetailsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutHandoverDetails and its DTO StockOutHandoverDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutHandoverDetailsMapper {

    @Mapping(source = "stockOutHandover.id", target = "stockOutHandoverId")
    @Mapping(source = "stockOutFrozenTube.id", target = "stockOutFrozenTubeId")
    StockOutHandoverDetailsDTO stockOutHandoverDetailsToStockOutHandoverDetailsDTO(StockOutHandoverDetails stockOutHandoverDetails);

    List<StockOutHandoverDetailsDTO> stockOutHandoverDetailsToStockOutHandoverDetailsDTOs(List<StockOutHandoverDetails> stockOutHandoverDetails);

    @Mapping(source = "stockOutHandoverId", target = "stockOutHandover")
    @Mapping(source = "stockOutFrozenTubeId", target = "stockOutFrozenTube")
    StockOutHandoverDetails stockOutHandoverDetailsDTOToStockOutHandoverDetails(StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO);

    List<StockOutHandoverDetails> stockOutHandoverDetailsDTOsToStockOutHandoverDetails(List<StockOutHandoverDetailsDTO> stockOutHandoverDetailsDTOs);

    default StockOutHandover stockOutHandoverFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutHandover stockOutHandover = new StockOutHandover();
        stockOutHandover.setId(id);
        return stockOutHandover;
    }

    default StockOutFrozenTube stockOutFrozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutFrozenTube stockOutFrozenTube = new StockOutFrozenTube();
        stockOutFrozenTube.setId(id);
        return stockOutFrozenTube;
    }
}
