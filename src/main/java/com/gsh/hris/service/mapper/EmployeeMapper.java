package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.Employee;
import com.gsh.hris.service.dto.EmployeeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, DepartmentMapper.class, EmploymentTypeMapper.class })
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "department", source = "department", qualifiedByName = "name")
    @Mapping(target = "employmentType", source = "employmentType", qualifiedByName = "name")
    EmployeeDTO toDto(Employee s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoId(Employee employee);
}
