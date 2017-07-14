package org.fwoxford.service.impl;

import io.swagger.models.auth.In;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.EquipmentRepository;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.SupportRackService;
import org.fwoxford.repository.SupportRackRepository;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.SupportRackDTO;
import org.fwoxford.service.mapper.SupportRackMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SupportRack.
 */
@Service
@Transactional
public class SupportRackServiceImpl implements SupportRackService{

    private final Logger log = LoggerFactory.getLogger(SupportRackServiceImpl.class);

    private final SupportRackRepository supportRackRepository;

    private final SupportRackMapper supportRackMapper;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    public SupportRackServiceImpl(SupportRackRepository supportRackRepository, SupportRackMapper supportRackMapper) {
        this.supportRackRepository = supportRackRepository;
        this.supportRackMapper = supportRackMapper;
    }

    /**
     * Save a supportRack.
     *
     * @param supportRackDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SupportRackDTO save(SupportRackDTO supportRackDTO) {
        log.debug("Request to save SupportRack : {}", supportRackDTO);
        SupportRack supportRack = supportRackMapper.supportRackDTOToSupportRack(supportRackDTO);
        supportRack = supportRackRepository.save(supportRack);
        SupportRackDTO result = supportRackMapper.supportRackToSupportRackDTO(supportRack);
        return result;
    }

    /**
     *  Get all the supportRacks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SupportRackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SupportRacks");
        Page<SupportRack> result = supportRackRepository.findAll(pageable);
        return result.map(supportRack -> supportRackMapper.supportRackToSupportRackDTO(supportRack));
    }

    /**
     *  Get one supportRack by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SupportRackDTO findOne(Long id) {
        log.debug("Request to get SupportRack : {}", id);
        SupportRack supportRack = supportRackRepository.findOne(id);
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);
        return supportRackDTO;
    }

    /**
     *  Delete the  supportRack by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SupportRack : {}", id);
        supportRackRepository.delete(id);
    }

    @Override
    public List<SupportRackDTO> getSupportRackList(String equipmentCode) {
        List<SupportRack> supportRacks = supportRackRepository.findSupportRackByEquipmentCode(equipmentCode);
        return supportRackMapper.supportRacksToSupportRackDTOs(supportRacks);
    }

    @Override
    public List<SupportRackDTO> getIncompleteShelves(String equipmentCode) {
        Equipment equipment = equipmentRepository.findOneByEquipmentCode(equipmentCode);
        if(equipment == null){
            throw new BankServiceException("设备不存在！");
        }
        List<SupportRack> supportRacks = supportRackRepository.findByEquipmentId(equipment.getId());
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findByEquipmentCode(equipmentCode);
        List<SupportRack> supportRackList = new ArrayList<SupportRack>();
        for(SupportRack rack :supportRacks){
            SupportRackType supportRackType = rack.getSupportRackType();
            String rows = supportRackType.getSupportRackRows();
            String colomns = supportRackType.getSupportRackColumns();
            int countShelves = Integer.parseInt(rows)*Integer.parseInt(colomns);
            int count = 0;
            for(FrozenBox box :frozenBoxes){
                if(!box.getStatus().equals(Constants.FROZEN_BOX_INVALID )
                    && box.getSupportRack()!=null
                    && rack.getId().equals(box.getSupportRack().getId())){
                    count++;
                }
            }
            if(countShelves > count){
                supportRackList.add(rack);
            }
        }
        return supportRackMapper.supportRacksToSupportRackDTOs(supportRackList);
    }

    @Override
    public List<SupportRackDTO> getIncompleteShelvesByEquipmentAndArea(String equipmentCode, String areaCode) {
        List<SupportRack> supportRacks = supportRackRepository.findSupportRackByEquipmentCodeAndAreaCode(equipmentCode,areaCode);
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findByEquipmentCodeAndAreaCode(equipmentCode,areaCode);
        List<SupportRack> supportRackList = new ArrayList<SupportRack>();
        for(SupportRack rack :supportRacks){
            SupportRackType supportRackType = rack.getSupportRackType();
            String rows = supportRackType.getSupportRackRows();
            String colomns = supportRackType.getSupportRackColumns();
            int countShelves = Integer.parseInt(rows)*Integer.parseInt(colomns);
            int count = 0;
            for(FrozenBox box :frozenBoxes){
                if(!box.getStatus().equals(Constants.FROZEN_BOX_INVALID)
                    && box.getSupportRack()!=null&& rack.getId().equals(box.getSupportRack().getId())){
                    count++;
                }
            }
            if(countShelves > count){
                supportRackList.add(rack);
            }
        }
        return supportRackMapper.supportRacksToSupportRackDTOs(supportRackList);
    }

    @Override
    public List<SupportRackDTO> getSupportRackListByEquipmentAndArea(String equipmentCode, String areaCode) {
        List<SupportRack> supportRacks = supportRackRepository.findSupportRackByEquipmentCodeAndAreaCode(equipmentCode,areaCode);
        return supportRackMapper.supportRacksToSupportRackDTOs(supportRacks);
    }
    /**
     * 根據區域ID查詢架子列表
     * @param areaId 區域ID
     * @return
     */
    @Override
    public List<SupportRackDTO> findSupportRackByAreaId(Long areaId) {
        log.debug("Request to get SupportRack ByAreaId: {}", areaId);
        List<SupportRack> supportRacks = supportRackRepository.findSupportRackByAreaId(areaId);
        List<SupportRackDTO> supportRackDTOS = supportRackMapper.supportRacksToSupportRackDTOs(supportRacks);
        return supportRackDTOS;
    }

    @Override
    public List<SupportRackDTO> getSupportRackByEquipmentAndArea(Long equipmentId, Long areaId) {
        List<SupportRackDTO> supportRackDTOS = new ArrayList<SupportRackDTO>();
        List<SupportRack> supportRacks = supportRackRepository.findSupportRackByAreaId(areaId);
        for(SupportRack s :supportRacks){
            SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(s);
            supportRackDTO.setSupportRackColumns(s.getSupportRackType().getSupportRackColumns());
            supportRackDTO.setSupportRackRows(s.getSupportRackType().getSupportRackRows());
            Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCodeAndSupportRackCode(s.getArea().getEquipmentCode(),s.getArea().getAreaCode(),s.getSupportRackCode());
            supportRackDTO.setFlag(Constants.NO);
            if(count.intValue()>0){
                supportRackDTO.setFlag(Constants.YES);
            }
             supportRackDTOS.add(supportRackDTO);
        }
        return supportRackDTOS;
    }

    @Override
    public SupportRackDTO getSupportRackByEquipmentAndAreaAndShelf(String equipmentCode, String areaCode, String shelfCode) {
        SupportRack supportRack = supportRackRepository.findBySupportRackCode(shelfCode);
        if(supportRack == null){
            throw new BankServiceException("冻存架不存在！");
        }
        List<FrozenBox> frozenBoxs = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(equipmentCode, areaCode, shelfCode);
        List<FrozenBoxDTO> frozenBoxDTOList = new ArrayList<FrozenBoxDTO>();
        ArrayList<String> positions = new ArrayList<>();
        int i = 0;
        for (FrozenBox frozenBox : frozenBoxs) {
            if (frozenBox.getStatus() != null && (frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID) || frozenBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED))) {
                continue;
            }
            i++;
            Project project = frozenBox.getProject();
            if(!positions.contains(project.getProjectCode())){
                positions.add(project.getProjectCode());
            }
        }
        int columns = supportRack.getSupportRackType().getSupportRackColumns()!=null? Integer.valueOf(supportRack.getSupportRackType().getSupportRackColumns()):0;
        int rows = supportRack.getSupportRackType().getSupportRackRows()!=null? Integer.valueOf(supportRack.getSupportRackType().getSupportRackRows()):0;
        int all = columns*rows;
        SupportRackDTO supportRackDTO = supportRackMapper.supportRackToSupportRackDTO(supportRack);
        supportRackDTO.setSupportRackColumns(supportRack.getSupportRackType().getSupportRackColumns());
        supportRackDTO.setSupportRackRows(supportRack.getSupportRackType().getSupportRackRows());
        supportRackDTO.setFrozenBoxDTOList(frozenBoxDTOList);
        supportRackDTO.setEquipmentType(supportRack.getArea().getEquipment().getEquipmentModle().getEquipmentType());
        supportRackDTO.setPosition(BankUtil.getPositionString(equipmentCode,areaCode,shelfCode,null,null,null,null));
        supportRackDTO.setProjectCodes(String.join(";", positions));
        supportRackDTO.setRestOfSpace(all-i);
        return supportRackDTO;
    }
}
