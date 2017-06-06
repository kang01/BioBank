package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutApplyForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StockOutApplyRepositries extends DataTablesRepository<StockOutApplyForDataTableEntity,Long> {
    @Query(value = " select ap.id,ap.purpose_of_sample,0 as level_no,\n" +
        "            (case when ap.start_time is not null then (TO_CHAR(ap.start_time,'yyyy-MM-dd')||'至'||TO_CHAR(ap.end_time,'yyyy-MM-dd')) else null end )  as apply_time,ap.apply_person_name,ap.apply_code,ap.status,\n" +
        "            (select d.delegate_name from delegate d where d.id = ap.delegate_id) as delegate_name,\n" +
        "            (select LISTAGG(TO_CHAR(sample),',') WITHIN GROUP (ORDER BY sample) from\n" +
        "            (select distinct s.sample_type_name as sample\n" +
        "            from stock_out_requirement req\n" +
        "            left join sample_type s on req.sample_type_id=s.id\n" +
        "            where req.stock_out_apply_id=ap.id\n" +
        "            union\n" +
        "            select distinct s.sample_type as sample\n" +
        "            from stock_out_requirement req\n" +
        "            left join stock_out_required_sample s on req.id=s.stock_out_requirement_id\n" +
        "            where req.stock_out_apply_id=ap.id)) as sample_types,\n" +
        "            (select sum(req.count_of_sample) from stock_out_requirement req where req.stock_out_apply_id=ap.id) as count_of_sample\n" +
        "            from stock_out_apply ap  where ap.parent_apply_id =?1",nativeQuery = true)
    List<StockOutApplyForDataTableEntity> findByParentApplyId(Long id);
}
