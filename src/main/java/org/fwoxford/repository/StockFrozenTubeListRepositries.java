package org.fwoxford.repository;

import org.fwoxford.service.dto.response.FrozenTubeListAllDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/6/21.
 */
public interface StockFrozenTubeListRepositries extends DataTablesRepository<FrozenTubeListAllDataTableEntity,Long>{
}
