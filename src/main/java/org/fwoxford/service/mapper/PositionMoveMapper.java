package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.PositionMoveDTO;

import org.fwoxford.service.dto.response.PositionMoveBoxDTO;
import org.fwoxford.service.dto.response.PositionMoveSampleDTO;
import org.fwoxford.service.dto.response.PositionMoveShelvesDTO;
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

    default  PositionMove positionMoveSampleDTOToPositionMove(PositionMoveSampleDTO positionMoveDTO){
        if ( positionMoveDTO == null ) {
            return null;
        }

        PositionMove positionMove = new PositionMove();

        positionMove.setCreatedBy( positionMoveDTO.getCreatedBy() );
        positionMove.setCreatedDate( positionMoveDTO.getCreatedDate() );
        positionMove.setLastModifiedBy( positionMoveDTO.getLastModifiedBy() );
        positionMove.setLastModifiedDate( positionMoveDTO.getLastModifiedDate() );
        positionMove.setId( positionMoveDTO.getId() );
        positionMove.setMoveReason( positionMoveDTO.getMoveReason() );
        positionMove.setMoveAffect( positionMoveDTO.getMoveAffect() );
        positionMove.setWhetherFreezingAndThawing( positionMoveDTO.getWhetherFreezingAndThawing() );
        positionMove.setMoveType( positionMoveDTO.getMoveType() );
        positionMove.setOperatorId1( positionMoveDTO.getOperatorId1() );
        positionMove.setOperatorId2( positionMoveDTO.getOperatorId2() );
        positionMove.setStatus( positionMoveDTO.getStatus() );
        positionMove.setMemo( positionMoveDTO.getMemo() );

        return positionMove;
    }
    default  PositionMove positionMoveBoxDTOToPositionMove(PositionMoveBoxDTO positionMoveDTO){
        if ( positionMoveDTO == null ) {
            return null;
        }

        PositionMove positionMove = new PositionMove();

        positionMove.setCreatedBy( positionMoveDTO.getCreatedBy() );
        positionMove.setCreatedDate( positionMoveDTO.getCreatedDate() );
        positionMove.setLastModifiedBy( positionMoveDTO.getLastModifiedBy() );
        positionMove.setLastModifiedDate( positionMoveDTO.getLastModifiedDate() );
        positionMove.setId( positionMoveDTO.getId() );
        positionMove.setMoveReason( positionMoveDTO.getMoveReason() );
        positionMove.setMoveAffect( positionMoveDTO.getMoveAffect() );
        positionMove.setWhetherFreezingAndThawing( positionMoveDTO.getWhetherFreezingAndThawing() );
        positionMove.setMoveType( positionMoveDTO.getMoveType() );
        positionMove.setOperatorId1( positionMoveDTO.getOperatorId1() );
        positionMove.setOperatorId2( positionMoveDTO.getOperatorId2() );
        positionMove.setStatus( positionMoveDTO.getStatus() );
        positionMove.setMemo( positionMoveDTO.getMemo() );

        return positionMove;
    }
    default  PositionMove positionMoveShelvesDTOToPositionMove(PositionMoveShelvesDTO positionMoveDTO){
        if ( positionMoveDTO == null ) {
            return null;
        }

        PositionMove positionMove = new PositionMove();

        positionMove.setCreatedBy( positionMoveDTO.getCreatedBy() );
        positionMove.setCreatedDate( positionMoveDTO.getCreatedDate() );
        positionMove.setLastModifiedBy( positionMoveDTO.getLastModifiedBy() );
        positionMove.setLastModifiedDate( positionMoveDTO.getLastModifiedDate() );
        positionMove.setId( positionMoveDTO.getId() );
        positionMove.setMoveReason( positionMoveDTO.getMoveReason() );
        positionMove.setMoveAffect( positionMoveDTO.getMoveAffect() );
        positionMove.setWhetherFreezingAndThawing( positionMoveDTO.getWhetherFreezingAndThawing() );
        positionMove.setMoveType( positionMoveDTO.getMoveType() );
        positionMove.setOperatorId1( positionMoveDTO.getOperatorId1() );
        positionMove.setOperatorId2( positionMoveDTO.getOperatorId2() );
        positionMove.setStatus( positionMoveDTO.getStatus() );
        positionMove.setMemo( positionMoveDTO.getMemo() );

        return positionMove;
    }
}
