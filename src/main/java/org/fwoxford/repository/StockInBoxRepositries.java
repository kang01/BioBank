package org.fwoxford.repository;

import org.fwoxford.domain.StockInBox;
import org.fwoxford.domain.StockInBoxForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/4/8.
 */
public interface StockInBoxRepositries  extends DataTablesRepository<StockInBoxForDataTableEntity,Long> {
}
