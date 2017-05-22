package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Map;

/**
 * Spring Data JPA repository for the FrozenTube entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeRepository extends JpaRepository<FrozenTube,Long> {

    @Query("select t from FrozenTube t where t.frozenBox.id = ?1 and t.status!='0000'")
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBoxCode = ?1 and t.status!='0000'")
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);

    List<FrozenTube> findFrozenTubeListByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select t.frozen_box_code as frozenBoxCode,count(t.frozen_box_code) as sampleNumber \n" +
        "from frozen_tube t where t.frozen_box_code in ?1 and t.status!='0000' group by t.frozen_box_code \n" +
        " order by sampleNumber asc,t.frozen_box_code desc " ,nativeQuery = true)
    List<Object[]> countSampleNumberByfrozenBoxList(List<String> frozenBoxList);

    int countByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select count(*) from frozen_tube t where t.frozen_box_id in ?1 and t.status=?2" ,nativeQuery = true)
    int countByFrozenBoxCodeStrAndStatus(List<Long> boxIds, String status);

    @Query(value = "select count(t.sample_code) from frozen_tube t where t.frozen_box_id in ?1 and t.status!='0000'" ,nativeQuery = true)
    int countByFrozenBoxCodeStrAndGroupBySampleCode(List<Long> boxIds);

    @Query(value = "select t.* from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id" +
        " left join sample_type st on t.sample_type_id = st.id " +
        " where b.status = '2004' and t.sample_code = ?1 or t.sample_temp_code =?1 " +
        " and st.sample_type_code=?2 and t.status !='0000'" +
        " and t.id not in (select f.frozen_tube_id from stock_out_req_frozen_tube f where f.stock_out_requirement_id =?3 and f.status='1301')" ,nativeQuery = true)
    FrozenTube findBySampleCodeAndSampleTypeCode(String appointedSampleCode, String appointedSampleType,Long id);

//    @Query(value = "select t.* from frozen_tube t\n" +
//        " join frozen_box b on t.frozen_box_id = b.id\n" +
//        " left join stock_out_req_frozen_tube f on t.id = f.frozen_tube_id and f.status='1301'\n"
//        + " where b.status = '2004' and t.status!='0000'\n"
//        + " and f.frozen_tube_id is null \n"
//        + " and (?1 = 0 or t.sample_type_id = ?1)\n"
//        + " and (?2 = 0 or t.sample_classification_id = ?2)\n"
//        + " and (?3 = 0 or t.frozen_tube_type_id = ?3)\n"
//        + " and (?7 is null or t.gender = ?7)\n"
//        + " and (?4 is null or ?5 is null or 1=1)\n"
////        + " and (?4 is null or (SELECT Trunc(MONTHS_BETWEEN(\n"
////        + "　　to_date(to_char(sysdate, 'yyyy-MM-dd'),'yyyy-MM-dd'),\n"
////        + "　　to_date(t.dob, 'yyyy-MM-dd')\n"
////        + "　　) / 12) from dual)  between ?4 and ?5)\n"
//        + " and (?6 is null or t.disease_type  = ?6)\n"
//        + " and (?8 is null or t.is_blood_lipid  = ?8)\n"
//        + " and (?9 is null or t.is_hemolysis  = ?9)\n"
//         ,nativeQuery = true)
//    List<FrozenTube> findByRequirement(Long sampleTypeId, Long samplyClassificationId, Long frozenTubeTypeId,
//                                       Integer ageMin, Integer ageMax, String diseaseType, String sex, Boolean isBloodLipid, Boolean isHemolysis);
    @Query(value = "select t from FrozenTube t\n" +
        "  left join t.frozenBox \n"
        + " left join StockOutReqFrozenTube f on t.id = f.frozenTube.id and f.status='1301' \n"
        + " where  t.frozenBox is not null and t.frozenBox.status = '2004' and t.status!='0000'\n"
        + " and f.frozenTube.id is null \n"
        + " and (?1 is null or t.sampleType.id = ?1)\n"
        + " and (?2 is null or t.sampleClassification.id = ?2)\n"
        + " and (?3 is null or t.frozenTubeType.id = ?3)\n"
        + " and (?4 is null or t.diseaseType  = ?4)\n"
        + " and (?5 is null or t.gender = ?5)\n"
        + " and (?6 is null or t.isBloodLipid  = ?6)\n"
         + " and (?7 is null or t.isHemolysis  = ?7)\n"
    )
    List<FrozenTube> findByRequirement(Long sampleTypeId, Long samplyClassificationId, Long frozenTubeTypeId,
                                  String diseaseType, String sex, Boolean isBloodLipid, Boolean isHemolysis);
    @Query(value = "select * from frozen_tube t\n" +
        " left join frozen_box b on t.frozen_box_id = b.id\n" +
        " where b.status = '2004' and t.status!='0000'\n" +
        " and t.id not in (select f.frozen_tube_id from stock_out_req_frozen_tube f where f.stock_out_requirement_id =?1 and f.status='1301')" ,nativeQuery = true)
    List<FrozenTube> findByRequirementId(Long id);
}
