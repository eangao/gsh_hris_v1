package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.Dependents;
import com.gsh.hris.service.dto.DependentsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Dependents} and its DTO {@link DependentsDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface DependentsMapper extends EntityMapper<DependentsDTO, Dependents> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    DependentsDTO toDto(Dependents s);
}
