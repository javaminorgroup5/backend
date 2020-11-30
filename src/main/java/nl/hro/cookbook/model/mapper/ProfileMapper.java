package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.dto.ProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper extends ModelToDTOMapper<Profile, ProfileDTO> {
}
