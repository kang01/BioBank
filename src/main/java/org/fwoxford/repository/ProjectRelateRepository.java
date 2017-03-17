package org.fwoxford.repository;

import org.fwoxford.domain.ProjectRelate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectRelate entity.
 */
@SuppressWarnings("unused")
public interface ProjectRelateRepository extends JpaRepository<ProjectRelate,Long> {

}
