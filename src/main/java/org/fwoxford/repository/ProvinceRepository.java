package org.fwoxford.repository;

import org.fwoxford.domain.Province;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Province entity.
 */
@SuppressWarnings("unused")
public interface ProvinceRepository extends JpaRepository<Province,Long> {

}
