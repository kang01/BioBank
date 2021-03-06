package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.SampleCountByTypeForm;
import org.fwoxford.service.dto.response.StockInForDataDetail;
import org.fwoxford.service.dto.response.StockInForDataTableEntity;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.mapper.*;
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
import java.util.stream.Collectors;

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
    TranshipService transhipService;
    @Autowired
    TranshipRepository transhipRepository;
    @Autowired
    FrozenBoxRepository frozenBoxRepository;
    @Autowired
    FrozenBoxMapper frozenBoxMapper;
    @Autowired
    TranshipBoxRepository transhipBoxRepository;
    @Autowired
    StockInBoxRepository stockInBoxRepository;
    @Autowired
    FrozenTubeRepository frozenTubeRepository;
    @Autowired
    StockInTubeRepository stockInTubeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    StockInTubeService stockInTubeService;
    @Autowired
    StockInBoxPositionRepository stockInBoxPositionRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectSiteRepository projectSiteRepository;
    @Autowired
    BankUtil bankUtil;
    @Autowired
    TranshipStockInRepository transhipStockInRepository;
    @Autowired
    TranshipTubeRepository transhipTubeRepository;
    @Autowired
    FrozenBoxService frozenBoxService;
    @Autowired
    TranshipTubeMapper transhipTubeMapper;
    @Autowired
    TranshipBoxMapper transhipBoxMapper;
    @Autowired
    StockInTubeMapper stockInTubeMapper;
    @Autowired
    StockInBoxMapper stockInBoxMapper;
    @Autowired
    FrozenTubeMapper frozenTubeMapper;

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
            StockInBox stockInBox = stockInBoxMapper.frozenBoxToStockInBox(box,stockIn,null);
            Long countOfSample = frozenTubeRepository.countFrozenTubeListByBoxCode(box.getFrozenBoxCode());
            stockInBox.setCountOfSample(countOfSample.intValue());
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
     *
     *  拆单时，已经入库的是新单子，未入库的是原单子
     * @param stockInCode
     * @return
     */
    @Override
    public StockInForDataDetail completedStockIn(String stockInCode,StockInCompleteDTO stockInCompleteDTO) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("该入库记录不存在！",stockInCode);
        }
        //修改入库
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
        //含有待入库的冻存盒开始拆单
        StockIn stockInNew = new StockIn();
        if(stockInBoxListForUnOnShelf.size()>0){
            //创建入库单
            BeanUtils.copyProperties(stockIn,stockInNew);
            stockInNew.setId(null);
            stockInNew.stockInCode(bankUtil.getUniqueID("B")).status(Constants.STOCK_IN_COMPLETE)
                .parentStockInId(stockIn.getId()).countOfSample(0).stockInDate(stockInDate).storeKeeper1(loginName1).storeKeeper2(loginName2)
                .storeKeeperId1(user1!=null?user1.getId():null).storeKeeperId2(user2!=null?user2.getId():null);
            stockInRepository.save(stockInNew);
        }else{
            stockIn.setStoreKeeperId1(user1!=null?user1.getId():null);
            stockIn.setStoreKeeperId2(user2!=null?user2.getId():null);
            stockIn.setStoreKeeper1(loginName1);
            stockIn.setStoreKeeper2(loginName2);
            stockIn.setStockInDate(stockInDate);
            stockIn.setStatus(Constants.STOCK_IN_COMPLETE);
            stockInNew = stockIn;
        }
        int  countOfSample = 0;
        if(stockInBoxListForOnShelf != null && stockInBoxListForOnShelf.size()>0){
            for(StockInBox stockInBox :stockInBoxListForOnShelf){
                //修改入库盒子
                stockInBox.stockIn(stockInNew).stockInCode(stockInNew.getStockInCode()).status(Constants.FROZEN_BOX_STOCKED);

                //保存入库盒子位置
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPositionRepository.save(stockInBoxPosition);
                //保存入库管子
                List<StockInTube> stockInTubes = stockInTubeRepository.findByStockInBoxId(stockInBox.getId());
                List<FrozenTube> frozenTubeList = new ArrayList<>();
                for(StockInTube stockInTube :stockInTubes){
                    if(!stockInTube.getFrozenTubeState().equals(Constants.FROZEN_BOX_PUT_SHELVES)){
                        continue;
                    }
                    stockInTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKED);
                    FrozenTube frozenTube = frozenTubeMapper.stockInTubeToFrozenTube(stockInTube);
                    frozenTubeList.add(frozenTube);
                }
                stockInTubeRepository.save(stockInTubes);
                frozenTubeRepository.save(frozenTubeList);
                countOfSample+=stockInBox.getCountOfSample();
            }

            List<Long> boxIds = stockInBoxListForOnShelf.stream().map(s -> {
                return s.getFrozenBox().getId();
            }).collect(Collectors.toList());
            List<Object[]> countSampleGroupByFrozenBoxId = new ArrayList<>();
            if(boxIds!=null&&boxIds.size()>0){
                countSampleGroupByFrozenBoxId = frozenTubeRepository.countGroupByFrozenBoxId(boxIds);
            }
            List<FrozenBox> frozenBoxList = new ArrayList<>();
            for(StockInBox box: stockInBoxListForOnShelf){
                FrozenBox frozenBox = box.getFrozenBox();
                Object[] obje = countSampleGroupByFrozenBoxId.stream().filter(s->Long.valueOf(s[0].toString()).equals(frozenBox.getId())).findFirst().orElse(null);
                Integer count = obje!=null?Integer.valueOf(obje[1].toString()):0;
                frozenBox.status(Constants.FROZEN_BOX_STOCKED).project(box.getProject()).projectName(box.getProjectName()).projectCode(box.getProjectCode())
                    .projectSite(box.getProjectSite()).projectSiteCode(box.getProjectSiteCode()).projectSiteName(box.getProjectSiteName()).countOfSample(count).lockFlag(Constants.FROZEN_BOX_UNLOCKED);
                frozenBoxList.add(frozenBox);
            }
            frozenBoxRepository.save(frozenBoxList);
            stockInNew.setCountOfSample(countOfSample);
            stockInRepository.save(stockInNew);
            stockInBoxRepository.save(stockInBoxListForOnShelf);
        }
        //未上架的冻存盒
        if(stockInBoxListForUnOnShelf!=null && stockInBoxListForUnOnShelf.size()>0){
            //修改盒子
            int countOfSampleIn = 0;
            List<Long> boxIds = stockInBoxListForUnOnShelf.stream().map(s -> {
                return s.getFrozenBox().getId();
            }).collect(Collectors.toList());
            List<Object[]> countSampleGroupByFrozenBoxId = new ArrayList<>();
            if(boxIds!=null&&boxIds.size()>0){
                countSampleGroupByFrozenBoxId = frozenTubeRepository.countGroupByFrozenBoxId(boxIds);
            }
            List<FrozenBox> frozenBoxList = new ArrayList<>();
            for(StockInBox box: stockInBoxListForUnOnShelf){
                countOfSampleIn+=box.getCountOfSample();
                FrozenBox frozenBox = box.getFrozenBox();
                Object[] obje = countSampleGroupByFrozenBoxId.stream().filter(s->Long.valueOf(s[0].toString()).equals(frozenBox.getId())).findFirst().orElse(null);
                Integer count = obje!=null?Integer.valueOf(obje[1].toString()):0;
                frozenBox.setCountOfSample(count);
                frozenBoxList.add(frozenBox);
            }
            stockIn.setCountOfSample(countOfSampleIn);
            stockInRepository.save(stockIn);
            frozenBoxRepository.save(frozenBoxList);
        }

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
        List<TranshipStockIn> transhipStockInList = transhipStockInRepository.findByStockInCode(stockIn.getStockInCode());
        List<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxByStockInCode(stockIn.getStockInCode());
        List<String> transhipCodes =transhipStockInList.stream().map(s->s.getTranshipCode()).collect(Collectors.toList());
        List<String> projectSiteCode = stockInBoxes.stream().map(s->s.getProjectSiteCode()).collect(Collectors.toList());
        stockInForDataDetail.setProjectName(stockIn.getProject()!=null?stockIn.getProject().getProjectName():null);
        stockInForDataDetail.setTranshipCode(String.join(", ",transhipCodes));
        stockInForDataDetail.setProjectSiteCode(String.join(", ",projectSiteCode));
        List<User> userList = userRepository.findAll();
        for(User u :userList){
            if(stockIn.getReceiveId()!=null&&stockIn.getReceiveId().equals(u.getId())){
                String name = (u.getLastName()!=null?u.getLastName():"")+(u.getFirstName()!=null?u.getFirstName():"");
                stockInForDataDetail.setReceiver(name);
            }

            if(stockIn.getStoreKeeper1()!=null&&stockIn.getStoreKeeperId1().equals(u.getId())){
                String name = (u.getLastName()!=null?u.getLastName():"")+(u.getFirstName()!=null?u.getFirstName():"");
                stockInForDataDetail.setStoreKeeper1(name);
            }

            if(stockIn.getStoreKeeper2()!=null&&stockIn.getStoreKeeperId2().equals(u.getId())){
                String name = (u.getLastName()!=null?u.getLastName():"")+(u.getFirstName()!=null?u.getFirstName():"");
                stockInForDataDetail.setStoreKeeper2(name);
            }
        }
        List<Object[]> alist = stockInRepository.countFrozenTubeGroupBySampleTypeAndClass(id);
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
        stockInForDataDetail.setSampleCountByTypeForms(sampleCountByTypeForms);
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

    /**
     * 根据多个转运创建入库单
     * @param transhipCode
     * @return
     */
    @Override
    public StockInDTO createStockInByTranshipCodes(String transhipCode) {
        if(StringUtils.isEmpty(transhipCode)){
            throw new BankServiceException("请传入有效的接收记录编码！");
        }
        String[] transhipCodeStr = transhipCode.split(",");
        if(transhipCodeStr.length == 0){
            throw new BankServiceException("未选择接收记录！");
        }
        Project project =null;
        List<Tranship> tranships = new ArrayList<Tranship>();
        for(String code:transhipCodeStr){
            //判断转运是否完成，转运完成，才可以入库；
            Tranship tranship = transhipRepository.findByTranshipCode(code);
            if(tranship == null){
                throw new BankServiceException("编码为"+code+"的接收记录不存在！");
            }
            if(!tranship.getTranshipState().equals(Constants.TRANSHIPE_IN_COMPLETE)){
                throw new BankServiceException("编码为"+code+"的接收记录转运未完成，不能入库！");
            }
            if(tranship.getProject() == null){
                throw new BankServiceException("编码为"+code+"的接收记录未指定项目，不能入库！");
            }
            if(project != null &&
                (project.getProjectCode()!=tranship.getProjectCode()
                    &&!project.getProjectCode().equals(tranship.getProjectCode()))){
                throw new BankServiceException("编码为"+code+"的接收记录与其他接收记录项目不一致，不能同时入库！");
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
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByTranshipCodesAndStatus(transhipCodeList);
        List<FrozenBox> frozenBoxes = new ArrayList<>();
        List<StockInBox> stockInBoxes = new ArrayList<StockInBox>();
        for(TranshipBox transhipBox : transhipBoxes){
            FrozenBox frozenBox = transhipBox.getFrozenBox();
            //冻存盒状态变为待入库
            frozenBox.setStatus(Constants.FROZEN_BOX_STOCKING);
            frozenBoxes.add(frozenBox);
            //保存入库冻存盒
            StockInBox stockInBox = new StockInBox();
            stockInBoxMapper.frozenBoxToStockInBox(frozenBox,stockIn,stockInBox);
            stockInBoxRepository.save(stockInBox);
            //保存入库管子
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
            List<StockInTube> stockInTubes = new ArrayList<StockInTube>();
            List<FrozenTube> frozenTubes = new ArrayList<FrozenTube>();
            for (FrozenTube tube : frozenTubeList) {
                if (!tube.getFrozenTubeState().equals(Constants.FROZEN_BOX_TRANSHIP_COMPLETE)) {
                    continue;
                }
                tube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKING);
                frozenTubes.add(tube);
                StockInTube stockInTube = stockInTubeMapper.frozenTubeToStockInTube(tube,stockInBox);
                stockInTubes.add(stockInTube);
            }
            frozenTubeRepository.save(frozenTubes);
            stockInTubeRepository.save(stockInTubes);
            stockInBox.countOfSample(stockInTubes.size());
            stockInBoxes.add(stockInBox);
        }
        frozenBoxRepository.save(frozenBoxes);
        stockInBoxRepository.save(stockInBoxes);
        return stockInMapper.stockInToStockInDTO(stockIn);
    }

    /**
     * 入库单作废
     * @param stockInCode
     * @return
     */
    @Override
    public StockInDTO invalidStockIn(String stockInCode) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("入库单不存在！");
        }
        if(!stockIn.getStatus().equals(Constants.STOCK_IN_PENDING)){
            throw new BankServiceException("入库单状态不在进行中，不能作废！");
        }
        //查询此次入库单是否有转运记录
        List<TranshipStockIn> transhipStockIn = transhipStockInRepository.findByStockInCode(stockInCode);
        if(transhipStockIn.size()>0){
            throw new BankServiceException("此次入库单含有转运记录，不能作废！");
        }
        stockIn.setStatus(Constants.STOCK_IN_INVALID);
        stockInRepository.save(stockIn);
        List<StockInBox> stockInBox = stockInBoxRepository.findStockInBoxByStockInCode(stockInCode);
        for(StockInBox s :stockInBox){
            s.setStatus(Constants.FROZEN_BOX_INVALID);
            //恢复盒子之前的状态，若历史信息最新一条是已入库，则是原盒入库，恢复为已入库，盒内新增样本置为无效，原盒样本为已出库，盒内原库存样本，更改为上一次的状态
            //若历史信息是已出库，则原盒已出库
            //若历史信息无更多历史，为新增的冻存盒，置为已作废
            String status = frozenBoxService.findFrozenBoxHistory(s.getFrozenBox().getId());
            s.getFrozenBox().setStatus(status);
            frozenBoxRepository.save(s.getFrozenBox());
            //恢复样本状态
           List<StockInTube> stockInTubes = stockInTubeRepository.findByStockInBoxId(s.getId());
           List<FrozenTube> frozenTubeList = new ArrayList<>();
           for(StockInTube stockInTube:stockInTubes){
               stockInTube.setStatus(Constants.INVALID);
               if(stockInTube.getFrozenTube().getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKING)){
                   stockInTube.getFrozenTube().setStatus(Constants.INVALID);
                   frozenTubeList.add(stockInTube.getFrozenTube());
               }
               stockInTube.setFrozenTubeState(status);
           }
           if(frozenTubeList.size()>0){
               frozenTubeRepository.save(frozenTubeList);
           }
           stockInTubeRepository.save(stockInTubes);
        }
        return stockInMapper.stockInToStockInDTO(stockIn);
    }
}
