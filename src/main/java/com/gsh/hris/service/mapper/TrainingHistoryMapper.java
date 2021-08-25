package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.TrainingHistory;
import com.gsh.hris.service.dto.TrainingHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TrainingHistory} and its DTO {@link TrainingHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface TrainingHistoryMapper extends EntityMapper<TrainingHistoryDTO, TrainingHistory> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    TrainingHistoryDTO toDto(TrainingHistory s);
}
