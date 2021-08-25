package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.Leave;
import com.gsh.hris.service.dto.LeaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Leave} and its DTO {@link LeaveDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class, LeaveTypeMapper.class })
public interface LeaveMapper extends EntityMapper<LeaveDTO, Leave> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    @Mapping(target = "leaveType", source = "leaveType", qualifiedByName = "name")
    LeaveDTO toDto(Leave s);
}
