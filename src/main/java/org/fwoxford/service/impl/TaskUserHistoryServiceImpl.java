package org.fwoxford.service.impl;

import org.fwoxford.service.TaskUserHistoryService;
import org.fwoxford.domain.TaskUserHistory;
import org.fwoxford.repository.TaskUserHistoryRepository;
import org.fwoxford.service.dto.TaskUserHistoryDTO;
import org.fwoxford.service.mapper.TaskUserHistoryMapper;
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
 * Service Implementation for managing TaskUserHistory.
 */
@Service
@Transactional
public class TaskUserHistoryServiceImpl implements TaskUserHistoryService {

    private final Logger log = LoggerFactory.getLogger(TaskUserHistoryServiceImpl.class);

    private final TaskUserHistoryRepository taskUserHistoryRepository;

    private final TaskUserHistoryMapper taskUserHistoryMapper;

    public TaskUserHistoryServiceImpl(TaskUserHistoryRepository taskUserHistoryRepository, TaskUserHistoryMapper taskUserHistoryMapper) {
        this.taskUserHistoryRepository = taskUserHistoryRepository;
        this.taskUserHistoryMapper = taskUserHistoryMapper;
    }

    /**
     * Save a taskUserHistory.
     *
     * @param taskUserHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TaskUserHistoryDTO save(TaskUserHistoryDTO taskUserHistoryDTO) {
        log.debug("Request to save TaskUserHistory : {}", taskUserHistoryDTO);
        TaskUserHistory taskUserHistory = taskUserHistoryMapper.taskUserHistoryDTOToTaskUserHistory(taskUserHistoryDTO);
        taskUserHistory = taskUserHistoryRepository.save(taskUserHistory);
        TaskUserHistoryDTO result = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);
        return result;
    }

    /**
     *  Get all the taskUserHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaskUserHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskUserHistories");
        Page<TaskUserHistory> result = taskUserHistoryRepository.findAll(pageable);
        return result.map(taskUserHistory -> taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory));
    }

    /**
     *  Get one taskUserHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TaskUserHistoryDTO findOne(Long id) {
        log.debug("Request to get TaskUserHistory : {}", id);
        TaskUserHistory taskUserHistory = taskUserHistoryRepository.findOne(id);
        TaskUserHistoryDTO taskUserHistoryDTO = taskUserHistoryMapper.taskUserHistoryToTaskUserHistoryDTO(taskUserHistory);
        return taskUserHistoryDTO;
    }

    /**
     *  Delete the  taskUserHistory by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskUserHistory : {}", id);
        taskUserHistoryRepository.delete(id);
    }
}
