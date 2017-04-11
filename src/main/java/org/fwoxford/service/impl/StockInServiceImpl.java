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
    private StockInBoxService stockInBoxService;
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
        StockIn stockIn = stockInMapper.stockInDTOToStockIn(stockInDTO);
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
    public StockInForDataDetail saveStockIns(String transhipCode) {
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
        transhipRes = transhipService.findTranshipAndFrozenBox(tranship.getId());
        List<FrozenBoxDTO> frozenBoxDTOList =  transhipRes.getFrozenBoxDTOList();
        if(frozenBoxDTOList.size()==0){
            throw new BankServiceException("此次转运没有冻存盒数据！",transhipCode);
        }
        //修改转运表中数据状态为待入库
        tranship.setTranshipState(Constants.TRANSHIPE_IN_STOCKING);
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
        //保存入库盒子
        // 修改盒子状态，转运盒子状态
        for(FrozenBoxDTO boxDTO: frozenBoxDTOList){
            boxDTO.setStatus(Constants.FROZEN_BOX_STOCKING);
            frozenBoxRepository.save(frozenBoxMapper.frozenBoxDTOToFrozenBox(boxDTO));
            TranshipBox transhipBox = transhipBoxRepository.findByTranshipIdAndFrozenBoxId(tranship.getId(),boxDTO.getId());
            transhipBox.setStatus(Constants.FROZEN_BOX_STOCKING);
            transhipBoxRepository.save(transhipBox);
        }

        List<StockInBoxDTO> stockInBoxDTOS = createStockInBoxDTO(frozenBoxDTOList,stockIn);
        List<StockInBoxDTO> stockInBoxDTOSList = stockInBoxService.saveBatch(stockInBoxDTOS);
        return stockInForDataDetail;
    }
    @Override
    public DataTablesOutput<StockInForDataTable> findStockIn(DataTablesInput input) {

        //获取转运列表
        DataTablesOutput<StockIn> stockInDataTablesOutput =  stockInRepositries.findAll(input);
        List<StockIn> stockIns =  stockInDataTablesOutput.getData();

        //构造返回列表
        List<StockInForDataTable> stockInDTOS = stockInMapper.stockInsToStockInTables(stockIns);

        //构造返回分页数据
        DataTablesOutput<StockInForDataTable> responseDataTablesOutput = new DataTablesOutput<>();
        responseDataTablesOutput.setDraw(stockInDataTablesOutput.getDraw());
        responseDataTablesOutput.setError(stockInDataTablesOutput.getError());
        responseDataTablesOutput.setData(stockInDTOS);
        responseDataTablesOutput.setRecordsFiltered(stockInDataTablesOutput.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(stockInDataTablesOutput.getRecordsTotal());
        return responseDataTablesOutput;
    }

    private List<StockInBoxDTO> createStockInBoxDTO(List<FrozenBoxDTO> frozenBoxDTOList, StockIn stockIn) {
        List<StockInBoxDTO> stockInBoxDTOS = new ArrayList<StockInBoxDTO>();
        for(FrozenBoxDTO box : frozenBoxDTOList){
            StockInBoxDTO inBoxDTO = new StockInBoxDTO();
            inBoxDTO.setEquipmentCode(box.getEquipmentCode());
            inBoxDTO.setEquipmentId(box.getEquipmentId());
            inBoxDTO.setAreaId(box.getAreaId());
            inBoxDTO.setAreaCode(box.getAreaCode());
            inBoxDTO.setSupportRackId(box.getSupportRackId());
            inBoxDTO.setSupportRackCode(box.getSupportRackCode());
            inBoxDTO.setRowsInShelf(box.getRowsInShelf());
            inBoxDTO.setColumnsInShelf(box.getColumnsInShelf());
            inBoxDTO.setFrozenBoxCode(box.getFrozenBoxCode());
            inBoxDTO.setStockInId(stockIn.getId());
            inBoxDTO.setStockInCode(stockIn.getStockInCode());
            inBoxDTO.setMemo("");
            inBoxDTO.setStatus(box.getStatus());
            inBoxDTO.setCreatedBy(SecurityUtils.getCurrentUserLogin());
            inBoxDTO.setCreatedDate(ZonedDateTime.now());
            inBoxDTO.setLastModifiedDate(ZonedDateTime.now());
            inBoxDTO.setLastModifiedBy(SecurityUtils.getCurrentUserLogin());
            stockInBoxDTOS.add(inBoxDTO);
        }
        return stockInBoxDTOS;
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
        stockInDTO.setCountOfSample(tranship.getSampleNumber());
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
    public StockInForDataDetail completedStockIn(String stockInCode) {
        StockInForDataDetail stockInForDataDetail = new StockInForDataDetail();
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn.getId()==null){
            throw new BankServiceException("该入库记录不存在！",stockInCode);
        }
        //修改入库
        stockIn.setStatus(Constants.STOCK_IN_COMPLETE);
        stockIn.setStockInDate(LocalDate.now());
        stockInRepository.save(stockIn);
        //修改入库盒子
        List<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxByStockInCode(stockInCode);
        stockInBoxRepository.updateByStockCode(stockInCode , Constants.FROZEN_BOX_STOCKED);
        //修改盒子
        for(StockInBox box: stockInBoxes){
            frozenBoxRepository.updateStatusByFrozenBoxCode(box.getFrozenBoxCode(),Constants.FROZEN_BOX_STOCKED);
            //修改转运盒子
            transhipBoxRepository.updateStatusByTranshipIdAndFrozenBoxCode(stockIn.getTranship().getId(),box.getFrozenBoxCode(),Constants.FROZEN_BOX_STOCKED);
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
