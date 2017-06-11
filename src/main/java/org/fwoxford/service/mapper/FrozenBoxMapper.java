package org.fwoxford.service.mapper;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenBoxForSaveBatchDTO;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.FrozenBoxDTO;

import org.fwoxford.service.dto.response.StockInBoxForDataTable;
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

    default FrozenBoxAndFrozenTubeResponse forzenBoxAndTubeToResponse(FrozenBox frozenBox, List<FrozenTubeResponse> frozenTube){
        if(frozenBox == null){
            return null;
        }
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();
        res.setFrozenTubeDTOS(frozenTube);
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
        return res;
    }

    default  TranshipBoxDTO frozenBoxToTranshipBoxDTO(FrozenBoxDTO boxDTO){
        if(boxDTO == null){
            return null;
        }
        TranshipBoxDTO transhipBoxDTO = new TranshipBoxDTO();
        transhipBoxDTO.setTranshipId(boxDTO.getTranshipId());
        transhipBoxDTO.setEquipmentCode(boxDTO.getEquipmentCode());
        transhipBoxDTO.setAreaCode(boxDTO.getAreaCode());
        transhipBoxDTO.setFrozenBoxCode(boxDTO.getFrozenBoxCode());
        transhipBoxDTO.setSupportRackCode(boxDTO.getSupportRackCode());
        transhipBoxDTO.setCreatedBy(boxDTO.getCreatedBy());
        transhipBoxDTO.setMemo(boxDTO.getMemo());
        transhipBoxDTO.setCreatedDate(boxDTO.getCreatedDate());
        transhipBoxDTO.setStatus(boxDTO.getStatus());
        transhipBoxDTO.setLastModifiedBy(boxDTO.getLastModifiedBy());
        transhipBoxDTO.setLastModifiedDate(boxDTO.getLastModifiedDate());
        transhipBoxDTO.setFrozenBoxId(boxDTO.getId());
        transhipBoxDTO.setColumnsInShelf(boxDTO.getColumnsInShelf());
        transhipBoxDTO.setRowsInShelf(boxDTO.getRowsInShelf());
        return transhipBoxDTO;
    }

    default List<FrozenBoxDTO> frozenTranshipAndBoxToFrozenBoxDTOList(List<FrozenBoxDTO> frozenBoxDTOList, Tranship tranship){
        List<FrozenBoxDTO> frozenBoxDTOLists = new ArrayList<FrozenBoxDTO>();
        for(FrozenBoxDTO box:frozenBoxDTOList){
            box.setProjectId(tranship.getProject()!=null?tranship.getProject().getId(): null);
            box.setProjectCode(tranship.getProjectCode());
            box.setProjectName(tranship.getProjectName());
            box.setProjectSiteId(tranship.getProjectSite() != null ? tranship.getProjectSite().getId() : null);
            box.setProjectSiteCode(tranship.getProjectSiteCode());
            box.setProjectSiteName(tranship.getProjectSiteName());

            box.setEquipmentCode(box.getEquipmentCode());
            box.setEquipmentId(box.getEquipmentId());

            box.setAreaCode(box.getAreaCode());
            box.setAreaId(box.getAreaId());

            box.setSupportRackId(box.getSupportRackId());
            box.setSupportRackCode(box.getSupportRackCode());
            box.setRowsInShelf(box.getRowsInShelf());
            box.setColumnsInShelf(box.getColumnsInShelf());


            box.setSampleNumber(box.getSampleNumber());
            box.setDislocationNumber(box.getDislocationNumber() != null ? box.getDislocationNumber() : 0);
            box.setEmptyHoleNumber(box.getEmptyHoleNumber() != null ? box.getEmptyHoleNumber() : 0);
            box.setEmptyTubeNumber(box.getEmptyTubeNumber() != null ? box.getEmptyTubeNumber() : 0);
            box.setIsRealData(box.getIsRealData() != null ? box.getIsRealData() : 0);
            box.setIsSplit(box.getIsSplit() != null ? box.getIsSplit() : 0);

            box.setStatus(box.getStatus() != null ? box.getStatus() : Constants.FROZEN_BOX_NEW);
            box.setTranshipId(tranship.getId());
            frozenBoxDTOLists.add(box);
        }
        return frozenBoxDTOLists;
    }

    default List<StockInBoxForDataTable> frozenBoxesToStockInBoxForDataTables(List<FrozenBox> frozenBoxes){
        List<StockInBoxForDataTable> stockInBoxForDataTables = new ArrayList<>();
        for(FrozenBox box:frozenBoxes){
            StockInBoxForDataTable stockInBoxForDataTable = frozenBoxToStockInBoxForDataTable(box);
            stockInBoxForDataTables.add(stockInBoxForDataTable);
        }
        return stockInBoxForDataTables;
    }
    default StockInBoxForDataTable frozenBoxToStockInBoxForDataTable(FrozenBox box){
        StockInBoxForDataTable stockInBoxForDataTable = new StockInBoxForDataTable();
        if(box == null){
            return null;
        }
        stockInBoxForDataTable.setIsSplit(box.getIsSplit());
        stockInBoxForDataTable.setCountOfSample(box.getSampleNumber());
        stockInBoxForDataTable.setId(box.getId());
        stockInBoxForDataTable.setFrozenBoxCode(box.getFrozenBoxCode());
        String position = toPositionString(box);
        stockInBoxForDataTable.setPosition(position);
        stockInBoxForDataTable.setSampleTypeName(box.getSampleTypeName());
        stockInBoxForDataTable.setSampleClassificationName(box.getSampleClassification()!=null?box.getSampleClassification().getSampleClassificationName():null);
        stockInBoxForDataTable.setStatus(box.getStatus());
        return stockInBoxForDataTable;
    }
    default  String toPositionString(FrozenBox pos){
        if(pos ==null){
            return null;
        }
        ArrayList<String> positions = new ArrayList<>();
        if (pos.getEquipmentCode() != null && pos.getEquipmentCode().length() > 0){
            positions.add(pos.getEquipmentCode());
        }

        if (pos.getAreaCode() != null && pos.getAreaCode().length() > 0) {
            positions.add(pos.getAreaCode());
        }

        if (pos.getSupportRackCode() != null && pos.getSupportRackCode().length() > 0){
            positions.add(pos.getSupportRackCode());
        }

        if (pos.getRowsInShelf() != null && pos.getRowsInShelf().length() > 0 && pos.getColumnsInShelf() != null && pos.getColumnsInShelf().length() > 0){
            positions.add(pos.getColumnsInShelf()+pos.getRowsInShelf());
        }

        return String.join(".", positions);
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
        frozenBox.setSampleNumber( frozenBoxDTO.getSampleNumber() );
        frozenBox.setIsSplit( frozenBoxDTO.getIsSplit() );
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

    default FrozenBoxAndFrozenTubeResponse forzenBoxDTOAndTubeToResponse(FrozenBoxDTO frozenBox, List<FrozenTubeResponse> frozenTube){
        if(frozenBox == null){
            return null;
        }
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();
        res.setFrozenTubeDTOS(frozenTube);
        res.setId(frozenBox.getId());
        res.setStatus(frozenBox.getStatus());
        res.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        res.setIsSplit(frozenBox.getIsSplit());
        res.setMemo(frozenBox.getMemo());
        res.setEquipmentId(frozenBox.getEquipmentId());
        res.setAreaId(frozenBox.getAreaId());
        res.setSupportRackId(frozenBox.getSupportRackId());
        res.setColumnsInShelf(frozenBox.getColumnsInShelf());
        res.setRowsInShelf(frozenBox.getRowsInShelf());
        return res;
    }
}
