package org.fwoxford.repository;

import org.fwoxford.domain.ProjectSampleClass;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectSampleClass entity.
 */
@SuppressWarnings("unused")
public interface ProjectSampleClassRepository extends JpaRepository<ProjectSampleClass,Long> {

}
