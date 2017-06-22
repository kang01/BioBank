package org.fwoxford.repository;

import org.fwoxford.service.dto.response.FrozenPositionListAllDataTableEntity;
import org.fwoxford.service.dto.response.FrozenPositionListDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/6/21.
 */
public interface StockListByProjectRepositries extends DataTablesRepository<FrozenPositionListAllDataTableEntity,Long>{
}
