package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.PositionChangeService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.PositionChangeDTO;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.PositionChangeMapper;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PositionChange.
 */
@Service
@Transactional
public class PositionChangeServiceImpl implements PositionChangeService{

    private final Logger log = LoggerFactory.getLogger(PositionChangeServiceImpl.class);

    private final PositionChangeRepository positionChangeRepository;

    private final PositionChangeMapper positionChangeMapper;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private ProjectSampleClassRepository projectSampleClassRepository;

    @Autowired
    private PositionChangeRecordRepository positionChangeRecordRepository;

    @Autowired
    private SupportRackRepository supportRackRepository;

    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FrozenTubeMapper frozenTubeMapper;
    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    public PositionChangeServiceImpl(PositionChangeRepository positionChangeRepository, PositionChangeMapper positionChangeMapper) {
        this.positionChangeRepository = positionChangeRepository;
        this.positionChangeMapper = positionChangeMapper;
    }

    /**
     * Save a positionChange.
     *
     * @param positionChangeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionChangeDTO save(PositionChangeDTO positionChangeDTO) {
        log.debug("Request to save PositionChange : {}", positionChangeDTO);
        PositionChange positionChange = positionChangeMapper.positionChangeDTOToPositionChange(positionChangeDTO);
        positionChange = positionChangeRepository.save(positionChange);
        PositionChangeDTO result = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);
        return result;
    }

    /**
     *  Get all the positionChanges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionChangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionChanges");
        Page<PositionChange> result = positionChangeRepository.findAll(pageable);
        return result.map(positionChange -> positionChangeMapper.positionChangeToPositionChangeDTO(positionChange));
    }

    /**
     *  Get one positionChange by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionChangeDTO findOne(Long id) {
        log.debug("Request to get PositionChange : {}", id);
        PositionChange positionChange = positionChangeRepository.findOne(id);
        PositionChangeDTO positionChangeDTO = positionChangeMapper.positionChangeToPositionChangeDTO(positionChange);
        return positionChangeDTO;
    }

    /**
     *  Delete the  positionChange by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionChange : {}", id);
        positionChangeRepository.delete(id);
    }

    /**
     * 换位
     * @param positionChangeDTO
     * @param moveType 1：样本换位，2：冻存盒换位，3：冻存架换位。
     * @return
     */
    @Override
    public PositionChangeDTO createChangePosition(PositionChangeDTO positionChangeDTO, String moveType) {
        if(positionChangeDTO == null){
            return null;
        }
        if(positionChangeDTO.getChangeId1()==null || positionChangeDTO.getChangeId2() == null){
            throw new BankServiceException("需要换位的ID不能为空！");
        }
        if(positionChangeDTO.getChangeId1() == positionChangeDTO.getChangeId2()){
            throw new BankServiceException("两个相同的ID不能换位！");
        }
        checkUser(positionChangeDTO);
        PositionChange positionChange = positionChangeMapper.positionChangeDTOToPositionChange(positionChangeDTO);
        positionChange.setChangeType(moveType);
        positionChange.setStatus(Constants.VALID);
        positionChangeRepository.save(positionChange);
        positionChangeDTO.setId(positionChange.getId());
        switch (moveType){
            case "1":createChangePositionForSample(positionChangeDTO);break;
            case "2":createChangePositionForBox(positionChangeDTO);break;
            case "3":createChangePositionForShelf(positionChangeDTO);break;
            default:break;
        }
        return positionChangeDTO;
    }

    /**
     * 样本换位
     *  需满足：1.冻存管类型相同
     *          2.两支冻存管所属冻存盒样本类型相同（99除外）
     *          2.两支冻存管所属冻存盒类型相同
     * @param positionChangeDTO
     */
    public void createChangePositionForSample(PositionChangeDTO positionChangeDTO) {
        //获取第一支样本
        Long sampleId1 = positionChangeDTO.getChangeId1();
        FrozenTube frozenTube1 = frozenTubeRepository.findOne(sampleId1);
        FrozenTubeDTO frozenTubeDTO1 = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube1);
        //获取第二支样本
        Long sampleId2 = positionChangeDTO.getChangeId2();
        FrozenTube frozenTube2 = frozenTubeRepository.findOne(sampleId2);
        FrozenTubeDTO frozenTubeDTO2 = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube2);
        if(frozenTube1 == null || (frozenTube1!=null && !frozenTube1.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKED))
            ||frozenTube2 == null || (frozenTube2!=null && !frozenTube2.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKED))){
            throw new BankServiceException("冻存管未入库，不能换位！");
        }
        FrozenBox frozenBox1 = frozenTube1.getFrozenBox();
        FrozenBox frozenBox2 = frozenTube2.getFrozenBox();
        if(!frozenTube1.getProjectCode().equals(frozenTube2.getProjectCode())){
            throw new BankServiceException("两支样本所属项目不同，不能执行换位！");
        }
        if(!frozenTube1.getFrozenBox().getFrozenBoxTypeCode().equals(frozenTube2.getFrozenBox().getFrozenBoxTypeCode())){
            throw new BankServiceException("两支样本所属冻存盒类型不同，不能执行换位！");
        }
        if(frozenTube1.getSampleType().getIsMixed().equals(Constants.NO)&&frozenTube2.getSampleType().getIsMixed().equals(Constants.NO)){
            if(!frozenTube1.getSampleTypeCode().equals(frozenTube2.getSampleTypeCode())){
                throw new BankServiceException("两支样本类型不同，不能执行换位！");
            }
            if(frozenTube1.getSampleClassification()!=null&&frozenTube2.getSampleClassification()!=null&&frozenTube1.getSampleClassification().getId()!=frozenTube2.getSampleClassification().getId()){
                throw new BankServiceException("两支样本分类不同，不能执行换位！");
            }
        }
        if(frozenTube1.getSampleType().getIsMixed().equals(Constants.YES)||frozenTube2.getSampleType().getIsMixed().equals(Constants.YES)){
            if(frozenTube1.getSampleType().getIsMixed().equals(Constants.NO)&&!frozenTube2.getSampleTypeCode().equals(frozenTube1.getSampleTypeCode())){
                throw new BankServiceException("两支样本类型不同，不能执行换位！");
            }
            if(frozenTube2.getSampleType().getIsMixed().equals(Constants.NO)&&!frozenTube2.getSampleTypeCode().equals(frozenTube1.getSampleTypeCode())){
                throw new BankServiceException("两支样本类型不同，不能执行换位！");
            }
        }

        frozenTube1.setFrozenBox(frozenBox2);
        frozenTube1.setFrozenBoxCode(frozenTubeDTO2.getFrozenBoxCode());
        frozenTube1.setTubeColumns(frozenTubeDTO2.getTubeColumns());
        frozenTube1.setTubeRows(frozenTubeDTO2.getTubeRows());
        frozenTubeRepository.save(frozenTube1);

        frozenTube2.setFrozenBox(frozenBox1);
        frozenTube2.setFrozenBoxCode(frozenTubeDTO1.getFrozenBoxCode());
        frozenTube2.setTubeColumns(frozenTubeDTO1.getTubeColumns());
        frozenTube2.setTubeRows(frozenTubeDTO1.getTubeRows());
        frozenTubeRepository.save(frozenTube2);
        List<FrozenTube> frozenTubeList = new ArrayList<FrozenTube>(){{add(frozenTube1);add(frozenTube2);}};
        saveChangeDetail(positionChangeDTO,Constants.MOVE_TYPE_1,frozenTubeList);
    }

    public void saveChangeDetail(PositionChangeDTO positionChangeDTO, String changeType, List<FrozenTube> frozenTubeList) {
        PositionChange positionChange = positionChangeMapper.positionChangeDTOToPositionChange(positionChangeDTO);
        List<PositionChangeRecord> positionChangeRecordList = new ArrayList<PositionChangeRecord>();
        for(FrozenTube frozenTube : frozenTubeList){
            PositionChangeRecord positionChangeRecord = new PositionChangeRecord()
                .sampleCode(StringUtils.isEmpty(frozenTube.getSampleCode())?frozenTube.getSampleTempCode():frozenTube.getSampleCode())
                .positionChange(positionChange)
                .frozenTube(frozenTube)
                .changeType(changeType)
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
                .whetherFreezingAndThawing(positionChange.isWhetherFreezingAndThawing())
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
            positionChangeRecordList.add(positionChangeRecord);
            if(positionChangeRecordList.size()>=5000){
                positionChangeRecordRepository.save(positionChangeRecordList);
                positionChangeRecordList = new ArrayList<PositionChangeRecord>();
            }
        }
        if(positionChangeRecordList.size()>0){
            positionChangeRecordRepository.save(positionChangeRecordList);
            positionChangeRecordList = new ArrayList<PositionChangeRecord>();
        }
    }

    public void createChangePositionForBox(PositionChangeDTO positionChangeDTO) {
        //获取第一个冻存盒
        Long boxId1 = positionChangeDTO.getChangeId1();
        FrozenBox frozenBox1 = frozenBoxRepository.findOne(boxId1);
        FrozenBoxDTO frozenBoxDTO1 = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox1);
        //获取第二个冻存盒
        Long boxId2 = positionChangeDTO.getChangeId2();
        FrozenBox frozenBox2 = frozenBoxRepository.findOne(boxId2);
        FrozenBox frozenBoxOld2 = frozenBox2;
        if(frozenBox1 == null || (frozenBox1!=null && !frozenBox1.getStatus().equals(Constants.FROZEN_BOX_STOCKED))
            ||frozenBox2 == null || (frozenBox2!=null && !frozenBox2.getStatus().equals(Constants.FROZEN_BOX_STOCKED))){
            throw new BankServiceException("冻存盒未入库，不能换位！");
        }

        frozenBox1.equipment(frozenBoxOld2.getEquipment()).equipmentCode(frozenBoxOld2.getEquipmentCode())
            .area(frozenBoxOld2.getArea()).areaCode(frozenBoxOld2.getAreaCode())
            .supportRack(frozenBoxOld2.getSupportRack()).supportRackCode(frozenBoxOld2.getSupportRackCode())
            .rowsInShelf(frozenBoxOld2.getRowsInShelf()).columnsInShelf(frozenBoxOld2.getColumnsInShelf());
        frozenBoxRepository.save(frozenBox1);

        FrozenBox frozenBoxOld1 = frozenBoxMapper.frozenBoxDTOToFrozenBox(frozenBoxDTO1);
        frozenBox2.equipment(frozenBoxOld1.getEquipment()).equipmentCode(frozenBoxOld1.getEquipmentCode())
            .area(frozenBoxOld1.getArea()).areaCode(frozenBoxOld1.getAreaCode())
            .supportRack(frozenBoxOld1.getSupportRack()).supportRackCode(frozenBoxOld1.getSupportRackCode())
            .rowsInShelf(frozenBoxOld1.getRowsInShelf()).columnsInShelf(frozenBoxOld1.getColumnsInShelf());
        frozenBoxRepository.save(frozenBox2);

        List<Long> frozenBoxIds = new ArrayList<Long>(){{add(boxId1);add(boxId2);}};
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxIdIn(frozenBoxIds);
        saveChangeDetail(positionChangeDTO,Constants.MOVE_TYPE_2,frozenTubeList);
    }
    public void createChangePositionForShelf(PositionChangeDTO positionChangeDTO) {
        //获取第一个冻存架
        Long shelvefId1 = positionChangeDTO.getChangeId1();
        SupportRack supportRack1 = supportRackRepository.findOne(shelvefId1);

        //获取第一个冻存架
        Long shelvefId2 = positionChangeDTO.getChangeId2();
        SupportRack supportRack2 = supportRackRepository.findOne(shelvefId2);
        if(supportRack1 == null || (supportRack1!=null && supportRack1.getStatus().equals(Constants.INVALID))
            ||supportRack2 == null || (supportRack2!=null && supportRack2.getStatus().equals(Constants.INVALID))){
            throw new BankServiceException("冻存架无效！");
        }
        //冻存架1里的冻存盒
        List<FrozenBox> frozenBoxList1 = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(supportRack1.getArea().getEquipmentCode(),supportRack1.getArea().getAreaCode(),supportRack1.getSupportRackCode());
        //冻存架2里的冻存盒
        List<FrozenBox> frozenBoxList2 = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(supportRack2.getArea().getEquipmentCode(),supportRack2.getArea().getAreaCode(),supportRack2.getSupportRackCode());
        List<FrozenBox> frozenBoxes = new ArrayList<>();
        List<Long> frozenBoxIds = new ArrayList<Long>();
        for(FrozenBox frozenBox1 : frozenBoxList1){
            frozenBoxIds.add(frozenBox1.getId());
            frozenBox1.equipment(supportRack2.getArea().getEquipment()).equipmentCode(supportRack2.getArea().getEquipment().getEquipmentCode())
                .area(supportRack2.getArea()).areaCode(supportRack2.getArea().getAreaCode())
                .supportRack(supportRack2).supportRackCode(supportRack2.getSupportRackCode());
            frozenBoxes.add(frozenBox1);
            if(frozenBoxes.size() == 5000){
                frozenBoxRepository.save(frozenBoxes);
                frozenBoxes = new ArrayList<FrozenBox>();
            }
        }
        for(FrozenBox frozenBox2 : frozenBoxList2){
            frozenBoxIds.add(frozenBox2.getId());
            frozenBox2.equipment(supportRack1.getArea().getEquipment()).equipmentCode(supportRack1.getArea().getEquipment().getEquipmentCode())
                .area(supportRack1.getArea()).areaCode(supportRack1.getArea().getAreaCode())
                .supportRack(supportRack1).supportRackCode(supportRack1.getSupportRackCode());
            frozenBoxes.add(frozenBox2);
            if(frozenBoxes.size() == 5000){
                frozenBoxRepository.save(frozenBoxes);
                frozenBoxes = new ArrayList<FrozenBox>();
            }
        }
        if(frozenBoxes.size()>0){
            frozenBoxRepository.save(frozenBoxes);
        }
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxIdIn(frozenBoxIds);
        saveChangeDetail(positionChangeDTO,Constants.MOVE_TYPE_3,frozenTubeList);
    }

    public void checkUser(PositionChangeDTO positionChangeDTO) {
        if(StringUtils.isEmpty(positionChangeDTO.getChangeReason())){
            throw new BankServiceException("换位原因不能为空！");
        }
        if(positionChangeDTO.getOperatorId1() == null || positionChangeDTO.getOperatorId2()==null){
            throw new BankServiceException("操作人不能为空！");
        }
        if(positionChangeDTO.getPassword1() == null || positionChangeDTO.getPassword2()==null){
            throw new BankServiceException("密码不能为空！");
        }
        Long operatorId1 = positionChangeDTO.getOperatorId1();
        String password1 = positionChangeDTO.getPassword1();
        User user = userRepository.findOne(operatorId1);
        if(user == null){
            throw new BankServiceException("操作人1不存在！");
        }
        if(!passwordEncoder.matches(password1,user.getPassword())){
            throw new BankServiceException("操作人1的用户名与密码不一致！");
        }

        Long operatorId2 = positionChangeDTO.getOperatorId2();
        String password2 = positionChangeDTO.getPassword2();
        User user2 = userRepository.findOne(operatorId2);
        if(user2 == null){
            throw new BankServiceException("操作人2不存在！");
        }
        if(!passwordEncoder.matches(password2,user2.getPassword())){
            throw new BankServiceException("操作人2的用户名与密码不一致！");
        }
    }
}
