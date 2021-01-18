package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Invite;
import nl.hro.cookbook.model.dto.InviteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
<<<<<<< HEAD
public interface InviteMapper extends ModelToDTOMapper<Invite, InviteDTO> { }
=======
public interface InviteMapper extends ModelToDTOMapper<Invite, InviteDTO> { }
>>>>>>> 829694e669eaa486cc0b6f1f40c399ff0c0b7377
