package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ProjectSampleClassDTO;

import org.mapstruct.*;
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
}
