package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.security.SecurityUtils;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.StockInService;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInForDataTable;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenBoxPositionMapper;
import org.fwoxford.service.mapper.SampleTypeMapper;
import org.fwoxford.service.mapper.StockInMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.mapping.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private FrozenBoxPositionRepository frozenBoxPositionRepository;

    @Autowired
    private FrozenBoxPositionMapper frozenBoxPositionMapper;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private StockInTubesRepository stockInTubesRepository;

    @Autowired
    private UserRepository userRepository;

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
        User user1 = userRepository.findOne(stockInDTO.getStoreKeeperId1());
        User user2 = userRepository.findOne(stockInDTO.getStoreKeeperId2());
        stockIn.setStoreKeeper1(user1!=null?user1.getLogin():stockInDTO.getStoreKeeper1());
        stockIn.setStoreKeeper2(user2!=null?user2.getLogin():stockInDTO.getStoreKeeper2());
        stockIn.setStockInDate(stockInDTO.getStockInDate());
        stockIn.setStoreKeeperId1(stockInDTO.getStoreKeeperId1());
        stockIn.setStoreKeeperId2(stockInDTO.getStoreKeeperId2());
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
    public StockInForDataDetail saveStockIns(String transhipCode,Long receiverId,LocalDate receiveDate) {
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        stockInForDataDetail.setTranshipCode(transhipCode);

        if(transhipCode == null){
            throw new BankServiceException("转运编码不能为空！",transhipCode);
        }
        TranshipByIdResponse transhipRes = new TranshipByIdResponse();
        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        if(tranship == null){
            throw new BankServiceException("转运记录不存在！",transhipCode);
        }
        List<StockIn> stockIns = stockInRepository.findByTranshipCode(transhipCode);
        if(stockIns.size()>0){
            throw new BankServiceException("此次转运已经在执行入库！",transhipCode);
        }
        transhipRes = transhipService.findTranshipAndFrozenBox(tranship.getId());
        List<FrozenBoxDTO> frozenBoxDTOList =  transhipRes.getFrozenBoxDTOList();
        if(frozenBoxDTOList.size()==0){
            throw new BankServiceException("此次转运没有冻存盒数据！",transhipCode);
        }
        //修改转运表中数据状态为待入库
        tranship.setTranshipState(Constants.TRANSHIPE_IN_STOCKING);
        User user = userRepository.findOne(receiverId);
        tranship.setReceiverId(receiverId);
        tranship.setReceiver(user!=null?user.getLogin():null);
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
            //保存盒子位置
            FrozenBoxPosition frozenBoxPosition = frozenBoxPositionRepository.findOneByFrozenBoxIdAndStatus(box.getId(),Constants.FROZEN_BOX_STOCKING);
            if(frozenBoxPosition == null){
                frozenBoxPosition = new FrozenBoxPosition();
            }
            frozenBoxPosition = frozenBoxPositionMapper.frozenBoxToFrozenBoxPosition(frozenBoxPosition,box);

            String status = Constants.FROZEN_BOX_STOCKING;
            if(box.getIsSplit().equals(Constants.YES)){
                status = Constants.FROZEN_BOX_SPLITING;
            }
            frozenBoxPosition.setStatus(status);
            frozenBoxPositionRepository.save(frozenBoxPosition);

            List<FrozenTube> frozenTubes =frozenTubeRepository.findFrozenTubeListByBoxId(box.getId());
            for(FrozenTube tube:frozenTubes){
                //保存入库与冻存管的关系
                StockInTubes stockInTubes = new StockInTubes();
                stockInTubes.setMemo(tube.getMemo());
                stockInTubes.setStatus(status);
                stockInTubes.setColumnsInTube(tube.getTubeColumns());
                stockInTubes.setRowsInTube(tube.getTubeRows());
                stockInTubes.setFrozenBoxPosition(frozenBoxPosition);
                stockInTubes.setFrozenTube(tube);
                stockInTubes.setFrozenTubeCode(tube.getFrozenTubeCode());
                stockInTubes.setSampleCode(tube.getSampleCode());
                stockInTubes.setSampleTempCode(tube.getSampleTempCode());
                stockInTubes.setStockInBox(stockInBox);
                stockInTubes.setTranshipBox(transhipBox);
                stockInTubesRepository.save(stockInTubes);
            }
        }
        return stockInForDataDetail;
    }
    @Override
    public DataTablesOutput<StockInForDataTable> findStockIn(DataTablesInput input) {
        //重新构造input
        input = createStockInFroDataTableInput(input);

        //获取入库列表
        input.addColumn("createdDate",true,true,"");
        input.addOrder("createdDate",false);
        DataTablesOutput<StockIn> stockInDataTablesOutput =  stockInRepositries.findAll(input);
        List<StockIn> stockIns =  stockInDataTablesOutput.getData();

        //构造返回列表
        List<StockInForDataTable> stockInDTOS = stockInMapper.stockInsToStockInTables(stockIns);
        List<StockInForDataTable> stockInList = new ArrayList<StockInForDataTable>();

        for(StockInForDataTable s:stockInDTOS){
            List<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxByStockInCode(s.getStockInCode());
            int countOfBox = 0;
            for(StockInBox stockInBox :stockInBoxes){
                if(!stockInBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED))
                    countOfBox++;
            }
            s.setCountOfBox(countOfBox);
            stockInList.add(s);
        }
        //构造返回分页数据
        DataTablesOutput<StockInForDataTable> responseDataTablesOutput = new DataTablesOutput<>();
        responseDataTablesOutput.setDraw(stockInDataTablesOutput.getDraw());
        responseDataTablesOutput.setError(stockInDataTablesOutput.getError());
        responseDataTablesOutput.setData(stockInList);
        responseDataTablesOutput.setRecordsFiltered(stockInDataTablesOutput.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(stockInDataTablesOutput.getRecordsTotal());
        return responseDataTablesOutput;
    }

    private DataTablesInput createStockInFroDataTableInput(DataTablesInput input) {
        Map<String,Column> columnMap =  input.getColumnsAsMap();
        for(String data:columnMap.keySet()){
            if(data.equals("transhipCode")){
                columnMap.get(data).setData("tranship.transhipCode");
            }
            if(data.equals("countOfBox")){
                columnMap.get(data).setData("");
            }
        }
        input.addColumn("stockInCode", true, true, "");
        return input;
    }

    private StockInBox createStockInBox(FrozenBox box, StockIn stockIn) {
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
        inBox.setMemo(box.getMemo());
        inBox.setStatus(box.getStatus());
        return inBox;
    }

    private StockInDTO createStockInDTO(Tranship tranship) {
        StockInDTO stockInDTO = new StockInDTO();
        stockInDTO.setStockInCode(BankUtil.getUniqueID());
        if(tranship.getProject()==null||tranship.getProjectSite()==null){
            throw new BankServiceException("项目信息不完整！",tranship.toString());
        }
        stockInDTO.setProjectId(tranship.getProject().getId());
        stockInDTO.setStatus(Constants.STOCK_IN_PENDING);
        stockInDTO.setProjectSiteCode(tranship.getProjectSiteCode());
        stockInDTO.setProjectCode(tranship.getProjectCode());
        stockInDTO.setProjectSiteId(tranship.getProjectSite().getId());
        stockInDTO.setReceiveId(null);
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
    public StockInForDataDetail completedStockIn(String stockInCode,String loginName1,String loginName2) {
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("该入库记录不存在！",stockInCode);
        }
        //修改入库
        stockIn.setStatus(Constants.STOCK_IN_COMPLETE);
        User user1 = userRepository.findByLogin(loginName1);
        User user2 = userRepository.findByLogin(loginName2);
        stockIn.setStoreKeeperId1(user1!=null?user1.getId():null);
        stockIn.setStoreKeeperId2(user2!=null?user2.getId():null);
        stockIn.setStoreKeeper1(loginName1);
        stockIn.setStoreKeeper2(loginName2);
        stockInRepository.save(stockIn);
        List<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxByStockInCode(stockInCode);
        //修改盒子
        for(StockInBox box: stockInBoxes){
            if(box.getStatus().equals(Constants.FROZEN_BOX_STOCKING)){
                throw new BankServiceException("冻存盒未上架！",box.toString());
            }
            if(box.getStatus().equals(Constants.FROZEN_BOX_PUT_SHELVES)){
                frozenBoxRepository.updateStatusByFrozenBoxCode(box.getFrozenBoxCode(),Constants.FROZEN_BOX_STOCKED);
                //修改入库盒子
                box.setStatus(Constants.FROZEN_BOX_STOCKED);
                stockInBoxRepository.save(box);
                //增加冻存盒位置记录
                FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(box.getFrozenBoxCode());
                FrozenBoxPosition frozenBoxPositionOld =  frozenBoxPositionRepository.findOneByFrozenBoxIdAndStatus(frozenBox.getId(),Constants.FROZEN_BOX_STOCKING);
                if(frozenBoxPositionOld == null){
                    throw new BankServiceException("未查询到该冻存盒的待入库记录！",box.toString());
                }
                FrozenBoxPosition frozenBoxPos = new FrozenBoxPosition();
                frozenBoxPos = frozenBoxPositionMapper.frozenBoxToFrozenBoxPosition(frozenBoxPos,frozenBox);
                frozenBoxPos.setStatus(Constants.FROZEN_BOX_STOCKED);
                frozenBoxPos = frozenBoxPositionRepository.save(frozenBoxPos);
                TranshipBox transhipBox = transhipBoxRepository.findByFrozenBoxCode(frozenBox.getFrozenBoxCode());
                //保存冻存管历史
                List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(box.getFrozenBoxCode());
                for(FrozenTube tube : frozenTubes){
                    //保存入库与冻存管的关系
                    StockInTubes stockInTubes = new StockInTubes();
                    stockInTubes.setMemo(tube.getMemo());
                    stockInTubes.setStatus(Constants.FROZEN_BOX_STOCKED);
                    stockInTubes.setColumnsInTube(tube.getTubeColumns());
                    stockInTubes.setRowsInTube(tube.getTubeRows());
                    stockInTubes.setFrozenBoxPosition(frozenBoxPos);
                    stockInTubes.setFrozenTube(tube);
                    stockInTubes.setFrozenTubeCode(tube.getFrozenTubeCode());
                    stockInTubes.setSampleCode(tube.getSampleCode());
                    stockInTubes.setStockInBox(box);
                    stockInTubes.setTranshipBox(transhipBox);
                    stockInTubes.setSampleTempCode(tube.getSampleTempCode());
                    stockInTubesRepository.save(stockInTubes);
                }
            }
        }
        //修改转运
        transhipRepository.updateTranshipStateById(stockIn.getTranship().getId(),Constants.TRANSHIPE_IN_STOCKED);

        return stockInMapper.stockInToStockInDetail(stockIn);
    }

    @Override
    public StockInForDataDetail getStockInById(Long id) {
        StockIn stockIn = stockInRepository.findOne(id);
        return stockInMapper.stockInToStockInDetail(stockIn);
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

}
