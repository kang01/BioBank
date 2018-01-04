package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInTubeDTO;

import org.fwoxford.service.dto.response.StockInTubeForBox;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity StockInTube and its DTO StockInTubeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInTubeMapper {

    @Mapping(source = "stockInBox.id", target = "stockInBoxId")
    @Mapping(source = "frozenTube.id", target = "frozenTubeId")
    @Mapping(source = "frozenTubeType.id", target = "frozenTubeTypeId")
    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "sampleClassification.id", target = "sampleClassificationId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockInTubeDTO stockInTubeToStockInTubeDTO(StockInTube stockInTube);

    List<StockInTubeDTO> stockInTubesToStockInTubeDTOs(List<StockInTube> stockInTubes);

    @Mapping(source = "stockInBoxId", target = "stockInBox")
    @Mapping(source = "frozenTubeId", target = "frozenTube")
    @Mapping(source = "frozenTubeTypeId", target = "frozenTubeType")
    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "sampleClassificationId", target = "sampleClassification")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockInTube stockInTubeDTOToStockInTube(StockInTubeDTO stockInTubeDTO);

    List<StockInTube> stockInTubeDTOsToStockInTubes(List<StockInTubeDTO> stockInTubeDTOs);

    default StockInBox stockInBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInBox stockInBox = new StockInBox();
        stockInBox.setId(id);
        return stockInBox;
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

    default StockInTubeDTO frozenTubeToStockInTubeDTO(FrozenTube f){
        if ( f == null ) {
            return null;
        }

        StockInTubeDTO stockInTubeDTO = new StockInTubeDTO();

        stockInTubeDTO.setFrozenTubeId( f.getId());
        stockInTubeDTO.setStockInBoxId( null );
        stockInTubeDTO.setSampleClassificationId( f.getSampleClassification()!=null?f.getSampleClassification().getId():null );
        stockInTubeDTO.setSampleTypeId(  f.getSampleType()!=null?f.getSampleType().getId():null  );
        stockInTubeDTO.setProjectSiteId(  f.getProjectSite()!=null?f.getProjectSite().getId():null  );
        stockInTubeDTO.setProjectId(  f.getProject()!=null?f.getProject().getId():null );
        stockInTubeDTO.setFrozenTubeTypeId( f.getFrozenTubeType()!=null?f.getFrozenTubeType().getId():null );

        stockInTubeDTO.setTubeRows( f.getTubeRows() );
        stockInTubeDTO.setTubeColumns( f.getTubeColumns() );
        stockInTubeDTO.setStatus( f.getStatus() );
        stockInTubeDTO.setMemo( f.getMemo() );
        stockInTubeDTO.setFrozenBoxCode( f.getFrozenBoxCode() );
        stockInTubeDTO.setProjectCode( f.getProjectCode() );
        stockInTubeDTO.setFrozenTubeCode( f.getFrozenTubeCode() );
        stockInTubeDTO.setSampleTempCode( f.getSampleTempCode() );
        stockInTubeDTO.setSampleCode( f.getSampleCode() );
        stockInTubeDTO.setFrozenTubeTypeCode( f.getFrozenTubeTypeCode() );
        stockInTubeDTO.setFrozenTubeTypeName( f.getFrozenTubeTypeName() );
        stockInTubeDTO.setSampleTypeCode( f.getSampleTypeCode() );
        stockInTubeDTO.setSampleTypeName( f.getSampleTypeName() );
        stockInTubeDTO.setSampleUsedTimesMost( f.getSampleUsedTimesMost() );
        stockInTubeDTO.setSampleUsedTimes( f.getSampleUsedTimes() );
        stockInTubeDTO.setFrozenTubeVolumns( f.getFrozenTubeVolumns() );
        stockInTubeDTO.setFrozenTubeVolumnsUnit( f.getFrozenTubeVolumnsUnit() );
        stockInTubeDTO.setSampleVolumns( f.getSampleVolumns() );
        stockInTubeDTO.setErrorType( f.getErrorType() );
        stockInTubeDTO.setFrozenTubeState( f.getFrozenTubeState() );
        stockInTubeDTO.setSampleClassificationCode( f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationCode():null );
        stockInTubeDTO.setSampleClassificationName(  f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationName():null );
        stockInTubeDTO.setProjectSiteCode( f.getProjectSiteCode() );
        stockInTubeDTO.setFrozenTubeType( f.getFrozenTubeType() );
        stockInTubeDTO.setSampleType( f.getSampleType() );
        stockInTubeDTO.setSampleClassification( f.getSampleClassification() );
        stockInTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
        stockInTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
        stockInTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
        stockInTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
        stockInTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
        return stockInTubeDTO;
    }


    default List<StockInTubeDTO> stockInTubesToStockInTubeDTOsForSampleType(List<StockInTube> stockInTubes){

        if ( stockInTubes == null ) {
            return null;
        }

        List<StockInTubeDTO> list = new ArrayList<StockInTubeDTO>();
        for ( StockInTube stockInTube : stockInTubes ) {
            StockInTubeDTO stockInTubeDTO = stockInTubeToStockInTubeDTOForSampleType( stockInTube,1 );
            list.add(stockInTubeDTO);
        }

        return list;
    }

    /**
     *
     * @param stockInTube
     * @param i 同一个Mapper里不能出现相同的入口参数& 返回参数，所以增加一个 I  与 stockInTubeToStockInTubeDTO 进行区分
     * @return
     */

    default StockInTubeDTO stockInTubeToStockInTubeDTOForSampleType(StockInTube stockInTube,int i){
        if ( stockInTube == null ) {
            return null;
        }

        StockInTubeDTO stockInTubeDTO = new StockInTubeDTO();

        stockInTubeDTO.setFrozenTubeId( stockInTube.getFrozenTube()!= null ?stockInTube.getFrozenTube().getId():null );
        stockInTubeDTO.setStockInBoxId( stockInTube.getStockInBox()!=null?stockInTube.getStockInBox().getId():null );
        stockInTubeDTO.setSampleClassificationId(stockInTube.getSampleClassification()!=null?stockInTube.getSampleClassification().getId():null);
        stockInTubeDTO.setSampleTypeId( stockInTube.getSampleType()!=null?stockInTube.getSampleType().getId():null );
        stockInTubeDTO.setProjectSiteId( stockInTube.getProjectSite()!=null?stockInTube.getProjectSite().getId():null );
        stockInTubeDTO.setProjectId( stockInTube.getProject()!=null?stockInTube.getProject().getId():null );
        stockInTubeDTO.setFrozenTubeTypeId( stockInTube.getFrozenTubeType()!=null?stockInTube.getFrozenTubeType().getId():null );
        stockInTubeDTO.setId( stockInTube.getId() );
        stockInTubeDTO.setTubeRows( stockInTube.getTubeRows() );
        stockInTubeDTO.setTubeColumns( stockInTube.getTubeColumns() );
        stockInTubeDTO.setStatus( stockInTube.getStatus() );
        stockInTubeDTO.setMemo( stockInTube.getMemo() );
        stockInTubeDTO.setFrozenBoxCode( stockInTube.getFrozenBoxCode() );
        stockInTubeDTO.setProjectCode( stockInTube.getProjectCode() );
        stockInTubeDTO.setFrozenTubeCode( stockInTube.getFrozenTubeCode() );
        stockInTubeDTO.setSampleTempCode( stockInTube.getSampleTempCode() );
        stockInTubeDTO.setSampleCode( stockInTube.getSampleCode() );
        stockInTubeDTO.setFrozenTubeTypeCode( stockInTube.getFrozenTubeTypeCode() );
        stockInTubeDTO.setFrozenTubeTypeName( stockInTube.getFrozenTubeTypeName() );
        stockInTubeDTO.setSampleTypeCode( stockInTube.getSampleTypeCode() );
        stockInTubeDTO.setSampleTypeName( stockInTube.getSampleTypeName() );
        stockInTubeDTO.setSampleUsedTimesMost( stockInTube.getSampleUsedTimesMost() );
        stockInTubeDTO.setSampleUsedTimes( stockInTube.getSampleUsedTimes() );
        stockInTubeDTO.setFrozenTubeVolumns( stockInTube.getFrozenTubeVolumns() );
        stockInTubeDTO.setFrozenTubeVolumnsUnit( stockInTube.getFrozenTubeVolumnsUnit() );
        stockInTubeDTO.setSampleVolumns( stockInTube.getSampleVolumns() );
        stockInTubeDTO.setErrorType( stockInTube.getErrorType() );
        stockInTubeDTO.setFrozenTubeState( stockInTube.getFrozenTubeState() );
        stockInTubeDTO.setSampleClassificationCode( stockInTube.getSampleClassificationCode() );
        stockInTubeDTO.setSampleClassificationName( stockInTube.getSampleClassificationName() );
        stockInTubeDTO.setProjectSiteCode( stockInTube.getProjectSiteCode() );
        stockInTubeDTO.setFrozenTubeType( stockInTube.getFrozenTubeType() );
        stockInTubeDTO.setSampleType( stockInTube.getSampleType() );
        stockInTubeDTO.setSampleClassification( stockInTube.getSampleClassification() );
        stockInTubeDTO.setFrontColor(stockInTube.getSampleType()!=null?stockInTube.getSampleType().getFrontColor():null);
        stockInTubeDTO.setFrontColorForClass(stockInTube.getSampleClassification()!=null?stockInTube.getSampleClassification().getFrontColor():null);
        stockInTubeDTO.setBackColor(stockInTube.getSampleType()!=null?stockInTube.getSampleType().getBackColor():null);
        stockInTubeDTO.setBackColorForClass(stockInTube.getSampleClassification()!=null?stockInTube.getSampleClassification().getBackColor():null);
        stockInTubeDTO.setIsMixed(stockInTube.getSampleType()!=null?stockInTube.getSampleType().getIsMixed():null);
        stockInTubeDTO.setTag1(stockInTube.getTag1());
        stockInTubeDTO.setTag2(stockInTube.getTag2());
        stockInTubeDTO.setTag3(stockInTube.getTag3());
        stockInTubeDTO.setTag4(stockInTube.getTag4());
        return stockInTubeDTO;
    }
    //根据入库管构造盒内入库冻存管
    default List<StockInTubeForBox> stockInTubesToStockInTubesForBox(List<StockInTube> stockInTubes){
        if ( stockInTubes == null ) {
            return null;
        }

        List<StockInTubeForBox> list = new ArrayList<StockInTubeForBox>();
        for ( StockInTube stockInTube : stockInTubes ) {
            StockInTubeForBox stockInTubeForBox = stockInTubeToStockInTubeForBox( stockInTube);
            list.add(stockInTubeForBox);
        }
        return list;
    }

    default StockInTubeForBox stockInTubeToStockInTubeForBox(StockInTube stockInTube){
        if ( stockInTube == null ) {
            return null;
        }
        StockInTubeForBox stockInTubeForBox = new StockInTubeForBox();
        stockInTubeForBox.setId(stockInTube.getId());
        stockInTubeForBox.setTubeRows(stockInTube.getTubeRows());
        stockInTubeForBox.setTubeColumns(stockInTube.getTubeColumns());
        stockInTubeForBox.setFrozenBoxCode(stockInTube.getFrozenBoxCode());
        stockInTubeForBox.setSampleCode(stockInTube.getSampleCode()!=null?stockInTube.getSampleCode():stockInTube.getSampleTempCode());
        stockInTubeForBox.setStatus(stockInTube.getStatus());
        stockInTubeForBox.setMemo(stockInTube.getMemo());
        SampleClassification sampleClassification = stockInTube.getSampleClassification();
        if(sampleClassification != null){
            stockInTubeForBox.setSampleClassificationId(sampleClassification.getId());
            stockInTubeForBox.setSampleClassificationName(sampleClassification.getSampleClassificationName());
            stockInTubeForBox.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
            stockInTubeForBox.setFrontColorForClass(sampleClassification.getFrontColor());
            stockInTubeForBox.setBackColorForClass(sampleClassification.getBackColor());
        }
        SampleType sampleType = stockInTube.getSampleType();
        if(sampleType!=null){
            stockInTubeForBox.setSampleTypeId(sampleType.getId());
            stockInTubeForBox.setSampleTypeCode(sampleType.getSampleTypeCode());
            stockInTubeForBox.setSampleTypeName(sampleType.getSampleTypeName());
            stockInTubeForBox.setIsMixed(sampleType.getIsMixed());
            stockInTubeForBox.setFrontColor(sampleType.getFrontColor());
            stockInTubeForBox.setBackColor(sampleType.getBackColor());
        }
        FrozenTubeType frozenTubeType = stockInTube.getFrozenTubeType();
        if(frozenTubeType!=null){
            stockInTubeForBox.setFrozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode());
            stockInTubeForBox.setFrozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName());
            stockInTubeForBox.setFrozenTubeTypeId(frozenTubeType.getId());
        }
        return stockInTubeForBox;
    }

    default StockInTube frozenTubeToStockInTube(FrozenTube tube,StockInBox stockInBox){
        if(tube == null){
            return null;
        }
        StockInTube stockInTube = new StockInTube();
        stockInTube.status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
            .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
            .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
            .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
            .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
            .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
            .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
            .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
            .sampleClassificationCode(tube.getSampleClassification()!=null?tube.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(tube.getSampleClassification()!=null?tube.getSampleClassification().getSampleClassificationName():null)
            .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
            .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
            .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
        stockInTube.setTag1(tube.getTag1());
        stockInTube.setTag2(tube.getTag2());
        stockInTube.setTag3(tube.getTag3());
        stockInTube.setTag4(tube.getTag4());
        return stockInTube;
    }
}
