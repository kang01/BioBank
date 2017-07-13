package org.fwoxford.repository;

import io.swagger.models.auth.In;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;

import org.fwoxford.service.dto.response.FrozenTubeHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Map;

/**
 * Spring Data JPA repository for the FrozenTube entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeRepository extends JpaRepository<FrozenTube,Long> {

    @Query("select t from FrozenTube t where t.frozenBox.id = ?1 and t.status!='0000' and t.frozenBox.status !='2005'")
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBoxCode = ?1 and t.status!='0000' and t.frozenBox.status !='2005'")
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);

    @Query("select count(t) from FrozenTube t where t.frozenBoxCode = ?1 and t.status!='0000' and t.frozenBox.status !='2005'")
    Long countFrozenTubeListByBoxCode(String frozenBoxCode);

    List<FrozenTube> findFrozenTubeListByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select t.frozen_box_code as frozenBoxCode,count(t.frozen_box_code) as sampleNumber \n" +
        "from frozen_tube t where t.frozen_box_code in ?1 and t.status!='0000' and t.frozenBox.status !='2005' group by t.frozen_box_code \n" +
        " order by sampleNumber asc,t.frozen_box_code desc " ,nativeQuery = true)
    List<Object[]> countSampleNumberByfrozenBoxList(List<String> frozenBoxList);

    int countByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select count(*) from frozen_tube t where t.frozen_box_id in ?1 and t.status=?2" ,nativeQuery = true)
    int countByFrozenBoxCodeStrAndStatus(List<Long> boxIds, String status);

    @Query(value = "select count(count(case when t.sample_code is not null THEN t.sample_code ELSE t.sample_temp_code end)) from frozen_tube t\n" +
        " where t.frozen_box_id in ?1 and t.status!='0000'\n" +
        " GROUP BY case when t.sample_code is not null THEN t.sample_code ELSE t.sample_temp_code end" ,nativeQuery = true)
    int countByFrozenBoxCodeStrAndGroupBySampleCode(List<Long> boxIds);

    @Query(value = "select t.* from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id" +
        " left join sample_type st on t.sample_type_id = st.id " +
        " where b.status = '2004' and (t.sample_code = ?1 or t.sample_temp_code =?1) " +
        " and st.sample_type_code=?2 and t.status !='0000'" +
        " and t.id not in (select f.frozen_tube_id from stock_out_req_frozen_tube f where f.stock_out_requirement_id =?3 and f.status='1301')" +
        " and t.project_id in ?4"
        ,nativeQuery = true)
    List<FrozenTube> findBySampleCodeAndSampleTypeCode(String appointedSampleCode, String appointedSampleType, Long id, List<Long> projectIds);

    @Query("select t from FrozenTube t\n" +
        "  left join t.frozenBox \n"
        + " left join StockOutReqFrozenTube f on t.id = f.frozenTube.id and f.status='1301' \n"
        + " where t.status='3001' and t.frozenBox is not null and t.frozenBox.status = '2004' and t.status!='0000'\n"
        + " and f.frozenTube.id is null \n"
        + " and (?1 is null or t.sampleType.id = ?1)\n"
        + " and (?2 is null or t.sampleClassification.id = ?2)\n"
        + " and (?3 is null or t.frozenTubeType.id = ?3)\n"
        + " and (?4 is null or t.diseaseType  = ?4)\n"
        + " and (?5 is null or t.gender = ?5)\n"
        + " and (?6 is null or ?6 is false or t.isBloodLipid  = ?6)\n"
        + " and (?7 is null or ?7 is false or t.isHemolysis  = ?7)\n"
        + " and (?8 is null or t.age >= ?8 )\n"
        + " and (?9 is null or t.age <= ?9 )\n"
    )
    List<FrozenTube> findByRequirement(Long sampleTypeId, Long samplyClassificationId, Long frozenTubeTypeId,
                                       String diseaseType, String sex, Boolean isBloodLipid, Boolean isHemolysis, Integer ageMin, Integer ageMax);

    List<FrozenTube> findBySampleCode(String sampleCode);

    @Query(value = "select  id,\n" +
        "cast(project_code as varchar2(255)) as projectCode,\n" +
        "tranship_id as transhipId,\n" +
        "cast(tranship_code as varchar2(255)) as transhipCode,\n" +
        "stock_in_id as stockInId,\n" +
        "cast(stock_in_code as varchar2(255)) as stockInCode,\n" +
        "stock_out_task_id as stockOutTaskId," +
        "cast(stock_out_task_code as varchar2(255)) as stockOutTaskCode,\n" +
        "handover_id as handoverId," +
        "cast(handover_code as varchar2(255)) as handoverCode,\n" +
        "cast(sample_code as varchar2(255)) as sampleCode,\n" +
        "cast(type as integer) as type,cast(status as varchar2(255)) as status,\n" +
        "cast(frozen_box_code as varchar2(255)) as frozenBoxCode,\n" +
        "cast(tube_rows as varchar2(255)) as tubeRows,\n" +
        "cast(tube_columns as varchar2(255)) as tubeColumns,\n" +
        "operate_time as operateTime\n " +
        "from\n" +
        "(\n" +
        "select tranship.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "tran.id as tranship_id, "+
        "N'' as tranship_code,\n" +
        "null as stock_in_id, "+
        "N'' as stock_in_code,\n" +
        "null as stock_out_task_id,\n" +
        "N'' as stock_out_task_code,\n" +
        "null as handover_id,\n" +
        "N'' as handover_code,\n" +
        "tube.sample_code,101 as type,\n" +
        "tranship.status,box.frozen_box_code,tranship.rows_in_tube as tube_rows,tranship.columns_in_tube as tube_columns\n" +
        " ,tran.tranship_date as operate_time\n" +
        "from tranship_tube tranship\n" +
        "left join  frozen_tube tube on tranship.frozen_tube_id = tube.id\n" +
        "left join tranship_box tbox on tranship.tranship_box_id = tbox.id\n" +
        "left join  tranship tran on tbox.tranship_id = tran.id\n" +
        "left join frozen_box box on tbox.frozen_box_id = box.id \n" +
        "where tube.sample_code=?1 \n" +
        "UNION\n" +
        "select stockIn.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "null as tranship_id, "+
        "N'' as tranship_code,\n" +
        "s.id as stock_in_id, "+
        "s.stock_in_code,\n" +
        "null as stock_out_task_id,\n" +
        "N'' as stock_out_task_code,\n" +
        "null as handover_id,\n" +
        "N'' as handover_code,\n" +
        "tube.sample_code,102 as type,\n" +
        "stockIn.status,box.frozen_box_code,stockIn.tube_rows,stockIn.tube_columns\n" +
        " ,s.stock_in_date as operate_time\n" +
        " from stock_in_tube stockIn\n" +
        "left join frozen_tube tube on stockIn.frozen_tube_id = tube.id\n" +
        "left join stock_in_box sbox on stockIn.stock_in_box_id = sbox.id\n" +
        "left join  stock_in s on sbox.stock_in_id = s.id\n" +
        "left join frozen_box box on sbox.frozen_box_id = box.id \n" +
        "where tube.sample_code=?1\n" +
        "UNION\n" +
        "select  stockOut.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "null as tranship_id,"+
        "N'' as tranship_code,\n" +
        "null as stock_in_id,"+
        "N'' as stock_in_code,\n" +
        "task.id as stock_out_task_id,\n" +
        "task.stock_out_task_code,\n" +
        "null as handover_id,\n" +
        "N'' as handover_code,\n" +
        "tube.sample_code,103 as type,\n" +
        "stockOut.status,box.frozen_box_code,stockOut.tube_rows,stockOut.tube_columns\n" +
        " ,task.stock_out_date as operate_time\n" +
        " from stock_out_box_tube stockOut \n" +
        "left join  frozen_tube tube on stockOut.frozen_tube_id = tube.id\n" +
        "left join stock_out_box soutbox on stockOut.stock_out_frozen_box_id = soutbox.id\n" +
        "left join frozen_box box on soutbox.frozen_box_id = box.id\n" +
        "left join  stock_out_task task on soutbox.stock_out_task_id = task.id\n" +
        "where tube.sample_code=?1\n" +
        "UNION\n" +
        "select  stockOut.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "null as tranship_id,"+
        "N'' as tranship_code,\n" +
        "null as stock_in_id,"+
        "N'' as stock_in_code,\n" +
        "null as stock_out_task_id,\n" +
        "N'' as stock_out_task_code,\n" +
        "handover.id as handover_id,\n" +
        "handover.handover_code ,\n" +
        "tube.sample_code,104 as type,\n" +
        "hand.status,box.frozen_box_code,stockOut.tube_rows,stockOut.tube_columns\n" +
        " ,handover.handover_time as operate_time\n" +
        " from stock_out_handover_details hand \n" +
        "left join stock_out_box_tube stockOut on hand.stock_out_box_tube_id = stockOut.id\n" +
        "left join  frozen_tube tube on stockOut.frozen_tube_id = tube.id\n" +
        "left join stock_out_box soutbox on stockOut.stock_out_frozen_box_id = soutbox.id\n" +
        "left join frozen_box box on soutbox.frozen_box_id = box.id\n" +
        "left join  stock_out_handover handover on hand.stock_out_handover_id = handover.id\n" +
        "where tube.sample_code=?1 \n" +
        ")\n" +
        " ORDER BY operate_time,type desc",nativeQuery = true)
    //此方法不能随意更改，尤其是返回参数的顺序
    List<Object[]> findFrozenTubeHistoryListBySample(String sampleCode);

    List<FrozenTube> findBySampleCodeAndProjectCode(String sampleCode, String projectCode);
    @Query(value = "select  id,\n" +
        "cast(project_code as varchar2(255)) as projectCode,\n" +
        "tranship_id as transhipId,\n" +
        "cast(tranship_code as varchar2(255)) as transhipCode,\n" +
        "stock_in_id as stockInId,\n" +
        "cast(stock_in_code as varchar2(255)) as stockInCode,\n" +
        "stock_out_task_id as stockOutTaskId," +
        "cast(stock_out_task_code as varchar2(255)) as stockOutTaskCode,\n" +
        "handover_id as handoverId," +
        "cast(handover_code as varchar2(255)) as handoverCode,\n" +
        "cast(sample_code as varchar2(255)) as sampleCode,\n" +
        "cast(type as integer) as type,cast(status as varchar2(255)) as status,\n" +
        "cast(frozen_box_code as varchar2(255)) as frozenBoxCode,\n" +
        "cast(tube_rows as varchar2(255)) as tubeRows,\n" +
        "cast(tube_columns as varchar2(255)) as tubeColumns,\n" +
        "operate_time as operateTime\n " +
        "from\n" +
        "(\n" +
        "select tranship.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "tran.id as tranship_id, "+
        "N'' as tranship_code,\n" +
        "null as stock_in_id, "+
        "N'' as stock_in_code,\n" +
        "null as stock_out_task_id,\n" +
        "N'' as stock_out_task_code,\n" +
        "null as handover_id,\n" +
        "N'' as handover_code,\n" +
        "tube.sample_code,101 as type,\n" +
        "tranship.status,box.frozen_box_code,tranship.rows_in_tube as tube_rows,tranship.columns_in_tube as tube_columns\n" +
        " ,tran.tranship_date as operate_time\n" +
        "from tranship_tube tranship\n" +
        "left join  frozen_tube tube on tranship.frozen_tube_id = tube.id\n" +
        "left join tranship_box tbox on tranship.tranship_box_id = tbox.id\n" +
        "left join  tranship tran on tbox.tranship_id = tran.id\n" +
        "left join frozen_box box on tbox.frozen_box_id = box.id \n" +
        "where tube.sample_code=?1 \n" +
        "and tube.project_code=?2 \n" +

        "UNION\n" +
        "select stockIn.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "null as tranship_id, "+
        "N'' as tranship_code,\n" +
        "s.id as stock_in_id, "+
        "s.stock_in_code,\n" +
        "null as stock_out_task_id,\n" +
        "N'' as stock_out_task_code,\n" +
        "null as handover_id,\n" +
        "N'' as handover_code,\n" +
        "tube.sample_code,102 as type,\n" +
        "stockIn.status,box.frozen_box_code,stockIn.tube_rows,stockIn.tube_columns\n" +
        " ,s.stock_in_date as operate_time\n" +
        " from stock_in_tube stockIn\n" +
        "left join frozen_tube tube on stockIn.frozen_tube_id = tube.id\n" +
        "left join stock_in_box sbox on stockIn.stock_in_box_id = sbox.id\n" +
        "left join  stock_in s on sbox.stock_in_id = s.id\n" +
        "left join frozen_box box on sbox.frozen_box_id = box.id \n" +
        "where tube.sample_code=?1\n" +
        "and tube.project_code=?2 \n" +

        "UNION\n" +
        "select  stockOut.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "null as tranship_id,"+
        "N'' as tranship_code,\n" +
        "null as stock_in_id,"+
        "N'' as stock_in_code,\n" +
        "task.id as stock_out_task_id,\n" +
        "task.stock_out_task_code,\n" +
        "null as handover_id,\n" +
        "N'' as handover_code,\n" +
        "tube.sample_code,103 as type,\n" +
        "stockOut.status,box.frozen_box_code,stockOut.tube_rows,stockOut.tube_columns\n" +
        " ,task.stock_out_date as operate_time\n" +
        " from stock_out_box_tube stockOut \n" +
        "left join  frozen_tube tube on stockOut.frozen_tube_id = tube.id\n" +
        "left join stock_out_box soutbox on stockOut.stock_out_frozen_box_id = soutbox.id\n" +
        "left join frozen_box box on soutbox.frozen_box_id = box.id\n" +
        "left join  stock_out_task task on soutbox.stock_out_task_id = task.id\n" +
        "where tube.sample_code=?1\n" +
        "and tube.project_code=?2 \n" +

        "UNION\n" +
        "select  stockOut.frozen_tube_id as id,\n" +
        "tube.project_code,\n" +
        "null as tranship_id,"+
        "N'' as tranship_code,\n" +
        "null as stock_in_id,"+
        "N'' as stock_in_code,\n" +
        "null as stock_out_task_id,\n" +
        "N'' as stock_out_task_code,\n" +
        "handover.id as handover_id,\n" +
        "handover.handover_code ,\n" +
        "tube.sample_code,104 as type,\n" +
        "hand.status,box.frozen_box_code,stockOut.tube_rows,stockOut.tube_columns\n" +
        " ,handover.handover_time as operate_time\n" +
        " from stock_out_handover_details hand \n" +
        "left join stock_out_box_tube stockOut on hand.stock_out_box_tube_id = stockOut.id\n" +
        "left join  frozen_tube tube on stockOut.frozen_tube_id = tube.id\n" +
        "left join stock_out_box soutbox on stockOut.stock_out_frozen_box_id = soutbox.id\n" +
        "left join frozen_box box on soutbox.frozen_box_id = box.id\n" +
        "left join  stock_out_handover handover on hand.stock_out_handover_id = handover.id\n" +
        "where tube.sample_code=?1 \n" +
        "and tube.project_code=?2 \n" +
        ")\n" +
        " ORDER BY operate_time desc,type desc",nativeQuery = true)
        //此方法不能随意更改，尤其是返回参数的顺序
    List<Object[]> findFrozenTubeHistoryListBySampleAndProjectCode(String sampleCode, String projectCode);
    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1) and t.project.projectCode = ?2 and t.sampleType.id = ?3 and t.status != ?4")
    List<FrozenTube> findBySampleCodeAndProjectCodeAndSampleTypeIdAndStatusNot(String sampleCode, String projectCode, Long sampleTypeId,String status);

    @Query("select t from FrozenTube t where t.sampleCode =?1  and t.projectCode = ?2 and t.sampleTypeCode =?3 and t.sampleClassification.sampleClassificationCode = ?4 and t.status!='0000'")
    FrozenTube findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(String sampleCode, String projectCode, String sampleTypeCode, String sampleClassTypeCode);


    @Query("select t from FrozenTube t where t.sampleCode =?1  and t.projectCode = ?2 and t.sampleType.id =?3 and t.sampleClassification.id = ?4 and t.status!='0000'")
    List<FrozenTube> findBySampleCodeAndProjectCodeAndSampleTypeIdAndSampleClassitionId(String sampleCode, String projectCode, Long sampleTypeId, Long sampleClassificationId);

    FrozenTube findByFrozenBoxCodeAndTubeRowsAndTubeColumnsAndStatusNot(String frozenBoxCode, String tubeRows, String tubeColumns, String status);
}
