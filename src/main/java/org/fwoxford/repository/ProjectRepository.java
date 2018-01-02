package org.fwoxford.repository;

import org.fwoxford.domain.Project;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query("select t from Project t  where t.status != '0000' order by t.id asc")
    List<Project> findAllProject();

    Project findByProjectCode(String projectCode);

    Project findByIdAndStatus(Long projectId, String valid);
}
