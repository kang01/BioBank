package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.service.DelegateService;
import org.fwoxford.domain.Delegate;
import org.fwoxford.repository.DelegateRepository;
import org.fwoxford.service.dto.DelegateDTO;
import org.fwoxford.service.dto.response.DelegateResponse;
import org.fwoxford.service.mapper.DelegateMapper;
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
 * Service Implementation for managing Delegate.
 */
@Service
@Transactional
public class DelegateServiceImpl implements DelegateService{

    private final Logger log = LoggerFactory.getLogger(DelegateServiceImpl.class);

    private final DelegateRepository delegateRepository;

    private final DelegateMapper delegateMapper;

    public DelegateServiceImpl(DelegateRepository delegateRepository, DelegateMapper delegateMapper) {
        this.delegateRepository = delegateRepository;
        this.delegateMapper = delegateMapper;
    }

    /**
     * Save a delegate.
     *
     * @param delegateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DelegateDTO save(DelegateDTO delegateDTO) {
        log.debug("Request to save Delegate : {}", delegateDTO);
        Delegate delegate = delegateMapper.delegateDTOToDelegate(delegateDTO);
        delegate = delegateRepository.save(delegate);
        DelegateDTO result = delegateMapper.delegateToDelegateDTO(delegate);
        return result;
    }

    /**
     *  Get all the delegates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DelegateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Delegates");
        Page<Delegate> result = delegateRepository.findAll(pageable);
        return result.map(delegate -> delegateMapper.delegateToDelegateDTO(delegate));
    }

    /**
     *  Get one delegate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DelegateDTO findOne(Long id) {
        log.debug("Request to get Delegate : {}", id);
        Delegate delegate = delegateRepository.findOne(id);
        DelegateDTO delegateDTO = delegateMapper.delegateToDelegateDTO(delegate);
        return delegateDTO;
    }

    /**
     *  Delete the  delegate by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Delegate : {}", id);
        delegateRepository.delete(id);
    }

    @Override
    public List<DelegateResponse> getAllDelegateList() {
        List<Delegate> delegates = delegateRepository.findByStatusNot(Constants.INVALID);
        return delegateMapper.delegatesToDelegateResponses(delegates);
    }
}
