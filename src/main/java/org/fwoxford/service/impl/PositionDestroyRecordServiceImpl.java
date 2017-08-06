package org.fwoxford.service.impl;

import org.fwoxford.service.PositionDestroyRecordService;
import org.fwoxford.domain.PositionDestroyRecord;
import org.fwoxford.repository.PositionDestroyRecordRepository;
import org.fwoxford.service.dto.PositionDestroyRecordDTO;
import org.fwoxford.service.mapper.PositionDestroyRecordMapper;
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
 * Service Implementation for managing PositionDestroyRecord.
 */
@Service
@Transactional
public class PositionDestroyRecordServiceImpl implements PositionDestroyRecordService{

    private final Logger log = LoggerFactory.getLogger(PositionDestroyRecordServiceImpl.class);
    
    private final PositionDestroyRecordRepository positionDestroyRecordRepository;

    private final PositionDestroyRecordMapper positionDestroyRecordMapper;

    public PositionDestroyRecordServiceImpl(PositionDestroyRecordRepository positionDestroyRecordRepository, PositionDestroyRecordMapper positionDestroyRecordMapper) {
        this.positionDestroyRecordRepository = positionDestroyRecordRepository;
        this.positionDestroyRecordMapper = positionDestroyRecordMapper;
    }

    /**
     * Save a positionDestroyRecord.
     *
     * @param positionDestroyRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionDestroyRecordDTO save(PositionDestroyRecordDTO positionDestroyRecordDTO) {
        log.debug("Request to save PositionDestroyRecord : {}", positionDestroyRecordDTO);
        PositionDestroyRecord positionDestroyRecord = positionDestroyRecordMapper.positionDestroyRecordDTOToPositionDestroyRecord(positionDestroyRecordDTO);
        positionDestroyRecord = positionDestroyRecordRepository.save(positionDestroyRecord);
        PositionDestroyRecordDTO result = positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(positionDestroyRecord);
        return result;
    }

    /**
     *  Get all the positionDestroyRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PositionDestroyRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PositionDestroyRecords");
        Page<PositionDestroyRecord> result = positionDestroyRecordRepository.findAll(pageable);
        return result.map(positionDestroyRecord -> positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(positionDestroyRecord));
    }

    /**
     *  Get one positionDestroyRecord by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionDestroyRecordDTO findOne(Long id) {
        log.debug("Request to get PositionDestroyRecord : {}", id);
        PositionDestroyRecord positionDestroyRecord = positionDestroyRecordRepository.findOne(id);
        PositionDestroyRecordDTO positionDestroyRecordDTO = positionDestroyRecordMapper.positionDestroyRecordToPositionDestroyRecordDTO(positionDestroyRecord);
        return positionDestroyRecordDTO;
    }

    /**
     *  Delete the  positionDestroyRecord by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionDestroyRecord : {}", id);
        positionDestroyRecordRepository.delete(id);
    }
}
