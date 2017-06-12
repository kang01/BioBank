package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.StockInTubeService;
import org.fwoxford.service.dto.FrozenBoxPositionDTO;
import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.dto.StockInTubeDTO;
import org.fwoxford.service.dto.response.StockInBoxDetail;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInBoxForSplit;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private ProjectSampleClassRepository projectSampleClassRepository;
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
    @Autowired
    private FrozenBoxPositionRepository frozenBoxPositionRepository;

    @Autowired
    private FrozenBoxPositionMapper frozenBoxPositionMapper;

    @Autowired
    private StockInBoxPositionRepository stockInBoxPositionRepository;

    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;

    @Autowired
    private StockInTubeService stockInTubeService;

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
    public Page<StockInBoxForDataTableEntity> getPageStockInBoxes(String stockInCode, Pageable pageable) {
        Page<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxPageByStockInCode(stockInCode,pageable);

        return stockInBoxes.map(e -> {
            StockInBoxForDataTableEntity stockInBox = new StockInBoxForDataTableEntity();
            Long countOfSample = frozenTubeRepository.countFrozenTubeListByBoxCode(e.getFrozenBoxCode());
            stockInBox.setCountOfSample(countOfSample.intValue());
            stockInBox.setStatus(e.getStatus());
            stockInBox.setId(e.getId());
            stockInBox.setFrozenBoxCode(e.getFrozenBoxCode());
            stockInBox.setIsSplit(e.getFrozenBox().getIsSplit());
            FrozenBox frozenBox = frozenBoxRepository.findOne(e.getId());
            stockInBox.setPosition(BankUtil.getPositionString(frozenBox));
            stockInBox.setSampleClassificationName(e.getFrozenBox().getSampleClassification()!=null?e.getFrozenBox().getSampleClassification().getSampleClassificationName():null);
            stockInBox.setSampleTypeName(e.getFrozenBox().getSampleTypeName());
            stockInBox.setStockInCode(e.getStockInCode());
            return stockInBox;
        });
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
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(boxCode);
        stockInBoxDetail.setMemo(frozenBox.getMemo());
        stockInBoxDetail.setStockInCode(stockInCode);
        int number = frozenTubeRepository.countByFrozenBoxCodeAndStatus(boxCode, Constants.FROZEN_TUBE_NORMAL);
        stockInBoxDetail.setCountOfSample(number);
        stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
        stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
        stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
        stockInBoxDetail.setEquipmentId(frozenBox.getEquipment()!=null?frozenBox.getEquipment().getId():null);
        stockInBoxDetail.setAreaId(frozenBox.getArea()!=null?frozenBox.getArea().getId():null);
        stockInBoxDetail.setSupportRackId(frozenBox.getArea()!=null?frozenBox.getSupportRack().getId():null);
        stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
        stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
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
    public List<StockInBoxForSplit> splitedStockIn(String stockInCode, String boxCode, List<StockInBoxForSplit> stockInBoxForDataSplits) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("入库记录不存在！",stockInCode);
        }
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！",boxCode);
        }
        if(frozenBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED)){
            throw new BankServiceException("该冻存盒已经被分装过！",boxCode);
        }
        if(frozenBox.getIsSplit().equals(Constants.NO)){
            throw new BankServiceException("该冻存盒不具备分装条件！",boxCode);
        }
        List<SampleType> sampleTypes = sampleTypeRepository.findAll();
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        List<Equipment> equipments = equipmentRepository.findAll();
        List<Area> areas = areaRepository.findAll();
        List<SupportRack> supportRacks = supportRackRepository.findAll();
        List<StockInBoxForSplit> stockInBoxForDataSplitList = new ArrayList<StockInBoxForSplit>();
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAll();

        for( StockInBoxForSplit stockInBoxForDataSplit :stockInBoxForDataSplits){
            StockInBoxForSplit stockInBoxSplit = splitedStockInSave(stockInBoxForDataSplit,sampleTypes,equipments,areas,supportRacks,frozenBox,stockInCode,frozenBoxTypeList,sampleClassifications);
            stockInBoxForDataSplitList.add(stockInBoxSplit);
        }
        //更改盒子状态
        //如果在盒子内还有剩余的管子，状态还是待入库
        List<FrozenTube> tubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(boxCode);
        StockInBox frozenBoxForSplit = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,boxCode);
        if(tubeList.size()==0){
            frozenBox.setStatus(Constants.FROZEN_BOX_SPLITED);
            frozenBoxForSplit.setStatus(Constants.FROZEN_BOX_SPLITED);
        }
        frozenBoxForSplit.setCountOfSample(tubeList.size());
        frozenBox.setSampleNumber(tubeList.size());
        stockInBoxRepository.save(frozenBoxForSplit);
        frozenBoxRepository.save(frozenBox);
      return stockInBoxForDataSplitList;
    }

    private StockInBoxForSplit splitedStockInSave(StockInBoxForSplit stockInBoxForDataSplit, List<SampleType> sampleTypes, List<Equipment> equipments, List<Area> areas, List<SupportRack> supportRacks, FrozenBox frozenBox, String stockInCode, List<FrozenBoxType> frozenBoxTypeList,List<SampleClassification> sampleClassifications) {
        if(stockInBoxForDataSplit.getFrozenBoxTypeId()==null){
            throw new BankServiceException("冻存盒类型不能为空！",stockInBoxForDataSplit.getFrozenBoxCode());
        }
        //验证盒子编码是否存在

        List<Object[]> obj = frozenBoxRepository.countByFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
        for(Object[] o:obj){
            String frozenBoxId = o[0].toString();
            if(stockInBoxForDataSplit.getFrozenBoxId()==null){
                throw new BankServiceException("冻存盒编码已存在！",stockInBoxForDataSplit.getFrozenBoxCode());
            }else if(stockInBoxForDataSplit.getFrozenBoxId()!=null&&!stockInBoxForDataSplit.getFrozenBoxId().toString().equals(frozenBoxId)){
                throw new BankServiceException("冻存盒编码已存在！",stockInBoxForDataSplit.getFrozenBoxCode());
            }
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
        }else{
            throw new BankServiceException("冻存盒类型不存在！",stockInBoxForDataSplit.toString());
        }

        //判断冻存盒类型是否一致

        if(frozenBox.getFrozenBoxType().getId()!=stockInBoxForDataSplit.getFrozenBoxTypeId()){
            throw new BankServiceException("冻存盒类型不一致，不能执行分装！",stockInBoxForDataSplit.toString());
        }

        int countOfTube = stockInBoxForDataSplit.getStockInFrozenTubeList().size();
        String columns = boxType.getFrozenBoxTypeColumns()!=null?boxType.getFrozenBoxTypeColumns():new String("0");
        String rows = boxType.getFrozenBoxTypeRows()!=null?boxType.getFrozenBoxTypeRows():new String("0");
        int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
        if(countOfTube==0||countOfTube>allCounts){
            throw new BankServiceException("冻存管数量错误！",stockInBoxForDataSplit.toString());
        }

        frozenBoxNew.setFrozenBoxType(boxType);
        frozenBoxNew.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
        frozenBoxNew.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
        frozenBoxNew.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());

        frozenBoxNew.setIsRealData(frozenBox.getIsRealData());
        frozenBoxNew.setIsSplit(Constants.NO);
        Equipment equipment = new Equipment();
        equipment.setId(stockInBoxForDataSplit.getEquipmentId());
        int equipmentIndex = equipments.indexOf(equipment);
        if (equipmentIndex >= 0){
            equipment = equipments.get(equipmentIndex);
        }else{
            equipment = frozenBox.getEquipment();
        }
        frozenBoxNew.setEquipment(equipment!=null&&equipment.getId()!=null?equipment:null);
        frozenBoxNew.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():new String(""));

        Area area = new Area();
        area.setId(stockInBoxForDataSplit.getAreaId());
        int areaIndex = areas.indexOf(area);

        if (areaIndex >= 0){
            area = areas.get(areaIndex);
        }else {
            area = frozenBox.getArea();
        }

        frozenBoxNew.setArea(area!=null&&area.getId()!=null?area:null);
        frozenBoxNew.setAreaCode(area!=null?area.getAreaCode():new String(""));
        SupportRack supportRack= new SupportRack();
        supportRack.setId(stockInBoxForDataSplit.getSupportRackId());
        int supportIndex = supportRacks.indexOf(supportRack);
        if (supportIndex >= 0){
            supportRack = supportRacks.get(supportIndex);
        }else{
            supportRack = frozenBox.getSupportRack();
        }
        frozenBoxNew.setSupportRack(supportRack!=null&&supportRack.getId()!=null?supportRack:null);
        frozenBoxNew.setSupportRackCode(supportRack!=null?supportRack.getSupportRackCode():new String(""));

        SampleType sampleType = sampleTypes.stream().filter(s-> s.getId().equals(stockInBoxForDataSplit.getSampleTypeId())).findFirst().orElse(null);
        if(sampleType == null){
            throw new BankServiceException("样本类型不存在！");
        }
        if(frozenBox.getSampleType().getIsMixed().equals(Constants.NO)
            && (!sampleType.getId().equals(frozenBox.getSampleType().getId()) || sampleType.getId()!= frozenBox.getSampleType().getId())){
            throw new BankServiceException("需要分装的冻存盒与分装后的冻存盒的样本类型不一致，不能分装！");
        }
        frozenBoxNew.setSampleType(sampleType);
        frozenBoxNew.setSampleTypeCode(sampleType.getSampleTypeCode());
        frozenBoxNew.setSampleTypeName(sampleType.getSampleTypeName());

        //验证项目下该样本类型是否有样本分类，如果已经配置了样本分类，则样本分类ID不能为空，（99的除外）
        int countOfSampleClassForTube = projectSampleClassRepository.countByProjectIdAndSampleTypeId(frozenBoxNew.getProject()!=null?frozenBoxNew.getProject().getId():null,frozenBoxNew.getSampleType().getId());
        if(countOfSampleClassForTube>0 && sampleType.getIsMixed().equals(Constants.NO)
            && stockInBoxForDataSplit.getSampleClassificationId()==null){
            throw new BankServiceException("该项目下已经配置样本分类，样本分类不能为空！");
        }
        //样本分类
        SampleClassification sampleClassification = sampleClassifications.stream().filter(s->s.getId().equals(stockInBoxForDataSplit.getSampleClassificationId())).findFirst().orElse(null);
        frozenBoxNew.setSampleClassification(sampleClassification);


        frozenBoxNew.setColumnsInShelf(stockInBoxForDataSplit.getColumnsInShelf());
        frozenBoxNew.setRowsInShelf(stockInBoxForDataSplit.getRowsInShelf());
        frozenBoxNew.setSampleNumber(stockInBoxForDataSplit.getStockInFrozenTubeList().size());

        frozenBoxNew.setMemo(stockInBoxForDataSplit.getMemo());
        frozenBoxNew = frozenBoxRepository.save(frozenBoxNew);

        stockInBoxForDataSplit.setFrozenBoxId(frozenBoxNew.getId());
//        TranshipBox transhipBox = transhipBoxRepository.findByFrozenBoxCode(frozenBoxNew.getFrozenBoxCode());

        //新增入库盒子
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,frozenBoxNew.getFrozenBoxCode())!=null?
            stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,frozenBoxNew.getFrozenBoxCode()):new StockInBox();
        stockInBox.setStockIn(stockIn);
        stockInBox.setFrozenBox(frozenBoxNew);
        stockInBox.setFrozenBoxCode(frozenBoxNew.getFrozenBoxCode());
        stockInBox.setStockInCode(stockInCode);
        stockInBox.setEquipment(equipment!=null&&equipment.getId()!=null?equipment:null);
        stockInBox.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);
        stockInBox.setArea(area!=null&&area.getId()!=null?area:null);
        stockInBox.setAreaCode(area!=null?area.getAreaCode():null);
        stockInBox.setSupportRack(supportRack!=null&&supportRack.getId()!=null?supportRack:null);
        stockInBox.setSupportRackCode(supportRack!=null?supportRack.getSupportRackCode():null);
        stockInBox.setColumnsInShelf(stockInBoxForDataSplit.getColumnsInShelf());
        stockInBox.setRowsInShelf(stockInBoxForDataSplit.getRowsInShelf());
        stockInBox.setStatus(Constants.FROZEN_BOX_STOCKING);
        stockInBox.setCountOfSample(stockInBoxForDataSplit.getStockInFrozenTubeList().size());
        stockInBoxRepository.save(stockInBox);
        stockInBoxForDataSplit.setFrozenBoxId(stockInBox.getId());
        List<StockInTubeDTO> stockInTubeDTOS = stockInBoxForDataSplit.getStockInFrozenTubeList();
        List<StockInTubeDTO> stockInTubeDTOList = new ArrayList<>();
        for(StockInTubeDTO tube : stockInTubeDTOS){
            if(tube.getId()==null){
                throw new BankServiceException("冻存管ID不能为空！",tube.toString());
            }
            FrozenTube frozenTube = frozenTubeRepository.findOne(tube.getId());
            if(frozenTube == null){
                throw new BankServiceException("冻存管不存在",tube.toString());
            }
            if(tube.getFrozenBoxCode()!=null&&tube.getFrozenBoxCode().equals(frozenTube.getFrozenBox().getFrozenBoxCode())
                &&tube.getTubeRows().equals(frozenTube.getTubeRows())
                &&tube.getTubeColumns().equals(frozenTube.getTubeColumns())){
                continue;
            }
            //更改管子的位置信息
            frozenTube.setFrozenBox(frozenBoxNew);
            frozenTube.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
            frozenTube.setTubeColumns(tube.getTubeColumns());
            frozenTube.setTubeRows(tube.getTubeRows());
            //如果管子的样本信息为99，更改管子的样本类型为分装后冻存盒的样本类型
            if(frozenTube.getSampleType()!=null&&frozenTube.getSampleType().getIsMixed().equals(Constants.YES)){
                frozenTube.setSampleType(frozenBoxNew.getSampleType());
                frozenTube.setSampleTypeCode(frozenBoxNew.getSampleTypeCode());
                frozenTube.setSampleTypeName(frozenBoxNew.getSampleTypeName());
            }
            frozenTube.setSampleClassification(frozenBoxNew.getSampleClassification());
            frozenTubeRepository.save(frozenTube);
            tube.setId(frozenTube.getId());
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
        StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,boxCode);
        if(stockInBox == null){
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
        FrozenBox oldFrozenBox = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(equipment.getEquipmentCode(),
            area.getAreaCode(),shelf.getSupportRackCode(),boxPositionDTO.getColumnsInShelf(),boxPositionDTO.getRowsInShelf());

        if(oldFrozenBox!=null && oldFrozenBox.getId()!=frozenBox.getId() && !oldFrozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID)){
            throw new BankServiceException("此位置已存放冻存盒，请更换其他位置！",boxPositionDTO.toString());
        }

        frozenBox.setEquipmentCode(equipment.getEquipmentCode());
        frozenBox.setEquipment(equipment);
        frozenBox.setArea(area);
        frozenBox.setAreaCode(area.getAreaCode());
        frozenBox.setSupportRack(shelf);
        frozenBox.setSupportRackCode(shelf.getSupportRackCode());
        frozenBox.setColumnsInShelf(boxPositionDTO.getColumnsInShelf());
        frozenBox.setRowsInShelf(boxPositionDTO.getRowsInShelf());
        frozenBox.setStatus(Constants.FROZEN_BOX_PUT_SHELVES);
        frozenBoxRepository.save(frozenBox);

        stockInBox.setEquipmentCode(frozenBox.getEquipmentCode());
        stockInBox.setFrozenBox(frozenBox);
        stockInBox.setEquipment(frozenBox.getEquipment());
        stockInBox.setArea(frozenBox.getArea());
        stockInBox.setAreaCode(frozenBox.getAreaCode());
        stockInBox.setSupportRack(frozenBox.getSupportRack());
        stockInBox.setSupportRackCode(frozenBox.getSupportRackCode());
        stockInBox.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBox.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBox.setStatus(Constants.FROZEN_BOX_PUT_SHELVES);
        stockInBoxRepository.save(stockInBox);

        //保存上架位置
        StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
        stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_PENDING).memo(stockInBox.getMemo())
            .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
            .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
            .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
            .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
            .stockInBox(stockInBox);
        stockInBoxPositionRepository.save(stockInBoxPosition);

        stockInBoxDetail = createStockInBoxDetail(frozenBox,stockInCode);
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
        stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
        stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
        stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
        stockInBoxDetail.setStatus(frozenBox.getStatus());
        return stockInBoxDetail;
    }

    /**
     * 撤销上架--输入入库单编码和盒子编码，返回保存后的盒子信息
     * @param stockInCode
     * @param boxCode
     * @return
     */
    @Override
    public StockInBoxDetail movedDownStockIn(String stockInCode, String boxCode) {
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！",boxCode);
        }
        StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,boxCode);
        if(stockInBox == null){
            throw new BankServiceException("未查询到该盒子的待入库信息！",boxCode);
        }
        //取撤消前的位置
        StockInBoxPosition stockInBoxPos = stockInBoxPositionRepository.findByStockInBoxIdLast(stockInBox.getId());
        if(stockInBoxPos == null){
            throw new BankServiceException("未查询到该盒子的位置历史！",boxCode);
        }
        frozenBox.setEquipmentCode(stockInBoxPos.getEquipmentCode());
        frozenBox.setEquipment(stockInBoxPos.getEquipment());
        frozenBox.setArea(stockInBoxPos.getArea());
        frozenBox.setAreaCode(stockInBoxPos.getAreaCode());
        frozenBox.setSupportRack(stockInBoxPos.getSupportRack());
        frozenBox.setSupportRackCode(stockInBoxPos.getSupportRackCode());
        frozenBox.setColumnsInShelf(stockInBoxPos.getColumnsInShelf());
        frozenBox.setRowsInShelf(stockInBoxPos.getRowsInShelf());
        frozenBox.setStatus(Constants.FROZEN_BOX_STOCKING);
        frozenBoxRepository.save(frozenBox);

        stockInBox.setEquipmentCode(frozenBox.getEquipmentCode());
        stockInBox.setFrozenBox(frozenBox);
        stockInBox.setEquipment(frozenBox.getEquipment());
        stockInBox.setArea(frozenBox.getArea());
        stockInBox.setAreaCode(frozenBox.getAreaCode());
        stockInBox.setSupportRack(frozenBox.getSupportRack());
        stockInBox.setSupportRackCode(frozenBox.getSupportRackCode());
        stockInBox.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBox.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBox.setStatus(Constants.FROZEN_BOX_STOCKING);
        stockInBoxRepository.save(stockInBox);
        //增加冻存盒位置记录
        List<StockInBoxPosition> stockInBoxPositions =  stockInBoxPositionRepository.findByStockInBoxIdAndStatus(stockInBox.getId(),Constants.STOCK_IN_BOX_POSITION_PENDING);
        if(stockInBoxPositions.size() == 0){
            throw new BankServiceException("未查询到该冻存盒的待入库记录！",frozenBox.toString());
        }
        //保存上架位置
        StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
        stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_CANCEL).memo(stockInBox.getMemo())
            .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
            .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
            .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
            .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
            .stockInBox(stockInBox);
        stockInBoxPositionRepository.save(stockInBoxPosition);
//        TranshipBox transhipBox = transhipBoxRepository.findByFrozenBoxCode(frozenBox.getFrozenBoxCode());
        //保存冻存管历史
//        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(boxCode);
//        for(FrozenTube tube : frozenTubes){
//            //保存入库与冻存管的关系
//            StockInTubes stockInTubes = new StockInTubes();
//            stockInTubes.setMemo(tube.getMemo());
//            stockInTubes.setStatus(Constants.FROZEN_BOX_STOCKING);
//            stockInTubes.setColumnsInTube(tube.getTubeColumns());
//            stockInTubes.setRowsInTube(tube.getTubeRows());
//            stockInTubes.setFrozenBoxPosition(frozenBoxPos);
//            stockInTubes.setFrozenTube(tube);
//            stockInTubes.setFrozenTubeCode(tube.getFrozenTubeCode());
//            stockInTubes.setSampleCode(tube.getSampleCode());
//            stockInTubes.setStockInBox(stockInBox);
//            stockInTubes.setTranshipBox(transhipBox);
//            stockInTubes.setSampleTempCode(tube.getSampleTempCode());
//            stockInTubesRepository.save(stockInTubes);
//        }

        stockInBoxDetail = createStockInBoxDetail(frozenBox,stockInCode);

        return stockInBoxDetail;
    }
}
