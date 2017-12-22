package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.PositionDestroyService;
import org.fwoxford.service.dto.PositionDestroyDTO;
import org.fwoxford.service.mapper.PositionDestroyMapper;
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
 * Service Implementation for managing PositionDestroy.
 */
@Service
@Transactional
public class PositionDestroyServiceImpl implements PositionDestroyService{

    private final Logger log = LoggerFactory.getLogger(PositionDestroyServiceImpl.class);

    private final PositionDestroyRepository positionDestroyRepository;

    private final PositionDestroyMapper positionDestroyMapper;
    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private ProjectSampleClassRepository projectSampleClassRepository;

    @Autowired
    private PositionDestroyRecordRepository positionDestroyRecordRepository;

    @Autowired
    private SupportRackRepository supportRackRepository;

    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PositionDestroyServiceImpl(PositionDestroyRepository positionDestroyRepository, PositionDestroyMapper positionDestroyMapper) {
        this.positionDestroyRepository = positionDestroyRepository;
        this.positionDestroyMapper = positionDestroyMapper;
    }

    /**
     * Save a positionDestroy.
     *
     * @param positionDestroyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionDestroyDTO save(PositionDestroyDTO positionDestroyDTO) {
        log.debug("Request to save PositionDestroy : {}", positionDestroyDTO);
        PositionDestroy positionDestroy = positionDestroyMapper.positionDestroyDTOToPositionDestroy(positionDestroyDTO);
        positionDestroy = positionDestroyRepository.save(positionDestroy);
        PositionDestroyDTO result = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);
        return result;
    }

    /**
     *  Get all the positionDestroys.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionDestroyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionDestroys");
        Page<PositionDestroy> result = positionDestroyRepository.findAll(pageable);
        return result.map(positionDestroy -> positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy));
    }

    /**
     *  Get one positionDestroy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionDestroyDTO findOne(Long id) {
        log.debug("Request to get PositionDestroy : {}", id);
        PositionDestroy positionDestroy = positionDestroyRepository.findOne(id);
        PositionDestroyDTO positionDestroyDTO = positionDestroyMapper.positionDestroyToPositionDestroyDTO(positionDestroy);
        return positionDestroyDTO;
    }

    /**
     *  Delete the  positionDestroy by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionDestroy : {}", id);
        positionDestroyRepository.delete(id);
    }

    @Override
    public PositionDestroyDTO createDestroyPosition(PositionDestroyDTO positionDestroyDTO, String type) {
        if(positionDestroyDTO == null){
            return null;
        }
        if(positionDestroyDTO.getIds()==null ||positionDestroyDTO.getIds().length==0 ){
            throw new BankServiceException("需要销毁的ID不能为空！");
        }
        checkUser(positionDestroyDTO);
        PositionDestroy positionDestroy = positionDestroyMapper.positionDestroyDTOToPositionDestroy(positionDestroyDTO);
        positionDestroy.setDestroyType(type);
        positionDestroy.setStatus(Constants.VALID);
        positionDestroy.setPositionDestroyDate(LocalDate.now());
        positionDestroyRepository.save(positionDestroy);
        positionDestroyDTO.setId(positionDestroy.getId());
        switch (type){
            case "1":createDestroyPositionForSample(positionDestroyDTO);break;
            case "2":createDestroyPositionForBox(positionDestroyDTO);break;
            default:break;
        }
        return positionDestroyDTO;
    }

    private void createDestroyPositionForSample(PositionDestroyDTO positionDestroyDTO) {
        Long[] ids =positionDestroyDTO.getIds();
        List<Long> frozenTubeIds = new ArrayList<Long>();
        for(Long id :ids){
            frozenTubeIds.add(id);
        }
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findAll(frozenTubeIds);
        for(FrozenTube f:frozenTubeList){
            if(!f.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKED)){
                throw new BankServiceException("冻存管未入库，不能销毁！");
            }
            f.setStatus(Constants.FROZEN_TUBE_DESTROY);
            f.setFrozenTubeState(Constants.FROZEN_BOX_DESTROY);
        }
        frozenTubeRepository.save(frozenTubeList);
        saveDestroyDetail(positionDestroyDTO,Constants.MOVE_TYPE_FOR_TUBE,frozenTubeList);
    }
    private void createDestroyPositionForBox(PositionDestroyDTO positionDestroyDTO) {
        Long[] ids =positionDestroyDTO.getIds();
        List<Long> boxIds = new ArrayList<Long>();
        for(Long id :ids){
            boxIds.add(id);
        }
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll(boxIds);
        for(FrozenBox frozenBox:frozenBoxList){
            if(!frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED)){
                throw new BankServiceException("冻存盒未入库，不能销毁！");
            }
            frozenBox.setStatus(Constants.FROZEN_BOX_DESTROY);
        }
        frozenBoxRepository.save(frozenBoxList);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxIdIn(boxIds);
        for(FrozenTube f:frozenTubeList){
            f.setStatus(Constants.FROZEN_TUBE_DESTROY);
            f.setFrozenTubeState(Constants.FROZEN_BOX_DESTROY);
        }
        frozenTubeRepository.save(frozenTubeList);
        saveDestroyDetail(positionDestroyDTO,Constants.MOVE_TYPE_FOR_BOX,frozenTubeList);
    }

    public void saveDestroyDetail(PositionDestroyDTO positionDestroyDTO, String type, List<FrozenTube> frozenTubeList) {
        PositionDestroy positionDestroy = positionDestroyMapper.positionDestroyDTOToPositionDestroy(positionDestroyDTO);
        List<PositionDestroyRecord> positionDestroyRecordList = new ArrayList<PositionDestroyRecord>();
        for(FrozenTube frozenTube : frozenTubeList){
            PositionDestroyRecord positionDestroyRecord = new PositionDestroyRecord()
                .sampleCode(StringUtils.isEmpty(frozenTube.getSampleCode())?frozenTube.getSampleTempCode():frozenTube.getSampleCode())
                .positionDestroy(positionDestroy)
                .frozenTube(frozenTube)
                .destroyType(type)
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
            positionDestroyRecordList.add(positionDestroyRecord);
            if(positionDestroyRecordList.size()>=5000){
                positionDestroyRecordRepository.save(positionDestroyRecordList);
                positionDestroyRecordList = new ArrayList<PositionDestroyRecord>();
            }
        }
        if(positionDestroyRecordList.size()>0){
            positionDestroyRecordRepository.save(positionDestroyRecordList);
        }
    }

    public void checkUser(PositionDestroyDTO positionDestroyDTO) {
        if(StringUtils.isEmpty(positionDestroyDTO.getDestroyReason())){
            throw new BankServiceException("销毁原因不能为空！");
        }
        if(positionDestroyDTO.getOperatorId1() == null || positionDestroyDTO.getOperatorId2()==null){
            throw new BankServiceException("操作人不能为空！");
        }
        if(positionDestroyDTO.getOperatorId1() ==  positionDestroyDTO.getOperatorId2()){
            throw new BankServiceException("操作人不能为同一个人！");
        }
        if(positionDestroyDTO.getPassword1() == null || positionDestroyDTO.getPassword2()==null){
            throw new BankServiceException("密码不能为空！");
        }
        Long operatorId1 = positionDestroyDTO.getOperatorId1();
        String password1 = positionDestroyDTO.getPassword1();
        User user = userRepository.findOne(operatorId1);
        if(user == null){
            throw new BankServiceException("操作人1不存在！");
        }
        if(!passwordEncoder.matches(password1,user.getPassword())){
            throw new BankServiceException("操作人1的用户名与密码不一致！");
        }

        Long operatorId2 = positionDestroyDTO.getOperatorId2();
        String password2 = positionDestroyDTO.getPassword2();
        User user2 = userRepository.findOne(operatorId2);
        if(user2 == null){
            throw new BankServiceException("操作人2不存在！");
        }
        if(!passwordEncoder.matches(password2,user2.getPassword())){
            throw new BankServiceException("操作人2的用户名与密码不一致！");
        }
    }
}
