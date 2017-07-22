package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockInBox and its DTO StockInBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInBoxMapper {

    @Mapping(source = "stockIn.id", target = "stockInId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "frozenBoxType.id", target = "frozenBoxTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockInBoxDTO stockInBoxToStockInBoxDTO(StockInBox stockInBox);

    List<StockInBoxDTO> stockInBoxesToStockInBoxDTOs(List<StockInBox> stockInBoxes);

    @Mapping(source = "stockInId", target = "stockIn")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "supportRackId", target = "supportRack")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockInBox stockInBoxDTOToStockInBox(StockInBoxDTO stockInBoxDTO);

    List<StockInBox> stockInBoxDTOsToStockInBoxes(List<StockInBoxDTO> stockInBoxDTOS);

    default StockIn stockInFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockIn stockIn = new StockIn();
        stockIn.setId(id);
        return stockIn;
    }

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }
    default Equipment equipmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }

    default SupportRack supportRackFromId(Long id) {
        if (id == null) {
            return null;
        }
        SupportRack supportRack = new SupportRack();
        supportRack.setId(id);
        return supportRack;
    }
    default Area areaFromId(Long id) {
        if (id == null) {
            return null;
        }
        Area area = new Area();
        area.setId(id);
        return area;
    }
    default FrozenBoxType frozenBoxTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBoxType frozenBoxType = new FrozenBoxType();
        frozenBoxType.setId(id);
        return frozenBoxType;
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
