package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutFilesService;
import org.fwoxford.domain.StockOutFiles;
import org.fwoxford.repository.StockOutFilesRepository;
import org.fwoxford.service.dto.StockOutFilesDTO;
import org.fwoxford.service.mapper.StockOutFilesMapper;
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
 * Service Implementation for managing StockOutFiles.
 */
@Service
@Transactional
public class StockOutFilesServiceImpl implements StockOutFilesService{

    private final Logger log = LoggerFactory.getLogger(StockOutFilesServiceImpl.class);
    
    private final StockOutFilesRepository stockOutFilesRepository;

    private final StockOutFilesMapper stockOutFilesMapper;

    public StockOutFilesServiceImpl(StockOutFilesRepository stockOutFilesRepository, StockOutFilesMapper stockOutFilesMapper) {
        this.stockOutFilesRepository = stockOutFilesRepository;
        this.stockOutFilesMapper = stockOutFilesMapper;
    }

    /**
     * Save a stockOutFiles.
     *
     * @param stockOutFilesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutFilesDTO save(StockOutFilesDTO stockOutFilesDTO) {
        log.debug("Request to save StockOutFiles : {}", stockOutFilesDTO);
        StockOutFiles stockOutFiles = stockOutFilesMapper.stockOutFilesDTOToStockOutFiles(stockOutFilesDTO);
        stockOutFiles = stockOutFilesRepository.save(stockOutFiles);
        StockOutFilesDTO result = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);
        return result;
    }

    /**
     *  Get all the stockOutFiles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFilesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutFiles");
        Page<StockOutFiles> result = stockOutFilesRepository.findAll(pageable);
        return result.map(stockOutFiles -> stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles));
    }

    /**
     *  Get one stockOutFiles by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutFilesDTO findOne(Long id) {
        log.debug("Request to get StockOutFiles : {}", id);
        StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(id);
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);
        return stockOutFilesDTO;
    }

    /**
     *  Delete the  stockOutFiles by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutFiles : {}", id);
        stockOutFilesRepository.delete(id);
    }
}
