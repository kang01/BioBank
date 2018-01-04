package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.repository.TranshipBoxRepository;
import org.fwoxford.service.TranshipTubeService;
import org.fwoxford.domain.TranshipTube;
import org.fwoxford.repository.TranshipTubeRepository;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.TranshipTubeDTO;
import org.fwoxford.service.mapper.TranshipBoxMapper;
import org.fwoxford.service.mapper.TranshipTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Service Implementation for managing TranshipTube.
 */
@Service
@Transactional
public class TranshipTubeServiceImpl implements TranshipTubeService{

    private final Logger log = LoggerFactory.getLogger(TranshipTubeServiceImpl.class);

    private final TranshipTubeRepository transhipTubeRepository;

    private final TranshipTubeMapper transhipTubeMapper;

    @Autowired
    TranshipBoxRepository transhipBoxRepository;

    @Autowired
    FrozenTubeRepository frozenTubeRepository;

    @Autowired
    TranshipBoxMapper transhipBoxMapper;


    public TranshipTubeServiceImpl(TranshipTubeRepository transhipTubeRepository, TranshipTubeMapper transhipTubeMapper) {
        this.transhipTubeRepository = transhipTubeRepository;
        this.transhipTubeMapper = transhipTubeMapper;
    }

    /**
     * Save a transhipTube.
     *
     * @param transhipTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranshipTubeDTO save(TranshipTubeDTO transhipTubeDTO) {
        log.debug("Request to save TranshipTube : {}", transhipTubeDTO);
        TranshipTube transhipTube = transhipTubeMapper.transhipTubeDTOToTranshipTube(transhipTubeDTO);
        transhipTube = transhipTubeRepository.save(transhipTube);
        TranshipTubeDTO result = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);
        return result;
    }

    /**
     *  Get all the transhipTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranshipTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TranshipTubes");
        Page<TranshipTube> result = transhipTubeRepository.findAll(pageable);
        return result.map(transhipTube -> transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube));
    }

    /**
     *  Get one transhipTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranshipTubeDTO findOne(Long id) {
        log.debug("Request to get TranshipTube : {}", id);
        TranshipTube transhipTube = transhipTubeRepository.findOne(id);
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);
        return transhipTubeDTO;
    }

    /**
     *  Delete the  transhipTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TranshipTube : {}", id);
        transhipTubeRepository.delete(id);
    }

    @Override
    public List<TranshipTube> saveTranshipTube(TranshipBox transhipBox, List<FrozenTube> frozenTubeList) {
        if(transhipBox == null){
            return null;
        }
        List<TranshipTube> transhipTubes = transhipTubeRepository.findByTranshipBoxIdAndStatusNotIn(transhipBox.getId()
                ,new ArrayList<String>(){{add(Constants.INVALID);add(Constants.FROZEN_BOX_INVALID);}});
        List<Long> oldTubeIds = new ArrayList<Long>();
        for (FrozenTube frozenTube : frozenTubeList) {
            oldTubeIds.add(frozenTube.getId());
        }
        List<TranshipTube> transhipTubesForDelete = new ArrayList<TranshipTube>();
        Map<Long,TranshipTube> map = new HashMap<>();
        for (TranshipTube transhipTube : transhipTubes) {
            if(!oldTubeIds.contains(transhipTube.getFrozenTube().getId())){
                transhipTube.setStatus(Constants.INVALID);
                transhipTubesForDelete.add(transhipTube);
            }else {
                map.put(transhipTube.getFrozenTube().getId(),transhipTube);
            }
        }
        transhipTubeRepository.save(transhipTubesForDelete);
        for (FrozenTube frozenTube : frozenTubeList) {
                TranshipTube transhipTube = map.get(frozenTube.getId())==null?new TranshipTube():map.get(frozenTube.getId());
                TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.frozenTubeToTranshipTube(frozenTube);
                transhipTubeDTO.setTranshipBoxId(transhipBox.getId());
                transhipTubeDTO.setId(transhipTube.getId());
                transhipTubes.add(transhipTubeMapper.transhipTubeDTOToTranshipTube(transhipTubeDTO));
        }
        return transhipTubes;
    }

    /**
     * 转运冻存管/归还冻存管销毁
     * @param transhipTubeDTO
     * @param boxId
     */
    @Override
    public TranshipBoxDTO destroyTranshipTube(TranshipTubeDTO transhipTubeDTO, Long boxId) {
        List<Long> frozenTubeIds = transhipTubeDTO.getFrozenTubeIds();
        if(StringUtils.isEmpty(transhipTubeDTO.getDestroyReason())){
            throw new BankServiceException("销毁原因不能为空!");
        }
        if(frozenTubeIds == null || frozenTubeIds.size()==0){
            throw new BankServiceException("请传入需要销毁冻存管ID!");
        }
        TranshipBox transhipBox = transhipBoxRepository.findByIdAndStatusNot(boxId,Constants.INVALID);
        if(transhipBox == null || (transhipBox!=null && transhipBox.getStatus().equals(Constants.FROZEN_BOX_TRANSHIP_COMPLETE))){
            throw new BankServiceException("冻存盒不存在或状态已经接受完成，不能销毁！");
        }
        //todo 增加保存销毁记录
        List<TranshipTube> transhipTubes = transhipTubeRepository.findByTranshipBoxIdAndStatusNotIn(boxId,new ArrayList<String>(){{add(Constants.INVALID);}});
        List<FrozenTube> frozenTubeList = new ArrayList<>();
        for(TranshipTube t :transhipTubes){
            FrozenTube frozenTube = t.getFrozenTube();
            t.setStatus(Constants.FROZEN_TUBE_DESTROY);
            frozenTube.setStatus(Constants.FROZEN_TUBE_DESTROY);
            frozenTubeList.add(frozenTube);
        }
        transhipTubeRepository.save(transhipTubes);
        frozenTubeRepository.save(frozenTubeList);

        return transhipBoxMapper.transhipBoxToTranshipBoxDTOWithSampleType(transhipBox,1);
    }
}
