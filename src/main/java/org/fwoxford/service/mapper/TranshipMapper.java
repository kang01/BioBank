package org.fwoxford.service.mapper;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.Project;
import org.fwoxford.domain.ProjectSite;
import org.fwoxford.domain.Tranship;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.fwoxford.web.rest.util.BankUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
        res.setTranshipCode(tranship.getTranshipCode());
        res.setTranshipDate( tranship.getTranshipDate() );
        res.setProjectCode( tranship.getProjectCode() );
        res.setProjectSiteCode( tranship.getProjectSiteCode() );
        res.setTranshipState( tranship.getTranshipState() );
        res.setReceiver( tranship.getReceiver() );
        res.setReceiveDate( tranship.getReceiveDate() );
        res.setSampleSatisfaction( tranship.getSampleSatisfaction() );
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

    default  Tranship transhipToDefaultValue(Tranship ship){
        Tranship tranship = new Tranship();
        tranship.setId(ship.getId());
        tranship.setTranshipCode(ship.getTranshipCode()!=null ?ship.getTranshipCode():BankUtil.getUniqueID());
        tranship.setEffectiveSampleNumber(ship.getEffectiveSampleNumber()!=null?ship.getEffectiveSampleNumber():0);
        tranship.setProjectCode(ship.getProjectCode()!=null?ship.getProjectCode():new String(" "));
        tranship.setProject(ship.getProject()!=null && ship.getProject().getId()!=null?ship.getProject():null);
        tranship.setEmptyHoleNumber(ship.getEmptyHoleNumber()!=null?ship.getEmptyHoleNumber():0);
        tranship.setEmptyTubeNumber(ship.getEmptyTubeNumber()!=null?ship.getEmptyTubeNumber():0);
        tranship.setFrozenBoxNumber(ship.getFrozenBoxNumber()!=null?ship.getFrozenBoxNumber():0);
        tranship.setProjectName(ship.getProjectName()!=null?ship.getProjectName():new String(" "));
        tranship.setProjectSite(ship.getProjectSite()!=null && ship.getProjectSite().getId()!=null ? ship.getProjectSite():null);
        tranship.setProjectSiteCode(ship.getProjectSiteCode()!=null?ship.getProjectSiteCode():new String(" "));
        tranship.setProjectSiteName(ship.getProjectSiteName() !=null ? ship.getProjectSiteName():new String(" "));
        tranship.setReceiver(ship.getReceiver() !=null ? ship.getReceiver():new String(" "));
        tranship.setReceiveDate(ship.getReceiveDate() !=null ? ship.getReceiveDate():null);
        tranship.setSampleNumber(ship.getSampleNumber()!=null?ship.getSampleNumber():0);
        tranship.setTrackNumber(ship.getTrackNumber() !=null ? ship.getTrackNumber():new String(" "));
        tranship.setTranshipBatch(ship.getTranshipBatch() !=null ? ship.getTranshipBatch():new String(" "));
        tranship.setSampleSatisfaction(ship.getSampleSatisfaction()!=null?ship.getSampleSatisfaction():0);
        tranship.setTranshipDate(ship.getTranshipDate() !=null ? ship.getTranshipDate():null);
        tranship.setTranshipState(ship.getTranshipState()!=null?ship.getTranshipState():Constants.TRANSHIPE_IN_PENDING);
        tranship.setStatus(ship.getStatus()!=null?ship.getStatus():Constants.VALID);
        tranship.setMemo(ship.getMemo());
        return tranship;
    }
}
