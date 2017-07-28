package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.StockInBoxService;
import org.fwoxford.service.StockInService;
import org.fwoxford.service.StockListService;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.mapping.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private StockInBoxPositionRepository stockInBoxPositionRepository;
    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;
    @Autowired
    private StockInService stockInService;
    @Autowired
    private FrozenTubeMapper frozenTubeMapper;
    @Autowired
    private FrozenTubeTypeRepository frozenTubeTypeRepository;
    @Autowired
    private FrozenBoxService frozenBoxService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectSiteRepository projectSiteRepository;
    @Autowired
    private StockListService stockListService;

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
    public DataTablesOutput<StockInBoxForDataTableEntity> getPageStockInBoxes(String stockInCode, DataTablesInput input) {
        input.addColumn("stockInCode",true, true, stockInCode+"+");
        List<Column> columns = input.getColumns();
        List<Order> orders = new ArrayList<Order>();

        input.getOrder().forEach(order -> {
            orders.add(order);
            Column column = columns.get(order.getColumn());
            Order o = new Order();
            if(column.getData()!=""&&column.getData().equals("status")){
                Order orderAdd = new Order(6,order.getDir());
                orders.add(orderAdd);
            }
        });
        input.setOrder(orders);
        Converter<StockInBoxForDataTableEntity,StockInBoxForDataTableEntity> convert = new Converter<StockInBoxForDataTableEntity, StockInBoxForDataTableEntity>() {
            @Override
            public StockInBoxForDataTableEntity convert(StockInBoxForDataTableEntity source) {
                String position = BankUtil.getPositionString(source.getEquipmentCode(),source.getAreaCode(),source.getSupportRackCode(),source.getColumnsInShelf(),source.getRowsInShelf(),null,null);
                return new StockInBoxForDataTableEntity(source.getId(),source.getCountOfSample(),source.getStatus(),
                    source.getFrozenBoxCode(),source.getSampleTypeName(),position,source.getIsSplit(),
                    source.getSampleClassificationName(),source.getStockInCode(),source.getEquipmentCode(),
                    source.getAreaCode(),source.getSupportRackCode(),source.getRowsInShelf(),source.getColumnsInShelf(),source.getSampleTypeCode(),source.getSampleClassificationCode());
            }
        };
        DataTablesOutput<StockInBoxForDataTableEntity> output = stockInBoxRepositries.findAll(input,convert);
        return output;
    }



    public String toStockInBoxPosition(StockInBox stockInBox) {
        String position = "";

        if(stockInBox == null){
            return null;
        }
        ArrayList<String> positions = new ArrayList<>();
        if (stockInBox.getEquipmentCode() != null && stockInBox.getEquipmentCode().length() > 0){
            positions.add(stockInBox.getEquipmentCode());
        }

        if (stockInBox.getAreaCode() != null && stockInBox.getAreaCode().length() > 0) {
            positions.add(stockInBox.getAreaCode());
        }

        if (stockInBox.getSupportRackCode() != null && stockInBox.getSupportRackCode().length() > 0){
            positions.add(stockInBox.getSupportRackCode());
        }

        if (stockInBox.getRowsInShelf() != null && stockInBox.getRowsInShelf().length() > 0 && stockInBox.getColumnsInShelf() != null && stockInBox.getColumnsInShelf().length() > 0){
            positions.add(stockInBox.getColumnsInShelf()+stockInBox.getRowsInShelf());
        }

        return String.join(".", positions);
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
        stockInBoxRepository.save(frozenBoxForSplit);
        frozenBoxRepository.save(frozenBox);
      return stockInBoxForDataSplitList;
    }

    public StockInBoxForSplit splitedStockInSave(StockInBoxForSplit stockInBoxForDataSplit, List<SampleType> sampleTypes, List<Equipment> equipments, List<Area> areas, List<SupportRack> supportRacks, FrozenBox frozenBox, String stockInCode, List<FrozenBoxType> frozenBoxTypeList,List<SampleClassification> sampleClassifications) {
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
        if(frozenBox.getSampleType().getIsMixed().equals(Constants.NO)&&!sampleType.getSampleTypeCode().equals("97")
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

        List<StockInTubeDTO> stockInTubeDTOS = stockInBoxForDataSplit.getStockInFrozenTubeList();
        List<StockInTubeDTO> stockInTubeDTOList = new ArrayList<>();
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
        List<Long> frozenTubeIds = new ArrayList<Long>();
        int countOfSample = 0;
        for(FrozenTube f :frozenTubeList){
            frozenTubeIds.add(f.getId());
        }
        for(StockInTubeDTO tube : stockInTubeDTOS){

            if(tube.getId()==null){
                throw new BankServiceException("冻存管ID不能为空！",tube.toString());
            }
            FrozenTube frozenTube = frozenTubeRepository.findOne(tube.getId());
            if(frozenTube == null){
                throw new BankServiceException("冻存管不存在",tube.toString());
            }
            if(!frozenTubeIds.contains(tube.getId())){
                continue;
            }
            if(tube.getFrozenBoxCode()!=null&&tube.getFrozenBoxCode().equals(frozenTube.getFrozenBox().getFrozenBoxCode())
                &&tube.getTubeRows().equals(frozenTube.getTubeRows())
                &&tube.getTubeColumns().equals(frozenTube.getTubeColumns())){
                continue;
            }
            countOfSample++;
            Long count = frozenTubeRepository.countFrozenTubeListByBoxCode(tube.getFrozenBoxCode());
            if(count.intValue()==100){
                throw new BankServiceException("冻存盒已满，不能继续分装！");
            }
            //更改管子的位置信息
            frozenTube.setFrozenBox(frozenBoxNew);
            frozenTube.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
            frozenTube.setTubeColumns(tube.getTubeColumns());
            frozenTube.setTubeRows(tube.getTubeRows());
            //如果管子的样本信息为99，更改管子的样本类型为在该项目下配置的样本分类对应的类型
            String sampleCode = frozenTube.getSampleCode()!=null?frozenTube.getSampleCode():frozenTube.getSampleTempCode();
            if(!frozenBoxNew.getSampleTypeCode().equals("97")&&(frozenTube.getSampleType().getIsMixed().equals(Constants.YES)&&frozenTube.getSampleClassification()==null)){
                throw new BankServiceException("编码为"+sampleCode+"的样本无样本类型，为问题样本，不能分装到类型为"+frozenBoxNew.getSampleTypeName()+"的冻存盒内，只能分装到问题样本冻存盒内！");
            }
            if(frozenTube.getSampleType()!=null&&frozenTube.getSampleType().getIsMixed().equals(Constants.YES)){
                if(frozenBox.getSampleTypeCode().equals("99")&&frozenTube.getSampleClassification()!=null){
                    List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findSampleTypeByProjectAndSampleClassification(frozenBoxNew.getProjectCode(),frozenTube.getSampleClassification().getSampleClassificationCode());
                    if(projectSampleClassList == null || projectSampleClassList.size()==0){
                        throw new BankServiceException("该"+sampleCode+"冻存管在"+frozenBoxNew.getProjectCode()+"下未配置样本分类，请联系管理员！");
                    }else {
                        frozenTube.setSampleType(projectSampleClassList.get(0).getSampleType());
                        frozenTube.setSampleTypeCode(projectSampleClassList.get(0).getSampleType().getSampleTypeCode());
                        frozenTube.setSampleTypeName(projectSampleClassList.get(0).getSampleType().getSampleTypeName());
                    }
                }
            }
            if(frozenBoxNew.getSampleClassification()!=null
                &&frozenTube.getSampleClassification()!=null
                &&frozenTube.getSampleClassification().getId()!=frozenBoxNew.getSampleClassification().getId()){
                throw new BankServiceException("样本分类不一致，不能进行分装！");
            }
            frozenTubeRepository.saveAndFlush(frozenTube);
            tube.setId(frozenTube.getId());
            stockInTubeDTOList.add(tube);
        }
        StockInBox oldStockBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,frozenBoxNew.getFrozenBoxCode());
        int countOfOldBox = oldStockBox!=null&&oldStockBox.getCountOfSample()!=null?oldStockBox.getCountOfSample():0;

        stockInBox.setCountOfSample(countOfOldBox+countOfSample);
        stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
            .sampleClassification(frozenBox.getSampleClassification())
            .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
            .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
            .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
            .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
            .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
            .projectSiteName(frozenBox.getProjectSiteName());
        stockInBoxRepository.save(stockInBox);
        stockInBoxForDataSplit.setFrozenBoxId(stockInBox.getId());
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
        StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
        stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_PENDING).memo(frozenBox.getMemo())
            .equipment(frozenBox.getEquipment()).equipmentCode(frozenBox.getEquipmentCode())
            .area(frozenBox.getArea()).areaCode(frozenBox.getAreaCode())
            .supportRack(frozenBox.getSupportRack()).supportRackCode(frozenBox.getSupportRackCode())
            .columnsInShelf(frozenBox.getColumnsInShelf()).rowsInShelf(frozenBox.getRowsInShelf());
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
        SupportRack shelf = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),boxPositionDTO.getSupportRackCode());
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
        stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
            .sampleClassification(frozenBox.getSampleClassification())
            .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
            .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
            .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
            .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
            .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
            .projectSiteName(frozenBox.getProjectSiteName());
        stockInBoxRepository.save(stockInBox);

        //保存上架位置
        stockInBoxPosition.stockInBox(stockInBox);
        stockInBoxPositionRepository.save(stockInBoxPosition);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(stockInBox.getFrozenBoxCode());
        for(FrozenTube tube :frozenTubeList){
            tube.setFrozenTubeState(Constants.FROZEN_BOX_PUT_SHELVES);
            frozenTubeRepository.saveAndFlush(tube);
        }
        stockInBoxDetail = frozenBoxService.createStockInBoxDetail(frozenBox,stockInCode);
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
        stockInBoxs = frozenBoxService.frozenBoxesToStockInBoxForDataTables(frozenBoxes);
        return stockInBoxs;
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
        stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
            .sampleClassification(frozenBox.getSampleClassification())
            .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
            .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
            .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
            .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
            .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
            .projectSiteName(frozenBox.getProjectSiteName());
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
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(stockInBox.getFrozenBoxCode());
        for(FrozenTube tube :frozenTubeList){
            tube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKING);
            frozenTubeRepository.saveAndFlush(tube);
        }
        stockInBoxDetail = frozenBoxService.createStockInBoxDetail(frozenBox,stockInCode);
        return stockInBoxDetail;
    }

    /**
     * 创建入库盒
     * @param frozenBoxDTO
     * @param stockInCode
     * @return
     */
    @Override
    public synchronized FrozenBoxDTO createBoxByStockIn(FrozenBoxDTO frozenBoxDTO, String stockInCode) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("入库记录不存在！");
        }

        //验证冻存盒编码是否重复，验证库存中冻存盒是否在另一个入库单内已满，如果在另一个入库单内已满，不能在这次入库单中出现该冻存盒
        Boolean flag = checkFrozenCode(frozenBoxDTO,stockInCode);
        //判断是否是原库存的冻存盒----如果该冻存盒有其他的入库信息，就是原库存中的冻存盒
        List<StockInBox> stockInBoxes = stockInBoxRepository.findByFrozenBoxCode(frozenBoxDTO.getFrozenBoxCode());
        FrozenBox frozenBox = new FrozenBox();
        for(StockInBox s :stockInBoxes){
            if(!s.getStockInCode().equals(stockInCode)){
                frozenBox = s.getFrozenBox();
            }
        }
        SampleType entity = sampleTypeRepository.findOne(frozenBoxDTO.getSampleTypeId());
        if(frozenBox.getId()==null){//新冻存盒
            //保存冻存盒信息
            frozenBoxDTO.setIsSplit(Constants.NO);
            //更改冻存盒的项目
            frozenBoxDTO = createFrozenBoxByStockInProject(frozenBoxDTO,stockIn);
            //冻存盒类型
            frozenBoxDTO = createFrozenBoxByFrozenBoxType(frozenBoxDTO);
            //冻存盒样本类型
            if(frozenBoxDTO.getSampleTypeId() == null){
                throw new BankServiceException("冻存盒样本类型不能为空！");
            }
            frozenBoxDTO = createFrozenBoxBySampleType(frozenBoxDTO,entity);
            //冻存盒样本分类验证
            frozenBoxDTO = createFrozenBoxBySampleClass(frozenBoxDTO,entity);
            //冻存盒位置验证
            frozenBoxDTO = createFrozenBoxByPosition(frozenBoxDTO);
            frozenBoxDTO.setStatus(Constants.FROZEN_BOX_STOCKING);
            frozenBox = frozenBoxMapper.frozenBoxDTOToFrozenBox(frozenBoxDTO);
        }
        frozenBox.setStatus(Constants.FROZEN_BOX_STOCKING);
        frozenBox.setMemo(frozenBoxDTO.getMemo());
        frozenBox = frozenBoxRepository.save(frozenBox);
        frozenBoxDTO.setId(frozenBox.getId());
        //保存入库冻存盒信息
        StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,frozenBox.getFrozenBoxCode());
        if(stockInBox == null){
            stockInBox = stockInService.createStockInBox(frozenBox,stockIn);
        }

        int countOfStockInTube = 0;

        //保存冻存管信息
        for(FrozenTubeDTO tubeDTO:frozenBoxDTO.getFrozenTubeDTOS()){
            if(!StringUtils.isEmpty(tubeDTO.getFrozenTubeState())
                &&(tubeDTO.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKING)
                ||tubeDTO.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
                ||tubeDTO.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER))){
                countOfStockInTube++;
            }
            //取原冻存管的患者信息
            FrozenTube frozenTube = null;
            String tubeflag = Constants.FROZEN_FLAG_3;//盒内新增
            if(tubeDTO.getId() != null){
                frozenTube = frozenTubeRepository.findOne(tubeDTO.getId());
                if(frozenTube == null){
                    throw new BankServiceException("冻存管不存在！");
                }
                //查询冻存管历史
                List<FrozenTubeHistory> frozenTubeHistories = stockListService.findFrozenTubeHistoryDetail(tubeDTO.getId());
                FrozenTubeHistory frozenTubeHistory = frozenTubeHistories.size()>0?frozenTubeHistories.get(0):null;
                if(frozenTubeHistory != null &&
                    (!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)&&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER))){
                    //原盒原库存
                    tubeflag = Constants.FROZEN_FLAG_2;
                }else if(frozenTubeHistory != null && frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT) || frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER)){
                    //出库再回来
                    tubeflag = Constants.FROZEN_FLAG_1;
                }
            }
            if(tubeflag.equals(Constants.FROZEN_FLAG_1)){//出库再回来
                frozenTube.setMemo(frozenTube.getMemo()+","+tubeDTO.getMemo());
                frozenTube.setSampleVolumns(tubeDTO.getSampleVolumns());
            }else if(tubeflag.equals(Constants.FROZEN_FLAG_2)){//原盒原库存
                continue;
            }else{
                //项目编码
                tubeDTO = createFrozenTubeByProject(frozenBoxDTO,tubeDTO);
                //样本类型---如果冻存盒不是混合的，则需要验证冻存管的样本类型和样本分类是否与冻存盒是一致的，反之，则不验证
                if(entity.getIsMixed().equals(Constants.NO)){
                    if(tubeDTO.getSampleTypeId() == null){
                        throw new BankServiceException("冻存管样本类型不能为空！");
                    }
                    if(entity.getId() != tubeDTO.getSampleTypeId()){
                        throw new BankServiceException("样本类型与冻存盒的样本类型不符！");
                    }
                }
                tubeDTO = createFrozenTubeBySampleType(tubeDTO);
                //验证冻存管是否重复
                List<FrozenTube> frozenTubeList = new ArrayList<FrozenTube>();
                if(tubeDTO.getSampleClassificationId()==null){
                    frozenTubeList = frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeIdAndStatusNot(tubeDTO.getSampleCode(),tubeDTO.getProjectCode(),tubeDTO.getSampleTypeId(),Constants.INVALID);
                }else{
                    frozenTubeList = frozenTubeRepository.findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(tubeDTO.getSampleCode(),tubeDTO.getProjectCode(),frozenBox.getId(),tubeDTO.getSampleTypeId(),tubeDTO.getSampleClassificationId());
                }
                for(FrozenTube f:frozenTubeList){
                    if(tubeDTO.getId()==null ||
                        (tubeDTO.getId()!=null&&!f.getId().equals(tubeDTO.getId()))){
                        throw new BankServiceException("冻存管编码"+tubeDTO.getSampleCode()+"已经存在，不能保存该冻存管！",tubeDTO.getSampleCode());
                    }
                }
                if(tubeDTO.getId()==null){
                    tubeDTO.setSampleUsedTimes(tubeDTO.getSampleUsedTimes()!=null?tubeDTO.getSampleUsedTimes():0);
                }
                tubeDTO.setFrozenBoxId(frozenBox.getId());
                tubeDTO.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                //冻存管类型
                tubeDTO = createFrozenTubeTypeInit(tubeDTO);
                frozenTube = frozenTubeMapper.frozenTubeDTOToFrozenTube(tubeDTO);
            }
            frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKING);
            frozenTubeRepository.save(frozenTube);
        }
        stockInBox.setCountOfSample(countOfStockInTube);
        stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
            .sampleClassification(frozenBox.getSampleClassification())
            .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
            .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
            .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
            .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
            .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
            .projectSiteName(frozenBox.getProjectSiteName());
        stockInBoxRepository.save(stockInBox);
        return frozenBoxDTO;
    }

    public FrozenTubeDTO createFrozenTubeByProject(FrozenBoxDTO frozenBoxDTO, FrozenTubeDTO tubeDTO) {
        if(tubeDTO.getProjectId()==null){
            tubeDTO.setProjectId(frozenBoxDTO.getProjectId());
            tubeDTO.setProjectCode(frozenBoxDTO.getProjectCode());
        }else {
            Project project = projectRepository.findOne(tubeDTO.getProjectId());
            if(project ==null){
                throw new BankServiceException("项目不存在！");
            }
            tubeDTO.setProjectCode(project.getProjectCode());
        }
        if(tubeDTO.getProjectSiteId()==null){
            tubeDTO.setProjectSiteId(frozenBoxDTO.getProjectSiteId());
            tubeDTO.setProjectSiteCode(frozenBoxDTO.getProjectSiteCode());
        }else {
            ProjectSite projectSite = projectSiteRepository.findOne(tubeDTO.getProjectSiteId());
            if(projectSite ==null){
                throw new BankServiceException("项目点不存在！");
            }
            tubeDTO.setProjectSiteCode(projectSite.getProjectSiteCode());
        }

        return tubeDTO;
    }

    /**
     * true:冻存盒编码不重复
     * false:冻存盒编码重复，冻存盒已在另一个入库单中满盒入库
     * @param frozenBoxDTO
     * @param stockInCode
     * @return
     */
    public Boolean checkFrozenCode(FrozenBoxDTO frozenBoxDTO, String stockInCode) {
        if(frozenBoxDTO == null){
            return false;
        }
        Boolean flag = true;
        String frozenBoxCode = frozenBoxDTO.getFrozenBoxCode();
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if(frozenBox !=null && !frozenBox.getId().equals(frozenBoxDTO.getId())
            &&  !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
            &&  !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)
            && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED) ){
            flag=false;
            throw new BankServiceException("冻存盒编码已存在！");
        }
        String columns = frozenBox != null && frozenBox.getFrozenBoxTypeColumns()!=null?frozenBox.getFrozenBoxTypeColumns():new String("0");
        String rows = frozenBox != null &&  frozenBox.getFrozenBoxTypeRows()!=null?frozenBox.getFrozenBoxTypeRows():new String("0");
        int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
        Long countOfSample = frozenTubeRepository.countFrozenTubeListByBoxCode(frozenBoxCode);
        if(allCounts!=0&&frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED) && countOfSample.intValue()==allCounts){
            flag=false;
            throw new BankServiceException("冻存盒在另一个入库单中已满！");
        }
        return flag;
    }

    public String getFrozenTubeStatus(FrozenTubeDTO tubeDTO) {
        String status = "";
        List<Object[]> frozenTubeHistoryList =  frozenTubeRepository.findFrozenTubeHistoryListBySampleAndProjectCode(tubeDTO.getSampleCode(),tubeDTO.getProjectCode());
        if(frozenTubeHistoryList.size()>0){
            Object[] object = frozenTubeHistoryList.get(0);
            Object obj = object[12];
            if(obj !=null){
                status = obj.toString();
            }
        }
        return status;
    }

    public FrozenTubeDTO createFrozenTubeByOldFrozenTube(FrozenTube frozenTube ,FrozenTubeDTO tubeDTO) {
       if(frozenTube == null || frozenTube.getId()==null){
           return tubeDTO;
       }
        tubeDTO.setSampleUsedTimes(frozenTube.getSampleUsedTimes());
        tubeDTO.setPatientId(frozenTube.getPatientId());
        tubeDTO.setAge(frozenTube.getAge());
        tubeDTO.setDiseaseType(frozenTube.getDiseaseType());
        tubeDTO.setDob(frozenTube.getDob());
        tubeDTO.setErrorType(frozenTube.getErrorType());
        tubeDTO.setGender(frozenTube.getGender());
        tubeDTO.setIsBloodLipid(frozenTube.isIsBloodLipid());
        tubeDTO.setIsHemolysis(frozenTube.isIsHemolysis());
        tubeDTO.setSampleTempCode(frozenTube.getSampleTempCode());
        tubeDTO.setVisitDate(frozenTube.getVisitDate());
        tubeDTO.setVisitType(frozenTube.getVisitType());
        return tubeDTO;
    }

    public FrozenTubeDTO createFrozenTubeBySampleType(FrozenTubeDTO tubeDTO) {
        if(tubeDTO == null){
            return  tubeDTO;
        }
        if(tubeDTO.getSampleTypeId() == null){
            throw new BankServiceException("冻存管样本类型不能为空！");
        }
        SampleType entity = sampleTypeRepository.findOne(tubeDTO.getSampleTypeId() );
        if(entity == null){
            throw new BankServiceException("冻存管样本类型不存在！");
        }
        tubeDTO.setSampleTypeCode(entity.getSampleTypeCode());
        tubeDTO.setSampleTypeName(entity.getSampleTypeName());
        return tubeDTO;
    }

    public FrozenTubeDTO createFrozenTubeTypeInit(FrozenTubeDTO tubeDTO) {
        //如果冻存管类型未选择，默认第一条
        if(tubeDTO == null){
            return tubeDTO;
        }
        FrozenTubeType frozenTubeType = new FrozenTubeType();
        if(tubeDTO.getFrozenTubeTypeId() ==null){
            frozenTubeType = frozenTubeTypeRepository.findTopOne();
        }else {
            frozenTubeType = frozenTubeTypeRepository.findOne(tubeDTO.getFrozenTubeTypeId());
        }
        if(frozenTubeType ==null){
            throw new BankServiceException("冻存管类型不存在！");
        }
        tubeDTO.setFrozenTubeTypeId(frozenTubeType.getId());
        tubeDTO.setFrozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode());
        tubeDTO.setFrozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName());
        tubeDTO.setFrozenTubeVolumnsUnit(frozenTubeType.getFrozenTubeVolumnUnit());
        tubeDTO.setFrozenTubeVolumns(frozenTubeType.getFrozenTubeVolumn());
        tubeDTO.setSampleUsedTimesMost(frozenTubeType.getSampleUsedTimesMost());
        return tubeDTO;
    }

    public FrozenBoxDTO createFrozenBoxByPosition(FrozenBoxDTO frozenBoxDTO) {
        if(frozenBoxDTO == null){
            return  frozenBoxDTO;
        }
        if(frozenBoxDTO.getEquipmentId()!=null){
            Equipment equipment = equipmentRepository.findOne(frozenBoxDTO.getEquipmentId());
            if(equipment==null){
                throw new BankServiceException("设备不存在！");
            }
            frozenBoxDTO.setEquipmentCode(equipment.getEquipmentCode());
        }
        if(frozenBoxDTO.getAreaId()!=null){
            Area area = areaRepository.findOne(frozenBoxDTO.getAreaId());
            if(area==null){
                throw new BankServiceException("区域不存在！");
            }
            frozenBoxDTO.setAreaCode(area.getAreaCode());
        }
        if(frozenBoxDTO.getSupportRackId()!=null){
            SupportRack supportRack = supportRackRepository.findOne(frozenBoxDTO.getSupportRackId());
            if(supportRack==null){
                throw new BankServiceException("冻存架不存在！");
            }
            frozenBoxDTO.setSupportRackCode(supportRack.getSupportRackCode());
        }
        Boolean flag = checkPositionValid(frozenBoxDTO);
        if(flag){
            throw new BankServiceException("此位置已存放冻存盒，请更换其他位置！",frozenBoxDTO.toString());
        }
        return frozenBoxDTO;
    }

    /**
     * 根据冻存盒位置找到冻存盒，查询出冻存盒的状态，如果不为空且不是已出库，已分装，已作废的状态，表示位置被占用！
     * @param frozenBoxDTO
     * @return 已占用：true。未被占用：false
     */
    public Boolean checkPositionValid(FrozenBoxDTO frozenBoxDTO) {
        Boolean flag = false;
        if(frozenBoxDTO.getColumnsInShelf()!=null && frozenBoxDTO.getRowsInShelf()!=null){
            FrozenBox frozenBoxOld = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf
                (frozenBoxDTO.getEquipmentCode(),frozenBoxDTO.getAreaCode(),frozenBoxDTO.getSupportRackCode(),frozenBoxDTO.getColumnsInShelf(),frozenBoxDTO.getRowsInShelf());
            if(frozenBoxOld!=null && frozenBoxOld.getId()!=frozenBoxDTO.getId()
                && !frozenBoxOld.getStatus().equals(Constants.FROZEN_BOX_INVALID)
                && !frozenBoxOld.getStatus().equals(Constants.FROZEN_BOX_SPLITED)
                && !frozenBoxOld.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)){
               flag = true;
            }
        }

        return flag;
    }

    public FrozenBoxDTO createFrozenBoxBySampleClass(FrozenBoxDTO frozenBoxDTO, SampleType entity) {
        //验证项目下该样本类型是否有样本分类，如果已经配置了样本分类，则样本分类ID不能为空，（99的,98的除外）
//        if(entity.getIsMixed().equals(Constants.YES)&&frozenBoxDTO.getSampleClassificationId()!=null){
//            throw new BankServiceException("混合型样本的冻存盒，样本分类应该为空！");
//        }
        int countOfSampleClass = projectSampleClassRepository.countByProjectIdAndSampleTypeId(frozenBoxDTO.getProjectId(),frozenBoxDTO.getSampleTypeId());
        if(countOfSampleClass>0&&entity.getIsMixed().equals(Constants.NO)&&frozenBoxDTO.getSampleClassificationId()==null){
            throw new BankServiceException("该项目下已经配置样本分类，样本分类不能为空！");
        }
        return frozenBoxDTO;
    }

    public FrozenBoxDTO createFrozenBoxBySampleType(FrozenBoxDTO frozenBoxDTO, SampleType entity) {
        if(frozenBoxDTO == null){
            return  frozenBoxDTO;
        }
        if(entity == null){
            throw new BankServiceException("冻存盒样本类型不存在！");
        }
        frozenBoxDTO.setSampleTypeCode(entity.getSampleTypeCode());
        frozenBoxDTO.setSampleTypeName(entity.getSampleTypeName());
        return frozenBoxDTO;
    }

    public FrozenBoxDTO createFrozenBoxByFrozenBoxType(FrozenBoxDTO frozenBoxDTO) {
        if(frozenBoxDTO == null){
            return  frozenBoxDTO;
        }
        if(frozenBoxDTO.getFrozenBoxTypeId() == null){
            throw new BankServiceException("冻存盒类型不能为空！");
        }
        FrozenBoxType boxType = frozenBoxTypeRepository.findOne(frozenBoxDTO.getFrozenBoxTypeId());
        if(boxType == null){
            throw new BankServiceException("冻存盒类型不存在！");
        }
        frozenBoxDTO.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
        frozenBoxDTO.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
        frozenBoxDTO.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
        return frozenBoxDTO;
    }

    public FrozenBoxDTO createFrozenBoxByStockInProject(FrozenBoxDTO frozenBoxDTO, StockIn stockIn) {
        if(frozenBoxDTO == null || stockIn ==null){
            return  frozenBoxDTO;
        }
        frozenBoxDTO.setProjectId(stockIn.getProject()!=null?stockIn.getProject().getId():null);
        frozenBoxDTO.setProjectName(stockIn.getProject()!=null?stockIn.getProject().getProjectName():null);
        frozenBoxDTO.setProjectCode(stockIn.getProject()!=null?stockIn.getProject().getProjectCode():null);

        frozenBoxDTO.setProjectSiteId(stockIn.getProjectSite()!=null?stockIn.getProjectSite().getId():null);
        frozenBoxDTO.setProjectSiteName(stockIn.getProjectSite()!=null?stockIn.getProjectSite().getProjectSiteName():null);
        frozenBoxDTO.setProjectSiteCode(stockIn.getProjectSite()!=null?stockIn.getProjectSite().getProjectSiteCode():null);
        return  frozenBoxDTO;
    }
    /**
     * 根据冻存盒编码查询入库冻存盒
     * @param frozenBoxCode
     * @return
     */
    @Override
    public FrozenBoxDTO getFrozenBoxAndTubeByForzenBoxCode(String frozenBoxCode) {
        //查询冻存盒信息
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！");
        }

        //查询冻存管列表信息
        List<FrozenTube> frozenTubeList = new ArrayList<>();
        if(!frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)){
            frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBoxCode);
        }

        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<FrozenTubeDTO>();
        for(FrozenTube f: frozenTubeList){
            FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(f);
            frozenTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            frozenTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            frozenTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            frozenTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            frozenTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            frozenTubeDTO.setSampleClassificationName(f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationName():null);
            frozenTubeDTO.setSampleClassificationCode(f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationCode():null);
            frozenTubeDTO.setFlag(Constants.FROZEN_FLAG_3);//盒内新增样本
            //查询样本历史信息
            List<FrozenTubeHistory> frozenTubeHistories = stockListService.findFrozenTubeHistoryDetail(f.getId());
            FrozenTubeHistory frozenTubeHistory = frozenTubeHistories.size()>0?frozenTubeHistories.get(0):null;
            if(frozenTubeHistory != null &&
                (!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)&&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER))){
                frozenTubeDTO.setFlag(Constants.FROZEN_FLAG_2);//原盒原库存
            }else if(frozenTubeHistory != null && frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT) || frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER)){
                frozenTubeDTO.setFlag(Constants.FROZEN_FLAG_1);//出库再回来
            }
            frozenTubeDTOS.add(frozenTubeDTO);
        }

        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);

        frozenBoxDTO.setFrozenTubeDTOS(frozenTubeDTOS);
        frozenBoxDTO.setFrontColor(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getFrontColor():null);
        frozenBoxDTO.setFrontColorForClass(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getFrontColor():null);
        frozenBoxDTO.setBackColor(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getBackColor():null);
        frozenBoxDTO.setBackColorForClass(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getBackColor():null);
        frozenBoxDTO.setIsMixed(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getIsMixed():null);
        frozenBoxDTO.setSampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null);
        frozenBoxDTO.setSampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null);

        return frozenBoxDTO;
    }
}
