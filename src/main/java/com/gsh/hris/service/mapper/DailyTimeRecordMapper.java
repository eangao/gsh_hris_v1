package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.*;
import com.gsh.hris.service.dto.DailyTimeRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DailyTimeRecord} and its DTO {@link DailyTimeRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface DailyTimeRecordMapper extends EntityMapper<DailyTimeRecordDTO, DailyTimeRecord> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "id")
    DailyTimeRecordDTO toDto(DailyTimeRecord s);
}
