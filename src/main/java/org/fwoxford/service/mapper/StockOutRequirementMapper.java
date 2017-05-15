package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutRequirementDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutRequirement and its DTO StockOutRequirementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutRequirementMapper {

    @Mapping(source = "stockOutApply.id", target = "stockOutApplyId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    StockOutRequirementDTO stockOutRequirementToStockOutRequirementDTO(StockOutRequirement stockOutRequirement);

    List<StockOutRequirementDTO> stockOutRequirementsToStockOutRequirementDTOs(List<StockOutRequirement> stockOutRequirements);

    @Mapping(source = "stockOutApplyId", target = "stockOutApply")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    StockOutRequirement stockOutRequirementDTOToStockOutRequirement(StockOutRequirementDTO stockOutRequirementDTO);

    List<StockOutRequirement> stockOutRequirementDTOsToStockOutRequirements(List<StockOutRequirementDTO> stockOutRequirementDTOs);

    default StockOutApply stockOutApplyFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutApply stockOutApply = new StockOutApply();
        stockOutApply.setId(id);
        return stockOutApply;
    }

    default SampleType sampleTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleType sampleType = new SampleType();
        sampleType.setId(id);
        return sampleType;
    }

    default SampleClassification sampleClassificationFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleClassification sampleClassification = new SampleClassification();
        sampleClassification.setId(id);
        return sampleClassification;
    }

    default FrozenTubeType frozenTubeTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTubeType frozenTubeType = new FrozenTubeType();
        frozenTubeType.setId(id);
        return frozenTubeType;
    }
}
