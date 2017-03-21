package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.EquipmentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Equipment and its DTO EquipmentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EquipmentMapper {

    @Mapping(source = "equipmentGroup.id", target = "equipmentGroupId")
    @Mapping(source = "equipmentModle.id", target = "equipmentModleId")
    EquipmentDTO equipmentToEquipmentDTO(Equipment equipment);

    List<EquipmentDTO> equipmentToEquipmentDTOs(List<Equipment> equipment);

    @Mapping(source = "equipmentGroupId", target = "equipmentGroup")
    @Mapping(source = "equipmentModleId", target = "equipmentModle")
    Equipment equipmentDTOToEquipment(EquipmentDTO equipmentDTO);

    List<Equipment> equipmentDTOsToEquipment(List<EquipmentDTO> equipmentDTOs);

    default EquipmentGroup equipmentGroupFromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentGroup equipmentGroup = new EquipmentGroup();
        equipmentGroup.setId(id);
        return equipmentGroup;
    }

    default EquipmentModle equipmentModleFromId(Long id) {
        if (id == null) {
            return null;
        }
        EquipmentModle equipmentModle = new EquipmentModle();
        equipmentModle.setId(id);
        return equipmentModle;
    }
}
