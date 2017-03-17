package org.fwoxford.repository;

import org.fwoxford.domain.Relations;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Relations entity.
 */
@SuppressWarnings("unused")
public interface RelationsRepository extends JpaRepository<Relations,Long> {

}
