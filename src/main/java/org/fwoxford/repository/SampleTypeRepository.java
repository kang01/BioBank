package org.fwoxford.repository;

import org.fwoxford.domain.SampleType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SampleType entity.
 */
@SuppressWarnings("unused")
public interface SampleTypeRepository extends JpaRepository<SampleType,Long> {

}
