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
    StorageInBoxDTO storageInBoxToStorageInBoxDTO(StorageInBox storageInBox);

    List<StorageInBoxDTO> storageInBoxesToStorageInBoxDTOs(List<StorageInBox> storageInBoxes);

    @Mapping(source = "storageInId", target = "storageIn")
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
}
