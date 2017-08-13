package org.fwoxford.repository;

import org.fwoxford.domain.StockOutFiles;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutFiles entity.
 */
@SuppressWarnings("unused")
public interface StockOutFilesRepository extends JpaRepository<StockOutFiles,Long> {

    @Modifying
    @Query("update StockOutFiles s set s.status ='0000' where s.id=?1")
    void updateStatusById(Long bigId);
}
