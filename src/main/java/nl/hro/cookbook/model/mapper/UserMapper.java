package nl.hro.cookbook.model.mapper;

import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends ModelToDTOMapper<User, UserDTO> {}