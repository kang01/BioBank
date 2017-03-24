package org.fwoxford.service.impl;

import org.fwoxford.service.EquipmentService;
import org.fwoxford.domain.Equipment;
import org.fwoxford.repository.EquipmentRepository;
import org.fwoxford.service.dto.EquipmentDTO;
import org.fwoxford.service.mapper.EquipmentMapper;
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
 * Service Implementation for managing Equipment.
 */
@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService{

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
    }

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        log.debug("Request to save Equipment : {}", equipmentDTO);
        Equipment equipment = equipmentMapper.equipmentDTOToEquipment(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        EquipmentDTO result = equipmentMapper.equipmentToEquipmentDTO(equipment);
        return result;
    }

    /**
     *  Get all the equipment.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EquipmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Equipment");
        Page<Equipment> result = equipmentRepository.findAll(pageable);
        return result.map(equipment -> equipmentMapper.equipmentToEquipmentDTO(equipment));
    }

    /**
     *  Get one equipment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EquipmentDTO findOne(Long id) {
        log.debug("Request to get Equipment : {}", id);
        Equipment equipment = equipmentRepository.findOne(id);
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);
        return equipmentDTO;
    }

    /**
     *  Delete the  equipment by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Equipment : {}", id);
        equipmentRepository.delete(id);
    }

    /**
     * 查詢所有的設備列表
     * @return
     */
    @Override
    public List<EquipmentDTO> findAllEquipments() {
        log.debug("Request to search All Equipment : {}");
        List<Equipment> equipments = equipmentRepository.findAllEquipments();
        List<EquipmentDTO> equipmentDTOS = equipmentMapper.equipmentToEquipmentDTOs(equipments);
        return equipmentDTOS;
    }
}
