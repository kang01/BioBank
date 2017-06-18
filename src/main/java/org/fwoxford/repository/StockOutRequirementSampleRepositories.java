package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutRequirementFrozenTubeDetail;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/4/9.
 */
public interface StockOutRequirementSampleRepositories extends DataTablesRepository<StockOutRequirementFrozenTubeDetail,Long> {
}
