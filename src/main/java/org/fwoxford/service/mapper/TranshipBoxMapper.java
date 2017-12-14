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
    TranshipBoxDTO transhipBoxToTranshipBoxDTO(TranshipBox transhipBox);

    List<TranshipBoxDTO> transhipBoxesToTranshipBoxDTOs(List<TranshipBox> transhipBoxes);

    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "frozenBoxTypeId", target = "frozenBoxType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
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
            frozenBoxAndFrozenTubeResponse.setTranshipTubeDTOS(transhipTubeDTOS.get(frozenBox.getId()));
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
        res.setAreaId(frozenBox.getArea()!=null?frozenBox.getArea().getId():null);
        res.setSupportRackId(frozenBox.getSupportRack()!=null?frozenBox.getSupportRack().getId():null);
        res.setColumnsInShelf(frozenBox.getColumnsInShelf());
        res.setRowsInShelf(frozenBox.getRowsInShelf());
        res.setProjectId(frozenBox.getProject()!=null?frozenBox.getProject().getId():null);
        res.setProjectCode(frozenBox.getProjectCode());
        res.setProjectName(frozenBox.getProjectName());
        res.setMemo(frozenBox.getMemo());
        res.setCountOfSample(frozenBox.getCountOfSample());
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

}
