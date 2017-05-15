package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutApplyService;
import org.fwoxford.domain.StockOutApply;
import org.fwoxford.repository.StockOutApplyRepository;
import org.fwoxford.service.dto.StockOutApplyDTO;
import org.fwoxford.service.mapper.StockOutApplyMapper;
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
 * Service Implementation for managing StockOutApply.
 */
@Service
@Transactional
public class StockOutApplyServiceImpl implements StockOutApplyService{

    private final Logger log = LoggerFactory.getLogger(StockOutApplyServiceImpl.class);
    
    private final StockOutApplyRepository stockOutApplyRepository;

    private final StockOutApplyMapper stockOutApplyMapper;

    public StockOutApplyServiceImpl(StockOutApplyRepository stockOutApplyRepository, StockOutApplyMapper stockOutApplyMapper) {
        this.stockOutApplyRepository = stockOutApplyRepository;
        this.stockOutApplyMapper = stockOutApplyMapper;
    }

    /**
     * Save a stockOutApply.
     *
     * @param stockOutApplyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutApplyDTO save(StockOutApplyDTO stockOutApplyDTO) {
        log.debug("Request to save StockOutApply : {}", stockOutApplyDTO);
        StockOutApply stockOutApply = stockOutApplyMapper.stockOutApplyDTOToStockOutApply(stockOutApplyDTO);
        stockOutApply = stockOutApplyRepository.save(stockOutApply);
        StockOutApplyDTO result = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);
        return result;
    }

    /**
     *  Get all the stockOutApplies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutApplyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutApplies");
        Page<StockOutApply> result = stockOutApplyRepository.findAll(pageable);
        return result.map(stockOutApply -> stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply));
    }

    /**
     *  Get one stockOutApply by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutApplyDTO findOne(Long id) {
        log.debug("Request to get StockOutApply : {}", id);
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);
        return stockOutApplyDTO;
    }

    /**
     *  Delete the  stockOutApply by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutApply : {}", id);
        stockOutApplyRepository.delete(id);
    }
}
