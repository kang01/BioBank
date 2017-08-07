package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.PositionDestroyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PositionDestroy and its DTO PositionDestroyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PositionDestroyMapper {

    PositionDestroyDTO positionDestroyToPositionDestroyDTO(PositionDestroy positionDestroy);

    List<PositionDestroyDTO> positionDestroysToPositionDestroyDTOs(List<PositionDestroy> positionDestroys);

    PositionDestroy positionDestroyDTOToPositionDestroy(PositionDestroyDTO positionDestroyDTO);

    List<PositionDestroy> positionDestroyDTOsToPositionDestroys(List<PositionDestroyDTO> positionDestroyDTOs);
}
