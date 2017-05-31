package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutTaskFrozenTubeService;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.StockOutTaskFrozenTubeDTO;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.mapper.StockOutTaskFrozenTubeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    private StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    @Autowired
    private StockOutBoxTubeRepository stockOutBoxTubeRepository;

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
            frozenTubeRepository.save(frozenTube);
        }
        return frozenTubeDTOS;
    }

    @Override
    public List<FrozenTubeResponse> repealStockOutTaskFrozenTube( List<FrozenTubeResponse> frozenTubeDTOS) {
        for(FrozenTubeResponse tube :frozenTubeDTOS){
            if(tube.getId() == null){
                continue;
            }
            //需求样本撤销
            StockOutReqFrozenTube stockOutReqFrozenTube = stockOutReqFrozenTubeRepository.findByFrozenTubeId(tube.getId());
            stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE_NOT);
            stockOutReqFrozenTube.setMemo(tube.getMemo());
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTube);

            //计划出库样本
            StockOutPlanFrozenTube stockOutPlanFrozenTube = stockOutPlanFrozenTubeRepository.findByStockOutReqFrozenTubeId(stockOutReqFrozenTube.getId());
            stockOutPlanFrozenTube.setStatus(Constants.STOCK_OUT_PLAN_TUBE_CANCEL);
            stockOutPlanFrozenTube.setMemo(tube.getMemo());
            stockOutPlanFrozenTubeRepository.save(stockOutPlanFrozenTube);
            //任务出库样本
            StockOutTaskFrozenTube stockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.findByStockOutPlanFrozenTubeId(stockOutPlanFrozenTube.getId());
            stockOutTaskFrozenTube.setStatus(Constants.STOCK_OUT_FROZEN_TUBE_CANCEL);
            stockOutTaskFrozenTube.setMemo(tube.getMemo());
            stockOutTaskFrozenTubeRepository.save(stockOutTaskFrozenTube);
            //冻存管与出库盒的关系
            StockOutBoxTube stockOutBoxTube = stockOutBoxTubeRepository.findByFrozenTubeId(tube.getId());
            stockOutBoxTube.setStatus(Constants.FROZEN_BOX_TUBE_STOCKOUT_CANCEL);
            stockOutBoxTube.setMemo(tube.getMemo());
            stockOutBoxTubeRepository.save(stockOutBoxTube);
            FrozenTube frozenTube = frozenTubeRepository.findOne(tube.getId());
            if(frozenTube == null){
                continue;
            }
            frozenTube.setMemo(tube.getMemo());
            frozenTubeRepository.save(frozenTube);
        }
        return frozenTubeDTOS;
    }

    @Override
    public List<FrozenTubeResponse> noteStockOutTaskFrozenTube( List<FrozenTubeResponse> frozenTubeDTOS) {
        for(FrozenTubeResponse tube :frozenTubeDTOS){
            if(tube.getId() == null){
                continue;
            }
            FrozenTube frozenTube = frozenTubeRepository.findOne(tube.getId());
            if(frozenTube == null){
                continue;
            }
            frozenTube.setMemo(tube.getMemo());
            frozenTubeRepository.save(frozenTube);
        }
        return frozenTubeDTOS;
    }
}
