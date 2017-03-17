package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FrozenTube and its DTO FrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenTubeMapper {

    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    FrozenTubeDTO frozenTubeToFrozenTubeDTO(FrozenTube frozenTube);

    List<FrozenTubeDTO> frozenTubesToFrozenTubeDTOs(List<FrozenTube> frozenTubes);

    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    FrozenTube frozenTubeDTOToFrozenTube(FrozenTubeDTO frozenTubeDTO);

    List<FrozenTube> frozenTubeDTOsToFrozenTubes(List<FrozenTubeDTO> frozenTubeDTOs);

    default FrozenTubeType frozenTubeTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTubeType frozenTubeType = new FrozenTubeType();
        frozenTubeType.setId(id);
        return frozenTubeType;
    }

    default SampleType sampleTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleType sampleType = new SampleType();
        sampleType.setId(id);
        return sampleType;
    }
}
