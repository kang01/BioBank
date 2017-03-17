package org.fwoxford.service;

import org.fwoxford.service.dto.ProjectRelateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing ProjectRelate.
 */
public interface ProjectRelateService {

    /**
     * Save a projectRelate.
     *
     * @param projectRelateDTO the entity to save
     * @return the persisted entity
     */
    ProjectRelateDTO save(ProjectRelateDTO projectRelateDTO);

    /**
     *  Get all the projectRelates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProjectRelateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" projectRelate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectRelateDTO findOne(Long id);

    /**
     *  Delete the "id" projectRelate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
