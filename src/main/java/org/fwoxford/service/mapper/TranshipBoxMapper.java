package org.fwoxford.service.mapper;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.TranshipBoxDTO;

import org.fwoxford.service.dto.TranshipTubeDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper for the entity TranshipBox and its DTO TranshipBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipBoxMapper {

    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "frozenBoxType.id", target = "frozenBoxTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    TranshipBoxDTO transhipBoxToTranshipBoxDTO(TranshipBox transhipBox);

    List<TranshipBoxDTO> transhipBoxesToTranshipBoxDTOs(List<TranshipBox> transhipBoxes);

    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "supportRackId", target = "supportRack")
    TranshipBox transhipBoxDTOToTranshipBox(TranshipBoxDTO transhipBoxDTO);

    List<TranshipBox> transhipBoxDTOsToTranshipBoxes(List<TranshipBoxDTO> transhipBoxDTOs);

    default Tranship transhipFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tranship tranship = new Tranship();
        tranship.setId(id);
        return tranship;
    }

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
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
    default SupportRack supportFromId(Long id) {
        if (id == null) {
            return null;
        }
        SupportRack supportRack = new SupportRack();
        supportRack.setId(id);
        return supportRack;
    }
    default TranshipBox initTranshipToTranship(TranshipBox box){

        TranshipBox transhipBox = new TranshipBox();

        transhipBox.setFrozenBox(box.getFrozenBox());
        transhipBox.setTranship(box.getTranship() );
        transhipBox.setCreatedBy( box.getCreatedBy() );
        transhipBox.setCreatedDate( box.getCreatedDate() );
        transhipBox.setLastModifiedBy( box.getLastModifiedBy() );
        transhipBox.setLastModifiedDate( box.getLastModifiedDate() );
        transhipBox.setId( box.getId() );
        transhipBox.setFrozenBoxCode( box.getFrozenBoxCode() );
        transhipBox.setFrozenBoxCode1D( box.getFrozenBoxCode1D() );
        transhipBox.setEquipmentCode( box.getEquipmentCode());
        transhipBox.setAreaCode( box.getAreaCode());
        transhipBox.setSupportRackCode( box.getSupportRackCode() );
        transhipBox.setRowsInShelf( box.getRowsInShelf() );
        transhipBox.setColumnsInShelf( box.getColumnsInShelf() );
        transhipBox.setCountOfSample( box.getCountOfSample() );
        transhipBox.setMemo( box.getMemo() );
        transhipBox.setStatus( box.getStatus() );
        transhipBox.setEquipment( box.getEquipment() );
        transhipBox.setArea( box.getArea() );
        transhipBox.setSupportRack( box.getSupportRack() );

        return transhipBox;
    }
    default List<TranshipBoxDTO> forzenBoxsAndTubesToFrozenBoxAndFrozenTubeResponses(List<FrozenBox> frozenBoxes, Map<Long, List<TranshipTubeDTO>> transhipTubeDTOS){
        if(frozenBoxes == null){
            return null;
        }
        List<TranshipBoxDTO> frozenBoxAndFrozenTubeResponses = new ArrayList<>();
        for(FrozenBox frozenBox :frozenBoxes){
            TranshipBoxDTO frozenBoxAndFrozenTubeResponse = forzenBoxAndTubeToTranshipBoxDTO(frozenBox);
            frozenBoxAndFrozenTubeResponse.setIsRealData(Constants.YES);
            frozenBoxAndFrozenTubeResponse.setIsSplit(Constants.NO);
            if(transhipTubeDTOS!=null) {
                frozenBoxAndFrozenTubeResponse.setTranshipTubeDTOS(transhipTubeDTOS.get(frozenBox.getId()));
                if(transhipTubeDTOS.get(frozenBox.getId())!=null){
                    frozenBoxAndFrozenTubeResponse.setCountOfSample(transhipTubeDTOS.get(frozenBox.getId()).size());
                }
            }
            frozenBoxAndFrozenTubeResponses.add(frozenBoxAndFrozenTubeResponse);
        }

        return frozenBoxAndFrozenTubeResponses;
    }

    default TranshipBoxDTO forzenBoxAndTubeToTranshipBoxDTO(FrozenBox frozenBox){
        if(frozenBox == null){
            return null;
        }
        TranshipBoxDTO res = new TranshipBoxDTO();

        res.setFrozenBoxId(frozenBox.getId());
        res.setStatus(frozenBox.getStatus());
        res.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        res.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
        res.setIsSplit(frozenBox.getIsSplit());
        res.setMemo(frozenBox.getMemo());
        res.setEquipmentId(frozenBox.getEquipment()!=null?frozenBox.getEquipment().getId():null);
        res.setEquipmentCode(frozenBox.getEquipmentCode());
        res.setAreaId(frozenBox.getArea()!=null?frozenBox.getArea().getId():null);
        res.setAreaCode(frozenBox.getAreaCode());
        res.setSupportRackId(frozenBox.getSupportRack()!=null?frozenBox.getSupportRack().getId():null);
        res.setSupportRackCode(frozenBox.getSupportRackCode());
        res.setColumnsInShelf(frozenBox.getColumnsInShelf());
        res.setRowsInShelf(frozenBox.getRowsInShelf());
        res.setProjectId(frozenBox.getProject()!=null?frozenBox.getProject().getId():null);
        res.setProjectCode(frozenBox.getProjectCode());
        res.setProjectName(frozenBox.getProjectName());
        res.setMemo(frozenBox.getMemo());
        res.setCountOfSample(frozenBox.getCountOfSample());
        res.setProjectSiteId(frozenBox.getProjectSite()!=null?frozenBox.getProjectSite().getId():null);
        res.setProjectSiteCode(frozenBox.getProjectSiteCode());
        res.setProjectSiteName(frozenBox.getProjectSiteName());
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

    default FrozenBox transhipBoxDTOToFrozenBox(TranshipBox transhipBox){
        if ( transhipBox == null ) {
            return null;
        }

        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(transhipBox.getFrozenBox()!=null?transhipBox.getFrozenBox().getId():null);
        frozenBox.setArea( transhipBox.getArea() );
        frozenBox.setSupportRack(transhipBox.getSupportRack() );
        frozenBox.setFrozenBoxType( transhipBox.getFrozenBoxType() );
        frozenBox.setProject( transhipBox.getProject() );
        frozenBox.setEquipment( transhipBox.getEquipment() );
        frozenBox.setProjectSite( transhipBox.getProjectSite());
        frozenBox.setSampleType( transhipBox.getSampleType() );
        frozenBox.setSampleClassification( transhipBox.getSampleClassification());
        frozenBox.setFrozenBoxCode( transhipBox.getFrozenBoxCode() );
        frozenBox.setFrozenBoxTypeCode( transhipBox.getFrozenBoxTypeCode() );
        frozenBox.setFrozenBoxTypeRows( transhipBox.getFrozenBoxTypeRows() );
        frozenBox.setFrozenBoxTypeColumns( transhipBox.getFrozenBoxTypeColumns() );
        frozenBox.setProjectCode( transhipBox.getProjectCode() );
        frozenBox.setProjectName( transhipBox.getProjectName() );
        frozenBox.setProjectSiteCode( transhipBox.getProjectSiteCode() );
        frozenBox.setProjectSiteName( transhipBox.getProjectSiteName() );
        frozenBox.setEquipmentCode( transhipBox.getEquipmentCode() );
        frozenBox.setAreaCode( transhipBox.getAreaCode() );
        frozenBox.setSupportRackCode( transhipBox.getSupportRackCode() );
        frozenBox.setSampleTypeCode( transhipBox.getSampleTypeCode() );
        frozenBox.setSampleTypeName( transhipBox.getSampleTypeName() );
        frozenBox.setCountOfSample( transhipBox.getCountOfSample() );
        frozenBox.setIsSplit( transhipBox.getIsSplit() );
        frozenBox.setMemo( transhipBox.getMemo() );
        frozenBox.setStatus( transhipBox.getStatus() );
        frozenBox.setEmptyTubeNumber( transhipBox.getEmptyTubeNumber() );
        frozenBox.setEmptyHoleNumber( transhipBox.getEmptyHoleNumber() );
        frozenBox.setDislocationNumber( transhipBox.getDislocationNumber() );
        frozenBox.setIsRealData( transhipBox.getIsRealData() );
        frozenBox.setRowsInShelf( transhipBox.getRowsInShelf() );
        frozenBox.setColumnsInShelf( transhipBox.getColumnsInShelf() );
        frozenBox.setFrozenBoxCode1D( transhipBox.getFrozenBoxCode1D() );
        return frozenBox;
    }

    default TranshipBoxDTO transhipBoxToTranshipBoxDTOWithSampleType(TranshipBox transhipBox ,int i ){
        if ( transhipBox == null ) {
            return null;
        }

        TranshipBoxDTO transhipBoxDTO = transhipBoxToTranshipBoxDTO(transhipBox);
        if(transhipBoxDTO == null){
            return null;
        }
        transhipBoxDTO.setFrozenBoxTypeName(transhipBox.getFrozenBoxType()!=null?transhipBox.getFrozenBoxType().getFrozenBoxTypeName():null);
        SampleType sampleType = transhipBox.getSampleType();
        if(sampleType!=null){
            transhipBoxDTO.setSampleTypeId(sampleType.getId());
            transhipBoxDTO.setSampleTypeCode(sampleType.getSampleTypeCode());
            transhipBoxDTO.setSampleTypeName(sampleType.getSampleTypeName());
            transhipBoxDTO.setIsMixed(sampleType.getIsMixed());
            transhipBoxDTO.setFrontColor(sampleType.getFrontColor());
            transhipBoxDTO.setBackColor(sampleType.getBackColor());
        }
        SampleClassification sampleClassification = transhipBox.getSampleClassification();
        if(sampleClassification != null){
            transhipBoxDTO.setSampleClassificationId(sampleClassification.getId());
            transhipBoxDTO.setSampleClassificationName(sampleClassification.getSampleClassificationName());
            transhipBoxDTO.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
            transhipBoxDTO.setFrontColorForClass(sampleClassification.getFrontColor());
            transhipBoxDTO.setBackColorForClass(sampleClassification.getBackColor());
        }
        return transhipBoxDTO;
    }

    default FrozenBoxAndFrozenTubeResponse transhipBoxToResponse(TranshipBox transhipBox){
        if(transhipBox == null){
            return null;
        }
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        res.setId(transhipBox.getFrozenBox().getId());
        res.setStatus(transhipBox.getStatus());
        res.setFrozenBoxCode(transhipBox.getFrozenBoxCode());
        res.setFrozenBoxCode1D(transhipBox.getFrozenBoxCode1D());
        res.setFrozenBoxType(transhipBox.getFrozenBoxType());
        res.setIsSplit(transhipBox.getIsSplit());
        res.setMemo(transhipBox.getMemo());
        res.setSampleType(transhipBox.getSampleType());
        res.setSampleClassification(transhipBox.getSampleClassification());
        res.setEquipmentId(transhipBox.getEquipment()!=null?transhipBox.getEquipment().getId():null);
        res.setAreaId(transhipBox.getArea()!=null?transhipBox.getArea().getId():null);
        res.setSupportRackId(transhipBox.getSupportRack()!=null?transhipBox.getSupportRack().getId():null);
        res.setColumnsInShelf(transhipBox.getColumnsInShelf());
        res.setRowsInShelf(transhipBox.getRowsInShelf());
        res.setProjectId(transhipBox.getProject()!=null?transhipBox.getProject().getId():null);
        res.setProjectCode(transhipBox.getProjectCode());
        res.setProjectName(transhipBox.getProjectName());
        res.setCountOfSample(transhipBox.getCountOfSample());
        SampleType sampleType = transhipBox.getSampleType();
        if(sampleType!=null){
            res.setSampleTypeId(sampleType.getId());
            res.setSampleTypeCode(sampleType.getSampleTypeCode());
            res.setSampleTypeName(sampleType.getSampleTypeName());
            res.setIsMixed(sampleType.getIsMixed());
            res.setFrontColor(sampleType.getFrontColor());
            res.setBackColor(sampleType.getBackColor());
        }
        FrozenBoxType frozenBoxType = transhipBox.getFrozenBoxType();
        if(frozenBoxType!=null){
            res.setFrozenBoxTypeId(frozenBoxType.getId());
            res.setFrozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode());
            res.setFrozenBoxTypeName(frozenBoxType.getFrozenBoxTypeName());
            res.setFrozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns());
            res.setFrozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows());
        }
        SampleClassification sampleClassification = transhipBox.getSampleClassification();
        if(sampleClassification != null){
            res.setSampleClassificationId(sampleClassification.getId());
            res.setSampleClassificationName(sampleClassification.getSampleClassificationName());
            res.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
            res.setFrontColorForClass(sampleClassification.getFrontColor());
            res.setBackColorForClass(sampleClassification.getBackColor());
        }
        return res;
    }
}
