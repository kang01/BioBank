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
    TranshipTubeDTO transhipTubeToTranshipTubeDTO(TranshipTube transhipTube);

    List<TranshipTubeDTO> transhipTubesToTranshipTubeDTOs(List<TranshipTube> transhipTubes);

    @Mapping(source = "transhipBoxId", target = "transhipBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
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
        transhipTubeDTO.setColumnsInTube( frozenTube.getTubeColumns() );
        transhipTubeDTO.setRowsInTube( frozenTube.getTubeRows() );
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
}
