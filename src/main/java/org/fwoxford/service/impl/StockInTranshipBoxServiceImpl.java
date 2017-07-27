package org.fwoxford.service.impl;

import org.fwoxford.service.StockInTranshipBoxService;
import org.fwoxford.domain.StockInTranshipBox;
import org.fwoxford.repository.StockInTranshipBoxRepository;
import org.fwoxford.service.dto.StockInTranshipBoxDTO;
import org.fwoxford.service.mapper.StockInTranshipBoxMapper;
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
 * Service Implementation for managing StockInTranshipBox.
 */
@Service
@Transactional
public class StockInTranshipBoxServiceImpl implements StockInTranshipBoxService{

    private final Logger log = LoggerFactory.getLogger(StockInTranshipBoxServiceImpl.class);
    
    private final StockInTranshipBoxRepository stockInTranshipBoxRepository;

    private final StockInTranshipBoxMapper stockInTranshipBoxMapper;

    public StockInTranshipBoxServiceImpl(StockInTranshipBoxRepository stockInTranshipBoxRepository, StockInTranshipBoxMapper stockInTranshipBoxMapper) {
        this.stockInTranshipBoxRepository = stockInTranshipBoxRepository;
        this.stockInTranshipBoxMapper = stockInTranshipBoxMapper;
    }

    /**
     * Save a stockInTranshipBox.
     *
     * @param stockInTranshipBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockInTranshipBoxDTO save(StockInTranshipBoxDTO stockInTranshipBoxDTO) {
        log.debug("Request to save StockInTranshipBox : {}", stockInTranshipBoxDTO);
        StockInTranshipBox stockInTranshipBox = stockInTranshipBoxMapper.stockInTranshipBoxDTOToStockInTranshipBox(stockInTranshipBoxDTO);
        stockInTranshipBox = stockInTranshipBoxRepository.save(stockInTranshipBox);
        StockInTranshipBoxDTO result = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);
        return result;
    }

    /**
     *  Get all the stockInTranshipBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockInTranshipBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockInTranshipBoxes");
        Page<StockInTranshipBox> result = stockInTranshipBoxRepository.findAll(pageable);
        return result.map(stockInTranshipBox -> stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox));
    }

    /**
     *  Get one stockInTranshipBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockInTranshipBoxDTO findOne(Long id) {
        log.debug("Request to get StockInTranshipBox : {}", id);
        StockInTranshipBox stockInTranshipBox = stockInTranshipBoxRepository.findOne(id);
        StockInTranshipBoxDTO stockInTranshipBoxDTO = stockInTranshipBoxMapper.stockInTranshipBoxToStockInTranshipBoxDTO(stockInTranshipBox);
        return stockInTranshipBoxDTO;
    }

    /**
     *  Delete the  stockInTranshipBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockInTranshipBox : {}", id);
        stockInTranshipBoxRepository.delete(id);
    }
}
