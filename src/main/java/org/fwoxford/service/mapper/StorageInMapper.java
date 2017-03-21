package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StorageInDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StorageIn and its DTO StorageInDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StorageInMapper {

    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StorageInDTO storageInToStorageInDTO(StorageIn storageIn);

    List<StorageInDTO> storageInsToStorageInDTOs(List<StorageIn> storageIns);

    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StorageIn storageInDTOToStorageIn(StorageInDTO storageInDTO);

    List<StorageIn> storageInDTOsToStorageIns(List<StorageInDTO> storageInDTOs);

    default Tranship transhipFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tranship tranship = new Tranship();
        tranship.setId(id);
        return tranship;
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
