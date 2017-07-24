package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutBoxTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutBoxTube and its DTO StockOutBoxTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutBoxTubeMapper {

    @Mapping(source = "stockOutFrozenBox.id", target = "stockOutFrozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "stockOutTaskFrozenTube.id", target = "stockOutTaskFrozenTubeId")
    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockOutBoxTubeDTO stockOutBoxTubeToStockOutBoxTubeDTO(StockOutBoxTube stockOutBoxTube);

    List<StockOutBoxTubeDTO> stockOutBoxTubesToStockOutBoxTubeDTOs(List<StockOutBoxTube> stockOutBoxTubes);

    @Mapping(source = "stockOutFrozenBoxId", target = "stockOutFrozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "stockOutTaskFrozenTubeId", target = "stockOutTaskFrozenTube")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockOutBoxTube stockOutBoxTubeDTOToStockOutBoxTube(StockOutBoxTubeDTO stockOutBoxTubeDTO);

    List<StockOutBoxTube> stockOutBoxTubeDTOsToStockOutBoxTubes(List<StockOutBoxTubeDTO> stockOutBoxTubeDTOs);

    default StockOutFrozenBox stockOutFrozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
        stockOutFrozenBox.setId(id);
        return stockOutFrozenBox;
    }

    default FrozenTube frozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(id);
        return frozenTube;
    }

    default StockOutTaskFrozenTube stockOutTaskFrozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTaskFrozenTube stockOutTaskFrozenTube = new StockOutTaskFrozenTube();
        stockOutTaskFrozenTube.setId(id);
        return stockOutTaskFrozenTube;
    }
    default FrozenTubeType frozenTubeTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTubeType frozenTubeType = new FrozenTubeType();
        frozenTubeType.setId(id);
        return frozenTubeType;
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
    default SampleClassification sampleClassificationFromId(Long id){
        if (id == null) {
            return null;
        }
        SampleClassification sampleClassification = new SampleClassification();
        sampleClassification.setId(id);
        return sampleClassification;
    }
}
