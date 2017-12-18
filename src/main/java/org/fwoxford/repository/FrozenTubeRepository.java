package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTube entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeRepository extends JpaRepository<FrozenTube,Long> {

    @Query("select t from FrozenTube t where t.frozenBox.id = ?1 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+Constants.FROZEN_BOX_INVALID+"'")
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBox.id in ?1 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+Constants.FROZEN_BOX_INVALID+"'")
    List<FrozenTube> findFrozenTubeListByBoxIdIn(List<Long> frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBoxCode = ?1 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+Constants.FROZEN_BOX_INVALID+"'")
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);

    @Query("select count(t) from FrozenTube t where t.frozenBoxCode = ?1 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+Constants.FROZEN_BOX_INVALID+"'")
    Long countFrozenTubeListByBoxCode(String frozenBoxCode);

    List<FrozenTube> findFrozenTubeListByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select t.frozen_box_code as frozenBoxCode,count(t.frozen_box_code) as sampleNumber \n" +
        "from frozen_tube t where t.frozen_box_code in ?1 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+Constants.FROZEN_BOX_INVALID+"' group by t.frozen_box_code \n" +
        " order by sampleNumber asc,t.frozen_box_code desc " ,nativeQuery = true)
    List<Object[]> countSampleNumberByfrozenBoxList(List<String> frozenBoxList);

    int countByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select count(1) from frozen_tube t where t.frozen_box_id in ?1 and t.status=?2" ,nativeQuery = true)
    int countByFrozenBoxCodeStrAndStatus(List<Long> boxIds, String status);

    @Query(value = "select t.sample_code,count(t.sample_code) as noo from frozen_tube t " +
        " where t.frozen_box_id in ?1 and t.status!='"+Constants.INVALID+"' " +
        " GROUP BY t.sample_code " ,nativeQuery = true)
    List<Object[]> countByFrozenBoxCodeStrAndGroupBySampleCode(List<Long> boxIds);

    @Query(value = "SELECT rt.FROZEN_TUBE_ID FROM STOCK_OUT_REQ_FROZEN_TUBE rt " +
        " WHERE rt.STATUS in ('"+Constants.STOCK_OUT_SAMPLE_IN_USE+"','"+Constants.STOCK_OUT_SAMPLE_WAITING_OUT+"')",nativeQuery = true)
    List<Object> findAllStockOutFrozenTube();

    @Query(value = "select t.* from frozen_tube t " +
        "  LEFT OUTER JOIN ( " +
        "  SELECT rt.FROZEN_TUBE_ID FROM STOCK_OUT_REQ_FROZEN_TUBE rt  " +
        "  WHERE  rt.STATUS in ('"+Constants.STOCK_OUT_SAMPLE_IN_USE+"','"+Constants.STOCK_OUT_SAMPLE_WAITING_OUT+"') " +
        ") vpt ON t.id = vpt.FROZEN_TUBE_ID"+
        " where t.frozen_tube_state='"+Constants.FROZEN_BOX_STOCKED+"' and t.status='"+Constants.FROZEN_TUBE_NORMAL+"' and vpt.FROZEN_TUBE_ID IS NULL " +
        " and t.project_id in ?10 "+
        " and (?1 = 0 or t.sample_type_id=?1) " +
        " and (?2 = 0 or t.sample_classification_id=?2)  " +
        " and (?3 = 0 or t.frozen_tube_type_id=?3) " +
        " and (?4 is null or t.disease_type=?4) " +
        " and (?5 is null or t.gender=?5) " +
        " and (?6 = 0 or t.is_blood_lipid=?6) " +
        " and (?7 = 0 or t.is_hemolysis=?7) " +
        " and (?8 = 0 or t.age>=?8) " +
        " and (?9 = 0 or t.age<=?9) " +
        " order by t.frozen_box_code,t.tube_rows,LPAD(t.tube_columns,2) asc offset ?11 rows fetch next ?12 rows only",nativeQuery = true)

    List<FrozenTube> findByRequirements(Integer sampleTypeId, Integer samplyClassificationId, Integer frozenTubeTypeId,
                                        String diseaseType, String sex, Integer isBloodLipid, Integer isHemolysis, Integer ageMin, Integer ageMax,  List<Long> projectIds,Integer startPos,Integer length);
    @Query(value = "select count(t.id) from frozen_tube t   " +
        "  LEFT OUTER JOIN ( " +
        "  SELECT rt.FROZEN_TUBE_ID FROM STOCK_OUT_REQ_FROZEN_TUBE rt  " +
        "  WHERE  rt.STATUS in ('"+Constants.STOCK_OUT_SAMPLE_IN_USE+"','"+Constants.STOCK_OUT_SAMPLE_WAITING_OUT+"') " +
        ") vpt ON t.id = vpt.FROZEN_TUBE_ID"+
        " where t.frozen_tube_state='"+Constants.FROZEN_BOX_STOCKED+"' and t.status='"+Constants.FROZEN_TUBE_NORMAL+"' and vpt.FROZEN_TUBE_ID IS NULL " +
        " and t.project_id in ?10 "+
        " and (?1 = 0 or t.sample_type_id=?1) " +
        " and (?2 = 0 or t.sample_classification_id=?2)  " +
        " and (?3 = 0 or t.frozen_tube_type_id=?3) " +
        " and (?4 is null or t.disease_type=?4) " +
        " and (?5 is null or t.gender=?5) " +
        " and (?6 = 0 or t.is_blood_lipid=?6) " +
        " and (?7 = 0 or t.is_hemolysis=?7) " +
        " and (?8 = 0 or t.age>=?8) " +
        " and (?9 = 0 or t.age<=?9) ", nativeQuery = true)
    Long countByRequirements(Integer sampleTypeId, Integer samplyClassificationId, Integer frozenTubeTypeId,
                             String diseaseType, String sex, Integer isBloodLipid, Integer isHemolysis, Integer ageMin, Integer ageMax, List<Long> projectIds);

    @Query("select t from FrozenTube t where (t.sampleCode in ?1 or t.sampleTempCode in ?1) and t.project.projectCode = ?2 and t.sampleType.id = ?3 and t.status != ?4")
    List<FrozenTube> findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(List<String> sampleCode, String projectCode, Long sampleTypeId,String status);

    @Query("select t from FrozenTube t where t.sampleCode =?1  and t.projectCode = ?2 and t.sampleTypeCode =?3 and t.sampleClassification.sampleClassificationCode = ?4 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')")
    FrozenTube findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(String sampleCode, String projectCode, String sampleTypeCode, String sampleClassTypeCode);


    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.sampleType.id =?3 and t.sampleClassification.id = ?4 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findBySampleCodeAndProjectCodeAndSampleTypeIdAndSampleClassitionId(String sampleCode, String projectCode, Long sampleTypeId, Long sampleClassificationId);

    FrozenTube findByFrozenBoxCodeAndTubeRowsAndTubeColumnsAndStatusNot(String frozenBoxCode, String tubeRows, String tubeColumns, String status);

    @Query("select t from FrozenTube t where t.frozenBox.equipmentCode =?1  and t.frozenBox.areaCode = ?2" +
        " and t.frozenBox.supportRackCode =?3 and  t.frozenBox.status = '"+Constants.FROZEN_BOX_STOCKED+"' and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String supportRackCode);

    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.frozenBox.id !=?3 and t.sampleType.id = ?4 and t.sampleClassification.id = ?5 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(String sampleCode, String projectCode, Long frozenBoxId, Long sampleTypeId, Long sampleClassificationId);

    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.frozenBox.id !=?3 and t.sampleType.id = ?4  and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleType(String sampleCode, String projectCode, Long frozenBoxId, Long sampleTypeId);

    @Modifying
    @Query("update FrozenTube t set t.frozenTubeState = ?1  where t.frozenBoxCode in ?2 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')")
    void updateFrozenTubeStateByFrozenBoxCodes(String status, List<String> frozenBoxCodes);

    @Query(value = "select count(t.id) from frozen_tube t left join tranship_box b on t.frozen_box_id = b.frozen_box_id left join tranship s on s.id = b.tranship_id left join frozen_box x on x.id = t.id and x.status not in ('"+Constants.FROZEN_BOX_INVALID+"','"+Constants.INVALID+"')" +
        "where s.tranship_code in ?1 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')" ,nativeQuery = true)
    Long countByTranshipCodes(List<String> transhipCodeList);

    @Query("select count(t) from FrozenTube t where t.frozenBoxCode = ?1 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+Constants.FROZEN_BOX_INVALID+"' and t.frozenTubeState=?2")
    Long countByFrozenBoxCodeAndFrozenTubeState(String frozenBoxCode,String frozenTubeState);

    FrozenTube findBySampleCodeAndSampleTypeCode(String sampleCode, String sampleTypeCode);

    @Query(value = "select * from frozen_tube t where t.frozen_tube_state = '"+Constants.FROZEN_BOX_STOCKED+"' and t.status!='"+Constants.INVALID+"'" +
        " and (t.sample_code in ?3" +
        " or t.sample_code in ?4" +
        " or t.sample_code in ?5" +
        " or t.sample_code in ?6" +
        " or t.sample_code in ?7" +
        " or t.sample_code in ?8" +
        " or t.sample_code in ?9" +
        " or t.sample_code in ?10" +
        " or t.sample_code in ?11" +
        " or t.sample_code in ?12" +
        " ) " +
        " and t.project_id in ?2 and t.sample_type_code = ?1"
        ,nativeQuery = true)
    List<FrozenTube> findBySampleCodeInAndSampleTypeCodeAndProjectIn(
        String appointedSampleType, List<Long> projectIds
        , List<String> sampleCodeList1
        , List<String> sampleCodeList2
        , List<String> sampleCodeList3
        , List<String> sampleCodeList4
        , List<String> sampleCodeList5
        , List<String> sampleCodeList6
        , List<String> sampleCodeList7
        , List<String> sampleCodeList8
        , List<String> sampleCodeList9
        , List<String> sampleCodeList10
    );

    List<FrozenTube> findByFrozenBoxCodeAndFrozenTubeState(String frozenBoxCode, String frozenTubeState);

    @Query(value = "select t.sample_code,t.sample_type_code,b.id from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1)" +
        "and t.sample_type_code = ?2 and t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' ",nativeQuery = true)
    List<Object[]> findByFrozenBoxCode1DInAndSampleType(List<String> boxCodeListEach1000, String type);

    @Query(value = "select count(1) from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1 )" +
        "and t.sample_type_code = ?2 and t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"'",nativeQuery = true)
    Long countByFrozenBoxCode1DInAndSampleType(List<String> boxCodeListEach1000, String type);

    List<FrozenTube> findByFrozenBoxCodeIn(List<String> frozenBoxCodeStr);

    @Query(value = "  select cast(s.tranship_code as varchar2(255)) as transhipCode," +
        "cast(t.frozen_box_code as varchar2(255)) as frozenBoxCode,count(1) " +
        "from (select * from frozen_tube where project_code = ?1 and sample_type_code = ?1 and frozen_tube_state='"+Constants.FROZEN_BOX_TRANSHIP_COMPLETE+"') t \n" +
        "        left join (select * from tranship_tube ) f on f.frozen_tube_id = t.id  " +
        "         left join tranship_box tb on  f.tranship_box_id = tb.id  " +
        "         left join tranship s on  tb.tranship_id = s.id  " +
        "    where f.id is not null group by s.tranship_code,t.frozen_box_code" , nativeQuery = true)
    List<Object[]> countTubeByProjectCodeGroupByTranshipCode(String projectCode , String sampleTypeCode);

    @Query(value = "SELECT T.FROZEN_BOX_ID,COUNT(T.ID) AS NOO FROM FROZEN_TUBE T WHERE T.FROZEN_BOX_ID IN ?1 AND STATUS not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"') GROUP BY T.FROZEN_BOX_ID ",nativeQuery = true)
    List<Object[]> countGroupByFrozenBoxId(List<Long> boxIds);

    @Query("select t from FrozenTube t where (t.sampleCode in ?1 or t.sampleTempCode in ?1) and t.projectCode=?2 and t.status not in ('"+Constants.INVALID+"','"+Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findBySampleCodeInAndProjectCode(List<String> sampleCodeStr,String projectCode );

    @Query("select t from FrozenTube t where t.frozenBoxCode in ?1 and  t.status not in (?2,"+Constants.FROZEN_TUBE_DESTROY+")")
    List<FrozenTube> findByFrozenBoxCodeInAndStatusNot(List<String> boxCodeStr, String status);

    @Query(value = "select t.sample_temp_code,count(t.sample_temp_code) as noo from frozen_tube t " +
        " where t.frozen_box_id in ?1 and t.status!='"+Constants.INVALID+"' and t.sample_code is null " +
        " GROUP BY t.sample_temp_code" ,nativeQuery = true)
    List<Object[]> countByFrozenBoxCodeStrAndGroupBySampleTempCode(List<Long> boxIds);

    Long countByFrozenBoxIdAndStatusNot(Long id, String status);

    @Modifying
    @Query("update FrozenTube b set b.status='"+Constants.INVALID+"' where b.id not in ?1 and b.frozenTubeState in ('"+Constants.FROZEN_BOX_NEW+"','"+Constants.FROZEN_BOX_STOCKING+"') and b.frozenBox.id = ?2")
    void updateStatusByFrozenBoxIdNotInAndFrozenTubeStateAndFrozenBox(List<Long> frozenTubeIdsOld, Long frozenBoxId);

    @Query(value = "select * from frozen_tube t where t.frozen_tube_state = '"+Constants.FROZEN_BOX_STOCKED+"' " +
        " and t.status!='"+Constants.INVALID+"'" +
        " and (t.sample_code in ?3" +
        " or t.sample_code in ?4" +
        " or t.sample_code in ?5" +
        " or t.sample_code in ?6" +
        " or t.sample_code in ?7" +
        " or t.sample_code in ?8" +
        " or t.sample_code in ?9" +
        " or t.sample_code in ?10" +
        " or t.sample_code in ?11" +
        " or t.sample_code in ?12" +
        " ) " +
        " and t.project_id in ?2 and t.sample_type_code = ?1 " +
        " and (?13 = 0  or t.frozen_box_id = ?13)"
        ,nativeQuery = true)
    List<FrozenTube> findBySampleTypeCodeAndProjectInAndSampleCodeInAndFrozenBoxId(
        String sampleType, List<Long> projectIds, List<String> strings, List<String> strings1, List<String> strings2, List<String> strings3,
        List<String> strings4, List<String> strings5,List<String> strings6, List<String> strings7, List<String> strings8, List<String> strings9,Long frozenBoxId);

    @Query(value = "select t.* from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' and  t.sample_type_code = ?2  and (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1)" +
        "and t.project_id in ?3 ",nativeQuery = true)
    List<FrozenTube> findByFrozenBoxCode1DInOrFrozenBoxCodeInAndSampleTypeAndProjectIdsIn(List<String> code, String type, List<Long> projectIds);

    List<FrozenTube> findBySampleCodeInAndStatusNot(List<String> sampleCodeStr, String invalid);

    @Modifying
    @Query("update FrozenTube t set t.status = ?1  where t.frozenBox.id = ?2 and t.frozenTubeState = '"+Constants.FROZEN_BOX_NEW+"'")
    void updateStatusByFrozenBoxId(String status, Long id);
}
