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
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.TranshipMapper;
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
    @Autowired
    private FrozenBoxRepository frozenBoxRepository;
    @Autowired
    private FrozenTubeRepository frozenTubeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectSiteRepository projectSiteRepository;
    @Autowired
    private TranshipBoxRepository transhipBoxRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private BankUtil bankUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private  StockInRepository stockInRepository;
    @Autowired
    private TranshipTubeRepository transhipTubeRepository;
    @Autowired
    private StockOutFilesService stockOutFilesService;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private StockOutFilesRepository stockOutFilesRepository;

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
        Project project = projectRepository.findOne(transhipDTO.getProjectId());
        User user = userRepository.findByLogin(transhipDTO.getReceiver());
        transhipDTO.setReceiverId(user!=null?user.getId():null);
        transhipDTO.setReceiver(user!=null?user.getLogin():transhipDTO.getReceiver());

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAllFrozenBoxByTranshipId(transhipId);
        Equipment equipment = new Equipment();Area area = new Area();
        if(transhipDTO.getTempEquipmentId()!=null){
            equipment = equipmentRepository.findOne(transhipDTO.getTempEquipmentId());
        }
        if(transhipDTO.getTempAreaId()!=null){
            area = areaRepository.findOne(transhipDTO.getTempAreaId());
        }
        int countOfEmptyHole = 0;int countOfEmptyTube = 0;int countOfTube = 0;int countOfSample =0;
        List<Long> boxIds = new ArrayList<Long>();
        for(FrozenBox box : frozenBoxList){
            if(transhipDTO.getTempEquipmentId()!=null){
                box.setEquipment(equipment);
                box.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);

                box.setArea(area);
                box.setAreaCode(area!=null?area.getAreaCode():null);
            }
            frozenBoxRepository.save(box);
            boxIds.add(box.getId());
        }
        if(boxIds.size()>0){
            countOfEmptyHole = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_HOLE_EMPTY);
            countOfEmptyTube = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_EMPTY);
            countOfTube = frozenTubeRepository.countByFrozenBoxCodeStrAndStatus(boxIds,Constants.FROZEN_TUBE_NORMAL);
            countOfSample = frozenTubeRepository.countByFrozenBoxCodeStrAndGroupBySampleCode(boxIds);
        }
        transhipDTO.setSampleNumber(transhipDTO.getSampleNumber()!=null&& transhipDTO.getSampleNumber()!=0?transhipDTO.getSampleNumber():countOfSample);
        transhipDTO.setFrozenBoxNumber(transhipDTO.getFrozenBoxNumber()!=null && transhipDTO.getFrozenBoxNumber()!=0?transhipDTO.getFrozenBoxNumber():frozenBoxList.size());
        transhipDTO.setEmptyHoleNumber(transhipDTO.getEmptyHoleNumber()!=null && transhipDTO.getEmptyHoleNumber()!=0?transhipDTO.getEmptyHoleNumber():countOfEmptyHole);
        transhipDTO.setEmptyTubeNumber(transhipDTO.getEmptyTubeNumber()!=null && transhipDTO.getEmptyTubeNumber()!=0?transhipDTO.getEmptyTubeNumber():countOfEmptyTube);
        transhipDTO.setEffectiveSampleNumber(transhipDTO.getEffectiveSampleNumber()!=null && transhipDTO.getEffectiveSampleNumber()!=0?transhipDTO.getEffectiveSampleNumber():countOfTube);
        if(frozenBoxList.size()==0){
            transhipDTO.setProjectCode(project!=null?project.getProjectCode():new String(""));
            transhipDTO.setProjectName(project!=null?project.getProjectName():new String(""));
        }else{
            transhipDTO.setProjectCode(oldTranship.getProjectCode());
            transhipDTO.setProjectName(oldTranship.getProjectName());
            transhipDTO.setProjectId(oldTranship.getProject()!=null?oldTranship.getProject().getId():null);
        }
        if(transhipDTO.getProjectSiteId()!=null){
            ProjectSite projectSite = projectSiteRepository.findOne(transhipDTO.getProjectSiteId());
            transhipDTO.setProjectSiteCode(projectSite!=null?projectSite.getProjectSiteCode():new String(""));
            transhipDTO.setProjectSiteName(projectSite!=null?projectSite.getProjectSiteName():new String(""));
        }

        Tranship tranship = transhipMapper.transhipDTOToTranship(transhipDTO);
        tranship.setTranshipState(oldTranship.getTranshipState());
        tranship.setStatus(oldTranship.getStatus());
        tranship = transhipRepository.save(tranship);
        TranshipDTO result = transhipMapper.transhipToTranshipDTO(tranship);
        return result;
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
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        stockInForDataDetail.setTranshipCode(transhipCode);

        if(transhipCode == null){
            throw new BankServiceException("转运编码不能为空！",transhipCode);
        }
        String receiver = transhipToStockInDTO.getLogin();
        LocalDate receiveDate = transhipToStockInDTO.getReceiveDate();
        String password = transhipToStockInDTO.getPassword();
        userService.isCorrectUser(receiver,password);

        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("转运记录不存在！",transhipCode);
        }
        if(tranship.getTrackNumber()==null||tranship.getTrackNumber()==""){
            throw new BankServiceException("运单号不能为空！",tranship.toString());
        }
        int number = stockInRepository.countByTranshipCode(transhipCode);
        if(number>0){
            throw new BankServiceException("此次转运已经在执行入库！",transhipCode);
        }
        List<FrozenBox> frozenBoxList =  frozenBoxRepository.findAllFrozenBoxByTranshipId(tranship.getId());
        if(frozenBoxList.size()==0){
            throw new BankServiceException("此次转运没有冻存盒数据！",transhipCode);
        }
        List<String> frozenBoxCodes = new ArrayList<String>();
        for(FrozenBox frozenBox:frozenBoxList){
            frozenBoxCodes.add(frozenBox.getFrozenBoxCode());
        }
        //修改转运表中数据状态为转运完成
        tranship.setTranshipState(Constants.TRANSHIPE_IN_COMPLETE);
        User user = userRepository.findByLogin(receiver);
        tranship.setReceiverId(user!=null?user.getId():null);
        tranship.setReceiver(receiver);
        tranship.setReceiveDate(receiveDate);
        transhipRepository.save(tranship);

        //转运盒的状态更改为转运完成
        transhipBoxRepository.updateStatusByTranshipId(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,tranship.getId());
        //冻存盒的状态更改为转运完成
        frozenBoxRepository.updateStatusByFrozenBoxCodes(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,frozenBoxCodes);
//        冻存管的状态更改为转运完成
        frozenTubeRepository.updateFrozenTubeStateByFrozenBoxCodes(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,frozenBoxCodes);
        //转运冻存管的状态为转运完成
        transhipTubeRepository.updateFrozenTubeStateByFrozenBoxCodesAndTranshipCode(Constants.FROZEN_BOX_TRANSHIP_COMPLETE,frozenBoxCodes);
        stockInForDataDetail.setProjectCode(tranship.getProjectCode());
        stockInForDataDetail.setProjectSiteCode(tranship.getProjectSiteCode());
        stockInForDataDetail.setReceiver(tranship.getReceiver());
        stockInForDataDetail.setReceiveDate(tranship.getReceiveDate());
        stockInForDataDetail.setStatus(Constants.TRANSHIPE_IN_COMPLETE);
        stockInForDataDetail.setId(tranship.getId());
        return stockInForDataDetail;
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
        transhipDTO.setSampleCountByTypeForms(sampleCountByTypeForms);
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

        Tranship tranship = new Tranship();
        tranship.setStatus(Constants.VALID);
        tranship.setTranshipCode(bankUtil.getUniqueID("A"));
        tranship.setTranshipState(Constants.TRANSHIPE_IN_PENDING);
        tranship.setFrozenBoxNumber(0);
        tranship.setEmptyHoleNumber(0);
        tranship.setEmptyTubeNumber(0);
        tranship.setSampleNumber(0);
        tranship.setEffectiveSampleNumber(0);
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
     * @return
     */
    @Override
    public TranshipDTO invalidTranship(String transhipCode) {
        TranshipDTO transhipDTO = new TranshipDTO();
        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("转运记录不存在！",transhipCode);
        }
        if(!tranship.getTranshipState().equals(Constants.TRANSHIPE_IN_PENDING)){
            throw new BankServiceException("转运已不在进行中的状态，不能作废！",transhipCode);
        }

        tranship.setTranshipState(Constants.TRANSHIPE_IN_INVALID);
        transhipRepository.save(tranship);
        //更改转运盒
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByTranshipId(tranship.getId());
        for(TranshipBox tBox:transhipBoxes){
            tBox.setStatus(Constants.FROZEN_BOX_INVALID);
            transhipBoxRepository.save(tBox);
        }
        //更改冻存盒
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findAllFrozenBoxByTranshipId(tranship.getId());
        for(FrozenBox frozenBox :frozenBoxes){
            frozenBox.setStatus(Constants.FROZEN_BOX_INVALID);
            frozenBoxRepository.save(frozenBox);
        }
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
}
