package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.PositionMoveDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PositionMove and its DTO PositionMoveDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PositionMoveMapper {

    PositionMoveDTO positionMoveToPositionMoveDTO(PositionMove positionMove);

    List<PositionMoveDTO> positionMovesToPositionMoveDTOs(List<PositionMove> positionMoves);

    PositionMove positionMoveDTOToPositionMove(PositionMoveDTO positionMoveDTO);

    List<PositionMove> positionMoveDTOsToPositionMoves(List<PositionMoveDTO> positionMoveDTOs);
}
