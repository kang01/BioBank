package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockIn and its DTO StockInDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInMapper {

    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockInDTO stockInToStockInDTO(StockIn stockIn);

    List<StockInDTO> stockInsToStockInDTOs(List<StockIn> stockIns);

    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockIn stockInDTOToStockIn(StockInDTO stockInDTO);

    List<StockIn> stockInDTOsToStockIns(List<StockInDTO> stockInDTOS);

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
