package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.PositionMoveService;
import org.fwoxford.service.dto.PositionMoveDTO;
import org.fwoxford.service.dto.PositionMoveRecordDTO;
import org.fwoxford.service.mapper.PositionMoveMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public void checkShelves(SupportRack supportRack, String columnsInShelf, String rowsInShelf) {
        FrozenBox frozenBox = frozenBoxRepository.findBySupportRackIdAndColumnsInShelfAndRowsInShelf(supportRack.getId(),columnsInShelf,rowsInShelf);
        if(frozenBox != null){
            throw new BankServiceException(supportRack.getSupportRackCode()+"内"+rowsInShelf+columnsInShelf+"位置已存在冻存盒，不能移入！");
        }
    }

    @Override
    public PositionMoveDTO createSamplePosition(PositionMoveDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveDTOToPositionMove(positionMoveDTO);
        checkUser(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_FOR_TUBE);
        positionMove.setStatus(Constants.VALID);
        positionMove.setPositionMoveDate(LocalDate.now());
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
        checkUser(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_FOR_BOX);
        positionMove.setStatus(Constants.VALID);
        positionMove.setPositionMoveDate(LocalDate.now());
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
        checkUser(positionMoveDTO);
        positionMove.setMoveType(Constants.MOVE_TYPE_FOR_SHELF);
        positionMove.setStatus(Constants.VALID);
        positionMove.setPositionMoveDate(LocalDate.now());
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
            if(p.getSupportRackOldId() == null){
                throw new BankServiceException("需要移动的冻存架ID不能为空！");
            }
            //原冻存架
            SupportRack supportRackOld = supportRackRepository.findOne(p.getSupportRackOldId());
            if(supportRackOld == null){
                throw new BankServiceException("原冻存架不存在！");
            }
            //新冻存架
            SupportRack supportRack = supportRackRepository.findOne(p.getSupportRackId());
            if(supportRack == null){
                throw new BankServiceException("新冻存架不存在！");
            }
            List<FrozenBox> frozenBoxList = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(supportRackOld.getArea().getEquipmentCode(),supportRackOld.getArea().getAreaCode(),supportRackOld.getSupportRackCode());
            List<FrozenBox> frozenBoxListNew = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(supportRack.getArea().getEquipmentCode(),supportRack.getArea().getAreaCode(),supportRack.getSupportRackCode());
            if(frozenBoxListNew.size()>=area.getFreezeFrameNumber()){
                throw new BankServiceException("该区域冻存架已满");
            }
            List<FrozenBox> frozenBoxes = new ArrayList<>();
            List<Long> frozenBoxIds = new ArrayList<Long>();

             for(FrozenBox frozenBox:frozenBoxList){
                 frozenBoxIds.add(frozenBox.getId());
                 frozenBox =  frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
                     .area(supportRack.getArea())
                     .areaCode(supportRack.getArea().getAreaCode())
                     .supportRack(supportRack)
                     .supportRackCode(supportRack.getSupportRackCode());
                 if(frozenBoxes.size() == 5000){
                     frozenBoxRepository.save(frozenBoxes);
                     frozenBoxes = new ArrayList<FrozenBox>();
                 }
             }
             if(frozenBoxes.size()>0){
                 frozenBoxRepository.save(frozenBoxes);
             }

            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxIdIn(frozenBoxIds);
            saveMoveDetail(positionMove,Constants.MOVE_TYPE_FOR_SHELF,frozenTubeList);
        }
        return positionMoveDTO;
    }

    public void checkUser(PositionMoveDTO positionMoveDTO) {
        if(StringUtils.isEmpty(positionMoveDTO.getMoveReason())){
            throw new BankServiceException("移位原因不能为空！");
        }
        if(positionMoveDTO.getOperatorId1() == null || positionMoveDTO.getOperatorId2()==null){
            throw new BankServiceException("操作人不能为空！");
        }
        if(positionMoveDTO.getOperatorId1().equals( positionMoveDTO.getOperatorId2())){
            throw new BankServiceException("操作人不能为同一个人！");
        }
        if(positionMoveDTO.getPassword1() == null || positionMoveDTO.getPassword2()==null){
            throw new BankServiceException("密码不能为空！");
        }
        Long operatorId1 = positionMoveDTO.getOperatorId1();
        String password1 = positionMoveDTO.getPassword1();
        User user = userRepository.findOne(operatorId1);
        if(user == null){
            throw new BankServiceException("操作人1不存在！");
        }
        if(!passwordEncoder.matches(password1,user.getPassword())){
            throw new BankServiceException("操作人1的用户名与密码不一致！");
        }

        Long operatorId2 = positionMoveDTO.getOperatorId2();
        String password2 = positionMoveDTO.getPassword2();
        User user2 = userRepository.findOne(operatorId2);
        if(user2 == null){
            throw new BankServiceException("操作人2不存在！");
        }
        if(!passwordEncoder.matches(password2,user2.getPassword())){
            throw new BankServiceException("操作人2的用户名与密码不一致！");
        }
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
        frozenBox =  frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
            .area(supportRack.getArea())
            .areaCode(supportRack.getArea().getAreaCode())
            .supportRack(supportRack)
            .supportRackCode(supportRack.getSupportRackCode())
            .columnsInShelf(p.getColumnsInShelf())
            .rowsInShelf(p.getRowsInShelf());
        frozenBoxRepository.save(frozenBox);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(p.getFrozenBoxId());
        saveMoveDetail(positionMove,Constants.MOVE_TYPE_FOR_BOX,frozenTubeList);
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
            frozenTube.setTubeColumns(p.getTubeColumns());
            frozenTube.setTubeRows(p.getTubeRows());
            frozenTube.setFrozenBox(frozenBox);
            frozenTube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            frozenTubeRepository.save(frozenTube);
            List<FrozenTube> frozenTubeListNew = new ArrayList<FrozenTube>(){{add(frozenTube);}};
            saveMoveDetail(positionMove,Constants.MOVE_TYPE_FOR_TUBE,frozenTubeListNew);
        }
    }

    public void saveMoveDetail(PositionMove positionMove, String moveType, List<FrozenTube> frozenTubeList) {
        List<PositionMoveRecord> positionMoveRecordList = new ArrayList<PositionMoveRecord>();
        for(FrozenTube frozenTube : frozenTubeList){
            PositionMoveRecord positionMoveRecord = new PositionMoveRecord()
                .sampleCode(StringUtils.isEmpty(frozenTube.getSampleCode())?frozenTube.getSampleTempCode():frozenTube.getSampleCode())
                .positionMove(positionMove)
                .frozenTube(frozenTube)
                .moveType(moveType)
                .equipment(frozenTube.getFrozenBox().getEquipment())
                .equipmentCode(frozenTube.getFrozenBox().getEquipmentCode())
                .area(frozenTube.getFrozenBox().getArea())
                .areaCode(frozenTube.getFrozenBox().getAreaCode())
                .supportRack(frozenTube.getFrozenBox().getSupportRack())
                .supportRackCode(frozenTube.getFrozenBox().getSupportRackCode())
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
                .tubeRows(frozenTube.getTubeRows())
                .frozenTubeType(frozenTube.getFrozenTubeType())
                .frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName())
                .sampleType(frozenTube.getSampleType())
                .sampleTypeCode(frozenTube.getSampleTypeCode())
                .sampleTypeName(frozenTube.getSampleTypeName())
                .sampleClassification(frozenTube.getSampleClassification())
                .sampleClassificationCode(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getSampleClassificationName():null)
                .frozenTubeCode(frozenTube.getFrozenTubeCode())
                .frozenTubeState(frozenTube.getFrozenTubeState())
                .sampleTempCode(frozenTube.getSampleTempCode())
                .sampleUsedTimes(frozenTube.getSampleUsedTimes())
                .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost())
                .frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit())
                .sampleVolumns(frozenTube.getSampleVolumns())
                .errorType(frozenTube.getErrorType());
            positionMoveRecordList.add(positionMoveRecord);
            if(positionMoveRecordList.size()==5000){
                positionMoveRecordRepository.save(positionMoveRecordList);
                positionMoveRecordList= new ArrayList<PositionMoveRecord>();
            }
        }
        if(positionMoveRecordList.size()>0){
            positionMoveRecordRepository.save(positionMoveRecordList);
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
