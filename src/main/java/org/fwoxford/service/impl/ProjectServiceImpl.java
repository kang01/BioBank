package org.fwoxford.service.impl;

import org.fwoxford.service.ProjectService;
import org.fwoxford.domain.Project;
import org.fwoxford.repository.ProjectRepository;
import org.fwoxford.service.dto.ProjectDTO;
import org.fwoxford.service.dto.response.ProjectResponse;
import org.fwoxford.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);
        Project project = projectMapper.projectDTOToProject(projectDTO);
        project = projectRepository.save(project);
        ProjectDTO result = projectMapper.projectToProjectDTO(project);
        return result;
    }

    /**
     *  Get all the projects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        Page<Project> result = projectRepository.findAll(pageable);
        return result.map(project -> projectMapper.projectToProjectDTO(project));
    }

    /**
     *  Get one project by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectDTO findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
        return projectDTO;
    }

    /**
     *  Delete the  project by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.delete(id);
    }

    @Override
    public List<ProjectDTO> findAllProjectDTOs() {
        log.debug("Request to get all Projects");
        List<Project> projects =  projectRepository.findAllProject();
        List<ProjectDTO> projectDTOS = new ArrayList<ProjectDTO>();
        for(Project p : projects){
            ProjectDTO projectDTO = projectMapper.projectToProjectDTO(p);
            projectDTO.setProjectName(projectDTO.getProjectCode()+","+projectDTO.getProjectName());
            projectDTOS.add(projectDTO);
        }
        return projectDTOS;
    }

    @Override
    public List<ProjectResponse> getProjectResponse() {
        List<Project> projects =  projectRepository.findAllProject();
        return projectMapper.projectsToProjectResponses(projects);
    }
}
