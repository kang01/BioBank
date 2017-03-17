package org.fwoxford.service.impl;

import org.fwoxford.service.RelationsService;
import org.fwoxford.domain.Relations;
import org.fwoxford.repository.RelationsRepository;
import org.fwoxford.service.dto.RelationsDTO;
import org.fwoxford.service.mapper.RelationsMapper;
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
 * Service Implementation for managing Relations.
 */
@Service
@Transactional
public class RelationsServiceImpl implements RelationsService{

    private final Logger log = LoggerFactory.getLogger(RelationsServiceImpl.class);
    
    private final RelationsRepository relationsRepository;

    private final RelationsMapper relationsMapper;

    public RelationsServiceImpl(RelationsRepository relationsRepository, RelationsMapper relationsMapper) {
        this.relationsRepository = relationsRepository;
        this.relationsMapper = relationsMapper;
    }

    /**
     * Save a relations.
     *
     * @param relationsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RelationsDTO save(RelationsDTO relationsDTO) {
        log.debug("Request to save Relations : {}", relationsDTO);
        Relations relations = relationsMapper.relationsDTOToRelations(relationsDTO);
        relations = relationsRepository.save(relations);
        RelationsDTO result = relationsMapper.relationsToRelationsDTO(relations);
        return result;
    }

    /**
     *  Get all the relations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RelationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Relations");
        Page<Relations> result = relationsRepository.findAll(pageable);
        return result.map(relations -> relationsMapper.relationsToRelationsDTO(relations));
    }

    /**
     *  Get one relations by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RelationsDTO findOne(Long id) {
        log.debug("Request to get Relations : {}", id);
        Relations relations = relationsRepository.findOne(id);
        RelationsDTO relationsDTO = relationsMapper.relationsToRelationsDTO(relations);
        return relationsDTO;
    }

    /**
     *  Delete the  relations by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Relations : {}", id);
        relationsRepository.delete(id);
    }
}
