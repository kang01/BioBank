package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.FrozenTubeDTO;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity FrozenTube and its DTO FrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenTubeMapper {

    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    FrozenTubeDTO frozenTubeToFrozenTubeDTO(FrozenTube frozenTube);

    List<FrozenTubeDTO> frozenTubesToFrozenTubeDTOs(List<FrozenTube> frozenTubes);

    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    FrozenTube frozenTubeDTOToFrozenTube(FrozenTubeDTO frozenTubeDTO);

    List<FrozenTube> frozenTubeDTOsToFrozenTubes(List<FrozenTubeDTO> frozenTubeDTOs);

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

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }

    default List<FrozenTubeResponse> frozenTubeToFrozenTubeResponse(List<FrozenTube> frozenTube){
        if ( frozenTube == null ) {
            return null;
        }
        List<FrozenTubeResponse> frozenTubeResponses = new ArrayList<FrozenTubeResponse>();
        for(FrozenTube tube:frozenTube){
            frozenTubeResponses.add(frozenTubeToFrozenTubeResponses(tube));
        }
        return frozenTubeResponses;
    }

    default  FrozenTubeResponse frozenTubeToFrozenTubeResponses(FrozenTube tube){
        if(tube == null){
            return null;
        }
        FrozenTubeResponse res = new FrozenTubeResponse();
        res.setProjectId(tube.getProject().getId());
        res.setProjectCode(tube.getProjectCode());
        res.setId(tube.getId());
        res.setErrorType(tube.getErrorType());
        res.setFrozenTubeCode(tube.getFrozenTubeCode());
        res.setFrozenTubeTypeCode(tube.getFrozenTubeTypeCode());
        res.setFrozenTubeTypeId(tube.getFrozenTubeType().getId());
        res.setMemo(tube.getMemo());
        res.setFrozenTubeTypeName(tube.getFrozenTubeTypeName());
        res.setSampleCode(tube.getSampleCode());
        res.setSampleTempCode(tube.getSampleTempCode());
        res.setSampleTypeCode(tube.getSampleTypeCode());
        res.setSampleTypeId(tube.getSampleType().getId());
        res.setSampleTypeName(tube.getSampleTypeName());
        res.setStatus(tube.getStatus());
        res.setTubeColumns(tube.getTubeColumns());
        res.setTubeRows(tube.getTubeRows());
        return res;
    }

   default List<FrozenTubeDTO> frozenBoxAndTubeToFrozenTubeDTOList(List<FrozenBoxDTO> frozenBoxDTOList, List<FrozenBox> frozenBoxes){
       List<FrozenTubeDTO> frozenTubeDTOList = new ArrayList<FrozenTubeDTO>();
       for(FrozenBoxDTO boxDto:frozenBoxDTOList){
           for(FrozenTubeDTO tube :boxDto.getFrozenTubeDTOS()){
               for(FrozenBox box:frozenBoxes){
                   if(tube.getFrozenBoxCode().equals(box.getFrozenBoxCode())){
                       frozenTubeDTOList.add(tube);
                   }
               }
           }
       }
       return frozenTubeDTOList;
   }
}
