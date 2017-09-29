package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.FrozenBoxCheckService;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.service.UserService;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.StockOutFrozenBoxMapper;
import org.fwoxford.service.mapper.StockOutTaskMapper;
import org.fwoxford.web.rest.StockOutFrozenBoxPoisition;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing StockOutFrozenBox.
 */
@Service
@Transactional
public class StockOutFrozenBoxServiceImpl implements StockOutFrozenBoxService{

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenBoxServiceImpl.class);

    private final StockOutFrozenBoxRepository stockOutFrozenBoxRepository;

    private final StockOutFrozenBoxMapper stockOutFrozenBoxMapper;

    @Autowired
    private StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    @Autowired
    private StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    @Autowired
    private StockOutTaskRepository stockOutTaskRepository;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private StockOutBoxTubeRepository stockOutBoxTubeRepository;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private FrozenBoxTypeRepository frozenBoxTypeRepository;

    @Autowired
    private FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private StockOutHandoverRepository stockOutHandoverRepository;

    @Autowired
    private StockOutBoxPositionRepository stockOutBoxPositionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private StockOutTaskMapper stockOutTaskMapper;

    @Autowired
    private ReportExportingService reportExportingService;

    @Autowired
    private StockOutApplyRepository stockOutApplyRepository;

    @Autowired
    private StockOutHandoverFrozenBoxRepositries stockOutHandoverFrozenBoxRepositries;

    @Autowired
    StockOutPlanRepository stockOutPlanRepository;

    @Autowired
    StockOutFrozenBoxForWaitingRepositries stockOutFrozenBoxForWaitingRepositries;

    @Autowired
    StockOutFrozenBoxInTaskRepositries stockOutFrozenBoxInTaskRepositries;

    @Autowired
    StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    private FrozenBoxCheckService frozenBoxCheckService;

    public StockOutFrozenBoxServiceImpl(StockOutFrozenBoxRepository stockOutFrozenBoxRepository
            , StockOutFrozenBoxMapper stockOutFrozenBoxMapper) {
        this.stockOutFrozenBoxRepository = stockOutFrozenBoxRepository;
        this.stockOutFrozenBoxMapper = stockOutFrozenBoxMapper;
    }

    /**
     * Save a stockOutFrozenBox.
     *
     * @param stockOutFrozenBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutFrozenBoxDTO save(StockOutFrozenBoxDTO stockOutFrozenBoxDTO) {
        log.debug("Request to save StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxMapper.stockOutFrozenBoxDTOToStockOutFrozenBox(stockOutFrozenBoxDTO);
        stockOutFrozenBox = stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        StockOutFrozenBoxDTO result = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);
        return result;
    }

    /**
     *  Get all the stockOutFrozenBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutFrozenBoxes");
        Page<StockOutFrozenBox> result = stockOutFrozenBoxRepository.findAll(pageable);
        return result.map(stockOutFrozenBox -> stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox));
    }

    /**
     *  Get one stockOutFrozenBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutFrozenBoxDTO findOne(Long id) {
        log.debug("Request to get StockOutFrozenBox : {}", id);
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(id);
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);
        return stockOutFrozenBoxDTO;
    }

    /**
     *  Delete the  stockOutFrozenBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutFrozenBox : {}", id);
        stockOutFrozenBoxRepository.delete(id);
    }




    /**
     *  获取指定任务的指定分页的出库盒子.
     *  @param taskId The task id
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenBoxForTaskDataTableEntity> findAllByTask(Long taskId, Pageable pageable) {
        log.debug("Request to get all StockOutFrozenBoxes");
        Page<FrozenBox> result = frozenBoxRepository.findAllByTask(taskId, pageable);
        return result.map(box -> {
            StockOutFrozenBoxForTaskDataTableEntity dto = new StockOutFrozenBoxForTaskDataTableEntity();
            dto.setId(box.getId());
            dto.setFrozenBoxCode(box.getFrozenBoxCode());
            dto.setSampleTypeName(box.getSampleTypeName());
            String position =  BankUtil.getPositionString(box);
            dto.setPosition(position);
            Long count = stockOutTaskFrozenTubeRepository.countSampleByFrozenBoxAndTask(box.getId(),taskId);
            dto.setCountOfSample(count);

            return dto;
        });
    }

    @Override
    public Page<StockOutFrozenBoxForTaskDataTableEntity> findAllByrequirementIds(List<Long> ids, Pageable pageable) {
        Page<FrozenBox> result = frozenBoxRepository.findAllByrequirementIds(ids, pageable);

        return result.map(frozenBox -> {
            StockOutFrozenBoxForTaskDataTableEntity dto = new StockOutFrozenBoxForTaskDataTableEntity();
            dto.setId(frozenBox.getId());
            dto.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            dto.setSampleTypeName(frozenBox.getSampleTypeName());
            String position =  BankUtil.getPositionString(frozenBox);
            dto.setPosition(position);
            Long countOfSample = stockOutPlanFrozenTubeRepository.countByFrozenBoxIdAndRequirement(ids,frozenBox.getId());
            dto.setCountOfSample(countOfSample);

            return dto;
        });
    }

    @Override
    public List<StockOutFrozenBoxForTaskDataTableEntity> getAllStockOutFrozenBoxesByTask(Long taskId) {
        List<StockOutFrozenBoxForTaskDataTableEntity> alist = new ArrayList<StockOutFrozenBoxForTaskDataTableEntity>();
        List<FrozenBox> boxes =  frozenBoxRepository.findByStockOutTaskId(taskId);
        List<Object[]> countOfSampleGroupByBox = stockOutReqFrozenTubeRepository.countByTaskGroupByBox(taskId);
        for(FrozenBox frozenBox :boxes){
            StockOutFrozenBoxForTaskDataTableEntity box = new StockOutFrozenBoxForTaskDataTableEntity();
            if(frozenBox ==null){continue;}
            Long count = 0L;
            for(Object[] obj:countOfSampleGroupByBox){
                if( obj[0]!=null&&frozenBox.getId().equals(Long.valueOf(obj[0].toString()))){
                    count = obj[1]!=null?Long.valueOf(obj[1].toString()):0L;
                    break;
                }
            }
//            Long count = stockOutTaskFrozenTubeRepository.countByFrozenBoxAndTask(frozenBox.getId(),taskId);
            if(count.intValue()==0){
                continue;
            }
            box.setId(frozenBox.getId());
            box.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            box.setSampleTypeName(frozenBox.getSampleTypeName());
            box.setMemo(frozenBox.getMemo());
            box.setSampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null);
            box.setStatus(frozenBox.getStatus());
            box.setProjectName(frozenBox.getProjectName());
            box.setProjectCode(frozenBox.getProjectCode());
            String position = BankUtil.getPositionString(frozenBox);
            box.setPosition(position);
            box.setCountOfSample(count);
            alist.add(box);
        }
        return alist;
    }

    /**
     * 临时盒的保存
     * @param frozenBoxDTO
     * @return
     */
    @Override
    public List<FrozenBoxAndFrozenTubeResponse> createFrozenBoxForStockOut(List<FrozenBoxAndFrozenTubeResponse> frozenBoxDTO, Long taskId) {
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(taskId);
        if(stockOutTask == null){
            throw new BankServiceException("任务ID无效！");
        }
        List<FrozenBoxType> boxTypes = frozenBoxTypeRepository.findAllFrozenBoxTypes();
        for(FrozenBoxAndFrozenTubeResponse box:frozenBoxDTO){
            if(box.getFrozenBoxCode() == null){
                throw new BankServiceException("冻存盒编码不能为空！");
            }
            //验证盒子编码是否存在
            Map<String,Long> map = new HashMap<String,Long>();
            Long id = box.getId()==null?-1:box.getId();
            map.put(box.getFrozenBoxCode(),id);
            frozenBoxCheckService.checkFrozenBoxCodeRepead(map);
//            List<Object[]> obj = frozenBoxRepository.countByFrozenBoxCode(box.getFrozenBoxCode());
//            for(Object[] o:obj){
//                String frozenBoxId = o[0].toString();
//                if(box.getId()==null){
//                    throw new BankServiceException("冻存盒编码已存在！",box.getFrozenBoxCode());
//                }else if(box.getId()!=null&&!box.getId().toString().equals(frozenBoxId)){
//                    throw new BankServiceException("冻存盒编码已存在！",box.getFrozenBoxCode());
//                }
//            }
            FrozenBox frozenBox = new FrozenBox();
            if(box.getId()!=null){
                frozenBox = frozenBoxRepository.findOne(box.getId())!=null?frozenBoxRepository.findOne(box.getId()):new FrozenBox();
            }
            FrozenBoxType boxType = new FrozenBoxType();
            if (box.getFrozenBoxTypeId() != null) {
                FrozenBoxType frozenBoxType = new FrozenBoxType();
                frozenBoxType.setId(box.getFrozenBoxTypeId());
                int boxTypeIndex = boxTypes.indexOf(frozenBoxType);
                if (boxTypeIndex >= 0) {
                    boxType = boxTypes.get(boxTypeIndex);
                    frozenBox.setFrozenBoxType(boxType);
                    frozenBox.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
                    frozenBox.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
                    frozenBox.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
                }else {
                    throw new BankServiceException("冻存盒类型不能为空！",box.toString());
                }
            }

            String columns = boxType.getFrozenBoxTypeColumns()!=null?boxType.getFrozenBoxTypeColumns():new String("0");
            String rows = boxType.getFrozenBoxTypeRows()!=null?boxType.getFrozenBoxTypeRows():new String("0");
            int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
            if(box.getFrozenTubeDTOS().size()==0){
                continue;
            }
            if(box.getFrozenTubeDTOS().size()>allCounts){
                throw new BankServiceException("临时盒中冻存管数量错误！",frozenBoxDTO.toString());
            }

            frozenBox.setFrozenBoxCode(box.getFrozenBoxCode());
//            frozenBox.setSampleNumber(box.getFrozenTubeDTOS().size());
            frozenBox.setStatus(Constants.FROZEN_BOX_STOCK_OUT_PENDING);
            frozenBoxRepository.save(frozenBox);
            box.setId(frozenBox.getId());
            //保存出库盒
            StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findByFrozenBoxId(frozenBox.getId());
            if(stockOutFrozenBox == null){
                stockOutFrozenBox = new StockOutFrozenBox();
            }
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_NEW);
            stockOutFrozenBox.setFrozenBox(frozenBox);
            stockOutFrozenBox.setStockOutTask(stockOutTask);
            stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode()).sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName()).memo(frozenBox.getMemo());
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
            List<Long> frozenTubeIds = new ArrayList<>();
            for(FrozenTubeDTO f: box.getFrozenTubeDTOS()){
                frozenTubeIds.add(f.getId());
            }
            //查询要出库的样本
            List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutTaskIdAndFrozenTubeIdInAndStatusNot(taskId,frozenTubeIds,Constants.STOCK_OUT_SAMPLE_WAITING_OUT);
            List<FrozenTube> frozenTubeList = new ArrayList<>();
            for(FrozenTubeDTO f: box.getFrozenTubeDTOS()){
                for(StockOutReqFrozenTube s:stockOutReqFrozenTubes){
                    if(f.getId().equals(s.getFrozenTube().getId())){
                        s.setStockOutFrozenBox(stockOutFrozenBox);
                        s.setStatus(Constants.STOCK_OUT_SAMPLE_WAITING_OUT);
                        FrozenTube frozenTube =s.getFrozenTube();
                        frozenTube.setFrozenBox(frozenBox);
                        frozenTube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                        frozenTube.setTubeColumns(f.getTubeColumns());
                        frozenTube.setTubeRows(f.getTubeRows());
                        frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_PENDING);
                        frozenTubeList.add(frozenTube);
                    }
                }
            }
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
            frozenTubeRepository.save(frozenTubeList);
        }
        return frozenBoxDTO;
    }

    @Override
    public List<FrozenBoxAndFrozenTubeResponse> getAllTempStockOutFrozenBoxesByTask(Long taskId) {
        List<FrozenBoxAndFrozenTubeResponse> alist = new ArrayList<FrozenBoxAndFrozenTubeResponse>();
        List<StockOutFrozenBox> boxes =  stockOutFrozenBoxRepository.findByStockOutTaskIdAndStatus(taskId,Constants.STOCK_OUT_FROZEN_BOX_NEW);
        for(StockOutFrozenBox s :boxes){
            FrozenBox frozenBox = s.getFrozenBox();
            if(frozenBox ==null){continue;}
            //根据冻存盒编码查询冻存管
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
            FrozenBoxType boxType = frozenBox.getFrozenBoxType();
            String columns = boxType.getFrozenBoxTypeColumns()!=null?boxType.getFrozenBoxTypeColumns():new String("0");
            String rows = boxType.getFrozenBoxTypeRows()!=null?boxType.getFrozenBoxTypeRows():new String("0");
            int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
            if(frozenTubes.size()==allCounts){
                continue;
            }
            List<FrozenTubeDTO> frozenTubeResponse = frozenTubeMapper.frozenTubesToFrozenTubeDTOs(frozenTubes);
            FrozenBoxAndFrozenTubeResponse box = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox);
            box.setFrozenTubeDTOS(frozenTubeResponse);
            alist.add(box);
        }
        return alist;
    }

    @Override
    public List<StockOutFrozenBoxDataTableEntity> getStockOutFrozenBoxesByTask(Long taskId) {
        List<StockOutFrozenBoxDataTableEntity> alist = new ArrayList<StockOutFrozenBoxDataTableEntity>();
        List<StockOutFrozenBox> boxes =  stockOutFrozenBoxRepository.findByStockOutTaskId(taskId);
        for(StockOutFrozenBox f :boxes){
            FrozenBox frozenBox = f.getFrozenBox();
            StockOutFrozenBoxDataTableEntity box = new StockOutFrozenBoxDataTableEntity();
            if(frozenBox ==null){continue;}
            box.setId(f.getId());
            box.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            box.setSampleTypeName(frozenBox.getSampleTypeName());
            String position = BankUtil.getPositionString(f.getEquipmentCode(),f.getAreaCode(),f.getSupportRackCode(),f.getColumnsInShelf(),f.getRowsInShelf(),null,null);
            box.setPosition(position);
            Long count = stockOutReqFrozenTubeRepository.countByStockOutFrozenBoxId(f.getId());
            box.setCountOfSample(count);
            box.setMemo(f.getMemo());
            box.setStatus(f.getStatus());
            StockOutHandover stockOutHandover = stockOutHandoverRepository.findByStockOutTaskIdAndstockOutBoxId(taskId,f.getId());
            box.setStockOutHandoverTime(stockOutHandover!=null?stockOutHandover.getHandoverTime():null);
            alist.add(box);
        }
        return alist;
    }

    /**
     * 出库
     * @param stockOutFrozenBoxPoisition
     * @param taskId
     * @param frozenBoxIds
     * @return
     */
    @Override
    public StockOutTaskDTO stockOut(StockOutFrozenBoxPoisition stockOutFrozenBoxPoisition, Long taskId, List<Long> frozenBoxIds) {
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(taskId);
        if(stockOutTask == null){
            throw new BankServiceException("出库任务不存在！");
        }
        //验证负责人密码

        Long loginId1 = stockOutTask.getStockOutHeadId1();
        Long loginId2 = stockOutTask.getStockOutHeadId2();
        if(loginId1 == null || loginId2 == null){
            throw new BankServiceException("出库负责人不能为空！");
        }
        User user1 = userRepository.findOne(loginId1);
        User user2 = userRepository.findOne(loginId2);
        String loginName1 = user1.getLogin();
        String loginName2 =  user2.getLogin();
        String password1 = stockOutFrozenBoxPoisition.getPassword1();
        String password2 = stockOutFrozenBoxPoisition.getPassword2();
        Long equipmentId = stockOutFrozenBoxPoisition.getEquipmentId();
        Long areaId = stockOutFrozenBoxPoisition.getAreaId();

        Equipment equipment = new Equipment();
        if(equipmentId != null){
            equipment = equipmentRepository.findOne(equipmentId);
        }

        Area area = new Area();
        if(areaId != null){
            area = areaRepository.findOne(areaId);
        }
        if(password1 == null || password2 == null){
            throw new BankServiceException("出库负责人密码不能为空！");
        }
        if(loginName1!=null&&password1!=null){
            userService.isCorrectUser(loginName1,password1);
        }
        if(loginName2!=null&&password2!=null){
            userService.isCorrectUser(loginName2,password2);
        }
        List<FrozenBox> frozenBoxList = new ArrayList<>();
        List<StockOutBoxPosition> stockOutBoxPositionList = new ArrayList<StockOutBoxPosition>();
        List<StockOutFrozenBox> stockOutFrozenBoxes = stockOutFrozenBoxRepository.findByIdIn(frozenBoxIds);
        for(StockOutFrozenBox stockOutFrozenBox : stockOutFrozenBoxes){
            FrozenBox frozenBox = stockOutFrozenBox.getFrozenBox();
            //保存出库冻存盒
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_COMPLETED);
            stockOutFrozenBox.setFrozenBox(frozenBox);
            stockOutFrozenBox.setEquipment(equipment);
            stockOutFrozenBox.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);
            stockOutFrozenBox.setArea(area);
            stockOutFrozenBox.setAreaCode(area!=null?area.getAreaCode():null);
            stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode()).sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName()).memo(frozenBox.getMemo());
            //保存冻存盒位置
            StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition();
            stockOutBoxPosition.setStockOutFrozenBox(stockOutFrozenBox);
            stockOutBoxPosition.setEquipment(equipment);
            stockOutBoxPosition.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);
            stockOutBoxPosition.setArea(area);
            stockOutBoxPosition.setAreaCode(area!=null?area.getAreaCode():null);
            stockOutBoxPosition.setStatus(Constants.VALID);
            stockOutBoxPositionList.add(stockOutBoxPosition);
            //保存冻存盒的位置和状态
            frozenBox.setEquipment(equipment);
            frozenBox.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);
            frozenBox.setArea(area);
            frozenBox.setAreaCode(area!=null?area.getAreaCode():null);
            frozenBox.setStatus(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED);
            frozenBoxList.add(frozenBox);
        }
        stockOutFrozenBoxRepository.save(stockOutFrozenBoxes);
        stockOutBoxPositionRepository.save(stockOutBoxPositionList);
        frozenBoxRepository.save(frozenBoxList);
        List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutFrozenBoxIdIn(frozenBoxIds);
       List<FrozenTube> frozenTubeList = new ArrayList<>();
        for(StockOutReqFrozenTube s: stockOutReqFrozenTubes){
            FrozenTube frozenTube = s.getFrozenTube();
            frozenTube.setSampleUsedTimes(frozenTube.getSampleUsedTimes()!=null?frozenTube.getSampleUsedTimes():0+1);
            frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED);
            s.setStatus(Constants.STOCK_OUT_SAMPLE_COMPLETED);
            frozenTubeList.add(frozenTube);
        }
        List<List<StockOutReqFrozenTube>> stockOutReqTubeList = Lists.partition(stockOutReqFrozenTubes, 1000);
        for(List<StockOutReqFrozenTube> f:stockOutReqTubeList){
            stockOutReqFrozenTubeRepository.save(f);
        }
        List<List<FrozenTube>> frozenTubes = Lists.partition(frozenTubeList, 1000);
        for(List<FrozenTube> f:frozenTubes){
            frozenTubeRepository.save(frozenTubeList);
        }

        List<String> statusList = new ArrayList<>();
        statusList.add(Constants.STOCK_OUT_SAMPLE_CANCEL);
        statusList.add(Constants.STOCK_OUT_SAMPLE_COMPLETED);

        //任务未出库样本量
        Long countOfTaskTube = stockOutReqFrozenTubeRepository.countByStockOutTaskIdAndStatusNotIn(taskId,statusList);
        //异常出库样本量
        Long abNormalTube = stockOutReqFrozenTubeRepository.countAbnormalTubeByStockOutTaskId(taskId);
        if(abNormalTube.intValue()==0){
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_ABNORMAL);
        }else if(countOfTaskTube.intValue()==0){
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_COMPLETED);
        }
        stockOutTaskRepository.save(stockOutTask);
        List<String> statusList_ = new ArrayList<>();
        statusList_.add(Constants.STOCK_OUT_TASK_NEW);
        statusList_.add(Constants.STOCK_OUT_TASK_PENDING);

        Long countOfUnCompleteTask = stockOutTaskRepository.countByStockOutPlanIdAndStatusIn(stockOutTask.getStockOutPlan().getId(),statusList_);
        if(countOfUnCompleteTask.intValue()==0){
            StockOutPlan stockOutPlan = stockOutTask.getStockOutPlan();
            stockOutPlan.setStatus(Constants.STOCK_OUT_PLAN_COMPLETED);
            stockOutPlanRepository.save(stockOutPlan);
        }

        //如果任务下的待出库样本都出库了则任务是已完成的状态
        //如果任务下需要出库的样本有异常，则为异常出库
        //如果计划出库的样本都出库了，则计划的状态为已完成，排除已撤销的

        return stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
    }

    @Override
    public StockOutFrozenBoxDTO stockOutNote(StockOutFrozenBoxDTO stockOutFrozenBoxDTO) {
        if(stockOutFrozenBoxDTO == null || stockOutFrozenBoxDTO.getId() == null ){
            throw new BankServiceException("出库盒ID不能为空！");
        }
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(stockOutFrozenBoxDTO.getId());
        if(stockOutFrozenBox == null){
            throw new BankServiceException("出库盒不存在！");
        }
        stockOutFrozenBox.setMemo(stockOutFrozenBoxDTO.getMemo());
        stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        return stockOutFrozenBoxDTO;
    }

    /**
     * 打印取盒单
     * @param taskId
     * @return
     */
    @Override
    public ByteArrayOutputStream printStockOutFrozenBox(Long taskId) {
        List<StockOutTakeBoxReportDTO> takeBoxDTOs =   createStockOutTakeBoxReportDTO(taskId);
        ByteArrayOutputStream outputStream = reportExportingService.makeStockOutTakeBoxReport(takeBoxDTOs);
        return outputStream;
    }

    private List<StockOutTakeBoxReportDTO> createStockOutTakeBoxReportDTO(Long taskId) {
        List<StockOutTakeBoxReportDTO> stockOutTakeBoxReportDTOS = new ArrayList<StockOutTakeBoxReportDTO>();
        //从任务出库样本中找到要出库的冻存盒
        List<FrozenBox> boxes =  frozenBoxRepository.findByStockOutTaskId(taskId);
        for(FrozenBox frozenBox :boxes){
            StockOutTakeBoxReportDTO stockOutTakeBoxReportDTO = new StockOutTakeBoxReportDTO();
            stockOutTakeBoxReportDTO.setId(frozenBox.getId());
            stockOutTakeBoxReportDTO.setAreaCode(frozenBox.getAreaCode());
            stockOutTakeBoxReportDTO.setEquipmentCode(frozenBox.getEquipmentCode());
            stockOutTakeBoxReportDTO.setShelfCode(frozenBox.getSupportRackCode());
            stockOutTakeBoxReportDTO.setBoxCode(frozenBox.getFrozenBoxCode());
            stockOutTakeBoxReportDTO.setShelfLocation(frozenBox.getColumnsInShelf()+frozenBox.getRowsInShelf());
            stockOutTakeBoxReportDTOS.add(stockOutTakeBoxReportDTO);
        }

        return stockOutTakeBoxReportDTOS;
    }

    /**
     * 查询待交接冻存盒
     * @param id 申请ID
     * @param input
     * @return
     */

    @Override
    public DataTablesOutput<StockOutFrozenBoxForDataTableEntity> getPageWaitingHandOverStockOutFrozenBoxes(Long id, DataTablesInput input) {
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        if(stockOutApply == null){
            throw new BankServiceException("未查询到申请单");
        }
        input.setLength(-1);
        input.getColumns().forEach(column -> {
            if(column.getData().equals("applyId") || column.getData().equals("planId") || column.getData().equals("taskId")){
                if(!column.getSearch().getValue().equals(null)&&!column.getSearch().getValue().equals("")){
                    column.setSearchValue(column.getSearch().getValue()+"+");
                }
            }
        });
        DataTablesOutput<StockOutFrozenBoxForDataTableEntity> output = stockOutHandoverFrozenBoxRepositries.findAll(input);
        List<StockOutFrozenBoxForDataTableEntity> stockOutFrozenBoxForDataTableEntities = new ArrayList<StockOutFrozenBoxForDataTableEntity>();
        output.getData().forEach(entity -> {
            StockOutFrozenBoxForDataTableEntity dataTableEntity = new StockOutFrozenBoxForDataTableEntity();
            StockOutBoxPosition stockOutBoxPosition = stockOutBoxPositionRepository.findOne(entity.getId());
            String position = BankUtil.toPositionString(stockOutBoxPosition);
            dataTableEntity.setPosition(position);
            dataTableEntity.setId(entity.getId());
            dataTableEntity.setPlanCode(entity.getPlanCode());
            dataTableEntity.setFrozenBoxCode(entity.getFrozenBoxCode());
            dataTableEntity.setPlanId(entity.getId());
            dataTableEntity.setApplyCode(entity.getApplyCode());
            dataTableEntity.setApplyId(entity.getApplyId());
            dataTableEntity.setTaskId(entity.getTaskId());
            dataTableEntity.setTaskCode(entity.getTaskCode());
            dataTableEntity.setCountOfSample(entity.getCountOfSample());
            dataTableEntity.setDelegateId(entity.getDelegateId());
            dataTableEntity.setDelegate(entity.getDelegate());
            dataTableEntity.setMemo(entity.getMemo());
            dataTableEntity.setSampleTypeName(entity.getSampleTypeName());
            dataTableEntity.setStatus(entity.getStatus());
            stockOutFrozenBoxForDataTableEntities.add(dataTableEntity);
        });
        output.setData(stockOutFrozenBoxForDataTableEntities);
        return output;
    }

    /**
     * 查询已交接冻存盒
     * @param id
     * @param pageRequest
     * @return
     */
    @Override
    public Page<StockOutFrozenBoxForDataTableEntity> getPageHandoverStockOutFrozenBoxes(Long id, Pageable pageRequest) {
        StockOutHandover stockOutHandover = stockOutHandoverRepository.findOne(id);
        if(stockOutHandover == null){
            throw new BankServiceException("未查询到交接单");
        }
        Page<StockOutFrozenBox> result = stockOutFrozenBoxRepository.findBoxesByHandOverAndStatus(id,Constants.STOCK_OUT_FROZEN_BOX_HANDOVER, pageRequest);
        return result.map(stockOutFrozenBox -> {
            StockOutFrozenBoxForDataTableEntity dto = new StockOutFrozenBoxForDataTableEntity();
            dto.setId(stockOutFrozenBox.getId());
            dto.setFrozenBoxCode(stockOutFrozenBox.getFrozenBox().getFrozenBoxCode());
            dto.setSampleTypeName(stockOutFrozenBox.getFrozenBox().getSampleTypeName());
            dto.setStatus(stockOutFrozenBox.getStatus());
            dto.setApplyId(stockOutHandover.getStockOutApply()!=null?stockOutHandover.getStockOutApply().getId():null);
            dto.setApplyCode(stockOutHandover.getStockOutApply()!=null?stockOutHandover.getStockOutApply().getApplyCode():null);
            dto.setPlanId(stockOutHandover.getStockOutPlan()!=null?stockOutHandover.getStockOutPlan().getId():null);
            dto.setPlanCode(stockOutHandover.getStockOutPlan()!=null?stockOutHandover.getStockOutPlan().getStockOutPlanCode():null);
            dto.setTaskId(stockOutHandover.getStockOutTask()!=null?stockOutHandover.getStockOutTask().getId():null);
            dto.setTaskCode(stockOutHandover.getStockOutTask()!=null?stockOutHandover.getStockOutTask().getStockOutTaskCode():null);
            dto.setStatus(stockOutFrozenBox.getStatus());
            dto.setDelegateId(stockOutHandover.getStockOutApply()!=null?stockOutHandover.getStockOutApply().getDelegate().getId():null);
            dto.setDelegate(stockOutHandover.getStockOutApply()!=null?stockOutHandover.getStockOutApply().getApplyPersonName():null);
            dto.setMemo(stockOutFrozenBox.getMemo());
            String position = BankUtil.getPositionString(stockOutFrozenBox.getEquipmentCode(),stockOutFrozenBox.getAreaCode(),
                stockOutFrozenBox.getSupportRackCode(),stockOutFrozenBox.getColumnsInShelf(),stockOutFrozenBox.getRowsInShelf(),null,null);
            dto.setPosition(position);
//            Long count = stockOutFrozenTubeRepository.countByFrozenBox(stockOutFrozenBox.getId());
//            dto.setCountOfSample(count);

            return dto;
        });
    }

    /**
     * 分页查询根据需求查询需要出库的冻存盒
     * @param ids 需求ID串
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<FrozenBoxForStockOutDataTableEntity> getPageByRequirementIds(List<Long> ids, DataTablesInput input) {
        Specification<FrozenBoxForStockOutDataTableEntity> specification = new Specification<FrozenBoxForStockOutDataTableEntity>() {
            @Override
            public Predicate toPredicate(Root<FrozenBoxForStockOutDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                CriteriaBuilder.In<Long> in  = cb.in(root.get("stockOutRequirementId"));
                for(Long id :ids){
                    in.value(id);
                }
                predicate.add(in);
                Predicate[] pre = new Predicate[predicate.size()];
                query.where(predicate.toArray(pre));
                return query.getRestriction();
            }
        };
        DataTablesOutput<FrozenBoxForStockOutDataTableEntity> output = stockOutFrozenBoxForWaitingRepositries.findAll(input,specification);
        List<FrozenBoxForStockOutDataTableEntity> alist = new ArrayList<FrozenBoxForStockOutDataTableEntity>();
        List<String> boxCodes = new ArrayList<>();
        output.getData().forEach(s->{
            if(!boxCodes.contains(s.getFrozenBoxCode())){
                boxCodes.add(s.getFrozenBoxCode());
                FrozenBoxForStockOutDataTableEntity dto = new FrozenBoxForStockOutDataTableEntity();
                dto.setId(s.getId());
                dto.setFrozenBoxCode(s.getFrozenBoxCode());
                dto.setSampleTypeName(s.getSampleTypeName());
                String position =  BankUtil.getPositionString(s.getEquipmentCode(),s.getAreaCode(),s.getSupportRackCode(),s.getColumnsInShelf(),s.getRowsInShelf(),null,null);
                dto.setPosition(position);
                dto.setCountOfSample(s.getCountOfSample());
                alist.add(dto);
            }else{
                long filterRecord = output.getRecordsFiltered()-1;
                output.setRecordsFiltered(filterRecord);
            }
        });
        output.setData(alist);
        return output;
    }

    /**
     * 分页查询某个计划的出库任务
     * @param id
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<StockOutFrozenBoxForTaskDetailDataTableEntity> getPageByTask(Long id, DataTablesInput input) {
        input.addColumn("stockOutTaskId", true, true, id+"+");
        DataTablesOutput<StockOutFrozenBoxForTaskDetailDataTableEntity> output = stockOutFrozenBoxInTaskRepositries.findAll(input);
        List<StockOutFrozenBoxForTaskDetailDataTableEntity> alist = new ArrayList<StockOutFrozenBoxForTaskDetailDataTableEntity>();
        output.getData().forEach(s->{
            StockOutFrozenBoxForTaskDetailDataTableEntity dto = new StockOutFrozenBoxForTaskDetailDataTableEntity();
            dto.setId(s.getId());
            dto.setFrozenBoxCode(s.getFrozenBoxCode());
            dto.setSampleTypeName(s.getSampleTypeName());
            String position =  BankUtil.getPositionString(s.getEquipmentCode(),s.getAreaCode(),s.getSupportRackCode(),s.getColumnsInShelf(),s.getRowsInShelf(),null,null);
            dto.setPosition(position);
            dto.setCountOfSample(s.getCountOfSample());
            alist.add(dto);
        });
        output.setData(alist);
        return output;
    }
}
