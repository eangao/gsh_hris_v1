package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.EmploymentType;
import com.gsh.hris.service.dto.EmploymentTypeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link EmploymentType} and its DTO {@link EmploymentTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmploymentTypeMapper extends EntityMapper<EmploymentTypeDTO, EmploymentType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EmploymentTypeDTO toDtoName(EmploymentType employmentType);
}
