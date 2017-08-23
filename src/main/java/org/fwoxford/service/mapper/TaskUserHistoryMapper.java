package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.TaskUserHistoryDTO;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity UserLoginHistory and its DTO UserLoginHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskUserHistoryMapper {

    TaskUserHistoryDTO taskUserHistoryToTaskUserHistoryDTO(TaskUserHistory taskUserHistory);

    List<TaskUserHistoryDTO> taskUserHistoriesToTaskUserHistoryDTOs(List<TaskUserHistory> taskUserHistories);

    TaskUserHistory taskUserHistoryDTOToTaskUserHistory(TaskUserHistoryDTO taskUserHistoryDTO);

    List<TaskUserHistory> taskUserHistoryDTOsToTaskUserHistories(List<TaskUserHistoryDTO> taskUserHistoryDTOs);
}
