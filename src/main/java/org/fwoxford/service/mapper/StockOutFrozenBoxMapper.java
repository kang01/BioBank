package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutFrozenBox and its DTO StockOutFrozenBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutFrozenBoxMapper {

    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    @Mapping(source = "stockOutTask.id", target = "stockOutTaskId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    StockOutFrozenBoxDTO stockOutFrozenBoxToStockOutFrozenBoxDTO(StockOutFrozenBox stockOutFrozenBox);

    List<StockOutFrozenBoxDTO> stockOutFrozenBoxesToStockOutFrozenBoxDTOs(List<StockOutFrozenBox> stockOutFrozenBoxes);

    @Mapping(source = "frozenBoxId", target = "frozenBox")
    @Mapping(source = "stockOutTaskId", target = "stockOutTask")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "supportRackId", target = "supportRack")
    StockOutFrozenBox stockOutFrozenBoxDTOToStockOutFrozenBox(StockOutFrozenBoxDTO stockOutFrozenBoxDTO);

    List<StockOutFrozenBox> stockOutFrozenBoxDTOsToStockOutFrozenBoxes(List<StockOutFrozenBoxDTO> stockOutFrozenBoxDTOs);

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }

    default StockOutTask stockOutTaskFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.setId(id);
        return stockOutTask;
    }
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

}
