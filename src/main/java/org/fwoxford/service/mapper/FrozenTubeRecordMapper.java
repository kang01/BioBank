package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.FrozenTubeRecordDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FrozenTubeRecord and its DTO FrozenTubeRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrozenTubeRecordMapper {

    @Mapping(source = "sampleType.id", target = "sampleTypeId")
    @Mapping(source = "tubeType.id", target = "tubeTypeId")
    FrozenTubeRecordDTO frozenTubeRecordToFrozenTubeRecordDTO(FrozenTubeRecord frozenTubeRecord);

    List<FrozenTubeRecordDTO> frozenTubeRecordsToFrozenTubeRecordDTOs(List<FrozenTubeRecord> frozenTubeRecords);

    @Mapping(source = "sampleTypeId", target = "sampleType")
    @Mapping(source = "tubeTypeId", target = "tubeType")
    FrozenTubeRecord frozenTubeRecordDTOToFrozenTubeRecord(FrozenTubeRecordDTO frozenTubeRecordDTO);

    List<FrozenTubeRecord> frozenTubeRecordDTOsToFrozenTubeRecords(List<FrozenTubeRecordDTO> frozenTubeRecordDTOs);

    default SampleType sampleTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        SampleType sampleType = new SampleType();
        sampleType.setId(id);
        return sampleType;
    }

    default FrozenTubeType frozenTubeTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        FrozenTubeType frozenTubeType = new FrozenTubeType();
        frozenTubeType.setId(id);
        return frozenTubeType;
    }
}
