package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutHandoverSampleReportDTO;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface StockOutHandoverSampleRepositries extends DataTablesRepository<StockOutHandoverSampleReportDTO,Long> {
}
