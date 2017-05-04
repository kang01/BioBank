package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.FrozenBoxTypeService;
import org.fwoxford.service.ProjectSampleClassService;
import org.fwoxford.service.SampleTypeService;
import org.fwoxford.service.TranshipBoxService;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.TranshipBoxMapper;
import org.fwoxford.service.util.EntityUtil;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing TranshipBox.
 */
@Service
@Transactional
public class TranshipBoxServiceImpl implements TranshipBoxService{

    private final Logger log = LoggerFactory.getLogger(TranshipBoxServiceImpl.class);

    private final TranshipBoxRepository transhipBoxRepository;
    private final FrozenBoxRepository frozenBoxRepository;
    private final FrozenTubeRepository frozenTubeRepository;
    private final SampleTypeRepository sampleTypeRepository;
    private final FrozenBoxTypeRepository frozenBoxTypeRepository;
    private final TranshipRepository transhipRepository;
    private final ProjectRepository projectRepository;
    private final EquipmentRepository equipmentRepository;
    private final AreaRepository areaRepository;
    private final SupportRackRepository supportRackRepository;
    private final ProjectSiteRepository projectSiteRepository;
    private final FrozenTubeTypeRepository frozenTubeTypeRepository;

    private final TranshipBoxMapper transhipBoxMapper;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private FrozenBoxTypeService frozenBoxTypeService;

    @Autowired
    private SampleTypeService sampleTypeService;

    @Autowired
    private StockInTubesRepository stockInTubesRepository;

    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;

    @Autowired
    private ProjectSampleClassService projectSampleClassService;

    public TranshipBoxServiceImpl(TranshipBoxRepository transhipBoxRepository,
                                  FrozenBoxRepository frozenBoxRepository,
                                  FrozenTubeRepository frozenTubeRepository,
                                  SampleTypeRepository sampleTypeRepository, FrozenBoxTypeRepository frozenBoxTypeRepository, TranshipRepository transhipRepository,
                                  ProjectRepository projectRepository, EquipmentRepository equipmentRepository, AreaRepository areaRepository, SupportRackRepository supportRackRepository, ProjectSiteRepository projectSiteRepository, FrozenTubeTypeRepository frozenTubeTypeRepository, TranshipBoxMapper transhipBoxMapper) {
        this.transhipBoxRepository = transhipBoxRepository;
        this.sampleTypeRepository = sampleTypeRepository;
        this.frozenBoxTypeRepository = frozenBoxTypeRepository;
        this.projectRepository = projectRepository;
        this.equipmentRepository = equipmentRepository;
        this.areaRepository = areaRepository;
        this.supportRackRepository = supportRackRepository;
        this.projectSiteRepository = projectSiteRepository;
        this.frozenTubeTypeRepository = frozenTubeTypeRepository;
        this.transhipBoxMapper = transhipBoxMapper;
        this.frozenBoxRepository = frozenBoxRepository;
        this.frozenTubeRepository = frozenTubeRepository;
        this.transhipRepository = transhipRepository;
    }

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

        result.setTranshipId(transhipId);
        result.setFrozenBoxDTOList(new ArrayList<>());

        for(FrozenBoxForSaveBatchDTO boxDTO : transhipBoxListDTO.getFrozenBoxDTOList()){
            FrozenBox oldBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxDTO.getFrozenBoxCode());
            if (oldBox != null && (boxDTO.getId() == null || !boxDTO.getId().equals(oldBox.getId()))
                 && !oldBox.getStatus().equals(Constants.FROZEN_BOX_INVALID)){
                // 盒子编码重复的错误需要抛异常
                throw new BankServiceException("此冻存盒编码已存在！",oldBox.getFrozenBoxCode());
            }

            FrozenBox box = frozenBoxMapper.frozenBoxForSaveBatchDTOToFrozenBox(boxDTO);
            box = (FrozenBox) EntityUtil.avoidFieldValueNull(box);
            if (oldBox != null) {
                box.setId(oldBox.getId());
            }
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

            if (box.getFrozenBoxType() != null) {
                int boxTypeIndex = boxTypes.indexOf(box.getFrozenBoxType());
                if (boxTypeIndex >= 0) {
                    FrozenBoxType boxType = boxTypes.get(boxTypeIndex);
                    box.setFrozenBoxType(boxType);
                    box.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
                    box.setFrozenBoxColumns(boxType.getFrozenBoxTypeColumns());
                    box.setFrozenBoxRows(boxType.getFrozenBoxTypeRows());
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
            if (box.getSampleClassification() != null) {
                int boxClassificationIndex = sampleClassifications.indexOf(box.getSampleClassification());
                if (boxClassificationIndex >= 0) {
                    SampleClassification sampleClass = sampleClassifications.get(boxClassificationIndex);
                    box.setSampleClassification(sampleClass);
                }
            }
            //验证项目下样本类型以及样本分类的有效性
            List<ProjectSampleClass> projectSampleClasses = projectSampleClassService.findByProjectIdAndSampleTypeIdAndSampleClassificationId(box.getProject()!=null?box.getProject().getId():null,boxDTO.getSampleTypeId(),boxDTO.getSampleClassificationId());
            if(projectSampleClasses.size()==0){
                throw new BankServiceException("样本类型无效！",box.toString());
            }else{
                if(boxDTO.getSampleClassificationId()!=null){
                    ProjectSampleClass sampleType = projectSampleClasses.stream()
                        .filter(s->s.getSampleClassification().getId().equals(boxDTO.getSampleClassificationId()))
                        .findFirst().orElse(null);
                    if(sampleType == null){
                        throw new BankServiceException("样本类型无效！",box.toString());
                    }
                }
            }
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
            String frozenBoxColumns = box.getFrozenBoxColumns()!=null?box.getFrozenBoxColumns():new String("0");
            String frozenBoxRows = box.getFrozenBoxRows()!=null?box.getFrozenBoxRows():new String("0");

            int allCount = Integer.parseInt(frozenBoxColumns)*Integer.parseInt(frozenBoxRows);
            if(countOfSampleAll>allCount){
                throw new BankServiceException("冻存管的数量已经超过冻存盒的最大容量值！",box.toString());
            }
            box.setSampleNumber(countOfSample);
            box.setStatus(Constants.FROZEN_BOX_NEW);
            box = frozenBoxRepository.save(box);
            //删除冻存管
            frozenTubeRepository.deleteByFrozenBoxCode(box.getFrozenBoxCode());
            for(FrozenTubeForSaveBatchDTO tubeDTO : boxDTO.getFrozenTubeDTOS()){
                FrozenTube tube = frozenTubeMapper.frozenTubeForSaveBatchDTOToFrozenTube(tubeDTO);

                tube = (FrozenTube) EntityUtil.avoidFieldValueNull(tube);
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
                //验证项目下样本类型以及样本分类的有效性
                List<ProjectSampleClass> projectSampleClassList = projectSampleClassService.findByProjectIdAndSampleTypeIdAndSampleClassificationId(box.getProject().getId(),tubeDTO.getSampleTypeId(),tubeDTO.getSampleClassificationId());
                if(projectSampleClassList.size()==0){
                    throw new BankServiceException("样本类型无效！",box.toString());
                }else{
                    if(tubeDTO.getSampleClassificationId()!=null){
                        ProjectSampleClass sampleType = projectSampleClassList.stream()
                            .filter(s->s.getSampleClassification().getId().equals(tubeDTO.getSampleClassificationId()))
                            .findFirst().orElse(null);
                        if(sampleType == null){
                            throw new BankServiceException("样本类型无效！",box.toString());
                        }
                    }
                }
                tube.setSampleTypeCode(tube.getSampleType().getSampleTypeCode());
                tube.setSampleTypeName(tube.getSampleType().getSampleTypeName());

                tube.setFrozenBox(box);
                tube.setFrozenBoxCode(box.getFrozenBoxCode());

                if (tube.getProject() == null || box.getProject().equals(tube.getProject())){
                    tube.setProject(box.getProject());
                    tube.setProjectCode(box.getProjectCode());
                } else {
                    Project project = projectRepository.findOne(tube.getProject().getId());
                    tube.setProject(box.getProject());
                    tube.setProjectCode(project.getProjectCode());
                }

                if (tube.getFrozenTubeType() != null){
                    int tubeTypeIndex = tubeTypes.indexOf(tube.getFrozenTubeType());
                    if (tubeTypeIndex >= 0){
                        FrozenTubeType tubeType = tubeTypes.get(tubeTypeIndex);
                        tube.setFrozenTubeType(tubeType);
                        tube.setFrozenTubeTypeCode(tubeType.getFrozenTubeTypeCode());
                        tube.setFrozenTubeTypeName(tubeType.getFrozenTubeTypeName());
                    }
                } else {
                    FrozenTubeType tubeType = tubeTypes.get(0);
                    tube.setFrozenTubeType(tubeType);
                    tube.setFrozenTubeTypeCode(tubeType.getFrozenTubeTypeCode());
                    tube.setFrozenTubeTypeName(tubeType.getFrozenTubeTypeName());
                }

                tube = frozenTubeRepository.save(tube);
            }

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

            transhipBoxRepository.save(transhipBox);

            result.getFrozenBoxDTOList().add(frozenBoxMapper.frozenBoxToFrozenBoxDTO(box));
        }
        return result;
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
        //查询冻存管列表信息
        List<FrozenTube> frozenTube = new ArrayList<FrozenTube>();
        List<FrozenTubeResponse> frozenTubeResponses = new ArrayList<FrozenTubeResponse>();

        if(frozenBox.getStatus().equals(Constants.FROZEN_BOX_NEW)){
            frozenTube = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBoxCode);
            frozenTubeResponses = frozenTubeMapper.frozenTubeToFrozenTubeResponse(frozenTube);
        }else{
            //如果冻存管已经转运至入库，则需要查询入库管子
            List<StockInTubes> stockInTubes = stockInTubesRepository.findByTranshipCodeAndFrozenBoxCode(transhipBox.getTranship().getTranshipCode(),frozenBoxCode);
            for(StockInTubes inTube:stockInTubes){
                FrozenTubeResponse tubeResponse = new FrozenTubeResponse();
                FrozenTube tube = inTube.getFrozenTube();
                tubeResponse.setId(tube.getId());
                tubeResponse.setFrozenTubeType(tube.getFrozenTubeType());
                tubeResponse.setMemo(tube.getMemo());
                tubeResponse.setSampleCode(tube.getSampleCode());
                tubeResponse.setSampleTempCode(tube.getSampleTempCode());
                tubeResponse.setSampleType(tube.getSampleType());
                tubeResponse.setSampleClassification(tube.getSampleClassification());
                tubeResponse.setStatus(tube.getStatus());
                tubeResponse.setTubeColumns(inTube.getColumnsInTube());
                tubeResponse.setTubeRows(inTube.getRowsInTube());
                tubeResponse.setFrozenBoxCode(inTube.getFrozenBoxPosition().getFrozenBox().getFrozenBoxCode());
                tubeResponse.setFrozenBoxId(inTube.getFrozenBoxPosition().getFrozenBox().getId());
                frozenTubeResponses.add(tubeResponse);
            }
        }
        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox, frozenTubeResponses);

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
        frozenTubeRepository.deleteInBatch(frozenTubes);//删除冻存管
        transhipBoxRepository.delete(transhipBox);
        frozenBoxRepository.delete(frozenBox);
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
}
