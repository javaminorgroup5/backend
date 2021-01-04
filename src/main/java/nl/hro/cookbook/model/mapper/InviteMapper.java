package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Invite;
import nl.hro.cookbook.model.dto.InviteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InviteMapper extends ModelToDTOMapper<Invite, InviteDTO> { }