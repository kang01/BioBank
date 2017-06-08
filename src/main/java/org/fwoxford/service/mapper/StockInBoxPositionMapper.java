package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInBoxPositionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockInBoxPosition and its DTO StockInBoxPositionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInBoxPositionMapper {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    @Mapping(source = "stockInBox.id", target = "stockInBoxId")
    StockInBoxPositionDTO stockInBoxPositionToStockInBoxPositionDTO(StockInBoxPosition stockInBoxPosition);

    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "supportRackId", target = "supportRack")
    @Mapping(source = "stockInBoxId", target = "stockInBox")
    StockInBoxPosition stockInBoxPositionDTOToStockInBoxPosition(StockInBoxPositionDTO stockInBoxPositionDTO);

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

    default StockInBox stockInBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInBox stockInBox = new StockInBox();
        stockInBox.setId(id);
        return stockInBox;
    }
}
