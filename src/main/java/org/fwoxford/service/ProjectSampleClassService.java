package org.fwoxford.service;

import org.fwoxford.domain.ProjectSampleClass;
import org.fwoxford.service.dto.ProjectSampleClassDTO;
import org.fwoxford.service.dto.ProjectSampleClassificationDTO;
import org.fwoxford.service.dto.ProjectSampleTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing ProjectSampleClass.
 */
public interface ProjectSampleClassService {

    /**
     * Save a projectSampleClass.
     *
     * @param projectSampleClassDTO the entity to save
     * @return the persisted entity
     */
    ProjectSampleClassDTO save(ProjectSampleClassDTO projectSampleClassDTO);

    /**
     *  Get all the projectSampleClasses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ProjectSampleClassDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" projectSampleClass.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectSampleClassDTO findOne(Long id);

    /**
     *  Delete the "id" projectSampleClass.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<ProjectSampleTypeDTO> getSampleTypeByProjectId(Long projectId);

    List<ProjectSampleClassificationDTO> getSampleClassificationByProjectIdAndsampleTypeId(Long projectId, Long sampleTypeId);

    List<ProjectSampleClass> findByProjectIdAndSampleTypeIdAndSampleClassificationId(Long projectId, Long sampleTypeId, Long sampleClassificationId);

    List<ProjectSampleClassificationDTO> getSampleClassificationByProjectIdsAndsampleTypeId(String projectIds, Long sampleTypeId);
}
