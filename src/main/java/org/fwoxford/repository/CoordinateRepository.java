package org.fwoxford.repository;

import org.fwoxford.domain.Coordinate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Coordinate entity.
 */
@SuppressWarnings("unused")
public interface CoordinateRepository extends JpaRepository<Coordinate,Long> {

    Coordinate findByProvinceAndCity(String province, String city);
}
