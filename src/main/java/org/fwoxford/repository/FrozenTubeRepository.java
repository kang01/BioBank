package org.fwoxford.repository;

import io.swagger.models.auth.In;
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

    @Query("select t from FrozenTube t\n" +
        "  left join t.frozenBox \n"
        + " left join StockOutReqFrozenTube f on t.id = f.frozenTube.id and f.status='1301' \n"
        + " where  t.frozenBox is not null and t.frozenBox.status = '2004' and t.status!='0000'\n"
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
    @Query(value = "select * from frozen_tube t\n" +
        " left join frozen_box b on t.frozen_box_id = b.id\n" +
        " where b.status = '2004' and t.status!='0000'\n" +
        " and t.id not in (select f.frozen_tube_id from stock_out_req_frozen_tube f where f.stock_out_requirement_id =?1 and f.status='1301')" ,nativeQuery = true)
    List<FrozenTube> findByRequirementId(Long id);
    @Query("select t from FrozenTube t\n" +
        "  left join t.frozenBox \n"
        + " left join StockOutReqFrozenTube f on t.id = f.frozenTube.id and f.status='1301' \n"
        + " where  t.frozenBox is not null and t.frozenBox.status = '2004' and t.status!='0000'\n"
        + " and f.frozenTube.id is null \n"
        + " and (?1 is null or t.sampleType.id = ?1)\n"
        + " and (?2 is null or t.sampleClassification.id = ?2)\n"
        + " and (?3 is null or t.frozenTubeType.id = ?3)\n"
        + " and (?4 is null or t.diseaseType  = ?4)\n"
        + " and (?5 is null or t.gender = ?5)\n"
        + " and (?6 is null or ?6 is false or t.isBloodLipid  = ?6)\n"
        + " and (?7 is null or ?7 is false or t.isHemolysis  = ?7)\n"
    )
    List<FrozenTube> findByRequiremented(Long sampleTypeId, Long samplyClassificationId, Long frozenTubeTypeId, String diseaseType, String sex, Boolean isBloodLipid, Boolean isHemolysis);
}
