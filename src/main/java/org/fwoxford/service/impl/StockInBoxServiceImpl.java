package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.dto.StockInTubeDTO;
import org.fwoxford.service.dto.response.StockInBoxDetail;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInBoxSplit;
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
    @Autowired
    private TranshipBoxRepository transhipBoxRepository;
    @Autowired
    private FrozenBoxTypeMapper frozenBoxTypeMapper;
    @Autowired
    private FrozenTubeRecordRepository frozenTubeRecordRepository;

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
        List<StockInBox> stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,boxCode);
        if(stockInBox.size() == 0){
            throw new BankServiceException("未查询到该冻存盒的入库记录！",stockInCode+","+boxCode);
        }
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);

        stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
        stockInBoxDetail.setId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(boxCode);
        stockInBoxDetail.setMemo(frozenBox.getMemo());
        stockInBoxDetail.setStockInCode(stockInCode);
        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(boxCode, Constants.FROZEN_TUBE_NORMAL);
        stockInBoxDetail.setCountOfSample(frozenTubes.size());
        stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
        stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
        stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
        stockInBoxDetail.setEquipmentId(frozenBox.getEquipment()!=null?frozenBox.getEquipment().getId():null);
        stockInBoxDetail.setAreaId(frozenBox.getArea()!=null?frozenBox.getArea().getId():null);
        stockInBoxDetail.setSupportRackId(frozenBox.getArea()!=null?frozenBox.getSupportRack().getId():null);
        stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBoxDetail.setFrozenBoxColumns(frozenBox.getFrozenBoxColumns());
        stockInBoxDetail.setFrozenBoxRows(frozenBox.getFrozenBoxRows());
        stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
        stockInBoxDetail.setStatus(frozenBox.getStatus());

        return stockInBoxDetail;
    }

    /**
     * 分装入库盒子
     * @param stockInCode
     * @param boxCode
     * @param stockInBoxForDataSplit
     * @return
     */
    @Override
    public StockInBoxSplit splitedStockIn(String stockInCode, String boxCode, StockInBoxSplit stockInBoxForDataSplit) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("入库记录不存在！",stockInCode);
        }
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！",boxCode);
        }
        if(frozenBox.getFrozenBoxType()==null){
            throw new BankServiceException("冻存盒类型不能为空！",boxCode);
        }
        if(stockInBoxForDataSplit.getEquipment()==null){
            throw new BankServiceException("设备不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getArea()==null){
            throw new BankServiceException("区域不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getShelf()==null){
            throw new BankServiceException("冻存架不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getColumnsInShelf()==null || stockInBoxForDataSplit.getColumnsInShelf() == ""){
            throw new BankServiceException("所在冻存架列数不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getRowsInShelf()==null || stockInBoxForDataSplit.getRowsInShelf() == ""){
            throw new BankServiceException("所在冻存架行数不能为空！",stockInBoxForDataSplit.toString());
        }
        //更改盒子状态
        frozenBox.setStatus(Constants.FROZEN_BOX_SPLITED);
        frozenBoxRepository.save(frozenBox);
        //更改转运盒子状态
        transhipBoxRepository.updateStatusByTranshipIdAndFrozenBoxCode(frozenBox.getTranship().getId(),boxCode,Constants.FROZEN_BOX_SPLITED);
        //更改入库盒子状态
        stockInBoxRepository.updateByStockCodeAndFrozenBoxCode(stockInCode,boxCode,Constants.FROZEN_BOX_SPLITED);
        //新增盒子
        FrozenBox frozenBoxNew = new FrozenBox();
        frozenBoxNew.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
        frozenBoxNew.setProject(frozenBox.getProject());
        frozenBoxNew.setProjectCode(frozenBox.getProjectCode());
        frozenBoxNew.setProjectName(frozenBox.getProjectName());
        frozenBoxNew.setProjectSite(frozenBox.getProjectSite());
        frozenBoxNew.setProjectSiteName(frozenBox.getProjectSiteName());
        frozenBoxNew.setProjectSiteCode(frozenBox.getProjectSiteCode());
        frozenBoxNew.setStatus(Constants.FROZEN_BOX_STOCKING);
        frozenBoxNew.setFrozenBoxType(frozenBoxTypeMapper.frozenBoxTypeDTOToFrozenBoxType(stockInBoxForDataSplit.getFrozenBoxTypeDTO()));
        frozenBoxNew.setFrozenBoxRows(stockInBoxForDataSplit.getFrozenBoxRows());
        frozenBoxNew.setFrozenBoxColumns(stockInBoxForDataSplit.getFrozenBoxColumns());
        frozenBoxNew.setFrozenBoxTypeCode(stockInBoxForDataSplit.getFrozenBoxTypeDTO().getFrozenBoxTypeCode());
        frozenBoxNew.setIsRealData(frozenBox.getIsRealData());
        frozenBoxNew.setIsSplit(Constants.NO);
        frozenBoxNew.setEquipment(equipmentMapper.equipmentDTOToEquipment(stockInBoxForDataSplit.getEquipment()));
        frozenBoxNew.setEquipmentCode(stockInBoxForDataSplit.getEquipment().getEquipmentCode());
        frozenBoxNew.setArea(areaMapper.areaDTOToArea(stockInBoxForDataSplit.getArea()));
        frozenBoxNew.setSupportRack(supportRackMapper.supportRackDTOToSupportRack(stockInBoxForDataSplit.getShelf()));
        frozenBoxNew.setSupportRackCode(stockInBoxForDataSplit.getShelf().getSupportRackCode());
        frozenBoxNew.setColumnsInShelf(stockInBoxForDataSplit.getColumnsInShelf());
        frozenBoxNew.setRowsInShelf(stockInBoxForDataSplit.getRowsInShelf());
        frozenBoxNew.setDislocationNumber(0);
        frozenBoxNew.setSampleType(sampleTypeMapper.sampleTypeDTOToSampleType(stockInBoxForDataSplit.getSampleType()));
        frozenBoxNew.setSampleTypeCode(stockInBoxForDataSplit.getSampleType().getSampleTypeCode());
        frozenBoxNew.setSampleTypeName(stockInBoxForDataSplit.getSampleType().getSampleTypeName());
        frozenBoxNew.setSampleNumber(stockInBoxForDataSplit.getCountOfSample());
        frozenBoxNew.setEmptyHoleNumber(0);
        frozenBoxNew.setEmptyTubeNumber(0);
        frozenBoxNew.setMemo(stockInBoxForDataSplit.getMemo());
        frozenBoxNew = frozenBoxRepository.save(frozenBoxNew);

        stockInBoxForDataSplit.setFrozenBoxId(frozenBoxNew.getId());

        //新增入库盒子
        List<StockInBox> stockInBoxList = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,frozenBoxNew.getFrozenBoxCode());
        StockInBox stockInBox = new StockInBox();
        stockInBox.setFrozenBoxCode(frozenBoxNew.getFrozenBoxCode());
        stockInBox.setStockInCode(stockInCode);
        stockInBox.setEquipment(equipmentMapper.equipmentDTOToEquipment(stockInBoxForDataSplit.getEquipment()));
        stockInBox.setEquipmentCode(stockInBoxForDataSplit.getEquipment().getEquipmentCode());
        stockInBox.setArea(areaMapper.areaDTOToArea(stockInBoxForDataSplit.getArea()));
        stockInBox.setAreaCode(stockInBoxForDataSplit.getArea().getAreaCode());
        stockInBox.setSupportRack(supportRackMapper.supportRackDTOToSupportRack(stockInBoxForDataSplit.getShelf()));
        stockInBox.setSupportRackCode(stockInBoxForDataSplit.getShelf().getSupportRackCode());
        stockInBox.setColumnsInShelf(stockInBoxForDataSplit.getColumnsInShelf());
        stockInBox.setRowsInShelf(stockInBoxForDataSplit.getRowsInShelf());
        stockInBox.setStatus(Constants.FROZEN_BOX_STOCKING);
        stockInBoxRepository.save(stockInBox);

        stockInBoxForDataSplit.setId(stockInBox.getId());

        //查询原管子的位置存历史
        List<StockInTubeDTO> stockInTubeDTOS = stockInBoxForDataSplit.getStockInTubeDTOList();
        for(StockInTubeDTO tube : stockInTubeDTOS){
            if(tube.getFrozenTubeId()==null){
                throw new BankServiceException("冻存管ID不能为空！",tube.toString());
            }
            FrozenTube frozenTube = frozenTubeRepository.findOne(tube.getFrozenTubeId());
            if(frozenTube == null){
                throw new BankServiceException("冻存管不存在",tube.toString());
            }
            //保存管子历史记录
            FrozenTubeRecord frozenTubeRecord = new FrozenTubeRecord();
            frozenTubeRecord.setFrozenTube(frozenTube);
            frozenTubeRecord.setStatus(frozenTube.getStatus());
            frozenTubeRecord.setProjectCode(frozenTube.getProjectCode());
            frozenTubeRecord.setFrozenBox(frozenTube.getFrozenBox());
            frozenTubeRecord.setFrozenBoxCode(frozenTube.getFrozenBoxCode());
            frozenTubeRecord.setIsModifyPosition(tube.getIsModifyPosition()!=null?tube.getIsModifyPosition():Constants.YES);
            frozenTubeRecord.setIsModifyState(tube.getIsModifyState()!=null?tube.getIsModifyState():Constants.NO);
            frozenTubeRecord.setSampleCode(frozenTube.getSampleCode());
            frozenTubeRecord.setSampleTempCode(frozenTube.getSampleTempCode());
            frozenTubeRecord.setSampleType(frozenTube.getSampleType());
            frozenTubeRecord.setSampleTypeName(frozenTube.getSampleTypeName());
            frozenTubeRecord.setSampleTypeCode(frozenTube.getSampleTypeCode());
            frozenTubeRecord.setTubeRows(frozenTube.getTubeRows());
            frozenTubeRecord.setTubeColumns(frozenTube.getTubeColumns());
            frozenTubeRecordRepository.save(frozenTubeRecord);
            //更改管子的位置信息
            frozenTube.setFrozenBox(frozenBoxNew);
            frozenTube.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
            frozenTube.setTubeColumns(tube.getTubeColumns());
            frozenTube.setTubeRows(tube.getTubeRows());
            frozenTubeRepository.save(frozenTube);
        }
        return stockInBoxForDataSplit;
    }

    private FrozenBox createSplitedFrozenBox(StockInBoxSplit stockInBoxForDataSplit) {
        FrozenBox frozenBox = new FrozenBox();
        frozenBox.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
        frozenBox = frozenBoxRepository.save(frozenBox);
        return frozenBox;
    }
}
