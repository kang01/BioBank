package org.fwoxford.repository;

import org.fwoxford.service.dto.response.FrozenPositionListDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/6/21.
 */
public interface StockListRepositries extends DataTablesRepository<FrozenPositionListDataTableEntity,Long>{
}
