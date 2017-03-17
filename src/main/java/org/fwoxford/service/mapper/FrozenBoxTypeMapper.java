package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenBoxTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FrozenBoxType and its DTO FrozenBoxTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenBoxTypeMapper {

    FrozenBoxTypeDTO frozenBoxTypeToFrozenBoxTypeDTO(FrozenBoxType frozenBoxType);

    List<FrozenBoxTypeDTO> frozenBoxTypesToFrozenBoxTypeDTOs(List<FrozenBoxType> frozenBoxTypes);

    FrozenBoxType frozenBoxTypeDTOToFrozenBoxType(FrozenBoxTypeDTO frozenBoxTypeDTO);

    List<FrozenBoxType> frozenBoxTypeDTOsToFrozenBoxTypes(List<FrozenBoxTypeDTO> frozenBoxTypeDTOs);
}
