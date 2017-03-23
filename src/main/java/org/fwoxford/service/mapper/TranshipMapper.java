package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.domain.response.TranshipResponse;
import org.fwoxford.service.dto.TranshipDTO;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity Tranship and its DTO TranshipDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectSite.id", target = "projectSiteId")
    TranshipDTO transhipToTranshipDTO(Tranship tranship);

    List<TranshipDTO> transhipsToTranshipDTOs(List<Tranship> tranships);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectSiteId", target = "projectSite")
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
        res.setTranshipDate( tranship.getTranshipDate() );
        res.setProjectCode( tranship.getProjectCode() );
        res.setProjectSiteCode( tranship.getProjectSiteCode() );
        res.setTranshipState( tranship.getTranshipState() );
        res.setReceiver( tranship.getReceiver() );
        res.setReceiveDate( tranship.getReceiveDate() );
        res.setSampleSatisfaction( tranship.getSampleSatisfaction() );
        return res;
    }
}
