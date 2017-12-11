package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutTaskFrozenTubeService;
import org.fwoxford.service.dto.StockOutTaskFrozenTubeDTO;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.mapper.StockOutTaskFrozenTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing StockOutTaskFrozenTube.
 */
@Service
@Transactional
public class StockOutTaskFrozenTubeServiceImpl implements StockOutTaskFrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutTaskFrozenTubeServiceImpl.class);

    private final StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    private final StockOutTaskFrozenTubeMapper stockOutTaskFrozenTubeMapper;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    private StockOutTaskRepository stockOutTaskRepository;

    @Autowired
    private StockOutPlanRepository stockOutPlanRepository;

    @Autowired
    private StockOutApplyRepository stockOutApplyRepository;

    public StockOutTaskFrozenTubeServiceImpl(StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository, StockOutTaskFrozenTubeMapper stockOutTaskFrozenTubeMapper) {
        this.stockOutTaskFrozenTubeRepository = stockOutTaskFrozenTubeRepository;
        this.stockOutTaskFrozenTubeMapper = stockOutTaskFrozenTubeMapper;
    }

    /**
     * Save a stockOutTaskFrozenTube.
     *
     * @param stockOutTaskFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutTaskFrozenTubeDTO save(StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO) {
        log.debug("Request to save StockOutTaskFrozenTube : {}", stockOutTaskFrozenTubeDTO);
        StockOutTaskFrozenTube stockOutTaskFrozenTube = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeDTOToStockOutTaskFrozenTube(stockOutTaskFrozenTubeDTO);
        stockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.save(stockOutTaskFrozenTube);
        StockOutTaskFrozenTubeDTO result = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube);
        return result;
    }

    /**
     *  Get all the stockOutTaskFrozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutTaskFrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutTaskFrozenTubes");
        Page<StockOutTaskFrozenTube> result = stockOutTaskFrozenTubeRepository.findAll(pageable);
        return result.map(stockOutTaskFrozenTube -> stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube));
    }

    /**
     *  Get one stockOutTaskFrozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutTaskFrozenTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutTaskFrozenTube : {}", id);
        StockOutTaskFrozenTube stockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.findOne(id);
        StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube);
        return stockOutTaskFrozenTubeDTO;
    }

    /**
     *  Delete the  stockOutTaskFrozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutTaskFrozenTube : {}", id);
        stockOutTaskFrozenTubeRepository.delete(id);
    }

    @Override
    public List<FrozenTubeResponse> abnormalStockOutTaskFrozenTube(List<FrozenTubeResponse> frozenTubeDTOS) {
        List<FrozenTube> frozenTubeList = new ArrayList<>();
        for(FrozenTubeResponse tube :frozenTubeDTOS){
            if(tube.getId() == null){
                continue;
            }
            FrozenTube frozenTube = frozenTubeRepository.findOne(tube.getId());
            if(frozenTube == null){
                continue;
            }
            frozenTube.setStatus(Constants.FROZEN_TUBE_ABNORMAL);
            frozenTube.setMemo(tube.getMemo());
            frozenTubeList.add(frozenTube);
        }
        frozenTubeRepository.save(frozenTubeList);
        return frozenTubeDTOS;
    }

    @Override
    public List<FrozenTubeResponse> repealStockOutTaskFrozenTube(List<FrozenTubeResponse> frozenTubeDTOS, Long taskId) {
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(taskId);
        if(stockOutTask == null ){
            throw new BankServiceException("出库任务查询失败！");
        }
        List<Long> frozenTubeIds = new ArrayList<>();
        for(FrozenTubeResponse tube :frozenTubeDTOS){
            frozenTubeIds.add(tube.getId());
        }
        //需求样本撤销
        List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutTaskIdAndFrozenTubeIdInAndStatusNot(taskId,frozenTubeIds,Constants.STOCK_OUT_SAMPLE_IN_USE_NOT);
        for(StockOutReqFrozenTube stockOutReqFrozenTube: stockOutReqFrozenTubes){
            for(FrozenTubeResponse tube :frozenTubeDTOS) {
                if(tube.getId().equals(stockOutReqFrozenTube.getFrozenTube().getId())){
                    stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE_NOT);
                    stockOutReqFrozenTube.setRepealReason(tube.getRepealReason());
                }
            }
        }
        stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
        Long countOfStockOutFrozenTubeForTask = stockOutReqFrozenTubeRepository.countByStockOutTaskIdAndStatusNotIn(stockOutTask.getId(),new ArrayList<String>(){{add(Constants.STOCK_OUT_SAMPLE_IN_USE_NOT);}});
        stockOutTask.countOfStockOutSample(countOfStockOutFrozenTubeForTask.intValue());
        //如果任务出库样本量为0 ，任务状态为已撤销
        //如果任务出库样本量等于已交接样本量，则状态为已完成
        if(countOfStockOutFrozenTubeForTask.intValue()==0){
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_REPEAL);
        }

        //任务未出库样本量
        List<String> statusList = new ArrayList<>();
        statusList.add(Constants.STOCK_OUT_SAMPLE_IN_USE_NOT);
        statusList.add(Constants.STOCK_OUT_SAMPLE_COMPLETED);
        Long countOfTaskTube = stockOutReqFrozenTubeRepository.countByStockOutTaskIdAndStatusNotIn(taskId,statusList);
        //如果任务内的样本量都出库了，任务状态为已完成，如果出库样本中有异常出库的样本为异常出库
        if(countOfTaskTube.intValue() == 0){
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_COMPLETED);
        }
        stockOutTaskRepository.save(stockOutTask);
        StockOutPlan stockOutPlan = stockOutTask.getStockOutPlan();

        StockOutApply stockOutApply = stockOutPlan.getStockOutApply();
        List<String> statusList_ = new ArrayList<>();
        statusList_.add(Constants.STOCK_OUT_SAMPLE_IN_USE);
        statusList_.add(Constants.STOCK_OUT_SAMPLE_WAITING_OUT);
        statusList_.add(Constants.STOCK_OUT_SAMPLE_COMPLETED);
        Long countOfStockOutFrozenTubeForApply = stockOutReqFrozenTubeRepository.countUnCompleteSampleByStockOutApplyAndStatusIn(stockOutApply.getId(),statusList_);
        stockOutApply.countOfStockSample(countOfStockOutFrozenTubeForApply.intValue());
        stockOutApplyRepository.save(stockOutApply);

        Long countOfUnStockOutFrozenTubeForApply = stockOutReqFrozenTubeRepository.countUnCompleteSampleByStockOutApplyAndStatusIn(stockOutApply.getId(),new ArrayList<String>(){{add(Constants.STOCK_OUT_SAMPLE_WAITING_OUT);add(Constants.STOCK_OUT_SAMPLE_IN_USE);}});
        stockOutPlan.countOfStockOutPlanSample(countOfStockOutFrozenTubeForApply.intValue());
        //如果计划出库样本量为0 ，任务状态为已撤销
        //如果未出库样本量为0，则状态为已完成
        if(countOfStockOutFrozenTubeForApply.intValue() == 0){
            stockOutPlan.setStatus(Constants.STOCK_OUT_PLAN_REPEAL);
        }
        if(countOfUnStockOutFrozenTubeForApply.intValue()==0){
            stockOutPlan.setStatus(Constants.STOCK_OUT_PLAN_COMPLETED);
        }

        stockOutPlanRepository.save(stockOutPlan);

        return frozenTubeDTOS;
    }

    @Override
    public List<FrozenTubeResponse> noteStockOutTaskFrozenTube( List<FrozenTubeResponse> frozenTubeDTOS) {

        List<FrozenTube> frozenTubeList = new ArrayList<>();
        for(FrozenTubeResponse tube :frozenTubeDTOS){
            if(tube.getId() == null){
                continue;
            }
            FrozenTube frozenTube = frozenTubeRepository.findOne(tube.getId());
            if(frozenTube == null){
                continue;
            }
            frozenTube.setMemo(tube.getMemo());
            frozenTubeList.add(frozenTube);
        }
        frozenTubeRepository.save(frozenTubeList);
        return frozenTubeDTOS;
    }
}
