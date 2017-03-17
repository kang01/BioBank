package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.SupportRackTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SupportRackType and its DTO SupportRackTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SupportRackTypeMapper {

    SupportRackTypeDTO supportRackTypeToSupportRackTypeDTO(SupportRackType supportRackType);

    List<SupportRackTypeDTO> supportRackTypesToSupportRackTypeDTOs(List<SupportRackType> supportRackTypes);

    SupportRackType supportRackTypeDTOToSupportRackType(SupportRackTypeDTO supportRackTypeDTO);

    List<SupportRackType> supportRackTypeDTOsToSupportRackTypes(List<SupportRackTypeDTO> supportRackTypeDTOs);
}
