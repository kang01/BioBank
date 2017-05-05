package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockInBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockInBox and its DTO StockInBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInBoxMapper {

    @Mapping(source = "stockIn.id", target = "stockInId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    @Mapping(source = "area.id", target = "areaId")
    StockInBoxDTO stockInBoxToStockInBoxDTO(StockInBox stockInBox);

    List<StockInBoxDTO> stockInBoxesToStockInBoxDTOs(List<StockInBox> stockInBoxes);

    @Mapping(source = "stockInId", target = "stockIn")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "supportRackId", target = "supportRack")
    @Mapping(source = "areaId", target = "area")
    StockInBox stockInBoxDTOToStockInBox(StockInBoxDTO stockInBoxDTO);

    List<StockInBox> stockInBoxDTOsToStockInBoxes(List<StockInBoxDTO> stockInBoxDTOS);

    default StockIn stockInFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockIn stockIn = new StockIn();
        stockIn.setId(id);
        return stockIn;
    }

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }
    default Equipment equipmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        equipment.setId(id);
        return equipment;
    }

    default SupportRack supportRackFromId(Long id) {
        if (id == null) {
            return null;
        }
        SupportRack supportRack = new SupportRack();
        supportRack.setId(id);
        return supportRack;
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
