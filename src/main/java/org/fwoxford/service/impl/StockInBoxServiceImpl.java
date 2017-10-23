package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

import java.util.*;

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
    @Autowired
    private StockInTubeRepository stockInTubeRepository;
    @Autowired
    private StockInTubeMapper stockInTubeMapper;
    @Autowired
    FrozenBoxCheckService frozenBoxCheckService;
    @Autowired
    private  FrozenBoxImportService frozenBoxImportService;


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

    /**
     * 根据入库单查询入库冻存盒（分页）
     * @param stockInCode
     * @param input
     * @return
     */
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
                    source.getFrozenBoxCode(),source.getFrozenBoxCode1D(),source.getSampleTypeName(),position,source.getIsSplit(),
                    source.getSampleClassificationName(),source.getStockInCode(),source.getEquipmentCode(),
                    source.getAreaCode(),source.getSupportRackCode(),source.getRowsInShelf(),source.getColumnsInShelf(),source.getSampleTypeCode(),source.getSampleClassificationCode(),source.getTranshipCode());
            }
        };
        DataTablesOutput<StockInBoxForDataTableEntity> output = stockInBoxRepositries.findAll(input,convert);
        return output;
    }

    /**
     * 输入入库单编码和盒子编码，返回该入库单的某个盒子的信息
     * @param stockInCode
     * @param boxCode
     * @return
     */
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
        stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        stockInBoxDetail.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
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
            StockInBoxForSplit stockInBoxSplit = splitedStockInSave(stockInBoxForDataSplit,sampleTypes,equipments,areas,supportRacks,frozenBox,stockInCode,frozenBoxTypeList,sampleClassifications,stockIn);
            stockInBoxForDataSplitList.add(stockInBoxSplit);
        }
        //更改盒子状态
        //如果在盒子内还有剩余的管子，状态还是待入库
        StockInBox frozenBoxForSplit = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,boxCode);
        List<StockInTube> tubeList = stockInTubeRepository.findByStockInBoxId(frozenBoxForSplit.getId());
        if(tubeList.size()==0){
            frozenBox.setStatus(Constants.FROZEN_BOX_SPLITED);
            frozenBoxForSplit.setStatus(Constants.FROZEN_BOX_SPLITED);
        }
        frozenBoxForSplit.setCountOfSample(tubeList.size());
        stockInBoxRepository.save(frozenBoxForSplit);
        frozenBoxRepository.save(frozenBox);
        return stockInBoxForDataSplitList;
    }

    /**
     * 入库分装保存
     * @param stockInBoxForDataSplit
     * @param sampleTypes
     * @param equipments
     * @param areas
     * @param supportRacks
     * @param frozenBox
     * @param stockInCode
     * @param frozenBoxTypeList
     * @param sampleClassifications
     * @param stockIn
     * @return
     */
    public StockInBoxForSplit splitedStockInSave(StockInBoxForSplit stockInBoxForDataSplit, List<SampleType> sampleTypes, List<Equipment> equipments, List<Area> areas, List<SupportRack> supportRacks, FrozenBox frozenBox, String stockInCode, List<FrozenBoxType> frozenBoxTypeList, List<SampleClassification> sampleClassifications, StockIn stockIn) {
        if(stockInBoxForDataSplit.getFrozenBoxTypeId()==null){
            throw new BankServiceException("冻存盒类型不能为空！",stockInBoxForDataSplit.getFrozenBoxCode());
        }
        //判断冻存盒类型是否一致
        if(frozenBox.getFrozenBoxType().getId()!=stockInBoxForDataSplit.getFrozenBoxTypeId()){
            throw new BankServiceException("冻存盒类型不一致，不能执行分装！",stockInBoxForDataSplit.toString());
        }
        SampleType sampleType = sampleTypes.stream().filter(s-> s.getId().equals(stockInBoxForDataSplit.getSampleTypeId())).findFirst().orElse(null);
        if(sampleType == null){
            throw new BankServiceException("样本类型不存在！");
        }
        if(frozenBox.getSampleType().getIsMixed().equals(Constants.NO)&&!sampleType.getSampleTypeCode().equals("97")
            && (!sampleType.getId().equals(frozenBox.getSampleType().getId()) || sampleType.getId()!= frozenBox.getSampleType().getId())){
            throw new BankServiceException("需要分装的冻存盒与分装后的冻存盒的样本类型不一致，不能分装！");
        }
        FrozenBoxType boxType = new FrozenBoxType();
        boxType.setId(stockInBoxForDataSplit.getFrozenBoxTypeId());
        int boxTypeIndex = frozenBoxTypeList.indexOf(boxType);
        if (boxTypeIndex >= 0){
            boxType = frozenBoxTypeList.get(boxTypeIndex);
        }else{
            throw new BankServiceException("冻存盒类型不存在！",stockInBoxForDataSplit.toString());
        }
        int countOfTube = stockInBoxForDataSplit.getStockInFrozenTubeList().size();
        String columns = boxType.getFrozenBoxTypeColumns()!=null?boxType.getFrozenBoxTypeColumns():new String("0");
        String rows = boxType.getFrozenBoxTypeRows()!=null?boxType.getFrozenBoxTypeRows():new String("0");
        int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
        if(countOfTube==0||countOfTube>allCounts){
            throw new BankServiceException("冻存管数量错误！",stockInBoxForDataSplit.toString());
        }
        //新增盒子
        FrozenBox frozenBoxNew = new FrozenBox();
        if(stockInBoxForDataSplit.getFrozenBoxCode() != null){
            frozenBoxNew = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
            if(frozenBoxNew == null){
                frozenBoxNew = new FrozenBox();
            }
        }
        StockInBox stockInBoxSplitIn = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,stockInBoxForDataSplit.getFrozenBoxCode())!=null?
            stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,stockInBoxForDataSplit.getFrozenBoxCode()):new StockInBox();
        stockInBoxSplitIn.setStatus(Constants.FROZEN_BOX_STOCKING);
        if(frozenBoxNew.getId()== null){
            //盒ID为空，表示新增的冻存盒---保存冻存盒，保存入库盒
            frozenBoxNew.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
            frozenBoxNew.setFrozenBoxCode1D(stockInBoxForDataSplit.getFrozenBoxCode1D());
            frozenBoxNew.setProject(frozenBox.getProject());
            frozenBoxNew.setProjectCode(frozenBox.getProjectCode());
            frozenBoxNew.setProjectName(frozenBox.getProjectName());
            frozenBoxNew.setProjectSite(frozenBox.getProjectSite());
            frozenBoxNew.setProjectSiteName(frozenBox.getProjectSiteName());
            frozenBoxNew.setProjectSiteCode(frozenBox.getProjectSiteCode());

            frozenBoxNew.setDislocationNumber(0);
            frozenBoxNew.setEmptyHoleNumber(0);
            frozenBoxNew.setEmptyTubeNumber(0);

            frozenBoxNew.setIsRealData(frozenBox.getIsRealData());
            frozenBoxNew.setIsSplit(Constants.NO);
            frozenBoxNew.setFrozenBoxType(boxType);
            frozenBoxNew.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
            frozenBoxNew.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
            frozenBoxNew.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
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
            //验证盒子编码是否存在
            Map<String,Long> map = new HashMap<String,Long>();
            map.put(frozenBoxNew.getFrozenBoxCode(),frozenBoxNew.getId());
            frozenBoxCheckService.checkFrozenBoxCodeRepead(map);
        }
        frozenBoxNew.setStatus(Constants.FROZEN_BOX_STOCKING);
        frozenBoxNew = frozenBoxRepository.save(frozenBoxNew);
        if(stockInBoxSplitIn.getId() == null){
            stockInBoxSplitIn.setStockIn(stockIn);
            stockInBoxSplitIn.setFrozenBox(frozenBoxNew);
            stockInBoxSplitIn.setFrozenBoxCode(frozenBoxNew.getFrozenBoxCode());
            stockInBoxSplitIn.setFrozenBoxCode1D(frozenBoxNew.getFrozenBoxCode1D());
            stockInBoxSplitIn.setStockInCode(stockInCode);
            stockInBoxSplitIn.setEquipment(frozenBoxNew.getEquipment()!=null&&frozenBoxNew.getEquipment().getId()!=null?frozenBoxNew.getEquipment():null);
            stockInBoxSplitIn.setEquipmentCode(frozenBoxNew.getEquipment()!=null?frozenBoxNew.getEquipment().getEquipmentCode():null);
            stockInBoxSplitIn.setArea(frozenBoxNew.getArea()!=null&&frozenBoxNew.getArea().getId()!=null?frozenBoxNew.getArea():null);
            stockInBoxSplitIn.setAreaCode(frozenBoxNew.getArea()!=null?frozenBoxNew.getArea().getAreaCode():null);
            stockInBoxSplitIn.setSupportRack(frozenBoxNew.getSupportRack()!=null&&frozenBoxNew.getSupportRack().getId()!=null?frozenBoxNew.getSupportRack():null);
            stockInBoxSplitIn.setSupportRackCode(frozenBoxNew.getSupportRack()!=null?frozenBoxNew.getSupportRack().getSupportRackCode():null);
            stockInBoxSplitIn.setColumnsInShelf(stockInBoxForDataSplit.getColumnsInShelf());
            stockInBoxSplitIn.setRowsInShelf(stockInBoxForDataSplit.getRowsInShelf());
            stockInBoxSplitIn.setCountOfSample(0);
            stockInBoxSplitIn.sampleTypeCode(frozenBoxNew.getSampleTypeCode()).sampleType(frozenBoxNew.getSampleType()).sampleTypeName(frozenBoxNew.getSampleTypeName())
                .sampleClassification(frozenBoxNew.getSampleClassification())
                .sampleClassificationCode(frozenBoxNew.getSampleClassification()!=null?frozenBoxNew.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenBoxNew.getSampleClassification()!=null?frozenBoxNew.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(frozenBoxNew.getDislocationNumber()).emptyHoleNumber(frozenBoxNew.getEmptyHoleNumber()).emptyTubeNumber(frozenBoxNew.getEmptyTubeNumber())
                .frozenBoxType(frozenBoxNew.getFrozenBoxType()).frozenBoxTypeCode(frozenBoxNew.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBoxNew.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBoxNew.getFrozenBoxTypeRows()).isRealData(frozenBoxNew.getIsRealData()).isSplit(frozenBoxNew.getIsSplit()).project(frozenBoxNew.getProject())
                .projectCode(frozenBoxNew.getProjectCode()).projectName(frozenBoxNew.getProjectName()).projectSite(frozenBoxNew.getProjectSite()).projectSiteCode(frozenBoxNew.getProjectSiteCode())
                .projectSiteName(frozenBoxNew.getProjectSiteName());
            stockInBoxRepository.save(stockInBoxSplitIn);
        }

        List<StockInTubeDTO> stockInTubeDTOS = stockInBoxForDataSplit.getStockInFrozenTubeList();
        List<StockInTubeDTO> stockInTubeDTOList = new ArrayList<>();
        int countOfSample = 0;
        for(StockInTubeDTO tube : stockInTubeDTOS){
            if(tube.getId()==null){
                continue;
            }
            StockInTube stockInTube = stockInTubeRepository.findOne(tube.getId());
            if(stockInTube == null){
                throw new BankServiceException("入库冻存管不存在！");
            }
            FrozenTube frozenTube = stockInTube.getFrozenTube();
            if(frozenTube == null){
                throw new BankServiceException("冻存管不存在!",tube.toString());
            }
            tube.setFrozenTubeId(frozenTube.getId());
            if(tube.getFrozenBoxCode()!=null&&tube.getFrozenBoxCode().equals(frozenTube.getFrozenBoxCode())
                &&tube.getTubeRows().equals(frozenTube.getTubeRows())
                &&tube.getTubeColumns().equals(frozenTube.getTubeColumns())){
                continue;
            }
            countOfSample++;
            Long countOfSampleOriginal = frozenTubeRepository.countByFrozenBoxCodeAndFrozenTubeState(tube.getFrozenBoxCode(),Constants.FROZEN_BOX_STOCKED);
            Long countOfSampleCurrent = stockInTubeRepository.countByFrozenBoxCodeAndStockInCode(tube.getFrozenBoxCode(),stockInCode);
            if((countOfSampleOriginal.intValue()+countOfSampleCurrent.intValue())==allCounts){
                throw new BankServiceException("冻存盒已满，不能继续分装！");
            }

            //更改入库管子的位置信息
            stockInTube.setStockInBox(stockInBoxSplitIn);
            stockInTube.setFrozenBoxCode(stockInBoxForDataSplit.getFrozenBoxCode());
            stockInTube.setTubeColumns(tube.getTubeColumns());
            stockInTube.setTubeRows(tube.getTubeRows());
            //如果管子的样本信息为99，更改管子的样本类型为在该项目下配置的样本分类对应的类型
            String sampleCode = stockInTube.getSampleCode()!=null?stockInTube.getSampleCode():stockInTube.getSampleTempCode();
            if(!frozenBoxNew.getSampleTypeCode().equals("97")&&(stockInTube.getSampleType().getIsMixed().equals(Constants.YES)&&stockInTube.getSampleClassification()==null)){
                throw new BankServiceException("编码为"+sampleCode+"的样本无样本类型，为问题样本，不能分装到类型为"+frozenBoxNew.getSampleTypeName()+"的冻存盒内，只能分装到问题样本冻存盒内！");
            }
            if(stockInTube.getSampleType()!=null&&stockInTube.getSampleType().getIsMixed().equals(Constants.YES)){
                if(frozenBox.getSampleTypeCode().equals("99")&&stockInTube.getSampleClassification()!=null){
                    List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findSampleTypeByProjectAndSampleClassification(frozenBoxNew.getProjectCode(),stockInTube.getSampleClassification().getSampleClassificationCode());
                    if(projectSampleClassList == null || projectSampleClassList.size()==0){
                        throw new BankServiceException("该"+sampleCode+"冻存管在"+frozenBoxNew.getProjectCode()+"下未配置样本分类，请联系管理员！");
                    }else {
                        stockInTube.setSampleType(projectSampleClassList.get(0).getSampleType());
                        stockInTube.setSampleTypeCode(projectSampleClassList.get(0).getSampleType().getSampleTypeCode());
                        stockInTube.setSampleTypeName(projectSampleClassList.get(0).getSampleType().getSampleTypeName());
                    }
                }
            }
            if(frozenBoxNew.getSampleClassification()!=null
                &&stockInTube.getSampleClassification()!=null
                &&stockInTube.getSampleClassification().getId()!=frozenBoxNew.getSampleClassification().getId()){
                throw new BankServiceException("样本分类不一致，不能进行分装！");
            }
            stockInTubeRepository.save(stockInTube);
            tube.setId(stockInTube.getId());
            stockInTubeDTOList.add(tube);
        }
        int countOfOldBox = stockInBoxSplitIn!=null&&stockInBoxSplitIn.getCountOfSample()!=null?stockInBoxSplitIn.getCountOfSample():0;
        stockInBoxSplitIn.setCountOfSample(countOfOldBox+countOfSample);
        stockInBoxRepository.save(stockInBoxSplitIn);
        stockInBoxForDataSplit.setFrozenBoxId(stockInBoxSplitIn.getId());
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
        List<StockInTube> stockInTubes = stockInTubeRepository.findByStockInBoxId(stockInBox.getId());
        for(StockInTube tube :stockInTubes){
            tube.setFrozenTubeState(Constants.FROZEN_BOX_PUT_SHELVES);
        }
        stockInTubeRepository.save(stockInTubes);
        stockInBoxDetail = frozenBoxService.createStockInBoxDetail(stockInBox,stockInCode);
        return stockInBoxDetail;
    }

    /**
     * 根据冻存盒编码字符串返回入库盒信息
     * @param frozenBoxCodeStr
     * @return
     */
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
        //修改入库冻存管
        List<StockInTube> stockInTubes = stockInTubeRepository.findByStockInBoxId(stockInBox.getId());
        for(StockInTube stockInTube :stockInTubes){
            stockInTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKING);
        }
        stockInTubeRepository.save(stockInTubes);
        stockInBoxDetail = frozenBoxService.createStockInBoxDetail(stockInBox,stockInCode);
        return stockInBoxDetail;
    }

    /**
     * 创建入库盒
     * @param stockInBoxDTO
     * @param stockInCode
     * @return
     */
    @Override
    public synchronized StockInBoxDTO createBoxByStockIn(StockInBoxDTO stockInBoxDTO, String stockInCode) {
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        if(stockIn == null){
            throw new BankServiceException("入库记录不存在！");
        }
        if(stockInBoxDTO == null ||(stockInBoxDTO!=null && StringUtils.isEmpty(stockInBoxDTO.getFrozenBoxCode()))){
            throw new BankServiceException("冻存盒编码不能为空！");
        }
        //判断是否是原库存的冻存盒----如果该冻存盒有其他的入库信息，就是原库存中的冻存盒
        FrozenBox frozenBox = new FrozenBox();
        StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,stockInBoxDTO.getFrozenBoxCode());
        if(stockInBox != null ){//此次入库单
            frozenBox = stockInBox.getFrozenBox();
        }else{//查询其他入库单
            stockInBox = new StockInBox();
            List<StockInBox> stockInBoxes = stockInBoxRepository.findByFrozenBoxCode(stockInBoxDTO.getFrozenBoxCode());
            for(StockInBox s :stockInBoxes){
                if(s.getStockInCode()!=stockInCode && !s.getStockInCode().equals(stockInCode)){
                    frozenBox = s.getFrozenBox();
                }
            }
        }
        stockInBoxDTO.setFrozenBoxId(frozenBox.getId());
        //验证冻存盒编码是否重复，验证库存中冻存盒是否在另一个入库单内已满，如果在另一个入库单内已满，不能在这次入库单中出现该冻存盒
        Boolean flag = checkFrozenCode(stockInBoxDTO,stockInCode);
        SampleType entity = sampleTypeRepository.findOne(stockInBoxDTO.getSampleTypeId());
        if(frozenBox.getId()==null){//新冻存盒
            //更改冻存盒的项目
            frozenBox = createFrozenBoxByStockInProject(frozenBox,stockInBoxDTO,stockIn);
            //冻存盒类型
            frozenBox = createFrozenBoxByFrozenBoxType(frozenBox,stockInBoxDTO);
            //冻存盒样本类型
            if(stockInBoxDTO.getSampleTypeId() == null){
                throw new BankServiceException("冻存盒样本类型不能为空！");
            }
            frozenBox = createFrozenBoxBySampleType(frozenBox,stockInBoxDTO,entity);
            //冻存盒样本分类验证
            frozenBox = createFrozenBoxBySampleClass(frozenBox,stockInBoxDTO,entity);
            //冻存盒位置验证
            frozenBox = createFrozenBoxByPosition(frozenBox,stockInBoxDTO);
            frozenBox.setStatus(Constants.FROZEN_BOX_STOCKING);
            frozenBox.setFrozenBoxCode(stockInBoxDTO.getFrozenBoxCode());
            frozenBox.setFrozenBoxCode1D(stockInBoxDTO.getFrozenBoxCode1D());
        }else{
            if(frozenBox.getStatus().equals(Constants.FROZEN_BOX_PUT_SHELVES)){
                throw new BankServiceException("冻存盒已上架，不能执行保存！");
            }
        }
        frozenBox.setMemo(stockInBoxDTO.getMemo());
        //保存冻存盒信息
        frozenBox.setIsSplit(stockInBoxDTO.getIsSplit());
        frozenBox.setStatus(Constants.FROZEN_BOX_STOCKING);
        frozenBox = frozenBoxRepository.save(frozenBox);
        //保存入库冻存盒信息
        int countOfStockInTube = 0;
        stockInBox.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        stockInBox.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
        stockInBox.setStockIn(stockIn);
        stockInBox.setStockInCode(stockIn.getStockInCode());
        stockInBox.setMemo(frozenBox.getMemo());
        stockInBox.setStatus(frozenBox.getStatus());
        stockInBox.setFrozenBox(frozenBox);
        stockInBox.setCountOfSample(countOfStockInTube);
        stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
            .sampleClassification(frozenBox.getSampleClassification())
            .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
            .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
            .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
            .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
            .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(stockIn.getProject())
            .projectCode(stockIn.getProjectCode()).projectName(stockIn.getProject()!=null?stockIn.getProject().getProjectName():null)
            .projectSite(stockIn.getProjectSite()).projectSiteCode(stockIn.getProjectSiteCode())
            .projectSiteName(stockIn.getProjectSite()!=null?stockIn.getProjectSite().getProjectSiteName():null);
        stockInBoxRepository.save(stockInBox);
        //查询盒内原来样本
        List<StockInTube> stockInTubesOld = stockInTubeRepository.findByFrozenBoxCodeAndSampleState(frozenBox.getFrozenBoxCode());
        stockInTubesOld.addAll(stockInTubeRepository.findByStockInBoxId(stockInBox.getId()));
        List<Long> tubeIds = new ArrayList<Long>();//传入过来的入库管ID
        List<Long> frozenTubeIds = new ArrayList<Long>();//根据入库管ID，查询出需要保存的冻存管ID
        List<StockInTubeDTO> stockInTubeDTOList =  stockInBoxDTO.getFrozenTubeDTOS();//传入过来的
        for(StockInTubeDTO stockInTubeDTO : stockInTubeDTOList){
            tubeIds.add(stockInTubeDTO.getId());//构造入库管ID，为了比较是否有变更
        }
        List<StockInTube> stockInfrozenTubesForDelete = new ArrayList<StockInTube>();//比较出来的，有删除的入库冻存管List
        List<FrozenTube> OldFrozenTubeList = new ArrayList<FrozenTube>();
        JSONArray jsonArray = new JSONArray();
        Map<Long,StockInTube> mapOfTube = new HashMap<Long,StockInTube>();
        List<Long> oldFrozenTubeIds = new ArrayList<Long>();
        for(StockInTube oldTube:stockInTubesOld){//原盒内的入库冻存管
            if(!tubeIds.contains(oldTube.getId())){
                oldTube.setStatus(Constants.INVALID);
                stockInfrozenTubesForDelete.add(oldTube);
                OldFrozenTubeList.add(oldTube.getFrozenTube());
                oldFrozenTubeIds.add(oldTube.getFrozenTube().getId());
            }else{
                mapOfTube.put(oldTube.getId(),oldTube);
                frozenTubeIds.add(oldTube.getFrozenTube().getId());
            }
        }
        stockInTubeRepository.save(stockInfrozenTubesForDelete);
        if(oldFrozenTubeIds.size()>0){
            Map<Long,FrozenTubeHistory> mapOld = stockListService.findFrozenTubeHistoryDetailByIds(oldFrozenTubeIds);
            for(FrozenTube tube:OldFrozenTubeList){
                FrozenTubeHistory frozenTubeHistory = mapOld.get(tube.getId());
                if(frozenTubeHistory==null||(frozenTubeHistory!=null&&frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_TRANSHIP))){
                    tube.setStatus(Constants.INVALID);
                }
            }
            frozenTubeRepository.save(OldFrozenTubeList);
        }

        Map<Long,FrozenTubeHistory> map = frozenTubeIds.size()>0?stockListService.findFrozenTubeHistoryDetailByIds(frozenTubeIds):new HashMap<>();
        //验证冻存管编码是否重复
        for(StockInTubeDTO tubeDTO :stockInTubeDTOList){
            //验证冻存管是否重复
            List<FrozenTube> frozenTubeListForCheckRepeat = new ArrayList<FrozenTube>();
            if(tubeDTO.getSampleClassificationId()==null){
                List<String> sampleCodes = new ArrayList<>();
                sampleCodes.add(tubeDTO.getSampleCode());
                frozenTubeListForCheckRepeat = frozenTubeRepository.findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(sampleCodes,stockIn.getProjectCode(),tubeDTO.getSampleTypeId(),Constants.INVALID);
            }else{
                frozenTubeListForCheckRepeat = frozenTubeRepository.findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(tubeDTO.getSampleCode(),stockIn.getProjectCode(),frozenBox.getId(),tubeDTO.getSampleTypeId(),tubeDTO.getSampleClassificationId());
            }
            for(FrozenTube f:frozenTubeListForCheckRepeat){
                if(tubeDTO.getFrozenTubeId()==null ||
                    (tubeDTO.getFrozenTubeId()!=null&&!f.getId().equals(tubeDTO.getFrozenTubeId()))){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",f.getId());
                    jsonObject.put("sampleCode",tubeDTO.getSampleCode());
                    jsonObject.put("tubeColumns",tubeDTO.getTubeColumns());
                    jsonObject.put("tubeRows",tubeDTO.getTubeRows());
                    jsonArray.add(jsonObject);
                }
            }
            String tubeflag = Constants.FROZEN_FLAG_NEW;//盒内新增
            if(tubeDTO.getId() != null){
                FrozenTubeHistory frozenTubeHistory = map.get(tubeDTO.getFrozenTubeId());
                if(frozenTubeHistory != null &&
                    (!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)
                        &&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER)
                        &&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_TRANSHIP)
                        &&!frozenTubeHistory.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKING)
                    )){
                    //原盒原库存
                    tubeflag = Constants.FROZEN_FLAG_ORIGINAL;
                }else if(frozenTubeHistory != null && (frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)
                    || frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER))){
                    //出库再回来
                    tubeflag = Constants.FROZEN_FLAG_STOCKIN_AGAIN;
                }
            }
            tubeDTO.setFlag(tubeflag);
        }
        if(jsonArray.size()>0){
            throw new BankServiceException("盒内有重复的冻存管，不能保存！",jsonArray.toString());
        }
        List<StockInTube> stockInTubes = new ArrayList<StockInTube>();
        //保存冻存管信息
        for(StockInTubeDTO tubeDTO:stockInTubeDTOList){
            //取原冻存管的患者信息
            StockInTubeDTO frozenTubeDTOForSave = new StockInTubeDTO();
            StockInTube stockInTube = null;
            String tubeflag = tubeDTO.getFlag();
            if(tubeflag.equals(Constants.FROZEN_FLAG_ORIGINAL)){//原盒原库存
                continue;
            }
            ArrayList<String> memoList = new ArrayList<String>();
            if(tubeDTO.getId() != null){
                stockInTube = stockInTubeRepository.findOne(tubeDTO.getId());
                if(stockInTube == null){
                    throw new BankServiceException("入库冻存管不存在！");
                }
                frozenTubeDTOForSave = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);
                tubeDTO.setFrozenTubeId(frozenTubeDTOForSave.getFrozenTubeId());
                if(!StringUtils.isEmpty(stockInTube.getMemo())){
                    memoList.add(stockInTube.getMemo());
                }
            }
            frozenTubeDTOForSave.setSampleCode(tubeDTO.getSampleCode());
            frozenTubeDTOForSave.setSampleTempCode(tubeDTO.getSampleTempCode());
            frozenTubeDTOForSave.setStatus(tubeDTO.getStatus());
            if(!StringUtils.isEmpty(tubeDTO.getMemo())){
                memoList.add(tubeDTO.getMemo());
            }
            frozenTubeDTOForSave.setMemo(String.join(",",memoList));
            //项目编码
            frozenTubeDTOForSave = createFrozenTubeByProject(frozenBox,tubeDTO);
            //冻存管类型
            frozenTubeDTOForSave = createFrozenTubeTypeInit(tubeDTO);
            //样本类型---如果冻存盒不是混合的，则需要验证冻存管的样本类型和样本分类是否与冻存盒是一致的，反之，则不验证
            if(entity.getIsMixed().equals(Constants.NO)){
                if(tubeDTO.getSampleTypeId() == null){
                    throw new BankServiceException("冻存管样本类型不能为空！");
                }
                if(entity.getId() != tubeDTO.getSampleTypeId()){
                    throw new BankServiceException("样本类型与冻存盒的样本类型不符！");
                }
            }
            frozenTubeDTOForSave.setStockInBoxId(stockInBox.getId());
            frozenTubeDTOForSave = createFrozenTubeBySampleType(tubeDTO);
            frozenTubeDTOForSave.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            frozenTubeDTOForSave.setFrozenTubeState(Constants.FROZEN_BOX_STOCKING);
            stockInTube = stockInTubeMapper.stockInTubeDTOToStockInTube(frozenTubeDTOForSave);

            if(tubeflag.equals(Constants.FROZEN_FLAG_NEW)){//盒内新增样本可以编辑样本本身和入库冻存管
                FrozenTube tube= createNewFrozenTube(frozenTubeDTOForSave,frozenBox);
                stockInTube = new StockInTube();
                stockInTube.status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
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
                stockInTube.setId(frozenTubeDTOForSave.getId());
            }
            stockInTubes.add(stockInTube);
            //保存入库样本表
            countOfStockInTube++;
        }
        stockInBox.setCountOfSample(countOfStockInTube);
        stockInBoxRepository.save(stockInBox);
        stockInTubeRepository.save(stockInTubes);
        stockInBoxDTO.setId(stockInBox.getId());
        return stockInBoxDTO;
    }

    /**
     * 创建入库盒----创建冻存管
     * @param frozenTubeDTOForSave
     * @param frozenBox
     * @return
     */
    public FrozenTube createNewFrozenTube(StockInTubeDTO frozenTubeDTOForSave,FrozenBox frozenBox) {
        //保存冻存管
        FrozenTube tube = new FrozenTube().projectCode(frozenTubeDTOForSave.getProjectCode()).projectSiteCode(frozenTubeDTOForSave.getProjectSiteCode())
            .sampleCode(frozenTubeDTOForSave.getSampleCode()).sampleTempCode(frozenTubeDTOForSave.getSampleTempCode())
            .frozenTubeTypeCode(frozenTubeDTOForSave.getFrozenTubeTypeCode())
            .frozenTubeTypeName(frozenTubeDTOForSave.getFrozenTubeTypeName())
            .sampleTypeCode(frozenTubeDTOForSave.getSampleTypeCode())
            .sampleTypeName(frozenTubeDTOForSave.getSampleTypeName())
            .sampleUsedTimesMost(frozenTubeDTOForSave.getSampleUsedTimesMost())
            .sampleUsedTimes(0)
            .frozenTubeVolumns(frozenTubeDTOForSave.getFrozenTubeVolumns())
            .frozenTubeVolumnsUnit(frozenTubeDTOForSave.getFrozenTubeVolumnsUnit())
            .tubeRows(frozenTubeDTOForSave.getTubeRows())
            .tubeColumns(frozenTubeDTOForSave.getTubeColumns())
            .status(frozenTubeDTOForSave.getStatus()).memo(frozenTubeDTOForSave.getMemo())
            .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenTubeType(frozenTubeMapper.frozenTubeTypeFromId(frozenTubeDTOForSave.getFrozenTubeTypeId()))
            .sampleType(frozenTubeMapper.sampleTypeFromId(frozenTubeDTOForSave.getSampleTypeId()))
            .sampleClassification(frozenTubeMapper.sampleClassificationFromId(frozenTubeDTOForSave.getSampleClassificationId()))
            .project(frozenTubeMapper.projectFromId(frozenTubeDTOForSave.getProjectId()))
            .projectSite(frozenTubeMapper.projectSiteFromId(frozenTubeDTOForSave.getProjectSiteId())).frozenBox(frozenBox).frozenTubeState(Constants.FROZEN_BOX_STOCKING);
        tube.setId(frozenTubeDTOForSave.getFrozenTubeId());
        frozenTubeRepository.save(tube);
        return tube;
    }
    /**
     * 创建入库盒----根据盒子的项目和项目点，创建入库管的项目和项目点
     * @param frozenBox
     * @param tubeDTO
     * @return
     */
    public StockInTubeDTO createFrozenTubeByProject(FrozenBox frozenBox, StockInTubeDTO tubeDTO) {
        if(tubeDTO.getProjectId()==null){
            tubeDTO.setProjectId(frozenBox.getProject()!=null?frozenBox.getProject().getId():null);
            tubeDTO.setProjectCode(frozenBox.getProjectCode());
        }else {
            Project project = projectRepository.findOne(tubeDTO.getProjectId());
            if(project ==null){
                throw new BankServiceException("项目不存在！");
            }
            tubeDTO.setProjectCode(project.getProjectCode());
        }
        if(tubeDTO.getProjectSiteId()==null){
            tubeDTO.setProjectSiteId(frozenBox.getProjectSite()!=null?frozenBox.getProjectSite().getId():null);
            tubeDTO.setProjectSiteCode(frozenBox.getProjectSiteCode());
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
     * @param stockInBoxDTO
     * @param stockInCode
     * @return
     */
    public Boolean checkFrozenCode(StockInBoxDTO stockInBoxDTO, String stockInCode) {
        if(stockInBoxDTO == null){
            return false;
        }
        Boolean flag = true;
        String frozenBoxCode = stockInBoxDTO.getFrozenBoxCode();
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if(frozenBox !=null && !frozenBox.getId().equals(stockInBoxDTO.getFrozenBoxId())
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

    /**
     * 创建入库盒----创建入库管的样本类型
     * @param tubeDTO
     * @return
     */
    public StockInTubeDTO createFrozenTubeBySampleType(StockInTubeDTO tubeDTO) {
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
    /**
     * 创建入库盒----初始化冻存管类型
     * @param tubeDTO
     * @return
     */
    public StockInTubeDTO createFrozenTubeTypeInit(StockInTubeDTO tubeDTO) {
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

    public FrozenBox createFrozenBoxByPosition(FrozenBox frozenBox,StockInBoxDTO stockInBoxDTO) {
        if(stockInBoxDTO == null){
            return  frozenBox;
        }
        if(stockInBoxDTO.getEquipmentId()!=null){
            Equipment equipment = equipmentRepository.findOne(stockInBoxDTO.getEquipmentId());
            if(equipment==null){
                throw new BankServiceException("设备不存在！");
            }
            frozenBox.setEquipment(equipment);
            frozenBox.setEquipmentCode(equipment.getEquipmentCode());
            stockInBoxDTO.setEquipmentCode(equipment.getEquipmentCode());
        }
        if(stockInBoxDTO.getAreaId()!=null){
            Area area = areaRepository.findOne(stockInBoxDTO.getAreaId());
            if(area==null){
                throw new BankServiceException("区域不存在！");
            }
            frozenBox.setArea(area);
            frozenBox.setAreaCode(area.getAreaCode());
            stockInBoxDTO.setAreaCode(area.getAreaCode());
        }
        if(stockInBoxDTO.getSupportRackId()!=null){
            SupportRack supportRack = supportRackRepository.findOne(stockInBoxDTO.getSupportRackId());
            if(supportRack==null){
                throw new BankServiceException("冻存架不存在！");
            }
            frozenBox.setSupportRack(supportRack);
            frozenBox.setSupportRackCode(supportRack.getSupportRackCode());
            stockInBoxDTO.setSupportRackCode(supportRack.getSupportRackCode());
        }
        Boolean flag = checkPositionValid(stockInBoxDTO);
        if(flag){
            throw new BankServiceException("此位置已存放冻存盒，请更换其他位置！",stockInBoxDTO.toString());
        }
        return frozenBox;
    }

    /**
     * 根据冻存盒位置找到冻存盒，查询出冻存盒的状态，如果不为空且不是已出库，已分装，已作废的状态，表示位置被占用！
     * @param stockInBoxDTO
     * @return 已占用：true。未被占用：false
     */
    public Boolean checkPositionValid(StockInBoxDTO stockInBoxDTO) {
        Boolean flag = false;
        if(stockInBoxDTO.getColumnsInShelf()!=null && stockInBoxDTO.getRowsInShelf()!=null){
            FrozenBox frozenBoxOld = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf
                (stockInBoxDTO.getEquipmentCode(),stockInBoxDTO.getAreaCode(),stockInBoxDTO.getSupportRackCode(),stockInBoxDTO.getColumnsInShelf(),stockInBoxDTO.getRowsInShelf());
            if(frozenBoxOld!=null && frozenBoxOld.getId()!=stockInBoxDTO.getFrozenBoxId()
                && !frozenBoxOld.getStatus().equals(Constants.FROZEN_BOX_INVALID)
                && !frozenBoxOld.getStatus().equals(Constants.FROZEN_BOX_SPLITED)
                && !frozenBoxOld.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)){
               flag = true;
            }
        }

        return flag;
    }

    public FrozenBox createFrozenBoxBySampleClass(FrozenBox frozenBox,StockInBoxDTO stockInBoxDTO, SampleType entity) {
        //验证项目下该样本类型是否有样本分类，如果已经配置了样本分类，则样本分类ID不能为空，（99的,98的除外）
//        if(entity.getIsMixed().equals(Constants.YES)&&frozenBoxDTO.getSampleClassificationId()!=null){
//            throw new BankServiceException("混合型样本的冻存盒，样本分类应该为空！");
//        }
        int countOfSampleClass = projectSampleClassRepository.countByProjectIdAndSampleTypeId(stockInBoxDTO.getProjectId(),stockInBoxDTO.getSampleTypeId());
        if(countOfSampleClass>0&&entity.getIsMixed().equals(Constants.NO)&&stockInBoxDTO.getSampleClassificationId()==null){
            throw new BankServiceException("该项目下已经配置样本分类，样本分类不能为空！");
        }
        frozenBox.setSampleClassification(frozenBoxMapper.sampleClassificationFromId(stockInBoxDTO.getSampleClassificationId()));
        return frozenBox;
    }

    public FrozenBox createFrozenBoxBySampleType(FrozenBox frozenBox,StockInBoxDTO stockInBoxDTO, SampleType entity) {
        if(stockInBoxDTO == null){
            return  frozenBox;
        }
        if(entity == null){
            throw new BankServiceException("冻存盒样本类型不存在！");
        }
        frozenBox.setSampleType(entity);
        frozenBox.setSampleTypeCode(entity.getSampleTypeCode());
        frozenBox.setSampleTypeName(entity.getSampleTypeName());
        return frozenBox;
    }

    public FrozenBox createFrozenBoxByFrozenBoxType(FrozenBox frozenBox,StockInBoxDTO stockInBoxDTO) {
        if(stockInBoxDTO == null){
            return  frozenBox;
        }
        if(stockInBoxDTO.getFrozenBoxTypeId() == null){
            throw new BankServiceException("冻存盒类型不能为空！");
        }
        FrozenBoxType boxType = frozenBoxTypeRepository.findOne(stockInBoxDTO.getFrozenBoxTypeId());
        if(boxType == null){
            throw new BankServiceException("冻存盒类型不存在！");
        }
        frozenBox.setFrozenBoxType(boxType);
        frozenBox.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
        frozenBox.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
        frozenBox.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
        return frozenBox;
    }

    public FrozenBox createFrozenBoxByStockInProject(FrozenBox frozenBox, StockInBoxDTO stockInBoxDTO, StockIn stockIn) {
        if(stockInBoxDTO == null || stockIn ==null){
            return  frozenBox;
        }
        frozenBox.setProject(stockIn.getProject()!=null?stockIn.getProject():null);
        frozenBox.setProjectName(stockIn.getProject()!=null?stockIn.getProject().getProjectName():null);
        frozenBox.setProjectCode(stockIn.getProject()!=null?stockIn.getProject().getProjectCode():null);

        frozenBox.setProjectSite(stockIn.getProjectSite()!=null?stockIn.getProjectSite():null);
        frozenBox.setProjectSiteName(stockIn.getProjectSite()!=null?stockIn.getProjectSite().getProjectSiteName():null);
        frozenBox.setProjectSiteCode(stockIn.getProjectSite()!=null?stockIn.getProjectSite().getProjectSiteCode():null);
        return  frozenBox;
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
        List<StockInTube> stockInTubes = stockInTubeRepository.findByFrozenBoxCodeAndSampleState(frozenBoxCode);
        List<StockInTubeDTO> frozenTubeDTOS = new ArrayList<StockInTubeDTO>();
        List<Long> ids = new ArrayList<Long>();
        for(StockInTube s :stockInTubes){
            ids.add(s.getFrozenTube().getId());
        }
        Map<Long,FrozenTubeHistory> allFrozenTubeHistories = ids.size()>0?stockListService.findFrozenTubeHistoryDetailByIds(ids):new HashMap<>();
        for(StockInTube f: stockInTubes){
            StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(f);
            stockInTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            stockInTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            stockInTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            stockInTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            stockInTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_NEW);//盒内新增样本
            FrozenTubeHistory frozenTubeHistory =  allFrozenTubeHistories.get(f.getFrozenTube().getId());
            if(frozenTubeHistory != null &&
                (!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)&&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER)
                    &&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_TRANSHIP)
                    &&!frozenTubeHistory.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKING))){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_ORIGINAL);//原盒原库存
            }else if(frozenTubeHistory != null &&(frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)
                || frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER))){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_STOCKIN_AGAIN);//出库再回来
            }

            frozenTubeDTOS.add(stockInTubeDTO);
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

    /**
     * 根据入库冻存盒ID查询冻存盒和冻存管的信息（包含盒内已入库的）
     * @param id
     * @return
     */
    @Override
    public StockInBoxDTO getBoxAndStockTubeByStockInBoxId(Long id) {
        //查询冻存盒信息
        StockInBox stockInBox = stockInBoxRepository.findOne(id);
        if(stockInBox == null){
            throw new BankServiceException("入库冻存盒不存在！");
        }
        FrozenBox frozenBox = stockInBox.getFrozenBox();
        List<StockInTube> stockInTubes = stockInTubeRepository.findByFrozenBoxCodeAndSampleState(frozenBox.getFrozenBoxCode());
        List<StockInTube> stockInTubesByStockInBoxId = stockInTubeRepository.findByStockInBoxId(id);
        stockInTubes.addAll(stockInTubesByStockInBoxId);
        List<StockInTubeDTO> frozenTubeDTOS = new ArrayList<StockInTubeDTO>();
        List<Long> ids = new ArrayList<Long>();
        for(StockInTube s :stockInTubes){
            ids.add(s.getFrozenTube().getId());
        }
        Map<Long,FrozenTubeHistory> allFrozenTubeHistories = ids.size()>0?stockListService.findFrozenTubeHistoryDetailByIds(ids):null;
        for(StockInTube f: stockInTubes){
            StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(f);
            stockInTubeDTO.setFrozenTubeType(f.getFrozenTubeType());
            stockInTubeDTO.setSampleType(f.getSampleType());
            stockInTubeDTO.setSampleClassification(f.getSampleClassification());
            stockInTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            stockInTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            stockInTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            stockInTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            stockInTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_NEW);//盒内新增样本
            FrozenTubeHistory frozenTubeHistory =  allFrozenTubeHistories.get(f.getFrozenTube().getId());
            if(frozenTubeHistory != null &&
                (!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)&&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER)
                    &&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_TRANSHIP)
                    &&!frozenTubeHistory.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKING))){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_ORIGINAL);//原盒原库存
            }else if(frozenTubeHistory != null &&(frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)
                || frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER))){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_STOCKIN_AGAIN);//出库再回来
            }

            frozenTubeDTOS.add(stockInTubeDTO);
        }
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);
        stockInBoxDTO.setSampleClassification(frozenBox.getSampleClassification());
        stockInBoxDTO.setSampleType(frozenBox.getSampleType());
        stockInBoxDTO.setFrozenBoxType(frozenBox.getFrozenBoxType());
        stockInBoxDTO.setFrozenTubeDTOS(frozenTubeDTOS);
        stockInBoxDTO.setFrontColor(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getFrontColor():null);
        stockInBoxDTO.setFrontColorForClass(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getFrontColor():null);
        stockInBoxDTO.setBackColor(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getBackColor():null);
        stockInBoxDTO.setBackColorForClass(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getBackColor():null);
        stockInBoxDTO.setIsMixed(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getIsMixed():null);
        stockInBoxDTO.setSampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null);
        stockInBoxDTO.setSampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null);

        return stockInBoxDTO;
    }

    /**
     * 根据入库单查找入库冻存盒（不分页）
     * @param stockInCode
     * @return
     */
    @Override
    public List<StockInBoxForDataTableEntity> getStockInBoxList(String stockInCode) {
        List<StockInBoxForDataTableEntity> result = new ArrayList<StockInBoxForDataTableEntity>();
        List<StockInBoxForDataTableEntity> stockInBoxForDataTableEntities = stockInBoxRepositries.findByStockInCode(stockInCode);
        for(StockInBoxForDataTableEntity source :stockInBoxForDataTableEntities){
            StockInBoxForDataTableEntity stockInBoxForDataTableEntity = new StockInBoxForDataTableEntity();
            BeanUtils.copyProperties(source,stockInBoxForDataTableEntity);
            String position = BankUtil.getPositionString(source.getEquipmentCode(),source.getAreaCode(),source.getSupportRackCode(),source.getColumnsInShelf(),source.getRowsInShelf(),null,null);
            stockInBoxForDataTableEntity.setPosition(position);
            result.add(stockInBoxForDataTableEntity);
        }
        return result;
    }

    @Override
    public StockInBoxDTO getStockInTubeByStockInBox(Long id) {
        //查询冻存盒信息
        StockInBox stockInBox = stockInBoxRepository.findOne(id);
        if(stockInBox == null){
            throw new BankServiceException("入库冻存盒不存在！");
        }
        FrozenBox frozenBox = stockInBox.getFrozenBox();
        List<StockInTube> stockInTubes = stockInTubeRepository.findByStockInBoxId(id);
        List<StockInTubeDTO> frozenTubeDTOS = new ArrayList<StockInTubeDTO>();
      for(StockInTube f: stockInTubes){
            StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(f);
            stockInTubeDTO.setFrozenTubeType(f.getFrozenTubeType());
            stockInTubeDTO.setSampleType(f.getSampleType());
            stockInTubeDTO.setSampleClassification(f.getSampleClassification());
            stockInTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            stockInTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            stockInTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            stockInTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            stockInTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            frozenTubeDTOS.add(stockInTubeDTO);
        }
        StockInBoxDTO stockInBoxDTO = stockInBoxMapper.stockInBoxToStockInBoxDTO(stockInBox);
        stockInBoxDTO.setSampleClassification(frozenBox.getSampleClassification());
        stockInBoxDTO.setSampleType(frozenBox.getSampleType());
        stockInBoxDTO.setFrozenBoxType(frozenBox.getFrozenBoxType());
        stockInBoxDTO.setFrozenTubeDTOS(frozenTubeDTOS);
        stockInBoxDTO.setFrontColor(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getFrontColor():null);
        stockInBoxDTO.setFrontColorForClass(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getFrontColor():null);
        stockInBoxDTO.setBackColor(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getBackColor():null);
        stockInBoxDTO.setBackColorForClass(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getBackColor():null);
        stockInBoxDTO.setIsMixed(frozenBox.getSampleType()!=null?frozenBox.getSampleType().getIsMixed():null);
        stockInBoxDTO.setSampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null);
        stockInBoxDTO.setSampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null);
        return stockInBoxDTO;
    }

    /**
     * 批量保存入库盒（出库再回来的）
     * @param stockInBoxDTO
     * @param stockInCode
     * @return
     */
    @Override
    public StockInBoxDTO createBoxByStockOutBox(StockInBoxDTO stockInBoxDTO, String stockInCode) {
        List<String> frozenBoxCodeStr = stockInBoxDTO.getFrozenBoxCodeStr();
        //验证冻存盒是否已经入库
        checkFrozenCodeStr(frozenBoxCodeStr);
        //从出库盒中查询已经出库的冻存盒，验证是否都存在
        //查询冻存盒的出库管的位置信息
        //保存入库盒
        //保存入库冻存管
        return null;
    }


    public void checkFrozenCodeStr(List<String> frozenBoxCodeStr) {
        Boolean flag = (frozenBoxCodeStr.size() == new HashSet<String>(frozenBoxCodeStr).size());
        if(flag){
            throw new BankServiceException("请勿提交重复的冻存盒编码！");
        }
        //未出过库的冻存盒
        List<String> frozenBoxOfInStock = new ArrayList<>();
        //新冻存盒
        List<String> frozenBoxOfnew = new ArrayList<>();
        List<List<String>> boxCodeEach1000 = Lists.partition(frozenBoxCodeStr,1000);
        List<FrozenBox> allFrozenBox = new ArrayList<FrozenBox>();
        for(List<String> boxCodes : boxCodeEach1000){
            List<FrozenBox> frozenBoxList = frozenBoxRepository.findByFrozenBoxCodeIn(boxCodes);
            allFrozenBox.addAll(frozenBoxList);
        }

        //盒子若没有出库则不能导入
        allFrozenBox.forEach(box->{
            if(!box.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)&&!box.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)){
                frozenBoxOfInStock.add(box.getFrozenBoxCode());
            }
//            Boolean checkFlag = false;
//            frozenBoxCodeStr.forEach(oldBox->{
//                if(oldBox.equals(box.getFrozenBoxCode())){
//                    checkFlag = true;
//                }
//            });
        });
    }

    /**
     * 根据冻存盒编码从数据接口导入出库再入库的样本
     * @param stockInBoxDTO
     * @return
     */

    @Override
    public List<StockInBoxDTO> getFrozenBoxAndTubeFromInterfaceByBoxCodeStr(StockInBoxDTO stockInBoxDTO) {
        List<StockInBoxDTO> stockInBoxDTOS = new ArrayList<>();
        List<String> frozenBoxCodeStr = stockInBoxDTO.getFrozenBoxCodeStr();

        if(frozenBoxCodeStr == null){
            throw new BankServiceException("请指定要导入的冻存盒编码！");
        }
        for(String boxCode : frozenBoxCodeStr){

            List<FrozenTubeDataForm> frozenTubeDataForms = frozenBoxImportService.importFrozenBoxByBoxCode(boxCode);
        }
        return stockInBoxDTOS;
    }

}
