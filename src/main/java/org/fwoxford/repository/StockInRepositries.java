package org.fwoxford.repository;

import org.fwoxford.domain.StockIn;
import org.fwoxford.domain.StockInForDataTableEntity;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StockInRepositries extends DataTablesRepository<StockInForDataTableEntity,Long> {
}
