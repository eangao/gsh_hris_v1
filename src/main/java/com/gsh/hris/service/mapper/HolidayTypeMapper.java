package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.HolidayType;
import com.gsh.hris.service.dto.HolidayTypeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link HolidayType} and its DTO {@link HolidayTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HolidayTypeMapper extends EntityMapper<HolidayTypeDTO, HolidayType> {}
