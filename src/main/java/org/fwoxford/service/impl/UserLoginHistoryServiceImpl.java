package org.fwoxford.service.impl;

import org.fwoxford.service.UserLoginHistoryService;
import org.fwoxford.domain.UserLoginHistory;
import org.fwoxford.repository.UserLoginHistoryRepository;
import org.fwoxford.service.dto.UserLoginHistoryDTO;
import org.fwoxford.service.mapper.UserLoginHistoryMapper;
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
 * Service Implementation for managing UserLoginHistory.
 */
@Service
@Transactional
public class UserLoginHistoryServiceImpl implements UserLoginHistoryService{

    private final Logger log = LoggerFactory.getLogger(UserLoginHistoryServiceImpl.class);
    
    private final UserLoginHistoryRepository userLoginHistoryRepository;

    private final UserLoginHistoryMapper userLoginHistoryMapper;

    public UserLoginHistoryServiceImpl(UserLoginHistoryRepository userLoginHistoryRepository, UserLoginHistoryMapper userLoginHistoryMapper) {
        this.userLoginHistoryRepository = userLoginHistoryRepository;
        this.userLoginHistoryMapper = userLoginHistoryMapper;
    }

    /**
     * Save a userLoginHistory.
     *
     * @param userLoginHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserLoginHistoryDTO save(UserLoginHistoryDTO userLoginHistoryDTO) {
        log.debug("Request to save UserLoginHistory : {}", userLoginHistoryDTO);
        UserLoginHistory userLoginHistory = userLoginHistoryMapper.userLoginHistoryDTOToUserLoginHistory(userLoginHistoryDTO);
        userLoginHistory = userLoginHistoryRepository.save(userLoginHistory);
        UserLoginHistoryDTO result = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);
        return result;
    }

    /**
     *  Get all the userLoginHistories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserLoginHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserLoginHistories");
        Page<UserLoginHistory> result = userLoginHistoryRepository.findAll(pageable);
        return result.map(userLoginHistory -> userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory));
    }

    /**
     *  Get one userLoginHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserLoginHistoryDTO findOne(Long id) {
        log.debug("Request to get UserLoginHistory : {}", id);
        UserLoginHistory userLoginHistory = userLoginHistoryRepository.findOne(id);
        UserLoginHistoryDTO userLoginHistoryDTO = userLoginHistoryMapper.userLoginHistoryToUserLoginHistoryDTO(userLoginHistory);
        return userLoginHistoryDTO;
    }

    /**
     *  Delete the  userLoginHistory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserLoginHistory : {}", id);
        userLoginHistoryRepository.delete(id);
    }
}
