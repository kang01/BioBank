package org.fwoxford.service.impl;

import org.fwoxford.service.FrozenTubeRecordService;
import org.fwoxford.domain.FrozenTubeRecord;
import org.fwoxford.repository.FrozenTubeRecordRepository;
import org.fwoxford.service.dto.FrozenTubeRecordDTO;
import org.fwoxford.service.mapper.FrozenTubeRecordMapper;
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
 * Service Implementation for managing FrozenTubeRecord.
 */
@Service
@Transactional
public class FrozenTubeRecordServiceImpl implements FrozenTubeRecordService{

    private final Logger log = LoggerFactory.getLogger(FrozenTubeRecordServiceImpl.class);
    
    private final FrozenTubeRecordRepository frozenTubeRecordRepository;

    private final FrozenTubeRecordMapper frozenTubeRecordMapper;

    public FrozenTubeRecordServiceImpl(FrozenTubeRecordRepository frozenTubeRecordRepository, FrozenTubeRecordMapper frozenTubeRecordMapper) {
        this.frozenTubeRecordRepository = frozenTubeRecordRepository;
        this.frozenTubeRecordMapper = frozenTubeRecordMapper;
    }

    /**
     * Save a frozenTubeRecord.
     *
     * @param frozenTubeRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenTubeRecordDTO save(FrozenTubeRecordDTO frozenTubeRecordDTO) {
        log.debug("Request to save FrozenTubeRecord : {}", frozenTubeRecordDTO);
        FrozenTubeRecord frozenTubeRecord = frozenTubeRecordMapper.frozenTubeRecordDTOToFrozenTubeRecord(frozenTubeRecordDTO);
        frozenTubeRecord = frozenTubeRecordRepository.save(frozenTubeRecord);
        FrozenTubeRecordDTO result = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);
        return result;
    }

    /**
     *  Get all the frozenTubeRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenTubeRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenTubeRecords");
        Page<FrozenTubeRecord> result = frozenTubeRecordRepository.findAll(pageable);
        return result.map(frozenTubeRecord -> frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord));
    }

    /**
     *  Get one frozenTubeRecord by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenTubeRecordDTO findOne(Long id) {
        log.debug("Request to get FrozenTubeRecord : {}", id);
        FrozenTubeRecord frozenTubeRecord = frozenTubeRecordRepository.findOne(id);
        FrozenTubeRecordDTO frozenTubeRecordDTO = frozenTubeRecordMapper.frozenTubeRecordToFrozenTubeRecordDTO(frozenTubeRecord);
        return frozenTubeRecordDTO;
    }

    /**
     *  Delete the  frozenTubeRecord by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenTubeRecord : {}", id);
        frozenTubeRecordRepository.delete(id);
    }
}
