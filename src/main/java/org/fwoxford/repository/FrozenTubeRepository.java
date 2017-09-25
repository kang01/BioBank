package org.fwoxford.repository;

import org.fwoxford.domain.FrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTube entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeRepository extends JpaRepository<FrozenTube,Long> {

    @Query("select t from FrozenTube t where t.frozenBox.id = ?1 and t.status!='0000' and t.frozenBox.status !='2090'")
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBox.id in ?1 and t.status!='0000' and t.frozenBox.status !='2090'")
    List<FrozenTube> findFrozenTubeListByBoxIdIn(List<Long> frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBoxCode = ?1 and t.status!='0000' and t.frozenBox.status !='2090'")
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);

    @Query("select count(t) from FrozenTube t where t.frozenBoxCode = ?1 and t.status!='0000' and t.frozenBox.status !='2090'")
    Long countFrozenTubeListByBoxCode(String frozenBoxCode);

    List<FrozenTube> findFrozenTubeListByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select t.frozen_box_code as frozenBoxCode,count(t.frozen_box_code) as sampleNumber \n" +
        "from frozen_tube t where t.frozen_box_code in ?1 and t.status!='0000' and t.frozenBox.status !='2090' group by t.frozen_box_code \n" +
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
        " and t.project_id in ?4 and ROWNUM <=1"
        ,nativeQuery = true)
    List<FrozenTube> findBySampleCodeAndSampleTypeCodeAndRequirementAndProject(String appointedSampleCode, String appointedSampleType, Long id, List<Long> projectIds);

//    @Query("select t from FrozenTube t\n" +
//        "  left join t.frozenBox \n"
//        + " left join StockOutReqFrozenTube f on t.id = f.frozenTube.id and f.status='1301' \n"
//        + " where t.status='3001' and t.frozenBox is not null and t.frozenBox.status = '2004' and t.status!='0000'\n"
//        + " and f.frozenTube.id is null \n"
//        + " and (?1 is null or t.sampleType.id = ?1)\n"
//        + " and (?2 is null or t.sampleClassification.id = ?2)\n"
//        + " and (?3 is null or t.frozenTubeType.id = ?3)\n"
//        + " and (?4 is null or t.diseaseType  = ?4)\n"
//        + " and (?5 is null or t.gender = ?5)\n"
//        + " and (?6 is null or ?6 is false or t.isBloodLipid  = ?6)\n"
//        + " and (?7 is null or ?7 is false or t.isHemolysis  = ?7)\n"
//        + " and (?8 is null or t.age >= ?8 )\n"
//        + " and (?9 is null or t.age <= ?9 )\n"
//        + " order by t.frozenBox.frozenBoxCode asc\n"
//    )
    @Query(value = "select frozentube0_.* from frozen_tube frozentube0_  " +
        " left outer join stock_out_req_frozen_tube stockoutre2_ on (frozentube0_.id=stockoutre2_.frozen_tube_id and stockoutre2_.status='1301') " +
        " where frozentube0_.status='3001'" +
        " and frozentube0_.frozen_tube_state='2004'  " +
        " and (stockoutre2_.frozen_tube_id is null) " +
        " and (?1 = 0 or frozentube0_.sample_type_id=?1) " +
        " and (?2 = 0 or frozentube0_.sample_classification_id=?2)  " +
        " and (?3 = 0 or frozentube0_.frozen_tube_type_id=?3) " +
        " and (?4 is null or frozentube0_.disease_type=?4) " +
        " and (?5 is null or frozentube0_.gender=?5) " +
        " and (?6 = 0 or frozentube0_.is_blood_lipid=?6) " +
        " and (?7 = 0 or frozentube0_.is_hemolysis=?7) " +
        " and (?8 = 0 or frozentube0_.age>=?8) " +
        " and (?9 = 0 or frozentube0_.age<=?9) " +
        " order by frozentube0_.frozen_box_code,frozentube0_.tube_rows,LPAD(frozentube0_.tube_columns,2) asc",nativeQuery = true)
    List<FrozenTube> findByRequirement(Integer sampleTypeId, Integer samplyClassificationId, Integer frozenTubeTypeId,
                                       String diseaseType, String sex, Integer isBloodLipid, Integer isHemolysis, Integer ageMin, Integer ageMax);

    @Query(value = "SELECT rt.FROZEN_TUBE_ID FROM STOCK_OUT_REQ_FROZEN_TUBE rt " +
        " LEFT OUTER JOIN STOCK_OUT_PLAN_TUBE pt ON pt.STOCK_OUT_REQ_FROZEN_TUBE_ID = rt.id AND pt.STATUS='1503'" +
        " WHERE pt.id IS NULL",nativeQuery = true)
    List<Object[]> findAllStockOutFrozenTube();

    @Query(value = "select t.id,t.project_id,t.frozen_box_id,t.tube_rows,LPAD(t.tube_columns,2) as tube_columns,memo from frozen_tube t   " +
        " where (?1 = 0 or t.sample_type_id=?1) " +
        " and (?2 = 0 or t.sample_classification_id=?2)  " +
        " and (?3 = 0 or t.frozen_tube_type_id=?3) " +
        " and (?4 is null or t.disease_type=?4) " +
        " and (?5 is null or t.gender=?5) " +
        " and (?6 = 0 or t.is_blood_lipid=?6) " +
        " and (?7 = 0 or t.is_hemolysis=?7) " +
        " and (?8 = 0 or t.age>=?8) " +
        " and (?9 = 0 or t.age<=?9) " +
        " and t.frozen_tube_state='2004' and t.status='3001' "+
        " order by t.frozen_box_code,t.tube_rows,LPAD(t.tube_columns,2) asc offset ?10 rows fetch next 1000 rows only",nativeQuery = true)
    List<Object[]> findByRequirements(Integer sampleTypeId, Integer samplyClassificationId, Integer frozenTubeTypeId,
                                       String diseaseType, String sex, Integer isBloodLipid, Integer isHemolysis, Integer ageMin, Integer ageMax,Integer count);
    @Query(value = "select count(*) from frozen_tube t   " +
        " where (?1 = 0 or t.sample_type_id=?1) " +
        " and (?2 = 0 or t.sample_classification_id=?2)  " +
        " and (?3 = 0 or t.frozen_tube_type_id=?3) " +
        " and (?4 is null or t.disease_type=?4) " +
        " and (?5 is null or t.gender=?5) " +
        " and (?6 = 0 or t.is_blood_lipid=?6) " +
        " and (?7 = 0 or t.is_hemolysis=?7) " +
        " and (?8 = 0 or t.age>=?8) " +
        " and (?9 = 0 or t.age<=?9) " +
        " and t.frozen_tube_state='2004' and t.status='3001' ",nativeQuery = true)
    Long countByRequirements(Integer sampleTypeId, Integer samplyClassificationId, Integer frozenTubeTypeId,
                                      String diseaseType, String sex, Integer isBloodLipid, Integer isHemolysis, Integer ageMin, Integer ageMax);

    @Query("select t from FrozenTube t where (t.sampleCode in ?1 or t.sampleTempCode in ?1) and t.project.projectCode = ?2 and t.sampleType.id = ?3 and t.status != ?4")
    List<FrozenTube> findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(List<String> sampleCode, String projectCode, Long sampleTypeId,String status);

    @Query("select t from FrozenTube t where t.sampleCode =?1  and t.projectCode = ?2 and t.sampleTypeCode =?3 and t.sampleClassification.sampleClassificationCode = ?4 and t.status!='0000'")
    FrozenTube findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(String sampleCode, String projectCode, String sampleTypeCode, String sampleClassTypeCode);


    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.sampleType.id =?3 and t.sampleClassification.id = ?4 and t.status!='0000'")
    List<FrozenTube> findBySampleCodeAndProjectCodeAndSampleTypeIdAndSampleClassitionId(String sampleCode, String projectCode, Long sampleTypeId, Long sampleClassificationId);

    FrozenTube findByFrozenBoxCodeAndTubeRowsAndTubeColumnsAndStatusNot(String frozenBoxCode, String tubeRows, String tubeColumns, String status);

    @Query("select t from FrozenTube t where t.frozenBox.equipmentCode =?1  and t.frozenBox.areaCode = ?2" +
        " and t.frozenBox.supportRackCode =?3 and  t.frozenBox.status = '2004'and t.status!='0000'")
    List<FrozenTube> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String supportRackCode);

    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.frozenBox.id !=?3 and t.sampleType.id = ?4 and t.sampleClassification.id = ?5 and t.status!='0000'")
    List<FrozenTube> findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(String sampleCode, String projectCode, Long frozenBoxId, Long sampleTypeId, Long sampleClassificationId);

    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.frozenBox.id !=?3 and t.sampleType.id = ?4  and t.status!='0000'")
    List<FrozenTube> findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleType(String sampleCode, String projectCode, Long frozenBoxId, Long sampleTypeId);

    @Modifying
    @Query("update FrozenTube t set t.frozenTubeState = ?1  where t.frozenBoxCode in ?2 and t.status not in ('0000')")
    void updateFrozenTubeStateByFrozenBoxCodes(String status, List<String> frozenBoxCodes);

    @Query(value = "select count(t.id) from frozen_tube t left join tranship_box b on t.frozen_box_id = b.frozen_box_id left join tranship s on s.id = b.tranship_id left join frozen_box x on x.id = t.id and x.status not in ('2090','0000')" +
        "where s.tranship_code in ?1 and t.status!='0000' " ,nativeQuery = true)
    Long countByTranshipCodes(List<String> transhipCodeList);

    @Query("select count(t) from FrozenTube t where t.frozenBoxCode = ?1 and t.status!='0000' and t.frozenBox.status !='2090' and t.frozenTubeState=?2")
    Long countByFrozenBoxCodeAndFrozenTubeState(String frozenBoxCode,String frozenTubeState);

    FrozenTube findBySampleCodeAndSampleTypeCode(String sampleCode, String sampleTypeCode);

    List<FrozenTube> findByFrozenTubeState(String status);
}
