package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockOutFrozenTube;
import org.fwoxford.domain.StockOutTaskFrozenTube;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.StockOutTaskFrozenTubeRepository;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;
/**
 * Service Implementation for managing FrozenTube.
 */
@Service
@Transactional
public class FrozenTubeServiceImpl implements FrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(FrozenTubeServiceImpl.class);

    private final FrozenTubeRepository frozenTubeRepository;

    private final FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    public FrozenTubeServiceImpl(FrozenTubeRepository frozenTubeRepository, FrozenTubeMapper frozenTubeMapper) {
        this.frozenTubeRepository = frozenTubeRepository;
        this.frozenTubeMapper = frozenTubeMapper;
    }

    /**
     * Save a frozenTube.
     *
     * @param frozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenTubeDTO save(FrozenTubeDTO frozenTubeDTO) {
        log.debug("Request to save FrozenTube : {}", frozenTubeDTO);
        FrozenTube frozenTube = frozenTubeMapper.frozenTubeDTOToFrozenTube(frozenTubeDTO);
        frozenTube = frozenTubeRepository.save(frozenTube);
        FrozenTubeDTO result = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);
        return result;
    }

    /**
     *  Get all the frozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenTubes");
        Page<FrozenTube> result = frozenTubeRepository.findAll(pageable);
        return result.map(frozenTube -> frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube));
    }

    /**
     *  Get one frozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenTubeDTO findOne(Long id) {
        log.debug("Request to get FrozenTube : {}", id);
        FrozenTube frozenTube = frozenTubeRepository.findOne(id);
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);
        return frozenTubeDTO;
    }

    /**
     *  Delete the  frozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenTube : {}", id);
        frozenTubeRepository.delete(id);
    }
    /**
     * 根据冻存盒id查询冻存管信息
     * @param frozenBoxId 冻存盒id
     */
    @Override
    public List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId) {
        log.debug("Request to findFrozenTubeListByBoxId : {}", frozenBoxId);
        return frozenTubeRepository.findFrozenTubeListByBoxId(frozenBoxId);
    }
    /**
     * 根据冻存盒Code查询冻存管信息
     * @param frozenBoxCode 冻存盒Code
     */
    @Override
    public List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode) {
        log.debug("Request to findFrozenTubeListByBoxCode : {}", frozenBoxCode);
        return frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBoxCode);
    }

    /**
     * 批量保存冻存管
     * @param frozenTubeDTOList
     */
    @Override
    public List<FrozenTube> saveBatch(List<FrozenTubeDTO> frozenTubeDTOList) {
        Assert.notNull(frozenTubeDTOList,"frozen Tube must not be null");
        List<FrozenTube> frozenTubes = frozenTubeMapper.frozenTubeDTOsToFrozenTubes(frozenTubeDTOList);
//        List<FrozenTube> frozenTubesList =  frozenTubeRepository.save(frozenTubes);

        List<FrozenTube> frozenTubesList = new ArrayList<>();
        for(FrozenTube frozenTube : frozenTubes){
            FrozenTube tube = frozenTubeRepository.save(frozenTube);
            frozenTubesList.add(tube);
        }

        return frozenTubesList;
    }

    @Override
    public FrozenBoxAndFrozenTubeResponse getFrozenTubeByFrozenBoxCode(String frozenBoxCode) {
        FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse = new FrozenBoxAndFrozenTubeResponse();
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);

        List<FrozenTubeResponse> frozenTubeResponses = new ArrayList<FrozenTubeResponse>();
        //根据冻存盒编码查询冻存管
        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBoxCode);
        //根据冻存盒查询要出库的样本
        List<StockOutTaskFrozenTube> stockOutFrozenTubes = stockOutTaskFrozenTubeRepository.findByFrozenBox(frozenBoxCode);
        for(FrozenTube f:frozenTubes){
            FrozenTubeResponse frozenTubeResponse = frozenTubeMapper.frozenTubeToFrozenTubeResponses(f);
            for(StockOutTaskFrozenTube s :stockOutFrozenTubes){
                if(s.getStockOutPlanFrozenTube().getStockOutReqFrozenTube().getFrozenTube().getId().equals(f.getId())){
                    frozenTubeResponse.setStockOutFlag(Constants.YES);
                }
            }
            frozenTubeResponses.add(frozenTubeResponse);
        }
        frozenBoxAndFrozenTubeResponse = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox,frozenTubeResponses);
        //构造返回结果
        return frozenBoxAndFrozenTubeResponse;
    }
}
