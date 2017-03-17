package org.fwoxford.repository;

import org.fwoxford.domain.StorageIn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StorageIn entity.
 */
@SuppressWarnings("unused")
public interface StorageInRepository extends JpaRepository<StorageIn,Long> {

}
