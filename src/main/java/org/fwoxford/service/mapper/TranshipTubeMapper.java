package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TranshipTubeDTO;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity TranshipTube and its DTO TranshipTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipTubeMapper {

    @Mapping(source = "transhipBox.id", target = "transhipBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "columnsInTube", target = "tubeColumns")
    @Mapping(source = "rowsInTube", target = "tubeRows")
    TranshipTubeDTO transhipTubeToTranshipTubeDTO(TranshipTube transhipTube);

    List<TranshipTubeDTO> transhipTubesToTranshipTubeDTOs(List<TranshipTube> transhipTubes);

    @Mapping(source = "transhipBoxId", target = "transhipBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    @Mapping(source = "tubeColumns", target = "columnsInTube")
    @Mapping(source = "tubeRows", target = "rowsInTube")
    TranshipTube transhipTubeDTOToTranshipTube(TranshipTubeDTO transhipTubeDTO);

    List<TranshipTube> transhipTubeDTOsToTranshipTubes(List<TranshipTubeDTO> transhipTubeDTOs);

    default TranshipBox transhipBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        TranshipBox transhipBox = new TranshipBox();
        transhipBox.setId(id);
        return transhipBox;
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

    default List<TranshipTubeDTO> frozenTubesToTranshipTubeDTOs(List<FrozenTube> frozenTubeList){
        if(frozenTubeList == null){
            return null;
        }
        List<TranshipTubeDTO> frozenTubeDTOS = new ArrayList<>();
        for(FrozenTube frozenTube :frozenTubeList){
            TranshipTubeDTO frozenTubeDTO = frozenTubeToTranshipTube(frozenTube);
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        return frozenTubeDTOS;
    }

    default TranshipTubeDTO frozenTubeToTranshipTube(FrozenTube frozenTube){
        if ( frozenTube == null ) {
            return null;
        }

        TranshipTubeDTO transhipTubeDTO = new TranshipTubeDTO();
        transhipTubeDTO.setFrozenBoxId(frozenTube.getFrozenBox()!=null?frozenTube.getFrozenBox().getId():null);
        transhipTubeDTO.setFrozenTubeId( frozenTube.getId() );
        transhipTubeDTO.setSampleClassificationId( frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getId():null );
        transhipTubeDTO.setSampleTypeId( frozenTube.getSampleType()!=null?frozenTube.getSampleType().getId():null );
        transhipTubeDTO.setProjectSiteId( frozenTube.getProjectSite()!=null? frozenTube.getProjectSite().getId():null);
        transhipTubeDTO.setProjectId( frozenTube.getProject()!=null? frozenTube.getProject().getId():null);
        transhipTubeDTO.setFrozenTubeTypeId( frozenTube.getFrozenTubeType()!=null?frozenTube.getFrozenTubeType().getId():null );
        transhipTubeDTO.setStatus( frozenTube.getStatus() );
        transhipTubeDTO.setMemo( frozenTube.getMemo() );
        transhipTubeDTO.setTubeColumns( frozenTube.getTubeColumns() );
        transhipTubeDTO.setTubeRows( frozenTube.getTubeRows() );
        transhipTubeDTO.setProjectCode( frozenTube.getProjectCode() );
        transhipTubeDTO.setFrozenTubeCode( frozenTube.getFrozenTubeCode() );
        transhipTubeDTO.setSampleTempCode( frozenTube.getSampleTempCode() );
        transhipTubeDTO.setSampleCode( frozenTube.getSampleCode() );
        transhipTubeDTO.setFrozenTubeTypeCode( frozenTube.getFrozenTubeTypeCode() );
        transhipTubeDTO.setFrozenTubeTypeName( frozenTube.getFrozenTubeTypeName() );
        transhipTubeDTO.setSampleTypeCode( frozenTube.getSampleTypeCode() );
        transhipTubeDTO.setSampleTypeName( frozenTube.getSampleTypeName() );
        transhipTubeDTO.setSampleUsedTimesMost( frozenTube.getSampleUsedTimesMost() );
        transhipTubeDTO.setSampleUsedTimes( frozenTube.getSampleUsedTimes() );
        transhipTubeDTO.setFrozenTubeVolumns( frozenTube.getFrozenTubeVolumns() );
        transhipTubeDTO.setFrozenTubeVolumnsUnit( frozenTube.getFrozenTubeVolumnsUnit() );
        transhipTubeDTO.setSampleVolumns( frozenTube.getSampleVolumns() );
        transhipTubeDTO.setErrorType( frozenTube.getErrorType() );
        transhipTubeDTO.setFrozenTubeState( frozenTube.getFrozenTubeState() );
        transhipTubeDTO.setFrozenBoxCode( frozenTube.getFrozenBoxCode() );
        transhipTubeDTO.setSampleClassificationCode( frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getSampleClassificationCode():null );
        transhipTubeDTO.setSampleClassificationName(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getSampleClassificationName():null );
        transhipTubeDTO.setProjectSiteCode( frozenTube.getProjectSiteCode() );
        transhipTubeDTO.setFrontColorForClass(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getFrontColor():null);
        transhipTubeDTO.setBackColorForClass(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getBackColor():null);
        transhipTubeDTO.setIsMixed(frozenTube.getSampleType()!=null?frozenTube.getSampleType().getIsMixed():null);
        transhipTubeDTO.setFrontColor(frozenTube.getSampleType()!=null?frozenTube.getSampleType().getFrontColor():null);
        transhipTubeDTO.setBackColor(frozenTube.getSampleType()!=null?frozenTube.getSampleType().getBackColor():null);
        return transhipTubeDTO;
    }

    default FrozenTube transhipTubeToFrozenTube(TranshipTube stockOutTube){
        if ( stockOutTube == null ) {
            return null;
        }

        FrozenTube frozenTube = new FrozenTube();
        frozenTube.setId(stockOutTube.getFrozenTube()!=null?stockOutTube.getFrozenTube().getId():null);
        frozenTube.setFrozenTubeType( stockOutTube.getFrozenTubeType() );
        frozenTube.setFrozenBox(stockOutTube.getTranshipBox().getFrozenBox());
        frozenTube.setProject( stockOutTube.getProject() );
        frozenTube.setProjectSite( stockOutTube.getProjectSite() );
        frozenTube.setSampleType(stockOutTube.getSampleType() );
        frozenTube.setSampleClassification( stockOutTube.getSampleClassification());
        frozenTube.setProjectCode( stockOutTube.getProjectCode() );
        frozenTube.setProjectSiteCode( stockOutTube.getProjectSiteCode() );
        frozenTube.setFrozenTubeCode( stockOutTube.getFrozenTubeCode() );
        frozenTube.setSampleTempCode( stockOutTube.getSampleTempCode() );
        frozenTube.setSampleCode( stockOutTube.getSampleCode() );
        frozenTube.setFrozenTubeTypeCode( stockOutTube.getFrozenTubeTypeCode() );
        frozenTube.setFrozenTubeTypeName( stockOutTube.getFrozenTubeTypeName() );
        frozenTube.setSampleTypeCode( stockOutTube.getSampleTypeCode() );
        frozenTube.setSampleTypeName( stockOutTube.getSampleTypeName() );
        frozenTube.setSampleUsedTimesMost( stockOutTube.getSampleUsedTimesMost() );
        frozenTube.setSampleUsedTimes( stockOutTube.getSampleUsedTimes() );
        frozenTube.setFrozenTubeVolumns( stockOutTube.getFrozenTubeVolumns() );
        frozenTube.setSampleVolumns( stockOutTube.getSampleVolumns() );
        frozenTube.setFrozenTubeVolumnsUnit( stockOutTube.getFrozenTubeVolumnsUnit() );
        frozenTube.setTubeRows( stockOutTube.getRowsInTube() );
        frozenTube.setTubeColumns( stockOutTube.getColumnsInTube() );
        frozenTube.setMemo( stockOutTube.getMemo() );
        frozenTube.setErrorType( stockOutTube.getErrorType() );
        frozenTube.setFrozenTubeState( stockOutTube.getFrozenTubeState() );
        frozenTube.setStatus( stockOutTube.getStatus() );
        frozenTube.setFrozenBoxCode( stockOutTube.getFrozenBoxCode() );
        return frozenTube;
    }

    default List<TranshipTubeDTO> transhipTubesToTranshipTubeDTOsWithSampleType(List<TranshipTube> transhipTubeList){
        if(transhipTubeList == null){
            return null;
        }
        List<TranshipTubeDTO> transhipTubeDTOS = new ArrayList<>();
        for(TranshipTube transhipTube :transhipTubeList){
            TranshipTubeDTO frozenTubeDTO = transhipTubeToTranshipTubeWithSampleType(transhipTube, 1);
            transhipTubeDTOS.add(frozenTubeDTO);
        }
        return transhipTubeDTOS;
    }

    default TranshipTubeDTO transhipTubeToTranshipTubeWithSampleType(TranshipTube transhipTube,int i){
        if ( transhipTube == null ) {
            return null;
        }

        TranshipTubeDTO transhipTubeDTO = transhipTubeToTranshipTubeDTO(transhipTube);
        if(transhipTubeDTO == null){
            return null;
        }
        transhipTubeDTO.setFrontColorForClass(transhipTube.getSampleClassification()!=null?transhipTube.getSampleClassification().getFrontColor():null);
        transhipTubeDTO.setBackColorForClass(transhipTube.getSampleClassification()!=null?transhipTube.getSampleClassification().getBackColor():null);
        transhipTubeDTO.setIsMixed(transhipTube.getSampleType()!=null?transhipTube.getSampleType().getIsMixed():null);
        transhipTubeDTO.setFrontColor(transhipTube.getSampleType()!=null?transhipTube.getSampleType().getFrontColor():null);
        transhipTubeDTO.setBackColor(transhipTube.getSampleType()!=null?transhipTube.getSampleType().getBackColor():null);
        FrozenTube frozenTube = transhipTube.getFrozenTube();
        transhipTubeDTO.setParentSampleCode(frozenTube!=null?frozenTube.getParentSampleCode():null);
        transhipTubeDTO.setParentSampleId(frozenTube!=null?frozenTube.getParentSampleId():null);
        transhipTubeDTO.setFrozenBoxId(frozenTube!=null?frozenTube.getFrozenBox().getId():null);
        return transhipTubeDTO;
    }
}
