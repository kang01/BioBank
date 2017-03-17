package org.fwoxford.service.impl;

import org.fwoxford.service.ProjectRelateService;
import org.fwoxford.domain.ProjectRelate;
import org.fwoxford.repository.ProjectRelateRepository;
import org.fwoxford.service.dto.ProjectRelateDTO;
import org.fwoxford.service.mapper.ProjectRelateMapper;
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
 * Service Implementation for managing ProjectRelate.
 */
@Service
@Transactional
public class ProjectRelateServiceImpl implements ProjectRelateService{

    private final Logger log = LoggerFactory.getLogger(ProjectRelateServiceImpl.class);
    
    private final ProjectRelateRepository projectRelateRepository;

    private final ProjectRelateMapper projectRelateMapper;

    public ProjectRelateServiceImpl(ProjectRelateRepository projectRelateRepository, ProjectRelateMapper projectRelateMapper) {
        this.projectRelateRepository = projectRelateRepository;
        this.projectRelateMapper = projectRelateMapper;
    }

    /**
     * Save a projectRelate.
     *
     * @param projectRelateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectRelateDTO save(ProjectRelateDTO projectRelateDTO) {
        log.debug("Request to save ProjectRelate : {}", projectRelateDTO);
        ProjectRelate projectRelate = projectRelateMapper.projectRelateDTOToProjectRelate(projectRelateDTO);
        projectRelate = projectRelateRepository.save(projectRelate);
        ProjectRelateDTO result = projectRelateMapper.projectRelateToProjectRelateDTO(projectRelate);
        return result;
    }

    /**
     *  Get all the projectRelates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectRelateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectRelates");
        Page<ProjectRelate> result = projectRelateRepository.findAll(pageable);
        return result.map(projectRelate -> projectRelateMapper.projectRelateToProjectRelateDTO(projectRelate));
    }

    /**
     *  Get one projectRelate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectRelateDTO findOne(Long id) {
        log.debug("Request to get ProjectRelate : {}", id);
        ProjectRelate projectRelate = projectRelateRepository.findOne(id);
        ProjectRelateDTO projectRelateDTO = projectRelateMapper.projectRelateToProjectRelateDTO(projectRelate);
        return projectRelateDTO;
    }

    /**
     *  Delete the  projectRelate by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectRelate : {}", id);
        projectRelateRepository.delete(id);
    }
}
