package org.fwoxford.service.impl;

import org.fwoxford.service.PositionMoveRecordService;
import org.fwoxford.domain.PositionMoveRecord;
import org.fwoxford.repository.PositionMoveRecordRepository;
import org.fwoxford.service.dto.PositionMoveRecordDTO;
import org.fwoxford.service.mapper.PositionMoveRecordMapper;
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
 * Service Implementation for managing PositionMoveRecord.
 */
@Service
@Transactional
public class PositionMoveRecordServiceImpl implements PositionMoveRecordService{

    private final Logger log = LoggerFactory.getLogger(PositionMoveRecordServiceImpl.class);
    
    private final PositionMoveRecordRepository positionMoveRecordRepository;

    private final PositionMoveRecordMapper positionMoveRecordMapper;

    public PositionMoveRecordServiceImpl(PositionMoveRecordRepository positionMoveRecordRepository, PositionMoveRecordMapper positionMoveRecordMapper) {
        this.positionMoveRecordRepository = positionMoveRecordRepository;
        this.positionMoveRecordMapper = positionMoveRecordMapper;
    }

    /**
     * Save a positionMoveRecord.
     *
     * @param positionMoveRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionMoveRecordDTO save(PositionMoveRecordDTO positionMoveRecordDTO) {
        log.debug("Request to save PositionMoveRecord : {}", positionMoveRecordDTO);
        PositionMoveRecord positionMoveRecord = positionMoveRecordMapper.positionMoveRecordDTOToPositionMoveRecord(positionMoveRecordDTO);
        positionMoveRecord = positionMoveRecordRepository.save(positionMoveRecord);
        PositionMoveRecordDTO result = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);
        return result;
    }

    /**
     *  Get all the positionMoveRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionMoveRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionMoveRecords");
        Page<PositionMoveRecord> result = positionMoveRecordRepository.findAll(pageable);
        return result.map(positionMoveRecord -> positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord));
    }

    /**
     *  Get one positionMoveRecord by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionMoveRecordDTO findOne(Long id) {
        log.debug("Request to get PositionMoveRecord : {}", id);
        PositionMoveRecord positionMoveRecord = positionMoveRecordRepository.findOne(id);
        PositionMoveRecordDTO positionMoveRecordDTO = positionMoveRecordMapper.positionMoveRecordToPositionMoveRecordDTO(positionMoveRecord);
        return positionMoveRecordDTO;
    }

    /**
     *  Delete the  positionMoveRecord by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionMoveRecord : {}", id);
        positionMoveRecordRepository.delete(id);
    }
}
