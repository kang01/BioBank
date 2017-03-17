package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ProjectSiteDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectSite and its DTO ProjectSiteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectSiteMapper {

    ProjectSiteDTO projectSiteToProjectSiteDTO(ProjectSite projectSite);

    List<ProjectSiteDTO> projectSitesToProjectSiteDTOs(List<ProjectSite> projectSites);

    @Mapping(target = "projectRelates", ignore = true)
    ProjectSite projectSiteDTOToProjectSite(ProjectSiteDTO projectSiteDTO);

    List<ProjectSite> projectSiteDTOsToProjectSites(List<ProjectSiteDTO> projectSiteDTOs);
}
