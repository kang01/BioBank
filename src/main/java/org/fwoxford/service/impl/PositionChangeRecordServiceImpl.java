package org.fwoxford.service.impl;

import org.fwoxford.service.PositionChangeRecordService;
import org.fwoxford.domain.PositionChangeRecord;
import org.fwoxford.repository.PositionChangeRecordRepository;
import org.fwoxford.service.dto.PositionChangeRecordDTO;
import org.fwoxford.service.mapper.PositionChangeRecordMapper;
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
 * Service Implementation for managing PositionChangeRecord.
 */
@Service
@Transactional
public class PositionChangeRecordServiceImpl implements PositionChangeRecordService{

    private final Logger log = LoggerFactory.getLogger(PositionChangeRecordServiceImpl.class);
    
    private final PositionChangeRecordRepository positionChangeRecordRepository;

    private final PositionChangeRecordMapper positionChangeRecordMapper;

    public PositionChangeRecordServiceImpl(PositionChangeRecordRepository positionChangeRecordRepository, PositionChangeRecordMapper positionChangeRecordMapper) {
        this.positionChangeRecordRepository = positionChangeRecordRepository;
        this.positionChangeRecordMapper = positionChangeRecordMapper;
    }

    /**
     * Save a positionChangeRecord.
     *
     * @param positionChangeRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionChangeRecordDTO save(PositionChangeRecordDTO positionChangeRecordDTO) {
        log.debug("Request to save PositionChangeRecord : {}", positionChangeRecordDTO);
        PositionChangeRecord positionChangeRecord = positionChangeRecordMapper.positionChangeRecordDTOToPositionChangeRecord(positionChangeRecordDTO);
        positionChangeRecord = positionChangeRecordRepository.save(positionChangeRecord);
        PositionChangeRecordDTO result = positionChangeRecordMapper.positionChangeRecordToPositionChangeRecordDTO(positionChangeRecord);
        return result;
    }

    /**
     *  Get all the positionChangeRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionChangeRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionChangeRecords");
        Page<PositionChangeRecord> result = positionChangeRecordRepository.findAll(pageable);
        return result.map(positionChangeRecord -> positionChangeRecordMapper.positionChangeRecordToPositionChangeRecordDTO(positionChangeRecord));
    }

    /**
     *  Get one positionChangeRecord by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionChangeRecordDTO findOne(Long id) {
        log.debug("Request to get PositionChangeRecord : {}", id);
        PositionChangeRecord positionChangeRecord = positionChangeRecordRepository.findOne(id);
        PositionChangeRecordDTO positionChangeRecordDTO = positionChangeRecordMapper.positionChangeRecordToPositionChangeRecordDTO(positionChangeRecord);
        return positionChangeRecordDTO;
    }

    /**
     *  Delete the  positionChangeRecord by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionChangeRecord : {}", id);
        positionChangeRecordRepository.delete(id);
    }
}
