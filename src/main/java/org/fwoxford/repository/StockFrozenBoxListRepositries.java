package org.fwoxford.repository;

import org.fwoxford.service.dto.response.FrozenBoxListAllDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/6/21.
 */
public interface StockFrozenBoxListRepositries extends DataTablesRepository<FrozenBoxListAllDataTableEntity,Long>{
}
