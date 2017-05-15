package org.fwoxford.repository;

import org.fwoxford.domain.Delegate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Delegate entity.
 */
@SuppressWarnings("unused")
public interface DelegateRepository extends JpaRepository<Delegate,Long> {

}
