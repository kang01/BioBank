package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.StockOutApplyDTO;

import org.fwoxford.service.dto.response.StockOutApplyForSave;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StockOutApply and its DTO StockOutApplyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockOutApplyMapper {

    @Mapping(source = "delegate.id", target = "delegateId")
    StockOutApplyDTO stockOutApplyToStockOutApplyDTO(StockOutApply stockOutApply);

    List<StockOutApplyDTO> stockOutAppliesToStockOutApplyDTOs(List<StockOutApply> stockOutApplies);

    @Mapping(source = "delegateId", target = "delegate")
    StockOutApply stockOutApplyDTOToStockOutApply(StockOutApplyDTO stockOutApplyDTO);

    List<StockOutApply> stockOutApplyDTOsToStockOutApplies(List<StockOutApplyDTO> stockOutApplyDTOs);

    default Delegate delegateFromId(Long id) {
        if (id == null) {
            return null;
        }
        Delegate delegate = new Delegate();
        delegate.setId(id);
        return delegate;
    }
}
