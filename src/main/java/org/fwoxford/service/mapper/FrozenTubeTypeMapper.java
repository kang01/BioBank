package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenTubeTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FrozenTubeType and its DTO FrozenTubeTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenTubeTypeMapper {

    FrozenTubeTypeDTO frozenTubeTypeToFrozenTubeTypeDTO(FrozenTubeType frozenTubeType);

    List<FrozenTubeTypeDTO> frozenTubeTypesToFrozenTubeTypeDTOs(List<FrozenTubeType> frozenTubeTypes);

    FrozenTubeType frozenTubeTypeDTOToFrozenTubeType(FrozenTubeTypeDTO frozenTubeTypeDTO);

    List<FrozenTubeType> frozenTubeTypeDTOsToFrozenTubeTypes(List<FrozenTubeTypeDTO> frozenTubeTypeDTOs);
}
