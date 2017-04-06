package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.StockIn;
import org.fwoxford.repository.StockInRepository;
import org.fwoxford.repository.StockInRepositries;
import org.fwoxford.security.SecurityUtils;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.StockInService;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.dto.StockInDTO;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.StockInForDataTable;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.mapper.StockInMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public StockInServiceImpl(StockInRepository stockInRepository, StockInMapper stockInMapper, StockInRepositries stockInRepositries) {
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
     * @param stockInDTO
     * @return
     */
    @Override
    public StockInDTO saveStockIns(StockInDTO stockInDTO) {
        Long transhipId = stockInDTO.getTranshipId();//转运ID
        //如果转运ID不为空，则查询这次转运的记录，保存至入库，如果为空，
        TranshipByIdResponse transhipRes = new TranshipByIdResponse();
        TranshipDTO tranship = new TranshipDTO();
        if(transhipId != null){
            tranship = transhipService.findOne(transhipId);
            transhipRes =  transhipService.findTranshipAndFrozenBox(transhipId);
        }
        stockInDTO = createStockInDTO(stockInDTO,tranship);
        StockInDTO stockIn = this.save(stockInDTO);
        List<FrozenBoxDTO> frozenBoxDTOList =  transhipRes.getFrozenBoxDTOList();
        List<StockInBoxDTO> stockInBoxDTOS = createStockInBoxDTO(frozenBoxDTOList,stockIn);
        List<StockInBoxDTO> stockInBoxDTOSList = stockInBoxService.saveBatch(stockInBoxDTOS);
        return stockInDTO;
    }
    @Override
    public DataTablesOutput<StockInForDataTable> findStockIn(DataTablesInput input) {

        //获取转运列表
        DataTablesOutput<StockIn> stockInDataTablesOutput =  stockInRepositries.findAll(input);
        List<StockIn> stockIns =  stockInDataTablesOutput.getData();

        //构造返回列表
//        List<StockInForDataTable> stockInDTOS = stockInMapper.stockInsToStockInDTOs(stockIns);

        //构造返回分页数据
        DataTablesOutput<StockInForDataTable> responseDataTablesOutput = new DataTablesOutput<>();
        responseDataTablesOutput.setDraw(stockInDataTablesOutput.getDraw());
        responseDataTablesOutput.setError(stockInDataTablesOutput.getError());
//        responseDataTablesOutput.setData(stockInDTOS);
        responseDataTablesOutput.setRecordsFiltered(stockInDataTablesOutput.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(stockInDataTablesOutput.getRecordsTotal());
        return responseDataTablesOutput;
    }

    private List<StockInBoxDTO> createStockInBoxDTO(List<FrozenBoxDTO> frozenBoxDTOList, StockInDTO stockIn) {
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
//            inBoxDTO.setStockInId(stockIn.getId());
            inBoxDTO.setMemo("");
            inBoxDTO.setStatus(Constants.STORANGE_IN_PENDING);
            inBoxDTO.setCreatedBy(SecurityUtils.getCurrentUserLogin());
            inBoxDTO.setCreatedDate(ZonedDateTime.now());
            inBoxDTO.setLastModifiedDate(ZonedDateTime.now());
            inBoxDTO.setLastModifiedBy(SecurityUtils.getCurrentUserLogin());
            stockInBoxDTOS.add(inBoxDTO);
        }
        return stockInBoxDTOS;
    }

    private StockInDTO createStockInDTO(StockInDTO stockInDTO, TranshipDTO tranship) {
        stockInDTO.setProjectId(tranship.getProjectId());
        stockInDTO.setStatus(Constants.STORANGE_IN_PENDING);
        stockInDTO.setProjectSiteCode(tranship.getProjectSiteCode());
        stockInDTO.setProjectCode(tranship.getProjectCode());
        stockInDTO.setProjectSiteId(tranship.getProjectSiteId());
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
        return stockInDTO;
    }
}
