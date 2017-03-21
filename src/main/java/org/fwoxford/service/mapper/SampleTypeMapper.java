package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.SampleTypeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SampleType and its DTO SampleTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SampleTypeMapper {

    SampleTypeDTO sampleTypeToSampleTypeDTO(SampleType sampleType);

    List<SampleTypeDTO> sampleTypesToSampleTypeDTOs(List<SampleType> sampleTypes);

    SampleType sampleTypeDTOToSampleType(SampleTypeDTO sampleTypeDTO);

    List<SampleType> sampleTypeDTOsToSampleTypes(List<SampleTypeDTO> sampleTypeDTOs);
}
