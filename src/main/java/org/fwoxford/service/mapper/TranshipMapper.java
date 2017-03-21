package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TranshipDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tranship and its DTO TranshipDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipMapper {

    TranshipDTO transhipToTranshipDTO(Tranship tranship);

    List<TranshipDTO> transhipsToTranshipDTOs(List<Tranship> tranships);

    Tranship transhipDTOToTranship(TranshipDTO transhipDTO);

    List<Tranship> transhipDTOsToTranships(List<TranshipDTO> transhipDTOs);
}
