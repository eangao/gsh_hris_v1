package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.*;
import com.gsh.hris.service.dto.ClusterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cluster} and its DTO {@link ClusterDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClusterMapper extends EntityMapper<ClusterDTO, Cluster> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClusterDTO toDtoName(Cluster cluster);
}
