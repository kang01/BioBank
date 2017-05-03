package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.SampleClassificationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SampleClassification and its DTO SampleClassificationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SampleClassificationMapper {

    SampleClassificationDTO sampleClassificationToSampleClassificationDTO(SampleClassification sampleClassification);

    List<SampleClassificationDTO> sampleClassificationsToSampleClassificationDTOs(List<SampleClassification> sampleClassifications);

    SampleClassification sampleClassificationDTOToSampleClassification(SampleClassificationDTO sampleClassificationDTO);

    List<SampleClassification> sampleClassificationDTOsToSampleClassifications(List<SampleClassificationDTO> sampleClassificationDTOs);
}
