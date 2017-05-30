package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.BoxAndTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BoxAndTube and its DTO BoxAndTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BoxAndTubeMapper {

    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    BoxAndTubeDTO boxAndTubeToBoxAndTubeDTO(BoxAndTube boxAndTube);

    List<BoxAndTubeDTO> boxAndTubesToBoxAndTubeDTOs(List<BoxAndTube> boxAndTubes);

    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    BoxAndTube boxAndTubeDTOToBoxAndTube(BoxAndTubeDTO boxAndTubeDTO);

    List<BoxAndTube> boxAndTubeDTOsToBoxAndTubes(List<BoxAndTubeDTO> boxAndTubeDTOs);

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
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
