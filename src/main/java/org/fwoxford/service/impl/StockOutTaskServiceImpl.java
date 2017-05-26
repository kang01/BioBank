package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutTaskService;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.mapper.StockOutTaskMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final StockOutFrozenBoxRepository stockOutFrozenBoxRepository;
    private final StockOutBoxPositionRepository stockOutBoxPositionRepository;

    @Autowired
    private StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    @Autowired
    private StockOutFrozenTubeRepository stockOutFrozenTubeRepository;

    private final StockOutTaskMapper stockOutTaskMapper;

    public StockOutTaskServiceImpl(StockOutTaskRepository stockOutTaskRepository, StockOutPlanRepository stockOutPlanRepository, FrozenBoxRepository frozenBoxRepository, StockOutFrozenBoxRepository stockOutFrozenBoxRepository, StockOutBoxPositionRepository stockOutBoxPositionRepository, StockOutTaskMapper stockOutTaskMapper) {
        this.stockOutTaskRepository = stockOutTaskRepository;
        this.stockOutPlanRepository = stockOutPlanRepository;
        this.frozenBoxRepository = frozenBoxRepository;
        this.stockOutFrozenBoxRepository = stockOutFrozenBoxRepository;
        this.stockOutBoxPositionRepository = stockOutBoxPositionRepository;
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
        StockOutTask stockOutTask = stockOutTaskMapper.stockOutTaskDTOToStockOutTask(stockOutTaskDTO);
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
        StockOutTaskDTO stockOutTaskDTO = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
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
//            //查询出库盒位置
//            List<Long> posIds = stockOutFrozenBoxRepository.findAllBoxPositionByTask(stockOutTask.getId());
            //删除出库盒
            stockOutFrozenBoxRepository.deleteByStockOutTaskId(stockOutTask.getId());
            //删除出库盒位置
//            if(posIds.size()>0){
//                stockOutBoxPositionRepository.deleteByIds(posIds);
//            }
            //根据出库盒ID查询出库样本ID
            stockOutFrozenTubeRepository.deleteByTask(stockOutTask.getId());
        }

        stockOutTaskRepository.delete(id);
    }

    /**
     * Save a stockOutTask.
     *
     * @return the persisted entity
     */
    @Override
    public StockOutTaskDTO save(Long planId, List<Long> boxIds) {
        log.debug("Request to save StockOutTask : {} {}", planId, boxIds);

        StockOutPlan stockOutPlan = stockOutPlanRepository.findOne(planId);
        if(stockOutPlan == null){
            throw new BankServiceException("查询计划失败！");
        }
        StockOutTask stockOutTask = new StockOutTask();
        stockOutTask.status(Constants.STOCK_OUT_TASK_NEW)
            .stockOutTaskCode(BankUtil.getUniqueID())
            .stockOutPlan(stockOutPlan).usedTime(0)
            .stockOutDate(LocalDate.now());

        stockOutTask = stockOutTaskRepository.save(stockOutTask);

//        List<StockOutFrozenBox> boxes = new ArrayList<>();
        for(Long id : boxIds){
            FrozenBox box = frozenBoxRepository.findOne(id);
            if (box == null){
                continue;
            }

            StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
            stockOutFrozenBox.status(Constants.STOCK_OUT_FROZEN_BOX_NEW)
                .stockOutTask(stockOutTask)
                .frozenBox(box);
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
//            boxes.add(stockOutFrozenBox);
            List<StockOutPlanFrozenTube> stockOutPlanFrozenTubes = stockOutPlanFrozenTubeRepository.findByStockOutPlanId(id);
            //保存出库样本
            List<StockOutFrozenTube> tubes = new ArrayList<StockOutFrozenTube>();
            for(StockOutPlanFrozenTube t: stockOutPlanFrozenTubes){
                StockOutFrozenTube stockOutFrozenTube = new StockOutFrozenTube();
                stockOutFrozenTube.status(Constants.STOCK_OUT_FROZEN_TUBE_NEW)
                    .stockOutFrozenBox(stockOutFrozenBox)
                    .frozenTube(t.getStockOutReqFrozenTube().getFrozenTube())
                    .tubeColumns(t.getStockOutReqFrozenTube().getFrozenTube().getTubeColumns())
                    .tubeRows(t.getStockOutReqFrozenTube().getFrozenTube().getTubeRows());
                tubes.add(stockOutFrozenTube);
            }

            stockOutFrozenTubeRepository.save(tubes);
        }

//        if (boxes.size() > 0){
//            stockOutFrozenBoxRepository.save(boxes);
//        }

        StockOutTaskDTO result = stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
        return result;
    }

}
