package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.SupportRackDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SupportRack and its DTO SupportRackDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SupportRackMapper {

    @Mapping(source = "supportRackType.id", target = "supportRackTypeId")
    @Mapping(source = "area.id", target = "areaId")
    SupportRackDTO supportRackToSupportRackDTO(SupportRack supportRack);

    List<SupportRackDTO> supportRacksToSupportRackDTOs(List<SupportRack> supportRacks);

    @Mapping(source = "supportRackTypeId", target = "supportRackType")
    @Mapping(source = "area", target = "area")
    SupportRack supportRackDTOToSupportRack(SupportRackDTO supportRackDTO);

    List<SupportRack> supportRackDTOsToSupportRacks(List<SupportRackDTO> supportRackDTOs);

    default SupportRackType supportRackTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        SupportRackType supportRackType = new SupportRackType();
        supportRackType.setId(id);
        return supportRackType;
    }

    default Area areaFromId(Long id) {
        if (id == null) {
            return null;
        }
        Area area = new Area();
        area.setId(id);
        return area;
    }
}
