package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.CoordinateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Coordinate and its DTO CoordinateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CoordinateMapper {

    CoordinateDTO coordinateToCoordinateDTO(Coordinate coordinate);

    List<CoordinateDTO> coordinatesToCoordinateDTOs(List<Coordinate> coordinates);

    Coordinate coordinateDTOToCoordinate(CoordinateDTO coordinateDTO);

    List<Coordinate> coordinateDTOsToCoordinates(List<CoordinateDTO> coordinateDTOs);
}
