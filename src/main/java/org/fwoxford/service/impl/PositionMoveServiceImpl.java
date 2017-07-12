package org.fwoxford.service.impl;

import org.fwoxford.service.PositionMoveService;
import org.fwoxford.domain.PositionMove;
import org.fwoxford.repository.PositionMoveRepository;
import org.fwoxford.service.dto.PositionMoveDTO;
import org.fwoxford.service.dto.PositionMoveRecordDTO;
import org.fwoxford.service.dto.response.PositionMoveForBox;
import org.fwoxford.service.dto.response.PositionMoveForSample;
import org.fwoxford.service.mapper.PositionMoveMapper;
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
 * Service Implementation for managing PositionMove.
 */
@Service
@Transactional
public class PositionMoveServiceImpl implements PositionMoveService{

    private final Logger log = LoggerFactory.getLogger(PositionMoveServiceImpl.class);

    private final PositionMoveRepository positionMoveRepository;

    private final PositionMoveMapper positionMoveMapper;

    public PositionMoveServiceImpl(PositionMoveRepository positionMoveRepository, PositionMoveMapper positionMoveMapper) {
        this.positionMoveRepository = positionMoveRepository;
        this.positionMoveMapper = positionMoveMapper;
    }

    /**
     * Save a positionMove.
     *
     * @param positionMoveDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionMoveDTO save(PositionMoveDTO positionMoveDTO) {
        log.debug("Request to save PositionMove : {}", positionMoveDTO);
        PositionMove positionMove = positionMoveMapper.positionMoveDTOToPositionMove(positionMoveDTO);
        positionMove = positionMoveRepository.save(positionMove);
        PositionMoveDTO result = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);
        return result;
    }

    /**
     *  Get all the positionMoves.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionMoveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionMoves");
        Page<PositionMove> result = positionMoveRepository.findAll(pageable);
        return result.map(positionMove -> positionMoveMapper.positionMoveToPositionMoveDTO(positionMove));
    }

    /**
     *  Get one positionMove by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionMoveDTO findOne(Long id) {
        log.debug("Request to get PositionMove : {}", id);
        PositionMove positionMove = positionMoveRepository.findOne(id);
        PositionMoveDTO positionMoveDTO = positionMoveMapper.positionMoveToPositionMoveDTO(positionMove);
        return positionMoveDTO;
    }

    /**
     *  Delete the  positionMove by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionMove : {}", id);
        positionMoveRepository.delete(id);
    }

    /**
     * 样本移位
     * @param positionMoveDTO
     * @return
     */
    @Override
    public PositionMoveDTO moveSamplePosition(PositionMoveDTO positionMoveDTO) {
        //保存移位数据
        PositionMove positionMove = positionMoveMapper.positionMoveDTOToPositionMove(positionMoveDTO);
        positionMoveRepository.save(positionMove);
        //保存移位记录详情数据
        List<PositionMoveForBox> positionMoveForBoxes = positionMoveDTO.getPositionMoveForBoxList();
        for(PositionMoveForBox p : positionMoveForBoxes){
            createMoveRecordDetail(p,positionMove);
        }
        positionMoveDTO.setId(positionMove.getId());
        return positionMoveDTO;
    }

    public void createMoveRecordDetail(PositionMoveForBox p, PositionMove positionMove) {
        List<PositionMoveForSample> sampleList = p.getPositionMoveForSampleList();
        for(PositionMoveForSample s :sampleList){
            //验证位置是否可以用
            //验证冻存盒类型与原冻存盒类型是否一样
            //验证样本类型与盒子是否一致，
            //盒子若有分类，验证与冻存管是否一致
            //冻存管若有分类，验证与冻存盒是否一致
            //验证项目是否一致


        }

    }
}
