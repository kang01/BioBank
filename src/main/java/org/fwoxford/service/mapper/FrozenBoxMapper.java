package org.fwoxford.service.mapper;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenBoxForSaveBatchDTO;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.FrozenBoxDTO;

import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.web.rest.util.BankUtil;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity FrozenBox and its DTO FrozenBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenBoxMapper {

    @Mapping(source = "frozenBoxType.id", target = "frozenBoxTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    FrozenBoxDTO frozenBoxToFrozenBoxDTO(FrozenBox frozenBox);

    List<FrozenBoxDTO> frozenBoxesToFrozenBoxDTOs(List<FrozenBox> frozenBoxes);

    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "supportRackId", target = "supportRack")
    FrozenBox frozenBoxDTOToFrozenBox(FrozenBoxDTO frozenBoxDTO);

    List<FrozenBox> frozenBoxDTOsToFrozenBoxes(List<FrozenBoxDTO> frozenBoxDTOs);

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

    default FrozenBoxAndFrozenTubeResponse forzenBoxAndTubeToResponse(FrozenBox frozenBox){
        if(frozenBox == null){
            return null;
        }
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        res.setId(frozenBox.getId());
        res.setStatus(frozenBox.getStatus());
        res.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        res.setFrozenBoxType(frozenBox.getFrozenBoxType());
        res.setIsSplit(frozenBox.getIsSplit());
        res.setMemo(frozenBox.getMemo());
        res.setSampleType(frozenBox.getSampleType());
        res.setSampleClassification(frozenBox.getSampleClassification());
        res.setEquipmentId(frozenBox.getEquipment()!=null?frozenBox.getEquipment().getId():null);
        res.setAreaId(frozenBox.getArea()!=null?frozenBox.getArea().getId():null);
        res.setSupportRackId(frozenBox.getSupportRack()!=null?frozenBox.getSupportRack().getId():null);
        res.setColumnsInShelf(frozenBox.getColumnsInShelf());
        res.setRowsInShelf(frozenBox.getRowsInShelf());
        res.setProjectId(frozenBox.getProject()!=null?frozenBox.getProject().getId():null);
        res.setProjectCode(frozenBox.getProjectCode());
        res.setProjectName(frozenBox.getProjectName());
        SampleType sampleType = frozenBox.getSampleType();
        if(sampleType!=null){
            res.setSampleTypeId(sampleType.getId());
            res.setSampleTypeCode(sampleType.getSampleTypeCode());
            res.setSampleTypeName(sampleType.getSampleTypeName());
            res.setIsMixed(sampleType.getIsMixed());
            res.setFrontColor(sampleType.getFrontColor());
            res.setBackColor(sampleType.getBackColor());
        }
        FrozenBoxType frozenBoxType = frozenBox.getFrozenBoxType();
        if(frozenBoxType!=null){
            res.setFrozenBoxTypeId(frozenBoxType.getId());
            res.setFrozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode());
            res.setFrozenBoxTypeName(frozenBoxType.getFrozenBoxTypeName());
            res.setFrozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns());
            res.setFrozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows());
        }
        SampleClassification sampleClassification = frozenBox.getSampleClassification();
       if(sampleClassification != null){
           res.setSampleClassificationId(sampleClassification.getId());
           res.setSampleClassificationName(sampleClassification.getSampleClassificationName());
           res.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
           res.setFrontColorForClass(sampleClassification.getFrontColor());
           res.setBackColorForClass(sampleClassification.getBackColor());
       }
        return res;
    }

    default FrozenBox frozenBoxForSaveBatchDTOToFrozenBox(FrozenBoxForSaveBatchDTO frozenBoxDTO){
        if ( frozenBoxDTO == null ) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setArea( areaFromId( frozenBoxDTO.getAreaId() ) );
        frozenBox.setSupportRack( supportRackFromId( frozenBoxDTO.getSupportRackId() ) );
        frozenBox.setFrozenBoxType( frozenBoxTypeFromId( frozenBoxDTO.getFrozenBoxTypeId() ) );
        frozenBox.setEquipment( equipmentFromId( frozenBoxDTO.getEquipmentId() ) );
        frozenBox.setSampleType( sampleTypeFromId( frozenBoxDTO.getSampleTypeId() ) );
        frozenBox.setSampleClassification(sampleClassificationFromId(frozenBoxDTO.getSampleClassificationId()));
        frozenBox.setId( frozenBoxDTO.getId() );
        frozenBox.setFrozenBoxCode( frozenBoxDTO.getFrozenBoxCode() );
        frozenBox.setIsSplit( frozenBoxDTO.getIsSplit()!=null?frozenBoxDTO.getIsSplit():0 );
        frozenBox.setMemo( frozenBoxDTO.getMemo() );
        frozenBox.setStatus( frozenBoxDTO.getStatus() );
        frozenBox.setEmptyTubeNumber( frozenBoxDTO.getEmptyTubeNumber() );
        frozenBox.setEmptyHoleNumber( frozenBoxDTO.getEmptyHoleNumber() );
        frozenBox.setDislocationNumber( frozenBoxDTO.getDislocationNumber() );
        frozenBox.setIsRealData( frozenBoxDTO.getIsRealData() );
        frozenBox.setRowsInShelf( frozenBoxDTO.getRowsInShelf() );
        frozenBox.setColumnsInShelf( frozenBoxDTO.getColumnsInShelf() );
        return frozenBox;
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
