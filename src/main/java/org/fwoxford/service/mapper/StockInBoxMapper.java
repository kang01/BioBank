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

    default StockInBox frozenBoxToStockInBox(FrozenBox frozenBox,StockIn stockIn,StockInBox stockInBox){
        if(frozenBox == null){
            return null;
        }
        if(stockInBox == null){
            stockInBox = new StockInBox();
        }
        stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
            .sampleClassification(frozenBox.getSampleClassification())
            .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
            .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
            .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
            .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(stockIn.getProject())
            .projectCode(stockIn.getProjectCode()).projectName(stockIn.getProject()!=null?stockIn.getProject().getProjectName():null)
            .projectSite(stockIn.getProjectSite()).projectSiteCode(stockIn.getProjectSiteCode())
            .projectSiteName(stockIn.getProjectSite()!=null?stockIn.getProjectSite().getProjectSiteName():null).frozenBox(frozenBox).status(frozenBox.getStatus()).memo(frozenBox.getMemo())
            .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).stockInCode(stockIn.getStockInCode()).stockIn(stockIn);
        return stockInBox;
    }

    default StockInBoxDTO stockInBoxToStockInBoxDTOForSampleType(StockInBox stockInBox,int i){
        if ( stockInBox == null ) {
            return null;
        }

        StockInBoxDTO stockInBoxDTO = new StockInBoxDTO();

        stockInBoxDTO.setAreaId( stockInBox.getArea()!=null?stockInBox.getArea().getId():null);
        stockInBoxDTO.setSupportRackId( stockInBox.getSupportRack()!=null?stockInBox.getSupportRack().getId():null);
        stockInBoxDTO.setStockInId( stockInBox.getStockIn()!=null?stockInBox.getStockIn().getId():null );
        stockInBoxDTO.setSampleClassificationId( stockInBox.getSampleClassification()!=null?stockInBox.getSampleClassification().getId():null );
        stockInBoxDTO.setFrozenBoxId( stockInBox.getFrozenBox()!=null?stockInBox.getFrozenBox().getId():null);
        stockInBoxDTO.setFrozenBoxTypeId( stockInBox.getFrozenBoxType()!=null?stockInBox.getFrozenBoxType().getId():null);
        stockInBoxDTO.setSampleTypeId( stockInBox.getSampleType()!=null?stockInBox.getSampleType().getId():null );
        stockInBoxDTO.setProjectSiteId( stockInBox.getProjectSite()!=null?stockInBox.getProjectSite().getId():null );
        stockInBoxDTO.setEquipmentId( stockInBox.getEquipment()!=null?stockInBox.getEquipment().getId():null);
        stockInBoxDTO.setProjectId( stockInBox.getProject()!=null?stockInBox.getProject().getId():null);
        stockInBoxDTO.setCreatedBy( stockInBox.getCreatedBy() );
        stockInBoxDTO.setCreatedDate( stockInBox.getCreatedDate() );
        stockInBoxDTO.setLastModifiedBy( stockInBox.getLastModifiedBy() );
        stockInBoxDTO.setLastModifiedDate( stockInBox.getLastModifiedDate() );
        stockInBoxDTO.setId( stockInBox.getId() );
        stockInBoxDTO.setEquipmentCode( stockInBox.getEquipmentCode() );
        stockInBoxDTO.setAreaCode( stockInBox.getAreaCode() );
        stockInBoxDTO.setSupportRackCode( stockInBox.getSupportRackCode() );
        stockInBoxDTO.setRowsInShelf( stockInBox.getRowsInShelf() );
        stockInBoxDTO.setColumnsInShelf( stockInBox.getColumnsInShelf() );
        stockInBoxDTO.setMemo( stockInBox.getMemo() );
        stockInBoxDTO.setStatus( stockInBox.getStatus() );
        stockInBoxDTO.setFrozenBoxCode( stockInBox.getFrozenBoxCode() );
        stockInBoxDTO.setCountOfSample( stockInBox.getCountOfSample() );
        stockInBoxDTO.setFrozenBoxTypeCode( stockInBox.getFrozenBoxTypeCode() );
        stockInBoxDTO.setFrozenBoxTypeRows( stockInBox.getFrozenBoxTypeRows() );
        stockInBoxDTO.setFrozenBoxTypeColumns( stockInBox.getFrozenBoxTypeColumns() );
        stockInBoxDTO.setProjectCode( stockInBox.getProjectCode() );
        stockInBoxDTO.setProjectName( stockInBox.getProjectName() );
        stockInBoxDTO.setProjectSiteCode( stockInBox.getProjectSiteCode() );
        stockInBoxDTO.setProjectSiteName( stockInBox.getProjectSiteName() );
        stockInBoxDTO.setSampleTypeCode( stockInBox.getSampleTypeCode() );
        stockInBoxDTO.setSampleTypeName( stockInBox.getSampleTypeName() );
        stockInBoxDTO.setSampleClassificationCode( stockInBox.getSampleClassificationCode() );
        stockInBoxDTO.setSampleClassificationName( stockInBox.getSampleClassificationName() );
        stockInBoxDTO.setIsSplit( stockInBox.getIsSplit() );
        stockInBoxDTO.setEmptyTubeNumber( stockInBox.getEmptyTubeNumber() );
        stockInBoxDTO.setEmptyHoleNumber( stockInBox.getEmptyHoleNumber() );
        stockInBoxDTO.setDislocationNumber( stockInBox.getDislocationNumber() );
        stockInBoxDTO.setIsRealData( stockInBox.getIsRealData() );
        stockInBoxDTO.setStockInCode( stockInBox.getStockInCode() );
        stockInBoxDTO.setFrozenBoxType( stockInBox.getFrozenBoxType() );
        stockInBoxDTO.setSampleClassification( stockInBox.getSampleClassification() );
        stockInBoxDTO.setSampleType( stockInBox.getSampleType() );
        stockInBoxDTO.setFrozenBoxCode1D( stockInBox.getFrozenBoxCode1D() );
        stockInBoxDTO.setBackColor(stockInBox.getSampleType()!=null?stockInBox.getSampleType().getBackColor():null);
        stockInBoxDTO.setFrontColor(stockInBox.getSampleType()!=null?stockInBox.getSampleType().getFrontColor():null);
        stockInBoxDTO.setIsMixed(stockInBox.getSampleType()!=null?stockInBox.getSampleType().getIsMixed():null);
        stockInBoxDTO.setFrontColorForClass(stockInBox.getSampleClassification()!=null?stockInBox.getSampleClassification().getFrontColor():null);
        stockInBoxDTO.setBackColorForClass(stockInBox.getSampleClassification()!=null?stockInBox.getSampleClassification().getBackColor():null);


        return stockInBoxDTO;
    }
}
