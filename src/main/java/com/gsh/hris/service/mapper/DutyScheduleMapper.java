package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.DutySchedule;
import com.gsh.hris.service.dto.DutyScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link DutySchedule} and its DTO {@link DutyScheduleDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface DutyScheduleMapper extends EntityMapper<DutyScheduleDTO, DutySchedule> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    DutyScheduleDTO toDto(DutySchedule s);
}
