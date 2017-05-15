package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutPlanService;
import org.fwoxford.domain.StockOutPlan;
import org.fwoxford.repository.StockOutPlanRepository;
import org.fwoxford.service.dto.StockOutPlanDTO;
import org.fwoxford.service.mapper.StockOutPlanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutPlan.
 */
@Service
@Transactional
public class StockOutPlanServiceImpl implements StockOutPlanService{

    private final Logger log = LoggerFactory.getLogger(StockOutPlanServiceImpl.class);
    
    private final StockOutPlanRepository stockOutPlanRepository;

    private final StockOutPlanMapper stockOutPlanMapper;

    public StockOutPlanServiceImpl(StockOutPlanRepository stockOutPlanRepository, StockOutPlanMapper stockOutPlanMapper) {
        this.stockOutPlanRepository = stockOutPlanRepository;
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
        stockOutPlan = stockOutPlanRepository.save(stockOutPlan);
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
}
