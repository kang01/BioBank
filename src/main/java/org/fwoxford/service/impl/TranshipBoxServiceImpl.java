package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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
    private  FrozenBoxService frozenBoxService;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private TranshipService transhipService;

    @Autowired
    private FrozenTubeService frozenTubeService;

    @Autowired
    private FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private FrozenBoxTypeService frozenBoxTypeService;

    @Autowired
    private SampleTypeService sampleTypeService;

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
    public TranshipBoxListDTO saveBatchTranshipBox(TranshipBoxListDTO transhipBoxListDTO) {
        TranshipBoxListDTO result = new TranshipBoxListDTO();
        Long transhipId = transhipBoxListDTO.getTranshipId();
        Tranship tranship = transhipRepository.findOne(transhipId);

        if (tranship == null){
            // 转运ID无效的情况
            throw new BankServiceException("转运记录不存在！",transhipBoxListDTO.toString());
        }

        List<SampleType> sampleTypes = sampleTypeRepository.findAllSampleTypes();
        List<FrozenBoxType> boxTypes = frozenBoxTypeRepository.findAllFrozenBoxTypes();
        List<FrozenTubeType> tubeTypes = frozenTubeTypeRepository.findAll();

        result.setTranshipId(transhipId);
        result.setFrozenBoxDTOList(new ArrayList<>());

        for(FrozenBoxDTO boxDTO : transhipBoxListDTO.getFrozenBoxDTOList()){
            FrozenBox oldBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxDTO.getFrozenBoxCode());
            if (oldBox != null && (boxDTO.getId() == null || !boxDTO.getId().equals(oldBox.getId()))){
                // 盒子编码重复的错误需要抛异常
                throw new BankServiceException("此冻存盒编码已存在！",oldBox.getFrozenBoxCode());
            }

            FrozenBox box = frozenBoxMapper.frozenBoxDTOToFrozenBox(boxDTO);
            box = (FrozenBox) EntityUtil.avoidFieldValueNull(box);
            if (oldBox != null) {
                box.setId(oldBox.getId());
            }

            box.setTranship(tranship);
            if (box.getProject() != null){
                Project project = projectRepository.findOne(box.getProject().getId());
                box.setProject(project);
                box.setProjectCode(project.getProjectCode());
                box.setProjectName(project.getProjectName());
            } else if (tranship.getProject() != null ) {
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
                // 把盒子位置信息单独拿出来放一个表
                box.setEquipment(null);
                box.setEquipmentCode(null);
            }

            if (box.getArea() != null) {
                Area area = areaRepository.findOne(box.getArea().getId());
                box.setArea(area);
                box.setAreaCode(area.getAreaCode());
            } else {
                // 把盒子位置信息单独拿出来放一个表
                box.setArea(null);
                box.setAreaCode(null);
            }

            if (box.getSupportRack() != null) {
                SupportRack shelf = supportRackRepository.findOne(box.getSupportRack().getId());
                box.setSupportRack(shelf);
                box.setSupportRackCode(shelf.getSupportRackCode());
            } else {
                // 把盒子位置信息单独拿出来放一个表
                box.setSupportRack(null);
                box.setSupportRackCode(null);
            }

            if (box.getProjectSite() != null) {
                ProjectSite projectSite = projectSiteRepository.findOne(box.getProjectSite().getId());
                box.setProjectSite(projectSite);
                box.setProjectSiteCode(projectSite.getProjectSiteCode());
                box.setProjectSiteName(projectSite.getProjectSiteName());
            } else if (tranship.getProjectSite() != null) {
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
                }
            }

            if (box.getSampleType() != null) {
                int boxSampleTypeIndex = sampleTypes.indexOf(box.getSampleType());
                if (boxSampleTypeIndex >= 0) {
                    SampleType sampleType = sampleTypes.get(boxSampleTypeIndex);
                    box.setSampleType(sampleType);
                    box.setSampleTypeCode(sampleType.getSampleTypeCode());
                    box.setSampleTypeName(sampleType.getSampleTypeName());
                }
            }
            int countOfEmptyHole = 0;int countOfEmptyTube = 0;
            for(FrozenTubeDTO tube : boxDTO.getFrozenTubeDTOS()){
                if(tube.getStatus().equals(Constants.FROZEN_TUBE_HOLE_EMPTY)){
                    countOfEmptyHole++;
                }
                if(tube.getStatus().equals(Constants.FROZEN_TUBE_EMPTY)){
                    countOfEmptyTube++;
                }
            }

            box.setEmptyTubeNumber(countOfEmptyTube);
            box.setEmptyHoleNumber(countOfEmptyHole);
            box.setSampleNumber(boxDTO.getFrozenTubeDTOS().size());

            box = frozenBoxRepository.save(box);

            for(FrozenTubeDTO tubeDTO : boxDTO.getFrozenTubeDTOS()){
                FrozenTube tube = frozenTubeMapper.frozenTubeDTOToFrozenTube(tubeDTO);

                tube = (FrozenTube) EntityUtil.avoidFieldValueNull(tube);

                if (tube.getSampleTypeCode() == null){
                    tube.setSampleTypeCode(box.getSampleTypeCode());
                } else {
//                    FrozenTube finalTube = tube;
//                    SampleType sampleType = sampleTypes.stream()
//                        .filter(s->s.getSampleTypeCode().equals(finalTube.getSampleTypeCode()))
//                        .findFirst().orElse(null);
                    SampleType sampleType = new SampleType();
                    for(SampleType sam :sampleTypes){
                        if(tube.getSampleTypeCode().equals(sam.getSampleTypeCode())){
                            sampleType = sam;
                        }
                    }
                    if(sampleType!=null){
                        tube.setSampleType(sampleType);
                    }else{
                        tube.setSampleType(tube.getSampleType());
                        tube.getSampleType().setSampleTypeName(tube.getSampleTypeName());
                    }
                }
                tube.setSampleType(tube.getSampleType());
                tube.setSampleTypeName(tube.getSampleType().getSampleTypeName());

                tube.setFrozenBox(box);
                tube.setFrozenBoxCode(box.getFrozenBoxCode());

                if (tube.getProject() == null || box.getProject().equals(tube.getProject())){
                    tube.setProject(box.getProject());
                    tube.setProjectCode(box.getProjectCode());
                } else {
                    Project project = projectRepository.findOne(tube.getProject().getId());
                    tube.setProject(project);
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

//        Long transhipId = transhipBoxListDTO.getTranshipId();
//        List<FrozenBoxDTO> frozenBoxDTOList = transhipBoxListDTO.getFrozenBoxDTOList();
//        frozenBoxDTOList = createFrozenBoxAndTubeDetail(frozenBoxDTOList);
//        //保存冻存盒
//        Tranship tranship = new Tranship();tranship.setId(transhipId);
//        List<FrozenBoxDTO> frozenBoxDTOLists =  frozenBoxMapper.frozenTranshipAndBoxToFrozenBoxDTOList(frozenBoxDTOList,tranship);
//        List<FrozenBox> frozenBoxes =  frozenBoxService.saveBatch(frozenBoxDTOLists);
//        List<FrozenBoxDTO> frozenBoxDTOListLast = frozenBoxMapper.frozenBoxesToFrozenBoxDTOs(frozenBoxes);
//
//        //保存冻存盒和转运的关系
//        List<TranshipBoxDTO> transhipBoxDTOList = new ArrayList<TranshipBoxDTO>();
//        for(FrozenBoxDTO boxDTO : frozenBoxDTOListLast){
//            TranshipBoxDTO transhipBoxDTO = frozenBoxMapper.frozenBoxToTranshipBoxDTO(boxDTO);
//            transhipBoxDTOList.add(transhipBoxDTO);
//        }
//        List<TranshipBoxDTO> transhipBoxes =  this.saveBatch(transhipBoxDTOList);
//
//        //保存冻存管
////        List<FrozenTubeDTO> frozenTubeDTOList = frozenTubeMapper.frozenBoxAndTubeToFrozenTubeDTOList(frozenBoxDTOList,frozenBoxes);
//        List<FrozenTubeDTO> frozenTubeDTOList = transhipService.getFrozenTubeDTOList(frozenBoxDTOList,frozenBoxes);
//        List<FrozenTube> frozenTubes =  frozenTubeService.saveBatch(frozenTubeDTOList);
//        List<FrozenTubeDTO> frozenTubeDTOS = frozenTubeMapper.frozenTubesToFrozenTubeDTOs(frozenTubes);
//        List<FrozenBoxDTO> alist = transhipService.getFrozenBoxDtoList(frozenBoxDTOListLast,frozenTubeDTOS);
//        transhipBoxListDTO.setFrozenBoxDTOList(alist);
//        return transhipBoxListDTO;
    }


    /**
     * 删除冻存管，冻存盒，转运盒子
     * @param transhipBoxListDTO
     */
    public void deleteTranshipBoxAndTube(TranshipBoxListDTO transhipBoxListDTO) {
        List<FrozenBoxDTO> frozenBoxDTOList = transhipBoxListDTO.getFrozenBoxDTOList();
        for(FrozenBoxDTO box:frozenBoxDTOList){
            transhipBoxRepository.deleteByFrozenBoxId(box.getId());

//            FrozenBox frozenBox = frozenBoxService.findFrozenBoxDetailsByBoxCode(box.getFrozenBoxCode());
//            if(frozenBox != null){
//                List<FrozenTube> frozenTubeS = frozenTubeService.findFrozenTubeListByBoxId(frozenBox.getId());
//                for(FrozenTube tube: frozenTubeS){
//                    //删除管子
//                    frozenTubeService.delete(tube.getId());
//                }
//                //删除转运盒子
//                transhipBoxRepository.deleteByFrozenBoxId(box.getId());
//                //删除盒子
//                frozenBoxService.delete(frozenBox.getId());
//            }
        }
    }

    public List<FrozenBoxDTO> createFrozenBoxAndTubeDetail(List<FrozenBoxDTO> frozenBoxDTOList) {
        List<FrozenBoxDTO> dtos = new ArrayList<FrozenBoxDTO>();

        List<FrozenBoxTypeDTO> frozenBoxTypeDTOList = frozenBoxTypeService.findAllFrozenBoxTypes();
        List<SampleTypeDTO> sampleTypeDTOS = sampleTypeService.findAllSampleTypes();
        for(FrozenBoxDTO box :frozenBoxDTOList){
            for(FrozenBoxTypeDTO boxType: frozenBoxTypeDTOList){
                if(box.getFrozenBoxTypeId().equals(boxType.getId())){
                    box.setFrozenBoxColumns(boxType.getFrozenBoxTypeColumns());
                    box.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
                    box.setFrozenBoxRows(boxType.getFrozenBoxTypeRows());
                }
            }
            for(SampleTypeDTO sampleType : sampleTypeDTOS){
                if(box.getSampleTypeId().equals(sampleType.getId())){
                    box.setSampleTypeName(sampleType.getSampleTypeName());
                    box.setSampleTypeCode(sampleType.getSampleTypeCode());
                }
            }
            dtos.add(box);
        }
        return dtos;
    }
}
