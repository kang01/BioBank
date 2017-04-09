package org.fwoxford.repository;

import org.fwoxford.domain.StockIn;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface StockInRepositries extends DataTablesRepository<StockIn,Long> {

}
