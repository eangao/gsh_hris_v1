package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.*;
import com.gsh.hris.service.dto.BenefitsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Benefits} and its DTO {@link BenefitsDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface BenefitsMapper extends EntityMapper<BenefitsDTO, Benefits> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    BenefitsDTO toDto(Benefits s);
}
