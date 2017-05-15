package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.DelegateDTO;

import org.mapstruct.*;
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
}
