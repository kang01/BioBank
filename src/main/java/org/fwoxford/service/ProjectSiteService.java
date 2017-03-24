package org.fwoxford.service;

import org.fwoxford.service.dto.ProjectSiteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing ProjectSite.
 */
public interface ProjectSiteService {

    /**
     * Save a projectSite.
     *
     * @param projectSiteDTO the entity to save
     * @return the persisted entity
     */
    ProjectSiteDTO save(ProjectSiteDTO projectSiteDTO);

    /**
     *  Get all the projectSites.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProjectSiteDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" projectSite.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectSiteDTO findOne(Long id);

    /**
     *  Delete the "id" projectSite.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 根据项目ID查询项目组ID信息
     * @param projectId 项目ID
     * @return
     */
    List<ProjectSiteDTO> findAllProjectSitesByProjectId(Long projectId);
}
