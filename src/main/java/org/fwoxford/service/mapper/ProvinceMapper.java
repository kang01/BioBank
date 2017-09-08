package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.ProvinceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Province and its DTO ProvinceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProvinceMapper {

    ProvinceDTO provinceToProvinceDTO(Province province);

    List<ProvinceDTO> provincesToProvinceDTOs(List<Province> provinces);

    Province provinceDTOToProvince(ProvinceDTO provinceDTO);

    List<Province> provinceDTOsToProvinces(List<ProvinceDTO> provinceDTOs);
}
