package org.fwoxford.repository;

import org.fwoxford.domain.StockOutFiles;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutFiles entity.
 */
@SuppressWarnings("unused")
public interface StockOutFilesRepository extends JpaRepository<StockOutFiles,Long> {

}
