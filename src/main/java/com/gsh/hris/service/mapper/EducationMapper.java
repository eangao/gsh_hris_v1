package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.*;
import com.gsh.hris.service.dto.EducationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Education} and its DTO {@link EducationDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface EducationMapper extends EntityMapper<EducationDTO, Education> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    EducationDTO toDto(Education s);
}
