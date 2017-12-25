package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.SampleCountByTypeForm;
import org.fwoxford.service.dto.response.StockInForDataDetail;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.fwoxford.service.mapper.AttachmentMapper;
import org.fwoxford.service.mapper.TranshipBoxMapper;
import org.fwoxford.service.mapper.TranshipMapper;
import org.fwoxford.service.mapper.TranshipTubeMapper;
import org.fwoxford.web.rest.CheckTypeResource;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.crypto.codec.Base64;
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
    FrozenBoxService frozenBoxService;
    @Autowired
    FrozenBoxRepository frozenBoxRepository;
    @Autowired
    FrozenTubeRepository frozenTubeRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectSiteRepository projectSiteRepository;
    @Autowired
    TranshipBoxRepository transhipBoxRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    AreaRepository areaRepository;
    @Autowired
    BankUtil bankUtil;
    @Autowired
    UserService userService;
    @Autowired
    StockInRepository stockInRepository;
    @Autowired
    TranshipTubeRepository transhipTubeRepository;
    @Autowired
    StockOutApplyRepository stockOutApplyRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentMapper attachmentMapper;
    @Autowired
    StockOutFilesRepository stockOutFilesRepository;
    @Autowired
    StockOutApplyProjectRepository stockOutApplyProjectRepository;
    @Autowired
    TranshipBoxPositionRepository transhipBoxPositionRepository;
    @Autowired
    TranshipBoxMapper transhipBoxMapper;
    @Autowired
    TranshipTubeMapper transhipTubeMapper;
    @Autowired
    CheckTypeRepository checkTypeRepository;

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
        Long transhipId = transhipDTO.getId();
        if(transhipId == null){
            throw new BankServiceException("转运记录不存在！",transhipDTO.toString());
        }
        Tranship oldTranship = transhipRepository.findOne(transhipDTO.getId());
        if(oldTranship!=null&&(oldTranship.getTranshipState().equals(Constants.TRANSHIPE_IN_STOCKED)
            || oldTranship.getTranshipState().equals(Constants.TRANSHIPE_IN_STOCKING))){
            throw new BankServiceException("转运已不在进行中状态，不能修改记录！",transhipDTO.toString());
        }
        if(transhipDTO.getProjectId()==null){
            throw new BankServiceException("项目不能为空！");
        }

        if(transhipDTO.getProjectSiteId()!=null){
            ProjectSite projectSite = projectSiteRepository.findOne(transhipDTO.getProjectSiteId());
            transhipDTO.setProjectSiteCode(projectSite!=null?projectSite.getProjectSiteCode():new String(""));
            transhipDTO.setProjectSiteName(projectSite!=null?projectSite.getProjectSiteName():new String(""));
        }
        transhipDTO.setTranshipState(oldTranship.getTranshipState());
        transhipDTO.setStatus(oldTranship.getStatus());
        transhipDTO.setProjectCode(oldTranship.getProjectCode());
        transhipDTO.setProjectName(oldTranship.getProjectName());
        transhipDTO.setProjectId(oldTranship.getProject()!=null?oldTranship.getProject().getId():transhipDTO.getProjectId());
        transhipDTO.setReceiveType(Constants.RECEIVE_TYPE_PROJECT_SITE);
        return updateTranship(transhipDTO);

    }

    /**
     * 判断运单号是否重复
     * @param transhipCode
     * @param trackNumber
     * @return
     */
    public Boolean isRepeatTrackNumber(String transhipCode, String trackNumber) {
        if(trackNumber==null || trackNumber.equals(null)){
            throw new BankServiceException("运单号不能为空！",trackNumber);
        }
        Boolean flag = false;
        Tranship tranship = transhipRepository.findByTrackNumber(trackNumber);
        if(tranship!=null&&!tranship.getTranshipCode().equals(transhipCode)){
            flag = true;
        }
        return flag;
    }

    /**
     * 转运完成
     * @param transhipCode
     * @param transhipToStockInDTO
     * @return
     */
    @Override
    public StockInForDataDetail completedTranship(String transhipCode, TranshipToStockInDTO transhipToStockInDTO) {
        return completedReceiveBox(transhipCode,transhipToStockInDTO,Constants.RECEIVE_TYPE_PROJECT_SITE );
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
        List<Object[]> alist = transhipRepository.countFrozenTubeGroupBySampleTypeAndClass(id);
        List<SampleCountByTypeForm> sampleCountByTypeForms = new ArrayList<SampleCountByTypeForm>();
        for(Object[] obj :alist ){
            SampleCountByTypeForm sampleCountByTypeForm = new SampleCountByTypeForm();
            sampleCountByTypeForm.setSampleTypeId(obj[0]!=null?Long.parseLong(obj[0].toString()):null);
            sampleCountByTypeForm.setSampleClassificationId(obj[1]!=null?Long.parseLong(obj[1].toString()):null);
            sampleCountByTypeForm.setSampleTypeName(obj[2]!=null?obj[2].toString():null);
            sampleCountByTypeForm.setSampleClassificationName(obj[3]!=null?obj[3].toString():null);
            sampleCountByTypeForm.setCountOfSample(obj[4]!=null?Long.parseLong(obj[4].toString()):null);
            sampleCountByTypeForms.add(sampleCountByTypeForm);
        }
        TranshipDTO transhipDTO = transhipMapper.transhipToTranshipDTO(tranship);
        transhipDTO.setDelegateName(tranship.getDelegate()!=null?tranship.getDelegate().getDelegateName():null);
        transhipDTO.setApplyCode(tranship.getStockOutApply()!=null?tranship.getStockOutApply().getApplyCode():null);
        transhipDTO.setSampleCountByTypeForms(sampleCountByTypeForms);
        if(tranship.getCheckTypeId()!=null){
            CheckType checkType = checkTypeRepository.findByIdAndStatus(tranship.getCheckTypeId(),Constants.VALID);
            if(checkType!=null){
                transhipDTO.setCheckTypeName(checkType.getCheckTypeName());
            }
        }
        List<User> userList = userRepository.findAll();
        for(User u :userList){
            if(transhipDTO.getReceiverId()!=null&&transhipDTO.getReceiverId().equals(u.getId())){
                String name = (u.getLastName()!=null?u.getLastName():"")+(u.getFirstName()!=null?u.getFirstName():"");
                transhipDTO.setReceiver(name);
            }
        }
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
    public DataTablesOutput<TranshipResponse> findAllTranship(DataTablesInput input ,String receiveType) {
        //获取转运列表
        input.addColumn("receiveType",true,true,"+"+receiveType);
        DataTablesOutput<TranshipResponse> transhipDataTablesOutput =  transhipRepositries.findAll(input);
        return transhipDataTablesOutput;
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
        Tranship tranship = transhipRepository.findOne(id);
        res = transhipMapper.transhipsToTranshipTranshipByIdResponse(tranship);

        //获取冻存盒列表
        List<FrozenBoxDTO> frozenBoxResponseList = frozenBoxService.findAllFrozenBoxByTranshipId(id);
        res.setFrozenBoxDTOList(frozenBoxResponseList);

        return res;
    }

    /**
     * 初始化转运记录
     * @return
     */
    @Override
    public TranshipDTO initTranship() {
        return initTranship(null, null,null);
    }
    @Override
    public TranshipDTO initTranship(Long projectId, Long projectSiteId,Long stockOutApplyId) {
        Tranship tranship = new Tranship();
        tranship.setStatus(Constants.VALID);
        String transhipCode = "";
        if(stockOutApplyId!=null){
            transhipCode = bankUtil.getUniqueID("AA");
        }else{
            transhipCode = bankUtil.getUniqueID("A");
        }
        tranship.setTranshipCode(transhipCode);
        tranship.setTranshipState(Constants.TRANSHIPE_IN_PENDING);
        tranship.setFrozenBoxNumber(0);
        tranship.setEmptyHoleNumber(0);
        tranship.setEmptyTubeNumber(0);
        tranship.setSampleNumber(0);
        tranship.setEffectiveSampleNumber(0);
        tranship.setReceiveType(Constants.RECEIVE_TYPE_PROJECT_SITE);
        Project project = new Project();
        if (projectId != null){
            project = projectRepository.findOne(projectId);
            tranship.setProject(project);
            tranship.setProjectCode(project.getProjectCode());
            tranship.setProjectName(project.getProjectName());
        }

        if (projectSiteId != null){
            ProjectSite projectSite = projectSiteRepository.findOne(projectSiteId);
            tranship.setProjectSite(projectSite);
            tranship.setProjectSiteCode(projectSite.getProjectSiteCode());
            tranship.setProjectSiteName(projectSite.getProjectSiteName());
        }
        if(stockOutApplyId != null){
            StockOutApply stockOutApply = stockOutApplyRepository.findOne(stockOutApplyId);
            if(stockOutApply == null){
                throw new BankServiceException("申请不存在！");
            }
            if(!stockOutApply.getStatus().equals(Constants.STOCK_OUT_APPROVED)){
                throw new BankServiceException("申请未批准！");
            }
            if(projectId == null){
                throw new BankServiceException("项目不能为空！");
            }
            StockOutApplyProject stockOutApplyProject = stockOutApplyProjectRepository.findByStockOutApplyIdAndProjectId(stockOutApply.getId(),projectId);
            if(stockOutApplyProject == null){
                throw new BankServiceException("该申请未关联"+project.getProjectCode()+"项目！");
            }
            tranship.setCheckTypeId(stockOutApply.getCheckTypeId());
            tranship.setDelegate(stockOutApply.getDelegate());
            tranship.setStockOutApply(stockOutApply);
            tranship.setApplyPersonName(stockOutApply.getApplyPersonName());
            tranship.setReceiveType(Constants.RECEIVE_TYPE_RETURN_BACK);
        }
        transhipRepository.save(tranship);
        return transhipMapper.transhipToTranshipDTO(tranship);
    }

    /**
     * 判断是否可以进行转运
     * @param transhipDTO
     * @return
     */
    public Boolean isCanTranship(TranshipDTO transhipDTO) {
        List<FrozenBoxDTO> frozenBoxDTOS = transhipDTO.getFrozenBoxDTOList();
        Boolean isCanTranship = true;
        //判断盒子位置有效性
        for(FrozenBoxDTO box:frozenBoxDTOS){
            Long equipmentId = box.getEquipmentId();
            Long areaId = box.getAreaId();
            Long supportRackId = box.getSupportRackId();
            String column = box.getColumnsInShelf();
            String row = box.getRowsInShelf();
            List<FrozenBoxDTO> frozenBoxDTOList =  frozenBoxService.findByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(equipmentId,areaId,supportRackId,column,row);
            for(FrozenBoxDTO b:frozenBoxDTOList){
                if(!b.getId().equals(box.getId())&&!b.getStatus().equals(Constants.FROZEN_BOX_INVALID)){
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

    /**
     * 作废转运记录
     * @param transhipCode
     * @param transhipDTO
     * @return
     */
    @Override
    public TranshipDTO invalidTranship(String transhipCode, TranshipDTO transhipDTO) {
        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("接收记录不存在！",transhipCode);
        }
        if(!tranship.getTranshipState().equals(Constants.TRANSHIPE_IN_PENDING)){
            throw new BankServiceException("接收已不在进行中的状态，不能作废！",transhipCode);
        }
        if(StringUtils.isEmpty(transhipDTO.getInvalidReason())){
            throw new BankServiceException("作废原因不能为空！",transhipCode);
        }
        //如果转运下有新接受的冻存盒不能作废
        Long countOfTranshipBox = transhipBoxRepository.countByTranshipIdAndStatusNotIn(tranship.getId()
                ,new ArrayList<String>(){{add(Constants.INVALID);add(Constants.FROZEN_BOX_DESTROY);}});
        if(countOfTranshipBox.intValue()>0){
            throw new BankServiceException("该接收记录内已经有接收的冻存盒，不能作废！",transhipCode);
        }
        tranship.setTranshipState(Constants.TRANSHIPE_IN_INVALID);
        transhipRepository.save(tranship);
        return transhipMapper.transhipToTranshipDTO(tranship);
    }

    @Override
    public AttachmentDTO saveAndUploadTranship(AttachmentDTO attachmentDTO, Long transhipId, MultipartFile file, HttpServletRequest request) {

        Tranship tranship = transhipRepository.findOne(transhipId);
        if(tranship == null){
            throw new BankServiceException("转运记录不存在！");
        }
        Attachment attachment = attachmentMapper.attachmentDTOToAttachment(attachmentDTO);
        attachment.businessId(transhipId).businessType(Constants.SAMPLE_HISTORY_TRANSHIP).status(Constants.VALID);
        String fileName= "";
        String filetype= "";
        if(!file.isEmpty()){
            int size = (int)(file.getSize());
            if(size>2*1048576){
                throw new BankServiceException("文件最大不能超过2M！");
            }
            try {
                    StockOutFiles bigFile = new StockOutFiles();
                    fileName=file.getOriginalFilename().split("\\.")[0];//文件名
                    filetype=file.getOriginalFilename().split("\\.")[1];//后缀
                    UUID uuid = UUID.randomUUID();
                    String uuidStr = uuid.toString();
                    uuidStr = uuidStr.replaceAll("-", "");
                    uuidStr="biobank"+uuidStr;
                    uuidStr+="."+filetype;
                    bigFile.setFileName(fileName);
                    // 文件保存路径
                    bigFile.setFilePath(uuidStr);

                    bigFile.setFiles(file.getBytes());
                    bigFile.setFilesContentType(null);
                    bigFile.setFileSize(size);
                    bigFile.setFileType(filetype);
                    bigFile.setStatus(Constants.VALID);
                    stockOutFilesRepository.save(bigFile);
                    attachment.setFileId1(bigFile.getId());
                    attachment.setFileName(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }else {
            throw  new BankServiceException("文件不能为空！");
        }

        if(!StringUtils.isEmpty(attachmentDTO.getSmallImage())){
            StockOutFiles smallFile = new StockOutFiles();
            String smallBase64 = attachmentDTO.getSmallImage();
            String smallFileData = smallBase64.substring(smallBase64.indexOf(",")+1);
            UUID uuid = UUID.randomUUID();
            String uuidStr = uuid.toString();
            uuidStr = uuidStr.replaceAll("-", "");
            uuidStr="biobank"+uuidStr;
            uuidStr+="."+filetype;
            // 接收base64文件字符串, 并对文件字符串进行解码
            byte[] smallFileByte = Base64.decode(smallFileData.getBytes());
            smallFile.setFileName(fileName);
            // 文件保存路径
            smallFile.setFilePath(uuidStr);
            smallFile.setFiles(smallFileByte);
            smallFile.setFilesContentType(null);
            smallFile.setFileSize((int)file.getSize());
            smallFile.setFileType(filetype);
            smallFile.setStatus(Constants.VALID);
            stockOutFilesRepository.save(smallFile);
            attachment.setFileId2(smallFile.getId());
        }
        if(StringUtils.isEmpty(attachment.getFileTitle())){
            attachment.setFileTitle(attachment.getFileName());
        }
        //保存附件
        attachmentRepository.save(attachment);
        attachmentDTO.setId(attachment.getId());
        return attachmentDTO;
    }

    /**
     * 初始化归还记录
     * @param projectId
     * @param stockOutApplyId
     * @return
     */
    @Override
    public TranshipDTO initReturnBack(Long projectId ,Long stockOutApplyId) {

        return initTranship(projectId,null,stockOutApplyId);
    }

    /**
     * 修改保存归还记录
     * @param transhipDTO
     * @return
     */
    @Override
    public TranshipDTO saveReturnBack(TranshipDTO transhipDTO) {
        log.debug("Request to save Tranship : {}", transhipDTO);
        Long transhipId = transhipDTO.getId();
        if(transhipId == null){
            throw new BankServiceException("归还记录不存在！",transhipDTO.toString());
        }
        Tranship oldTranship = transhipRepository.findOne(transhipDTO.getId());

        if(oldTranship!=null&&(oldTranship.getTranshipState().equals(Constants.TRANSHIPE_IN_STOCKED)
            || oldTranship.getTranshipState().equals(Constants.TRANSHIPE_IN_STOCKING))){

            throw new BankServiceException("归还已不在进行中状态，不能修改记录！",transhipDTO.toString());
        }
        if(transhipDTO.getStockOutApplyId()==null){
            throw new BankServiceException("申请ID不能为空！");
        }
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(transhipDTO.getStockOutApplyId());
        if(stockOutApply == null ||(stockOutApply!=null&&!stockOutApply.getStatus().equals(Constants.STOCK_OUT_APPROVED))){
            throw new BankServiceException("申请无效！");
        }
        if(transhipDTO.getCheckTypeId()!=null){
            CheckType checkType = checkTypeRepository.findByIdAndStatus(transhipDTO.getCheckTypeId(),Constants.VALID);
            if(checkType == null){
                throw new BankServiceException("检测类型不存在！");
            }
        }
        transhipDTO.setTranshipState(oldTranship.getTranshipState());
        transhipDTO.setStatus(oldTranship.getStatus());
        transhipDTO.setReceiveType(Constants.RECEIVE_TYPE_RETURN_BACK);
        return updateTranship(transhipDTO);
    }

    public TranshipDTO updateTranship(TranshipDTO transhipDTO) {
        if(transhipDTO == null ||(transhipDTO!=null&&transhipDTO.getId()==null)){
            return null;
        }
        if(transhipDTO.getTranshipDate() == null){
            transhipDTO.setTranshipDate(LocalDate.now());
        }
        Long receiverId = transhipDTO.getReceiverId();
        if(receiverId!=null){
            User user = userRepository.findOne(receiverId);
            String username = user!=null?(user.getLastName()+user.getFirstName()):null;
            transhipDTO.setReceiver(user!=null?username:transhipDTO.getReceiver());
        }
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByTranshipId(transhipDTO.getId());
        Equipment equipment = new Equipment();
        Area area = new Area();
        //接收样本的暂存位置，如果转运有了暂存位置，则盒子上的暂存位置也改成相应的位置
        if(transhipDTO.getTempEquipmentId()!=null){
            equipment = equipmentRepository.findOne(transhipDTO.getTempEquipmentId());
        }
        if(transhipDTO.getTempAreaId()!=null){
            area = areaRepository.findOne(transhipDTO.getTempAreaId());
        }
        List<FrozenBox> frozenBoxList = new ArrayList<>();
        List<TranshipBoxPosition> transhipBoxPositions = new ArrayList<>();
        for(TranshipBox box : transhipBoxes){
            FrozenBox frozenBox = box.getFrozenBox();
            if(transhipDTO.getTempEquipmentId()!=null){
                TranshipBoxPosition transhipBoxPosition = transhipBoxPositionRepository.findByTranshipBoxId(box.getId());
                transhipBoxPosition.equipmentCode(equipment!=null?equipment.getEquipmentCode():null).equipment(equipment)
                        .area(area).areaCode(area!=null?area.getAreaCode():null).supportRack(null).supportRackCode(null).columnsInShelf(null).rowsInShelf(null);
                box.equipmentCode(equipment!=null?equipment.getEquipmentCode():null).equipment(equipment)
                        .area(area).areaCode(area!=null?area.getAreaCode():null).supportRack(null).supportRackCode(null).columnsInShelf(null).rowsInShelf(null);
                if(frozenBox.getStatus().equals(Constants.FROZEN_BOX_NEW)){
                    frozenBox.equipmentCode(equipment!=null?equipment.getEquipmentCode():null).equipment(equipment)
                            .area(area).areaCode(area!=null?area.getAreaCode():null).supportRack(null).supportRackCode(null).columnsInShelf(null).rowsInShelf(null);
                    frozenBoxList.add(frozenBox);
                }
                transhipBoxPositions.add(transhipBoxPosition);
            }
        }
        frozenBoxRepository.save(frozenBoxList);
        transhipBoxRepository.save(transhipBoxes);
        transhipBoxPositionRepository.save(transhipBoxPositions);
        List<Long> boxIds = transhipBoxes.stream().map(s->s.getId()).collect(Collectors.toList());
        int countOfEmptyHole = 0;int countOfEmptyTube = 0;
        int countOfTube = 0;//有效样本数
        final  Integer[] countOfPeople = {0};//样本人份
        if(boxIds.size()>0){
            countOfEmptyHole = transhipTubeRepository.countByTranshipBoxIdsStrAndStatus(boxIds,Constants.FROZEN_TUBE_HOLE_EMPTY);
            countOfEmptyTube = transhipTubeRepository.countByTranshipBoxIdsStrAndStatus(boxIds,Constants.FROZEN_TUBE_EMPTY);
            countOfTube = transhipTubeRepository.countByTranshipBoxIdsStrAndStatus(boxIds,Constants.FROZEN_TUBE_NORMAL);
            //查询临时样本人份
//            List<Object[]> countOfTempSampleCodeGroupBySampleTempCode = frozenTubeRepository.countByFrozenBoxCodeStrAndGroupBySampleTempCode(boxIds);
            List<Object[]> countOfTempSampleCodeGroupBySampleTempCode = transhipTubeRepository.countByTranshipBoxIdsAndGroupBySampleTempCode(boxIds);
            //查询扫码后的样本人份
//            List<Object[]> countOfSampleCodeGroupBySampleCode = frozenTubeRepository.countByFrozenBoxCodeStrAndGroupBySampleCode(boxIds);
            List<Object[]> countOfSampleCodeGroupBySampleCode = transhipTubeRepository.countByTranshipBoxIdsAndGroupBySampleCode(boxIds);
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
        //判断是否有转运盒，如果有转运盒，项目的编码不能变，如果没有转运盒，项目编码可以改变；
        if(frozenBoxList.size()==0 && transhipDTO.getProjectId()!=null){
            Project project = projectRepository.findOne(transhipDTO.getProjectId());
            transhipDTO.setProjectCode(project!=null?project.getProjectCode():new String(""));
            transhipDTO.setProjectName(project!=null?project.getProjectName():new String(""));
        }

        transhipDTO.setSampleNumber(transhipDTO.getSampleNumber()!=null&& transhipDTO.getSampleNumber()!=0?transhipDTO.getSampleNumber():countOfPeople[0]);
        transhipDTO.setFrozenBoxNumber(transhipDTO.getFrozenBoxNumber()!=null && transhipDTO.getFrozenBoxNumber()!=0?transhipDTO.getFrozenBoxNumber():frozenBoxList.size());
        transhipDTO.setEmptyHoleNumber(transhipDTO.getEmptyHoleNumber()!=null && transhipDTO.getEmptyHoleNumber()!=0?transhipDTO.getEmptyHoleNumber():countOfEmptyHole);
        transhipDTO.setEmptyTubeNumber(transhipDTO.getEmptyTubeNumber()!=null && transhipDTO.getEmptyTubeNumber()!=0?transhipDTO.getEmptyTubeNumber():countOfEmptyTube);
        transhipDTO.setEffectiveSampleNumber(transhipDTO.getEffectiveSampleNumber()!=null && transhipDTO.getEffectiveSampleNumber()!=0?transhipDTO.getEffectiveSampleNumber():countOfTube);
        Tranship tranship = transhipMapper.transhipDTOToTranship(transhipDTO);
        transhipRepository.save(tranship);
        return transhipDTO;
    }

    /**
     * 接收完成
     * @param returnBackCode
     * @param transhipToStockInDTO
     * @return
     */
    @Override
    public StockInForDataDetail completedReturnBack(String returnBackCode, TranshipToStockInDTO transhipToStockInDTO) {
        return completedReceiveBox(returnBackCode,transhipToStockInDTO,Constants.RECEIVE_TYPE_RETURN_BACK );
    }

    /**
     * 接收完成的具体实现
     * 当接收类型为归还时，更改实际样本数据
     * @param transhipCode
     * @param transhipToStockInDTO
     * @param receiveType
     * @return
     */
    private StockInForDataDetail completedReceiveBox(String transhipCode, TranshipToStockInDTO transhipToStockInDTO, String receiveType) {
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        stockInForDataDetail.setTranshipCode(transhipCode);
        //验证运单是否存在，验证接收人，运单号
        Tranship  tranship = checkTranshipForCompletedReceive(transhipCode,transhipToStockInDTO,receiveType);

        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByTranshipId(tranship.getId());
        if(transhipBoxes.size()==0){
            throw new BankServiceException("此次接收没有冻存盒数据！",transhipCode);
        }
        switch (receiveType){
            case Constants.RECEIVE_TYPE_PROJECT_SITE:
                updateFrozenBoxAndTubeForCompleteReceiveFromProjectStie(transhipBoxes,tranship.getId());break;
            case Constants.RECEIVE_TYPE_RETURN_BACK:
                updateFrozenBoxAndTubeForCompleteReturnBack(transhipBoxes);break;
            default:break;
        }
        stockInForDataDetail.setProjectCode(tranship.getProjectCode());
        stockInForDataDetail.setProjectSiteCode(tranship.getProjectSiteCode());
        stockInForDataDetail.setReceiver(tranship.getReceiver());
        stockInForDataDetail.setReceiveDate(tranship.getReceiveDate());
        stockInForDataDetail.setStatus(Constants.TRANSHIPE_IN_COMPLETE);
        stockInForDataDetail.setId(tranship.getId());
        return stockInForDataDetail;
    }

    /**
     * 接收类型为项目点时，接收完成，仅更改转运样本和转运冻存盒，实际样本和冻存盒的状态
     * @param transhipBoxes
     * @param transhipId
     */
    public void updateFrozenBoxAndTubeForCompleteReceiveFromProjectStie(List<TranshipBox> transhipBoxes,Long transhipId) {
        List<String> frozenBoxCodes = transhipBoxes.stream().map(s->s.getFrozenBoxCode()).collect(Collectors.toList());
        //转运盒的状态更改为转运完成
        transhipBoxRepository.updateStatusByTranshipId(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,transhipId);
        //冻存盒的状态更改为转运完成
        frozenBoxRepository.updateStatusByFrozenBoxCodes(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,frozenBoxCodes);
        //冻存管的状态更改为转运完成
        frozenTubeRepository.updateFrozenTubeStateByFrozenBoxCodes(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,frozenBoxCodes);
        //转运冻存管的状态为转运完成
        transhipTubeRepository.updateFrozenTubeStateByFrozenBoxCodesAndTranshipCode(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,frozenBoxCodes);
    }

    /**
     * 当接收类型为归还时，接收完成，在更改转运样本和转运冻存盒时，同时更改实际样本和冻存盒的所有数据
     * @param transhipBoxes
     */
    public void updateFrozenBoxAndTubeForCompleteReturnBack(List<TranshipBox> transhipBoxes) {
        List<FrozenBox> frozenBoxes = new ArrayList<>();
        for(TranshipBox transhipBox : transhipBoxes){
            transhipBox.setStatus(Constants.FROZEN_BOX_TRANSHIP_COMPLETE);
            FrozenBox frozenBox = transhipBoxMapper.transhipBoxDTOToFrozenBox(transhipBox);
            frozenBoxes.add(frozenBox);

            List<TranshipTube> transhipTubes = transhipTubeRepository.findByTranshipBoxIdAndStatusNotIn(transhipBox.getId(),
                    new ArrayList<String>(){{
                        add(Constants.FROZEN_BOX_INVALID);
                        add(Constants.INVALID);
                    }});
            List<FrozenTube> frozenTubes = new ArrayList<FrozenTube>();
            for (TranshipTube transhipTube : transhipTubes) {
                transhipTube.setFrozenTubeState(Constants.FROZEN_BOX_TRANSHIP_COMPLETE);
                FrozenTube frozenTube = transhipTubeMapper.transhipTubeToFrozenTube(transhipTube);
                frozenTubes.add(frozenTube);
            }
            transhipTubeRepository.save(transhipTubes);
            frozenTubeRepository.save(frozenTubes);
        }
        transhipBoxRepository.save(transhipBoxes);
        frozenBoxRepository.save(frozenBoxes);
    }

    /**
     * 接收完成的验证
     * @param transhipCode
     * @param transhipToStockInDTO
     * @param receiveType
     * @return
     */
    public Tranship checkTranshipForCompletedReceive(String transhipCode, TranshipToStockInDTO transhipToStockInDTO,String receiveType) {
        if(transhipCode == null){
            throw new BankServiceException("编码不能为空！",transhipCode);
        }
        if(transhipToStockInDTO.getReceiverId()==null){
            throw new BankServiceException("接收人不能为空！");
        }
        LocalDate receiveDate = transhipToStockInDTO.getReceiveDate();
        String password = transhipToStockInDTO.getPassword();
        User user = userRepository.findOne(transhipToStockInDTO.getReceiverId());
        if(user==null){
            throw new BankServiceException("接收人不存在！");
        }

        userService.isCorrectUser(user.getLogin(),password);

        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("接收记录不存在！",transhipCode);
        }
        if(receiveType.equals(Constants.RECEIVE_TYPE_PROJECT_SITE)){
            if(tranship.getTrackNumber()==null||tranship.getTrackNumber()==""){
                throw new BankServiceException("运单号不能为空！",tranship.toString());
            }
        }
        int number = stockInRepository.countByTranshipCode(transhipCode);
        if(number>0){
            throw new BankServiceException("此次接收已经在执行入库！",transhipCode);
        }
        //修改转运表中数据状态为转运完成
        tranship.setTranshipState(Constants.TRANSHIPE_IN_COMPLETE);
        tranship.setReceiverId(user!=null?user.getId():null);
        tranship.setReceiver(user.getLastName()+user.getFirstName());
        tranship.setReceiveDate(receiveDate);
        transhipRepository.save(tranship);
        return tranship;
    }
}
