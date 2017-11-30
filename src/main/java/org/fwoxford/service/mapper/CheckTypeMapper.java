package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.CheckTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CheckType and its DTO CheckTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CheckTypeMapper {

    CheckTypeDTO checkTypeToCheckTypeDTO(CheckType checkType);

    List<CheckTypeDTO> checkTypesToCheckTypeDTOs(List<CheckType> checkTypes);

    CheckType checkTypeDTOToCheckType(CheckTypeDTO checkTypeDTO);

    List<CheckType> checkTypeDTOsToCheckTypes(List<CheckTypeDTO> checkTypeDTOs);
}
