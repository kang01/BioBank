package org.fwoxford.service.mapper;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenTubeForSaveBatchDTO;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.FrozenTubeDTO;

import org.fwoxford.service.dto.response.StockInTubeForBox;
import org.mapstruct.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity FrozenTube and its DTO FrozenTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenTubeMapper {

    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    FrozenTubeDTO frozenTubeToFrozenTubeDTO(FrozenTube frozenTube);

    List<FrozenTubeDTO> frozenTubesToFrozenTubeDTOs(List<FrozenTube> frozenTubes);

    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
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

    default ProjectSite projectSiteFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectSite projectSite = new ProjectSite();
        projectSite.setId(id);
        return projectSite;
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
        res.setId(tube.getId());
        res.setFrozenTubeType(tube.getFrozenTubeType());
        res.setMemo(tube.getMemo());
        res.setSampleCode(tube.getSampleCode());
        res.setSampleTempCode(tube.getSampleTempCode());
        res.setSampleType(tube.getSampleType());
        res.setSampleClassification(tube.getSampleClassification());
        res.setStatus(tube.getStatus());
        res.setTubeColumns(tube.getTubeColumns());
        res.setTubeRows(tube.getTubeRows());
        res.setFrozenBoxCode(tube.getFrozenBoxCode());
        res.setFrozenBoxId(tube.getFrozenBox()!=null?tube.getFrozenBox().getId():null);
        SampleClassification sampleClassification = tube.getSampleClassification();
        if(sampleClassification != null){
            res.setSampleClassificationId(sampleClassification.getId());
            res.setSampleClassificationName(sampleClassification.getSampleClassificationName());
            res.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
            res.setFrontColorForClass(sampleClassification.getFrontColor());
            res.setBackColorForClass(sampleClassification.getBackColor());
        }
        SampleType sampleType = tube.getSampleType();
        if(sampleType!=null){
            res.setSampleTypeId(sampleType.getId());
            res.setSampleTypeCode(sampleType.getSampleTypeCode());
            res.setSampleTypeName(sampleType.getSampleTypeName());
            res.setIsMixed(sampleType.getIsMixed());
            res.setFrontColor(sampleType.getFrontColor());
            res.setBackColor(sampleType.getBackColor());
        }
        return res;
    }

    default FrozenTube frozenTubeForSaveBatchDTOToFrozenTube(FrozenTubeForSaveBatchDTO frozenTubeDTO){
        if ( frozenTubeDTO == null ) {
            return null;
        }

        FrozenTube frozenTube = new FrozenTube();

        frozenTube.setFrozenBox( frozenBoxFromId( frozenTubeDTO.getFrozenBoxId() ) );
        frozenTube.setSampleType( sampleTypeFromId( frozenTubeDTO.getSampleTypeId() ) );
        frozenTube.setId( frozenTubeDTO.getId() );
        frozenTube.setSampleTempCode( frozenTubeDTO.getSampleTempCode() );
        frozenTube.setSampleCode( frozenTubeDTO.getSampleCode() );
        frozenTube.setTubeRows( frozenTubeDTO.getTubeRows() );
        frozenTube.setTubeColumns( frozenTubeDTO.getTubeColumns() );
        frozenTube.setMemo( frozenTubeDTO.getMemo() );
        frozenTube.setSampleClassification( sampleClassificationFromId( frozenTubeDTO.getSampleClassificationId() ));
        frozenTube.setStatus(StringUtils.isEmpty(frozenTubeDTO.getStatus())?Constants.FROZEN_TUBE_NORMAL: frozenTubeDTO.getStatus());
        frozenTube.setFrozenBoxCode( frozenTubeDTO.getFrozenBoxCode() );
        return frozenTube;
    }

    default SampleClassification sampleClassificationFromId(Long id){
        if (id == null) {
            return null;
        }
        SampleClassification sampleClassification = new SampleClassification();
        sampleClassification.setId(id);
        return sampleClassification;
    }

    //根据冻存管构造盒内入库冻存管
    default List<StockInTubeForBox> frozenTubesToStockInTubesForBox(List<FrozenTube> frozenTubeList){
        if ( frozenTubeList == null ) {
            return null;
        }

        List<StockInTubeForBox> list = new ArrayList<StockInTubeForBox>();
        for ( FrozenTube frozenTube : frozenTubeList ) {
            StockInTubeForBox stockInTubeForBox = frozenTubeToStockInTubeForBox( frozenTube);
            list.add(stockInTubeForBox);
        }
        return list;
    }

    default StockInTubeForBox frozenTubeToStockInTubeForBox(FrozenTube frozenTube){
        if ( frozenTube == null ) {
            return null;
        }
        StockInTubeForBox stockInTubeForBox = new StockInTubeForBox();
//        stockInTubeForBox.setId(frozenTube.getId());
        stockInTubeForBox.setTubeRows(frozenTube.getTubeRows());
        stockInTubeForBox.setTubeColumns(frozenTube.getTubeColumns());
        stockInTubeForBox.setFrozenBoxCode(frozenTube.getFrozenBoxCode());
        stockInTubeForBox.setSampleCode(frozenTube.getSampleCode()!=null?frozenTube.getSampleCode():frozenTube.getSampleTempCode());
        SampleClassification sampleClassification = frozenTube.getSampleClassification();
        if(sampleClassification != null){
            stockInTubeForBox.setSampleClassificationId(sampleClassification.getId());
            stockInTubeForBox.setSampleClassificationName(sampleClassification.getSampleClassificationName());
            stockInTubeForBox.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
            stockInTubeForBox.setFrontColorForClass(sampleClassification.getFrontColor());
            stockInTubeForBox.setBackColorForClass(sampleClassification.getBackColor());
        }
        SampleType sampleType = frozenTube.getSampleType();
        if(sampleType!=null){
            stockInTubeForBox.setSampleTypeId(sampleType.getId());
            stockInTubeForBox.setSampleTypeCode(sampleType.getSampleTypeCode());
            stockInTubeForBox.setSampleTypeName(sampleType.getSampleTypeName());
            stockInTubeForBox.setIsMixed(sampleType.getIsMixed());
            stockInTubeForBox.setFrontColor(sampleType.getFrontColor());
            stockInTubeForBox.setBackColor(sampleType.getBackColor());
        }
        FrozenTubeType frozenTubeType = frozenTube.getFrozenTubeType();
        if(frozenTubeType!=null){
            stockInTubeForBox.setFrozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode());
            stockInTubeForBox.setFrozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName());
            stockInTubeForBox.setFrozenTubeTypeId(frozenTubeType.getId());
        }
        return stockInTubeForBox;
    }

    default List<FrozenTubeDTO> frozenTubesToFrozenTubeDTOsForSample(List<FrozenTube> frozenTubeList){
        if(frozenTubeList == null){
            return null;
        }
        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<>();
        for(FrozenTube frozenTube :frozenTubeList){
            FrozenTubeDTO frozenTubeDTO = frozenTubeToFrozenTubeDTO(frozenTube);
            SampleClassification sampleClassification = frozenTube.getSampleClassification();
            if(sampleClassification != null){
                frozenTubeDTO.setSampleClassificationId(sampleClassification.getId());
                frozenTubeDTO.setSampleClassificationName(sampleClassification.getSampleClassificationName());
                frozenTubeDTO.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
                frozenTubeDTO.setFrontColorForClass(sampleClassification.getFrontColor());
                frozenTubeDTO.setBackColorForClass(sampleClassification.getBackColor());
            }
            SampleType sampleType = frozenTube.getSampleType();
            if(sampleType!=null){
                frozenTubeDTO.setSampleTypeId(sampleType.getId());
                frozenTubeDTO.setSampleTypeCode(sampleType.getSampleTypeCode());
                frozenTubeDTO.setSampleTypeName(sampleType.getSampleTypeName());
                frozenTubeDTO.setIsMixed(sampleType.getIsMixed());
                frozenTubeDTO.setFrontColor(sampleType.getFrontColor());
                frozenTubeDTO.setBackColor(sampleType.getBackColor());
            }
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        return frozenTubeDTOS;
    }
}
