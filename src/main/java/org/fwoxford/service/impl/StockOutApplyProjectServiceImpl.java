package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutApplyProjectService;
import org.fwoxford.domain.StockOutApplyProject;
import org.fwoxford.repository.StockOutApplyProjectRepository;
import org.fwoxford.service.dto.StockOutApplyProjectDTO;
import org.fwoxford.service.mapper.StockOutApplyProjectMapper;
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
 * Service Implementation for managing StockOutApplyProject.
 */
@Service
@Transactional
public class StockOutApplyProjectServiceImpl implements StockOutApplyProjectService{

    private final Logger log = LoggerFactory.getLogger(StockOutApplyProjectServiceImpl.class);
    
    private final StockOutApplyProjectRepository stockOutApplyProjectRepository;

    private final StockOutApplyProjectMapper stockOutApplyProjectMapper;

    public StockOutApplyProjectServiceImpl(StockOutApplyProjectRepository stockOutApplyProjectRepository, StockOutApplyProjectMapper stockOutApplyProjectMapper) {
        this.stockOutApplyProjectRepository = stockOutApplyProjectRepository;
        this.stockOutApplyProjectMapper = stockOutApplyProjectMapper;
    }

    /**
     * Save a stockOutApplyProject.
     *
     * @param stockOutApplyProjectDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutApplyProjectDTO save(StockOutApplyProjectDTO stockOutApplyProjectDTO) {
        log.debug("Request to save StockOutApplyProject : {}", stockOutApplyProjectDTO);
        StockOutApplyProject stockOutApplyProject = stockOutApplyProjectMapper.stockOutApplyProjectDTOToStockOutApplyProject(stockOutApplyProjectDTO);
        stockOutApplyProject = stockOutApplyProjectRepository.save(stockOutApplyProject);
        StockOutApplyProjectDTO result = stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(stockOutApplyProject);
        return result;
    }

    /**
     *  Get all the stockOutApplyProjects.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutApplyProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutApplyProjects");
        Page<StockOutApplyProject> result = stockOutApplyProjectRepository.findAll(pageable);
        return result.map(stockOutApplyProject -> stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(stockOutApplyProject));
    }

    /**
     *  Get one stockOutApplyProject by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutApplyProjectDTO findOne(Long id) {
        log.debug("Request to get StockOutApplyProject : {}", id);
        StockOutApplyProject stockOutApplyProject = stockOutApplyProjectRepository.findOne(id);
        StockOutApplyProjectDTO stockOutApplyProjectDTO = stockOutApplyProjectMapper.stockOutApplyProjectToStockOutApplyProjectDTO(stockOutApplyProject);
        return stockOutApplyProjectDTO;
    }

    /**
     *  Delete the  stockOutApplyProject by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutApplyProject : {}", id);
        stockOutApplyProjectRepository.delete(id);
    }
}
