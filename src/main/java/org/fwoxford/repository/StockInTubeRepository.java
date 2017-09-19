package org.fwoxford.repository;

import org.fwoxford.domain.StockInTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInTube entity.
 */
@SuppressWarnings("unused")
public interface StockInTubeRepository extends JpaRepository<StockInTube,Long> {

    StockInTube findByFrozenTubeId(Long id);

    @Query("select t from StockInTube t where t.stockInBox.id = ?1 and t.status!='0000'")
    List<StockInTube> findByStockInBoxId(Long id);

    List<StockInTube> findByFrozenBoxCode(String frozenBoxCode);

    @Query("select t from StockInTube t where t.frozenBoxCode = ?1 and t.frozenTube.frozenTubeState  in ('2004') and t.status!='0000'")
    List<StockInTube> findByFrozenBoxCodeAndSampleState(String frozenBoxCode);

    @Query("select t from StockInTube t where t.frozenBoxCode = ?1 and  t.status!='0000' and t.stockInBox.stockIn.stockInCode =?2")
    List<StockInTube> findByFrozenBoxCodeAndStockInCode(String frozenBoxCode, String stockInCode);

    @Modifying
    @Query("update StockInTube b set b.status=?1 where b.stockInBox.id = ?2")
    void updateStatusByStockInBoxId(String status, Long id);

    @Query("select count(t) from StockInTube t where t.frozenBoxCode = ?1 and  t.status!='0000' and t.stockInBox.stockIn.stockInCode =?2")
    Long countByFrozenBoxCodeAndStockInCode(String frozenBoxCode, String stockInCode);

    @Query(value = " select * from (" +
        "   select row_number() over(partition by frozen_tube_id order by CREATED_DATE desc) rn, a.* from  stock_in_tube a where sample_code = ?1 and sample_type_code = ?2 and status !='0000'" +
        ") where rn = 1 " , nativeQuery = true)
    StockInTube findBySampleCodeLast(String sampleCode, String sampleTypeCode);
}
