package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FrozenBox and its DTO FrozenBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenBoxMapper {

    @Mapping(source = "frozenBoxType.id", target = "frozenBoxTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    FrozenBoxDTO frozenBoxToFrozenBoxDTO(FrozenBox frozenBox);

    List<FrozenBoxDTO> frozenBoxesToFrozenBoxDTOs(List<FrozenBox> frozenBoxes);

    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    FrozenBox frozenBoxDTOToFrozenBox(FrozenBoxDTO frozenBoxDTO);

    List<FrozenBox> frozenBoxDTOsToFrozenBoxes(List<FrozenBoxDTO> frozenBoxDTOs);

    default FrozenBoxType frozenBoxTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBoxType frozenBoxType = new FrozenBoxType();
        frozenBoxType.setId(id);
        return frozenBoxType;
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

    default ProjectSite projectSiteFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectSite projectSite = new ProjectSite();
        projectSite.setId(id);
        return projectSite;
    }
}
