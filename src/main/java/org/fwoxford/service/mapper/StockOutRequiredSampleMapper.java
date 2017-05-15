package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutRequiredSample and its DTO StockOutRequiredSampleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutRequiredSampleMapper {

    @Mapping(source = "stockOutRequirement.id", target = "stockOutRequirementId")
    StockOutRequiredSampleDTO stockOutRequiredSampleToStockOutRequiredSampleDTO(StockOutRequiredSample stockOutRequiredSample);

    List<StockOutRequiredSampleDTO> stockOutRequiredSamplesToStockOutRequiredSampleDTOs(List<StockOutRequiredSample> stockOutRequiredSamples);

    @Mapping(source = "stockOutRequirementId", target = "stockOutRequirement")
    StockOutRequiredSample stockOutRequiredSampleDTOToStockOutRequiredSample(StockOutRequiredSampleDTO stockOutRequiredSampleDTO);

    List<StockOutRequiredSample> stockOutRequiredSampleDTOsToStockOutRequiredSamples(List<StockOutRequiredSampleDTO> stockOutRequiredSampleDTOs);

    default StockOutRequirement stockOutRequirementFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutRequirement stockOutRequirement = new StockOutRequirement();
        stockOutRequirement.setId(id);
        return stockOutRequirement;
    }
}
