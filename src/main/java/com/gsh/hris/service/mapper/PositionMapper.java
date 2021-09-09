package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.*;
import com.gsh.hris.service.dto.PositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    PositionDTO toDto(Position s);
}
