package org.fwoxford.repositories;

import org.fwoxford.domain.Tranship;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface TempTranshipRepositry extends DataTablesRepository<Tranship,Long> {

}
