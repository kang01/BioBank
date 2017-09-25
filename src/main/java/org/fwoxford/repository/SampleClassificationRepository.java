package org.fwoxford.repository;

import org.fwoxford.domain.SampleClassification;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SampleClassification entity.
 */
@SuppressWarnings("unused")
public interface SampleClassificationRepository extends JpaRepository<SampleClassification,Long> {
    /**
     * 不能使用此方法
     * @param sampleClassTypeCode
     * @return
     */
    SampleClassification findBySampleClassificationCode(String sampleClassTypeCode);

}
