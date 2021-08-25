package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.Holiday;
import com.gsh.hris.service.dto.HolidayDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Holiday} and its DTO {@link HolidayDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HolidayMapper extends EntityMapper<HolidayDTO, Holiday> {}
