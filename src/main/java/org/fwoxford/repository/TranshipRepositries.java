package org.fwoxford.repository;

import org.fwoxford.domain.Tranship;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface TranshipRepositries extends DataTablesRepository<TranshipResponse,Long> {

}
