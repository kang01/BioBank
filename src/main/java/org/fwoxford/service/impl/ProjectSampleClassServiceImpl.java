package org.fwoxford.service.impl;

import org.fwoxford.service.ProjectSampleClassService;
import org.fwoxford.domain.ProjectSampleClass;
import org.fwoxford.repository.ProjectSampleClassRepository;
import org.fwoxford.service.dto.ProjectSampleClassDTO;
import org.fwoxford.service.dto.ProjectSampleClassificationDTO;
import org.fwoxford.service.dto.ProjectSampleTypeDTO;
import org.fwoxford.service.mapper.ProjectSampleClassMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProjectSampleClass.
 */
@Service
@Transactional
public class ProjectSampleClassServiceImpl implements ProjectSampleClassService{

    private final Logger log = LoggerFactory.getLogger(ProjectSampleClassServiceImpl.class);

    private final ProjectSampleClassRepository projectSampleClassRepository;

    private final ProjectSampleClassMapper projectSampleClassMapper;

    public ProjectSampleClassServiceImpl(ProjectSampleClassRepository projectSampleClassRepository, ProjectSampleClassMapper projectSampleClassMapper) {
        this.projectSampleClassRepository = projectSampleClassRepository;
        this.projectSampleClassMapper = projectSampleClassMapper;
    }

    /**
     * Save a projectSampleClass.
     *
     * @param projectSampleClassDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectSampleClassDTO save(ProjectSampleClassDTO projectSampleClassDTO) {
        log.debug("Request to save ProjectSampleClass : {}", projectSampleClassDTO);
        ProjectSampleClass projectSampleClass = projectSampleClassMapper.projectSampleClassDTOToProjectSampleClass(projectSampleClassDTO);
        projectSampleClass = projectSampleClassRepository.save(projectSampleClass);
        ProjectSampleClassDTO result = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);
        return result;
    }

    /**
     *  Get all the projectSampleClasses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectSampleClassDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectSampleClasses");
        Page<ProjectSampleClass> result = projectSampleClassRepository.findAll(pageable);
        return result.map(projectSampleClass -> projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass));
    }

    /**
     *  Get one projectSampleClass by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectSampleClassDTO findOne(Long id) {
        log.debug("Request to get ProjectSampleClass : {}", id);
        ProjectSampleClass projectSampleClass = projectSampleClassRepository.findOne(id);
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);
        return projectSampleClassDTO;
    }

    /**
     *  Delete the  projectSampleClass by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectSampleClass : {}", id);
        projectSampleClassRepository.delete(id);
    }

    @Override
    public List<ProjectSampleTypeDTO> getSampleTypeByProjectId(Long projectId) {
        if(projectId == null){
            throw new BankServiceException("项目ID不能为空！");
        }
        List<Object[]> projectSample =  projectSampleClassRepository.findSampleTypeByProject(projectId);
        List<ProjectSampleTypeDTO> projectSampleTypeDTOList = new ArrayList<>();
        for(int i= 0 ;i<projectSample.size();i++){
            Object[] obj = projectSample.get(i);
            Long sampleTypeId = Long.valueOf(obj[0].toString());
            String sampleTypeName = obj[1].toString();
            ProjectSampleTypeDTO projectSampleTypeDTO = new ProjectSampleTypeDTO();
            projectSampleTypeDTO.setSampleTypeId(sampleTypeId);
            projectSampleTypeDTO.setSampleTypeName(sampleTypeName);
            projectSampleTypeDTOList.add(projectSampleTypeDTO);
        }
        return projectSampleTypeDTOList;
    }

    @Override
    public List<ProjectSampleClassificationDTO> getSampleClassificationByProjectIdAndsampleTypeId(Long projectId, Long sampleTypeId) {
        if(projectId ==null){
            throw new BankServiceException("项目ID不能为空！");
        }
        if(sampleTypeId ==null){
            throw new BankServiceException("样本类型Id不能为空！");
        }
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findByProjectAndSampleTypeId(projectId,sampleTypeId);
        List<ProjectSampleClassificationDTO> projectSampleClassificationDTOS = projectSampleClassMapper.projectSampleClassesToProjectClassificationDTOs(projectSampleClasses);
        return projectSampleClassificationDTOS;
    }

    @Override
    public List<ProjectSampleClass> findByProjectIdAndSampleTypeIdAndSampleClassificationId(Long projectId, Long sampleTypeId, Long sampleClassificationId) {
        if(projectId ==null){
            throw new BankServiceException("项目ID不能为空！");
        }
        if(sampleTypeId==null){
            throw new BankServiceException("样本类型Id不能为空！");
        }
        List<ProjectSampleClass> projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeId(projectId,sampleTypeId);
        return projectSampleClass;
    }
}
