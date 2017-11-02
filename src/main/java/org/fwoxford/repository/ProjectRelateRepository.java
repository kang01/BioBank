package org.fwoxford.repository;

import org.fwoxford.domain.ProjectRelate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectRelate entity.
 */
@SuppressWarnings("unused")
public interface ProjectRelateRepository extends JpaRepository<ProjectRelate,Long> {

    ProjectRelate findByProjectIdAndProjectSiteId(Long projectId, Long projectSiteId);

    @Query("select t from ProjectRelate t where t.project.projectCode=?1 and t.projectSite.projectSiteCode=?2")
    ProjectRelate findByProjectCodeAndProjectSiteCode(String projectCode, String projectSiteCode);
}
