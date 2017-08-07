package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TranshipStockInDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TranshipStockIn and its DTO TranshipStockInDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipStockInMapper {

    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "stockIn.id", target = "stockInId")
    TranshipStockInDTO transhipStockInToTranshipStockInDTO(TranshipStockIn transhipStockIn);

    List<TranshipStockInDTO> transhipStockInsToTranshipStockInDTOs(List<TranshipStockIn> transhipStockIns);

    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "stockInId", target = "stockIn")
    TranshipStockIn transhipStockInDTOToTranshipStockIn(TranshipStockInDTO transhipStockInDTO);

    List<TranshipStockIn> transhipStockInDTOsToTranshipStockIns(List<TranshipStockInDTO> transhipStockInDTOs);

    default Tranship transhipFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tranship tranship = new Tranship();
        tranship.setId(id);
        return tranship;
    }

    default StockIn stockInFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockIn stockIn = new StockIn();
        stockIn.setId(id);
        return stockIn;
    }
}
