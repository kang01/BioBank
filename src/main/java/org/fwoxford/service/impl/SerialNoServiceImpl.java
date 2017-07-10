package org.fwoxford.service.impl;

import org.fwoxford.service.SerialNoService;
import org.fwoxford.domain.SerialNo;
import org.fwoxford.repository.SerialNoRepository;
import org.fwoxford.service.dto.SerialNoDTO;
import org.fwoxford.service.mapper.SerialNoMapper;
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
 * Service Implementation for managing SerialNo.
 */
@Service
@Transactional
public class SerialNoServiceImpl implements SerialNoService{

    private final Logger log = LoggerFactory.getLogger(SerialNoServiceImpl.class);
    
    private final SerialNoRepository serialNoRepository;

    private final SerialNoMapper serialNoMapper;

    public SerialNoServiceImpl(SerialNoRepository serialNoRepository, SerialNoMapper serialNoMapper) {
        this.serialNoRepository = serialNoRepository;
        this.serialNoMapper = serialNoMapper;
    }

    /**
     * Save a serialNo.
     *
     * @param serialNoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SerialNoDTO save(SerialNoDTO serialNoDTO) {
        log.debug("Request to save SerialNo : {}", serialNoDTO);
        SerialNo serialNo = serialNoMapper.serialNoDTOToSerialNo(serialNoDTO);
        serialNo = serialNoRepository.save(serialNo);
        SerialNoDTO result = serialNoMapper.serialNoToSerialNoDTO(serialNo);
        return result;
    }

    /**
     *  Get all the serialNos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SerialNoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SerialNos");
        Page<SerialNo> result = serialNoRepository.findAll(pageable);
        return result.map(serialNo -> serialNoMapper.serialNoToSerialNoDTO(serialNo));
    }

    /**
     *  Get one serialNo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SerialNoDTO findOne(Long id) {
        log.debug("Request to get SerialNo : {}", id);
        SerialNo serialNo = serialNoRepository.findOne(id);
        SerialNoDTO serialNoDTO = serialNoMapper.serialNoToSerialNoDTO(serialNo);
        return serialNoDTO;
    }

    /**
     *  Delete the  serialNo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SerialNo : {}", id);
        serialNoRepository.delete(id);
    }
}
