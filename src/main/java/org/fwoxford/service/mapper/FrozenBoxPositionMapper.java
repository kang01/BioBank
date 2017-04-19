package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenBoxPositionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FrozenBoxPosition and its DTO FrozenBoxPositionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenBoxPositionMapper {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    FrozenBoxPositionDTO frozenBoxPositionToFrozenBoxPositionDTO(FrozenBoxPosition frozenBoxPosition);

    List<FrozenBoxPositionDTO> frozenBoxPositionsToFrozenBoxPositionDTOs(List<FrozenBoxPosition> frozenBoxPositions);

    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "areaId", target = "area")
    @Mapping(source = "supportRackId", target = "supportRack")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    FrozenBoxPosition frozenBoxPositionDTOToFrozenBoxPosition(FrozenBoxPositionDTO frozenBoxPositionDTO);

    List<FrozenBoxPosition> frozenBoxPositionDTOsToFrozenBoxPositions(List<FrozenBoxPositionDTO> frozenBoxPositionDTOs);

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

    default FrozenBoxPosition frozenBoxToFrozenBoxPosition(FrozenBoxPosition frozenBoxPosition,FrozenBox box){
        if(box == null){
            return frozenBoxPosition;
        }
        frozenBoxPosition.setEquipment(box.getEquipment());
        frozenBoxPosition.setEquipmentCode(box.getEquipmentCode());
        frozenBoxPosition.setArea(box.getArea());
        frozenBoxPosition.setAreaCode(box.getAreaCode());
        frozenBoxPosition.setSupportRack(box.getSupportRack());
        frozenBoxPosition.setSupportRackCode(box.getSupportRackCode());
        frozenBoxPosition.setColumnsInShelf(box.getColumnsInShelf());
        frozenBoxPosition.setRowsInShelf(box.getRowsInShelf());
        frozenBoxPosition.setMemo(box.getMemo());
        frozenBoxPosition.setStatus(box.getStatus());
        frozenBoxPosition.setFrozenBox(box);
        frozenBoxPosition.setFrozenBoxCode(box.getFrozenBoxCode());
        return  frozenBoxPosition;
    }
}
