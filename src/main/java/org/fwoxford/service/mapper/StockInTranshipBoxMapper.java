package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInTranshipBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockInTranshipBox and its DTO StockInTranshipBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInTranshipBoxMapper {

    @Mapping(source = "transhipBox.id", target = "transhipBoxId")
    @Mapping(source = "stockIn.id", target = "stockInId")
    StockInTranshipBoxDTO stockInTranshipBoxToStockInTranshipBoxDTO(StockInTranshipBox stockInTranshipBox);

    List<StockInTranshipBoxDTO> stockInTranshipBoxesToStockInTranshipBoxDTOs(List<StockInTranshipBox> stockInTranshipBoxes);

    @Mapping(source = "transhipBoxId", target = "transhipBox")
    @Mapping(source = "stockInId", target = "stockIn")
    StockInTranshipBox stockInTranshipBoxDTOToStockInTranshipBox(StockInTranshipBoxDTO stockInTranshipBoxDTO);

    List<StockInTranshipBox> stockInTranshipBoxDTOsToStockInTranshipBoxes(List<StockInTranshipBoxDTO> stockInTranshipBoxDTOs);

    default TranshipBox transhipBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        TranshipBox transhipBox = new TranshipBox();
        transhipBox.setId(id);
        return transhipBox;
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
