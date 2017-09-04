package org.fwoxford.repository;

import org.fwoxford.domain.ProjectSite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectSite entity.
 */
@SuppressWarnings("unused")
public interface ProjectSiteRepository extends JpaRepository<ProjectSite,Long> {
    @Query("select t from ProjectSite t left join ProjectRelate r on t.id=r.projectSite.id where r.project.id = ?1 order by t.projectSiteCode asc")
    List<ProjectSite> findAllProjectSitesByProjectId(Long projectId);

    ProjectSite findByProjectSiteCode(String projectSiteCode);

    @Query("select t.province,t.city from ProjectSite t group by t.province,t.city")
    List<Object[]> findAllGroupByProvinceAndCity();
}
