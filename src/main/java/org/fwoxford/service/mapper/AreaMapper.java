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

    @Mapping(source = "equipment.id", target = "equipmentId")
    AreaDTO areaToAreaDTO(Area area);

    List<AreaDTO> areasToAreaDTOs(List<Area> areas);

    @Mapping(source = "equipmentId", target = "equipment")
    Area areaDTOToArea(AreaDTO areaDTO);

    List<Area> areaDTOsToAreas(List<AreaDTO> areaDTOs);

    default Equipment equipmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }
}
