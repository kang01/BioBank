package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.PositionMoveService;
import org.fwoxford.service.dto.PositionMoveDTO;
import org.fwoxford.service.dto.PositionMoveRecordDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.PositionMoveMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
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
 * Service Implementation for managing PositionMove.
 */
@Service
@Transactional
public class PositionMoveServiceImpl implements PositionMoveService {

    private final Logger log = LoggerFactory.getLogger(PositionMoveServiceImpl.class);

    private final PositionMoveRepository positionMoveRepository;

    private final PositionMoveMapper positionMoveMapper;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private ProjectSampleClassRepository projectSampleClassRepository;

    @Autowired
    private PositionMoveRecordRepository positionMoveRecordRepository;
    @Autowired
    private SupportRackRepository supportRackRepository;
    @Autowired
    private AreaRepository areaRepository;

    public PositionMoveServiceImpl(PositionMoveRepository positionMoveRepository, PositionMoveMapper positionMoveMapper) {
        this.positionMoveRepository = positionMoveRepository;
        this.positionMoveMapper = positionMoveMapper;
    }

    /**
     * Save a positionMove.
     *
     * @param positionMoveDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionMoveDTO save(PositionMoveDTO positionMoveDTO) {
        log.debug("Request to save PositionMove : {}", positionMoveDTO);
        PositionMove positionMove = positionMoveMapper.positionMoveDTOToPositionMove(positionMoveDTO);
        positionMove = positionMoveRepository.save(positionMove);
        PositionMoveDTO result = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);
        return result;
    }

    /**
     * Get all the positionMoves.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionMoveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionMoves");
        Page<PositionMove> result = positionMoveRepository.findAll(pageable);
        return result.map(positionMove -> positionMoveMapper.positionMoveToPositionMoveDTO(positionMove));
    }

    /**
     * Get one positionMove by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionMoveDTO findOne(Long id) {
        log.debug("Request to get PositionMove : {}", id);
        PositionMove positionMove = positionMoveRepository.findOne(id);
        PositionMoveDTO positionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);
        return positionMoveDTO;
    }

    /**
     * Delete the  positionMove by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionMove : {}", id);
        positionMoveRepository.delete(id);
    }

    /**
     * 样本移位
     *
     * @param positionMoveDTO
     * @return
     */
    @Override
    public PositionMoveSampleDTO moveSamplePosition(PositionMoveSampleDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveSampleDTOToPositionMove(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_1);
        positionMove.setStatus(Constants.VALID);
        positionMoveRepository.save(positionMove);
        //保存移位记录详情数据
        List<PositionMoveForBox> positionMoveForBoxes = positionMoveDTO.getPositionMoveForBoxList();
        for (PositionMoveForBox p : positionMoveForBoxes) {
            createMoveRecordDetail(p, positionMove);
        }
        positionMoveDTO.setId(positionMove.getId());
        return positionMoveDTO;
    }

    @Override
    public PositionMoveBoxDTO savePositionMoveForBox(PositionMoveBoxDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveBoxDTOToPositionMove(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_2);
        positionMove.setStatus(Constants.VALID);
        positionMoveRepository.save(positionMove);
        positionMoveDTO.setId(positionMove.getId());
        List<PositionMoveForShelves> positionMoveForShelves = positionMoveDTO.getPositionMoveForShelves();
        for(PositionMoveForShelves shelves:positionMoveForShelves){
            if(shelves.getId() == null){
                throw new BankServiceException("冻存架ID不能为空！");
            }
            SupportRack supportRack = supportRackRepository.findOne(shelves.getId());
            if(supportRack == null) {
                throw new BankServiceException("冻存架不存在！");
            }
            List<PositionMoveForBox> positionMoveForBoxes = shelves.getPositionMoveForBoxList();
            for(PositionMoveForBox p : positionMoveForBoxes){
                if(p.getId() == null){
                    throw new BankServiceException("冻存盒ID不能为空！");
                }
                FrozenBox frozenBox = frozenBoxRepository.findOne(p.getId());
                if(frozenBox == null || (frozenBox != null &&frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED))){
                    throw new BankServiceException("冻存盒不存在！");
                }
                if(p.getRowsInShelf()== null || p.getColumnsInShelf() == null){
                    throw new BankServiceException("未指定冻存盒位置！");
                }
                checkShelves(supportRack,p.getColumnsInShelf(),p.getRowsInShelf());
                List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(p.getId());
                 for(FrozenTube frozenTube:frozenTubeList){
                     PositionMoveRecord positionMoveRecord = new PositionMoveRecord()
                         .positionMove(positionMove)
                         .moveType(Constants.MOVE_TYPE_2)
                         .equipment(frozenTube.getFrozenBox().getEquipment())
                         .equipmentCode(frozenTube.getFrozenBox().getEquipmentCode())
                         .area(frozenTube.getFrozenBox().getArea())
                         .areaCode(frozenTube.getFrozenBox().getAreaCode())
                         .supportRack(frozenTube.getFrozenBox().getSupportRack())
                         .supportRackCode(frozenTube.getFrozenBox().getSampleTypeCode())
                         .columnsInShelf(frozenTube.getFrozenBox().getColumnsInShelf())
                         .rowsInShelf(frozenTube.getFrozenBox().getRowsInShelf())
                         .frozenBox(frozenTube.getFrozenBox())
                         .frozenBoxCode(frozenTube.getFrozenBoxCode())
                         .memo(frozenTube.getMemo())
                         .project(frozenTube.getProject())
                         .projectCode(frozenTube.getProjectCode())
                         .projectSite(frozenTube.getProjectSite())
                         .projectSiteCode(frozenTube.getProjectSiteCode())
                         .whetherFreezingAndThawing(positionMove.isWhetherFreezingAndThawing())
                         .status(frozenTube.getStatus())
                         .tubeColumns(frozenTube.getTubeColumns())
                         .tubeRows(frozenTube.getTubeRows());
                     positionMoveRecordRepository.save(positionMoveRecord);
                }
                frozenBox =  frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
                    .area(supportRack.getArea())
                    .areaCode(supportRack.getArea().getAreaCode())
                    .supportRack(supportRack)
                    .supportRackCode(supportRack.getSupportRackCode())
                    .columnsInShelf(p.getColumnsInShelf())
                    .rowsInShelf(p.getRowsInShelf());
                frozenBoxRepository.save(frozenBox);
            }
        }
        return positionMoveDTO;
    }

    public void checkShelves(SupportRack supportRack, String columnsInShelf, String rowsInShelf) {
        FrozenBox frozenBox = frozenBoxRepository.findBySupportRackIdAndColumnsInShelfAndRowsInShelf(supportRack.getId(),columnsInShelf,rowsInShelf);
        if(frozenBox != null){
            throw new BankServiceException(supportRack.getSupportRackCode()+"内"+rowsInShelf+columnsInShelf+"位置已存在冻存盒，不能移入！");
        }
    }

    @Override
    public PositionMoveShelvesDTO savePositionMoveForShelf(PositionMoveShelvesDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveShelvesDTOToPositionMove(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_3);
        positionMove.setStatus(Constants.VALID);
        positionMoveRepository.save(positionMove);
        positionMoveDTO.setId(positionMove.getId());
        List<PositionMoveForArea> positionMoveForAreas = positionMoveDTO.getPositionMoveForAreas();
        for(PositionMoveForArea p:positionMoveForAreas){
            if(p.getId() == null){
                throw new BankServiceException("区域ID不能空！");
            }
            Area area = areaRepository.findOne(p.getId());
            if(area == null){
                throw new BankServiceException("区域不存在！");
            }
            List<PositionMoveForShelves> positionMoveForShelves = p.getPositionMoveForShelves();
            for(PositionMoveForShelves shelve :positionMoveForShelves){
                if(shelve.getId() == null){
                    throw new BankServiceException("冻存架ID不能为空！");
                }
                SupportRack supportRack = supportRackRepository.findOne(shelve.getId());
                if(supportRack == null){
                    throw new BankServiceException("冻存架不存在！");
                }
                checkArea(area);
                List<FrozenBox> frozenBoxList = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(supportRack.getArea().getEquipmentCode(),supportRack.getArea().getAreaCode(),supportRack.getSupportRackCode());
               for(FrozenBox frozenBox:frozenBoxList){
                   List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(p.getId());
                   for(FrozenTube frozenTube:frozenTubeList){
                       saveMoveDetail(positionMove,Constants.MOVE_TYPE_3,frozenTube);
                   }
                   frozenBox =  frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
                       .area(supportRack.getArea())
                       .areaCode(supportRack.getArea().getAreaCode())
                       .supportRack(supportRack)
                       .supportRackCode(supportRack.getSupportRackCode());
                   frozenBoxRepository.save(frozenBox);
               }
                supportRack.area(area);
                supportRackRepository.save(supportRack);
            }

        }
        return positionMoveDTO;
    }

    @Override
    public PositionMoveDTO creataSamplePosition(PositionMoveDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveDTOToPositionMove(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_1);
        positionMove.setStatus(Constants.VALID);
        positionMoveRepository.save(positionMove);
        //保存移位记录详情数据
        List<PositionMoveRecordDTO> positionMoveForBoxes = positionMoveDTO.getPositionMoveRecordDTOS();
        for (PositionMoveRecordDTO p : positionMoveForBoxes) {
            createMoveRecordDetailForSample(p, positionMove);
        }
        positionMoveDTO.setId(positionMove.getId());
        return positionMoveDTO;
    }

    @Override
    public PositionMoveDTO createPositionMoveForBox(PositionMoveDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveDTOToPositionMove(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_2);
        positionMove.setStatus(Constants.VALID);
        positionMoveRepository.save(positionMove);
        List<PositionMoveRecordDTO> positionMoveForBoxes = positionMoveDTO.getPositionMoveRecordDTOS();
        for (PositionMoveRecordDTO p : positionMoveForBoxes) {
            createMoveRecordDetailForBox(p, positionMove);
        }
        positionMoveDTO.setId(positionMove.getId());
        return positionMoveDTO;
    }

    @Override
    public PositionMoveDTO createPositionMoveForShelf(PositionMoveDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveDTOToPositionMove(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_3);
        positionMove.setStatus(Constants.VALID);
        positionMoveRepository.save(positionMove);
        positionMoveDTO.setId(positionMove.getId());
        List<PositionMoveRecordDTO> positionMoveForAreas = positionMoveDTO.getPositionMoveRecordDTOS();
        for(PositionMoveRecordDTO p:positionMoveForAreas){
            if(p.getAreaId() == null){
                throw new BankServiceException("区域ID不能空！");
            }
            Area area = areaRepository.findOne(p.getAreaId());
            if(area == null){
                throw new BankServiceException("区域不存在！");
            }
            if(p.getSupportRackId() == null){
                throw new BankServiceException("冻存架ID不能为空！");
            }
            SupportRack supportRack = supportRackRepository.findOne(p.getSupportRackId());
            if(supportRack == null){
                throw new BankServiceException("冻存架不存在！");
            }
             checkArea(area);
             List<FrozenBox> frozenBoxList = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(supportRack.getArea().getEquipmentCode(),supportRack.getArea().getAreaCode(),supportRack.getSupportRackCode());
             for(FrozenBox frozenBox:frozenBoxList){
                List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(p.getId());
                for(FrozenTube frozenTube:frozenTubeList){
                    saveMoveDetail(positionMove,Constants.MOVE_TYPE_3,frozenTube);
                }
                frozenBox =  frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
                    .area(supportRack.getArea())
                    .areaCode(supportRack.getArea().getAreaCode())
                    .supportRack(supportRack)
                    .supportRackCode(supportRack.getSupportRackCode());
                frozenBoxRepository.save(frozenBox);
             }
            supportRack.area(area);
            supportRackRepository.save(supportRack);
        }
        return positionMoveDTO;
    }

    public void createMoveRecordDetailForBox(PositionMoveRecordDTO p, PositionMove positionMove) {
        if(p.getSupportRackId() == null){
            throw new BankServiceException("冻存架ID不能为空！");
        }
        SupportRack supportRack = supportRackRepository.findOne(p.getSupportRackId());
        if(supportRack == null) {
            throw new BankServiceException("冻存架不存在！");
        }
        if(p.getFrozenBoxId() == null){
            throw new BankServiceException("冻存盒ID不能为空！");
         }
        FrozenBox frozenBox = frozenBoxRepository.findOne(p.getFrozenBoxId());
        if(frozenBox == null || (frozenBox != null &&!frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED))){
            throw new BankServiceException("冻存盒不存在！");
        }
        if(p.getRowsInShelf()== null || p.getColumnsInShelf() == null){
            throw new BankServiceException("未指定冻存盒位置！");
        }
        checkShelves(supportRack,p.getColumnsInShelf(),p.getRowsInShelf());
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(p.getFrozenBoxId());
        for(FrozenTube frozenTube:frozenTubeList){
            saveMoveDetail(positionMove,Constants.MOVE_TYPE_2,frozenTube);
        }
        frozenBox =  frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
            .area(supportRack.getArea())
            .areaCode(supportRack.getArea().getAreaCode())
            .supportRack(supportRack)
            .supportRackCode(supportRack.getSupportRackCode())
            .columnsInShelf(p.getColumnsInShelf())
            .rowsInShelf(p.getRowsInShelf());
        frozenBoxRepository.save(frozenBox);
    }


    public void createMoveRecordDetailForSample(PositionMoveRecordDTO p, PositionMove positionMove) {
        if (p.getFrozenBoxId() == null) {
            throw new BankServiceException("冻存盒ID不能为空！");
        }
        FrozenBox frozenBox = frozenBoxRepository.findOne(p.getFrozenBoxId());
        if(frozenBox == null ||(frozenBox!= null && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED))){
            throw new BankServiceException("冻存盒不在库存中！");
        }
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
        List<Long> frozenTubeIds = new ArrayList<Long>();
        for(FrozenTube f :frozenTubeList){
            frozenTubeIds.add(f.getId());
        }
        if (p.getFrozenTubeId() == null) {
                throw new BankServiceException("冻存管ID不能为空！");
            }
        FrozenTube frozenTube = frozenTubeRepository.findOne(p.getFrozenTubeId());
        if(frozenTube == null || (!frozenTube.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKED))){
            throw new BankServiceException("样本不在库存中！");
        }
        if(!frozenTubeIds.contains(frozenTube.getId())){
            String sampleCode = frozenTube.getSampleCode() != null ? frozenTube.getSampleCode() : frozenTube.getSampleTempCode();
            //验证项目是否一致
            checkProject(frozenBox, frozenTube);
            //验证冻存盒类型与原冻存盒类型是否一样
            checkFrozenBoxType(frozenBox, frozenTube);
            //验证样本类型与盒子是否一致，盒子若有分类，验证与冻存管是否一致，冻存管若有分类，验证与冻存盒是否一致
            checkSampleTypeAndClassification(frozenBox, frozenTube);
            //验证盒内位置是否有效
            checkPositionInBox(frozenBox,p.getTubeRows(),p.getTubeColumns(),sampleCode);
            saveMoveDetail(positionMove,Constants.MOVE_TYPE_1,frozenTube);
            frozenTube.setTubeColumns(p.getTubeColumns());
            frozenTube.setTubeRows(p.getTubeRows());
            frozenTube.setFrozenBox(frozenBox);
            frozenTube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            frozenTubeRepository.save(frozenTube);
        }
    }

    public void saveMoveDetail(PositionMove positionMove, String moveType, FrozenTube frozenTube) {
        PositionMoveRecord positionMoveRecord = new PositionMoveRecord()
            .positionMove(positionMove)
            .frozenTube(frozenTube)
            .moveType(moveType)
            .equipment(frozenTube.getFrozenBox().getEquipment())
            .equipmentCode(frozenTube.getFrozenBox().getEquipmentCode())
            .area(frozenTube.getFrozenBox().getArea())
            .areaCode(frozenTube.getFrozenBox().getAreaCode())
            .supportRack(frozenTube.getFrozenBox().getSupportRack())
            .supportRackCode(frozenTube.getFrozenBox().getSampleTypeCode())
            .columnsInShelf(frozenTube.getFrozenBox().getColumnsInShelf())
            .rowsInShelf(frozenTube.getFrozenBox().getRowsInShelf())
            .frozenBox(frozenTube.getFrozenBox())
            .frozenBoxCode(frozenTube.getFrozenBoxCode())
            .memo(frozenTube.getMemo())
            .project(frozenTube.getProject())
            .projectCode(frozenTube.getProjectCode())
            .projectSite(frozenTube.getProjectSite())
            .projectSiteCode(frozenTube.getProjectSiteCode())
            .whetherFreezingAndThawing(positionMove.isWhetherFreezingAndThawing())
            .status(frozenTube.getStatus())
            .tubeColumns(frozenTube.getTubeColumns())
            .tubeRows(frozenTube.getTubeRows());
        positionMoveRecordRepository.save(positionMoveRecord);
    }

    public void checkArea(Area area) {
        //判断这个设备下的这个区域是否还有剩余空间
        List<SupportRack> supportRack = supportRackRepository.findSupportRackByAreaId(area.getId());
        if(supportRack.size()>=area.getFreezeFrameNumber()){
            throw new BankServiceException("该区域已满，不能移入冻存架");
        }
    }

    public void createMoveRecordDetail(PositionMoveForBox p, PositionMove positionMove) {
        if (p.getId() == null) {
            throw new BankServiceException("冻存盒ID不能为空！");
        }
        FrozenBox frozenBox = frozenBoxRepository.findOne(p.getId());
        if(frozenBox == null ||(frozenBox!= null && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED))){
            throw new BankServiceException("冻存盒不在库存中！");
        }
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
        List<Long> frozenTubeIds = new ArrayList<Long>();
        for(FrozenTube f :frozenTubeList){
            frozenTubeIds.add(f.getId());
        }
        List<PositionMoveForSample> sampleList = p.getFrozenTubeDTOS();
        for (PositionMoveForSample s : sampleList) {
            if (s.getId() == null) {
                throw new BankServiceException("冻存管ID不能为空！");
            }
            FrozenTube frozenTube = frozenTubeRepository.findOne(s.getId());
            if(frozenTube == null || (!frozenTube.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKED))){
                throw new BankServiceException("样本不在库存中！");
            }
            if(frozenTubeIds.contains(frozenTube.getId())){
                continue;
            }
            String sampleCode = frozenTube.getSampleCode() != null ? frozenTube.getSampleCode() : frozenTube.getSampleTempCode();
            //验证项目是否一致
            checkProject(frozenBox, frozenTube);
            //验证冻存盒类型与原冻存盒类型是否一样
            checkFrozenBoxType(frozenBox, frozenTube);
            //验证样本类型与盒子是否一致，盒子若有分类，验证与冻存管是否一致，冻存管若有分类，验证与冻存盒是否一致
            checkSampleTypeAndClassification(frozenBox, frozenTube);
            //验证盒内位置是否有效
            checkPositionInBox(frozenBox,s.getTubeRows(),s.getTubeColumns(),sampleCode);
            PositionMoveRecord positionMoveRecord = new PositionMoveRecord()
                .positionMove(positionMove)
                .moveType(Constants.MOVE_TYPE_1)
                .equipment(frozenTube.getFrozenBox().getEquipment())
                .equipmentCode(frozenTube.getFrozenBox().getEquipmentCode())
                .area(frozenTube.getFrozenBox().getArea())
                .areaCode(frozenTube.getFrozenBox().getAreaCode())
                .supportRack(frozenTube.getFrozenBox().getSupportRack())
                .supportRackCode(frozenTube.getFrozenBox().getSampleTypeCode())
                .columnsInShelf(frozenTube.getFrozenBox().getColumnsInShelf())
                .rowsInShelf(frozenTube.getFrozenBox().getRowsInShelf())
                .frozenBox(frozenTube.getFrozenBox())
                .frozenBoxCode(frozenTube.getFrozenBoxCode())
                .memo(frozenTube.getMemo())
                .project(frozenTube.getProject())
                .projectCode(frozenTube.getProjectCode())
                .projectSite(frozenTube.getProjectSite())
                .projectSiteCode(frozenTube.getProjectSiteCode())
                .whetherFreezingAndThawing(positionMove.isWhetherFreezingAndThawing())
                .status(frozenTube.getStatus())
                .tubeColumns(frozenTube.getTubeColumns())
                .tubeRows(frozenTube.getTubeRows());
            positionMoveRecordRepository.save(positionMoveRecord);
            frozenTube.setTubeColumns(s.getTubeColumns());
            frozenTube.setTubeRows(s.getTubeRows());
            frozenTube.setFrozenBox(frozenBox);
            frozenTube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            frozenTubeRepository.save(frozenTube);
        }

    }

    public void checkFrozenBoxType(FrozenBox frozenBox, FrozenTube frozenTube) {
        String sampleCode = frozenTube.getSampleCode() != null ? frozenTube.getSampleCode() : frozenTube.getSampleTempCode();
        if (frozenTube.getFrozenBox().getFrozenBoxType().getId() != frozenBox.getFrozenBoxType().getId()) {
            throw new BankServiceException("冻存盒类型不一致，编码为" + sampleCode
                + "的冻存管，不能移入" + frozenBox.getFrozenBoxCode() + "冻存盒内！");
        }
    }

    public void checkSampleTypeAndClassification(FrozenBox frozenBox, FrozenTube frozenTube) {
        SampleType sampleTypeOfTube = frozenTube.getSampleType();
        SampleType sampleTypeOfBox = frozenBox.getSampleType();
        String sampleCode = frozenTube.getSampleCode() != null ? frozenTube.getSampleCode() : frozenTube.getSampleTempCode();
        //97，无分类的盒子可以加入任何类型的样本
        Project project = frozenBox.getProject();
        //验证项目下该样本类型是否有样本分类，如果已经配置了样本分类，则样本分类ID不能为空，（99的除外）
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findByProjectIdAndSampleTypeId(project != null ? project.getId() : null, sampleTypeOfBox.getId());
        List<Long> sampleClassificationId = new ArrayList<Long>();
        for (ProjectSampleClass p : projectSampleClasses) {
            if (p.getSampleClassification() != null) {
                sampleClassificationId.add(p.getSampleClassification().getId());
            }
        }
        //不混合，有分类
        if (projectSampleClasses.size() > 0 && sampleTypeOfBox.getIsMixed().equals(Constants.NO) && frozenTube.getSampleClassification() == null) {
            throw new BankServiceException("样本分类不一致，编码为" + sampleCode+ "的冻存管，不能移入" + frozenBox.getFrozenBoxCode() + "冻存盒内！");
        }
        //非99，非97，不混合，有分类，必须放同类型同分类的样本
        if (frozenTube.getSampleClassification() != null && frozenBox.getSampleClassification() != null && frozenTube.getSampleClassification().getId() != frozenBox.getSampleClassification().getId()) {
            throw new BankServiceException("样本分类不一致，编码为"+sampleCode+"的冻存管，不能移入" + frozenBox.getFrozenBoxCode() + "冻存盒内！");
        }
        //非99，非97，不混合，无分类，必须放入相同类型，无分类的样本
        if (sampleTypeOfBox.getIsMixed().equals(Constants.NO) && projectSampleClasses.size()==0
            && (!sampleTypeOfTube.getId().equals(sampleTypeOfBox.getId()) || sampleTypeOfTube.getId() != sampleTypeOfBox.getId())) {
            throw new BankServiceException("样本类型不一致，编码为" + sampleCode+ "的冻存管，不能移入" + frozenBox.getFrozenBoxCode() + "冻存盒内！");
        }
        //99，混合，有分类的盒子可以加入99配置的分类的样本
        if (sampleTypeOfTube.getIsMixed().equals(Constants.YES) && sampleTypeOfBox.equals("99")&&projectSampleClasses.size() > 0
            &&(frozenTube.getSampleClassification()==null || frozenTube.getSampleClassification() != null && !sampleClassificationId.contains(frozenTube.getSampleClassification().getId()))) {
            throw new BankServiceException("该冻存盒未配置，" + sampleCode + "分类，编码为" + frozenTube.getSampleCode() != null ? frozenTube.getSampleCode() : frozenTube.getSampleTempCode()+ "的冻存管，不能移入" + frozenBox.getFrozenBoxCode() + "冻存盒内！");
        }
    }

    public void checkProject(FrozenBox frozenBox, FrozenTube frozenTube) {
        String sampleCode = frozenTube.getSampleCode() != null ? frozenTube.getSampleCode() : frozenTube.getSampleTempCode();
        if (!frozenBox.getProjectCode().equals(frozenTube.getProjectCode())) {
            throw new BankServiceException("项目不一致，编码为" + sampleCode
                + "的冻存管，不能移入" + frozenBox.getFrozenBoxCode() + "冻存盒内！");
        }
    }

    public void checkPositionInBox(FrozenBox frozenBox, String tubeRows, String tubeColumns,String sampleCode) {
        FrozenTube tube = frozenTubeRepository.findByFrozenBoxCodeAndTubeRowsAndTubeColumnsAndStatusNot(frozenBox.getFrozenBoxCode(),tubeRows,tubeColumns,Constants.INVALID);
        if(tube!=null){
            throw new BankServiceException("编码为"+frozenBox.getFrozenBoxCode()+"的冻存盒内"+tubeRows+tubeColumns+"位置已有样本，不能移入样本"+sampleCode);
        }
    }
}
