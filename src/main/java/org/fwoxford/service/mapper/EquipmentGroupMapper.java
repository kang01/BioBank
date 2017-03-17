package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.EquipmentGroupDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EquipmentGroup and its DTO EquipmentGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EquipmentGroupMapper {

    EquipmentGroupDTO equipmentGroupToEquipmentGroupDTO(EquipmentGroup equipmentGroup);

    List<EquipmentGroupDTO> equipmentGroupsToEquipmentGroupDTOs(List<EquipmentGroup> equipmentGroups);

    EquipmentGroup equipmentGroupDTOToEquipmentGroup(EquipmentGroupDTO equipmentGroupDTO);

    List<EquipmentGroup> equipmentGroupDTOsToEquipmentGroups(List<EquipmentGroupDTO> equipmentGroupDTOs);
}
