package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper extends ModelToDTOMapper<Profile, ProfileDTO> {
}
