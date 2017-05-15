package org.fwoxford.repository;

import org.fwoxford.domain.StockOutHandoverDetails;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutHandoverDetails entity.
 */
@SuppressWarnings("unused")
public interface StockOutHandoverDetailsRepository extends JpaRepository<StockOutHandoverDetails,Long> {

}
