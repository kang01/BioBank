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

    @Query("select t from FrozenTube t where t.frozenBox.id = ?1")
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBoxCode = ?1 and status!='0000'")
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);

    List<FrozenTube> findFrozenTubeListByFrozenBoxCodeAndStatus(String frozenBoxCode, String frozenTubeNormal);
    @Query(value = "select t.frozen_box_code as frozenBoxCode,count(t.frozen_box_code) as sampleNumber \n" +
        "from frozen_tube t where t.frozen_box_code in ?1 group by t.frozen_box_code \n" +
        " order by t.frozen_box_code desc,sampleNumber asc " ,nativeQuery = true)
    List<Object[]> countSampleNumberByfrozenBoxList(List<String> frozenBoxList);

    //TODO:该方法暂不能使用，建立入库转运盒子管子关系表后再修改
    @Query(value = "select t.id as id ,t.FROZEN_BOX_ID as frozenBox,t.FROZEN_BOX_CODE as frozenBoxCode,t.FROZEN_TUBE_CODE as frozenTubeCode,\n" +
        "t.FROZEN_TUBE_TYPE_CODE as frozenTubeTypeCode,t.FROZEN_TUBE_TYPE_ID as frozenTubeType,t.FROZEN_TUBE_TYPE_NAME as frozenTubeTypeName,\n" +
        "t.FROZEN_TUBE_VOLUMNS as frozenTubeVolumns,t.FROZEN_TUBE_VOLUMNS_UNIT as frozenTubeVolumnsUnit,\n" +
        "t.PROJECT_CODE as projectCode,t.PATIENT_ID as patientId,t.PROJECT_ID as project,t.SAMPLE_CODE as sampleCode,\n" +
        "t.SAMPLE_TEMP_CODE as sampleTempCode,t.SAMPLE_TYPE_CODE as sampleTypeCode,t.SAMPLE_TYPE_ID as sampleType,\n" +
        "t.SAMPLE_TYPE_NAME as sampleTypeName,t.SAMPLE_USED_TIMES as sampleUsedTimes,t.SAMPLE_USED_TIMES_MOST as sampleUsedTimesMost,\n" +
        "t.memo as memo,t.TUBE_COLUMNS as tubeColumns,t.TUBE_ROWS as tubeRows,t.STATUS as status\n" +
        "from frozen_tube t\n" +
        "where t.FROZEN_BOX_CODE=?1\n" +
        "UNION \n" +
        "select r.FROZEN_TUBE_ID as id ,r.FROZEN_BOX_ID as frozenBox,r.FROZEN_BOX_CODE as frozenBoxCode,'' as frozenTubeCode,\n" +
        "'' as frozenTubeTypeCode,null as frozenTubeType,'' as frozenTubeTypeName,\n" +
        "null as frozenTubeVolumns,'' as frozenTubeVolumnsUnit,\n" +
        "r.PROJECT_CODE as projectCode,null as patientId,null as project,r.SAMPLE_CODE as sampleCode,\n" +
        "r.SAMPLE_TEMP_CODE as sampleTempCode,r.SAMPLE_TYPE_CODE as sampleTypeCode,r.SAMPLE_TYPE_ID as sampleType,\n" +
        "r.SAMPLE_TYPE_NAME as sampleTypeName,0 as sampleUsedTimes,0 as sampleUsedTimesMost,\n" +
        "r.memo as memo,r.TUBE_COLUMNS as tubeColumns,r.TUBE_ROWS as tubeRows,r.STATUS as status\n" +
        "from tube_record r\n" +
        "where r.frozen_box_code = ?1" ,nativeQuery = true)
    List<FrozenTube> findTranshipFrozenTubeListByBoxCode(String frozenBoxCode);
}
