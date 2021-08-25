package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.Department;
import com.gsh.hris.service.dto.DepartmentDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClusterMapper.class })
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "cluster", source = "cluster", qualifiedByName = "name")
    DepartmentDTO toDto(Department s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoName(Department department);
}
