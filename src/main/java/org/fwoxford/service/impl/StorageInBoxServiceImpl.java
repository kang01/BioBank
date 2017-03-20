package org.fwoxford.service.impl;

import org.fwoxford.service.StorageInBoxService;
import org.fwoxford.domain.StorageInBox;
import org.fwoxford.repository.StorageInBoxRepository;
import org.fwoxford.service.dto.StorageInBoxDTO;
import org.fwoxford.service.mapper.StorageInBoxMapper;
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
 * Service Implementation for managing StorageInBox.
 */
@Service
@Transactional
public class StorageInBoxServiceImpl implements StorageInBoxService{

    private final Logger log = LoggerFactory.getLogger(StorageInBoxServiceImpl.class);
    
    private final StorageInBoxRepository storageInBoxRepository;

    private final StorageInBoxMapper storageInBoxMapper;

    public StorageInBoxServiceImpl(StorageInBoxRepository storageInBoxRepository, StorageInBoxMapper storageInBoxMapper) {
        this.storageInBoxRepository = storageInBoxRepository;
        this.storageInBoxMapper = storageInBoxMapper;
    }

    /**
     * Save a storageInBox.
     *
     * @param storageInBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StorageInBoxDTO save(StorageInBoxDTO storageInBoxDTO) {
        log.debug("Request to save StorageInBox : {}", storageInBoxDTO);
        StorageInBox storageInBox = storageInBoxMapper.storageInBoxDTOToStorageInBox(storageInBoxDTO);
        storageInBox = storageInBoxRepository.save(storageInBox);
        StorageInBoxDTO result = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);
        return result;
    }

    /**
     *  Get all the storageInBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StorageInBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StorageInBoxes");
        Page<StorageInBox> result = storageInBoxRepository.findAll(pageable);
        return result.map(storageInBox -> storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox));
    }

    /**
     *  Get one storageInBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StorageInBoxDTO findOne(Long id) {
        log.debug("Request to get StorageInBox : {}", id);
        StorageInBox storageInBox = storageInBoxRepository.findOne(id);
        StorageInBoxDTO storageInBoxDTO = storageInBoxMapper.storageInBoxToStorageInBoxDTO(storageInBox);
        return storageInBoxDTO;
    }

    /**
     *  Delete the  storageInBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StorageInBox : {}", id);
        storageInBoxRepository.delete(id);
    }
}
