package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ProjectRelateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectRelate and its DTO ProjectRelateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectRelateMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.projectCode", target = "projectProjectCode")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "projectSite.projectSiteCode", target = "projectSiteProjectSiteCode")
    ProjectRelateDTO projectRelateToProjectRelateDTO(ProjectRelate projectRelate);

    List<ProjectRelateDTO> projectRelatesToProjectRelateDTOs(List<ProjectRelate> projectRelates);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    ProjectRelate projectRelateDTOToProjectRelate(ProjectRelateDTO projectRelateDTO);

    List<ProjectRelate> projectRelateDTOsToProjectRelates(List<ProjectRelateDTO> projectRelateDTOs);

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
