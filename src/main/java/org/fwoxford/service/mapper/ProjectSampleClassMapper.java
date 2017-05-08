package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ProjectSampleClassDTO;

import org.fwoxford.service.dto.ProjectSampleClassificationDTO;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity ProjectSampleClass and its DTO ProjectSampleClassDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectSampleClassMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    ProjectSampleClassDTO projectSampleClassToProjectSampleClassDTO(ProjectSampleClass projectSampleClass);

    List<ProjectSampleClassDTO> projectSampleClassesToProjectSampleClassDTOs(List<ProjectSampleClass> projectSampleClasses);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    ProjectSampleClass projectSampleClassDTOToProjectSampleClass(ProjectSampleClassDTO projectSampleClassDTO);

    List<ProjectSampleClass> projectSampleClassDTOsToProjectSampleClasses(List<ProjectSampleClassDTO> projectSampleClassDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }

    default SampleType sampleTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleType sampleType = new SampleType();
        sampleType.setId(id);
        return sampleType;
    }

    default SampleClassification sampleClassificationFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleClassification sampleClassification = new SampleClassification();
        sampleClassification.setId(id);
        return sampleClassification;
    }

    default List<ProjectSampleClassificationDTO> projectSampleClassesToProjectClassificationDTOs(List<ProjectSampleClass> projectSampleClasses){
        if(projectSampleClasses == null){
            return null;
        }
        List<ProjectSampleClassificationDTO> projectSampleClassificationDTOS = new ArrayList<ProjectSampleClassificationDTO>();
        for(ProjectSampleClass p: projectSampleClasses){
            projectSampleClassificationDTOS.add(projectSampleClassToProjectClassificationDTO(p));
        }
        return projectSampleClassificationDTOS;
    }

    default ProjectSampleClassificationDTO projectSampleClassToProjectClassificationDTO(ProjectSampleClass p){
        if(p == null){
            return null;
        }
        ProjectSampleClassificationDTO dto = new ProjectSampleClassificationDTO();
        dto.setSampleClassificationName(p.getSampleClassification().getSampleClassificationName());
        dto.setSampleClassificationId(p.getSampleClassification().getId());
        dto.setBackColor(p.getSampleClassification().getBackColor());
        dto.setFrontColor(p.getSampleClassification().getFrontColor());
        dto.setColumnsNumber(p.getColumnsNumber());
        return dto;
    }
}
