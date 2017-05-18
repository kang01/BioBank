package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;

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

    @Query(value = "select * from frozen_tube t\n" +
        "left join frozen_box b on t.frozen_box_id = b.id\n" +
        "where t.status = '2004' and t.status!='0000'\n" +
        "and (t.sample_type_id is null or t.sample_type_id = (select s.sample_type_id from stock_out_requirement s where s.id = ?1))\n" +
        "and (t.sample_classification_id is null or t.sample_classification_id = (select s.sample_classification_id from stock_out_requirement s where s.id = ?1))\n" +
        "and (t.frozen_tube_type_id is null or t.frozen_tube_type_id = (select s.frozen_tube_type_id from stock_out_requirement s where s.id = ?1))\n" +
        "and (t.gender is null or t.gender = (select s.sex from stock_out_requirement s where s.id = ?1))\n" +
        "and (t.dob is null or (SELECT Trunc(MONTHS_BETWEEN(\n" +
        "　　to_date(to_char(sysdate, 'yyyy-MM-dd'),'yyyy-MM-dd'),\n" +
        "　　to_date(t.dob, 'yyyy-MM-dd')\n" +
        "　　) / 12) from dual)  between (select s.age_min from stock_out_requirement s where s.id = ?1) and (select s.age_max from stock_out_requirement s where s.id = ?1))\n" +
        "and (t.disease_type is null or t.disease_type  = (select s.sex from stock_out_requirement s where s.id = ?1))\n" +
        "and (t.is_blood_lipid is null or t.is_hemolysis  = (select s.is_hemolysis from stock_out_requirement s where s.id = ?1))\n" +
        "and (t.is_hemolysis is null or t.is_hemolysis  = (select s.is_hemolysis from stock_out_requirement s where s.id = ?1))\n" +
        "and t.id not in (select f.frozen_tube_id from stock_out_req_frozen_tube f where f.stock_out_requirement_id =?1 and f.status='1301')" ,nativeQuery = true)
    List<FrozenTube> findByRequirement(Long id);
}
