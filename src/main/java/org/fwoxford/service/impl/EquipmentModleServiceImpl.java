package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.Equipment;
import org.fwoxford.service.EquipmentModleService;
import org.fwoxford.domain.EquipmentModle;
import org.fwoxford.repository.EquipmentModleRepository;
import org.fwoxford.service.dto.EquipmentModleDTO;
import org.fwoxford.service.mapper.EquipmentModleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing EquipmentModle.
 */
@Service
@Transactional
public class EquipmentModleServiceImpl implements EquipmentModleService{

    private final Logger log = LoggerFactory.getLogger(EquipmentModleServiceImpl.class);
    
    private final EquipmentModleRepository equipmentModleRepository;

    private final EquipmentModleMapper equipmentModleMapper;

    public EquipmentModleServiceImpl(EquipmentModleRepository equipmentModleRepository, EquipmentModleMapper equipmentModleMapper) {
        this.equipmentModleRepository = equipmentModleRepository;
        this.equipmentModleMapper = equipmentModleMapper;
    }

    /**
     * Save a equipmentModle.
     *
     * @param equipmentModleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EquipmentModleDTO save(EquipmentModleDTO equipmentModleDTO) {
        log.debug("Request to save EquipmentModle : {}", equipmentModleDTO);
        EquipmentModle equipmentModle = equipmentModleMapper.equipmentModleDTOToEquipmentModle(equipmentModleDTO);
        equipmentModle = equipmentModleRepository.save(equipmentModle);
        EquipmentModleDTO result = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);
        return result;
    }

    /**
     *  Get all the equipmentModles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EquipmentModleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EquipmentModles");
        Page<EquipmentModle> result = equipmentModleRepository.findAll(pageable);
        return result.map(equipmentModle -> equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle));
    }

    /**
     *  Get one equipmentModle by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EquipmentModleDTO findOne(Long id) {
        log.debug("Request to get EquipmentModle : {}", id);
        EquipmentModle equipmentModle = equipmentModleRepository.findOne(id);
        EquipmentModleDTO equipmentModleDTO = equipmentModleMapper.equipmentModleToEquipmentModleDTO(equipmentModle);
        return equipmentModleDTO;
    }

    /**
     *  Delete the  equipmentModle by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EquipmentModle : {}", id);
        equipmentModleRepository.delete(id);
    }

    /**
     * 获取所有的冻存架类型
     * @return
     */
    @Override
    public List<EquipmentModleDTO> findAllEquipmentType() {
        List<EquipmentModle> equipmentModles = equipmentModleRepository.findByStatus(Constants.VALID);
        for (int i = 0; i < equipmentModles.size() - 1; i++) {
            for (int j = equipmentModles.size() - 1; j > i; j--) {
                if (equipmentModles.get(j).getEquipmentType().equals(equipmentModles.get(i).getEquipmentType())) {
                    equipmentModles.remove(j);
                }
            }
        }
        return equipmentModleMapper.equipmentModlesToEquipmentModleDTOs(equipmentModles);
    }
}
