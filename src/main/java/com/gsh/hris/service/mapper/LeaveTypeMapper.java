package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.LeaveType;
import com.gsh.hris.service.dto.LeaveTypeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link LeaveType} and its DTO {@link LeaveTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaveTypeMapper extends EntityMapper<LeaveTypeDTO, LeaveType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LeaveTypeDTO toDtoName(LeaveType leaveType);
}
