package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.SampleType;
import org.fwoxford.domain.Tranship;
import org.fwoxford.repository.TranshipRepository;
import org.fwoxford.repository.TranshipRepositries;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.TranshipMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Tranship.
 */
@Service
@Transactional
public class TranshipServiceImpl implements TranshipService{

    private final Logger log = LoggerFactory.getLogger(TranshipServiceImpl.class);

    private final TranshipRepository transhipRepository;

    private final TranshipMapper transhipMapper;

    private  TranshipRepositries transhipRepositries;

    @Autowired
    private FrozenBoxService frozenBoxService;

    @Autowired
    private FrozenTubeService frozenTubeService;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private TranshipBoxService transhipBoxService;

    @Autowired
    private FrozenTubeTypeService frozenTubeTypeService;

    @Autowired
    private SampleTypeService sampleTypeService;

    public TranshipServiceImpl(TranshipRepository transhipRepository, TranshipMapper transhipMapper,TranshipRepositries transhipRepositries) {
        this.transhipRepository = transhipRepository;
        this.transhipMapper = transhipMapper;
        this.transhipRepositries = transhipRepositries;
    }

    /**
     * Save a tranship.
     *
     * @param transhipDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranshipDTO save(TranshipDTO transhipDTO) {
        log.debug("Request to save Tranship : {}", transhipDTO);
        Tranship tranship = transhipMapper.transhipDTOToTranship(transhipDTO);
        tranship = transhipRepository.save(tranship);
        TranshipDTO result = transhipMapper.transhipToTranshipDTO(tranship);
        return result;
    }

    /**
     *  Get all the tranships.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranshipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tranships");
        Page<Tranship> result = transhipRepository.findAll(pageable);
        return result.map(tranship -> transhipMapper.transhipToTranshipDTO(tranship));
    }

    /**
     *  Get one tranship by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranshipDTO findOne(Long id) {
        log.debug("Request to get Tranship : {}", id);
        Tranship tranship = transhipRepository.findOne(id);
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);
        return transhipDTO;
    }

    /**
     *  Delete the  tranship by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tranship : {}", id);
        transhipRepository.delete(id);
    }

    /**
     * 获取转运记录
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<TranshipResponse> findAllTranship(DataTablesInput input) {

        //获取转运列表
        DataTablesOutput<Tranship> transhipDataTablesOutput =  transhipRepositries.findAll(input);
        List<Tranship> tranships =  transhipDataTablesOutput.getData();

        //构造返回列表
        List<TranshipResponse> transhipDTOS = transhipMapper.transhipsToTranshipTranshipResponse(tranships);

        //构造返回分页数据
        DataTablesOutput<TranshipResponse> responseDataTablesOutput = new DataTablesOutput<>();
        responseDataTablesOutput.setDraw(transhipDataTablesOutput.getDraw());
        responseDataTablesOutput.setError(transhipDataTablesOutput.getError());
        responseDataTablesOutput.setData(transhipDTOS);
        responseDataTablesOutput.setRecordsFiltered(transhipDataTablesOutput.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(transhipDataTablesOutput.getRecordsTotal());

        return responseDataTablesOutput;
    }

    /**
     * 根据转运记录ID查询转运记录以及冻存盒的信息
     * @param id 转运ID
     * @return
     */
    @Override
    public TranshipByIdResponse findTranshipAndFrozenBox(Long id) {

        TranshipByIdResponse res = new TranshipByIdResponse();

        //获取转运详情
        Tranship tranship = transhipRepositries.findOne(id);
        res = transhipMapper.transhipsToTranshipTranshipByIdResponse(tranship);

        //获取冻存盒列表
        List<FrozenBoxDTO> frozenBoxResponseList = frozenBoxService.findAllFrozenBoxByTranshipId(id);
        res.setFrozenBoxDTOList(frozenBoxResponseList);

        return res;
    }

    /**
     * 保存转运记录，包括冻存盒，冻存管
     * @param transhipDTO
     * @return
     */
    @Override
    public TranshipDTO insertTranship(TranshipDTO transhipDTO) {
        //验证是否可以保存转运记录
        Boolean isCanTranship =  isCanTranship(transhipDTO);
        //保存转运记录
        Tranship tranship =transhipMapper.transhipDTOToTranship(transhipDTO);
        tranship.setStatus(Constants.VALID);
        tranship.setTranshipCode(BankUtil.getUniqueID());
        transhipRepositries.save(tranship);

        //保存冻存盒
        List<FrozenBoxDTO> frozenBoxDTOList =  transhipDTO.getFrozenBoxDTOList();
        List<FrozenBoxDTO> frozenBoxDTOLists =  frozenBoxMapper.frozenTranshipAndBoxToFrozenBoxDTOList(frozenBoxDTOList,tranship);
        List<FrozenBox> frozenBoxes =  frozenBoxService.saveBatch(frozenBoxDTOLists);
        List<FrozenBoxDTO> frozenBoxDTOListLast = frozenBoxMapper.frozenBoxesToFrozenBoxDTOs(frozenBoxes);

        //保存转运与冻存盒的关系
        List<TranshipBoxDTO> transhipBoxes = saveTranshipAndBoxRelation(frozenBoxDTOListLast);

        //保存冻存管
//        List<FrozenTubeDTO> frozenTubeDTOList = frozenTubeMapper.frozenBoxAndTubeToFrozenTubeDTOList(frozenBoxDTOList,frozenBoxes);
        List<FrozenTubeDTO> frozenTubeDTOList = getFrozenTubeDTOList(frozenBoxDTOList,frozenBoxes);
        List<FrozenTube> frozenTubes =  frozenTubeService.saveBatch(frozenTubeDTOList);
        List<FrozenTubeDTO> frozenTubeDTOS = frozenTubeMapper.frozenTubesToFrozenTubeDTOs(frozenTubes);

        //构造返回函数
        TranshipDTO dto = transhipMapper.transhipToTranshipDTO(tranship);
        List<FrozenBoxDTO> alist = getFrozenBoxDtoList(frozenBoxDTOListLast,frozenTubeDTOS);
        dto.setFrozenBoxDTOList(alist);
        log.debug("Response to saveBatch tranship: {}", dto);
        return dto;
    }

    /**
     * 初始化转运记录
     * @return
     */
    @Override
    public TranshipDTO initTranship() {
        Tranship tranship = new Tranship();
        tranship = transhipInit();
        transhipRepositries.save(tranship);
        return transhipMapper.transhipToTranshipDTO(tranship);
    }

    private Tranship transhipInit() {
        Tranship tranship = new Tranship();
        tranship.setTranshipCode(BankUtil.getUniqueID());
        tranship.setEffectiveSampleNumber(0);
        tranship.setProjectCode(new String(" "));
        tranship.setProject(null);
        tranship.setEmptyHoleNumber(0);
        tranship.setEmptyTubeNumber(0);
        tranship.setFrozenBoxNumber(0);
        tranship.setProjectName(new String(" "));
        tranship.setProjectSite(null);
        tranship.setProjectSiteCode(new String(" "));
        tranship.setProjectSiteName(new String(" "));
        tranship.setReceiver(new String(" "));
        tranship.setReceiveDate(null);
        tranship.setSampleNumber(0);
        tranship.setTrackNumber(new String(" "));
        tranship.setTranshipBatch(new String(" "));
        tranship.setSampleSatisfaction(0);
        tranship.setTranshipDate(null);
        tranship.setTranshipState(Constants.TRANSHIPE_IN_PENDING);
        tranship.setStatus(Constants.VALID);
        return tranship;
    }

    public List<FrozenTubeDTO> getFrozenTubeDTOList(List<FrozenBoxDTO> frozenBoxDTOList, List<FrozenBox> frozenBoxes) {
        List<FrozenTubeDTO> frozenTubeDTOList = new ArrayList<FrozenTubeDTO>();
        Page<FrozenTubeTypeDTO> frozentubeTypeDTO =  frozenTubeTypeService.findAll(new PageRequest(1,1));
        List<SampleTypeDTO> sampleTypeDTOS = sampleTypeService.findAllSampleTypes();
        //        FrozenTubeTypeDTO frozentubeTypeDTO = frozenTubeTypeService.findTopOne();
        for(FrozenBoxDTO boxDto:frozenBoxDTOList){
            for(FrozenTubeDTO tube :boxDto.getFrozenTubeDTOS()){
                for(FrozenBox box:frozenBoxes){
                    if(tube != null && tube.getFrozenBoxCode().equals(box.getFrozenBoxCode())){
                        if(frozentubeTypeDTO.getContent().size()>0){
                            tube.setFrozenTubeTypeId(frozentubeTypeDTO.getContent().get(0).getId());
                            tube.setFrozenTubeTypeCode(frozentubeTypeDTO.getContent().get(0).getFrozenTubeTypeCode());
                            tube.setFrozenTubeTypeName(frozentubeTypeDTO.getContent().get(0).getFrozenTubeTypeName());
                            tube.setFrozenTubeVolumns(frozentubeTypeDTO.getContent().get(0).getFrozenTubeVolumn());
                            tube.setFrozenTubeVolumnsUnit(frozentubeTypeDTO.getContent().get(0).getFrozenTubeVolumnUnit());
                            tube.setSampleUsedTimesMost(frozentubeTypeDTO.getContent().get(0).getSampleUsedTimesMost());
                        }
                        tube.setProjectId(box.getProject() !=null ?box.getProject().getId():null);
                        tube.setProjectCode(box.getProjectCode());
                        tube.setFrozenBoxId(box.getId());
                        frozenTubeDTOList.add(tube);
                    }
                }
                for(SampleTypeDTO sampleType : sampleTypeDTOS){
                    if(tube.getSampleTypeId().equals(sampleType.getId())){
                        tube.setSampleTypeName(sampleType.getSampleTypeName());
                        tube.setSampleTypeCode(sampleType.getSampleTypeCode());
                    }
                }
                tube.setSampleCode(tube.getSampleCode()!=null && tube.getSampleCode() !="" ? tube.getSampleCode():" ");
            }
        }
        return frozenTubeDTOList;
    }

    /**
     * 保存转运记录和冻存盒的关系
     * @param frozenBoxDTOListLast
     * @return
     */
    public List<TranshipBoxDTO> saveTranshipAndBoxRelation(List<FrozenBoxDTO> frozenBoxDTOListLast) {
        List<TranshipBoxDTO> transhipBoxDTOList = new ArrayList<TranshipBoxDTO>();
        for(FrozenBoxDTO boxDTO : frozenBoxDTOListLast){
            TranshipBoxDTO transhipBoxDTO = transhipBoxService.findByTranshipIdAndFrozenBoxId(boxDTO.getTranshipId(),boxDTO.getId());
            transhipBoxDTO = frozenBoxMapper.frozenBoxToTranshipBoxDTO(boxDTO);
            transhipBoxDTOList.add(transhipBoxDTO);
        }
        List<TranshipBoxDTO> transhipBoxes =  transhipBoxService.saveBatch(transhipBoxDTOList);
        return transhipBoxes;
    }

    /**
     * 判断是否可以进行转运
     * @param transhipDTO
     * @return
     */
    private Boolean isCanTranship(TranshipDTO transhipDTO) {
        List<FrozenBoxDTO> frozenBoxDTOS = transhipDTO.getFrozenBoxDTOList();
        Boolean isCanTranship = true;
        //判断盒子位置有效性
        for(FrozenBoxDTO box:frozenBoxDTOS){
            Long equipmentId = box.getEquipmentId();
            Long areaId = box.getAreaId();
            Long supportRackId = box.getSupportRackId();
            String column = box.getColumnsInShelf();
            String row = box.getRowsInShelf();
            List<FrozenBoxDTO> frozenBoxDTOList =  frozenBoxService.countByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(equipmentId,areaId,supportRackId,column,row);
            for(FrozenBoxDTO b:frozenBoxDTOList){
                if(!b.getId().equals(box.getId())){
                    throw new BankServiceException("该位置已有冻存盒存在，请更换冻存盒位置！",box.getEquipmentCode()+"."+box.getAreaCode()+"."+box.getSupportRackCode()+"."+box.getRowsInShelf()+box.getColumnsInShelf());
                }
            }
        }
        //判断盒子内样本是否有效，即是否有数据
        for(FrozenBoxDTO box:frozenBoxDTOS){
            if(box.getFrozenTubeDTOS().isEmpty() || (box.getFrozenTubeDTOS().size() > 0 && box.getFrozenTubeDTOS().get(0).getFrozenTubeCode()==null)){
                throw new BankServiceException("盒子号为:"+box.getFrozenBoxCode()+"的样本数据无效！",box.getFrozenBoxCode());
            }
        }
        return isCanTranship;
    }
    public List<FrozenBoxDTO> getFrozenBoxDtoList(List<FrozenBoxDTO> frozenBoxDTOListLast, List<FrozenTubeDTO> frozenTubeDTOS) {
        List<FrozenBoxDTO> alist = new ArrayList<FrozenBoxDTO>();
        for(FrozenBoxDTO box :frozenBoxDTOListLast){
            List<FrozenTubeDTO> frozenTubeList = new ArrayList<FrozenTubeDTO>();
            for(FrozenTubeDTO tube:frozenTubeDTOS){
                if(box.getId().equals(tube.getFrozenBoxId())){
                    frozenTubeList.add(tube);
                }
            }
            box.setFrozenTubeDTOS(frozenTubeList);
            alist.add(box);
        }
        return alist;
    }
}
