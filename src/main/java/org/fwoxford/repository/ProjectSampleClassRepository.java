package org.fwoxford.repository;

import org.fwoxford.domain.ProjectSampleClass;
import org.fwoxford.service.dto.ProjectSampleTypeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectSampleClass entity.
 */
@SuppressWarnings("unused")
public interface ProjectSampleClassRepository extends JpaRepository<ProjectSampleClass,Long> {

    @Query(value="select t.sample_type_id as sampleTypeId,s.sample_type_name as sampleTypeName from project_sample  t\n" +
        "left join sample_type s on t.sample_type_id=s.id where t.project_id=?1 group by t.sample_type_id,s.sample_type_name",nativeQuery = true)
    List<Object[]> findSampleTypeByProject(Long projectId);

    @Query("select p from ProjectSampleClass p where p.project.id=?1 and p.sampleType.id=?2")
    List<ProjectSampleClass> findByProjectAndSampleTypeId(Long projectId, Long sampleTypeId);

    ProjectSampleClass findByProjectIdAndSampleTypeIdAndSampleClassificationId(Long projectId, Long sampleTypeId, Long sampleClassificationId);

    List<ProjectSampleClass> findByProjectIdAndSampleTypeId(Long projectId, Long sampleTypeId);

    int countByProjectIdAndSampleTypeId(Long projectId, Long sampleTypeId);

    List<ProjectSampleClass> findSampleTypeByProjectCode(String projectCode);

    List<ProjectSampleClass> findByProjectIdInAndSampleTypeId(List<Long> projectIdList, Long sampleTypeId);

    @Query("select p from ProjectSampleClass p where p.projectCode=?1 and p.sampleClassification.sampleClassificationCode=?2 and columnsNumber is null ")
    List<ProjectSampleClass> findSampleTypeByProjectAndSampleClassification(String projectCode, String sampleClassificationCode);
}
