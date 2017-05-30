package org.fwoxford.repository;

import org.fwoxford.domain.BoxAndTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BoxAndTube entity.
 */
@SuppressWarnings("unused")
public interface BoxAndTubeRepository extends JpaRepository<BoxAndTube,Long> {

}
