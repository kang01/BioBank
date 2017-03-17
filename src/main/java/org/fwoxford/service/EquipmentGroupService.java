package org.fwoxford.service;

import org.fwoxford.service.dto.EquipmentGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing EquipmentGroup.
 */
public interface EquipmentGroupService {

    /**
     * Save a equipmentGroup.
     *
     * @param equipmentGroupDTO the entity to save
     * @return the persisted entity
     */
    EquipmentGroupDTO save(EquipmentGroupDTO equipmentGroupDTO);

    /**
     *  Get all the equipmentGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EquipmentGroupDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" equipmentGroup.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EquipmentGroupDTO findOne(Long id);

    /**
     *  Delete the "id" equipmentGroup.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
