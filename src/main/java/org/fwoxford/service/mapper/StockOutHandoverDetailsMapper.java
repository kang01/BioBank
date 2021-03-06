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

//    @Mapping(source = "stockOutHandover.id", target = "stockOutHandoverId")
//    @Mapping(source = "stockOutBoxTube.id", target = "stockOutBoxTubeId")
    @Mapping(source = "stockOutReqFrozenTube.id", target = "stockOutReqFrozenTubeId")
    @Mapping(source = "stockOutHandoverBox.id", target = "stockOutHandoverBoxId")
    StockOutHandoverDetailsDTO stockOutHandoverDetailsToStockOutHandoverDetailsDTO(StockOutHandoverDetails stockOutHandoverDetails);

    List<StockOutHandoverDetailsDTO> stockOutHandoverDetailsToStockOutHandoverDetailsDTOs(List<StockOutHandoverDetails> stockOutHandoverDetails);

//    @Mapping(source = "stockOutHandoverId", target = "stockOutHandover")
//    @Mapping(source = "stockOutBoxTubeId", target = "stockOutBoxTube")
    @Mapping(source = "stockOutReqFrozenTubeId", target = "stockOutReqFrozenTube")
    @Mapping(source = "stockOutHandoverBoxId", target = "stockOutHandoverBox")
    StockOutHandoverDetails stockOutHandoverDetailsDTOToStockOutHandoverDetails(StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO);

    List<StockOutHandoverDetails> stockOutHandoverDetailsDTOsToStockOutHandoverDetails(List<StockOutHandoverDetailsDTO> stockOutHandoverDetailsDTOs);

//    default StockOutHandover stockOutHandoverFromId(Long id) {
//        if (id == null) {
//            return null;
//        }
//        StockOutHandover stockOutHandover = new StockOutHandover();
//        stockOutHandover.setId(id);
//        return stockOutHandover;
//    }
//
//    default StockOutBoxTube stockOutBoxTubeFromId(Long id) {
//        if (id == null) {
//            return null;
//        }
//        StockOutBoxTube stockOutBoxTube = new StockOutBoxTube();
//        stockOutBoxTube.setId(id);
//        return stockOutBoxTube;
//    }

    default StockOutReqFrozenTube stockOutReqFrozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
        stockOutReqFrozenTube.setId(id);
        return stockOutReqFrozenTube;
    }

    default StockOutHandoverBox stockOutHandoverBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutHandoverBox stockOutHandoverBox = new StockOutHandoverBox();
        stockOutHandoverBox.setId(id);
        return stockOutHandoverBox;
    }
}
