package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.AreaDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Area and its DTO AreaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AreaMapper {
    @Mapping(source = "equipment.equipmentCode", target = "equipmentCode")
    AreaDTO areaToAreaDTO(Area area);

    List<AreaDTO> areasToAreaDTOs(List<Area> areas);

    @Mapping(source = "equipmentCode", target = "equipment")
    Area areaDTOToArea(AreaDTO areaDTO);

    List<Area> areaDTOsToAreas(List<AreaDTO> areaDTOs);

    default Equipment equipmentFromId(String equipmentCode) {
        if (equipmentCode == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setEquipmentCode(equipmentCode);
        return equipment;
    }
}
