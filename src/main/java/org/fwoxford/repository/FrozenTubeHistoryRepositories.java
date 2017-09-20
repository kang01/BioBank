package org.fwoxford.repository;

import org.fwoxford.service.dto.response.AreasListAllDataTableEntity;
import org.fwoxford.service.dto.response.FrozenTubeHistory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import java.time.temporal.ValueRange;
import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTubeHistory entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeHistoryRepositories extends DataTablesRepository<FrozenTubeHistory,Long> {

    @Query(value ="select tranship.frozen_tube_id as frozen_tube_id,\n" +
        "            tranship.project_code,\n" +
        "            tran.id as tranship_id,\n" +
        "            tran.tranship_code,\n" +
        "            null as stock_in_id,\n" +
        "            null as stock_in_code,\n" +
        "            null as stock_out_task_id,\n" +
        "            null as stock_out_task_code,\n" +
        "            null as handover_id,\n" +
        "            null as handover_code,\n" +
        "            tranship.sample_code,\n" +
        "            101 as type,\n" +
        "            tranship.status,box.id as frozen_box_id,box.frozen_box_code,tranship.rows_in_tube as tube_rows,tranship.columns_in_tube as tube_columns\n" +
        "            ,tran.TRANSHIP_DATE as operate_time,\n" +
        "            (tbox.equipment_code||'.'||tbox.area_code||'.'||tbox.support_rack_code||'.'||tbox.columns_in_shelf||tbox.rows_in_shelf) as position,\n" +
        "            (tranship.ROWS_IN_TUBE||tranship.COLUMNS_IN_TUBE) as position_in_box,\n" +
        "            tbox.equipment_code,tbox.equipment_id,tbox.area_code,tbox.area_id,tbox.support_rack_code as shelves_code ,\n" +
        "            tbox.support_rack_id as shelves_id,\n" +
        "            tbox.columns_in_shelf,tbox.rows_in_shelf,tranship.memo, u.last_name||u.first_name as operator,\n" +
        "            tranship.sample_type_id,tranship.sample_type_code,tranship.sample_type_name,\n" +
        "            tranship.frozen_tube_type_id,tranship.frozen_tube_type_code,tranship.frozen_tube_type_name,tranship.frozen_tube_volumns,tranship.frozen_tube_volumns_unit,\n" +
        "            tranship.sample_used_times_most,tranship.sample_used_times,tranship.sample_volumns,\n" +
        "            tranship.project_id,\n" +
        "            tranship.project_site_id,tranship.project_site_code,\n" +
        "            tranship.sample_classification_id,\n" +
        "            tranship.frozen_tube_state,tranship.CREATED_DATE\n" +
        "\n" +
        "            from (select * from tranship_tube where frozen_tube_id in ?1 and status!='0000') tranship" +
        "            left join tranship_box tbox on tranship.tranship_box_id = tbox.id\n" +
        "            left join  tranship tran on tbox.tranship_id = tran.id\n" +
        "            left join frozen_box box on tbox.frozen_box_id = box.id\n" +
        "            left join jhi_user u on tran.receiver_id = u.id",nativeQuery = true)
    List<Object[]> findTranshipHistoryBySamples(List<Long> ids);

    @Query(value = "select stockIn.frozen_tube_id as frozen_tube_id,\n" +
        "            stockIn.project_code,\n" +
        "            null as tranship_id,\n" +
        "            null as tranship_code,\n" +
        "            s.id as stock_in_id,\n" +
        "            s.stock_in_code,\n" +
        "            null as stock_out_task_id,\n" +
        "            null as stock_out_task_code,\n" +
        "            null as handover_id,\n" +
        "            null as handover_code,\n" +
        "            stockIn.sample_code,\n" +
        "            102 as type,\n" +
        "            stockIn.status,box.id as frozen_box_id,box.frozen_box_code,stockIn.tube_rows,stockIn.tube_columns\n" +
        "            ,s.STOCK_IN_DATE as operate_time,\n" +
        "            (sbox.equipment_code||'.'||sbox.area_code||'.'||sbox.support_rack_code||'.'||sbox.columns_in_shelf||sbox.rows_in_shelf) as position,\n" +
        "            (stockIn.tube_rows||stockIn.tube_columns) as position_in_box,\n" +
        "            sbox.equipment_code,sbox.equipment_id,sbox.area_code,sbox.area_id,sbox.support_rack_code as shelves_code ,\n" +
        "            sbox.support_rack_id as shelves_id,\n" +
        "            sbox.columns_in_shelf,sbox.rows_in_shelf,stockIn.memo, u.last_name||u.first_name||','||u2.last_name||u2.first_name as operator,\n" +
        "            stockIn.sample_type_id,stockIn.sample_type_code,stockIn.sample_type_name,\n" +
        "            stockIn.frozen_tube_type_id,stockIn.frozen_tube_type_code,stockIn.frozen_tube_type_name,stockIn.frozen_tube_volumns,stockIn.frozen_tube_volumns_unit,\n" +
        "            stockIn.sample_used_times_most,stockIn.sample_used_times,stockIn.sample_volumns,\n" +
        "            stockIn.project_id,\n" +
        "            stockIn.project_site_id,stockIn.project_site_code,\n" +
        "            stockIn.sample_classification_id,\n" +
        "            stockIn.frozen_tube_state,stockIn.CREATED_DATE\n" +
        "\n" +
        "            from (select * from stock_in_tube  where frozen_tube_id in ?1 and status!='0000') stockIn \n" +
        "            left join stock_in_box sbox on stockIn.stock_in_box_id = sbox.id\n" +
        "            left join  stock_in s on sbox.stock_in_id = s.id\n" +
        "            left join frozen_box box on sbox.frozen_box_id = box.id\n" +
        "            left join jhi_user u on  s.store_keeper_id_1 = u.id\n" +
        "            left join jhi_user u2 on  s.store_keeper_id_2 = u2.id",nativeQuery = true)
    List<Object[]> findStockInHistoryBySamples(List<Long> ids);

    @Query(value = " select stockOut.frozen_tube_id as frozen_tube_id,\n" +
        "            stockOut.project_code,\n" +
        "            null as tranship_id,\n" +
        "            null as tranship_code,\n" +
        "            null as stock_in_id,\n" +
        "            null as stock_in_code,\n" +
        "            task.id as stock_out_task_id,\n" +
        "            task.stock_out_task_code,\n" +
        "            null as handover_id,\n" +
        "            null as handover_code,\n" +
        "            stockOut.sample_code,\n" +
        "            103 as type,\n" +
        "            stockOut.status,box.id as frozen_box_id,box.frozen_box_code,stockOut.tube_rows,stockOut.tube_columns\n" +
        "            ,task.STOCK_OUT_DATE as operate_time,\n" +
        "            (soutbox.equipment_code||'.'||soutbox.area_code||'.'||soutbox.support_rack_code||'.'||soutbox.columns_in_shelf||soutbox.rows_in_shelf) as position,\n" +
        "            (stockOut.tube_rows||stockOut.tube_columns) as position_in_box,\n" +
        "            soutbox.equipment_code,soutbox.equipment_id,soutbox.area_code,soutbox.area_id,soutbox.support_rack_code as shelves_code ,\n" +
        "            soutbox.support_rack_id as shelves_id,\n" +
        "            soutbox.columns_in_shelf,soutbox.rows_in_shelf,stockOut.memo,u.last_name||u.first_name||','||u2.last_name||u2.first_name as operator,\n" +
        "            stockOut.sample_type_id,stockOut.sample_type_code,stockOut.sample_type_name,\n" +
        "            stockOut.frozen_tube_type_id,stockOut.frozen_tube_type_code,stockOut.frozen_tube_type_name,stockOut.frozen_tube_volumns,stockOut.frozen_tube_volumns_unit,\n" +
        "            stockOut.sample_used_times_most,stockOut.sample_used_times,stockOut.sample_volumns,\n" +
        "            stockOut.project_id,\n" +
        "            stockOut.project_site_id,stockOut.project_site_code,\n" +
        "            stockOut.sample_classification_id,\n" +
        "            stockOut.frozen_tube_state,stockOut.CREATED_DATE\n" +
        "\n" +
        "            from (select * from stock_out_box_tube  where frozen_tube_id in ?1 and status!='0000') stockOut" +
        "            left join stock_out_box soutbox on stockOut.stock_out_frozen_box_id = soutbox.id\n" +
        "            left join frozen_box box on soutbox.frozen_box_id = box.id\n" +
        "            left join  stock_out_task task on soutbox.stock_out_task_id = task.id\n" +
        "            left join jhi_user u on  task.stock_out_head_id_1 = u.id\n" +
        "            left join jhi_user u2 on  task.stock_out_head_id_2 = u2.id",nativeQuery = true)
    List<Object[]> findStockOutHistoryBySamples(List<Long> ids);
    @Query(value = "  select stockOut.frozen_tube_id as frozen_tube_id,\n" +
        "            stockOut.project_code,\n" +
        "            null as tranship_id,\n" +
        "            null as tranship_code,\n" +
        "            null as stock_in_id,\n" +
        "            null as stock_in_code,\n" +
        "            null as stock_out_task_id,\n" +
        "            null as stock_out_task_code,\n" +
        "            handover.id as handover_id,\n" +
        "            handover.handover_code,\n" +
        "            stockOut.sample_code,\n" +
        "            104 as type,\n" +
        "            stockOut.status,box.id as frozen_box_id,box.frozen_box_code,stockOut.tube_rows,stockOut.tube_columns\n" +
        "            ,handover.handover_time  as operate_time,\n" +
        "            (soutbox.equipment_code||'.'||soutbox.area_code||'.'||soutbox.support_rack_code||'.'||soutbox.columns_in_shelf||soutbox.rows_in_shelf) as position,\n" +
        "            (stockOut.tube_rows||stockOut.tube_columns) as position_in_box,\n" +
        "            soutbox.equipment_code,soutbox.equipment_id,soutbox.area_code,soutbox.area_id,soutbox.support_rack_code as shelves_code ,\n" +
        "            soutbox.support_rack_id as shelves_id,\n" +
        "            soutbox.columns_in_shelf,soutbox.rows_in_shelf,hand.memo, u.last_name||u.first_name as operator,\n" +
        "            stockOut.sample_type_id,stockOut.sample_type_code,stockOut.sample_type_name,\n" +
        "            stockOut.frozen_tube_type_id,stockOut.frozen_tube_type_code,stockOut.frozen_tube_type_name,stockOut.frozen_tube_volumns,stockOut.frozen_tube_volumns_unit,\n" +
        "            stockOut.sample_used_times_most,stockOut.sample_used_times,stockOut.sample_volumns,\n" +
        "            stockOut.project_id,\n" +
        "            stockOut.project_site_id,stockOut.project_site_code,\n" +
        "            stockOut.sample_classification_id,\n" +
        "            hand.status as frozen_tube_state,handover.CREATED_DATE\n" +
        "\n" +
        "            from (select * from  stock_out_box_tube stockOut where frozen_tube_id in ?1 and status!='0000') stockOut\n" +
        "            inner join stock_out_handover_details hand on hand.stock_out_box_tube_id = stockOut.id\n" +
        "            left join stock_out_box soutbox on stockOut.stock_out_frozen_box_id = soutbox.id\n" +
        "            left join frozen_box box on soutbox.frozen_box_id = box.id\n" +
        "            left join  stock_out_handover handover on hand.stock_out_handover_id = handover.id\n" +
        "            left join jhi_user u on  handover.handover_person_id = u.id\n",nativeQuery = true)
    List<Object[]> findHandOverHistoryBySamples(List<Long> ids);
    @Query(value = "select m.frozen_tube_id as frozen_tube_id,\n" +
        "            m.project_code,\n" +
        "            null as tranship_id,\n" +
        "            null as tranship_code,\n" +
        "            null as stock_in_id,\n" +
        "            null as stock_in_code,\n" +
        "            null as stock_out_task_id,\n" +
        "            null as stock_out_task_code,\n" +
        "            null as handover_id,\n" +
        "            null as handover_code,\n" +
        "            m.sample_code,\n" +
        "            105 as type,\n" +
        "            m.status,m.frozen_box_id,m.frozen_box_code,m.tube_rows,m.tube_columns\n" +
        "            ,p.position_move_date  as operate_time,\n" +
        "            (m.equipment_code||'.'||m.area_code||'.'||m.support_rack_code||'.'||m.columns_in_shelf||m.rows_in_shelf) as position,\n" +
        "            (m.tube_rows||m.tube_columns) as position_in_box,\n" +
        "            m.equipment_code,m.equipment_id,m.area_code,m.area_id,m.support_rack_code as shelves_code ,\n" +
        "            m.support_rack_id as shelves_id,\n" +
        "            m.columns_in_shelf,m.rows_in_shelf,p.move_reason as memo ,u.last_name||u.first_name||','||u2.last_name||u2.first_name as operator,\n" +
        "            m.sample_type_id,m.sample_type_code,m.sample_type_name,\n" +
        "            m.frozen_tube_type_id,m.frozen_tube_type_code,m.frozen_tube_type_name,m.frozen_tube_volumns,m.frozen_tube_volumns_unit,\n" +
        "            m.sample_used_times_most,m.sample_used_times,m.sample_volumns,\n" +
        "            m.project_id,\n" +
        "            m.project_site_id,m.project_site_code,\n" +
        "            m.sample_classification_id,\n" +
        "            m.frozen_tube_state,p.CREATED_DATE\n" +
        "\n" +
        "            from ( select * from position_move_record  where frozen_tube_id in ?1 and status!='0000') m " +
        "            left join position_move p on m.position_move_id = p.id\n" +
        "            left join jhi_user u on  p.operator_id_1 = u.id\n" +
        "            left join jhi_user u2 on  p.operator_id_2 = u2.id",nativeQuery = true)
    List<Object[]> findMoveHistoryBySamples(List<Long> ids);
    @Query(value = " select m.frozen_tube_id as frozen_tube_id,\n" +
        "            m.project_code,\n" +
        "            null as tranship_id,\n" +
        "            null as tranship_code,\n" +
        "            null as stock_in_id,\n" +
        "            null as stock_in_code,\n" +
        "            null as stock_out_task_id,\n" +
        "            null as stock_out_task_code,\n" +
        "            null as handover_id,\n" +
        "            null as handover_code,\n" +
        "            m.sample_code,\n" +
        "            106 as type,\n" +
        "            m.status,m.frozen_box_id,m.frozen_box_code,m.tube_rows,m.tube_columns\n" +
        "            ,p.position_change_date  as operate_time,\n" +
        "            (m.equipment_code||'.'||m.area_code||'.'||m.support_rack_code||'.'||m.columns_in_shelf||m.rows_in_shelf) as position,\n" +
        "            (m.tube_rows||m.tube_columns) as position_in_box,\n" +
        "            m.equipment_code,m.equipment_id,m.area_code,m.area_id,m.support_rack_code as shelves_code ,\n" +
        "            m.support_rack_id as shelves_id,\n" +
        "            m.columns_in_shelf,m.rows_in_shelf,p.change_reason as memo ,u.last_name||u.first_name||','||u2.last_name||u2.first_name as operator,\n" +
        "            m.sample_type_id,m.sample_type_code,m.sample_type_name,\n" +
        "            m.frozen_tube_type_id,m.frozen_tube_type_code,m.frozen_tube_type_name,m.frozen_tube_volumns,m.frozen_tube_volumns_unit,\n" +
        "            m.sample_used_times_most,m.sample_used_times,m.sample_volumns,\n" +
        "            m.project_id,\n" +
        "            m.project_site_id,m.project_site_code,\n" +
        "            m.sample_classification_id,\n" +
        "            m.frozen_tube_state,p.CREATED_DATE\n" +
        "\n" +
        "            from (select * from position_change_record  where frozen_tube_id in ?1 and status!='0000') m\n" +
        "            left join position_change p on m.position_change_id = p.id\n" +
        "            left join jhi_user u on  p.operator_id_1 = u.id\n" +
        "            left join jhi_user u2 on  p.operator_id_2 = u2.id",nativeQuery = true)
    List<Object[]> findChangeHistoryBySamples(List<Long> ids);

    @Query(value=" select m.frozen_tube_id as frozen_tube_id,\n" +
        "            m.project_code,\n" +
        "            null as tranship_id,\n" +
        "            null as tranship_code,\n" +
        "            null as stock_in_id,\n" +
        "            null as stock_in_code,\n" +
        "            null as stock_out_task_id,\n" +
        "            null as stock_out_task_code,\n" +
        "            null as handover_id,\n" +
        "            null as handover_code,\n" +
        "            m.sample_code,\n" +
        "            107 as type,\n" +
        "            m.status,m.frozen_box_id,m.frozen_box_code,m.tube_rows,m.tube_columns\n" +
        "            ,p.position_destroy_date  as operate_time,\n" +
        "            (m.equipment_code||'.'||m.area_code||'.'||m.support_rack_code||'.'||m.columns_in_shelf||m.rows_in_shelf) as position,\n" +
        "            (m.tube_rows||m.tube_columns) as position_in_box,\n" +
        "            m.equipment_code,m.equipment_id,m.area_code,m.area_id,m.support_rack_code as shelves_code ,\n" +
        "            m.support_rack_id as shelves_id,\n" +
        "            m.columns_in_shelf,m.rows_in_shelf,p.destroy_reason as memo ,u.last_name||u.first_name||','||u2.last_name||u2.first_name as operator,\n" +
        "            m.sample_type_id,m.sample_type_code,m.sample_type_name,\n" +
        "            m.frozen_tube_type_id,m.frozen_tube_type_code,m.frozen_tube_type_name,m.frozen_tube_volumns,m.frozen_tube_volumns_unit,\n" +
        "            m.sample_used_times_most,m.sample_used_times,m.sample_volumns,\n" +
        "            m.project_id,\n" +
        "            m.project_site_id,m.project_site_code,\n" +
        "            m.sample_classification_id,\n" +
        "            m.frozen_tube_state,p.CREATED_DATE\n" +
        "\n" +
        "            from (select * from position_destroy_record  where frozen_tube_id in ?1 and status!='0000') m \n" +
        "            left join position_destroy p on m.position_destroy_id = p.id\n" +
        "            left join jhi_user u on  p.operator_id_1 = u.id\n" +
        "            left join jhi_user u2 on  p.operator_id_2 = u2.id\n",nativeQuery = true)
    List<Object[]> findDestroyHistoryBySamples(List<Long> ids);
}
