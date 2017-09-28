package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutReqFrozenTube and its DTO StockOutReqFrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutReqFrozenTubeMapper {

    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "stockOutRequirement.id", target = "stockOutRequirementId")

    @Mapping(source = "stockOutFrozenBox.id", target = "stockOutFrozenBoxId")
    @Mapping(source = "stockOutTask.id", target = "stockOutTaskId")
    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockOutReqFrozenTubeDTO stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(StockOutReqFrozenTube stockOutReqFrozenTube);

    List<StockOutReqFrozenTubeDTO> stockOutReqFrozenTubesToStockOutReqFrozenTubeDTOs(List<StockOutReqFrozenTube> stockOutReqFrozenTubes);

    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "stockOutRequirementId", target = "stockOutRequirement")

    @Mapping(source = "stockOutFrozenBoxId", target = "stockOutFrozenBox")
    @Mapping(source = "stockOutTaskId", target = "stockOutTask")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockOutReqFrozenTube stockOutReqFrozenTubeDTOToStockOutReqFrozenTube(StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO);

    List<StockOutReqFrozenTube> stockOutReqFrozenTubeDTOsToStockOutReqFrozenTubes(List<StockOutReqFrozenTubeDTO> stockOutReqFrozenTubeDTOs);

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }

    default FrozenTube frozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(id);
        return frozenTube;
    }

    default StockOutRequirement stockOutRequirementFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutRequirement stockOutRequirement = new StockOutRequirement();
        stockOutRequirement.setId(id);
        return stockOutRequirement;
    }





    default StockOutTask stockOutTaskFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.setId(id);
        return stockOutTask;
    }

    default StockOutFrozenBox stockOutFrozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
        stockOutFrozenBox.setId(id);
        return stockOutFrozenBox;
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
