package org.fwoxford.repository;

import org.fwoxford.domain.StorageInBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StorageInBox entity.
 */
@SuppressWarnings("unused")
public interface StorageInBoxRepository extends JpaRepository<StorageInBox,Long> {

}
