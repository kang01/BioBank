package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.dto.FrozenBoxPositionDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private SupportRackRepository supportRackRepository;
    @Autowired
    private SampleTypeRepository sampleTypeRepository;
    @Autowired
    private FrozenBoxTypeRepository frozenBoxTypeRepository;
    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

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

            String position = box.getEquipmentCode()+"."+box.getAreaCode()+"."+box.getSupportRackCode()+"."+box.getColumnsInShelf()+box.getRowsInShelf();
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
     * @param stockInBoxForDataSplits
     * @return
     */
    @Override
    public List<StockInBoxSplit> splitedStockIn(String stockInCode, String boxCode, List<StockInBoxSplit> stockInBoxForDataSplits) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("入库记录不存在！",stockInCode);
        }
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！",boxCode);
        }
        List<SampleType> sampleTypes = sampleTypeRepository.findAll();
        List<Equipment> equipments = equipmentRepository.findAll();
        List<Area> areas = areaRepository.findAll();
        List<SupportRack> supportRacks = supportRackRepository.findAll();
        List<StockInBoxSplit> stockInBoxForDataSplitList = new ArrayList<StockInBoxSplit>();
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();

        for( StockInBoxSplit stockInBoxForDataSplit :stockInBoxForDataSplits){
            StockInBoxSplit stockInBoxSplit = splitedStockInSave(stockInBoxForDataSplit,sampleTypes,equipments,areas,supportRacks,frozenBox,stockInCode,frozenBoxTypeList);
            stockInBoxForDataSplitList.add(stockInBoxSplit);
        }
        //更改盒子状态
        //如果在盒子内还有剩余的管子，状态还是待入库
        List<FrozenTube> tubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(boxCode);
        if(tubeList.size()==0){
            frozenBox.setStatus(Constants.FROZEN_BOX_SPLITED);
            frozenBoxRepository.save(frozenBox);
            //更改转运盒子状态
            transhipBoxRepository.updateStatusByTranshipIdAndFrozenBoxCode(frozenBox.getTranship().getId(),boxCode,Constants.FROZEN_BOX_SPLITED);
            //更改入库盒子状态
            stockInBoxRepository.updateByStockCodeAndFrozenBoxCode(stockInCode,boxCode,Constants.FROZEN_BOX_SPLITED);

        }
      return stockInBoxForDataSplitList;
    }

    private StockInBoxSplit splitedStockInSave(StockInBoxSplit stockInBoxForDataSplit, List<SampleType> sampleTypes, List<Equipment> equipments, List<Area> areas, List<SupportRack> supportRacks, FrozenBox frozenBox, String stockInCode, List<FrozenBoxType> frozenBoxTypeList) {
        if(stockInBoxForDataSplit.getFrozenBoxTypeId()==null){
            throw new BankServiceException("冻存盒类型不能为空！",stockInBoxForDataSplit.getFrozenBoxCode());
        }
        if(stockInBoxForDataSplit.getEquipmentId()==null){
            throw new BankServiceException("设备不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getAreaId()==null){
            throw new BankServiceException("区域不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getSupportRackId()==null){
            throw new BankServiceException("冻存架不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getColumnsInShelf()==null || stockInBoxForDataSplit.getColumnsInShelf() == ""){
            throw new BankServiceException("所在冻存架列数不能为空！",stockInBoxForDataSplit.toString());
        }
        if(stockInBoxForDataSplit.getRowsInShelf()==null || stockInBoxForDataSplit.getRowsInShelf() == ""){
            throw new BankServiceException("所在冻存架行数不能为空！",stockInBoxForDataSplit.toString());
        }
        //新增盒子
        FrozenBox frozenBoxNew = new FrozenBox();
        frozenBoxNew.setId(stockInBoxForDataSplit.getFrozenBoxId());
        frozenBoxNew.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
        frozenBoxNew.setProject(frozenBox.getProject());
        frozenBoxNew.setProjectCode(frozenBox.getProjectCode());
        frozenBoxNew.setProjectName(frozenBox.getProjectName());
        frozenBoxNew.setProjectSite(frozenBox.getProjectSite());
        frozenBoxNew.setProjectSiteName(frozenBox.getProjectSiteName());
        frozenBoxNew.setProjectSiteCode(frozenBox.getProjectSiteCode());

        frozenBoxNew.setStatus(Constants.FROZEN_BOX_STOCKING);
        frozenBoxNew.setDislocationNumber(0);
        frozenBoxNew.setEmptyHoleNumber(0);
        frozenBoxNew.setEmptyTubeNumber(0);

        FrozenBoxType boxType = new FrozenBoxType();
        boxType.setId(stockInBoxForDataSplit.getFrozenBoxTypeId());
        int boxTypeIndex = frozenBoxTypeList.indexOf(boxType);


        if (boxTypeIndex >= 0){
            boxType = frozenBoxTypeList.get(boxTypeIndex);
            stockInBoxForDataSplit.setFrozenBoxType(frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(boxType));
            stockInBoxForDataSplit.setFrozenBoxRows(boxType.getFrozenBoxTypeRows());
            stockInBoxForDataSplit.setFrozenBoxColumns(boxType.getFrozenBoxTypeColumns());
        }else{
            throw new BankServiceException("冻存盒类型不存在！",stockInBoxForDataSplit.toString());
        }

        frozenBoxNew.setFrozenBoxType(boxType);
        frozenBoxNew.setFrozenBoxRows(stockInBoxForDataSplit.getFrozenBoxRows());
        frozenBoxNew.setFrozenBoxColumns(stockInBoxForDataSplit.getFrozenBoxColumns());
        frozenBoxNew.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());

        frozenBoxNew.setIsRealData(frozenBox.getIsRealData());
        frozenBoxNew.setIsSplit(stockInBoxForDataSplit.getIsSplit()!=null?stockInBoxForDataSplit.getIsSplit():Constants.NO);
        Equipment equipment = new Equipment();
        equipment.setId(stockInBoxForDataSplit.getEquipmentId());
        int equipmentIndex = equipments.indexOf(equipment);
        if (equipmentIndex >= 0){
            equipment = equipments.get(equipmentIndex);
            stockInBoxForDataSplit.setEquipment(equipmentMapper.equipmentToEquipmentDTO(equipment));
        }else{
            throw new BankServiceException("设备不存在！",stockInBoxForDataSplit.toString());
        }

        frozenBoxNew.setEquipment(equipment);
        frozenBoxNew.setEquipmentCode(equipment.getEquipmentCode());
        Area area = new Area();
        area.setId(stockInBoxForDataSplit.getAreaId());
        int areaIndex = areas.indexOf(area);

        if (areaIndex >= 0){
            area = areas.get(areaIndex);
            stockInBoxForDataSplit.setArea(areaMapper.areaToAreaDTO(area));
        }else{
            throw new BankServiceException("区域不存在！",stockInBoxForDataSplit.toString());
        }

        frozenBoxNew.setArea(area);
        frozenBoxNew.setAreaCode(area.getAreaCode());
        SupportRack supportRack= new SupportRack();
        supportRack.setId(stockInBoxForDataSplit.getSupportRackId());
        int supportIndex = supportRacks.indexOf(supportRack);
        if (supportIndex >= 0){
            supportRack = supportRacks.get(supportIndex);
            stockInBoxForDataSplit.setShelf(supportRackMapper.supportRackToSupportRackDTO(supportRack));
        }else{
            throw new BankServiceException("冻存架不存在！",stockInBoxForDataSplit.toString());
        }

        frozenBoxNew.setSupportRack(supportRack);
        frozenBoxNew.setSupportRackCode(supportRack.getSupportRackCode());

        SampleType sampleType = sampleTypes.stream().filter(s-> s.getSampleTypeCode().equals(stockInBoxForDataSplit.getSampleTypeCode())).findFirst().orElse(null);

        stockInBoxForDataSplit.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(sampleType));
        stockInBoxForDataSplit.setSampleTypeCode(sampleType.getSampleTypeCode());

        frozenBoxNew.setSampleType(sampleType);
        frozenBoxNew.setSampleTypeCode(sampleType.getSampleTypeCode());
        frozenBoxNew.setSampleTypeName(sampleType.getSampleTypeName());

        frozenBoxNew.setColumnsInShelf(stockInBoxForDataSplit.getColumnsInShelf());
        frozenBoxNew.setRowsInShelf(stockInBoxForDataSplit.getRowsInShelf());
        frozenBoxNew.setSampleNumber(stockInBoxForDataSplit.getStockInFrozenTubeList().size());

        frozenBoxNew.setMemo(stockInBoxForDataSplit.getMemo());
        frozenBoxNew = frozenBoxRepository.save(frozenBoxNew);

        stockInBoxForDataSplit.setFrozenBoxId(frozenBoxNew.getId());
        stockInBoxForDataSplit.setCountOfSample(stockInBoxForDataSplit.getStockInFrozenTubeList().size());


        //新增入库盒子
        List<StockInBox> stockInBoxList = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,frozenBoxNew.getFrozenBoxCode());
        StockInBox stockInBox = new StockInBox();
        stockInBox.setFrozenBoxCode(frozenBoxNew.getFrozenBoxCode());
        stockInBox.setStockInCode(stockInCode);
        stockInBox.setEquipment(equipment);
        stockInBox.setEquipmentCode(equipment.getEquipmentCode());
        stockInBox.setArea(area);
        stockInBox.setAreaCode(area.getAreaCode());
        stockInBox.setSupportRack(supportRack);
        stockInBox.setSupportRackCode(supportRack.getSupportRackCode());
        stockInBox.setColumnsInShelf(stockInBoxForDataSplit.getColumnsInShelf());
        stockInBox.setRowsInShelf(stockInBoxForDataSplit.getRowsInShelf());
        stockInBox.setStatus(Constants.FROZEN_BOX_STOCKING);
        stockInBoxRepository.save(stockInBox);

        stockInBoxForDataSplit.setFrozenBoxId(stockInBox.getId());

        //查询原管子的位置存历史
        List<StockInTubeDTO> stockInTubeDTOS = stockInBoxForDataSplit.getStockInFrozenTubeList();
        List<StockInTubeDTO> stockInTubeDTOList = new ArrayList<>();
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
            tube.setFrozenTubeId(frozenTube.getId());
            stockInTubeDTOList.add(tube);
        }
        stockInBoxForDataSplit.setStockInFrozenTubeList(stockInTubeDTOList);
        return stockInBoxForDataSplit;
    }

    /**
     * 输入入库单编码和盒子编码，以及冻存位置信息，返回保存后的盒子信息
     * @param stockInCode
     * @param boxCode
     * @param boxPositionDTO
     * @return
     */
    @Override
    public StockInBoxDetail movedStockIn(String stockInCode, String boxCode, FrozenBoxPositionDTO boxPositionDTO) {
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！",boxCode);
        }
        List<StockInBox> stockInBoxList = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,boxCode);
        if(stockInBoxList.size()==0){
            throw new BankServiceException("未查询到该盒子的待入库信息！",boxCode);
        }
        Equipment equipment = equipmentRepository.findOneByEquipmentCode(boxPositionDTO.getEquipmentCode());
        if (equipment == null){
            throw new BankServiceException("未查询到指定设备！",boxPositionDTO.toString());
        }
        Area area = areaRepository.findOneByAreaCodeAndEquipmentId(boxPositionDTO.getAreaCode(), equipment.getId());
        if (area == null){
            throw new BankServiceException("未查询到指定设备的指定区域！",boxPositionDTO.toString());
        }
        SupportRack shelf = supportRackRepository.findOneBySupportRackCodeAndAreaId(boxPositionDTO.getSupportRackCode(), area.getId());
        if (shelf == null){
            throw new BankServiceException("未查询到指定设备的指定区域的指定架子！",boxPositionDTO.toString());
        }
        frozenBox.setEquipmentCode(equipment.getEquipmentCode());
        frozenBox.setEquipment(equipment);
        frozenBox.setArea(area);
        frozenBox.setAreaCode(area.getAreaCode());
        frozenBox.setSupportRack(shelf);
        frozenBox.setSupportRackCode(shelf.getSupportRackCode());
        frozenBox.setColumnsInShelf(boxPositionDTO.getColumnsInShelf());
        frozenBox.setRowsInShelf(boxPositionDTO.getRowsInShelf());
        frozenBoxRepository.save(frozenBox);

        StockInBox stockInBox = stockInBoxList.get(0);
        stockInBox.setEquipmentCode(frozenBox.getEquipmentCode());
        stockInBox.setEquipment(frozenBox.getEquipment());
        stockInBox.setArea(frozenBox.getArea());
        stockInBox.setAreaCode(frozenBox.getAreaCode());
        stockInBox.setSupportRack(frozenBox.getSupportRack());
        stockInBox.setSupportRackCode(frozenBox.getSupportRackCode());
        stockInBox.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBox.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBoxRepository.save(stockInBox);
        stockInBoxDetail = createStockInBoxDetail(frozenBox,stockInCode);

        // todo:: 增加一个已上架的状态，这样前台可以排除已上架的盒子被再次上架
        return stockInBoxDetail;
    }

    @Override
    public List<StockInBoxForDataTable> findFrozenBoxListByBoxCodeStr(List<String> frozenBoxCodeStr) {
        List<StockInBoxForDataTable> stockInBoxs = new ArrayList<>();
        if (frozenBoxCodeStr.size()==0) {
            throw new BankServiceException("请传入有效的冻存盒编码！", frozenBoxCodeStr.toString());
        }
        List<String> statusStr = new ArrayList<String>();
        statusStr.add(Constants.FROZEN_BOX_STOCKED);
        statusStr.add(Constants.FROZEN_BOX_STOCKING);
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findByFrozenBoxCodeInAndStatusIn(frozenBoxCodeStr,statusStr);
        stockInBoxs = frozenBoxMapper.frozenBoxesToStockInBoxForDataTables(frozenBoxes);
        return stockInBoxs;
    }

    private StockInBoxDetail createStockInBoxDetail(FrozenBox frozenBox,String stockInCode) {
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        stockInBoxDetail.setMemo(frozenBox.getMemo());
        stockInBoxDetail.setStockInCode(stockInCode);
        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(frozenBox.getFrozenBoxCode(), Constants.FROZEN_TUBE_NORMAL);
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
}
