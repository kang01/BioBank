package org.fwoxford.service.impl;

import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.StockListService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.StockInTubeDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Service Implementation for managing FrozenBox.
 */
@Service
@Transactional
public class FrozenBoxServiceImpl implements FrozenBoxService {

    private final Logger log = LoggerFactory.getLogger(FrozenBoxServiceImpl.class);
    private final FrozenBoxRepository frozenBoxRepository;
    private final FrozenBoxRepositories frozenBoxRepositories;
    private final FrozenBoxMapper frozenBoxMapper;
    @Autowired
    private FrozenTubeService frozenTubeService;
    @Autowired
    private FrozenTubeMapper frozenTubeMapping;
    @Autowired
    private TranshipRepository transhipRepository;
    @Autowired
    private FrozenTubeRepository frozenTubeRepository;
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private SupportRackMapper supportRackMapper;
    @Autowired
    private SampleTypeMapper sampleTypeMapper;
    @Autowired
    private FrozenBoxTypeMapper frozenBoxTypeMapper;
    @Autowired
    private ProjectSampleClassRepository projectSampleClassRepository;
    @Autowired
    private SampleClassificationMapper sampleClassificationMapper;
    @Autowired
    private FrozenTubeMapper frozenTubeMapper;
    @Autowired
    private StockInBoxRepository stockInBoxRepository;
    @Autowired
    private SampleTypeRepository sampleTypeRepository;
    @Autowired
    private StockListService stockListService;
    @Autowired
    private StockInTubeRepository stockInTubeRepository;
    @Autowired
    private StockInTubeMapper stockInTubeMapper;
    public FrozenBoxServiceImpl(FrozenBoxRepository frozenBoxRepository, FrozenBoxMapper frozenBoxMapper, FrozenBoxRepositories frozenBoxRepositories) {
        this.frozenBoxRepository = frozenBoxRepository;
        this.frozenBoxMapper = frozenBoxMapper;
        this.frozenBoxRepositories = frozenBoxRepositories;
    }

    /**
     * Save a frozenBox.
     *
     * @param frozenBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenBoxDTO save(FrozenBoxDTO frozenBoxDTO) {
        log.debug("Request to save FrozenBox : {}", frozenBoxDTO);
        FrozenBox frozenBox = frozenBoxMapper.frozenBoxDTOToFrozenBox(frozenBoxDTO);
        frozenBox = frozenBoxRepository.save(frozenBox);
        FrozenBoxDTO result = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);
        return result;
    }

    /**
     * Get all the frozenBoxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenBoxes");
        Page<FrozenBox> result = frozenBoxRepository.findAll(pageable);
        return result.map(frozenBox -> frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox));
    }

    /**
     * Get one frozenBox by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenBoxDTO findOne(Long id) {
        log.debug("Request to get FrozenBox : {}", id);
        FrozenBox frozenBox = frozenBoxRepository.findOne(id);
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);
        return frozenBoxDTO;
    }

    /**
     * Delete the  frozenBox by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenBox : {}", id);
        frozenBoxRepository.delete(id);
    }

    /**
     * 根据转运记录ID 查询冻存盒列表
     *
     * @param transhipId 转运记录ID
     * @return
     */
    @Override
    public List<FrozenBoxDTO> findAllFrozenBoxByTranshipId(Long transhipId) {
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAllFrozenBoxByTranshipId(transhipId);
        return frozenBoxMapper.frozenBoxesToFrozenBoxDTOs(frozenBoxList);
    }

    /**
     * 根据冻存盒id查询冻存管信息
     *
     * @param frozenBoxId 冻存盒id
     * @return
     */
    @Override
    public FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxId(Long frozenBoxId) {
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();
        //查询冻存盒信息
        FrozenBox frozenBox = frozenBoxRepository.findOne(frozenBoxId);

        //查询冻存管列表信息
        List<FrozenTube> frozenTube = frozenTubeService.findFrozenTubeListByBoxId(frozenBoxId);
        List<FrozenTubeResponse> frozenTubeResponses = frozenTubeMapping.frozenTubeToFrozenTubeResponse(frozenTube);

        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox, frozenTubeResponses);

        return res;
    }

    /**
     * 根据冻存盒CODE查询冻存管信息
     *
     * @param frozenBoxCode 冻存盒CODE
     * @return
     */
    @Override
    public FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxCode(String frozenBoxCode) {
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        //查询冻存盒信息
        FrozenBox frozenBox = this.findFrozenBoxDetailsByBoxCode(frozenBoxCode);

        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！");
        }

        //查询冻存管列表信息
        List<FrozenTube> frozenTube = frozenTubeService.findFrozenTubeListByBoxCode(frozenBoxCode);

        List<FrozenTubeResponse> frozenTubeResponses = frozenTubeMapping.frozenTubeToFrozenTubeResponse(frozenTube);

        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox, frozenTubeResponses);

        return res;
    }

    /**
     * 根据冻存盒code查询冻存盒基本信息
     *
     * @param frozenBoxCode 冻存盒code
     * @return
     */
    public FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode) {
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        return frozenBox;
    }

    /**
     * 批量保存冻存盒
     *
     * @param frozenBoxDTOList
     */
    @Override
    public List<FrozenBox> saveBatch(List<FrozenBoxDTO> frozenBoxDTOList) {
        List<FrozenBox> frozenBoxes = frozenBoxMapper.frozenBoxDTOsToFrozenBoxes(frozenBoxDTOList);
        List<FrozenBox> frozenBoxList = frozenBoxRepository.save(frozenBoxes);
        return frozenBoxList;
    }

    /**
     * 根据冻存盒code串查询冻存盒以及冻存管的信息
     *
     * @param frozenBoxCodeStr 冻存盒code串
     * @return
     */
    @Override
    public List<FrozenBoxAndFrozenTubeResponse> findFrozenBoxAndTubeListByBoxCodeStr(String frozenBoxCodeStr) {
        List<FrozenBoxAndFrozenTubeResponse> frozenBoxAndFrozenTubeResponses = new ArrayList<FrozenBoxAndFrozenTubeResponse>();

        if (StringUtils.isEmpty(frozenBoxCodeStr)) {
            throw new BankServiceException("请传入有效的冻存盒编码！", frozenBoxCodeStr);
        }

        String frozenBoxCode[] = frozenBoxCodeStr.split(",");
        for (String i : frozenBoxCode) {
            FrozenBoxAndFrozenTubeResponse response = findFrozenBoxAndTubeByBoxCode(i);
            frozenBoxAndFrozenTubeResponses.add(response);
        }

        return frozenBoxAndFrozenTubeResponses;
    }

    /**
     * 判断某设备某区域某架子某行某列是否有盒子存在
     *
     * @param equipmentId
     * @param areaId
     * @param supportRackId
     * @param column
     * @param row
     * @return
     */
    @Override
    public List<FrozenBoxDTO> findByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row) {
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(equipmentId, areaId, supportRackId, column, row);
        return frozenBoxMapper.frozenBoxesToFrozenBoxDTOs(frozenBoxes);
    }

    /**
     * 根据转运编码查询冻存盒列表
     *
     * @param transhipCode 转运编码
     * @return
     */
    @Override
    public List<FrozenBoxAndFrozenTubeResponse> getFrozenBoxAndTubeByTranshipCode(String transhipCode) {

        List<FrozenBoxAndFrozenTubeResponse> res = new ArrayList<FrozenBoxAndFrozenTubeResponse>();

        Tranship tranship = transhipRepository.findByTranshipCode(transhipCode);
        //根据转运code查询冻存盒列表
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findAllFrozenBoxByTranshipId(tranship != null ? tranship.getId() : null);
//        List<FrozenBox> frozenBoxes = frozenBoxRepository.findFrozenBoxByTranshipCode(transhipCode);
        for (FrozenBox box : frozenBoxes) {

            //查询冻存管列表信息
            List<FrozenTube> frozenTube = frozenTubeService.findFrozenTubeListByBoxCode(box.getFrozenBoxCode());

            List<FrozenTubeResponse> frozenTubeResponses = frozenTubeMapping.frozenTubeToFrozenTubeResponse(frozenTube);

            FrozenBoxAndFrozenTubeResponse tempRes = frozenBoxMapper.forzenBoxAndTubeToResponse(box, frozenTubeResponses);

            res.add(tempRes);
        }
        return res;
    }

    @Override
    public List<StockInBoxForChangingPosition> getIncompleteFrozenBoxes(String projectCode, String sampleTypeCode,String transhipCode) {
        List<StockInBoxForChangingPosition> stockInBoxForChangingPositionList = new ArrayList<StockInBoxForChangingPosition>();
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findByProjectCodeAndSampleTypeCodeAndTranshipCodeAndStatus(projectCode, sampleTypeCode,transhipCode, Constants.FROZEN_BOX_STOCKING);
        if (frozenBoxList.size() == 0) {
            frozenBoxList = frozenBoxRepository.findByProjectCodeAndSampleTypeCodeAndStatus(projectCode, sampleTypeCode, Constants.FROZEN_BOX_STOCKED);
        }
        List<FrozenBox> unSplitedBoxList = new ArrayList<FrozenBox>();
        for (FrozenBox box : frozenBoxList) {
            if(box.getIsSplit().equals(Constants.NO)){
                unSplitedBoxList.add(box);
            }
        }
        List<String> frozenBoxCodes = new ArrayList<>();
        for (FrozenBox box : unSplitedBoxList) {
             frozenBoxCodes.add(box.getFrozenBoxCode());
        }
        List<Object[]> map = new ArrayList<>();
        if (unSplitedBoxList.size() > 0) {
            map = frozenTubeRepository.countSampleNumberByfrozenBoxList(frozenBoxCodes);
        }
        for (FrozenBox box : unSplitedBoxList) {
            for (int i = 0; i < map.size(); i++) {
                Object[] obj = map.get(i);
                String frozenBoxCodeKey = obj[0].toString();
                String number = obj[1].toString();
                if (box.getFrozenBoxCode().equals(frozenBoxCodeKey)) {
                    String columns = box.getFrozenBoxTypeColumns() != null ? box.getFrozenBoxTypeColumns() : box.getFrozenBoxType().getFrozenBoxTypeColumns();
                    String rows = box.getFrozenBoxTypeRows() != null ? box.getFrozenBoxTypeRows() : box.getFrozenBoxType().getFrozenBoxTypeRows();
                    int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
                    int countOfSample = Integer.parseInt(number);
                    if (allCounts > countOfSample) {
                        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(box.getFrozenBoxCode());
                        StockInBoxForChangingPosition newBox = createStockInBoxForDataMoved(box, frozenTubeList, countOfSample);
                        stockInBoxForChangingPositionList.add(newBox);
                    }
                }
            }
        }
        return stockInBoxForChangingPositionList;
    }

    private StockInBoxForChangingPosition createStockInBoxForDataMoved(FrozenBox box, List<FrozenTube> frozenTubeList, int countOfSample) {
        StockInBoxForChangingPosition res = new StockInBoxForChangingPosition();
        res.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(box.getSampleType()));
        res.setCountOfSample(countOfSample);
        res.setFrozenBoxId(box.getId());
        res.setFrozenBoxCode(box.getFrozenBoxCode());
        res.setFrozenBoxTypeColumns(box.getFrozenBoxTypeColumns());
        res.setFrozenBoxTypeRows(box.getFrozenBoxTypeRows());
        res.setStockInFrozenTubeList(new ArrayList<>());
        res.setFrozenBoxTypeId(box.getFrozenBoxType().getId());
        res.setFrozenBoxType(frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(box.getFrozenBoxType()));
        res.setIsSplit(box.getIsSplit());
        res.setEquipmentId(box.getEquipment()!=null?box.getEquipment().getId():null);
        res.setEquipment(equipmentMapper.equipmentToEquipmentDTO(box.getEquipment()));
        res.setArea(areaMapper.areaToAreaDTO(box.getArea()));
        res.setAreaId(box.getArea()!=null?box.getArea().getId():null);
        res.setShelf(supportRackMapper.supportRackToSupportRackDTO(box.getSupportRack()));
        res.setSupportRackId(box.getSupportRack()!=null?box.getSupportRack().getId():null);
        res.setStatus(box.getStatus());
        res.setSampleTypeCode(box.getSampleTypeCode());
        res.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(box.getSampleType()));
        for (FrozenTube tubes : frozenTubeList) {
            StockInTubeForBox tube = new StockInTubeForBox();
            tube.setId(tubes.getId());
            tube.setFrozenBoxCode(box.getFrozenBoxCode());
            tube.setTubeColumns(tubes.getTubeColumns());
            tube.setTubeRows(tubes.getTubeRows());
            res.getStockInFrozenTubeList().add(tube);
        }

        return res;
    }

    @Override
    public DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipment(DataTablesInput input, String equipmentCode) {
        input.addColumn("equipmentCode", true, true, equipmentCode+"+");
        DataTablesOutput<FrozenBox> output = frozenBoxRepositories.findAll(input);
        List<FrozenBox> frozenBoxes = output.getData();
        List<StockInBoxDetail> res = new ArrayList<StockInBoxDetail>();
        for (FrozenBox frozenBox : frozenBoxes) {
            if(frozenBox.getStatus()!=null&&(frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID)||frozenBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED))){
                continue;
            }
            StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
            stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
            stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
            stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            stockInBoxDetail.setMemo(frozenBox.getMemo());
            // stockInBoxDetail.setStockInCode("");
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(frozenBox.getFrozenBoxCode(), Constants.FROZEN_TUBE_NORMAL);
            stockInBoxDetail.setCountOfSample(frozenTubes.size());
            stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
            stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
            stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
            stockInBoxDetail.setEquipmentId(frozenBox.getEquipment() != null ? frozenBox.getEquipment().getId() : null);
            stockInBoxDetail.setAreaId(frozenBox.getArea() != null ? frozenBox.getArea().getId() : null);
            stockInBoxDetail.setSupportRackId(frozenBox.getArea() != null ? frozenBox.getSupportRack().getId() : null);
            stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
            stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
            stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
            stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
            stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
            stockInBoxDetail.setStatus(frozenBox.getStatus());
            res.add(stockInBoxDetail);
        }
        //构造返回分页数据
        DataTablesOutput<StockInBoxDetail> responseDataTablesOutput = new DataTablesOutput<StockInBoxDetail>();
        responseDataTablesOutput.setDraw(output.getDraw());
        responseDataTablesOutput.setError(output.getError());
        responseDataTablesOutput.setData(res);
        responseDataTablesOutput.setRecordsFiltered(output.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(output.getRecordsTotal());
        return responseDataTablesOutput;
    }

    @Override
    public DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipmentAndArea(DataTablesInput input, String equipmentCode, String areaCode) {
        input.addColumn("equipmentCode", true, true, equipmentCode+"+");
        input.addColumn("areaCode", true, true, areaCode+"+");
        DataTablesOutput<FrozenBox> output = frozenBoxRepositories.findAll(input);
        List<FrozenBox> frozenBoxes = output.getData();
        List<StockInBoxDetail> res = new ArrayList<StockInBoxDetail>();
        for (FrozenBox frozenBox : frozenBoxes) {
            if(frozenBox.getStatus()!=null&&(frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID)||frozenBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED))){
                continue;
            }
            StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
            stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
            stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
            stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            stockInBoxDetail.setMemo(frozenBox.getMemo());
            // stockInBoxDetail.setStockInCode("");
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(frozenBox.getFrozenBoxCode(), Constants.FROZEN_TUBE_NORMAL);
            stockInBoxDetail.setCountOfSample(frozenTubes.size());
            stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
            stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
            stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
            stockInBoxDetail.setEquipmentId(frozenBox.getEquipment() != null ? frozenBox.getEquipment().getId() : null);
            stockInBoxDetail.setAreaId(frozenBox.getArea() != null ? frozenBox.getArea().getId() : null);
            stockInBoxDetail.setSupportRackId(frozenBox.getArea() != null ? frozenBox.getSupportRack().getId() : null);
            stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
            stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
            stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
            stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
            stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
            stockInBoxDetail.setStatus(frozenBox.getStatus());
            res.add(stockInBoxDetail);
        }
        //构造返回分页数据
        DataTablesOutput<StockInBoxDetail> responseDataTablesOutput = new DataTablesOutput<StockInBoxDetail>();
        responseDataTablesOutput.setDraw(output.getDraw());
        responseDataTablesOutput.setError(output.getError());
        responseDataTablesOutput.setData(res);
        responseDataTablesOutput.setRecordsFiltered(output.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(output.getRecordsTotal());
        return responseDataTablesOutput;
    }

    @Override
    public List<StockInBoxDetail> getFrozenBoxByEquipmentAndAreaAndShelves(String equipmentCode, String areaCode, String shelfCode) {
        List<FrozenBox> frozenBoxs = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(equipmentCode, areaCode, shelfCode);
        List<StockInBoxDetail> res = new ArrayList<StockInBoxDetail>();
        for (FrozenBox frozenBox : frozenBoxs) {
            if(frozenBox.getStatus()!=null&&(frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID)||frozenBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED))){
                continue;
            }
            StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
            stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
            stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
            stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            stockInBoxDetail.setMemo(frozenBox.getMemo());
            // stockInBoxDetail.setStockInCode("");
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(frozenBox.getFrozenBoxCode(), Constants.FROZEN_TUBE_NORMAL);
            stockInBoxDetail.setCountOfSample(frozenTubes.size());
            stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
            stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
            stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
            stockInBoxDetail.setEquipmentId(frozenBox.getEquipment() != null ? frozenBox.getEquipment().getId() : null);
            stockInBoxDetail.setAreaId(frozenBox.getArea() != null ? frozenBox.getArea().getId() : null);
            stockInBoxDetail.setSupportRackId(frozenBox.getArea() != null ? frozenBox.getSupportRack().getId() : null);
            stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
            stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
            stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
            stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
            stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
            stockInBoxDetail.setStatus(frozenBox.getStatus());
            res.add(stockInBoxDetail);
        }
        return res;
    }

    @Override
    public StockInBoxDetail getFrozenBoxByEquipmentAndAreaAndShelvesAndPosition(String equipmentCode, String areaCode, String shelfCode, String position) {
        String columnsInShelf = position.substring(0, 1);
        String rowsInShelf = position.substring(1);
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        FrozenBox frozenBox = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(equipmentCode, areaCode, shelfCode, columnsInShelf, rowsInShelf);
        if(frozenBox == null){
            return stockInBoxDetail;
        }

        stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        stockInBoxDetail.setMemo(frozenBox.getMemo());
        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByFrozenBoxCodeAndStatus(frozenBox.getFrozenBoxCode(), Constants.FROZEN_TUBE_NORMAL);
        stockInBoxDetail.setCountOfSample(frozenTubes.size());
        stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
        stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
        stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
        stockInBoxDetail.setEquipmentId(frozenBox.getEquipment() != null ? frozenBox.getEquipment().getId() : null);
        stockInBoxDetail.setAreaId(frozenBox.getArea() != null ? frozenBox.getArea().getId() : null);
        stockInBoxDetail.setSupportRackId(frozenBox.getArea() != null ? frozenBox.getSupportRack().getId() : null);
        stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
        stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
        stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
        stockInBoxDetail.setStatus(frozenBox.getStatus());
        return stockInBoxDetail;
    }

    @Override
    public List<StockInBoxForDataTable> findFrozenBoxListByBoxCodeStr(List<String> frozenBoxCodeStr) {
        List<StockInBoxForDataTable> stockInBoxs = new ArrayList<>();
        if (StringUtils.isEmpty(frozenBoxCodeStr)) {
            throw new BankServiceException("请传入有效的冻存盒编码！", frozenBoxCodeStr.toString());
        }
        List<FrozenBox> frozenBoxes = frozenBoxRepository.findByFrozenBoxCodeIn(frozenBoxCodeStr);
        stockInBoxs = frozenBoxesToStockInBoxForDataTables(frozenBoxes);
        return stockInBoxs;
    }
    public List<StockInBoxForDataTable> frozenBoxesToStockInBoxForDataTables(List<FrozenBox> frozenBoxes){
        List<StockInBoxForDataTable> stockInBoxForDataTables = new ArrayList<>();
        for(FrozenBox box:frozenBoxes){
            StockInBoxForDataTable stockInBoxForDataTable = frozenBoxToStockInBoxForDataTable(box);
            stockInBoxForDataTables.add(stockInBoxForDataTable);
        }
        return stockInBoxForDataTables;
    }


    public StockInBoxForDataTable frozenBoxToStockInBoxForDataTable(FrozenBox box){
        StockInBoxForDataTable stockInBoxForDataTable = new StockInBoxForDataTable();
        if(box == null){
            return null;
        }
        stockInBoxForDataTable.setIsSplit(box.getIsSplit());
        Long countOfSample = frozenTubeRepository.countFrozenTubeListByBoxCode(box.getFrozenBoxCode());
        stockInBoxForDataTable.setCountOfSample(countOfSample.intValue());
        stockInBoxForDataTable.setId(box.getId());
        stockInBoxForDataTable.setFrozenBoxCode(box.getFrozenBoxCode());
        String position = BankUtil.getPositionString(box);
        stockInBoxForDataTable.setPosition(position);
        stockInBoxForDataTable.setSampleTypeName(box.getSampleTypeName());
        stockInBoxForDataTable.setSampleClassificationName(box.getSampleClassification()!=null?box.getSampleClassification().getSampleClassificationName():null);
        stockInBoxForDataTable.setStatus(box.getStatus());
        return stockInBoxForDataTable;
    }
    public StockInBoxDetail createStockInBoxDetail(StockInBox frozenBox,String stockInCode) {
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        stockInBoxDetail.setMemo(frozenBox.getMemo());
        stockInBoxDetail.setStockInCode(stockInCode);
        List<StockInTube> frozenTubes = stockInTubeRepository.findByStockInBoxId(frozenBox.getId());
        stockInBoxDetail.setCountOfSample(frozenTubes.size());
        stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
        stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
        stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
        stockInBoxDetail.setEquipmentId(frozenBox.getEquipment()!=null?frozenBox.getEquipment().getId():null);
        stockInBoxDetail.setAreaId(frozenBox.getArea()!=null?frozenBox.getArea().getId():null);
        stockInBoxDetail.setSupportRackId(frozenBox.getSupportRack()!=null?frozenBox.getSupportRack().getId():null);
        stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
        stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
        stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
        stockInBoxDetail.setStatus(frozenBox.getStatus());
        return stockInBoxDetail;
    }

    @Override
    public Boolean isRepeatFrozenBoxCode(String frozenBoxCode) {
        Boolean flag = false;
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if(frozenBox!=null&&!frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID)){
            flag = true;
        }
        return flag;
    }

    /**
     * 获取未满冻存盒
     * @param projectCode
     * @param sampleTypeCode
     * @param stockInCode
     * @return
     */
    @Override
    public List<StockInBoxForChangingPosition> getIncompleteFrozenBoxesByStockIn(String projectCode, String sampleTypeCode, String stockInCode) {
        List<StockInBoxForChangingPosition> stockInBoxForChangingPositionList = new ArrayList<StockInBoxForChangingPosition>();
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findByProjectCodeAndSampleTypeCodeAndStockInCodeAndStatus(projectCode, sampleTypeCode,stockInCode, Constants.FROZEN_BOX_STOCKING);
        if (frozenBoxList.size() == 0) {
            frozenBoxList = frozenBoxRepository.findByProjectCodeAndSampleTypeCodeAndStatus(projectCode, sampleTypeCode, Constants.FROZEN_BOX_STOCKED);
        }
        List<FrozenBox> unSplitedBoxList = new ArrayList<FrozenBox>();
        for (FrozenBox box : frozenBoxList) {
            if(box.getIsSplit().equals(Constants.NO)){
                unSplitedBoxList.add(box);
            }
        }
        List<String> frozenBoxCodes = new ArrayList<>();
        for (FrozenBox box : unSplitedBoxList) {
            frozenBoxCodes.add(box.getFrozenBoxCode());
        }
        List<Object[]> map = new ArrayList<>();
        if (unSplitedBoxList.size() > 0) {
            map = frozenTubeRepository.countSampleNumberByfrozenBoxList(frozenBoxCodes);
        }
        for (FrozenBox box : unSplitedBoxList) {
            for (int i = 0; i < map.size(); i++) {
                Object[] obj = map.get(i);
                String frozenBoxCodeKey = obj[0].toString();
                String number = obj[1].toString();
                if (box.getFrozenBoxCode().equals(frozenBoxCodeKey)) {
                    String columns = box.getFrozenBoxTypeColumns() != null ? box.getFrozenBoxTypeColumns() : box.getFrozenBoxType().getFrozenBoxTypeColumns();
                    String rows = box.getFrozenBoxTypeRows() != null ? box.getFrozenBoxTypeRows() : box.getFrozenBoxType().getFrozenBoxTypeRows();
                    int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
                    int countOfSample = Integer.parseInt(number);
                    if (allCounts > countOfSample) {
                        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(box.getFrozenBoxCode());
                        StockInBoxForChangingPosition newBox = createStockInBoxForDataMoved(box, frozenTubeList, countOfSample);
                        stockInBoxForChangingPositionList.add(newBox);
                    }
                }
            }
        }
        return stockInBoxForChangingPositionList;
    }

    @Override
    public List<StockInBoxForIncomplete> getIncompleteFrozenBoxeList(String frozenBoxCode, String stockInCode) {
        List<StockInBoxForIncomplete> stockInBoxForIncompleteList = new ArrayList<StockInBoxForIncomplete>();

        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        SampleType sampleType = frozenBox.getSampleType();
        if(sampleType == null){
            throw new BankServiceException("该冻存盒没有样本类型！",frozenBoxCode);
        }

        FrozenBoxType frozenBoxType = frozenBox.getFrozenBoxType();
        if(frozenBoxType == null){
            throw new BankServiceException("未查询到该冻存盒的类型！",frozenBoxCode);
        }
        List<ProjectSampleClass> projectSampleClasses =  projectSampleClassRepository.findByProjectIdAndSampleTypeId(frozenBox.getProject().getId(),sampleType.getId());

        List<Long> sampleClassificationIdStr = new ArrayList<Long>();

        for(ProjectSampleClass s : projectSampleClasses ){
            sampleClassificationIdStr.add(s.getSampleClassification().getId());
        }
        List<FrozenBox> frozenBoxList = new ArrayList<FrozenBox>();
        List<FrozenBox> wrongFrozenBoxList = new ArrayList<FrozenBox>();
        //取本次入库的所有冻存盒
        List<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxByStockInCodeAndStatus(stockInCode,Constants.FROZEN_BOX_STOCKING);
        SampleType wrongSample =sampleTypeRepository.findBySampleTypeCode("97");
        Map<Long,List<FrozenBox>> map = new HashMap<Long,List<FrozenBox>>();
        for(StockInBox inBox:stockInBoxes){
            FrozenBox boxIn = inBox.getFrozenBox();
            if(boxIn.getFrozenBoxTypeCode().equals(frozenBox.getFrozenBoxTypeCode())
                &&boxIn.getId()!=frozenBox.getId()&&boxIn.getIsSplit().equals(Constants.NO)){
                Long frozenTube = frozenTubeRepository.countFrozenTubeListByBoxCode(boxIn.getFrozenBoxCode());
                String columns = boxIn.getFrozenBoxTypeColumns()!=null?boxIn.getFrozenBoxTypeColumns():new String("0");
                String rows = boxIn.getFrozenBoxTypeRows()!=null?boxIn.getFrozenBoxTypeRows():new String("0");
                int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
                if(frozenTube.intValue()==allCounts){
                    continue;
                }
                Long key = null;
                if(boxIn.getSampleTypeCode().equals("97")){
                    wrongFrozenBoxList.add(boxIn);
                    continue;
                }
                if(sampleClassificationIdStr.size()>0){//有分类
                    key = boxIn.getSampleClassification().getId();
                }else{//无分类
                    key = boxIn.getSampleType().getId();
                }
                List<FrozenBox> frozenBoxes =map.get(key);
                if(frozenBoxes==null||frozenBoxes.size()==0){
                    List<FrozenBox> frozenBoxLists = new ArrayList<FrozenBox>();
                    frozenBoxLists.add(boxIn);
                    map.put(key,frozenBoxLists);
                }else{
                    frozenBoxes.add(boxIn);
                    map.put(key,frozenBoxes);
                }
            }
        }

        //有分类---取相同分类的
        if(sampleClassificationIdStr.size()>0){
            for(Long id :sampleClassificationIdStr){
                if(map.get(id)!=null && map.get(id).size()>0){
                    frozenBoxList.addAll(map.get(id));
                }else{
//                    List<StockInBox> stockInFrozenBox = stockInBoxRepository.findIncompleteFrozenBoxBySampleClassificationIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), new ArrayList<Long>(){{add(id);}}, frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED);
                    List<FrozenBox> stockInFrozenBox = frozenBoxRepository.findIncompleteFrozenBoxBySampleClassificationIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), new ArrayList<Long>(){{add(id);}}, frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED,stockInCode);
                    frozenBoxList.addAll(stockInFrozenBox);
                }
            }
        }
        //无分类---取相同类型的
        else{
            Long sampleTypeId = null;
            if(!sampleType.getSampleTypeCode().equals("98")&&!sampleType.getSampleTypeCode().equals("99")){
                sampleTypeId = sampleType.getId();
                if (map.get(sampleTypeId) != null && map.get(sampleTypeId).size() > 0) {
                    frozenBoxList.addAll(map.get(sampleTypeId));
                } else {
//                    List<StockInBox> stockInFrozenBox = stockInBoxRepository.findIncompleteFrozenBoxBySampleTypeIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), sampleTypeId, frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED);
                    List<FrozenBox> stockInFrozenBox = frozenBoxRepository.findIncompleteFrozenBoxBySampleTypeIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), sampleTypeId, frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED,stockInCode);
                    frozenBoxList.addAll(stockInFrozenBox);
                }
            }

        }
        if(wrongFrozenBoxList==null || wrongFrozenBoxList.size()== 0 ){
//            wrongFrozenBoxList =  stockInBoxRepository.findIncompleteFrozenBoxBySampleTypeIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), wrongSample.getId(), frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED);
            wrongFrozenBoxList =  frozenBoxRepository.findIncompleteFrozenBoxBySampleTypeIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), wrongSample.getId(), frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED,stockInCode);
        }
        frozenBoxList.addAll(wrongFrozenBoxList);
        List<FrozenBox> frozenBoxes = new ArrayList<>();
        for(FrozenBox f :frozenBoxList){
            StockInBoxForIncomplete stockInBoxForIncomplete = new StockInBoxForIncomplete();
            stockInBoxForIncomplete.setFrozenBoxId(f.getId());
            stockInBoxForIncomplete.setFrozenBoxCode(f.getFrozenBoxCode());
            stockInBoxForIncomplete.setFrozenBoxType(frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(f.getFrozenBoxType()));
            stockInBoxForIncomplete.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(f.getSampleType()));
            stockInBoxForIncomplete.setSampleClassification(sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(f.getSampleClassification()));
            List<StockInTube> stockInTubes = stockInTubeRepository.findByFrozenBoxCodeAndStockInCode(f.getFrozenBoxCode(),stockInCode);
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(f.getFrozenBoxCode());
            List<StockInTubeForBox> stockInTubeForBoxes = new ArrayList<StockInTubeForBox>();
            for(FrozenTube t :frozenTubes){
                StockInTubeForBox inTubeForBox = new StockInTubeForBox();
                inTubeForBox.setId(t.getId());
                inTubeForBox.setFrozenBoxCode(f.getFrozenBoxCode());
                inTubeForBox.setTubeColumns(t.getTubeColumns());
                inTubeForBox.setTubeRows(t.getTubeRows());
                stockInTubeForBoxes.add(inTubeForBox);
            }
            for(StockInTube stockInTube : stockInTubes){
                StockInTubeForBox inTubeForBox = new StockInTubeForBox();
                inTubeForBox.setId(stockInTube.getFrozenTube().getId());
                inTubeForBox.setFrozenBoxCode(stockInTube.getFrozenBoxCode());
                inTubeForBox.setTubeColumns(stockInTube.getTubeColumns());
                inTubeForBox.setTubeRows(stockInTube.getTubeRows());
                stockInTubeForBoxes.add(inTubeForBox);
            }
            FrozenBoxType boxType = f.getFrozenBoxType();
            String columns = boxType.getFrozenBoxTypeColumns()!=null?boxType.getFrozenBoxTypeColumns():new String("0");
            String rows = boxType.getFrozenBoxTypeRows()!=null?boxType.getFrozenBoxTypeRows():new String("0");
            int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
            if(stockInTubeForBoxes.size() >= allCounts){
                continue;
            }
            stockInBoxForIncomplete.setCountOfSample(stockInTubeForBoxes.size());
            stockInBoxForIncomplete.setStockInFrozenTubeList(stockInTubeForBoxes);
            stockInBoxForIncompleteList.add(stockInBoxForIncomplete);
        }

        return stockInBoxForIncompleteList;
    }

    @Override
    public FrozenBoxDTO getBoxAndTubeByForzenBoxCode(String frozenBoxCode) {

        //查询冻存盒信息
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);

        //只能给已出库和已入库的和已交接的
        if(frozenBox == null){
            return new FrozenBoxDTO();
        }else if(frozenBox !=null && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
            && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED)
            && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)){
            throw new BankServiceException("冻存盒编码已存在！");
        }

        //查询冻存管列表信息
        List<StockInTube> stockInTubes = new ArrayList<StockInTube>();
        if(!frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)&&!frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)){
            stockInTubes = stockInTubeRepository.findByFrozenBoxCodeAndSampleState(frozenBoxCode);
        }
        List<Long> ids = new ArrayList<Long>();
        for(StockInTube s :stockInTubes){
            ids.add(s.getFrozenTube().getId());
        }
        String columns = frozenBox.getFrozenBoxTypeColumns()!=null?frozenBox.getFrozenBoxTypeColumns():new String("0");
        String rows = frozenBox.getFrozenBoxTypeRows()!=null?frozenBox.getFrozenBoxTypeRows():new String("0");
        int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
        if(stockInTubes.size() == allCounts){
            throw new BankServiceException("冻存盒已满！");
        }
        List<StockInTubeDTO> frozenTubeDTOS = new ArrayList<StockInTubeDTO>();
        Map<Long,FrozenTubeHistory> allFrozenTubeHistories = stockListService.findFrozenTubeHistoryDetailByIds(ids);
        for(StockInTube f: stockInTubes){
            StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(f);
            stockInTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            stockInTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            stockInTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            stockInTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            stockInTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_3);//盒内新增样本
            FrozenTubeHistory frozenTubeHistory =  allFrozenTubeHistories.get(f.getFrozenTube().getId());
            if(frozenTubeHistory != null &&
                (!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)&&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER)
                    &&!frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_TRANSHIP))){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_2);//原盒原库存
            }else if(frozenTubeHistory != null &&(frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_STOCK_OUT)
                || frozenTubeHistory.getType().equals(Constants.SAMPLE_HISTORY_HAND_OVER))){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_1);//出库再回来
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
     * 查询冻存盒的上一次状态
     * @param id
     * @return
     */
    @Override
    public String findFrozenBoxHistory(Long id) {
        String status = Constants.FROZEN_BOX_INVALID;
        List<Object[]> positionHistory = frozenBoxRepository.findPositionHistory(id);
        if(positionHistory.size()>0){
            status = Constants.FROZEN_BOX_STOCKED;
        }else{
            List<Object[]> boxHistory = frozenBoxRepository.findFrozenBoxHistory(id);
            for(Object[] objects:boxHistory){
                String type = objects[25].toString();
                if(type.equals("102")||type.equals(102)){
                    status = Constants.FROZEN_BOX_STOCKED;
                }
                if(type.equals("103")||type.equals(103)){
                    status = Constants.FROZEN_BOX_STOCK_OUT_COMPLETED;
                }
            }
        }
        return status;
    }
}
