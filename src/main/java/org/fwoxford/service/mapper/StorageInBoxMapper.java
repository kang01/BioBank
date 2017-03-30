package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StorageInBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StorageInBox and its DTO StorageInBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StorageInBoxMapper {

    @Mapping(source = "storageIn.id", target = "storageInId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "supportRack.id", target = "supportRackId")
    @Mapping(source = "area.id", target = "areaId")
    StorageInBoxDTO storageInBoxToStorageInBoxDTO(StorageInBox storageInBox);

    List<StorageInBoxDTO> storageInBoxesToStorageInBoxDTOs(List<StorageInBox> storageInBoxes);

    @Mapping(source = "storageInId", target = "storageIn")
    @Mapping(source = "equipmentId", target = "equipment")
    @Mapping(source = "supportRackId", target = "supportRack")
    @Mapping(source = "areaId", target = "area")
    StorageInBox storageInBoxDTOToStorageInBox(StorageInBoxDTO storageInBoxDTO);

    List<StorageInBox> storageInBoxDTOsToStorageInBoxes(List<StorageInBoxDTO> storageInBoxDTOs);

    default StorageIn storageInFromId(Long id) {
        if (id == null) {
            return null;
        }
        StorageIn storageIn = new StorageIn();
        storageIn.setId(id);
        return storageIn;
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
