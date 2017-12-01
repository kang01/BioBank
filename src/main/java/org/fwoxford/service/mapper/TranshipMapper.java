package org.fwoxford.service.mapper;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.fwoxford.web.rest.util.BankUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity Tranship and its DTO TranshipDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    @Mapping(source = "stockOutApply.id", target = "stockOutApplyId")
    @Mapping(source = "delegate.id", target = "delegateId")
    TranshipDTO transhipToTranshipDTO(Tranship tranship);

    List<TranshipDTO> transhipsToTranshipDTOs(List<Tranship> tranships);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
    @Mapping(source = "stockOutApplyId", target = "stockOutApply")
    @Mapping(source = "delegateId", target = "delegate")
    Tranship transhipDTOToTranship(TranshipDTO transhipDTO);

    List<Tranship> transhipDTOsToTranships(List<TranshipDTO> transhipDTOs);

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

    default StockOutApply stockOutApplyFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutApply stockOutApply = new StockOutApply();
        stockOutApply.setId(id);
        return stockOutApply;
    }

    default Delegate delegateFromId(Long id) {
        if (id == null) {
            return null;
        }
        Delegate delegate = new Delegate();
        delegate.setId(id);
        return delegate;
    }

    default List<TranshipResponse> transhipsToTranshipTranshipResponse(List<Tranship> tranships){
        if ( tranships == null ) {
            return null;
        }
        List<TranshipResponse> transhipResponses = new ArrayList<TranshipResponse>();
        for(Tranship ship:tranships){
            transhipResponses.add(transhipToTranshipResponses(ship));
        }
        return transhipResponses;
    }

    default TranshipResponse transhipToTranshipResponses(Tranship tranship){
        TranshipResponse res = new TranshipResponse();
        if ( tranship == null ) {
            return null;
        }
        res.setId( tranship.getId() );
        res.setTranshipCode(tranship.getTranshipCode());
        res.setTranshipDate( tranship.getTranshipDate() );
        res.setProjectCode( tranship.getProjectCode() );
        res.setProjectSiteCode( tranship.getProjectSiteCode() );
        res.setTranshipState( tranship.getTranshipState() );
        res.setReceiver( tranship.getReceiver() );
        res.setReceiveDate( tranship.getReceiveDate() );
        res.setSampleSatisfaction( tranship.getSampleSatisfaction() );
        res.setTrackNumber( tranship.getTrackNumber() );
        return res;
    }

    default TranshipByIdResponse transhipsToTranshipTranshipByIdResponse(Tranship tranship){
        TranshipByIdResponse res = new TranshipByIdResponse();
        if ( tranship == null ) {
            return null;
        }
        res.setId( tranship.getId() );
        res.setTranshipCode(tranship.getTranshipCode());
        res.setTranshipDate( tranship.getTranshipDate() );
        res.setProjectCode( tranship.getProjectCode() );
        res.setProjectSiteCode( tranship.getProjectSiteCode() );
        res.setTranshipState( tranship.getTranshipState() );
        res.setReceiver( tranship.getReceiver() );
        res.setReceiveDate( tranship.getReceiveDate() );
        res.setSampleSatisfaction( tranship.getSampleSatisfaction() );
        res.setProjectId(tranship.getProject() != null ?tranship.getProject().getId():null);
        res.setProjectSiteId(tranship.getProjectSite()!=null?tranship.getProjectSite().getId():null);
        return res;
    }
}
