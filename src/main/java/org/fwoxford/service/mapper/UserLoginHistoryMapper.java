package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.UserLoginHistoryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity UserLoginHistory and its DTO UserLoginHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserLoginHistoryMapper {

    UserLoginHistoryDTO userLoginHistoryToUserLoginHistoryDTO(UserLoginHistory userLoginHistory);

    List<UserLoginHistoryDTO> userLoginHistoriesToUserLoginHistoryDTOs(List<UserLoginHistory> userLoginHistories);

    UserLoginHistory userLoginHistoryDTOToUserLoginHistory(UserLoginHistoryDTO userLoginHistoryDTO);

    List<UserLoginHistory> userLoginHistoryDTOsToUserLoginHistories(List<UserLoginHistoryDTO> userLoginHistoryDTOs);
}
