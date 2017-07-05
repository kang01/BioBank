package org.fwoxford.service.impl;

import org.fwoxford.service.ProjectSiteService;
import org.fwoxford.domain.ProjectSite;
import org.fwoxford.repository.ProjectSiteRepository;
import org.fwoxford.service.dto.ProjectSiteDTO;
import org.fwoxford.service.mapper.ProjectSiteMapper;
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
 * Service Implementation for managing ProjectSite.
 */
@Service
@Transactional
public class ProjectSiteServiceImpl implements ProjectSiteService{

    private final Logger log = LoggerFactory.getLogger(ProjectSiteServiceImpl.class);

    private final ProjectSiteRepository projectSiteRepository;

    private final ProjectSiteMapper projectSiteMapper;

    public ProjectSiteServiceImpl(ProjectSiteRepository projectSiteRepository, ProjectSiteMapper projectSiteMapper) {
        this.projectSiteRepository = projectSiteRepository;
        this.projectSiteMapper = projectSiteMapper;
    }

    /**
     * Save a projectSite.
     *
     * @param projectSiteDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectSiteDTO save(ProjectSiteDTO projectSiteDTO) {
        log.debug("Request to save ProjectSite : {}", projectSiteDTO);
        ProjectSite projectSite = projectSiteMapper.projectSiteDTOToProjectSite(projectSiteDTO);
        projectSite = projectSiteRepository.save(projectSite);
        ProjectSiteDTO result = projectSiteMapper.projectSiteToProjectSiteDTO(projectSite);
        return result;
    }

    /**
     *  Get all the projectSites.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectSiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectSites");
        Page<ProjectSite> result = projectSiteRepository.findAll(pageable);
        return result.map(projectSite -> projectSiteMapper.projectSiteToProjectSiteDTO(projectSite));
    }

    /**
     *  Get one projectSite by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectSiteDTO findOne(Long id) {
        log.debug("Request to get ProjectSite : {}", id);
        ProjectSite projectSite = projectSiteRepository.findOne(id);
        ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(projectSite);
        return projectSiteDTO;
    }

    /**
     *  Delete the  projectSite by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectSite : {}", id);
        projectSiteRepository.delete(id);
    }

    /**
     * 根据项目ID查询项目组ID信息
     * @param projectId 项目ID
     * @return
     */
    @Override
    public List<ProjectSiteDTO> findAllProjectSitesByProjectId(Long projectId) {
        log.debug("Request to get ProjectSite : {}", projectId);
        List<ProjectSite> projectSites = projectSiteRepository.findAllProjectSitesByProjectId(projectId);
        List<ProjectSiteDTO> projectSiteDTOs = new ArrayList<ProjectSiteDTO>();
        for(ProjectSite p :projectSites){
            ProjectSiteDTO projectSiteDTO = projectSiteMapper.projectSiteToProjectSiteDTO(p);
            projectSiteDTO.setProjectSiteName(projectSiteDTO.getProjectSiteCode()+","+projectSiteDTO.getProjectSiteName());
            projectSiteDTOs.add(projectSiteDTO);
        }
        return projectSiteDTOs;
    }
}
