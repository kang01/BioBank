package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.StockInTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInTube entity.
 */
@SuppressWarnings("unused")
public interface StockInTubeRepository extends JpaRepository<StockInTube,Long> {

    StockInTube findByFrozenTubeId(Long id);

    @Query("select t from StockInTube t where t.stockInBox.id = ?1 and t.status!='"+Constants.INVALID+"'")
    List<StockInTube> findByStockInBoxId(Long id);
    //此方法仅可以给导入数据时使用，根据冻存盒获取入库管信息，获取到的是多次记录
    List<StockInTube> findByFrozenBoxCode(String frozenBoxCode);

    @Query("select t from StockInTube t where t.frozenBoxCode = ?1 and t.frozenTube.frozenTubeState = '"+ Constants.FROZEN_BOX_STOCKED+"' and t.status!='"+Constants.INVALID+"'")
    List<StockInTube> findByFrozenBoxCodeAndSampleState(String frozenBoxCode);

    @Query("select t from StockInTube t where t.frozenBoxCode = ?1 and  t.status!='"+Constants.INVALID+"' and t.stockInBox.stockIn.stockInCode =?2")
    List<StockInTube> findByFrozenBoxCodeAndStockInCode(String frozenBoxCode, String stockInCode);

    @Modifying
    @Query("update StockInTube b set b.status=?1 where b.stockInBox.id = ?2")
    void updateStatusByStockInBoxId(String status, Long id);

    @Query("select count(t) from StockInTube t where t.frozenBoxCode = ?1 and  t.status!='"+Constants.INVALID+"' and t.stockInBox.stockIn.stockInCode =?2")
    Long countByFrozenBoxCodeAndStockInCode(String frozenBoxCode, String stockInCode);

    @Query(value = " select * from (" +
        "   select row_number() over(partition by frozen_tube_id order by CREATED_DATE desc) rn, a.* from  stock_in_tube a where sample_code = ?1 and sample_type_code = ?2 and status !='"+Constants.INVALID+"'" +
        ") where rn = 1 " , nativeQuery = true)
    StockInTube findBySampleCodeLast(String sampleCode, String sampleTypeCode);

    @Query(value = "SELECT B.FROZEN_BOX_ID,COUNT(T.ID) FROM STOCK_IN_TUBE T LEFT JOIN (SELECT B.ID,B.FROZEN_BOX_ID FROM STOCK_IN_BOX B WHERE B.FROZEN_BOX_ID IN ?1 AND B.STOCK_IN_CODE = ?2) B ON T.STOCK_IN_BOX_ID = B.ID WHERE B.ID  IS NOT NULL GROUP BY B.FROZEN_BOX_ID",nativeQuery = true)
    List<Object[]> countByFrozenBoxIdsAndStockInCodeGroupByFrozenBoxId(List<Long> boxIds, String stockInCode);

    @Query("select t from StockInTube t where t.frozenBoxCode in ?1 and  t.status!='"+Constants.INVALID+"' and t.stockInBox.stockIn.stockInCode =?2")
    List<StockInTube> findByFrozenBoxCodeInAndStockInCode(List<String> boxCodeStr, String stockInCode);

    @Modifying
    @Query("update StockInTube b set b.status='"+Constants.INVALID+"' where b.stockInBox.id = ?1 and id not in ?2")
    void updateStatusByStockInBoxIdAndIdNotIn(Long id, List<Long> tubeIds);

    @Query("select count(t.id) from StockInTube t where t.stockInBox.id = ?1 and  t.status!='"+Constants.INVALID+"'")
    Long countByStockInBoxId(Long id);
}
