package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.service.CheckTypeService;
import org.fwoxford.domain.CheckType;
import org.fwoxford.repository.CheckTypeRepository;
import org.fwoxford.service.dto.CheckTypeDTO;
import org.fwoxford.service.mapper.CheckTypeMapper;
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
 * Service Implementation for managing CheckType.
 */
@Service
@Transactional
public class CheckTypeServiceImpl implements CheckTypeService{

    private final Logger log = LoggerFactory.getLogger(CheckTypeServiceImpl.class);

    private final CheckTypeRepository checkTypeRepository;

    private final CheckTypeMapper checkTypeMapper;

    public CheckTypeServiceImpl(CheckTypeRepository checkTypeRepository, CheckTypeMapper checkTypeMapper) {
        this.checkTypeRepository = checkTypeRepository;
        this.checkTypeMapper = checkTypeMapper;
    }

    /**
     * Save a checkType.
     *
     * @param checkTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CheckTypeDTO save(CheckTypeDTO checkTypeDTO) {
        log.debug("Request to save CheckType : {}", checkTypeDTO);
        CheckType checkType = checkTypeMapper.checkTypeDTOToCheckType(checkTypeDTO);
        checkType = checkTypeRepository.save(checkType);
        CheckTypeDTO result = checkTypeMapper.checkTypeToCheckTypeDTO(checkType);
        return result;
    }

    /**
     *  Get all the checkTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CheckTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CheckTypes");
        Page<CheckType> result = checkTypeRepository.findAll(pageable);
        return result.map(checkType -> checkTypeMapper.checkTypeToCheckTypeDTO(checkType));
    }

    /**
     *  Get one checkType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CheckTypeDTO findOne(Long id) {
        log.debug("Request to get CheckType : {}", id);
        CheckType checkType = checkTypeRepository.findOne(id);
        CheckTypeDTO checkTypeDTO = checkTypeMapper.checkTypeToCheckTypeDTO(checkType);
        return checkTypeDTO;
    }

    /**
     *  Delete the  checkType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CheckType : {}", id);
        checkTypeRepository.delete(id);
    }

    @Override
    public List<CheckTypeDTO> findAllCheckTypeList() {
        List<CheckType> checkTypes = checkTypeRepository.findByStatus(Constants.VALID);
        return checkTypeMapper.checkTypesToCheckTypeDTOs(checkTypes);
    }
}
