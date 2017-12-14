package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.TranshipBoxMapper;
import org.fwoxford.service.mapper.TranshipTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.json.JSONArray;
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
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TranshipBox.
 */
@Service
@Transactional
public class TranshipBoxServiceImpl implements TranshipBoxService{

    private final Logger log = LoggerFactory.getLogger(TranshipBoxServiceImpl.class);
    @Autowired
    TranshipBoxRepository transhipBoxRepository;
    @Autowired
    FrozenBoxRepository frozenBoxRepository;
    @Autowired
    FrozenTubeRepository frozenTubeRepository;
    @Autowired
    SampleTypeRepository sampleTypeRepository;
    @Autowired
    FrozenBoxTypeRepository frozenBoxTypeRepository;
    @Autowired
    TranshipRepository transhipRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    AreaRepository areaRepository;
    @Autowired
    SupportRackRepository supportRackRepository;
    @Autowired
    FrozenTubeTypeRepository frozenTubeTypeRepository;
    @Autowired
    TranshipBoxMapper transhipBoxMapper;
    @Autowired
    FrozenBoxMapper frozenBoxMapper;
    @Autowired
    FrozenTubeMapper frozenTubeMapper;
    @Autowired
    ProjectSampleClassRepository projectSampleClassRepository;
    @Autowired
    SampleClassificationRepository sampleClassificationRepository;
    @Autowired
    TranshipBoxPositionService transhipBoxPositionService;
    @Autowired
    TranshipTubeService transhipTubeService;
    @Autowired
    TranshipTubeRepository transhipTubeRepository;
    @Autowired
    TranshipBoxPositionRepository transhipBoxPositionRepository;
    @Autowired
    FrozenBoxCheckService frozenBoxCheckService;
    @Autowired
    TranshipBoxRepositories transhipBoxRepositories;
    @Autowired
    FrozenTubeCheckService frozenTubeCheckService;
    @Autowired
    StockOutFrozenBoxRepository stockOutFrozenBoxRepository;
    @Autowired
    StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;
    @Autowired
    FrozenBoxImportService frozenBoxImportService;
    @Autowired
    TranshipTubeMapper transhipTubeMapper;
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
            box.setCountOfSample(countOfSampleAll);
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
            transhipBox.setFrozenBoxCode1D(box.getFrozenBoxCode1D());
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

        tube.setSampleTypeCode(tube.getSampleType().getSampleTypeCode());
        tube.setSampleTypeName(tube.getSampleType().getSampleTypeName());

        tube.setFrozenBox(box);
        tube.setFrozenBoxCode(box.getFrozenBoxCode());

        tube.setProject(box.getProject());
        tube.setProjectCode(box.getProjectCode());
        tube.setProjectSite(box.getProjectSite());
        tube.setProjectSiteCode(box.getProjectSiteCode());
        if (tube.getSampleClassification() == null){
            tube.setSampleClassification(box.getSampleClassification());
        } else {
            int sampleClassificationsIndex = sampleClassifications.indexOf(tube.getSampleClassification());
            if (sampleClassificationsIndex >= 0) {
                SampleClassification sampleClass = sampleClassifications.get(sampleClassificationsIndex);
                tube.setSampleClassification(sampleClass);
                //验证项目与样本类型与样本分类是否配置
                ProjectSampleClass projectSampleClass = projectSampleClasses.stream().filter(
                    s->s.getSampleType().equals(tube.getSampleType())&& s.getProjectCode().equals(tube.getProjectCode())
                        &&s.getSampleClassificationCode().equals(sampleClass.getSampleClassificationCode())).findFirst().orElse(null);
                if(projectSampleClass==null){
                    throw new BankServiceException(tube.getProjectCode()+"项目未配置"+sampleClass.getSampleClassificationCode()+"分类！");
                }
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
//            if(sampleType == null){
//                throw new BankServiceException("样本类型无效！",box.toString());
//            }
        }

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
            if(box.getSampleTypeCode().equals("RNA")){
                tubeType = tubeTypes.stream().filter(t->t.getFrozenTubeTypeCode().equals("RNA")).findFirst().orElse(tubeTypes.get(0));
            }

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
        int countOfEmptyHole = 0;int countOfEmptyTube = 0;
        int countOfTube = 0;//有效样本数
        final  Integer[] countOfPeople = {0};//样本人份
        List<Long> boxIds = new ArrayList<Long>();
        for(FrozenBox box : frozenBoxList){
            frozenBoxRepository.save(box);
            boxIds.add(box.getId());
        }
        if(boxIds.size()>0){
            countOfEmptyHole = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_HOLE_EMPTY);
            countOfEmptyTube = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_EMPTY);
            countOfTube = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_NORMAL);
            //查询临时样本人份
            List<Object[]> countOfTempSampleCodeGroupBySampleTempCode = frozenTubeRepository.countByFrozenBoxCodeStrAndGroupBySampleTempCode(boxIds);
            //查询扫码后的样本人份
            List<Object[]> countOfSampleCodeGroupBySampleCode = frozenTubeRepository.countByFrozenBoxCodeStrAndGroupBySampleCode(boxIds);
            countOfTempSampleCodeGroupBySampleTempCode.forEach(s->{
                if(s[0]!=null){
                    countOfPeople[0]++;
                }
            });
            countOfSampleCodeGroupBySampleCode.forEach(s->{
                if(s[0]!=null){
                    countOfPeople[0]++;
                }
            });
        }
        tranship.setSampleNumber(countOfPeople[0]);
        tranship.setFrozenBoxNumber(frozenBoxList.size());
        tranship.setEmptyHoleNumber(countOfEmptyHole);
        tranship.setEmptyTubeNumber(countOfEmptyTube);
        tranship.setEffectiveSampleNumber(countOfTube);
        transhipRepository.save(tranship);
    }

    /**
     * 根据冻存盒CODE查询冻存盒和冻存管信息（取转运的冻存盒以及冻存管数据）
     * @param frozenBoxCode
     * @return
     */
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
            frozenTubeDTO.setSampleTypeId(f.getId());
            frozenTubeDTO.setSampleTypeCode(f.getSampleTypeCode());
            frozenTubeDTO.setSampleTypeName(f.getSampleTypeName());
            frozenTubeDTO.setSampleClassificationId(f.getSampleClassification()!=null?f.getSampleClassification().getId():null);
            frozenTubeDTO.setSampleClassificationCode(f.getSampleClassificationCode());
            frozenTubeDTO.setSampleClassificationName(f.getSampleClassificationName());
            frozenTubeDTO.setFrozenBoxCode(f.getFrozenBoxCode());
            frozenTubeDTO.setTubeColumns(f.getColumnsInTube());
            frozenTubeDTO.setTubeRows(f.getRowsInTube());
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox);
        res.setFrozenTubeDTOS(frozenTubeDTOS);
        return res;
    }

    /**
     * 根据冻存盒编码删除转运中的冻存盒以及冻存管
     * @param frozenBoxCode
     */
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

    /**
     * 根据转运编码查询冻存盒编码List
     * @param transhipCode
     * @return
     */
    @Override
    public List<FrozenBoxCodeForTranshipDTO> getFrozenBoxCodeByTranshipCode(String transhipCode) {
        if(StringUtils.isEmpty(transhipCode)){
            throw new BankServiceException("转运或归还编码不能为空！");
        }

        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("转运或归还记录不存在！",transhipCode);
        }

        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByTranshipId(tranship.getId());
        List<FrozenBoxCodeForTranshipDTO> frozenBoxCodeForTranshipDTOS = new ArrayList<FrozenBoxCodeForTranshipDTO>();
        for(TranshipBox t : transhipBoxes){
            FrozenBoxCodeForTranshipDTO frozenBoxCodeForTranshipDTO = new FrozenBoxCodeForTranshipDTO();
            frozenBoxCodeForTranshipDTO.setFrozenBoxId(t.getFrozenBox()!=null?t.getFrozenBox().getId():null);
            frozenBoxCodeForTranshipDTO.setFrozenBoxCode(t.getFrozenBoxCode());
            frozenBoxCodeForTranshipDTO.setFrozenBoxCode1D(t.getFrozenBoxCode1D());
            frozenBoxCodeForTranshipDTOS.add(frozenBoxCodeForTranshipDTO);
        }
        return frozenBoxCodeForTranshipDTOS;
    }

    /**
     * 分页查询转运冻存盒
     * @param transhipCode
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<FrozenBoxCodeForTranshipDTO> getPageFrozenBoxCodeByTranshipCode(String transhipCode, DataTablesInput input) {
        if(StringUtils.isEmpty(transhipCode)){
            throw new BankServiceException("转运或归还编码不能为空！");
        }
        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("转运或归还记录不存在！",transhipCode);
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
                javax.persistence.criteria.Order order = cb.asc(root.get("id"));

                Predicate p1 = cb.notEqual(root.get("status").as(String.class), Constants.INVALID);
                predicate.add(p1);
                Predicate p2 = cb.notEqual(root.get("status").as(String.class), Constants.FROZEN_BOX_INVALID);
                predicate.add(p2);
                Predicate[] pre = new Predicate[predicate.size()];
                query.where(predicate.toArray(pre));
                query.orderBy(order);
                return query.getRestriction();
            }
        };
        output = transhipBoxRepositories.findAll(input,specification,null,convert);
        return output;
    }

    /**
     * 归还冻存盒的保存
     * @param transhipBoxListDTO
     * @return
     */
    @Override
    public TranshipBoxListForSaveBatchDTO saveBatchTranshipBoxForReturn(TranshipBoxListDTO transhipBoxListDTO) {
        return createBatchTranshipBoxForReturn(transhipBoxListDTO,Constants.RECEIVE_TYPE_RETURN_BACK);
    }

    public TranshipBoxListForSaveBatchDTO createBatchTranshipBoxForReturn(TranshipBoxListDTO transhipBoxListDTO, String receiveType) {
        TranshipBoxListForSaveBatchDTO result = new TranshipBoxListForSaveBatchDTO();
        Long transhipId = transhipBoxListDTO.getTranshipId();
        Tranship tranship = transhipRepository.findOne(transhipId);
        if (tranship == null){
            // 转运ID无效的情况
            throw new BankServiceException("转运或归还记录不存在！",transhipBoxListDTO.toString());
        }
        List<SampleType> sampleTypes = sampleTypeRepository.findAllSampleTypes();
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        List<FrozenBoxType> boxTypes = frozenBoxTypeRepository.findAllFrozenBoxTypes();
        List<FrozenTubeType> tubeTypes = frozenTubeTypeRepository.findAll();
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findSampleTypeByProjectCode(tranship.getProjectCode());
        result.setTranshipId(transhipId);
        result.setFrozenBoxDTOList(new ArrayList<>());

        //如果接收类型为项目点，判断冻存盒编码是否重复
        //如果接受类型为实验室，验证（1）冻存盒是否是出库交接的，（2）冻存盒是否是本次出库申请的，（3）若冻存盒是新增的，是否有样本的来源
        //验证冻存盒编码是否重复
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

        switch (receiveType){
            case Constants.RECEIVE_TYPE_PROJECT_SITE:
                frozenBoxCheckService.checkFrozenBoxCodeRepead(frozenBoxCodeMap);
                break;
            case Constants.RECEIVE_TYPE_RETURN_BACK:
                frozenBoxCheckService.checkFrozenBoxCodeForStockOutReturn(transhipBoxListDTO);
                ;break;
            default: break;
        }


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
            box.setCountOfSample(countOfSampleAll);
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
            transhipBox.setFrozenBoxCode1D(box.getFrozenBoxCode1D());
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
        }
        this.updateTranshipSampleNumber(tranship);

        return result;
    }


    /**
     * 根据出库申请编码和冻存盒编码串获取出库冻存盒和样本信息
     * @param applyCode
     * @param frozenBoxCodeStr
     * @return
     */
    @Override
    public List<TranshipBoxDTO> getStockOutFrozenBoxAndSample(String applyCode, String frozenBoxCodeStr) {
        //定义返回结果
        List<TranshipBoxDTO> frozenBoxAndFrozenTubeResponses = new ArrayList<>();
        //从出库中获取
        String[] boxCodeStr = frozenBoxCodeStr.split(",");
        List<String> boxCodeList = Arrays.asList(boxCodeStr);
        //对传入的需要导入的冻存盒每500条进行分组
        List<List<String>> boxCodeEach500 = Lists.partition(boxCodeList,500);
        //未从出库冻存盒中获取到数据的冻存盒编码
        List<String> unStockOutFrozenBoxCode = new ArrayList();
        for(List<String> boxCodes :boxCodeEach500){
            //从出库冻存盒中获取出库盒和出库样本的信息
            List<StockOutFrozenBox> stockOutFrozenBoxes = stockOutFrozenBoxRepository.findByFrozenBoxCodeAndStockOutApply(boxCodes,applyCode);
            //获取出库冻存盒ID串
            List<Long> stockOutFrozenBoxIds = stockOutFrozenBoxes.stream().map(s->s.getId()).collect(Collectors.toList());
            //根据出库冻存盒ID查询出库样本
            List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutFrozenBoxIdIn(stockOutFrozenBoxIds);
            List<String> outFrozenBoxCodeStr = stockOutReqFrozenTubes.stream().map(s->s.getFrozenBoxCode()).collect(Collectors.toList());
            List<String> outFrozenBoxCodeIdStr = stockOutReqFrozenTubes.stream().map(s->s.getFrozenBoxCode1D()).collect(Collectors.toList());
            //是否全部获取到，如果没有获取到，需要从LIMS中获取

            for(String s : boxCodes){
                if(!outFrozenBoxCodeStr.contains(s)&&!outFrozenBoxCodeIdStr.contains(s)){
                    unStockOutFrozenBoxCode.add(s);
                }
            }

            //从出库冻存盒中查出冻存盒信息
            List<FrozenBox> frozenBoxList = stockOutFrozenBoxes.stream().map(s->s.getFrozenBox()).collect(Collectors.toList());
            //从出库样本中查询出样本的信息
            List<FrozenTube> frozenTubeList = stockOutReqFrozenTubes.stream().map(s->s.getFrozenTube()).collect(Collectors.toList());
            List<TranshipTubeDTO> transhipTubeDTOS =  transhipTubeMapper.frozenTubesToTranshipTubeDTOs(frozenTubeList);
            //将样本信息根据冻存盒ID进行分组
            Map<Long,List<TranshipTubeDTO>> frozenTubeMapGroupByFrozenBoxId = transhipTubeDTOS.stream().collect(Collectors.groupingBy(s->s.getFrozenBoxId()));
            List<TranshipBoxDTO> frozenBoxAndFrozenTubeResponse = transhipBoxMapper.forzenBoxsAndTubesToFrozenBoxAndFrozenTubeResponses(frozenBoxList,frozenTubeMapGroupByFrozenBoxId);
            frozenBoxAndFrozenTubeResponses.addAll(frozenBoxAndFrozenTubeResponse);
        }

        //验证 todo(1) 是否所有的冻存盒都是同一个项目，如果不是，需要根据项目拆成不同的归还记录；（2）是否冻存盒都在此次出库申请下

        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("96KB");
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("2DDCG");
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode("DNA");
        SampleClassification sampleClassification = sampleClassificationRepository.findBySampleClassificationCode("12");
        for(String boxCode : unStockOutFrozenBoxCode){
            //从实验室获取所有的DNA数据
            List<JSONObject> mapList = frozenBoxImportService.importFrozenBoxAndSampleAllDataFromLIMS(boxCode);
            if(mapList==null || mapList.size()==0){
                TranshipBoxDTO transhipBoxDTO = new TranshipBoxDTO();
                transhipBoxDTO.setFrozenBoxCode(boxCode);
                transhipBoxDTO.setIsRealData(Constants.NO);
                frozenBoxAndFrozenTubeResponses.add(transhipBoxDTO);
            }else{
                TranshipBoxDTO frozenBoxAndFrozenTubeResponse = createFrozenBoxAndTypeFromLIMSData(mapList,frozenBoxType,frozenTubeType,sampleType,sampleClassification);
                frozenBoxAndFrozenTubeResponses.add(frozenBoxAndFrozenTubeResponse);
            }
        }
        return frozenBoxAndFrozenTubeResponses;
    }

    private TranshipBoxDTO createFrozenBoxAndTypeFromLIMSData(List<JSONObject> mapList, FrozenBoxType frozenBoxType, FrozenTubeType frozenTubeType, SampleType sampleType, SampleClassification sampleClassification) {
        //上一级样本编码
        List<String> sampleCodeStr = mapList.stream().map(s->s.getString("LABEL_NR")).collect(Collectors.toList());
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeInAndStatusNot(sampleCodeStr,Constants.INVALID);
        //构造返回参数
        TranshipBoxDTO transhipBoxDTO = new TranshipBoxDTO();
        String frozenBoxCode = mapList.get(0).getString("TRAY_BARCODE");
        transhipBoxDTO.setIsRealData(Constants.YES);
        transhipBoxDTO.setFrozenBoxCode(mapList.get(0).getString("TRAY_BARCODE"));

        //冻存盒类型
        transhipBoxDTO.setFrozenBoxTypeId(frozenBoxType.getId());
        transhipBoxDTO.setFrozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode());
        transhipBoxDTO.setFrozenBoxTypeName(frozenBoxType.getFrozenBoxTypeName());
        transhipBoxDTO.setFrozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows());
        transhipBoxDTO.setFrozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns());
        transhipBoxDTO.setIsSplit(Constants.NO);
        //冻存盒样本类型
        transhipBoxDTO.setSampleTypeId(sampleType.getId());
        transhipBoxDTO.setSampleTypeCode(sampleType.getSampleTypeCode());
        transhipBoxDTO.setSampleTypeName(sampleType.getSampleTypeName());
        transhipBoxDTO.setFrontColor(sampleType.getFrontColor());
        transhipBoxDTO.setIsMixed(sampleType.getIsMixed());
        transhipBoxDTO.setBackColor(sampleType.getBackColor());
        //冻存盒样本分类
        transhipBoxDTO.setSampleClassificationId(sampleClassification.getId());
        transhipBoxDTO.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
        transhipBoxDTO.setSampleClassificationName(sampleClassification.getSampleClassificationName());
        transhipBoxDTO.setFrontColorForClass(sampleClassification.getFrontColor());
        transhipBoxDTO.setBackColorForClass(sampleClassification.getBackColor());
        transhipBoxDTO.setStatus(Constants.FROZEN_BOX_NEW);
        transhipBoxDTO.setCountOfSample(mapList.size());
        //构造样本
        List<TranshipTubeDTO> transhipTubeDTOS = new ArrayList<>();
        for(JSONObject jsonObject : mapList){
            String parentSampleCode = jsonObject.getString("LABEL_NR");
            String sampleCode = jsonObject.getString("TUBE_BARCODE");
            String volumn = jsonObject.getString("VOLUME");
            String pos = jsonObject.getString("SLOT");
            Integer postison = Integer.valueOf(pos);
            if(StringUtils.isEmpty(parentSampleCode) || StringUtils.isEmpty(sampleCode)){
                throw new BankServiceException("冻存盒"+frozenBoxCode+"内获取样本为空！请联系管理员");
            }
            FrozenTube frozenTube = frozenTubeList.stream().filter(d->d.getSampleCode().equals(parentSampleCode)).findFirst().orElse(null);
            if(frozenTube == null){
                throw new BankServiceException("样本"+sampleCode+"未查询到上一级样本"+parentSampleCode);
            }

            int tubeRowsInt = postison/12+1;
            int tubeColumns = postison%12+1;
            String tubeRows = "";
            if (tubeRowsInt >= 9) {
                tubeRows = String.valueOf((char) (tubeRowsInt + 65));
            }else{
                tubeRows = String.valueOf((char) (tubeRowsInt + 64));
            }
            TranshipTubeDTO transhipTubeDTO = new TranshipTubeDTO();
            transhipTubeDTO.setSampleCode(sampleCode);
            transhipTubeDTO.setFrozenBoxCode(frozenBoxCode);
            transhipTubeDTO.setParentSampleId(frozenTube.getId());
            transhipTubeDTO.setParentSampleCode(frozenTube.getSampleCode());
            //冻存管类型
            transhipTubeDTO.setFrozenTubeTypeId(frozenTubeType.getId());
            transhipTubeDTO.setFrozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName());
            transhipTubeDTO.setFrozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode());
            //转运管样本类型
            transhipTubeDTO.setSampleTypeId(sampleType.getId());
            transhipTubeDTO.setSampleTypeCode(sampleType.getSampleTypeCode());
            transhipTubeDTO.setSampleTypeName(sampleType.getSampleTypeName());
            transhipTubeDTO.setFrontColor(sampleType.getFrontColor());
            transhipTubeDTO.setIsMixed(sampleType.getIsMixed());
            transhipTubeDTO.setBackColor(sampleType.getBackColor());
            //转运管样本分类
            transhipTubeDTO.setSampleClassificationId(sampleClassification.getId());
            transhipTubeDTO.setSampleClassificationCode(sampleClassification.getSampleClassificationCode());
            transhipTubeDTO.setSampleClassificationName(sampleClassification.getSampleClassificationName());
            transhipTubeDTO.setFrontColorForClass(sampleClassification.getFrontColor());
            transhipTubeDTO.setBackColorForClass(sampleClassification.getBackColor());
            //盒内位置
            transhipTubeDTO.setColumnsInTube(String.valueOf(tubeColumns));
            transhipTubeDTO.setRowsInTube(tubeRows);

            transhipTubeDTO.setFrozenTubeState(Constants.FROZEN_BOX_NEW);
            transhipTubeDTO.setStatus(Constants.FROZEN_TUBE_NORMAL);
            transhipTubeDTO.setSampleVolumns(!volumn.equals(null)?Double.valueOf(volumn):null);

            transhipTubeDTOS.add(transhipTubeDTO);
        }
        transhipBoxDTO.setTranshipTubeDTOS(transhipTubeDTOS);
        return transhipBoxDTO;
    }
}
