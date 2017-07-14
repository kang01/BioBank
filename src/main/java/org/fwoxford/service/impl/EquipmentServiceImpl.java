package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.Area;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.Project;
import org.fwoxford.repository.AreaRepository;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.SupportRackRepository;
import org.fwoxford.service.EquipmentService;
import org.fwoxford.domain.Equipment;
import org.fwoxford.repository.EquipmentRepository;
import org.fwoxford.service.SupportRackService;
import org.fwoxford.service.dto.AreaDTO;
import org.fwoxford.service.dto.EquipmentDTO;
import org.fwoxford.service.dto.SupportRackDTO;
import org.fwoxford.service.mapper.AreaMapper;
import org.fwoxford.service.mapper.EquipmentMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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

    @Autowired
    private SupportRackService supportRackService;

    @Autowired
    private SupportRackRepository supportRackRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

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
        Page<Equipment> result = equipmentRepository.findAllUnFullEquipment(pageable);
        Iterator<Equipment> it = result.iterator();
        while(it.hasNext()){
            Equipment equipment = it.next();
            List<SupportRackDTO> supportRackDTOList = supportRackService.getIncompleteShelves(equipment.getEquipmentCode());
            if(supportRackDTOList==null || supportRackDTOList.size()==0){
                it.remove();
            }
        }
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
        if(equipment == null){
            throw new BankServiceException("设备不存在！");
        }
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);
        List<Area> areas = areaRepository.getAreaByEquipmentId(id);
        List<AreaDTO> areaDTOS = new ArrayList<AreaDTO>();
        int allAreas = areas.size();
        int usedAreas = 0;
        for(Area a:areas){
            AreaDTO areaDTO = areaMapper.areaToAreaDTO(a);
            Long count = supportRackRepository.countSupportRackByAreaId(a.getId());
            areaDTO.setFlag(Constants.NO);
            if(count.intValue()>0){
                areaDTO.setFlag(Constants.YES);
                usedAreas+=1;
            }
            areaDTOS.add(areaDTO);
        }
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findProjectByEquipmentId(id);
        ArrayList<String> positions = new ArrayList<>();
        for(FrozenBox f : frozenBoxes){
            Project project = f.getProject();
            if(!positions.contains(project.getProjectCode())){
                positions.add(project.getProjectCode());
            }
        }
        equipmentDTO.setAreaDTOList(areaDTOS);
        equipmentDTO.setEquipmentType(equipment.getEquipmentModle().getEquipmentType());
        equipmentDTO.setProjectCodes(String.join(";", positions));
        equipmentDTO.setRestOfSpace(allAreas-usedAreas);
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
