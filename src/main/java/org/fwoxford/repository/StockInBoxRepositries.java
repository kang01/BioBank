package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockInBoxForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/4/8.
 */
public interface StockInBoxRepositries  extends DataTablesRepository<StockInBoxForDataTableEntity,Long> {
}
