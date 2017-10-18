package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.config.SecurityConfiguration;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.security.SecurityUtils;
import org.fwoxford.service.StockOutTaskService;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.dto.response.StockOutTaskForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutTaskForPlanDataTableEntity;
import org.fwoxford.service.mapper.StockOutTaskMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service Implementation for managing StockOutTask.
 */
@Service
@Transactional
public class StockOutTaskServiceImpl implements StockOutTaskService{

    private final Logger log = LoggerFactory.getLogger(StockOutTaskServiceImpl.class);

    private final StockOutTaskRepository stockOutTaskRepository;
    private final StockOutPlanRepository stockOutPlanRepository;
    private final FrozenBoxRepository frozenBoxRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockOutHandoverDetailsRepository stockOutHandoverDetailsRepository;

    @Autowired
    private StockOutHandoverRepository stockOutHandoverRepository;

    @Autowired
    private StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    @Autowired
    private TaskUserHistoryRepository taskUserHistoryRepository;

    @Autowired
    private StockOutTaskRepositories stockOutTaskRepositories;

    @Autowired
    private StockOutTaskByPlanRepositories stockOutTaskByPlanRepositories;

    @Autowired
    private BankUtil bankUtil;

    private final StockOutTaskMapper stockOutTaskMapper;

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    private StockOutRequirementRepository stockOutRequirementRepository;

    public StockOutTaskServiceImpl(StockOutTaskRepository stockOutTaskRepository, StockOutPlanRepository stockOutPlanRepository, FrozenBoxRepository frozenBoxRepository, StockOutTaskMapper stockOutTaskMapper) {
        this.stockOutTaskRepository = stockOutTaskRepository;
        this.stockOutPlanRepository = stockOutPlanRepository;
        this.frozenBoxRepository = frozenBoxRepository;
        this.stockOutTaskMapper = stockOutTaskMapper;
    }

    /**
     * Save a stockOutTask.
     *
     * @param stockOutTaskDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutTaskDTO save(StockOutTaskDTO stockOutTaskDTO) {
        log.debug("Request to save StockOutTask : {}", stockOutTaskDTO);
        StockOutTask stockOutTask = new StockOutTask();
        if(stockOutTaskDTO.getId()!=null){

            if(stockOutTaskDTO.getStockOutHeadId1()!=null&&stockOutTaskDTO.getStockOutHeadId2()!=null
                &&stockOutTaskDTO.getStockOutHeadId1().equals(stockOutTaskDTO.getStockOutHeadId2())){
                throw new BankServiceException("出库负责人不能为同一人！");
            }
            stockOutTask = stockOutTaskRepository.findOne(stockOutTaskDTO.getId());
            stockOutTask.stockOutDate(stockOutTaskDTO.getStockOutDate())
                .stockOutHeadId1(stockOutTaskDTO.getStockOutHeadId1())
                .stockOutHeadId2(stockOutTaskDTO.getStockOutHeadId2())
                .memo(stockOutTaskDTO.getMemo());
        }else{
           stockOutTask = stockOutTaskMapper.stockOutTaskDTOToStockOutTask(stockOutTaskDTO);
        }

        stockOutTask = stockOutTaskRepository.save(stockOutTask);
        StockOutTaskDTO result = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
        return result;
    }

    /**
     *  Get all the stockOutTasks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutTasks");
        Page<StockOutTask> result = stockOutTaskRepository.findAll(pageable);
        return result.map(stockOutTask -> stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask));
    }

    /**
     *  Get one stockOutTask by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutTaskDTO findOne(Long id) {
        log.debug("Request to get StockOutTask : {}", id);
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(id);
        if(stockOutTask==null){
            throw new BankServiceException("任务查询失败！");
        }
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
        stockOutTaskDTO.setStockOutPlanCode(stockOutTask.getStockOutPlan()!=null?stockOutTask.getStockOutPlan().getStockOutPlanCode():null);
        if(stockOutTask.getStockOutHeadId1() != null){
            User u = userRepository.findOne(stockOutTask.getStockOutHeadId1());
            String name = (u.getLastName()!=null?u.getLastName():"")+(u.getFirstName()!=null?u.getFirstName():"");
            stockOutTaskDTO.setStockOutHeader1(name);
        }
        if(stockOutTask.getStockOutHeadId2() != null){
            User u = userRepository.findOne(stockOutTask.getStockOutHeadId2());
            String name = (u.getLastName()!=null?u.getLastName():"")+(u.getFirstName()!=null?u.getFirstName():"");
            stockOutTaskDTO.setStockOutHeader2(name);
        }
        stockOutTaskDTO.setStockOutApplyId(stockOutTask.getStockOutPlan().getStockOutApply().getId());
        stockOutTaskDTO.setStockOutApplyCode(stockOutTask.getStockOutPlan().getStockOutApply().getApplyCode());
        return stockOutTaskDTO;
    }

    /**
     *  Delete the  stockOutTask by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutTask : {}", id);
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(id);
        if (stockOutTask != null){
            if(!stockOutTask.getStatus().equals(Constants.STOCK_OUT_TASK_NEW)){
                throw new BankServiceException("任务已经开始，不能删除");
            }
            //根据出库盒ID查询出库样本ID
//            List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutTaskId(id);
//            List<StockOutReqFrozenTube> stockOutReqFrozenTubesLast = new ArrayList<>();
//            for(StockOutReqFrozenTube s:stockOutReqFrozenTubes){
//                s.setStockOutTask(null);
//                stockOutReqFrozenTubesLast.add(s);
//                if(stockOutReqFrozenTubesLast.size()>=1000){
//                    stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubesLast);
//                    stockOutReqFrozenTubesLast = new ArrayList<StockOutReqFrozenTube>();
//                }
//            }
//            if(stockOutReqFrozenTubesLast.size()>0){
//                stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubesLast);
//            }
//            stockOutTaskRepository.delete(id);
            //出库需求样本中的任务ID 置空
            StringBuffer sql = new StringBuffer();
            sql.append("update stock_out_req_frozen_tube t set t.stock_out_task_id = null where t.stock_out_task_id = ?1 ");
            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter("1", id);
            query.executeUpdate();
            //删除任务
            StringBuffer sqlDel = new StringBuffer();
            sqlDel.append("delete from stock_out_task t where t.id = ?1 ");
            Query del = entityManager.createNativeQuery(sqlDel.toString());
            del.setParameter("1", id);
            del.executeUpdate();
        }
    }

    /**
     * Save a stockOutTask.
     *
     * @return the persisted entity
     */
    @Override
//    public StockOutTaskDTO save(Long planId, List<Long> boxIds) {
//        log.debug("Request to save StockOutTask : {} {}", planId, boxIds);
//
//        StockOutPlan stockOutPlan = stockOutPlanRepository.findOne(planId);
//        if(stockOutPlan == null){
//            throw new BankServiceException("查询计划失败！");
//        }
//        StockOutTask stockOutTask = new StockOutTask();
//        stockOutTask.status(Constants.STOCK_OUT_TASK_NEW)
//            .stockOutTaskCode(bankUtil.getUniqueID("F"))
//            .stockOutPlan(stockOutPlan).usedTime(0)
//            .stockOutDate(LocalDate.now());
//
//        stockOutTask = stockOutTaskRepository.save(stockOutTask);
//        List<StockOutTaskFrozenTube> tubes = new ArrayList<StockOutTaskFrozenTube>();
//        for(Long id : boxIds){
//            FrozenBox box = frozenBoxRepository.findOne(id);
//            if (box == null){
//                continue;
//            }
//            List<StockOutPlanFrozenTube> stockOutPlanFrozenTubes = stockOutPlanFrozenTubeRepository.findByStockOutPlanIdAndFrozenBoxId(planId,id);
//            //保存出库任务样本
//
//            for(StockOutPlanFrozenTube t: stockOutPlanFrozenTubes){
//                StockOutTaskFrozenTube tube = new StockOutTaskFrozenTube();
//                tube.status(Constants.STOCK_OUT_FROZEN_TUBE_NEW).stockOutTask(stockOutTask).stockOutPlanFrozenTube(t);
//                tubes.add(tube);
//            }
//            if(tubes.size()>=1000){
//                stockOutTaskFrozenTubeRepository.save(tubes);
//                tubes = new ArrayList<StockOutTaskFrozenTube>();
//            }
//        }
//        if(tubes.size()>=0){
//            stockOutTaskFrozenTubeRepository.save(tubes);
//            tubes = new ArrayList<StockOutTaskFrozenTube>();
//        }
//        StockOutTaskDTO result = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
//        return result;
//    }
    public StockOutTaskDTO save(Long planId, List<Long> boxIds) {
        log.debug("Request to save StockOutTask : {} {}", planId, boxIds);

        StockOutPlan stockOutPlan = stockOutPlanRepository.findOne(planId);
        if(stockOutPlan == null){
            throw new BankServiceException("查询计划失败！");
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.status(Constants.STOCK_OUT_TASK_NEW)
            .stockOutTaskCode(bankUtil.getUniqueID("F"))
            .stockOutPlan(stockOutPlan).usedTime(0)
            .stockOutDate(LocalDate.now());

        stockOutTask = stockOutTaskRepository.save(stockOutTask);
        List<StockOutTaskFrozenTube> tubes = new ArrayList<StockOutTaskFrozenTube>();
        //查询出计划下的所有需求
        List<Long> stockOutRequirementIds =stockOutRequirementRepository.findRequirementByStockOutApplyId(stockOutPlan.getStockOutApply().getId());
        StringBuffer sql = new StringBuffer();
        sql.append("update stock_out_req_frozen_tube t set t.stock_out_task_id = ?1 where t.stock_out_requirement_id in ?2 and t.frozen_box_id in ?3");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("1", stockOutTask.getId());
        query.setParameter("2", stockOutRequirementIds);
        query.setParameter("3", boxIds);
        query.executeUpdate();
//
//        List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findAllByStockOutRequirementIdInAndFrozenBoxIdIn(stockOutRequirementIds,boxIds);
//        List<StockOutReqFrozenTube> stockOutReqFrozenTubesLast = new ArrayList<>();
//        for(StockOutReqFrozenTube s:stockOutReqFrozenTubes){
//            s.setStockOutTask(stockOutTask);
//            stockOutReqFrozenTubesLast.add(s);
//            if(stockOutReqFrozenTubesLast.size()>=1000){
//                stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubesLast);
//                stockOutReqFrozenTubesLast = new ArrayList<StockOutReqFrozenTube>();
//            }
//        }
//        if(stockOutReqFrozenTubesLast.size()>0){
//            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubesLast);
//        }
        StockOutTaskDTO result = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
        return result;
    }



    @Override
    public List<StockOutTaskDTO> getAllStockOutTasksByPlanId(Long id) {
        StockOutPlan stockOutPlan = stockOutPlanRepository.findOne(id);
        if(stockOutPlan == null){
            throw new BankServiceException("计划不存在！");
        }
        List<StockOutTask> stockOutTasks = stockOutTaskRepository.findByStockOutPlanId(id);
        return stockOutTaskMapper.stockOutTasksToStockOutTaskDTOs(stockOutTasks);
    }

    @Override
    public synchronized StockOutTaskDTO startStockOutTask(Long id) {
        if(id==null){
            throw new BankServiceException("任务ID不能为空！");
        }
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(id);
        if(stockOutTask.getStatus().equals(Constants.STOCK_OUT_TASK_COMPLETED)
            ||stockOutTask.getStatus().equals(Constants.STOCK_OUT_TASK_ABNORMAL)
            ||stockOutTask.getStatus().equals(Constants.STOCK_OUT_TASK_INVALID)){
            return new StockOutTaskDTO();
        }
        String login = SecurityUtils.getCurrentUserLogin();
        User user = userRepository.findByLogin(login);
        //获取访问历史数据
        TaskUserHistory taskUserHistory = taskUserHistoryRepository.findByBusinessId(id);
        ZonedDateTime time = ZonedDateTime.now();
        if(taskUserHistory==null){//访问历史为空---首次开始任务
            taskUserHistory = new TaskUserHistory();
            stockOutTask.setTaskStartTime(time);
            stockOutTask.setUsedTime(0);
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_PENDING);

            //记录访问历史
            taskUserHistory.setStatus(Constants.VALID);
            taskUserHistory.setBusinessName("TASK BEGIN");
            ZonedDateTime invalidDate = time.plusMinutes(20);
            taskUserHistory.setInvalidDate(invalidDate);
            taskUserHistory.setLoginUserId(user.getId());
        }else{
            //验证是否为同一个用户
            if(taskUserHistory.getLoginUserId().equals(user.getId()) || taskUserHistory.getLoginUserId() == user.getId() ){
                //同一个用户--失效时间改为当前时间+20
                ZonedDateTime invalidDate = time.plusMinutes(20);
                taskUserHistory.setInvalidDate(invalidDate);

            }else{
                //验证是否失效
                if(taskUserHistory.getInvalidDate().isBefore(time)){
                    return stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
                }else{
                    taskUserHistory.setLoginUserId(user.getId());
                    ZonedDateTime invalidDate = time.plusMinutes(20);
                    taskUserHistory.setInvalidDate(invalidDate);
                }
            }
            ZonedDateTime lastTime = stockOutTask.getTaskEndTime();
            if(lastTime == null){
                lastTime = stockOutTask.getTaskStartTime();
            }
            Date date = Date.from(time.toInstant());
            Long nowTime = date.getTime();
            Long lastDate = Date.from(lastTime.toInstant()).getTime();
            Long diffTime = nowTime-lastDate;
//            BigDecimal diffTime = bigDecimal.divide(BigDecimal.valueOf(60 * 1000),2, BigDecimal.ROUND_HALF_UP);
//            Integer diffTime = (nowTime-lastDate)/(60 * 1000);
            if(diffTime<=60000){
                return stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
            }else if(diffTime<=300000){
                stockOutTask.setUsedTime(stockOutTask.getUsedTime()+(int)(diffTime/60000));
            }
        }
        stockOutTask.setTaskEndTime(time);
        taskUserHistory.setBusinessId(id);
        stockOutTaskRepository.save(stockOutTask);
        taskUserHistoryRepository.save(taskUserHistory);
        return stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
    }

    @Override
    public DataTablesOutput<StockOutTaskForPlanDataTableEntity> getPageStockOutTaskByPlan(Long id, DataTablesInput input) {
        input.addColumn("stockOutPlanId",true,true,id+"+");
        DataTablesOutput<StockOutTaskForPlanDataTableEntity> output =stockOutTaskByPlanRepositories.findAll(input);
//        List<StockOutTaskForPlanDataTableEntity> alist = new ArrayList<StockOutTaskForPlanDataTableEntity>();
//        output.getData().forEach(o->{
//            StockOutTaskForPlanDataTableEntity rowData = new StockOutTaskForPlanDataTableEntity();
//            rowData.setId(o.getId());
//            rowData.setStatus(o.getStatus());
//            rowData.setStockOutTaskCode(o.getStockOutTaskCode());
//            rowData.setMemo(o.getMemo());
//            rowData.setCreateDate(o.getCreateDate());
//            rowData.setStockOutDate(o.getStockOutDate());
//            Long countOfBox = stockOutTaskFrozenTubeRepository.countFrozenBoxByStockOutTaskId(o.getId());
//            rowData.setCountOfSample(o.getCountOfSample());
//            rowData.setCountOfFrozenBox(countOfBox);
//            alist.add(rowData);
//        });
//        output.setData(alist);
        return output;
    }

    @Override
    public DataTablesOutput<StockOutTaskForDataTableEntity> getPageStockOutTask(DataTablesInput input) {
        DataTablesOutput<StockOutTaskForDataTableEntity> output =stockOutTaskRepositories.findAll(input);
        return output;
    }
}
