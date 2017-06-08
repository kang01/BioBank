package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TranshipTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TranshipTube and its DTO TranshipTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipTubeMapper {

    @Mapping(source = "transhipBox.id", target = "transhipBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    TranshipTubeDTO transhipTubeToTranshipTubeDTO(TranshipTube transhipTube);

    List<TranshipTubeDTO> transhipTubesToTranshipTubeDTOs(List<TranshipTube> transhipTubes);

    @Mapping(source = "transhipBoxId", target = "transhipBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    TranshipTube transhipTubeDTOToTranshipTube(TranshipTubeDTO transhipTubeDTO);

    List<TranshipTube> transhipTubeDTOsToTranshipTubes(List<TranshipTubeDTO> transhipTubeDTOs);

    default TranshipBox transhipBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        TranshipBox transhipBox = new TranshipBox();
        transhipBox.setId(id);
        return transhipBox;
    }

    default FrozenTube frozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(id);
        return frozenTube;
    }
}
