package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ProjectDTO;

import org.fwoxford.service.dto.response.ProjectResponse;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper {

    @Mapping(source = "delegate.id", target = "delegateId")
    ProjectDTO projectToProjectDTO(Project project);

    List<ProjectDTO> projectsToProjectDTOs(List<Project> projects);

    @Mapping(target = "projectRelates", ignore = true)
    @Mapping(source = "delegateId", target = "delegate")
    Project projectDTOToProject(ProjectDTO projectDTO);

    List<Project> projectDTOsToProjects(List<ProjectDTO> projectDTOs);

    default Delegate delegateFromId(Long id) {
        if (id == null) {
            return null;
        }
        Delegate delegate = new Delegate();
        delegate.setId(id);
        return delegate;
    }

    default List<ProjectResponse> projectsToProjectResponses(List<Project> projects){
        if ( projects == null ) {
            return null;
        }

        List<ProjectResponse> list = new ArrayList<ProjectResponse>();
        for ( Project project : projects ) {
            list.add( projectToProjectResponse( project ) );
        }

        return list;
    }
    default ProjectResponse projectToProjectResponse(Project project){
        if(project == null){
            return null;
        }
        ProjectResponse res = new ProjectResponse();
        res.setId(project.getId());
        res.setProjectCode(project.getProjectCode());
        res.setProjectName(project.getProjectName());
        return res;
    }
}
