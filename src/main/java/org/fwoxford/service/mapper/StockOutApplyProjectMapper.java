package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutApplyProjectDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutApplyProject and its DTO StockOutApplyProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutApplyProjectMapper {

    @Mapping(source = "stockOutApply.id", target = "stockOutApplyId")
    @Mapping(source = "project.id", target = "projectId")
    StockOutApplyProjectDTO stockOutApplyProjectToStockOutApplyProjectDTO(StockOutApplyProject stockOutApplyProject);

    List<StockOutApplyProjectDTO> stockOutApplyProjectsToStockOutApplyProjectDTOs(List<StockOutApplyProject> stockOutApplyProjects);

    @Mapping(source = "stockOutApplyId", target = "stockOutApply")
    @Mapping(source = "projectId", target = "project")
    StockOutApplyProject stockOutApplyProjectDTOToStockOutApplyProject(StockOutApplyProjectDTO stockOutApplyProjectDTO);

    List<StockOutApplyProject> stockOutApplyProjectDTOsToStockOutApplyProjects(List<StockOutApplyProjectDTO> stockOutApplyProjectDTOs);

    default StockOutApply stockOutApplyFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutApply stockOutApply = new StockOutApply();
        stockOutApply.setId(id);
        return stockOutApply;
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
