package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutPlanService;
import org.fwoxford.service.dto.StockOutPlanDTO;
import org.fwoxford.service.dto.response.StockOutPlansForDataTableEntity;
import org.fwoxford.service.mapper.StockOutPlanMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing StockOutPlan.
 */
@Service
@Transactional
public class StockOutPlanServiceImpl implements StockOutPlanService{

    private final Logger log = LoggerFactory.getLogger(StockOutPlanServiceImpl.class);

    private final StockOutPlanRepository stockOutPlanRepository;
    private final StockOutApplyRepository stockOutApplyRepository;
    private final StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;
    private final StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    private final StockOutPlanMapper stockOutPlanMapper;

    @Autowired
    private StockOutPlanRepositories stockOutPlanRepositories;
    @Autowired
    private StockOutRequirementRepository stockOutRequirementRepository;

    public StockOutPlanServiceImpl(StockOutPlanRepository stockOutPlanRepository, StockOutApplyRepository stockOutApplyRepository, StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository, StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository, StockOutPlanMapper stockOutPlanMapper) {
        this.stockOutPlanRepository = stockOutPlanRepository;
        this.stockOutApplyRepository = stockOutApplyRepository;
        this.stockOutReqFrozenTubeRepository = stockOutReqFrozenTubeRepository;
        this.stockOutPlanFrozenTubeRepository = stockOutPlanFrozenTubeRepository;
        this.stockOutPlanMapper = stockOutPlanMapper;
    }

    /**
     * Save a stockOutPlan.
     *
     * @param stockOutPlanDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutPlanDTO save(StockOutPlanDTO stockOutPlanDTO) {
        log.debug("Request to save StockOutPlan : {}", stockOutPlanDTO);
        StockOutPlan stockOutPlan = stockOutPlanMapper.stockOutPlanDTOToStockOutPlan(stockOutPlanDTO);

        stockOutPlan.stockOutPlanCode(BankUtil.getUniqueID());

        stockOutPlan = stockOutPlanRepository.save(stockOutPlan);
        StockOutPlanDTO result = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);
        return result;
    }

    /**
     * Create and Save a stockOutPlan.
     *
     * @param applyId the application id
     * @return the persisted entity
     */
    @Override
    public StockOutPlanDTO save(Long applyId) {
        log.debug("Request to save StockOutPlan : {}", applyId);
        StockOutApply apply = stockOutApplyRepository.findOne(applyId);
        if(!apply.getStatus().equals(Constants.STOCK_OUT_APPROVED)){
            throw new BankServiceException("申请未批准，不能创建计划！");
        }
        List<StockOutPlan> stockOutPlans = stockOutPlanRepository.findAllByStockOutApplyId(applyId);

        if (stockOutPlans!=null && stockOutPlans.size() > 0){
            StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlans.get(0));
            return stockOutPlanDTO;
        }

        StockOutPlan stockOutPlan = new StockOutPlan();
        stockOutPlan.status(Constants.STOCK_OUT_PLAN_PENDING)
            .stockOutPlanCode(BankUtil.getUniqueID())
            .applyNumber(apply.getApplyCode())
            .stockOutApply(apply);
        stockOutPlan = stockOutPlanRepository.save(stockOutPlan);

        List<StockOutPlanFrozenTube> planTubes = new ArrayList<>();
        List<StockOutReqFrozenTube> reqTubes = stockOutReqFrozenTubeRepository.findAllByStockOutApplyId(applyId);
        StockOutPlan finalStockOutPlan = stockOutPlan;

        reqTubes.forEach(t -> {
            StockOutPlanFrozenTube planTube = new StockOutPlanFrozenTube();
            planTube.status(Constants.STOCK_OUT_PLAN_TUBE_PENDING)
                .stockOutPlan(finalStockOutPlan)
                .stockOutReqFrozenTube(t);
            planTubes.add(planTube);
            if (planTubes.size() >= 1000){
                stockOutPlanFrozenTubeRepository.save(planTubes);
                planTubes.clear();
            }
        });
        if (planTubes.size() > 0){
            stockOutPlanFrozenTubeRepository.save(planTubes);
            planTubes.clear();
        }


        StockOutPlanDTO result = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);
        return result;
    }

    /**
     *  Get all the stockOutPlans.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutPlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutPlans");
        Page<StockOutPlan> result = stockOutPlanRepository.findAll(pageable);
        return result.map(stockOutPlan -> stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan));
    }

    /**
     *  Get one stockOutPlan by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutPlanDTO findOne(Long id) {
        log.debug("Request to get StockOutPlan : {}", id);
        StockOutPlan stockOutPlan = stockOutPlanRepository.findOne(id);
        StockOutPlanDTO stockOutPlanDTO = stockOutPlanMapper.stockOutPlanToStockOutPlanDTO(stockOutPlan);
        return stockOutPlanDTO;
    }

    /**
     *  Delete the  stockOutPlan by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutPlan : {}", id);
        stockOutPlanRepository.delete(id);
    }

    /**
     * 查询出库计划列表
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<StockOutPlansForDataTableEntity> findAllStockOutPlan(DataTablesInput input) {
        return stockOutPlanRepositories.findAll(input);
    }

    /**
     * 根据申请ID查询出库计划
     * @param id
     * @return
     */
    @Override
    public List<StockOutPlanDTO> getAllStockOutPlansByApplyId(Long id) {
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        if(stockOutApply == null){
            throw new BankServiceException("申请不存在！");
        }
        List<StockOutPlan> stockOutPlans = stockOutPlanRepository.findAllByStockOutApplyId(id);
        return stockOutPlanMapper.stockOutPlansToStockOutPlanDTOs(stockOutPlans);
    }
}
