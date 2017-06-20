package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.service.UserService;
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
import java.util.List;

/**
 * Service Implementation for managing StockOutFrozenBox.
 */
@Service
@Transactional
public class StockOutFrozenBoxServiceImpl implements StockOutFrozenBoxService{

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenBoxServiceImpl.class);

    private final StockOutFrozenBoxRepository stockOutFrozenBoxRepository;
    private final StockOutFrozenTubeRepository stockOutFrozenTubeRepository;

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

    public StockOutFrozenBoxServiceImpl(StockOutFrozenBoxRepository stockOutFrozenBoxRepository
            , StockOutFrozenBoxMapper stockOutFrozenBoxMapper
            , StockOutFrozenTubeRepository stockOutFrozenTubeRepository) {
        this.stockOutFrozenBoxRepository = stockOutFrozenBoxRepository;
        this.stockOutFrozenBoxMapper = stockOutFrozenBoxMapper;
        this.stockOutFrozenTubeRepository = stockOutFrozenTubeRepository;
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
        for(FrozenBox frozenBox :boxes){
            StockOutFrozenBoxForTaskDataTableEntity box = new StockOutFrozenBoxForTaskDataTableEntity();
            if(frozenBox ==null){continue;}
            Long count = stockOutTaskFrozenTubeRepository.countByFrozenBoxAndTask(frozenBox.getId(),taskId);
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

            List<Object[]> obj = frozenBoxRepository.countByFrozenBoxCode(box.getFrozenBoxCode());
            for(Object[] o:obj){
                String frozenBoxId = o[0].toString();
                if(box.getId()==null){
                    throw new BankServiceException("冻存盒编码已存在！",box.getFrozenBoxCode());
                }else if(box.getId()!=null&&!box.getId().toString().equals(frozenBoxId)){
                    throw new BankServiceException("冻存盒编码已存在！",box.getFrozenBoxCode());
                }
            }
            FrozenBox frozenBox = new FrozenBox();
            if(box.getId()!=null){
                frozenBox = frozenBoxRepository.findOne(box.getId())!=null?frozenBoxRepository.findOne(box.getId()):new FrozenBox();
            }
            FrozenBoxType boxType = new FrozenBoxType();
            if (box.getFrozenBoxType() != null) {
                int boxTypeIndex = boxTypes.indexOf(box.getFrozenBoxType());
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
            frozenBox.setSampleNumber(box.getFrozenTubeDTOS().size());
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
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);

            //保存出库盒与管之间的关系
            for(FrozenTubeResponse f: box.getFrozenTubeDTOS()){
                StockOutTaskFrozenTube stockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.findByStockOutTaskAndFrozenTube(taskId,f.getId());
                if(stockOutTaskFrozenTube == null){
                    continue;
                }

                FrozenTube frozenTube = stockOutTaskFrozenTube.getStockOutPlanFrozenTube().getStockOutReqFrozenTube().getFrozenTube();
                frozenTube.setFrozenBox(frozenBox);
                frozenTube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                frozenTube.setTubeColumns(f.getTubeColumns());
                frozenTube.setTubeRows(f.getTubeRows());
                frozenTubeRepository.saveAndFlush(frozenTube);
                StockOutBoxTube  stockOutBoxTube = stockOutBoxTubeRepository.findByStockOutTaskFrozenTubeId(stockOutTaskFrozenTube.getId());
                if(stockOutBoxTube == null){
                    stockOutBoxTube = new StockOutBoxTube();
                }
                stockOutBoxTube.setStatus(Constants.FROZEN_BOX_TUBE_STOCKOUT_PENDING);
                stockOutBoxTube.setStockOutFrozenBox(stockOutFrozenBox);
                stockOutBoxTube.setStockOutTaskFrozenTube(stockOutTaskFrozenTube);
                FrozenTube tube = stockOutTaskFrozenTube.getStockOutPlanFrozenTube().getStockOutReqFrozenTube().getFrozenTube();
                stockOutBoxTube.setFrozenTube(tube);
                stockOutBoxTube.setTubeRows(f.getTubeRows());
                stockOutBoxTube.setTubeColumns(f.getTubeColumns());
                stockOutBoxTubeRepository.save(stockOutBoxTube);
            }

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
            List<FrozenTubeResponse> frozenTubeResponse = frozenTubeMapper.frozenTubeToFrozenTubeResponse(frozenTubes);
            FrozenBoxAndFrozenTubeResponse box = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox,frozenTubeResponse);
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
//            String position = getPositionString(frozenBox);
            String position = BankUtil.toPositionString(f.getStockOutBoxPosition());
            box.setPosition(position);
            Long count = stockOutBoxTubeRepository.countByStockOutFrozenBoxId(f.getId());
            box.setCountOfSample(count);
            box.setMemo(f.getMemo());
            box.setStatus(f.getStatus());
            StockOutHandover stockOutHandover = stockOutHandoverRepository.findByStockOutTaskIdAndstockOutBoxId(taskId,f.getId());
            box.setStockOutHandoverTime(stockOutHandover!=null?stockOutHandover.getHandoverTime():null);
            alist.add(box);
        }
        return alist;
    }

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

        for(Long id:frozenBoxIds){
            //保存冻存盒位置
            StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition();
            StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(id);
            if(stockOutFrozenBox == null){
                continue;
            }
            FrozenBox frozenBox = stockOutFrozenBox.getFrozenBox();
            stockOutBoxPosition.frozenBox(frozenBox);
            stockOutBoxPosition.setEquipment(equipment);
            stockOutBoxPosition.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);
            stockOutBoxPosition.setArea(area);
            stockOutBoxPosition.setAreaCode(area!=null?area.getAreaCode():null);
            stockOutBoxPosition.setStatus(Constants.VALID);
            stockOutBoxPositionRepository.save(stockOutBoxPosition);
            //保存出库冻存盒
            stockOutFrozenBox.setStockOutBoxPosition(stockOutBoxPosition);
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_COMPLETED);
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);

            //保存冻存盒的位置和状态
            frozenBox.setEquipment(equipment);
            frozenBox.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);
            frozenBox.setArea(area);
            frozenBox.setAreaCode(area!=null?area.getAreaCode():null);
            frozenBox.setStatus(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED);
            frozenBoxRepository.save(frozenBox);
            //保存出库盒与冻存管的关系
            stockOutBoxTubeRepository.updateByStockOutFrozenBox(id); //样本使用次数加1
            List<StockOutBoxTube> stockOutBoxTubes = stockOutBoxTubeRepository.findByStockOutFrozenBoxId(id);
            for(StockOutBoxTube s:stockOutBoxTubes){
                FrozenTube frozenTube = s.getFrozenTube();
                frozenTube.setSampleUsedTimes(frozenTube.getSampleUsedTimes()+1);
                frozenTubeRepository.save(frozenTube);
            }
            List<Long> planTubes = stockOutBoxTubeRepository.findPlanFrozenTubeByStockOutFrozenBoxId(id);
            List<Long> taskTubes = stockOutBoxTubeRepository.findTaskFrozenTubeByStockOutFrozenBoxId(id);
            stockOutTaskFrozenTubeRepository.updateByStockOutFrozenTubeIds(taskTubes);
            stockOutPlanFrozenTubeRepository.updateByStockOutFrozenTubeIds(planTubes);
        }

        Long planId = stockOutTask.getStockOutPlan().getId();
        List<String> taskStatus = new ArrayList<>();
        taskStatus.add(Constants.STOCK_OUT_FROZEN_TUBE_COMPLETED);
        taskStatus.add(Constants.STOCK_OUT_FROZEN_TUBE_CANCEL);
        //任务未出库样本量
        Long countOfTaskTube = stockOutTaskFrozenTubeRepository.countByStockOutTaskIdAndStatusNotIn(taskId,taskStatus);
        //计划未出库样本量
        List<String> planStatus = new ArrayList<>();
        planStatus.add(Constants.STOCK_OUT_PLAN_TUBE_COMPLETED);
        taskStatus.add(Constants.STOCK_OUT_PLAN_TUBE_CANCEL);
        Long countOfPlanTube = stockOutPlanFrozenTubeRepository.countByStockOutPlanIdAndStatusNotIn(planId,planStatus);
        //异常出库样本量
        Long abNormalTube = stockOutBoxTubeRepository.countAbnormalTubeByStockOutTaskId(taskId);
        if(abNormalTube.intValue()==0){
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_ABNORMAL);
        }else if(countOfTaskTube.intValue()==0){
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_COMPLETED);
        }
        stockOutTaskRepository.save(stockOutTask);
        if(countOfPlanTube.intValue()==0){
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
            String position = BankUtil.toPositionString(stockOutFrozenBox.getStockOutBoxPosition());
            dto.setPosition(position);
            Long count = stockOutFrozenTubeRepository.countByFrozenBox(stockOutFrozenBox.getId());
            dto.setCountOfSample(count);

            return dto;
        });
    }

    /**
     * 分页查询根据需求查询x需要出库的冻存盒
     * @param ids
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
                Long countOfSample = stockOutPlanFrozenTubeRepository.countByFrozenBoxIdAndRequirement(ids,s.getId());
                FrozenBoxForStockOutDataTableEntity dto = new FrozenBoxForStockOutDataTableEntity();
                dto.setId(s.getId());
                dto.setFrozenBoxCode(s.getFrozenBoxCode());
                dto.setSampleTypeName(s.getSampleTypeName());
                FrozenBox frozenBox = frozenBoxRepository.findOne(s.getId());
                String position =  BankUtil.getPositionString(frozenBox);
                dto.setPosition(position);
                dto.setCountOfSample(countOfSample);
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
            FrozenBox frozenBox = frozenBoxRepository.findOne(s.getId());
            String position =  BankUtil.getPositionString(frozenBox);
            dto.setPosition(position);
            Long countOfSample = stockOutTaskFrozenTubeRepository.countSampleByFrozenBoxAndTask(s.getId(),id);
            dto.setCountOfSample(countOfSample);
            alist.add(dto);
        });
        output.setData(alist);

        return output;
    }
}
