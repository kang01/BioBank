package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.dto.response.StockInBoxDetail;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing StockInBox.
 */
@Service
@Transactional
public class StockInBoxServiceImpl implements StockInBoxService {

    private final Logger log = LoggerFactory.getLogger(StockInBoxServiceImpl.class);

    private final StockInRepository stockInRepository;

    private final StockInBoxRepository stockInBoxRepository;

    private final StockInBoxMapper stockInBoxMapper;
    @Autowired
    private final StockInBoxRepositries stockInBoxRepositries;
    @Autowired
    private SampleTypeMapper sampleTypeMapper;
    @Autowired
    private FrozenTubeRepository frozenTubeRepository;
    @Autowired
    private FrozenBoxRepository frozenBoxRepository;
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private SupportRackMapper supportRackMapper;

    public StockInBoxServiceImpl(StockInBoxRepository stockInBoxRepository, StockInBoxMapper stockInBoxMapper,
                                 StockInBoxRepositries stockInBoxRepositries,StockInRepository stockInRepository) {
        this.stockInBoxRepository = stockInBoxRepository;
        this.stockInBoxMapper = stockInBoxMapper;
        this.stockInBoxRepositries = stockInBoxRepositries;
        this.stockInRepository = stockInRepository;
    }

    /**
     * Save a stockInBox.
     *
     * @param stockInBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockInBoxDTO save(StockInBoxDTO stockInBoxDTO) {
        log.debug("Request to save StockInBox : {}", stockInBoxDTO);
        StockInBox stockInBox = stockInBoxMapper.stockInBoxDTOToStockInBox(stockInBoxDTO);
        stockInBox = stockInBoxRepository.save(stockInBox);
        StockInBoxDTO result = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);
        return result;
    }

    /**
     *  Get all the stockInBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockInBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockInBoxes");
        Page<StockInBox> result = stockInBoxRepository.findAll(pageable);
        return result.map(stockInBox -> stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox));
    }

    /**
     *  Get one stockInBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockInBoxDTO findOne(Long id) {
        log.debug("Request to get StockInBox : {}", id);
        StockInBox stockInBox = stockInBoxRepository.findOne(id);
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);
        return stockInBoxDTO;
    }

    /**
     *  Delete the  stockInBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockInBox : {}", id);
        stockInBoxRepository.delete(id);
    }

    @Override
    public List<StockInBoxDTO> saveBatch(List<StockInBoxDTO> stockInBoxDTOS) {
        List<StockInBox> stockInBoxes = stockInBoxMapper.stockInBoxDTOsToStockInBoxes(stockInBoxDTOS);
        List<StockInBox> stockInBoxesList = stockInBoxRepository.save(stockInBoxes);
        return stockInBoxMapper.stockInBoxesToStockInBoxDTOs(stockInBoxesList);
    }


    @Override
    public DataTablesOutput<StockInBoxForDataTable> getPageStockInBoxes(DataTablesInput input, String stockInCode) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("入库记录不存在！",stockInCode);
        }
        input.addColumn("stockInCode", true, true, stockInCode);
        DataTablesOutput<StockInBox> output =stockInBoxRepositries.findAll(input);
        List<StockInBox> alist = output.getData();

        List<StockInBoxForDataTable> stockInBoxForDataTables = new ArrayList<StockInBoxForDataTable>();
        for(StockInBox box : alist){
            StockInBoxForDataTable stockInBoxForDataTable = new StockInBoxForDataTable();

            String position = box.getEquipmentCode()+"."+box.getAreaCode()+"."+box.getSupportRackCode()+"."+box.getRowsInShelf()+box.getColumnsInShelf();
            stockInBoxForDataTable.setPosition(position);
            FrozenBox frozenBox =frozenBoxRepository.findFrozenBoxDetailsByBoxCode(box.getFrozenBoxCode());
            stockInBoxForDataTable.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
            stockInBoxForDataTable.setSampleTypeName(frozenBox.getSampleTypeName());
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(frozenBox.getFrozenBoxCode(), Constants.FROZEN_TUBE_NORMAL);
            stockInBoxForDataTable.setCountOfSample(frozenTubes.size());
            stockInBoxForDataTable.setFrozenBoxCode(box.getFrozenBoxCode());
            stockInBoxForDataTable.setFrozenBoxColumns(Integer.parseInt(frozenBox.getFrozenBoxColumns()));
            stockInBoxForDataTable.setFrozenBoxRows(Integer.parseInt(frozenBox.getFrozenBoxRows()));
            stockInBoxForDataTable.setId(frozenBox.getId());
            stockInBoxForDataTable.setIsSplit(frozenBox.getIsSplit());
            stockInBoxForDataTable.setStatus(box.getStatus());
            stockInBoxForDataTables.add(stockInBoxForDataTable);
        }
        //构造返回分页数据
        DataTablesOutput<StockInBoxForDataTable> responseDataTablesOutput = new DataTablesOutput<>();
        responseDataTablesOutput.setDraw(output.getDraw());
        responseDataTablesOutput.setError(output.getError());
        responseDataTablesOutput.setData(stockInBoxForDataTables);
        responseDataTablesOutput.setRecordsFiltered(output.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(output.getRecordsTotal());
        return responseDataTablesOutput;
    }

    @Override
    public StockInBoxDetail getStockInBoxDetail(String stockInCode, String boxCode) {
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,boxCode);
        if(stockInBox == null){
            throw new BankServiceException("未查询到该冻存盒的入库记录！",stockInCode+","+boxCode);
        }
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);

        stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
        stockInBoxDetail.setId(stockInBox.getId());
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(boxCode);
        stockInBoxDetail.setMemo(stockInBox.getMemo());
        stockInBoxDetail.setStockInCode(stockInCode);
        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(boxCode, Constants.FROZEN_TUBE_NORMAL);
        stockInBoxDetail.setCountOfSample(frozenTubes.size());
        stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(stockInBox.getEquipment()));
        stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(stockInBox.getArea()));
        stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(stockInBox.getSupportRack()));
        stockInBoxDetail.setEquipmentId(stockInBox.getEquipment()!=null?stockInBox.getEquipment().getId():null);
        stockInBoxDetail.setAreaId(stockInBox.getArea()!=null?stockInBox.getArea().getId():null);
        stockInBoxDetail.setSupportRackId(stockInBox.getArea()!=null?stockInBox.getSupportRack().getId():null);
        stockInBoxDetail.setColumnsInShelf(stockInBox.getColumnsInShelf());
        stockInBoxDetail.setRowsInShelf(stockInBox.getRowsInShelf());
        stockInBoxDetail.setFrozenBoxColumns(frozenBox.getFrozenBoxColumns());
        stockInBoxDetail.setFrozenBoxRows(frozenBox.getFrozenBoxRows());
        stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
        stockInBoxDetail.setStatus(stockInBox.getStatus());

        return stockInBoxDetail;
    }
}
