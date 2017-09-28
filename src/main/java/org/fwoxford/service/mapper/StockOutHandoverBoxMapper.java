package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutHandoverBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutHandoverBox and its DTO StockOutHandoverBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutHandoverBoxMapper {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    @Mapping(source = "stockOutFrozenBox.id", target = "stockOutFrozenBoxId")
    @Mapping(source = "frozenBoxType.id", target = "frozenBoxTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockOutHandoverBoxDTO stockOutHandoverBoxToStockOutHandoverBoxDTO(StockOutHandoverBox stockOutHandoverBox);

    List<StockOutHandoverBoxDTO> stockOutHandoverBoxesToStockOutHandoverBoxDTOs(List<StockOutHandoverBox> stockOutHandoverBoxes);

    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "supportRackId", target = "supportRack")
    @Mapping(source = "stockOutFrozenBoxId", target = "stockOutFrozenBox")
    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockOutHandoverBox stockOutHandoverBoxDTOToStockOutHandoverBox(StockOutHandoverBoxDTO stockOutHandoverBoxDTO);

    List<StockOutHandoverBox> stockOutHandoverBoxDTOsToStockOutHandoverBoxes(List<StockOutHandoverBoxDTO> stockOutHandoverBoxDTOs);

    default Equipment equipmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }

    default Area areaFromId(Long id) {
        if (id == null) {
            return null;
        }
        Area area = new Area();
        area.setId(id);
        return area;
    }

    default SupportRack supportRackFromId(Long id) {
        if (id == null) {
            return null;
        }
        SupportRack supportRack = new SupportRack();
        supportRack.setId(id);
        return supportRack;
    }

    default StockOutFrozenBox stockOutFrozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
        stockOutFrozenBox.setId(id);
        return stockOutFrozenBox;
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
