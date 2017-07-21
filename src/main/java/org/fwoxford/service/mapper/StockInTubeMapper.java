package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInTubeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockInTube and its DTO StockInTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInTubeMapper {

    @Mapping(source = "stockInBox.id", target = "stockInBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockInTubeDTO stockInTubeToStockInTubeDTO(StockInTube stockInTube);

    List<StockInTubeDTO> stockInTubesToStockInTubeDTOs(List<StockInTube> stockInTubes);

    @Mapping(source = "stockInBoxId", target = "stockInBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockInTube stockInTubeDTOToStockInTube(StockInTubeDTO stockInTubeDTO);

    List<StockInTube> stockInTubeDTOsToStockInTubes(List<StockInTubeDTO> stockInTubeDTOs);

    default StockInBox stockInBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInBox stockInBox = new StockInBox();
        stockInBox.setId(id);
        return stockInBox;
    }

    default FrozenTube frozenTubeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(id);
        return frozenTube;
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
