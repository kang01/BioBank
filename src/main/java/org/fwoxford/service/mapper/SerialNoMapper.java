package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.SerialNoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SerialNo and its DTO SerialNoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SerialNoMapper {

    SerialNoDTO serialNoToSerialNoDTO(SerialNo serialNo);

    List<SerialNoDTO> serialNosToSerialNoDTOs(List<SerialNo> serialNos);

    SerialNo serialNoDTOToSerialNo(SerialNoDTO serialNoDTO);

    List<SerialNo> serialNoDTOsToSerialNos(List<SerialNoDTO> serialNoDTOs);
}
