package org.fwoxford.service.impl;

import io.swagger.models.auth.In;
import net.sf.json.JSONArray;
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
import org.fwoxford.web.rest.util.ExcelUtils;
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
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private FrozenBoxTypeRepository frozenBoxTypeRepository;
    @Autowired
    private FrozenTubeTypeRepository frozenTubeTypeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;

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

        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox);
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

        if (frozenBox == null) {
            throw new BankServiceException("冻存盒不存在！");
        }

        //查询冻存管列表信息
        List<FrozenTube> frozenTube = frozenTubeService.findFrozenTubeListByBoxCode(frozenBoxCode);

        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox);

        res.setFrozenTubeDTOS(frozenTubeMapping.frozenTubesToFrozenTubeDTOs(frozenTube));
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
     * 根据冻存盒code串查询冻存盒以及冻存管的信息
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

        for (FrozenBox box : frozenBoxes) {

            //查询冻存管列表信息
            List<FrozenTube> frozenTube = frozenTubeService.findFrozenTubeListByBoxCode(box.getFrozenBoxCode());

            List<FrozenTubeResponse> frozenTubeResponses = frozenTubeMapping.frozenTubeToFrozenTubeResponse(frozenTube);

            FrozenBoxAndFrozenTubeResponse tempRes = frozenBoxMapper.forzenBoxAndTubeToResponse(box);

            res.add(tempRes);
        }
        return res;
    }
    /**
     * 输入设备编码，返回该设备下的所有盒子信息
     * @param input
     * @param equipmentCode
     * @return
     */
    @Override
    public DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipment(DataTablesInput input, String equipmentCode) {
        input.addColumn("equipmentCode", true, true, equipmentCode + "+");
        DataTablesOutput<FrozenBox> output = frozenBoxRepositories.findAll(input);
        List<FrozenBox> frozenBoxes = output.getData();
        List<StockInBoxDetail> res = new ArrayList<StockInBoxDetail>();
        for (FrozenBox frozenBox : frozenBoxes) {
            if (frozenBox.getStatus() != null && (frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID) || frozenBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED))) {
                continue;
            }
            StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
            stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
            stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
            stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            stockInBoxDetail.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
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

    /**
     * 输入设备编码，区域编码，返回指定区域下的所有盒子信息
     * @param input
     * @param equipmentCode
     * @param areaCode
     * @return
     */
    @Override
    public DataTablesOutput<StockInBoxDetail> getPageFrozenBoxByEquipmentAndArea(DataTablesInput input, String equipmentCode, String areaCode) {
        input.addColumn("equipmentCode", true, true, equipmentCode + "+");
        input.addColumn("areaCode", true, true, areaCode + "+");
        DataTablesOutput<FrozenBox> output = frozenBoxRepositories.findAll(input);
        List<FrozenBox> frozenBoxes = output.getData();
        List<StockInBoxDetail> res = new ArrayList<StockInBoxDetail>();
        for (FrozenBox frozenBox : frozenBoxes) {
            if (frozenBox.getStatus() != null && (frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID) || frozenBox.getStatus().equals(Constants.FROZEN_BOX_SPLITED))) {
                continue;
            }
            StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
            stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
            stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
            stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            stockInBoxDetail.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
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

    /**
     * 输入设备编码，区域编码，架子编码，返回架子中的所有盒子信息
     * @param equipmentCode
     * @param areaCode
     * @param shelfCode
     * @return
     */
    @Override
    public List<StockInBoxDetail> getFrozenBoxByEquipmentAndAreaAndShelves(String equipmentCode, String areaCode, String shelfCode) {
        List<FrozenBox> frozenBoxs = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCode(equipmentCode, areaCode, shelfCode);
        List<StockInBoxDetail> res = new ArrayList<StockInBoxDetail>();
        for (FrozenBox frozenBox : frozenBoxs) {
            if (frozenBox.getStatus() != null &&
                (frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKING))) {
                continue;
            }
            StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
            stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
            stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
            stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            stockInBoxDetail.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
            stockInBoxDetail.setMemo(frozenBox.getMemo());
            // stockInBoxDetail.setStockInCode("");
            int countOfSample = frozenTubeRepository.countByFrozenBoxCodeAndStatus(frozenBox.getFrozenBoxCode(), Constants.FROZEN_TUBE_NORMAL);
            stockInBoxDetail.setCountOfSample(countOfSample);
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

    /**
     * 输入完整的位置信息，返回某个盒子的信息
     * @param equipmentCode
     * @param areaCode
     * @param shelfCode
     * @param position
     * @return
     */
    @Override
    public StockInBoxDetail getFrozenBoxByEquipmentAndAreaAndShelvesAndPosition(String equipmentCode, String areaCode, String shelfCode, String position) {
        String columnsInShelf = position.substring(0, 1);
        String rowsInShelf = position.substring(1);
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        FrozenBox frozenBox = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(equipmentCode, areaCode, shelfCode, columnsInShelf, rowsInShelf);
        if (frozenBox == null) {
            return stockInBoxDetail;
        }

        stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        stockInBoxDetail.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
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

    /**
     * 根据冻存盒编码字符串返回入库盒信息
     * @param frozenBoxes
     * @return
     */
    public List<StockInBoxForDataTable> frozenBoxesToStockInBoxForDataTables(List<FrozenBox> frozenBoxes) {
        List<StockInBoxForDataTable> stockInBoxForDataTables = new ArrayList<>();
        for (FrozenBox box : frozenBoxes) {
            StockInBoxForDataTable stockInBoxForDataTable = frozenBoxToStockInBoxForDataTable(box);
            stockInBoxForDataTables.add(stockInBoxForDataTable);
        }
        return stockInBoxForDataTables;
    }


    /**
     * 根据冻存盒对象构造入库盒对象
     * @param box
     * @return
     */
    public StockInBoxForDataTable frozenBoxToStockInBoxForDataTable(FrozenBox box) {
        StockInBoxForDataTable stockInBoxForDataTable = new StockInBoxForDataTable();
        if (box == null) {
            return null;
        }
        stockInBoxForDataTable.setIsSplit(box.getIsSplit());
        Long countOfSample = frozenTubeRepository.countFrozenTubeListByBoxCode(box.getFrozenBoxCode());
        stockInBoxForDataTable.setCountOfSample(countOfSample.intValue());
        stockInBoxForDataTable.setId(box.getId());
        stockInBoxForDataTable.setFrozenBoxCode(box.getFrozenBoxCode());
        stockInBoxForDataTable.setFrozenBoxCode1D(box.getFrozenBoxCode1D());
        String position = BankUtil.getPositionString(box);
        stockInBoxForDataTable.setPosition(position);
        stockInBoxForDataTable.setSampleTypeName(box.getSampleTypeName());
        stockInBoxForDataTable.setSampleClassificationName(box.getSampleClassification() != null ? box.getSampleClassification().getSampleClassificationName() : null);
        stockInBoxForDataTable.setStatus(box.getStatus());
        return stockInBoxForDataTable;
    }

    /**
     * 构造入库盒
     * @param frozenBox
     * @param stockInCode
     * @return
     */
    public StockInBoxDetail createStockInBoxDetail(StockInBox frozenBox, String stockInCode) {
        StockInBoxDetail stockInBoxDetail = new StockInBoxDetail();
        stockInBoxDetail.setIsSplit(frozenBox.getIsSplit());
        stockInBoxDetail.setFrozenBoxId(frozenBox.getId());
        stockInBoxDetail.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
        stockInBoxDetail.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
        stockInBoxDetail.setMemo(frozenBox.getMemo());
        stockInBoxDetail.setStockInCode(stockInCode);
        List<StockInTube> frozenTubes = stockInTubeRepository.findByStockInBoxId(frozenBox.getId());
        stockInBoxDetail.setCountOfSample(frozenTubes.size());
        stockInBoxDetail.setEquipment(equipmentMapper.equipmentToEquipmentDTO(frozenBox.getEquipment()));
        stockInBoxDetail.setArea(areaMapper.areaToAreaDTO(frozenBox.getArea()));
        stockInBoxDetail.setShelf(supportRackMapper.supportRackToSupportRackDTO(frozenBox.getSupportRack()));
        stockInBoxDetail.setEquipmentId(frozenBox.getEquipment() != null ? frozenBox.getEquipment().getId() : null);
        stockInBoxDetail.setAreaId(frozenBox.getArea() != null ? frozenBox.getArea().getId() : null);
        stockInBoxDetail.setSupportRackId(frozenBox.getSupportRack() != null ? frozenBox.getSupportRack().getId() : null);
        stockInBoxDetail.setColumnsInShelf(frozenBox.getColumnsInShelf());
        stockInBoxDetail.setRowsInShelf(frozenBox.getRowsInShelf());
        stockInBoxDetail.setFrozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns());
        stockInBoxDetail.setFrozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows());
        stockInBoxDetail.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
        stockInBoxDetail.setStatus(frozenBox.getStatus());
        return stockInBoxDetail;
    }

    /**
     * 判断盒子编码是否已经存在  ----true：已经存在，false:不存在
     * @param frozenBoxCode
     * @return
     */
    @Override
    public Boolean isRepeatFrozenBoxCode(String frozenBoxCode) {
        Boolean flag = false;
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if (frozenBox != null && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_INVALID)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取未满冻存盒
     * @param frozenBoxCode
     * @param stockInCode
     * @return
     */
    @Override
    public List<StockInBoxForIncomplete> getIncompleteFrozenBoxeList(String frozenBoxCode, String stockInCode) {
        List<StockInBoxForIncomplete> stockInBoxForIncompleteList = new ArrayList<StockInBoxForIncomplete>();

        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        SampleType sampleType = frozenBox.getSampleType();
        if (sampleType == null) {
            throw new BankServiceException("该冻存盒没有样本类型！", frozenBoxCode);
        }

        FrozenBoxType frozenBoxType = frozenBox.getFrozenBoxType();
        if (frozenBoxType == null) {
            throw new BankServiceException("未查询到该冻存盒的类型！", frozenBoxCode);
        }
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findByProjectIdAndSampleTypeId(frozenBox.getProject().getId(), sampleType.getId());

        List<Long> sampleClassificationIdStr = new ArrayList<Long>();

        for (ProjectSampleClass s : projectSampleClasses) {
            sampleClassificationIdStr.add(s.getSampleClassification().getId());
        }
        List<FrozenBox> frozenBoxList = new ArrayList<FrozenBox>();
        List<FrozenBox> wrongFrozenBoxList = new ArrayList<FrozenBox>();
        //取本次入库的所有冻存盒
        List<StockInBox> stockInBoxes = stockInBoxRepository.findStockInBoxByStockInCodeAndStatus(stockInCode, Constants.FROZEN_BOX_STOCKING);
        SampleType wrongSample = sampleTypeRepository.findBySampleTypeCode("97");
        Map<Long, List<FrozenBox>> map = new HashMap<Long, List<FrozenBox>>();
        for (StockInBox inBox : stockInBoxes) {
            FrozenBox boxIn = inBox.getFrozenBox();
            if (boxIn.getFrozenBoxTypeCode().equals(frozenBox.getFrozenBoxTypeCode())
                && boxIn.getId() != frozenBox.getId() && boxIn.getIsSplit().equals(Constants.NO)) {
                Long countOfSampleOriginal = frozenTubeRepository.countByFrozenBoxCodeAndFrozenTubeState(boxIn.getFrozenBoxCode(), Constants.FROZEN_BOX_STOCKED);
                Long countOfSampleCurrent = stockInTubeRepository.countByFrozenBoxCodeAndStockInCode(boxIn.getFrozenBoxCode(), stockInCode);
                String columns = boxIn.getFrozenBoxTypeColumns() != null ? boxIn.getFrozenBoxTypeColumns() : new String("0");
                String rows = boxIn.getFrozenBoxTypeRows() != null ? boxIn.getFrozenBoxTypeRows() : new String("0");
                int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
                if ((countOfSampleOriginal.intValue() + countOfSampleCurrent.intValue()) == allCounts) {
                    continue;
                }
                Long key = null;
                if (boxIn.getSampleTypeCode().equals("97")) {
                    wrongFrozenBoxList.add(boxIn);
                    continue;
                }
                if (sampleClassificationIdStr.size() > 0) {//有分类
                    key = boxIn.getSampleClassification().getId();
                } else {//无分类
                    key = boxIn.getSampleType().getId();
                }
                List<FrozenBox> frozenBoxes = map.get(key);
                if (frozenBoxes == null || frozenBoxes.size() == 0) {
                    List<FrozenBox> frozenBoxLists = new ArrayList<FrozenBox>();
                    frozenBoxLists.add(boxIn);
                    map.put(key, frozenBoxLists);
                } else {
                    frozenBoxes.add(boxIn);
                    map.put(key, frozenBoxes);
                }
            }
        }

        //有分类---取相同分类的
        if (sampleClassificationIdStr.size() > 0) {
//            List<Long> frozenBoxIdLastList = new ArrayList<>();
//            Long countOfAllFrozenBox = frozenBoxRepository.count();
            for (Long id : sampleClassificationIdStr) {
                if (map.get(id) != null && map.get(id).size() > 0) {
                    frozenBoxList.addAll(map.get(id));
                } else {
                    //取库存
                    for (int i = 0;i< countOfAllFrozenBox.intValue(); i += 200) {
                        int length = 200;
                        List<Object[]> frozenBoxIdsAndCount = frozenBoxRepository
                            .findIncompleteFrozenBoxIdBydProjectIdAnSampleClassificationIdAndBoxTypeId(
                                frozenBox.getProject().getId(),
                                new ArrayList<Long>() {{
                                    add(id);
                                }},
                                frozenBoxType.getId(),
                                frozenBoxCode,
                                i, length);

                        List<Long> boxIds = frozenBoxIdsAndCount.stream().map(s -> {
                            return Long.valueOf(s[0].toString());
                        }).collect(Collectors.toList());

                        if(boxIds==null||boxIds.size()==0){
                            continue;
                        }
                        //取当前冻存盒的数据量
                        List<Object[]> countOfSampleGroupByBoxId = frozenTubeRepository.countGroupByFrozenBoxId(boxIds);
                        //取当前冻存盒号在本次入库的入库量
                        List<Object[]> countOfStockInSampleGroupByBoxId = stockInTubeRepository.countByFrozenBoxIdsAndStockInCodeGroupByFrozenBoxId(boxIds, stockInCode);
                        Map<Object, List<Object[]>> countOfSampleMapGroupByBoxId = countOfSampleGroupByBoxId.stream().collect(Collectors.groupingBy(s -> s[0]));
                        Map<Object, List<Object[]>> countOfStockInSampleMapGroupByBoxId = countOfStockInSampleGroupByBoxId.stream().collect(Collectors.groupingBy(s -> s[0]));
                        TreeMap<Integer, List<Long>> boxMap = new TreeMap<>();
                        for (Object[] box : frozenBoxIdsAndCount) {
                            Long boxId = Long.valueOf(box[0].toString());
                            int allCount = Integer.valueOf(box[1].toString());
                            int oldCount = countOfSampleMapGroupByBoxId.get(box[0]) != null ? Integer.valueOf(countOfSampleMapGroupByBoxId.get(box[0]).get(0)[1].toString()) : 0;
                            int nowCount = countOfStockInSampleMapGroupByBoxId.get(box[0]) != null ? Integer.valueOf(countOfStockInSampleMapGroupByBoxId.get(box[0]).get(0)[1].toString()) : 0;
                            int lastCount = oldCount + nowCount;
                            if (allCount > lastCount) {
                                List<Long> oldBoxIds = boxMap.get(lastCount);
                                List<Long> newBoxIds = new ArrayList<>();
                                if (oldBoxIds != null) {
                                    newBoxIds.addAll(oldBoxIds);
                                }
                                newBoxIds.add(boxId);
                                boxMap.put(lastCount, newBoxIds);
                            }
                        }
                        if (boxMap.size() > 0) {
                            Long frozenBoxId = boxMap.firstEntry().getValue().get(0);
                            frozenBoxIdLastList.add(frozenBoxId);
                            break;
                        }
                    }
                }
            }

            List<FrozenBox> frozenBoxLast = frozenBoxRepository.findByIdIn(frozenBoxIdLastList);
            frozenBoxList.addAll(frozenBoxLast);

        }
        //无分类---取相同类型的
        else {
            Long sampleTypeId = null;
            if (!sampleType.getSampleTypeCode().equals("98") && !sampleType.getSampleTypeCode().equals("99")) {
                sampleTypeId = sampleType.getId();
                if (map.get(sampleTypeId) != null && map.get(sampleTypeId).size() > 0) {
                    frozenBoxList.addAll(map.get(sampleTypeId));
                } else {
                    //取库存
                    List<FrozenBox> stockInFrozenBox = frozenBoxRepository.findIncompleteFrozenBoxBySampleTypeIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), sampleTypeId, frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED, stockInCode);
                    frozenBoxList.addAll(stockInFrozenBox);
                }
            }

        }
        //取库存中问题冻存盒
        if (wrongFrozenBoxList == null || wrongFrozenBoxList.size() == 0) {
            wrongFrozenBoxList = frozenBoxRepository.findIncompleteFrozenBoxBySampleTypeIdInAllStock(frozenBoxCode, frozenBox.getProject().getId(), wrongSample.getId(), frozenBoxType.getId(), Constants.FROZEN_BOX_STOCKED, stockInCode);
        }
        frozenBoxList.addAll(wrongFrozenBoxList);

        List<String> boxCodeStr = new ArrayList<>();
        frozenBoxList.forEach(s -> {
            boxCodeStr.add(s.getFrozenBoxCode());
        });
        List<StockInTube> stockInTubeList = new ArrayList<>();
        List<FrozenTube> frozenTubeList = new ArrayList<>();
        if(boxCodeStr!=null&&boxCodeStr.size()>0){
            stockInTubeList = stockInTubeRepository.findByFrozenBoxCodeInAndStockInCode(boxCodeStr, stockInCode);
            frozenTubeList = frozenTubeRepository.findByFrozenBoxCodeInAndStatusNot(boxCodeStr,Constants.INVALID);
        }
        Map<String, List<StockInTube>> stockInTubeMapGroupByFrozenBoxCode = stockInTubeList.stream().collect(Collectors.groupingBy(s -> s.getFrozenBoxCode()));
        Map<String, List<FrozenTube>> frozenTubeMapGroupByFrozenBoxCode = frozenTubeList.stream().collect(Collectors.groupingBy(s -> s.getFrozenBoxCode()));

        for (FrozenBox f : frozenBoxList) {
            StockInBoxForIncomplete stockInBoxForIncomplete = new StockInBoxForIncomplete();
            stockInBoxForIncomplete.setFrozenBoxId(f.getId());
            stockInBoxForIncomplete.setFrozenBoxCode(f.getFrozenBoxCode());
            stockInBoxForIncomplete.setFrozenBoxCode1D(f.getFrozenBoxCode1D());
            stockInBoxForIncomplete.setFrozenBoxType(frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(f.getFrozenBoxType()));
            stockInBoxForIncomplete.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(f.getSampleType()));
            stockInBoxForIncomplete.setSampleClassification(sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(f.getSampleClassification()));
            List<StockInTube> stockInTubes = stockInTubeMapGroupByFrozenBoxCode.get(f.getFrozenBoxCode()) != null ? stockInTubeMapGroupByFrozenBoxCode.get(f.getFrozenBoxCode()) : new ArrayList<>();
            List<FrozenTube> frozenTubes = frozenTubeMapGroupByFrozenBoxCode.get(f.getFrozenBoxCode()) != null ? frozenTubeMapGroupByFrozenBoxCode.get(f.getFrozenBoxCode()) : new ArrayList<>();
            List<StockInTubeForBox> stockInTubeForBoxes = new ArrayList<StockInTubeForBox>();
            for (FrozenTube t : frozenTubes) {
                StockInTubeForBox inTubeForBox = new StockInTubeForBox();
                inTubeForBox.setId(t.getId());
                inTubeForBox.setFrozenBoxCode(f.getFrozenBoxCode());
                inTubeForBox.setTubeColumns(t.getTubeColumns());
                inTubeForBox.setTubeRows(t.getTubeRows());
                inTubeForBox.setSampleCode(t.getSampleCode()!=null?t.getSampleCode():t.getSampleTempCode());
                stockInTubeForBoxes.add(inTubeForBox);
            }
            for (StockInTube stockInTube : stockInTubes) {
                int flag = 0;
                for (StockInTubeForBox stockInTubeForBox : stockInTubeForBoxes) {
                    if (stockInTubeForBox.getId() == stockInTube.getFrozenTube().getId()) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    continue;
                }
                StockInTubeForBox inTubeForBox = new StockInTubeForBox();
                inTubeForBox.setId(stockInTube.getFrozenTube().getId());
                inTubeForBox.setFrozenBoxCode(stockInTube.getFrozenBoxCode());
                inTubeForBox.setTubeColumns(stockInTube.getTubeColumns());
                inTubeForBox.setTubeRows(stockInTube.getTubeRows());
                inTubeForBox.setSampleCode(stockInTube.getSampleCode()!=null?stockInTube.getSampleCode():stockInTube.getSampleTempCode());
                stockInTubeForBoxes.add(inTubeForBox);
            }
            FrozenBoxType boxType = f.getFrozenBoxType();
            String columns = boxType.getFrozenBoxTypeColumns() != null ? boxType.getFrozenBoxTypeColumns() : new String("0");
            String rows = boxType.getFrozenBoxTypeRows() != null ? boxType.getFrozenBoxTypeRows() : new String("0");
            int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
            if (stockInTubeForBoxes.size() >= allCounts) {
                continue;
            }
            stockInBoxForIncomplete.setCountOfSample(stockInTubeForBoxes.size());
            stockInBoxForIncomplete.setStockInFrozenTubeList(stockInTubeForBoxes);
            stockInBoxForIncompleteList.add(stockInBoxForIncomplete);
        }

        return stockInBoxForIncompleteList;
    }

    /**
     * 冻存盒直接入库，取原冻存盒的信息
     * @param frozenBoxCode
     * @return
     */
    @Override
    public FrozenBoxDTO getBoxAndTubeByForzenBoxCode(String frozenBoxCode) {

        //查询冻存盒信息
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);

        //只能给已出库和已入库的和已交接的
        if (frozenBox == null) {
            return new FrozenBoxDTO();
        } else if (frozenBox != null && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
            && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED)
            && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)) {
            throw new BankServiceException("冻存盒编码已存在！");
        }

        List<StockInTube> stockInTubes = new ArrayList<StockInTube>();
//        if (!frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED) && !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)) {
//            stockInTubes = stockInTubeRepository.findByFrozenBoxCodeAndSampleState(frozenBoxCode);
//        }

        List<FrozenTube> frozenTubeList = frozenTubeRepository.findByFrozenBoxCodeAndFrozenTubeState(frozenBoxCode,Constants.FROZEN_BOX_STOCKED);

        List<Long> ids = new ArrayList<Long>();
        for (StockInTube s : stockInTubes) {
            ids.add(s.getFrozenTube().getId());
        }
        String columns = frozenBox.getFrozenBoxTypeColumns() != null ? frozenBox.getFrozenBoxTypeColumns() : new String("0");
        String rows = frozenBox.getFrozenBoxTypeRows() != null ? frozenBox.getFrozenBoxTypeRows() : new String("0");
        int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
        if (stockInTubes.size() == allCounts) {
            throw new BankServiceException("冻存盒已满！");
        }
        List<StockInTubeDTO> frozenTubeDTOS = new ArrayList<StockInTubeDTO>();
//        Map<Long, FrozenTubeHistory> allFrozenTubeHistories = ids.size() > 0 ? stockListService.findFrozenTubeHistoryDetailByIds(ids) : null;
        for (FrozenTube f : frozenTubeList) {

            StockInTubeDTO stockInTubeDTO = stockInTubeMapper.frozenTubeToStockInTubeDTO(f);
            stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_NEW);//盒内新增样本
            if(stockInTubeDTO.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCKED)){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_ORIGINAL);//原盒原库存
            }else if (stockInTubeDTO.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
                ||stockInTubeDTO.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)){
                stockInTubeDTO.setFlag(Constants.FROZEN_FLAG_STOCKIN_AGAIN);//出库再回来
            }
            frozenTubeDTOS.add(stockInTubeDTO);
        }
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);
        frozenBoxDTO.setFrozenTubeDTOS(frozenTubeDTOS);
        frozenBoxDTO.setFrontColor(frozenBox.getSampleType() != null ? frozenBox.getSampleType().getFrontColor() : null);
        frozenBoxDTO.setFrontColorForClass(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getFrontColor() : null);
        frozenBoxDTO.setBackColor(frozenBox.getSampleType() != null ? frozenBox.getSampleType().getBackColor() : null);
        frozenBoxDTO.setBackColorForClass(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getBackColor() : null);
        frozenBoxDTO.setIsMixed(frozenBox.getSampleType() != null ? frozenBox.getSampleType().getIsMixed() : null);
        frozenBoxDTO.setSampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null);
        frozenBoxDTO.setSampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null);
        return frozenBoxDTO;
    }

    /**
     * 查询冻存盒的上一次状态
     *
     * @param id
     * @return
     */
    @Override
    public String findFrozenBoxHistory(Long id) {
        String status = Constants.FROZEN_BOX_INVALID;
        List<Object[]> histroy = new ArrayList<>();
        //查詢转运历史
        List<Object[]> transhipHistory = frozenBoxRepository.findFrozenBoxTranshipHistory(id);
        //查詢入库历史
        List<Object[]> stockInHistory = frozenBoxRepository.findFrozenBoxStockInHistory(id);
        //查询出库历史
        List<Object[]> stockOutHistory = frozenBoxRepository.findFrozenBoxStockOutHistory(id);
        //查询交接历史
        List<Object[]> handOverHistory = frozenBoxRepository.findFrozenBoxStockOutHandOverHistory(id);
        //查询移位历史
        List<Object[]> moveHistory = frozenBoxRepository.findFrozenBoxMoveHistory(id);
        //查询换位历史
        List<Object[]> changeHistory = frozenBoxRepository.findFrozenBoxChangeHistory(id);
        //查询销毁历史
        List<Object[]> destroyHistory = frozenBoxRepository.findFrozenBoxDestoryHistory(id);
        histroy.addAll(transhipHistory);
        histroy.addAll(stockInHistory);
        histroy.addAll(stockOutHistory);
        histroy.addAll(handOverHistory);
        histroy.addAll(moveHistory);
        histroy.addAll(changeHistory);
        histroy.addAll(destroyHistory);
        Map<String, List<Object[]>> histroyMap =
            histroy.stream().collect(Collectors.groupingBy(w -> (w[20] != null ? w[20].toString() : null)));
        TreeMap<String, List<Object[]>> listTreeMap = new TreeMap<>(Collections.reverseOrder());
        listTreeMap.putAll(histroyMap);
        for (String key : histroyMap.keySet()) {
            List<Object[]> boxHistroyList = histroyMap.get(key);
            int i = boxHistroyList.size() - 1;
            String type = boxHistroyList.get(i)[24].toString();
            if (type.equals("101") || type.equals(101)) {
                status = Constants.FROZEN_BOX_STOCKING;
            } else if (type.equals("102") || type.equals(102)) {
                status = Constants.FROZEN_BOX_STOCKED;
            } else if (type.equals("103") || type.equals(103)) {
                status = Constants.FROZEN_BOX_STOCK_OUT_COMPLETED;
            } else if (type.equals("104") || type.equals(104)) {
                status = Constants.FROZEN_BOX_STOCKED;
            } else if (type.equals("105") || type.equals(105)) {
                status = Constants.FROZEN_BOX_STOCKED;
            } else if (type.equals("106") || type.equals(106)) {
                status = Constants.FROZEN_BOX_DESTROY;
            } else if (type.equals("107") || type.equals(107)) {
                status = Constants.FROZEN_BOX_STOCK_OUT_HANDOVER;
            }
        }
        return status;
    }

    @Override
    public String makeNewFrozenBoxCode(Long projectId, Long sampleTypeId, Long sampleClassId) {
        String newBoxCode = "";
        if (projectId == null || (sampleTypeId == null && sampleClassId == null)) {
            throw new BankServiceException("必须指定项目，样本类型或者样本分类。");
        }

        List<ProjectSampleClass> projectSampleClass = null;
        SampleClassification sampleClassification = null;
        SampleType sampleType = null;
        String projectCode = null;
        String sampleClassCode = null;
        if (sampleClassId != null) {
            sampleClassification = sampleClassificationRepository.findOne(sampleClassId);
            projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleClassificationId(projectId, sampleClassId);
            if (projectSampleClass.size() == 0) {
                throw new BankServiceException("指定的项目和样本分类无效。");
            }
            sampleType = projectSampleClass.get(0).getSampleType();
        } else if (sampleTypeId != null) {
            sampleType = sampleTypeRepository.findOne(sampleTypeId);
            projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeId(projectId, sampleTypeId);
            if (projectSampleClass.size() == 0) {
                throw new BankServiceException("指定的项目和样本类型无效。");
            } else if (sampleType.getIsMixed() != 1 && projectSampleClass.size() >= 1) {
                throw new BankServiceException("指定的项目和样本类型有多个样本分类，必须指定其中一个样本分类。");
            }
            sampleClassification = projectSampleClass.get(0).getSampleClassification();
        }

        projectCode = projectSampleClass.get(0).getProjectCode();
        sampleClassCode = sampleType.getIsMixed() == 1 ? sampleType.getSampleTypeCode() : sampleClassification.getSampleClassificationCode();
        final String boxCodePrefix = projectCode + sampleClassCode;
        int codeLength = boxCodePrefix.length() + 5;
        sampleTypeId = sampleType.getId();
        sampleClassId = sampleType.getIsMixed() == 1 ? 0 : sampleClassification.getId();

        TreeSet<String> boxCode = new TreeSet<>();
        List<String> boxCodeInTranship = frozenBoxRepository.findAllTranshipFrozenBoxCode(projectId, sampleTypeId, sampleClassId);
        List<String> boxCodeInStockIn = frozenBoxRepository.findAllStockInFrozenBoxCode(projectId, sampleTypeId, sampleClassId);
        List<String> boxCodeInDestroy = frozenBoxRepository.findAllDestroyFrozenBoxCode(projectId, sampleTypeId, sampleClassId);
        boxCode.addAll(boxCodeInTranship.stream().filter(code -> code.length() >= codeLength && code.startsWith(boxCodePrefix)).collect(Collectors.toList()));
        boxCode.addAll(boxCodeInStockIn.stream().filter(code -> code.length() >= codeLength && code.startsWith(boxCodePrefix)).collect(Collectors.toList()));
        boxCode.addAll(boxCodeInDestroy.stream().filter(code -> code.length() >= codeLength && code.startsWith(boxCodePrefix)).collect(Collectors.toList()));

        if (boxCode.size() == 0) {
            newBoxCode = boxCodePrefix + "00001";
        } else {
            newBoxCode = boxCode.last().substring(boxCodePrefix.length(), codeLength);
            Long no;
            try {
                no = Long.parseLong(newBoxCode);
            } catch (Exception e) {
                throw new BankServiceException("库存中冻存盒编码规则有误无法自动创建。");
            }
            if (no == 99999) {
                throw new BankServiceException("库存中冻存盒编码达到最大限额无法自动创建。");
            }
            newBoxCode = String.format("%s%05d", boxCodePrefix, no + 1);
        }

        return newBoxCode;
    }
    /**
     * 获取指定冻存盒编码的冻存盒信息（包含本次入库单内待入库冻存盒，全部已入库未满，已出库，已交接冻存盒）
     * @param frozenBoxCode
     * @param stockInCode
     * @return
     */
    @Override
    public StockInBoxForIncomplete getIncompleteSpecifyFrozenBox(String frozenBoxCode, Long projectId,String stockInCode) {
        //定义返回的冻存盒
        StockInBoxForIncomplete stockInBoxForIncomplete = new StockInBoxForIncomplete();
        //定义返回的冻存盒内的样本
        List<StockInTubeForBox> stockInFrozenTubeList = new ArrayList<>();
        //获取冻存盒详情
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if(frozenBox == null){
            return stockInBoxForIncomplete;
        }
        if(frozenBox.getProject()!=null&&frozenBox.getProject().getId()!=projectId){
            throw new BankServiceException("冻存盒"+frozenBoxCode+"属于"+frozenBox.getProjectCode()+"项目，与当前入库单的项目不一致，不能用于分装！");
        }

        String status = frozenBox.getStatus();
        if(!status.equals(Constants.FROZEN_BOX_STOCKING)&&!status.equals(Constants.FROZEN_BOX_STOCKED)
            &&!status.equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)&&!status.equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)){
            throw new BankServiceException("冻存盒"+frozenBoxCode+"状态为"+Constants.FROZEN_BOX_STATUS_MAP.get(status)+"，不能用于分装！");
        }
        //判断冻存盒状态，如果是已入库，判断盒内样本是否已满，如果满了，提示错误
        //如果是待入库，判断是否在该入库单内，如果是判断在本次入库单内，盒内样本是否已满
        if(status.equals(Constants.FROZEN_BOX_STOCKING)||(frozenBox.getLockFlag()!=null&&frozenBox.getLockFlag().equals(Constants.FROZEN_BOX_LOCKED_FOR_SPLIT))){
            StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockInCode,frozenBoxCode);
            if(stockInBox == null){
                throw new BankServiceException("冻存盒"+frozenBoxCode+"被其他入库单锁定，不能用于分装！");
            }
        }
        //查询盒内样本
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findByFrozenBoxCodeAndFrozenTubeState(frozenBox.getFrozenBoxCode(),Constants.FROZEN_BOX_STOCKED);
        List<StockInTube> stockInTubesByBoxAndStockInCode = stockInTubeRepository.findByFrozenBoxCodeAndStockInCode(frozenBoxCode,stockInCode);
        stockInFrozenTubeList.addAll(frozenTubeMapper.frozenTubesToStockInTubesForBox(frozenTubeList));
        stockInFrozenTubeList.addAll(stockInTubeMapper.stockInTubesToStockInTubesForBox(stockInTubesByBoxAndStockInCode));

        String columns = frozenBox.getFrozenBoxTypeColumns()!=null?frozenBox.getFrozenBoxTypeColumns():new String("0");
        String rows = frozenBox.getFrozenBoxTypeRows()!=null?frozenBox.getFrozenBoxTypeRows():new String("0");
        int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);

        if(stockInFrozenTubeList.size()==allCounts){
            throw new BankServiceException("冻存盒已满！");
        }
        stockInBoxForIncomplete = frozenBoxMapper.frozenBoxDTOToStockInBoxForIncomplete(frozenBox,stockInFrozenTubeList);
        stockInBoxForIncomplete.setFrozenBoxType(frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBox.getFrozenBoxType()));
        stockInBoxForIncomplete.setSampleClassification(sampleClassificationMapper.sampleClassificationToSampleClassificationDTO(frozenBox.getSampleClassification()));
        stockInBoxForIncomplete.setSampleType(sampleTypeMapper.sampleTypeToSampleTypeDTO(frozenBox.getSampleType()));
        return stockInBoxForIncomplete;
    }
}
