package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.RelationsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Relations and its DTO RelationsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RelationsMapper {

    @Mapping(source = "frozenBoxType.id", target = "frozenBoxTypeId")
    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "project.id", target = "projectId")
    RelationsDTO relationsToRelationsDTO(Relations relations);

    List<RelationsDTO> relationsToRelationsDTOs(List<Relations> relations);

    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "projectId", target = "project")
    Relations relationsDTOToRelations(RelationsDTO relationsDTO);

    List<Relations> relationsDTOsToRelations(List<RelationsDTO> relationsDTOs);

    default FrozenBoxType frozenBoxTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBoxType frozenBoxType = new FrozenBoxType();
        frozenBoxType.setId(id);
        return frozenBoxType;
    }

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

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
