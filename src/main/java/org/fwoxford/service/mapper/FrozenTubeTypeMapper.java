package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenTubeTypeDTO;

import org.fwoxford.service.dto.response.FrozenTubeTypeResponse;
import org.mapstruct.*;

import java.util.ArrayList;
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

    default List<FrozenTubeTypeResponse> frozenTubeTypesToFrozenTubeTypeResponses(List<FrozenTubeType> frozenTubeTypes){
        if ( frozenTubeTypes == null ) {
            return null;
        }

        List<FrozenTubeTypeResponse> list = new ArrayList<FrozenTubeTypeResponse>();
        for ( FrozenTubeType frozenTubeType : frozenTubeTypes ) {
            list.add( frozenTubeTypeToFrozenTubeTypeResponse( frozenTubeType ) );
        }

        return list;
    }

    default FrozenTubeTypeResponse frozenTubeTypeToFrozenTubeTypeResponse(FrozenTubeType frozenTubeType){
        if(frozenTubeType == null){
            return null;
        }
        FrozenTubeTypeResponse res = new FrozenTubeTypeResponse();
        res.setId(frozenTubeType.getId());
        res.setFrozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode());
        res.setFrozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName());
        return res;
    }
}
