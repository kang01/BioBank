package org.fwoxford.service.impl;

import org.fwoxford.service.FrozenBoxTypeService;
import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.repository.FrozenBoxTypeRepository;
import org.fwoxford.service.dto.FrozenBoxTypeDTO;
import org.fwoxford.service.mapper.FrozenBoxTypeMapper;
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
 * Service Implementation for managing FrozenBoxType.
 */
@Service
@Transactional
public class FrozenBoxTypeServiceImpl implements FrozenBoxTypeService{

    private final Logger log = LoggerFactory.getLogger(FrozenBoxTypeServiceImpl.class);

    private final FrozenBoxTypeRepository frozenBoxTypeRepository;

    private final FrozenBoxTypeMapper frozenBoxTypeMapper;

    public FrozenBoxTypeServiceImpl(FrozenBoxTypeRepository frozenBoxTypeRepository, FrozenBoxTypeMapper frozenBoxTypeMapper) {
        this.frozenBoxTypeRepository = frozenBoxTypeRepository;
        this.frozenBoxTypeMapper = frozenBoxTypeMapper;
    }

    /**
     * Save a frozenBoxType.
     *
     * @param frozenBoxTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenBoxTypeDTO save(FrozenBoxTypeDTO frozenBoxTypeDTO) {
        log.debug("Request to save FrozenBoxType : {}", frozenBoxTypeDTO);
        FrozenBoxType frozenBoxType = frozenBoxTypeMapper.frozenBoxTypeDTOToFrozenBoxType(frozenBoxTypeDTO);
        frozenBoxType = frozenBoxTypeRepository.save(frozenBoxType);
        FrozenBoxTypeDTO result = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);
        return result;
    }

    /**
     *  Get all the frozenBoxTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenBoxTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenBoxTypes");
        Page<FrozenBoxType> result = frozenBoxTypeRepository.findAll(pageable);
        return result.map(frozenBoxType -> frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType));
    }

    /**
     *  Get one frozenBoxType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenBoxTypeDTO findOne(Long id) {
        log.debug("Request to get FrozenBoxType : {}", id);
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findOne(id);
        FrozenBoxTypeDTO frozenBoxTypeDTO = frozenBoxTypeMapper.frozenBoxTypeToFrozenBoxTypeDTO(frozenBoxType);
        return frozenBoxTypeDTO;
    }

    /**
     *  Delete the  frozenBoxType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenBoxType : {}", id);
        frozenBoxTypeRepository.delete(id);
    }

    /**
     * 查詢所有的有效凍存盒類型
     * @return
     */
    @Override
    public List<FrozenBoxTypeDTO> findAllFrozenBoxTypes() {
        List<FrozenBoxType> frozenBoxTypeList = frozenBoxTypeRepository.findAllFrozenBoxTypes();
        List<FrozenBoxTypeDTO> frozenBoxTypeDTOList =  frozenBoxTypeMapper.frozenBoxTypesToFrozenBoxTypeDTOs(frozenBoxTypeList);
        return frozenBoxTypeDTOList;
    }
}
