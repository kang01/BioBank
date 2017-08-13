package org.fwoxford.repository;

import org.fwoxford.domain.Attachment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Attachment entity.
 */
@SuppressWarnings("unused")
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

    @Query("select a from Attachment a where a.businessId =?1 and a.status!='0000'")
    List<Attachment> findByBusinessId(Long id);
}
