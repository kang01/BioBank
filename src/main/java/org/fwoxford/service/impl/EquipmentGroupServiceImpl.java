package org.fwoxford.service.impl;

import org.fwoxford.service.EquipmentGroupService;
import org.fwoxford.domain.EquipmentGroup;
import org.fwoxford.repository.EquipmentGroupRepository;
import org.fwoxford.service.dto.EquipmentGroupDTO;
import org.fwoxford.service.mapper.EquipmentGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing EquipmentGroup.
 */
@Service
@Transactional
public class EquipmentGroupServiceImpl implements EquipmentGroupService{

    private final Logger log = LoggerFactory.getLogger(EquipmentGroupServiceImpl.class);
    
    private final EquipmentGroupRepository equipmentGroupRepository;

    private final EquipmentGroupMapper equipmentGroupMapper;

    public EquipmentGroupServiceImpl(EquipmentGroupRepository equipmentGroupRepository, EquipmentGroupMapper equipmentGroupMapper) {
        this.equipmentGroupRepository = equipmentGroupRepository;
        this.equipmentGroupMapper = equipmentGroupMapper;
    }

    /**
     * Save a equipmentGroup.
     *
     * @param equipmentGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EquipmentGroupDTO save(EquipmentGroupDTO equipmentGroupDTO) {
        log.debug("Request to save EquipmentGroup : {}", equipmentGroupDTO);
        EquipmentGroup equipmentGroup = equipmentGroupMapper.equipmentGroupDTOToEquipmentGroup(equipmentGroupDTO);
        equipmentGroup = equipmentGroupRepository.save(equipmentGroup);
        EquipmentGroupDTO result = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);
        return result;
    }

    /**
     *  Get all the equipmentGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EquipmentGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentGroups");
        Page<EquipmentGroup> result = equipmentGroupRepository.findAll(pageable);
        return result.map(equipmentGroup -> equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup));
    }

    /**
     *  Get one equipmentGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EquipmentGroupDTO findOne(Long id) {
        log.debug("Request to get EquipmentGroup : {}", id);
        EquipmentGroup equipmentGroup = equipmentGroupRepository.findOne(id);
        EquipmentGroupDTO equipmentGroupDTO = equipmentGroupMapper.equipmentGroupToEquipmentGroupDTO(equipmentGroup);
        return equipmentGroupDTO;
    }

    /**
     *  Delete the  equipmentGroup by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EquipmentGroup : {}", id);
        equipmentGroupRepository.delete(id);
    }
}
