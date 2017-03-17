package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.EquipmentModleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EquipmentModle and its DTO EquipmentModleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EquipmentModleMapper {

    EquipmentModleDTO equipmentModleToEquipmentModleDTO(EquipmentModle equipmentModle);

    List<EquipmentModleDTO> equipmentModlesToEquipmentModleDTOs(List<EquipmentModle> equipmentModles);

    EquipmentModle equipmentModleDTOToEquipmentModle(EquipmentModleDTO equipmentModleDTO);

    List<EquipmentModle> equipmentModleDTOsToEquipmentModles(List<EquipmentModleDTO> equipmentModleDTOs);
}
