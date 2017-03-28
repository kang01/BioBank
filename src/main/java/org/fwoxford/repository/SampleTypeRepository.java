package org.fwoxford.repository;

import org.fwoxford.domain.SampleType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SampleType entity.
 */
@SuppressWarnings("unused")
public interface SampleTypeRepository extends JpaRepository<SampleType,Long> {
    @Query("select t from SampleType t where t.status !='0000' order by t.sampleTypeCode asc")
    List<SampleType> findAllSampleTypes();
}
