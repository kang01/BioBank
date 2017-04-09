package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInDTO;

import org.fwoxford.service.dto.StockInForDataDetail;
import org.fwoxford.service.dto.response.StockInForDataTable;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity StockIn and its DTO StockInDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInMapper {

    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    StockInDTO stockInToStockInDTO(StockIn stockIn);

    List<StockInDTO> stockInsToStockInDTOs(List<StockIn> stockIns);

    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    StockIn stockInDTOToStockIn(StockInDTO stockInDTO);

    List<StockIn> stockInDTOsToStockIns(List<StockInDTO> stockInDTOS);

    default Tranship transhipFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tranship tranship = new Tranship();
        tranship.setId(id);
        return tranship;
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

    default List<StockInForDataTable> stockInsToStockInTables(List<StockIn> stockIns){
        if ( stockIns == null ) {
            return null;
        }

        List<StockInForDataTable> list = new ArrayList<StockInForDataTable>();
        for ( StockIn stockIn : stockIns ) {
            list.add( stockInToStockInTable( stockIn ) );
        }

        return list;
    }

    default StockInForDataTable stockInToStockInTable(StockIn stockIn){
        if ( stockIn == null ) {
            return null;
        }

        StockInForDataTable stockInTable = new StockInForDataTable();

        stockInTable.setId( stockIn.getId() );
        stockInTable.setProjectCode( stockIn.getProjectCode() );
        stockInTable.setProjectSiteCode( stockIn.getProjectSiteCode() );
        stockInTable.setStoreKeeper1( stockIn.getStoreKeeper1() );
        stockInTable.setStoreKeeper2( stockIn.getStoreKeeper2() );
        stockInTable.setStockInDate( stockIn.getStockInDate() );
        stockInTable.setCountOfSample( stockIn.getCountOfSample() );
        stockInTable.setStatus( stockIn.getStatus() );
        stockInTable.setTranshipCode(stockIn.getTranship().getTranshipCode());
        stockInTable.setCountOfBox(stockIn.getTranship().getFrozenBoxNumber());
        stockInTable.setRecordDate(stockIn.getTranship().getReceiveDate());
        return stockInTable;
    }

    default StockInForDataDetail stockInToStockInDetail(StockIn stockIn){
        if ( stockIn == null ) {
            return null;
        }

        StockInForDataDetail stockInDetail = new StockInForDataDetail();

        stockInDetail.setId( stockIn.getId() );
        stockInDetail.setProjectCode( stockIn.getProjectCode() );
        stockInDetail.setProjectSiteCode( stockIn.getProjectSiteCode() );
        stockInDetail.setStoreKeeper1( stockIn.getStoreKeeper1() );
        stockInDetail.setStoreKeeper2( stockIn.getStoreKeeper2() );
        stockInDetail.setStockInDate( stockIn.getStockInDate() );
        stockInDetail.setStatus( stockIn.getStatus() );
        stockInDetail.setTranshipCode(stockIn.getTranship().getTranshipCode());
        stockInDetail.setReceiveDate(stockIn.getReceiveDate());
        stockInDetail.setStockInCode(stockIn.getStockInCode());
        stockInDetail.setReceiver(stockIn.getReceiveName());
        return stockInDetail;
    }
}
