package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TranshipDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tranship and its DTO TranshipDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    TranshipDTO transhipToTranshipDTO(Tranship tranship);

    List<TranshipDTO> transhipsToTranshipDTOs(List<Tranship> tranships);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    Tranship transhipDTOToTranship(TranshipDTO transhipDTO);

    List<Tranship> transhipDTOsToTranships(List<TranshipDTO> transhipDTOs);

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
