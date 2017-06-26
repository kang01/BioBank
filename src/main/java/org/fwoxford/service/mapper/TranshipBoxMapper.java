package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TranshipBoxDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TranshipBox and its DTO TranshipBoxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TranshipBoxMapper {

    @Mapping(source = "tranship.id", target = "transhipId")
    @Mapping(source = "frozenBox.id", target = "frozenBoxId")
    TranshipBoxDTO transhipBoxToTranshipBoxDTO(TranshipBox transhipBox);

    List<TranshipBoxDTO> transhipBoxesToTranshipBoxDTOs(List<TranshipBox> transhipBoxes);

    @Mapping(source = "transhipId", target = "tranship")
    @Mapping(source = "frozenBoxId", target = "frozenBox")
    TranshipBox transhipBoxDTOToTranshipBox(TranshipBoxDTO transhipBoxDTO);

    List<TranshipBox> transhipBoxDTOsToTranshipBoxes(List<TranshipBoxDTO> transhipBoxDTOs);

    default Tranship transhipFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tranship tranship = new Tranship();
        tranship.setId(id);
        return tranship;
    }

    default FrozenBox frozenBoxFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setId(id);
        return frozenBox;
    }

    default TranshipBox initTranshipToTranship(TranshipBox box){

        TranshipBox transhipBox = new TranshipBox();

        transhipBox.setFrozenBox(box.getFrozenBox());
        transhipBox.setTranship(box.getTranship() );
        transhipBox.setCreatedBy( box.getCreatedBy() );
        transhipBox.setCreatedDate( box.getCreatedDate() );
        transhipBox.setLastModifiedBy( box.getLastModifiedBy() );
        transhipBox.setLastModifiedDate( box.getLastModifiedDate() );
        transhipBox.setId( box.getId() );
        transhipBox.setFrozenBoxCode( box.getFrozenBoxCode() );
        transhipBox.setEquipmentCode( box.getEquipmentCode());
        transhipBox.setAreaCode( box.getAreaCode());
        transhipBox.setSupportRackCode( box.getSupportRackCode() );
        transhipBox.setRowsInShelf( box.getRowsInShelf() );
        transhipBox.setColumnsInShelf( box.getColumnsInShelf() );
        transhipBox.setCountOfSample( box.getCountOfSample() );
        transhipBox.setMemo( box.getMemo() );
        transhipBox.setStatus( box.getStatus() );
        transhipBox.setEquipment( box.getEquipment() );
        transhipBox.setArea( box.getArea() );
        transhipBox.setSupportRack( box.getSupportRack() );

        return transhipBox;
    }
}
