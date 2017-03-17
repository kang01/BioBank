package org.fwoxford.repository;

import org.fwoxford.domain.ProjectSite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectSite entity.
 */
@SuppressWarnings("unused")
public interface ProjectSiteRepository extends JpaRepository<ProjectSite,Long> {

}
