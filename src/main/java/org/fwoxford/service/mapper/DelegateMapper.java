package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.DelegateDTO;

import org.fwoxford.service.dto.response.DelegateResponse;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity Delegate and its DTO DelegateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DelegateMapper {

    DelegateDTO delegateToDelegateDTO(Delegate delegate);

    List<DelegateDTO> delegatesToDelegateDTOs(List<Delegate> delegates);

    Delegate delegateDTOToDelegate(DelegateDTO delegateDTO);

    List<Delegate> delegateDTOsToDelegates(List<DelegateDTO> delegateDTOs);

    default List<DelegateResponse> delegatesToDelegateResponses(List<Delegate> delegates){
        if(delegates == null){
            return null;
        }
        List<DelegateResponse> delegateResponses = new ArrayList<DelegateResponse>();
        for(Delegate delegate : delegates){
            delegateResponses.add(delegateToDelegateResponse(delegate));
        }
        return delegateResponses;
    }

    default DelegateResponse delegateToDelegateResponse(Delegate delegate){
        if(delegate == null){
            return null;
        }
        DelegateResponse res = new DelegateResponse();
        res.setId(delegate.getId());
        res.setDelegateCode(delegate.getDelegateCode());
        res.setDelegateName(delegate.getDelegateName());
        return res;
    }
}
