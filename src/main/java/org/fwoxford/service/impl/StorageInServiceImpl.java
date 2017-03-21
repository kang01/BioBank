package org.fwoxford.service.impl;

import org.fwoxford.service.StorageInService;
import org.fwoxford.domain.StorageIn;
import org.fwoxford.repository.StorageInRepository;
import org.fwoxford.service.dto.StorageInDTO;
import org.fwoxford.service.mapper.StorageInMapper;
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
 * Service Implementation for managing StorageIn.
 */
@Service
@Transactional
public class StorageInServiceImpl implements StorageInService{

    private final Logger log = LoggerFactory.getLogger(StorageInServiceImpl.class);
    
    private final StorageInRepository storageInRepository;

    private final StorageInMapper storageInMapper;

    public StorageInServiceImpl(StorageInRepository storageInRepository, StorageInMapper storageInMapper) {
        this.storageInRepository = storageInRepository;
        this.storageInMapper = storageInMapper;
    }

    /**
     * Save a storageIn.
     *
     * @param storageInDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StorageInDTO save(StorageInDTO storageInDTO) {
        log.debug("Request to save StorageIn : {}", storageInDTO);
        StorageIn storageIn = storageInMapper.storageInDTOToStorageIn(storageInDTO);
        storageIn = storageInRepository.save(storageIn);
        StorageInDTO result = storageInMapper.storageInToStorageInDTO(storageIn);
        return result;
    }

    /**
     *  Get all the storageIns.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StorageInDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StorageIns");
        Page<StorageIn> result = storageInRepository.findAll(pageable);
        return result.map(storageIn -> storageInMapper.storageInToStorageInDTO(storageIn));
    }

    /**
     *  Get one storageIn by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StorageInDTO findOne(Long id) {
        log.debug("Request to get StorageIn : {}", id);
        StorageIn storageIn = storageInRepository.findOne(id);
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);
        return storageInDTO;
    }

    /**
     *  Delete the  storageIn by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StorageIn : {}", id);
        storageInRepository.delete(id);
    }
}
