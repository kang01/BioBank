package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutFilesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutFiles and its DTO StockOutFilesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutFilesMapper {

    StockOutFilesDTO stockOutFilesToStockOutFilesDTO(StockOutFiles stockOutFiles);

    List<StockOutFilesDTO> stockOutFilesToStockOutFilesDTOs(List<StockOutFiles> stockOutFiles);

    StockOutFiles stockOutFilesDTOToStockOutFiles(StockOutFilesDTO stockOutFilesDTO);

    List<StockOutFiles> stockOutFilesDTOsToStockOutFiles(List<StockOutFilesDTO> stockOutFilesDTOs);
}
