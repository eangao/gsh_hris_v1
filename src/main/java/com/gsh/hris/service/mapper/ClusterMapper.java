package com.gsh.hris.service.mapper;

import com.gsh.hris.domain.Cluster;
import com.gsh.hris.service.dto.ClusterDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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
