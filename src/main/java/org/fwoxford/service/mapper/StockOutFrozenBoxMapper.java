package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutFrozenBox and its DTO StockOutFrozenBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutFrozenBoxMapper {

    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "stockOutTask.id", target = "stockOutTaskId")
    StockOutFrozenBoxDTO stockOutFrozenBoxToStockOutFrozenBoxDTO(StockOutFrozenBox stockOutFrozenBox);

    List<StockOutFrozenBoxDTO> stockOutFrozenBoxesToStockOutFrozenBoxDTOs(List<StockOutFrozenBox> stockOutFrozenBoxes);

    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "stockOutTaskId", target = "stockOutTask")
    StockOutFrozenBox stockOutFrozenBoxDTOToStockOutFrozenBox(StockOutFrozenBoxDTO stockOutFrozenBoxDTO);

    List<StockOutFrozenBox> stockOutFrozenBoxDTOsToStockOutFrozenBoxes(List<StockOutFrozenBoxDTO> stockOutFrozenBoxDTOs);

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }

    default StockOutTask stockOutTaskFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.setId(id);
        return stockOutTask;
    }
}
