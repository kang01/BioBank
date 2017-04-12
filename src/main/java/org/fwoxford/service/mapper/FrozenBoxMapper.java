package org.fwoxford.service.mapper;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
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
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    FrozenBoxDTO frozenBoxToFrozenBoxDTO(FrozenBox frozenBox);

    List<FrozenBoxDTO> frozenBoxesToFrozenBoxDTOs(List<FrozenBox> frozenBoxes);

    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    @Mapping(source = "transhipId", target = "tranship")
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

    default Tranship transhipFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tranship tranship = new Tranship();
        tranship.setId(id);
        return tranship;
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
        res.setAreaCode(frozenBox.getAreaCode());
        res.setEquipmentCode(frozenBox.getEquipmentCode());
        res.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        res.setFrozenBoxColumns(frozenBox.getFrozenBoxColumns());
        res.setFrozenBoxRows(frozenBox.getFrozenBoxRows());
        res.setFrozenBoxTypeId(frozenBox.getFrozenBoxType().getId());
        res.setIsSplit(frozenBox.getIsSplit());
        res.setMemo(frozenBox.getMemo());
        res.setSampleTypeId(frozenBox.getSampleType().getId());
        res.setSupportRackCode(frozenBox.getSupportRackCode());
        res.setEquipmentId(frozenBox.getEquipment()!=null?frozenBox.getEquipment().getId():null);
        res.setAreaId(frozenBox.getArea()!=null?frozenBox.getArea().getId():null);
        res.setSupportRackId(frozenBox.getSupportRack()!=null?frozenBox.getSupportRack().getId():null);
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
            box.setProjectCode(tranship.getProjectCode()!=null?tranship.getProjectCode(): " ");
            box.setProjectName(tranship.getProjectName()!=null?tranship.getProjectName(): " ");
            box.setProjectSiteId(tranship.getProjectSite() != null ? tranship.getProjectSite().getId() : null);
            box.setProjectSiteCode(tranship.getProjectSiteCode()!=null?tranship.getProjectSiteCode(): " ");
            box.setProjectSiteName(tranship.getProjectSiteName()!=null?tranship.getProjectSiteName(): " ");

            box.setEquipmentCode(box.getEquipmentCode() != null ? box.getEquipmentCode() : " ");
            box.setEquipmentId(box.getEquipmentId() != null ? box.getEquipmentId() : null);

            box.setAreaCode(box.getAreaCode() != null ? box.getAreaCode() : " ");
            box.setAreaId(box.getAreaId() != null ? box.getAreaId() : null);

            box.setSupportRackId(box.getSupportRackId() != null ? box.getSupportRackId() :null);
            box.setSupportRackCode(box.getSupportRackCode() !=null ? box.getSupportRackCode() : " ");
            box.setRowsInShelf(box.getRowsInShelf() != null ? box.getRowsInShelf() : " ");
            box.setColumnsInShelf(box.getColumnsInShelf() != null ? box.getColumnsInShelf() : " ");


            box.setSampleNumber(box.getSampleNumber() != null ? box.getSampleNumber() : 0);
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
        stockInBoxForDataTable.setFrozenBoxRows(box.getFrozenBoxRows()!=null?Integer.parseInt(box.getFrozenBoxRows()):0);
        stockInBoxForDataTable.setFrozenBoxColumns(box.getFrozenBoxColumns()!=null?Integer.parseInt(box.getFrozenBoxColumns()):0);
        stockInBoxForDataTable.setFrozenBoxCode(box.getFrozenBoxCode());
        stockInBoxForDataTable.setPosition(box.getEquipmentCode()+"."+box.getAreaCode()+"."+box.getSupportRackCode()+"."+box.getColumnsInShelf()+box.getRowsInShelf());
        stockInBoxForDataTable.setSampleTypeName(box.getSampleTypeName());
        stockInBoxForDataTable.setStatus(box.getStatus());
        return stockInBoxForDataTable;
    }
}
