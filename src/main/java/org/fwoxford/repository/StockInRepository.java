package org.fwoxford.repository;

import org.fwoxford.domain.StockIn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockIn entity.
 */
@SuppressWarnings("unused")
public interface StockInRepository extends JpaRepository<StockIn,Long> {

    StockIn findStockInByStockInCode(String stockInCode);

    StockIn findStockInByTranshipId(Long id);

    @Query("select s from StockIn s where s.tranship.transhipCode=?1")
    List<StockIn> findByTranshipCode(String transhipCode);

    @Query("select count(s) from StockIn s where s.tranship.transhipCode=?1")
    int countByTranshipCode(String transhipCode);

    @Query(value = "select t.sample_type_id as sampleTypeId,t.sample_classification_id as sampleClassificationId," +
        " t.sample_type_name  as sampleTypeName," +
        " t.sample_classification_name as sampleClassificationName," +
        "count(t.id) as countOfTube from stock_in_tube t " +
        " left join stock_in_box b on t.stock_in_box_id = b.id " +
        " where b.stock_in_id = ?1 and t.status!='0000'" +
        " group by t.sample_type_id,t.sample_classification_id,t.sample_type_name,t.sample_classification_name",nativeQuery = true)
    List<Object[]> countFrozenTubeGroupBySampleTypeAndClass(Long id);
}
