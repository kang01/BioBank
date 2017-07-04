package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.StockInBox;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.service.StockInTubeService;
import org.fwoxford.domain.StockInTube;
import org.fwoxford.repository.StockInTubeRepository;
import org.fwoxford.service.dto.StockInTubeDTO;
import org.fwoxford.service.mapper.StockInTubeMapper;
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
 * Service Implementation for managing StockInTube.
 */
@Service
@Transactional
public class StockInTubeServiceImpl implements StockInTubeService{

    private final Logger log = LoggerFactory.getLogger(StockInTubeServiceImpl.class);

    private final StockInTubeRepository stockInTubeRepository;

    private final StockInTubeMapper stockInTubeMapper;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    public StockInTubeServiceImpl(StockInTubeRepository stockInTubeRepository, StockInTubeMapper stockInTubeMapper) {
        this.stockInTubeRepository = stockInTubeRepository;
        this.stockInTubeMapper = stockInTubeMapper;
    }

    /**
     * Save a stockInTube.
     *
     * @param stockInTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockInTubeDTO save(StockInTubeDTO stockInTubeDTO) {
        log.debug("Request to save StockInTube : {}", stockInTubeDTO);
        StockInTube stockInTube = stockInTubeMapper.stockInTubeDTOToStockInTube(stockInTubeDTO);
        stockInTube = stockInTubeRepository.save(stockInTube);
        StockInTubeDTO result = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);
        return result;
    }

    /**
     *  Get all the stockInTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockInTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockInTubes");
        Page<StockInTube> result = stockInTubeRepository.findAll(pageable);
        return result.map(stockInTube -> stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube));
    }

    /**
     *  Get one stockInTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockInTubeDTO findOne(Long id) {
        log.debug("Request to get StockInTube : {}", id);
        StockInTube stockInTube = stockInTubeRepository.findOne(id);
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);
        return stockInTubeDTO;
    }

    /**
     *  Delete the  stockInTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockInTube : {}", id);
        stockInTubeRepository.delete(id);
    }

    @Override
    public void saveStockInTube(StockInBox stockInBox) {
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(stockInBox.getFrozenBoxCode());
//        List<StockInTube> stockInTubeList = stockInTubeRepository.findByStockInBoxId(stockInBox.getId());

        for(FrozenTube tube :frozenTubeList){
//            if(stockInTubeList!=null&&stockInTubeList.size()>0){
//                for(StockInTube s :stockInTubeList){
//                    if(tube.getId().equals(s.getId())){
//                        if(tube.getTubeColumns()!=s.getTubeColumns()||tube.getTubeColumns()!=s.getTubeColumns()){
//                            StockInTube stockInTube = new StockInTube();
//                            stockInTube.status(Constants.STOCK_IN_TUBE_PENDING).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
//                                .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox);
//                        }
//                        if(tube.getStatus()!=s.getStatus()){
//                            StockInTube stockInTube = new StockInTube();
//                            stockInTube.status(Constants.STOCK_IN_TUBE_PENDING).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
//                                .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox);
//                        }
//                    }
//                }
//            }else{
                tube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKING);
                frozenTubeRepository.saveAndFlush(tube);

//                StockInTube stockInTube = new StockInTube();
//                stockInTube.status(Constants.STOCK_IN_TUBE_PENDING).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
//                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox);
//                stockInTubeRepository.save(stockInTube);
//            }
        }
    }
}
