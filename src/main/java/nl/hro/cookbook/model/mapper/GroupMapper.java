package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Group;
import nl.hro.cookbook.model.dto.GroupDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper extends ModelToDTOMapper<Group, GroupDto> {}