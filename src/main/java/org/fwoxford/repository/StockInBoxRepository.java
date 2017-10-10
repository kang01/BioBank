package org.fwoxford.repository;

import org.fwoxford.domain.StockInBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data JPA repository for the StockInBox entity.
 */
@SuppressWarnings("unused")
public interface StockInBoxRepository extends JpaRepository<StockInBox,Long> {
    @Modifying
    @Query("update StockInBox t set t.status=?2 where t.stockInCode=?1")
    void updateByStockCode(String stockInCode, String status);

    List<StockInBox> findStockInBoxByStockInCode(String stockInCode);

    StockInBox findStockInBoxByStockInCodeAndFrozenBoxCode(String stockInCode, String frozenBoxCode);

    @Modifying
    @Query("update StockInBox t set t.status=?3 where t.stockInCode=?1 and t.frozenBoxCode=?2")
    void updateByStockCodeAndFrozenBoxCode(String stockInCode, String boxCode, String status);

    Page<StockInBox> findStockInBoxPageByStockInCode(String stockInCode, Pageable pageable);

    Long countByStockInCode(String stockInCode);

    @Query("select t from StockInBox t where t.frozenBoxCode =  ?1 and t.status = '2004'")
    List<StockInBox> findByFrozenBoxCode(String frozenBoxCode);

    Long countStockInBoxByStockInCodeAndFrozenBoxCode(String stockInCode, String frozenBoxCode);

    List<StockInBox> findStockInBoxByStockInCodeAndStatus(String stockInCode, String status);
    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from stock_in_box f  " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_classification_id in ?3" +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc",nativeQuery = true)
    List<StockInBox> findIncompleteFrozenBoxBySampleClassificationIdInAllStock(String frozenBoxCode, Long id, ArrayList<Long> longs, Long id1, String frozenBoxStocked);

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from stock_in_box f " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_type_id=?3 " +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc",nativeQuery = true)
    List<StockInBox> findIncompleteFrozenBoxBySampleTypeIdInAllStock(String frozenBoxCode, Long id, Long sampleTypeId, Long id1, String frozenBoxStocked);
}
