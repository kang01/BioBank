package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.PositionChangeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PositionChange and its DTO PositionChangeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PositionChangeMapper {

    PositionChangeDTO positionChangeToPositionChangeDTO(PositionChange positionChange);

    List<PositionChangeDTO> positionChangesToPositionChangeDTOs(List<PositionChange> positionChanges);

    PositionChange positionChangeDTOToPositionChange(PositionChangeDTO positionChangeDTO);

    List<PositionChange> positionChangeDTOsToPositionChanges(List<PositionChangeDTO> positionChangeDTOs);
}
