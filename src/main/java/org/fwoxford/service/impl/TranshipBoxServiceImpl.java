package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.TranshipBoxMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Service Implementation for managing TranshipBox.
 */
@Service
@Transactional
public class TranshipBoxServiceImpl implements TranshipBoxService{

    private final Logger log = LoggerFactory.getLogger(TranshipBoxServiceImpl.class);
    @Autowired
    private TranshipBoxRepository transhipBoxRepository;
    @Autowired
    private FrozenBoxRepository frozenBoxRepository;
    @Autowired
    private FrozenTubeRepository frozenTubeRepository;
    @Autowired
    private SampleTypeRepository sampleTypeRepository;
    @Autowired
    private FrozenBoxTypeRepository frozenBoxTypeRepository;
    @Autowired
    private TranshipRepository transhipRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private SupportRackRepository supportRackRepository;
    @Autowired
    private FrozenTubeTypeRepository frozenTubeTypeRepository;
    @Autowired
    private TranshipBoxMapper transhipBoxMapper;
    @Autowired
    private FrozenBoxMapper frozenBoxMapper;
    @Autowired
    private FrozenTubeMapper frozenTubeMapper;
    @Autowired
    private ProjectSampleClassRepository projectSampleClassRepository;
    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;
    @Autowired
    private TranshipBoxPositionService transhipBoxPositionService;
    @Autowired
    private TranshipTubeService transhipTubeService;
    @Autowired
    private TranshipTubeRepository transhipTubeRepository;
    @Autowired
    private TranshipBoxPositionRepository transhipBoxPositionRepository;
    @Autowired
    private FrozenBoxCheckService frozenBoxCheckService;
    @Autowired
    private TranshipBoxRepositories transhipBoxRepositories;
    @Autowired
    private FrozenTubeCheckService frozenTubeCheckService;
    /**
     * Save a transhipBox.
     *
     * @param transhipBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranshipBoxDTO save(TranshipBoxDTO transhipBoxDTO) {
        log.debug("Request to save TranshipBox : {}", transhipBoxDTO);

        TranshipBox transhipBox = transhipBoxMapper.transhipBoxDTOToTranshipBox(transhipBoxDTO);
        TranshipBox newTranshipBox = transhipBoxMapper.initTranshipToTranship(transhipBox);
        transhipBox = transhipBoxRepository.save(newTranshipBox);
        TranshipBoxDTO result = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);
        return result;
    }

    /**
     *  Get all the transhipBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranshipBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TranshipBoxes");
        Page<TranshipBox> result = transhipBoxRepository.findAll(pageable);
        return result.map(transhipBox -> transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox));
    }

    /**
     *  Get one transhipBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranshipBoxDTO findOne(Long id) {
        log.debug("Request to get TranshipBox : {}", id);
        TranshipBox transhipBox = transhipBoxRepository.findOne(id);
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);
        return transhipBoxDTO;
    }

    /**
     *  Delete the  transhipBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TranshipBox : {}", id);
        transhipBoxRepository.delete(id);
    }

    /**
     * 批量保存转运与冻存盒的关系
     * @param transhipBoxDTOList
     * @return
     */
    @Override
    public List<TranshipBoxDTO> saveBatch(List<TranshipBoxDTO> transhipBoxDTOList) {
        List<TranshipBox> transhipBoxes = transhipBoxMapper.transhipBoxDTOsToTranshipBoxes(transhipBoxDTOList);
        List<TranshipBox> transhipBoxList =  transhipBoxRepository.save(transhipBoxes);
        return transhipBoxMapper.transhipBoxesToTranshipBoxDTOs(transhipBoxList);
    }

    /**
     * 根据转运ID和冻存盒ID查询转运与冻存盒的关系
     * @param transhipId 转运ID
     * @param frozenBoxId 冻存盒ID
     * @return
     */
    @Override
    public TranshipBoxDTO findByTranshipIdAndFrozenBoxId(Long transhipId, Long frozenBoxId) {
        TranshipBox transhipBox = transhipBoxRepository.findByTranshipIdAndFrozenBoxId(transhipId,frozenBoxId);
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);
        return transhipBoxDTO;
    }

    /**
     * 批量保存转运的冻存盒
     *
     * @param transhipBoxListDTO
     * @return
     */
    @Override
    public synchronized TranshipBoxListForSaveBatchDTO saveBatchTranshipBox(TranshipBoxListDTO transhipBoxListDTO) {
        TranshipBoxListForSaveBatchDTO result = new TranshipBoxListForSaveBatchDTO();
        Long transhipId = transhipBoxListDTO.getTranshipId();
        Tranship tranship = transhipRepository.findOne(transhipId);
        if (tranship == null){
            // 转运ID无效的情况
            throw new BankServiceException("转运记录不存在！",transhipBoxListDTO.toString());
        }
        List<SampleType> sampleTypes = sampleTypeRepository.findAllSampleTypes();
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        List<FrozenBoxType> boxTypes = frozenBoxTypeRepository.findAllFrozenBoxTypes();
        List<FrozenTubeType> tubeTypes = frozenTubeTypeRepository.findAll();
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findSampleTypeByProjectCode(tranship.getProjectCode());
        result.setTranshipId(transhipId);
        result.setFrozenBoxDTOList(new ArrayList<>());
        //验证冻存管编码是否重复
        Map<String,Long> frozenBoxCodeMap = new HashMap<String,Long>();
        ArrayList<String> repeatCode = new ArrayList<>();
        for(FrozenBoxForSaveBatchDTO boxDTO : transhipBoxListDTO.getFrozenBoxDTOList()) {
            if(frozenBoxCodeMap.get(boxDTO.getFrozenBoxCode())!=null){
                repeatCode.add(boxDTO.getFrozenBoxCode());
            }else{
                Long id = boxDTO.getId()==null?-1:boxDTO.getId();
                frozenBoxCodeMap.put(boxDTO.getFrozenBoxCode(),id);
            }
        }
        if(repeatCode.size()>0){
            throw new BankServiceException("请勿提交重复的冻存盒编码！",String.join(",",repeatCode));
        }
        frozenBoxCheckService.checkFrozenBoxCodeRepead(frozenBoxCodeMap);
        for(FrozenBoxForSaveBatchDTO boxDTO : transhipBoxListDTO.getFrozenBoxDTOList()){
            FrozenBox box = frozenBoxMapper.frozenBoxForSaveBatchDTOToFrozenBox(boxDTO);
            box = createFrozenBox(tranship,box);
            box = createFrozenBoxTypeAndSampleType(box,boxTypes,sampleTypes,sampleClassifications,projectSampleClasses);
            int countOfEmptyHole = 0;int countOfEmptyTube = 0;int countOfSample=0;
            for(FrozenTubeForSaveBatchDTO tube : boxDTO.getFrozenTubeDTOS()){
                if(tube.getStatus().equals(Constants.FROZEN_TUBE_HOLE_EMPTY)){
                    countOfEmptyHole++;
                }
                if(tube.getStatus().equals(Constants.FROZEN_TUBE_EMPTY)){
                    countOfEmptyTube++;
                }
                if(tube.getStatus().equals(Constants.FROZEN_TUBE_NORMAL)){
                    countOfSample++;
                }
            }

            box.setEmptyTubeNumber(countOfEmptyTube);
            box.setEmptyHoleNumber(countOfEmptyHole);
            int countOfSampleAll = boxDTO.getFrozenTubeDTOS().size();
            String frozenBoxColumns = box.getFrozenBoxTypeColumns()!=null?box.getFrozenBoxTypeColumns():new String("0");
            String frozenBoxRows = box.getFrozenBoxTypeRows()!=null?box.getFrozenBoxTypeRows():new String("0");

            int allCount = Integer.parseInt(frozenBoxColumns)*Integer.parseInt(frozenBoxRows);
            if(countOfSampleAll>allCount){
                throw new BankServiceException("冻存管的数量已经超过冻存盒的最大容量值！",box.toString());
            }
            frozenBoxCheckService.checkFrozenBoxPosition(box);
            box.setStatus(Constants.FROZEN_BOX_NEW);
            box = frozenBoxRepository.save(box);
            TranshipBox transhipBox = transhipBoxRepository.findByTranshipIdAndFrozenBoxId(transhipId, box.getId());
            if (transhipBox == null){
                transhipBox = new TranshipBox();
            }
            transhipBox.setEquipmentCode(box.getEquipmentCode());
            transhipBox.setAreaCode(box.getAreaCode());
            transhipBox.setSupportRackCode(box.getSupportRackCode());
            transhipBox.setColumnsInShelf(box.getColumnsInShelf());
            transhipBox.setRowsInShelf(box.getRowsInShelf());
            transhipBox.setStatus(box.getStatus());
            transhipBox.setMemo(box.getMemo());
            transhipBox.setFrozenBoxCode(box.getFrozenBoxCode());
            transhipBox.setFrozenBox(box);
            transhipBox.setTranship(tranship);
            transhipBox.setCountOfSample(countOfSample);
            transhipBox.setEquipment( box.getEquipment() );
            transhipBox.setArea( box.getArea() );
            transhipBox.setSupportRack( box.getSupportRack() );
            transhipBox.sampleTypeCode(box.getSampleTypeCode()).sampleType(box.getSampleType()).sampleTypeName(box.getSampleTypeName())
                .sampleClassification(box.getSampleClassification())
                .sampleClassificationCode(box.getSampleClassification()!=null?box.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(box.getSampleClassification()!=null?box.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(box.getDislocationNumber()).emptyHoleNumber(box.getEmptyHoleNumber()).emptyTubeNumber(box.getEmptyTubeNumber())
                .frozenBoxType(box.getFrozenBoxType()).frozenBoxTypeCode(box.getFrozenBoxTypeCode()).frozenBoxTypeColumns(box.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(box.getFrozenBoxTypeRows()).isRealData(box.getIsRealData()).isSplit(box.getIsSplit()).project(box.getProject())
                .projectCode(box.getProjectCode()).projectName(box.getProjectName()).projectSite(box.getProjectSite()).projectSiteCode(box.getProjectSiteCode())
                .projectSiteName(box.getProjectSiteName());
            transhipBoxRepository.save(transhipBox);
            //转运盒位置
            TranshipBoxPosition transhipBoxPosition = transhipBoxPositionService.saveTranshipBoxPosition(transhipBox);
           //获取原冻存管，与当前冻存管比对，删除原来有而当前没有的冻存管
           List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxId(box.getId());
           List<Long> forSaveTubeIds = new ArrayList<Long>();
           List<FrozenTube> frozenTubeListForDelete = new ArrayList<FrozenTube>();
           for(FrozenTubeForSaveBatchDTO tubeDTO : boxDTO.getFrozenTubeDTOS()){
               forSaveTubeIds.add(tubeDTO.getId());
           }
           for(FrozenTube f:frozenTubes){
                if(!forSaveTubeIds.contains(f.getId())){
                    f.setStatus(Constants.INVALID);
                    frozenTubeListForDelete.add(f);
                }
            }
           frozenTubeRepository.save(frozenTubeListForDelete);
           List<FrozenTube> frozenTubeList = new ArrayList<FrozenTube>();
           Map<List<String>,FrozenTube> frozenTubeMap = new HashMap<List<String>,FrozenTube>();
           List<String> sampleCodeStr = new ArrayList<String>();
           for(FrozenTubeForSaveBatchDTO tubeDTO : boxDTO.getFrozenTubeDTOS()){
                FrozenTube tube = frozenTubeMapper.frozenTubeForSaveBatchDTOToFrozenTube(tubeDTO);
                tube = createFrozenTubeSampleType(tube,box,sampleTypes,sampleClassifications,projectSampleClasses,tubeTypes);
                if((StringUtils.isEmpty(tube.getSampleTempCode()))
                        &&(StringUtils.isEmpty(tube.getSampleCode()))){
                    throw new BankServiceException("冻存管编码为空，不能保存！");
                }
                frozenTubeList.add(tube);
                String sampleCode = tube.getSampleCode()!=null?tube.getSampleCode():tube.getSampleTempCode();
                sampleCodeStr.add(sampleCode);
               List<String> stringList = new ArrayList<>();
               String sampleClassificationCode = "-1";
               if(tube.getSampleClassification()!=null){
                   sampleClassificationCode = tube.getSampleClassification().getSampleClassificationCode();
               }
               stringList.add(0,sampleCode);stringList.add(1,sampleClassificationCode);
               frozenTubeMap.put(stringList,tube);
            }
            frozenTubeCheckService.checkSampleCodeRepeat(sampleCodeStr,frozenTubeMap,box);
            frozenTubeRepository.save(frozenTubeList);
            //转运冻存管
            List<TranshipTube> transhipTubeDTOS = transhipTubeService.saveTranshipTube(transhipBox,frozenTubeList);
            result.getFrozenBoxDTOList().add(frozenBoxMapper.frozenBoxToFrozenBoxDTO(box));
            transhipTubeRepository.save(transhipTubeDTOS);
//            transhipTubeList.addAll(transhipTubeDTOS);
        }
        this.updateTranshipSampleNumber(tranship);

        return result;
    }

    public FrozenTube createFrozenTubeSampleType(FrozenTube tube, FrozenBox box, List<SampleType> sampleTypes, List<SampleClassification> sampleClassifications, List<ProjectSampleClass> projectSampleClasses, List<FrozenTubeType> tubeTypes) {
        if (tube.getSampleType() == null){
            tube.setSampleType(box.getSampleType());
        } else {
            int sampleTypeIndex = sampleTypes.indexOf(box.getSampleType());
            if (sampleTypeIndex >= 0) {
                SampleType sampleType = sampleTypes.get(sampleTypeIndex);
                tube.setSampleType(sampleType);
            } else {
                tube.getSampleType().setSampleTypeCode(tube.getSampleTypeCode());
                tube.getSampleType().setSampleTypeName(tube.getSampleTypeName());
            }
        }
        if(tube.getSampleType().getIsMixed().equals(Constants.YES) &&tube.getSampleClassification() == null){
            throw new BankServiceException("混合类型的样本分类不能为空！");
        }
        if (tube.getSampleClassification() == null){
            tube.setSampleClassification(box.getSampleClassification());
        } else {
            int sampleClassificationsIndex = sampleClassifications.indexOf(tube.getSampleClassification());
            if (sampleClassificationsIndex >= 0) {
                SampleClassification sampleClass = sampleClassifications.get(sampleClassificationsIndex);
                tube.setSampleClassification(sampleClass);
            } else {
                throw new BankServiceException("样本分类不能为空！");
            }
        }
        if(tube.getSampleClassification()!=null){
            //验证项目下样本类型以及样本分类的有效性
            ProjectSampleClass sampleType = null;
            for(ProjectSampleClass p :projectSampleClasses){
                if(tube.getSampleClassification().getId().equals(p.getSampleClassification().getId())
                    &&tube.getSampleType().getId().equals(p.getSampleType().getId())){
                    sampleType = p;
                }
            }
            if(sampleType == null){
                throw new BankServiceException("样本类型无效！",box.toString());
            }
        }

        tube.setSampleTypeCode(tube.getSampleType().getSampleTypeCode());
        tube.setSampleTypeName(tube.getSampleType().getSampleTypeName());

        tube.setFrozenBox(box);
        tube.setFrozenBoxCode(box.getFrozenBoxCode());

        tube.setProject(box.getProject());
        tube.setProjectCode(box.getProjectCode());
        tube.setProjectSite(box.getProjectSite());
        tube.setProjectSiteCode(box.getProjectSiteCode());
        if (tube.getFrozenTubeType() != null){
            int tubeTypeIndex = tubeTypes.indexOf(tube.getFrozenTubeType());
            if (tubeTypeIndex >= 0){
                FrozenTubeType tubeType = tubeTypes.get(tubeTypeIndex);
                tube.setFrozenTubeType(tubeType);
                tube.setFrozenTubeTypeCode(tubeType.getFrozenTubeTypeCode());
                tube.setFrozenTubeTypeName(tubeType.getFrozenTubeTypeName());
                tube.setFrozenTubeVolumnsUnit(tubeType.getFrozenTubeVolumnUnit());
                tube.setFrozenTubeVolumns(tubeType.getFrozenTubeVolumn());
                tube.setSampleUsedTimesMost(tubeType.getSampleUsedTimesMost());
            }
        } else {
            FrozenTubeType tubeType = tubeTypes.get(0);
            tube.setFrozenTubeType(tubeType);
            tube.setFrozenTubeTypeCode(tubeType.getFrozenTubeTypeCode());
            tube.setFrozenTubeTypeName(tubeType.getFrozenTubeTypeName());
            tube.setFrozenTubeVolumnsUnit(tubeType.getFrozenTubeVolumnUnit());
            tube.setFrozenTubeVolumns(tubeType.getFrozenTubeVolumn());
            tube.setSampleUsedTimesMost(tubeType.getSampleUsedTimesMost());
        }
        tube.setSampleUsedTimes(0);
        tube.setFrozenTubeState(Constants.FROZEN_BOX_NEW);
        return tube;
    }

    public FrozenBox createFrozenBoxTypeAndSampleType(FrozenBox box, List<FrozenBoxType> boxTypes, List<SampleType> sampleTypes, List<SampleClassification> sampleClassifications, List<ProjectSampleClass> projectSampleClasses) {
        if (box.getFrozenBoxType() != null) {
            int boxTypeIndex = boxTypes.indexOf(box.getFrozenBoxType());
            if (boxTypeIndex >= 0) {
                FrozenBoxType boxType = boxTypes.get(boxTypeIndex);
                box.setFrozenBoxType(boxType);
                box.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
                box.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
                box.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
            }else {
                throw new BankServiceException("冻存盒类型不能为空！",box.toString());
            }
        }

        if (box.getSampleType() != null) {
            int boxSampleTypeIndex = sampleTypes.indexOf(box.getSampleType());
            if (boxSampleTypeIndex >= 0) {
                SampleType sampleType = sampleTypes.get(boxSampleTypeIndex);
                box.setSampleType(sampleType);
                box.setSampleTypeCode(sampleType.getSampleTypeCode());
                box.setSampleTypeName(sampleType.getSampleTypeName());
            }else{
                throw new BankServiceException("样本类型不能为空！",box.toString());
            }
        }
        //验证项目下该样本类型是否有样本分类，如果已经配置了样本分类，则样本分类ID不能为空，（99的除外）
        if(box.getSampleType().getIsMixed().equals(Constants.YES)&&box.getSampleClassification()!=null){
            throw new BankServiceException("混合型样本的冻存盒，样本分类应该为空！");
        }

        if(box.getSampleType().getIsMixed().equals(Constants.YES)
            &&(box.getIsSplit()==null||box.getIsSplit()!=null&&box.getIsSplit().equals(Constants.NO))){
            throw new BankServiceException("混合型样本的冻存盒，必须分装！");
        }

        int countOfSampleClass = projectSampleClassRepository.countByProjectIdAndSampleTypeId(box.getProject()!=null?box.getProject().getId():null,box.getSampleType().getId());
        if(countOfSampleClass>0&&box.getSampleType().getIsMixed().equals(Constants.NO)&&box.getSampleClassification()==null){
            throw new BankServiceException("该项目下已经配置样本分类，样本分类不能为空！");
        }

        if (box.getSampleClassification() != null) {
            int boxClassificationIndex = sampleClassifications.indexOf(box.getSampleClassification());
            if (boxClassificationIndex >= 0) {
                SampleClassification sampleClass = sampleClassifications.get(boxClassificationIndex);
                box.setSampleClassification(sampleClass);
            }
        }
        if(box.getSampleClassification()!=null){
            //验证项目下样本类型以及样本分类的有效性
            ProjectSampleClass sampleType = null;
            for(ProjectSampleClass p :projectSampleClasses){
                if(box.getSampleClassification().getId().equals(p.getSampleClassification().getId())
                    &&box.getSampleType().getId().equals(p.getSampleType().getId())){
                    sampleType = p;
                }
            }
            if(sampleType == null){
                throw new BankServiceException("冻存盒的样本类型无效！",box.toString());
            }
        }
        return box;
    }

    public FrozenBox createFrozenBox(Tranship tranship, FrozenBox box) {
        if (tranship.getProject() != null ) {
            Project project = tranship.getProject();
            box.setProject(project);
            box.setProjectCode(project.getProjectCode());
            box.setProjectName(project.getProjectName());
        }

        if (box.getEquipment() != null) {
            Equipment equipment = equipmentRepository.findOne(box.getEquipment().getId());
            box.setEquipment(equipment);
            box.setEquipmentCode(equipment.getEquipmentCode());
        } else {
            box.setEquipment(null);
            box.setEquipmentCode(null);
        }

        if (box.getArea() != null) {
            Area area = areaRepository.findOne(box.getArea().getId());
            box.setArea(area);
            box.setAreaCode(area.getAreaCode());
        } else {
            box.setArea(null);
            box.setAreaCode(null);
        }

        if (box.getSupportRack() != null) {
            SupportRack shelf = supportRackRepository.findOne(box.getSupportRack().getId());
            box.setSupportRack(shelf);
            box.setSupportRackCode(shelf.getSupportRackCode());
        } else {
            box.setSupportRack(null);
            box.setSupportRackCode(null);
        }

        if (tranship.getProjectSite() != null) {
            ProjectSite projectSite = tranship.getProjectSite();
            box.setProjectSite(projectSite);
            box.setProjectSiteCode(projectSite.getProjectSiteCode());
            box.setProjectSiteName(projectSite.getProjectSiteName());
        }
        return box;
    }

    public void updateTranshipSampleNumber(Tranship tranship) {
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAllFrozenBoxByTranshipId(tranship.getId());
        int countOfEmptyHole = 0;int countOfEmptyTube = 0;int countOfTube = 0;int countOfSample =0;
        List<Long> boxIds = new ArrayList<Long>();
        for(FrozenBox box : frozenBoxList){
            frozenBoxRepository.save(box);
            boxIds.add(box.getId());
        }
        if(boxIds.size()>0){
            countOfEmptyHole = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_HOLE_EMPTY);
            countOfEmptyTube = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_EMPTY);
            countOfTube = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_NORMAL);
            countOfSample = frozenTubeRepository.countByFrozenBoxCodeStrAndGroupBySampleCode(boxIds);
        }
        tranship.setSampleNumber(countOfSample);
        tranship.setFrozenBoxNumber(frozenBoxList.size());
        tranship.setEmptyHoleNumber(countOfEmptyHole);
        tranship.setEmptyTubeNumber(countOfEmptyTube);
        tranship.setEffectiveSampleNumber(countOfTube);
        transhipRepository.save(tranship);
    }


    @Override
    public FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxCode(String frozenBoxCode) {
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        //查询冻存盒信息
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        //查询冻存盒的转运记录
        TranshipBox transhipBox = transhipBoxRepository.findByFrozenBoxCode(frozenBoxCode);
        if(transhipBox == null){
            throw new BankServiceException("未查询到该冻存盒的转运记录！",frozenBoxCode);
        }
        //查询转运盒的位置
        TranshipBoxPosition transhipBoxPosition = transhipBoxPositionRepository.findByTranshipBoxIdLast(transhipBox.getId());
        if(transhipBoxPosition != null){
            frozenBox.setEquipment(transhipBoxPosition.getEquipment()!=null?transhipBoxPosition.getEquipment():null);
            frozenBox.setEquipmentCode(transhipBoxPosition.getEquipmentCode());
            frozenBox.setArea(transhipBoxPosition.getArea()!=null?transhipBoxPosition.getArea():null);
            frozenBox.setAreaCode(transhipBoxPosition.getAreaCode());
            frozenBox.setSupportRack(transhipBoxPosition.getSupportRack()!=null?transhipBoxPosition.getSupportRack():null);
            frozenBox.setSupportRackCode(transhipBoxPosition.getSupportRackCode());
            frozenBox.setRowsInShelf(transhipBoxPosition.getRowsInShelf());
            frozenBox.setColumnsInShelf(transhipBoxPosition.getColumnsInShelf());
        }
        //查询转运冻存管
        List<TranshipTube> transhipTubeList = transhipTubeRepository.findByTranshipBoxIdLast(transhipBox.getId());
        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<FrozenTubeDTO>();
        for(TranshipTube f: transhipTubeList){
            FrozenTube frozenTube = f.getFrozenTube();
            FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);
            frozenTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            frozenTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            frozenTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            frozenTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            frozenTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            frozenTubeDTO.setSampleClassificationCode(f.getSampleClassificationCode());
            frozenTubeDTO.setSampleClassificationName(f.getSampleClassificationName());
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox);
        res.setFrozenTubeDTOS(frozenTubeDTOS);
        return res;
    }

    @Override
    public void deleteTranshipBoxByFrozenBox(String frozenBoxCode) {
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！",frozenBoxCode);
        }
        if(!frozenBox.getStatus().equals(Constants.FROZEN_BOX_NEW)){
            throw new BankServiceException("冻存盒的状态已经不能进行删除操作！",frozenBoxCode);
        }
        TranshipBox transhipBox = transhipBoxRepository.findByFrozenBoxCode(frozenBoxCode);
        if(transhipBox == null){
            throw new BankServiceException("未查询到该冻存盒的转运记录！",frozenBoxCode);
        }
        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBoxCode);
        for(FrozenTube f:frozenTubes){
            f.setStatus(Constants.INVALID);
        }
        frozenTubeRepository.save(frozenTubes);
        transhipBox.setStatus(Constants.INVALID);
        transhipBoxRepository.save(transhipBox);
        frozenBox.setStatus(Constants.INVALID);
        frozenBoxRepository.save(frozenBox);
        TranshipBoxPosition transhipBoxPosition = new TranshipBoxPosition();
        transhipBoxPosition.transhipBox(transhipBox).memo(transhipBox.getMemo()).status(Constants.INVALID)
            .equipment(transhipBox.getFrozenBox().getEquipment()).equipmentCode(transhipBox.getEquipmentCode())
            .area(transhipBox.getFrozenBox().getArea()).areaCode(transhipBox.getAreaCode())
            .supportRack(transhipBox.getFrozenBox().getSupportRack()).supportRackCode(transhipBox.getSupportRackCode())
            .rowsInShelf(transhipBox.getRowsInShelf()).columnsInShelf(transhipBox.getColumnsInShelf());
        transhipBoxPositionRepository.save(transhipBoxPosition);
        //修改转运的数量
        this.updateTranshipSampleNumber(transhipBox.getTranship());
    }

    @Override
    public List<FrozenBoxCodeForTranshipDTO> getFrozenBoxCodeByTranshipCode(String transhipCode) {
        if(StringUtils.isEmpty(transhipCode)){
            throw new BankServiceException("转运编码不能为空！");
        }

        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("转运记录不存在！",transhipCode);
        }

        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByTranshipId(tranship.getId());
        List<FrozenBoxCodeForTranshipDTO> frozenBoxCodeForTranshipDTOS = new ArrayList<FrozenBoxCodeForTranshipDTO>();
        for(TranshipBox t : transhipBoxes){
            FrozenBoxCodeForTranshipDTO frozenBoxCodeForTranshipDTO = new FrozenBoxCodeForTranshipDTO();
            frozenBoxCodeForTranshipDTO.setFrozenBoxId(t.getFrozenBox()!=null?t.getFrozenBox().getId():null);
            frozenBoxCodeForTranshipDTO.setFrozenBoxCode(t.getFrozenBoxCode());
            frozenBoxCodeForTranshipDTOS.add(frozenBoxCodeForTranshipDTO);
        }
        return frozenBoxCodeForTranshipDTOS;
    }

    @Override
    public DataTablesOutput<FrozenBoxCodeForTranshipDTO> getPageFrozenBoxCodeByTranshipCode(String transhipCode, DataTablesInput input) {
        if(StringUtils.isEmpty(transhipCode)){
            throw new BankServiceException("转运编码不能为空！");
        }
        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("转运记录不存在！",transhipCode);
        }
        input.addColumn("tranship.id",true,true,tranship.getId()+"+");
        DataTablesOutput<FrozenBoxCodeForTranshipDTO> output = new DataTablesOutput<FrozenBoxCodeForTranshipDTO>();
        Converter<TranshipBox, FrozenBoxCodeForTranshipDTO> convert = new Converter<TranshipBox, FrozenBoxCodeForTranshipDTO>() {
            @Override
            public FrozenBoxCodeForTranshipDTO convert(TranshipBox e) {
                return new FrozenBoxCodeForTranshipDTO(e.getFrozenBox().getId(),e.getFrozenBoxCode(), e.getFrozenBoxCode1D());
            }
        };
        Specification<TranshipBox> specification = new Specification<TranshipBox>() {
            @Override
            public Predicate toPredicate(Root<TranshipBox> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate p1 = cb.notEqual(root.get("status").as(String.class), Constants.INVALID);
                predicate.add(p1);
                Predicate p2 = cb.notEqual(root.get("status").as(String.class), Constants.FROZEN_BOX_INVALID);
                predicate.add(p2);
                Predicate[] pre = new Predicate[predicate.size()];
                query.where(predicate.toArray(pre));
                return query.getRestriction();
            }
        };
        output = transhipBoxRepositories.findAll(input,specification,null,convert);
        return output;
    }
}
