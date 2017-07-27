package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.StockInForDataDetail;
import org.fwoxford.service.dto.response.StockInForDataTableEntity;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.StockInMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * Service Implementation for managing StockIn.
 */
@Service
@Transactional
public class StockInServiceImpl implements StockInService {

    private final Logger log = LoggerFactory.getLogger(StockInServiceImpl.class);

    private final StockInRepository stockInRepository;

    private final StockInMapper stockInMapper;

    private final StockInRepositries stockInRepositries;

    @Autowired
    private TranshipService transhipService;
    @Autowired
    private TranshipRepository transhipRepository;
    @Autowired
    private FrozenBoxRepository frozenBoxRepository;
    @Autowired
    private FrozenBoxMapper frozenBoxMapper;
    @Autowired
    private TranshipBoxRepository transhipBoxRepository;
    @Autowired
    private StockInBoxRepository stockInBoxRepository;
    @Autowired
    private FrozenTubeRepository frozenTubeRepository;
    @Autowired
    private StockInTubeRepository stockInTubeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StockInTubeService stockInTubeService;
    @Autowired
    private StockInBoxPositionRepository stockInBoxPositionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectSiteRepository projectSiteRepository;
    @Autowired
    private BankUtil bankUtil;
    @Autowired
    private TranshipStockInRepository transhipStockInRepository;
    @Autowired
    private StockInTranshipBoxRepository stockInTranshipBoxRepository;
    @Autowired
    private TranshipTubeRepository transhipTubeRepository;

    public StockInServiceImpl(StockInRepository stockInRepository,
                              StockInMapper stockInMapper,
                              StockInRepositries stockInRepositries) {
        this.stockInRepository = stockInRepository;
        this.stockInMapper = stockInMapper;
        this.stockInRepositries = stockInRepositries;
    }

    /**
     * Save a stockIn.
     *
     * @param stockInDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockInDTO save(StockInDTO stockInDTO) {
        log.debug("Request to save StockIn : {}", stockInDTO);
        if(stockInDTO.getId()==null){
            throw new BankServiceException("入库ID不能为空！",stockInDTO.toString());
        }
        StockIn stockIn = stockInRepository.findOne(stockInDTO.getId());
        User user1 = userRepository.findByLogin(stockInDTO.getStoreKeeper1());
        User user2 = userRepository.findByLogin(stockInDTO.getStoreKeeper2());
        stockIn.setStoreKeeper1(stockInDTO.getStoreKeeper1());
        stockIn.setStoreKeeper2(stockInDTO.getStoreKeeper2());
        stockIn.setStockInDate(stockInDTO.getStockInDate());
        stockIn.setStoreKeeperId1(user1!=null?user1.getId():stockInDTO.getStoreKeeperId1());
        stockIn.setStoreKeeperId2(user2!=null?user2.getId():stockInDTO.getStoreKeeperId2());
        stockIn = stockInRepository.save(stockIn);
        StockInDTO result = stockInMapper.stockInToStockInDTO(stockIn);
        return result;
    }

    /**
     *  Get all the stockIns.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockInDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockIns");
        Page<StockIn> result = stockInRepository.findAll(pageable);
        return result.map(stockIn -> stockInMapper.stockInToStockInDTO(stockIn));
    }

    /**
     *  Get one stockIn by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockInDTO findOne(Long id) {
        log.debug("Request to get StockIn : {}", id);
        StockIn stockIn = stockInRepository.findOne(id);
        StockInDTO stockInDTO = stockInMapper.stockInToStockInDTO(stockIn);
        return stockInDTO;
    }

    /**
     *  Delete the  stockIn by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockIn : {}", id);
        stockInRepository.delete(id);
    }
    /**
     * 入库保存
     * @param transhipCode
     * @return
     */
    @Override
    public StockInForDataDetail saveStockIns(String transhipCode, TranshipToStockInDTO transhipToStockInDTO) {
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
        TranshipByIdResponse transhipRes = transhipService.findTranshipAndFrozenBox(tranship.getId());
        List<FrozenBoxDTO> frozenBoxDTOList =  transhipRes.getFrozenBoxDTOList();
        if(frozenBoxDTOList.size()==0){
            throw new BankServiceException("此次转运没有冻存盒数据！",transhipCode);
        }
        //修改转运表中数据状态为待入库
        tranship.setTranshipState(Constants.TRANSHIPE_IN_STOCKING);
        User user = userRepository.findByLogin(receiver);
        tranship.setReceiverId(user!=null?user.getId():null);
        tranship.setReceiver(receiver);
        tranship.setReceiveDate(receiveDate);
        transhipRepository.save(tranship);

        stockInForDataDetail.setProjectCode(tranship.getProjectCode());
        stockInForDataDetail.setProjectSiteCode(tranship.getProjectSiteCode());
        stockInForDataDetail.setReceiver(tranship.getReceiver());
        stockInForDataDetail.setReceiveDate(tranship.getReceiveDate());
        stockInForDataDetail.setStatus(Constants.STOCK_IN_PENDING);

        //保存入库记录，状态为进行中
        StockInDTO stockInDTO = createStockInDTO(tranship);
        StockIn stockIn = stockInRepository.save(stockInMapper.stockInDTOToStockIn(stockInDTO));
        stockInForDataDetail.setId(stockIn.getId());
        stockInForDataDetail.setStockInCode(stockIn.getStockInCode());
        // 修改盒子状态，转运盒子状态
        for(FrozenBoxDTO boxDTO: frozenBoxDTOList){
            boxDTO.setStatus(Constants.FROZEN_BOX_STOCKING);
            FrozenBox box = frozenBoxMapper.frozenBoxDTOToFrozenBox(boxDTO);
            frozenBoxRepository.save(box);

            TranshipBox transhipBox = transhipBoxRepository.findByTranshipIdAndFrozenBoxId(tranship.getId(),boxDTO.getId());
            transhipBox.setStatus(Constants.FROZEN_BOX_STOCKING);
            transhipBoxRepository.save(transhipBox);
            //保存入库盒子
            StockInBox stockInBox = createStockInBox(box,stockIn);
            stockInBoxRepository.save(stockInBox);
            //保存入库管子
            stockInTubeService.saveStockInTube(stockInBox);
        }
        return stockInForDataDetail;
    }
    @Override
    public DataTablesOutput<StockInForDataTableEntity> findStockIn(DataTablesInput input) {
        //获取入库列表
        DataTablesOutput<StockInForDataTableEntity> stockInDataTablesOutput =  stockInRepositries.findAll(input);
        return stockInDataTablesOutput;
    }


    public StockInBox createStockInBox(FrozenBox box, StockIn stockIn) {
        StockInBox inBox = new StockInBox();
        inBox.setEquipmentCode(box.getEquipmentCode());
        inBox.setEquipment(box.getEquipment());
        inBox.setArea(box.getArea());
        inBox.setAreaCode(box.getAreaCode());
        inBox.setSupportRack(box.getSupportRack());
        inBox.setSupportRackCode(box.getSupportRackCode());
        inBox.setRowsInShelf(box.getRowsInShelf());
        inBox.setColumnsInShelf(box.getColumnsInShelf());
        inBox.setFrozenBoxCode(box.getFrozenBoxCode());
        inBox.setStockIn(stockIn);
        inBox.setStockInCode(stockIn.getStockInCode());
        Long countOfSample = frozenTubeRepository.countFrozenTubeListByBoxCode(box.getFrozenBoxCode());
        inBox.setCountOfSample(countOfSample.intValue());
        inBox.setMemo(box.getMemo());
        inBox.setStatus(box.getStatus());
        inBox.setFrozenBox(box);
        inBox.sampleTypeCode(box.getSampleTypeCode()).sampleType(box.getSampleType()).sampleTypeName(box.getSampleTypeName())
            .sampleClassification(box.getSampleClassification())
            .sampleClassificationCode(box.getSampleClassification()!=null?box.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(box.getSampleClassification()!=null?box.getSampleClassification().getSampleClassificationName():null)
            .dislocationNumber(box.getDislocationNumber()).emptyHoleNumber(box.getEmptyHoleNumber()).emptyTubeNumber(box.getEmptyTubeNumber())
            .frozenBoxType(box.getFrozenBoxType()).frozenBoxTypeCode(box.getFrozenBoxTypeCode()).frozenBoxTypeColumns(box.getFrozenBoxTypeColumns())
            .frozenBoxTypeRows(box.getFrozenBoxTypeRows()).isRealData(box.getIsRealData()).isSplit(box.getIsSplit()).project(box.getProject())
            .projectCode(box.getProjectCode()).projectName(box.getProjectName()).projectSite(box.getProjectSite()).projectSiteCode(box.getProjectSiteCode())
            .projectSiteName(box.getProjectSiteName());
        return inBox;
    }

    public StockInDTO createStockInDTO(Tranship tranship) {
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setStockInCode(bankUtil.getUniqueID("B"));
        if(tranship.getProject()==null||tranship.getProjectSite()==null){
            throw new BankServiceException("项目信息不完整！",tranship.toString());
        }
        stockInDTO.setProjectId(tranship.getProject().getId());
        stockInDTO.setStatus(Constants.STOCK_IN_PENDING);
        stockInDTO.setProjectSiteCode(tranship.getProjectSiteCode());
        stockInDTO.setProjectCode(tranship.getProjectCode());
        stockInDTO.setProjectSiteId(tranship.getProjectSite().getId());
        stockInDTO.setReceiveId(tranship.getReceiverId());
        stockInDTO.setReceiveDate(tranship.getReceiveDate());
        stockInDTO.setReceiveName(tranship.getReceiver());
        stockInDTO.setCountOfSample(tranship.getEffectiveSampleNumber());
        stockInDTO.setSignId(null);
        stockInDTO.setSignDate(null);
        stockInDTO.setSignName("");
        stockInDTO.setStockInDate(null);
        stockInDTO.setStoreKeeperId1(null);
        stockInDTO.setStoreKeeper1("");
        stockInDTO.setStoreKeeperId2(null);
        stockInDTO.setStoreKeeper2("");
        stockInDTO.setStockInType(Constants.STORANGE_IN_TYPE_1ST);
        stockInDTO.setMemo("");
        stockInDTO.setTranshipId(tranship.getId());
        return stockInDTO;
    }

    /**
     * 入库完成
     * @param stockInCode
     * @return
     */
    @Override
    public StockInForDataDetail completedStockIn(String stockInCode,StockInCompleteDTO stockInCompleteDTO) {
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("该入库记录不存在！",stockInCode);
        }
        //修改入库
        stockIn.setStatus(Constants.STOCK_IN_COMPLETE);
        String loginName1 = stockInCompleteDTO.getLoginName1();
        String loginName2 = stockInCompleteDTO.getLoginName2();
        String password1 = stockInCompleteDTO.getPassword1();
        String password2 = stockInCompleteDTO.getPassword2();
        LocalDate stockInDate = stockInCompleteDTO.getStockInDate();
        if(loginName1 .equals(loginName2)){
            throw new BankServiceException("两个入库员不能为用一个人！");
        }
        if(loginName1!=null&&password1!=null){
            userService.isCorrectUser(loginName1,password1);
        }
        if(loginName2!=null&&password2!=null){
            userService.isCorrectUser(loginName2,password2);
        }
        User user1 = userRepository.findByLogin(loginName1);
        User user2 = userRepository.findByLogin(loginName2);
        stockIn.setStoreKeeperId1(user1!=null?user1.getId():null);
        stockIn.setStoreKeeperId2(user2!=null?user2.getId():null);
        stockIn.setStoreKeeper1(loginName1);
        stockIn.setStoreKeeper2(loginName2);
        stockIn.setStockInDate(stockInDate);
        List<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxByStockInCode(stockInCode);
        //已上架的冻存盒列表
        List<StockInBox> stockInBoxListForOnShelf = new ArrayList<StockInBox>();
        //未上架的冻存盒列表
        List<StockInBox> stockInBoxListForUnOnShelf = new ArrayList<StockInBox>();
        for(StockInBox box: stockInBoxes){
            if(box.getStatus().equals(Constants.FROZEN_BOX_STOCKING)){
                stockInBoxListForUnOnShelf.add(box);
            }else if (box.getStatus().equals(Constants.FROZEN_BOX_PUT_SHELVES)){
                stockInBoxListForOnShelf.add(box);
            }
        }
        if(stockInBoxListForOnShelf.size()==0){
            throw new BankServiceException("该入库单下不含有已上架的冻存盒！");
        }
        //修改盒子
        int countOfSampleIn = 0;
        for(StockInBox box: stockInBoxListForOnShelf){
            countOfSampleIn+=box.getCountOfSample();
            frozenBoxRepository.updateStatusByFrozenBoxCode(box.getFrozenBoxCode(),Constants.FROZEN_BOX_STOCKED);
            //修改入库盒子
            box.setStatus(Constants.FROZEN_BOX_STOCKED);
            stockInBoxRepository.save(box);
            //保存入库盒子位置
            StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
            stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(box.getMemo())
                .equipment(box.getEquipment()).equipmentCode(box.getEquipmentCode())
                .area(box.getArea()).areaCode(box.getAreaCode())
                .supportRack(box.getSupportRack()).supportRackCode(box.getSupportRackCode())
                .columnsInShelf(box.getColumnsInShelf()).rowsInShelf(box.getRowsInShelf())
                .stockInBox(box);
            stockInBoxPositionRepository.save(stockInBoxPosition);
            //保存入库管子
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(box.getFrozenBoxCode());
            for(FrozenTube tube :frozenTubeList){
                tube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKED);
                frozenTubeRepository.saveAndFlush(tube);
                StockInTube stockInTube = new StockInTube();
                stockInTube.status(Constants.STOCK_IN_TUBE_COMPELETE).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(box).errorType(tube.getErrorType())
                    .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                    .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                    .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                    .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                    .sampleClassificationCode(tube.getSampleClassification()!=null?tube.getSampleClassification().getSampleClassificationCode():null)
                    .sampleClassificationName(tube.getSampleClassification()!=null?tube.getSampleClassification().getSampleClassificationName():null)
                    .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                    .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                    .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                stockInTubeRepository.save(stockInTube);
            }
        }
        stockIn.setCountOfSample(countOfSampleIn);
        stockInRepository.save(stockIn);
        //含有待入库的冻存盒开始拆单
        StockIn stockInNew = new StockIn();
        if(stockInBoxListForUnOnShelf.size()>0){
            //创建入库单
            BeanUtils.copyProperties(stockIn,stockInNew);
            stockInNew.setId(null);
            stockInNew.stockInCode(bankUtil.getUniqueID("B")).status(Constants.STOCK_IN_PENDING)
                .parentStockInId(stockIn.getId()).countOfSample(0).stockInDate(null).storeKeeper1(null).storeKeeper2(null)
                .storeKeeperId1(null).storeKeeperId2(null);
            stockInRepository.save(stockInNew);
            int  countOfSample = 0;
            for(StockInBox stockInBox :stockInBoxListForUnOnShelf){
                stockInBox.stockIn(stockInNew).stockInCode(stockInNew.getStockInCode());
                stockInBoxRepository.save(stockInBox);
                countOfSample+=stockInBox.getCountOfSample();
            }
            stockInNew.setCountOfSample(countOfSample);
            stockInRepository.save(stockInNew);
        }
        //查询出入库单下的转运盒
        List<String> transhipCodes = new ArrayList<String>();
        List<TranshipStockIn> transhipStockIns = transhipStockInRepository.findByStockInCode(stockInCode);
        for(TranshipStockIn transhipStockIn :transhipStockIns){
            String transhipCode = transhipStockIn.getTranshipCode();
            if(transhipCodes.contains(transhipCode)){
                continue;
            }else{
                transhipCodes.add(transhipCode);
            }
            //查询此次转运内已入库完成的样本量
            Long countInStock = transhipTubeRepository.countUnStockInTubeByTranshipCodeAndStatus(transhipCode,Constants.FROZEN_BOX_STOCKED);
            //查询此次转运内待入库的样本量
            Long countUnStock = transhipTubeRepository.countUnStockInTubeByTranshipCodeAndStatus(transhipCode,Constants.FROZEN_BOX_STOCKING);
            if(countUnStock.intValue()==0){//转运全部都入库完成
                transhipRepository.updateTranshipStateById(transhipStockIn.getTranship().getId(),Constants.TRANSHIPE_IN_STOCKED);
            }else{
                if(countInStock.intValue() > 0){
                    TranshipStockIn transhipStockIn1 = new TranshipStockIn();
                    BeanUtils.copyProperties(transhipStockIn,transhipStockIn1);
                    transhipStockIn1.setId(null);
                    transhipStockIn1.setStockIn(stockInNew);
                    transhipStockIn1.setStockInCode(stockInNew.getStockInCode());
                    transhipStockInRepository.save(transhipStockIn1);
                }else if(countInStock.intValue() == 0){
                    transhipStockIn.setStockIn(stockInNew);
                    transhipStockIn.setStockInCode(stockInNew.getStockInCode());
                    transhipStockInRepository.save(transhipStockIn);
                }

            }
        }
        return stockInMapper.stockInToStockInDetail(stockIn);
    }

    @Override
    public StockInDTO getStockInById(Long id) {
        StockIn stockIn = stockInRepository.findOne(id);
        StockInDTO stockInForDataDetail = stockInMapper.stockInToStockInDTO(stockIn);
        List<StockInTranshipBox> transhipStockIns = stockInTranshipBoxRepository.findByStockInCode(stockIn.getStockInCode());
        List<String> transhipCodes = new ArrayList<String>();
        List<String> projectSiteCode = new ArrayList<String>();
        for(StockInTranshipBox transhipStockIn:transhipStockIns){
            if(!transhipCodes.contains(transhipStockIn.getTranshipCode())){
                transhipCodes.add(transhipStockIn.getTranshipCode());
            }
            if(!projectSiteCode.contains(transhipStockIn.getTranshipBox().getProjectSiteCode())){
                projectSiteCode.add(transhipStockIn.getTranshipBox().getProjectSiteCode());
            }
        }
        stockInForDataDetail.setTranshipCode(String.join(", ",transhipCodes));
        stockInForDataDetail.setProjectSiteCode(String.join(", ",projectSiteCode));
        List<User> userList = userRepository.findAll();
            for(User u :userList){
                if(stockIn.getReceiveId()!=null&&stockIn.getReceiveId().equals(u.getId())){
                    stockInForDataDetail.setReceiver(u.getLastName()+u.getFirstName());
                }
            }
        return stockInForDataDetail;
    }

    @Override
    public StockInForDataDetail getStockInByTranshipCode(String transhipCode) {
        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship.getId() == null){
            throw new BankServiceException("转运记录不存在！",transhipCode);
        }
        StockIn stockIn = stockInRepository.findStockInByTranshipId(tranship.getId());
        return stockInMapper.stockInToStockInDetail(stockIn);
    }
    /**
     * 新增入库
     * @param stockInDTO
     * @return
     */
    @Override
    public StockInDTO createStockIn(StockInDTO stockInDTO) {
        log.debug("Request to save StockIn : {}", stockInDTO);
        if(stockInDTO.getProjectId() == null){
            throw new BankServiceException("项目ID不能为空！");
        }
        StockIn stockIn = stockInMapper.stockInDTOToStockIn(stockInDTO);
        stockIn.setStatus(Constants.STOCK_IN_PENDING);
        stockIn.setStockInType(Constants.STORANGE_IN_TYPE_1ST);
        stockIn.setCountOfSample(0);
        Project project = projectRepository.findOne(stockInDTO.getProjectId());
        if(project == null){
            throw new BankServiceException("项目不存在！");
        }
        if(stockInDTO.getProjectSiteId() != null){
            ProjectSite projectSite = projectSiteRepository.findOne(stockInDTO.getProjectSiteId());
            if(projectSite == null){
                throw new BankServiceException("项目点不存在！");
            }
            stockIn.setProjectSiteCode(projectSite.getProjectSiteCode());
        }
        stockIn.setProjectCode(project.getProjectCode());
        stockIn.setStockInCode(bankUtil.getUniqueID("B"));
        stockInRepository.save(stockIn);
        StockInDTO result = stockInMapper.stockInToStockInDTO(stockIn);
        return result;
    }
    /**
     * 编辑入库
     * @param stockInDTO
     * @return
     */
    @Override
    public StockInDTO updateStockIns(StockInDTO stockInDTO) {
        log.debug("Request to save StockIn : {}", stockInDTO);
        if(stockInDTO.getId()==null){
            throw new BankServiceException("入库ID不能为空！",stockInDTO.toString());
        }
        StockIn stockInOld = stockInRepository.findOne(stockInDTO.getId());
        Long countOfBox = stockInBoxRepository.countByStockInCode(stockInOld.getStockInCode());
        if(countOfBox.intValue()>0){
            if(stockInDTO.getProjectId() != stockInOld.getProject().getId()){
                throw new BankServiceException("该入库单下已经有冻存盒记录，不能修改项目！",stockInDTO.toString());
            }
        }
        StockIn stockIn = stockInMapper.stockInDTOToStockIn(stockInDTO);
        stockInRepository.save(stockIn);
        return stockInMapper.stockInToStockInDTO(stockIn);
    }

    @Override
    public StockInDTO createStockInByTranshipCodes(String transhipCode) {
        if(StringUtils.isEmpty(transhipCode)){
            throw new BankServiceException("请传入有效的转运编码！");
        }
        String[] transhipCodeStr = transhipCode.split(",");
        if(transhipCodeStr.length == 0){
            throw new BankServiceException("未选择转运记录！");
        }
        Project project =null;
        List<Tranship> tranships = new ArrayList<Tranship>();
        for(String code:transhipCodeStr){
            //判断转运是否完成，转运完成，才可以入库；
            Tranship tranship = transhipRepository.findByTranshipCode(code);
            if(tranship == null){
                throw new BankServiceException("转运编码为"+code+"的转运记录不存在！");
            }
            if(!tranship.getTranshipState().equals(Constants.TRANSHIPE_IN_COMPLETE)){
                throw new BankServiceException("转运编码为"+code+"的转运记录转运未完成，不能入库！");
            }
            if(tranship.getProject() == null){
                throw new BankServiceException("转运编码为"+code+"的转运记录未指定项目，不能入库！");
            }
            if(project != null &&
                (project.getProjectCode()!=tranship.getProjectCode()
                    &&!project.getProjectCode().equals(tranship.getProjectCode()))){
                throw new BankServiceException("转运编码为"+code+"的转运记录与其他转运记录项目不一致，不能同时入库！");
            }else{
                project = tranship.getProject();
            }
            tranships.add(tranship);
        }
        List<String> transhipCodeList = Arrays.asList(transhipCodeStr);
        Long count = frozenTubeRepository.countByTranshipCodes(transhipCodeList);
        //创建入库单
        StockIn stockIn = new StockIn();
        stockIn.stockInCode(bankUtil.getUniqueID("B")).status(Constants.STOCK_IN_PENDING)
            .receiveId(tranships.get(0).getReceiverId())
            .receiveDate(tranships.get(0).getReceiveDate())
            .receiveName(tranships.get(0).getReceiver())
            .stockInType(Constants.STORANGE_IN_TYPE_1ST).countOfSample(count.intValue()).project(project).projectCode(project.getProjectCode());
        stockInRepository.save(stockIn);

        for(Tranship tranship:tranships){
            //创建入库与转运的关系
            TranshipStockIn transhipStockIn = new TranshipStockIn();
            transhipStockIn.stockInCode(stockIn.getStockInCode()).stockIn(stockIn).transhipCode(tranship.getTranshipCode()).tranship(tranship).status(Constants.VALID);
            transhipStockInRepository.save(transhipStockIn);
            //转运状态为待入库
            tranship.setTranshipState(Constants.TRANSHIPE_IN_STOCKING);
            transhipRepository.save(tranship);
        }
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByTranshipCodes(transhipCodeList);
        List<String> frozenBoxCodes = new ArrayList<String>();
        for(TranshipBox transhipBox : transhipBoxes){
            frozenBoxCodes.add(transhipBox.getFrozenBoxCode());
            FrozenBox frozenBox = transhipBox.getFrozenBox();
            //冻存盒状态变为待入库
            frozenBox.setStatus(Constants.FROZEN_BOX_STOCKING);
            frozenBoxRepository.save(frozenBox);
            //保存入库冻存盒
            StockInBox stockInBox = new StockInBox();
            Long countOfSample = frozenTubeRepository.countFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
            stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName()).countOfSample(countOfSample.intValue()).status(frozenBox.getStatus()).stockIn(stockIn).stockInCode(stockIn.getStockInCode())
                .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBox(frozenBox);
            stockInBoxRepository.save(stockInBox);
            StockInTranshipBox stockInTranshipBox = new StockInTranshipBox()
                .transhipBox(transhipBox).stockIn(stockIn)
                .frozenBoxCode(frozenBox.getFrozenBoxCode())
                .stockInCode(stockIn.getStockInCode())
                .status(Constants.FROZEN_BOX_STOCKING)
                .transhipCode(transhipBox.getTranship().getTranshipCode());
            stockInTranshipBoxRepository.save(stockInTranshipBox);
        }
        //修改转运冻存管状态
        frozenTubeRepository.updateFrozenTubeStateByFrozenBoxCodes(Constants.FROZEN_BOX_STOCKING,frozenBoxCodes);
        return stockInMapper.stockInToStockInDTO(stockIn);
    }
}
