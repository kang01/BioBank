package org.fwoxford.service.impl;

import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.mapper.FrozenTubeMapper;
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
 * Service Implementation for managing FrozenTube.
 */
@Service
@Transactional
public class FrozenTubeServiceImpl implements FrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(FrozenTubeServiceImpl.class);

    private final FrozenTubeRepository frozenTubeRepository;

    private final FrozenTubeMapper frozenTubeMapper;

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
}
