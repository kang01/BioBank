package org.fwoxford.repository;

import org.fwoxford.domain.PersistentToken;
import org.fwoxford.domain.User;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

    PersistentToken findOneByUser(User user);
    PersistentToken findOneBySeries(String series);
}
