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

    @Query("select t from FrozenTube t where t.frozenBox.id in ?1 and t.status!='0000' and t.frozenBox.status !='2005'")
    List<FrozenTube> findFrozenTubeListByBoxIdIn(List<Long> frozenBoxId);

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
        " and t.project_id in ?4 and ROWNUM <=1"
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
        + " order by t.frozenBox.frozenBoxCode asc\n"
    )
    List<FrozenTube> findByRequirement(Long sampleTypeId, Long samplyClassificationId, Long frozenTubeTypeId,
                                       String diseaseType, String sex, Boolean isBloodLipid, Boolean isHemolysis, Integer ageMin, Integer ageMax);

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

    @Query(value = "select count(t.id) from frozen_tube t left join tranship_box b on t.frozen_box_id = b.frozen_box_id left join tranship s on s.id = b.tranship_id left join frozen_box x on x.id = t.id and x.status not in ('2005','0000')" +
        "where s.tranship_code in ?1 and t.status!='0000' " ,nativeQuery = true)
    Long countByTranshipCodes(List<String> transhipCodeList);

}
