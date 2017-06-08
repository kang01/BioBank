package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutBoxPositionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutBoxPosition and its DTO StockOutBoxPositionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutBoxPositionMapper {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    StockOutBoxPositionDTO stockOutBoxPositionToStockOutBoxPositionDTO(StockOutBoxPosition stockOutBoxPosition);

    List<StockOutBoxPositionDTO> stockOutBoxPositionsToStockOutBoxPositionDTOs(List<StockOutBoxPosition> stockOutBoxPositions);

    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "supportRackId", target = "supportRack")
    StockOutBoxPosition stockOutBoxPositionDTOToStockOutBoxPosition(StockOutBoxPositionDTO stockOutBoxPositionDTO);

    List<StockOutBoxPosition> stockOutBoxPositionDTOsToStockOutBoxPositions(List<StockOutBoxPositionDTO> stockOutBoxPositionDTOs);

    default Equipment equipmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }

    default Area areaFromId(Long id) {
        if (id == null) {
            return null;
        }
        Area area = new Area();
        area.setId(id);
        return area;
    }

    default SupportRack supportRackFromId(Long id) {
        if (id == null) {
            return null;
        }
        SupportRack supportRack = new SupportRack();
        supportRack.setId(id);
        return supportRack;
    }

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }
}
