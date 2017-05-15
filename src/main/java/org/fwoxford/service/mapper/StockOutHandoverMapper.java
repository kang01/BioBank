package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutHandoverDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutHandover and its DTO StockOutHandoverDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutHandoverMapper {

    @Mapping(source = "stockOutTask.id", target = "stockOutTaskId")
    StockOutHandoverDTO stockOutHandoverToStockOutHandoverDTO(StockOutHandover stockOutHandover);

    List<StockOutHandoverDTO> stockOutHandoversToStockOutHandoverDTOs(List<StockOutHandover> stockOutHandovers);

    @Mapping(source = "stockOutTaskId", target = "stockOutTask")
    StockOutHandover stockOutHandoverDTOToStockOutHandover(StockOutHandoverDTO stockOutHandoverDTO);

    List<StockOutHandover> stockOutHandoverDTOsToStockOutHandovers(List<StockOutHandoverDTO> stockOutHandoverDTOs);

    default StockOutTask stockOutTaskFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.setId(id);
        return stockOutTask;
    }
}
